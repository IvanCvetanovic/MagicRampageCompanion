package xyz.magicrampagecompanion.data.models;

public class SetStats {
    // Core numbers
    public int damage;
    public int armor;
    public int speedPct;
    public int jumpPct;
    public int pierce;
    public int attackSpeedStars; // 1..5

    // Flags shown as checkmarks
    public boolean fireRes;
    public boolean frostRes;
    public boolean spikeRes;
    public boolean projPersist;
    public boolean poisonAttack;
    public boolean frostAttack;

    // Composite rating (0..10)
    public double rating;
}
