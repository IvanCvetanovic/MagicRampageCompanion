package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.ImageButton;

public class SurvivalModeSelection extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survival_mode_selection);

        ImageButton survivalButton1 = findViewById(R.id.SurvivalButton1);
        survivalButton1.setOnClickListener(v -> { openSurvivalDungeon1(); playSound(); });

        ImageButton survivalButton2 = findViewById(R.id.SurvivalButton2);
        survivalButton2.setOnClickListener(v -> { openSurvivalDungeon2(); playSound(); });

        ImageButton survivalButton3 = findViewById(R.id.SurvivalButton3);
        survivalButton3.setOnClickListener(v -> { openSurvivalDungeon3(); playSound(); });

        ImageButton survivalButton4 = findViewById(R.id.SurvivalButton4);
        survivalButton4.setOnClickListener(v -> { openSurvivalDungeon4(); playSound(); });

        ImageButton survivalButton5 = findViewById(R.id.SurvivalButton5);
        survivalButton5.setOnClickListener(v -> { openSurvivalDungeon5(); playSound(); });

        ImageButton survivalButton6 = findViewById(R.id.SurvivalButton6);
        survivalButton6.setOnClickListener(v -> { openSurvivalDungeon6(); playSound(); });
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

        // Use your short click sound resource
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

    public void openSurvivalDungeon1() { startActivity(new Intent(this, SurvivalDungeon1.class)); }
    public void openSurvivalDungeon2() { startActivity(new Intent(this, SurvivalDungeon2.class)); }
    public void openSurvivalDungeon3() { startActivity(new Intent(this, SurvivalDungeon3.class)); }
    public void openSurvivalDungeon4() { startActivity(new Intent(this, SurvivalDungeon4.class)); }
    public void openSurvivalDungeon5() { startActivity(new Intent(this, SurvivalDungeon5.class)); }
    public void openSurvivalDungeon6() { startActivity(new Intent(this, SurvivalDungeon6.class)); }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
