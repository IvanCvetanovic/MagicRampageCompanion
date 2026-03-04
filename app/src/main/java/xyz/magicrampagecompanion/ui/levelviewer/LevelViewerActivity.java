package xyz.magicrampagecompanion.ui.levelviewer;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.level.Level;
import xyz.magicrampagecompanion.level.LevelParser;

public class LevelViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_viewer);

        String levelFile = getIntent().getStringExtra("levelFile");
        if (levelFile == null) {
            Toast.makeText(this, "No level file provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
            return WindowInsetsCompat.CONSUMED;
        });

        TextView title = findViewById(R.id.levelViewerTitle);
        title.setText(levelFile.replace(".esc", ""));

        ImageButton btnBack = findViewById(R.id.btnLevelBack);
        btnBack.setOnClickListener(v -> finish());

        // --- Parse and display level ---
        LevelRenderView renderView = findViewById(R.id.levelRenderView);

        try {
            Level level = LevelParser.parse(this, "levels/" + levelFile);
            renderView.setLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load level: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
