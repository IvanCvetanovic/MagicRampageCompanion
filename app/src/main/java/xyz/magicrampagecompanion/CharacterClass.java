package xyz.magicrampagecompanion;

import android.os.Parcel;
import android.os.Parcelable;

public class CharacterClass implements Parcelable {

    private ClassNames name;
    private int armorBonus;
    private int magicBonus;
    private int swordBonus;
    private int daggerBonus;
    private int hammerBonus;
    private int axeBonus;
    private int spearBonus;
    private int staffBonus;
    private int speedBonus;
    private int jumpImpulseBonus;
    private int imageResId; // Now storing the image resource ID

    public CharacterClass(ClassNames name, int armorBonus, int magicBonus, int swordBonus, int daggerBonus, int hammerBonus,
                          int axeBonus, int spearBonus, int staffBonus, int speedBonus, int jumpImpulseBonus, int imageResId) {
        this.name = name;
        this.armorBonus = armorBonus;
        this.magicBonus = magicBonus;
        this.swordBonus = swordBonus;
        this.daggerBonus = daggerBonus;
        this.hammerBonus = hammerBonus;
        this.axeBonus = axeBonus;
        this.spearBonus = spearBonus;
        this.staffBonus = staffBonus;
        this.speedBonus = speedBonus;
        this.jumpImpulseBonus = jumpImpulseBonus;
        this.imageResId = imageResId;
    }

    public String getName(android.content.Context context) {
        if (name.equals(ClassNames.ROGUE)) {
            return context.getString(R.string.rogue);
        } else if (name.equals(ClassNames.THIEF)) {
            return context.getString(R.string.thief);
        } else if (name.equals(ClassNames.MAGE)) {
            return context.getString(R.string.mage);
        } else if (name.equals(ClassNames.BLACK_MAGE)) {
            return context.getString(R.string.black_mage);
        } else if (name.equals(ClassNames.HIGH_MAGE)) {
            return context.getString(R.string.high_mage);
        } else if (name.equals(ClassNames.PRIEST)) {
            return context.getString(R.string.priest);
        } else if (name.equals(ClassNames.WARLOCK)) {
            return context.getString(R.string.warlock);
        } else if (name.equals(ClassNames.WARRIOR)) {
            return context.getString(R.string.warrior);
        } else if (name.equals(ClassNames.ELITE_WARRIOR)) {
            return context.getString(R.string.elite_warrior);
        } else if (name.equals(ClassNames.PALADIN)) {
            return context.getString(R.string.paladin);
        } else if (name.equals(ClassNames.RANGER)) {
            return context.getString(R.string.ranger);
        } else if (name.equals(ClassNames.WITCHDOCTOR)) {
            return context.getString(R.string.witchdoctor);
        } else if (name.equals(ClassNames.DRUID)) {
            return context.getString(R.string.druid);
        }
        return null;
    }

    public int getArmorBonus() {
        return armorBonus;
    }

    public int getMagicBonus() {
        return magicBonus;
    }

    public int getSwordBonus() {
        return swordBonus;
    }

    public int getDaggerBonus() {
        return daggerBonus;
    }

    public int getHammerBonus() {
        return hammerBonus;
    }

    public int getAxeBonus() {
        return axeBonus;
    }

    public int getSpearBonus() {
        return spearBonus;
    }

    public int getStaffBonus() {
        return staffBonus;
    }

    public int getSpeedBonus() {
        return speedBonus;
    }

    public int getJumpImpulseBonus() {
        return jumpImpulseBonus;
    }

    public int getImageResId() {
        return imageResId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(String.valueOf(name));
        dest.writeInt(armorBonus);
        dest.writeInt(magicBonus);
        dest.writeInt(swordBonus);
        dest.writeInt(daggerBonus);
        dest.writeInt(hammerBonus);
        dest.writeInt(axeBonus);
        dest.writeInt(spearBonus);
        dest.writeInt(staffBonus);
        dest.writeInt(speedBonus);
        dest.writeInt(jumpImpulseBonus);
        dest.writeInt(imageResId); // Write the resource ID
    }

    protected CharacterClass(Parcel in) {
        name = ClassNames.valueOf(in.readString());
        armorBonus = in.readInt();
        magicBonus = in.readInt();
        swordBonus = in.readInt();
        daggerBonus = in.readInt();
        hammerBonus = in.readInt();
        axeBonus = in.readInt();
        spearBonus = in.readInt();
        staffBonus = in.readInt();
        speedBonus = in.readInt();
        jumpImpulseBonus = in.readInt();
        imageResId = in.readInt(); // Read the resource ID
    }

    public static final Creator<CharacterClass> CREATOR = new Creator<CharacterClass>() {
        @Override
        public CharacterClass createFromParcel(Parcel in) {
            return new CharacterClass(in);
        }

        @Override
        public CharacterClass[] newArray(int size) {
            return new CharacterClass[size];
        }
    };
}
