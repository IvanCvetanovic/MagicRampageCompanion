package xyz.magicrampagecompanion.character;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;

import org.junit.Test;

/**
 * P0 gate for the Character Editor: {@link CharacterDocument} must round-trip every bundled
 * {@code .character} file <b>byte-for-byte</b>, and {@code setValue} must change only the
 * targeted value.
 */
public class CharacterDocumentTest {

    private File entitiesDir() {
        for (String p : new String[]{"src/main/assets/entities", "app/src/main/assets/entities"}) {
            File f = new File(p);
            if (f.isDirectory()) return f;
        }
        throw new IllegalStateException("entities dir not found; cwd=" + new File(".").getAbsolutePath());
    }

    @Test
    public void roundTripIsByteIdenticalForEveryBundledCharacter() throws Exception {
        File[] files = entitiesDir().listFiles((d, n) -> n.endsWith(".character"));
        assertNotNull("no .character files found", files);
        assertTrue("expected many .character files, got " + files.length, files.length > 100);

        int checked = 0;
        for (File f : files) {
            byte[] raw = Files.readAllBytes(f.toPath());
            byte[] out = CharacterDocument.parse(raw).toBytes();
            assertArrayEquals("byte round-trip differs for " + f.getName(), raw, out);
            checked++;
        }
        System.out.println("CharacterDocument round-trip OK, byte-identical for " + checked + " files");
    }

    @Test
    public void setValueChangesOnlyTheTargetedValue() throws Exception {
        File f = new File(entitiesDir(), "skeleton.character");
        assertTrue("skeleton.character missing", f.exists());
        byte[] raw = Files.readAllBytes(f.toPath());

        CharacterDocument d = CharacterDocument.parse(raw);
        assertFalse(d.isDirty());
        assertEquals("Skeleton", d.firstValue("character", "name"));
        assertEquals("6", d.firstValue("character", "speed"));

        // edit speed 6 -> 9 via its line index
        int speedLine = -1;
        for (CharacterDocument.Block b : d.blocks()) {
            if (!b.name.equalsIgnoreCase("character")) continue;
            for (CharacterDocument.Field fl : b.fields) {
                if (fl.key.equalsIgnoreCase("speed")) speedLine = fl.lineIndex;
            }
        }
        assertTrue("speed field not located", speedLine >= 0);
        d.setValue(speedLine, "9");
        assertTrue(d.isDirty());

        byte[] out = d.toBytes();
        CharacterDocument re = CharacterDocument.parse(out);
        assertEquals("9", re.firstValue("character", "speed"));
        assertEquals("Skeleton", re.firstValue("character", "name")); // untouched

        // Exactly one byte fewer or equal length change, and the only textual diff is 6 -> 9.
        String before = new String(raw, java.nio.charset.StandardCharsets.ISO_8859_1);
        String after = new String(out, java.nio.charset.StandardCharsets.ISO_8859_1);
        assertEquals(before.replaceFirst("speed = 6;", "speed = 9;"), after);
    }

    @Test
    public void addAndRemoveFieldRoundTrip() throws Exception {
        byte[] raw = Files.readAllBytes(new File(entitiesDir(), "skeleton.character").toPath());
        CharacterDocument d = CharacterDocument.parse(raw);

        CharacterDocument.Block character = null;
        for (CharacterDocument.Block b : d.blocks()) if (b.name.equalsIgnoreCase("character")) character = b;
        assertNotNull(character);

        d.addField(character, "qaKey", "42");
        CharacterDocument re = CharacterDocument.parse(d.toBytes());
        assertEquals("42", re.firstValue("character", "qaKey"));
        assertTrue(re.hasEofMarker());
        assertEquals("Skeleton", re.firstValue("character", "name")); // untouched

        // remove the field we just added
        int qaLine = -1;
        for (CharacterDocument.Block b : re.blocks())
            if (b.name.equalsIgnoreCase("character"))
                for (CharacterDocument.Field f : b.fields)
                    if (f.key.equalsIgnoreCase("qaKey")) qaLine = f.lineIndex;
        assertTrue(qaLine >= 0);
        re.removeLine(qaLine);
        CharacterDocument re2 = CharacterDocument.parse(re.toBytes());
        assertEquals(null, re2.firstValue("character", "qaKey"));
        assertEquals("Skeleton", re2.firstValue("character", "name"));
    }
}
