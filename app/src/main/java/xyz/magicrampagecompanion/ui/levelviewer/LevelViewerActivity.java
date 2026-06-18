package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.rewarded.RewardItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.core.utils.RewardedAdManager;
import xyz.magicrampagecompanion.level.Level;
import xyz.magicrampagecompanion.level.LevelParser;
import xyz.magicrampagecompanion.level.LevelSaver;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class LevelViewerActivity extends BaseActivity {

    private static final String TAG = "LevelViewerActivity";
    private static final String PREFS_NAME = "LevelViewerPrefs";
    private static final String UNLOCK_PREFIX = "unlocked_secrets_";

    private final RewardedAdManager rewardedAdManager = RewardedAdManager.forLevelViewer();
    private LevelRenderView renderView;
    private ImageButton btnShowSecrets;
    private Level currentLevel;
    private String levelFile;   // asset name (null when opened from storage)
    private String levelPath;   // absolute storage path (null when opened from assets)
    private String levelKey;    // file basename for title / secrets / zoom checks (both sources)

    // Editor property inspector (Phase 2)
    private LinearLayout editorBottomPanel;
    private View editorInspector;
    private TextView inspectorHeader;
    private TextView inspectorMeta;
    private EditText etX, etY, etZ, etScaleX, etScaleY, etAngle;
    private CheckBox cbFlipX, cbFlipY;
    private boolean suppressWatchers = false;
    private ImageButton btnAddEntity, btnDuplicateEntity, btnDeleteEntity, btnUndo, btnRedo;
    private List<String> entFiles;
    // Pending inspector numeric edit (captured on focus, committed as one undoable command on blur)
    private LevelEntity pendingEntity;
    private EntityFloatGetter pendingGetter;
    private EntityFloatSetter pendingSetter;
    private float pendingBefore = Float.NaN;

    // SAF export: user picks a destination (Downloads/Documents/...) — no storage permission needed.
    private final ActivityResultLauncher<String> exportLauncher =
            registerForActivityResult(new ActivityResultContracts.CreateDocument("application/octet-stream"),
                    uri -> { if (uri != null) writeExport(uri); });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_viewer);

        levelFile = getIntent().getStringExtra("levelFile");
        levelPath = getIntent().getStringExtra("levelPath");
        if (levelFile == null && levelPath == null) {
            Toast.makeText(this, R.string.no_level_file_provided, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        levelKey = (levelPath != null) ? new File(levelPath).getName() : levelFile;

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
            // Keep the editor's bottom panel/toolbar clear of the system nav bar.
            if (editorBottomPanel != null) {
                editorBottomPanel.setPadding(
                        editorBottomPanel.getPaddingLeft(),
                        editorBottomPanel.getPaddingTop(),
                        editorBottomPanel.getPaddingRight(),
                        sysBars.bottom);
            }
            return WindowInsetsCompat.CONSUMED;
        });

        TextView title = findViewById(R.id.levelViewerTitle);
        java.util.regex.Matcher titleMatcher = java.util.regex.Pattern.compile("dungeon(\\d+)")
                .matcher(levelKey.replace(".esc", ""));
        String titleKey = titleMatcher.find()
                ? titleMatcher.replaceFirst("dungeon_" + (Integer.parseInt(titleMatcher.group(1)) + 1)).replace(".", "_")
                : levelKey.replace(".esc", "");
        int titleResId = getResources().getIdentifier(titleKey, "string", getPackageName());
        title.setText(titleResId != 0 ? getString(titleResId) : levelKey.replace(".esc", ""));

        ImageButton btnBack = findViewById(R.id.btnLevelBack);
        btnBack.setOnClickListener(v -> { playClick(); finish(); });

        renderView = findViewById(R.id.levelRenderView);

        // Check if this level is already unlocked
        if (isLevelUnlocked(levelKey)) {
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

        ImageButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> { playClick(); attemptSave(); });

        ImageButton btnToggleEdit = findViewById(R.id.btnToggleEdit);
        btnToggleEdit.setAlpha(0.4f); // starts in VIEW mode
        btnToggleEdit.setOnClickListener(v -> {
            playClick();
            boolean enable = !renderView.isEditMode();
            renderView.setEditMode(enable);
            btnToggleEdit.setAlpha(enable ? 1.0f : 0.4f);
            editorBottomPanel.setVisibility(enable ? View.VISIBLE : View.GONE);
            btnToggleSnap.setVisibility(enable ? View.VISIBLE : View.GONE);
            btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
            if (enable) populateInspector(renderView.getSelectedEntity());
            Toast.makeText(this, enable ? R.string.edit_mode_on : R.string.edit_mode_off,
                    Toast.LENGTH_SHORT).show();
        });

        // --- Editor toolbar actions (Phase 4) ---
        btnAddEntity = findViewById(R.id.btnAddEntity);
        btnDuplicateEntity = findViewById(R.id.btnDuplicateEntity);
        btnDeleteEntity = findViewById(R.id.btnDeleteEntity);
        btnAddEntity.setOnClickListener(v -> { playClick(); showPaletteDialog(); });
        btnDuplicateEntity.setOnClickListener(v -> { playClick(); renderView.duplicateSelected(); });
        btnDeleteEntity.setOnClickListener(v -> { playClick(); confirmDeleteSelected(); });
        updateEditActionButtons(renderView.getSelectedEntity());

        btnUndo = findViewById(R.id.btnUndo);
        btnRedo = findViewById(R.id.btnRedo);
        btnUndo.setOnClickListener(v -> { playClick(); renderView.undo(); });
        btnRedo.setOnClickListener(v -> { playClick(); renderView.redo(); });
        renderView.setOnHistoryChangedListener(this::updateUndoRedoButtons);
        updateUndoRedoButtons();

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
            if (levelPath != null) {
                try (InputStream is = new FileInputStream(levelPath)) {
                    currentLevel = LevelParser.parse(this, is, levelKey);
                }
            } else {
                currentLevel = LevelParser.parse(this, "levels/" + levelFile);
            }
            if (!levelKey.matches("dungeon\\d+.*\\.esc")) {
                renderView.setInitialZoomMultiplier(4.0f);
            }
            renderView.setLevel(currentLevel);
        } catch (Exception e) {
            Log.e(TAG, "Failed to load level " + levelKey, e);
            Toast.makeText(this, getString(R.string.failed_to_load_level, e.getMessage()), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupInspectorBindings() {
        bindFloatField(etX, e -> e.x, (e, v) -> e.x = v);
        bindFloatField(etY, e -> e.y, (e, v) -> e.y = v);
        bindFloatField(etZ, e -> e.z, (e, v) -> e.z = v);
        bindFloatField(etScaleX, e -> e.scaleX, (e, v) -> { e.scaleX = v; e.scaleEdited = true; });
        bindFloatField(etScaleY, e -> e.scaleY, (e, v) -> { e.scaleY = v; e.scaleEdited = true; });
        bindFloatField(etAngle, e -> e.angle, (e, v) -> e.angle = v);
        cbFlipX.setOnCheckedChangeListener((b, checked) -> onFlipChanged(true, checked));
        cbFlipY.setOnCheckedChangeListener((b, checked) -> onFlipChanged(false, checked));
    }

    /** Applies a flip toggle live and records it as an undoable command. */
    private void onFlipChanged(boolean isX, boolean checked) {
        if (suppressWatchers) return;
        LevelEntity e = renderView.getSelectedEntity();
        if (e == null) return;
        boolean before = isX ? e.flipX : e.flipY;
        if (before == checked) return;
        if (isX) e.flipX = checked; else e.flipY = checked;
        renderView.invalidate();
        final LevelEntity ent = e;
        renderView.pushCommand(new LevelRenderView.EditCommand() {
            @Override public void undo() { if (isX) ent.flipX = before; else ent.flipY = before; }
            @Override public void redo() { if (isX) ent.flipX = checked; else ent.flipY = checked; }
        });
    }

    private void bindFloatField(EditText et, EntityFloatGetter getter, EntityFloatSetter setter) {
        // Live apply while typing (re-renders immediately).
        et.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                if (suppressWatchers) return;
                LevelEntity e = renderView.getSelectedEntity();
                if (e == null) return;
                try {
                    setter.set(e, Float.parseFloat(s.toString().trim()));
                    renderView.invalidate();
                } catch (NumberFormatException ignored) {
                    // Partial input (e.g. "-" or empty) — wait for a valid number.
                }
            }
        });
        // Capture before/after across a focus session → one undoable command per edit.
        et.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                commitPendingPropertyEdit();
                pendingEntity = renderView.getSelectedEntity();
                pendingGetter = getter;
                pendingSetter = setter;
                pendingBefore = (pendingEntity != null) ? getter.get(pendingEntity) : Float.NaN;
            } else {
                commitPendingPropertyEdit();
            }
        });
    }

    /** Pushes an undoable command if the focused field's value changed; clears the pending slot. */
    private void commitPendingPropertyEdit() {
        if (pendingEntity != null && pendingSetter != null && !Float.isNaN(pendingBefore)) {
            float after = pendingGetter.get(pendingEntity);
            if (after != pendingBefore) {
                final LevelEntity e = pendingEntity;
                final EntityFloatSetter s = pendingSetter;
                final float b = pendingBefore, a = after;
                renderView.pushCommand(new LevelRenderView.EditCommand() {
                    @Override public void undo() { s.set(e, b); }
                    @Override public void redo() { s.set(e, a); }
                });
            }
        }
        pendingEntity = null;
        pendingGetter = null;
        pendingSetter = null;
        pendingBefore = Float.NaN;
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
            boolean hasData = e.customData != null && !e.customData.isEmpty();
            if (hasData) {
                meta += "  •  {" + android.text.TextUtils.join(", ", e.customData.keySet()) + "}  ✎";
            }
            inspectorMeta.setText(meta);
            inspectorMeta.setClickable(hasData);
            if (hasData) inspectorMeta.setOnClickListener(v -> showCustomDataDialog(e));
            else inspectorMeta.setOnClickListener(null);
            etX.setText(fmt(e.x));
            etY.setText(fmt(e.y));
            etZ.setText(fmt(e.z));
            etScaleX.setText(fmt(e.scaleX));
            etScaleY.setText(fmt(e.scaleY));
            etAngle.setText(fmt(e.angle));
            cbFlipX.setChecked(e.flipX);
            cbFlipY.setChecked(e.flipY);
        }
        updateEditActionButtons(e);
        suppressWatchers = false;
    }

    private static String fmt(float v) {
        if (!Float.isInfinite(v) && v == Math.rint(v)) return String.valueOf((long) v);
        return String.valueOf(v);
    }

    private interface EntityFloatGetter { float get(LevelEntity e); }
    private interface EntityFloatSetter { void set(LevelEntity e, float v); }

    private void updateEditActionButtons(LevelEntity e) {
        boolean has = e != null;
        if (btnDuplicateEntity != null) { btnDuplicateEntity.setEnabled(has); btnDuplicateEntity.setAlpha(has ? 1f : 0.4f); }
        if (btnDeleteEntity != null) { btnDeleteEntity.setEnabled(has); btnDeleteEntity.setAlpha(has ? 1f : 0.4f); }
    }

    private void updateUndoRedoButtons() {
        if (btnUndo != null) { boolean u = renderView.canUndo(); btnUndo.setEnabled(u); btnUndo.setAlpha(u ? 1f : 0.4f); }
        if (btnRedo != null) { boolean r = renderView.canRedo(); btnRedo.setEnabled(r); btnRedo.setAlpha(r ? 1f : 0.4f); }
    }

    /** Edits the selected entity's CustomData values (existing keys only) — the data that drives
     *  gameplay (enemy lists, hp, text, drop chances, ...). Persisted on save via customDataEdited. */
    private void showCustomDataDialog(LevelEntity e) {
        if (e.customData == null || e.customData.isEmpty()) return;
        List<String> keys = new ArrayList<>(e.customData.keySet());
        Collections.sort(keys);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        container.setPadding(pad, pad / 2, pad, 0);
        List<EditText> fields = new ArrayList<>();
        for (String k : keys) {
            TextView label = new TextView(this);
            label.setText(k);
            label.setTextColor(getColor(R.color.color_text_primary));
            label.setTextSize(12f);
            container.addView(label);
            EditText field = new EditText(this);
            field.setText(e.customData.get(k));
            container.addView(field);
            fields.add(field);
        }
        android.widget.ScrollView scroll = new android.widget.ScrollView(this);
        scroll.addView(container);
        new AlertDialog.Builder(this)
                .setTitle(R.string.customdata_dialog_title)
                .setView(scroll)
                .setPositiveButton(R.string.save, (d, w) -> {
                    for (int i = 0; i < keys.size(); i++) {
                        e.customData.put(keys.get(i), fields.get(i).getText().toString());
                    }
                    e.customDataEdited = true;
                    if (e.customData.containsKey("text")) e.displayText = e.customData.get("text");
                    renderView.invalidate();
                    populateInspector(renderView.getSelectedEntity());
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void confirmDeleteSelected() {
        if (renderView.getSelectedEntity() == null) return;
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete_entity)
                .setPositiveButton(R.string.delete, (d, w) -> renderView.deleteSelected())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showPaletteDialog() {
        if (entFiles == null) entFiles = loadEntFileList();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_entity_palette, null);
        EditText search = dialogView.findViewById(R.id.paletteSearch);
        ListView list = dialogView.findViewById(R.id.paletteList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<>(entFiles));
        list.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.palette_title)
                .setView(dialogView)
                .setNegativeButton(R.string.close, null)
                .create();

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { adapter.getFilter().filter(s); }
            @Override public void afterTextChanged(Editable s) {}
        });

        list.setOnItemClickListener((parent, v, position, id) -> {
            String name = adapter.getItem(position);
            dialog.dismiss();
            if (name != null) renderView.addEntityFromEnt(name);
        });

        dialog.show();
    }

    private List<String> loadEntFileList() {
        List<String> out = new ArrayList<>();
        try {
            String[] files = getAssets().list("entities");
            if (files != null) {
                for (String f : files) if (f.endsWith(".ent")) out.add(f);
                Collections.sort(out);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to list .ent assets", e);
        }
        return out;
    }

    /** Runs pre-save sanity checks and gates the save dialog on the result. Warnings are advisory —
     *  the user can still "Save anyway" (a work-in-progress save is legitimate). Save and Export both
     *  funnel through {@link #showSaveDialog()}, so this single hook covers both. */
    private void attemptSave() {
        List<String> warnings = validateLevel();
        if (warnings.isEmpty()) {
            showSaveDialog();
            return;
        }
        StringBuilder msg = new StringBuilder(getString(R.string.validate_intro));
        for (String w : warnings) msg.append("\n\n•  ").append(w);
        new AlertDialog.Builder(this)
                .setTitle(R.string.validate_title)
                .setMessage(msg.toString())
                .setPositiveButton(R.string.validate_save_anyway, (d, w) -> showSaveDialog())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /** Pre-save sanity checks for a hand-edited level. Returns human-readable warnings (empty = clean).
     *  Magic Rampage needs a player spawn ("spawn0") to start a scene, and an empty scene is almost
     *  always an accident. We deliberately do NOT flag duplicate ids (stock levels reuse ids and the
     *  game tolerates it — saves reconcile by document order, not id) nor out-of-bounds placement
     *  (a scene has no declared bounds in the model). */
    private List<String> validateLevel() {
        List<String> warnings = new ArrayList<>();
        if (currentLevel == null) return warnings;

        List<LevelEntity> entities = currentLevel.entities;
        if (entities == null || entities.isEmpty()) {
            warnings.add(getString(R.string.validate_empty_level));
            return warnings; // nothing else is meaningful on an empty scene
        }

        boolean hasSpawn = false;
        for (LevelEntity e : entities) {
            if (e != null && "spawn0".equals(e.entityName)) { hasSpawn = true; break; }
        }
        if (!hasSpawn) warnings.add(getString(R.string.validate_no_spawn));

        return warnings;
    }

    private void showSaveDialog() {
        if (currentLevel == null) return;
        final EditText input = new EditText(this);
        input.setText(defaultSaveName());
        input.setSelection(input.getText().length());
        new AlertDialog.Builder(this)
                .setTitle(R.string.save_dialog_title)
                .setView(input)
                .setPositiveButton(R.string.save, (d, w) -> doSave(input.getText().toString().trim()))
                .setNeutralButton(R.string.export, (d, w) -> startExport(input.getText().toString().trim()))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private InputStream openSource() throws IOException {
        return (levelPath != null) ? new FileInputStream(levelPath) : getAssets().open("levels/" + levelFile);
    }

    private String defaultSaveName() {
        String base = (levelKey != null ? levelKey : "level").replaceAll("\\.esc$", "");
        // Stock level → suggest an "_edited" copy; an already-saved My Level → overwrite same name.
        return (levelPath != null) ? base : base + "_edited";
    }

    private void doSave(String name) {
        if (currentLevel == null) return;
        if (name == null || name.isEmpty()) name = defaultSaveName();
        if (!name.endsWith(".esc")) name += ".esc";
        File dest = new File(new File(getFilesDir(), "userlevels"), name);
        try (InputStream src = openSource()) {
            LevelSaver.save(src, currentLevel, dest);
            Toast.makeText(this, getString(R.string.save_success, name), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Save failed", e);
            Toast.makeText(this, getString(R.string.save_failed, String.valueOf(e.getMessage())), Toast.LENGTH_LONG).show();
        }
    }

    /** Launches the system file picker so the user chooses where to save the .esc (e.g. Downloads),
     *  then writes it there. From there the user transfers it to the PC's Magic Rampage/scenes/. */
    private void startExport(String name) {
        if (name == null || name.isEmpty()) name = defaultSaveName();
        if (!name.endsWith(".esc")) name += ".esc";
        exportLauncher.launch(name);
    }

    private void writeExport(Uri uri) {
        if (currentLevel == null) return;
        try (InputStream src = openSource();
             OutputStream out = getContentResolver().openOutputStream(uri)) {
            if (out == null) throw new IOException("Could not open the chosen destination");
            LevelSaver.save(src, currentLevel, out);
            Toast.makeText(this, R.string.export_success, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Export failed", e);
            Toast.makeText(this, getString(R.string.export_failed, String.valueOf(e.getMessage())), Toast.LENGTH_LONG).show();
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
                    saveLevelUnlock(levelKey);
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
