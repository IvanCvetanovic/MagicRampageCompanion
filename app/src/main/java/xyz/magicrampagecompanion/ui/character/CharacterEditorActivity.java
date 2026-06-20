package xyz.magicrampagecompanion.ui.character;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.character.CharacterDocument;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

/**
 * Character editor screen. This phase renders a bundled or saved {@code .character} file
 * read-only (title + each block's fields), built on {@link CharacterDocument}. Editing + save
 * arrive in a later phase. Opened with a {@code characterFile} (asset) or {@code characterPath}
 * (storage) extra, mirroring the level tools.
 */
public class CharacterEditorActivity extends BaseActivity {

    private String characterFile; // asset filename, null when opened from storage
    private String characterPath; // absolute storage path, null when opened from assets

    private TextView titleView;
    private LinearLayout container;
    private CharacterDocument doc;

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

        characterFile = getIntent().getStringExtra("characterFile");
        characterPath = getIntent().getStringExtra("characterPath");
        if (characterFile == null && characterPath == null) {
            Toast.makeText(this, R.string.character_open_failed, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!load()) {
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
                addFieldRow(f.key, doc.value(f.lineIndex));
            }
        }
    }

    private String stem() {
        String n = characterPath != null ? new File(characterPath).getName() : characterFile;
        return n == null ? "" : n.replaceAll("\\.character$", "");
    }

    private static String prettyBlock(String raw) {
        if (raw == null || raw.isEmpty()) return "Character";
        // "equippedItem0" -> "Equipped Item 0"
        String s = raw.replaceAll("([a-z])([A-Z])", "$1 $2").replaceAll("([A-Za-z])(\\d)", "$1 $2");
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

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

    private void addFieldRow(String key, String value) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rlp.bottomMargin = dp(6);
        row.setLayoutParams(rlp);

        TextView keyView = new TextView(this);
        keyView.setText(key);
        keyView.setTextColor(0xFFB0B0B0);
        keyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);

        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setTextColor(0xFFFFFFFF);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);

        row.addView(keyView);
        row.addView(valueView);
        container.addView(row);
    }

    private int dp(int value) {
        return Math.round(getResources().getDisplayMetrics().density * value);
    }
}
