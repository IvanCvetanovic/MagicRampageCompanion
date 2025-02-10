package xyz.magicrampagecompanion;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
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

    private double currentDamage = 0;
    private double currentArmor = 0;
    private int currentSpeed = 0;
    private int currentJumpImpulse = 0;

    private boolean[] skillsPicked;
    private double currentRating = 0;

    private ImageView lockButton1;
    private ImageView lockButton2;
    private LinearLayout upgradeArmorButtonsLayout;
    private LinearLayout upgradeWeaponButtonsLayout;
    private TextView armorUpgradesView;
    private TextView weaponUpgradesView;
    private ImageButton arrowPlusMaxButton1;
    private ImageButton arrowMinusMaxButton1;
    private ImageButton arrowPlusButton1;
    private ImageButton arrowMinusButton1;
    private ImageButton arrowPlusMaxButton2;
    private ImageButton arrowMinusMaxButton2;
    private ImageButton arrowPlusButton2;
    private ImageButton arrowMinusButton2;
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
        int desiredWidth = 100;
        int desiredHeight = 100;
        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
        ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fireValueTextView.setText(spannable);

        lockButton1 = findViewById(R.id.lockButton1);
        upgradeArmorButtonsLayout = findViewById(R.id.upgradeArmorButtonsLayout);
        upgradeArmorButtonsLayout.setVisibility(View.GONE);
        armorUpgradesView = findViewById(R.id.armorUpgradesView);
        armorUpgradesView.setVisibility(View.GONE);
        arrowPlusMaxButton1 = findViewById(R.id.ButtonPlusMax1);
        arrowMinusMaxButton1 = findViewById(R.id.ButtonMinusMax1);
        arrowPlusButton1 = findViewById(R.id.ButtonPlus1);
        arrowMinusButton1 = findViewById(R.id.ButtonMinus1);
        armorElementButton = findViewById(R.id.ButtonElement1);
        armorElementButton.setVisibility(View.GONE);

        TextView spikeValueTextView = findViewById(R.id.spikeResistance);
        String helpText1 = getString(R.string.spike_resistance) + "  ";
        spannable = new SpannableString(helpText);
        customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
        desiredWidth = 100;
        desiredHeight = 100;
        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
        imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spikeValueTextView.setText(spannable);

        lockButton2 = findViewById(R.id.lockButton2);
        upgradeWeaponButtonsLayout = findViewById(R.id.upgradeWeaponButtonsLayout);
        upgradeWeaponButtonsLayout.setVisibility(View.GONE);
        weaponUpgradesView = findViewById(R.id.weaponUpgradesView);
        weaponUpgradesView.setVisibility(View.GONE);
        arrowPlusMaxButton2 = findViewById(R.id.ButtonPlusMax2);
        arrowMinusMaxButton2 = findViewById(R.id.ButtonMinusMax2);
        arrowPlusButton2 = findViewById(R.id.ButtonPlus2);
        arrowMinusButton2 = findViewById(R.id.ButtonMinus2);
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

                Bitmap armorBitmap = selectedArmor.getPicture();
                if (armorBitmap != null) {
                    armorButton.setImageBitmap(armorBitmap);
                }
            }
            currentArmorUpgrades = selectedArmor.getUpgrades();
            upgradeArmorButtonsLayout.setVisibility(View.VISIBLE);
            lockButton1.setVisibility(View.GONE);
            armorElementButton.setVisibility(View.VISIBLE);
            armorUpgradesView.setVisibility(View.VISIBLE);
            armorUpgradesView.setText(getString(R.string.upgrades) + " " + currentArmorUpgrades + " " + getString(R.string.max));
            if(selectedArmor.getElement().equals(Elements.NEUTRAL)) {
                armorElementButton.setImageResource(R.drawable.edit_icon);
            } else {
                armorElementButton.setImageResource(R.drawable.icon_lock);
            }
            initialArmorElement = selectedArmor.getElement();
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedRing = data.getParcelableExtra("selectedRing");
            if (selectedRing != null) {
                Bitmap ringBitmap = selectedRing.getPicture();
                if (ringBitmap != null) {
                    ringButton.setImageBitmap(ringBitmap);
                }
            }
            initialRingElement = selectedRing.getElement();
            ringElementButton.setVisibility(View.VISIBLE);
            if(selectedRing.getElement().equals(Elements.NEUTRAL)) {
                ringElementButton.setImageResource(R.drawable.edit_icon);
            } else {
                ringElementButton.setImageResource(R.drawable.icon_lock);
            }
        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            selectedWeapon = data.getParcelableExtra("selectedWeapon");
            if (selectedWeapon != null) {
                Bitmap weaponBitmap = selectedWeapon.getPicture();
                if (weaponBitmap != null) {
                    weaponButton.setImageBitmap(weaponBitmap);
                }
            }
            currentWeaponUpgrades = selectedWeapon.getUpgrades();
            upgradeWeaponButtonsLayout.setVisibility(View.VISIBLE);
            lockButton2.setVisibility(View.GONE);
            weaponElementButton.setVisibility(View.VISIBLE);
            weaponUpgradesView.setVisibility(View.VISIBLE);
            weaponUpgradesView.setText(getString(R.string.upgrades) + " " + currentWeaponUpgrades + " " + getString(R.string.max));
            if(selectedWeapon.getElement().equals(Elements.NEUTRAL)) {
                weaponElementButton.setImageResource(R.drawable.edit_icon);
            } else {
                weaponElementButton.setImageResource(R.drawable.icon_lock);
            }
            initialWeaponElement = selectedWeapon.getElement();
        } else if (requestCode == 4 && resultCode == RESULT_OK && data != null) {
            selectedClass = data.getParcelableExtra("selectedClass");
            if (selectedClass != null) {
                Bitmap classBitmap = selectedClass.getPicture();
                if (classBitmap != null) {
                    classButton.setImageBitmap(classBitmap);
                }
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
            Log.d("EquipmentTester", "Calculating the stats!");
            currentDamage = selectedWeapon != null ? (selectedWeapon.getMinDamage() + ((selectedWeapon.getMaxDamage() - selectedWeapon.getMinDamage()) / ((double) selectedWeapon.getUpgrades())) * currentWeaponUpgrades) : 0;

            //Magic Boni Apply only on non-neutral items
            // Armor magic bonus
            if (selectedArmor != null && selectedWeapon!=null && !selectedWeapon.getElement().equals(Elements.NEUTRAL)) {
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
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.DAGGER)) {
                    currentDamage = (currentDamage * (1 + selectedArmor.getDagger() / 100.0));
                    Log.d("EquipmentTester", "Armor Bonus: Dagger Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.STAFF)) {
                    currentDamage = (currentDamage * (1 + selectedArmor.getStaff() / 100.0));
                    Log.d("EquipmentTester", "Armor Bonus: Staff Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.AXE)) {
                    currentDamage = (currentDamage * (1 + selectedArmor.getAxe() / 100.0));
                    Log.d("EquipmentTester", "Armor Bonus: Axe Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.HAMMER)) {
                    currentDamage = (currentDamage * (1 + selectedArmor.getHammer() / 100.0));
                    Log.d("EquipmentTester", "Armor Bonus: Hammer Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.SPEAR)) {
                    currentDamage = (currentDamage * (1 + selectedArmor.getSpear() / 100.0));
                    Log.d("EquipmentTester", "Armor Bonus: Spear Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
            }

            // Ring magic bonus
            if (selectedRing != null  && selectedWeapon!=null && !selectedWeapon.getElement().equals(Elements.NEUTRAL)) {
                currentDamage = (currentDamage * (1 + selectedRing.getMagic() / 100.0));
                Log.d("EquipmentTester", "Ring Bonus: Magic Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
            }

            // Ring weapon bonus
            if (selectedRing != null && selectedWeapon != null) {
                if (selectedWeapon.getType().equals(WeaponTypes.SWORD)) {
                    currentDamage = (currentDamage * (1 + selectedRing.getSword() / 100.0));
                    Log.d("EquipmentTester", "Ring Bonus: Sword Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.DAGGER)) {
                    currentDamage = (currentDamage * (1 + selectedRing.getDagger() / 100.0));
                    Log.d("EquipmentTester", "Ring Bonus: Dagger Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.STAFF)) {
                    currentDamage = (currentDamage * (1 + selectedRing.getStaff() / 100.0));
                    Log.d("EquipmentTester", "Ring Bonus: Staff Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.AXE)) {
                    currentDamage = (currentDamage * (1 + selectedRing.getAxe() / 100.0));
                    Log.d("EquipmentTester", "Ring Bonus: Axe Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.HAMMER)) {
                    currentDamage = (currentDamage * (1 + selectedRing.getHammer() / 100.0));
                    Log.d("EquipmentTester", "Ring Bonus: Hammer Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.SPEAR)) {
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
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.DAGGER)) {
                    currentDamage = (currentDamage * (1 + selectedClass.getDaggerBonus() / 100.0));
                    Log.d("EquipmentTester", "Class Bonus: Dagger Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.STAFF)) {
                    currentDamage = (currentDamage * (1 + selectedClass.getStaffBonus() / 100.0));
                    Log.d("EquipmentTester", "Class Bonus: Staff Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.AXE)) {
                    currentDamage = (currentDamage * (1 + selectedClass.getAxeBonus() / 100.0));
                    Log.d("EquipmentTester", "Class Bonus: Axe Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.HAMMER)) {
                    currentDamage = (currentDamage * (1 + selectedClass.getHammerBonus() / 100.0));
                    Log.d("EquipmentTester", "Class Bonus: Hammer Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                else if (selectedWeapon.getType().equals(WeaponTypes.SPEAR)) {
                    currentDamage = (currentDamage * (1 + selectedClass.getSpearBonus() / 100.0));
                    Log.d("EquipmentTester", "Class Bonus: Spear Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
            }

            // Skill Tree Boni
            if (skillsPicked != null) {
                if (skillsPicked[1] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.SWORD)) {
                    currentDamage = (currentDamage * 1.2);
                    Log.d("EquipmentTester", "Skill Tree: Sword Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                if (skillsPicked[2] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.DAGGER)) {
                    currentDamage = (currentDamage * 1.2);
                    Log.d("EquipmentTester", "Skill Tree: Dagger Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                if (skillsPicked[10] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.STAFF)) {
                    currentDamage = (currentDamage * 1.25);
                    Log.d("EquipmentTester", "Skill Tree: Staff Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                if (skillsPicked[11] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.SPEAR)) {
                    currentDamage = (currentDamage * 1.40);
                    Log.d("EquipmentTester", "Skill Tree: Spear Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                if (skillsPicked[19] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.HAMMER)) {
                    currentDamage = (currentDamage * 1.5);
                    Log.d("EquipmentTester", "Skill Tree: Hammer Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
                if (skillsPicked[20] && selectedWeapon != null && selectedWeapon.getType().equals(WeaponTypes.AXE)) {
                    currentDamage = (currentDamage * 1.45);
                    Log.d("EquipmentTester", "Skill Tree: Axe Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
                }
            }

            // Armor + Weapon: Same element bonus
            if (selectedArmor != null && selectedWeapon != null && selectedWeapon.getElement().equals(selectedArmor.getElement()))
            {
                currentDamage =  (currentDamage * 1.25);
                Log.d("EquipmentTester", "Armor and Weapon Same Element Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
            }

            // Ring + Weapon: Same element Bonus
            if (selectedWeapon != null && selectedRing != null && selectedWeapon.getElement().equals(selectedRing.getElement()))
            {
                currentDamage = (currentDamage * 1.2);
                Log.d("EquipmentTester", "Ring and Weapon Same Element Bonus Added: " + currentDamage + ", Current Damage: " + currentDamage);
            }

            currentDamage = Math.ceil(currentDamage);

            Log.d("EquipmentTester", "Current Damage: " + currentDamage);
            Log.d("EquipmentTester", "Upgrades: " + currentWeaponUpgrades);
            TextView damageValueTextView = findViewById(R.id.currentDamage);
            damageValueTextView.setText(getString(R.string.damage_in_calculation) + (int) currentDamage);

            currentArmor = (
                    (
                            (selectedArmor != null ? (selectedArmor.getMinArmor() + ((selectedArmor.getMaxArmor() - selectedArmor.getMinArmor()) / ((double) selectedArmor.getUpgrades())) * currentArmorUpgrades) : 0)
                                    + (selectedRing != null ? selectedRing.getArmor() : 0))
                            * (selectedRing != null ? 1 + (selectedRing.getArmorBonus() / 100.0) : 1)
                            * (selectedWeapon != null ? 1 + (selectedWeapon.getArmorBonus() / 100.0) : 1)
                            * (selectedClass != null ? 1 + (selectedClass.getArmorBonus() / 100.0) : 1)
                            * (skillsPicked != null && skillsPicked[18] && selectedArmor != null ? 1.25 : 1)
            );

            if(currentArmor >= 100)
                currentArmor+=1;

            if(selectedArmor != null && selectedArmor.getUpgrades() == 0)
                currentArmor=selectedArmor.getMinArmor();

            Log.d("EquipmentTester", "Current Armor: " + currentArmor);
            TextView armorValueTextView = findViewById(R.id.currentArmor);
            armorValueTextView.setText(getString(R.string.armor_in_calculation) + (int) currentArmor);

            double helpArmorSpeed = 0;
            if(selectedArmor!=null)
                helpArmorSpeed = selectedArmor.getSpeed() /100.0 + 1;

            double helpWeaponSpeed = 0;
            if(selectedWeapon!=null)
                helpWeaponSpeed = selectedWeapon.getSpeed() /100.0 + 1;

            double helpRingSpeed = 0;
            if(selectedRing!=null)
                helpRingSpeed = selectedRing.getSpeed() /100.0 + 1;

            currentSpeed = (int) ((helpArmorSpeed * helpWeaponSpeed * helpRingSpeed)*100-100);

            if(selectedClass!=null)
                currentSpeed+=selectedClass.getSpeedBonus();


            if (skillsPicked != null && skillsPicked[0]) {
                currentSpeed += 4;
            }
            TextView speedValueTextView = findViewById(R.id.currentSpeed);
            speedValueTextView.setText(getString(R.string.speed_in_calculation) + currentSpeed + "%");

            currentJumpImpulse = (selectedArmor != null ? selectedArmor.getJump() : 0) + (selectedRing != null ? selectedRing.getJump() : 0)
                    + (selectedClass != null ? selectedClass.getJumpImpulseBonus() : 0) + (selectedWeapon != null ? selectedWeapon.getJump() : 0);
            if (skillsPicked != null && skillsPicked[9]) {
                currentJumpImpulse += 3;
            }
            TextView jumpImpulseValueTextView = findViewById(R.id.currentJumpImpulse);
            jumpImpulseValueTextView.setText(getString(R.string.jump_impulse_in_calculation) + currentJumpImpulse + "%");

            if ((selectedRing != null && Elements.FIRE.equals(selectedRing.getElement())) || currentArmor >= 330 || (selectedArmor != null && Elements.FIRE.equals(selectedArmor.getElement())) || (skillsPicked != null && skillsPicked[13])) {
                TextView fireValueTextView = findViewById(R.id.fireResistance);
                String helpText = getString(R.string.fire_resistance) + "  ";
                SpannableString spannable = new SpannableString(helpText);
                Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                int desiredWidth = 100;
                int desiredHeight = 100;
                customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                fireValueTextView.setText(spannable);
            } else {
                TextView fireValueTextView = findViewById(R.id.fireResistance);
                String helpText = getString(R.string.fire_resistance) + "  ";
                SpannableString spannable = new SpannableString(helpText);
                Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                int desiredWidth = 100;
                int desiredHeight = 100;
                customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                fireValueTextView.setText(spannable);
            }

            if (selectedArmor != null && selectedArmor.isFrostImmune()) {
                TextView frostValueTextView = findViewById(R.id.frostResistance);
                String helpText = getString(R.string.frost_resistance) + "  ";
                SpannableString spannable = new SpannableString(helpText);
                Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                int desiredWidth = 100;
                int desiredHeight = 100;
                customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                frostValueTextView.setText(spannable);
            } else {
                TextView frostValueTextView = findViewById(R.id.frostResistance);
                String helpText = getString(R.string.frost_resistance) + "  ";
                SpannableString spannable = new SpannableString(helpText);
                Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                int desiredWidth = 100;
                int desiredHeight = 100;
                customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                frostValueTextView.setText(spannable);
            }

            if ((selectedRing != null && (Elements.AIR.equals(selectedRing.getElement()) || Elements.WATER.equals(selectedRing.getElement()))) ||
                    (selectedArmor != null && (Elements.AIR.equals(selectedArmor.getElement()) || Elements.WATER.equals(selectedArmor.getElement()))) || (skillsPicked != null && skillsPicked[23])) {
                TextView spikeValueTextView = findViewById(R.id.spikeResistance);
                String helpText = getString(R.string.spike_resistance) + "  ";
                SpannableString spannable = new SpannableString(helpText);
                Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_check, null);
                int desiredWidth = 100;
                int desiredHeight = 100;
                customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spikeValueTextView.setText(spannable);
            } else {
                TextView spikeValueTextView = findViewById(R.id.spikeResistance);
                String helpText = getString(R.string.spike_resistance) + "  ";
                SpannableString spannable = new SpannableString(helpText);
                Drawable customEmojiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_uncheck, null);
                int desiredWidth = 100;
                int desiredHeight = 100;
                customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                spannable.setSpan(imageSpan, spannable.length() - 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spikeValueTextView.setText(spannable);
            }

            currentRating = ((currentDamage >= 10000.0 ? currentDamage/4000.0 : 0)
                    + (currentArmor >= 200.0 ? currentArmor/150.0 : 0)
                    + (currentSpeed >= 20.0 ? currentSpeed/15.0 : 0)
                    + (currentJumpImpulse >= 20.0 ? currentJumpImpulse/15.0 : 0)
                    + (skillsPicked != null ? ((skillsPicked[3] ? 0.1 : 0) + (skillsPicked[4] ? 0.1 : 0)  +(skillsPicked[5] ? 0.1 : 0)  + (skillsPicked[6] ? 0.1 : 0)
            + (skillsPicked[7] ? 0.1 : 0)+ (skillsPicked[8] ? 0.1 : 0) +(skillsPicked[12] ? 0.1 : 0) +(skillsPicked[14] ? 0.1 : 0)
            + (skillsPicked[15] ? 0.1 : 0) + (skillsPicked[16] ? 0.1 : 0) + (skillsPicked[17] ? 0.1 : 0)+ (skillsPicked[21] ? 0.1 : 0)
            + (skillsPicked[22] ? 0.1 : 0) + (skillsPicked[24] ? 0.1 : 0) + (skillsPicked[25] ? 0.1 : 0) + (skillsPicked[26] ? 0.1 : 0)
            + (((selectedRing != null && Elements.FIRE.equals(selectedRing.getElement())) || (selectedArmor != null && Elements.FIRE.equals(selectedArmor.getElement())) || (skillsPicked != null && skillsPicked[13])) ? 0.2 : 0)
            + (((selectedRing != null && (Elements.AIR.equals(selectedRing.getElement()) || Elements.WATER.equals(selectedRing.getElement()))) ||
                    (selectedArmor != null && (Elements.AIR.equals(selectedArmor.getElement()) || Elements.WATER.equals(selectedArmor.getElement()))) || (skillsPicked != null && skillsPicked[23])) ? 0.2 : 0)) : 0));

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String formattedRating = decimalFormat.format(currentRating);

            TextView rating = findViewById(R.id.rating);

            if(currentRating < 1)
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
            else if (currentRating >= 9 && currentRating < 10)
                rating.setText("Rating: " + formattedRating + "/10 \uD83D\uDDFF");

            Log.d("EquipmentTester", "Current Rating: " + currentRating);
        }

        private boolean[] retrieveSkillsPickedFromSharedPreferences() {
            SharedPreferences preferences = getSharedPreferences("SkillState", MODE_PRIVATE);
            boolean[] skillsPicked = new boolean[27];

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
        if (countSelectedSkills() == 20) {
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
            float volume = 0.5f; // 50%
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

