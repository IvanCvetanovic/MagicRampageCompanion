package xyz.magicrampagecompanion.ui.editor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.character.CharacterListActivity;
import xyz.magicrampagecompanion.ui.common.BaseActivity;
import xyz.magicrampagecompanion.ui.levelviewer.LevelListActivity;

/**
 * Landing screen for the "Editor" menu entry: two large image cards (Levels / Characters), each a
 * composited in-game scene built by {@link HubSceneBuilder} (dungeon backdrop + glow + characters),
 * with a gradient scrim and a bold overlaid title. View vs. edit stays a mode inside each tool.
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

        loadCardArt();
    }

    /** Build the two card scenes off the UI thread and set them crisp (no blur). */
    private void loadCardArt() {
        final ImageView levelsImg = findViewById(R.id.editorLevelsImage);
        final ImageView charsImg = findViewById(R.id.editorCharactersImage);
        new Thread(() -> {
            final Bitmap levels = HubSceneBuilder.buildLevelsScene(this);
            final Bitmap chars = HubSceneBuilder.buildCharactersScene(this);
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                setCrisp(levelsImg, levels);
                setCrisp(charsImg, chars);
            });
        }).start();
    }

    private void setCrisp(ImageView view, Bitmap bmp) {
        if (bmp == null) return;
        BitmapDrawable d = new BitmapDrawable(getResources(), bmp);
        d.setFilterBitmap(false); // crisp nearest-neighbour when the scene is scaled to fill
        view.setImageDrawable(d);
    }
}
