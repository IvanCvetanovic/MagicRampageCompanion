package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SurvivalDungeon4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survival_dungeon4);

        // Apply edge-to-edge system insets for status & nav bars
        View scroll = findViewById(R.id.survivalScroll);
        View content = findViewById(R.id.survivalContentRoot);
        applySystemInsets(scroll, content);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    // Reusable helper for safe-area handling
    private void applySystemInsets(View scrollViewOrNull, View contentRootOrNull) {
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

            // Push content below status bar / cutout
            contentRoot.setPadding(
                    baseContentLeft,
                    baseContentTop + bars.top,
                    baseContentRight,
                    baseContentBottom
            );

            // Keep scrollable content above gesture nav bar
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
