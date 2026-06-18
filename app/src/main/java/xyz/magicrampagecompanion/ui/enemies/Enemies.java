package xyz.magicrampagecompanion.ui.enemies;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.data.models.Enemy;
import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.adapters.ImageAdapter;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class Enemies extends BaseActivity {
    public static final String EXTRA_ENEMY = "xyz.magicrampagecompanion.EXTRA_ENEMY";

    private RecyclerView recyclerView;
    private EditText searchEdit;
    private TextView emptyStateText;

    // Full, unfiltered enemy list (populated upstream by ItemData.init in MainActivity).
    private List<Enemy> fullList = new ArrayList<>();

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
        recyclerView.setClipToPadding(false);

        searchEdit = findViewById(R.id.searchEdit);
        emptyStateText = findViewById(R.id.emptyStateText);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { applyFilter(s.toString()); }
        });

        fullList = ItemData.enemyList != null ? ItemData.enemyList : new ArrayList<>();
        applyFilter("");
    }

    /** Filters the enemy grid by name and refreshes the empty-state. */
    private void applyFilter(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        List<Enemy> filtered = new ArrayList<>();
        for (Enemy e : fullList) {
            String name = e.getName() != null ? e.getName().toLowerCase() : "";
            if (q.isEmpty() || name.contains(q)) filtered.add(e);
        }

        ImageAdapter<Enemy> adapter = new ImageAdapter<>(filtered, (view, position) -> {
            playClick();
            Enemy selected = filtered.get(position);
            Intent intent = new Intent(Enemies.this, EnemyDetail.class);
            intent.putExtra(EXTRA_ENEMY, selected);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        boolean empty = filtered.isEmpty();
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        emptyStateText.setVisibility(empty ? View.VISIBLE : View.GONE);
    }
}
