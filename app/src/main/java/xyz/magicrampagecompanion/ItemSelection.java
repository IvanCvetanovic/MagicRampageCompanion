package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

public class ItemSelection extends AppCompatActivity {

    private MediaPlayer mediaPlayerTopTab;
    private MediaPlayer mediaPlayerItemPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        mediaPlayerTopTab = MediaPlayer.create(this, R.raw.click);

        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        int buttonId = getIntent().getIntExtra("buttonId", -1);
        List<Armor> armorList = new ArrayList<>();
        List<Ring> ringList = new ArrayList<>();
        List<CharacterClass> characterList = new ArrayList<>();

        List<Weapon> swordList = new ArrayList<>();
        List<Weapon> staffList = new ArrayList<>();
        List<Weapon> daggerList = new ArrayList<>();
        List<Weapon> axeList = new ArrayList<>();
        List<Weapon> spearList = new ArrayList<>();
        List<Weapon> hammerList = new ArrayList<>();

        HorizontalScrollView buttonsScrollView = findViewById(R.id.buttonsScrollView);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();

        Button buttonSwords = findViewById(R.id.button1);
        Button buttonStaffs = findViewById(R.id.button2);
        Button buttonDaggers = findViewById(R.id.button3);
        Button buttonAxes = findViewById(R.id.button4);
        Button buttonSpears = findViewById(R.id.button5);
        Button buttonHammers = findViewById(R.id.button6);

        buttonSwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAdapter adapter = new ImageAdapter(swordList, new ImageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        playSound(mediaPlayerTopTab);
                        Weapon selectedWeapon = swordList.get(position);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedWeapon", selectedWeapon);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        buttonStaffs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAdapter adapter = new ImageAdapter(staffList, new ImageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        playSound(mediaPlayerTopTab);
                        Weapon selectedWeapon = staffList.get(position);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedWeapon", selectedWeapon);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        buttonDaggers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAdapter adapter = new ImageAdapter(daggerList, new ImageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        playSound(mediaPlayerTopTab);
                        Weapon selectedWeapon = daggerList.get(position);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedWeapon", selectedWeapon);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        buttonAxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAdapter adapter = new ImageAdapter(axeList, new ImageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        playSound(mediaPlayerTopTab);
                        Weapon selectedWeapon = axeList.get(position);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedWeapon", selectedWeapon);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        buttonHammers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAdapter adapter = new ImageAdapter(hammerList, new ImageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        playSound(mediaPlayerTopTab);
                        Weapon selectedWeapon = hammerList.get(position);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedWeapon", selectedWeapon);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        buttonSpears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageAdapter adapter = new ImageAdapter(spearList, new ImageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        playSound(mediaPlayerTopTab);
                        Weapon selectedWeapon = spearList.get(position);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedWeapon", selectedWeapon);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });

        if (buttonId == 1) {
            Log.d("ItemSelection", "Armor button clicked");
            armorList.add(createArmor(R.string.jumpman_suit, Elements.AIR, false, 0, 0, 0, -19, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_jumpman_suit));
            armorList.add(createArmor(R.string.dancers_raiment_tiktok, Elements.AIR, false, 10, 10, 0, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_dancers_raiment_tiktok));
            armorList.add(createArmor(R.string.improved_jumpman_suit, Elements.AIR, false, 10, 10, 0, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_jumpman_suit));
            armorList.add(createArmor(R.string.recruit_set, Elements.NEUTRAL, false, 6, 10, 3, 0, 0, 0, 10, 0, 14, 0, 0, 0, R.drawable.armor_recruit_soldier_set));
            armorList.add(createArmor(R.string.twitch_suit, Elements.AIR, false, 10, 10, 0, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_twitch_suit));
            armorList.add(createArmor(R.string.tubeman_suit, Elements.AIR, false, 10, 10, 0, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_tubeman_suit));
            armorList.add(createArmor(R.string.cleric_vest, Elements.LIGHT, false, 8, 13, 3, 2, 2, 35, 0, 19, 19, 0, 0, 0, R.drawable.armor_cleric_vest));
            armorList.add(createArmor(R.string.fire_mage_vest, Elements.FIRE, false, 8, 13, 3, 0, 0, 30, 0, 19, 0, 0, 0, 0, R.drawable.armor_fire_mage_vest));
            armorList.add(createArmor(R.string.nature_mage_vest, Elements.EARTH, false, 8, 13, 3, 0, 0, 30, 0, 19, 0, 0, 0, 0, R.drawable.armor_nature_mage_vest));
            armorList.add(createArmor(R.string.bunny_suit, Elements.NEUTRAL, false, 15, 15, 0, 5, 28, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_bunny_suit));
            armorList.add(createArmor(R.string.soldier_set, Elements.NEUTRAL, false, 12, 20, 3, 0, 0, 0, 19, 0, 19, 14, 0, 0, R.drawable.armor_recruit_soldier_set));
            armorList.add(createArmor(R.string.plague_fighter_suit, Elements.AIR, false, 14, 24, 3, 4, 3, 0, 0, 0, 19, 0, 0, 0, R.drawable.armor_plague_fighter_suit));
            armorList.add(createArmor(R.string.elite_soldier_set, Elements.NEUTRAL, false, 15, 25, 3, 3, 2, 0, 30, 0, 19, 19, 0, 0, R.drawable.armor_elite_soldier_set));
            armorList.add(createArmor(R.string.bankers_coat, Elements.DARKNESS, false, 10, 26, 7, 4, 4, 39, 0, 0, 30, 0, 0, 50, R.drawable.armor_bankers_coat));
            armorList.add(createArmor(R.string.santa_claus, Elements.LIGHT, true, 30, 30, 0, 8, 19, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_santa_claus));
            armorList.add(createArmor(R.string.ranger_set, Elements.AIR, false, 19, 32, 3, 3, 2, 0, 30, 0, 30, 0, 0, 0, R.drawable.armor_ranger_umbranian_set));
            armorList.add(createArmor(R.string.umbranian_vest, Elements.AIR, false, 21, 36, 3, 5, 3, 30, 19, 0, 39, 0, 0, 0, R.drawable.armor_ranger_umbranian_set));
            armorList.add(createArmor(R.string.druid_vest, Elements.EARTH, false, 15, 43, 8, 0, 3, 35, 0, 25, 25, 0, 0, 0, R.drawable.armor_druid_vest));
            armorList.add(createArmor(R.string.deathdoctor_set, Elements.DARKNESS, false, 20, 48, 6, 4, 1, 19, 0, 19, 55, 0, 0, 0, R.drawable.armor_deathdoctor_set));
            armorList.add(createArmor(R.string.brass_set, Elements.NEUTRAL, false, 30, 51, 3, 1, 1, 0, 14, 0, 0, 19, 19, 0, R.drawable.armor_brass_set));
            armorList.add(createArmor(R.string.heros_set, Elements.AIR, false, 25, 54, 5, 6, 2, 0, 39, 0, 39, 0, 19, 0, R.drawable.armor_heros_set));
            armorList.add(createArmor(R.string.elite_brass_set, Elements.NEUTRAL, false, 36, 61, 3, 2, 2, 0, 19, 0, 0, 25, 25, 0, R.drawable.armor_elite_brass_set));
            armorList.add(createArmor(R.string.forest_witch_suit, Elements.EARTH, false, 14, 64, 15, 3, 4, 60, 0, 60, 0, 0, 39, 0, R.drawable.armor_forest_witch_suit));
            armorList.add(createArmor(R.string.obsidian_set, Elements.NEUTRAL, false, 42, 72, 3, 2, 2, 0, 25, 0, 0, 30, 30, 0, R.drawable.armor_obsidian_set));
            armorList.add(createArmor(R.string.green_witch_suit, Elements.EARTH, false, 16, 73, 15, 3, 3, 39, 0, 39, 0, 0, 0, 0, R.drawable.armor_green_witch_suit));
            armorList.add(createArmor(R.string.witch_dress, Elements.EARTH, false, 16, 73, 15, 2, 7, 120, 0, 80, 80, 0, 0, 0, R.drawable.armor_witch_dress));
            armorList.add(createArmor(R.string.ifrits_disciple_robe, Elements.FIRE, false, 22, 74, 10, 3, 2, 39, 0, 35, 35, 35, 0, 0, R.drawable.armor_ifrits_disciple_robe));
            armorList.add(createArmor(R.string.monks_tunic, Elements.LIGHT, false, 24, 75, 9, 4, 2, 60, 0, 30, 0, 0, 30, 0, R.drawable.armor_monks_tunic));
            armorList.add(createArmor(R.string.elite_assassin_suit, Elements.DARKNESS, false, 32, 78, 6, 5, 3, 60, 60, 0, 60, 60, 0, 60, R.drawable.armor_elite_assassin_suit));
            armorList.add(createArmor(R.string.high_elf_robe, Elements.EARTH, false, 25, 79, 9, 3, 2, 39, 39, 39, 0, 0, 0, 39, R.drawable.armor_high_elf_robe));
            armorList.add(createArmor(R.string.jungle_assassin_suit, Elements.EARTH, false, 25, 79, 9, 4, 4, 44, 0, 35, 35, 0, 39, 0, R.drawable.armor_jungle_assassin_suit));
            armorList.add(createArmor(R.string.faldangs_vest, Elements.LIGHT, false, 22, 80, 11, 4, 1, 44, 0, 35, 0, 0, 0, 0, R.drawable.armor_faldangs_vest));
            armorList.add(createArmor(R.string.gayan_set, Elements.EARTH, false, 80, 80, 0, 4, 3, 80, 0, 0, 80, 80, 0, 100, R.drawable.armor_gayan_set));
            armorList.add(createArmor(R.string.dragon_vest, Elements.FIRE, false, 24, 81, 10, 5, 1, 60, 0, 39, 39, 39, 0, 0, R.drawable.armor_dragon_vest));
            armorList.add(createArmor(R.string.high_cleric_tunic, Elements.LIGHT, false, 20, 82, 13, 5, 2, 60, 0, 39, 0, 0, 0, 0, R.drawable.armor_high_cleric_tunic));
            armorList.add(createArmor(R.string.antiheros_set, Elements.AIR, false, 27, 85, 9, 7, 3, 39, 60, 0, 60, 0, 0, 0, R.drawable.armor_antiheros_set));
            armorList.add(createArmor(R.string.black_witch_suit, Elements.DARKNESS, false, 19, 87, 15, 4, 2, 70, 0, 75, 0, 0, 0, 60, R.drawable.armor_black_witch_suit));
            armorList.add(createArmor(R.string.warlock_vest, Elements.DARKNESS, false, 22, 90, 13, 4, 2, 50, 0, 30, 30, 0, 0, 0, R.drawable.armor_warlock_vest));
            armorList.add(createArmor(R.string.elite_obsidian_set, Elements.NEUTRAL, false, 53, 91, 3, 3, 2, 0, 35, 0, 0, 35, 35, 0, R.drawable.armor_elite_obsidian_set));
            armorList.add(createArmor(R.string.greenmoon_vest, Elements.EARTH, false, 27, 91, 10, 4, 3, 60, 0, 39, 0, 0, 39, 39, R.drawable.armor_greenmoon_vest));
            armorList.add(createArmor(R.string.tunic_of_hellish_illusions, Elements.DARKNESS, false, 20, 92, 15, 4, 8, 150, 0, 0, 0, 80, 100, 80, R.drawable.armor_tunic_of_hellish_illusions));
            armorList.add(createArmor(R.string.blue_wizard_suit, Elements.WATER, false, 30, 94, 9, 4, 2, 60, 0, 60, 0, 60, 0, 0, R.drawable.armor_blue_wizard_suit));
            armorList.add(createArmor(R.string.penumbra_suit, Elements.DARKNESS, false, 23, 94, 13, 5, 3, 70, 0, 70, 50, 0, 0, 0, R.drawable.armor_penumbra_suit));
            armorList.add(createArmor(R.string.kings_spectre_tunic, Elements.NEUTRAL, false, 28, 95, 10, 4, 4, 400, 0, 0, 0, 0, 0, 0, R.drawable.armor_kings_spectre_tunic));
            armorList.add(createArmor(R.string.moonkeeper_set, Elements.EARTH, false, 36, 96, 7, 6, 4, 60, 0, 0, 39, 0, 39, 0, R.drawable.armor_moonkeeper_set));
            armorList.add(createArmor(R.string.tunic_of_ending_nights, Elements.NEUTRAL, true, 21, 96, 15, 3, 2, 150, 0, 140, 140, 0, 0, 0, R.drawable.armor_tunic_of_ending_nights));
            armorList.add(createArmor(R.string.light_spellmaster_suit, Elements.LIGHT, false, 100, 100, 0, 6, 3, 110, 80, 80, 0, 0, 0, 0, R.drawable.armor_light_spellmaster_suit));
            armorList.add(createArmor(R.string.arcane_mage_suit, Elements.NEUTRAL, true, 30, 101, 10, 5, 3, 110, 0, 120, 80, 0, 80, 100, R.drawable.armor_arcane_mage_suit));
            armorList.add(createArmor(R.string.purple_witch_suit, Elements.DARKNESS, false, 22, 101, 15, 3, 3, 50, 0, 30, 0, 0, 0, 0, R.drawable.armor_purple_witch_suit));
            armorList.add(createArmor(R.string.special_antiheros_set, Elements.AIR, false, 25, 103, 13, 8, 5, 60, 70, 0, 70, 0, 0, 0, R.drawable.armor_antiheros_set));
            armorList.add(createArmor(R.string.special_dragon_vest, Elements.FIRE, false, 25, 103, 13, 6, 3, 100, 0, 0, 35, 0, 0, 60, R.drawable.armor_dragon_vest));
            armorList.add(createArmor(R.string.liquid_assassin_suit, Elements.WATER, false, 34, 107, 9, 6, 3, 60, 60, 0, 60, 0, 60, 60, R.drawable.armor_liquid_assassin_suit));
            armorList.add(createArmor(R.string.crow_set, Elements.DARKNESS, false, 38, 110, 8, 6, 2, 80, 50, 0, 0, 0, 50, 0, R.drawable.armor_crow_set));
            armorList.add(createArmor(R.string.light_champion_suit, Elements.LIGHT, false, 110, 110, 0, 4, 4, 50, 100, 0, 0, 100, 100, 100, R.drawable.armor_light_champion_set));
            armorList.add(createArmor(R.string.tunic_of_endless_justice, Elements.LIGHT, false, 24, 110, 15, 5, 3, 179, 70, 129, 0, 0, 0, 70, R.drawable.armor_tunic_of_endless_justice));
            armorList.add(createArmor(R.string.spider_exoskeleton, Elements.DARKNESS, false, 27, 111, 13, 6, 3, 100, 0, 0, 35, 100, 0, 0, R.drawable.armor_spider_exoskeleton));
            armorList.add(createArmor(R.string.bacoon_set, Elements.WATER, false, 34, 115, 10, 4, 3, 64, 50, 0, 0, 0, 60, 0, R.drawable.armor_bacoon_set));
            armorList.add(createArmor(R.string.gatekeepers_bionic_arm, Elements.AIR, false, 25, 115, 15, 6, 2, 0, 200, 0, 39, 0, 19, 0, R.drawable.armor_gatekeepers_bionic_arm));
            armorList.add(createArmor(R.string.swamp_witch_suit, Elements.NEUTRAL, false, 35, 118, 10, 5, 3, 120, 140, 140, 80, 0, 0, 0, R.drawable.armor_swamp_witch_suit));
            armorList.add(createArmor(R.string.winter_witch_armor, Elements.WATER, true, 35, 118, 10, 4, 2, 100, 100, 100, 0, 0, 0, 0, R.drawable.armor_winter_witch_armor));
            armorList.add(createArmor(R.string.death_stalker, Elements.DARKNESS, false, 26, 119, 15, 2, 0, 160, 0, 110, 129, 0, 0, 150, R.drawable.armor_death_stalker));
            armorList.add(createArmor(R.string.special_crow_set, Elements.DARKNESS, false, 29, 119, 13, 6, 4, 80, 70, 0, 0, 60, 60, 0, R.drawable.armor_special_crow_set));
            armorList.add(createArmor(R.string.dark_lord_set, Elements.DARKNESS, false, 45, 120, 7, 6, 2, 129, 70, 0, 70, 0, 0, 50, R.drawable.armor_dark_lord_set));
            armorList.add(createArmor(R.string.firestorm_armor, Elements.FIRE, true, 50, 122, 6, 6, 2, 120, 0, 0, 30, 60, 0, 50, R.drawable.armor_firestorm_armor));
            armorList.add(createArmor(R.string.jungle_master, Elements.EARTH, false, 50, 122, 6, 4, 5, 80, 0, 80, 0, 0, 80, 100, R.drawable.armor_jungle_master));
            armorList.add(createArmor(R.string.robe_of_eternal_fire, Elements.FIRE, false, 27, 124, 15, 4, 3, 179, 120, 0, 0, 50, 0, 120, R.drawable.armor_robe_of_eternal_fire));
            armorList.add(createArmor(R.string.penumbra_suit, Elements.DARKNESS, false, 26, 125, 16, 6, 4, 140, 0, 50, 0, 0, 0, 60, R.drawable.armor_penumbra_suit));
            armorList.add(createArmor(R.string.storm, Elements.AIR, false, 28, 128, 15, 6, 4, 160, 0, 170, 0, 170, 0, 0, R.drawable.armor_storm));
            armorList.add(createArmor(R.string.tunic_of_twisted_illusions, Elements.EARTH, false, 28, 128, 15, 4, 8, 100, 0, 80, 100, 0, 0, 80, R.drawable.armor_tunic_of_twisted_illusions));
            armorList.add(createArmor(R.string.dark_lord_set, Elements.DARKNESS, false, 45, 131, 8, 6, 4, 129, 80, 0, 80, 0, 0, 80, R.drawable.armor_dark_lord_set));
            armorList.add(createArmor(R.string.robe_of_distant_visions, Elements.EARTH, false, 29, 133, 15, 4, 3, 170, 0, 129, 0, 0, 0, 160, R.drawable.armor_robe_of_distant_visions));
            armorList.add(createArmor(R.string.bacoon_set, Elements.WATER, false, 28, 135, 16, 5, 5, 75, 60, 0, 0, 0, 80, 60, R.drawable.armor_bacoon_set));
            armorList.add(createArmor(R.string.firestorm_armor, Elements.FIRE, true, 28, 135, 16, 8, 3, 80, 80, 0, 50, 70, 0, 70, R.drawable.armor_firestorm_armor));
            armorList.add(createArmor(R.string.steelblow_armor, Elements.AIR, false, 28, 135, 16, 9, 5, 64, 70, 0, 80, 0, 0, 0, R.drawable.armor_steelblow_armor));
            armorList.add(createArmor(R.string.swamp_ranger_suit, Elements.NEUTRAL, true, 40, 136, 10, 5, 3, 70, 100, 0, 100, 129, 0, 129, R.drawable.armor_swamp_ranger_suit));
            armorList.add(createArmor(R.string.enchanted_nutcracker, Elements.NEUTRAL, false, 30, 138, 15, 2, 4, 100, 120, 120, 0, 0, 0, 0, R.drawable.armor_enchanted_nutcracker));
            armorList.add(createArmor(R.string.scarlet_shadowweaver_garb, Elements.DARKNESS, false, 30, 138, 15, 4, 6, 129, 0, 135, 135, 0, 0, 0, R.drawable.armor_scarlet_shadowweaver_garb));
            armorList.add(createArmor(R.string.tunic_of_frozen_voices, Elements.WATER, true, 30, 138, 15, 0, 0, 170, 0, 120, 129, 0, 0, 150, R.drawable.armor_tunic_of_frozen_voices));
            armorList.add(createArmor(R.string.vest_of_phantoms_keeper, Elements.DARKNESS, false, 30, 138, 15, 4, 2, 100, 120, 0, 120, 0, 0, 0, R.drawable.armor_vest_of_phantoms_keeper));
            armorList.add(createArmor(R.string.vest_of_twisted_illusions, Elements.DARKNESS, true, 30, 138, 15, 3, 0, 70, 0, 0, 0, 0, 0, 240, R.drawable.armor_vest_of_twisted_illusions));
            armorList.add(createArmor(R.string.wind_witch_dress, Elements.AIR, false, 30, 138, 15, 6, 4, 150, 110, 0, 140, 0, 0, 140, R.drawable.armor_wind_witch_suit));
            armorList.add(createArmor(R.string.steelblow_armor, Elements.AIR, false, 45, 142, 9, 8, 4, 60, 64, 0, 75, 0, 0, 0, R.drawable.armor_steelblow_armor));
            armorList.add(createArmor(R.string.bankers_coat, Elements.DARKNESS, false, 32, 147, 15, 5, 4, 60, 0, 0, 160, 0, 0, 0, R.drawable.armor_bankers_coat));
            armorList.add(createArmor(R.string.cthulhu_armor, Elements.NEUTRAL, false, 32, 147, 15, 2, 3, 129, 100, 100, 100, 100, 100, 100, R.drawable.armor_cthulhu_armor));
            armorList.add(createArmor(R.string.heros_set, Elements.AIR, false, 32, 147, 15, 14, 4, 70, 100, 0, 0, 0, 100, 100, R.drawable.armor_heros_set));
            armorList.add(createArmor(R.string.champion_set, Elements.NEUTRAL, false, 90, 154, 3, 4, 4, 70, 100, 0, 0, 0, 0, 120, R.drawable.armor_champion_set));
            armorList.add(createArmor(R.string.barrenlands_banditin, Elements.NEUTRAL, false, 34, 156, 15, 2, 3, 70, 0, 80, 200, 120, 0, 0, R.drawable.armor_barrenlands_banditin));
            armorList.add(createArmor(R.string.barrenlands_bandit, Elements.NEUTRAL, false, 34, 156, 15, 2, 3, 70, 179, 0, 129, 0, 150, 0, R.drawable.armor_barrenlands_bandit));
            armorList.add(createArmor(R.string.fire_moonkeeper_set, Elements.FIRE, true, 35, 161, 15, 6, 3, 100, 0, 110, 110, 0, 0, 110, R.drawable.armor_fire_moonkeeper_set));
            armorList.add(createArmor(R.string.robe_of_imminent_fires, Elements.FIRE, false, 35, 161, 15, 3, 0, 140, 0, 110, 0, 160, 0, 129, R.drawable.armor_robe_of_imminent_fires));
            armorList.add(createArmor(R.string.tunic_of_distant_ancestors, Elements.EARTH, false, 36, 165, 15, 3, 2, 150, 0, 0, 170, 160, 0, 0, R.drawable.armor_tunic_of_distant_ancestors));
            armorList.add(createArmor(R.string.vest_of_blessed_glory, Elements.LIGHT, false, 36, 165, 15, 3, 2, 104, 170, 0, 100, 0, 129, 160, R.drawable.armor_vest_of_blessed_glory));
            armorList.add(createArmor(R.string.desert_rogue_suit, Elements.NEUTRAL, true, 50, 170, 10, 6, 3, 70, 0, 0, 129, 129, 0, 160, R.drawable.armor_desert_rogue_suit));
            armorList.add(createArmor(R.string.tunic_of_the_relentless, Elements.WATER, true, 38, 174, 15, 6, 4, 154, 70, 80, 0, 0, 0, 89, R.drawable.armor_tunic_of_the_relentless));
            armorList.add(createArmor(R.string.special_dragon_vest, Elements.FIRE, false, 180, 180, 0, 6, 4, 80, 70, 89, 70, 89, 0, 0, R.drawable.armor_dragon_vest));
            armorList.add(createArmor(R.string.iron_widow, Elements.NEUTRAL, true, 56, 190, 10, 0, 0, 70, 140, 0, 0, 179, 140, 160, R.drawable.armor_iron_widow));
            armorList.add(createArmor(R.string.unholy_storm, Elements.WATER, false, 42, 193, 15, 6, 4, 150, 0, 80, 80, 0, 0, 80, R.drawable.armor_unholy_storm));
            armorList.add(createArmor(R.string.umbranian_tanker, Elements.NEUTRAL, false, 80, 195, 6, 3, 2, 0, 60, 0, 0, 60, 60, 0, R.drawable.armor_umbranian_tanker));
            armorList.add(createArmor(R.string.time_patroller_suit, Elements.NEUTRAL, false, 60, 203, 10, 4, 4, 300, 0, 0, 0, 0, 0, 0, R.drawable.armor_time_patroller_suit));
            armorList.add(createArmor(R.string.easterminator, Elements.NEUTRAL, false, 46, 211, 15, 5, 17, 50, 50, 0, 50, 0, 50, 0, R.drawable.armor_easterminator));
            armorList.add(createArmor(R.string.innkeepers_costume, Elements.NEUTRAL, false, 46, 211, 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, R.drawable.armor_innkeepers_costume));
            armorList.add(createArmor(R.string.greenfang_ensemble, Elements.DARKNESS, false, 48, 220, 15, 6, 4, 100, 0, 0, 135, 0, 135, 0, R.drawable.armor_greenfang_ensemble));
            armorList.add(createArmor(R.string.interrogator, Elements.WATER, false, 51, 234, 15, 0, 0, 120, 0, 0, 0, 160, 129, 150, R.drawable.armor_interrogator));
            armorList.add(createArmor(R.string.protector_of_the_oracle, Elements.EARTH, false, 51, 234, 15, -1, 0, 120, 0, 100, 0, 0, 160, 89, R.drawable.armor_protector_of_the_oracle));
            armorList.add(createArmor(R.string.destroyer_of_the_corrupted, Elements.LIGHT, false, 52, 239, 15, 0, -1, 80, 100, 0, 0, 140, 170, 0, R.drawable.armor_destroyer_of_the_corrupted));
            armorList.add(createArmor(R.string.warden_of_demons, Elements.DARKNESS, false, 52, 239, 15, 0, -1, 80, 140, 0, 0, 140, 140, 0, R.drawable.armor_warden_of_demons));
            armorList.add(createArmor(R.string.vanquisher, Elements.AIR, false, 55, 253, 15, 0, 0, 80, 170, 0, 0, 0, 0, 170, R.drawable.armor_vanquisher));
            armorList.add(createArmor(R.string.cataclysmic_plate, Elements.FIRE, false, 56, 257, 15, -1, -1, 55, 100, 0, 0, 170, 220, 0, R.drawable.armor_cataclysmic_plate));
            mediaPlayerItemPick = MediaPlayer.create(this, R.raw.bag);
            ImageAdapter adapter = new ImageAdapter(armorList, new ImageAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    playSound(mediaPlayerItemPick);
                    Armor selectedArmor = armorList.get(position);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedArmor", selectedArmor);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            layoutParams.topMargin = 0;
            recyclerView.setLayoutParams(layoutParams);
        } else if (buttonId == 2) {
            Log.d("ItemSelection", "Ring button clicked");
            ringList.add(createRing(R.string.blue_ring, Elements.NEUTRAL, 0, 0, 4, 2, 60, 0, 70, 70, 0, 0, 70, R.drawable.ring_blue_ring));
            ringList.add(createRing(R.string.cleric_ring, Elements.LIGHT, 0, 0, 1, 1, 19, 0, 19, 0, 0, 0, 0, R.drawable.ring_cleric_ring));
            ringList.add(createRing(R.string.dragon_fang_ring, Elements.NEUTRAL, 0, 0, 4, 4, 60, 70, 0, 0, 70, 70, 0, R.drawable.ring_dragon_fang_ring));
            ringList.add(createRing(R.string.narya, Elements.FIRE, 0, 0, 2, 0, 19, 0, 0, 0, 19, 0, 0, R.drawable.ring_shaman_narya_ring));
            ringList.add(createRing(R.string.nazguh_ring, Elements.DARKNESS, 0, 0, 0, 0, 19, 0, 19, 0, 0, 0, 0, R.drawable.ring_nazguh_ring));
            ringList.add(createRing(R.string.nenya, Elements.WATER, 0, 0, 4, 2, 50, 0, 0, 39, 0, 0, 50, R.drawable.ring_nenya));
            ringList.add(createRing(R.string.nightshade, Elements.NEUTRAL, 0, 0, 7, 9, 220, 0, 0, 0, 0, 0, 0, R.drawable.ring_nightshade));
            ringList.add(createRing(R.string.ring_of_agility, Elements.NEUTRAL, 0, 0, 2, 2, 0, 19, 0, 0, 0, 0, 0, R.drawable.ring_ring_of_agility));
            ringList.add(createRing(R.string.sea_wave_ring, Elements.WATER, 0, 0, 5, 3, 70, 60, 70, 0, 0, 70, 0, R.drawable.ring_sea_wave_ring));
            ringList.add(createRing(R.string.shaman_ring, Elements.NEUTRAL, 0, 0, 2, 2, 19, 0, 0, 0, 0, 0, 0, R.drawable.ring_shaman_narya_ring));
            ringList.add(createRing(R.string.spellmasters_ring, Elements.FIRE, 0, 0, 4, 2, 70, 0, 60, 0, 39, 0, 0, R.drawable.ring_spellmasters_ring));
            ringList.add(createRing(R.string.supersonic_ring, Elements.NEUTRAL, 0, -90, 70, 0, -90, 0, 0, 0, 0, 0, 0, R.drawable.ring_supersonic_ring));
            ringList.add(createRing(R.string.swordsmans_ring, Elements.AIR, 0, 0, 5, 2, 0, 50, 0, 0, 0, 0, 0, R.drawable.ring_swordsmans_ring));
            ringList.add(createRing(R.string.bunny_ring, Elements.NEUTRAL, 5, 0, 0, 10, 70, 0, 70, 60, 0, 0, 0, R.drawable.ring_bunny_ring));
            ringList.add(createRing(R.string.eastercreep, Elements.NEUTRAL, 5, 0, 0, 10, 60, 60, 0, 0, 0, 70, 0, R.drawable.ring_eastercreep));
            ringList.add(createRing(R.string.cursed_amethyst_ring, Elements.DARKNESS, 5, -40, 4, 3, 120, 0, 140, 0, 0, 0, 0, R.drawable.ring_cursed_amethyst_ring));
            ringList.add(createRing(R.string.falcon_ring, Elements.AIR, 5, 0, 8, 3, 80, 100, 0, 100, 0, 100, 0, R.drawable.ring_falcon_ring));
            ringList.add(createRing(R.string.ancient_warden, Elements.WATER, 8, 0, 4, 3, 80, 100, 100, 0, 0, 140, 0, R.drawable.ring_ancient_warden));
            ringList.add(createRing(R.string.pirouette_pointe, Elements.NEUTRAL, 10, 0, 5, 7, 39, 0, 0, 0, 0, 0, 0, R.drawable.ring_pirouette_pointe));
            ringList.add(createRing(R.string.warlocks_ring, Elements.DARKNESS, 13, 0, 4, 2, 50, 0, 35, 35, 0, 0, 0, R.drawable.ring_warlock_ring));
            ringList.add(createRing(R.string.ring_of_protection, Elements.EARTH, 14, 0, 2, 2, 14, 0, 10, 0, 0, 10, 0, R.drawable.ring_ring_of_protection));
            ringList.add(createRing(R.string.jellybunny, Elements.NEUTRAL, 15, 0, 0, 10, 60, 0, 60, 70, 0, 0, 60, R.drawable.ring_jellybunny));
            ringList.add(createRing(R.string.verdant_ring, Elements.EARTH, 16, 0, 4, 3, 70, 0, 70, 0, 0, 70, 0, R.drawable.ring_verdant_ring));
            ringList.add(createRing(R.string.all_seeing_ring, Elements.DARKNESS, 20, 0, 4, 2, 60, 60, 60, 0, 70, 0, 0, R.drawable.ring_all_seeing_ring));
            ringList.add(createRing(R.string.dragon_orb_ring, Elements.FIRE, 20, 0, 5, 2, 60, 0, 0, 0, 60, 0, 60, R.drawable.ring_dragon_orb_ring));
            ringList.add(createRing(R.string.steelblow_ring, Elements.AIR, 20, 0, 12, 3, 39, 50, 0, 30, 50, 0, 0, R.drawable.ring_steelblow_ring));
            ringList.add(createRing(R.string.trickytreat, Elements.NEUTRAL, 20, 0, 3, 3, 30, 19, 19, 19, 19, 19, 19, R.drawable.ring_trickytreat));
            ringList.add(createRing(R.string.volcano_ring, Elements.FIRE, 20, 0, 3, 2, 75, 64, 64, 75, 0, 0, 0, R.drawable.ring_volcano_ring));
            ringList.add(createRing(R.string.cait, Elements.NEUTRAL, 30, 0, 6, 4, 80, 0, 80, 80, 0, 0, 0, R.drawable.ring_cait));
            ringList.add(createRing(R.string.spektator, Elements.NEUTRAL, 30, 0, 4, 3, 110, 80, 80, 80, 0, 0, 80, R.drawable.ring_spektator));
            ringList.add(createRing(R.string.white_pearl_ring, Elements.LIGHT, 30, 0, 4, 3, 60, 0, 70, 0, 0, 0, 0, R.drawable.ring_white_pearl_ring));
            ringList.add(createRing(R.string.witchdoctor_ring, Elements.DARKNESS, 30, 0, 4, 2, 60, 0, 0, 50, 0, 50, 0, R.drawable.ring_witchdoctor_ring));
            ringList.add(createRing(R.string.dragon_eye_ring, Elements.EARTH, 36, 0, 4, 2, 60, 0, 0, 0, 0, 50, 70, R.drawable.ring_dragon_eye_ring));
            ringList.add(createRing(R.string.creepy_crawler, Elements.NEUTRAL, 40, 0, 3, 3, 89, 39, 39, 39, 39, 39, 39, R.drawable.ring_creepy_crawler));
            ringList.add(createRing(R.string.cuteulhu, Elements.NEUTRAL, 40, 0, 4, 3, 100, 0, 89, 0, 89, 89, 0, R.drawable.ring_cuteulhu));
            ringList.add(createRing(R.string.ghostwalker, Elements.NEUTRAL, 40, 150, -2, 0, 39, 0, 0, 0, 0, 0, 0, R.drawable.ring_ghostwalker));
            ringList.add(createRing(R.string.golden_guardian, Elements.NEUTRAL, 40, 0, 0, 0, 100, 0, 0, 0, 0, 150, 150, R.drawable.ring_golden_guardian));
            ringList.add(createRing(R.string.magic_cog, Elements.NEUTRAL, 40, 0, 5, 3, 210, 0, 0, 0, 0, 0, 0, R.drawable.ring_magic_cog));
            ringList.add(createRing(R.string.phoenix, Elements.FIRE, 40, 0, 0, 0, 100, 0, 0, 89, 0, 129, 89, R.drawable.ring_phoenix));
            ringList.add(createRing(R.string.ring_of_light, Elements.LIGHT, 40, 0, 4, 4, 60, 50, 0, 50, 0, 50, 0, R.drawable.ring_ring_of_light));
            ringList.add(createRing(R.string.vindicator_guardian, Elements.AIR, 40, 0, 5, 4, 100, 89, 0, 100, 0, 0, 110, R.drawable.ring_vindicator_guardian));
            ringList.add(createRing(R.string.whisper_of_death, Elements.DARKNESS, 40, 0, 5, 3, 100, 0, 89, 89, 0, 89, 0, R.drawable.ring_whisper_of_death));
            ringList.add(createRing(R.string.whisper_of_fire, Elements.FIRE, 40, 0, 5, 3, 100, 89, 89, 0, 89, 0, 0, R.drawable.ring_whisper_of_fire));
            ringList.add(createRing(R.string.whisper_of_light, Elements.LIGHT, 40, 0, 5, 3, 100, 89, 89, 0, 0, 120, 0, R.drawable.ring_whisper_of_light));
            ringList.add(createRing(R.string.vina, Elements.EARTH, 50, 0, 0, 0, 80, 100, 100, 0, 0, 0, 0, R.drawable.ring_vina));
            ImageAdapter adapter = new ImageAdapter(ringList, new ImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    playSound(mediaPlayerItemPick);
                    Ring selectedRing = ringList.get(position);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedRing", selectedRing);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close the ItemSelection activity after selecting an armor
                }
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            layoutParams.topMargin = 0;
            recyclerView.setLayoutParams(layoutParams);
        } else if (buttonId == 3) {
            Log.d("ItemSelection", "Weapon button clicked");
            swordList.add(createWeapon(R.string.recruit_sword, WeaponTypes.SWORD, Elements.NEUTRAL, 7, 12, 3, 0, 4, 0, R.drawable.sword_recruit_fire_sword));
            swordList.add(createWeapon(R.string.fire_sword, WeaponTypes.SWORD, Elements.FIRE, 8, 13, 3, 0, 4, 0, R.drawable.sword_recruit_fire_sword));
            swordList.add(createWeapon(R.string.steel_blade, WeaponTypes.SWORD, Elements.NEUTRAL, 11, 18, 3, 14, 5, 0, R.drawable.sword_steel_blade));
            swordList.add(createWeapon(R.string.master_sword, WeaponTypes.SWORD, Elements.AIR, 14, 24, 3, 20, 4, 2, R.drawable.sword_master_sword));
            swordList.add(createWeapon(R.string.vorpal_sword, WeaponTypes.SWORD, Elements.NEUTRAL, 25, 43, 3, 40, 6, 0, R.drawable.sword_vorpal_sword));
            swordList.add(createWeapon(R.string.pirate_sword, WeaponTypes.SWORD, Elements.NEUTRAL, 26, 44, 3, 10, 6, 4, R.drawable.sword_pirate_sword));
            swordList.add(createWeapon(R.string.bunny_sword, WeaponTypes.SWORD, Elements.EARTH, 49, 49, 0, 0, 0, 19, R.drawable.sword_bunny_sword));
            swordList.add(createWeapon(R.string.nobel_sword, WeaponTypes.SWORD, Elements.NEUTRAL, 29, 49, 3, 30, 4, 0, R.drawable.sword_nobel_sword));
            swordList.add(createWeapon(R.string.leaf_sword, WeaponTypes.SWORD, Elements.EARTH, 23, 72, 9, 25, 3, 2, R.drawable.sword_leaf_sword));
            swordList.add(createWeapon(R.string.magicsaber, WeaponTypes.SWORD, Elements.LIGHT, 27, 111, 13, 0, 3, 3, R.drawable.sword_magicsaber));
            swordList.add(createWeapon(R.string.darkskull_sword, WeaponTypes.SWORD, Elements.DARKNESS, 32, 124, 12, 20, 4, 0, R.drawable.sword_fireskull_darkskull_sword));
            swordList.add(createWeapon(R.string.umbranian_long_sword, WeaponTypes.SWORD, Elements.AIR, 38, 129, 10, 45, 7, 2, R.drawable.sword_umbranian_long_sword));
            swordList.add(createWeapon(R.string.umbranian_short_sword, WeaponTypes.SWORD, Elements.NEUTRAL, 32, 131, 13, 50, 7, 0, R.drawable.sword_umbranian_short_sword));
            swordList.add(createWeapon(R.string.fireskull_sword, WeaponTypes.SWORD, Elements.FIRE, 36, 139, 12, 30, 6, 0, R.drawable.sword_fireskull_darkskull_sword));
            swordList.add(createWeapon(R.string.sword_of_light, WeaponTypes.SWORD, Elements.LIGHT, 40, 145, 11, 0, 5, 2, R.drawable.sword_sword_of_light));
            swordList.add(createWeapon(R.string.throat_cutter, WeaponTypes.SWORD, Elements.DARKNESS, 50, 230, 15, 0, 4, 2, R.drawable.sword_throat_cutter));
            swordList.add(createWeapon(R.string.ice_claw, WeaponTypes.SWORD, Elements.WATER, 66, 303, 15, 40, 5, 2, R.drawable.sword_ice_claw));
            swordList.add(createWeapon(R.string.moonlit_razor, WeaponTypes.SWORD, Elements.NEUTRAL, 80, 329, 13, 40, 6, 3, R.drawable.sword_moonlit_razor));
            swordList.add(createWeapon(R.string.abyssal_blade, WeaponTypes.SWORD, Elements.WATER, 76, 349, 15, 50, 2, 2, R.drawable.sword_abyssal_blade));
            swordList.add(createWeapon(R.string.light_piercer, WeaponTypes.SWORD, Elements.LIGHT, 76, 349, 15, 0, 7, 3, R.drawable.sword_light_piercer));
            swordList.add(createWeapon(R.string.air_piercer, WeaponTypes.SWORD, Elements.AIR, 77, 354, 15, 0, 10, 3, R.drawable.sword_air_piercer));
            swordList.add(createWeapon(R.string.gorgon_blade, WeaponTypes.SWORD, Elements.NEUTRAL, 80, 368, 15, 40, 9, 0, R.drawable.sword_gorgon_blade));
            swordList.add(createWeapon(R.string.air_striker, WeaponTypes.SWORD, Elements.AIR, 85, 391, 15, 40, 7, 4, R.drawable.sword_air_striker));
            swordList.add(createWeapon(R.string.glass_blade, WeaponTypes.SWORD, Elements.NEUTRAL, 85, 391, 15, 14, 7, 3, R.drawable.sword_glass_blade));
            swordList.add(createWeapon(R.string.arachnid_sword, WeaponTypes.SWORD, Elements.NEUTRAL, 86, 395, 15, 20, 4, 6, R.drawable.sword_arachnid_sword));
            swordList.add(createWeapon(R.string.frost_heart, WeaponTypes.SWORD, Elements.DARKNESS, 88, 404, 15, 40, 5, 2, R.drawable.sword_frostheart));
            swordList.add(createWeapon(R.string.volcano_sword, WeaponTypes.SWORD, Elements.FIRE, 89, 409, 15, 25, 5, 0, R.drawable.sword_volcano_sword));
            swordList.add(createWeapon(R.string.blade_of_the_wicked, WeaponTypes.SWORD, Elements.NEUTRAL, 90, 414, 15, 0, 7, 4, R.drawable.sword_blade_of_the_wicked));
            swordList.add(createWeapon(R.string.cthulhu_blade, WeaponTypes.SWORD, Elements.NEUTRAL, 90, 414, 15, 35, 7, 3, R.drawable.sword_cthulhu_blade));
            swordList.add(createWeapon(R.string.goldsteel, WeaponTypes.SWORD, Elements.NEUTRAL, 90, 414, 15, 30, 5, 2, R.drawable.sword_goldsteel));
            swordList.add(createWeapon(R.string.bunny_claymore, WeaponTypes.SWORD, Elements.NEUTRAL, 92, 423, 15, 30, 6, 10, R.drawable.sword_bunny_claymore));
            swordList.add(createWeapon(R.string.golden_needle, WeaponTypes.SWORD, Elements.NEUTRAL, 94, 432, 15, 0, 6, 2, R.drawable.sword_golden_needle));
            swordList.add(createWeapon(R.string.blade_of_rusted_souls, WeaponTypes.SWORD, Elements.NEUTRAL, 99, 455, 15, 30, 3, 0, R.drawable.sword_blade_of_rusted_souls));
            swordList.add(createWeapon(R.string.haunted_slicer, WeaponTypes.SWORD, Elements.DARKNESS, 100, 460, 15, 20, 5, 3, R.drawable.sword_haunted_slicer));
            swordList.add(createWeapon(R.string.all_seeing_sword, WeaponTypes.SWORD, Elements.DARKNESS, 110, 506, 15, 30, 2, 2, R.drawable.sword_all_seeing_sword));
            swordList.add(createWeapon(R.string.sword_of_1000_lies, WeaponTypes.SWORD, Elements.FIRE, 115, 529, 15, 20, 0, 2, R.drawable.sword_sword_of_1000_lies));
            swordList.add(createWeapon(R.string.blade_of_survival, WeaponTypes.SWORD, Elements.NEUTRAL, 120, 552, 15, 40, 0, 0, R.drawable.sword_blade_of_survival));
            swordList.add(createWeapon(R.string.unleashed_kraken, WeaponTypes.SWORD, Elements.WATER, 130, 598, 15, 25, 2, 1, R.drawable.sword_unleashed_kraken));

            staffList.add(createWeapon(R.string.nature_staff, WeaponTypes.STAFF, Elements.EARTH, 8, 13, 3, 0, 0, 0, R.drawable.staff_nature_gadarasts_staff));
            staffList.add(createWeapon(R.string.fire_staff, WeaponTypes.STAFF, Elements.FIRE, 10, 17, 3, 0, 0, 0, R.drawable.staff_fire_staff));
            staffList.add(createWeapon(R.string.light_staff, WeaponTypes.STAFF, Elements.LIGHT, 12, 20, 3, 0, 1, 1, R.drawable.staff_light_staff));
            staffList.add(createWeapon(R.string.oxymos_staff, WeaponTypes.STAFF, Elements.LIGHT, 21, 96, 15, 0, 0, 2, R.drawable.staff_oxymos_staff));
            staffList.add(createWeapon(R.string.gadarasts_staff, WeaponTypes.STAFF, Elements.EARTH, 32, 108, 10, 30, 1, 2, R.drawable.staff_nature_gadarasts_staff));
            staffList.add(createWeapon(R.string.firebane, WeaponTypes.STAFF, Elements.FIRE, 41, 149, 11, 0, 2, 2, R.drawable.staff_firebane));
            staffList.add(createWeapon(R.string.staff_of_darkness, WeaponTypes.STAFF, Elements.DARKNESS, 41, 149, 11, 0, 2, 2, R.drawable.staff_staff_of_darkness));
            staffList.add(createWeapon(R.string.epic_fire_staff, WeaponTypes.STAFF, Elements.FIRE, 49, 178, 11, 0, 3, 1, R.drawable.staff_epic_fire_staff));
            staffList.add(createWeapon(R.string.necromorphicon, WeaponTypes.STAFF, Elements.DARKNESS, 39, 179, 15, -40, 0, 0, R.drawable.grimoire_necromorphicon));
            staffList.add(createWeapon(R.string.the_testament_of_ifrit, WeaponTypes.STAFF, Elements.FIRE, 39, 179, 15, 0, 8, 0, R.drawable.grimoire_the_testament_of_ifrit));
            staffList.add(createWeapon(R.string.the_arachnid_codex, WeaponTypes.STAFF, Elements.NEUTRAL, 42, 193, 15, -30, 3, 4, R.drawable.grimoire_the_arachnid_codex));
            staffList.add(createWeapon(R.string.compendium_of_trees, WeaponTypes.STAFF, Elements.EARTH, 43, 197, 15, 40, 3, 0, R.drawable.grimoire_compendium_of_trees));
            staffList.add(createWeapon(R.string.dragon_orb_staff, WeaponTypes.STAFF, Elements.FIRE, 48, 197, 13, 0, 1, 1, R.drawable.staff_dragon_orb_staff));
            staffList.add(createWeapon(R.string.the_oxymos_codex, WeaponTypes.STAFF, Elements.LIGHT, 44, 202, 15, -20, 10, 0, R.drawable.grimoire_the_oxymos_codex));
            staffList.add(createWeapon(R.string.the_abyssal_scrolls, WeaponTypes.STAFF, Elements.WATER, 48, 220, 15, 0, 3, 0, R.drawable.grimoire_the_abyssal_scrolls));
            staffList.add(createWeapon(R.string.the_word_of_the_wind, WeaponTypes.STAFF, Elements.AIR, 51, 234, 15, -20, 8, 0, R.drawable.grimoire_the_word_of_the_wind));
            staffList.add(createWeapon(R.string.broom_staff, WeaponTypes.STAFF, Elements.EARTH, 60, 276, 15, 0, 0, 9, R.drawable.staff_broom_staff));
            staffList.add(createWeapon(R.string.icebane, WeaponTypes.STAFF, Elements.WATER, 62, 285, 15, 30, 4, 2, R.drawable.staff_icebane));
            staffList.add(createWeapon(R.string.nightfall, WeaponTypes.STAFF, Elements.DARKNESS, 62, 285, 15, 30, 2, 4, R.drawable.staff_nightfall));
            staffList.add(createWeapon(R.string.tome_of_the_sands, WeaponTypes.STAFF, Elements.AIR, 66, 303, 15, -35, 10, 0, R.drawable.grimoire_tome_of_the_sands));
            staffList.add(createWeapon(R.string.hippie_dragon_staff, WeaponTypes.STAFF, Elements.EARTH, 70, 322, 15, 40, 5, 3, R.drawable.staff_hippie_dragon_staff));
            staffList.add(createWeapon(R.string.dragonskull_staff, WeaponTypes.STAFF, Elements.FIRE, 72, 331, 15, 0, 5, 1, R.drawable.staff_dragonskull_staff));
            staffList.add(createWeapon(R.string.legacy_of_the_oracle, WeaponTypes.STAFF, Elements.EARTH, 72, 331, 15, 45, 5, 2, R.drawable.staff_legacy_of_the_oracle));
            staffList.add(createWeapon(R.string.royal_war_staff, WeaponTypes.STAFF, Elements.NEUTRAL, 72, 331, 15, 20, 3, 2, R.drawable.staff_royal_war_staff));
            staffList.add(createWeapon(R.string.hippie_dragon_staff, WeaponTypes.STAFF, Elements.EARTH, 75, 345, 15, 30, 2, 2, R.drawable.staff_hippie_dragon_staff));
            staffList.add(createWeapon(R.string.seabane, WeaponTypes.STAFF, Elements.WATER, 75, 345, 15, 30, 4, 2, R.drawable.staff_seabane));
            staffList.add(createWeapon(R.string.golden_dragon_staff, WeaponTypes.STAFF, Elements.LIGHT, 76, 349, 15, 20, 3, 2, R.drawable.staff_golden_dragon_staff));
            staffList.add(createWeapon(R.string.cthulhu_staff, WeaponTypes.STAFF, Elements.DARKNESS, 77, 354, 15, 30, 0, 3, R.drawable.staff_cthulhu_staff));
            staffList.add(createWeapon(R.string.death_scythe, WeaponTypes.STAFF, Elements.DARKNESS, 80, 368, 15, 0, 4, 1, R.drawable.staff_death_scythe));
            staffList.add(createWeapon(R.string.water_staff, WeaponTypes.STAFF, Elements.WATER, 80, 368, 15, 0, 3, 3, R.drawable.staff_water_staff));
            staffList.add(createWeapon(R.string.death_scythe, WeaponTypes.STAFF, Elements.DARKNESS, 85, 391, 15, 0, 4, 3, R.drawable.staff_death_scythe));
            staffList.add(createWeapon(R.string.dragonskull_staff, WeaponTypes.STAFF, Elements.FIRE, 90, 414, 15, 0, 4, 2, R.drawable.staff_dragonskull_staff));
            staffList.add(createWeapon(R.string.golden_dragon_staff, WeaponTypes.STAFF, Elements.LIGHT, 90, 414, 15, 25, 5, 3, R.drawable.staff_golden_dragon_staff));
            staffList.add(createWeapon(R.string.spider_staff, WeaponTypes.STAFF, Elements.DARKNESS, 90, 414, 15, 0, 4, 4, R.drawable.staff_spider_staff));
            staffList.add(createWeapon(R.string.toxic_staff, WeaponTypes.STAFF, Elements.EARTH, 90, 414, 15, 0, 5, 2, R.drawable.staff_toxic_staff));
            staffList.add(createWeapon(R.string.windbane, WeaponTypes.STAFF, Elements.AIR, 90, 414, 15, 0, 8, 2, R.drawable.staff_windbane));
            staffList.add(createWeapon(R.string.dead_vulture_staff, WeaponTypes.STAFF, Elements.DARKNESS, 95, 437, 15, 0, 4, 1, R.drawable.staff_dead_vulture_staff));
            staffList.add(createWeapon(R.string.staff_of_the_wicked, WeaponTypes.STAFF, Elements.NEUTRAL, 110, 506, 15, 20, 3, 2, R.drawable.staff_staff_of_the_wicked));
            staffList.add(createWeapon(R.string.voidcaller_staff, WeaponTypes.STAFF, Elements.DARKNESS, 115, 529, 15, -30, 4, 0, R.drawable.staff_voidcaller_staff));
            staffList.add(createWeapon(R.string.ancient_stone_staff, WeaponTypes.STAFF, Elements.NEUTRAL, 120, 552, 15, 0, 0, 0, R.drawable.staff_ancient_stone_staff));

            daggerList.add(createWeapon(R.string.iron_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 4, 6, 3, 0, 5, 0, R.drawable.dagger_iron_fire_dagger));
            daggerList.add(createWeapon(R.string.fire_dagger, WeaponTypes.DAGGER, Elements.FIRE, 7, 12, 3, 0, 6, 0, R.drawable.dagger_iron_fire_dagger));
            daggerList.add(createWeapon(R.string.machete, WeaponTypes.DAGGER, Elements.NEUTRAL, 8, 13, 3, 0, 7, 0, R.drawable.dagger_machete));
            daggerList.add(createWeapon(R.string.chef_knife, WeaponTypes.DAGGER, Elements.NEUTRAL, 9, 15, 3, 0, 4, 2, R.drawable.dagger_chef_knife));
            daggerList.add(createWeapon(R.string.steel_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 16, 27, 3, 0, 6, 3, R.drawable.dagger_steel_dagger));
            daggerList.add(createWeapon(R.string.kunai, WeaponTypes.DAGGER, Elements.DARKNESS, 11, 45, 13, 0, 15, 3, R.drawable.dagger_kunai));
            daggerList.add(createWeapon(R.string.poisoned_dagger, WeaponTypes.DAGGER, Elements.EARTH, 10, 48, 16, 0, 19, 4, R.drawable.dagger_poisoned_dagger));
            daggerList.add(createWeapon(R.string.poisoned_dart, WeaponTypes.DAGGER, Elements.NEUTRAL, 15, 72, 16, 0, 19, 4, R.drawable.dagger_poisoned_dart));
            daggerList.add(createWeapon(R.string.light_dagger, WeaponTypes.DAGGER, Elements.LIGHT, 12, 75, 22, 0, 10, 10, R.drawable.dagger_light_dagger));
            daggerList.add(createWeapon(R.string.gorgon_dagger, WeaponTypes.DAGGER, Elements.EARTH, 24, 98, 13, 30, 14, 2, R.drawable.dagger_gorgon_dagger));
            daggerList.add(createWeapon(R.string.ifrits_dagger, WeaponTypes.DAGGER, Elements.FIRE, 22, 101, 15, 0, 19, 2, R.drawable.dagger_ifrits_dagger));
            daggerList.add(createWeapon(R.string.dagger_of_sand, WeaponTypes.DAGGER, Elements.AIR, 32, 131, 13, 0, 19, 2, R.drawable.dagger_dagger_of_sand));
            daggerList.add(createWeapon(R.string.dark_dagger, WeaponTypes.DAGGER, Elements.DARKNESS, 36, 148, 13, 0, 25, 2, R.drawable.dagger_dark_dagger));
            daggerList.add(createWeapon(R.string.precious_skewer, WeaponTypes.DAGGER, Elements.NEUTRAL, 35, 161, 15, 0, 30, 3, R.drawable.dagger_precious_skewer));
            daggerList.add(createWeapon(R.string.dark_dagger, WeaponTypes.DAGGER, Elements.DARKNESS, 40, 164, 13, 0, 25, 3, R.drawable.dagger_dark_dagger));
            daggerList.add(createWeapon(R.string.last_survivors_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 36, 165, 15, 0, 19, 3, R.drawable.dagger_last_survivors_dagger));
            daggerList.add(createWeapon(R.string.precious_dual_blade, WeaponTypes.DAGGER, Elements.NEUTRAL, 37, 170, 15, 0, 12, 5, R.drawable.dagger_precious_dual_blade));
            daggerList.add(createWeapon(R.string.bunnyhop_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 39, 179, 15, 0, 19, 17, R.drawable.dagger_bunnyhop_dagger));
            daggerList.add(createWeapon(R.string.gayan, WeaponTypes.DAGGER, Elements.EARTH, 42, 183, 14, 30, 27, 4, R.drawable.dagger_gayan));
            daggerList.add(createWeapon(R.string.ice_dagger, WeaponTypes.DAGGER, Elements.WATER, 40, 184, 15, 0, 14, 3, R.drawable.dagger_ice_dagger));
            daggerList.add(createWeapon(R.string.cthulhu_dagger, WeaponTypes.DAGGER, Elements.DARKNESS, 44, 202, 15, 20, 19, 3, R.drawable.dagger_cthulhu_dagger));
            daggerList.add(createWeapon(R.string.survivors_double_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 44, 202, 15, 0, 14, 5, R.drawable.dagger_survivors_double_dagger));
            daggerList.add(createWeapon(R.string.venomous_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 44, 202, 15, 10, 17, 2, R.drawable.dagger_venomous_dagger));
            daggerList.add(createWeapon(R.string.undines_dagger, WeaponTypes.DAGGER, Elements.WATER, 50, 206, 13, 0, 25, 4, R.drawable.dagger_undines_dagger));
            daggerList.add(createWeapon(R.string.dragon_tooth_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 45, 207, 15, -40, 25, 12, R.drawable.dagger_dragon_tooth_dagger));
            daggerList.add(createWeapon(R.string.sapphire_pawns_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 45, 207, 15, 0, 19, 0, R.drawable.dagger_sapphire_pawns_dagger));
            daggerList.add(createWeapon(R.string.dual_dragon_fang, WeaponTypes.DAGGER, Elements.NEUTRAL, 48, 220, 15, 0, 14, 5, R.drawable.dagger_dual_dragon_fang));
            daggerList.add(createWeapon(R.string.abyssal_dual_blade, WeaponTypes.DAGGER, Elements.WATER, 50, 230, 15, 0, 14, 5, R.drawable.dagger_abyssal_dual_blade));
            daggerList.add(createWeapon(R.string.toxic_dual_dagger, WeaponTypes.DAGGER, Elements.NEUTRAL, 50, 230, 15, 0, 14, 5, R.drawable.dagger_toxic_dual_dagger));
            daggerList.add(createWeapon(R.string.iron_dagger_plus, WeaponTypes.DAGGER, Elements.NEUTRAL, 52, 239, 15, -10, 25, 3, R.drawable.dagger_iron_dagger_plus));
            daggerList.add(createWeapon(R.string.malicious_quickblade, WeaponTypes.DAGGER, Elements.NEUTRAL, 52, 239, 15, 0, 19, 5, R.drawable.dagger_malicious_quickblade));
            daggerList.add(createWeapon(R.string.all_seeing_cleaver, WeaponTypes.DAGGER, Elements.NEUTRAL, 54, 248, 15, -15, 25, 3, R.drawable.dagger_all_seeing_cleaver));
            daggerList.add(createWeapon(R.string.ruby_pawns_edge, WeaponTypes.DAGGER, Elements.NEUTRAL, 58, 266, 15, -20, 25, 0, R.drawable.dagger_ruby_pawns_edge));
            daggerList.add(createWeapon(R.string.blood_swiftblade, WeaponTypes.DAGGER, Elements.FIRE, 61, 280, 15, 0, 25, 0, R.drawable.dagger_blood_swiftblade));
            daggerList.add(createWeapon(R.string.ifrits_shuriken, WeaponTypes.DAGGER, Elements.FIRE, 74, 340, 15, -20, 14, 3, R.drawable.shuriken_ifrits_shuriken));
            daggerList.add(createWeapon(R.string.undines_shuriken, WeaponTypes.DAGGER, Elements.WATER, 76, 349, 15, -20, 14, 3, R.drawable.shuriken_undines_shuriken));
            daggerList.add(createWeapon(R.string.gayan_shuriken, WeaponTypes.DAGGER, Elements.EARTH, 80, 368, 15, -10, 14, 3, R.drawable.shuriken_gayan_shuriken));
            daggerList.add(createWeapon(R.string.shooting_star_shuriken, WeaponTypes.DAGGER, Elements.LIGHT, 84, 386, 15, -20, 17, 0, R.drawable.shuriken_shooting_star_shuriken));

            axeList.add(createWeapon(R.string.silver_axe, WeaponTypes.AXE, Elements.NEUTRAL, 13, 22, 3, 35, 0, 0, R.drawable.axe_silver_fire_lightning_axe));
            axeList.add(createWeapon(R.string.umbranian_cutter, WeaponTypes.AXE, Elements.NEUTRAL, 35, 60, 3, 30, 0, 0, R.drawable.axe_umbranian_flaming_cutter));
            axeList.add(createWeapon(R.string.bone_chopper, WeaponTypes.AXE, Elements.AIR, 22, 69, 9, 35, 6, 3, R.drawable.axe_bone_chopper));
            axeList.add(createWeapon(R.string.axe_of_fire, WeaponTypes.AXE, Elements.FIRE, 17, 78, 15, 30, 3, 0, R.drawable.axe_silver_fire_lightning_axe));
            axeList.add(createWeapon(R.string.flaming_cutter, WeaponTypes.AXE, Elements.FIRE, 24, 110, 15, 40, 4, 1, R.drawable.axe_umbranian_flaming_cutter));
            axeList.add(createWeapon(R.string.lightning_axe, WeaponTypes.AXE, Elements.LIGHT, 25, 115, 15, 0, 3, 2, R.drawable.axe_silver_fire_lightning_axe));
            axeList.add(createWeapon(R.string.spider_axe, WeaponTypes.AXE, Elements.FIRE, 40, 164, 13, 40, 3, 3, R.drawable.axe_spider_axe));
            axeList.add(createWeapon(R.string.fortunes_battleaxe, WeaponTypes.AXE, Elements.NEUTRAL, 48, 220, 15, 35, 0, 0, R.drawable.axe_fortunes_battleaxe));
            axeList.add(createWeapon(R.string.toxic_axe, WeaponTypes.AXE, Elements.NEUTRAL, 52, 239, 15, 60, 3, 2, R.drawable.axe_toxic_axe));
            axeList.add(createWeapon(R.string.axe_of_rusted_gods, WeaponTypes.AXE, Elements.NEUTRAL, 53, 243, 15, 35, 0, 0, R.drawable.axe_axe_of_rusted_gods));
            axeList.add(createWeapon(R.string.bone_axe, WeaponTypes.AXE, Elements.NEUTRAL, 60, 276, 15, 20, 7, 3, R.drawable.axe_bone_axe));
            axeList.add(createWeapon(R.string.stormcaller, WeaponTypes.AXE, Elements.WATER, 65, 299, 15, 40, 2, 3, R.drawable.axe_stormcaller));
            axeList.add(createWeapon(R.string.ice_axe, WeaponTypes.AXE, Elements.NEUTRAL, 70, 322, 15, 20, 0, 0, R.drawable.axe_ice_axe));
            axeList.add(createWeapon(R.string.steel_axe, WeaponTypes.AXE, Elements.NEUTRAL, 70, 322, 15, 35, 0, 0, R.drawable.axe_steel_axe));
            axeList.add(createWeapon(R.string.cthulhu_axe, WeaponTypes.AXE, Elements.DARKNESS, 74, 340, 15, 70, 4, 3, R.drawable.axe_cthulhu_axe));
            axeList.add(createWeapon(R.string.path_of_exile, WeaponTypes.AXE, Elements.FIRE, 74, 340, 15, 70, 4, 3, R.drawable.axe_path_of_exile));
            axeList.add(createWeapon(R.string.windpearl_axe, WeaponTypes.AXE, Elements.AIR, 75, 345, 15, 20, 8, 1, R.drawable.axe_windpearl_axe));
            axeList.add(createWeapon(R.string.dawnlight, WeaponTypes.AXE, Elements.LIGHT, 80, 368, 15, 70, 4, 3, R.drawable.axe_dawnlight));
            axeList.add(createWeapon(R.string.gorgon_axe, WeaponTypes.AXE, Elements.EARTH, 90, 414, 15, 60, 6, 1, R.drawable.axe_gorgon_axe));
            axeList.add(createWeapon(R.string.all_seeing_axe, WeaponTypes.AXE, Elements.DARKNESS, 98, 450, 15, 70, 3, 3, R.drawable.axe_all_seeing_axe));

            spearList.add(createWeapon(R.string.obsidian_spear, WeaponTypes.SPEAR, Elements.NEUTRAL, 40, 164, 13, 20, 4, 3, R.drawable.spear_obsidian_spear));
            spearList.add(createWeapon(R.string.cthulhu_spear, WeaponTypes.SPEAR, Elements.DARKNESS, 42, 193, 15, 100, 0, 0, R.drawable.spear_cthulhu_spear));
            spearList.add(createWeapon(R.string.gayan_spear, WeaponTypes.SPEAR, Elements.EARTH, 53, 243, 15, 65, 3, 4, R.drawable.spear_gayan_spear));
            spearList.add(createWeapon(R.string.golden_piercer, WeaponTypes.SPEAR, Elements.NEUTRAL, 55, 253, 15, 20, 4, 3, R.drawable.spear_golden_piercer));
            spearList.add(createWeapon(R.string.yansahn, WeaponTypes.SPEAR, Elements.AIR, 57, 262, 15, 0, 10, 4, R.drawable.spear_yansahn));
            spearList.add(createWeapon(R.string.fire_pierce, WeaponTypes.SPEAR, Elements.FIRE, 58, 266, 15, 0, 5, 3, R.drawable.spear_fire_pierce));
            spearList.add(createWeapon(R.string.freyr, WeaponTypes.SPEAR, Elements.LIGHT, 58, 266, 15, 40, 6, 7, R.drawable.spear_freyr));
            spearList.add(createWeapon(R.string.poisoned_spear, WeaponTypes.SPEAR, Elements.EARTH, 58, 266, 15, 0, 10, 0, R.drawable.spear_poisoned_spear));
            spearList.add(createWeapon(R.string.last_survivors_spear, WeaponTypes.SPEAR, Elements.NEUTRAL, 61, 280, 15, 20, 2, 2, R.drawable.spear_last_survivors_spear));
            spearList.add(createWeapon(R.string.gorgon_spear, WeaponTypes.SPEAR, Elements.NEUTRAL, 64, 294, 15, 0, 10, 0, R.drawable.spear_gorgon_spear));
            spearList.add(createWeapon(R.string.osun, WeaponTypes.SPEAR, Elements.WATER, 64, 294, 15, 20, 5, 4, R.drawable.spear_osun));
            spearList.add(createWeapon(R.string.azatoth, WeaponTypes.SPEAR, Elements.DARKNESS, 67, 308, 15, 30, 5, 3, R.drawable.spear_azatoth));
            spearList.add(createWeapon(R.string.spider_spear, WeaponTypes.SPEAR, Elements.EARTH, 68, 312, 15, 25, 3, 12, R.drawable.spear_spider_spear));
            spearList.add(createWeapon(R.string.wicked_skewer, WeaponTypes.SPEAR, Elements.FIRE, 72, 331, 15, 0, 5, 3, R.drawable.spear_wicked_skewer));
            spearList.add(createWeapon(R.string.ruby_queens_spear, WeaponTypes.SPEAR, Elements.FIRE, 74, 340, 15, 0, 5, 3, R.drawable.spear_ruby_queens_spear));
            spearList.add(createWeapon(R.string.vindictive_piercer, WeaponTypes.SPEAR, Elements.AIR, 80, 368, 15, 0, 10, 4, R.drawable.spear_vindictive_piercer));
            spearList.add(createWeapon(R.string.onyx_queens_lance, WeaponTypes.SPEAR, Elements.WATER, 84, 386, 15, 0, 5, 0, R.drawable.spear_onyx_queens_lance));

            hammerList.add(createWeapon(R.string.wooden_hawaiian, WeaponTypes.HAMMER, Elements.NEUTRAL, 10, 17, 3, 0, 0, 0, R.drawable.hammer_wooden_hammer));
            hammerList.add(createWeapon(R.string.silver_mace, WeaponTypes.HAMMER, Elements.NEUTRAL, 12, 20, 3, 35, 0, 0, R.drawable.axe_silver_fire_lightning_axe));
            hammerList.add(createWeapon(R.string.club, WeaponTypes.HAMMER, Elements.NEUTRAL, 16, 27, 3, 30, 0, 0, R.drawable.mace_club));
            hammerList.add(createWeapon(R.string.insect_crusher, WeaponTypes.HAMMER, Elements.NEUTRAL, 16, 27, 3, 35, 0, 0, R.drawable.hammer_insect_crusher));
            hammerList.add(createWeapon(R.string.iron_mace, WeaponTypes.HAMMER, Elements.NEUTRAL, 16, 27, 3, 30, 0, 0, R.drawable.mace_iron_mace));
            hammerList.add(createWeapon(R.string.wooden_hammer, WeaponTypes.HAMMER, Elements.EARTH, 17, 41, 6, 35, 0, 0, R.drawable.hammer_wooden_hammer));
            hammerList.add(createWeapon(R.string.bionic_hammer, WeaponTypes.HAMMER, Elements.NEUTRAL, 38, 65, 3, 35, 0, 6, R.drawable.hammer_bionic_hammer));
            hammerList.add(createWeapon(R.string.warhammer, WeaponTypes.HAMMER, Elements.NEUTRAL, 28, 108, 12, 50, 0, 0, R.drawable.hammer_warhammer));
            hammerList.add(createWeapon(R.string.dark_mace, WeaponTypes.HAMMER, Elements.DARKNESS, 35, 127, 11, 50, 2, 1, R.drawable.mace_silver_dark_mace));
            hammerList.add(createWeapon(R.string.spider_crusher, WeaponTypes.HAMMER, Elements.EARTH, 47, 159, 10, 35, 0, 2, R.drawable.hammer_spider_crusher));
            hammerList.add(createWeapon(R.string.inquisidor, WeaponTypes.HAMMER, Elements.DARKNESS, 40, 164, 13, 50, 1, 1, R.drawable.hammer_inquisidor));
            hammerList.add(createWeapon(R.string.thunder_hammer, WeaponTypes.HAMMER, Elements.NEUTRAL, 70, 204, 8, 45, 0, 2, R.drawable.hammer_thunder_hammer));
            hammerList.add(createWeapon(R.string.liquid_hammer, WeaponTypes.HAMMER, Elements.WATER, 46, 211, 15, 65, 5, 1, R.drawable.hammer_liquid_hammer));
            hammerList.add(createWeapon(R.string.toxic_mace, WeaponTypes.HAMMER, Elements.NEUTRAL, 46, 211, 15, 50, 2, 0, R.drawable.mace_toxic_mace));
            hammerList.add(createWeapon(R.string.wealthy_crusher, WeaponTypes.HAMMER, Elements.NEUTRAL, 46, 211, 15, 70, 2, 0, R.drawable.mace_wealthy_crusher));
            hammerList.add(createWeapon(R.string.wealthy_smasher, WeaponTypes.HAMMER, Elements.NEUTRAL, 46, 211, 15, 70, 0, 2, R.drawable.hammer_wealthy_smasher));
            hammerList.add(createWeapon(R.string.falcon_hammer, WeaponTypes.HAMMER, Elements.AIR, 45, 217, 16, 60, 9, 2, R.drawable.hammer_falcon_hammer));
            hammerList.add(createWeapon(R.string.hammer_of_light, WeaponTypes.HAMMER, Elements.LIGHT, 48, 220, 15, 50, 3, 3, R.drawable.hammer_hammer_of_light));
            hammerList.add(createWeapon(R.string.toxic_hammer, WeaponTypes.HAMMER, Elements.NEUTRAL, 48, 220, 15, 70, 0, 0, R.drawable.hammer_toxic_hammer));
            hammerList.add(createWeapon(R.string.volcano_hammer, WeaponTypes.HAMMER, Elements.FIRE, 47, 227, 16, 80, 0, 0, R.drawable.hammer_volcano_hammer));
            hammerList.add(createWeapon(R.string.verdant_hammer, WeaponTypes.HAMMER, Elements.EARTH, 48, 232, 16, 80, 0, 3, R.drawable.hammer_verdant_hammer));
            hammerList.add(createWeapon(R.string.smasher_of_decay, WeaponTypes.HAMMER, Elements.NEUTRAL, 52, 239, 15, 50, 0, 0, R.drawable.hammer_smasher_of_decay));
            hammerList.add(createWeapon(R.string.war_forged_smasher, WeaponTypes.HAMMER, Elements.FIRE, 53, 243, 15, 70, 0, 0, R.drawable.hammer_war_forged_smasher));
            hammerList.add(createWeapon(R.string.mojaras_hammer, WeaponTypes.HAMMER, Elements.DARKNESS, 55, 266, 16, 70, 2, 2, R.drawable.hammer_mojaras_hammer));
            hammerList.add(createWeapon(R.string.enlightened_hammer, WeaponTypes.HAMMER, Elements.LIGHT, 61, 280, 15, 60, 0, 0, R.drawable.hammer_enlightened_hammer));
            hammerList.add(createWeapon(R.string.bone_hammer, WeaponTypes.HAMMER, Elements.NEUTRAL, 62, 285, 15, 70, 0, 2, R.drawable.hammer_bone_hammer));
            hammerList.add(createWeapon(R.string.wit_of_ancient_power, WeaponTypes.HAMMER, Elements.NEUTRAL, 60, 290, 16, 80, 0, 2, R.drawable.hammer_wit_of_ancient_power));
            mediaPlayerItemPick = MediaPlayer.create(this, R.raw.projectile_heavy_blade_withdraw);
            ImageAdapter adapter = new ImageAdapter(swordList, new ImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    playSound(mediaPlayerItemPick);
                    Weapon selectedWeapon = swordList.get(position);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedWeapon", selectedWeapon);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close the ItemSelection activity after selecting an armor
                }
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.VISIBLE);
            layoutParams.topMargin = 165;
        } else if (buttonId == 4) {
            Log.d("ItemSelection", "Class button clicked");

            characterList.add(createCharacterClass(ClassNames.ROGUE, 0, 0, -10, 45, -30, -30, 0, 0, 6, 5, R.drawable.class_rogue));
            characterList.add(createCharacterClass(ClassNames.THIEF, 0, 0, 20, 30, 30, 0, 0, -30, 6, 7, R.drawable.class_thief));
            characterList.add(createCharacterClass(ClassNames.MAGE, 0, 30, 0, 0, -30, 20, 0, 30, 2, 4, R.drawable.class_mage));
            characterList.add(createCharacterClass(ClassNames.BLACK_MAGE, 0, 30, 0, 20, -30, 0, 0, 30, 4, 2, R.drawable.class_black_mage));
            characterList.add(createCharacterClass(ClassNames.HIGH_MAGE, 0, 35, 20, 0, -30, 0, 0, 30, 6, 4, R.drawable.class_high_mage));
            characterList.add(createCharacterClass(ClassNames.PRIEST, 20, 20, 0, -30, 0, 0, 0, 20, 3, 4, R.drawable.class_priest));
            characterList.add(createCharacterClass(ClassNames.WARLOCK, 0, 30, 25, 20, 0, 20, 40, 0, 4, 0, R.drawable.class_warlock));
            characterList.add(createCharacterClass(ClassNames.WARRIOR, 30, 0, 30, 0, 30, 30, 0, -40, 0, 0, R.drawable.class_warrior));
            characterList.add(createCharacterClass(ClassNames.ELITE_WARRIOR, 40, 0, 35, 0, 35, 35, 0, -40, 0, 5, R.drawable.class_elite_warrior));
            characterList.add(createCharacterClass(ClassNames.PALADIN, 20, 20, 20, 0, 20, 20, 0, 0, 0, 0, R.drawable.class_paladin));
            characterList.add(createCharacterClass(ClassNames.RANGER, 20, 0, 35, 0, 0, 25, 25, 0, 0, 5, R.drawable.class_ranger));
            characterList.add(createCharacterClass(ClassNames.WITCHDOCTOR, 0, 30, 0, 25, 0, 0, 0, 25, 0, 6, R.drawable.class_witchdoctor));
            characterList.add(createCharacterClass(ClassNames.DRUID, 0, 30, 0, 0, 0, 0, 30, 20, 3, 6, R.drawable.class_druid));
            mediaPlayerItemPick = MediaPlayer.create(this, R.raw.bag);
            ImageAdapter adapter = new ImageAdapter(characterList, new ImageAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    playSound(mediaPlayerItemPick);
                    CharacterClass selectedClass = characterList.get(position);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("selectedClass", selectedClass);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close the ItemSelection activity after selecting an armor
                }
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            layoutParams.topMargin = 0;
            recyclerView.setLayoutParams(layoutParams);
        }
    }

    private Armor createArmor(int nameResId, Elements element, boolean frostImmune, int minArmor, int maxArmor, int upgrades, int speed, int jump, int magic, int sword, int staff, int dagger, int axe, int hammer, int spear, int imageResourceId) {
        String name = getResources().getString(nameResId);
        Bitmap armorBitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(armorBitmap, 300, 300, true);
        return new Armor(name, element, frostImmune, minArmor, maxArmor, upgrades, speed, jump, magic, sword, staff, dagger, axe, hammer, spear, resizedBitmap);
    }


    private Ring createRing(int nameResId, Elements element, int armor, int armorBonus, int speed, int jump, int magic, int sword, int staff, int dagger, int axe, int hammer, int spear, int imageResourceId) {
        String name = getResources().getString(nameResId);
        Bitmap ringBitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(ringBitmap, 300, 300, true);
        return new Ring(name, element, armor, armorBonus, speed, jump, magic, sword, staff, dagger, axe, hammer, spear, resizedBitmap);
    }


    private Weapon createWeapon(int nameResId, WeaponTypes type, Elements element, int minDamage, int maxDamage, int upgrades, int armorBonus, int speed, int jump, int imageResourceId) {
        String name = getResources().getString(nameResId);
        Bitmap weaponBitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
        return new Weapon(name, type, element, minDamage, maxDamage, upgrades, armorBonus, speed, jump, weaponBitmap);
    }

    private CharacterClass createCharacterClass(ClassNames name, int armorBonus, int magicBonus, int swordBonus, int daggerBonus, int hammerBonus, int axeBonus, int spearBonus, int staffBonus, int speedBonus, int jumpImpulseBonus, int imageResourceId) {
        Bitmap classBitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
        return new CharacterClass(name, armorBonus, magicBonus, swordBonus, daggerBonus, hammerBonus, axeBonus, spearBonus, staffBonus,  speedBonus, jumpImpulseBonus, classBitmap);
    }

    private void playSound(MediaPlayer mediaPlayer) {
        // Check if MediaPlayer is null or not
        if (mediaPlayer != null) {
            // Reset MediaPlayer if it's already playing
            mediaPlayer.seekTo(0);

            // Set volume to 50%
            float volume = 0.5f; // 50%
            mediaPlayer.setVolume(volume, volume);

            mediaPlayer.start();
        }
    }

    protected void onDestroy(MediaPlayer mediaPlayer) {
        super.onDestroy();
        // Release MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
