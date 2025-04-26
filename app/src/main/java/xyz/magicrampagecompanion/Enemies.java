package xyz.magicrampagecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Enemies extends AppCompatActivity {
    public static final String EXTRA_ENEMY = "xyz.magicrampagecompanion.EXTRA_ENEMY";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemies);

        recyclerView = findViewById(R.id.recyclerViewEnemies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // ← use your existing ItemData.enemyList
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
