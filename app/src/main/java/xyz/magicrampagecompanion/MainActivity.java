package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String SKILL_PREFERENCES = "SkillState";
    private static final String APP_PREFERENCES = "AppPrefs";

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

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

        // Apply system insets to keep content clear of status/nav bars
        View scroll = findViewById(R.id.mainScroll);
        View content = findViewById(R.id.contentRoot);
        applySystemInsets(scroll, content);

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

    @Override
    protected void onStart() {
        super.onStart();
        // Defer init so the first frame can draw before audio setup
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
            if (status == 0 && sampleId == clickSfxId) {
                clickSfxLoaded = true;
            }
        });

        // Load click sound
        clickSfxId = soundPool.load(this, R.raw.click, 1);
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            clickSfxLoaded = false;
            clickSfxId = 0;
        }
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
                    LocaleHelper.saveLanguage(MainActivity.this, chosenLang);
                    recreate();
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
        if (soundPool != null && clickSfxLoaded) {
            soundPool.play(clickSfxId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

    // ---- Edge-to-edge helper (status+nav bar insets) ----
    private void applySystemInsets(View scrollView, View contentRoot) {
        final int baseScrollLeft   = scrollView.getPaddingLeft();
        final int baseScrollTop    = scrollView.getPaddingTop();
        final int baseScrollRight  = scrollView.getPaddingRight();
        final int baseScrollBottom = scrollView.getPaddingBottom();

        final int baseContentLeft  = contentRoot.getPaddingLeft();
        final int baseContentTop   = contentRoot.getPaddingTop();
        final int baseContentRight = contentRoot.getPaddingRight();
        final int baseContentBottom= contentRoot.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Push the *content* below the status bar (changes where the first touchable pixel is)
            contentRoot.setPadding(
                    baseContentLeft,
                    baseContentTop + bars.top,
                    baseContentRight,
                    baseContentBottom
            );
            // Keep the last button above the nav bar
            scrollView.setPadding(
                    baseScrollLeft,
                    baseScrollTop,
                    baseScrollRight,
                    baseScrollBottom + bars.bottom
            );
            return insets;
        });

        ViewCompat.requestApplyInsets(findViewById(android.R.id.content));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSoundPool();
    }
}
