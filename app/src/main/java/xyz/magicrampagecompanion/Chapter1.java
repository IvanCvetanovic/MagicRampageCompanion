package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter1 extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter1);

        // Buttons + navigation (unchanged)
        final ImageButton dungeon1 = findViewById(R.id.Dungeon1Button);
        dungeon1.setOnClickListener(v -> { openDungeon1(); playSound(); });

        ImageButton dungeon2 = findViewById(R.id.Dungeon2Button);
        dungeon2.setOnClickListener(v -> { openDungeon2(); playSound(); });

        ImageButton dungeon3 = findViewById(R.id.Dungeon3Button);
        dungeon3.setOnClickListener(v -> { openDungeon3(); playSound(); });

        ImageButton dungeon3Bonus = findViewById(R.id.Dungeon3BonusButton);
        dungeon3Bonus.setOnClickListener(v -> { openDungeon3bonus(); playSound(); });

        ImageButton dungeon4 = findViewById(R.id.Dungeon4Button);
        dungeon4.setOnClickListener(v -> { openDungeon4(); playSound(); });

        ImageButton dungeon5 = findViewById(R.id.Dungeon5Button);
        dungeon5.setOnClickListener(v -> { openDungeon5(); playSound(); });

        ImageButton dungeon5Bonus = findViewById(R.id.Dungeon5BonusButton);
        dungeon5Bonus.setOnClickListener(v -> { openDungeon5bonus(); playSound(); });

        ImageButton dungeon6 = findViewById(R.id.Dungeon6Button);
        dungeon6.setOnClickListener(v -> { openDungeon6(); playSound(); });

        ImageButton dungeon7 = findViewById(R.id.Dungeon7Button);
        dungeon7.setOnClickListener(v -> { openDungeon7(); playSound(); });

        ImageButton dungeon7Bonus = findViewById(R.id.Dungeon7BonusButton);
        dungeon7Bonus.setOnClickListener(v -> { openDungeon7bonus(); playSound(); });

        ImageButton dungeon8 = findViewById(R.id.Dungeon8Button);
        dungeon8.setOnClickListener(v -> { openDungeon8(); playSound(); });

        ImageButton dungeon9 = findViewById(R.id.Dungeon9Button);
        dungeon9.setOnClickListener(v -> { openDungeon9(); playSound(); });

        ImageButton dungeon10 = findViewById(R.id.Dungeon10Button);
        dungeon10.setOnClickListener(v -> { openDungeon10(); playSound(); });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Defer building SoundPool slightly so the first frame can draw
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
                .setMaxStreams(4)
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

        // Load your short click sound (R.raw.button)
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
        // Volume 0.25f (25%) on both channels; no loop; normal rate
        if (soundPool != null && clickSfxLoaded) {
            soundPool.play(clickSfxId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

    public void openDungeon1() { startActivity(new Intent(this, Dungeon1.class)); }
    public void openDungeon2() { startActivity(new Intent(this, Dungeon2.class)); }
    public void openDungeon3() { startActivity(new Intent(this, Dungeon3.class)); }
    public void openDungeon3bonus() { startActivity(new Intent(this, Dungeon3Bonus.class)); }
    public void openDungeon4() { startActivity(new Intent(this, Dungeon4.class)); }
    public void openDungeon5() { startActivity(new Intent(this, Dungeon5.class)); }
    public void openDungeon5bonus() { startActivity(new Intent(this, Dungeon5Bonus.class)); }
    public void openDungeon6() { startActivity(new Intent(this, Dungeon6.class)); }
    public void openDungeon7() { startActivity(new Intent(this, Dungeon7.class)); }
    public void openDungeon7bonus() { startActivity(new Intent(this, Dungeon7Bonus.class)); }
    public void openDungeon8() { startActivity(new Intent(this, Dungeon8.class)); }
    public void openDungeon9() { startActivity(new Intent(this, Dungeon9.class)); }
    public void openDungeon10() { startActivity(new Intent(this, Dungeon10.class)); }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
