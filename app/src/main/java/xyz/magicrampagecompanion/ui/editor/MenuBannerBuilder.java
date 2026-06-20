package xyz.magicrampagecompanion.ui.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import java.io.InputStream;

import xyz.magicrampagecompanion.character.CharacterSpriteCompositor;
import xyz.magicrampagecompanion.level.LevelParser;
import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;

/**
 * Builds a wide menu-row banner in the hub's design language: a darkened dungeon backdrop, an
 * accent wash + glow on the right, and a single crisp in-game sprite (a character via
 * {@link CharacterSpriteCompositor}, or a decoded PNG). The row's bold label + accent border live
 * in the layout; this only paints the art. {@code artSpec} = {@code "char:<file>.character"} or
 * {@code "png:<file>.png"}.
 */
public final class MenuBannerBuilder {

    private static final int W = 1000, H = 168;
    private static final String BG = "entities/castle_bg_wall_window.png";

    private MenuBannerBuilder() {}

    public static Bitmap build(Context ctx, String artSpec, int accent) {
        Bitmap out = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);

        Bitmap bg = decode(ctx, BG);
        if (bg != null) {
            float s = Math.max((float) W / bg.getWidth(), (float) H / bg.getHeight());
            float w = bg.getWidth() * s, h = bg.getHeight() * s;
            c.drawBitmap(bg, null, new RectF((W - w) / 2, (H - h) / 2, (W + w) / 2, (H + h) / 2),
                    new Paint(Paint.FILTER_BITMAP_FLAG));
        } else {
            c.drawColor(0xFF12161F);
        }
        c.drawColor(0xC8 << 24); // darken so the label + sprite read clearly

        // subtle accent wash, brightening toward the sprite on the right
        Paint wash = new Paint();
        wash.setShader(new LinearGradient(0, 0, W, 0,
                0x00000000, (accent & 0x00FFFFFF) | 0x3C000000, Shader.TileMode.CLAMP));
        c.drawRect(0, 0, W, H, wash);

        // glow behind the sprite
        glow(c, W * 0.84f, H * 0.5f, H * 0.85f, (accent & 0x00FFFFFF) | 0x99000000);

        Bitmap art = trim(resolve(ctx, artSpec));
        if (art != null) {
            float th = H * 0.92f;
            float s = th / art.getHeight();
            float w = art.getWidth() * s;
            float cx = W * 0.84f, cy = H * 0.52f;
            Paint p = new Paint();
            p.setFilterBitmap(false); // crisp pixel art
            c.drawBitmap(art, null, new RectF(cx - w / 2, cy - th / 2, cx + w / 2, cy + th / 2), p);
        }
        return out;
    }

    private static void glow(Canvas c, float cx, float cy, float r, int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new RadialGradient(cx, cy, r, color, color & 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, r, p);
    }

    private static Bitmap resolve(Context ctx, String artSpec) {
        if (artSpec == null) return null;
        if (artSpec.startsWith("char:")) {
            LevelEntity e = new LevelEntity();
            try (InputStream is = ctx.getAssets().open("entities/" + artSpec.substring(5))) {
                LevelParser.parseCharacterStream(ctx, e, is);
            } catch (Exception ignored) {}
            return CharacterSpriteCompositor.compose(ctx, e);
        }
        if (artSpec.startsWith("png:")) {
            return decode(ctx, "entities/" + artSpec.substring(4));
        }
        return null;
    }

    private static Bitmap decode(Context ctx, String asset) {
        try (InputStream is = ctx.getAssets().open(asset)) {
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            return null;
        }
    }

    private static Bitmap trim(Bitmap b) {
        if (b == null) return null;
        int w = b.getWidth(), h = b.getHeight();
        int minX = w, minY = h, maxX = -1, maxY = -1;
        int[] row = new int[w];
        for (int y = 0; y < h; y++) {
            b.getPixels(row, 0, w, 0, y, w, 1);
            for (int x = 0; x < w; x++) {
                if ((row[x] >>> 24) > 16) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }
        if (maxX < minX || maxY < minY) return b;
        return Bitmap.createBitmap(b, minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
}
