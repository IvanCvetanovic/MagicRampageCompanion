package xyz.magicrampagecompanion.ui.survival;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class SurvivalModeSelection extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survival_mode_selection);

        // ---- Apply system bar insets (status bar / camera cutout / nav bar) ----
        View scroll = findViewById(R.id.survivalScroll);
        View content = findViewById(R.id.survivalContentRoot);
        applySystemInsets(scroll, content);

        bindDungeonButton(R.id.SurvivalButton1, R.layout.activity_survival_dungeon1);
        bindDungeonButton(R.id.SurvivalButton2, R.layout.activity_survival_dungeon2);
        bindDungeonButton(R.id.SurvivalButton3, R.layout.activity_survival_dungeon3);
        bindDungeonButton(R.id.SurvivalButton4, R.layout.activity_survival_dungeon4);
        bindDungeonButton(R.id.SurvivalButton5, R.layout.activity_survival_dungeon5);
        bindDungeonButton(R.id.SurvivalButton6, R.layout.activity_survival_dungeon6);
    }

    @Override
    protected int clickSoundRes() {
        return R.raw.button;
    }

    // ---- Edge-to-edge helper (mirrors MainActivity, with safe fallbacks) ----
    private void applySystemInsets(View scrollViewOrNull, View contentRootOrNull) {
        // Fallbacks if IDs aren’t present
        final View root = findViewById(android.R.id.content);
        final View contentRoot = (contentRootOrNull != null) ? contentRootOrNull : root;
        final View scrollView   = (scrollViewOrNull   != null) ? scrollViewOrNull   : contentRoot;

        final int baseScrollLeft   = scrollView.getPaddingLeft();
        final int baseScrollTop    = scrollView.getPaddingTop();
        final int baseScrollRight  = scrollView.getPaddingRight();
        final int baseScrollBottom = scrollView.getPaddingBottom();

        final int baseContentLeft   = contentRoot.getPaddingLeft();
        final int baseContentTop    = contentRoot.getPaddingTop();
        final int baseContentRight  = contentRoot.getPaddingRight();
        final int baseContentBottom = contentRoot.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Push the first touchable pixel below the status bar / camera cutout
            contentRoot.setPadding(
                    baseContentLeft,
                    baseContentTop + bars.top,
                    baseContentRight,
                    baseContentBottom
            );

            // Keep the last item above the gesture nav bar
            scrollView.setPadding(
                    baseScrollLeft,
                    baseScrollTop,
                    baseScrollRight,
                    baseScrollBottom + bars.bottom
            );

            return insets;
        });

        ViewCompat.requestApplyInsets(root);
    }
}
