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

    public boolean flipX = false;
    public boolean flipY = false;

    // Parallax intensity from the .ent file's Entity tag (parallaxIntensity="N").
    // 1.0 = full z-based y-offset (Ethanon's default when the attribute is absent).
    // 0.0 = entity stays at its raw world y regardless of z and scene parallax.
    public float parallaxIntensity = 1.0f;

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

    // Diffuse color from <DiffuseColor r g b> in the .ent file — used to tint the sprite.
    // NaN means no tint (treat as white / 1,1,1).
    public float diffuseColorR = Float.NaN;
    public float diffuseColorG = Float.NaN;
    public float diffuseColorB = Float.NaN;

    // Custom Data storage for generic variables.
    public Map<String, String> customData = new HashMap<>();

    // Runtime resolution helper
    public boolean isNPCResolved = false;

    // Uniform scale from the .character file (e.g. 0.7 for small spider, 1.2 for gigaspider).
    // Applied after NPC resolution; defaults to 1.0 if not set.
    public float characterScale = 1.0f;

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

    // Editor bookkeeping (NOT parsed from / written to the file directly): document-order ordinal
    // among the level's ORIGINAL entities (-1 = added during editing). For duplicates, sourceOrdinal
    // is the ordinal of the entity it was copied from, so the saver can clone the right source node.
    public int editOrdinal = -1;
    public int sourceOrdinal = -1;
    // True once the user edits this entity's scale in the inspector. Gates writing <Scale> on save:
    // the renderer mutates scaleX/scaleY for resolved NPC spawns, so we must NOT persist that drift.
    public boolean scaleEdited = false;
    // True once the user edits this entity's CustomData. Gates writing <Value> changes on save.
    public boolean customDataEdited = false;

    public static final float HIT_RADIUS = 20f;

    public boolean hitTest(float worldX, float worldY) {
        float dx = worldX - x;
        float dy = worldY - y;
        return dx * dx + dy * dy <= HIT_RADIUS * HIT_RADIUS;
    }

    /** Deep copy of this entity (fresh CustomData map) — used by the editor's duplicate action. */
    public LevelEntity copy() {
        LevelEntity c = new LevelEntity();
        c.id = id; c.spriteFrame = spriteFrame; c.blendMode = blendMode;
        c.entityName = entityName; c.spriteFile = spriteFile;
        c.x = x; c.y = y; c.z = z; c.angle = angle;
        c.scaleX = scaleX; c.scaleY = scaleY;
        c.flipX = flipX; c.flipY = flipY;
        c.parallaxIntensity = parallaxIntensity;
        c.spriteCutX = spriteCutX; c.spriteCutY = spriteCutY;
        c.lightHaloSize = lightHaloSize;
        c.lightColorR = lightColorR; c.lightColorG = lightColorG; c.lightColorB = lightColorB;
        c.diffuseColorR = diffuseColorR; c.diffuseColorG = diffuseColorG; c.diffuseColorB = diffuseColorB;
        c.customData = new HashMap<>(customData);
        c.isNPCResolved = isNPCResolved;
        c.characterScale = characterScale;
        c.bodyColor = bodyColor;
        c.hairSprite = hairSprite; c.armorSprite = armorSprite; c.weaponSprite = weaponSprite;
        c.weaponOffsetX = weaponOffsetX; c.weaponOffsetY = weaponOffsetY; c.weaponAngle = weaponAngle;
        c.displayText = displayText;
        return c;
    }
}
