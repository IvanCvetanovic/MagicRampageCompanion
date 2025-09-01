package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity {
    private static final String SKILL_PREFERENCES = "SkillState";
    private static final String APP_PREFERENCES = "AppPrefs";
    private MediaPlayer mediaPlayer;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ItemData.init(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.click);

        resetSkills();

        // --- Language Button ---
        ImageButton btnLanguage = findViewById(R.id.btnLanguage);
        btnLanguage.setOnClickListener(v -> showLanguageDialog());

        // --- News Button (New) ---
        Button newsButton = findViewById(R.id.NewsButton);
        newsButton.setOnClickListener(v -> {
            openNews();
            playSound();
        });

        // --- Existing buttons ---
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

    private void showLanguageDialog() {
        final String[] languages = {"System Default", "English", "Deutsch", "Español", "Français", "Italiano", "Português", "Русский", "Türkçe", "Українська", "日本語"};
        final String[] langCodes = {"system", "en", "de", "es", "fr", "it", "pt", "ru", "tr", "uk", "ja"};

        SharedPreferences prefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        String currentLang = prefs.getString("language", "system");

        int checkedItem = 0;
        for (int i = 0; i < langCodes.length; i++) {
            if (langCodes[i].equals(currentLang)) {
                checkedItem = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_language))
                .setSingleChoiceItems(languages, checkedItem, (dialog, which) -> {
                    String chosenLang = langCodes[which];
                    LocaleHelper.saveLanguage(MainActivity.this, chosenLang); // Save choice
                    recreate(); // Restart activity to apply new language
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void resetSkills() {
        SharedPreferences preferences = getSharedPreferences(SKILL_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < 27; i++) {
            editor.putBoolean("skillsPicked_" + i, false);
        }
        editor.apply();
    }

    // --- Navigation methods ---
    public void openNews() { startActivity(new Intent(this, News.class)); }
    public void openChapterSelection() { startActivity(new Intent(this, ChapterSelection.class)); }
    public void openSurvivalModeSelection() { startActivity(new Intent(this, SurvivalModeSelection.class)); }
    public void openEquipmentTester() { startActivity(new Intent(this, EquipmentTester.class)); }
    public void openAboutSection() { startActivity(new Intent(this, About.class)); }
    public void openAchievementsSection() { startActivity(new Intent(this, AchievementsPage.class)); }
    public void openItemSelection() { startActivity(new Intent(this, Items.class)); }
    public void openEnemies() { startActivity(new Intent(this, Enemies.class)); }

    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            float volume = 0.25f;
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}