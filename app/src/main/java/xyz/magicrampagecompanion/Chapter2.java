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

public class Chapter2 extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter2);

        ImageButton dungeon11 = findViewById(R.id.Dungeon11Button);
        dungeon11.setOnClickListener(v -> { opendungeon11(); playSound(); });

        ImageButton dungeon11Bonus = findViewById(R.id.Dungeon11BonusButton);
        dungeon11Bonus.setOnClickListener(v -> { opendungeon11bonus(); playSound(); });

        ImageButton dungeon12 = findViewById(R.id.Dungeon12Button);
        dungeon12.setOnClickListener(v -> { opendungeon12(); playSound(); });

        ImageButton dungeon13 = findViewById(R.id.Dungeon13Button);
        dungeon13.setOnClickListener(v -> { opendungeon13(); playSound(); });

        ImageButton dungeon13bonus = findViewById(R.id.Dungeon13BonusButton);
        dungeon13bonus.setOnClickListener(v -> { opendungeon13bonus(); playSound(); });

        ImageButton dungeon14button = findViewById(R.id.Dungeon14Button);
        dungeon14button.setOnClickListener(v -> { opendungeon14(); playSound(); });

        ImageButton dungeon15button = findViewById(R.id.Dungeon15Button);
        dungeon15button.setOnClickListener(v -> { opendungeon15(); playSound(); });

        ImageButton dungeon16button = findViewById(R.id.Dungeon16Button);
        dungeon16button.setOnClickListener(v -> { opendungeon16(); playSound(); });

        ImageButton dungeon17button = findViewById(R.id.Dungeon17Button);
        dungeon17button.setOnClickListener(v -> { opendungeon17(); playSound(); });

        ImageButton dungeon17bonusbutton = findViewById(R.id.Dungeon17BonusButton);
        dungeon17bonusbutton.setOnClickListener(v -> { opendungeon17bonus(); playSound(); });

        ImageButton dungeon18button = findViewById(R.id.Dungeon18Button);
        dungeon18button.setOnClickListener(v -> { opendungeon18(); playSound(); });

        ImageButton dungeon19button = findViewById(R.id.Dungeon19Button);
        dungeon19button.setOnClickListener(v -> { openDungeon19(); playSound(); });

        ImageButton dungeon19bonusbutton = findViewById(R.id.Dungeon19BonusButton);
        dungeon19bonusbutton.setOnClickListener(v -> { openDungeon19Bonus(); playSound(); });

        ImageButton dungeon20button = findViewById(R.id.Dungeon20Button);
        dungeon20button.setOnClickListener(v -> { openDungeon20(); playSound(); });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Defer init so first frame can draw before audio setup
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
            soundPool.play(clickSfxId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

    public void opendungeon11() {
        startActivity(new Intent(this, Dungeon11.class));
    }

    public void opendungeon11bonus() {
        startActivity(new Intent(this, Dungeon11Bonus.class));
    }

    public void opendungeon12() {
        startActivity(new Intent(this, Dungeon12.class));
    }

    public void opendungeon13() {
        startActivity(new Intent(this, Dungeon13.class));
    }

    public void opendungeon13bonus() {
        startActivity(new Intent(this, Dungeon13Bonus.class));
    }

    public void opendungeon14() {
        startActivity(new Intent(this, Dungeon14.class));
    }

    public void opendungeon15() {
        startActivity(new Intent(this, Dungeon15.class));
    }

    public void opendungeon16() {
        startActivity(new Intent(this, Dungeon16.class));
    }

    public void opendungeon17() {
        startActivity(new Intent(this, Dungeon17.class));
    }

    public void opendungeon17bonus() {
        startActivity(new Intent(this, Dungeon17Bonus.class));
    }

    public void opendungeon18() {
        startActivity(new Intent(this, Dungeon18.class));
    }

    public void openDungeon19() {
        startActivity(new Intent(this, Dungeon19.class));
    }

    public void openDungeon19Bonus() {
        startActivity(new Intent(this, Dungeon19BonusButton.class));
    }

    public void openDungeon20() {
        startActivity(new Intent(this, Dungeon20.class));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
