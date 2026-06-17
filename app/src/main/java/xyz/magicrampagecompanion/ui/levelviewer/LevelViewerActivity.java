package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.rewarded.RewardItem;

import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.core.utils.RewardedAdManager;
import xyz.magicrampagecompanion.level.Level;
import xyz.magicrampagecompanion.level.LevelParser;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class LevelViewerActivity extends BaseActivity {

    private static final String TAG = "LevelViewerActivity";
    private static final String PREFS_NAME = "LevelViewerPrefs";
    private static final String UNLOCK_PREFIX = "unlocked_secrets_";

    private final RewardedAdManager rewardedAdManager = RewardedAdManager.forLevelViewer();
    private LevelRenderView renderView;
    private ImageButton btnShowSecrets;
    private Level currentLevel;
    private String levelFile;

    // Editor property inspector (Phase 2)
    private LinearLayout editorBottomPanel;
    private View editorInspector;
    private TextView inspectorHeader;
    private TextView inspectorMeta;
    private EditText etX, etY, etZ, etScaleX, etScaleY, etAngle;
    private CheckBox cbFlipX, cbFlipY;
    private boolean suppressWatchers = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_viewer);

        levelFile = getIntent().getStringExtra("levelFile");
        if (levelFile == null) {
            Toast.makeText(this, R.string.no_level_file_provided, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rewardedAdManager.loadAd(this);

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
        java.util.regex.Matcher titleMatcher = java.util.regex.Pattern.compile("dungeon(\\d+)")
                .matcher(levelFile.replace(".esc", ""));
        String titleKey = titleMatcher.find()
                ? titleMatcher.replaceFirst("dungeon_" + (Integer.parseInt(titleMatcher.group(1)) + 1)).replace(".", "_")
                : levelFile.replace(".esc", "");
        int titleResId = getResources().getIdentifier(titleKey, "string", getPackageName());
        title.setText(titleResId != 0 ? getString(titleResId) : levelFile.replace(".esc", ""));

        ImageButton btnBack = findViewById(R.id.btnLevelBack);
        btnBack.setOnClickListener(v -> { playClick(); finish(); });

        renderView = findViewById(R.id.levelRenderView);

        // Check if this level is already unlocked
        if (isLevelUnlocked(levelFile)) {
            renderView.setSecretsUnlocked(true);
        }

        // --- Toggle Logic Button ---
        ImageButton btnToggleLogic = findViewById(R.id.btnToggleLogic);
        btnToggleLogic.setAlpha(0.4f); // starts hidden
        btnToggleLogic.setOnClickListener(v -> {
            playClick();
            boolean current = renderView.isShowingLogicEntities();
            renderView.setShowLogicEntities(!current);
            btnToggleLogic.setAlpha(renderView.isShowingLogicEntities() ? 1.0f : 0.4f);
            int statusRes = renderView.isShowingLogicEntities() ? R.string.utility_markers_visible : R.string.utility_markers_hidden;
            Toast.makeText(this, statusRes, Toast.LENGTH_SHORT).show();
        });

        // --- Edit Mode toggle + property inspector ---
        editorBottomPanel = findViewById(R.id.editorBottomPanel);
        editorInspector   = findViewById(R.id.editorInspector);
        inspectorHeader   = findViewById(R.id.inspectorHeader);
        inspectorMeta     = findViewById(R.id.inspectorMeta);
        etX = findViewById(R.id.inspectorX);
        etY = findViewById(R.id.inspectorY);
        etZ = findViewById(R.id.inspectorZ);
        etScaleX = findViewById(R.id.inspectorScaleX);
        etScaleY = findViewById(R.id.inspectorScaleY);
        etAngle  = findViewById(R.id.inspectorAngle);
        cbFlipX  = findViewById(R.id.inspectorFlipX);
        cbFlipY  = findViewById(R.id.inspectorFlipY);
        setupInspectorBindings();
        renderView.setOnSelectionChangedListener(this::populateInspector);

        ImageButton btnToggleSnap = findViewById(R.id.btnToggleSnap);
        btnToggleSnap.setAlpha(renderView.isSnapToGrid() ? 1.0f : 0.4f);
        btnToggleSnap.setOnClickListener(v -> {
            playClick();
            boolean snap = !renderView.isSnapToGrid();
            renderView.setSnapToGrid(snap);
            btnToggleSnap.setAlpha(snap ? 1.0f : 0.4f);
            Toast.makeText(this, snap ? R.string.snap_on : R.string.snap_off, Toast.LENGTH_SHORT).show();
        });

        ImageButton btnToggleEdit = findViewById(R.id.btnToggleEdit);
        btnToggleEdit.setAlpha(0.4f); // starts in VIEW mode
        btnToggleEdit.setOnClickListener(v -> {
            playClick();
            boolean enable = !renderView.isEditMode();
            renderView.setEditMode(enable);
            btnToggleEdit.setAlpha(enable ? 1.0f : 0.4f);
            editorBottomPanel.setVisibility(enable ? View.VISIBLE : View.GONE);
            btnToggleSnap.setVisibility(enable ? View.VISIBLE : View.GONE);
            if (enable) populateInspector(renderView.getSelectedEntity());
            Toast.makeText(this, enable ? R.string.edit_mode_on : R.string.edit_mode_off,
                    Toast.LENGTH_SHORT).show();
        });

        // --- Show Secrets Button (Ad-locked + Navigation) ---
        btnShowSecrets = findViewById(R.id.btnShowSecrets);
        if (renderView.isSecretsUnlocked()) {
            btnShowSecrets.setColorFilter(Color.YELLOW);
        }

        btnShowSecrets.setOnClickListener(v -> {
            playClick();
            if (renderView.isSecretsUnlocked()) {
                showSecretAreasList();
            } else if (!hasSecretAreas()) {
                showNoSecretAreaDialog();
            } else {
                showAdConfirmationDialog();
            }
        });

        // --- Parse and display level ---
        try {
            currentLevel = LevelParser.parse(this, "levels/" + levelFile);
            if (!levelFile.matches("dungeon\\d+.*\\.esc")) {
                renderView.setInitialZoomMultiplier(4.0f);
            }
            renderView.setLevel(currentLevel);
        } catch (Exception e) {
            Log.e(TAG, "Failed to load level " + levelFile, e);
            Toast.makeText(this, getString(R.string.failed_to_load_level, e.getMessage()), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupInspectorBindings() {
        bindFloatField(etX, v -> { LevelEntity e = renderView.getSelectedEntity(); if (e != null) e.x = v; });
        bindFloatField(etY, v -> { LevelEntity e = renderView.getSelectedEntity(); if (e != null) e.y = v; });
        bindFloatField(etZ, v -> { LevelEntity e = renderView.getSelectedEntity(); if (e != null) e.z = v; });
        bindFloatField(etScaleX, v -> { LevelEntity e = renderView.getSelectedEntity(); if (e != null) e.scaleX = v; });
        bindFloatField(etScaleY, v -> { LevelEntity e = renderView.getSelectedEntity(); if (e != null) e.scaleY = v; });
        bindFloatField(etAngle, v -> { LevelEntity e = renderView.getSelectedEntity(); if (e != null) e.angle = v; });
        cbFlipX.setOnCheckedChangeListener((b, checked) -> {
            if (suppressWatchers) return;
            LevelEntity e = renderView.getSelectedEntity();
            if (e != null) { e.flipX = checked; renderView.invalidate(); }
        });
        cbFlipY.setOnCheckedChangeListener((b, checked) -> {
            if (suppressWatchers) return;
            LevelEntity e = renderView.getSelectedEntity();
            if (e != null) { e.flipY = checked; renderView.invalidate(); }
        });
    }

    private void bindFloatField(EditText et, FloatApplier applier) {
        et.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                if (suppressWatchers || renderView.getSelectedEntity() == null) return;
                try {
                    applier.apply(Float.parseFloat(s.toString().trim()));
                    renderView.invalidate();
                } catch (NumberFormatException ignored) {
                    // Partial input (e.g. "-" or empty) — wait for a valid number.
                }
            }
        });
    }

    private void populateInspector(LevelEntity e) {
        suppressWatchers = true;
        if (e == null) {
            editorInspector.setVisibility(View.GONE);
        } else {
            editorInspector.setVisibility(View.VISIBLE);
            String name = (e.entityName == null || e.entityName.isEmpty()) ? ("id " + e.id) : e.entityName;
            String sprite = (e.spriteFile == null || e.spriteFile.isEmpty())
                    ? getString(R.string.inspector_no_sprite) : e.spriteFile;
            inspectorHeader.setText(name + "  •  " + sprite);
            String meta = getString(R.string.inspector_blend, e.blendMode);
            if (e.customData != null && !e.customData.isEmpty()) {
                meta += "  •  {" + android.text.TextUtils.join(", ", e.customData.keySet()) + "}";
            }
            inspectorMeta.setText(meta);
            etX.setText(fmt(e.x));
            etY.setText(fmt(e.y));
            etZ.setText(fmt(e.z));
            etScaleX.setText(fmt(e.scaleX));
            etScaleY.setText(fmt(e.scaleY));
            etAngle.setText(fmt(e.angle));
            cbFlipX.setChecked(e.flipX);
            cbFlipY.setChecked(e.flipY);
        }
        suppressWatchers = false;
    }

    private static String fmt(float v) {
        if (!Float.isInfinite(v) && v == Math.rint(v)) return String.valueOf((long) v);
        return String.valueOf(v);
    }

    private interface FloatApplier { void apply(float v); }

    private boolean isLevelUnlocked(String fileName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(UNLOCK_PREFIX + fileName, false);
    }

    private void saveLevelUnlock(String fileName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(UNLOCK_PREFIX + fileName, true).apply();
    }

    private boolean hasSecretAreas() {
        if (currentLevel == null) return false;
        for (LevelEntity e : currentLevel.entities) {
            if (e.entityName.toLowerCase().contains("secret")) return true;
        }
        return false;
    }

    private void showNoSecretAreaDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.no_secret_area_available)
                .setPositiveButton(R.string.close, null)
                .show();
    }

    private void showAdConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.unlock_secret_areas_title)
                .setMessage(R.string.unlock_secret_areas_message)
                .setPositiveButton(R.string.watch_ad, (d, which) -> showAdOrLoad())
                .setNegativeButton(R.string.maybe_later, null)
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getColor(R.color.dialog_ok));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getColor(R.color.dialog_cancel));
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
            Toast.makeText(this, R.string.no_secret_areas, Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[secrets.size()];
        for (int i = 0; i < secrets.size(); i++) {
            names[i] = getString(R.string.secret_area_item, i + 1);
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.select_secret_area)
                .setItems(names, (dialog, which) -> {
                    LevelEntity selected = secrets.get(which);
                    renderView.centerAndZoomOnWorldPos(selected.x, selected.y);
                    Toast.makeText(this, getString(R.string.moved_to_secret_area, which + 1), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.close, null)
                .show();
    }

    private void showAdOrLoad() {
        if (rewardedAdManager.isReady()) {
            rewardedAdManager.show(this, new RewardedAdManager.RewardCallback() {
                @Override
                public void onUserEarnedReward(RewardItem rewardItem) {
                    saveLevelUnlock(levelFile);
                    renderView.setSecretsUnlocked(true);
                    btnShowSecrets.setColorFilter(Color.YELLOW);
                    Toast.makeText(LevelViewerActivity.this, R.string.secrets_revealed, Toast.LENGTH_LONG).show();
                }

                @Override public void onAdClosed() {}

                @Override
                public void onAdFailed(AdError error) {
                    Log.w(TAG, "Rewarded ad failed to show: " + error.getMessage());
                }

                @Override public void onAdNotReady() {}
            });
        } else {
            Toast.makeText(this, R.string.ad_still_loading, Toast.LENGTH_SHORT).show();
            rewardedAdManager.loadAd(this);
        }
    }
}
