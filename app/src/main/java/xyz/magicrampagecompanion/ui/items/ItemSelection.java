package xyz.magicrampagecompanion.ui.items;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
        });
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

    // --- Returning the selected item to caller ---
    private void returnResult(String key, Object selectedItem) {
        Intent resultIntent = new Intent();
        if (selectedItem instanceof Armor) {
            resultIntent.putExtra(key, (Armor) selectedItem);
        } else if (selectedItem instanceof Ring) {
            resultIntent.putExtra(key, (Ring) selectedItem);
        } else if (selectedItem instanceof Weapon) {
            resultIntent.putExtra(key, (Weapon) selectedItem);
        } else if (selectedItem instanceof CharacterClass) {
            resultIntent.putExtra(key, (CharacterClass) selectedItem);
        } else if (selectedItem instanceof Elixir) {
            resultIntent.putExtra(key, (Elixir) selectedItem);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
