package xyz.magicrampagecompanion.ui.main;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import xyz.magicrampagecompanion.core.utils.ArtCache;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;
import xyz.magicrampagecompanion.ui.achievements.AchievementsPage;
import xyz.magicrampagecompanion.ui.common.BaseActivity;
import xyz.magicrampagecompanion.ui.enemies.Enemies;
import xyz.magicrampagecompanion.ui.items.EquipmentTester;
import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.ui.items.Items;
import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.items.Skins;
import xyz.magicrampagecompanion.ui.levelviewer.LevelListActivity;
import xyz.magicrampagecompanion.ui.editor.MenuBannerBuilder;
import xyz.magicrampagecompanion.ui.survival.SurvivalModeSelection;
import xyz.magicrampagecompanion.ui.chapters.ChapterSelection;
import xyz.magicrampagecompanion.data.storage.ItemSyncer;
import xyz.magicrampagecompanion.data.storage.LiveStatsSyncer;

public class MainActivity extends BaseActivity {
    // ---------------- App prefs ----------------
    private static final String APP_PREFERENCES = "AppPrefs";

    // ---------------- Ads prefs (manual toggle) ----------------
    // Users can force non-personalized ads even if UMP would allow personalized.
    private static final String ADS_PREFERENCES = "AdsPrefs";
    private static final String KEY_PERSONALIZED = "ads_personalized"; // default true

    // ---------------- UMP ----------------
    private ConsentInformation consentInformation;

    private static final String TAG = "ConsentDebug";

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

        // --- Language Button ---
        ImageButton btnLanguage = findViewById(R.id.btnLanguage);
        btnLanguage.setOnClickListener(v -> showLanguageDialog());

        ImageButton btnAdPreferences = findViewById(R.id.btnAdPreferences);
        btnAdPreferences.setOnClickListener(v -> {
            if (consentInformation != null
                    && consentInformation.getPrivacyOptionsRequirementStatus()
                       == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED) {
                UserMessagingPlatform.showPrivacyOptionsForm(this, formError -> {
                    if (formError != null) {
                        Toast.makeText(this,
                                getString(R.string.privacy_options_unavailable, formError.getMessage()),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, R.string.ad_consent_not_required, Toast.LENGTH_SHORT).show();
            }
        });

        // --- Navigation buttons ---
        bindNavigationButton(R.id.NewsButton, News.class);
        bindNavigationButton(R.id.ChapterSelectionButton, ChapterSelection.class);
        bindNavigationButton(R.id.SurvivalModeSelectionButton, SurvivalModeSelection.class);
        bindNavigationButton(R.id.EquipmentTesterButton, EquipmentTester.class);
        bindNavigationButton(R.id.LevelViewerButton, LevelListActivity.class);
        bindNavigationButton(R.id.AchievementButton, AchievementsPage.class);
        bindNavigationButton(R.id.ItemsButton, Items.class);
        bindNavigationButton(R.id.EnemiesButton, Enemies.class);
        bindNavigationButton(R.id.SkinsButton, Skins.class);
        bindNavigationButton(R.id.AboutButton, About.class);

        styleMenuBanners();

        // ---------------- Ads SDK + UMP ----------------
        // Request/update consent info, show form if required, then initialize MobileAds.
        requestConsentAndMaybeShowForm();
    }

    private void bindNavigationButton(int buttonId, Class<?> target) {
        View button = findViewById(buttonId); // may be a Button or a MaterialCardView banner
        if (button == null) return;
        button.setOnClickListener(v -> {
            startActivity(new Intent(this, target));
            playClick();
        });
    }

    /**
     * Paint the in-game art into the redesigned menu banners. Cached renders ({@link ArtCache}) are set
     * synchronously here, so they are present on the very first frame (no pop-in); only an uncached
     * banner is built off the UI thread and faded in, then cached for next time.
     */
    private void styleMenuBanners() {
        final int[] imgIds = {
                R.id.NewsImage, R.id.ChapterSelectionImage, R.id.SurvivalModeImage, R.id.EquipmentTesterImage,
                R.id.LevelViewerImage, R.id.AchievementImage, R.id.ItemsImage, R.id.EnemiesImage,
                R.id.SkinsImage, R.id.AboutImage
        };
        final String[] art = {
                "png:kings_room_banner.png", "png:chapter4-dungeon-portal.png", "png:skull_entity.png", "png:axe_0.png",
                "char:armored-skeleton.character", "png:covil-kings-crown.png", "png:diamond_0.png", "char:vampire.character",
                "char:rat-fire-mage.character", "png:spellbook_1.png"
        };
        final int[] accents = {
                getColor(R.color.menu_accent_news), getColor(R.color.menu_accent_dungeons),
                getColor(R.color.menu_accent_survival), getColor(R.color.menu_accent_equipment),
                getColor(R.color.menu_accent_editor), getColor(R.color.menu_accent_achievements),
                getColor(R.color.menu_accent_items), getColor(R.color.menu_accent_enemies),
                getColor(R.color.menu_accent_skins), getColor(R.color.menu_accent_about)
        };
        final boolean[] pending = new boolean[imgIds.length];
        // Cached art → set now (on the first frame). Uncached ones get built below.
        for (int i = 0; i < imgIds.length; i++) {
            Bitmap cached = ArtCache.loadCached(this, bannerKey(art[i], accents[i]));
            if (cached != null) setBanner(imgIds[i], cached, false);
            else pending[i] = true;
        }
        // First run only: build the uncached banners off the UI thread, fade them in, and cache them.
        new Thread(() -> {
            for (int i = 0; i < imgIds.length; i++) {
                if (!pending[i]) continue;
                final int idx = i;
                final Bitmap b = ArtCache.loadOrBuild(this, bannerKey(art[idx], accents[idx]),
                        () -> MenuBannerBuilder.build(this, art[idx], accents[idx]));
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) setBanner(imgIds[idx], b, true);
                });
            }
        }).start();
    }

    /** Set a banner bitmap crisply; optionally fade it in (for freshly built, uncached art). */
    private void setBanner(int imgId, Bitmap bmp, boolean fadeIn) {
        ImageView iv = findViewById(imgId);
        if (iv == null || bmp == null) return;
        BitmapDrawable d = new BitmapDrawable(getResources(), bmp);
        d.setFilterBitmap(false);
        iv.setImageDrawable(d);
        if (fadeIn) {
            iv.setAlpha(0f);
            iv.animate().alpha(1f).setDuration(220).start();
        }
    }

    /** Cache key for a banner: its art spec + accent (the W/H/darken are fixed in MenuBannerBuilder). */
    private static String bannerKey(String artSpec, int accent) {
        return "banner_" + artSpec + "_" + Integer.toHexString(accent);
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

    // After UMP has settled, initialize the Ads SDK and start loading ads.
    private void onConsentFlowFinished() {
        Log.d(TAG, "Consent flow finished. canRequestAds = "
                + (consentInformation != null && consentInformation.canRequestAds()));
        MobileAds.initialize(this, initializationStatus ->
                ((MyApplication) getApplication()).initializeAds());
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
}
