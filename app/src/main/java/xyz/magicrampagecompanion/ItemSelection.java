package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

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

        HorizontalScrollView buttonsScrollView = findViewById(R.id.buttonsScrollView);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();

        Button buttonSwords = findViewById(R.id.button1);
        Button buttonStaffs = findViewById(R.id.button2);
        Button buttonDaggers = findViewById(R.id.button3);
        Button buttonAxes = findViewById(R.id.button4);
        Button buttonSpears = findViewById(R.id.button5);
        Button buttonHammers = findViewById(R.id.button6);

        // Sword tab click
        buttonSwords.setOnClickListener(v -> {
            playSound(mediaPlayerTopTab);
            ImageAdapter adapter = new ImageAdapter(ItemData.swordList, (view, position) -> {
                Weapon selectedWeapon = ItemData.swordList.get(position);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Staff tab click
        buttonStaffs.setOnClickListener(v -> {
            playSound(mediaPlayerTopTab);
            ImageAdapter adapter = new ImageAdapter(ItemData.staffList, (view, position) -> {
                Weapon selectedWeapon = ItemData.staffList.get(position);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Dagger tab click
        buttonDaggers.setOnClickListener(v -> {
            playSound(mediaPlayerTopTab);
            ImageAdapter adapter = new ImageAdapter(ItemData.daggerList, (view, position) -> {
                Weapon selectedWeapon = ItemData.daggerList.get(position);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Axe tab click
        buttonAxes.setOnClickListener(v -> {
            playSound(mediaPlayerTopTab);
            ImageAdapter adapter = new ImageAdapter(ItemData.axeList, (view, position) -> {
                Weapon selectedWeapon = ItemData.axeList.get(position);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Hammer tab click
        buttonHammers.setOnClickListener(v -> {
            playSound(mediaPlayerTopTab);
            ImageAdapter adapter = new ImageAdapter(ItemData.hammerList, (view, position) -> {
                Weapon selectedWeapon = ItemData.hammerList.get(position);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Spear tab click
        buttonSpears.setOnClickListener(v -> {
            playSound(mediaPlayerTopTab);
            ImageAdapter adapter = new ImageAdapter(ItemData.spearList, (view, position) -> {
                Weapon selectedWeapon = ItemData.spearList.get(position);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
        });

        // Load based on button ID
        if (buttonId == 1) { // Armor
            Log.d("ItemSelection", "Armor button clicked");
            mediaPlayerItemPick = MediaPlayer.create(this, R.raw.bag);
            ImageAdapter adapter = new ImageAdapter(ItemData.armorList, (view, position) -> {
                playSound(mediaPlayerItemPick);
                Armor selectedArmor = ItemData.armorList.get(position);
                returnResult("selectedArmor", selectedArmor);
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            layoutParams.topMargin = 0;
            recyclerView.setLayoutParams(layoutParams);

        } else if (buttonId == 2) { // Ring
            Log.d("ItemSelection", "Ring button clicked");
            ImageAdapter adapter = new ImageAdapter(ItemData.ringList, (view, position) -> {
                playSound(mediaPlayerItemPick);
                Ring selectedRing = ItemData.ringList.get(position);
                returnResult("selectedRing", selectedRing);
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            layoutParams.topMargin = 0;
            recyclerView.setLayoutParams(layoutParams);

        } else if (buttonId == 3) { // Weapons (default: sword tab shown)
            Log.d("ItemSelection", "Weapon button clicked");
            mediaPlayerItemPick = MediaPlayer.create(this, R.raw.projectile_heavy_blade_withdraw);
            ImageAdapter adapter = new ImageAdapter(ItemData.swordList, (view, position) -> {
                playSound(mediaPlayerItemPick);
                Weapon selectedWeapon = ItemData.swordList.get(position);
                returnResult("selectedWeapon", selectedWeapon);
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.VISIBLE);
            layoutParams.topMargin = 165;

        } else if (buttonId == 4) { // Character class
            Log.d("ItemSelection", "Class button clicked");
            ImageAdapter adapter = new ImageAdapter(ItemData.classList, (view, position) -> {
                playSound(mediaPlayerItemPick);
                CharacterClass selectedClass = ItemData.classList.get(position);
                returnResult("selectedClass", selectedClass);
            });
            recyclerView.setAdapter(adapter);
            buttonsScrollView.setVisibility(View.GONE);
            layoutParams.topMargin = 0;
            recyclerView.setLayoutParams(layoutParams);
        }
    }

    private void playSound(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            mediaPlayer.setVolume(0.5f, 0.5f); // 50% volume
            mediaPlayer.start();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerTopTab != null) {
            mediaPlayerTopTab.release();
            mediaPlayerTopTab = null;
        }
        if (mediaPlayerItemPick != null) {
            mediaPlayerItemPick.release();
            mediaPlayerItemPick = null;
        }
    }
}
