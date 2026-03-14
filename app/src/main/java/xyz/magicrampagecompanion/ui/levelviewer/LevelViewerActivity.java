package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.level.Level;
import xyz.magicrampagecompanion.level.LevelParser;

public class LevelViewerActivity extends AppCompatActivity {

    private static final String TAG = "LevelViewerActivity";
    private static final String PREFS_NAME = "LevelViewerPrefs";
    private static final String UNLOCK_PREFIX = "unlocked_secrets_";
    
    // Testing Rewarded Ad ID
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";

    private RewardedAd rewardedAd;
    private LevelRenderView renderView;
    private ImageButton btnShowSecrets;
    private boolean isLoadingAd = false;
    private Level currentLevel;
    private String levelFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_viewer);

        levelFile = getIntent().getStringExtra("levelFile");
        if (levelFile == null) {
            Toast.makeText(this, "No level file provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this, initializationStatus -> {
            Log.d(TAG, "AdMob Initialized");
            loadRewardedAd();
        });

        // --- Top bar: insets, title, back button ---
        LinearLayout topBar = findViewById(R.id.levelTopBar);
        ViewCompat.setOnApplyWindowInsetsListener(topBar, (v, insets) -> {
            Insets sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft(),
                    sysBars.top,
                    v.getPaddingRight(),
                    v.getPaddingBottom());
            v.getLayoutParams().height = (int) (56 * getResources().getDisplayMetrics().density)
                    + sysBars.top;
            v.requestLayout();
            return WindowInsetsCompat.CONSUMED;
        });

        TextView title = findViewById(R.id.levelViewerTitle);
        title.setText(levelFile.replace(".esc", ""));

        ImageButton btnBack = findViewById(R.id.btnLevelBack);
        btnBack.setOnClickListener(v -> finish());

        renderView = findViewById(R.id.levelRenderView);

        // Check if this level is already unlocked
        if (isLevelUnlocked(levelFile)) {
            renderView.setSecretsUnlocked(true);
        }

        // --- Toggle Logic Button ---
        ImageButton btnToggleLogic = findViewById(R.id.btnToggleLogic);
        btnToggleLogic.setOnClickListener(v -> {
            boolean current = renderView.isShowingLogicEntities();
            renderView.setShowLogicEntities(!current);
            btnToggleLogic.setAlpha(renderView.isShowingLogicEntities() ? 1.0f : 0.4f);
            String status = renderView.isShowingLogicEntities() ? "Visible" : "Hidden";
            Toast.makeText(this, "Utility markers: " + status, Toast.LENGTH_SHORT).show();
        });

        // --- Show Secrets Button (Ad-locked + Navigation) ---
        btnShowSecrets = findViewById(R.id.btnShowSecrets);
        if (renderView.isSecretsUnlocked()) {
            btnShowSecrets.setColorFilter(Color.YELLOW);
        }
        
        btnShowSecrets.setOnClickListener(v -> {
            if (renderView.isSecretsUnlocked()) {
                showSecretAreasList();
            } else {
                showAdConfirmationDialog();
            }
        });

        // --- Parse and display level ---
        try {
            currentLevel = LevelParser.parse(this, "levels/" + levelFile);
            renderView.setLevel(currentLevel);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load level: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean isLevelUnlocked(String fileName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(UNLOCK_PREFIX + fileName, false);
    }

    private void saveLevelUnlock(String fileName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(UNLOCK_PREFIX + fileName, true).apply();
    }

    private void showAdConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Unlock Secret Areas")
                .setMessage("Do you want to watch an ad to permanently unlock the positions of all secret areas for this level?")
                .setPositiveButton("Watch Ad", (dialog, which) -> showAdOrLoad())
                .setNegativeButton("Maybe later", null)
                .show();
    }

    private void showSecretAreasList() {
        if (currentLevel == null) return;

        List<LevelEntity> secrets = new ArrayList<>();
        for (LevelEntity e : currentLevel.entities) {
            if (e.entityName.toLowerCase().contains("secret")) {
                secrets.add(e);
            }
        }

        if (secrets.isEmpty()) {
            Toast.makeText(this, "No secret areas found in this level.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[secrets.size()];
        for (int i = 0; i < secrets.size(); i++) {
            names[i] = "Secret Area " + (i + 1) + " (ID: " + secrets.get(i).id + ")";
        }

        new AlertDialog.Builder(this)
                .setTitle("Select Secret Area")
                .setItems(names, (dialog, which) -> {
                    LevelEntity selected = secrets.get(which);
                    renderView.centerAndZoomOnWorldPos(selected.x, selected.y);
                    Toast.makeText(this, "Moved to Secret Area " + (which + 1), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Close", null)
                .show();
    }

    private void loadRewardedAd() {
        if (isLoadingAd || rewardedAd != null) return;

        isLoadingAd = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.w(TAG, "Ad failed to load: " + loadAdError.getMessage());
                rewardedAd = null;
                isLoadingAd = false;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                Log.d(TAG, "Ad was loaded.");
                rewardedAd = ad;
                isLoadingAd = false;
            }
        });
    }

    private void showAdOrLoad() {
        if (rewardedAd != null) {
            rewardedAd.show(this, rewardItem -> {
                saveLevelUnlock(levelFile);
                renderView.setSecretsUnlocked(true);
                btnShowSecrets.setColorFilter(Color.YELLOW);
                Toast.makeText(this, "Secret areas permanently revealed for this level!", Toast.LENGTH_LONG).show();
                rewardedAd = null; // Reset for next use
                loadRewardedAd(); // Pre-load next
            });
        } else {
            Toast.makeText(this, "Ad is still loading, please try again in a moment.", Toast.LENGTH_SHORT).show();
            loadRewardedAd();
        }
    }
}
