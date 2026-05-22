package xyz.magicrampagecompanion.ui.enemies;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.adapters.ImageAdapter;

public class Enemies extends AppCompatActivity {
    public static final String EXTRA_ENEMY = "xyz.magicrampagecompanion.EXTRA_ENEMY";

    private RecyclerView recyclerView;
    private TextView emptyStateText;

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().post(this::initSoundPoolIfNeeded);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseSoundPool();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSoundPool();
    }

    private void initSoundPoolIfNeeded() {
        if (soundPool != null) return;
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build())
                .build();
        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0 && sampleId == clickSfxId) clickSfxLoaded = true;
        });
        clickSfxId = soundPool.load(this, R.raw.click, 1);
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            clickSfxLoaded = false;
            clickSfxId = 0;
        }
    }

    private void playSound() {
        if (soundPool != null && clickSfxLoaded)
            soundPool.play(clickSfxId, 0.25f, 0.25f, 1, 0, 1.0f);
    }

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

        emptyStateText = findViewById(R.id.emptyStateText);

        loadEnemies(ItemData.enemyList);
    }

    private <T extends Parcelable> void loadEnemies(List<T> items) {
        ImageAdapter<T> adapter = new ImageAdapter<>(items, (view, position) -> {
            playSound();
            T selected = items.get(position);
            Intent intent = new Intent(Enemies.this, EnemyDetail.class);
            intent.putExtra(EXTRA_ENEMY, selected);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        boolean empty = items == null || items.isEmpty();
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        emptyStateText.setVisibility(empty ? View.VISIBLE : View.GONE);
    }
}
