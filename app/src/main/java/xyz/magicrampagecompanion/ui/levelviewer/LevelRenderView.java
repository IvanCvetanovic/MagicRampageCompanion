package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import xyz.magicrampagecompanion.level.Level;

public class LevelRenderView extends View {

    private static final String TAG = "LevelRenderView";

    // Base tile size in world units (Magic Rampage uses ~64-unit tiles)
    private static final float BASE_TILE = 64f;

    // Minimum screen-pixel size for an entity before switching to dot rendering
    private static final float MIN_BOX_PX = 4f;

    // Minimum screen-pixel entity width before labels are drawn
    private static final float MIN_LABEL_BOX_PX = 44f;

    private Level level;

    // Sorted entity list (by z depth, background first)
    private final List<LevelEntity> sortedEntities = new ArrayList<>();

    // Camera state
    private float scale = 1f;
    private float offsetX = 0f;
    private float offsetY = 0f;

    // Level world bounds (computed once per level)
    private float minX, minY, maxX, maxY;
    private boolean boundsReady = false;

    // Touch state
    private float lastTouchX;
    private float lastTouchY;
    private boolean isPanning = false;

    private ScaleGestureDetector scaleDetector;

    // Sprite cache
    private final Map<String, Bitmap> spriteCache = new HashMap<>();

    // Tracks sprite names already logged as missing (avoids per-frame spam)
    private final Set<String> loggedMissingSprites = new HashSet<>();

    // Paints (created once in init)
    private Paint bgPaint;
    private Paint fillPaint;
    private Paint borderPaint;
    private Paint textPaint;
    private Paint additivePaint;
    private final RectF scratchRect = new RectF();

    public LevelRenderView(Context context) {
        super(context);
        init(context);
    }

    public LevelRenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LevelRenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.argb(180, 255, 255, 255));
        borderPaint.setStrokeWidth(1.5f);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setShadowLayer(2f, 1f, 1f, Color.BLACK);

        additivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        additivePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setClickable(true);
    }

    public void setLevel(Level level) {
        this.level = level;
        spriteCache.clear();
        sortedEntities.clear();
        loggedMissingSprites.clear();
        boundsReady = false;

        if (level != null && level.entities != null) {
            sortedEntities.addAll(level.entities);
            // Draw background (most-negative z) first
            sortedEntities.sort((a, b) -> Float.compare(a.z, b.z));
            computeBounds();
            logLevelStats();
        }

        // If the view is already laid out, fit now; otherwise onSizeChanged will do it.
        if (getWidth() > 0 && getHeight() > 0 && boundsReady) {
            fitToScreen();
        }

        invalidate();
    }

    // --------------------
    // BOUNDS + FIT
    // --------------------

    private void computeBounds() {
        minX = Float.MAX_VALUE;
        minY = Float.MAX_VALUE;
        maxX = -Float.MAX_VALUE;
        maxY = -Float.MAX_VALUE;

        for (LevelEntity e : sortedEntities) {
            // Only include visible entities so invisible markers/cam-controls
            // at extreme positions don't distort the fit-to-screen scale.
            if (e.spriteFile.isEmpty()) continue;

            if (e.x < minX) minX = e.x;
            if (e.y < minY) minY = e.y;
            if (e.x > maxX) maxX = e.x;
            if (e.y > maxY) maxY = e.y;
        }

        boundsReady = (minX != Float.MAX_VALUE);
    }

    private void logLevelStats() {
        int total = sortedEntities.size();
        int withSprite = 0;
        int invisible = 0;

        // position key → list of "sprite(z=N)" strings for visible entities
        Map<String, List<String>> clusters = new TreeMap<>();

        for (LevelEntity e : sortedEntities) {
            if (e.spriteFile.isEmpty()) {
                invisible++;
                continue;
            }
            withSprite++;
            String key = (int) e.x + "," + (int) e.y;
            clusters.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(e.spriteFile + "(z=" + (int) e.z + ")");
        }

        Log.d(TAG, "=== LEVEL STATS ===");
        Log.d(TAG, "Total entities  : " + total);
        Log.d(TAG, "With sprite     : " + withSprite + "  (will render)");
        Log.d(TAG, "No sprite       : " + invisible + "  (skipped)");
        Log.d(TAG, "Bounds: x=[" + minX + ".." + maxX + "]  y=[" + minY + ".." + maxY + "]");

        // Report every position where 2+ visible entities overlap
        Log.d(TAG, "--- Stacked entity clusters ---");
        boolean anyStack = false;
        for (Map.Entry<String, List<String>> entry : clusters.entrySet()) {
            if (entry.getValue().size() > 1) {
                Log.d(TAG, "  (" + entry.getKey() + ") x" + entry.getValue().size()
                        + " -> " + entry.getValue());
                anyStack = true;
            }
        }
        if (!anyStack) Log.d(TAG, "  (none)");
    }

    /** Computes scale and offset so the level fits the view with a small margin. */
    private void fitToScreen() {
        float levelW = maxX - minX;
        float levelH = maxY - minY;

        if (levelW <= 0 || levelH <= 0) return;

        float fitScaleX = getWidth() / levelW;
        float fitScaleY = getHeight() / levelH;
        scale = Math.min(fitScaleX, fitScaleY) * 0.88f; // 88% for breathing room

        float levelCenterX = (minX + maxX) / 2f;
        float levelCenterY = (minY + maxY) / 2f;
        offsetX = getWidth() / 2f - levelCenterX * scale;
        offsetY = getHeight() / 2f - levelCenterY * scale;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        if (boundsReady && w > 0 && h > 0) {
            fitToScreen();
        }
    }

    // --------------------
    // DRAWING
    // --------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Background: tinted by scene ambient color
        if (level != null && level.sceneProperties != null) {
            SceneProperties sp = level.sceneProperties;
            int r = (int) (sp.ambientR * 30);
            int g = (int) (sp.ambientG * 30);
            int b = (int) (sp.ambientB * 50);
            bgPaint.setColor(Color.rgb(
                    Math.min(r, 255),
                    Math.min(g, 255),
                    Math.min(b, 255)));
        } else {
            bgPaint.setColor(Color.rgb(18, 18, 28));
        }
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);

        if (sortedEntities.isEmpty()) return;

        canvas.save();
        canvas.translate(offsetX, offsetY);
        canvas.scale(scale, scale);

        for (LevelEntity entity : sortedEntities) {
            drawEntity(canvas, entity);
        }

        canvas.restore();
    }

    private void drawEntity(Canvas canvas, LevelEntity entity) {
        if (entity == null) return;

        // No sprite defined means this is an invisible game-logic object (marker,
        // camera control, trigger zone, light source, fog volume, etc.) — skip it.
        if (entity.spriteFile.isEmpty()) return;

        Bitmap frame = loadSpriteFrame(entity.spriteFile, entity.spriteFrame);

        if (frame != null) {
            Matrix m = new Matrix();
            float cx = frame.getWidth() / 2f;
            float cy = frame.getHeight() / 2f;
            m.postTranslate(-cx, -cy);
            m.postScale(entity.scaleX, entity.scaleY);
            m.postRotate(entity.angle);
            m.postTranslate(entity.x, entity.y);
            Paint paint = (entity.blendMode == 1) ? additivePaint : null;
            canvas.drawBitmap(frame, m, paint);
        } else {
            drawEntityFallback(canvas, entity);
        }
    }

    private void drawEntityFallback(Canvas canvas, LevelEntity entity) {
        float hw = entity.scaleX * BASE_TILE / 2f;
        float hh = entity.scaleY * BASE_TILE / 2f;

        // How wide this entity appears on screen right now
        float renderedW = hw * 2f * scale;

        fillPaint.setColor(getEntityColor(entity));

        canvas.save();
        canvas.translate(entity.x, entity.y);
        if (entity.angle != 0f) canvas.rotate(entity.angle);

        if (renderedW < MIN_BOX_PX) {
            // Too small for a rect — draw a colored dot
            float r = MIN_BOX_PX / 2f / scale;
            canvas.drawCircle(0, 0, r, fillPaint);
        } else {
            scratchRect.set(-hw, -hh, hw, hh);
            canvas.drawRect(scratchRect, fillPaint);
            borderPaint.setStrokeWidth(1.5f / scale);
            canvas.drawRect(scratchRect, borderPaint);

            if (renderedW > MIN_LABEL_BOX_PX) {
                String label = shortLabel(entity.entityName);
                // Text size in world units so it appears as ~13 screen-px
                textPaint.setTextSize(13f / scale);
                canvas.drawText(label, 0, 4f / scale, textPaint);
            }
        }

        canvas.restore();
    }

    // --------------------
    // ENTITY COLORS
    // --------------------

    private int getEntityColor(LevelEntity entity) {
        String name = entity.entityName.toLowerCase();
        String sprite = entity.spriteFile.toLowerCase();

        if (name.contains("invisible") || name.contains("wall") || name.contains("collide"))
            return Color.argb(100, 180, 180, 180);
        if (name.startsWith("_marker") || name.contains("marker") || name.contains("waypoint"))
            return Color.argb(220, 255, 160, 0);
        if (name.contains("light") || name.contains("beam") || name.contains("ambient") || name.contains("glow"))
            return Color.argb(180, 255, 230, 80);
        if (name.contains("enemy") || name.contains("spider") || name.contains("zombie")
                || name.contains("orc") || name.contains("skeleton") || name.contains("troll"))
            return Color.argb(220, 220, 40, 40);
        if (name.contains("bg") || name.contains("background") || sprite.contains("bg"))
            return Color.argb(180, 60, 80, 160);
        if (name.contains("chain") || name.contains("rope"))
            return Color.argb(200, 140, 120, 80);
        if (name.contains("coin") || sprite.contains("coin"))
            return Color.argb(220, 255, 210, 30);
        if (name.contains("chest") || name.contains("treasure"))
            return Color.argb(220, 180, 110, 30);
        if (name.contains("door") || name.contains("gate") || name.contains("portal"))
            return Color.argb(220, 30, 200, 100);
        if (name.contains("platform") || name.contains("floor") || name.contains("tile")
                || name.contains("ground"))
            return Color.argb(200, 110, 85, 55);
        if (name.contains("flag"))
            return Color.argb(220, 60, 220, 80);
        if (name.contains("pottery") || name.contains("pot") || name.contains("barrel")
                || name.contains("crate") || name.contains("box"))
            return Color.argb(200, 170, 110, 60);
        if (name.contains("castle") || name.contains("stone") || name.contains("brick"))
            return Color.argb(200, 120, 100, 80);
        if (sprite.contains("item") || sprite.contains("pickup"))
            return Color.argb(220, 160, 80, 220);

        // Default
        return Color.argb(180, 70, 170, 200);
    }

    private String shortLabel(String entityName) {
        // Strip ".ent" or ".png" extension for display
        int dot = entityName.lastIndexOf('.');
        String label = (dot > 0) ? entityName.substring(0, dot) : entityName;
        // Trim leading underscore
        if (label.startsWith("_")) label = label.substring(1);
        return label;
    }

    // --------------------
    // SPRITE LOADING
    // --------------------

    /**
     * Returns the cropped frame bitmap for a sprite sheet, cached after first use.
     * Frame width is assumed to equal the sheet height (square frames in a horizontal strip).
     * Single-frame sprites (width <= height) are returned as-is.
     */
    private Bitmap loadSpriteFrame(String spriteName, int frameIndex) {
        if (spriteName == null || spriteName.trim().isEmpty()) return null;

        String sheetKey = spriteName.endsWith(".png") ? spriteName : spriteName + ".png";
        String frameKey = sheetKey + "#" + frameIndex;

        if (spriteCache.containsKey(frameKey)) return spriteCache.get(frameKey);

        // Load full sheet if not already cached
        if (!spriteCache.containsKey(sheetKey)) {
            try {
                InputStream is = getContext().getAssets().open("entities/" + sheetKey);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                spriteCache.put(sheetKey, bmp);
                Log.d(TAG, "Loaded sheet: " + sheetKey);
            } catch (IOException e) {
                if (loggedMissingSprites.add(sheetKey)) {
                    Log.w(TAG, "MISSING PNG: entities/" + sheetKey);
                }
                spriteCache.put(sheetKey, null);
                spriteCache.put(frameKey, null);
                return null;
            }
        }

        Bitmap sheet = spriteCache.get(sheetKey);
        if (sheet == null) {
            spriteCache.put(frameKey, null);
            return null;
        }

        // Determine number of frames: if width > height, assume horizontal strip of square frames
        int numFrames = (sheet.getWidth() > sheet.getHeight())
                ? sheet.getWidth() / sheet.getHeight()
                : 1;
        int frameWidth = sheet.getWidth() / numFrames;
        int clampedFrame = Math.min(Math.max(frameIndex, 0), numFrames - 1);

        Bitmap frameBitmap;
        if (numFrames > 1) {
            frameBitmap = Bitmap.createBitmap(sheet, clampedFrame * frameWidth, 0, frameWidth, sheet.getHeight());
        } else {
            frameBitmap = sheet;
        }

        spriteCache.put(frameKey, frameBitmap);
        return frameBitmap;
    }

    // --------------------
    // TOUCH
    // --------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                isPanning = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (!scaleDetector.isInProgress() && isPanning) {
                    offsetX += event.getX() - lastTouchX;
                    offsetY += event.getY() - lastTouchY;
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                isPanning = false;
                performClick();
                break;

            case MotionEvent.ACTION_CANCEL:
                isPanning = false;
                break;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float prevScale = scale;
            scale *= detector.getScaleFactor();
            scale = Math.max(0.05f, Math.min(scale, 10f));

            // Zoom towards the pinch focal point
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            offsetX = focusX - (focusX - offsetX) * (scale / prevScale);
            offsetY = focusY - (focusY - offsetY) * (scale / prevScale);

            invalidate();
            return true;
        }
    }
}
