package xyz.magicrampagecompanion.data.models;

import java.util.List;

public class Achievement {

    public enum AchievementCategory { RAMPAGE, NORMAL, NOT_RELEASED }

    private final String name;
    private final String description;
    private final List<String> rewards;
    private final AchievementCategory category;
    private int originalIndex;

    public Achievement(String name, String description, List<String> rewards, AchievementCategory category) {
        this.name = name;
        this.description = description;
        this.rewards = rewards;
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getRewards() {
        return this.rewards;
    }

    public AchievementCategory getCategory() {
        return category;
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int index) {
        this.originalIndex = index;
    }
}
