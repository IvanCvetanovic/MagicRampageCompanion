package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.rewarded.RewardItem;

import java.util.ArrayList;
import java.util.List;

public class EquipmentTester extends AppCompatActivity {

    private EquipmentSetAdapter adapter;

    // Sounds
    private SoundPool soundPool;
    private int sfxClickId = 0;      // generic UI click
    private int sfxBagId = 0;        // equip / save / confirm
    private int sfxWeaponId = 0;     // weapon-ish action
    private int sfxPotionId = 0;     // elixir confirm

    private boolean sfxClickLoaded = false;
    private boolean sfxBagLoaded = false;
    private boolean sfxWeaponLoaded = false;
    private boolean sfxPotionLoaded = false;

    private RewardedAdManager rewardedAdManager;

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
            @Override public void onPickArmor(int position)  { playSfx(sfxBagId); pendingPosition = position; openItemSelection(1); }
            @Override public void onPickRing(int position)   { playSfx(sfxBagId); pendingPosition = position; openItemSelection(2); }
            @Override public void onPickWeapon(int position) { playSfx(sfxBagId); pendingPosition = position; openItemSelection(3); }
            @Override public void onPickClass(int position)  { playSfx(sfxClickId); pendingPosition = position; openItemSelection(4); }
            @Override public void onPickSkills(int position) { playSfx(sfxClickId); pendingPosition = position; openSkillPicker(); }
            @Override public void onRemoveSet(int position)  { playSfx(sfxClickId); adapter.removeAt(position); }

            @Override public void onSaveSet(int position) {
                playSfx(sfxClickId);
                EquipmentSet set = adapter.getItem(position);
                promptAndSaveSingleSet(set);
            }

            @Override
            public void onShowStats(int position) {
                playSfx(sfxClickId);
                EquipmentSet set = adapter.getItem(position);
                showStatsDialog(set);
            }

            @Override public void onPickArmorElement(int position)  { playSfx(sfxClickId); handleElementPick(position, Slot.ARMOR); }
            @Override public void onPickWeaponElement(int position) { playSfx(sfxClickId); handleElementPick(position, Slot.WEAPON); }
            @Override public void onPickRingElement(int position)   { playSfx(sfxClickId); handleElementPick(position, Slot.RING); }

            @Override public void onPickDrink(int position) {
                playSfx(sfxClickId);
                pendingPosition = position;
                openItemSelection(6);
            }

            // FOOTER: Load / Add / Recommend
            @Override public void onFooterLoadTapped() {
                playSfx(sfxClickId);
                showLoadDialogWithDelete();
            }

            @Override public void onFooterAddTapped(boolean atLimit) {
                playSfx(sfxClickId);
                if (atLimit) {
                    Toast.makeText(EquipmentTester.this, R.string.max_sets_reached, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFooterRecommendTapped() {
                playSfx(sfxClickId);

                if (rewardedAdManager != null && rewardedAdManager.isReady()) {
                    rewardedAdManager.show(EquipmentTester.this, new RewardedAdManager.RewardCallback() {
                        @Override public void onUserEarnedReward(RewardItem rewardItem) {
                            if (!ItemData.rewardSets.isEmpty()) {
                                int idx = new java.util.Random().nextInt(ItemData.rewardSets.size());
                                EquipmentSet src = ItemData.rewardSets.get(idx);

                                EquipmentSet set = normalizeCopy(src); // your existing copy

                                // Preserve manual element overrides from the source reward set
                                if (src.weaponElement != null && src.weaponElement != Elements.NEUTRAL &&
                                        (set.weapon == null || set.weapon.getElement() == null || set.weapon.getElement() == Elements.NEUTRAL)) {
                                    set.weaponElement = src.weaponElement;
                                }
                                if (src.armorElement != null && src.armorElement != Elements.NEUTRAL &&
                                        (set.armor == null || set.armor.getElement() == null || set.armor.getElement() == Elements.NEUTRAL)) {
                                    set.armorElement = src.armorElement;
                                }
                                if (src.ringElement != null && src.ringElement != Elements.NEUTRAL &&
                                        (set.ring == null || set.ring.getElement() == null || set.ring.getElement() == Elements.NEUTRAL)) {
                                    set.ringElement = src.ringElement;
                                }

                                appendLoadedSet(set, "Reward Set (random #" + (idx + 1) + ")");
                            } else {
                                Toast.makeText(EquipmentTester.this, R.string.no_reward_sets, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onAdClosed() {
                            // Optional: maybe toast or UI refresh
                        }

                        @Override
                        public void onAdFailed(AdError error) {
                            Toast.makeText(EquipmentTester.this, R.string.ad_failed + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdNotReady() {
                            Toast.makeText(EquipmentTester.this, R.string.ad_still_loading, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(EquipmentTester.this, R.string.ad_still_loading, Toast.LENGTH_SHORT).show();
                    if (rewardedAdManager != null) rewardedAdManager.loadAd(EquipmentTester.this);
                }
            }

        });
        rv.setAdapter(adapter);

        ArrayList<EquipmentSet> initial = new ArrayList<>();
        initial.add(new EquipmentSet());
        adapter.setItems(initial);

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

    private void handleElementPick(int position, Slot slot) {
        EquipmentSet set = adapter.getItem(position);
        switch (slot) {
            case ARMOR: {
                if (set.armor == null) { Toast.makeText(this, R.string.pick_item_first, Toast.LENGTH_SHORT).show(); return; }
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

    private void showLoadDialogWithDelete() {
        List<String> names = SaveLoadManager.listSavedSetNames(this);
        if (names.isEmpty()) {
            Toast.makeText(this, R.string.nothing_to_load, Toast.LENGTH_SHORT).show();
            return;
        }

        View content = getLayoutInflater().inflate(R.layout.dialog_saved_sets, null);
        RecyclerView list = content.findViewById(R.id.rvSavedSets);
        list.setLayoutManager(new LinearLayoutManager(this));

        final AlertDialog[] dlgHolder = new AlertDialog[1];

        // This is a *different* adapter (SavedSetsAdapter, not EquipmentSetAdapter)
        final SavedSetsAdapter[] holder = new SavedSetsAdapter[1];

        SavedSetsAdapter savedAdapter = new SavedSetsAdapter(
                new ArrayList<>(names),
                name -> {
                    EquipmentSet loaded = SaveLoadManager.loadSetByName(this, name);
                    if (loaded != null) {
                        appendLoadedSet(loaded, name);
                        playSfx(sfxBagId);
                        if (dlgHolder[0] != null) dlgHolder[0].dismiss();
                    } else {
                        Toast.makeText(this, R.string.load_failed, Toast.LENGTH_SHORT).show();
                    }
                },
                name -> {
                    playSfx(sfxClickId);
                    @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) AlertDialog confirm = new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.delete_question, name))
                            .setNegativeButton(android.R.string.cancel, (d,w) -> playSfx(sfxClickId))
                            .setPositiveButton(R.string.delete, (d,w) -> {
                                playSfx(sfxBagId);
                                SaveLoadManager.deleteNamed(this, name);
                                if (holder[0] != null) {
                                    holder[0].removeName(name);
                                    if (holder[0].getItemCount() == 0 && dlgHolder[0] != null) {
                                        dlgHolder[0].dismiss();
                                    }
                                }
                            })
                            .show();
                    tintDialogButtons(confirm);
                }
        );
        holder[0] = savedAdapter;

        list.setAdapter(savedAdapter);


        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle(R.string.load_set_title)
                .setView(content)
                .setNegativeButton(android.R.string.cancel, (d,w) -> playSfx(sfxClickId))
                .create();
        dlgHolder[0] = dlg;
        dlg.show();
        tintDialogButtons(dlg);
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
                    playSfx(sfxClickId);
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
        if (slot == Slot.ARMOR)      set.armorElement  = picked;
        else if (slot == Slot.WEAPON) set.weaponElement = picked;
        else                          set.ringElement   = picked;

        adapter.notifyItemChanged(position);
        requestRecalc();
        playSfx(sfxBagId);
    }
    // ---- END ELEMENT PICKER ----

    // ---- SAVE helpers ----
    @SuppressLint("StringFormatInvalid")
    private void promptAndSaveSingleSet(EquipmentSet set) {
        final EditText input = new EditText(this);
        input.setHint(R.string.enter_set_name_hint);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        int pad = (int) (8 * getResources().getDisplayMetrics().density);
        LinearLayout container = new LinearLayout(this);
        container.setPadding(pad, pad, pad, pad);
        container.addView(input);

        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle(R.string.save_set_title)
                .setView(container)
                .setNegativeButton(android.R.string.cancel, (dlg, w) -> playSfx(sfxClickId))
                .setPositiveButton(android.R.string.ok, (dlg, w) -> {
                    playSfx(sfxBagId);
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(this, R.string.set_name_empty, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (SaveLoadManager.exists(this, name)) {
                        AlertDialog ow = new AlertDialog.Builder(this)
                                .setMessage(getString(R.string.overwrite_question, name))
                                .setNegativeButton(android.R.string.cancel, (d2, w2) -> playSfx(sfxClickId))
                                .setPositiveButton(R.string.overwrite, (d2, w2) -> {
                                    playSfx(sfxBagId);
                                    SaveLoadManager.saveSetNamed(this, name, set);
                                    Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                                })
                                .show();
                        tintDialogButtons(ow);
                    } else {
                        SaveLoadManager.saveSetNamed(this, name, set);
                        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

        tintDialogButtons(d);
    }

    @SuppressLint({"StringFormatInvalid", "SetTextI18n"})
    private void showStatsDialog(EquipmentSet set) {
        SetStats stats = StatsCalculator.compute(set);
        View content = getLayoutInflater().inflate(R.layout.dialog_set_stats, null);

        // --- base/bonus pieces ---
        int baseDamage = (set.weapon != null) ? set.weapon.getMaxDamage() : 0;
        int bonusDamage = Math.max(0, stats.damage - baseDamage);

        int baseArmor = (set.armor != null) ? set.armor.getMaxArmor() : 0;
        int bonusArmor = Math.max(0, stats.armor - baseArmor);

        // --- labels ---
        String dmgLabel    = getString(R.string.damage_in_calculation);
        String armorLabel  = getString(R.string.armor_in_calculation);
        String speedLabel  = getString(R.string.speed_in_calculation);
        String jumpLabel   = getString(R.string.jump_impulse_in_calculation);
        String atkLabel    = getString(R.string.attack_speed);
        String pierceLabel = getString(R.string.pierce_count);

        // icon size (dp → px) for stars & checkmarks
        int iconPx = (int) (getResources().getDisplayMetrics().density * 20);

        // --- Damage (base + bonus) ---
        TextView tvDamage = content.findViewById(R.id.tvDamage);
        tvDamage.setText(
                android.text.Html.fromHtml(
                        dmgLabel + baseDamage + " <font color='#8cfdfc'>+" + bonusDamage + "</font>",
                        android.text.Html.FROM_HTML_MODE_LEGACY
                )
        );

        // --- Armor (base + bonus) ---
        TextView tvArmor = content.findViewById(R.id.tvArmor);
        tvArmor.setText(
                android.text.Html.fromHtml(
                        armorLabel + baseArmor + " <font color='#8cfdfc'>+" + bonusArmor + "</font>",
                        android.text.Html.FROM_HTML_MODE_LEGACY
                )
        );

        // --- Attack Speed (stars) ---
        TextView tvAttackSpeed = content.findViewById(R.id.tvAttackSpeed);
        tvAttackSpeed.setText(starsLine(atkLabel, stats.attackSpeedStars, iconPx),
                TextView.BufferType.SPANNABLE);

        // --- Speed / Jump / Pierce numbers ---
        TextView tvSpeed = content.findViewById(R.id.tvSpeed);
        tvSpeed.setText(speedLabel + stats.speedPct + "%");

        TextView tvJump = content.findViewById(R.id.tvJump);
        tvJump.setText(jumpLabel + stats.jumpPct + "%");

        TextView tvPierce = content.findViewById(R.id.tvPierce);
        tvPierce.setText(pierceLabel + stats.pierce);

        // --- Boolean flags (each has its own TextView in your layout) ---
        boolean pierceArea  = (set.weapon != null && set.weapon.isEnablePierceAreaDamage());
        boolean projPersist = stats.projPersist;
        boolean poisonous   = stats.poisonAttack;
        boolean frostAtk    = stats.frostAttack;

        TextView tvPierceArea = content.findViewById(R.id.tvPierceAreaDamage);
        tvPierceArea.setText(
                checkLine(getString(R.string.pierce_area_damage), pierceArea, iconPx),
                TextView.BufferType.SPANNABLE
        );

        // NOTE: your XML id is "tvProjectilePersistenec" (typo). Use it as-is or fix the XML & update here.
        TextView tvProjectile = content.findViewById(R.id.tvProjectilePersistence);
        tvProjectile.setText(
                checkLine(getString(R.string.projectile_persistence), projPersist, iconPx),
                TextView.BufferType.SPANNABLE
        );

        TextView tvPoison = content.findViewById(R.id.tvPoisonousAttack);
        tvPoison.setText(
                checkLine(getString(R.string.poisonous_attack), poisonous, iconPx),
                TextView.BufferType.SPANNABLE
        );

        TextView tvFrost = content.findViewById(R.id.tvFrostAttack);
        tvFrost.setText(
                checkLine(getString(R.string.frost_attack), frostAtk, iconPx),
                TextView.BufferType.SPANNABLE
        );

        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle(R.string.set_stats_title)
                .setView(content)
                .setPositiveButton(android.R.string.ok, (d, w) -> playSfx(sfxClickId))
                .show();

        tintDialogButtons(dlg);
    }



    /** Builds "Label: ★★★☆☆" using your star drawable as ImageSpans. */
    private CharSequence starsLine(String label, int starCount, int iconPx) {
        android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder();
        ssb.append(label);

        android.graphics.drawable.Drawable star = androidx.core.content.ContextCompat.getDrawable(this, R.drawable.star);
        if (star != null) {
            star.setBounds(0, 0, iconPx, iconPx);
            for (int i = 0; i < starCount; i++) {
                int start = ssb.length();
                ssb.append(" ");
                ssb.setSpan(new android.text.style.ImageSpan(star, android.text.style.ImageSpan.ALIGN_BASELINE),
                        start, start + 1, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            // Fallback to unicode stars
            for (int i = 0; i < starCount; i++) ssb.append("★");
        }
        return ssb;
    }

    /** Builds "Label: [✓]" (or [✗]) using your icon_check / icon_uncheck as ImageSpans. */
    private CharSequence checkLine(String label, boolean checked, int iconPx) {
        android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder();
        ssb.append(label).append(": ");

        int iconRes = checked ? R.drawable.icon_check : R.drawable.icon_uncheck;
        android.graphics.drawable.Drawable icon = androidx.core.content.ContextCompat.getDrawable(this, iconRes);
        if (icon != null) {
            icon.setBounds(0, 0, iconPx, iconPx);
            int start = ssb.length();
            ssb.append(" ");
            ssb.setSpan(new android.text.style.ImageSpan(icon, android.text.style.ImageSpan.ALIGN_BASELINE),
                    start, start + 1, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(checked ? "✓" : "✗");
        }
        return ssb;
    }



    @SuppressLint("StringFormatInvalid")
    private void appendLoadedSet(EquipmentSet loaded, String name) {
        List<EquipmentSet> items = adapter.getItems();
        if (items.size() >= EquipmentSetAdapter.MAX_SETS) {
            Toast.makeText(this, R.string.max_sets_reached, Toast.LENGTH_SHORT).show();
            return;
        }
        items.add(loaded);
        adapter.notifyItemInserted(items.size() - 1);
        requestRecalc();
        Toast.makeText(this, getString(R.string.loaded_named, name), Toast.LENGTH_SHORT).show();
    }

    // ---- END SAVE helpers ----

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
            case 1: {
                Armor selectedArmor = data.getParcelableExtra("selectedArmor");
                if (selectedArmor != null) {
                    set.armor = selectedArmor;
                    set.armorUpgrades = selectedArmor.getUpgrades();
                    set.armorElement = selectedArmor.getElement();
                    playSfx(sfxBagId);
                }
                break;
            }
            case 2: {
                Ring selectedRing = data.getParcelableExtra("selectedRing");
                if (selectedRing != null) {
                    set.ring = selectedRing;
                    set.ringElement = selectedRing.getElement();
                    playSfx(sfxBagId);
                }
                break;
            }
            case 3: {
                Weapon selectedWeapon = data.getParcelableExtra("selectedWeapon");
                if (selectedWeapon != null) {
                    set.weapon = selectedWeapon;
                    set.weaponUpgrades = selectedWeapon.getUpgrades();
                    set.weaponElement = selectedWeapon.getElement();
                    playSfx(sfxWeaponId);
                }
                break;
            }
            case 4: {
                CharacterClass selectedClass = data.getParcelableExtra("selectedClass");
                if (selectedClass != null) {
                    set.characterClass = selectedClass;
                    playSfx(sfxBagId);
                }
                break;
            }
            case 5: {
                boolean[] skillsPicked = retrieveSkillsPickedFromSharedPreferences();
                int count = 0; for (boolean b : skillsPicked) if (b) count++;
                Log.d("SKILLS", "skills picked count = " + count + " (from SharedPreferences)");
                set.skills = (skillsPicked != null) ? skillsPicked.clone() : null;
                playSfx(sfxBagId);
                break;
            }
            case 6: {
                Elixir selectedElixir = data.getParcelableExtra("selectedElixir");
                set.elixir = selectedElixir;
                playSfx(sfxPotionId != 0 ? sfxPotionId : sfxBagId);
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
        // recompute shared stats panel if you have one
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().post(this::initSoundPoolIfNeeded);
        if (rewardedAdManager == null) {
            rewardedAdManager = new RewardedAdManager();
        }
        rewardedAdManager.loadAd(this); // preload on start
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
            if (status != 0) return;
            if (sampleId == sfxClickId)  sfxClickLoaded  = true;
            if (sampleId == sfxBagId)    sfxBagLoaded    = true;
            if (sampleId == sfxWeaponId) sfxWeaponLoaded = true;
            if (sampleId == sfxPotionId) sfxPotionLoaded = true;
        });

        sfxClickId   = soundPool.load(this, R.raw.click, 1);
        sfxBagId     = soundPool.load(this, R.raw.bag, 1);
        sfxWeaponId  = soundPool.load(this, R.raw.projectile_heavy_blade_withdraw, 1);
        sfxPotionId  = soundPool.load(this, R.raw.potion, 1);
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            sfxClickLoaded = false;
            sfxBagLoaded = false;
            sfxWeaponLoaded = false;
            sfxPotionLoaded = false;
            sfxClickId = sfxBagId = sfxWeaponId = sfxPotionId = 0;
        }
    }

    private void playSfx(int soundId) {
        if (soundPool == null || soundId == 0) return;
        boolean loaded =
                (soundId == sfxClickId   && sfxClickLoaded) ||
                        (soundId == sfxBagId     && sfxBagLoaded)   ||
                        (soundId == sfxWeaponId  && sfxWeaponLoaded)||
                        (soundId == sfxPotionId  && sfxPotionLoaded);
        if (loaded) {
            soundPool.play(soundId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    private void tintDialogButtons(AlertDialog d) {
        int ok = ContextCompat.getColor(this, R.color.dialog_ok);
        int cancel = ContextCompat.getColor(this, R.color.dialog_cancel);
        if (d.getButton(AlertDialog.BUTTON_POSITIVE) != null)
            d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ok);
        if (d.getButton(AlertDialog.BUTTON_NEGATIVE) != null)
            d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(cancel);
    }

    private EquipmentSet normalizeCopy(EquipmentSet src) {
        EquipmentSet dst = new EquipmentSet();

        // Shallow copy items/classes/elixir
        dst.weapon = src.weapon;
        dst.armor = src.armor;
        dst.ring = src.ring;
        dst.characterClass = src.characterClass;
        dst.elixir = src.elixir;

        // Skills: clone to avoid sharing the same array
        dst.skills = (src.skills != null) ? src.skills.clone() : null;

        // Upgrades default like in onActivityResult
        dst.weaponUpgrades = (dst.weapon != null) ? dst.weapon.getUpgrades() : 0;
        dst.armorUpgrades  = (dst.armor  != null) ? dst.armor.getUpgrades()  : 0;

        // Elements default like in onActivityResult
        dst.weaponElement = (dst.weapon != null) ? dst.weapon.getElement() : null;
        dst.armorElement  = (dst.armor  != null) ? dst.armor.getElement()  : null;
        dst.ringElement   = (dst.ring   != null) ? dst.ring.getElement()   : null;

        return dst;
    }

}


