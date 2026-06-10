package xyz.magicrampagecompanion.ui.dungeons;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LayoutRes;

import xyz.magicrampagecompanion.ui.common.BaseActivity;

/**
 * Displays a single dungeon guide layout. Replaces the former one-activity-per-
 * dungeon classes (Dungeon1..Dungeon44, bonus dungeons, survival dungeons),
 * which only differed in the layout they inflated.
 */
public class DungeonActivity extends BaseActivity {

    public static final String EXTRA_LAYOUT_RES = "layoutRes";

    public static Intent intentFor(Context context, @LayoutRes int layoutRes) {
        return new Intent(context, DungeonActivity.class)
                .putExtra(EXTRA_LAYOUT_RES, layoutRes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        int layoutRes = getIntent().getIntExtra(EXTRA_LAYOUT_RES, 0);
        if (layoutRes == 0) {
            finish();
            return;
        }
        setContentView(layoutRes);
    }
}
