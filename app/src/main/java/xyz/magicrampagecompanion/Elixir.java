package xyz.magicrampagecompanion;

import android.os.Parcel;
import android.os.Parcelable;

public class Elixir implements Parcelable {
    private final String name;
    private final String description;
    private final int imageResId;
    private final ElixirType type;

    public Elixir(String name, String description, int imageResId, ElixirType type) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.type = (type != null) ? type : ElixirType.UNKNOWN;
    }

    protected Elixir(Parcel in) {
        name = in.readString();
        description = in.readString();
        imageResId = in.readInt();
        String t = in.readString();
        ElixirType parsed = ElixirType.UNKNOWN;
        try { parsed = ElixirType.valueOf(t); } catch (Exception ignored) {}
        type = parsed;
    }

    public static final Creator<Elixir> CREATOR = new Creator<Elixir>() {
        @Override public Elixir createFromParcel(Parcel in) { return new Elixir(in); }
        @Override public Elixir[] newArray(int size) { return new Elixir[size]; }
    };

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public ElixirType getType() { return type; }

    // ---- Live-bound bonuses pulled from ItemData each time ----
    public int getDamageBonus() {
        switch (type) {
            case MONSTERS_JUICE:           return ItemData.monstersJuiceDamageBonus;
            case STARLIGHT_SUPERTONIC:     return ItemData.starlightSupertonicDamageBonus;
            default:                       return 0;
        }
    }

    public int getArmorBonus() {
        switch (type) {
            case ARCANE_PRECISION_TONIC:   return ItemData.precisionTonicArmorBonus;
            case ELIXIR_OF_DUPLICATION:   return ItemData.elixirOfDuplicationArmorBonus;
            case MONSTERS_JUICE:           return ItemData.monstersJuiceArmorBonus;
            case PEPPER_BREW:              return ItemData.pepperBrewArmorBonus;
            case TONIC_OF_INVULNERABILITY: return ItemData.tonicOfInvulnerabilityArmorBonus;
            default:                       return 0;
        }
    }

    public int getSpeedBoost() {
        switch (type) {
            case ELIXIR_OF_DUPLICATION:    return ItemData.elixirOfDuplicationSpeedBonus;
            default:                       return 0;
        }
    }

    @Override public int describeContents() { return 0; }
    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(imageResId);
        dest.writeString(type.name());
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Elixir)) return false;
        Elixir e = (Elixir) o;
        return type == e.type;
    }

    @Override public int hashCode() { return type.hashCode(); }
}
