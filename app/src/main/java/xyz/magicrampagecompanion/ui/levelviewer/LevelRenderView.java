package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import xyz.magicrampagecompanion.level.Level;

public class LevelRenderView extends View {

    private static final String TAG = "LevelRenderView";

    private Paint debugPaint;
    private Level level;

    private float scale = 1f;
    private float offsetX = 0f;
    private float offsetY = 0f;

    private float lastTouchX;
    private float lastTouchY;
    private boolean isPanning = false;

    private ScaleGestureDetector scaleDetector;
    private boolean centeredOnce = false;

    // 🔹 Sprite cache
    private final Map<String, Bitmap> spriteCache = new HashMap<>();

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
        debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        debugPaint.setColor(Color.RED);
        debugPaint.setStyle(Paint.Style.FILL);

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setClickable(true); // 👈 required for performClick
    }

    public void setLevel(Level level) {
        this.level = level;
        centeredOnce = false;
        spriteCache.clear();

        if (level != null && level.entities != null) {
            Log.d(TAG, "Level loaded with " + level.entities.size() + " entities");
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (level == null || level.entities == null || level.entities.isEmpty()) {
            Log.w(TAG, "Nothing to draw: level or entities empty");
            return;
        }

        if (!centeredOnce) {
            centerLevel();
            centeredOnce = true;
            Log.d(TAG, "Level centered");
        }

        canvas.save();
        canvas.translate(offsetX, offsetY);
        canvas.scale(scale, scale);

        for (LevelEntity entity : level.entities) {
            drawEntity(canvas, entity);
        }

        canvas.restore();
    }

    private void drawEntity(Canvas canvas, LevelEntity entity) {
        if (entity == null) return;

        Log.v(TAG, "Drawing entity: name=" + entity.entityName +
                ", sprite=" + entity.spriteFile +
                ", x=" + entity.x +
                ", y=" + entity.y);

        Bitmap sprite = loadSprite(entity.spriteFile);

        if (sprite == null) {
            // 🔴 fallback
            Log.w(TAG, "Missing sprite for entity '" + entity.entityName +
                    "' (spriteFile=" + entity.spriteFile + ")");
            canvas.drawCircle(entity.x, entity.y, 10, debugPaint);
            return;
        }

        Matrix m = new Matrix();

        float cx = sprite.getWidth() / 2f;
        float cy = sprite.getHeight() / 2f;

        m.postTranslate(-cx, -cy);
        m.postScale(entity.scaleX, entity.scaleY);
        m.postRotate(entity.angle);
        m.postTranslate(entity.x, entity.y);

        canvas.drawBitmap(sprite, m, null);
    }

    // --------------------
    // SPRITE LOADING
    // --------------------

    private Bitmap loadSprite(String spriteName) {
        if (spriteName == null || spriteName.trim().isEmpty()) {
            Log.w(TAG, "Sprite name is null or empty");
            return null;
        }

        String key = spriteName.endsWith(".png") ? spriteName : spriteName + ".png";

        if (spriteCache.containsKey(key)) {
            return spriteCache.get(key);
        }

        try {
            InputStream is = getContext().getAssets().open("entities/" + key);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            spriteCache.put(key, bmp);

            Log.d(TAG, "Loaded sprite: entities/" + key);
            return bmp;

        } catch (IOException e) {
            Log.e(TAG, "Failed to load sprite: entities/" + key, e);
            spriteCache.put(key, null);
            return null;
        }
    }

    // --------------------
    // CENTERING
    // --------------------

    private void centerLevel() {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (LevelEntity e : level.entities) {
            minX = Math.min(minX, e.x);
            minY = Math.min(minY, e.y);
            maxX = Math.max(maxX, e.x);
            maxY = Math.max(maxY, e.y);
        }

        float levelW = maxX - minX;
        float levelH = maxY - minY;

        offsetX = (getWidth() - levelW * scale) / 2f - minX * scale;
        offsetY = (getHeight() - levelH * scale) / 2f - minY * scale;

        Log.d(TAG, "Center bounds: min=(" + minX + "," + minY +
                "), max=(" + maxX + "," + maxY + ")");
    }

    // --------------------
    // TOUCH CONTROLS
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
                performClick(); // 👈 REQUIRED
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
            scale *= detector.getScaleFactor();
            scale = Math.max(0.2f, Math.min(scale, 5f));
            invalidate();
            return true;
        }
    }
}
