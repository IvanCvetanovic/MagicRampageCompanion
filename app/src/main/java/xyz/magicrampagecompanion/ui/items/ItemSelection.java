package xyz.magicrampagecompanion.ui.items;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import java.util.Locale;
import androidx.appcompat.app.AlertDialog;
import xyz.magicrampagecompanion.enums.Elements;
import xyz.magicrampagecompanion.enums.WeaponTypes;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.adapters.ImageAdapter;
import xyz.magicrampagecompanion.data.models.Armor;
import xyz.magicrampagecompanion.data.models.CharacterClass;
import xyz.magicrampagecompanion.data.models.Elixir;
import xyz.magicrampagecompanion.data.models.Ring;
import xyz.magicrampagecompanion.data.models.Weapon;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;

public class ItemSelection extends AppCompatActivity {

    // --- Audio ---
    private SoundPool soundPool;
    private int sfxClickId = 0;      // top tab click
    private int sfxBagId = 0;        // armor/ring/class pick
    private int sfxWeaponId = 0;     // weapon pick
    private int sfxPotionId = 0;     // elixir pick

    private boolean sfxClickLoaded = false;
    private boolean sfxBagLoaded = false;
    private boolean sfxWeaponLoaded = false;
    private boolean sfxPotionLoaded = false;

    // --- UI ---
    private RecyclerView recyclerView;
    private HorizontalScrollView buttonsScrollView;
    private EditText searchEdit;

    // --- Data for current list ---
    private List<?> currentFullList = new ArrayList<>();
    private String currentReturnKey = "";  // "selectedWeapon", "selectedArmor", etc.
    private int currentPickSoundId = 0;

    // --- Weapon tab highlighting ---
    private Button currentSelectedTab = null;
    private final List<Button> weaponTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_selection);

        initSoundPoolIfNeeded();

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        int buttonId = getIntent().getIntExtra("buttonId", -1);

        buttonsScrollView = findViewById(R.id.buttonsScrollView);
        searchEdit = findViewById(R.id.searchEdit);

        // Weapon tabs ONLY (no Armor/Rings buttons here!)
        Button buttonAll     = findViewById(R.id.button0); // ALL
        Button buttonSwords  = findViewById(R.id.button1);
        Button buttonStaffs  = findViewById(R.id.button2);
        Button buttonDaggers = findViewById(R.id.button3);
        Button buttonAxes    = findViewById(R.id.button4);
        Button buttonSpears  = findViewById(R.id.button5);
        Button buttonHammers = findViewById(R.id.button6);

        // Register weapon tabs for selection highlighting
        weaponTabs.clear();
        weaponTabs.add(buttonAll);
        weaponTabs.add(buttonSwords);
        weaponTabs.add(buttonStaffs);
        weaponTabs.add(buttonDaggers);
        weaponTabs.add(buttonAxes);
        weaponTabs.add(buttonSpears);
        weaponTabs.add(buttonHammers);

        // Insets (handles case when tabs row is gone/visible)
        applySystemInsets(findViewById(R.id.main), buttonsScrollView, searchEdit, recyclerView);

        // Search listener → filters whatever list is currently shown
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { applyFilter(s.toString()); }
        });

        // --- Weapon tab listeners ---
        if (buttonAll != null) {
            buttonAll.setOnClickListener(v -> {
                selectTab((Button) v);
                showList(getAllWeapons(), "selectedWeapon", sfxWeaponId);
            });
        }
        buttonSwords.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.swordList, "selectedWeapon", sfxWeaponId);
        });
        buttonStaffs.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.staffList, "selectedWeapon", sfxWeaponId);
        });
        buttonDaggers.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.daggerList, "selectedWeapon", sfxWeaponId);
        });
        buttonAxes.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.axeList, "selectedWeapon", sfxWeaponId);
        });
        buttonSpears.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.spearList, "selectedWeapon", sfxWeaponId);
        });
        buttonHammers.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.hammerList, "selectedWeapon", sfxWeaponId);
        });

        // --- Initial load depending on entry point ---
        if (buttonId == 1) { // Armor picker (no weapon tabs)
            Log.d("ItemSelection", "Armor picker");
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null); // clear any prior selection state
            showList(ItemData.armorList, "selectedArmor", sfxBagId);

        } else if (buttonId == 2) { // Ring picker (no weapon tabs)
            Log.d("ItemSelection", "Ring picker");
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null);
            showList(ItemData.ringList, "selectedRing", sfxBagId);

        } else if (buttonId == 3) { // Weapon picker (show tabs, default to ALL)
            Log.d("ItemSelection", "Weapon picker");
            buttonsScrollView.setVisibility(View.VISIBLE);
            selectTab(buttonAll);
            showList(getAllWeapons(), "selectedWeapon", sfxWeaponId);
            buttonsScrollView.post(() -> buttonsScrollView.smoothScrollTo(0, 0));

        } else if (buttonId == 4) { // Class picker (no weapon tabs)
            Log.d("ItemSelection", "Class picker");
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null);
            showList(ItemData.classList, "selectedClass", sfxBagId);

        } else if (buttonId == 6) { // Elixir picker (no weapon tabs)
            Log.d("ItemSelection", "Elixir picker");
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null);
            showList(ItemData.elixirList, "selectedElixir", sfxPotionId);

        } else {
            // Fallback: hide tabs, empty list
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null);
            showList(new ArrayList<>(), "", 0);
        }
    }

    // --- Tab selection highlighting (weapons only) ---
    private void selectTab(Button tab) {
        currentSelectedTab = tab;
        for (Button b : weaponTabs) {
            if (b == null) continue;
            boolean isSelected = (b == tab);
            b.setSelected(isSelected);
            b.setAlpha(isSelected ? 1.0f : 0.8f);
        }
        if (tab != null) {
            tab.announceForAccessibility(tab.getText() + " selected");
            if (buttonsScrollView != null) {
                buttonsScrollView.post(() ->
                        buttonsScrollView.smoothScrollTo(Math.max(0, tab.getLeft() - 32), 0));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Defer init so first frame draws before audio setup
        getWindow().getDecorView().post(this::initSoundPoolIfNeeded);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // keep SoundPool alive during transition so click is audible
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSoundPool();
    }

    // --- Show list & re-apply current search ---
    private void showList(List<?> list, String returnKey, int soundId) {
        // Only play click if we're actually switching tabs in the weapons view
        if (buttonsScrollView.getVisibility() == View.VISIBLE) playTopTabClick();

        currentFullList = (list != null) ? list : new ArrayList<>();
        currentReturnKey = returnKey;
        currentPickSoundId = soundId;

        applyFilter(searchEdit.getText() != null ? searchEdit.getText().toString() : "");
        ViewCompat.requestApplyInsets(findViewById(R.id.main));
    }

    // --- Filter current list based on query; set adapter snapshot ---
    private void applyFilter(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        List<Object> filtered = new ArrayList<>();
        if (q.isEmpty()) {
            for (Object o : currentFullList) filtered.add(o);
        } else {
            for (Object o : currentFullList) {
                String text = itemText(o).toLowerCase();
                if (text.contains(q)) filtered.add(o);
            }
        }

        ImageAdapter adapter = new ImageAdapter(filtered, (view, position) -> {
            Object selected = filtered.get(position);
            playSfx(currentPickSoundId);
            returnResult(currentReturnKey, selected);
        }, (view, position) -> showItemStatsPopup(filtered.get(position)));
        recyclerView.setAdapter(adapter);
    }

    // Extract human-readable label for search
    private String itemText(Object o) {
        try {
            if (o instanceof Weapon) return ((Weapon) o).getName();
            if (o instanceof Armor) return ((Armor) o).getName();
            if (o instanceof Ring) return ((Ring) o).getName();
            if (o instanceof CharacterClass) return ((CharacterClass) o).getName(this);
            if (o instanceof Elixir) return ((Elixir) o).getName();
        } catch (Exception ignored) {}
        return String.valueOf(o);
    }

    // Merge all weapon lists for "ALL"
    private List<Weapon> getAllWeapons() {
        List<Weapon> all = new ArrayList<>();
        if (ItemData.swordList  != null) all.addAll(ItemData.swordList);
        if (ItemData.staffList  != null) all.addAll(ItemData.staffList);
        if (ItemData.daggerList != null) all.addAll(ItemData.daggerList);
        if (ItemData.axeList    != null) all.addAll(ItemData.axeList);
        if (ItemData.spearList  != null) all.addAll(ItemData.spearList);
        if (ItemData.hammerList != null) all.addAll(ItemData.hammerList);
        return all;
    }

    // --- Edge-to-edge helper (tabs + search + RV) ---
    private void applySystemInsets(View root, HorizontalScrollView tabs, EditText search, RecyclerView rv) {
        final int baseRootL = root.getPaddingLeft();
        final int baseRootT = root.getPaddingTop();
        final int baseRootR = root.getPaddingRight();
        final int baseRootB = root.getPaddingBottom();

        final int baseTabsL = tabs.getPaddingLeft();
        final int baseTabsT = tabs.getPaddingTop();
        final int baseTabsR = tabs.getPaddingRight();
        final int baseTabsB = tabs.getPaddingBottom();

        final int baseSearchL = search.getPaddingLeft();
        final int baseSearchT = search.getPaddingTop();
        final int baseSearchR = search.getPaddingRight();
        final int baseSearchB = search.getPaddingBottom();

        final int baseRvL = rv.getPaddingLeft();
        final int baseRvT = rv.getPaddingTop();
        final int baseRvR = rv.getPaddingRight();
        final int baseRvB = rv.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int top = bars.top;
            int bottom = bars.bottom;

            if (tabs.getVisibility() == View.VISIBLE) {
                // Tabs present → tabs get the top inset
                root.setPadding(baseRootL, baseRootT, baseRootR, baseRootB);
                tabs.setPadding(baseTabsL, baseTabsT + top, baseTabsR, baseTabsB);
                search.setPadding(baseSearchL, baseSearchT, baseSearchR, baseSearchB);
                rv.setPadding(baseRvL, baseRvT, baseRvR, baseRvB + bottom);
            } else {
                // Tabs hidden → parent gets the top inset so search is tappable
                root.setPadding(baseRootL, baseRootT + top, baseRootR, baseRootB);
                tabs.setPadding(baseTabsL, baseTabsT, baseTabsR, baseTabsB);
                search.setPadding(baseSearchL, baseSearchT, baseSearchR, baseSearchB);
                rv.setPadding(baseRvL, baseRvT, baseRvR, baseRvB + bottom);
            }
            return insets;
        });

        ViewCompat.requestApplyInsets(root);
    }

    // --- Audio ---
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

        // Preload the SFX you use in this screen
        sfxClickId  = soundPool.load(this, R.raw.click, 1);
        sfxBagId    = soundPool.load(this, R.raw.bag, 1);
        sfxWeaponId = soundPool.load(this, R.raw.projectile_heavy_blade_withdraw, 1);
        sfxPotionId = soundPool.load(this, R.raw.potion, 1);
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            sfxClickLoaded = sfxBagLoaded = sfxWeaponLoaded = sfxPotionLoaded = false;
            sfxClickId = sfxBagId = sfxWeaponId = sfxPotionId = 0;
        }
    }

    private void playTopTabClick() {
        playSfx(sfxClickId);
    }

    private void playSfx(int soundId) {
        if (soundPool == null || soundId == 0) return;
        boolean loaded =
                (soundId == sfxClickId  && sfxClickLoaded) ||
                        (soundId == sfxBagId    && sfxBagLoaded)   ||
                        (soundId == sfxWeaponId && sfxWeaponLoaded)||
                        (soundId == sfxPotionId && sfxPotionLoaded);
        if (loaded) {
            soundPool.play(soundId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

    // --- Long-press stats popup ---
    private void showItemStatsPopup(Object item) {
        String title;
        StringBuilder sb = new StringBuilder();

        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            title = w.getName();
            sb.append(getWeaponTypeString(w.getType()));
            if (w.getElement() != Elements.NEUTRAL) {
                sb.append("  •  ").append(getElementString(w.getElement()));
            }
            sb.append("\n\n");
            sb.append(getString(R.string.damage_in_calculation)).append(w.getMinDamage()).append(" – ").append(w.getMaxDamage());
            if (w.getSpeed() != 0)      sb.append("\n").append(getString(R.string.speed_in_calculation)).append(sign(w.getSpeed())).append("%");
            if (w.getJump() != 0)       sb.append("\n").append(getString(R.string.jump_impulse_in_calculation)).append(sign(w.getJump())).append("%");
            if (w.getArmorBonus() != 0) sb.append("\n").append(getString(R.string.armor_bonus)).append(sign((int) w.getArmorBonus())).append("%");
            sb.append("\n").append(getString(R.string.attack_speed)).append(w.getAttackCooldown()).append("ms");
            if (w.getPierceCount() > 0) sb.append("\n").append(getString(R.string.pierce_count)).append(w.getPierceCount());
            if (w.isEnablePierceAreaDamage())   sb.append("\n").append(getString(R.string.pierce_area_damage));
            if (w.isPersistAgainstProjectile()) sb.append("\n").append(getString(R.string.projectile_persistence));
            if (w.isPoisonous()) sb.append("\n").append(getString(R.string.poisonous_attack));
            if (w.isFrost())     sb.append("\n").append(getString(R.string.frost_attack));

        } else if (item instanceof Armor) {
            Armor a = (Armor) item;
            title = a.getName();
            if (a.getElement() != Elements.NEUTRAL) sb.append(getElementString(a.getElement()));
            if (a.isFrostImmune()) {
                if (sb.length() > 0) sb.append("  •  ");
                sb.append(getString(R.string.frost_immune));
            }
            if (sb.length() > 0) sb.append("\n\n");
            if (a.getMinArmor() > 0 || a.getMaxArmor() > 0) {
                sb.append(getString(R.string.armor_in_calculation)).append(a.getMinArmor()).append(" – ").append(a.getMaxArmor()).append("\n");
            }
            appendBonus(sb, R.string.speed_in_calculation,   a.getSpeed());
            appendBonus(sb, R.string.jump_impulse_in_calculation, a.getJump());
            appendBonus(sb, R.string.magic_bonus,  (int) a.getMagic());
            appendBonus(sb, R.string.sword_bonus,  (int) a.getSword());
            appendBonus(sb, R.string.staff_bonus,  (int) a.getStaff());
            appendBonus(sb, R.string.dagger_bonus, (int) a.getDagger());
            appendBonus(sb, R.string.axe_bonus,    (int) a.getAxe());
            appendBonus(sb, R.string.hammer_bonus, (int) a.getHammer());
            appendBonus(sb, R.string.spear_bonus,  (int) a.getSpear());

        } else if (item instanceof Ring) {
            Ring r = (Ring) item;
            title = r.getName();
            if (r.getElement() != Elements.NEUTRAL) sb.append(getElementString(r.getElement())).append("\n\n");
            if (r.getArmor() != 0)      sb.append(getString(R.string.armor_in_calculation)).append(sign(r.getArmor())).append("\n");
            if (r.getArmorBonus() != 0) sb.append(getString(R.string.armor_bonus)).append(sign((int) r.getArmorBonus())).append("%\n");
            appendBonus(sb, R.string.speed_in_calculation,   r.getSpeed());
            appendBonus(sb, R.string.jump_impulse_in_calculation, r.getJump());
            appendBonus(sb, R.string.magic_bonus,  (int) r.getMagic());
            appendBonus(sb, R.string.sword_bonus,  (int) r.getSword());
            appendBonus(sb, R.string.staff_bonus,  (int) r.getStaff());
            appendBonus(sb, R.string.dagger_bonus, (int) r.getDagger());
            appendBonus(sb, R.string.axe_bonus,    (int) r.getAxe());
            appendBonus(sb, R.string.hammer_bonus, (int) r.getHammer());
            appendBonus(sb, R.string.spear_bonus,  (int) r.getSpear());

        } else {
            return; // CharacterClass / Elixir — no stats popup
        }

        // Trim trailing newline for a clean look
        String message = sb.toString().trim();
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.close, null)
                .show();
    }

    private String getWeaponTypeString(WeaponTypes type) {
        switch (type) {
            case SWORD:  return getString(R.string.weapon_type_sword);
            case STAFF:  return getString(R.string.weapon_type_staff);
            case DAGGER: return getString(R.string.weapon_type_dagger);
            case AXE:    return getString(R.string.weapon_type_axe);
            case HAMMER: return getString(R.string.weapon_type_hammer);
            case SPEAR:  return getString(R.string.weapon_type_spear);
            default:     return capitalize(type.name());
        }
    }

    private String getElementString(Elements element) {
        switch (element) {
            case FIRE:     return getString(R.string.element_fire);
            case WATER:    return getString(R.string.element_water);
            case DARKNESS: return getString(R.string.element_darkness);
            case LIGHT:    return getString(R.string.element_light);
            case EARTH:    return getString(R.string.element_earth);
            case AIR:      return getString(R.string.element_air);
            default:       return capitalize(element.name());
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT);
    }

    private String sign(int n) {
        return n >= 0 ? "+" + n : String.valueOf(n);
    }

    private void appendBonus(StringBuilder sb, int resId, int value) {
        if (value != 0) sb.append(getString(resId)).append(sign(value)).append("%\n");
    }

    // --- Returning the selected item to caller ---
    private void returnResult(String key, Object selectedItem) {
        Intent resultIntent = new Intent();

        if (selectedItem instanceof Armor) {
            resultIntent.putExtra(key + "Id", ((Armor) selectedItem).getId());
        } else if (selectedItem instanceof Ring) {
            resultIntent.putExtra(key + "Id", ((Ring) selectedItem).getId());
        } else if (selectedItem instanceof Weapon) {
            resultIntent.putExtra(key + "Id", ((Weapon) selectedItem).getId());
        } else if (selectedItem instanceof CharacterClass) {
            resultIntent.putExtra(key + "Id", ((CharacterClass) selectedItem).getId());
        } else if (selectedItem instanceof Elixir) {
            resultIntent.putExtra(key + "Id", ((Elixir) selectedItem).getId());
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
