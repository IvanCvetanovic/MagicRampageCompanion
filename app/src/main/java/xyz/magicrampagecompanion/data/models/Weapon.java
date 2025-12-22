package xyz.magicrampagecompanion.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

import xyz.magicrampagecompanion.enums.Elements;
import xyz.magicrampagecompanion.enums.WeaponTypes;

public class Weapon implements Parcelable {
    private String name;
    private WeaponTypes type;
    private Elements element;
    private int minDamage;
    private int maxDamage;
    private int upgrades;
    private double armorBonus;
    private int speed;
    private int jump;
    private int imageResId;
    private int attackCooldown;
    private int pierceCount;
    private boolean enablePierceAreaDamage;
    private boolean persistAgainstProjectile;
    private boolean isPoisonous;
    private boolean isFrost;

    private int freemiumGoldPrice;
    private int premiumGoldPrice;
    private int freemiumCoinPrice;
    private int premiumCoinPrice;
    private int baseFreemiumSellPrice;
    private int basePremiumSellPrice;

    private List<String> obtainability;

    public Weapon(String name, WeaponTypes type, Elements element, int minDamage, int maxDamage, int upgrades,
                  double armorBonus, int speed, int jump, int imageResId,
                  int attackCooldown, int pierceCount, boolean enablePierceAreaDamage,
                  boolean persistAgainstProjectile, boolean isPoisonous, boolean isFrost,
                  int freemiumGoldPrice, int premiumGoldPrice, int freemiumCoinPrice, int premiumCoinPrice,
                  int baseFreemiumSellPrice, int basePremiumSellPrice,
                  List<String> obtainability) {

        this.name = name;
        this.type = type;
        this.element = element;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.upgrades = upgrades;
        this.armorBonus = armorBonus;
        this.speed = speed;
        this.jump = jump;
        this.imageResId = imageResId;
        this.attackCooldown = attackCooldown;
        this.pierceCount = pierceCount;
        this.enablePierceAreaDamage = enablePierceAreaDamage;
        this.persistAgainstProjectile = persistAgainstProjectile;
        this.isPoisonous = isPoisonous;
        this.isFrost = isFrost;

        this.freemiumGoldPrice = freemiumGoldPrice;
        this.premiumGoldPrice = premiumGoldPrice;
        this.freemiumCoinPrice = freemiumCoinPrice;
        this.premiumCoinPrice = premiumCoinPrice;
        this.baseFreemiumSellPrice = baseFreemiumSellPrice;
        this.basePremiumSellPrice = basePremiumSellPrice;

        this.obtainability = obtainability;
    }

    public String getName() { return name; }
    public WeaponTypes getType() { return type; }
    public Elements getElement() { return element; }
    public int getMinDamage() { return minDamage; }
    public int getMaxDamage() { return maxDamage; }
    public int getUpgrades() { return upgrades; }
    public double getArmorBonus() { return armorBonus; }
    public int getSpeed() { return speed; }
    public int getJump() { return jump; }
    public int getImageResId() { return imageResId; }
    public int getAttackCooldown() { return attackCooldown; }
    public int getPierceCount() { return pierceCount; }
    public boolean isEnablePierceAreaDamage() { return enablePierceAreaDamage; }
    public boolean isPersistAgainstProjectile() { return persistAgainstProjectile; }
    public boolean isPoisonous() { return isPoisonous; }
    public boolean isFrost() { return isFrost; }

    public int getFreemiumGoldPrice() { return freemiumGoldPrice; }
    public int getPremiumGoldPrice() { return premiumGoldPrice; }
    public int getFreemiumCoinPrice() { return freemiumCoinPrice; }
    public int getPremiumCoinPrice() { return premiumCoinPrice; }
    public int getBaseFreemiumSellPrice() { return baseFreemiumSellPrice; }
    public int getBasePremiumSellPrice() { return basePremiumSellPrice; }

    public void setFreemiumGoldPrice(int value) { this.freemiumGoldPrice = value; }
    public void setPremiumGoldPrice(int value) { this.premiumGoldPrice = value; }
    public void setFreemiumCoinPrice(int value) { this.freemiumCoinPrice = value; }
    public void setPremiumCoinPrice(int value) { this.premiumCoinPrice = value; }
    public void setBaseFreemiumSellPrice(int value) { this.baseFreemiumSellPrice = value; }
    public void setBasePremiumSellPrice(int value) { this.basePremiumSellPrice = value; }


    public List<String> getObtainability() { return obtainability; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(String.valueOf(type));
        dest.writeString(String.valueOf(element));
        dest.writeInt(minDamage);
        dest.writeInt(maxDamage);
        dest.writeInt(upgrades);
        dest.writeDouble(armorBonus);
        dest.writeInt(speed);
        dest.writeInt(jump);
        dest.writeInt(imageResId);
        dest.writeInt(attackCooldown);
        dest.writeInt(pierceCount);
        dest.writeByte((byte) (enablePierceAreaDamage ? 1 : 0));
        dest.writeByte((byte) (persistAgainstProjectile ? 1 : 0));
        dest.writeByte((byte) (isPoisonous ? 1 : 0));
        dest.writeByte((byte) (isFrost ? 1 : 0));

        dest.writeInt(freemiumGoldPrice);
        dest.writeInt(premiumGoldPrice);
        dest.writeInt(freemiumCoinPrice);
        dest.writeInt(premiumCoinPrice);
        dest.writeInt(baseFreemiumSellPrice);
        dest.writeInt(basePremiumSellPrice);

        dest.writeStringList(obtainability);
    }

    protected Weapon(Parcel in) {
        name = in.readString();
        type = WeaponTypes.valueOf(in.readString());
        element = Elements.valueOf(in.readString());
        minDamage = in.readInt();
        maxDamage = in.readInt();
        upgrades = in.readInt();
        armorBonus = in.readDouble();
        speed = in.readInt();
        jump = in.readInt();
        imageResId = in.readInt();
        attackCooldown = in.readInt();
        pierceCount = in.readInt();
        enablePierceAreaDamage = in.readByte() != 0;
        persistAgainstProjectile = in.readByte() != 0;
        isPoisonous = in.readByte() != 0;
        isFrost = in.readByte() != 0;

        freemiumGoldPrice = in.readInt();
        premiumGoldPrice = in.readInt();
        freemiumCoinPrice = in.readInt();
        premiumCoinPrice = in.readInt();
        baseFreemiumSellPrice = in.readInt();
        basePremiumSellPrice = in.readInt();

        obtainability = in.createStringArrayList();
    }

    public static final Creator<Weapon> CREATOR = new Creator<Weapon>() {
        @Override
        public Weapon createFromParcel(Parcel in) { return new Weapon(in); }
        @Override
        public Weapon[] newArray(int size) { return new Weapon[size]; }
    };

    public int getId() {
        return (getClass().getSimpleName() + ":" + name).hashCode();
    }
}
