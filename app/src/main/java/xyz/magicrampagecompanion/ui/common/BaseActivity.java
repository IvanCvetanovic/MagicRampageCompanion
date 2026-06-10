package xyz.magicrampagecompanion.ui.common;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;
import xyz.magicrampagecompanion.core.utils.SfxPlayer;
import xyz.magicrampagecompanion.ui.dungeons.DungeonActivity;

/**
 * Base for all activities: applies the user-selected locale and provides the
 * shared button-click sound effect.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final SfxPlayer sfx = new SfxPlayer();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Defer audio setup so the first frame can draw first.
        getWindow().getDecorView().post(() -> {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                initSfx();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (releaseSfxOnStop()) {
            sfx.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sfx.release();
    }

    /**
     * Screens that finish right after playing a sound can return false so the
     * effect is not cut off; the pool is then released in onDestroy.
     */
    protected boolean releaseSfxOnStop() {
        return true;
    }

    /** The raw resource used by {@link #playClick()}. */
    @RawRes
    protected int clickSoundRes() {
        return R.raw.click;
    }

    /** Subclasses needing extra sounds should override and call super first. */
    protected void initSfx() {
        sfx.init(this, clickSoundRes());
    }

    protected void playClick() {
        sfx.play(clickSoundRes());
    }

    /** Wires a button to open the given dungeon layout in {@link DungeonActivity}. */
    protected void bindDungeonButton(@IdRes int buttonId, @LayoutRes int dungeonLayoutRes) {
        findViewById(buttonId).setOnClickListener(v -> {
            startActivity(DungeonActivity.intentFor(this, dungeonLayoutRes));
            playClick();
        });
    }
}
