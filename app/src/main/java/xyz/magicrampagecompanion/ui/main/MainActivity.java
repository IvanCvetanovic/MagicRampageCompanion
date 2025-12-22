package xyz.magicrampagecompanion.ui.main;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import xyz.magicrampagecompanion.core.utils.LocaleHelper;
import xyz.magicrampagecompanion.ui.achievements.AchievementsPage;
import xyz.magicrampagecompanion.ui.enemies.Enemies;
import xyz.magicrampagecompanion.ui.items.EquipmentTester;
import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.ui.items.Items;
import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.items.Skins;
import xyz.magicrampagecompanion.ui.levelviewer.LevelListActivity;
import xyz.magicrampagecompanion.ui.survival.SurvivalModeSelection;
import xyz.magicrampagecompanion.ui.chapters.ChapterSelection;
import xyz.magicrampagecompanion.data.storage.ItemSyncer;
import xyz.magicrampagecompanion.data.storage.LiveStatsSyncer;

public class MainActivity extends AppCompatActivity {
    // ---------------- App prefs ----------------
    private static final String SKILL_PREFERENCES = "SkillState";
    private static final String APP_PREFERENCES   = "AppPrefs";

    // ---------------- Ads prefs (manual toggle) ----------------
    // Users can force non-personalized ads even if UMP would allow personalized.
    private static final String ADS_PREFERENCES = "AdsPrefs";
    private static final String KEY_PERSONALIZED = "ads_personalized"; // default true

    // ---------------- Audio ----------------
    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    // ---------------- UMP ----------------
    private ConsentInformation consentInformation;

    private static final String TAG = "ConsentDebug";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // App init
        ItemData.init(this);
        ItemSyncer.run(this);
        LiveStatsSyncer.sync(this);

        // Apply system insets to keep content clear of status/nav bars
        View scroll = findViewById(R.id.mainScroll);
        View content = findViewById(R.id.contentRoot);
        applySystemInsets(scroll, content);

        resetSkills();

        // --- Language Button ---
        ImageButton btnLanguage = findViewById(R.id.btnLanguage);
        btnLanguage.setOnClickListener(v -> showLanguageDialog());

        ImageButton btnAdPreferences = findViewById(R.id.btnAdPreferences);
        btnAdPreferences.setOnClickListener(v -> {
            // This opens Google's official Privacy Options UI
            UserMessagingPlatform.showPrivacyOptionsForm(
                    this,
                    formError -> {
                        if (formError != null) {
                            Toast.makeText(this, "Privacy options unavailable: " + formError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        });

        // --- News Button ---
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

        /*Button levelEditorButton = findViewById(R.id.LevelViewerButton);

        levelEditorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LevelListActivity.class);
            startActivity(intent);
        });*/


        Button achievementsButton = findViewById(R.id.AchievementButton);
        achievementsButton.setOnClickListener(v -> {
            openAchievementsSection();
            playSound();
        });

        Button itemsButton = findViewById(R.id.ItemsButton);
        itemsButton.setOnClickListener(v -> {
            openItemSelection();
            playSound();
        });

        Button enemiesButton = findViewById(R.id.EnemiesButton);
        enemiesButton.setOnClickListener(v -> {
            openEnemies();
            playSound();
        });

        Button skinsButton = findViewById(R.id.SkinsButton);
        if (skinsButton != null) {
            skinsButton.setOnClickListener(v -> {
                openSkins();
                playSound();
            });
        }

        Button aboutButton = findViewById(R.id.AboutButton);
        aboutButton.setOnClickListener(v -> {
            openAboutSection();
            playSound();
        });

        // Long-press About to open the Privacy & Ads dialog (no new XML needed)
        aboutButton.setOnLongClickListener(v -> {
            showPrivacyAndAdsDialog();
            return true;
        });

        // ---------------- Ads SDK + UMP ----------------
        // 1) Initialize the Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {
            // Optionally inspect adapters here
        });

        // 2) Request/update consent info and show the consent form if required
        requestConsentAndMaybeShowForm();
    }

    // Request consent info on every app start and show form if required.
    private void requestConsentAndMaybeShowForm() {
        Log.d(TAG, "Starting consent info request...");

        ConsentRequestParameters params =
                new ConsentRequestParameters.Builder()
                        // Add ConsentDebugSettings here if needed for testing.
                        .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);

        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                () -> {
                    Log.d(TAG, "Consent info updated. Form available: "
                            + consentInformation.isConsentFormAvailable());

                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            formError -> {
                                if (formError != null) {
                                    Log.w(TAG, "Form error: " + formError.getMessage());
                                } else {
                                    Log.d(TAG, "Consent form was shown and closed by the user.");
                                }
                                onConsentFlowFinished();
                            }
                    );

                    if (!consentInformation.isConsentFormAvailable()) {
                        Log.d(TAG, "Consent form is NOT available for this user/region.");
                    }
                },
                requestError -> {
                    Log.e(TAG, "Consent info request failed: " + requestError.getMessage());
                    onConsentFlowFinished();
                }
        );
    }

    // After UMP has settled, decide if we can request ads now.
    private void onConsentFlowFinished() {
        boolean canRequest = consentInformation != null && consentInformation.canRequestAds();
        Log.d(TAG, "Consent flow finished. canRequestAds = " + canRequest);
    }

    // ---------- Privacy & Ads dialog (no extra XML) ----------
    private void showPrivacyAndAdsDialog() {
        Log.d(TAG, "Opening Privacy Options form...");
        final SharedPreferences prefs = getSharedPreferences(ADS_PREFERENCES, MODE_PRIVATE);
        final boolean currentPersonalized = prefs.getBoolean(KEY_PERSONALIZED, true);

        final boolean[] temp = new boolean[]{ currentPersonalized }; // single checkbox state

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name) + " – Privacy & Ads")
                // One checkbox row: "Personalized ads"
                .setMultiChoiceItems(new CharSequence[]{"Personalized ads"}, new boolean[]{ currentPersonalized },
                        (d, which, isChecked) -> temp[0] = isChecked)
                // Opens UMP's official Privacy Options screen (only works when required/available).
                .setNeutralButton("Privacy options…", (d, w) -> {
                    if (consentInformation != null &&
                            consentInformation.getPrivacyOptionsRequirementStatus()
                                    == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED) {
                        UserMessagingPlatform.showPrivacyOptionsForm(
                                this,
                                formError -> {
                                    // Returned after user closes the form (formError may be null/has info).
                                }
                        );
                    } else {
                        Toast.makeText(this, "Privacy options not required/available on this device.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    prefs.edit().putBoolean(KEY_PERSONALIZED, temp[0]).apply();
                    Toast.makeText(this,
                            temp[0] ? "Personalized ads enabled" : "Personalized ads disabled",
                            Toast.LENGTH_SHORT).show();
                })
                .create();

        dialog.show();
    }

    public static AdRequest buildAdRequest(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(ADS_PREFERENCES, MODE_PRIVATE);
        boolean wantsPersonalized = prefs.getBoolean(KEY_PERSONALIZED, true);

        AdRequest.Builder builder = new AdRequest.Builder();

        if (!wantsPersonalized) {
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
        }

        return builder.build();
    }

    // ---------------- Existing code below ----------------

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
    public void openSkins() { startActivity(new Intent(this, Skins.class)); } // NEW

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
