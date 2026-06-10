package xyz.magicrampagecompanion.ui.items;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RawRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import java.util.Locale;
import androidx.appcompat.app.AlertDialog;
import xyz.magicrampagecompanion.enums.Elements;
import xyz.magicrampagecompanion.enums.WeaponTypes;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

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
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class ItemSelection extends BaseActivity {

    // --- UI ---
    private RecyclerView recyclerView;
    private HorizontalScrollView buttonsScrollView;
    private EditText searchEdit;
    private TextView emptyStateText;

    // --- Data for current list ---
    private List<?> currentFullList = new ArrayList<>();
    private String currentReturnKey = "";  // "selectedWeapon", "selectedArmor", etc.
    @RawRes private int currentPickSoundRes = 0;

    // --- Weapon tab highlighting ---
    private Button currentSelectedTab = null;
    private final List<Button> weaponTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_selection);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        int buttonId = getIntent().getIntExtra("buttonId", -1);

        buttonsScrollView = findViewById(R.id.buttonsScrollView);
        searchEdit = findViewById(R.id.searchEdit);
        emptyStateText = findViewById(R.id.emptyStateText);

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
                showList(getAllWeapons(), "selectedWeapon", R.raw.projectile_heavy_blade_withdraw);
            });
        }
        buttonSwords.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.swordList, "selectedWeapon", R.raw.projectile_heavy_blade_withdraw);
        });
        buttonStaffs.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.staffList, "selectedWeapon", R.raw.projectile_heavy_blade_withdraw);
        });
        buttonDaggers.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.daggerList, "selectedWeapon", R.raw.projectile_heavy_blade_withdraw);
        });
        buttonAxes.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.axeList, "selectedWeapon", R.raw.projectile_heavy_blade_withdraw);
        });
        buttonSpears.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.spearList, "selectedWeapon", R.raw.projectile_heavy_blade_withdraw);
        });
        buttonHammers.setOnClickListener(v -> {
            selectTab((Button) v);
            showList(ItemData.hammerList, "selectedWeapon", R.raw.projectile_heavy_blade_withdraw);
        });

        // --- Initial load depending on entry point ---
        if (buttonId == 1) { // Armor picker (no weapon tabs)
            Log.d("ItemSelection", "Armor picker");
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null); // clear any prior selection state
            showList(ItemData.armorList, "selectedArmor", R.raw.bag);

        } else if (buttonId == 2) { // Ring picker (no weapon tabs)
            Log.d("ItemSelection", "Ring picker");
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null);
            showList(ItemData.ringList, "selectedRing", R.raw.bag);

        } else if (buttonId == 3) { // Weapon picker (show tabs, default to ALL)
            Log.d("ItemSelection", "Weapon picker");
            buttonsScrollView.setVisibility(View.VISIBLE);
            selectTab(buttonAll);
            showList(getAllWeapons(), "selectedWeapon", R.raw.projectile_heavy_blade_withdraw);
            buttonsScrollView.post(() -> buttonsScrollView.smoothScrollTo(0, 0));

        } else if (buttonId == 4) { // Class picker (no weapon tabs)
            Log.d("ItemSelection", "Class picker");
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null);
            showList(ItemData.classList, "selectedClass", R.raw.bag);

        } else if (buttonId == 6) { // Elixir picker (no weapon tabs)
            Log.d("ItemSelection", "Elixir picker");
            buttonsScrollView.setVisibility(View.GONE);
            selectTab(null);
            showList(ItemData.elixirList, "selectedElixir", R.raw.potion);

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
    protected void initSfx() {
        super.initSfx();
        // Pick sounds vary by item category
        sfx.init(this, R.raw.bag, R.raw.projectile_heavy_blade_withdraw, R.raw.potion);
    }

    @Override
    protected boolean releaseSfxOnStop() {
        // Keep the pool alive during the closing transition so the pick sound
        // is audible; BaseActivity releases it in onDestroy.
        return false;
    }

    // --- Show list & re-apply current search ---
    private void showList(List<?> list, String returnKey, @RawRes int pickSoundRes) {
        // Only play click if we're actually switching tabs in the weapons view
        if (buttonsScrollView.getVisibility() == View.VISIBLE) playClick();

        currentFullList = (list != null) ? list : new ArrayList<>();
        currentReturnKey = returnKey;
        currentPickSoundRes = pickSoundRes;

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
            if (currentPickSoundRes != 0) sfx.play(currentPickSoundRes);
            returnResult(currentReturnKey, selected);
        }, (view, position) -> showItemStatsPopup(filtered.get(position)));
        recyclerView.setAdapter(adapter);

        boolean empty = filtered.isEmpty();
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        emptyStateText.setVisibility(empty ? View.VISIBLE : View.GONE);
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

    // --- Long-press stats popup ---
    private void showItemStatsPopup(Object item) {
        String title;
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int imageResId = 0;

        if (item instanceof Weapon) {
            Weapon w = (Weapon) item;
            title = w.getName();
            imageResId = w.getImageResId();
            buildWeaponStats(ssb, w);
        } else if (item instanceof Armor) {
            Armor a = (Armor) item;
            title = a.getName();
            imageResId = a.getImageResId();
            buildArmorStats(ssb, a);
        } else if (item instanceof Ring) {
            Ring r = (Ring) item;
            title = r.getName();
            imageResId = r.getImageResId();
            buildRingStats(ssb, r);
        } else {
            return; // CharacterClass / Elixir — no stats popup
        }

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_item_stats, null);
        ImageView imageView = dialogView.findViewById(R.id.dialogItemImage);
        TextView statsText = dialogView.findViewById(R.id.dialogItemStats);

        if (imageResId != 0) imageView.setImageResource(imageResId);
        statsText.setText(ssb);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton(R.string.close, null)
                .show();
    }

    private void buildWeaponStats(SpannableStringBuilder ssb, Weapon w) {
        ssb.append(getString(R.string.type)).append(getWeaponTypeString(w.getType())).append("\n");

        Elements element = w.getElement();
        ssb.append(getString(R.string.element));
        String elementName = getElementString(element);
        if (element != Elements.NEUTRAL) {
            ssb.append(elementName, new ForegroundColorSpan(getElementColor(element)), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(elementName);
        }
        ssb.append("\n");

        ssb.append(getString(R.string.damage_in_calculation))
                .append(String.valueOf(w.getMinDamage())).append(" – ").append(String.valueOf(w.getMaxDamage())).append("\n");
        ssb.append(getString(R.string.upgrades)).append(" ").append(String.valueOf(w.getUpgrades())).append("\n");

        if (w.getArmorBonus() != 0)
            ssb.append(getString(R.string.armor_bonus)).append(sign((int) w.getArmorBonus())).append("%\n");
        if (w.getSpeed() != 0)
            ssb.append(getString(R.string.speed_in_calculation)).append(sign(w.getSpeed())).append("%\n");
        if (w.getJump() != 0)
            ssb.append(getString(R.string.jump_impulse_in_calculation)).append(sign(w.getJump())).append("%\n");

        appendAttackSpeedStars(ssb, w.getAttackCooldown());

        if (w.getPierceCount() > 0)
            ssb.append(getString(R.string.pierce_count)).append(String.valueOf(w.getPierceCount())).append("\n");

        appendBooleanStat(ssb, getString(R.string.pierce_area_damage),    w.isEnablePierceAreaDamage());
        appendBooleanStat(ssb, getString(R.string.projectile_persistence), w.isPersistAgainstProjectile());
        appendBooleanStat(ssb, getString(R.string.poisonous_attack),       w.isPoisonous());
        appendBooleanStat(ssb, getString(R.string.frost_attack),           w.isFrost());

        appendObtainability(ssb, w.getObtainability());
    }

    private void buildArmorStats(SpannableStringBuilder ssb, Armor a) {
        Elements element = a.getElement();
        ssb.append(getString(R.string.element));
        String elementName = getElementString(element);
        if (element != Elements.NEUTRAL) {
            ssb.append(elementName, new ForegroundColorSpan(getElementColor(element)), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(elementName);
        }
        ssb.append("\n");

        ssb.append(getString(R.string.armor_in_calculation))
                .append(String.valueOf(a.getMinArmor())).append(" – ").append(String.valueOf(a.getMaxArmor())).append("\n");
        ssb.append(getString(R.string.upgrades)).append(" ").append(String.valueOf(a.getUpgrades())).append("\n");

        appendBonusSsb(ssb, R.string.speed_in_calculation,        a.getSpeed());
        appendBonusSsb(ssb, R.string.jump_impulse_in_calculation, a.getJump());
        appendBonusSsb(ssb, R.string.magic_bonus,                 (int) a.getMagic());
        appendBonusSsb(ssb, R.string.sword_bonus,                 (int) a.getSword());
        appendBonusSsb(ssb, R.string.staff_bonus,                 (int) a.getStaff());
        appendBonusSsb(ssb, R.string.dagger_bonus,                (int) a.getDagger());
        appendBonusSsb(ssb, R.string.axe_bonus,                   (int) a.getAxe());
        appendBonusSsb(ssb, R.string.hammer_bonus,                (int) a.getHammer());
        appendBonusSsb(ssb, R.string.spear_bonus,                 (int) a.getSpear());

        appendBooleanStat(ssb, getString(R.string.frost_resistance), a.isFrostImmune());

        appendObtainability(ssb, a.getObtainability());
    }

    private void buildRingStats(SpannableStringBuilder ssb, Ring r) {
        Elements element = r.getElement();
        ssb.append(getString(R.string.element));
        String elementName = getElementString(element);
        if (element != Elements.NEUTRAL) {
            ssb.append(elementName, new ForegroundColorSpan(getElementColor(element)), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(elementName);
        }
        ssb.append("\n");

        if (r.getArmor() != 0)
            ssb.append(getString(R.string.armor_in_calculation)).append(sign(r.getArmor())).append("\n");
        if (r.getArmorBonus() != 0)
            ssb.append(getString(R.string.armor_bonus)).append(sign((int) r.getArmorBonus())).append("%\n");

        appendBonusSsb(ssb, R.string.speed_in_calculation,        r.getSpeed());
        appendBonusSsb(ssb, R.string.jump_impulse_in_calculation, r.getJump());
        appendBonusSsb(ssb, R.string.magic_bonus,                 (int) r.getMagic());
        appendBonusSsb(ssb, R.string.sword_bonus,                 (int) r.getSword());
        appendBonusSsb(ssb, R.string.staff_bonus,                 (int) r.getStaff());
        appendBonusSsb(ssb, R.string.dagger_bonus,                (int) r.getDagger());
        appendBonusSsb(ssb, R.string.axe_bonus,                   (int) r.getAxe());
        appendBonusSsb(ssb, R.string.hammer_bonus,                (int) r.getHammer());
        appendBonusSsb(ssb, R.string.spear_bonus,                 (int) r.getSpear());

        appendObtainability(ssb, r.getObtainability());
    }

    private void appendAttackSpeedStars(SpannableStringBuilder ssb, int cooldownMs) {
        int starCount;
        if (cooldownMs <= 300)      starCount = 5;
        else if (cooldownMs <= 450) starCount = 4;
        else if (cooldownMs <= 650) starCount = 3;
        else if (cooldownMs <= 750) starCount = 2;
        else                        starCount = 1;

        ssb.append(getString(R.string.attack_speed));
        Drawable star = ContextCompat.getDrawable(this, R.drawable.star);
        if (star != null) {
            star.setBounds(0, 0, 80, 80);
            for (int i = 0; i < starCount; i++) {
                int start = ssb.length();
                ssb.append(" ");
                ssb.setSpan(new ImageSpan(star, ImageSpan.ALIGN_BASELINE), start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            ssb.append(String.valueOf(starCount)).append("★");
        }
        ssb.append("\n");
    }

    private void appendBooleanStat(SpannableStringBuilder ssb, String label, boolean value) {
        ssb.append(label).append(" ");
        Drawable icon = ContextCompat.getDrawable(this, value ? R.drawable.icon_check : R.drawable.icon_uncheck);
        if (icon != null) {
            icon.setBounds(0, 0, 80, 80);
            int start = ssb.length();
            ssb.append(" ");
            ssb.setSpan(new ImageSpan(icon, ImageSpan.ALIGN_BASELINE), start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(value ? "✓" : "✗");
        }
        ssb.append("\n");
    }

    private void appendBonusSsb(SpannableStringBuilder ssb, int resId, int value) {
        if (value != 0) ssb.append(getString(resId)).append(sign(value)).append("%\n");
    }

    private void appendObtainability(SpannableStringBuilder ssb, List<String> list) {
        if (list == null || list.isEmpty()) return;
        ssb.append("\n").append(getString(R.string.obtainability)).append("\n");
        for (String entry : list) {
            ssb.append("• ").append(entry).append("\n");
        }
    }

    private int getElementColor(Elements element) {
        switch (element) {
            case WATER:    return ContextCompat.getColor(this, R.color.water_element_color);
            case EARTH:    return ContextCompat.getColor(this, R.color.earth_element_color);
            case FIRE:     return ContextCompat.getColor(this, R.color.fire_element_color);
            case DARKNESS: return ContextCompat.getColor(this, R.color.darkness_element_color);
            case LIGHT:    return ContextCompat.getColor(this, R.color.light_element_color);
            case AIR:      return ContextCompat.getColor(this, R.color.air_element_color);
            default:       return ContextCompat.getColor(this, R.color.white);
        }
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
            default:       return getString(R.string.element_neutral);
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT);
    }

    private String sign(int n) {
        return n >= 0 ? "+" + n : String.valueOf(n);
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

}
