package xyz.magicrampagecompanion.data.models;

import androidx.annotation.DrawableRes;

/**
 * Represents a single skin entry with a name (text label) and an image resource.
 * You can later expand this with more fields (e.g., rarity, description, category).
 */
public class SkinItem {

    private final String name;
    private final @DrawableRes int imageResId;

    /**
     * Constructs a SkinItem.
     *
     * @param name       The display name of the skin.
     * @param imageResId The drawable resource ID for the skin's image.
     */
    public SkinItem(String name, @DrawableRes int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    // ---- Getters ----

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    // ---- Optional: convenience for debugging ----
    @Override
    public String toString() {
        return "SkinItem{name='" + name + "', imageResId=" + imageResId + "}";
    }
}
