package xyz.magicrampagecompanion.ui.levelviewer;

public class LevelEntity {
    public int id = -1;
    public int spriteFrame = 0;

    public String entityName = "";
    public String spriteFile = "";

    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    public float angle = 0f;

    public float scaleX = 1f;
    public float scaleY = 1f;

    // Editor helpers
    public static final float HIT_RADIUS = 20f;

    public boolean hitTest(float worldX, float worldY) {
        float dx = worldX - x;
        float dy = worldY - y;
        return dx * dx + dy * dy <= HIT_RADIUS * HIT_RADIUS;
    }
}
