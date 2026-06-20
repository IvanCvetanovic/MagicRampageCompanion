package xyz.magicrampagecompanion.character;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

import xyz.magicrampagecompanion.level.LevelParser;
import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;

/**
 * Builds a static preview bitmap of a character by compositing frame 0 of its body (tinted by
 * {@code bodyColor}), armor and hair layers — the same layers/order the level renderer uses
 * ({@code drawCompositedCharacter}), minus the weapon (its world-space offset/angle isn't
 * meaningful in a static thumbnail). Self-contained (mirrors the renderer's atlas + uniform-grid
 * frame logic) so it doesn't depend on the tested {@code LevelRenderView}. Decode off the UI thread.
 */
public final class CharacterSpriteCompositor {

    private CharacterSpriteCompositor() {}

    /** @param e a LevelEntity already populated via {@link LevelParser#parseCharacterStream}. */
    public static Bitmap compose(Context ctx, LevelEntity e) {
        AssetManager am = ctx.getAssets();

        Bitmap body = loadFrame0(am, e.spriteFile, e.spriteCutX, e.spriteCutY);
        if (body == null) return null;

        Bitmap result = Bitmap.createBitmap(body.getWidth(), body.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(result);

        Paint bodyPaint = null;
        if (e.bodyColor != -1) {
            int color = (int) e.bodyColor;
            if ((color & 0xFF000000) == 0) color |= 0xFF000000; // engine stores RGB w/o alpha; force opaque
            bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bodyPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        }
        c.drawBitmap(body, 0, 0, bodyPaint);

        if (notEmpty(e.armorSprite)) {
            String armor = e.armorSprite;
            if (!armor.startsWith("armor_") && !armor.endsWith(".png")) armor = "armor_" + armor;
            Bitmap a = loadFrame0(am, armor, 4, 2);
            if (a == null && !armor.equals(e.armorSprite)) a = loadFrame0(am, e.armorSprite, 4, 2);
            if (a != null) c.drawBitmap(a, 0, 0, null);
        }

        if (notEmpty(e.hairSprite)) {
            Bitmap h = loadFrame0(am, LevelParser.resolveHeadSprite(e.hairSprite), 4, 2);
            if (h != null) c.drawBitmap(h, 0, 0, null);
        }
        return result;
    }

    private static boolean notEmpty(String s) {
        return s != null && !s.isEmpty() && !"none".equalsIgnoreCase(s);
    }

    /** Frame 0 of a sprite sheet — atlas (.xml sidecar) if present, else a uniform-grid crop. */
    private static Bitmap loadFrame0(AssetManager am, String sprite, int cutX, int cutY) {
        if (!notEmpty(sprite)) return null;
        String key = sprite.endsWith(".png") ? sprite : sprite + ".png";

        Bitmap sheet;
        try (InputStream is = am.open("entities/" + key)) {
            sheet = BitmapFactory.decodeStream(is);
        } catch (Exception ex) {
            return null;
        }
        if (sheet == null) return null;

        AtlasSprite a0 = loadAtlas0(am, key);
        if (a0 != null) return buildAtlasFrame(sheet, a0);

        int cols = cutX > 0 ? cutX : 1;
        int rows = cutY > 0 ? cutY : 1;
        int fw = sheet.getWidth() / cols;
        int fh = sheet.getHeight() / rows;
        if (fw <= 0 || fh <= 0) return sheet; // degrade to whole sheet rather than a bad crop
        return Bitmap.createBitmap(sheet, 0, 0, fw, fh);
    }

    private static final class AtlasSprite {
        int srcX, srcY, srcW, srcH, oX, oY, oW, oH;
    }

    /** First {@code <sprite>} (frame 0) from a {@code <sheet>.xml} atlas, or null if none. */
    private static AtlasSprite loadAtlas0(AssetManager am, String sheetKey) {
        String xmlName = sheetKey.substring(0, sheetKey.length() - 4) + ".xml";
        try (InputStream is = am.open("entities/" + xmlName)) {
            XmlPullParser p = Xml.newPullParser();
            p.setInput(is, null);
            int event = p.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG && "sprite".equals(p.getName())) {
                    AtlasSprite s = new AtlasSprite();
                    s.srcX = safeInt(p.getAttributeValue(null, "x"));
                    s.srcY = safeInt(p.getAttributeValue(null, "y"));
                    s.srcW = safeInt(p.getAttributeValue(null, "w"));
                    s.srcH = safeInt(p.getAttributeValue(null, "h"));
                    s.oX = safeInt(p.getAttributeValue(null, "oX"));
                    s.oY = safeInt(p.getAttributeValue(null, "oY"));
                    s.oW = safeInt(p.getAttributeValue(null, "oW"));
                    s.oH = safeInt(p.getAttributeValue(null, "oH"));
                    return s; // frame 0
                }
                event = p.next();
            }
        } catch (Exception ignored) {}
        return null;
    }

    private static Bitmap buildAtlasFrame(Bitmap sheet, AtlasSprite s) {
        int srcX = Math.max(0, Math.min(s.srcX, sheet.getWidth() - 1));
        int srcY = Math.max(0, Math.min(s.srcY, sheet.getHeight() - 1));
        int srcW = Math.min(s.srcW, sheet.getWidth() - srcX);
        int srcH = Math.min(s.srcH, sheet.getHeight() - srcY);
        if (srcW <= 0 || srcH <= 0) return null;

        Bitmap crop = Bitmap.createBitmap(sheet, srcX, srcY, srcW, srcH);
        if (s.oW <= 0 || s.oH <= 0) return crop;

        Bitmap frame = Bitmap.createBitmap(s.oW, s.oH, Bitmap.Config.ARGB_8888);
        new Canvas(frame).drawBitmap(crop, s.oX, s.oY, null);
        crop.recycle();
        return frame;
    }

    private static int safeInt(String v) {
        try { return v == null ? 0 : Integer.parseInt(v.trim()); } catch (Exception e) { return 0; }
    }
}
