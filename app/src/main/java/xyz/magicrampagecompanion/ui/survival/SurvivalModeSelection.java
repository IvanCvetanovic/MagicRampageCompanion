package xyz.magicrampagecompanion.ui.survival;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;

public class SurvivalModeSelection extends AppCompatActivity {

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survival_mode_selection);

        // ---- Apply system bar insets (status bar / camera cutout / nav bar) ----
        // If your XML has dedicated containers, give them these IDs:
        //   - R.id.survivalScroll (ScrollView or Recycler)
        //   - R.id.survivalContentRoot (the topmost inner container)
        // If they don't exist, this helper safely falls back to the root view.
        View scroll = findViewById(R.id.survivalScroll);
        View content = findViewById(R.id.survivalContentRoot);
        applySystemInsets(scroll, content);

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

    // ---- Edge-to-edge helper (mirrors your MainActivity, with safe fallbacks) ----
    private void applySystemInsets(View scrollViewOrNull, View contentRootOrNull) {
        // Fallbacks if IDs aren’t present
        final View root = findViewById(android.R.id.content);
        final View contentRoot = (contentRootOrNull != null) ? contentRootOrNull : root;
        final View scrollView   = (scrollViewOrNull   != null) ? scrollViewOrNull   : contentRoot;

        final int baseScrollLeft   = scrollView.getPaddingLeft();
        final int baseScrollTop    = scrollView.getPaddingTop();
        final int baseScrollRight  = scrollView.getPaddingRight();
        final int baseScrollBottom = scrollView.getPaddingBottom();

        final int baseContentLeft   = contentRoot.getPaddingLeft();
        final int baseContentTop    = contentRoot.getPaddingTop();
        final int baseContentRight  = contentRoot.getPaddingRight();
        final int baseContentBottom = contentRoot.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Push the first touchable pixel below the status bar / camera cutout
            contentRoot.setPadding(
                    baseContentLeft,
                    baseContentTop + bars.top,
                    baseContentRight,
                    baseContentBottom
            );

            // Keep the last item above the gesture nav bar
            scrollView.setPadding(
                    baseScrollLeft,
                    baseScrollTop,
                    baseScrollRight,
                    baseScrollBottom + bars.bottom
            );

            return insets;
        });

        ViewCompat.requestApplyInsets(root);
    }
}
