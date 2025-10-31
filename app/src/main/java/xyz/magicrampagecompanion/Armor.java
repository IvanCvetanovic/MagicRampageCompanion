package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

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
    private int imageResId; // Now storing the image resource ID

    // NEW: pricing fields
    private int freemiumGoldPrice;
    private int premiumGoldPrice;
    private int freemiumCoinPrice;
    private int premiumCoinPrice;
    private int baseFreemiumSellPrice;
    private int basePremiumSellPrice;

    public Armor(String name, Elements element, boolean frostImmune, int minArmor, int maxArmor, int upgrades,
                 int speed, int jump, int magic, int sword, int staff, int dagger, int axe, int hammer, int spear,
                 int imageResId) {
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
        this.imageResId = imageResId;

        // default prices to 0
        this.freemiumGoldPrice = 0;
        this.premiumGoldPrice = 0;
        this.freemiumCoinPrice = 0;
        this.premiumCoinPrice = 0;
        this.baseFreemiumSellPrice = 0;
        this.basePremiumSellPrice = 0;
    }

    // NEW: overloaded constructor with prices (order: FG, PG, FC, PC, SellF, SellP)
    public Armor(String name, Elements element, boolean frostImmune, int minArmor, int maxArmor, int upgrades,
                 int speed, int jump, int magic, int sword, int staff, int dagger, int axe, int hammer, int spear,
                 int imageResId,
                 int freemiumGoldPrice, int premiumGoldPrice, int freemiumCoinPrice, int premiumCoinPrice,
                 int baseFreemiumSellPrice, int basePremiumSellPrice) {
        this(name, element, frostImmune, minArmor, maxArmor, upgrades,
                speed, jump, magic, sword, staff, dagger, axe, hammer, spear, imageResId);
        this.freemiumGoldPrice = freemiumGoldPrice;
        this.premiumGoldPrice = premiumGoldPrice;
        this.freemiumCoinPrice = freemiumCoinPrice;
        this.premiumCoinPrice = premiumCoinPrice;
        this.baseFreemiumSellPrice = baseFreemiumSellPrice;
        this.basePremiumSellPrice = basePremiumSellPrice;
    }

    public String getName() { return name; }
    public Elements getElement() { return element; }
    public void setElement(Elements element) { this.element = element; }
    public boolean isFrostImmune() { return frostImmune; }
    public int getMinArmor() { return minArmor; }
    public int getMaxArmor() { return maxArmor; }
    public int getUpgrades() { return upgrades; }
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

    // NEW: price getters
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

    @Override
    public int describeContents() { return 0; }

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
        dest.writeInt(imageResId); // Write the resource ID

        // NEW: prices
        dest.writeInt(freemiumGoldPrice);
        dest.writeInt(premiumGoldPrice);
        dest.writeInt(freemiumCoinPrice);
        dest.writeInt(premiumCoinPrice);
        dest.writeInt(baseFreemiumSellPrice);
        dest.writeInt(basePremiumSellPrice);
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
        imageResId = in.readInt(); // Read the resource ID

        // NEW: prices
        freemiumGoldPrice = in.readInt();
        premiumGoldPrice = in.readInt();
        freemiumCoinPrice = in.readInt();
        premiumCoinPrice = in.readInt();
        baseFreemiumSellPrice = in.readInt();
        basePremiumSellPrice = in.readInt();
    }

    public static final Creator<Armor> CREATOR = new Creator<Armor>() {
        @Override
        public Armor createFromParcel(Parcel in) { return new Armor(in); }
        @Override
        public Armor[] newArray(int size) { return new Armor[size]; }
    };

    public void setUpgrades(int i) { this.upgrades = i; }
}
