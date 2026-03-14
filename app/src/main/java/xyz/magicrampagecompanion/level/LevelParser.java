package xyz.magicrampagecompanion.level;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;
import xyz.magicrampagecompanion.ui.levelviewer.SceneProperties;

public class LevelParser {

    private static final String TAG = "LevelParser";

    // Global mapping for heads/hairs from .enml files
    private static final Map<String, String> headMap = new HashMap<>();

    // Reason codes for why an entity ended up with no sprite
    private static final int REASON_INLINE_SPRITE   = 0; // <Sprite> found inline in .esc
    private static final int REASON_ENT_SPRITE      = 1; // sprite resolved from .ent file
    private static final int REASON_ENT_NO_SPRITE   = 2; // .ent parsed OK but had no <Sprite> tag
    private static final int REASON_ENT_MISSING     = 3; // .ent file not found in assets
    private static final int REASON_ENT_ERROR       = 4; // .ent file found but failed to parse
    private static final int REASON_NO_FILENAME     = 5; // entity had no <FileName> and no inline sprite

    public static Level parse(Context ctx, String assetPath) throws Exception {
        // Ensure head mappings are loaded
        if (headMap.isEmpty()) {
            loadEnmlMapping(ctx, "entities/npc-head.enml");
        }

        try (InputStream is = ctx.getAssets().open(assetPath)) {

            // Load localized strings for this level (best-effort; empty map if missing)
            Map<String, String> strings = loadStrings(ctx, stringsPathForLevel(assetPath));

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
            boolean insideLight = false;
            boolean insideParticles = false;
            // CustomData variable parsing
            String currentVarName = null;

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
                        } else if ("Particles".equals(tag)) {
                            insideParticles = true;
                        } else if ("SpriteCut".equals(tag) && current != null && !insideParticles) {
                            String cx = parser.getAttributeValue(null, "x");
                            String cy = parser.getAttributeValue(null, "y");
                            if (cx != null) current.spriteCutX = safeParseInt(cx, 0);
                            if (cy != null) current.spriteCutY = safeParseInt(cy, 0);
                        } else if ("Light".equals(tag) && current != null) {
                            current.lightHaloSize = parseFloatAttr(parser, "haloSize", current.lightHaloSize);
                            insideLight = true;
                        } else if ("Color".equals(tag) && current != null && insideLight) {
                            current.lightColorR = parseFloatAttr(parser, "r", 1f);
                            current.lightColorG = parseFloatAttr(parser, "g", 1f);
                            current.lightColorB = parseFloatAttr(parser, "b", 1f);
                        } else if ("FileName".equals(tag) && current != null) {
                            String fileName = safeText(parser);
                            if (!fileName.isEmpty()) {
                                currentFileName = fileName;
                                parseEntFile(ctx, current, fileName);
                            }
                        } else if ("Name".equals(tag) && current != null) {
                            currentVarName = safeText(parser);
                        } else if ("Value".equals(tag) && current != null && currentVarName != null) {
                            String value = safeText(parser);
                            current.customData.put(currentVarName, value);

                            if ("text".equals(currentVarName)) {
                                // Resolve key to actual string; fall back to key if not found
                                current.displayText = strings.containsKey(value) ? strings.get(value) : value;
                            }
                            currentVarName = null;
                        }

                        break;
                    }

                    case XmlPullParser.END_TAG: {
                        tag = parser.getName();

                        if ("Light".equals(tag)) {
                            insideLight = false;
                        } else if ("Particles".equals(tag)) {
                            insideParticles = false;
                        } else if ("Entity".equals(tag)) {
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

    public static void parseEntFile(Context ctx, LevelEntity entity, String fileName) {
        if (fileName == null || fileName.isEmpty()) return;

        // Try standard location (entities/basename.ent) first
        String baseName = fileName.contains("/") ? fileName.substring(fileName.lastIndexOf("/") + 1) : fileName;
        if (!baseName.endsWith(".ent")) baseName += ".ent";

        try (InputStream is = ctx.getAssets().open("entities/" + baseName)) {
            parseEntInternal(ctx, entity, is);
            return;
        } catch (Exception ignored) {}

        // Fallback: Try original path as provided
        String fullPath = fileName.endsWith(".ent") ? fileName : fileName + ".ent";
        try (InputStream is = ctx.getAssets().open(fullPath)) {
            parseEntInternal(ctx, entity, is);
            return;
        } catch (Exception ignored) {}

        // Fallback: Try entities/ + fullPath
        try (InputStream is = ctx.getAssets().open("entities/" + fullPath)) {
            parseEntInternal(ctx, entity, is);
            return;
        } catch (Exception ignored) {}

        Log.w(TAG, "ENT MISSING: " + fileName + " (entity='" + entity.entityName + "' id=" + entity.id + ")");
    }

    private static void parseEntInternal(Context ctx, LevelEntity entity, InputStream is) throws Exception {
        XmlPullParser p = Xml.newPullParser();
        p.setInput(is, null);

        boolean insideParticles = false;
        int event = p.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG) {
                String tag = p.getName();
                if ("Particles".equals(tag)) {
                    insideParticles = true;
                } else if ("Entity".equals(tag)) {
                    String bm = p.getAttributeValue(null, "blendMode");
                    if (bm != null) entity.blendMode = safeParseInt(bm, 0);
                } else if ("Sprite".equals(tag) && !insideParticles) {
                    String sprite = p.nextText();
                    if (sprite != null && !sprite.trim().isEmpty()) {
                        entity.spriteFile = sprite.trim();
                    }
                    event = p.getEventType();
                    continue;
                } else if ("Scale".equals(tag) && !insideParticles) {
                    entity.scaleX = parseFloatAttr(p, "x", entity.scaleX);
                    entity.scaleY = parseFloatAttr(p, "y", entity.scaleY);
                } else if ("SpriteCut".equals(tag) && !insideParticles) {
                    String cx = p.getAttributeValue(null, "x");
                    String cy = p.getAttributeValue(null, "y");
                    if (cx != null) entity.spriteCutX = safeParseInt(cx, 0);
                    if (cy != null) entity.spriteCutY = safeParseInt(cy, 0);
                } else if ("Light".equals(tag)) {
                    entity.lightHaloSize = parseFloatAttr(p, "haloSize", entity.lightHaloSize);
                } else if ("Color".equals(tag)) {
                    entity.lightColorR = parseFloatAttr(p, "r", 1f);
                    entity.lightColorG = parseFloatAttr(p, "g", 1f);
                    entity.lightColorB = parseFloatAttr(p, "b", 1f);
                }
            } else if (event == XmlPullParser.END_TAG) {
                String tag = p.getName();
                if ("Particles".equals(tag)) insideParticles = false;
            }
            event = p.next();
        }
    }

    public static void parseCharacterFile(Context ctx, LevelEntity entity, String fileName) {
        if (fileName == null || fileName.isEmpty()) return;

        String baseName = fileName.contains("/") ? fileName.substring(fileName.lastIndexOf("/") + 1) : fileName;
        if (!baseName.endsWith(".character")) baseName += ".character";

        try (InputStream is = ctx.getAssets().open("entities/" + baseName)) {
            parseCharacterInternal(entity, is);
            Log.d(TAG, "Parsed .character file: " + baseName + " -> Body: " + entity.spriteFile + ", Armor: " + entity.armorSprite + ", Hair: " + entity.hairSprite + ", Weapon: " + entity.weaponSprite + " (off=" + entity.weaponOffsetX + "," + entity.weaponOffsetY + " angle=" + entity.weaponAngle + ")");
            return;
        } catch (Exception ignored) {}

        String fullPath = fileName.endsWith(".character") ? fileName : fileName + ".character";
        try (InputStream is = ctx.getAssets().open(fullPath)) {
            parseCharacterInternal(entity, is);
            return;
        } catch (Exception ignored) {}

        try (InputStream is = ctx.getAssets().open("entities/" + fullPath)) {
            parseCharacterInternal(entity, is);
            return;
        } catch (Exception ignored) {}

        Log.w(TAG, "CHARACTER FILE MISSING: " + fileName);
    }

    private static void parseCharacterInternal(LevelEntity entity, InputStream is) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            String currentBlock = "";
            Map<String, String> blockVars = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) continue;

                if (line.endsWith("{") || line.equals("{")) {
                    if (line.endsWith("{") && line.length() > 1) {
                        currentBlock = line.substring(0, line.length() - 1).trim().toLowerCase();
                    }
                    blockVars.clear();
                    continue;
                }
                
                if (line.equals("}")) {
                    processCharacterBlock(entity, currentBlock, blockVars);
                    currentBlock = "";
                    blockVars.clear();
                    continue;
                }

                if (line.contains("=")) {
                    String key = line.substring(0, line.indexOf("=")).trim();
                    String value = line.substring(line.indexOf("=") + 1).replace(";", "").trim();
                    if (!value.isEmpty()) {
                        blockVars.put(key.toLowerCase(), value);
                    }
                } else if (!line.contains("{") && !line.contains("}")) {
                    currentBlock = line.toLowerCase();
                }
            }
        }
    }

    private static void processCharacterBlock(LevelEntity entity, String blockName, Map<String, String> vars) {
        if ("character".equals(blockName) || blockName.isEmpty()) {
            if (vars.containsKey("bodysprite")) {
                entity.spriteFile = vars.get("bodysprite");
                entity.spriteCutX = 4;
                entity.spriteCutY = 2;
            }
            if (vars.containsKey("bodycolor")) {
                try { entity.bodyColor = Long.parseLong(vars.get("bodycolor")); } catch (Exception ignored) {}
            }
            if (vars.containsKey("hairsprite")) {
                entity.hairSprite = vars.get("hairsprite");
            }
        } else if (blockName.startsWith("equippeditem")) {
            String type = vars.get("type");
            String sprite = vars.get("sprite");
            if (sprite != null && !"none".equalsIgnoreCase(sprite)) {
                if ("weapon".equalsIgnoreCase(type)) {
                    entity.weaponSprite = sprite;
                    if (vars.containsKey("equipoffsetx")) entity.weaponOffsetX = parseFloat(vars.get("equipoffsetx"), 0f);
                    if (vars.containsKey("equipoffsety")) entity.weaponOffsetY = parseFloat(vars.get("equipoffsety"), 0f);
                    if (vars.containsKey("equippedangle")) entity.weaponAngle = parseFloat(vars.get("equippedangle"), 0f);
                } else if ("armor".equalsIgnoreCase(type)) {
                    entity.armorSprite = sprite;
                }
            }
        }
    }

    /**
     * Loads a .enml file and stores name=sprite mappings in headMap.
     */
    private static void loadEnmlMapping(Context ctx, String assetPath) {
        try (InputStream is = ctx.getAssets().open(assetPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            String currentKey = null;
            String lastPotentialKey = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) continue;

                if (line.endsWith("{") || line.equals("{")) {
                    if (line.endsWith("{") && line.length() > 1) {
                        currentKey = line.substring(0, line.length() - 1).trim();
                    } else {
                        currentKey = lastPotentialKey;
                    }
                    continue;
                }
                if (line.equals("}")) {
                    currentKey = null;
                    lastPotentialKey = null;
                    continue;
                }

                if (currentKey != null && line.contains("=")) {
                    String key = line.substring(0, line.indexOf("=")).trim();
                    String value = line.substring(line.indexOf("=") + 1).replace(";", "").trim();
                    if ("sprite".equalsIgnoreCase(key)) {
                        headMap.put(currentKey, value);
                    }
                } else {
                    lastPotentialKey = line;
                }
            }
            Log.d(TAG, "Loaded " + headMap.size() + " mappings from " + assetPath);
        } catch (Exception e) {
            Log.w(TAG, "Failed to load mapping: " + assetPath);
        }
    }

    public static String resolveHeadSprite(String headId) {
        return headMap.getOrDefault(headId, headId);
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

    private static float parseFloat(String s, float fallback) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    private static String safeText(XmlPullParser parser) throws Exception {
        String t = parser.nextText();
        return (t == null) ? "" : t.trim();
    }

    static Map<String, String> loadStrings(Context ctx, String assetPath) {
        Map<String, String> map = new HashMap<>();
        try (InputStream is = ctx.getAssets().open(assetPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("\"")) continue;
                int eq = line.indexOf("\" = \"");
                if (eq < 0) continue;
                String key = line.substring(1, eq);
                int valueStart = eq + 5;
                int valueEnd = line.lastIndexOf("\";");
                if (valueEnd <= valueStart) continue;
                String value = line.substring(valueStart, valueEnd);
                map.put(key, value);
            }
        } catch (Exception e) {
            Log.d(TAG, "No strings file at " + assetPath + " — text keys will show as-is");
        }
        return map;
    }

    static String stringsPathForLevel(String levelAssetPath) {
        String name = levelAssetPath;
        int slash = name.lastIndexOf('/');
        if (slash >= 0) name = name.substring(slash + 1);
        int dot = name.lastIndexOf('.');
        if (dot >= 0) name = name.substring(0, dot);
        String biome = name.replaceAll("\\d+$", "");
        return "entities/" + biome + "-texts.strings";
    }
}
