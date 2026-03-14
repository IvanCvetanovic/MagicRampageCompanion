package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;

import org.xmlpull.v1.XmlPullParser;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import xyz.magicrampagecompanion.level.Level;
import xyz.magicrampagecompanion.level.LevelParser;

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
    
    // Store the base scale used to fit the level to screen
    private float minFitScale = 1f;

    // Visibility toggles
    private boolean showLogicEntities = true;
    private boolean secretsUnlocked = false;

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

    // Atlas cache: sheet filename → ordered list of sprite entries (null = no XML exists)
    private final Map<String, List<AtlasSprite>> atlasCache = new HashMap<>();

    /** One entry from a TextureAtlas XML file. */
    private static class AtlasSprite {
        int srcX, srcY, srcW, srcH; // source rect in the PNG
        int oX, oY, oW, oH;        // offset and logical frame size
    }

    // Cache of tinted paints keyed by packed ARGB color (avoids allocating per frame)
    private final Map<Integer, Paint> tintPaintCache = new HashMap<>();

    // Tracks sprite names already logged as missing (avoids per-frame spam)
    private final Set<String> loggedMissingSprites = new HashSet<>();

    // Set to true after the first draw so we log the sprite-load summary exactly once
    private boolean spriteSummaryLogged = false;

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
        atlasCache.clear();
        tintPaintCache.clear();
        sortedEntities.clear();
        loggedMissingSprites.clear();
        spriteSummaryLogged = false;
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

    public void setShowLogicEntities(boolean show) {
        this.showLogicEntities = show;
        invalidate();
    }

    public boolean isShowingLogicEntities() {
        return showLogicEntities;
    }

    public void setSecretsUnlocked(boolean unlocked) {
        this.secretsUnlocked = unlocked;
        invalidate();
    }

    public boolean isSecretsUnlocked() {
        return secretsUnlocked;
    }

    public void centerOnWorldPos(float worldX, float worldY) {
        offsetX = getWidth() / 2f - worldX * scale;
        offsetY = getHeight() / 2f - worldY * scale;
        invalidate();
    }

    public void centerAndZoomOnWorldPos(float worldX, float worldY) {
        // Zoom in more than the initial fit scale (initial is 10x, secret is 25x)
        this.scale = minFitScale * 25.0f;
        offsetX = getWidth() / 2f - worldX * scale;
        offsetY = getHeight() / 2f - worldY * scale;
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

        // Track no-sprite entity names (name → count) for diagnosis
        Map<String, Integer> noSpriteName = new LinkedHashMap<>();
        // Track distinct sprite files used
        Set<String> distinctSprites = new HashSet<>();

        for (LevelEntity e : sortedEntities) {
            if (e.spriteFile.isEmpty()) {
                invisible++;
                String key = e.entityName.isEmpty() ? "<no-name>" : e.entityName;
                noSpriteName.merge(key, 1, Integer::sum);
                continue;
            }
            withSprite++;
            distinctSprites.add(e.spriteFile);
            String key = (int) e.x + "," + (int) e.y;
            clusters.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(e.spriteFile + "(z=" + (int) e.z + ")");
        }

        Log.d(TAG, "=== LEVEL STATS ===");
        Log.d(TAG, "Total entities  : " + total);
        Log.d(TAG, "With sprite     : " + withSprite + "  (will render)");
        Log.d(TAG, "No sprite       : " + invisible + "  (skipped)");
        Log.d(TAG, "Distinct sprites: " + distinctSprites.size());
        Log.d(TAG, "Bounds: x=[" + minX + ".." + maxX + "]  y=[" + minY + ".." + maxY + "]");

        // --- No-sprite entity names sorted by count (most common first) ---
        if (!noSpriteName.isEmpty()) {
            List<Map.Entry<String, Integer>> sorted = new ArrayList<>(noSpriteName.entrySet());
            sorted.sort((a, b) -> b.getValue() - a.getValue());
            Log.d(TAG, "--- No-sprite entities (name → count, top 20) ---");
            int shown = 0;
            for (Map.Entry<String, Integer> entry : sorted) {
                Log.d(TAG, "  " + entry.getValue() + "x  " + entry.getKey());
                if (++shown >= 20) break;
            }
            if (sorted.size() > 20) {
                Log.d(TAG, "  ... and " + (sorted.size() - 20) + " more distinct names");
            }
        }

        // --- Distinct sprite files used by the 'withSprite' entities ---
        if (!distinctSprites.isEmpty()) {
            List<String> spriteList = new ArrayList<>(distinctSprites);
            Collections.sort(spriteList);
            Log.d(TAG, "--- Sprite files referenced (" + spriteList.size() + ") ---");
            for (String s : spriteList) {
                Log.d(TAG, "  " + s);
            }
        }

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
        minFitScale = Math.min(fitScaleX, fitScaleY);
        scale = minFitScale * 10.0f; // Significantly zoomed in initially

        float levelCenterX = (minX + maxX) / 2f;
        float levelCenterY = (minY + maxY) / 2f;

        // Try to center on spawn0
        for (LevelEntity e : sortedEntities) {
            if ("spawn0".equalsIgnoreCase(e.entityName)) {
                levelCenterX = e.x;
                levelCenterY = e.y;
                break;
            }
        }

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

        // One-shot summary after the first full draw (all sheets attempted)
        if (!spriteSummaryLogged) {
            spriteSummaryLogged = true;
            logSpriteSummary();
        }
    }

    private void logSpriteSummary() {
        int loaded = 0, missing = 0;
        List<String> missingList = new ArrayList<>();
        List<String> loadedList = new ArrayList<>();
        for (Map.Entry<String, Bitmap> entry : spriteCache.entrySet()) {
            String key = entry.getKey();
            // Only log sheet keys (no "#frame" suffix) to avoid duplicates
            if (key.contains("#")) continue;
            if (entry.getValue() == null) {
                missing++;
                missingList.add(key);
            } else {
                loaded++;
                loadedList.add(key);
            }
        }
        Collections.sort(missingList);
        Collections.sort(loadedList);
        Log.d(TAG, "=== SPRITE LOAD SUMMARY (after first draw) ===");
        Log.d(TAG, "  Sheets loaded OK : " + loaded);
        Log.d(TAG, "  Sheets MISSING   : " + missing);
        if (!missingList.isEmpty()) {
            Log.w(TAG, "--- Missing PNG sheets (" + missingList.size() + ") ---");
            for (String s : missingList) {
                Log.w(TAG, "  MISSING: " + s);
            }
        }
        if (!loadedList.isEmpty()) {
            Log.d(TAG, "--- Loaded PNG sheets (" + loadedList.size() + ") ---");
            for (String s : loadedList) {
                Log.d(TAG, "  loaded: " + s);
            }
        }
        Log.d(TAG, "=== END SPRITE LOAD SUMMARY ===");
    }

    private void drawEntity(Canvas canvas, LevelEntity entity) {
        if (entity == null) return;

        // Resolve NPC-specific sprites at runtime for spawn entities.
        if ("character_spawn".equalsIgnoreCase(entity.entityName) && !entity.isNPCResolved) {
            String npcFile = entity.customData.get("fileName");
            if (npcFile == null) npcFile = entity.customData.get("type");
            
            android.util.Log.d("LevelRenderView", "Resolving NPC for id=" + entity.id + " file=" + npcFile);

            if (npcFile != null && !npcFile.trim().isEmpty()) {
                String oldSprite = entity.spriteFile;
                
                if (npcFile.endsWith(".character")) {
                    LevelParser.parseCharacterFile(getContext(), entity, npcFile);
                } else if (npcFile.endsWith(".ent")) {
                    LevelParser.parseEntFile(getContext(), entity, npcFile);
                } else {
                    // Try character first, then ent if no extension
                    LevelParser.parseCharacterFile(getContext(), entity, npcFile);
                    if (entity.spriteFile.isEmpty() || entity.spriteFile.equals(oldSprite)) {
                        LevelParser.parseEntFile(getContext(), entity, npcFile);
                    }
                }
                
                if (!entity.spriteFile.equals(oldSprite) || !entity.hairSprite.isEmpty() || !entity.armorSprite.isEmpty() || !entity.weaponSprite.isEmpty()) {
                    android.util.Log.d("LevelRenderView", "Successfully resolved " + npcFile + " to sprite " + entity.spriteFile);
                    // Reset frame to 0 since numbers use spriteFrame for team ID
                    entity.spriteFrame = 0;
                    // Reset scale to 1.0; character_spawn uses 4.0 for the mario_sheet numbers
                    // but actual NPC sprites should be rendered at unit scale.
                    entity.scaleX = 1.0f;
                    entity.scaleY = 1.0f;
                } else {
                    android.util.Log.w("LevelRenderView", "Failed to resolve sprite for " + npcFile);
                }
            }
            entity.isNPCResolved = true;
        }

        // Text entities: drawn in world space so they scale with zoom
        if (!entity.displayText.isEmpty()) {
            drawTextEntity(canvas, entity);
            return;
        }

        // Composited Characters (from .character files)
        boolean hasExtraLayers = !entity.hairSprite.isEmpty() || !entity.armorSprite.isEmpty() || !entity.weaponSprite.isEmpty() || entity.bodyColor != -1;
        if (hasExtraLayers) {
            drawCompositedCharacter(canvas, entity);
            return;
        }

        // Secret areas handling: always hide if not unlocked
        if (entity.entityName.toLowerCase().contains("secret")) {
            if (!secretsUnlocked) return;
            // If unlocked, draw it regardless of logic toggle (to highlight it)
            drawEntityFallback(canvas, entity);
            return;
        }

        if (entity.spriteFile.isEmpty()) {
            // No sprite — either a pure runtime FX emitter (skip silently) or a
            // game-logic object worth showing as a placeholder overlay.
            if (!showLogicEntities || !isLogicEntity(entity)) return;
            drawEntityFallback(canvas, entity);
            return;
        }

        // Hide utility/logic sprites if toggle is off
        if (!showLogicEntities && isUtilitySprite(entity.spriteFile)) {
            return;
        }

        Bitmap frame = loadSpriteFrame(entity.spriteFile, entity.spriteFrame, entity.spriteCutX, entity.spriteCutY);

        if (frame != null) {
            float sx, sy;
            if (entity.lightHaloSize > 0f && isPureHaloSprite(entity.spriteFile)) {
                if (!showLogicEntities) return; // Hide wheel/halo sprites if logic entities are hidden
                // Scale uniformly so the halo sprite fills lightHaloSize world units.
                float uniform = entity.lightHaloSize / Math.max(frame.getWidth(), frame.getHeight());
                sx = uniform;
                sy = uniform;
            } else {
                // For regular sprites, scaleX/scaleY from the level file are multipliers
                // for the actual pixel dimensions of the sprite frame.
                sx = entity.scaleX;
                sy = entity.scaleY;
            }
            Matrix m = new Matrix();
            float cx = frame.getWidth() / 2f;
            float cy = frame.getHeight() / 2f;
            m.postTranslate(-cx, -cy);
            m.postScale(sx, sy);
            m.postRotate(entity.angle);
            m.postTranslate(entity.x, entity.y);
            Paint paint = resolvePaint(entity);
            canvas.drawBitmap(frame, m, paint);
        } else {
            if (showLogicEntities) drawEntityFallback(canvas, entity);
        }
    }

    private void drawCompositedCharacter(Canvas canvas, LevelEntity entity) {
        // Layer order: Weapon (on back) -> Body -> Armor -> Hair
        String[] layerNames = { "Weapon", "Body", "Armor", "Hair" };
        String[] layers = { entity.weaponSprite, entity.spriteFile, entity.armorSprite, entity.hairSprite };
        
        for (int i = 0; i < layers.length; i++) {
            String sprite = layers[i];
            if (sprite == null || sprite.isEmpty() || "none".equalsIgnoreCase(sprite)) continue;
            
            // Resolve filenames for layers
            String resolvedSprite = sprite;
            int cutX = 4, cutY = 2; // Default for body parts

            if (i == 0) { // Weapon
                if (!sprite.startsWith("weapon_") && !sprite.endsWith(".png")) resolvedSprite = "weapon_" + sprite;
                cutX = 1; cutY = 1; // Weapons are usually single frames
            } else if (i == 2) { // Armor
                if (!sprite.startsWith("armor_") && !sprite.endsWith(".png")) resolvedSprite = "armor_" + sprite;
            } else if (i == 3) { // Hair (Mapping via .enml)
                resolvedSprite = LevelParser.resolveHeadSprite(sprite);
            }

            Bitmap frame = loadSpriteFrame(resolvedSprite, entity.spriteFrame, cutX, cutY);
            
            // Fallback for weapon/armor prefixes
            if (frame == null && (i == 0 || i == 2) && !resolvedSprite.equals(sprite)) {
                frame = loadSpriteFrame(sprite, entity.spriteFrame, cutX, cutY);
            }

            if (frame == null) {
                Log.w(TAG, "COMPOSITE FAIL: " + layerNames[i] + " (" + sprite + ") could not be loaded as " + resolvedSprite);
                continue;
            }

            Matrix m = new Matrix();
            float cx = frame.getWidth() / 2f;
            float cy = frame.getHeight() / 2f;
            
            float tx = entity.x;
            float ty = entity.y;
            float rotation = entity.angle;

            if (i == 0) { // Weapon specific positioning
                if (entity.weaponOffsetX != 0 || entity.weaponOffsetY != 0) {
                    tx += entity.weaponOffsetX;
                    ty += entity.weaponOffsetY;
                    rotation += entity.weaponAngle;
                } else {
                    // Larger default offset if none in file
                    tx -= 25f; 
                    ty -= 10f;
                    rotation += 35f;
                }
            }

            m.postTranslate(-cx, -cy);
            m.postScale(entity.scaleX, entity.scaleY);
            m.postRotate(rotation);
            m.postTranslate(tx, ty);

            Paint paint = null;
            if (i == 1 && entity.bodyColor != -1) { // Body is layer 1 now
                int color = (int)entity.bodyColor;
                if ((color & 0xFF000000) == 0) color |= 0xFF000000;
                paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            } else {
                paint = resolvePaint(entity);
            }

            canvas.drawBitmap(frame, m, paint);
        }
    }

    /**
     * Returns true for spriteless entities that represent game-logic objects
     * worth visualising as a placeholder (walls, triggers, spawns, markers).
     * Returns false for pure runtime FX (particle emitters, volumetric fog)
     * that have no useful static representation.
     */
    private boolean isLogicEntity(LevelEntity entity) {
        String name = entity.entityName.toLowerCase();
        // Pure FX — skip
        if (name.contains("fireflies") || name.contains("fire_sparkling")
                || name.contains("volumetric") || name.contains("fog")) return false;
        // Everything else with no sprite is a logic/trigger entity — show it
        return true;
    }

    /** Draws a text entity in world space — scales with zoom just like sprites. */
    private void drawTextEntity(Canvas canvas, LevelEntity entity) {
        String cleaned = entity.displayText.replaceAll("<[^>]+>", "");
        String[] lines = cleaned.split("\\\\n", -1);

        float textSize = 26f; // world units
        float lineH = textSize * 1.4f;
        float pad = textSize * 0.5f;

        textPaint.setTextSize(textSize);
        float maxW = 0;
        for (String line : lines) maxW = Math.max(maxW, textPaint.measureText(line));

        float boxW = maxW + pad * 2f;
        float boxH = lineH * lines.length + pad;

        fillPaint.setColor(Color.argb(210, 15, 15, 40));
        scratchRect.set(entity.x - boxW / 2f, entity.y - boxH,
                entity.x + boxW / 2f, entity.y + pad * 0.5f);
        canvas.drawRoundRect(scratchRect, pad, pad, fillPaint);

        textPaint.setColor(Color.argb(255, 230, 230, 180));
        for (int i = 0; i < lines.length; i++) {
            float ty = entity.y - (lines.length - 1 - i) * lineH - pad * 0.3f;
            canvas.drawText(lines[i], entity.x, ty, textPaint);
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

            if (renderedW >= MIN_LABEL_BOX_PX) {
                String label = shortLabel(entity.entityName);
                float labelSize = Math.max(6f, hh * 0.45f);
                textPaint.setTextSize(labelSize);
                textPaint.setColor(Color.WHITE);
                canvas.drawText(label, 0, labelSize * 0.4f, textPaint);
            }
        }

        canvas.restore();
    }

    private void drawScreenLabels(Canvas canvas) {
        float density  = getResources().getDisplayMetrics().density;
        float storyPx  = 14f * density;
        float labelPx  = 11f * density;
        float pad      = storyPx * 0.4f;

        for (LevelEntity entity : sortedEntities) {
            // Convert world position to screen position
            float sx = entity.x * scale + offsetX;
            float sy = entity.y * scale + offsetY;

            if (!entity.displayText.isEmpty()) {
                // Story / tip text entity — multi-line box
                String cleaned = entity.displayText.replaceAll("<[^>]+>", "");
                String[] lines = cleaned.split("\\\\n", -1);

                textPaint.setTextSize(storyPx);
                float lineH = storyPx * 1.4f;
                float maxW = 0;
                for (String line : lines) maxW = Math.max(maxW, textPaint.measureText(line));

                float boxW = maxW + pad * 2f;
                float boxH = lineH * lines.length + pad;

                fillPaint.setColor(Color.argb(210, 15, 15, 40));
                scratchRect.set(sx - boxW / 2f, sy - boxH, sx + boxW / 2f, sy + pad * 0.5f);
                canvas.drawRoundRect(scratchRect, pad, pad, fillPaint);

                textPaint.setColor(Color.argb(255, 230, 230, 180));
                for (int i = 0; i < lines.length; i++) {
                    float ty = sy - (lines.length - 1 - i) * lineH - pad * 0.3f;
                    canvas.drawText(lines[i], sx, ty, textPaint);
                }

            } else if (entity.spriteFile.isEmpty() && isLogicEntity(entity)) {
                // Special case: secret areas only show if unlocked
                if (entity.entityName.toLowerCase().contains("secret") && !secretsUnlocked) continue;
                
                if (!showLogicEntities && !entity.entityName.toLowerCase().contains("secret")) continue;
                
                // Fallback box label — only if the box is big enough on screen
                float renderedW = entity.scaleX * BASE_TILE * scale;
                if (renderedW < MIN_LABEL_BOX_PX) continue;

                String label = shortLabel(entity.entityName);
                textPaint.setTextSize(labelPx);
                textPaint.setColor(Color.WHITE);
                canvas.drawText(label, sx, sy + labelPx * 0.4f, textPaint);
            }
        }
    }

    // --------------------
    // ENTITY COLORS
    // --------------------

    private int getEntityColor(LevelEntity entity) {
        String name = entity.entityName.toLowerCase();
        String sprite = entity.spriteFile.toLowerCase();

        if (name.contains("invisible") || name.contains("wall") || name.contains("collide"))
            return Color.argb(80, 200, 200, 200);
        if (name.contains("instant_death") || name.contains("death") || name.contains("spike")
                || name.contains("lava") || name.contains("void"))
            return Color.argb(180, 220, 0, 0);
        if (name.contains("spawn") || name.contains("respawn"))
            return Color.argb(200, 0, 220, 120);
        if (name.contains("level_end") || name.contains("exit"))
            return Color.argb(220, 0, 200, 255);
        if (name.contains("secret"))
            return Color.argb(180, 200, 0, 255);
        if (name.contains("cam") || name.contains("camera"))
            return Color.argb(140, 100, 180, 255);
        if (name.contains("turnstile") || name.contains("switch") || name.contains("lever"))
            return Color.argb(200, 255, 140, 0);
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

    /**
     * Returns the Paint to use when drawing this entity's sprite.
     * Handles additive blend mode and light color tinting.
     */
    private Paint resolvePaint(LevelEntity entity) {
        // Pure halo sprites always render additive regardless of declared blendMode,
        // matching how the game engine renders light halos.
        boolean isHalo = entity.lightHaloSize > 0f && isPureHaloSprite(entity.spriteFile);
        int effectiveBlend = isHalo ? 1 : entity.blendMode;

        // Light color tint only applies to pure halo sprites — for everything else
        // (torches, chests, etc.) the <Light><Color> describes the light they cast,
        // not a tint on their own sprite.
        boolean hasTint = isHalo && !Float.isNaN(entity.lightColorR);
        if (!hasTint) {
            return (effectiveBlend == 1) ? additivePaint : null;
        }
        // Pack the color + effective blend mode into a cache key
        int r = Math.min(255, (int) (entity.lightColorR * 255));
        int g = Math.min(255, (int) (entity.lightColorG * 255));
        int b = Math.min(255, (int) (entity.lightColorB * 255));
        int key = Color.argb(effectiveBlend, r, g, b);
        Paint cached = tintPaintCache.get(key);
        if (cached != null) return cached;

        ColorMatrix cm = new ColorMatrix(new float[]{
                entity.lightColorR, 0, 0, 0, 0,
                0, entity.lightColorG, 0, 0, 0,
                0, 0, entity.lightColorB, 0, 0,
                0, 0, 0, 1, 0
        });
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColorFilter(new ColorMatrixColorFilter(cm));
        if (effectiveBlend == 1) {
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        }
        tintPaintCache.put(key, p);
        return p;
    }

    // --------------------
    // SPRITE LOADING
    // --------------------

    /**
     * Returns true for sprites that are pure utility or markers that should
     * be toggled by the showLogicEntities flag.
     */
    private boolean isUtilitySprite(String spriteFile) {
        if (spriteFile == null || spriteFile.isEmpty()) return false;
        String s = spriteFile.toLowerCase();
        return s.equals("film.png") || s.equals("waypoint_flag.png") || isPureHaloSprite(s);
    }

    /**
     * Returns true for sprites that are pure radial glow textures whose visual
     * size is defined by the entity's lightHaloSize rather than a <Scale> tag.
     * All other sprites with a <Light> block should render at their declared scale.
     */
    private boolean isPureHaloSprite(String spriteFile) {
        if (spriteFile == null) return false;
        String s = spriteFile.toLowerCase();
        return s.contains("wheel_sprite") || s.equals("hard_halo.png")
                || s.contains("hard_sparkle");
    }

    /** Sprites that have a white background baked in and need it stripped. */
    private boolean needsWhiteStripped(String sheetKey) {
        String k = sheetKey.toLowerCase();
        return k.contains("blood_decal");
    }

    /**
     * Returns a new ARGB_8888 bitmap where pixels brighter than a threshold on
     * all three channels are made fully transparent.
     */
    private Bitmap stripWhiteBackground(Bitmap src) {
        Bitmap out = src.copy(Bitmap.Config.ARGB_8888, true);
        int w = out.getWidth(), h = out.getHeight();
        int[] pixels = new int[w * h];
        out.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < pixels.length; i++) {
            int r = (pixels[i] >> 16) & 0xFF;
            int g = (pixels[i] >>  8) & 0xFF;
            int b =  pixels[i]        & 0xFF;
            if (r > 200 && g > 200 && b > 200) {
                pixels[i] = 0x00000000; // fully transparent
            }
        }
        out.setPixels(pixels, 0, w, 0, 0, w, h);
        return out;
    }

    /**
     * Returns the frame bitmap for a sprite, cached after first use.
     * Prefers a TextureAtlas XML for exact source coordinates; falls back to
     * SpriteCut-based uniform grid slicing.
     */
    private Bitmap loadSpriteFrame(String spriteName, int frameIndex, int spriteCutX, int spriteCutY) {
        if (spriteName == null || spriteName.trim().isEmpty()) return null;

        String sheetKey = spriteName.endsWith(".png") ? spriteName : spriteName + ".png";
        String frameKey = sheetKey + "#" + frameIndex + "c" + spriteCutX + "x" + spriteCutY;

        if (spriteCache.containsKey(frameKey)) return spriteCache.get(frameKey);

        // Load full sheet if not already cached
        if (!spriteCache.containsKey(sheetKey)) {
            try {
                InputStream is = getContext().getAssets().open("entities/" + sheetKey);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                if (needsWhiteStripped(sheetKey)) {
                    bmp = stripWhiteBackground(bmp);
                }
                spriteCache.put(sheetKey, bmp);
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

        // --- Atlas path ---
        List<AtlasSprite> atlas = loadAtlas(sheetKey);
        if (atlas != null && !atlas.isEmpty()) {
            int idx = Math.min(Math.max(frameIndex, 0), atlas.size() - 1);
            Bitmap frameBitmap = buildAtlasFrame(sheet, atlas.get(idx));
            spriteCache.put(frameKey, frameBitmap);
            return frameBitmap;
        }

        // --- Uniform grid fallback (SpriteCut) ---
        // If no SpriteCut is defined, treat the entire image as a single frame.
        // Entities with multiple frames always declare SpriteCut in their .ent file.
        int numCols, numRows;
        if (spriteCutX > 0) {
            numCols = spriteCutX;
            numRows = (spriteCutY > 0) ? spriteCutY : 1;
        } else {
            numCols = 1;
            numRows = 1;
        }
        int frameWidth  = sheet.getWidth()  / numCols;
        int frameHeight = sheet.getHeight() / numRows;
        int totalFrames  = numCols * numRows;
        int clampedFrame = Math.min(Math.max(frameIndex, 0), totalFrames - 1);
        int col = clampedFrame % numCols;
        int row = clampedFrame / numCols;

        Bitmap frameBitmap = (totalFrames > 1)
                ? Bitmap.createBitmap(sheet, col * frameWidth, row * frameHeight, frameWidth, frameHeight)
                : sheet;

        spriteCache.put(frameKey, frameBitmap);
        return frameBitmap;
    }

    /**
     * Loads and caches the TextureAtlas XML for a sprite sheet.
     * Returns null if no XML exists (normal for non-packed sheets).
     */
    private List<AtlasSprite> loadAtlas(String sheetKey) {
        if (atlasCache.containsKey(sheetKey)) return atlasCache.get(sheetKey);

        String xmlName = sheetKey.substring(0, sheetKey.length() - 4) + ".xml"; // replace .png
        try (InputStream is = getContext().getAssets().open("entities/" + xmlName)) {
            List<AtlasSprite> entries = new ArrayList<>();
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
                    s.oX   = safeInt(p.getAttributeValue(null, "oX"));
                    s.oY   = safeInt(p.getAttributeValue(null, "oY"));
                    s.oW   = safeInt(p.getAttributeValue(null, "oW"));
                    s.oH   = safeInt(p.getAttributeValue(null, "oH"));
                    entries.add(s);
                }
                event = p.next();
            }
            atlasCache.put(sheetKey, entries);
            return entries;
        } catch (Exception e) {
            atlasCache.put(sheetKey, null);
            return null;
        }
    }

    /** Crops the sprite from the atlas source rect and places it into its logical output frame. */
    private Bitmap buildAtlasFrame(Bitmap sheet, AtlasSprite s) {
        // Clamp source rect to sheet bounds
        int srcX = Math.max(0, Math.min(s.srcX, sheet.getWidth()  - 1));
        int srcY = Math.max(0, Math.min(s.srcY, sheet.getHeight() - 1));
        int srcW = Math.min(s.srcW, sheet.getWidth()  - srcX);
        int srcH = Math.min(s.srcH, sheet.getHeight() - srcY);
        if (srcW <= 0 || srcH <= 0) return null;

        Bitmap crop = Bitmap.createBitmap(sheet, srcX, srcY, srcW, srcH);

        // If no output frame metadata, return the raw crop
        if (s.oW <= 0 || s.oH <= 0) return crop;

        // Place crop into the logical output frame at the given offset
        Bitmap frame = Bitmap.createBitmap(s.oW, s.oH, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(frame);
        c.drawBitmap(crop, s.oX, s.oY, null);
        crop.recycle();
        return frame;
    }

    private static int safeInt(String v) {
        if (v == null) return 0;
        try { return Integer.parseInt(v.trim()); } catch (Exception e) { return 0; }
    }

    // --------------------
    // TOUCH
    // --------------------

    // Screen position where the finger first went down (for tap detection)
    private float tapStartX, tapStartY;
    // Max finger travel (in screen px) that still counts as a tap, not a pan
    private static final float TAP_SLOP_PX = 12f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);

        int action = event.getActionMasked();
        int pointerCount = event.getPointerCount();

        // Determine which pointer (if any) is leaving the screen
        int skipIndex = (action == MotionEvent.ACTION_POINTER_UP) ? event.getActionIndex() : -1;

        // Calculate focal point of remaining fingers
        float focusX = 0f;
        float focusY = 0f;
        int count = 0;
        for (int i = 0; i < pointerCount; i++) {
            if (i == skipIndex) continue;
            focusX += event.getX(i);
            focusY += event.getY(i);
            count++;
        }

        if (count > 0) {
            focusX /= count;
            focusY /= count;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = focusX;
                lastTouchY = focusY;
                tapStartX  = focusX;
                tapStartY  = focusY;
                isPanning = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // Update lastTouch to the focal point of the fingers that WILL remain
                // on screen. This prevents the jump when transitioning between 2 and 1 fingers.
                lastTouchX = focusX;
                lastTouchY = focusY;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isPanning) {
                    offsetX += focusX - lastTouchX;
                    offsetY += focusY - lastTouchY;
                    lastTouchX = focusX;
                    lastTouchY = focusY;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                isPanning = false;
                float dx = focusX - tapStartX;
                float dy = focusY - tapStartY;
                if (!scaleDetector.isInProgress()
                        && dx * dx + dy * dy <= TAP_SLOP_PX * TAP_SLOP_PX) {
                    handleTap(focusX, focusY);
                }
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

    private void handleTap(float screenX, float screenY) {
        // Convert screen → world coordinates
        float worldX = (screenX - offsetX) / scale;
        float worldY = (screenY - offsetY) / scale;

        // Collect all entities whose bounding box contains the tap point.
        // Use the same half-extents as drawEntityFallback so the hit area
        // matches what the user actually sees.
        List<LevelEntity> hits = new ArrayList<>();
        for (LevelEntity e : sortedEntities) {
            float hw = Math.max(e.scaleX * BASE_TILE / 2f, LevelEntity.HIT_RADIUS);
            float hh = Math.max(e.scaleY * BASE_TILE / 2f, LevelEntity.HIT_RADIUS);
            float lx = worldX - e.x;
            float ly = worldY - e.y;
            if (lx >= -hw && lx <= hw && ly >= -hh && ly <= hh) {
                hits.add(e);
            }
        }

        if (hits.isEmpty()) {
            Log.d(TAG, "TAP (world " + (int) worldX + ", " + (int) worldY + ") — no entity");
            return;
        }

        // Sort hits: rendered-on-top last in draw order = highest z → log that one first,
        // then list the others so the user knows what's stacked here.
        hits.sort((a, b) -> Float.compare(b.z, a.z));

        Log.d(TAG, "=== TAP at world (" + (int) worldX + ", " + (int) worldY + ") — "
                + hits.size() + " hit(s) ===");
        for (int i = 0; i < hits.size(); i++) {
            LevelEntity e = hits.get(i);
            Log.d(TAG, "  [" + i + "] id=" + e.id
                    + "  name='" + e.entityName + "'"
                    + "  sprite='" + e.spriteFile + "'"
                    + "  pos=(" + e.x + ", " + e.y + ", z=" + e.z + ")"
                    + "  scale=(" + e.scaleX + ", " + e.scaleY + ")"
                    + "  angle=" + e.angle
                    + "  blendMode=" + e.blendMode
                    + "  frame=" + e.spriteFrame);
        }
        Log.d(TAG, "=== END TAP ===");
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
