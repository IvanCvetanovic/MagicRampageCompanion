package xyz.magicrampagecompanion.data.models;

import xyz.magicrampagecompanion.enums.Elements;

public class EquipmentSet {
    public Armor armor;
    public Weapon weapon;
    public Ring ring;
    public CharacterClass characterClass;

    public boolean[] skills = new boolean[36];

    public int armorUpgrades = 0;
    public int weaponUpgrades = 0;

    public Elements armorElement  = Elements.NEUTRAL;
    public Elements weaponElement = Elements.NEUTRAL;
    public Elements ringElement   = Elements.NEUTRAL;

    public Elixir elixir;
}
