package xyz.magicrampagecompanion;

import android.graphics.Bitmap;
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
    private Bitmap picture;

    public Weapon(String name, WeaponTypes type, Elements element, int minDamage, int maxDamage, int upgrades, double armorBonus, int speed, int jump, Bitmap picture) {
        this.name = name;
        this.type = type;
        this.element = element;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.upgrades = upgrades;
        this.armorBonus = armorBonus;
        this.speed = speed;
        this.jump = jump;
        this.picture = picture;
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

    public void setElement(Elements element) { this.element = element; }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() { return maxDamage; }
    public int getUpgrades() { return upgrades; }

    public double getArmorBonus() {
        return armorBonus;
    }

    public int getSpeed() {
        return speed;
    }

    public int getJump() {
        return jump;
    }

    public Bitmap getPicture() {
        return picture;
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
        dest.writeParcelable(picture, flags);
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
        picture = in.readParcelable(Bitmap.class.getClassLoader());
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
