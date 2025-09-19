package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ItemSelection extends AppCompatActivity {

    private SoundPool soundPool;
    private int sfxClickId = 0;      // top tab click
    private int sfxBagId = 0;        // armor pick
    private int sfxWeaponId = 0;     // weapon pick

    private boolean sfxClickLoaded = false;
    private boolean sfxBagLoaded = false;
    private boolean sfxWeaponLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_selection);

        RecyclerView recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        int buttonId = getIntent().getIntExtra("buttonId", -1);

        HorizontalScrollView buttonsScrollView = findViewById(R.id.buttonsScrollView);
        // We won't use layoutParams.topMargin anymore — insets will handle spacing.

        Button buttonSwords  = findViewById(R.id.button1);
        Button buttonStaffs  = findViewById(R.id.button2);
        Button buttonDaggers = findViewById(R.id.button3);
        Button buttonAxes    = findViewById(R.id.button4);
        Button buttonSpears  = findViewById(R.id.button5);
        Button buttonHammers = findViewById(R.id.button6);

        // ---- Edge-to-edge insets handling ----
        applySystemInsets(findViewById(R.id.main), buttonsScrollView, recyclerView);

        // Sword tab click
        buttonSwords.setOnClickListener(v -> {
            playTopTabClick();
            ImageAdapter adapter = new ImageAdapter(ItemData.swordList, (view, position) -> {
                Weapon selectedWeapon = ItemData.swordList.get(position);
                playSfx(sfxWeaponId);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Staff tab click
        buttonStaffs.setOnClickListener(v -> {
            playTopTabClick();
            ImageAdapter adapter = new ImageAdapter(ItemData.staffList, (view, position) -> {
                Weapon selectedWeapon = ItemData.staffList.get(position);
                playSfx(sfxWeaponId);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Dagger tab click
        buttonDaggers.setOnClickListener(v -> {
            playTopTabClick();
            ImageAdapter adapter = new ImageAdapter(ItemData.daggerList, (view, position) -> {
                Weapon selectedWeapon = ItemData.daggerList.get(position);
                playSfx(sfxWeaponId);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Axe tab click
        buttonAxes.setOnClickListener(v -> {
            playTopTabClick();
            ImageAdapter adapter = new ImageAdapter(ItemData.axeList, (view, position) -> {
                Weapon selectedWeapon = ItemData.axeList.get(position);
                playSfx(sfxWeaponId);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Hammer tab click
        buttonHammers.setOnClickListener(v -> {
            playTopTabClick();
            ImageAdapter adapter = new ImageAdapter(ItemData.hammerList, (view, position) -> {
                Weapon selectedWeapon = ItemData.hammerList.get(position);
                playSfx(sfxWeaponId);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Spear tab click
        buttonSpears.setOnClickListener(v -> {
            playTopTabClick();
            ImageAdapter adapter = new ImageAdapter(ItemData.spearList, (view, position) -> {
                Weapon selectedWeapon = ItemData.spearList.get(position);
                playSfx(sfxWeaponId);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Load based on button ID
        if (buttonId == 1) { // Armor
            Log.d("ItemSelection", "Armor button clicked");
            ImageAdapter adapter = new ImageAdapter(ItemData.armorList, (view, position) -> {
                playSfx(sfxBagId); // armor pick sound
                Armor selectedArmor = ItemData.armorList.get(position);
                returnResult("selectedArmor", selectedArmor);
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            ViewCompat.requestApplyInsets(findViewById(R.id.main));

        } else if (buttonId == 2) { // Ring
            Log.d("ItemSelection", "Ring button clicked");
            ImageAdapter adapter = new ImageAdapter(ItemData.ringList, (view, position) -> {
                playSfx(sfxBagId);
                Ring selectedRing = ItemData.ringList.get(position);
                returnResult("selectedRing", selectedRing);
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            ViewCompat.requestApplyInsets(findViewById(R.id.main));

        } else if (buttonId == 3) { // Weapons (default: sword tab shown)
            Log.d("ItemSelection", "Weapon button clicked");
            ImageAdapter adapter = new ImageAdapter(ItemData.swordList, (view, position) -> {
                playSfx(sfxWeaponId); // weapon pick sound
                Weapon selectedWeapon = ItemData.swordList.get(position);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.VISIBLE);
            ViewCompat.requestApplyInsets(findViewById(R.id.main));

        } else if (buttonId == 4) { // Character class
            Log.d("ItemSelection", "Class button clicked");
            ImageAdapter adapter = new ImageAdapter(ItemData.classList, (view, position) -> {
                playSfx(sfxBagId);
                CharacterClass selectedClass = ItemData.classList.get(position);
                returnResult("selectedClass", selectedClass);
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            ViewCompat.requestApplyInsets(findViewById(R.id.main));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Defer init slightly so first frame draws before audio work
        getWindow().getDecorView().post(this::initSoundPoolIfNeeded);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseSoundPool();
    }

    // === Edge-to-edge helper ===
    private void applySystemInsets(View root, HorizontalScrollView buttonsScrollView, RecyclerView recyclerView) {
        final int baseButtonsLeft   = buttonsScrollView.getPaddingLeft();
        final int baseButtonsTop    = buttonsScrollView.getPaddingTop();
        final int baseButtonsRight  = buttonsScrollView.getPaddingRight();
        final int baseButtonsBottom = buttonsScrollView.getPaddingBottom();

        final int baseRvLeft   = recyclerView.getPaddingLeft();
        final int baseRvTop    = recyclerView.getPaddingTop();
        final int baseRvRight  = recyclerView.getPaddingRight();
        final int baseRvBottom = recyclerView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int topInset = sysBars.top;
            int bottomInset = sysBars.bottom;

            if (buttonsScrollView.getVisibility() == View.VISIBLE) {
                // Push the tabs below the status bar
                buttonsScrollView.setPadding(
                        baseButtonsLeft,
                        baseButtonsTop + topInset,
                        baseButtonsRight,
                        baseButtonsBottom
                );
                // RecyclerView sits below tabs; no extra top pad needed
                recyclerView.setPadding(
                        baseRvLeft,
                        baseRvTop,
                        baseRvRight,
                        baseRvBottom + bottomInset
                );
            } else {
                // Tabs hidden: give the top inset to the RecyclerView
                recyclerView.setPadding(
                        baseRvLeft,
                        baseRvTop + topInset,
                        baseRvRight,
                        baseRvBottom + bottomInset
                );
            }
            return insets;
        });

        // Trigger initial insets pass
        ViewCompat.requestApplyInsets(root);
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
        });

        // Preload the SFX you use in this screen
        sfxClickId  = soundPool.load(this, R.raw.click, 1);
        sfxBagId    = soundPool.load(this, R.raw.bag, 1);
        sfxWeaponId = soundPool.load(this, R.raw.projectile_heavy_blade_withdraw, 1);
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            sfxClickLoaded = sfxBagLoaded = sfxWeaponLoaded = false;
            sfxClickId = sfxBagId = sfxWeaponId = 0;
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
                        (soundId == sfxWeaponId && sfxWeaponLoaded);
        if (loaded) {
            soundPool.play(soundId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

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
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
