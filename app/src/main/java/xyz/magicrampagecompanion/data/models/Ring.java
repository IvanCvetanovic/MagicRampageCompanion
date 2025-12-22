package xyz.magicrampagecompanion.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

import xyz.magicrampagecompanion.enums.Elements;

public class Ring implements Parcelable {
    private String name;
    private Elements element;
    private int armor;
    private double armorBonus;
    private int speed;
    private int jump;
    private int magic;
    private int sword;
    private int staff;
    private int dagger;
    private int axe;
    private int hammer;
    private int spear;
    private int imageResId;

    private int freemiumGoldPrice;
    private int premiumGoldPrice;
    private int freemiumCoinPrice;
    private int premiumCoinPrice;
    private int baseFreemiumSellPrice;
    private int basePremiumSellPrice;

    private List<String> obtainability;

    public Ring(String name, Elements element, int armor, double armorBonus, int speed, int jump, int magic,
                int sword, int staff, int dagger, int axe, int hammer, int spear, int imageResId,
                int freemiumGoldPrice, int premiumGoldPrice, int freemiumCoinPrice, int premiumCoinPrice,
                int baseFreemiumSellPrice, int basePremiumSellPrice, List<String> obtainability) {

        this.name = name;
        this.element = element;
        this.armor = armor;
        this.armorBonus = armorBonus;
        this.speed = speed;
        this.jump = jump;
        this.magic = magic;
        this.sword = sword;
        this.staff = staff;
        this.dagger = dagger;
        this.axe = axe;
        this.hammer = hammer;
        this.spear = spear;
        this.imageResId = imageResId;

        this.freemiumGoldPrice = freemiumGoldPrice;
        this.premiumGoldPrice = premiumGoldPrice;
        this.freemiumCoinPrice = freemiumCoinPrice;
        this.premiumCoinPrice = premiumCoinPrice;
        this.baseFreemiumSellPrice = baseFreemiumSellPrice;
        this.basePremiumSellPrice = basePremiumSellPrice;

        this.obtainability = obtainability;
    }

    public String getName() { return name; }
    public Elements getElement() { return element; }
    public int getArmor() { return armor; }
    public double getArmorBonus() { return armorBonus; }
    public int getSpeed() { return speed; }
    public int getJump() { return jump; }
    public double getMagic() { return magic; }
    public double getSword() { return sword; }
    public double getStaff() { return staff; }
    public double getDagger() { return dagger; }
    public double getAxe() { return axe; }
    public double getHammer() { return hammer; }
    public double getSpear() { return spear; }
    public int getImageResId() { return imageResId; }

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
        dest.writeString(String.valueOf(element));
        dest.writeInt(armor);
        dest.writeDouble(armorBonus);
        dest.writeInt(speed);
        dest.writeInt(jump);
        dest.writeInt(magic);
        dest.writeInt(sword);
        dest.writeInt(staff);
        dest.writeInt(dagger);
        dest.writeInt(axe);
        dest.writeInt(hammer);
        dest.writeInt(spear);
        dest.writeInt(imageResId);

        dest.writeInt(freemiumGoldPrice);
        dest.writeInt(premiumGoldPrice);
        dest.writeInt(freemiumCoinPrice);
        dest.writeInt(premiumCoinPrice);
        dest.writeInt(baseFreemiumSellPrice);
        dest.writeInt(basePremiumSellPrice);

        dest.writeStringList(obtainability);
    }

    protected Ring(Parcel in) {
        name = in.readString();
        element = Elements.valueOf(in.readString());
        armor = in.readInt();
        armorBonus = in.readDouble();
        speed = in.readInt();
        jump = in.readInt();
        magic = in.readInt();
        sword = in.readInt();
        staff = in.readInt();
        dagger = in.readInt();
        axe = in.readInt();
        hammer = in.readInt();
        spear = in.readInt();
        imageResId = in.readInt();

        freemiumGoldPrice = in.readInt();
        premiumGoldPrice = in.readInt();
        freemiumCoinPrice = in.readInt();
        premiumCoinPrice = in.readInt();
        baseFreemiumSellPrice = in.readInt();
        basePremiumSellPrice = in.readInt();

        obtainability = in.createStringArrayList();
    }

    public static final Creator<Ring> CREATOR = new Creator<Ring>() {
        @Override
        public Ring createFromParcel(Parcel in) { return new Ring(in); }
        @Override
        public Ring[] newArray(int size) { return new Ring[size]; }
    };

    public int getId() {
        return (getClass().getSimpleName() + ":" + name).hashCode();
    }
}
