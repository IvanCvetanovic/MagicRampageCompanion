package xyz.magicrampagecompanion.level;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;
import xyz.magicrampagecompanion.ui.levelviewer.SceneProperties;

public class LevelParser {

    public static Level parse(Context ctx, String assetPath) throws Exception {
        try (InputStream is = ctx.getAssets().open(assetPath)) {

            Level level = new Level();
            SceneProperties props = level.sceneProperties;

            LevelEntity current = null;

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");

            int event = parser.getEventType();
            String tag;

            // Helps avoid adding nested <Entity> blocks (if your file has them)
            int entityDepth = 0;

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
                            }
                        } else if ("EntityName".equals(tag) && current != null) {
                            current.entityName = safeText(parser);
                        } else if ("Position".equals(tag) && current != null) {
                            current.x = parseFloatAttr(parser, "x", current.x);
                            current.y = parseFloatAttr(parser, "y", current.y);
                            current.z = parseFloatAttr(parser, "z", current.z);
                            current.angle = parseFloatAttr(parser, "angle", current.angle);
                        } else if ("Sprite".equals(tag) && current != null) {
                            current.spriteFile = safeText(parser);
                        } else if ("Scale".equals(tag) && current != null) {
                            current.scaleX = parseFloatAttr(parser, "x", current.scaleX);
                            current.scaleY = parseFloatAttr(parser, "y", current.scaleY);
                        }

                        break;
                    }

                    case XmlPullParser.END_TAG: {
                        tag = parser.getName();

                        if ("Entity".equals(tag)) {
                            if (entityDepth == 1 && current != null) {
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

            return level;
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
