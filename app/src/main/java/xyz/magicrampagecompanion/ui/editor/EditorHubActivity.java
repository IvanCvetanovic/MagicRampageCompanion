package xyz.magicrampagecompanion.ui.editor;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.character.CharacterListActivity;
import xyz.magicrampagecompanion.ui.common.BaseActivity;
import xyz.magicrampagecompanion.ui.levelviewer.LevelListActivity;

/**
 * Landing screen for the "Editor" menu entry: two large cinematic image cards (Levels / Characters),
 * each a composited in-game scene from {@link HubSceneBuilder} (dungeon backdrop, god-rays, glow,
 * character roster, torches, embers, vignette), with a gradient scrim, a bold overlaid title, an
 * accent glowing border, and a slow Ken-Burns zoom so the scenes feel alive.
 */
public class EditorHubActivity extends BaseActivity {

    private final List<ValueAnimator> animators = new ArrayList<>();

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

    /** Build the two card scenes off the UI thread, set them crisp, then animate them. */
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
                kenBurns(levelsImg, 9000);   // staggered so the two cards drift out of sync
                kenBurns(charsImg, 11000);
            });
        }).start();
    }

    private void setCrisp(ImageView view, Bitmap bmp) {
        if (bmp == null) return;
        BitmapDrawable d = new BitmapDrawable(getResources(), bmp);
        d.setFilterBitmap(false); // crisp nearest-neighbour when the scene is scaled to fill
        view.setImageDrawable(d);
    }

    /** Slow, looping zoom (1.0 → 1.06) for a living, cinematic feel. */
    private void kenBurns(ImageView view, long durationMs) {
        ValueAnimator a = ValueAnimator.ofFloat(1f, 1.06f);
        a.setDuration(durationMs);
        a.setRepeatMode(ValueAnimator.REVERSE);
        a.setRepeatCount(ValueAnimator.INFINITE);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.addUpdateListener(an -> {
            float s = (float) an.getAnimatedValue();
            view.setScaleX(s);
            view.setScaleY(s);
        });
        a.start();
        animators.add(a);
    }

    @Override
    protected void onDestroy() {
        for (ValueAnimator a : animators) a.cancel();
        animators.clear();
        super.onDestroy();
    }
}
