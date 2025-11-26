package xyz.magicrampagecompanion.ui.enemies;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import java.util.List;

import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.adapters.ImageAdapter;

public class Enemies extends AppCompatActivity {
    public static final String EXTRA_ENEMY = "xyz.magicrampagecompanion.EXTRA_ENEMY";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enemies);

        final View root = findViewById(R.id.enemies_root);
        // Save base paddings so we don't accumulate on re-applies
        final int baseL = root.getPaddingLeft();
        final int baseT = root.getPaddingTop();
        final int baseR = root.getPaddingRight();
        final int baseB = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Push content below status bar / cutout and above nav bar
            root.setPadding(baseL, baseT + bars.top, baseR, baseB + bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(root);

        recyclerView = findViewById(R.id.recyclerViewEnemies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setClipToPadding(false); // keep last row visible above nav inset

        loadEnemies(ItemData.enemyList);
    }

    private <T extends Parcelable> void loadEnemies(List<T> items) {
        ImageAdapter<T> adapter = new ImageAdapter<>(items, (view, position) -> {
            T selected = items.get(position);
            Intent intent = new Intent(Enemies.this, EnemyDetail.class);
            intent.putExtra(EXTRA_ENEMY, selected);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}
