package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String SKILL_PREFERENCES = "SkillState";
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ItemData.init(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.click);

        resetSkills();

        Button chapterSelectionButton = findViewById(R.id.ChapterSelectionButton);
        chapterSelectionButton.setOnClickListener(v -> {
            openChapterSelection();
            playSound();
        });

        Button survivalModeSelectionButton = findViewById(R.id.SurvivalModeSelectionButton);
        survivalModeSelectionButton.setOnClickListener(v -> {
            openSurvivalModeSelection();
            playSound();
        });

        Button equipmentTesterButton = findViewById(R.id.EquipmentTesterButton);
        equipmentTesterButton.setOnClickListener(v -> {
            openEquipmentTester();
            playSound();
        });

        Button aboutButton = findViewById(R.id.AboutButton);
        aboutButton.setOnClickListener(v -> {
            openAboutSection();
            playSound();
        });

        Button achievementsButton = findViewById(R.id.AchievementButton);
        achievementsButton.setOnClickListener(v -> {
            openAchievementsSection();
            playSound();
        });

        Button ItemsButton = findViewById(R.id.ItemsButton);
        ItemsButton.setOnClickListener(v -> {
            openItemSelection();
            playSound();
        });

        Button EnemiesButton = findViewById(R.id.EnemiesButton);
        EnemiesButton.setOnClickListener(v -> {
            openEnemies();
            playSound();
        });

    }

    private void resetSkills() {
        SharedPreferences preferences = getSharedPreferences(SKILL_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < 27; i++) {
            editor.putBoolean("skillsPicked_" + i, false);
        }
        editor.apply();
    }

    public void openChapterSelection()
    {
        Intent intent = new Intent(this, ChapterSelection.class);
        startActivity(intent);
    }

    public void openSurvivalModeSelection()
    {
        Intent intent = new Intent(this, SurvivalModeSelection.class);
        startActivity(intent);
    }

    public void openEquipmentTester()
    {
        Intent intent = new Intent(this, EquipmentTester.class);
        startActivity(intent);
    }

    public void openAboutSection()
    {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    public void openAchievementsSection()
    {
        Intent intent = new Intent(this, AchievementsPage.class);
        startActivity(intent);
    }

    public void openItemSelection()
    {
        Intent intent = new Intent(this, Items.class);
        startActivity(intent);
    }

    public void openEnemies()
    {
        Intent intent = new Intent(this, Enemies.class);
        startActivity(intent);
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

