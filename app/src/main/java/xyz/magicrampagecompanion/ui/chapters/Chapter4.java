package xyz.magicrampagecompanion.ui.chapters;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.ImageButton;

import xyz.magicrampagecompanion.core.utils.LocaleHelper;
import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon31;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon31Bonus;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon32;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon33;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon34;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon35;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon36;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon37;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon38;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon39;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon40;

public class Chapter4 extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter4);

        ImageButton dungeon31 = findViewById(R.id.Dungeon31Button);
        dungeon31.setOnClickListener(v -> { openDungeon31(); playSound(); });

        ImageButton dungeon31bonus = findViewById(R.id.Dungeon31BonusButton);
        dungeon31bonus.setOnClickListener(v -> { openDungeon31bonus(); playSound(); });

        ImageButton dungeon32 = findViewById(R.id.Dungeon32Button);
        dungeon32.setOnClickListener(v -> { openDungeon32(); playSound(); });

        ImageButton dungeon33 = findViewById(R.id.Dungeon33Button);
        dungeon33.setOnClickListener(v -> { openDungeon33(); playSound(); });

        ImageButton dungeon34 = findViewById(R.id.Dungeon34Button);
        dungeon34.setOnClickListener(v -> { openDungeon34(); playSound(); });

        ImageButton dungeon35 = findViewById(R.id.Dungeon35Button);
        dungeon35.setOnClickListener(v -> { openDungeon35(); playSound(); });

        ImageButton dungeon36 = findViewById(R.id.Dungeon36Button);
        dungeon36.setOnClickListener(v -> { openDungeon36(); playSound(); });

        ImageButton dungeon37 = findViewById(R.id.Dungeon37Button);
        dungeon37.setOnClickListener(v -> { openDungeon37(); playSound(); });

        ImageButton dungeon38 = findViewById(R.id.Dungeon38Button);
        dungeon38.setOnClickListener(v -> { openDungeon38(); playSound(); });

        ImageButton dungeon39 = findViewById(R.id.Dungeon39Button);
        dungeon39.setOnClickListener(v -> { openDungeon39(); playSound(); });

        ImageButton dungeon40 = findViewById(R.id.Dungeon40Button);
        dungeon40.setOnClickListener(v -> { openDungeon40(); playSound(); });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Defer init so the first frame can draw before audio setup
        getWindow().getDecorView().post(this::initSoundPoolIfNeeded);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
            if (status == 0 && sampleId == clickSfxId) {
                clickSfxLoaded = true;
            }
        });

        clickSfxId = soundPool.load(this, R.raw.button, 1);
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
        if (soundPool != null && clickSfxLoaded) {
            // Volume 25% on both channels; no loop; normal rate
            soundPool.play(clickSfxId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

    public void openDungeon31() { startActivity(new Intent(this, Dungeon31.class)); }
    public void openDungeon31bonus() { startActivity(new Intent(this, Dungeon31Bonus.class)); }
    public void openDungeon32() { startActivity(new Intent(this, Dungeon32.class)); }
    public void openDungeon33() { startActivity(new Intent(this, Dungeon33.class)); }
    public void openDungeon34() { startActivity(new Intent(this, Dungeon34.class)); }
    public void openDungeon35() { startActivity(new Intent(this, Dungeon35.class)); }
    public void openDungeon36() { startActivity(new Intent(this, Dungeon36.class)); }
    public void openDungeon37() { startActivity(new Intent(this, Dungeon37.class)); }
    public void openDungeon38() { startActivity(new Intent(this, Dungeon38.class)); }
    public void openDungeon39() { startActivity(new Intent(this, Dungeon39.class)); }
    public void openDungeon40() { startActivity(new Intent(this, Dungeon40.class)); }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
