package xyz.magicrampagecompanion;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.text.DecimalFormat;

public class EquipmentTester extends AppCompatActivity {

    private ImageView armorButton;
    private Armor selectedArmor;
    private ImageView ringButton;
    private Ring selectedRing;
    private ImageView weaponButton;
    private Weapon selectedWeapon;
    private CharacterClass selectedClass;
    private ImageView classButton;
    private ImageView skillButton;

    private boolean[] skillsPicked;

    private ImageView lockButton1;
    private ImageView lockButton2;
    private LinearLayout upgradeArmorButtonsLayout;
    private LinearLayout upgradeWeaponButtonsLayout;
    private TextView armorUpgradesView;
    private TextView weaponUpgradesView;
    private ImageButton armorElementButton;
    private ImageButton weaponElementButton;
    private ImageButton ringElementButton;
    private int currentArmorUpgrades;
    private int currentWeaponUpgrades;
    Elements initialArmorElement;
    Elements initialWeaponElement;
    Elements initialRingElement;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_tester);

        mediaPlayer = MediaPlayer.create(this, R.raw.metallic_button);

        TextView fireValueTextView = findViewById(R.id.fireResistance);
        String helpText = getString(R.string.fire_resistance) + "  ";
        SpannableString spannable = new SpannableString(helpText);
        Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
        int desiredWidth = 80;
        int desiredHeight = 80;
        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
        ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fireValueTextView.setText(spannable);

        TextView spikeValueTextView = findViewById(R.id.spikeResistance);
        String helpText1 = getString(R.string.spike_resistance) + "  ";
        spannable = new SpannableString(helpText);
        customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
        imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spikeValueTextView.setText(spannable);

        TextView frostValueTextView = findViewById(R.id.frostResistance);
        String helpText2 = getString(R.string.frost_resistance) + "  ";
        spannable = new SpannableString(helpText2);
        customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
        imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        frostValueTextView.setText(spannable);

        TextView projectilePersistenceValueTextView = findViewById(R.id.projectilePersistence);
        String helpText3 = getString(R.string.projectile_persistence) + "  ";
        spannable = new SpannableString(helpText3);
        customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
        imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        projectilePersistenceValueTextView.setText(spannable);

        TextView poisonousAttackValueTextView = findViewById(R.id.poisonousAttack);
        String helpText4 = getString(R.string.poisonous_attack) + "  ";
        spannable = new SpannableString(helpText4);
        customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
        imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        poisonousAttackValueTextView.setText(spannable);

        TextView frostAttackValueTextView = findViewById(R.id.frostAttack);
        String helpText5 = getString(R.string.frost_attack) + "  ";
        spannable = new SpannableString(helpText5);
        customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
        imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        frostAttackValueTextView.setText(spannable);

        lockButton1 = findViewById(R.id.lockButton1);
        upgradeArmorButtonsLayout = findViewById(R.id.upgradeArmorButtonsLayout);
        upgradeArmorButtonsLayout.setVisibility(View.GONE);
        armorUpgradesView = findViewById(R.id.armorUpgradesView);
        armorUpgradesView.setVisibility(View.GONE);
        ImageButton arrowPlusMaxButton1 = findViewById(R.id.ButtonPlusMax1);
        ImageButton arrowMinusMaxButton1 = findViewById(R.id.ButtonMinusMax1);
        ImageButton arrowPlusButton1 = findViewById(R.id.ButtonPlus1);
        ImageButton arrowMinusButton1 = findViewById(R.id.ButtonMinus1);
        armorElementButton = findViewById(R.id.ButtonElement1);
        armorElementButton.setVisibility(View.GONE);

        lockButton2 = findViewById(R.id.lockButton2);
        upgradeWeaponButtonsLayout = findViewById(R.id.upgradeWeaponButtonsLayout);
        upgradeWeaponButtonsLayout.setVisibility(View.GONE);
        weaponUpgradesView = findViewById(R.id.weaponUpgradesView);
        weaponUpgradesView.setVisibility(View.GONE);
        ImageButton arrowPlusMaxButton2 = findViewById(R.id.ButtonPlusMax2);
        ImageButton arrowMinusMaxButton2 = findViewById(R.id.ButtonMinusMax2);
        ImageButton arrowPlusButton2 = findViewById(R.id.ButtonPlus2);
        ImageButton arrowMinusButton2 = findViewById(R.id.ButtonMinus2);
        weaponElementButton = findViewById(R.id.ButtonElement2);
        weaponElementButton.setVisibility(View.GONE);

        ringElementButton= findViewById(R.id.ButtonElement3);
        ringElementButton.setVisibility(View.GONE);

        armorButton = findViewById(R.id.ArmorButton);
        armorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openItemSelection(1);
                playSound();// 1 for "armor" button
            }
        });

        ringButton = findViewById(R.id.RingButton);
        ringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openItemSelection(2);
                playSound(); // 2 for "ring" button
            }
        });

        weaponButton = findViewById(R.id.WeaponButton);
        weaponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openItemSelection(3);
                playSound();// 3 for "weapon" button
            }
        });

        classButton = findViewById(R.id.ClassButton);
        classButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openItemSelection(4);
                playSound();// 4 for "class" button
            }
        });

        skillButton = findViewById(R.id.SkillTreeButton);
        skillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSkillPicker();
                playSound();// 4 for "class" button
            }
        });

        arrowPlusMaxButton1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                currentArmorUpgrades = selectedArmor.getUpgrades();
                calculateStats();
                armorUpgradesView.setText(getString(R.string.upgrades) + " " + currentArmorUpgrades + " " + getString(R.string.max));
            }
        });

        arrowMinusMaxButton1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                currentArmorUpgrades = 0;
                calculateStats();
                armorUpgradesView.setText(getString(R.string.upgrades) + " " + currentArmorUpgrades);
            }
        });

        arrowPlusButton1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (currentArmorUpgrades != selectedArmor.getUpgrades())
                    currentArmorUpgrades += 1;
                else
                    Toast.makeText(EquipmentTester.this, R.string.item_already_maxed, Toast.LENGTH_SHORT).show();
                calculateStats();
                if (currentArmorUpgrades == selectedArmor.getUpgrades())
                    armorUpgradesView.setText(getString(R.string.upgrades) + " " + currentArmorUpgrades + " " + getString(R.string.max));
                else
                    armorUpgradesView.setText(getString(R.string.upgrades) + " " + currentArmorUpgrades);
            }
        });

        arrowMinusButton1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (currentArmorUpgrades != 0)
                    currentArmorUpgrades -= 1;
                else
                    Toast.makeText(EquipmentTester.this, R.string.item_already_at_zero_upgrades, Toast.LENGTH_SHORT).show();
                calculateStats();
                armorUpgradesView.setText(getString(R.string.upgrades) + " " + currentArmorUpgrades);
            }
        });

        armorElementButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!initialArmorElement.equals(Elements.NEUTRAL)) {
                    Toast.makeText(EquipmentTester.this, R.string.element_cannot_be_picked, Toast.LENGTH_SHORT).show();
                } else {
                    // Create and show a dialog with the 6 selectable items
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EquipmentTester.this);
                    dialogBuilder.setTitle("Select Element");

                    // Create an array of element options
                    String[] elementOptions = {
                            getString(R.string.element_fire),
                            getString(R.string.element_water),
                            getString(R.string.element_darkness),
                            getString(R.string.element_light),
                            getString(R.string.element_earth),
                            getString(R.string.element_air)
                    };

                    dialogBuilder.setItems(elementOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle the selected element option
                            String selectedElement = elementOptions[which];

                            // Update UI or perform any necessary actions with the selected element
                            int elementDrawableResource = 0;
                            Elements newElement = Elements.NEUTRAL;

                            switch (selectedElement) {
                                case "Fire":
                                    elementDrawableResource = R.drawable.essence_fire;
                                    newElement = Elements.FIRE;
                                    break;
                                case "Water":
                                    elementDrawableResource = R.drawable.essence_water;
                                    newElement = Elements.WATER;
                                    break;
                                case "Darkness":
                                    elementDrawableResource = R.drawable.essence_darkness;
                                    newElement = Elements.DARKNESS;
                                    break;
                                case "Light":
                                    elementDrawableResource = R.drawable.essence_light;
                                    newElement = Elements.LIGHT;
                                    break;
                                case "Earth":
                                    elementDrawableResource = R.drawable.essence_earth;
                                    newElement = Elements.EARTH;
                                    break;
                                case "Air":
                                    elementDrawableResource = R.drawable.essence_air;
                                    newElement = Elements.AIR;
                                    break;
                            }

                            // Update the armor element button's image and the selected armor's element
                            armorElementButton.setImageResource(elementDrawableResource);
                            selectedArmor.setElement(newElement);
                            calculateStats();

                        }
                    });

                    // Show the dialog
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
            }
        });

        arrowPlusMaxButton2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                currentWeaponUpgrades = selectedWeapon.getUpgrades();
                calculateStats();
                weaponUpgradesView.setText(getString(R.string.upgrades) + " " + currentWeaponUpgrades + " " + getString(R.string.max));
            }
        });

        arrowMinusMaxButton2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                currentWeaponUpgrades = 0;
                calculateStats();
                weaponUpgradesView.setText(getString(R.string.upgrades) + " " + currentWeaponUpgrades);
            }
        });

        arrowPlusButton2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (currentWeaponUpgrades != selectedWeapon.getUpgrades())
                    currentWeaponUpgrades += 1;
                else
                    Toast.makeText(EquipmentTester.this, R.string.item_already_maxed, Toast.LENGTH_SHORT).show();
                calculateStats();
                if (currentWeaponUpgrades == selectedWeapon.getUpgrades())
                    weaponUpgradesView.setText(getString(R.string.upgrades) + " " + currentWeaponUpgrades + " " + getString(R.string.max));
                else
                    weaponUpgradesView.setText(getString(R.string.upgrades) + " " + currentWeaponUpgrades);
            }
        });

        arrowMinusButton2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (currentWeaponUpgrades != 0)
                    currentWeaponUpgrades -= 1;
                else
                    Toast.makeText(EquipmentTester.this, R.string.item_already_at_zero_upgrades, Toast.LENGTH_SHORT).show();
                calculateStats();
                weaponUpgradesView.setText(getString(R.string.upgrades) + " " + currentWeaponUpgrades);
            }
        });

        weaponElementButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!initialWeaponElement.equals(Elements.NEUTRAL)) {
                    Toast.makeText(EquipmentTester.this, R.string.element_cannot_be_picked, Toast.LENGTH_SHORT).show();
                } else {
                    // Create and show a dialog with the 6 selectable items
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EquipmentTester.this);
                    dialogBuilder.setTitle("Select Element");

                    // Create an array of element options
                    String[] elementOptions = {
                            getString(R.string.element_fire),
                            getString(R.string.element_water),
                            getString(R.string.element_darkness),
                            getString(R.string.element_light),
                            getString(R.string.element_earth),
                            getString(R.string.element_air)
                    };

                    dialogBuilder.setItems(elementOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle the selected element option
                            String selectedElement = elementOptions[which];

                            // Update UI or perform any necessary actions with the selected element
                            int elementDrawableResource = 0;
                            Elements newElement = Elements.NEUTRAL;

                            switch (selectedElement) {
                                case "Fire":
                                    elementDrawableResource = R.drawable.essence_fire;
                                    newElement = Elements.FIRE;
                                    break;
                                case "Water":
                                    elementDrawableResource = R.drawable.essence_water;
                                    newElement = Elements.WATER;
                                    break;
                                case "Darkness":
                                    elementDrawableResource = R.drawable.essence_darkness;
                                    newElement = Elements.DARKNESS;
                                    break;
                                case "Light":
                                    elementDrawableResource = R.drawable.essence_light;
                                    newElement = Elements.LIGHT;
                                    break;
                                case "Earth":
                                    elementDrawableResource = R.drawable.essence_earth;
                                    newElement = Elements.EARTH;
                                    break;
                                case "Air":
                                    elementDrawableResource = R.drawable.essence_air;
                                    newElement = Elements.AIR;
                                    break;
                            }

                            // Update the armor element button's image and the selected armor's element
                            weaponElementButton.setImageResource(elementDrawableResource);
                            selectedWeapon.setElement(newElement);
                            calculateStats();

                        }
                    });

                    // Show the dialog
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
            }
        });

        ringElementButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!initialRingElement.equals(Elements.NEUTRAL)) {
                    Toast.makeText(EquipmentTester.this, R.string.element_cannot_be_picked, Toast.LENGTH_SHORT).show();
                } else {
                    // Create and show a dialog with the 6 selectable items
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EquipmentTester.this);
                    dialogBuilder.setTitle("Select Element");

                    // Create an array of element options
                    String[] elementOptions = {
                            getString(R.string.element_fire),
                            getString(R.string.element_water),
                            getString(R.string.element_darkness),
                            getString(R.string.element_light),
                            getString(R.string.element_earth),
                            getString(R.string.element_air)
                    };

                    dialogBuilder.setItems(elementOptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle the selected element option
                            String selectedElement = elementOptions[which];

                            // Update UI or perform any necessary actions with the selected element
                            int elementDrawableResource = 0;
                            Elements newElement = Elements.NEUTRAL;

                            switch (selectedElement) {
                                case "Fire":
                                    elementDrawableResource = R.drawable.essence_fire;
                                    newElement = Elements.FIRE;
                                    break;
                                case "Water":
                                    elementDrawableResource = R.drawable.essence_water;
                                    newElement = Elements.WATER;
                                    break;
                                case "Darkness":
                                    elementDrawableResource = R.drawable.essence_darkness;
                                    newElement = Elements.DARKNESS;
                                    break;
                                case "Light":
                                    elementDrawableResource = R.drawable.essence_light;
                                    newElement = Elements.LIGHT;
                                    break;
                                case "Earth":
                                    elementDrawableResource = R.drawable.essence_earth;
                                    newElement = Elements.EARTH;
                                    break;
                                case "Air":
                                    elementDrawableResource = R.drawable.essence_air;
                                    newElement = Elements.AIR;
                                    break;
                            }

                            // Update the armor element button's image and the selected armor's element
                            ringElementButton.setImageResource(elementDrawableResource);
                            selectedRing.setElement(newElement);
                            calculateStats();

                        }
                    });

                    // Show the dialog
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
            }
        });
    }

    public void openItemSelection(int buttonId) {
        Intent intent = new Intent(this, ItemSelection.class);
        intent.putExtra("buttonId", buttonId);
        startActivityForResult(intent, buttonId); // Use buttonId as the requestCode
    }

    public void openSkillPicker() {
        Intent intent = new Intent(this, SkillPicker.class);
        startActivityForResult(intent, 5);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedArmor = data.getParcelableExtra("selectedArmor");
            if (selectedArmor != null) {
                armorButton.setImageResource(selectedArmor.getImageResId());
            }
            currentArmorUpgrades = selectedArmor.getUpgrades();
            upgradeArmorButtonsLayout.setVisibility(View.VISIBLE);
            lockButton1.setVisibility(View.GONE);
            armorElementButton.setVisibility(View.VISIBLE);
            armorUpgradesView.setVisibility(View.VISIBLE);
            armorUpgradesView.setText(getString(R.string.upgrades) + " " + currentArmorUpgrades + " " + getString(R.string.max));
            if (selectedArmor.getElement().equals(Elements.NEUTRAL)) {
                armorElementButton.setImageResource(R.drawable.edit_icon);
            } else {
                armorElementButton.setImageResource(R.drawable.icon_lock);
            }
            initialArmorElement = selectedArmor.getElement();
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedRing = data.getParcelableExtra("selectedRing");
            if (selectedRing != null) {
                ringButton.setImageResource(selectedRing.getImageResId());
            }
            initialRingElement = selectedRing.getElement();
            ringElementButton.setVisibility(View.VISIBLE);
            if (selectedRing.getElement().equals(Elements.NEUTRAL)) {
                ringElementButton.setImageResource(R.drawable.edit_icon);
            } else {
                ringElementButton.setImageResource(R.drawable.icon_lock);
            }
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            selectedWeapon = data.getParcelableExtra("selectedWeapon");
            if (selectedWeapon != null) {
                weaponButton.setImageResource(selectedWeapon.getImageResId());
            }
            currentWeaponUpgrades = selectedWeapon.getUpgrades();
            upgradeWeaponButtonsLayout.setVisibility(View.VISIBLE);
            lockButton2.setVisibility(View.GONE);
            weaponElementButton.setVisibility(View.VISIBLE);
            weaponUpgradesView.setVisibility(View.VISIBLE);
            weaponUpgradesView.setText(getString(R.string.upgrades) + " " + currentWeaponUpgrades + " " + getString(R.string.max));
            if (selectedWeapon.getElement().equals(Elements.NEUTRAL)) {
                weaponElementButton.setImageResource(R.drawable.edit_icon);
            } else {
                weaponElementButton.setImageResource(R.drawable.icon_lock);
            }
            initialWeaponElement = selectedWeapon.getElement();
        } else if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            selectedClass = data.getParcelableExtra("selectedClass");
            if (selectedClass != null) {
                classButton.setImageResource(selectedClass.getImageResId());
            }
        } else if (requestCode == 5 && resultCode == RESULT_OK) {
            skillsPicked = retrieveSkillsPickedFromSharedPreferences();
            countSelectedSkills();
            updateSkillButtonImage();
        }

        calculateStats();
    }


    @SuppressLint("SetTextI18n")
        private void calculateStats()
        {
            if (selectedClass != null && selectedArmor != null && selectedWeapon != null && selectedRing != null && skillsPicked != null) {

                int currentAttackSpeed = selectedWeapon.getAttackCooldown();
                int currentPierceCount = selectedWeapon.getPierceCount();
                Log.d("EquipmentTester", "Calculating the stats!");
                double currentDamage = 0;
                if (currentWeaponUpgrades != 0)
                    currentDamage = selectedWeapon != null ? (selectedWeapon.getMinDamage() + ((selectedWeapon.getMaxDamage() - selectedWeapon.getMinDamage()) / ((double) selectedWeapon.getUpgrades())) * currentWeaponUpgrades) : 0;
                else
                    currentDamage = selectedWeapon != null ? selectedWeapon.getMinDamage() : 0;
                //Magic Boni Apply only on non-neutral items
                // Armor magic bonus
                if (selectedArmor != null && selectedWeapon != null && !selectedWeapon.getElement().equals(Elements.NEUTRAL)) {
                    currentDamage = (currentDamage * (1 + selectedArmor.getMagic() / 100.0));
                    Log.d("Equipmenttester", "Armor Magic Bonus: " + selectedArmor.getMagic());
                    Log.d("EquipmentTester", "Armor Bonus: Magic Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }

                // Armor weapon bonus
                if (selectedArmor != null && selectedWeapon != null) {
                    if (selectedWeapon.getType().equals(WeaponTypes.SWORD)) {
                        currentDamage = (currentDamage * (1 + selectedArmor.getSword() / 100.0));
                        Log.d("Equipmenttester", "Armor Sword Bonus:" + selectedArmor.getSword());
                        Log.d("EquipmentTester", "Armor Bonus: Sword Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.DAGGER)) {
                        currentDamage = (currentDamage * (1 + selectedArmor.getDagger() / 100.0));
                        Log.d("EquipmentTester", "Armor Bonus: Dagger Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.STAFF)) {
                        currentDamage = (currentDamage * (1 + selectedArmor.getStaff() / 100.0));
                        Log.d("EquipmentTester", "Armor Bonus: Staff Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.AXE)) {
                        currentDamage = (currentDamage * (1 + selectedArmor.getAxe() / 100.0));
                        Log.d("EquipmentTester", "Armor Bonus: Axe Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.HAMMER)) {
                        currentDamage = (currentDamage * (1 + selectedArmor.getHammer() / 100.0));
                        Log.d("EquipmentTester", "Armor Bonus: Hammer Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.SPEAR)) {
                        currentDamage = (currentDamage * (1 + selectedArmor.getSpear() / 100.0));
                        Log.d("EquipmentTester", "Armor Bonus: Spear Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                }

                // Ring magic bonus
                if (selectedRing != null && selectedWeapon != null && !selectedWeapon.getElement().equals(Elements.NEUTRAL)) {
                    currentDamage = (currentDamage * (1 + selectedRing.getMagic() / 100.0));
                    Log.d("EquipmentTester", "Ring Bonus: Magic Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }

                // Ring weapon bonus
                if (selectedRing != null && selectedWeapon != null) {
                    if (selectedWeapon.getType().equals(WeaponTypes.SWORD)) {
                        currentDamage = (currentDamage * (1 + selectedRing.getSword() / 100.0));
                        Log.d("EquipmentTester", "Ring Bonus: Sword Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.DAGGER)) {
                        currentDamage = (currentDamage * (1 + selectedRing.getDagger() / 100.0));
                        Log.d("EquipmentTester", "Ring Bonus: Dagger Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.STAFF)) {
                        currentDamage = (currentDamage * (1 + selectedRing.getStaff() / 100.0));
                        Log.d("EquipmentTester", "Ring Bonus: Staff Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.AXE)) {
                        currentDamage = (currentDamage * (1 + selectedRing.getAxe() / 100.0));
                        Log.d("EquipmentTester", "Ring Bonus: Axe Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.HAMMER)) {
                        currentDamage = (currentDamage * (1 + selectedRing.getHammer() / 100.0));
                        Log.d("EquipmentTester", "Ring Bonus: Hammer Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.SPEAR)) {
                        currentDamage = (currentDamage * (1 + selectedRing.getSpear() / 100.0));
                        Log.d("EquipmentTester", "Ring Bonus: Spear Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                }

                // Class Boni
                if (selectedClass != null && selectedWeapon != null) {
                    currentDamage = (currentDamage * (1 + selectedClass.getMagicBonus() / 100.0));
                    Log.d("EquipmentTester", "Class Bonus: Magic Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    if (selectedWeapon.getType().equals(WeaponTypes.SWORD)) {
                        currentDamage = (currentDamage * (1 + selectedClass.getSwordBonus() / 100.0));
                        Log.d("EquipmentTester", "Class Bonus: Sword Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.DAGGER)) {
                        currentDamage = (currentDamage * (1 + selectedClass.getDaggerBonus() / 100.0));
                        Log.d("EquipmentTester", "Class Bonus: Dagger Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.STAFF)) {
                        currentDamage = (currentDamage * (1 + selectedClass.getStaffBonus() / 100.0));
                        Log.d("EquipmentTester", "Class Bonus: Staff Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.AXE)) {
                        currentDamage = (currentDamage * (1 + selectedClass.getAxeBonus() / 100.0));
                        Log.d("EquipmentTester", "Class Bonus: Axe Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.HAMMER)) {
                        currentDamage = (currentDamage * (1 + selectedClass.getHammerBonus() / 100.0));
                        Log.d("EquipmentTester", "Class Bonus: Hammer Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    } else if (selectedWeapon.getType().equals(WeaponTypes.SPEAR)) {
                        currentDamage = (currentDamage * (1 + selectedClass.getSpearBonus() / 100.0));
                        Log.d("EquipmentTester", "Class Bonus: Spear Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                }

                // Skill Tree Boni
                if (skillsPicked != null) {
                    if (skillsPicked[1] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.SWORD)) {
                        currentDamage = (currentDamage * 1.15);
                        Log.d("EquipmentTester", "Skill Tree: Sword Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                    if (skillsPicked[2] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.DAGGER)) {
                        currentDamage = (currentDamage * 1.2);
                        Log.d("EquipmentTester", "Skill Tree: Dagger Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                    if (skillsPicked[13] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.STAFF)) {
                        currentDamage = (currentDamage * 1.20);
                        Log.d("EquipmentTester", "Skill Tree: Staff Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                    if (skillsPicked[14] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.SPEAR)) {
                        currentDamage = (currentDamage * 1.35);
                        Log.d("EquipmentTester", "Skill Tree: Spear Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                    if (skillsPicked[25] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.HAMMER)) {
                        currentDamage = (currentDamage * 1.60);
                        Log.d("EquipmentTester", "Skill Tree: Hammer Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                    if (skillsPicked[26] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.AXE)) {
                        currentDamage = (currentDamage * 1.50);
                        Log.d("EquipmentTester", "Skill Tree: Axe Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                    }
                }


                // Armor + Weapon: Same element bonus
                if (selectedArmor != null && selectedWeapon != null && selectedWeapon.getElement().equals(selectedArmor.getElement())) {
                    currentDamage = (currentDamage * 1.25);
                    Log.d("EquipmentTester", "Armor and Weapon Same Element Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }

                // Ring + Weapon: Same element Bonus
                if (selectedWeapon != null && selectedRing != null && selectedWeapon.getElement().equals(selectedRing.getElement())) {
                    currentDamage = (currentDamage * 1.2);
                    Log.d("EquipmentTester", "Ring and Weapon Same Element Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }

                currentDamage = Math.ceil(currentDamage);

                Log.d("EquipmentTester", "Current Damage: " + currentDamage);
                Log.d("EquipmentTester", "Upgrades: " + currentWeaponUpgrades);
                TextView damageValueTextView = findViewById(R.id.currentDamage);
                damageValueTextView.setText(getString(R.string.damage_in_calculation) + (int) currentDamage);

                if (selectedArmor != null && selectedArmor.getUpgrades() == 0)
                    selectedArmor.setUpgrades(1);

                double currentArmor = (selectedArmor != null
                        ? (selectedArmor.getMinArmor() + ((selectedArmor.getMaxArmor() - selectedArmor.getMinArmor())
                        / ((double) selectedArmor.getUpgrades())) * currentArmorUpgrades) : 0);

                Log.d("EquipmentTester", "Starting armor for current upgrades: " + currentArmor);

                currentArmor += (selectedRing != null ? selectedRing.getArmor() : 0);

                Log.d("EquipmentTester", "Adding Ring Armor:  " + currentArmor);

                currentArmor *= (selectedRing != null ? 1 + (selectedRing.getArmorBonus() / 100.0) : 1);

                Log.d("EquipmentTester", "Adding Ring Armor Bonus: " + currentArmor);

                currentArmor *= (selectedWeapon != null ? 1 + (selectedWeapon.getArmorBonus() / 100.0) : 1);

                Log.d("EquipmentTester", "Adding Weapon Armor Bonus: " + currentArmor);

                currentArmor *= (selectedClass != null ? 1 + (selectedClass.getArmorBonus() / 100.0) : 1);

                Log.d("EquipmentTester", "Adding Class Armor Bonus: " + currentArmor);

                currentArmor *= (skillsPicked != null && skillsPicked[18] && selectedArmor != null ? 1.25 : 1);

                Log.d("EquipmentTester", "Adding Skill Tree Armor Bonus: " + currentArmor);

                currentArmor = Math.round(currentArmor);

                if (selectedArmor != null && selectedArmor.getUpgrades() == 0)
                    currentArmor = selectedArmor.getMinArmor();

                Log.d("EquipmentTester", "Current Armor: " + currentArmor);
                TextView armorValueTextView = findViewById(R.id.currentArmor);
                armorValueTextView.setText(getString(R.string.armor_in_calculation) + (int) currentArmor);

                int starCount;
                if (currentAttackSpeed <= 300) {
                    starCount = 5;
                } else if (currentAttackSpeed <= 450) {
                    starCount = 4;
                } else if (currentAttackSpeed <= 650) {
                    starCount = 3;
                } else if (currentAttackSpeed <= 750) {
                    starCount = 2;
                } else {
                    starCount = 1;
                }

                TextView attackSpeedTextView = findViewById(R.id.attackSpeed);
                String helpText = getString(R.string.attack_speed) + " ";
                SpannableStringBuilder builder = new SpannableStringBuilder(helpText);

                Drawable starDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.star, null);
                int desiredWidth = 80;
                int desiredHeight = 80;
                starDrawable.setBounds(0, 0, desiredWidth, desiredHeight);

                for (int i = 0; i < starCount; i++) {
                    int start = builder.length();
                    builder.append(" ");
                    int end = builder.length();
                    builder.setSpan(new ImageSpan(starDrawable, ImageSpan.ALIGN_BOTTOM), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                attackSpeedTextView.setText(builder);


                TextView pierceCountTextView = findViewById(R.id.pierceCount);
                pierceCountTextView.setText(getString(R.string.pierce_count) + " " + currentPierceCount);

                double helpArmorSpeed = 0;
                double helpArmorJump = 0;
                if (selectedArmor != null) {
                    helpArmorSpeed = selectedArmor.getSpeed() / 100.0 + 1;
                    helpArmorJump = selectedArmor.getJump() / 100.0 + 1;
                }
                double helpWeaponSpeed = 0;
                double helpWeaponJump = 0;
                if (selectedWeapon != null) {
                    helpWeaponSpeed = selectedWeapon.getSpeed() / 100.0 + 1;
                    helpWeaponJump = selectedWeapon.getJump() / 100.0 + 1;
                }
                double helpRingSpeed = 0;
                double helpRingJump = 0;
                if (selectedRing != null) {
                    helpRingSpeed = selectedRing.getSpeed() / 100.0 + 1;
                    helpRingJump = selectedRing.getJump() / 100.0 + 1;
                }

                int currentSpeed = (int) ((helpArmorSpeed * helpWeaponSpeed * helpRingSpeed) * 100 - 100);

                double helpClassJump = 0;
                if (selectedClass != null) {
                    currentSpeed += selectedClass.getSpeedBonus();
                    helpClassJump = selectedClass.getJumpImpulseBonus() / 100.0 + 1;
                }


                if (skillsPicked != null && skillsPicked[0]) {
                    currentSpeed += 4;
                }
                TextView speedValueTextView = findViewById(R.id.currentSpeed);
                speedValueTextView.setText(getString(R.string.speed_in_calculation) + currentSpeed + "%");

                if (selectedArmor != null && selectedRing != null && selectedWeapon != null) {
                    Log.d("EquipmentTester", "Armor Jump: " + selectedArmor.getJump());
                    Log.d("EquipmentTester", "Ring Jump: " + selectedRing.getJump());
                    Log.d("EquipmentTester", "Weapon Jump: " + selectedWeapon.getJump());
                }

                double currentJumpImpulse = (selectedArmor != null ? helpArmorJump : 0) * (selectedRing != null ? helpRingJump : 0)
                        * (selectedWeapon != null ? helpWeaponJump : 0) * (selectedClass != null ? helpClassJump : 0);
                if (skillsPicked != null && skillsPicked[12]) {
                    currentJumpImpulse += 0.03;
                }
                currentJumpImpulse = Math.floor(currentJumpImpulse * 100.0) / 100.0;
                currentJumpImpulse *= 100;
                currentJumpImpulse -= 100;
                TextView jumpImpulseValueTextView = findViewById(R.id.currentJumpImpulse);
                jumpImpulseValueTextView.setText(getString(R.string.jump_impulse_in_calculation) + (int) currentJumpImpulse + "%");

                if ((selectedRing != null && Elements.FIRE.equals(selectedRing.getElement())) || currentArmor >= 330 || (selectedArmor != null && Elements.FIRE.equals(selectedArmor.getElement())) || (skillsPicked != null && skillsPicked[16])) {
                    TextView fireValueTextView = findViewById(R.id.fireResistance);
                    helpText = getString(R.string.fire_resistance) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    fireValueTextView.setText(spannable);
                } else {
                    TextView fireValueTextView = findViewById(R.id.fireResistance);
                    helpText = getString(R.string.fire_resistance) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    fireValueTextView.setText(spannable);
                }

                if (selectedArmor != null && selectedArmor.isFrostImmune()) {
                    TextView frostValueTextView = findViewById(R.id.frostResistance);
                    helpText = getString(R.string.frost_resistance) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    frostValueTextView.setText(spannable);
                } else {
                    TextView frostValueTextView = findViewById(R.id.frostResistance);
                    helpText = getString(R.string.frost_resistance) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    frostValueTextView.setText(spannable);
                }

                if ((selectedRing != null && (Elements.AIR.equals(selectedRing.getElement()) || Elements.WATER.equals(selectedRing.getElement()))) ||
                        (selectedArmor != null && (Elements.AIR.equals(selectedArmor.getElement()) || Elements.WATER.equals(selectedArmor.getElement()))) || (skillsPicked != null && skillsPicked[29])) {
                    TextView spikeValueTextView = findViewById(R.id.spikeResistance);
                    helpText = getString(R.string.spike_resistance) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spikeValueTextView.setText(spannable);
                } else {
                    TextView spikeValueTextView = findViewById(R.id.spikeResistance);
                    helpText = getString(R.string.spike_resistance) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spikeValueTextView.setText(spannable);
                }

                if (selectedWeapon != null && selectedWeapon.isPersistAgainstProjectile()) {
                    TextView projectilePersistenceValueTextView = findViewById(R.id.projectilePersistence);
                    helpText = getString(R.string.projectile_persistence) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    projectilePersistenceValueTextView.setText(spannable);
                } else {
                    TextView projectilePersistenceValueTextView = findViewById(R.id.projectilePersistence);
                    helpText = getString(R.string.projectile_persistence) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    projectilePersistenceValueTextView.setText(spannable);
                }

                if (selectedWeapon != null && selectedWeapon.isPoisonous()) {
                    TextView poisonousAttackValueTextView = findViewById(R.id.poisonousAttack);
                    helpText = getString(R.string.poisonous_attack) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    poisonousAttackValueTextView.setText(spannable);
                } else {
                    TextView poisonousAttackValueTextView = findViewById(R.id.poisonousAttack);
                    helpText = getString(R.string.poisonous_attack) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    poisonousAttackValueTextView.setText(spannable);
                }

                if (selectedWeapon != null && selectedWeapon.isFrost()) {
                    TextView frostAttackValueTextView = findViewById(R.id.frostAttack);
                    helpText = getString(R.string.frost_attack) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    frostAttackValueTextView.setText(spannable);
                } else {
                    TextView frostAttackValueTextView = findViewById(R.id.frostAttack);
                    helpText = getString(R.string.frost_attack) + "  ";
                    SpannableString spannable = new SpannableString(helpText);
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                    customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                    ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    frostAttackValueTextView.setText(spannable);
                }

                double currentRating = ((currentDamage / 5000.0)
                        + (currentArmor / 150.0)
                        + (currentSpeed / 25.0)
                        + (currentJumpImpulse / 25.0)
                        + (skillsPicked != null ? ((skillsPicked[3] ? 0.1 : 0) + (skillsPicked[4] ? 0.1 : 0) + (skillsPicked[5] ? 0.1 : 0) + (skillsPicked[6] ? 0.1 : 0)
                        + (skillsPicked[7] ? 0.1 : 0) + (skillsPicked[8] ? 0.1 : 0) + (skillsPicked[9] ? 0.1 : 0) + (skillsPicked[10] ? 0.1 : 0)
                        + (skillsPicked[11] ? 0.1 : 0) + (skillsPicked[15] ? 0.1 : 0) + (skillsPicked[17] ? 0.1 : 0) + (skillsPicked[18] ? 0.1 : 0)
                        + (skillsPicked[19] ? 0.1 : 0) + (skillsPicked[20] ? 0.1 : 0) + (skillsPicked[21] ? 0.1 : 0) + (skillsPicked[22] ? 0.1 : 0)
                        + (skillsPicked[23] ? 0.1 : 0) + (skillsPicked[27] ? 0.1 : 0) + (skillsPicked[28] ? 0.1 : 0) + (skillsPicked[30] ? 0.1 : 0)
                        + (skillsPicked[31] ? 0.1 : 0) + (skillsPicked[32] ? 0.1 : 0) + (skillsPicked[33] ? 0.1 : 0) + (skillsPicked[34] ? 0.1 : 0) + (skillsPicked[35] ? 0.1 : 0)
                        + (((selectedRing != null && Elements.FIRE.equals(selectedRing.getElement())) || (selectedArmor != null && Elements.FIRE.equals(selectedArmor.getElement())) || (skillsPicked != null && skillsPicked[13])) ? 0.2 : 0)
                        + (((selectedRing != null && (Elements.AIR.equals(selectedRing.getElement()) || Elements.WATER.equals(selectedRing.getElement()))) ||
                        (selectedArmor != null && (Elements.AIR.equals(selectedArmor.getElement()) || Elements.WATER.equals(selectedArmor.getElement()))) || (skillsPicked != null && skillsPicked[29])) ? 0.2 : 0)) : 0)
                        + ((double) currentPierceCount / 10)
                        + ((double) (currentAttackSpeed - 1400) / 700)
                        + (selectedWeapon.isFrost() ? 0.15 : 0)
                        + (selectedWeapon.isPoisonous() ? 0.15 : 0)
                );

                if(currentRating > 10)
                    currentRating = 10;

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String formattedRating = decimalFormat.format(currentRating);

                TextView rating = findViewById(R.id.rating);

                if (currentRating < 1)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDCA9");
                else if (currentRating >= 1 && currentRating < 2)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83E\uDD21");
                else if (currentRating >= 2 && currentRating < 3)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDE35");
                else if (currentRating >= 3 && currentRating < 4)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDE35\u200D\uD83D\uDCAB");
                else if (currentRating >= 4 && currentRating < 5)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDE36\u200D\uD83C\uDF2B️");
                else if (currentRating >= 5 && currentRating < 6)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDE42");
                else if (currentRating >= 6 && currentRating < 7)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDE00");
                else if (currentRating >= 7 && currentRating < 8)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDE0D");
                else if (currentRating >= 8 && currentRating < 9)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDE0E");
                else if (currentRating >= 9 && currentRating <= 10)
                    rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDDFF");

                Log.d("EquipmentTester", "Current Rating: " + currentRating);
            }
        }

        private boolean[] retrieveSkillsPickedFromSharedPreferences() {
            SharedPreferences preferences = getSharedPreferences("SkillState", MODE_PRIVATE);
            boolean[] skillsPicked = new boolean[36];

            for (int i = 0; i < skillsPicked.length; i++) {
                skillsPicked[i] = preferences.getBoolean("skillsPicked_" + i, false);
            }

            return skillsPicked;
        }

    private int countSelectedSkills() {
        if(skillsPicked != null){
            int count = 0;
            for (boolean skillPicked : skillsPicked) {
                if (skillPicked) {
                    count++;
                }
            }
            Log.d("EquipmentTester", "Count: " + count);
            return count;
        }
        return 0;
    }

    private void updateSkillButtonImage() {
        Log.d("EquipmentTester", "Current Selected Skills:" + countSelectedSkills());
        if (countSelectedSkills() == 22) {
            skillButton.setImageResource(R.drawable.select_skill_tree_button);
            Log.d("EquipmentTester", "Changing to color picture");
        } else {
            skillButton.setImageResource(R.drawable.select_skill_tree_button_grey);
            Log.d("EquipmentTester", "Changing to grey picture");
        }
    }

    private void playSound() {
        // Check if MediaPlayer is null or not
        if (mediaPlayer != null) {
            // Reset MediaPlayer if it's already playing
            mediaPlayer.seekTo(0);

            // Set volume to 50%
            float volume = 0.25f; // 50%
            mediaPlayer.setVolume(volume, volume);

            mediaPlayer.start();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

