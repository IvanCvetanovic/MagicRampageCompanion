package xyz.magicrampagecompanion.ui.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import java.io.InputStream;

import xyz.magicrampagecompanion.character.CharacterSpriteCompositor;
import xyz.magicrampagecompanion.level.LevelParser;
import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;

/**
 * Builds the two "crazy" Editor-hub card scenes off the UI thread: a dungeon backdrop, a coloured
 * radial glow, and composited in-game characters/props layered for depth. Pure drawing on top of
 * the existing {@link CharacterSpriteCompositor}; no dependency on the live renderer.
 */
final class HubSceneBuilder {

    private static final int W = 720, H = 600;
    private static final String BG = "entities/castle_bg_wall_window.png";

    private HubSceneBuilder() {}

    /** Characters card: crimson glow + a big armored skeleton flanked by two enemies. */
    static Bitmap buildCharactersScene(Context ctx) {
        Bitmap out = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        fillBackdrop(c, decode(ctx, BG), 0xB4);
        drawGlow(c, W / 2f, H * 0.52f, W * 0.46f, 0x99E0534F);
        // flankers first (behind, dimmer), hero in front
        drawSprite(c, charTrim(ctx, "dark-skeleton.character"), W * 0.22f, H * 0.93f, 300, 200);
        drawSprite(c, charTrim(ctx, "vampire.character"),       W * 0.79f, H * 0.93f, 300, 200);
        drawSprite(c, charTrim(ctx, "armored-skeleton.character"), W / 2f, H * 0.99f, 430, 255);
        return out;
    }

    /** Levels card: steel-blue glow + the castle battlement with two patrolling enemies. */
    static Bitmap buildLevelsScene(Context ctx) {
        Bitmap out = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        fillBackdrop(c, decode(ctx, BG), 0x8C);
        drawGlow(c, W / 2f, H * 0.55f, W * 0.52f, 0x885B8DEF);
        Bitmap wall = trim(decode(ctx, "entities/castle_hendrix.png"));
        // wall first; enemies stand ON the battlement (feet at the wall's top edge)
        float wallTop = drawWall(c, wall);
        drawSprite(c, charTrim(ctx, "skeleton.character"),      W * 0.32f, wallTop + 18, 150, 255);
        drawSprite(c, charTrim(ctx, "fire-skeleton.character"), W * 0.66f, wallTop + 18, 150, 255);
        return out;
    }

    // ── drawing helpers ──────────────────────────────────────────────────────────────────────────

    private static void fillBackdrop(Canvas c, Bitmap bg, int darkenAlpha) {
        if (bg != null) {
            float s = Math.max((float) W / bg.getWidth(), (float) H / bg.getHeight());
            float w = bg.getWidth() * s, h = bg.getHeight() * s;
            Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
            c.drawBitmap(bg, null, new RectF((W - w) / 2, (H - h) / 2, (W + w) / 2, (H + h) / 2), p);
        } else {
            c.drawColor(0xFF101826);
        }
        c.drawColor(darkenAlpha << 24); // darken so the subjects + glow pop
    }

    private static void drawGlow(Canvas c, float cx, float cy, float r, int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new RadialGradient(cx, cy, r, color, color & 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, r, p);
    }

    /** Draw a sprite scaled to targetH (px), centred on cx with its feet at bottomY, crisp. */
    private static void drawSprite(Canvas c, Bitmap b, float cx, float bottomY, float targetH, int alpha) {
        if (b == null) return;
        float s = targetH / b.getHeight();
        float w = b.getWidth() * s;
        RectF dst = new RectF(cx - w / 2, bottomY - targetH, cx + w / 2, bottomY);
        Paint p = new Paint();
        p.setFilterBitmap(false); // crisp pixel art
        p.setAlpha(alpha);
        c.drawBitmap(b, null, dst, p);
    }

    /** The battlement spans the bottom width, anchored to the floor. Returns its top edge (y). */
    private static float drawWall(Canvas c, Bitmap wall) {
        if (wall == null) return H;
        float targetW = W * 0.92f;
        float s = targetW / wall.getWidth();
        float h = wall.getHeight() * s;
        float top = H - h;
        RectF dst = new RectF((W - targetW) / 2, top, (W + targetW) / 2, H);
        Paint p = new Paint();
        p.setFilterBitmap(false);
        c.drawBitmap(wall, null, dst, p);
        return top;
    }

    // ── asset helpers ────────────────────────────────────────────────────────────────────────────

    private static Bitmap charTrim(Context ctx, String file) {
        LevelEntity e = new LevelEntity();
        try (InputStream is = ctx.getAssets().open("entities/" + file)) {
            LevelParser.parseCharacterStream(ctx, e, is);
        } catch (Exception ignored) {}
        return trim(CharacterSpriteCompositor.compose(ctx, e));
    }

    private static Bitmap decode(Context ctx, String asset) {
        try (InputStream is = ctx.getAssets().open(asset)) {
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            return null;
        }
    }

    /** Crop transparent margins so a sprite fills its target box (composited frames are padded). */
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
