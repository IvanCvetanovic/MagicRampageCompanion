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

public class ChapterSelection extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter_selection);

        ImageButton chapter1 = findViewById(R.id.Chapter1Button);
        chapter1.setOnClickListener(v -> { openChapter1(); playSound(); });

        ImageButton chapter2 = findViewById(R.id.Chapter2Button);
        chapter2.setOnClickListener(v -> { openChapter2(); playSound(); });

        ImageButton chapter3 = findViewById(R.id.Chapter3Button);
        chapter3.setOnClickListener(v -> { openChapter3(); playSound(); });

        ImageButton chapter4 = findViewById(R.id.Chapter4Button);
        chapter4.setOnClickListener(v -> { openChapter4(); playSound(); });

        ImageButton chapter5 = findViewById(R.id.Chapter5Button);
        chapter5.setOnClickListener(v -> { openChapter5(); playSound(); });
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

        // Load your click sound
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
        if (soundPool != null && clickSfxLoaded) {
            // Volume 25% on both channels; no loop; normal rate
            soundPool.play(clickSfxId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

    public void openChapter1() { startActivity(new Intent(this, Chapter1.class)); }
    public void openChapter2() { startActivity(new Intent(this, Chapter2.class)); }
    public void openChapter3() { startActivity(new Intent(this, Chapter3.class)); }
    public void openChapter4() { startActivity(new Intent(this, Chapter4.class)); }
    public void openChapter5() { startActivity(new Intent(this, Chapter5.class)); }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
