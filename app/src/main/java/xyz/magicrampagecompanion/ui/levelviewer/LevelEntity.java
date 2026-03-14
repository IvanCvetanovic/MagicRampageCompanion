package xyz.magicrampagecompanion.ui.levelviewer;

import java.util.HashMap;
import java.util.Map;

public class LevelEntity {
    public int id = -1;
    public int spriteFrame = 0;
    public int blendMode = 0; // 0 = normal, 1 = additive

    public String entityName = "";
    public String spriteFile = "";

    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    public float angle = 0f;

    public float scaleX = 1f;
    public float scaleY = 1f;

    // Sprite sheet layout from <SpriteCut x="N" y="M" /> in the .ent file.
    // 0 means "not set" — fall back to aspect-ratio guess.
    public int spriteCutX = 0;
    public int spriteCutY = 0;

    // Light halo size from <Light haloSize="N"> — used to scale light sprites.
    // 0 means not a light entity; use scaleX/scaleY instead.
    public float lightHaloSize = 0f;

    // Light color from <Light><Color r g b> — used to tint the halo sprite.
    // NaN means no color set.
    public float lightColorR = Float.NaN;
    public float lightColorG = Float.NaN;
    public float lightColorB = Float.NaN;

    // Custom Data storage for generic variables.
    public Map<String, String> customData = new HashMap<>();

    // Runtime resolution helper
    public boolean isNPCResolved = false;

    // Composited Character Data (from .character files)
    public long bodyColor = -1; // -1 means no tint
    public String hairSprite = "";
    public String armorSprite = "";
    public String weaponSprite = "";
    
    // Weapon positioning
    public float weaponOffsetX = 0f;
    public float weaponOffsetY = 0f;
    public float weaponAngle = 0f;

    // Localization key from <CustomData> for "text" entities (entityName == "text").
    // Empty means this is not a text entity.
    public String displayText = "";

    public static final float HIT_RADIUS = 20f;

    public boolean hitTest(float worldX, float worldY) {
        float dx = worldX - x;
        float dy = worldY - y;
        return dx * dx + dy * dy <= HIT_RADIUS * HIT_RADIUS;
    }
}
