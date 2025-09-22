package xyz.magicrampagecompanion;

public class JsonItem {
    public String  name;        // localized (for logs)
    public String  name_en;     // used for matching (case-insensitive)
    public String  element;     // "air","water","earth","fire","light","darkness","neutral" or ""

    // Common-ish
    public Integer maxLevelAllowed;

    // Weapon fields
    public Integer attackCooldown;     // ms
    public Integer damage;             // base → minDamage
    public Integer maxLevelDamage;     // → maxDamage
    public Integer pierceCount;
    public Boolean enablePierceAreaDamage;
    public Boolean persistAgainstProjectile;
    public Boolean poisonous;
    public Boolean frost;              // for weapons: frost attack; for armors: frost-immune

    // Movement/bonus multipliers (e.g., 1.10)
    public Double  speedBoost;
    public Double  jumpBoost;
    public Double  armorBoost;

    // Armor/Ring numeric stats
    public Integer armor;              // base armor → minArmor / ring.armor
    public Integer maxLevelArmor;      // → maxArmor

    // Class/weapon-type boosts (for armor/ring; multipliers)
    public Double  magicBoost;
    public Double  swordBoost;
    public Double  staffBoost;
    public Double  daggerBoost;
    public Double  axeBoost;
    public Double  hammerBoost;
    public Double  spearBoost;
}
