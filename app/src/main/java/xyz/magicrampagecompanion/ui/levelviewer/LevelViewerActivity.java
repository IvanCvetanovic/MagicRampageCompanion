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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        ImageButton btnEntityList = findViewById(R.id.btnEntityList);
        btnEntityList.setOnClickListener(v -> { playClick(); showEntityBrowser(); });

        ImageButton btnMarquee = findViewById(R.id.btnMarquee);
        btnMarquee.setAlpha(0.4f);
        btnMarquee.setOnClickListener(v -> {
            playClick();
            boolean on = !renderView.isMarqueeMode();
            renderView.setMarqueeMode(on);
            btnMarquee.setAlpha(on ? 1.0f : 0.4f);
            Toast.makeText(this, on ? R.string.marquee_on : R.string.marquee_off, Toast.LENGTH_SHORT).show();
        });

        renderView.setOnMultiSelectionChangedListener(count -> {
            if (count > 1) Toast.makeText(this, getString(R.string.entities_selected, count), Toast.LENGTH_SHORT).show();
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
            btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
            btnEntityList.setVisibility(enable ? View.VISIBLE : View.GONE);
            btnMarquee.setVisibility(enable ? View.VISIBLE : View.GONE);
            if (!enable) { renderView.setMarqueeMode(false); btnMarquee.setAlpha(0.4f); }
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
        boolean single = e != null;
        boolean group = renderView.hasMultiSelection();
        boolean dup = single;            // duplicate stays single-only in v1
        boolean del = single || group;   // delete also works on a group selection
        if (btnDuplicateEntity != null) { btnDuplicateEntity.setEnabled(dup); btnDuplicateEntity.setAlpha(dup ? 1f : 0.4f); }
        if (btnDeleteEntity != null) { btnDeleteEntity.setEnabled(del); btnDeleteEntity.setAlpha(del ? 1f : 0.4f); }
    }

    private void updateUndoRedoButtons() {
        if (btnUndo != null) { boolean u = renderView.canUndo(); btnUndo.setEnabled(u); btnUndo.setAlpha(u ? 1f : 0.4f); }
        if (btnRedo != null) { boolean r = renderView.canRedo(); btnRedo.setEnabled(r); btnRedo.setAlpha(r ? 1f : 0.4f); }
    }

    /** Holds a CustomData editor row: its key, the value field, and the row view (for removal). */
    private static final class CdRow {
        final String key; final EditText field; final View view;
        CdRow(String key, EditText field, View view) { this.key = key; this.field = field; this.view = view; }
    }

    /** Edits the selected entity's CustomData — the data that drives gameplay (enemy lists, hp, text,
     *  drop chances, ...). Values are editable; fields can be removed (✕) or added (with an engine type).
     *  Persisted on save via customDataEdited and undoable via a single command. */
    private void showCustomDataDialog(LevelEntity e) {
        if (e.customData == null) return;

        // Snapshot BEFORE for undo.
        final Map<String, String> beforeData = new LinkedHashMap<>(e.customData);
        final Map<String, String> beforeTypes = new LinkedHashMap<>(e.customDataTypes);
        final boolean beforeEdited = e.customDataEdited;
        final String beforeText = e.displayText;

        final int pad = (int) (16 * getResources().getDisplayMetrics().density);
        final List<CdRow> rows = new ArrayList<>();
        final Map<String, String> newTypes = new LinkedHashMap<>();   // types for keys added in this session

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(pad, pad / 2, pad, 0);

        final LinearLayout rowsArea = new LinearLayout(this);
        rowsArea.setOrientation(LinearLayout.VERTICAL);
        container.addView(rowsArea);

        List<String> keys = new ArrayList<>(e.customData.keySet());
        Collections.sort(keys);
        for (String k : keys) addCustomDataRow(rowsArea, rows, k, e.customData.get(k));

        android.widget.Button addBtn = new android.widget.Button(this);
        addBtn.setText(R.string.cd_add_field);
        addBtn.setOnClickListener(v -> { playClick(); showAddFieldDialog(rowsArea, rows, newTypes); });
        container.addView(addBtn);

        android.widget.ScrollView scroll = new android.widget.ScrollView(this);
        scroll.addView(container);
        new AlertDialog.Builder(this)
                .setTitle(R.string.customdata_dialog_title)
                .setView(scroll)
                .setPositiveButton(R.string.save, (d, w) -> {
                    Map<String, String> after = new LinkedHashMap<>();
                    for (CdRow r : rows) after.put(r.key, r.field.getText().toString());
                    e.customData.clear();
                    e.customData.putAll(after);
                    e.customDataTypes.putAll(newTypes);
                    e.customDataTypes.keySet().retainAll(e.customData.keySet());   // drop types for removed keys
                    e.customDataEdited = true;
                    e.displayText = e.customData.containsKey("text") ? e.customData.get("text") : "";

                    final Map<String, String> afterData = new LinkedHashMap<>(e.customData);
                    final Map<String, String> afterTypes = new LinkedHashMap<>(e.customDataTypes);
                    final String afterText = e.displayText;
                    if (!afterData.equals(beforeData) || !afterTypes.equals(beforeTypes)) {
                        renderView.pushCommand(new LevelRenderView.EditCommand() {
                            @Override public void undo() {
                                e.customData.clear(); e.customData.putAll(beforeData);
                                e.customDataTypes.clear(); e.customDataTypes.putAll(beforeTypes);
                                e.customDataEdited = beforeEdited; e.displayText = beforeText;
                            }
                            @Override public void redo() {
                                e.customData.clear(); e.customData.putAll(afterData);
                                e.customDataTypes.clear(); e.customDataTypes.putAll(afterTypes);
                                e.customDataEdited = true; e.displayText = afterText;
                            }
                        });
                    }
                    renderView.invalidate();
                    populateInspector(renderView.getSelectedEntity());
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /** Adds one CustomData editor row (key label + value field + ✕ remove) to the dialog. */
    private void addCustomDataRow(LinearLayout rowsArea, List<CdRow> rows, String key, String value) {
        LinearLayout block = new LinearLayout(this);
        block.setOrientation(LinearLayout.VERTICAL);

        TextView label = new TextView(this);
        label.setText(key);
        label.setTextColor(getColor(R.color.color_text_primary));
        label.setTextSize(12f);
        block.addView(label);

        LinearLayout line = new LinearLayout(this);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(android.view.Gravity.CENTER_VERTICAL);

        EditText field = new EditText(this);
        field.setText(value);
        field.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        line.addView(field);

        // P8: a spawner's "mobs" list references characters by path — let the user insert one of
        // their saved (My Characters) files without hand-typing the npcs/<category>/ path.
        if ("mobs".equalsIgnoreCase(key)) {
            android.widget.Button addChar = new android.widget.Button(this);
            addChar.setText(R.string.cd_add_my_character);
            addChar.setTextSize(11f);
            addChar.setMinWidth(0);
            addChar.setMinimumWidth(0);
            int hp = (int) (8 * getResources().getDisplayMetrics().density);
            addChar.setPadding(hp, 0, hp, 0);
            addChar.setOnClickListener(v -> { playClick(); pickMyCharacterInto(field); });
            line.addView(addChar);
        }

        android.widget.ImageButton remove = new android.widget.ImageButton(this);
        remove.setImageResource(android.R.drawable.ic_menu_delete);
        remove.setBackgroundResource(0);
        remove.setContentDescription(getString(R.string.cd_remove_field));
        line.addView(remove);

        block.addView(line);
        rowsArea.addView(block);

        CdRow row = new CdRow(key, field, block);
        rows.add(row);
        remove.setOnClickListener(v -> { playClick(); rows.remove(row); rowsArea.removeView(block); });
    }

    /** P8: pick a saved (My Characters) file and append its reference to a spawner's mobs list. */
    private void pickMyCharacterInto(EditText field) {
        File dir = new File(getFilesDir(), "usercharacters");
        File[] files = dir.listFiles((d, n) -> n.endsWith(".character"));
        if (files == null || files.length == 0) {
            Toast.makeText(this, R.string.cd_no_my_characters, Toast.LENGTH_LONG).show();
            return;
        }
        java.util.Arrays.sort(files, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        final String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) names[i] = files[i].getName().replaceAll("\\.character$", "");
        new AlertDialog.Builder(this)
                .setTitle(R.string.cd_pick_my_character)
                .setItems(names, (d, which) -> {
                    // Spawner mobs default to enemies; the path stays editable for ally/boss/fighter.
                    String ref = "npcs/enemies/" + names[which] + ".character,default";
                    String cur = field.getText().toString().trim();
                    String next = cur.isEmpty() ? ref : (cur.endsWith(";") ? cur + ref : cur + ";" + ref);
                    field.setText(next);
                    Toast.makeText(this, getString(R.string.cd_my_character_added, names[which]), Toast.LENGTH_LONG).show();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    /** Sub-dialog to add a new CustomData field: name + engine type + value. */
    private void showAddFieldDialog(LinearLayout rowsArea, List<CdRow> rows, Map<String, String> newTypes) {
        final int pad = (int) (16 * getResources().getDisplayMetrics().density);
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(pad, pad / 2, pad, 0);

        EditText nameF = new EditText(this);
        nameF.setHint(R.string.cd_field_name);
        nameF.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        c.addView(nameF);

        android.widget.RadioGroup types = new android.widget.RadioGroup(this);
        types.setOrientation(android.widget.RadioGroup.HORIZONTAL);
        String[] typeOpts = {"string", "int", "uint", "float"};
        for (int i = 0; i < typeOpts.length; i++) {
            android.widget.RadioButton rb = new android.widget.RadioButton(this);
            rb.setId(i + 1);
            rb.setText(typeOpts[i]);
            types.addView(rb);
        }
        types.check(1);
        c.addView(types);

        EditText valF = new EditText(this);
        valF.setHint(R.string.cd_field_value);
        c.addView(valF);

        new AlertDialog.Builder(this)
                .setTitle(R.string.cd_add_field)
                .setView(c)
                .setPositiveButton(R.string.add, (d, w) -> {
                    String name = nameF.getText().toString().trim();
                    if (name.isEmpty()) { Toast.makeText(this, R.string.cd_field_name_empty, Toast.LENGTH_SHORT).show(); return; }
                    for (CdRow r : rows) if (r.key.equals(name)) { Toast.makeText(this, R.string.cd_field_exists, Toast.LENGTH_SHORT).show(); return; }
                    String type = "string";
                    android.widget.RadioButton checked = types.findViewById(types.getCheckedRadioButtonId());
                    if (checked != null) type = checked.getText().toString();
                    newTypes.put(name, type);
                    addCustomDataRow(rowsArea, rows, name, valF.getText().toString());
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void confirmDeleteSelected() {
        if (renderView.hasMultiSelection()) {
            int count = renderView.getMultiSelectionCount();
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.confirm_delete_group, count))
                    .setPositiveButton(R.string.delete, (d, w) -> renderView.deleteSelectedGroup())
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return;
        }
        if (renderView.getSelectedEntity() == null) return;
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete_entity)
                .setPositiveButton(R.string.delete, (d, w) -> renderView.deleteSelected())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showPaletteDialog() {
        if (entFiles == null) entFiles = loadEntFileList();
        if (spawnerTemplates == null) spawnerTemplates = loadSpawnerTemplates();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_entity_palette, null);
        EditText search = dialogView.findViewById(R.id.paletteSearch);
        ListView list = dialogView.findViewById(R.id.paletteList);

        if (thumbExecutor == null || thumbExecutor.isShutdown()) {
            thumbExecutor = Executors.newFixedThreadPool(3);
        }
        // Inline-template entities (spawners / buttons / invisible collision) first, then FileName .ent files.
        List<String> items = new ArrayList<>(spawnerTemplates.keySet());
        items.addAll(entFiles);
        PaletteAdapter adapter = new PaletteAdapter(items);
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
            if (name == null) return;
            String template = spawnerTemplates.get(name);
            if (template != null) renderView.addSpawnerTemplate(template);
            else renderView.addEntityFromEnt(name);
        });

        dialog.setOnDismissListener(d -> shutdownThumbExecutor());
        dialog.show();
    }

    // ── Palette thumbnails ──────────────────────────────────────────────────────
    private static final int THUMB_TARGET_PX = 120;
    // Resolved sprite previews for the Add-entity palette, bounded by an LruCache (~8 MB).
    private final LruCache<String, Bitmap> thumbCache =
            new LruCache<String, Bitmap>(8 * 1024 * 1024) {
                @Override protected int sizeOf(String key, Bitmap value) { return value.getByteCount(); }
            };
    // .ent files that resolve to no sprite — remembered so we don't keep re-decoding them.
    private final Set<String> thumbMisses = Collections.synchronizedSet(new HashSet<>());
    private ExecutorService thumbExecutor;
    private final Handler thumbHandler = new Handler(Looper.getMainLooper());

    private void shutdownThumbExecutor() {
        if (thumbExecutor != null) { thumbExecutor.shutdownNow(); thumbExecutor = null; }
    }

    /** ListView adapter for the Add-entity palette: each row shows the entity's sprite thumbnail
     *  (resolved + cached off the UI thread) plus its name. Extends ArrayAdapter so the search
     *  field's built-in filtering keeps working on the .ent filenames. */
    private final class PaletteAdapter extends ArrayAdapter<String> {
        PaletteAdapter(List<String> data) { super(LevelViewerActivity.this, 0, data); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = (convertView != null) ? convertView
                    : getLayoutInflater().inflate(R.layout.item_palette_entry, parent, false);
            ImageView thumb = row.findViewById(R.id.paletteItemThumb);
            TextView label = row.findViewById(R.id.paletteItemName);

            String file = getItem(position);
            label.setText(displayName(file));
            thumb.setTag(file);

            Bitmap cached = (file != null) ? thumbCache.get(file) : null;
            if (cached != null) {
                thumb.setImageBitmap(cached);
            } else {
                thumb.setImageDrawable(null);
                if (file != null && !file.startsWith(TEMPLATE_MARK) && !thumbMisses.contains(file)) loadThumbAsync(file, thumb);
            }
            return row;
        }
    }

    private void loadThumbAsync(final String file, final ImageView target) {
        final ExecutorService exec = thumbExecutor;
        if (exec == null || exec.isShutdown()) return;
        exec.execute(() -> {
            Bitmap bmp = null;
            try { bmp = resolveThumbnail(file); } catch (Exception ignored) {}
            if (bmp == null) { thumbMisses.add(file); return; }
            final Bitmap result = bmp;
            thumbCache.put(file, result);
            thumbHandler.post(() -> { if (file.equals(target.getTag())) target.setImageBitmap(result); });
        });
    }

    /** Resolves a bundled .ent file to a small sprite thumbnail. Self-contained — it does NOT touch
     *  the render view's sprite cache, so it is safe to run off the UI thread: parse the .ent for its
     *  sprite, decode that PNG downsampled, and crop frame 0 from any uniform sprite sheet. Returns
     *  null when the .ent has no direct sprite (e.g. composed NPCs resolved at the level layer). */
    private Bitmap resolveThumbnail(String entFileName) {
        LevelEntity tmp = new LevelEntity();
        tmp.entityName = entFileName;
        LevelParser.parseEntFile(this, tmp, entFileName);

        String sprite = (tmp.spriteFile == null) ? "" : tmp.spriteFile.trim();
        if (sprite.isEmpty()) return null;
        if (!sprite.endsWith(".png")) sprite += ".png";
        String assetPath = "entities/" + sprite;

        // Bounds pass → pick a downsample that keeps one frame near THUMB_TARGET_PX.
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        try (InputStream is = getAssets().open(assetPath)) {
            BitmapFactory.decodeStream(is, null, bounds);
        } catch (IOException e) { return null; }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null;

        int cols = (tmp.spriteCutX > 0) ? tmp.spriteCutX : 1;
        int rows = (tmp.spriteCutY > 0) ? tmp.spriteCutY : 1;
        int frameW = Math.max(1, bounds.outWidth / cols);
        int frameH = Math.max(1, bounds.outHeight / rows);

        int sample = 1;
        int maxFrame = Math.max(frameW, frameH);
        while (maxFrame / (sample * 2) >= THUMB_TARGET_PX) sample *= 2;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = sample;
        Bitmap full;
        try (InputStream is = getAssets().open(assetPath)) {
            full = BitmapFactory.decodeStream(is, null, opts);
        } catch (IOException e) { return null; }
        if (full == null) return null;

        if (cols <= 1 && rows <= 1) return full;

        int fw = Math.max(1, full.getWidth() / cols);
        int fh = Math.max(1, full.getHeight() / rows);
        if (fw >= full.getWidth() && fh >= full.getHeight()) return full;
        Bitmap frame = Bitmap.createBitmap(full, 0, 0, fw, fh);
        if (frame != full) full.recycle();
        return frame;
    }

    /** Human-readable palette label: drop the .ent extension and turn underscores into spaces. */
    private String displayName(String entFile) {
        if (entFile == null) return "";
        String n = entFile.endsWith(".ent") ? entFile.substring(0, entFile.length() - 4) : entFile;
        return n.replace('_', ' ');
    }

    // ── Inline "spawner" templates (harvested real blocks bundled under assets/spawner_templates) ──
    private static final String TEMPLATE_MARK = "★ ";   // prefixes template labels in the palette list
    private Map<String, String> spawnerTemplates;       // palette label → raw <Entity> block XML

    /** Loads the bundled inline-entity templates: label = "★ <name>", value = the raw <Entity> XML. */
    private Map<String, String> loadSpawnerTemplates() {
        Map<String, String> out = new LinkedHashMap<>();
        try {
            String[] files = getAssets().list("spawner_templates");
            if (files != null) {
                java.util.Arrays.sort(files);
                for (String f : files) {
                    if (!f.endsWith(".xml")) continue;
                    String label = TEMPLATE_MARK + f.substring(0, f.length() - 4);
                    try (InputStream is = getAssets().open("spawner_templates/" + f)) {
                        out.put(label, readAll(is));
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to load spawner templates", e);
        }
        return out;
    }

    private static String readAll(InputStream is) throws IOException {
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
        return new String(bos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
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

    // ── Entity browser: searchable list of every entity in the level → select + center on it ──
    /** Opens a searchable list of all entities in the level. Tapping one selects it and pans the view
     *  to center it — far easier than hunting for a specific entity by tap among hundreds. */
    private void showEntityBrowser() {
        List<LevelEntity> entities = renderView.getEntities();
        if (entities.isEmpty()) {
            Toast.makeText(this, R.string.entity_browser_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_entity_palette, null);
        EditText search = dialogView.findViewById(R.id.paletteSearch);
        ListView list = dialogView.findViewById(R.id.paletteList);

        List<EntityRef> refs = new ArrayList<>();
        for (LevelEntity e : entities) refs.add(new EntityRef(e));
        ArrayAdapter<EntityRef> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, refs);
        list.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.entity_browser_title)
                .setView(dialogView)
                .setNegativeButton(R.string.close, null)
                .create();

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { adapter.getFilter().filter(s); }
            @Override public void afterTextChanged(Editable s) {}
        });

        list.setOnItemClickListener((parent, v, position, id) -> {
            EntityRef ref = adapter.getItem(position);
            dialog.dismiss();
            if (ref != null) renderView.selectAndCenter(ref.entity);
        });

        dialog.show();
    }

    /** Wraps a level entity with a searchable label (name · id · position) for the browser list. */
    private static final class EntityRef {
        final LevelEntity entity;
        private final String label;
        EntityRef(LevelEntity e) {
            this.entity = e;
            String name = (e.entityName == null || e.entityName.isEmpty()) ? ("id " + e.id) : e.entityName;
            this.label = name + "   ·   id " + e.id + "   @ " + (int) e.x + ", " + (int) e.y;
        }
        @Override public String toString() { return label; }
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
