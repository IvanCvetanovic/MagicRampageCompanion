package xyz.magicrampagecompanion.ui.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

import java.io.InputStream;

import xyz.magicrampagecompanion.character.CharacterSpriteCompositor;
import xyz.magicrampagecompanion.level.LevelParser;
import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;

/**
 * Builds the cinematic Editor-hub card scenes off the UI thread: a darkened dungeon backdrop,
 * additive god-ray light shafts, a coloured radial glow, depth-layered characters with soft ground
 * shadows, fire torches + floating embers, and a vignette. All from in-game art (reusing
 * {@link CharacterSpriteCompositor}); no dependency on the live renderer.
 */
final class HubSceneBuilder {

    private static final int W = 720, H = 600;
    private static final String BG = "entities/castle_bg_wall_window.png";
    private static final PorterDuffXfermode ADD = new PorterDuffXfermode(PorterDuff.Mode.ADD);

    private HubSceneBuilder() {}

    /** Characters card: crimson glow + god-rays + a hero flanked/backed by a monster roster. */
    static Bitmap buildCharactersScene(Context ctx) {
        Bitmap out = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        Bitmap beam = decode(ctx, "entities/light_beam.png");
        Bitmap spark = decode(ctx, "entities/sparkle_dot_entity.png");

        fillBackdrop(c, decode(ctx, BG), 0xC0);
        godRay(c, beam, W * 0.50f, W * 0.52f, H * 0.98f, 140);
        godRay(c, beam, W * 0.26f, W * 0.30f, H * 0.82f, 80);
        godRay(c, beam, W * 0.74f, W * 0.30f, H * 0.82f, 80);
        drawGlow(c, W / 2f, H * 0.50f, W * 0.50f, 0x99E0534F);

        // back row (dim, smaller) → depth
        actor(c, charTrim(ctx, "zombie-guard.character"),  W * 0.40f, H * 0.80f, 210, 150);
        actor(c, charTrim(ctx, "fire-skeleton.character"), W * 0.60f, H * 0.80f, 210, 150);
        // front flankers
        actor(c, charTrim(ctx, "dark-skeleton.character"), W * 0.20f, H * 0.95f, 300, 230);
        actor(c, charTrim(ctx, "vampire.character"),       W * 0.80f, H * 0.95f, 300, 230);
        // hero
        actor(c, charTrim(ctx, "armored-skeleton.character"), W / 2f, H * 1.00f, 440, 255);

        embers(c, spark, 0x66FF6040);
        vignette(c);
        return out;
    }

    /** Levels card: steel-blue glow + god-rays + the battlement with torches and patrolling skeletons. */
    static Bitmap buildLevelsScene(Context ctx) {
        Bitmap out = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        Bitmap beam = decode(ctx, "entities/light_beam.png");
        Bitmap fire = decode(ctx, "entities/fire_entity.png");
        Bitmap spark = decode(ctx, "entities/sparkle_dot_entity.png");

        fillBackdrop(c, decode(ctx, BG), 0x9A);
        godRay(c, beam, W * 0.50f, W * 0.62f, H * 1.0f, 165);
        godRay(c, beam, W * 0.28f, W * 0.30f, H * 0.85f, 90);
        drawGlow(c, W / 2f, H * 0.52f, W * 0.56f, 0x885B8DEF);

        Bitmap wall = trim(decode(ctx, "entities/castle_hendrix.png"));
        float wallTop = drawWall(c, wall);
        // torches flanking, then skeletons patrolling on the battlement
        flame(c, fire, W * 0.15f, wallTop + 26, 96);
        flame(c, fire, W * 0.85f, wallTop + 26, 96);
        actor(c, charTrim(ctx, "skeleton.character"),      W * 0.35f, wallTop + 16, 150, 255);
        actor(c, charTrim(ctx, "fire-skeleton.character"), W * 0.63f, wallTop + 16, 150, 255);

        embers(c, spark, 0x66FFC060);
        vignette(c);
        return out;
    }

    // ── scene elements ───────────────────────────────────────────────────────────────────────────

    private static void fillBackdrop(Canvas c, Bitmap bg, int darkenAlpha) {
        if (bg != null) {
            float s = Math.max((float) W / bg.getWidth(), (float) H / bg.getHeight());
            float w = bg.getWidth() * s, h = bg.getHeight() * s;
            c.drawBitmap(bg, null, new RectF((W - w) / 2, (H - h) / 2, (W + w) / 2, (H + h) / 2),
                    new Paint(Paint.FILTER_BITMAP_FLAG));
        } else {
            c.drawColor(0xFF101826);
        }
        c.drawColor(darkenAlpha << 24);
    }

    /** Additive light shaft from above (the sprite is white streaks on black → ADD = glow). */
    private static void godRay(Canvas c, Bitmap beam, float cx, float w, float h, int alpha) {
        if (beam == null) return;
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        p.setXfermode(ADD);
        p.setAlpha(alpha);
        c.drawBitmap(beam, null, new RectF(cx - w / 2, -20, cx + w / 2, -20 + h), p);
    }

    private static void drawGlow(Canvas c, float cx, float cy, float r, int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new RadialGradient(cx, cy, r, color, color & 0x00FFFFFF, Shader.TileMode.CLAMP));
        c.drawCircle(cx, cy, r, p);
    }

    /** Soft ground shadow + the sprite (crisp), centred on cx with feet at feetY. */
    private static void actor(Canvas c, Bitmap b, float cx, float feetY, float targetH, int alpha) {
        if (b == null) return;
        float s = targetH / b.getHeight();
        float w = b.getWidth() * s;
        Paint sh = new Paint(Paint.ANTI_ALIAS_FLAG);
        sh.setColor(0x73000000);
        sh.setMaskFilter(new BlurMaskFilter(Math.max(4f, w * 0.06f), BlurMaskFilter.Blur.NORMAL));
        c.drawOval(new RectF(cx - w * 0.36f, feetY - w * 0.07f, cx + w * 0.36f, feetY + w * 0.07f), sh);
        Paint p = new Paint();
        p.setFilterBitmap(false);
        p.setAlpha(alpha);
        c.drawBitmap(b, null, new RectF(cx - w / 2, feetY - targetH, cx + w / 2, feetY), p);
    }

    private static void flame(Canvas c, Bitmap fire, float cx, float bottomY, float size) {
        drawGlow(c, cx, bottomY - size * 0.45f, size * 1.4f, 0x99FF8828);
        if (fire == null) return;
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        p.setXfermode(ADD);
        c.drawBitmap(fire, null, new RectF(cx - size / 2, bottomY - size, cx + size / 2, bottomY), p);
    }

    private static void embers(Canvas c, Bitmap spark, int tint) {
        if (spark == null) return;
        float[][] pts = {{0.18f, 0.26f, 34}, {0.50f, 0.16f, 26}, {0.82f, 0.30f, 36},
                {0.34f, 0.46f, 22}, {0.66f, 0.40f, 28}, {0.13f, 0.56f, 24}, {0.88f, 0.58f, 30}};
        Paint p = new Paint(Paint.FILTER_BITMAP_FLAG);
        p.setXfermode(ADD);
        p.setColorFilter(new android.graphics.PorterDuffColorFilter(tint | 0xFF000000, PorterDuff.Mode.MULTIPLY));
        for (int i = 0; i < pts.length; i++) {
            float x = pts[i][0] * W, y = pts[i][1] * H, sz = pts[i][2];
            p.setAlpha((i % 2 == 0) ? 150 : 100);
            c.drawBitmap(spark, null, new RectF(x - sz / 2, y - sz / 2, x + sz / 2, y + sz / 2), p);
        }
    }

    private static void vignette(Canvas c) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        float cx = W / 2f, cy = H / 2f, r = (float) Math.hypot(W, H) / 2f;
        p.setShader(new RadialGradient(cx, cy, r,
                new int[]{0x00000000, 0x00000000, 0xBE000000}, new float[]{0f, 0.55f, 1f},
                Shader.TileMode.CLAMP));
        c.drawRect(0, 0, W, H, p);
    }

    /** Battlement anchored to the floor; returns its top edge (y). */
    private static float drawWall(Canvas c, Bitmap wall) {
        if (wall == null) return H;
        float targetW = W * 0.92f;
        float s = targetW / wall.getWidth();
        float h = wall.getHeight() * s;
        float top = H - h;
        Paint p = new Paint();
        p.setFilterBitmap(false);
        c.drawBitmap(wall, null, new RectF((W - targetW) / 2, top, (W + targetW) / 2, H), p);
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
