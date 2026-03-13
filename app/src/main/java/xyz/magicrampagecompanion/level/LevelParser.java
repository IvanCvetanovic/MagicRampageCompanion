package xyz.magicrampagecompanion.level;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;
import xyz.magicrampagecompanion.ui.levelviewer.SceneProperties;

public class LevelParser {

    private static final String TAG = "LevelParser";

    // Reason codes for why an entity ended up with no sprite
    private static final int REASON_INLINE_SPRITE   = 0; // <Sprite> found inline in .esc
    private static final int REASON_ENT_SPRITE      = 1; // sprite resolved from .ent file
    private static final int REASON_ENT_NO_SPRITE   = 2; // .ent parsed OK but had no <Sprite> tag
    private static final int REASON_ENT_MISSING     = 3; // .ent file not found in assets
    private static final int REASON_ENT_ERROR       = 4; // .ent file found but failed to parse
    private static final int REASON_NO_FILENAME     = 5; // entity had no <FileName> and no inline sprite

    public static Level parse(Context ctx, String assetPath) throws Exception {
        try (InputStream is = ctx.getAssets().open(assetPath)) {

            Level level = new Level();
            SceneProperties props = level.sceneProperties;

            LevelEntity current = null;
            // Track the <FileName> seen for the current entity
            String currentFileName = null;

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");

            int event = parser.getEventType();
            String tag;

            // Helps avoid adding nested <Entity> blocks (if your file has them)
            int entityDepth = 0;

            // Parse-time diagnostics
            int[] reasonCounts = new int[6];
            // Map from reason → sample entity names (up to 5 per bucket)
            Map<Integer, List<String>> reasonSamples = new LinkedHashMap<>();
            for (int i = 0; i < 6; i++) reasonSamples.put(i, new ArrayList<>());

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {

                    case XmlPullParser.START_TAG: {
                        tag = parser.getName();

                        if ("SceneProperties".equals(tag)) {
                            props.lightIntensity = parseFloatAttr(parser, "lightIntensity", props.lightIntensity);
                            props.parallaxIntensity = parseFloatAttr(parser, "parallaxIntensity", props.parallaxIntensity);
                        } else if ("Ambient".equals(tag)) {
                            props.ambientR = parseFloatAttr(parser, "r", props.ambientR);
                            props.ambientG = parseFloatAttr(parser, "g", props.ambientG);
                            props.ambientB = parseFloatAttr(parser, "b", props.ambientB);
                        } else if ("Entity".equals(tag)) {
                            entityDepth++;

                            // Only treat the OUTERMOST Entity as a real scene entity
                            if (entityDepth == 1) {
                                currentFileName = null;
                                String idStr = parser.getAttributeValue(null, "id");
                                if (idStr != null) {
                                    current = new LevelEntity();
                                    current.id = safeParseInt(idStr, -1);

                                    String frame = parser.getAttributeValue(null, "spriteFrame");
                                    if (frame != null) current.spriteFrame = safeParseInt(frame, 0);
                                } else {
                                    // If there's no id, we don't treat it as a real entity
                                    current = null;
                                }
                            } else if (entityDepth == 2 && current != null) {
                                // Inner entity — read rendering properties
                                String bm = parser.getAttributeValue(null, "blendMode");
                                if (bm != null) current.blendMode = safeParseInt(bm, 0);
                            }
                        } else if ("EntityName".equals(tag) && current != null) {
                            current.entityName = safeText(parser);
                        } else if ("Position".equals(tag) && current != null && entityDepth == 1) {
                            // Only read the world-space Position from the outer entity.
                            // Inner entities contain <Collision><Position> offsets that must be ignored.
                            current.x = parseFloatAttr(parser, "x", current.x);
                            current.y = parseFloatAttr(parser, "y", current.y);
                            current.z = parseFloatAttr(parser, "z", current.z);
                            current.angle = parseFloatAttr(parser, "angle", current.angle);
                        } else if ("Sprite".equals(tag) && current != null) {
                            current.spriteFile = safeText(parser);
                        } else if ("Scale".equals(tag) && current != null) {
                            current.scaleX = parseFloatAttr(parser, "x", current.scaleX);
                            current.scaleY = parseFloatAttr(parser, "y", current.scaleY);
                        } else if ("FileName".equals(tag) && current != null) {
                            String fileName = safeText(parser);
                            if (!fileName.isEmpty()) {
                                currentFileName = fileName;
                                parseEntFile(ctx, current, fileName);
                            }
                        }

                        break;
                    }

                    case XmlPullParser.END_TAG: {
                        tag = parser.getName();

                        if ("Entity".equals(tag)) {
                            if (entityDepth == 1 && current != null) {
                                // Classify why this entity has/doesn't have a sprite
                                int reason;
                                if (!current.spriteFile.isEmpty()) {
                                    if (currentFileName != null) {
                                        reason = REASON_ENT_SPRITE;
                                    } else {
                                        reason = REASON_INLINE_SPRITE;
                                    }
                                } else {
                                    if (currentFileName == null) {
                                        reason = REASON_NO_FILENAME;
                                    } else {
                                        // spriteFile is empty despite having a FileName —
                                        // parseEntFile already recorded the specific sub-reason
                                        // in the entity's debugReason field if we had it, but
                                        // for now we can't distinguish ENT_NO_SPRITE vs ENT_MISSING
                                        // without passing it back. Use NO_FILENAME as a fallback;
                                        // parseEntFile logs the details per-file anyway.
                                        reason = REASON_ENT_NO_SPRITE;
                                    }
                                }
                                reasonCounts[reason]++;
                                List<String> samples = reasonSamples.get(reason);
                                if (samples.size() < 8) {
                                    String label = current.entityName.isEmpty()
                                            ? ("id=" + current.id)
                                            : current.entityName;
                                    if (currentFileName != null) label += " [" + currentFileName + "]";
                                    samples.add(label);
                                }

                                level.entities.add(current);
                                current = null;
                            }
                            entityDepth = Math.max(0, entityDepth - 1);
                        }
                        break;
                    }
                }

                event = parser.next();
            }

            // ── Parse summary ──────────────────────────────────────────────
            Log.d(TAG, "=== PARSE SUMMARY for " + assetPath + " ===");
            Log.d(TAG, "  Total entities parsed : " + level.entities.size());
            Log.d(TAG, "  [OK] Inline sprite    : " + reasonCounts[REASON_INLINE_SPRITE]
                    + " — " + reasonSamples.get(REASON_INLINE_SPRITE));
            Log.d(TAG, "  [OK] .ent sprite      : " + reasonCounts[REASON_ENT_SPRITE]
                    + " — " + reasonSamples.get(REASON_ENT_SPRITE));
            Log.d(TAG, "  [!!] .ent no <Sprite> : " + reasonCounts[REASON_ENT_NO_SPRITE]
                    + " — " + reasonSamples.get(REASON_ENT_NO_SPRITE));
            Log.d(TAG, "  [!!] .ent file MISSING: " + reasonCounts[REASON_ENT_MISSING]
                    + " — " + reasonSamples.get(REASON_ENT_MISSING));
            Log.d(TAG, "  [!!] .ent parse ERROR : " + reasonCounts[REASON_ENT_ERROR]
                    + " — " + reasonSamples.get(REASON_ENT_ERROR));
            Log.d(TAG, "  [--] No <FileName>    : " + reasonCounts[REASON_NO_FILENAME]
                    + " — " + reasonSamples.get(REASON_NO_FILENAME));
            Log.d(TAG, "=== END PARSE SUMMARY ===");

            return level;
        }
    }

    /**
     * Opens assets/entities/{fileName}, parses it as an Ethanon entity definition,
     * and fills in sprite, scale, and blendMode on the entity if not already set.
     * The .ent XML mirrors the inline inner <Entity> block in .esc files.
     */
    private static void parseEntFile(Context ctx, LevelEntity entity, String fileName) {
        InputStream is;
        try {
            is = ctx.getAssets().open("entities/" + fileName);
        } catch (Exception e) {
            Log.w(TAG, "ENT MISSING: entities/" + fileName
                    + "  (entity='" + entity.entityName + "' id=" + entity.id + ")");
            return;
        }

        try (InputStream autoClose = is) {
            XmlPullParser p = Xml.newPullParser();
            p.setInput(autoClose, null); // null = auto-detect, correctly strips UTF-8 BOM

            boolean spriteFound = false;
            int event = p.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String tag = p.getName();
                    if ("Entity".equals(tag)) {
                        // Root element carries blendMode
                        String bm = p.getAttributeValue(null, "blendMode");
                        if (bm != null) entity.blendMode = safeParseInt(bm, 0);
                    } else if ("Sprite".equals(tag)) {
                        String sprite = p.nextText();
                        if (sprite != null && !sprite.trim().isEmpty()) {
                            entity.spriteFile = sprite.trim();
                            spriteFound = true;
                            Log.v(TAG, "ENT sprite resolved: " + fileName + " -> " + entity.spriteFile);
                        }
                        // nextText() consumed the end tag; skip the extra next() below
                        event = p.getEventType();
                        continue;
                    } else if ("Scale".equals(tag)) {
                        entity.scaleX = parseFloatAttr(p, "x", entity.scaleX);
                        entity.scaleY = parseFloatAttr(p, "y", entity.scaleY);
                    } else if ("SpriteCut".equals(tag)) {
                        String cx = p.getAttributeValue(null, "x");
                        String cy = p.getAttributeValue(null, "y");
                        if (cx != null) entity.spriteCutX = safeParseInt(cx, 0);
                        if (cy != null) entity.spriteCutY = safeParseInt(cy, 0);
                    }
                }
                event = p.next();
            }

            if (!spriteFound) {
                Log.d(TAG, "ENT NO-SPRITE: entities/" + fileName
                        + "  (entity='" + entity.entityName + "' id=" + entity.id
                        + ")  — file parsed OK but contains no <Sprite> tag");
            }
        } catch (Exception e) {
            Log.e(TAG, "ENT PARSE ERROR: entities/" + fileName
                    + "  (entity='" + entity.entityName + "' id=" + entity.id + "): " + e);
        }
    }

    private static float parseFloatAttr(XmlPullParser parser, String attr, float fallback) {
        String v = parser.getAttributeValue(null, attr);
        if (v == null) return fallback;
        try {
            return Float.parseFloat(v);
        } catch (Exception e) {
            return fallback;
        }
    }

    private static int safeParseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    private static String safeText(XmlPullParser parser) throws Exception {
        String t = parser.nextText();
        return (t == null) ? "" : t.trim();
    }
}
