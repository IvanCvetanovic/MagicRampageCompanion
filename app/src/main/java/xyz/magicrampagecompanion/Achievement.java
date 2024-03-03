package xyz.magicrampagecompanion;

import java.util.List;

public class Achievement {
    private final String name;
    private final String description;
    private final List<String> rewards;

    public Achievement(String name, String description, List<String> rewards) {
        this.name = name;
        this.description = description;
        this.rewards = rewards;
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
}

