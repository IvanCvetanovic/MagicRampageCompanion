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
import xyz.magicrampagecompanion.ui.dungeons.Dungeon21;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon22;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon23;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon23Bonus;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon24;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon25;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon26;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon27;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon27Bonus;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon28;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon29;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon29Bonus;
import xyz.magicrampagecompanion.ui.dungeons.Dungeon30;

public class Chapter3 extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter3);

        ImageButton dungeon21 = findViewById(R.id.Dungeon21Button);
        dungeon21.setOnClickListener(v -> { openDungeon21(); playSound(); });

        ImageButton dungeon22 = findViewById(R.id.Dungeon22Button);
        dungeon22.setOnClickListener(v -> { openDungeon22(); playSound(); });

        ImageButton dungeon23 = findViewById(R.id.Dungeon23Button);
        dungeon23.setOnClickListener(v -> { openDungeon23(); playSound(); });

        ImageButton dungeon23bonus = findViewById(R.id.Dungeon23BonusButton);
        dungeon23bonus.setOnClickListener(v -> { openDungeon23Bonus(); playSound(); });

        ImageButton dungeon24 = findViewById(R.id.Dungeon24Button);
        dungeon24.setOnClickListener(v -> { openDungeon24(); playSound(); });

        ImageButton dungeon25 = findViewById(R.id.Dungeon25Button);
        dungeon25.setOnClickListener(v -> { openDungeon25(); playSound(); });

        ImageButton dungeon26 = findViewById(R.id.Dungeon26Button);
        dungeon26.setOnClickListener(v -> { openDungeon26(); playSound(); });

        ImageButton dungeon27 = findViewById(R.id.Dungeon27Button);
        dungeon27.setOnClickListener(v -> { openDungeon27(); playSound(); });

        ImageButton dungeon27Bonus = findViewById(R.id.Dungeon27BonusButton);
        dungeon27Bonus.setOnClickListener(v -> { openDungeon27Bonus(); playSound(); });

        ImageButton dungeon28 = findViewById(R.id.Dungeon28Button);
        dungeon28.setOnClickListener(v -> { openDungeon28(); playSound(); });

        ImageButton dungeon29 = findViewById(R.id.Dungeon29Button);
        dungeon29.setOnClickListener(v -> { openDungeon29(); playSound(); });

        ImageButton dungeon29bonus = findViewById(R.id.Dungeon29BonusButton);
        dungeon29bonus.setOnClickListener(v -> { openDungeon29bonus(); playSound(); });

        ImageButton dungeon30 = findViewById(R.id.Dungeon30Button);
        dungeon30.setOnClickListener(v -> { openDungeon30(); playSound(); });
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

    public void openDungeon21() { startActivity(new Intent(this, Dungeon21.class)); }
    public void openDungeon22() { startActivity(new Intent(this, Dungeon22.class)); }
    public void openDungeon23() { startActivity(new Intent(this, Dungeon23.class)); }
    public void openDungeon23Bonus() { startActivity(new Intent(this, Dungeon23Bonus.class)); }
    public void openDungeon24() { startActivity(new Intent(this, Dungeon24.class)); }
    public void openDungeon25() { startActivity(new Intent(this, Dungeon25.class)); }
    public void openDungeon26() { startActivity(new Intent(this, Dungeon26.class)); }
    public void openDungeon27() { startActivity(new Intent(this, Dungeon27.class)); }
    public void openDungeon27Bonus() { startActivity(new Intent(this, Dungeon27Bonus.class)); }
    public void openDungeon28() { startActivity(new Intent(this, Dungeon28.class)); }
    public void openDungeon29() { startActivity(new Intent(this, Dungeon29.class)); }
    public void openDungeon29bonus() { startActivity(new Intent(this, Dungeon29Bonus.class)); }
    public void openDungeon30() { startActivity(new Intent(this, Dungeon30.class)); }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
