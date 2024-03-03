package xyz.magicrampagecompanion;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Armor implements Parcelable {
    private String name;
    private Elements element;
    private boolean frostImmune;
    private int minArmor;
    private int maxArmor;
    private int upgrades;
    private int speed;
    private int jump;
    private int magic;
    private int sword;
    private int staff;
    private int dagger;
    private int axe;
    private int hammer;
    private int spear;
    private Bitmap picture;

    public Armor(String name, Elements element, boolean frostImmune, int minArmor, int maxArmor, int upgrades, int speed, int jump, int magic,
                 int sword, int staff, int dagger, int axe, int hammer, int spear, Bitmap picture) {
        this.name = name;
        this.element = element;
        this.frostImmune = frostImmune;
        this.minArmor = minArmor;
        this.maxArmor = maxArmor;
        this.upgrades = upgrades;
        this.speed = speed;
        this.jump = jump;
        this.magic = magic;
        this.sword = sword;
        this.staff = staff;
        this.dagger = dagger;
        this.axe = axe;
        this.hammer = hammer;
        this.spear = spear;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public Elements getElement() {
        return element;
    }

    public void setElement(Elements element) { this.element = element; }

    public boolean isFrostImmune() {
        return frostImmune;
    }

    public int getMinArmor() { return minArmor; }

    public int getMaxArmor() { return maxArmor;}

    public int getUpgrades() { return upgrades; }

    public int getSpeed() {
        return speed;
    }

    public int getJump() {
        return jump;
    }

    public double getMagic() {
        return magic;
    }

    public double getSword() {
        return sword;
    }

    public double getStaff() {
        return staff;
    }

    public double getDagger() {
        return dagger;
    }

    public double getAxe() {
        return axe;
    }

    public double getHammer() {
        return hammer;
    }

    public double getSpear() {
        return spear;
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
        dest.writeString(String.valueOf(element));
        dest.writeByte((byte) (frostImmune ? 1 : 0));
        dest.writeInt(minArmor);
        dest.writeInt(maxArmor);
        dest.writeInt(upgrades);
        dest.writeInt(speed);
        dest.writeInt(jump);
        dest.writeInt(magic);
        dest.writeInt(sword);
        dest.writeInt(staff);
        dest.writeInt(dagger);
        dest.writeInt(axe);
        dest.writeInt(hammer);
        dest.writeInt(spear);
        dest.writeParcelable(picture, flags);
    }

    protected Armor(Parcel in) {
        name = in.readString();
        element = Elements.valueOf(in.readString());
        frostImmune = in.readByte() != 0;
        minArmor = in.readInt();
        maxArmor = in.readInt();
        upgrades = in.readInt();
        speed = in.readInt();
        jump = in.readInt();
        magic = in.readInt();
        sword = in.readInt();
        staff = in.readInt();
        dagger = in.readInt();
        axe = in.readInt();
        hammer = in.readInt();
        spear = in.readInt();
        picture = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Armor> CREATOR = new Creator<Armor>() {
        @Override
        public Armor createFromParcel(Parcel in) {
            return new Armor(in);
        }

        @Override
        public Armor[] newArray(int size) {
            return new Armor[size];
        }
    };
}
