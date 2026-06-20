package xyz.magicrampagecompanion.ui.editor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.character.CharacterListActivity;
import xyz.magicrampagecompanion.ui.common.BaseActivity;
import xyz.magicrampagecompanion.ui.levelviewer.LevelListActivity;

/**
 * Landing screen for the renamed "Editor" menu entry: a two-card chooser that routes to the
 * existing Level tools or the new Character tools. View vs. edit stays a mode <i>inside</i> each
 * tool (the level viewer's pencil toggle; the character form), so it is not gated here.
 */
public class EditorHubActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_hub);

        View root = findViewById(R.id.editorHubRoot);
        final int l = root.getPaddingLeft(), t = root.getPaddingTop(),
                r = root.getPaddingRight(), b = root.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            root.setPadding(l + bars.left, t + bars.top, r + bars.right, b + bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(root);

        findViewById(R.id.editorLevelsButton).setOnClickListener(v -> {
            playClick();
            startActivity(new Intent(this, LevelListActivity.class));
        });
        findViewById(R.id.editorCharactersButton).setOnClickListener(v -> {
            playClick();
            startActivity(new Intent(this, CharacterListActivity.class));
        });
    }
}
