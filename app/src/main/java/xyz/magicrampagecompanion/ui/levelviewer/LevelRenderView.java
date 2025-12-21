package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import xyz.magicrampagecompanion.level.Level;

public class LevelRenderView extends View {

    private Paint paint;
    private Paint selectedPaint;

    private Level level;
    private LevelEntity selectedEntity;

    private float scale = 1f;
    private float offsetX = 0f;
    private float offsetY = 0f;

    private float lastTouchX;
    private float lastTouchY;
    private boolean isPanning = false;

    private ScaleGestureDetector scaleDetector;
    private boolean centeredOnce = false;

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

    public LevelRenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setColor(Color.YELLOW);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(3f);

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public void setLevel(Level level) {
        this.level = level;
        selectedEntity = null;
        centeredOnce = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (level == null || level.entities == null || level.entities.isEmpty()) return;

        if (!centeredOnce) {
            centerLevel();
            centeredOnce = true;
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
        canvas.drawCircle(entity.x, entity.y, 10, paint);

        if (entity == selectedEntity) {
            canvas.drawCircle(entity.x, entity.y, 14, selectedPaint);
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

        float levelWidth = maxX - minX;
        float levelHeight = maxY - minY;

        offsetX = (getWidth() - levelWidth * scale) / 2f - minX * scale;
        offsetY = (getHeight() - levelHeight * scale) / 2f - minY * scale;
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
                if (!scaleDetector.isInProgress()) {
                    handleTap(event.getX(), event.getY());
                }
                isPanning = false;
                break;

            case MotionEvent.ACTION_CANCEL:
                isPanning = false;
                break;
        }

        return true;
    }

    private void handleTap(float screenX, float screenY) {
        float worldX = (screenX - offsetX) / scale;
        float worldY = (screenY - offsetY) / scale;

        selectedEntity = null;
        for (LevelEntity e : level.entities) {
            if (e.hitTest(worldX, worldY)) {
                selectedEntity = e;
                break;
            }
        }

        invalidate();
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
