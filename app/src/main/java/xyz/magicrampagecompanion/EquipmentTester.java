package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class EquipmentTester extends AppCompatActivity {

    private EquipmentSetAdapter adapter;

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    private final Handler ui = new Handler(Looper.getMainLooper());
    private Runnable pendingCalc;

    private void requestRecalc() {
        if (pendingCalc != null) ui.removeCallbacks(pendingCalc);
        pendingCalc = this::calculateStats;
        ui.postDelayed(pendingCalc, 16);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_equipment_tester);

        RecyclerView rv = findViewById(R.id.setRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setHasFixedSize(true);

        adapter = new EquipmentSetAdapter(new EquipmentSetAdapter.Listener() {
            @Override public void onPickArmor(int position)  { pendingPosition = position; openItemSelection(1); }
            @Override public void onPickRing(int position)   { pendingPosition = position; openItemSelection(2); }
            @Override public void onPickWeapon(int position) { pendingPosition = position; openItemSelection(3); }
            @Override public void onPickClass(int position)  { pendingPosition = position; openItemSelection(4); }
            @Override public void onPickSkills(int position) { pendingPosition = position; openSkillPicker(); }
            @Override public void onRemoveSet(int position)  { adapter.removeAt(position); }
            @Override public void onAddSetTapped(boolean atLimit) {
                if (atLimit) Toast.makeText(EquipmentTester.this, R.string.max_sets_reached, Toast.LENGTH_SHORT).show();
            }

            // Element taps
            @Override public void onPickArmorElement(int position)  { handleElementPick(position, Slot.ARMOR); }
            @Override public void onPickWeaponElement(int position) { handleElementPick(position, Slot.WEAPON); }
            @Override public void onPickRingElement(int position)   { handleElementPick(position, Slot.RING); }
        });
        rv.setAdapter(adapter);

        // Seed with one empty set
        java.util.ArrayList<EquipmentSet> initial = new java.util.ArrayList<>();
        initial.add(new EquipmentSet());
        adapter.setItems(initial);

        // Insets
        final View root = findViewById(R.id.equipmentRoot);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottom = Math.max(sys.bottom, ime.bottom);
            v.setPadding(v.getPaddingLeft(), sys.top, v.getPaddingRight(), bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    // Track which card to update when pickers return
    private int pendingPosition = RecyclerView.NO_POSITION;

    public void openItemSelection(int buttonId) {
        Intent intent = new Intent(this, ItemSelection.class);
        intent.putExtra("buttonId", buttonId);
        startActivityForResult(intent, buttonId);
    }

    public void openSkillPicker() {
        Intent intent = new Intent(this, SkillPicker.class);
        startActivityForResult(intent, 5);
    }

    // ---- ELEMENT PICKER ----
    private enum Slot { ARMOR, WEAPON, RING }

    /** Enforce: if the item's intrinsic element is non-neutral, it is LOCKED and cannot be changed. */
    private void handleElementPick(int position, Slot slot) {
        EquipmentSet set = adapter.getItem(position);

        switch (slot) {
            case ARMOR: {
                if (set.armor == null) { Toast.makeText(this, R.string.pick_item_first, Toast.LENGTH_SHORT).show(); return; }
                // Hard lock if intrinsic non-neutral
                if (set.armor.getElement() != null && set.armor.getElement() != Elements.NEUTRAL) {
                    Toast.makeText(this, R.string.element_cannot_be_picked, Toast.LENGTH_SHORT).show();
                    return;
                }
                showElementDialog(position, Slot.ARMOR);
                break;
            }
            case WEAPON: {
                if (set.weapon == null) { Toast.makeText(this, R.string.pick_item_first, Toast.LENGTH_SHORT).show(); return; }
                if (set.weapon.getElement() != null && set.weapon.getElement() != Elements.NEUTRAL) {
                    Toast.makeText(this, R.string.element_cannot_be_picked, Toast.LENGTH_SHORT).show();
                    return;
                }
                showElementDialog(position, Slot.WEAPON);
                break;
            }
            case RING: {
                if (set.ring == null) { Toast.makeText(this, R.string.pick_item_first, Toast.LENGTH_SHORT).show(); return; }
                if (set.ring.getElement() != null && set.ring.getElement() != Elements.NEUTRAL) {
                    Toast.makeText(this, R.string.element_cannot_be_picked, Toast.LENGTH_SHORT).show();
                    return;
                }
                showElementDialog(position, Slot.RING);
                break;
            }
        }
    }

    private void showElementDialog(int position, Slot slot) {
        final String[] labels = new String[] {
                getString(R.string.element_water),
                getString(R.string.element_earth),
                getString(R.string.element_darkness),
                getString(R.string.element_light),
                getString(R.string.element_fire),
                getString(R.string.element_air)
        };

        new AlertDialog.Builder(this)
                .setTitle(R.string.select_element)
                .setItems(labels, (dialog, which) -> {
                    Elements picked = Elements.NEUTRAL;
                    switch (which) {
                        case 0: picked = Elements.WATER; break;
                        case 1: picked = Elements.EARTH; break;
                        case 2: picked = Elements.DARKNESS; break;
                        case 3: picked = Elements.LIGHT; break;
                        case 4: picked = Elements.FIRE; break;
                        case 5: picked = Elements.AIR; break;
                    }
                    applyElement(position, slot, picked);
                })
                .show();
    }

    private void applyElement(int position, Slot slot, Elements picked) {
        EquipmentSet set = adapter.getItem(position);

        if (slot == Slot.ARMOR) {
            set.armorElement = picked;
            // DO NOT mutate the item: leave set.armor.getElement() as its intrinsic
            // if (set.armor != null) set.armor.setElement(picked);  <-- remove this
        } else if (slot == Slot.WEAPON) {
            set.weaponElement = picked;
            // if (set.weapon != null) set.weapon.setElement(picked); <-- remove
        } else {
            set.ringElement = picked;
            // if (set.ring != null) set.ring.setElement(picked);     <-- remove
        }

        adapter.notifyItemChanged(position);
        requestRecalc();
    }

    // ---- END ELEMENT PICKER ----

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (pendingPosition == RecyclerView.NO_POSITION) {
            Log.d("SKILLS", "onActivityResult: pendingPosition invalid, ignoring.");
            return;
        }
        if (resultCode != RESULT_OK) {
            Log.d("SKILLS", "onActivityResult: result not OK, code=" + resultCode);
            pendingPosition = RecyclerView.NO_POSITION;
            return;
        }

        EquipmentSet set = adapter.getItem(pendingPosition);
        Log.d("SKILLS", "onActivityResult: requestCode=" + requestCode + ", pos=" + pendingPosition);

        switch (requestCode) {
            case 1: { // Armor
                Armor selectedArmor = data.getParcelableExtra("selectedArmor");
                if (selectedArmor != null) {
                    set.armor = selectedArmor;
                    set.armorUpgrades = selectedArmor.getUpgrades();
                    set.armorElement = selectedArmor.getElement(); // ⬅ important
                }
                break;
            }
            case 2: { // Ring
                if (data != null) {
                    Ring selectedRing = data.getParcelableExtra("selectedRing");
                    if (selectedRing != null) {
                        set.ring = selectedRing;
                        set.ringElement = selectedRing.getElement(); // sync
                    }
                }
                break;
            }
            case 3: { // Weapon
                if (data != null) {
                    Weapon selectedWeapon = data.getParcelableExtra("selectedWeapon");
                    if (selectedWeapon != null) {
                        set.weapon = selectedWeapon;
                        set.weaponUpgrades = selectedWeapon.getUpgrades();
                        set.weaponElement = selectedWeapon.getElement(); // sync
                    }
                }
                break;
            }
            case 4: { // Class
                if (data != null) {
                    CharacterClass selectedClass = data.getParcelableExtra("selectedClass");
                    if (selectedClass != null) {
                        set.characterClass = selectedClass;
                    }
                }
                break;
            }
            case 5: { // Skills
                boolean[] skillsPicked = retrieveSkillsPickedFromSharedPreferences();
                int count = 0; for (boolean b : skillsPicked) if (b) count++;
                Log.d("SKILLS", "skills picked count = " + count + " (from SharedPreferences)");
                set.skills = (skillsPicked != null) ? skillsPicked.clone() : null;
                break;
            }
        }

        adapter.notifyItemChanged(pendingPosition);
        requestRecalc();
        pendingPosition = RecyclerView.NO_POSITION;
    }

    private boolean[] retrieveSkillsPickedFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("SkillState", MODE_PRIVATE);
        boolean[] picked = new boolean[36];
        for (int i = 0; i < picked.length; i++) {
            picked[i] = preferences.getBoolean("skill_" + i, false);
        }
        return picked;
    }

    private void calculateStats() {
        // your existing shared stats panel recompute, if any
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().post(this::initSoundPoolIfNeeded);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseSoundPool();
    }

    private void initSoundPoolIfNeeded() {
        if (soundPool != null) return;
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build())
                .build();
        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0 && sampleId == clickSfxId) clickSfxLoaded = true;
        });
        clickSfxId = soundPool.load(this, R.raw.metallic_button, 1);
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            clickSfxLoaded = false;
            clickSfxId = 0;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
