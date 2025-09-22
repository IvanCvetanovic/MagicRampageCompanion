package xyz.magicrampagecompanion;

import android.os.Parcel;
import android.os.Parcelable;

public class Elixir implements Parcelable {
    private final String name;
    private final String description;
    private final int imageResId;

    // new fields
    private final int damageBonus;
    private final int armorBonus;
    private final int speedBoost;

    public Elixir(String name, String description, int imageResId,
                  int damageBonus, int armorBonus, int speedBoost) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.damageBonus = damageBonus;
        this.armorBonus = armorBonus;
        this.speedBoost = speedBoost;
    }

    protected Elixir(Parcel in) {
        name = in.readString();
        description = in.readString();
        imageResId = in.readInt();
        damageBonus = in.readInt();
        armorBonus = in.readInt();
        speedBoost = in.readInt();
    }

    public static final Creator<Elixir> CREATOR = new Creator<Elixir>() {
        @Override public Elixir createFromParcel(Parcel in) { return new Elixir(in); }
        @Override public Elixir[] newArray(int size) { return new Elixir[size]; }
    };

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }

    // new getters
    public int getDamageBonus() { return damageBonus; }
    public int getArmorBonus() { return armorBonus; }
    public int getSpeedBoost() { return speedBoost; }

    @Override public int describeContents() { return 0; }
    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(imageResId);
        dest.writeInt(damageBonus);
        dest.writeInt(armorBonus);
        dest.writeInt(speedBoost);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Elixir)) return false;
        Elixir e = (Elixir) o;
        return name != null && name.equals(e.name);
    }

    @Override public int hashCode() { return name != null ? name.hashCode() : 0; }
}
