package xyz.magicrampagecompanion;

import xyz.magicrampagecompanion.Armor;
import xyz.magicrampagecompanion.CharacterClass;
import xyz.magicrampagecompanion.Elements;
import xyz.magicrampagecompanion.Ring;
import xyz.magicrampagecompanion.Weapon;

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
