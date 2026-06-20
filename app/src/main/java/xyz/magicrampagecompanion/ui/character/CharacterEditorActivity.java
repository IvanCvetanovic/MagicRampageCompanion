package xyz.magicrampagecompanion.ui.character;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.character.CharacterDocument;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

/**
 * Character editor: renders a {@code .character} file as an editable per-block key/value form
 * backed by {@link CharacterDocument}, and saves an edited copy to
 * {@code filesDir/usercharacters/<name>.character}. Because {@code CharacterDocument} keeps the
 * whole file verbatim and patches only edited value spans, a no-op save is byte-identical and an
 * edit changes exactly its value — no source re-read is needed (unlike the level saver).
 */
public class CharacterEditorActivity extends BaseActivity {

    private static final String DIR = "usercharacters";

    private String characterFile; // asset filename, null when opened from storage
    private String characterPath; // absolute storage path, null when opened from assets

    private TextView titleView;
    private LinearLayout container;
    private CharacterDocument doc;

    private final ActivityResultLauncher<String> exportLauncher =
            registerForActivityResult(new ActivityResultContracts.CreateDocument("application/octet-stream"),
                    uri -> { if (uri != null) writeExport(uri); });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_editor);

        View root = findViewById(R.id.characterEditorRoot);
        final int l = root.getPaddingLeft(), t = root.getPaddingTop(),
                r = root.getPaddingRight(), b = root.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            root.setPadding(l + bars.left, t + bars.top, r + bars.right, b + bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(root);

        titleView = findViewById(R.id.characterEditorTitle);
        container = findViewById(R.id.characterFormContainer);
        Button saveButton = findViewById(R.id.characterSaveButton);
        saveButton.setOnClickListener(v -> { playClick(); showSaveDialog(); });

        characterFile = getIntent().getStringExtra("characterFile");
        characterPath = getIntent().getStringExtra("characterPath");
        if ((characterFile == null && characterPath == null) || !load()) {
            Toast.makeText(this, R.string.character_open_failed, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        render();
    }

    private boolean load() {
        try (InputStream is = (characterPath != null)
                ? new FileInputStream(characterPath)
                : getAssets().open("entities/" + characterFile)) {
            doc = CharacterDocument.parse(is);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void render() {
        String name = doc.firstValue("character", "name");
        titleView.setText(name != null && !name.isEmpty() ? name : stem());

        container.removeAllViews();
        for (CharacterDocument.Block block : doc.blocks()) {
            addSectionHeader(prettyBlock(block.name));
            for (CharacterDocument.Field f : block.fields) {
                addFieldRow(f.lineIndex, f.key, doc.value(f.lineIndex));
            }
        }
    }

    // ── save ─────────────────────────────────────────────────────────────────────────────────

    private void showSaveDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setText(defaultSaveName());
        input.setSelectAllOnFocus(true);

        int pad = dp(20);
        FrameLayoutPad wrap = new FrameLayoutPad(this, pad);
        wrap.addView(input);

        new AlertDialog.Builder(this)
                .setTitle(R.string.character_save_title)
                .setView(wrap)
                .setPositiveButton(R.string.character_save, (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) name = defaultSaveName();
                    save(name);
                })
                .setNeutralButton(R.string.character_export, (d, w) -> startExport(input.getText().toString().trim()))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private String defaultSaveName() {
        // Re-saving a My Characters file keeps its name; a bundled file becomes a "_edited" copy.
        return characterPath != null ? stem() : stem() + "_edited";
    }

    private void save(String name) {
        try {
            File dir = new File(getFilesDir(), DIR);
            if (!dir.exists() && !dir.mkdirs()) throw new Exception("mkdir failed");
            File out = new File(dir, name.replaceAll("\\.character$", "") + ".character");
            try (FileOutputStream fos = new FileOutputStream(out)) {
                fos.write(doc.toBytes());
            }
            // Subsequent saves overwrite this copy by default.
            characterPath = out.getAbsolutePath();
            characterFile = null;
            Toast.makeText(this, getString(R.string.character_saved, out.getName()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.character_save_failed, Toast.LENGTH_SHORT).show();
        }
    }

    /** Launch the system file picker to export the .character to a user-accessible location. */
    private void startExport(String name) {
        if (name == null || name.isEmpty()) name = defaultSaveName();
        if (!name.endsWith(".character")) name += ".character";
        exportLauncher.launch(name);
    }

    private void writeExport(Uri uri) {
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            if (out == null) throw new Exception("Could not open the chosen destination");
            out.write(doc.toBytes());
            Toast.makeText(this, R.string.character_export_success, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.character_save_failed, Toast.LENGTH_LONG).show();
        }
    }

    // ── form building ──────────────────────────────────────────────────────────────────────────

    private void addSectionHeader(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        tv.setTypeface(tv.getTypeface(), android.graphics.Typeface.BOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(16);
        lp.bottomMargin = dp(4);
        tv.setLayoutParams(lp);
        container.addView(tv);
    }

    private void addFieldRow(final int lineIndex, String key, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rlp.bottomMargin = dp(8);
        row.setLayoutParams(rlp);

        TextView keyView = new TextView(this);
        keyView.setText(key);
        keyView.setTextColor(0xFFB0B0B0);
        keyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

        final EditText valueView = new EditText(this);
        valueView.setText(value);
        valueView.setTextColor(0xFFFFFFFF);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        valueView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        valueView.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                doc.setValue(lineIndex, s.toString());
            }
        });

        row.addView(keyView);
        row.addView(valueView);
        container.addView(row);
    }

    private String stem() {
        String n = characterPath != null ? new File(characterPath).getName() : characterFile;
        return n == null ? "character" : n.replaceAll("\\.character$", "");
    }

    private static String prettyBlock(String raw) {
        if (raw == null || raw.isEmpty()) return "Character";
        String s = raw.replaceAll("([a-z])([A-Z])", "$1 $2").replaceAll("([A-Za-z])(\\d)", "$1 $2");
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private int dp(int value) {
        return Math.round(getResources().getDisplayMetrics().density * value);
    }

    /** Tiny padded FrameLayout so the dialog EditText isn't flush against the edges. */
    private static final class FrameLayoutPad extends android.widget.FrameLayout {
        FrameLayoutPad(android.content.Context c, int pad) { super(c); setPadding(pad, pad / 2, pad, 0); }
    }
}
