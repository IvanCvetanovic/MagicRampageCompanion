package xyz.magicrampagecompanion.character;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Byte-exact, structure-preserving reader/writer for Magic Rampage {@code .character} files.
 *
 * <p>The format is a flat brace config: one {@code character { ... }} block plus a few
 * {@code equippedItem0..2 { ... }} / {@code item0} / {@code id} blocks, terminated by a literal
 * {@code EOF____} line. Lines are {@code key = value;} with CRLF endings, inline {@code //}
 * comments, occasional duplicate keys, and escaped {@code \;} inside values.
 *
 * <p>This class is deliberately a <b>text patcher</b>, not a model re-serializer: it keeps every
 * physical line verbatim and only rewrites the value span of lines the caller edits. An unedited
 * document therefore round-trips <b>byte-for-byte</b> (verified by {@code CharacterDocumentTest}),
 * which the existing lossy {@code LevelParser.parseCharacterInternal} could never do. Latin-1 is
 * used for a lossless 1:1 byte&lt;-&gt;char mapping so {@link #toBytes()} reproduces CRLF endings
 * and the trailing {@code EOF____} (no final newline) exactly.
 *
 * <p>Pure Java (no Android deps) so it can be unit-tested on the host.
 */
public final class CharacterDocument {

    private static final Charset CS = Charset.forName("ISO-8859-1");

    /** One physical line: its content (sans terminator) + the exact terminator that followed it. */
    private static final class Line {
        String content;
        final String terminator; // "\r\n", "\n", or "" for a final line without a newline
        boolean edited;
        int valueStart = -1, valueEnd = -1; // span of the value within content, for key=value lines
        Line(String content, String terminator) { this.content = content; this.terminator = terminator; }
    }

    /** A key=value entry inside a block, pointing back at its physical line. */
    public static final class Field {
        public final int lineIndex;
        public final String key;
        Field(int lineIndex, String key) { this.lineIndex = lineIndex; this.key = key; }
    }

    /** A named block ({@code character}, {@code equippedItem0}, ...) and its ordered fields. */
    public static final class Block {
        public final String name;
        public final List<Field> fields = new ArrayList<>();
        Block(String name) { this.name = name; }
    }

    private final List<Line> lines = new ArrayList<>();
    private final List<Block> blocks = new ArrayList<>();

    private CharacterDocument() {}

    // ── parsing ────────────────────────────────────────────────────────────────────────────────

    public static CharacterDocument parse(byte[] raw) {
        CharacterDocument d = new CharacterDocument();
        d.split(new String(raw, CS));
        d.index();
        return d;
    }

    public static CharacterDocument parse(InputStream is) throws IOException {
        return parse(readAll(is));
    }

    /** Split into physical lines, preserving each line's exact terminator. */
    private void split(String text) {
        int n = text.length(), start = 0, i = 0;
        while (i < n) {
            if (text.charAt(i) == '\n') {
                int contentEnd = i;
                String term = "\n";
                if (contentEnd > start && text.charAt(contentEnd - 1) == '\r') { contentEnd--; term = "\r\n"; }
                lines.add(new Line(text.substring(start, contentEnd), term));
                i++;
                start = i;
            } else {
                i++;
            }
        }
        if (start < n) lines.add(new Line(text.substring(start), ""));
    }

    /** Best-effort structural index (blocks + fields). Does not affect byte round-trip. */
    private void index() {
        Block current = null;
        String pendingName = null;
        for (int idx = 0; idx < lines.size(); idx++) {
            Line ln = lines.get(idx);
            String t = ln.content.trim();
            if (t.isEmpty() || t.startsWith("//")) continue;

            if (t.equals("{")) {
                if (pendingName != null) { current = new Block(pendingName); blocks.add(current); pendingName = null; }
                continue;
            }
            if (t.endsWith("{")) { // defensive: inline "name {" (not used by stock files)
                current = new Block(t.substring(0, t.length() - 1).trim());
                blocks.add(current);
                pendingName = null;
                continue;
            }
            if (t.equals("}")) { current = null; continue; }

            int eq = ln.content.indexOf('=');
            if (eq >= 0 && current != null) {
                computeValueSpan(ln, eq);
                current.fields.add(new Field(idx, ln.content.substring(0, eq).trim()));
                continue;
            }
            // A bare token line (e.g. "character", "equippedItem0", "EOF____") => pending block name.
            pendingName = t;
        }
    }

    /** Value = text between '=' (after spaces) and the first UNescaped ';' (or end of line). */
    private void computeValueSpan(Line ln, int eq) {
        String s = ln.content;
        int p = eq + 1;
        while (p < s.length() && (s.charAt(p) == ' ' || s.charAt(p) == '\t')) p++;
        int ve = s.length();
        for (int k = p; k < s.length(); k++) {
            if (s.charAt(k) == ';' && (k == 0 || s.charAt(k - 1) != '\\')) { ve = k; break; }
        }
        ln.valueStart = p;
        ln.valueEnd = ve;
    }

    // ── accessors ──────────────────────────────────────────────────────────────────────────────

    public List<Block> blocks() { return blocks; }

    /** Trimmed display value of a key=value line. */
    public String value(int lineIndex) {
        Line ln = lines.get(lineIndex);
        if (ln.valueStart < 0) return "";
        return ln.content.substring(ln.valueStart, ln.valueEnd).trim();
    }

    /** First value for {@code key} in the first block named {@code blockName}, or null. */
    public String firstValue(String blockName, String key) {
        for (Block b : blocks) {
            if (!b.name.equalsIgnoreCase(blockName)) continue;
            for (Field f : b.fields) if (f.key.equalsIgnoreCase(key)) return value(f.lineIndex);
        }
        return null;
    }

    /** Replace only the value span of one line; everything else stays byte-identical. */
    public void setValue(int lineIndex, String newValue) {
        Line ln = lines.get(lineIndex);
        if (ln.valueStart < 0) return;
        String s = ln.content;
        ln.content = s.substring(0, ln.valueStart) + newValue + s.substring(ln.valueEnd);
        ln.valueEnd = ln.valueStart + newValue.length();
        ln.edited = true;
    }

    /** True if any line was edited since parsing (drives "unsaved changes" UI). */
    public boolean isDirty() {
        for (Line ln : lines) if (ln.edited) return true;
        return false;
    }

    // ── serialization ──────────────────────────────────────────────────────────────────────────

    public byte[] toBytes() {
        StringBuilder sb = new StringBuilder();
        for (Line ln : lines) sb.append(ln.content).append(ln.terminator);
        return sb.toString().getBytes(CS);
    }

    private static byte[] readAll(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int r;
        while ((r = is.read(buf)) != -1) bos.write(buf, 0, r);
        return bos.toByteArray();
    }
}
