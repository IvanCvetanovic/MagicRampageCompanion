package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.ImageButton;

public class Chapter5 extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter5);

        ImageButton dungeon41 = findViewById(R.id.Dungeon41Button);
        dungeon41.setOnClickListener(v -> { openDungeon41(); playSound(); });

        ImageButton dungeon42 = findViewById(R.id.Dungeon42Button);
        dungeon42.setOnClickListener(v -> { openDungeon42(); playSound(); });

        ImageButton dungeon42bonus = findViewById(R.id.Dungeon42BonusButton);
        dungeon42bonus.setOnClickListener(v -> { openDungeon42bonus(); playSound(); });

        ImageButton dungeon43 = findViewById(R.id.Dungeon43Button);
        dungeon43.setOnClickListener(v -> { openDungeon43(); playSound(); });

        ImageButton dungeon43bonus = findViewById(R.id.Dungeon43BonusButton);
        dungeon43bonus.setOnClickListener(v -> { openDungeon43bonus(); playSound(); });

        ImageButton dungeon44 = findViewById(R.id.Dungeon44Button);
        dungeon44.setOnClickListener(v -> { openDungeon44(); playSound(); });
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

    public void openDungeon41() { startActivity(new Intent(this, Dungeon41.class)); }
    public void openDungeon42() { startActivity(new Intent(this, Dungeon42.class)); }
    public void openDungeon42bonus() { startActivity(new Intent(this, Dungeon42Bonus.class)); }
    public void openDungeon43() { startActivity(new Intent(this, Dungeon43.class)); }
    public void openDungeon43bonus() { startActivity(new Intent(this, Dungeon43Bonus.class)); }
    public void openDungeon44() { startActivity(new Intent(this, Dungeon44.class)); }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
