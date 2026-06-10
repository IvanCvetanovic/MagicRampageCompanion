package xyz.magicrampagecompanion.core.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.RawRes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Small SoundPool wrapper for short UI sound effects. Replaces the
 * init/release/play boilerplate previously copy-pasted across activities.
 */
public class SfxPlayer {

    private static final float DEFAULT_VOLUME = 0.25f;

    private SoundPool soundPool;
    private final Map<Integer, Integer> soundIds = new HashMap<>(); // raw res -> sound id
    private final Set<Integer> loadedSoundIds = new HashSet<>();

    /** Creates the pool if needed and loads the given raw sounds. */
    public void init(Context context, @RawRes int... rawResIds) {
        if (soundPool == null) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build())
                    .build();
            soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
                if (status == 0) loadedSoundIds.add(sampleId);
            });
        }
        for (int rawResId : rawResIds) {
            if (!soundIds.containsKey(rawResId)) {
                soundIds.put(rawResId, soundPool.load(context, rawResId, 1));
            }
        }
    }

    public void play(@RawRes int rawResId) {
        play(rawResId, DEFAULT_VOLUME);
    }

    public void play(@RawRes int rawResId, float volume) {
        if (soundPool == null) return;
        Integer soundId = soundIds.get(rawResId);
        if (soundId != null && loadedSoundIds.contains(soundId)) {
            soundPool.play(soundId, volume, volume, 1, 0, 1.0f);
        }
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        soundIds.clear();
        loadedSoundIds.clear();
    }
}
