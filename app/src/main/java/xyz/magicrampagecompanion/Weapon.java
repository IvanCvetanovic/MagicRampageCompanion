package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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

    public Weapon(String name, WeaponTypes type, Elements element, int minDamage, int maxDamage, int upgrades,
                  double armorBonus, int speed, int jump, int imageResId,
                  int attackCooldown, int pierceCount, boolean enablePierceAreaDamage,
                  boolean persistAgainstProjectile, boolean isPoisonous, boolean isFrost) {
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
    }

    public String getName() {
        return name;
    }

    public WeaponTypes getType() {
        return type;
    }

    public Elements getElement() {
        return element;
    }

    public void setElement(Elements element) {
        this.element = element;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public int getUpgrades() {
        return upgrades;
    }

    public double getArmorBonus() {
        return armorBonus;
    }

    public int getSpeed() {
        return speed;
    }

    public int getJump() {
        return jump;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public int getPierceCount() {
        return pierceCount;
    }

    public boolean isEnablePierceAreaDamage() {
        return enablePierceAreaDamage;
    }

    public boolean isPersistAgainstProjectile() {
        return persistAgainstProjectile;
    }

    public boolean isPoisonous() {
        return isPoisonous;
    }

    public boolean isFrost() {
        return isFrost;
    }

    @Override
    public int describeContents() {
        return 0;
    }

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
    }

    public static final Creator<Weapon> CREATOR = new Creator<Weapon>() {
        @Override
        public Weapon createFromParcel(Parcel in) {
            return new Weapon(in);
        }

        @Override
        public Weapon[] newArray(int size) {
            return new Weapon[size];
        }
    };

}