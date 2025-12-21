package xyz.magicrampagecompanion.data.models;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.enums.ClassNames;
import xyz.magicrampagecompanion.enums.Elements;
import xyz.magicrampagecompanion.enums.ElixirType;
import xyz.magicrampagecompanion.enums.WeaponTypes;

public class ItemData {
    public static final List<Weapon> staffList = new ArrayList<>();
    public static final List<Weapon> daggerList = new ArrayList<>();
    public static final List<Weapon> axeList = new ArrayList<>();
    public static final List<Weapon> spearList = new ArrayList<>();
    public static final List<Weapon> hammerList = new ArrayList<>();
    public static final List<Weapon> swordList = new ArrayList<>();
    public static final List<Armor> armorList = new ArrayList<>();
    public static final List<Ring> ringList = new ArrayList<>();
    public static final List<Enemy> enemyList = new ArrayList<>();
    public static final List<CharacterClass> classList = new ArrayList<>();
    public static final List<Elixir> elixirList = new ArrayList<>();
    public static final List<EquipmentSet> rewardSets = new ArrayList<>();
    private static String str(Context ctx, int resId) {
        return ctx.getString(resId);
    }

    public static final List<SkinItem> rangerSkins = new ArrayList<>();
    public static final List<SkinItem> priestSkins = new ArrayList<>();
    public static final List<SkinItem> warlockSkins = new ArrayList<>();
    public static final List<SkinItem> blackMageSkins = new ArrayList<>();
    public static final List<SkinItem> rogueSkins = new ArrayList<>();
    public static final List<SkinItem> thiefSkins = new ArrayList<>();
    public static final List<SkinItem> warriorSkins = new ArrayList<>();
    public static final List<SkinItem> mageSkins = new ArrayList<>();
    public static final List<SkinItem> druidSkins = new ArrayList<>();
    public static final List<SkinItem> paladinSkins = new ArrayList<>();
    public static final List<SkinItem> highMageSkins = new ArrayList<>();
    public static final List<SkinItem> eliteWarriorSkins = new ArrayList<>();
    public static final List<SkinItem> witchdoctorSkins = new ArrayList<>();

    // Skill Tree
    public static int SkillTreeDaggerBonus = 20;
    public static int SkillTreeSwordBonus  = 15;
    public static int SkillTreeStaffBonus  = 20;
    public static int SkillTreeSpearBonus  = 40;
    public static int SkillTreeHammerBonus = 60;
    public static int SkillTreeAxeBonus    = 50;
    public static int SkillTreeSpeedBonus  = 4;
    public static int SkillTreeJumpBonus   = 3;

    // Elixirs
    public static int precisionTonicArmorBonus        = -15;
    public static int elixirOfDuplicationArmorBonus = -15;
    public static int elixirOfDuplicationSpeedBonus    = 5;
    public static int monstersJuiceDamageBonus         = 60;
    public static int monstersJuiceArmorBonus          = -20;
    public static int pepperBrewArmorBonus              = 25;
    public static int starlightSupertonicDamageBonus   = 70;
    public static int tonicOfInvulnerabilityArmorBonus = 25;


    public static void init(Context context) {
        swordList.clear();
        staffList.clear();
        daggerList.clear();
        spearList.clear();
        axeList.clear();
        hammerList.clear();
        armorList.clear();
        ringList.clear();
        enemyList.clear();
        classList.clear();
        elixirList.clear();
        rewardSets.clear();
        rangerSkins.clear();
        priestSkins.clear();
        warlockSkins.clear();
        blackMageSkins.clear();
        rogueSkins.clear();
        thiefSkins.clear();
        warriorSkins.clear();
        mageSkins.clear();
        druidSkins.clear();
        paladinSkins.clear();
        highMageSkins.clear();
        eliteWarriorSkins.clear();
        witchdoctorSkins.clear();

        // Swords
        swordList.add(new Weapon(str(context, R.string.recruit_sword), WeaponTypes.SWORD, Elements.NEUTRAL, 7, 12, 3, 0, 4, 0, R.drawable.sword_recruit_fire_sword, 650, 0, true, false, false, false, 450, 200, 0, 0, 50, 100, List.of(str(context, R.string.chapter_one_random_drop))));
        swordList.add(new Weapon(str(context, R.string.fire_sword), WeaponTypes.SWORD, Elements.FIRE, 8, 13, 3, 0, 4, 0, R.drawable.sword_recruit_fire_sword, 700, 0, true, false, false, false, 550, 250, 0, 0, 61, 122, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.steel_blade), WeaponTypes.SWORD, Elements.NEUTRAL, 11, 18, 3, 15, 5, 0, R.drawable.sword_steel_blade, 650, 0, true, false, false, false, 350, 150, 0, 0, 38, 77, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.master_sword), WeaponTypes.SWORD, Elements.AIR, 14, 24, 3, 20, 4, 2, R.drawable.sword_master_sword, 650, 0, true, false, false, false, 1400, 700, 0, 0, 155, 311, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_one_random_drop))));
        swordList.add(new Weapon(str(context, R.string.vorpal_sword), WeaponTypes.SWORD, Elements.NEUTRAL, 25, 43, 3, 40, 6, 0, R.drawable.sword_vorpal_sword, 400, 0, true, false, false, false, 3400, 1700, 0, 0, 377, 755, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.pirate_sword), WeaponTypes.SWORD, Elements.NEUTRAL, 26, 44, 3, 10, 6, 4, R.drawable.sword_pirate_sword, 500, 0, true, false, false, false, 3400, 1700, 0, 0, 377, 755, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop))));
        swordList.add(new Weapon(str(context, R.string.nobel_sword), WeaponTypes.SWORD, Elements.NEUTRAL, 29, 49, 3, 30, 4, 0, R.drawable.sword_nobel_sword, 400, 0, true, false, false, false, 3900, 2000, 0, 0, 433, 866, List.of(str(context, R.string.chapter_two_random_drop))));
        swordList.add(new Weapon(str(context, R.string.bunny_sword), WeaponTypes.SWORD, Elements.EARTH, 49, 49, 1, 0, 0, 20, R.drawable.sword_bunny_sword, 500, 1, false, false, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.leaf_sword), WeaponTypes.SWORD, Elements.EARTH, 23, 72, 9, 25, 3, 2, R.drawable.sword_leaf_sword, 400, 0, true, false, false, false, 9000, 4600, 0, 0, 1000, 2000, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.magicsaber), WeaponTypes.SWORD, Elements.LIGHT, 27, 111, 13, 0, 3, 3, R.drawable.sword_magicsaber, 600, 0, true, false, false, false, 20000, 10000, 0, 0, 2222, 4444, List.of(str(context, R.string.chapter_four_random_drop))));
        swordList.add(new Weapon(str(context, R.string.darkskull_sword), WeaponTypes.SWORD, Elements.DARKNESS, 32, 124, 12, 20, 4, 0, R.drawable.sword_fireskull_darkskull_sword, 600, 0, true, false, false, false, 25000, 12500, 0, 0, 2777, 5555, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.umbranian_long_sword), WeaponTypes.SWORD, Elements.AIR, 38, 129, 10, 45, 7, 2, R.drawable.sword_umbranian_long_sword, 450, 0, true, false, false, false, 27500, 14000, 0, 0, 3055, 6111, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.umbranian_short_sword), WeaponTypes.SWORD, Elements.NEUTRAL, 32, 131, 13, 50, 7, 0, R.drawable.sword_umbranian_short_sword, 300, 0, true, false, false, false, 6000, 3000, 0, 0, 666, 1333, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.fireskull_sword), WeaponTypes.SWORD, Elements.FIRE, 36, 139, 12, 30, 6, 0, R.drawable.sword_fireskull_darkskull_sword, 500, 0, true, false, false, false, 31500, 16000, 0, 0, 3500, 7000, List.of(str(context, R.string.chapter_four_random_drop), str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.sword_of_light), WeaponTypes.SWORD, Elements.LIGHT, 40, 145, 11, 0, 5, 2, R.drawable.sword_sword_of_light, 350, 1, true, true, false, false, 155000, 80000, 0, 0, 17222, 34444, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.throat_cutter), WeaponTypes.SWORD, Elements.DARKNESS, 50, 230, 15, 0, 4, 2, R.drawable.sword_throat_cutter, 500, 0, true, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement), str(context, R.string.survival_mode_5))));
        swordList.add(new Weapon(str(context, R.string.ice_claw), WeaponTypes.SWORD, Elements.WATER, 66, 303, 15, 40, 5, 2, R.drawable.sword_ice_claw, 500, 0, true, false, false, true, 202000, 104000, 0, 0, 22444, 44888, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.moonlit_razor), WeaponTypes.SWORD, Elements.NEUTRAL, 80, 329, 13, 40, 6, 3, R.drawable.sword_moonlit_razor, 550, 2, false, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        swordList.add(new Weapon(str(context, R.string.sovereigns_onyx_sword), WeaponTypes.SWORD, Elements.EARTH, 72, 331, 15, 40, 4, 4, R.drawable.sword_sovereigns_onyx_sword, 500, 0, true, false, false, true, 0, 0, 3000, 1801, 33333, 66666, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.light_piercer), WeaponTypes.SWORD, Elements.LIGHT, 90, 414, 15, 25, 7, 5, R.drawable.sword_light_piercer, 550, 1, false, true, false, false, 874000, 449000, 3000, 1801, 33333, 66666, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.air_piercer), WeaponTypes.SWORD, Elements.AIR, 77, 354, 15, 0, 10, 3, R.drawable.sword_air_piercer, 450, 1, false, true, false, false, 900000, 463000, 0, 0, 100000, 200000, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.abyssal_blade), WeaponTypes.SWORD, Elements.WATER, 77, 354, 15, 40, 4, 2, R.drawable.sword_abyssal_blade, 650, 1, true, true, false, false, 904000, 465000, 0, 0, 100444, 200888, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.gorgon_blade), WeaponTypes.SWORD, Elements.NEUTRAL, 80, 368, 15, 30, 9, 0, R.drawable.sword_gorgon_blade, 550, 1, true, true, true, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.air_striker), WeaponTypes.SWORD, Elements.AIR, 85, 391, 15, 40, 5, 5, R.drawable.sword_air_striker, 600, 0, true, true, false, false, 311000, 160000, 0, 0, 34555, 69111, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.glass_blade), WeaponTypes.SWORD, Elements.NEUTRAL, 85, 391, 15, 15, 7, 3, R.drawable.sword_glass_blade, 550, 1, false, false, false, false, 819000, 421000, 0, 0, 91000, 182000, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.sovereigns_ruby_sword), WeaponTypes.SWORD, Elements.FIRE, 86, 395, 15, 40, 4, 4, R.drawable.sword_sovereigns_ruby_sword, 500, 1, true, false, true, false, 0, 0, 3000, 1801, 33333, 66666, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.arachnid_sword), WeaponTypes.SWORD, Elements.NEUTRAL, 86, 395, 15, 20, 4, 6, R.drawable.sword_arachnid_sword, 500, 1, false, true, true, false, 0, 0, 3000, 1801, 33333, 66666, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.frost_heart), WeaponTypes.SWORD, Elements.DARKNESS, 88, 404, 15, 30, 3, 2, R.drawable.sword_frostheart, 500, 1, false, false, false, true, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.volcano_sword), WeaponTypes.SWORD, Elements.FIRE, 100, 460, 15, 25, 5, 0, R.drawable.sword_volcano_sword, 500, 1, false, true, false, false, 0, 0, 4000, 2400, 33333, 66666, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.cthulhu_blade), WeaponTypes.SWORD, Elements.NEUTRAL, 90, 414, 15, 35, 7, 3, R.drawable.sword_cthulhu_blade, 550, 1, false, true, true, false, 1310000, 677000, 0, 0, 145555, 291111, List.of(str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.blade_of_the_wicked), WeaponTypes.SWORD, Elements.NEUTRAL, 90, 414, 15, 0, 7, 4, R.drawable.sword_blade_of_the_wicked, 470, 1, true, true, true, false, 0, 0, 2500, 1500, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.goldsteel), WeaponTypes.SWORD, Elements.NEUTRAL, 90, 414, 15, 30, 5, 2, R.drawable.sword_goldsteel, 500, 0, true, false, false, false, 900000, 462000, 0, 0, 100000, 200000, List.of(str(context, R.string.chapter_five_random_drop))));
        swordList.add(new Weapon(str(context, R.string.bunny_claymore), WeaponTypes.SWORD, Elements.NEUTRAL, 92, 423, 15, 30, 6, 10, R.drawable.sword_bunny_claymore, 500, 0, true, false, false, false, 0, 0, 2500, 1500, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.golden_needle), WeaponTypes.SWORD, Elements.NEUTRAL, 94, 432, 15, 0, 6, 2, R.drawable.sword_golden_needle, 575, 2, false, false, false, false, 1750000, 900000, 0, 0, 194444, 388888, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.blade_of_rusted_souls), WeaponTypes.SWORD, Elements.NEUTRAL, 99, 455, 15, 30, 2, 2, R.drawable.sword_blade_of_rusted_souls, 530, 1, false, false, false, false, 1100000, 570000, 0, 0, 122222, 244444, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.survival_mode_4))));
        swordList.add(new Weapon(str(context, R.string.haunted_slicer), WeaponTypes.SWORD, Elements.DARKNESS, 100, 460, 15, 20, 5, 3, R.drawable.sword_haunted_slicer, 500, 2, false, true, false, false, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest), str(context, R.string.shop))));
        swordList.add(new Weapon(str(context, R.string.all_seeing_sword), WeaponTypes.SWORD, Elements.DARKNESS, 120, 552, 15, 30, 2, 5, R.drawable.sword_all_seeing_sword, 700, 1, true, false, false, false, 0, 0, 6000, 3601, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.sword_of_1000_lies), WeaponTypes.SWORD, Elements.FIRE, 115, 529, 15, 20, 0, 2, R.drawable.sword_sword_of_1000_lies, 850, 1, false, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        swordList.add(new Weapon(str(context, R.string.blade_of_survival), WeaponTypes.SWORD, Elements.NEUTRAL, 120, 552, 15, 40, 0, 0, R.drawable.sword_blade_of_survival, 800, 0, true, true, false, false, 0, 0, 3000, 1801, 66444, 132888, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        swordList.add(new Weapon(str(context, R.string.unleashed_kraken), WeaponTypes.SWORD, Elements.WATER, 135, 621, 15, 25, 3, 2, R.drawable.sword_unleashed_kraken, 550, 0, true, true, false, false, 0, 0, 4500, 2700, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));

        // Daggers
        daggerList.add(new Weapon(str(context, R.string.iron_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 4, 6, 3, 0, 5, 0, R.drawable.dagger_iron_fire_dagger, 450, 0, true, false, false, false, 300, 150, 0, 0, 33, 66, List.of(str(context, R.string.chapter_one_random_drop))));
        daggerList.add(new Weapon(str(context, R.string.fire_dagger), WeaponTypes.DAGGER, Elements.FIRE, 7, 12, 3, 0, 6, 0, R.drawable.dagger_iron_fire_dagger, 600, 0, true, false, false, false, 500, 250, 0, 0, 55, 111, List.of(str(context, R.string.chapter_one_random_drop))));
        daggerList.add(new Weapon(str(context, R.string.machete), WeaponTypes.DAGGER, Elements.NEUTRAL, 8, 13, 3, 0, 7, 0, R.drawable.dagger_machete, 500, 0, true, false, false, false, 500, 250, 0, 0, 55, 111, List.of(str(context, R.string.chapter_one_random_drop))));
        daggerList.add(new Weapon(str(context, R.string.chef_knife), WeaponTypes.DAGGER, Elements.NEUTRAL, 9, 15, 3, 0, 4, 2, R.drawable.dagger_chef_knife, 500, 0, true, false, false, false, 600, 300, 0, 0, 66, 133, List.of(str(context, R.string.chapter_one_random_drop))));
        daggerList.add(new Weapon(str(context, R.string.steel_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 16, 27, 3, 0, 6, 3, R.drawable.dagger_steel_dagger, 450, 0, true, false, false, false, 1400, 700, 0, 0, 155, 311, List.of(str(context, R.string.chapter_one_random_drop))));
        daggerList.add(new Weapon(str(context, R.string.kunai), WeaponTypes.DAGGER, Elements.DARKNESS, 11, 45, 13, 0, 16, 3, R.drawable.dagger_kunai, 500, 0, true, false, false, false, 3800, 1900, 0, 0, 422, 844, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.shop))));
        daggerList.add(new Weapon(str(context, R.string.light_dagger), WeaponTypes.DAGGER, Elements.LIGHT, 12, 75, 22, 0, 10, 10, R.drawable.dagger_light_dagger, 400, 0, true, false, false, false, 9700, 4900, 0, 0, 1077, 2155, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        daggerList.add(new Weapon(str(context, R.string.poisoned_dagger), WeaponTypes.DAGGER, Elements.EARTH, 10, 48, 16, 0, 20, 4, R.drawable.dagger_poisoned_dart, 400, 0, true, false, true, false, 4800, 2400, 0, 0, 533, 1066, List.of(str(context, R.string.achievement))));
        daggerList.add(new Weapon(str(context, R.string.gorgon_dagger), WeaponTypes.DAGGER, Elements.EARTH, 24, 98, 13, 30, 15, 2, R.drawable.dagger_gorgon_dagger, 600, 1, true, false, true, false, 75000, 38500, 0, 0, 8333, 16666, List.of(str(context, R.string.pvp_silver_purple_chest))));
        daggerList.add(new Weapon(str(context, R.string.ifrits_dagger), WeaponTypes.DAGGER, Elements.FIRE, 22, 101, 15, 0, 20, 2, R.drawable.dagger_ifrits_dagger, 400, 0, true, false, false, false, 17000, 8700, 0, 0, 1888, 3777, List.of(str(context, R.string.shop))));
        daggerList.add(new Weapon(str(context, R.string.dagger_of_sand), WeaponTypes.DAGGER, Elements.AIR, 32, 131, 13, 0, 20, 2, R.drawable.dagger_dagger_of_sand, 384, 0, true, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        daggerList.add(new Weapon(str(context, R.string.dark_dagger), WeaponTypes.DAGGER, Elements.DARKNESS, 36, 148, 13, 0, 25, 2, R.drawable.dagger_dark_dagger, 360, 0, true, false, false, false, 35500, 18000, 0, 0, 3944, 7888, List.of(str(context, R.string.shop))));
        daggerList.add(new Weapon(str(context, R.string.precious_skewer), WeaponTypes.DAGGER, Elements.NEUTRAL, 35, 161, 15, -10, 25, 3, R.drawable.dagger_precious_skewer, 360, 0, true, false, false, false, 500000, 257000, 0, 0, 55555, 111111, List.of(str(context, R.string.chapter_five_random_drop))));
        daggerList.add(new Weapon(str(context, R.string.dark_dagger_plus), WeaponTypes.DAGGER, Elements.DARKNESS, 40, 164, 13, 0, 25, 3, R.drawable.dagger_dark_dagger, 330, 0, true, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        daggerList.add(new Weapon(str(context, R.string.last_survivors_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 36, 165, 15, 0, 20, 3, R.drawable.dagger_last_survivors_dagger, 300, 0, true, false, false, false, 41500, 21500, 0, 0, 4611, 9222, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.survival_mode_1))));
        daggerList.add(new Weapon(str(context, R.string.precious_dual_blade), WeaponTypes.DAGGER, Elements.NEUTRAL, 37, 170, 15, 0, 12, 5, R.drawable.dagger_precious_dual_blade, 400, 1, false, true, false, false, 700000, 360000, 0, 0, 77777, 155555, List.of(str(context, R.string.chapter_five_random_drop))));
        daggerList.add(new Weapon(str(context, R.string.bunnyhop_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 39, 179, 15, 0, 20, 18, R.drawable.dagger_bunnyhop_dagger, 550, 1, true, false, false, false, 0, 0, 1000, 600, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.gayan), WeaponTypes.DAGGER, Elements.EARTH, 42, 183, 14, 30, 27, 4, R.drawable.dagger_gayan, 360, 0, true, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.dungeon_39))));
        daggerList.add(new Weapon(str(context, R.string.ice_dagger), WeaponTypes.DAGGER, Elements.WATER, 40, 184, 15, 0, 15, 3, R.drawable.dagger_ice_dagger, 360, 1, true, false, false, true, 0, 0, 2500, 1500, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.sapphire_pawns_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 42, 193, 15, 0, 20, 0, R.drawable.dagger_sapphire_pawns_dagger, 500, 0, true, false, false, true, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.survivors_double_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 44, 202, 15, 0, 15, 5, R.drawable.dagger_survivors_double_dagger, 400, 1, true, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.undines_dagger), WeaponTypes.DAGGER, Elements.WATER, 50, 206, 13, 0, 25, 4, R.drawable.dagger_undines_dagger, 500, 0, true, false, true, false, 74500, 38000, 0, 0, 8277, 16555, List.of(str(context, R.string.shop))));
        daggerList.add(new Weapon(str(context, R.string.venomous_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 46, 211, 15, 0, 18, 2, R.drawable.dagger_venomous_dagger, 400, 1, true, false, true, false, 265000, 136000, 0, 0, 29444, 58888, List.of(str(context, R.string.shop))));
        daggerList.add(new Weapon(str(context, R.string.dragon_tooth_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 48, 220, 15, -40, 25, 10, R.drawable.dagger_dragon_tooth_dagger, 360, 1, true, false, false, false, 259000, 133000, 0, 0, 28777, 57555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.dual_dragon_fang), WeaponTypes.DAGGER, Elements.NEUTRAL, 48, 220, 15, 0, 15, 5, R.drawable.dagger_dual_dragon_fang, 400, 1, true, true, false, false, 340000, 175000, 0, 0, 37777, 75555, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.toxic_dual_dagger), WeaponTypes.DAGGER, Elements.NEUTRAL, 50, 230, 15, 0, 15, 5, R.drawable.dagger_toxic_dual_dagger, 500, 1, true, true, true, false, 0, 0, 2000, 1200, 45333, 90666, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.abyssal_dual_blade), WeaponTypes.DAGGER, Elements.WATER, 50, 230, 15, 0, 15, 5, R.drawable.dagger_abyssal_dual_blade, 500, 1, true, true, true, false, 0, 0, 2000, 1200, 47111, 94222, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.iron_dagger_plus), WeaponTypes.DAGGER, Elements.NEUTRAL, 50, 230, 15, -30, 25, 0, R.drawable.dagger_iron_dagger_plus, 440, 1, true, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        daggerList.add(new Weapon(str(context, R.string.cthulhu_dagger), WeaponTypes.DAGGER, Elements.DARKNESS, 52, 239, 15, 0, 20, 3, R.drawable.dagger_cthulhu_dagger, 400, 2, true, false, true, false, 0, 0, 2700, 1621, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.malicious_quickblade), WeaponTypes.DAGGER, Elements.NEUTRAL, 48, 220, 15, -20, 20, 5, R.drawable.dagger_malicious_quickblade, 450, 1, true, false, true, false, 0, 0, 2500, 1500, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.all_seeing_cleaver), WeaponTypes.DAGGER, Elements.NEUTRAL, 52, 239, 15, -15, 25, 3, R.drawable.dagger_all_seeing_cleaver, 360, 1, true, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.ruby_pawns_edge), WeaponTypes.DAGGER, Elements.NEUTRAL, 58, 266, 15, -20, 25, 0, R.drawable.dagger_ruby_pawns_edge, 440, 0, true, true, true, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.blood_swiftblade), WeaponTypes.DAGGER, Elements.FIRE, 61, 280, 15, 0, 25, 0, R.drawable.dagger_blood_swiftblade, 250, 2, true, true, false, false, 625000, 321000, 0, 0, 69444, 138888, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.ifrits_shuriken), WeaponTypes.DAGGER, Elements.FIRE, 74, 340, 15, -20, 15, 3, R.drawable.shuriken_ifrits_shuriken, 340, 0, true, false, true, false, 198000, 101000, 0, 0, 22000, 44000, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.undines_shuriken), WeaponTypes.DAGGER, Elements.WATER, 76, 349, 15, -20, 15, 3, R.drawable.shuriken_undines_shuriken, 360, 0, true, false, false, true, 265000, 136000, 0, 0, 29444, 58888, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.gayan_shuriken), WeaponTypes.DAGGER, Elements.EARTH, 80, 368, 15, -10, 15, 3, R.drawable.shuriken_gayan_shuriken, 340, 0, true, false, false, false, 210000, 108000, 0, 0, 23333, 46666, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        daggerList.add(new Weapon(str(context, R.string.shooting_star_shuriken), WeaponTypes.DAGGER, Elements.LIGHT, 84, 386, 15, -20, 18, 0, R.drawable.shuriken_shooting_star_shuriken, 360, 1, true, false, false, false, 820000, 421000, 0, 0, 91111, 182222, List.of(str(context, R.string.weekly_dungeon_golden_chest))));

        // Hammers
        hammerList.add(new Weapon(str(context, R.string.wooden_hawaiian), WeaponTypes.HAMMER, Elements.NEUTRAL, 10, 17, 3, 0, 0, 0, R.drawable.mace_wooden_hawaiian, 800, 0, true, false, false, false, 2100, 1000, 0, 0, 233, 466, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop))));
        hammerList.add(new Weapon(str(context, R.string.silver_mace), WeaponTypes.HAMMER, Elements.NEUTRAL, 12, 20, 3, 35, 0, 0, R.drawable.mace_silver_dark_mace, 800, 0, true, true, false, false, 1300, 700, 0, 0, 144, 288, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop))));
        hammerList.add(new Weapon(str(context, R.string.club), WeaponTypes.HAMMER, Elements.NEUTRAL, 16, 27, 3, 30, 0, 0, R.drawable.mace_club, 800, 0, true, true, false, false, 2000, 1000, 0, 0, 222, 444, List.of(str(context, R.string.chapter_one_random_drop))));
        hammerList.add(new Weapon(str(context, R.string.iron_mace), WeaponTypes.HAMMER, Elements.NEUTRAL, 16, 27, 3, 30, 0, 0, R.drawable.mace_iron_mace, 800, 0, true, true, false, false, 2000, 1000, 0, 0, 222, 444, List.of(str(context, R.string.chapter_two_random_drop))));
        hammerList.add(new Weapon(str(context, R.string.insect_crusher), WeaponTypes.HAMMER, Elements.NEUTRAL, 16, 27, 3, 35, 0, 0, R.drawable.hammer_insect_crusher, 800, 0, true, true, false, false, 2000, 1000, 0, 0, 222, 444, List.of(str(context, R.string.chapter_one_random_drop))));
        hammerList.add(new Weapon(str(context, R.string.wooden_hammer), WeaponTypes.HAMMER, Elements.EARTH, 17, 41, 6, 35, 0, 0, R.drawable.hammer_wooden_hammer, 800, 0, true, true, false, false, 4300, 2200, 0, 0, 477, 955, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.bionic_hammer), WeaponTypes.HAMMER, Elements.NEUTRAL, 38, 65, 3, 35, 0, 6, R.drawable.hammer_bionic_hammer, 500, 0, true, true, false, false, 9200, 4700, 0, 0, 1022, 2044, List.of(str(context, R.string.chapter_three_random_drop))));
        hammerList.add(new Weapon(str(context, R.string.warhammer), WeaponTypes.HAMMER, Elements.NEUTRAL, 28, 108, 12, 50, 0, 0, R.drawable.hammer_warhammer, 800, 0, true, true, false, false, 24000, 12000, 0, 0, 2666, 5333, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.shop))));
            hammerList.add(new Weapon(str(context, R.string.dark_mace), WeaponTypes.HAMMER, Elements.DARKNESS, 35, 127, 11, 50, 2, 1, R.drawable.mace_silver_dark_mace, 700, 0, true, true, false, false, 34500, 17500, 0, 0, 3833, 7666, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.spider_crusher), WeaponTypes.HAMMER, Elements.EARTH, 47, 159, 10, 35, 0, 2, R.drawable.hammer_spider_crusher, 800, 0, true, true, false, false, 53000, 27000, 0, 0, 5888, 11777, List.of(str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.inquisidor), WeaponTypes.HAMMER, Elements.DARKNESS, 40, 164, 13, 50, 1, 1, R.drawable.hammer_inquisidor, 700, 0, true, true, false, false, 75000, 38500, 0, 0, 8333, 16666, List.of(str(context, R.string.pvp_silver_purple_chest))));
        hammerList.add(new Weapon(str(context, R.string.thunder_hammer), WeaponTypes.HAMMER, Elements.NEUTRAL, 70, 204, 8, 45, 0, 2, R.drawable.hammer_thunder_hammer, 800, 0, true, true, false, false, 83000, 42500, 0, 0, 9222, 18444, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.liquid_hammer), WeaponTypes.HAMMER, Elements.WATER, 46, 211, 15, 65, 5, 1, R.drawable.hammer_liquid_hammer, 1050, 1, true, true, false, false, 331000, 170000, 0, 0, 36777, 73555, List.of(str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.toxic_mace), WeaponTypes.HAMMER, Elements.NEUTRAL, 46, 211, 15, 50, 2, 0, R.drawable.mace_toxic_mace, 600, 0, true, true, true, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        hammerList.add(new Weapon(str(context, R.string.wealthy_smasher), WeaponTypes.HAMMER, Elements.NEUTRAL, 46, 211, 15, 70, 0, 2, R.drawable.hammer_wealthy_smasher, 1050, 1, true, true, false, false, 1300000, 668000, 0, 0, 144444, 288888, List.of(str(context, R.string.chapter_five_random_drop))));
        hammerList.add(new Weapon(str(context, R.string.wealthy_crusher), WeaponTypes.HAMMER, Elements.NEUTRAL, 46, 211, 15, 70, 2, 0, R.drawable.mace_wealthy_crusher, 1050, 2, true, true, false, false, 1500000, 771000, 0, 0, 166666, 333333, List.of(str(context, R.string.chapter_five_random_drop))));
        hammerList.add(new Weapon(str(context, R.string.falcon_hammer), WeaponTypes.HAMMER, Elements.AIR, 45, 217, 16, 60, 9, 2, R.drawable.hammer_falcon_hammer, 1050, 1, true, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_red_chest))));
        hammerList.add(new Weapon(str(context, R.string.hammer_of_light), WeaponTypes.HAMMER, Elements.LIGHT, 48, 220, 15, 50, 3, 3, R.drawable.hammer_hammer_of_light, 1050, 1, true, true, false, false, 357000, 183000, 0, 0, 39666, 79333, List.of(str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.toxic_hammer), WeaponTypes.HAMMER, Elements.NEUTRAL, 48, 220, 15, 70, 0, 0, R.drawable.hammer_toxic_hammer, 750, 1, true, true, true, false, 0, 0, 2000, 1200, 27777, 55555, List.of(str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.volcano_hammer), WeaponTypes.HAMMER, Elements.FIRE, 47, 227, 16, 80, 0, 0, R.drawable.hammer_volcano_hammer, 1050, 1, true, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_red_chest))));
        hammerList.add(new Weapon(str(context, R.string.verdant_hammer), WeaponTypes.HAMMER, Elements.EARTH, 48, 232, 16, 80, 0, 3, R.drawable.hammer_verdant_hammer, 1050, 1, true, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_red_chest))));
        hammerList.add(new Weapon(str(context, R.string.smasher_of_decay), WeaponTypes.HAMMER, Elements.NEUTRAL, 52, 239, 15, 50, 0, 0, R.drawable.hammer_smasher_of_decay, 900, 0, true, true, false, false, 113000, 58000, 0, 0, 12555, 25111, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.survival_mode_3))));
        hammerList.add(new Weapon(str(context, R.string.mojaras_hammer), WeaponTypes.HAMMER, Elements.DARKNESS, 55, 266, 16, 70, 2, 2, R.drawable.hammer_mojaras_hammer, 1050, 1, true, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_red_chest))));
        hammerList.add(new Weapon(str(context, R.string.wit_of_ancient_power), WeaponTypes.HAMMER, Elements.NEUTRAL, 56, 271, 16, 80, 0, 0, R.drawable.hammer_wit_of_ancient_power, 1400, 1, true, true, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.dungeon_43))));
        hammerList.add(new Weapon(str(context, R.string.bone_hammer), WeaponTypes.HAMMER, Elements.NEUTRAL, 62, 285, 15, 70, 0, 2, R.drawable.hammer_bone_hammer, 1050, 1, true, true, false, false, 573000, 295000, 0, 0, 63666, 127333, List.of(str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.war_forged_smasher), WeaponTypes.HAMMER, Elements.FIRE, 64, 294, 15, 70, 0, 0, R.drawable.hammer_war_forged_smasher, 700, 1, true, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.enlightened_hammer), WeaponTypes.HAMMER, Elements.LIGHT, 64, 294, 15, 60, 0, 0, R.drawable.hammer_enlightened_hammer, 650, 2, true, true, false, false, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.shop))));
        hammerList.add(new Weapon(str(context, R.string.root_of_judgement), WeaponTypes.HAMMER, Elements.EARTH, 64, 294, 15, 70, 0, 4, R.drawable.hammer_root_of_judgement, 700, 1, true, true, false, false, 0, 0, 3900, 2340, 16666, 33333, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        hammerList.add(new Weapon(str(context, R.string.eye_of_the_rift), WeaponTypes.HAMMER, Elements.DARKNESS, 64, 294, 15, 70, 0, 0, R.drawable.hammer_eye_of_the_rift, 700, 1, true, true, false, false, 0, 0, 3900, 2340, 16666, 33333, List.of(str(context, R.string.weekly_dungeon_golden_chest))));

        // Staffs
        staffList.add(new Weapon(str(context, R.string.nature_staff), WeaponTypes.STAFF, Elements.EARTH, 8, 13, 3, 0, 0, 0, R.drawable.staff_nature_gadarasts_staff, 800, 0, true, false, false, false, 500, 250, 0, 0, 55, 111, List.of(str(context, R.string.chapter_one_random_drop))));
        staffList.add(new Weapon(str(context, R.string.fire_staff), WeaponTypes.STAFF, Elements.FIRE, 10, 17, 3, 0, 0, 0, R.drawable.staff_fire_staff, 800, 0, true, false, false, false, 700, 350, 0, 0, 77, 155, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.light_staff), WeaponTypes.STAFF, Elements.LIGHT, 12, 20, 3, 0, 1, 1, R.drawable.staff_light_staff, 800, 0, true, false, false, false, 900, 450, 0, 0, 100, 200, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.oxymos_staff), WeaponTypes.STAFF, Elements.LIGHT, 21, 96, 15, 0, 0, 2, R.drawable.staff_oxymos_staff, 600, 0, true, true, false, false, 19500, 10000, 0, 0, 2166, 4333, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.gadarasts_staff), WeaponTypes.STAFF, Elements.EARTH, 32, 108, 10, 30, 1, 2, R.drawable.staff_nature_gadarasts_staff, 700, 0, true, false, false, false, 19000, 9900, 0, 0, 2111, 4222, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.staff_of_darkness), WeaponTypes.STAFF, Elements.DARKNESS, 41, 149, 11, 0, 2, 2, R.drawable.staff_staff_of_darkness, 700, 0, true, false, false, false, 35500, 18000, 0, 0, 3944, 7888, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.firebane), WeaponTypes.STAFF, Elements.FIRE, 41, 149, 11, 0, 2, 2, R.drawable.staff_firebane, 700, 0, true, true, false, false, 46000, 23500, 0, 0, 5111, 10222, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.the_testament_of_ifrit), WeaponTypes.STAFF, Elements.FIRE, 38, 174, 15, 0, 7, 0, R.drawable.grimoire_the_testament_of_ifrit, 400, 4, true, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.epic_fire_staff), WeaponTypes.STAFF, Elements.FIRE, 49, 178, 11, 0, 3, 1, R.drawable.staff_epic_fire_staff, 750, 0, true, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.chapter_two_random_drop))));
        staffList.add(new Weapon(str(context, R.string.necromorphicon), WeaponTypes.STAFF, Elements.DARKNESS, 39, 179, 15, -40, 0, 0, R.drawable.grimoire_necromorphicon, 550, 1, true, false, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        staffList.add(new Weapon(str(context, R.string.the_arachnid_codex), WeaponTypes.STAFF, Elements.NEUTRAL, 42, 193, 15, -25, 4, 4, R.drawable.grimoire_the_arachnid_codex, 500, 2, true, false, true, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.dragon_orb_staff), WeaponTypes.STAFF, Elements.FIRE, 48, 197, 13, 0, 1, 1, R.drawable.staff_dragon_orb_staff, 500, 0, true, true, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        staffList.add(new Weapon(str(context, R.string.the_oxymos_codex), WeaponTypes.STAFF, Elements.LIGHT, 44, 202, 15, -20, 10, 0, R.drawable.grimoire_the_oxymos_codex, 450, 1, true, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.the_ancient_codices), WeaponTypes.STAFF, Elements.NEUTRAL, 50, 230, 15, 0, 4, 6, R.drawable.grimoire_the_ancient_codices, 450, 0, false, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.the_word_of_the_wind), WeaponTypes.STAFF, Elements.AIR, 51, 234, 15, -20, 8, 0, R.drawable.grimoire_the_word_of_the_wind, 500, 2, true, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.compendium_of_trees), WeaponTypes.STAFF, Elements.EARTH, 52, 239, 15, 20, 4, 2, R.drawable.grimoire_compendium_of_trees, 550, 1, true, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.the_abyssal_scrolls), WeaponTypes.STAFF, Elements.WATER, 52, 239, 15, 0, 5, 0, R.drawable.grimoire_the_abyssal_scrolls, 500, 3, true, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.the_daemon_codices), WeaponTypes.STAFF, Elements.DARKNESS, 54, 248, 15, -20, 0, 7, R.drawable.grimoire_the_daemon_codices, 400, 2, false, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.broom_staff), WeaponTypes.STAFF, Elements.EARTH, 60, 276, 15, 0, 0, 9, R.drawable.staff_broom_staff, 650, 1, true, true, false, false, 0, 0, 500, 300, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.icebane), WeaponTypes.STAFF, Elements.WATER, 62, 285, 15, 30, 4, 2, R.drawable.staff_icebane, 610, 1, true, true, false, true, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.nightfall), WeaponTypes.STAFF, Elements.DARKNESS, 62, 285, 15, 0, 2, 4, R.drawable.staff_nightfall, 630, 1, true, true, false, true, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.tome_of_the_sands), WeaponTypes.STAFF, Elements.AIR, 66, 303, 15, -50, 10, 0, R.drawable.grimoire_tome_of_the_sands, 450, 2, true, false, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.hippie_dragon_staff_plus), WeaponTypes.STAFF, Elements.EARTH, 70, 322, 15, 40, 5, 3, R.drawable.staff_hippie_dragon_staff, 550, 0, true, true, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        staffList.add(new Weapon(str(context, R.string.dragonskull_staff), WeaponTypes.STAFF, Elements.FIRE, 72, 331, 15, 0, 5, 1, R.drawable.staff_dragonskull_staff, 650, 0, true, true, false, false, 222000, 114000, 0, 0, 24666, 49333, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.dragonskull_staff_plus), WeaponTypes.STAFF, Elements.FIRE, 95, 437, 15, 0, 4, 4, R.drawable.staff_dragonskull_staff, 550, 1, true, true, false, false, 222000, 114000, 4000, 2400, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.royal_war_staff), WeaponTypes.STAFF, Elements.NEUTRAL, 72, 331, 15, 20, 3, 2, R.drawable.staff_royal_war_staff, 600, 0, true, true, false, false, 700000, 360000, 0, 0, 77777, 155555, List.of(str(context, R.string.chapter_four_random_drop))));
        staffList.add(new Weapon(str(context, R.string.hippie_dragon_staff), WeaponTypes.STAFF, Elements.EARTH, 75, 345, 15, 30, 2, 2, R.drawable.staff_hippie_dragon_staff, 650, 0, true, true, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        staffList.add(new Weapon(str(context, R.string.windbane), WeaponTypes.STAFF, Elements.AIR, 75, 345, 15, 0, 8, 2, R.drawable.staff_windbane, 700, 2, true, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.golden_dragon_staff), WeaponTypes.STAFF, Elements.LIGHT, 76, 349, 15, 20, 3, 2, R.drawable.staff_golden_dragon_staff, 650, 0, true, true, false, false, 247000, 127000, 0, 0, 27444, 54888, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.golden_dragon_staff_plus), WeaponTypes.STAFF, Elements.LIGHT, 90, 414, 15, 20, 4, 3, R.drawable.staff_golden_dragon_staff, 650, 1, true, true, false, false, 247000, 127000, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.cthulhu_staff), WeaponTypes.STAFF, Elements.DARKNESS, 77, 354, 15, 30, 0, 3, R.drawable.staff_cthulhu_staff, 590, 2, true, true, true, false, 1090000, 561000, 0, 0, 121111, 242222, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.death_scythe), WeaponTypes.STAFF, Elements.DARKNESS, 80, 368, 15, 0, 2, 1, R.drawable.staff_death_scythe, 650, 0, true, true, false, false, 273000, 140000, 0, 0, 30333, 60666, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.water_staff), WeaponTypes.STAFF, Elements.WATER, 80, 368, 15, 0, 3, 3, R.drawable.staff_water_staff, 650, 0, true, true, false, false, 274000, 140000, 0, 0, 30444, 60888, List.of(str(context, R.string.shop))));
        staffList.add(new Weapon(str(context, R.string.death_scythe_plus), WeaponTypes.STAFF, Elements.DARKNESS, 85, 391, 15, 0, 2, 3, R.drawable.staff_death_scythe, 450, 1, true, true, false, false, 1090000, 562000, 0, 0, 121111, 242222, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.legacy_of_the_oracle), WeaponTypes.STAFF, Elements.EARTH, 88, 404, 15, 15, 3, 7, R.drawable.staff_legacy_of_the_oracle, 600, 1, true, true, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.staff_of_withering), WeaponTypes.STAFF, Elements.EARTH, 88, 404, 15, 0, 7, 0, R.drawable.staff_staff_of_withering, 600, 1, true, true, false, false, 0, 0, 4500, 2700, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.toxic_staff), WeaponTypes.STAFF, Elements.EARTH, 90, 414, 15, 0, 5, 2, R.drawable.staff_toxic_staff, 450, 0, true, true, true, false, 380000, 195000, 0, 0, 42222, 84444, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.dead_vulture_staff), WeaponTypes.STAFF, Elements.DARKNESS, 95, 437, 15, 0, 2, 1, R.drawable.staff_dead_vulture_staff, 650, 1, true, true, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.seabane), WeaponTypes.STAFF, Elements.WATER, 95, 437, 15, 15, 4, 3, R.drawable.staff_seabane, 700, 1, true, true, false, false, 1360000, 702000, 0, 0, 151111, 302222, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.spider_staff), WeaponTypes.STAFF, Elements.DARKNESS, 97, 446, 15, 0, 4, 4, R.drawable.staff_spider_staff, 550, 1, true, true, true, false, 1560000, 803000, 0, 0, 173333, 346666, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.crowned_dragonhead), WeaponTypes.STAFF, Elements.FIRE, 102, 469, 15, -10, 4, 4, R.drawable.staff_crowned_dragonhead, 550, 1, true, true, false, false, 0, 0, 4900, 2940, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.staff_of_the_wicked), WeaponTypes.STAFF, Elements.NEUTRAL, 110, 506, 15, 20, 3, 2, R.drawable.staff_staff_of_the_wicked, 400, 0, true, false, false, false, 386000, 198000, 0, 0, 42888, 85777, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.survival_mode_2))));
        staffList.add(new Weapon(str(context, R.string.ancient_stone_staff), WeaponTypes.STAFF, Elements.NEUTRAL, 120, 552, 15, 0, 0, 0, R.drawable.staff_ancient_stone_staff, 700, 0, true, false, false, false, 0, 0, 2000, 1200, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        staffList.add(new Weapon(str(context, R.string.voidcaller_staff), WeaponTypes.STAFF, Elements.DARKNESS, 130, 598, 15, -30, 4, 5, R.drawable.staff_voidcaller_staff, 600, 1, false, false, false, false, 0, 0, 5000, 3000, 27777, 55555, List.of(str(context, R.string.shop))));

        // Spears
        spearList.add(new Weapon(str(context, R.string.obsidian_spear), WeaponTypes.SPEAR, Elements.NEUTRAL, 40, 164, 13, 20, 4, 3, R.drawable.spear_obsidian_spear, 750, 4, false, true, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.pvp_silver_purple_chest), str(context, R.string.achievement))));
        spearList.add(new Weapon(str(context, R.string.gayan_spear), WeaponTypes.SPEAR, Elements.EARTH, 53, 243, 15, 65, 3, 4, R.drawable.spear_gayan_spear, 750, 4, false, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_purple_chest))));
        spearList.add(new Weapon(str(context, R.string.cthulhu_spear), WeaponTypes.SPEAR, Elements.DARKNESS, 55, 253, 15, 100, 0, 0, R.drawable.spear_cthulhu_spear, 750, 4, false, true, true, false, 0, 0, 2000, 1200, 33333, 66666, List.of(str(context, R.string.shop))));
        spearList.add(new Weapon(str(context, R.string.golden_piercer), WeaponTypes.SPEAR, Elements.NEUTRAL, 55, 253, 15, 20, 4, 3, R.drawable.spear_golden_piercer, 750, 4, false, true, false, false, 800000, 411000, 0, 0, 88888, 177777, List.of(str(context, R.string.chapter_five_random_drop))));
        spearList.add(new Weapon(str(context, R.string.yansahn), WeaponTypes.SPEAR, Elements.AIR, 57, 262, 15, 0, 10, 4, R.drawable.spear_yansahn, 750, 4, false, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_purple_chest))));
        spearList.add(new Weapon(str(context, R.string.fire_pierce), WeaponTypes.SPEAR, Elements.FIRE, 58, 266, 15, 0, 5, 3, R.drawable.spear_fire_pierce, 750, 4, false, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_purple_chest))));
        spearList.add(new Weapon(str(context, R.string.freyr), WeaponTypes.SPEAR, Elements.LIGHT, 58, 266, 15, 40, 6, 7, R.drawable.spear_freyr, 750, 4, false, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_purple_chest))));
        spearList.add(new Weapon(str(context, R.string.poisoned_spear), WeaponTypes.SPEAR, Elements.EARTH, 58, 266, 15, 0, 10, 0, R.drawable.spear_poisoned_spear, 500, 4, true, true, true, false, 749000, 385000, 0, 0, 83222, 166444, List.of(str(context, R.string.shop))));
        spearList.add(new Weapon(str(context, R.string.gorgon_spear), WeaponTypes.SPEAR, Elements.NEUTRAL, 60, 276, 15, 0, 10, 0, R.drawable.spear_gorgon_spear, 650, 4, true, true, true, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        spearList.add(new Weapon(str(context, R.string.last_survivors_spear), WeaponTypes.SPEAR, Elements.NEUTRAL, 61, 280, 15, 20, 2, 2, R.drawable.spear_last_survivors_spear, 750, 3, false, true, false, false, 663000, 341000, 0, 0, 73666, 147333, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.survival_mode_5))));
        spearList.add(new Weapon(str(context, R.string.osun), WeaponTypes.SPEAR, Elements.WATER, 64, 294, 15, 20, 5, 4, R.drawable.spear_osun, 750, 4, false, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_purple_chest))));
        spearList.add(new Weapon(str(context, R.string.azatoth), WeaponTypes.SPEAR, Elements.DARKNESS, 67, 308, 15, 30, 5, 3, R.drawable.spear_azatoth, 750, 4, false, true, false, false, 150000, 77000, 0, 0, 16666, 33333, List.of(str(context, R.string.pvp_golden_purple_chest))));
        spearList.add(new Weapon(str(context, R.string.spider_spear), WeaponTypes.SPEAR, Elements.EARTH, 68, 312, 15, 25, 3, 12, R.drawable.spear_spider_spear, 650, 4, false, true, true, false, 0, 0, 2000, 1200, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        spearList.add(new Weapon(str(context, R.string.wicked_skewer), WeaponTypes.SPEAR, Elements.FIRE, 72, 331, 15, 0, 5, 3, R.drawable.spear_wicked_skewer, 450, 3, false, true, false, false, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        spearList.add(new Weapon(str(context, R.string.ruby_queens_spear), WeaponTypes.SPEAR, Elements.FIRE, 74, 340, 15, 0, 5, 3, R.drawable.spear_ruby_queens_spear, 650, 4, false, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        spearList.add(new Weapon(str(context, R.string.vindictive_piercer), WeaponTypes.SPEAR, Elements.AIR, 80, 368, 15, 0, 6, 2, R.drawable.spear_vindictive_piercer, 650, 5, false, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        spearList.add(new Weapon(str(context, R.string.onyx_queens_lance), WeaponTypes.SPEAR, Elements.WATER, 84, 386, 15, 0, 5, 0, R.drawable.spear_onyx_queens_lance, 650, 4, false, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));

        // Axes
        axeList.add(new Weapon(str(context, R.string.silver_axe), WeaponTypes.AXE, Elements.NEUTRAL, 19, 32, 3, 35, 0, 0, R.drawable.axe_silver_fire_lightning_axe, 800, 0, true, false, false, false, 2000, 1000, 0, 0, 222, 444, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.umbranian_cutter), WeaponTypes.AXE, Elements.NEUTRAL, 35, 60, 3, 30, 0, 0, R.drawable.axe_umbranian_flaming_cutter, 800, 0, true, true, false, false, 9000, 4600, 0, 0, 1000, 2000, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.bone_chopper), WeaponTypes.AXE, Elements.AIR, 22, 69, 9, 35, 6, 3, R.drawable.axe_bone_chopper, 450, 0, true, false, false, false, 8500, 4300, 0, 0, 944, 1888, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.axe_of_fire), WeaponTypes.AXE, Elements.FIRE, 21, 96, 15, 30, 3, 0, R.drawable.axe_silver_fire_lightning_axe, 600, 0, true, false, false, false, 15000, 7900, 0, 0, 1666, 3333, List.of(str(context, R.string.chapter_three_random_drop))));
        axeList.add(new Weapon(str(context, R.string.flaming_cutter), WeaponTypes.AXE, Elements.FIRE, 24, 110, 15, 40, 4, 1, R.drawable.axe_umbranian_flaming_cutter, 600, 0, true, false, false, false, 20000, 10000, 0, 0, 2222, 4444, List.of(str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.lightning_axe), WeaponTypes.AXE, Elements.LIGHT, 25, 115, 15, 0, 3, 2, R.drawable.axe_silver_fire_lightning_axe, 450, 1, true, true, false, false, 99000, 50500, 0, 0, 11000, 22000, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.spider_axe), WeaponTypes.AXE, Elements.FIRE, 40, 164, 13, 30, 3, 3, R.drawable.axe_spider_axe, 600, 0, true, true, true, false, 75000, 38500, 0, 0, 8333, 16666, List.of(str(context, R.string.pvp_silver_purple_chest))));
        axeList.add(new Weapon(str(context, R.string.fortunes_battleaxe), WeaponTypes.AXE, Elements.NEUTRAL, 48, 220, 15, 35, 0, 0, R.drawable.axe_fortunes_battleaxe, 800, 1, true, true, false, false, 1000000, 514000, 0, 0, 111111, 222222, List.of(str(context, R.string.chapter_five_random_drop))));
        axeList.add(new Weapon(str(context, R.string.toxic_axe), WeaponTypes.AXE, Elements.NEUTRAL, 52, 239, 15, 40, 3, 2, R.drawable.axe_toxic_axe, 600, 1, true, true, true, false, 0, 0, 3000, 1801, 0, 1, List.of(str(context, R.string.achievement))));
        axeList.add(new Weapon(str(context, R.string.eggscellent_axe), WeaponTypes.AXE, Elements.NEUTRAL, 52, 239, 15, 30, 3, 17, R.drawable.axe_eggscellent_axe, 500, 0, true, true, false, false, 0, 0, 1500, 901, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        axeList.add(new Weapon(str(context, R.string.axe_of_rusted_gods), WeaponTypes.AXE, Elements.NEUTRAL, 53, 243, 15, 35, 0, 0, R.drawable.axe_axe_of_rusted_gods, 800, 0, true, true, false, false, 117000, 60000, 0, 0, 13000, 26000, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.survival_mode_6))));
        axeList.add(new Weapon(str(context, R.string.bone_axe), WeaponTypes.AXE, Elements.NEUTRAL, 60, 276, 15, 20, 7, 3, R.drawable.axe_bone_axe, 380, 1, true, true, false, false, 534000, 274000, 0, 0, 59333, 118666, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        axeList.add(new Weapon(str(context, R.string.steel_axe), WeaponTypes.AXE, Elements.NEUTRAL, 70, 322, 15, 35, 0, 0, R.drawable.axe_steel_axe, 800, 2, true, true, false, false, 797000, 409000, 0, 0, 88555, 177111, List.of(str(context, R.string.dungeon_42))));
        axeList.add(new Weapon(str(context, R.string.ice_axe), WeaponTypes.AXE, Elements.NEUTRAL, 70, 322, 15, 30, 2, 5, R.drawable.axe_ice_axe, 450, 0, true, false, false, true, 0, 0, 3000, 1801, 24444, 48888, List.of(str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.path_of_exile), WeaponTypes.AXE, Elements.FIRE, 74, 340, 15, 40, 4, 0, R.drawable.axe_path_of_exile, 750, 1, true, true, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        axeList.add(new Weapon(str(context, R.string.windpearl_axe), WeaponTypes.AXE, Elements.AIR, 75, 345, 15, 20, 8, 1, R.drawable.axe_windpearl_axe, 600, 1, true, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.cthulhu_axe), WeaponTypes.AXE, Elements.DARKNESS, 78, 358, 15, 40, 4, 3, R.drawable.axe_cthulhu_axe, 600, 1, true, true, true, false, 1010000, 523000, 0, 0, 112222, 224444, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        axeList.add(new Weapon(str(context, R.string.dawnlight), WeaponTypes.AXE, Elements.LIGHT, 80, 368, 15, 50, 4, 4, R.drawable.axe_dawnlight, 450, 1, true, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.path_of_exile_plus), WeaponTypes.AXE, Elements.FIRE, 82, 377, 15, 60, 7, 3, R.drawable.axe_path_of_exile, 550, 1, true, true, false, false, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        axeList.add(new Weapon(str(context, R.string.stormcaller), WeaponTypes.AXE, Elements.WATER, 85, 391, 15, 30, 3, 3, R.drawable.axe_stormcaller, 600, 1, true, true, false, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.stormcaller))));
        axeList.add(new Weapon(str(context, R.string.gorgon_axe), WeaponTypes.AXE, Elements.EARTH, 92, 423, 15, 40, 6, 1, R.drawable.axe_gorgon_axe, 600, 1, true, false, true, false, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));
        axeList.add(new Weapon(str(context, R.string.all_seeing_axe), WeaponTypes.AXE, Elements.DARKNESS, 105, 483, 15, 50, 3, 3, R.drawable.axe_all_seeing_axe, 600, 1, true, true, false, false, 0, 0, 6000, 3601, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));

        // Armors
        armorList.add(new Armor(str(context, R.string.jumpman_suit), Elements.AIR, false, 0, 0, 1, 0, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_jumpman_suit, 400, 200, 0, 0, 44, 88, List.of(str(context, R.string.chapter_one_random_drop))));
        armorList.add(new Armor(str(context, R.string.minion_vest), Elements.NEUTRAL, false, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_minion_warlock_vest, 200, 100, 0, 0, 22, 44, List.of(str(context, R.string.chapter_one_random_drop))));
        armorList.add(new Armor(str(context, R.string.recruit_set), Elements.NEUTRAL, false, 6, 10, 3, 0, 0, 0, 10, 0, 15, 0, 0, 0, R.drawable.armor_recruit_soldier_set, 550, 300, 0, 0, 61, 122, List.of(str(context, R.string.chapter_one_random_drop))));
        armorList.add(new Armor(str(context, R.string.improved_jumpman_suit), Elements.AIR, false, 10, 10, 1, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_jumpman_suit, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.tubeman_suit), Elements.AIR, false, 10, 10, 1, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_tubeman_suit, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.for_content_makers))));
        armorList.add(new Armor(str(context, R.string.twitch_suit), Elements.AIR, false, 10, 10, 1, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_twitch_suit, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.for_content_makers))));
        armorList.add(new Armor(str(context, R.string.dancers_raiment), Elements.AIR, false, 10, 10, 1, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_dancers_raiment_tiktok, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.for_content_makers))));
        armorList.add(new Armor(str(context, R.string.knight_of_discord), Elements.NEUTRAL, false, 10, 10, 1, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_knight_of_discord, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.for_content_makers))));
        armorList.add(new Armor(str(context, R.string.cleric_vest), Elements.LIGHT, false, 8, 13, 3, 2, 2, 35, 0, 20, 20, 0, 0, 0, R.drawable.armor_cleric_vest, 3000, 1500, 0, 0, 333, 666, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.nature_mage_vest), Elements.EARTH, false, 8, 13, 3, 0, 0, 30, 0, 20, 0, 0, 0, 0, R.drawable.armor_nature_mage_vest, 1800, 950, 0, 0, 200, 400, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop))));
        armorList.add(new Armor(str(context, R.string.fire_mage_vest), Elements.FIRE, false, 8, 13, 3, 0, 0, 30, 0, 20, 0, 0, 0, 0, R.drawable.armor_fire_mage_vest, 1800, 950, 0, 0, 200, 400, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop))));
        armorList.add(new Armor(str(context, R.string.bunny_suit), Elements.NEUTRAL, false, 15, 15, 1, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_bunny_suit, 0, 0, 3500, 2100, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.soldier_set), Elements.NEUTRAL, false, 12, 20, 3, 0, 0, 0, 20, 0, 20, 15, 0, 0, R.drawable.armor_recruit_soldier_set, 1600, 850, 0, 0, 177, 355, List.of(str(context, R.string.chapter_one_random_drop))));
        armorList.add(new Armor(str(context, R.string.plague_fighter_suit), Elements.AIR, false, 14, 24, 3, 4, 3, 0, 0, 0, 20, 0, 0, 0, R.drawable.armor_plague_fighter_suit, 2000, 1000, 0, 0, 222, 444, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop))));
        armorList.add(new Armor(str(context, R.string.elite_soldier_set), Elements.NEUTRAL, false, 15, 25, 3, 3, 2, 0, 30, 0, 20, 20, 0, 0, R.drawable.armor_elite_soldier_set, 2500, 1300, 0, 0, 277, 555, List.of(str(context, R.string.chapter_one_random_drop))));
        armorList.add(new Armor(str(context, R.string.bankers_coat), Elements.DARKNESS, false, 10, 26, 7, 4, 4, 40, 0, 0, 30, 0, 0, 50, R.drawable.armor_bankers_coat, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.santa_claus), Elements.LIGHT, true, 30, 30, 1, 8, 20, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_santa_claus, 0, 0, 500, 300, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.ranger_set), Elements.AIR, false, 19, 32, 3, 3, 2, 0, 30, 0, 30, 0, 0, 0, R.drawable.armor_ranger_umbranian_set, 3600, 1800, 0, 0, 400, 800, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.umbranian_vest), Elements.AIR, false, 21, 36, 3, 5, 3, 30, 20, 0, 40, 0, 0, 0, R.drawable.armor_ranger_umbranian_set, 7000, 3600, 0, 0, 777, 1555, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop))));
        armorList.add(new Armor(str(context, R.string.druid_vest), Elements.EARTH, false, 15, 43, 8, 0, 3, 35, 0, 25, 25, 0, 0, 0, R.drawable.armor_druid_vest, 8600, 4400, 0, 0, 955, 1911, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.gatekeepers_bionic_arm), Elements.NEUTRAL, false, 10, 46, 15, 6, 6, 0, 30, 0, 20, 30, 30, 0, R.drawable.armor_gatekeepers_bionic_arm, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.deathdoctor_set), Elements.DARKNESS, false, 20, 48, 6, 4, 1, 20, 0, 20, 55, 0, 0, 0, R.drawable.armor_deathdoctor_set, 9000, 4600, 0, 0, 1000, 2000, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop))));
        armorList.add(new Armor(str(context, R.string.brass_set), Elements.NEUTRAL, false, 30, 51, 3, 1, 1, 0, 15, 0, 0, 20, 20, 0, R.drawable.armor_brass_set, 8300, 4300, 0, 0, 922, 1844, List.of(str(context, R.string.chapter_one_random_drop))));
        armorList.add(new Armor(str(context, R.string.heros_set), Elements.AIR, false, 25, 54, 5, 6, 2, 0, 40, 0, 40, 0, 20, 0, R.drawable.armor_heros_set, 9900, 5000, 0, 0, 1100, 2200, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.elite_brass_set), Elements.NEUTRAL, false, 36, 61, 3, 2, 2, 0, 20, 0, 0, 25, 25, 0, R.drawable.armor_elite_brass_set, 11500, 6100, 0, 0, 1277, 2555, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.forest_witch_suit), Elements.EARTH, false, 14, 64, 15, 3, 4, 60, 0, 60, 0, 0, 40, 0, R.drawable.armor_forest_witch_suit, 30500, 15500, 0, 0, 3388, 6777, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.obsidian_set), Elements.NEUTRAL, false, 42, 72, 3, 2, 2, 0, 25, 0, 0, 30, 30, 0, R.drawable.armor_obsidian_set, 16000, 8400, 0, 0, 1777, 3555, List.of(str(context, R.string.chapter_one_random_drop), str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.witch_dress), Elements.EARTH, false, 16, 73, 15, 2, 7, 120, 0, 80, 80, 0, 0, 0, R.drawable.armor_witch_dress, 0, 0, 500, 300, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.green_witch_suit), Elements.EARTH, false, 16, 73, 15, 3, 3, 40, 0, 40, 0, 0, 0, 0, R.drawable.armor_green_witch_suit, 20000, 10000, 0, 0, 2222, 4444, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.ifrits_disciple_robe), Elements.FIRE, false, 22, 74, 10, 3, 2, 40, 0, 35, 35, 35, 0, 0, R.drawable.armor_ifrits_disciple_robe, 25500, 13000, 0, 0, 2833, 5666, List.of(str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.monks_tunic), Elements.LIGHT, false, 24, 75, 9, 4, 2, 60, 0, 30, 0, 0, 30, 0, R.drawable.armor_monks_tunic, 28500, 14500, 0, 0, 3166, 6333, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.elite_assassin_suit), Elements.DARKNESS, false, 32, 78, 6, 5, 3, 60, 60, 0, 60, 60, 0, 60, R.drawable.armor_elite_assassin_suit, 117000, 60000, 0, 0, 13000, 26000, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.high_elf_robe), Elements.EARTH, false, 25, 79, 9, 3, 2, 40, 40, 40, 0, 0, 0, 40, R.drawable.armor_high_elf_robe, 29500, 15000, 0, 0, 3277, 6555, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.jungle_assassin_suit), Elements.EARTH, false, 25, 79, 9, 4, 4, 45, 0, 35, 35, 0, 40, 0, R.drawable.armor_jungle_assassin_suit, 32000, 16500, 0, 0, 3555, 7111, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.faldangs_vest), Elements.LIGHT, false, 22, 80, 11, 4, 1, 45, 0, 35, 0, 0, 0, 0, R.drawable.armor_faldangs_vest, 23500, 12000, 0, 0, 2611, 5222, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.gayan_set), Elements.EARTH, false, 80, 80, 1, 4, 3, 80, 0, 0, 80, 80, 0, 100, R.drawable.armor_gayan_set, 212000, 109000, 0, 0, 23555, 47111, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.dragon_vest), Elements.FIRE, false, 24, 81, 10, 5, 1, 60, 0, 40, 40, 40, 0, 0, R.drawable.armor_dragon_vest, 45000, 23000, 0, 0, 5000, 10000, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.high_cleric_tunic), Elements.LIGHT, false, 20, 82, 13, 5, 2, 60, 0, 40, 0, 0, 0, 0, R.drawable.armor_high_cleric_tunic, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.black_witch_suit), Elements.DARKNESS, false, 19, 87, 15, 4, 2, 70, 0, 75, 0, 0, 0, 60, R.drawable.armor_black_witch_suit, 61000, 31500, 0, 0, 6777, 13555, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.warlock_vest), Elements.DARKNESS, false, 22, 90, 13, 4, 2, 50, 0, 30, 30, 0, 0, 0, R.drawable.armor_minion_warlock_vest, 32500, 16500, 0, 0, 3611, 7222, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.elite_obsidian_set), Elements.NEUTRAL, false, 53, 91, 3, 3, 2, 0, 35, 0, 0, 35, 35, 0, R.drawable.armor_elite_obsidian_set, 26000, 13000, 0, 0, 2888, 5777, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.greenmoon_vest), Elements.EARTH, false, 27, 91, 10, 4, 3, 60, 0, 40, 0, 0, 40, 40, R.drawable.armor_greenmoon_vest, 52000, 26500, 0, 0, 5777, 11555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.tunic_of_hellish_illusions), Elements.DARKNESS, false, 20, 92, 15, 4, 8, 150, 0, 0, 0, 80, 100, 80, R.drawable.armor_tunic_of_hellish_illusions, 0, 0, 2500, 1500, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.storm), Elements.AIR, false, 20, 92, 15, 7, 4, 130, 0, 170, 0, 170, 0, 0, R.drawable.armor_storm, 0, 0, 3900, 2340, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.penumbra_suit), Elements.DARKNESS, false, 23, 94, 13, 5, 3, 70, 0, 70, 50, 0, 0, 0, R.drawable.armor_penumbra_suit, 63000, 32000, 0, 0, 7000, 14000, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.blue_wizard_suit), Elements.WATER, false, 30, 94, 9, 4, 2, 60, 0, 60, 0, 60, 0, 0, R.drawable.armor_blue_wizard_suit, 50500, 26000, 0, 0, 5611, 11222, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.kings_spectre_tunic), Elements.NEUTRAL, false, 28, 95, 10, 4, 4, 400, 0, 0, 0, 0, 0, 0, R.drawable.armor_kings_spectre_tunic, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.moonkeeper_set), Elements.EARTH, false, 36, 96, 7, 6, 4, 60, 0, 0, 40, 0, 40, 0, R.drawable.armor_moonkeeper_set, 45500, 23500, 0, 0, 5055, 10111, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.wind_witch_suit), Elements.AIR, false, 21, 96, 15, 6, 4, 140, 100, 0, 140, 0, 0, 140, R.drawable.armor_wind_witch_suit, 0, 0, 3900, 2340, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.tunic_of_ending_nights), Elements.NEUTRAL, true, 21, 96, 15, 3, 2, 150, 0, 140, 100, 0, 0, 0, R.drawable.armor_tunic_of_ending_nights, 0, 0, 4500, 2700, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.light_spellmaster_suit), Elements.LIGHT, false, 100, 100, 1, 6, 3, 110, 80, 80, 0, 0, 0, 0, R.drawable.armor_light_spellmaster_suit, 225000, 115000, 0, 0, 25000, 50000, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.purple_witch_suit), Elements.DARKNESS, false, 22, 101, 15, 3, 3, 50, 0, 30, 0, 0, 0, 0, R.drawable.armor_purple_witch_suit, 36500, 18500, 0, 0, 4055, 8111, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop), str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.arcane_mage_suit), Elements.NEUTRAL, true, 30, 101, 10, 5, 3, 110, 0, 120, 80, 0, 80, 100, R.drawable.armor_arcane_mage_suit, 2500000, 1280000, 0, 0, 277777, 555555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.winter_witch_armor), Elements.WATER, true, 30, 101, 10, 3, 2, 80, 100, 100, 0, 0, 0, 0, R.drawable.armor_winter_witch_armor, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.special_antiheros_set), Elements.AIR, false, 25, 103, 13, 8, 5, 60, 70, 0, 70, 0, 0, 0, R.drawable.armor_antiheros_set, 75000, 38500, 0, 0, 8333, 16666, List.of(str(context, R.string.pvp_silver_purple_chest))));
        armorList.add(new Armor(str(context, R.string.gorgons_skin), Elements.FIRE, false, 25, 103, 13, 6, 3, 100, 0, 0, 35, 0, 0, 60, R.drawable.armor_gorgons_skin, 75000, 38500, 0, 0, 8333, 16666, List.of(str(context, R.string.pvp_silver_purple_chest))));
        armorList.add(new Armor(str(context, R.string.antiheros_set), Elements.AIR, false, 33, 104, 9, 7, 5, 65, 60, 0, 60, 65, 0, 0, R.drawable.armor_antiheros_set, 43000, 22000, 0, 0, 4777, 9555, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.liquid_assassin_suit), Elements.WATER, false, 34, 107, 9, 6, 3, 60, 60, 0, 60, 0, 60, 60, R.drawable.armor_liquid_assassin_suit, 146000, 75500, 0, 0, 16222, 32444, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.crow_set), Elements.DARKNESS, false, 38, 110, 8, 6, 2, 80, 50, 0, 0, 0, 50, 0, R.drawable.armor_crow_set, 81000, 41500, 0, 0, 9000, 18000, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.light_champion_set), Elements.LIGHT, false, 110, 110, 1, 4, 4, 50, 100, 0, 0, 100, 100, 100, R.drawable.armor_light_champion_set, 322000, 165000, 0, 0, 35777, 71555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.tunic_of_endless_justice), Elements.LIGHT, false, 24, 110, 15, 5, 3, 180, 100, 130, 0, 0, 0, 100, R.drawable.armor_tunic_of_endless_justice, 0, 0, 3900, 2340, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.barrenlands_banditin), Elements.NEUTRAL, false, 24, 110, 15, 2, 3, 100, 0, 150, 190, 120, 0, 0, R.drawable.armor_barrenlands_banditin, 0, 0, 3600, 2160, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.spider_exoskeleton), Elements.DARKNESS, false, 27, 111, 13, 6, 3, 100, 0, 0, 35, 100, 0, 0, R.drawable.armor_spider_exoskeleton, 75000, 38500, 0, 0, 8333, 16666, List.of(str(context, R.string.pvp_silver_purple_chest))));
        armorList.add(new Armor(str(context, R.string.bacoon_set), Elements.WATER, false, 34, 115, 10, 4, 3, 65, 50, 0, 0, 0, 60, 0, R.drawable.armor_bacoon_set, 68500, 35000, 0, 0, 7611, 15222, List.of(str(context, R.string.chapter_five_random_drop), str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.swamp_witch_suit), Elements.NEUTRAL, false, 34, 115, 10, 4, 4, 110, 130, 140, 80, 0, 0, 0, R.drawable.armor_swamp_witch_suit, 1110000, 571000, 0, 0, 123333, 246666, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.tunic_of_frozen_voices), Elements.WATER, true, 25, 115, 15, 0, 0, 170, 0, 140, 90, 0, 0, 150, R.drawable.armor_tunic_of_frozen_voices, 0, 0, 3900, 2340, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.scarlet_shadowweaver), Elements.DARKNESS, false, 25, 115, 15, 4, 6, 130, 0, 130, 130, 0, 0, 0, R.drawable.armor_scarlet_shadowweaver, 0, 0, 4500, 2700, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.special_crow_set), Elements.DARKNESS, false, 29, 119, 13, 6, 4, 80, 70, 0, 0, 60, 60, 0, R.drawable.armor_special_crow_set, 75000, 38500, 0, 0, 8333, 16666, List.of(str(context, R.string.pvp_silver_purple_chest))));
        armorList.add(new Armor(str(context, R.string.death_stalker), Elements.DARKNESS, false, 26, 119, 15, 2, 0, 160, 0, 110, 140, 0, 0, 150, R.drawable.armor_death_stalker, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.innkeepers_costume), Elements.NEUTRAL, false, 26, 119, 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_innkeepers_costume, 0, 0, 3500, 2100, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.vest_of_ominous_comrades), Elements.NEUTRAL, false, 26, 119, 15, 8, 0, 100, 100, 0, 0, 100, 100, 0, R.drawable.armor_vest_of_ominous_comrades, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.vest_of_ominous_jesters), Elements.NEUTRAL, false, 26, 119, 15, 0, 12, 100, 0, 100, 100, 0, 0, 100, R.drawable.armor_vest_of_ominous_jesters, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.dark_lord_set), Elements.DARKNESS, false, 45, 120, 7, 6, 2, 130, 70, 0, 70, 0, 0, 50, R.drawable.armor_dark_lord_set, 583000, 300000, 0, 0, 64777, 129555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.firestorm_armor), Elements.FIRE, true, 50, 122, 6, 6, 2, 120, 0, 0, 30, 60, 0, 50, R.drawable.armor_firestorm_armor, 418000, 215000, 0, 0, 46444, 92888, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.jungle_master), Elements.EARTH, false, 50, 122, 6, 4, 5, 80, 0, 80, 0, 0, 80, 100, R.drawable.armor_jungle_master, 279000, 143000, 0, 0, 31000, 62000, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.barrenlands_bandit), Elements.NEUTRAL, false, 27, 124, 15, 2, 3, 60, 210, 0, 130, 0, 150, 0, R.drawable.armor_barrenlands_bandit, 0, 0, 3600, 2160, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.robe_of_eternal_fire), Elements.FIRE, false, 27, 124, 15, 4, 3, 180, 120, 0, 0, 50, 0, 120, R.drawable.armor_robe_of_eternal_fire, 0, 0, 3900, 2340, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.penumbra_suit_plus), Elements.DARKNESS, false, 26, 125, 16, 6, 4, 140, 0, 50, 0, 0, 0, 60, R.drawable.armor_penumbra_suit, 250000, 128000, 0, 0, 27777, 55555, List.of(str(context, R.string.pvp_golden_blue_chest))));
        armorList.add(new Armor(str(context, R.string.heros_set_plus), Elements.AIR, false, 28, 128, 15, 15, 2, 40, 100, 0, 0, 0, 100, 100, R.drawable.armor_heros_set, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.tunic_of_twisted_illusions), Elements.EARTH, false, 28, 128, 15, 4, 8, 100, 0, 80, 100, 0, 0, 80, R.drawable.armor_tunic_of_twisted_illusions, 0, 0, 1500, 901, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.dark_lord_set_plus), Elements.DARKNESS, false, 45, 131, 8, 6, 4, 130, 80, 0, 80, 0, 0, 80, R.drawable.armor_dark_lord_set, 976000, 501000, 0, 0, 108444, 216888, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.robe_of_distant_visions), Elements.EARTH, false, 29, 133, 15, 4, 3, 170, 0, 130, 0, 0, 0, 160, R.drawable.armor_robe_of_distant_visions, 0, 0, 3900, 2340, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.bacoon_set_plus), Elements.WATER, false, 28, 135, 16, 5, 5, 75, 60, 0, 0, 0, 80, 60, R.drawable.armor_bacoon_set, 182000, 93500, 0, 0, 20222, 40444, List.of(str(context, R.string.pvp_golden_blue_chest))));
        armorList.add(new Armor(str(context, R.string.firestorm_armor_plus), Elements.FIRE, true, 28, 135, 16, 8, 3, 80, 80, 0, 50, 70, 0, 70, R.drawable.armor_firestorm_armor, 250000, 128000, 0, 0, 27777, 55555, List.of(str(context, R.string.pvp_golden_blue_chest))));
        armorList.add(new Armor(str(context, R.string.steelblow_armor_plus), Elements.AIR, false, 28, 135, 16, 9, 5, 65, 70, 0, 80, 0, 0, 0, R.drawable.armor_steelblow_armor, 250000, 128000, 0, 0, 27777, 55555, List.of(str(context, R.string.pvp_golden_blue_chest))));
        armorList.add(new Armor(str(context, R.string.swamp_ranger_suit), Elements.NEUTRAL, true, 40, 136, 10, 5, 3, 70, 100, 0, 100, 130, 0, 130, R.drawable.armor_swamp_ranger_suit, 1470000, 758000, 0, 0, 163333, 326666, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.vest_of_phantoms_keeper), Elements.DARKNESS, false, 30, 138, 15, 4, 2, 100, 120, 0, 120, 0, 0, 0, R.drawable.armor_vest_of_phantoms_keeper, 0, 0, 2500, 1500, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.enchanted_nutcracker), Elements.NEUTRAL, false, 30, 138, 15, 2, 4, 100, 120, 120, 0, 0, 0, 0, R.drawable.armor_enchanted_nutcracker, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.blaze_armor), Elements.FIRE, true, 30, 138, 15, 6, 3, 100, 130, 0, 0, 0, 140, 0, R.drawable.armor_blaze_armor, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.steelblow_armor), Elements.AIR, false, 45, 142, 9, 8, 4, 60, 65, 0, 75, 0, 0, 0, R.drawable.armor_steelblow_armor, 100000, 51500, 0, 0, 11111, 22222, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.bankers_coat_plus), Elements.DARKNESS, false, 32, 147, 15, 5, 4, 60, 0, 0, 200, 0, 0, 0, R.drawable.armor_bankers_coat, 1500, 901, 1500, 901, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.cthulhu_set), Elements.NEUTRAL, false, 32, 147, 15, 2, 3, 130, 100, 100, 100, 100, 100, 100, R.drawable.armor_cthulhu_set, 30500000, 15700000, 1500, 901, 3388888, 6777777, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.champion_set), Elements.NEUTRAL, false, 90, 154, 3, 4, 4, 70, 100, 0, 0, 0, 0, 120, R.drawable.armor_champion_set, 170000, 87500, 0, 0, 18888, 37777, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.vest_of_twisted_illusions), Elements.DARKNESS, true, 34, 156, 15, 3, 0, 70, 0, 0, 0, 0, 0, 240, R.drawable.armor_vest_of_twisted_illusions, 0, 0, 2500, 1500, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.fire_moonkeeper_set), Elements.FIRE, true, 35, 161, 15, 6, 3, 100, 0, 110, 100, 0, 0, 110, R.drawable.armor_fire_moonkeeper_set, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.robe_of_imminent_fires), Elements.FIRE, false, 35, 161, 15, 3, 0, 140, 0, 110, 0, 190, 0, 130, R.drawable.armor_robe_of_imminent_fires, 0, 0, 3900, 2340, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.tunic_of_distant_ancestors), Elements.EARTH, false, 36, 165, 15, 3, 2, 130, 0, 0, 200, 170, 0, 0, R.drawable.armor_tunic_of_distant_ancestors, 0, 0, 3900, 2340, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.vest_of_blessed_glory), Elements.LIGHT, false, 36, 165, 15, 3, 2, 105, 170, 0, 100, 0, 130, 160, R.drawable.armor_vest_of_blessed_glory, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.desert_rogue_suit), Elements.NEUTRAL, false, 50, 170, 10, 5, 2, 70, 0, 0, 130, 130, 0, 160, R.drawable.armor_desert_rogue_suit, 646000, 332000, 0, 0, 71777, 143555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.tunic_of_the_relentless), Elements.WATER, true, 38, 174, 15, 6, 4, 155, 70, 80, 0, 0, 0, 90, R.drawable.armor_tunic_of_the_relentless, 2830000, 1450000, 0, 0, 314444, 628888, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.special_dragon_vest), Elements.FIRE, false, 180, 180, 1, 6, 4, 80, 70, 90, 70, 90, 0, 0, R.drawable.armor_dragon_vest, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.easterminator), Elements.NEUTRAL, false, 40, 184, 15, 5, 18, 40, 50, 0, 50, 0, 50, 0, R.drawable.armor_easterminator, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.time_patroller), Elements.NEUTRAL, false, 56, 190, 10, 2, 2, 300, 0, 0, 0, 0, 0, 0, R.drawable.armor_time_patroller_suit, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        armorList.add(new Armor(str(context, R.string.iron_widow), Elements.NEUTRAL, true, 56, 190, 10, 0, 0, 70, 140, 0, 0, 160, 140, 160, R.drawable.armor_iron_widow, 3350000, 1720000, 0, 0, 372222, 744444, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.umbranian_tanker), Elements.NEUTRAL, false, 80, 195, 6, 3, 2, 0, 60, 0, 0, 60, 60, 0, R.drawable.armor_umbranian_tanker, 117000, 60500, 0, 0, 13000, 26000, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.greenfang_ensemble), Elements.DARKNESS, false, 48, 220, 15, 0, 4, 100, 0, 0, 135, 0, 135, 0, R.drawable.armor_greenfang_ensemble, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.interrogator), Elements.WATER, false, 51, 234, 15, 0, 0, 110, 0, 0, 0, 120, 140, 160, R.drawable.armor_interrogator, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.protector_of_the_oracle), Elements.EARTH, false, 51, 234, 15, -1, 0, 120, 0, 70, 0, 0, 170, 100, R.drawable.armor_protector_of_the_oracle, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.warden_of_demons), Elements.DARKNESS, false, 52, 239, 15, 0, 0, 80, 140, 0, 0, 140, 145, 0, R.drawable.armor_warden_of_demons, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        armorList.add(new Armor(str(context, R.string.destroyer_of_the_corrupted), Elements.LIGHT, false, 52, 239, 15, 0, 0, 80, 100, 0, 0, 140, 175, 0, R.drawable.armor_destroyer_of_the_corrupted, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.vanquisher), Elements.AIR, false, 55, 253, 15, 0, 0, 70, 160, 0, 0, 0, 0, 170, R.drawable.armor_vanquisher, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.shop))));
        armorList.add(new Armor(str(context, R.string.cataclysmic_plate), Elements.FIRE, false, 56, 257, 15, -1, 0, 55, 140, 0, 0, 170, 220, 0, R.drawable.armor_cataclysmic_plate, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.shop))));

        // Rings
        ringList.add(new Ring(str(context, R.string.ring_of_agility), Elements.NEUTRAL, 0, 0, 2, 2, 0, 20, 0, 0, 0, 0, 0, R.drawable.ring_ring_of_agility, 2100, 1100, 0, 0, 233, 466, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.shaman_ring), Elements.NEUTRAL, 0, 0, 2, 2, 20, 0, 0, 0, 0, 0, 0, R.drawable.ring_shaman_narya_ring, 4800, 2500, 0, 0, 533, 1066, List.of(str(context, R.string.chapter_two_random_drop), str(context, R.string.chapter_three_random_drop), str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.narya), Elements.FIRE, 0, 0, 2, 0, 20, 0, 0, 0, 20, 0, 0, R.drawable.ring_shaman_narya_ring, 6000, 3100, 0, 0, 666, 1333, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.nazguh_ring), Elements.DARKNESS, 0, 0, 0, 0, 20, 0, 20, 0, 0, 0, 0, R.drawable.ring_nazguh_ring, 5600, 2900, 0, 0, 622, 1244, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.cleric_ring), Elements.LIGHT, 0, 0, 1, 1, 20, 0, 20, 0, 0, 0, 0, R.drawable.ring_cleric_ring, 6000, 3100, 0, 0, 666, 1333, List.of(str(context, R.string.chapter_three_random_drop))));
        ringList.add(new Ring(str(context, R.string.supersonic_ring), Elements.NEUTRAL, 0, -90, 70, 0, -90, 0, 0, 0, 0, 0, 0, R.drawable.ring_supersonic_ring, 3900, 2000, 0, 0, 433, 866, List.of(str(context, R.string.chapter_one_random_drop))));
        ringList.add(new Ring(str(context, R.string.swordsmans_ring), Elements.AIR, 0, 0, 5, 2, 0, 50, 0, 0, 0, 0, 0, R.drawable.ring_swordsmans_ring, 3200, 1600, 0, 0, 355, 711, List.of(str(context, R.string.dungeon_10))));
        ringList.add(new Ring(str(context, R.string.spellmasters_ring), Elements.FIRE, 0, 0, 4, 2, 70, 0, 60, 0, 40, 0, 0, R.drawable.ring_spellmasters_ring, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        ringList.add(new Ring(str(context, R.string.nenya), Elements.WATER, 0, 0, 4, 2, 50, 0, 0, 40, 0, 0, 50, R.drawable.ring_nenya, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.chapter_four_random_drop))));
        ringList.add(new Ring(str(context, R.string.sea_wave_ring), Elements.WATER, 0, 0, 5, 3, 70, 60, 70, 0, 0, 70, 0, R.drawable.ring_sea_wave_ring, 440000, 226000, 0, 0, 48888, 97777, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.dragon_fang_ring), Elements.NEUTRAL, 0, 0, 4, 4, 60, 70, 0, 0, 70, 70, 0, R.drawable.ring_dragon_fang_ring, 335000, 172000, 0, 0, 37222, 74444, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.blue_ring), Elements.NEUTRAL, 0, 0, 4, 2, 60, 0, 70, 70, 0, 0, 70, R.drawable.ring_blue_ring, 316000, 162000, 0, 0, 35111, 70222, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.nightshade), Elements.NEUTRAL, 0, 0, 7, 9, 220, 0, 0, 0, 0, 0, 0, R.drawable.ring_nightshade, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.falcon_ring), Elements.AIR, 5, 0, 8, 3, 80, 100, 0, 100, 0, 100, 0, R.drawable.ring_falcon_ring, 1540000, 792000, 0, 0, 171111, 342222, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.cursed_amethyst_ring), Elements.DARKNESS, 5, -40, 4, 3, 120, 0, 140, 0, 0, 0, 0, R.drawable.ring_cursed_amethyst_ring, 0, 0, 4000, 2400, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.bunny_ring), Elements.NEUTRAL, 5, 0, 0, 10, 70, 0, 70, 60, 0, 0, 0, R.drawable.ring_bunny_ring, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.eastercreep), Elements.NEUTRAL, 5, 0, 0, 10, 60, 60, 0, 0, 0, 70, 0, R.drawable.ring_eastercreep, 0, 0, 2500, 1500, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.pirouette_pointe), Elements.NEUTRAL, 10, 0, 5, 7, 40, 0, 0, 0, 0, 0, 0, R.drawable.ring_pirouette_pointe, 0, 0, 1500, 901, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.bloodforged_charm_earring), Elements.NEUTRAL, 10, 0, 0, 14, 110, 110, 0, 100, 0, 0, 0, R.drawable.ring_bloodforged_charm, 0, 0, 5500, 3301, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.moonbound_necklace), Elements.NEUTRAL, 10, 0, 0, 14, 110, 0, 0, 0, 110, 150, 0, R.drawable.ring_moonbound_necklace, 0, 0, 5500, 3301, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.skybreaker_band), Elements.NEUTRAL, 10, 0, 0, 14, 110, 0, 120, 0, 0, 0, 120, R.drawable.ring_skybreaker_band, 0, 0, 5500, 3301, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.warlocks_ring), Elements.DARKNESS, 13, 0, 4, 2, 50, 0, 35, 35, 0, 0, 0, R.drawable.ring_warlock_ring, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        ringList.add(new Ring(str(context, R.string.ring_of_protection), Elements.EARTH, 14, 0, 2, 2, 15, 0, 10, 0, 0, 10, 0, R.drawable.ring_ring_of_protection, 5600, 2900, 0, 0, 622, 1244, List.of(str(context, R.string.chapter_three_random_drop), str(context, R.string.chapter_four_random_drop))));
        ringList.add(new Ring(str(context, R.string.jellybunny), Elements.NEUTRAL, 15, 0, 0, 10, 60, 0, 60, 70, 0, 0, 60, R.drawable.ring_jellybunny, 0, 0, 2500, 1500, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.verdant_ring), Elements.EARTH, 16, 0, 4, 3, 70, 0, 70, 0, 0, 70, 0, R.drawable.ring_verdant_ring, 214000, 110000, 0, 0, 23777, 47555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.steelblow_ring), Elements.AIR, 20, 0, 12, 3, 40, 50, 0, 30, 50, 0, 0, R.drawable.ring_steelblow_ring, 86000, 44000, 0, 0, 9555, 19111, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.dragon_orb_ring), Elements.FIRE, 20, 0, 5, 2, 60, 0, 0, 0, 60, 0, 60, R.drawable.ring_dragon_orb_ring, 125000, 64000, 0, 0, 13888, 27777, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.volcano_ring), Elements.FIRE, 20, 0, 3, 2, 75, 65, 65, 75, 0, 0, 0, R.drawable.ring_volcano_ring, 510000, 262000, 0, 0, 56666, 113333, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.all_seeing_ring), Elements.DARKNESS, 20, 0, 4, 2, 60, 60, 60, 0, 70, 0, 0, R.drawable.ring_all_seeing_ring, 268000, 138000, 0, 0, 29777, 59555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.trickytreat), Elements.NEUTRAL, 20, 0, 3, 3, 30, 20, 20, 20, 20, 20, 20, R.drawable.ring_trickytreat, 0, 0, 500, 300, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.white_pearl_ring), Elements.LIGHT, 30, 0, 4, 3, 60, 0, 70, 0, 0, 0, 0, R.drawable.ring_white_pearl_ring, 70000, 36000, 0, 0, 7777, 15555, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.witchdoctor_ring), Elements.DARKNESS, 30, 0, 4, 2, 60, 0, 0, 50, 0, 50, 0, R.drawable.ring_witchdoctor_ring, 102000, 52500, 0, 0, 11333, 22666, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.cait), Elements.NEUTRAL, 30, 0, 6, 4, 80, 0, 80, 80, 0, 0, 0, R.drawable.ring_cait, 0, 0, 2000, 1200, 27777, 55555, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.spektator), Elements.NEUTRAL, 30, 0, 5, 3, 110, 80, 80, 80, 0, 0, 80, R.drawable.ring_spektator, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.ancient_warden), Elements.WATER, 30, 0, 4, 6, 100, 100, 100, 0, 0, 140, 0, R.drawable.ring_ancient_warden, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.ghostwalker), Elements.NEUTRAL, 30, 150, -2, 0, 40, 0, 0, 0, 0, 0, 0, R.drawable.ring_ghostwalker, 0, 0, 2500, 1500, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.golden_guardian), Elements.NEUTRAL, 30, 0, 0, 0, 100, 0, 0, 0, 0, 130, 100, R.drawable.ring_golden_guardian, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.tidecaller_ring), Elements.WATER, 30, 0, 4, 6, 100, 0, 0, 100, 100, 0, 100, R.drawable.ring_tidecaller_ring, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.dragon_eye_ring), Elements.EARTH, 36, 0, 4, 2, 60, 0, 0, 0, 0, 50, 70, R.drawable.ring_dragon_eye_ring, 125000, 64000, 0, 0, 13888, 27777, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.ring_of_light), Elements.LIGHT, 40, 0, 4, 4, 60, 50, 0, 50, 0, 50, 0, R.drawable.ring_ring_of_light, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.achievement))));
        ringList.add(new Ring(str(context, R.string.cuteulhu), Elements.NEUTRAL, 40, 0, 4, 4, 100, 0, 90, 0, 90, 90, 0, R.drawable.ring_cuteulhu, 0, 0, 3000, 1801, 27777, 55555, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.whisper_of_fire), Elements.FIRE, 40, 0, 5, 3, 100, 90, 90, 0, 90, 0, 0, R.drawable.ring_whisper_of_fire, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.creepy_crawler), Elements.NEUTRAL, 40, 0, 3, 3, 90, 40, 40, 40, 40, 40, 40, R.drawable.ring_creepy_crawler, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.whisper_of_death), Elements.DARKNESS, 40, 0, 5, 3, 100, 0, 90, 90, 0, 90, 0, R.drawable.ring_whisper_of_death, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop), str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.magic_cog), Elements.NEUTRAL, 40, 0, 5, 3, 210, 0, 0, 0, 0, 0, 0, R.drawable.ring_magic_cog, 5, 2, 0, 0, 0, 1, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.vindicator_guardian), Elements.AIR, 40, 0, 5, 4, 100, 90, 0, 100, 0, 0, 110, R.drawable.ring_vindicator_guardian, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.weekly_dungeon_golden_chest))));
        ringList.add(new Ring(str(context, R.string.whisper_of_light), Elements.LIGHT, 40, 0, 5, 3, 100, 90, 90, 0, 0, 120, 0, R.drawable.ring_whisper_of_light, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.phoenix), Elements.FIRE, 40, 0, 2, 3, 100, 0, 0, 90, 0, 130, 90, R.drawable.ring_phoenix, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));
        ringList.add(new Ring(str(context, R.string.vina), Elements.EARTH, 50, 0, 2, 3, 100, 100, 100, 0, 0, 0, 0, R.drawable.ring_vina, 0, 0, 3500, 2100, 27777, 55555, List.of(str(context, R.string.shop))));

        // Classes
        classList.add(new CharacterClass(ClassNames.RANGER, 20, 0, 35, 0, 0, 25, 25, 0, 0, 5, R.drawable.class_ranger));
        classList.add(new CharacterClass(ClassNames.PRIEST, 20, 20, 0, -30, 0, 0, 0, 20, 3, 4, R.drawable.class_priest));
        classList.add(new CharacterClass(ClassNames.WARLOCK, 0, 30, 25, 20, 0, 20, 40, 0, 4, 0, R.drawable.class_warlock));
        classList.add(new CharacterClass(ClassNames.BLACK_MAGE, 0, 30, 0, 20, -30, 0, 0, 30, 4, 2, R.drawable.class_black_mage));
        classList.add(new CharacterClass(ClassNames.ROGUE, 0, 0, -10, 50, -30, -30, 0, 0, 6, 5, R.drawable.class_rogue));
        classList.add(new CharacterClass(ClassNames.THIEF, 0, 0, 20, 40, 20, 0, 0, -30, 6, 7, R.drawable.class_thief));
        classList.add(new CharacterClass(ClassNames.WARRIOR, 35, 0, 40, 0, 30, 30, 0, -40, 0, 0, R.drawable.class_warrior));
        classList.add(new CharacterClass(ClassNames.MAGE, 0, 30, 0, 0, -30, 20, 0, 30, 2, 4, R.drawable.class_mage));
        classList.add(new CharacterClass(ClassNames.DRUID, 0, 30, 0, 0, 0, 0, 40, 30, 3, 6, R.drawable.class_druid));
        classList.add(new CharacterClass(ClassNames.PALADIN, 20, 20, 20, 0, 20, 20, 0, 0, 0, 0, R.drawable.class_paladin));
        classList.add(new CharacterClass(ClassNames.HIGH_MAGE, 0, 35, 20, 0, -30, 0, 0, 30, 6, 4, R.drawable.class_high_mage));
        classList.add(new CharacterClass(ClassNames.ELITE_WARRIOR, 40, 0, 35, 0, 35, 35, 0, -40, 0, 5, R.drawable.class_elite_warrior));
        classList.add(new CharacterClass(ClassNames.WITCHDOCTOR, 0, 30, 0, 25, 0, 0, 0, 25, 5, 6, R.drawable.class_witchdoctor));

        // Enemies
        enemyList.add(new Enemy(str(context, R.string.enemy_spider), 9, 18, 0, 0, 2.8, 21.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_noob_knife_guard), 13, 18, 0, 2, 5.0, 34.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_zombie));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_guard), 14, 24, 0, 0, 6.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_zombie));
        enemyList.add(new Enemy(str(context, R.string.enemy_armored_enraged_skeleton), 15, 19, 0, 0, 5.62, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_jumping_fire_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_b_weak), 15, 22, 0, 0, 3.2, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_dark_ghost), 20, 25, 0, 0, 3.0, 16.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_dark_ghost));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_knife_guard), 20, 15, 0, 2, 5.0, 34.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_zombie));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_elite_guard), 26, 24, 0, 0, 8.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_zombie));
        enemyList.add(new Enemy(str(context, R.string.enemy_beetle_anubis), 30, 22, 0, 0, 4.2, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_beetle_anubis));
        enemyList.add(new Enemy(str(context, R.string.enemy_beetle_anubis_red), 30, 22, 0, 0, 4.2, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_beetle_anubis_red));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_b), 30, 22, 0, 0, 3.2, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_duck), 30, 22, 0, 0, 9.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_duck));
        enemyList.add(new Enemy(str(context, R.string.enemy_dark_tanker), 40, 35, 0, 0, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_dark_tanker));
        enemyList.add(new Enemy(str(context, R.string.enemy_mummy), 40, 23, 0, 0, 6.0, 29.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_mummy));
        enemyList.add(new Enemy(str(context, R.string.enemy_skeleton), 40, 23, 0, 0, 6.0, 29.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_fire), 40, 24, 0, 0, 3.2, 25.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_red_guard), 40, 24, 0, 2, 5.0, 30.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_zombie));
        enemyList.add(new Enemy(str(context, R.string.enemy_demonic_spider_blowing_minion), 50, 23, 0, 0, 7.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_blowing_demonic_spider_minion));
        enemyList.add(new Enemy(str(context, R.string.enemy_demonic_spider_minion), 50, 23, 0, 0, 4.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_blowing_demonic_spider_minion));
        enemyList.add(new Enemy(str(context, R.string.enemy_ghost_rat), 60, 25, 0, 0, 6.0, 20.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_rat_ghost));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_blow), 70, 27, 0, 0, 3.9, 30.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_bat), 80, 40, 0, 0, 3.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_bat));
        enemyList.add(new Enemy(str(context, R.string.enemy_harmless_tanker), 80, 0, 0, 0, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_harmless_tanker));
        enemyList.add(new Enemy(str(context, R.string.enemy_miniboss_gigaspider_critter), 80, 30, 0, 0, 3.2, 20.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_miniboss_gigaspider_critter));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_c), 80, 22, 0, 0, 4.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_swamp), 80, 30, 0, 0, 3.2, 30.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_tanker_rage_matryoshka), 80, 40, 0, 0, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_harmless_tanker));
        enemyList.add(new Enemy(str(context, R.string.enemy_armored_skeleton), 90, 25, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_armored_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_dark_skeleton), 90, 20, 0, 0, 5.45, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_dark_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_skeleton_b), 90, 30, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ifrits_disciple));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_skeleton), 90, 30, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ifrits_disciple));
        enemyList.add(new Enemy(str(context, R.string.enemy_tanker_helmet_matryoshka), 90, 30, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_helmet_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_guard_boss), 100, 25, 0, 2, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_zombie_b));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_pilot_mask_skeleton), 110, 30, 0, 0, 5.4, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_nature_skeleton), 110, 15, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_nature_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_demon_head), 120, 55, 0, 0, 3.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_demon_head));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_guard_boss_b), 120, 26, 0, 2, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_zombie_b));
        enemyList.add(new Enemy(str(context, R.string.enemy_guard_tanker_a), 130, 40, 0, 0, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_helmet_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_guard_tanker_b), 130, 30, 0, 0, 5.2, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_helmet_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_jumping_fire_skeleton), 140, 40, 0, 10, 5.4, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_jumping_fire_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_guard_boss_c), 140, 32, 0, 17, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_zombie_c));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_bunny_mask_skeleton), 150, 16, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_pumpkin_mask_skeleton), 150, 30, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_helmet_skeleton), 150, 30, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_helmet_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_skeleton_red_warrior), 150, 30, 0, 150, 5.67, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_skeleton_red_warrior));
        enemyList.add(new Enemy(str(context, R.string.enemy_jumping_skeleton), 160, 25, 0, 0, 5.4, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_jumping_fire_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_buffoon_purple_skeleton), 170, 40, 0, 20, 5.9, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_carnival_buffoon_purple_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_skeleton_master), 170, 50, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ifrits_disciple));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_skeleton_red_mage), 170, 50, 0, 150, 5.67, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_skeleton_red_warrior));
        enemyList.add(new Enemy(str(context, R.string.enemy_carcara), 200, 55, 0, 0, 3.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_carcara));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_bat_adult), 200, 55, 0, 0, 3.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_bat));
        enemyList.add(new Enemy(str(context, R.string.enemy_armored_axe_skeleton), 300, 30, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_jumping_fire_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_vampire), 320, 45, 0, 0, 6.0, 30.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_vampire));
        enemyList.add(new Enemy(str(context, R.string.enemy_water_vampire), 340, 45, 0, 20, 6.18, 30.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_water_vampire));
        enemyList.add(new Enemy(str(context, R.string.enemy_copy_cat), 380, 0, 0, 0, 9.0, 33.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_skeleton_b), 450, 50, 0, 0, 8.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_mini_slime), 500, 0, 0, 0, 4.0, 15.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_mini_slime));
        enemyList.add(new Enemy(str(context, R.string.enemy_pumpkin_rat_skeleton), 500, 40, 0, 0, 7.5, 36.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_pumpkin_rat_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_air_elemental), 600, 0, 0, 0, 2.5, 10.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_air_elemental));
        enemyList.add(new Enemy(str(context, R.string.enemy_armored_ape_faster_weaker), 600, 0, 120, 0, 14.0, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_armored_ape));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_buffoon_green_skeleton), 600, 40, 0, 20, 5.9, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_carnival_buffoon_green_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_captain_mask_skeleton), 600, 100, 0, 0, 5.4, 32.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_salesman_mask_skeleton), 600, 8, 0, 0, 5.4, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_skeleton), 600, 100, 0, 150, 5.67, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_ice_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_ifrits_disciple), 600, 30, 0, 0, 6.24, 30.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ifrits_disciple));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_easter), 600, 42, 0, 0, 5.0, 16.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_easter_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_skeleton_blue_helmet_ice), 600, 100, 0, 150, 5.67, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_skeleton_blue_helmet_ice));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_skeleton_blue_ice), 600, 100, 0, 150, 5.67, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_skeleton_blue_helmet_ice));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_chief), 600, 40, 0, 0, 9.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_zombie_chief));
        enemyList.add(new Enemy(str(context, R.string.enemy_giant_demon_head), 600, 10, 0, 0, 3.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_demon_head));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_skeleton_elite), 800, 150, 0, 250, 5.67, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_ice_skeleton_elite));
        enemyList.add(new Enemy(str(context, R.string.enemy_swamp_monster), 800, 60, 0, 0, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_swamp_monster));
        enemyList.add(new Enemy(str(context, R.string.enemy_shaman_fire), 850, 60, 0, 40, 6.24, 30.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_shaman_fire));
        enemyList.add(new Enemy(str(context, R.string.enemy_shaman_nature), 850, 40, 0, 40, 6.78, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_shaman_nature));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_fighter), 900, 35, 0, 0, 6.5, 38.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_rat_fighter));
        enemyList.add(new Enemy(str(context, R.string.enemy_cthulhu_imp), 1000, 35, 0, 0, 6.0, 40.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_cthulhu_imp));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_elemental), 1000, 20, 0, 0, 4.5, 30.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_fire_elemental));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_fire_mage), 1000, 80, 0, 0, 5.45, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_rat_fire_mage));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_skeleton), 1000, 40, 0, 0, 7.5, 36.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_rat_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_water_mage), 1000, 40, 0, 0, 5.4, 30.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_rat_water_mage));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_fire_bear), 1000, 20, 0, 0, 5.4, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_fire_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_skeleton_blue_mage), 1000, 40, 0, 150, 5.67, 30.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_skeleton_blue_mage));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_lancer), 1100, 60, 0, 0, 5.72, 37.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_rat_lancer));
        enemyList.add(new Enemy(str(context, R.string.enemy_easter_skeleton), 1200, 30, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_easter_green), 1200, 42, 0, 0, 5.0, 16.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_green_easter_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_easter_pink), 1200, 42, 0, 0, 5.0, 16.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_pink_easter_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_gift), 1200, 42, 0, 0, 5.0, 16.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_gift_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_spider_boss), 1250, 42, 0, 0, 4.5, 30.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spider_boss));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_black_mage), 1400, 80, 0, 0, 5.4, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_rat_black_mage));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_rogue_prisoner), 1500, 0, 0, 40, 8.0, 37.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_rat_rogue_prisoner));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_rogue), 1500, 30, 0, 40, 8.48, 37.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_rat_rogue_prisoner));
        enemyList.add(new Enemy(str(context, R.string.enemy_evil_warlock), 2000, 38, 0, 0, 3.0, 30.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_evil_warlock));
        enemyList.add(new Enemy(str(context, R.string.enemy_pumpkin_skeleton), 2000, 80, 0, 50, 7.14, 36.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_pumpkin_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_pumpkin_water_skeleton), 2000, 80, 0, 50, 7.14, 36.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_pumpkin_water_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_zombie_knife_guard_b), 2000, 51, 0, 25, 6.24, 38.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_rat_zombie_knife_guard));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_zombie_knife_guard), 2000, 51, 0, 25, 6.24, 38.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_rat_zombie_knife_guard));
        enemyList.add(new Enemy(str(context, R.string.enemy_giant_spider), 2100, 30, 0, 0, 3.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_giant_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_junina_skeleton_a), 2500, 40, 0, 0, 5.78, 37.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_junina_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_armored_ape), 2800, 0, 120, 0, 11.0, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_armored_ape));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_skeleton_wdg), 3000, 100, 0, 150, 5.67, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_ice_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_pumpkin_zombie_rat), 3000, 40, 0, 0, 6.0, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_pumpkin_rat));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_doll_blond), 3000, 55, 0, 0, 3.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_doll_blond));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_doll_brunette), 3000, 55, 0, 0, 3.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_doll_brunette));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_green_bear_small), 3000, 50, 0, 0, 12.0, 38.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_green_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_purple_bear_small), 3000, 40, 0, 0, 6.3, 38.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_purple_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_captain_mask_necromancer), 3500, 180, 0, 70, 0.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_carnival_captain_mask_necromancer));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_necromancer), 3500, 180, 0, 70, 0.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ice_necromancer));
        enemyList.add(new Enemy(str(context, R.string.enemy_junina_skeleton_b), 3500, 40, 0, 0, 7.88, 37.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_junina_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_junina_skeleton_poison_thrower), 3500, 15, 0, 0, 7.5, 37.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_junina_skeleton_poison_thrower));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_poison_thrower), 3500, 15, 0, 0, 7.5, 37.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_rat_poison_thrower));
        enemyList.add(new Enemy(str(context, R.string.enemy_time_patroller), 3500, 240, 0, 0, 6.5, 38.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_time_patroller));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_pink_bear_small), 3500, 100, 0, 0, 10.0, 38.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_pink_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_grinch), 3700, 0, 250, 0, 11.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_grinch));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_skeleton_elite_wdg), 4000, 150, 0, 250, 5.67, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_ice_skeleton_elite));
        enemyList.add(new Enemy(str(context, R.string.enemy_junina_skeleton_dark_shadow), 4000, 20, 0, 0, 6.06, 29.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_junina_skeleton_poison_thrower));
        enemyList.add(new Enemy(str(context, R.string.enemy_junina_skeleton_fire), 4000, 19, 0, 0, 6.18, 29.5, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_junina_skeleton_poison_thrower));
        enemyList.add(new Enemy(str(context, R.string.enemy_junina_skeleton_knife_guard), 4000, 51, 0, 0, 7.2, 29.5, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_junina_skeleton_poison_thrower));
        enemyList.add(new Enemy(str(context, R.string.enemy_junina_skeleton_shadow), 4000, 100, 0, 0, 6.0, 29.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_junina_skeleton_poison_thrower));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_warrior), 4000, 70, 0, 0, 6.18, 39.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_rat_warrior));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_jumping_green_bear), 4000, 70, 0, 0, 9.0, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_green_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_pink_bear), 4000, 100, 0, 0, 10.0, 29.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_pink_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_purple_bear_ez), 4000, 40, 0, 0, 5.67, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_purple_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_purple_bear), 4000, 100, 0, 0, 5.67, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_purple_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_armored_ape_faster), 4500, 0, 120, 0, 14.0, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_armored_ape));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_skeleton_elite), 4500, 50, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ifrits_disciple));
        enemyList.add(new Enemy(str(context, R.string.enemy_jumping_fire_skeleton_elite), 4500, 40, 0, 10, 7.2, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_jumping_fire_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_jumping_skeleton_elite), 4500, 60, 0, 0, 7.2, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_jumping_fire_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_mushroom_spitter), 4500, 80, 0, 0, 6.0, 0.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_mushroom_spitter));
        enemyList.add(new Enemy(str(context, R.string.enemy_white_ape), 4700, 0, 250, 0, 11.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_white_ape));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_ape), 5000, 0, 250, 0, 11.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_white_ape));
        enemyList.add(new Enemy(str(context, R.string.enemy_pumpkin_skeleton_scythe), 5000, 140, 0, 50, 7.0, 36.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_pumpkin_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_pumpkin_water_skeleton_scythe), 5000, 85, 0, 102, 7.0, 36.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_pumpkin_water_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_zombie_rat), 5000, 100, 0, 0, 6.0, 32.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_zombie_rat));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_skeleton_tanker), 5500, 190, 0, 80, 5.4, 33.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ice_skeleton_tanker));
        enemyList.add(new Enemy(str(context, R.string.enemy_nature_skeleton_elite), 5500, 45, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_nature_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_snow_monster), 6550, 80, 0, 0, 5.0, 25.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_snow_monster));
        enemyList.add(new Enemy(str(context, R.string.enemy_dark_skeleton_elite), 7500, 80, 0, 0, 5.45, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_dark_skeleton));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_skeleton_red_mage_elite), 7500, 80, 0, 150, 5.73, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_skeleton_red_warrior));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_rat_buffoon_green), 8000, 100, 0, 25, 5.46, 39.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_carnival_rat_buffoon_green));
        enemyList.add(new Enemy(str(context, R.string.enemy_carnival_rat_buffoon_purple), 8000, 100, 0, 25, 5.46, 39.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_carnival_rat_buffoon_purple));
        enemyList.add(new Enemy(str(context, R.string.enemy_rat_zombie_knife_guard_elite), 8000, 100, 0, 25, 6.24, 39.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_rat_zombie_knife_guard_elite));
        enemyList.add(new Enemy(str(context, R.string.enemy_tanker_elite_matryoshka), 8000, 70, 0, 0, 5.25, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_harmless_tanker));
        enemyList.add(new Enemy(str(context, R.string.enemy_guard_tanker_elite), 9000, 70, 0, 0, 5.25, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_harmless_tanker));
        enemyList.add(new Enemy(str(context, R.string.enemy_shaman_fire_elite), 9000, 60, 0, 80, 6.24, 30.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_shaman_fire));
        enemyList.add(new Enemy(str(context, R.string.enemy_shaman_nature_elite), 9000, 50, 0, 80, 9.04, 34.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_shaman_nature));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_skeleton_red_warrior_elite), 9000, 70, 0, 150, 5.51, 25.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_skeleton_red_warrior));
        enemyList.add(new Enemy(str(context, R.string.enemy_mummy_blue_cosmos), 9500, 100, 0, 0, 5.4, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_mummy));
        enemyList.add(new Enemy(str(context, R.string.enemy_mummy_gold_cosmos), 9500, 70, 0, 0, 5.67, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_mummy));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_necromancer_wdg), 10000, 180, 0, 70, 0.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ice_necromancer));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_fire_bear_elite), 10000, 30, 0, 0, 5.82, 31.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_fire_bear));
        enemyList.add(new Enemy(str(context, R.string.enemy_mush_vacuum), 12000, 40, 0, 0, 6.0, 0.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_mush_vacuum));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_gift_vacuum), 12000, 40, 0, 0, 6.0, 0.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_mush_vacuum));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_skeleton_tanker_wdg), 15000, 190, 0, 80, 5.4, 33.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ice_skeleton_tanker));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_skeleton_blue_tanker), 15000, 190, 0, 150, 5.67, 33.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_xmas_skeleton_blue_helmet_ice));
        enemyList.add(new Enemy(str(context, R.string.enemy_evil_warlock_boss), 24000, 80, 0, 0, 3.0, 35.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_evil_warlock));
        enemyList.add(new Enemy(str(context, R.string.enemy_mush_vacuum_wdg), 36000, 40, 0, 0, 6.0, 0.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_mush_vacuum));
        enemyList.add(new Enemy(str(context, R.string.enemy_wizard), 500000, 0, 0, 0, 4.0, 33.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_wizard));
        enemyList.add(new Enemy(str(context, R.string.enemy_alien), 9999999, 0, 0, 36, 7.42, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_alien));
        enemyList.add(new Enemy(str(context, R.string.enemy_kingptain_dungeon43), 999999999, 600, 0, 8000, 8.0, 36.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_kingptain));
        enemyList.add(new Enemy(str(context, R.string.enemy_priest_minion), 999999999, 28, 0, 0, 8.48, 33.0, "", "", R.drawable.enemy_priest_minion));
        enemyList.add(new Enemy(str(context, R.string.enemy_time_patroller_dungeon41), 999999999, 0, 0, 36, 7.42, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_time_patroller_boss));
        enemyList.add(new Enemy(str(context, R.string.enemy_time_patroller_dungeon40), 999999999, 6, 0, 36, 7.42, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_time_patroller_boss));
        enemyList.add(new Enemy(str(context, R.string.enemy_ape_demon), 999999999, 0, 80, 0, 15.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_ape));
        enemyList.add(new Enemy(str(context, R.string.enemy_ape_junina), 999999999, 0, 80, 0, 15.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_ape_junina));
        enemyList.add(new Enemy(str(context, R.string.enemy_ape_santa), 999999999, 0, 80, 0, 15.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_ape));
        enemyList.add(new Enemy(str(context, R.string.enemy_ape_skeleton), 999999999, 0, 5, 0, 8.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_skeleton_ape));
        enemyList.add(new Enemy(str(context, R.string.enemy_armademon_easter), 999999999, 0, 20, 0, 9.0, 16.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_easter_armademon));
        enemyList.add(new Enemy(str(context, R.string.enemy_armademon), 999999999, 0, 20, 0, 9.0, 16.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_armademon));
        enemyList.add(new Enemy(str(context, R.string.enemy_armadillo), 999999999, 0, 1, 0, 7.0, 12.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_armadillo));
        enemyList.add(new Enemy(str(context, R.string.enemy_armadimp), 999999999, 0, 7, 0, 7.0, 12.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_armadillo));
        enemyList.add(new Enemy(str(context, R.string.enemy_beholder_witch), 999999999, 60, 0, 0, 5.2, 1.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_beholder_witch));
        enemyList.add(new Enemy(str(context, R.string.enemy_beholder), 999999999, 60, 0, 0, 5.2, 1.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_beholder));
        enemyList.add(new Enemy(str(context, R.string.enemy_cangaceiro), 999999999, 60, 20, 0, 13.52, 0.0, context.getString(R.string.waypoints), "", R.drawable.enemy_cangaceiro));
        enemyList.add(new Enemy(str(context, R.string.enemy_chest_demon_gift), 999999999, 40, 30, 0, 11.0, 20.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_chest_demon_gift));
        enemyList.add(new Enemy(str(context, R.string.enemy_chest_demon), 999999999, 40, 30, 0, 11.0, 20.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_chest_demon));
        enemyList.add(new Enemy(str(context, R.string.enemy_cthulhu), 999999999, 0, 0, 0, 5.0, 15.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_cthulhu));
        enemyList.add(new Enemy(str(context, R.string.enemy_cyclops), 999999999, 140, 0, 70, 8.0, 29.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_cyclops));
        enemyList.add(new Enemy(str(context, R.string.enemy_demon_stomper), 999999999, 0, 0, 0, 8.0, 1.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_demon_stomper));
        enemyList.add(new Enemy(str(context, R.string.enemy_demonic_spider), 999999999, 34, 0, 0, 7.0, 55.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_demonic_spider));
        enemyList.add(new Enemy(str(context, R.string.enemy_easter_demon), 999999999, 0, 80, 0, 15.0, 31.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_easter_demon));
        enemyList.add(new Enemy(str(context, R.string.enemy_final_warlock), 999999999, 250, 0, 0, 3.0, 36.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_evil_warlock));
        enemyList.add(new Enemy(str(context, R.string.enemy_fire_lord), 999999999, 110, 0, 0, 5.0, 1.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_fire_lord));
        enemyList.add(new Enemy(str(context, R.string.enemy_gigaspider), 999999999, 30, 0, 0, 5.0, 24.5, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_gigaspider));
        enemyList.add(new Enemy(str(context, R.string.enemy_golem), 999999999, 18, 0, 0, 7.0, 18.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_golem));
        enemyList.add(new Enemy(str(context, R.string.enemy_gorgon), 999999999, 15, 0, 0, 7.0, 35.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_gorgon));
        enemyList.add(new Enemy(str(context, R.string.enemy_ice_golem), 999999999, 18, 0, 0, 7.0, 23.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_ice_golem));
        enemyList.add(new Enemy(str(context, R.string.enemy_orc_fighter), 999999999, 120, 0, 0, 6.0, 1.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_orc_fighter));
        enemyList.add(new Enemy(str(context, R.string.enemy_slime_demon), 999999999, 44, 0, 0, 15.0, 5.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_slime_demon));
        enemyList.add(new Enemy(str(context, R.string.enemy_spidemon_higher_jump), 999999999, 23, 0, 0, 6.5, 18.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spidemon));
        enemyList.add(new Enemy(str(context, R.string.enemy_spidemon), 999999999, 23, 0, 0, 6.5, 15.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_spidemon));
        enemyList.add(new Enemy(str(context, R.string.enemy_tombstone_mimic), 999999999, 40, 30, 0, 11.0, 20.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_tombstone_mimic));
        enemyList.add(new Enemy(str(context, R.string.enemy_vindicator_carnival), 999999999, 220, 0, 0, 5.0, 1.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_vindicator_carnival));
        enemyList.add(new Enemy(str(context, R.string.enemy_vindicator), 999999999, 220, 0, 0, 5.0, 1.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_vindicator));
        enemyList.add(new Enemy(str(context, R.string.enemy_vulkling), 999999999, 3, 0, 0, 6.0, 18.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_vulkling));
        enemyList.add(new Enemy(str(context, R.string.enemy_walker), 999999999, 0, 0, 200000, 8.0, 18.0, context.getString(R.string.waypoints), context.getString(R.string.knife_thrower), R.drawable.enemy_walker));
        enemyList.add(new Enemy(str(context, R.string.enemy_warlock_final_boss), 999999999, 0, 400, 0, 7.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_warlock_final_boss));
        enemyList.add(new Enemy(str(context, R.string.enemy_xmas_mimic), 999999999, 40, 30, 0, 14.0, 28.0, context.getString(R.string.waypoints), context.getString(R.string.stone_thrower), R.drawable.enemy_xmas_mimic));

        // Skins
        // Put this once before the lists to avoid repeating the lookup:
        final String COLORED = " " + context.getString(R.string.suffix_colored);

// --- Ranger ---
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger1),  R.drawable.skin_ranger1));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger2),  R.drawable.skin_ranger2));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger3),  R.drawable.skin_ranger3));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger4),  R.drawable.skin_ranger4));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger5),  R.drawable.skin_ranger5));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger6),  R.drawable.skin_ranger6));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger7),  R.drawable.skin_ranger7));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger8),  R.drawable.skin_ranger8));
// 9 & 10 are colored variants of 1 & 2
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger1) + COLORED, R.drawable.skin_ranger9));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger2) + COLORED, R.drawable.skin_ranger10));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger11), R.drawable.skin_ranger11));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger12), R.drawable.skin_ranger12));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger13), R.drawable.skin_ranger13));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger14), R.drawable.skin_ranger14));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger15), R.drawable.skin_ranger15));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger16), R.drawable.skin_ranger16));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger17), R.drawable.skin_ranger17));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger18), R.drawable.skin_ranger18));
        rangerSkins.add(new SkinItem(context.getString(R.string.skin_ranger19), R.drawable.skin_ranger19));

// --- Priest ---
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest1),  R.drawable.skin_priest1));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest2),  R.drawable.skin_priest2));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest3),  R.drawable.skin_priest3));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest4),  R.drawable.skin_priest4));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest5),  R.drawable.skin_priest5));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest6),  R.drawable.skin_priest6));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest7),  R.drawable.skin_priest7));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest8),  R.drawable.skin_priest8));
// 9 & 10 colored
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest1) + COLORED, R.drawable.skin_priest9));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest2) + COLORED, R.drawable.skin_priest10));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest11), R.drawable.skin_priest11));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest12), R.drawable.skin_priest12));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest13), R.drawable.skin_priest13));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest14), R.drawable.skin_priest14));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest15), R.drawable.skin_priest15));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest16), R.drawable.skin_priest16));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest17), R.drawable.skin_priest17));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest18), R.drawable.skin_priest18));
        priestSkins.add(new SkinItem(context.getString(R.string.skin_priest19), R.drawable.skin_priest19));

// --- Warlock ---
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock1),  R.drawable.skin_warlock1));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock2),  R.drawable.skin_warlock2));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock3),  R.drawable.skin_warlock3));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock4),  R.drawable.skin_warlock4));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock5),  R.drawable.skin_warlock5));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock6),  R.drawable.skin_warlock6));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock7),  R.drawable.skin_warlock7));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock8),  R.drawable.skin_warlock8));
// 9 & 10 colored
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock1) + COLORED, R.drawable.skin_warlock9));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock2) + COLORED, R.drawable.skin_warlock10));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock11), R.drawable.skin_warlock11));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock12), R.drawable.skin_warlock12));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock13), R.drawable.skin_warlock13));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock14), R.drawable.skin_warlock14));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock15), R.drawable.skin_warlock15));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock16), R.drawable.skin_warlock16));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock17), R.drawable.skin_warlock17));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock18), R.drawable.skin_warlock18));
        warlockSkins.add(new SkinItem(context.getString(R.string.skin_warlock19), R.drawable.skin_warlock19));

// --- Black Mage ---
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage1),  R.drawable.skin_black_mage1));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage2),  R.drawable.skin_black_mage2));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage3),  R.drawable.skin_black_mage3));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage4),  R.drawable.skin_black_mage4));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage5),  R.drawable.skin_black_mage5));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage6),  R.drawable.skin_black_mage6));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage7),  R.drawable.skin_black_mage7));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage8),  R.drawable.skin_black_mage8));
// 9 & 10 colored
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage1) + COLORED, R.drawable.skin_black_mage9));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage2) + COLORED, R.drawable.skin_black_mage10));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage11), R.drawable.skin_black_mage11));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage12), R.drawable.skin_black_mage12));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage13), R.drawable.skin_black_mage13));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage14), R.drawable.skin_black_mage14));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage14) + " B", R.drawable.skin_black_mage14_b));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage15), R.drawable.skin_black_mage15));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage16), R.drawable.skin_black_mage16));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage17), R.drawable.skin_black_mage17));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage18), R.drawable.skin_black_mage18));
        blackMageSkins.add(new SkinItem(context.getString(R.string.skin_black_mage19), R.drawable.skin_black_mage19));

// --- Rogue ---
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue1),  R.drawable.skin_rogue1));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue2),  R.drawable.skin_rogue2));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue3),  R.drawable.skin_rogue3));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue4),  R.drawable.skin_rogue4));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue5),  R.drawable.skin_rogue5));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue6),  R.drawable.skin_rogue6));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue7),  R.drawable.skin_rogue7));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue8),  R.drawable.skin_rogue8));
// 9 & 10 colored
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue1) + COLORED, R.drawable.skin_rogue9));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue2) + COLORED, R.drawable.skin_rogue10));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue11), R.drawable.skin_rogue11));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue12), R.drawable.skin_rogue12));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue13), R.drawable.skin_rogue13));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue14), R.drawable.skin_rogue14));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue15), R.drawable.skin_rogue15));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue16), R.drawable.skin_rogue16));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue17), R.drawable.skin_rogue17));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue18), R.drawable.skin_rogue18));
        rogueSkins.add(new SkinItem(context.getString(R.string.skin_rogue19), R.drawable.skin_rogue19));

// --- Thief ---
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief1),  R.drawable.skin_thief1));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief2),  R.drawable.skin_thief2));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief3),  R.drawable.skin_thief3));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief4),  R.drawable.skin_thief4));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief5),  R.drawable.skin_thief5));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief6),  R.drawable.skin_thief6));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief7),  R.drawable.skin_thief7));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief8),  R.drawable.skin_thief8));
// 9 & 10 colored
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief1) + COLORED, R.drawable.skin_thief9));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief2) + COLORED, R.drawable.skin_thief10));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief11), R.drawable.skin_thief11));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief12), R.drawable.skin_thief12));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief13), R.drawable.skin_thief13));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief14), R.drawable.skin_thief14));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief15), R.drawable.skin_thief15));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief16), R.drawable.skin_thief16));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief17), R.drawable.skin_thief17));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief18), R.drawable.skin_thief18));
        thiefSkins.add(new SkinItem(context.getString(R.string.skin_thief19), R.drawable.skin_thief19));

// --- Warrior ---
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior1),  R.drawable.skin_warrior1));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior2),  R.drawable.skin_warrior2));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior3),  R.drawable.skin_warrior3));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior4),  R.drawable.skin_warrior4));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior5),  R.drawable.skin_warrior5));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior6),  R.drawable.skin_warrior6));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior7),  R.drawable.skin_warrior7));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior8),  R.drawable.skin_warrior8));
// 9 & 10 colored
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior1) + COLORED, R.drawable.skin_warrior9));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior2) + COLORED, R.drawable.skin_warrior10));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior11), R.drawable.skin_warrior11));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior12), R.drawable.skin_warrior12));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior13), R.drawable.skin_warrior13));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior14), R.drawable.skin_warrior14));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior15), R.drawable.skin_warrior15));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior16), R.drawable.skin_warrior16));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior17), R.drawable.skin_warrior17));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior18), R.drawable.skin_warrior18));
        warriorSkins.add(new SkinItem(context.getString(R.string.skin_warrior19), R.drawable.skin_warrior19));

// --- Mage ---
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage1),  R.drawable.skin_mage1));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage2),  R.drawable.skin_mage2));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage3),  R.drawable.skin_mage3));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage4),  R.drawable.skin_mage4));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage5),  R.drawable.skin_mage5));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage6),  R.drawable.skin_mage6));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage7),  R.drawable.skin_mage7));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage8),  R.drawable.skin_mage8));
// 9 & 10 colored
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage1) + COLORED, R.drawable.skin_mage9));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage2) + COLORED, R.drawable.skin_mage10));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage11), R.drawable.skin_mage11));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage12), R.drawable.skin_mage12));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage13), R.drawable.skin_mage13));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage14), R.drawable.skin_mage14));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage14) + " B", R.drawable.skin_mage14_b));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage15), R.drawable.skin_mage15));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage16), R.drawable.skin_mage16));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage17), R.drawable.skin_mage17));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage18), R.drawable.skin_mage18));
        mageSkins.add(new SkinItem(context.getString(R.string.skin_mage19), R.drawable.skin_mage19));

// --- Druid ---
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid1),  R.drawable.skin_druid1));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid2),  R.drawable.skin_druid2));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid3),  R.drawable.skin_druid3));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid4),  R.drawable.skin_druid4));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid5),  R.drawable.skin_druid5));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid6),  R.drawable.skin_druid6));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid7),  R.drawable.skin_druid7));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid8),  R.drawable.skin_druid8));
// 9 & 10 colored
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid1) + COLORED, R.drawable.skin_druid9));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid2) + COLORED, R.drawable.skin_druid10));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid11), R.drawable.skin_druid11));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid12), R.drawable.skin_druid12));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid13), R.drawable.skin_druid13));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid14), R.drawable.skin_druid14));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid15), R.drawable.skin_druid15));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid16), R.drawable.skin_druid16));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid17), R.drawable.skin_druid17));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid18), R.drawable.skin_druid18));
        druidSkins.add(new SkinItem(context.getString(R.string.skin_druid19), R.drawable.skin_druid19));

// --- Paladin ---
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin1),  R.drawable.skin_paladin1));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin2),  R.drawable.skin_paladin2));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin3),  R.drawable.skin_paladin3));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin4),  R.drawable.skin_paladin4));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin5),  R.drawable.skin_paladin5));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin6),  R.drawable.skin_paladin6));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin7),  R.drawable.skin_paladin7));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin8),  R.drawable.skin_paladin8));
// 9 & 10 colored
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin1) + COLORED, R.drawable.skin_paladin9));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin2) + COLORED, R.drawable.skin_paladin10));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin11), R.drawable.skin_paladin11));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin12), R.drawable.skin_paladin12));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin13), R.drawable.skin_paladin13));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin14), R.drawable.skin_paladin14));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin15), R.drawable.skin_paladin15));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin16), R.drawable.skin_paladin16));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin17), R.drawable.skin_paladin17));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin18), R.drawable.skin_paladin18));
        paladinSkins.add(new SkinItem(context.getString(R.string.skin_paladin19), R.drawable.skin_paladin19));

// --- High Mage ---
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage1),  R.drawable.skin_high_mage1));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage2),  R.drawable.skin_high_mage2));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage3),  R.drawable.skin_high_mage3));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage4),  R.drawable.skin_high_mage4));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage5),  R.drawable.skin_high_mage5));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage6),  R.drawable.skin_high_mage6));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage7),  R.drawable.skin_high_mage7));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage8),  R.drawable.skin_high_mage8));
// 9 & 10 colored
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage1) + COLORED, R.drawable.skin_high_mage9));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage2) + COLORED, R.drawable.skin_high_mage10));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage11), R.drawable.skin_high_mage11));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage12), R.drawable.skin_high_mage12));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage13), R.drawable.skin_high_mage13));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage14), R.drawable.skin_high_mage14));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage15), R.drawable.skin_high_mage15));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage16), R.drawable.skin_high_mage16));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage17), R.drawable.skin_high_mage17));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage18), R.drawable.skin_high_mage18));
        highMageSkins.add(new SkinItem(context.getString(R.string.skin_high_mage19), R.drawable.skin_high_mage19));

        // --- Elite Warrior ---
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior1),  R.drawable.skin_elite_warrior1));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior2),  R.drawable.skin_elite_warrior2));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior3),  R.drawable.skin_elite_warrior3));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior4),  R.drawable.skin_elite_warrior4));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior5),  R.drawable.skin_elite_warrior5));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior6),  R.drawable.skin_elite_warrior6));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior7),  R.drawable.skin_elite_warrior7));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior8),  R.drawable.skin_elite_warrior8));
        // 9 & 10 colored
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior1) + COLORED, R.drawable.skin_elite_warrior9));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior2) + COLORED, R.drawable.skin_elite_warrior10));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior11), R.drawable.skin_elite_warrior11));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior12), R.drawable.skin_elite_warrior12));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior12) + " A", R.drawable.skin_elite_warrior12a));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior13), R.drawable.skin_elite_warrior13));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior14), R.drawable.skin_elite_warrior14));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior15), R.drawable.skin_elite_warrior15));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior16), R.drawable.skin_elite_warrior16));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior17), R.drawable.skin_elite_warrior17));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior18), R.drawable.skin_elite_warrior18));
        eliteWarriorSkins.add(new SkinItem(context.getString(R.string.skin_elite_warrior19), R.drawable.skin_elite_warrior19));

        // --- Witchdoctor ---
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor1),  R.drawable.skin_witchdoctor1));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor2),  R.drawable.skin_witchdoctor2));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor3),  R.drawable.skin_witchdoctor3));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor4),  R.drawable.skin_witchdoctor4));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor5),  R.drawable.skin_witchdoctor5));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor6),  R.drawable.skin_witchdoctor6));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor7),  R.drawable.skin_witchdoctor7));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor8),  R.drawable.skin_witchdoctor8));
        // 9 & 10 colored
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor1) + COLORED, R.drawable.skin_witchdoctor9));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor2) + COLORED, R.drawable.skin_witchdoctor10));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor11), R.drawable.skin_witchdoctor11));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor12), R.drawable.skin_witchdoctor12));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor13), R.drawable.skin_witchdoctor13));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor14), R.drawable.skin_witchdoctor14));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor15), R.drawable.skin_witchdoctor15));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor16), R.drawable.skin_witchdoctor16));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor17), R.drawable.skin_witchdoctor17));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor18), R.drawable.skin_witchdoctor18));
        witchdoctorSkins.add(new SkinItem(context.getString(R.string.skin_witchdoctor19), R.drawable.skin_witchdoctor19));

        // Elixirs
        elixirList.add(new Elixir(
                str(context, R.string.arcane_precision_tonic),
                str(context, R.string.arcane_precision_tonic_desc),
                R.drawable.elixir_arcane_precision_tonic,
                ElixirType.ARCANE_PRECISION_TONIC
        ));
        elixirList.add(new Elixir(
                str(context, R.string.elixir_of_duplication),
                str(context, R.string.elixir_of_duplication_desc),
                R.drawable.elixir_elixir_of_duplication,
                ElixirType.ELIXIR_OF_DUPLICATION
        ));
        elixirList.add(new Elixir(
                str(context, R.string.forbidden_flowers_extract),
                str(context, R.string.forbidden_flowers_extract_desc),
                R.drawable.elixir_forbidden_flowers_extract,
                ElixirType.FORBIDDEN_FLOWERS_EXTRACT
        ));
        elixirList.add(new Elixir(
                str(context, R.string.instant_charge_blast),
                str(context, R.string.instant_charge_blast_desc),
                R.drawable.elixir_instant_charge_blast,
                ElixirType.INSTANT_CHARGE_BLAST
        ));
        elixirList.add(new Elixir(
                str(context, R.string.instant_dark_twin),
                str(context, R.string.instant_dark_twin_desc),
                R.drawable.elixir_instant_dark_twin,
                ElixirType.INSTANT_DARK_TWIN
        ));
        elixirList.add(new Elixir(
                str(context, R.string.instant_gust),
                str(context, R.string.instant_gust_desc),
                R.drawable.elixir_instant_gust,
                ElixirType.INSTANT_GUST
        ));
        elixirList.add(new Elixir(
                str(context, R.string.instant_radiant_shield),
                str(context, R.string.instant_radiant_shield_desc),
                R.drawable.elixir_instant_radiant_shield,
                ElixirType.INSTANT_RADIANT_SHIELD
        ));
        elixirList.add(new Elixir(
                str(context, R.string.instant_thunder_wave),
                str(context, R.string.instant_thunder_wave_desc),
                R.drawable.elixir_instant_thunder_wave,
                ElixirType.INSTANT_THUNDER_WAVE
        ));
        elixirList.add(new Elixir(
                str(context, R.string.levitation_liqueur),
                str(context, R.string.levitation_liqueur_desc),
                R.drawable.elixir_levitation_liqueur,
                ElixirType.LEVITATION_LIQUEUR
        ));
        elixirList.add(new Elixir(
                str(context, R.string.monsters_juice),
                str(context, R.string.monsters_juice_desc),
                R.drawable.elixir_monsters_juice,
                ElixirType.MONSTERS_JUICE
        ));
        elixirList.add(new Elixir(
                str(context, R.string.moonjump_mixture),
                str(context, R.string.moonjump_mixture_desc),
                R.drawable.elixir_moonjump_mixture,
                ElixirType.MOONJUMP_MIXTURE
        ));
        elixirList.add(new Elixir(
                str(context, R.string.pepper_brew),
                str(context, R.string.pepper_brew_desc),
                R.drawable.elixir_pepper_brew,
                ElixirType.PEPPER_BREW
        ));
        elixirList.add(new Elixir(
                str(context, R.string.starlight_supertonic),
                str(context, R.string.starlight_supertonic_desc),
                R.drawable.elixir_starlight_supertonic,
                ElixirType.STARLIGHT_SUPERTONIC
        ));
        elixirList.add(new Elixir(
                str(context, R.string.tonic_of_invulnerability),
                str(context, R.string.tonic_of_invulnerability_desc),
                R.drawable.elixir_tonic_of_invulnerability,
                ElixirType.TONIC_OF_INVULNERABILITY
        ));
        elixirList.add(new Elixir(
                str(context, R.string.umbranian_water),
                str(context, R.string.umbranian_water_desc),
                R.drawable.elixir_umbranian_water,
                ElixirType.UMBRANIAN_WATER
        ));

        // Reward Sets
        { int w=indexOfWeaponByName(swordList,str(context,R.string.all_seeing_sword)), a=indexOfArmorByName(armorList,str(context,R.string.warden_of_demons)), r=indexOfRingByName(ringList,str(context,R.string.spektator)), c=indexOfClassByEnum(classList,ClassNames.WARRIOR); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=swordList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true, false,false,false, false, true,true,true,true,true,true,true,true, false,false,false,false,false, true,true,true,true,true,true,true,true,true, false,false,false}; elixir=null; ringElement=Elements.DARKNESS; }}); }
        { int w=indexOfWeaponByName(hammerList,str(context,R.string.falcon_hammer)), a=indexOfArmorByName(armorList,str(context,R.string.heros_set_plus)), r=indexOfRingByName(ringList,str(context,R.string.falcon_ring)), c=indexOfClassByEnum(classList,ClassNames.ELITE_WARRIOR); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=hammerList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(daggerList,str(context,R.string.venomous_dagger)), a=indexOfArmorByName(armorList,str(context,R.string.tunic_of_distant_ancestors)), r=indexOfRingByName(ringList,str(context,R.string.ghostwalker)), c=indexOfClassByEnum(classList,ClassNames.ROGUE); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=daggerList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; weaponElement=Elements.EARTH; ringElement=Elements.EARTH; }}); }
        { int w=indexOfWeaponByName(swordList,str(context,R.string.whisper_of_death)), a=indexOfArmorByName(armorList,str(context,R.string.storm)), r=indexOfRingByName(ringList,str(context,R.string.the_daemon_codices)), c=indexOfClassByEnum(classList,ClassNames.HIGH_MAGE); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=swordList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; }}); }
        { int w=indexOfWeaponByName(swordList,str(context,R.string.all_seeing_sword)), a=indexOfArmorByName(armorList,str(context,R.string.swamp_witch_suit)), r=indexOfRingByName(ringList,str(context,R.string.nightshade)), c=indexOfClassByEnum(classList,ClassNames.HIGH_MAGE); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=swordList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; armorElement=Elements.DARKNESS; ringElement=Elements.DARKNESS; }}); }
        { int w=indexOfWeaponByName(daggerList,str(context,R.string.iron_dagger_plus)), a=indexOfArmorByName(armorList,str(context,R.string.barrenlands_banditin)), r=indexOfRingByName(ringList,str(context,R.string.spektator)), c=indexOfClassByEnum(classList,ClassNames.HIGH_MAGE); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=daggerList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; armorElement=Elements.LIGHT; ringElement=Elements.LIGHT; weaponElement=Elements.LIGHT; }}); }
        { int w=indexOfWeaponByName(swordList,str(context,R.string.arachnid_sword)), a=indexOfArmorByName(armorList,str(context,R.string.time_patroller_suit)), r=indexOfRingByName(ringList,str(context,R.string.spektator)), c=indexOfClassByEnum(classList,ClassNames.WARRIOR); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=swordList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; armorElement=Elements.DARKNESS; ringElement=Elements.DARKNESS; weaponElement=Elements.DARKNESS; }}); }
        { int w=indexOfWeaponByName(spearList,str(context,R.string.ruby_queens_spear)), a=indexOfArmorByName(armorList,str(context,R.string.swamp_witch_suit)), r=indexOfRingByName(ringList,str(context,R.string.spektator)), c=indexOfClassByEnum(classList,ClassNames.DRUID); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=spearList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; armorElement=Elements.FIRE; ringElement=Elements.FIRE; }}); }
        { int w=indexOfWeaponByName(swordList,str(context,R.string.volcano_sword)), a=indexOfArmorByName(armorList,str(context,R.string.swamp_witch_suit)), r=indexOfRingByName(ringList,str(context,R.string.spektator)), c=indexOfClassByEnum(classList,ClassNames.RANGER); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=swordList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; armorElement=Elements.FIRE; ringElement=Elements.FIRE; }}); }
        { int w=indexOfWeaponByName(swordList,str(context,R.string.dawnlight)), a=indexOfArmorByName(armorList,str(context,R.string.robe_of_imminent_fires)), r=indexOfRingByName(ringList,str(context,R.string.ghostwalker)), c=indexOfClassByEnum(classList,ClassNames.WARLOCK); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=swordList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; ringElement=Elements.LIGHT; }}); }
        { int w=indexOfWeaponByName(spearList,str(context,R.string.air_piercer)), a=indexOfArmorByName(armorList,str(context,R.string.wind_witch_suit)), r=indexOfRingByName(ringList,str(context,R.string.falcon_ring)), c=indexOfClassByEnum(classList,ClassNames.RANGER); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=spearList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false}; elixir=null; }}); }
        { int w=indexOfWeaponByName(staffList,str(context,R.string.the_ancient_codices)), a=indexOfArmorByName(armorList,str(context,R.string.tunic_of_endless_justice)), r=indexOfRingByName(ringList,str(context,R.string.spektator)), c=indexOfClassByEnum(classList,ClassNames.DRUID); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=staffList.get(w); weaponElement=Elements.LIGHT; armor=armorList.get(a); ring=ringList.get(r); ringElement=Elements.LIGHT; characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(staffList,str(context,R.string.voidcaller_staff)), a=indexOfArmorByName(armorList,str(context,R.string.scarlet_shadowweaver)), r=indexOfRingByName(ringList,str(context,R.string.whisper_of_death)), c=indexOfClassByEnum(classList,ClassNames.WITCHDOCTOR); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=staffList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(daggerList,str(context,R.string.shooting_star_shuriken)), a=indexOfArmorByName(armorList,str(context,R.string.desert_rogue_suit)), r=indexOfRingByName(ringList,str(context,R.string.spektator)), c=indexOfClassByEnum(classList,ClassNames.WITCHDOCTOR); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=daggerList.get(w); weaponElement=Elements.LIGHT; armor=armorList.get(a); armorElement=Elements.LIGHT; ring=ringList.get(r); ringElement=Elements.LIGHT; characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(staffList,str(context,R.string.the_ancient_codices)), a=indexOfArmorByName(armorList,str(context,R.string.swamp_witch_suit)), r=indexOfRingByName(ringList,str(context,R.string.sea_wave_ring)), c=indexOfClassByEnum(classList,ClassNames.HIGH_MAGE); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=staffList.get(w); weaponElement=Elements.WATER; armor=armorList.get(a); armorElement=Elements.WATER; ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(swordList,str(context,R.string.golden_needle)), a=indexOfArmorByName(armorList,str(context,R.string.wind_witch_suit)), r=indexOfRingByName(ringList,str(context,R.string.vindicator_guardian)), c=indexOfClassByEnum(classList,ClassNames.RANGER); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=swordList.get(w); weaponElement=Elements.AIR; armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(staffList,str(context,R.string.compendium_of_trees)), a=indexOfArmorByName(armorList,str(context,R.string.robe_of_distant_visions)), r=indexOfRingByName(ringList,str(context,R.string.vina)), c=indexOfClassByEnum(classList,ClassNames.DRUID); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=staffList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(staffList,str(context,R.string.hippie_dragon_staff_plus)), a=indexOfArmorByName(armorList,str(context,R.string.protector_of_the_oracle)), r=indexOfRingByName(ringList,str(context,R.string.vina)), c=indexOfClassByEnum(classList,ClassNames.DRUID); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=staffList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(staffList,str(context,R.string.stormcaller)), a=indexOfArmorByName(armorList,str(context,R.string.interrogator)), r=indexOfRingByName(ringList,str(context,R.string.tidecaller_ring)), c=indexOfClassByEnum(classList,ClassNames.PALADIN); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=staffList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(hammerList,str(context,R.string.enlightened_hammer)), a=indexOfArmorByName(armorList,str(context,R.string.destroyer_of_the_corrupted)), r=indexOfRingByName(ringList,str(context,R.string.whisper_of_light)), c=indexOfClassByEnum(classList,ClassNames.PALADIN); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=hammerList.get(w); armor=armorList.get(a); ring=ringList.get(r); characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
        { int w=indexOfWeaponByName(daggerList,str(context,R.string.gayan_shuriken)), a=indexOfArmorByName(armorList,str(context,R.string.tunic_of_distant_ancestors)), r=indexOfRingByName(ringList,str(context,R.string.spektator)), c=indexOfClassByEnum(classList,ClassNames.PALADIN); if(w>=0&&a>=0&&r>=0&&c>=0) rewardSets.add(new EquipmentSet(){{ weapon=daggerList.get(w); armor=armorList.get(a); ring=ringList.get(r); ringElement=Elements.EARTH; characterClass=classList.get(c); skills=new boolean[]{ true,true,true,true,true,true,true,true,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false }; elixir=null; }}); }
    }

    private static int indexOfWeaponByName(List<Weapon> list, String name) {
        for (int i = 0; i < list.size(); i++) if (list.get(i).getName().equalsIgnoreCase(name)) return i;
        return -1;
    }
    private static int indexOfArmorByName(List<Armor> list, String name) {
        for (int i = 0; i < list.size(); i++) if (list.get(i).getName().equalsIgnoreCase(name)) return i;
        return -1;
    }
    private static int indexOfRingByName(List<Ring> list, String name) {
        for (int i = 0; i < list.size(); i++) if (list.get(i).getName().equalsIgnoreCase(name)) return i;
        return -1;
    }
    private static int indexOfClassByEnum(List<CharacterClass> list, ClassNames cn) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getClassEnum() == cn) return i;
        }
        return -1;
    }

    public static Armor getArmorById(int id) {
        for (Armor a : armorList) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    public static Ring getRingById(int id) {
        for (Ring r : ringList) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    public static Weapon getWeaponById(int id) {
        for (Weapon w : swordList)  if (w.getId() == id) return w;
        for (Weapon w : staffList)  if (w.getId() == id) return w;
        for (Weapon w : daggerList) if (w.getId() == id) return w;
        for (Weapon w : axeList)    if (w.getId() == id) return w;
        for (Weapon w : hammerList) if (w.getId() == id) return w;
        for (Weapon w : spearList)  if (w.getId() == id) return w;
        return null;
    }

    public static Elixir getElixirById(int id) {
        for (Elixir e : elixirList) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public static CharacterClass getClassById(int id) {
        for (CharacterClass c : classList) {
            if (c.getId() == id) return c;
        }
        return null;
    }

}

