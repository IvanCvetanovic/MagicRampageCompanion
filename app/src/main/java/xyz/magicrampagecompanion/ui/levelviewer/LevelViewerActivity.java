package xyz.magicrampagecompanion.ui.levelviewer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import xyz.magicrampagecompanion.level.Level;
import xyz.magicrampagecompanion.level.LevelParser;

public class LevelViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create render view
        LevelRenderView renderView = new LevelRenderView(this);
        setContentView(renderView);

        // Get level file from intent
        String levelFile = getIntent().getStringExtra("levelFile");
        if (levelFile == null) {
            Toast.makeText(this, "No level file provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            // IMPORTANT: path relative to assets/
            Level level = LevelParser.parse(this, "levels/" + levelFile);
            renderView.setLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load level", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
