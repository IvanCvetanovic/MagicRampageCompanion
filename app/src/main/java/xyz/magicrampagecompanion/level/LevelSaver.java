package xyz.magicrampagecompanion.level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;

/**
 * Saves an edited {@link Level} by patching the ORIGINAL .esc DOM in place
 * (the lossy pull-parser drops many attributes; the DOM keeps everything).
 *
 * Matching key is document ORDER, not id — entity ids are not unique across all
 * stock levels (dungeon16/35/42/43.1). {@link LevelEntity#editOrdinal} is the
 * 0-based position assigned at parse time; new entities carry -1.
 */
public class LevelSaver {

    private LevelSaver() {}

    /**
     * @param sourceEsc the ORIGINAL .esc the level was loaded from (asset or storage stream)
     * @param level     the edited level (entities carry editOrdinal/sourceOrdinal)
     * @param dest       file to write the reconciled .esc to
     */
    public static void save(InputStream sourceEsc, Level level, File dest) throws Exception {
        File parent = dest.getParentFile();
        if (parent != null) parent.mkdirs();
        try (OutputStream os = new FileOutputStream(dest)) {
            save(sourceEsc, level, os);
        }
    }

    /** Same reconcile as {@link #save(InputStream, Level, File)} but writes to an arbitrary stream
     *  (e.g. a Storage Access Framework document the user picked for export). */
    public static void save(InputStream sourceEsc, Level level, OutputStream out) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(sourceEsc);

        Element scene = firstByTag(doc, "EntitiesInScene");
        if (scene == null) throw new IllegalStateException("No <EntitiesInScene> in source .esc");

        // Direct-child <Entity> nodes in document order (all stock outer entities have ids).
        List<Element> nodes = new ArrayList<>();
        NodeList ch = scene.getChildNodes();
        for (int i = 0; i < ch.getLength(); i++) {
            Node n = ch.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && "Entity".equals(n.getNodeName())) {
                nodes.add((Element) n);
            }
        }

        // Partition current entities: surviving originals (by ordinal) vs newly added.
        Map<Integer, LevelEntity> survivors = new HashMap<>();
        List<LevelEntity> added = new ArrayList<>();
        int maxOrd = -1;
        for (LevelEntity e : level.entities) {
            if (e.editOrdinal >= 0) {
                survivors.put(e.editOrdinal, e);
                if (e.editOrdinal > maxOrd) maxOrd = e.editOrdinal;
            } else {
                added.add(e);
            }
        }
        if (maxOrd >= nodes.size()) {
            throw new IllegalStateException("Entity ordinal/DOM-node misalignment (maxOrd="
                    + maxOrd + ", nodes=" + nodes.size() + ") — refusing to save to avoid corruption.");
        }

        // Patch survivors in place; remove deleted nodes.
        for (int i = 0; i < nodes.size(); i++) {
            Element node = nodes.get(i);
            LevelEntity e = survivors.get(i);
            if (e == null) {
                scene.removeChild(node);
            } else {
                patchNode(node, e);
            }
        }

        // Append newly added entities (clone the source node for duplicates; synthesize for palette adds).
        for (LevelEntity e : added) {
            scene.appendChild(buildNewNode(doc, e, nodes));
        }

        // Strip inter-element whitespace so re-indentation is clean and uniform.
        stripWhitespace(doc.getDocumentElement());

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        t.transform(new DOMSource(doc), new StreamResult(out));
    }

    /** Updates Position (x/y/z/angle), flip flags, and an existing inner Scale. Never inserts Scale
     *  or touches spriteFrame — keeps a no-op save semantically identical to the source. */
    private static void patchNode(Element node, LevelEntity e) {
        Element pos = firstChildElement(node, "Position");
        if (pos != null) {
            pos.setAttribute("x", fmt(e.x));
            pos.setAttribute("y", fmt(e.y));
            pos.setAttribute("z", fmt(e.z));
            pos.setAttribute("angle", fmt(e.angle));
        }
        setFlag(node, "flipX", e.flipX);
        setFlag(node, "flipY", e.flipY);

        Element inner = firstChildElement(node, "Entity");
        if (inner != null) {
            // Only persist scale the user actually changed — the renderer mutates scaleX/scaleY for
            // resolved NPC spawns, so writing it unconditionally would corrupt those (e.g. 4 -> 1).
            Element scale = firstChildElement(inner, "Scale");
            if (scale != null && e.scaleEdited) {
                scale.setAttribute("x", fmt(e.scaleX));
                scale.setAttribute("y", fmt(e.scaleY));
            }
            // No <Scale> present (e.g. FileName-referenced entity): scale lives in the .ent.
            // v1 leaves it alone rather than inserting a node of uncertain engine support.

            // Update CustomData <Value>s the user edited (matched by <Name>); gated to keep no-op saves clean.
            if (e.customDataEdited) {
                Element cd = firstChildElement(inner, "CustomData");
                if (cd != null) {
                    NodeList vars = cd.getChildNodes();
                    for (int i = 0; i < vars.getLength(); i++) {
                        Node vn = vars.item(i);
                        if (vn.getNodeType() != Node.ELEMENT_NODE || !"Variable".equals(vn.getNodeName())) continue;
                        Element var = (Element) vn;
                        Element nameEl = firstChildElement(var, "Name");
                        Element valEl = firstChildElement(var, "Value");
                        if (nameEl != null && valEl != null) {
                            String key = nameEl.getTextContent().trim();
                            if (e.customData.containsKey(key)) valEl.setTextContent(e.customData.get(key));
                        }
                    }
                }
            }
        }
    }

    private static Element buildNewNode(Document doc, LevelEntity e, List<Element> sourceNodes) throws Exception {
        // Inline "spawner" template placed from the palette → write its harvested block verbatim,
        // swapping only the id and the patched Position/flip. Save fidelity never depends on the
        // lossy parser. This branch is gated on sourceBlockXml (null for every other flow), so it
        // is inert for no-op saves, normal edits, FileName palette adds, and duplicates.
        if (e.sourceBlockXml != null && !e.sourceBlockXml.isEmpty()) {
            Element block = parseBlock(doc, e.sourceBlockXml);
            block.setAttribute("id", String.valueOf(e.id));
            patchNode(block, e);
            return block;
        }
        // Duplicate of an existing original → clone its real, complete DOM node (faithful copy).
        if (e.sourceOrdinal >= 0 && e.sourceOrdinal < sourceNodes.size()) {
            Element clone = (Element) sourceNodes.get(e.sourceOrdinal).cloneNode(true);
            clone.setAttribute("id", String.valueOf(e.id));
            patchNode(clone, e);
            return clone;
        }
        // Palette add (or duplicate rooted at one) → minimal FileName-referenced block.
        Element outer = doc.createElement("Entity");
        outer.setAttribute("id", String.valueOf(e.id));
        outer.setAttribute("spriteFrame", String.valueOf(e.spriteFrame));
        if (e.flipX) outer.setAttribute("flipX", "1");
        if (e.flipY) outer.setAttribute("flipY", "1");

        Element name = doc.createElement("EntityName");
        name.setTextContent(e.entityName);
        outer.appendChild(name);

        Element pos = doc.createElement("Position");
        pos.setAttribute("x", fmt(e.x));
        pos.setAttribute("y", fmt(e.y));
        pos.setAttribute("z", fmt(e.z));
        pos.setAttribute("angle", fmt(e.angle));
        outer.appendChild(pos);

        Element inner = doc.createElement("Entity");
        Element fn = doc.createElement("FileName");
        fn.setTextContent(e.entityName);
        inner.appendChild(fn);
        inner.appendChild(doc.createElement("CustomData"));
        outer.appendChild(inner);
        return outer;
    }

    /** Parses a standalone <Entity> block string and imports it into {@code doc} (does not attach it). */
    private static Element parseBlock(Document doc, String xml) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document frag = db.parse(new InputSource(new StringReader(xml)));
        return (Element) doc.importNode(frag.getDocumentElement(), true);
    }

    private static void setFlag(Element node, String attr, boolean on) {
        if (on) node.setAttribute(attr, "1");
        else if (node.hasAttribute(attr)) node.removeAttribute(attr);
    }

    private static Element firstByTag(Document doc, String tag) {
        NodeList nl = doc.getElementsByTagName(tag);
        return nl.getLength() > 0 ? (Element) nl.item(0) : null;
    }

    private static Element firstChildElement(Element parent, String tag) {
        NodeList ch = parent.getChildNodes();
        for (int i = 0; i < ch.getLength(); i++) {
            Node n = ch.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && tag.equals(n.getNodeName())) {
                return (Element) n;
            }
        }
        return null;
    }

    private static void stripWhitespace(Node node) {
        NodeList ch = node.getChildNodes();
        for (int i = ch.getLength() - 1; i >= 0; i--) {
            Node n = ch.item(i);
            if (n.getNodeType() == Node.TEXT_NODE) {
                if (n.getTextContent().trim().isEmpty()) node.removeChild(n);
            } else if (n.getNodeType() == Node.ELEMENT_NODE) {
                stripWhitespace(n);
            }
        }
    }

    /** Whole numbers as integers ("960", not "960.0"); keeps patched Positions clean. */
    private static String fmt(float v) {
        if (!Float.isInfinite(v) && v == Math.rint(v)) return String.valueOf((long) v);
        return String.valueOf(v);
    }
}
