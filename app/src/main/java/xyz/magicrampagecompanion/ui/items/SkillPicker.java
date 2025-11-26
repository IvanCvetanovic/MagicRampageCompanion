package xyz.magicrampagecompanion.ui.items;

import androidx.activity.EdgeToEdge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.function.Function;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;

public class SkillPicker extends AppCompatActivity {

    private static final int TOTAL_POINTS = 25;
    private static final int LANE_SIZE    = 12; // 3 lanes of 12 = 36

    private int skillPointsLeft = TOTAL_POINTS;
    private TextView skillPointsTextView;
    private final boolean[] skillsPicked = new boolean[36];

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_skill_picker);

        // Edge-to-edge padding
        View root = findViewById(R.id.skillRoot);
        applySystemInsets(root);

        // Long-press tooltips
        setupLongClickListener(R.id.frameEnhancedVelocity, 0);
        setupLongClickListener(R.id.frameSwordMaster, 1);
        setupLongClickListener(R.id.frameDaggerSpecialist, 2);
        setupLongClickListener(R.id.frameObstinateShot, 3);
        setupLongClickListener(R.id.frameCorrosiveShot, 4);
        setupLongClickListener(R.id.frameSphericalShieldBreaker, 5);
        setupLongClickListener(R.id.frameVoraciousShot, 6);
        setupLongClickListener(R.id.frameMonstrousVigour, 7);
        setupLongClickListener(R.id.frameChargeBlast, 8);
        setupLongClickListener(R.id.frameFinalWrath, 9);
        setupLongClickListener(R.id.frameRushOfTheDoomed, 10);
        setupLongClickListener(R.id.frameOvercharge, 11);

        setupLongClickListener(R.id.frameFearlessJump, 12);
        setupLongClickListener(R.id.frameVirtuousWizard, 13);
        setupLongClickListener(R.id.frameThrowingLegend, 14);
        setupLongClickListener(R.id.frameGoldMagnet, 15);
        setupLongClickListener(R.id.frameFireproofSkin, 16);
        setupLongClickListener(R.id.frameToxicHaze, 17);
        setupLongClickListener(R.id.frameShadowCamouflage, 18);
        setupLongClickListener(R.id.frameMagicJump, 19);
        setupLongClickListener(R.id.frameDarkTwin, 20);
        setupLongClickListener(R.id.frameArcaneDiscipline, 21);
        setupLongClickListener(R.id.frameSwiftCast, 22);
        setupLongClickListener(R.id.frameAirborneDancer, 23);

        setupLongClickListener(R.id.frameUnstoppableStrength, 24);
        setupLongClickListener(R.id.frameCrushingStrength, 25);
        setupLongClickListener(R.id.frameSlicingMaster, 26);
        setupLongClickListener(R.id.frameMonstrousMetabolism, 27);
        setupLongClickListener(R.id.frameEnhancedVitality, 28);
        setupLongClickListener(R.id.frameUntouchableSkin, 29);
        setupLongClickListener(R.id.frameConfidentMove, 30);
        setupLongClickListener(R.id.frameArcaneProtection, 31);
        setupLongClickListener(R.id.frameThunderWave, 32);
        setupLongClickListener(R.id.frameSeismicReach, 33);
        setupLongClickListener(R.id.frameTitansPush, 34);
        setupLongClickListener(R.id.frameShockwaveForce, 35);

        skillPointsTextView = findViewById(R.id.textSkillPointsLeft);
        updateSkillPointsText();

        Button selectSkillsButton = findViewById(R.id.SelectSkills);
        selectSkillsButton.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
            playSound();
        });

        // Click handlers — enforce lane order and support multi-fill selection
        setupButtonClickListener(R.id.frameEnhancedVelocity, R.id.overlayView1, 0);
        setupButtonClickListener(R.id.frameSwordMaster, R.id.overlayView2, 1);
        setupButtonClickListener(R.id.frameDaggerSpecialist, R.id.overlayView3, 2);
        setupButtonClickListener(R.id.frameObstinateShot, R.id.overlayView4, 3);
        setupButtonClickListener(R.id.frameCorrosiveShot, R.id.overlayView5, 4);
        setupButtonClickListener(R.id.frameSphericalShieldBreaker, R.id.overlayView6, 5);
        setupButtonClickListener(R.id.frameVoraciousShot, R.id.overlayView7, 6);
        setupButtonClickListener(R.id.frameMonstrousVigour, R.id.overlayView8, 7);
        setupButtonClickListener(R.id.frameChargeBlast, R.id.overlayView9, 8);
        setupButtonClickListener(R.id.frameFinalWrath, R.id.overlayView10, 9);
        setupButtonClickListener(R.id.frameRushOfTheDoomed, R.id.overlayView11, 10);
        setupButtonClickListener(R.id.frameOvercharge, R.id.overlayView12, 11);

        setupButtonClickListener(R.id.frameFearlessJump, R.id.overlayView13, 12);
        setupButtonClickListener(R.id.frameVirtuousWizard, R.id.overlayView14, 13);
        setupButtonClickListener(R.id.frameThrowingLegend, R.id.overlayView15, 14);
        setupButtonClickListener(R.id.frameGoldMagnet, R.id.overlayView16, 15);
        setupButtonClickListener(R.id.frameFireproofSkin, R.id.overlayView17, 16);
        setupButtonClickListener(R.id.frameToxicHaze, R.id.overlayView18, 17);
        setupButtonClickListener(R.id.frameShadowCamouflage, R.id.overlayView19, 18);
        setupButtonClickListener(R.id.frameMagicJump, R.id.overlayView20, 19);
        setupButtonClickListener(R.id.frameDarkTwin, R.id.overlayView21, 20);
        setupButtonClickListener(R.id.frameArcaneDiscipline, R.id.overlayView22, 21);
        setupButtonClickListener(R.id.frameSwiftCast, R.id.overlayView23, 22);
        setupButtonClickListener(R.id.frameAirborneDancer, R.id.overlayView24, 23);

        setupButtonClickListener(R.id.frameUnstoppableStrength, R.id.overlayView25, 24);
        setupButtonClickListener(R.id.frameCrushingStrength, R.id.overlayView26, 25);
        setupButtonClickListener(R.id.frameSlicingMaster, R.id.overlayView27, 26);
        setupButtonClickListener(R.id.frameMonstrousMetabolism, R.id.overlayView28, 27);
        setupButtonClickListener(R.id.frameEnhancedVitality, R.id.overlayView29, 28);
        setupButtonClickListener(R.id.frameUntouchableSkin, R.id.overlayView30, 29);
        setupButtonClickListener(R.id.frameConfidentMove, R.id.overlayView31, 30);
        setupButtonClickListener(R.id.frameArcaneProtection, R.id.overlayView32, 31);
        setupButtonClickListener(R.id.frameThunderWave, R.id.overlayView33, 32);
        setupButtonClickListener(R.id.frameSeismicReach, R.id.overlayView34, 33);
        setupButtonClickListener(R.id.frameTitansPush, R.id.overlayView35, 34);
        setupButtonClickListener(R.id.frameShockwaveForce, R.id.overlayView36, 35);

        Button btnDeselectAllSkills = findViewById(R.id.btnDeselectAllSkills);
        btnDeselectAllSkills.setOnClickListener(view -> {
            deselectAllSkills();
            playSound();
        });

        // Restore saved state
        Arrays.fill(skillsPicked, false);
        SharedPreferences preferences = getSharedPreferences("SkillState", MODE_PRIVATE);
        for (int i = 0; i < skillsPicked.length; i++) {
            skillsPicked[i] = preferences.getBoolean("skill_" + i, false);
            if (skillsPicked[i]) setOverlayVisible(i, true);
        }
        skillPointsLeft = preferences.getInt("skill_points_left", TOTAL_POINTS);
        updateSkillPointsText();
    }

    // Insets
    private void applySystemInsets(View root) {
        final int baseLeft = root.getPaddingLeft();
        final int baseTop = root.getPaddingTop();
        final int baseRight = root.getPaddingRight();
        final int baseBottom = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(baseLeft, baseTop + bars.top, baseRight, baseBottom + bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    // Clicks: lane order + multi-fill
    private void setupButtonClickListener(int frameLayoutId, int overlayViewId, final int skillIndex) {
        FrameLayout frameLayout = findViewById(frameLayoutId);
        frameLayout.setOnClickListener(view -> {
            if (skillsPicked[skillIndex]) {
                // Deselect this and everything after it in the lane
                setPicked(skillIndex, false);
                deselectSkillsAfter(skillIndex);
                recalcPointsFromSelection();
                updatePersistAndUI();
                playSound();
                return;
            }

            // Selecting: if previous is already OK, just pick this one (if points left)
            if (canPickSkill(skillIndex)) {
                if (skillPointsLeft <= 0) {
                    Toast.makeText(this, "You have no skill points left", Toast.LENGTH_SHORT).show();
                    return;
                }
                setPicked(skillIndex, true);
                skillPointsLeft--;
                updatePersistAndUI();
                playSound();
                return;
            }

            // NEW: Multi-fill up to the tapped index (lane start .. index)
            int laneStart = getLaneStart(skillIndex);
            int needed = 0;
            for (int i = laneStart; i <= skillIndex; i++) {
                if (!skillsPicked[i]) needed++;
            }
            if (needed == 0) return; // nothing to do (shouldn't happen)

            if (skillPointsLeft < needed) {
                Toast.makeText(this, "Not enough skill points to unlock all previous skills.", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = laneStart; i <= skillIndex; i++) {
                if (!skillsPicked[i]) {
                    setPicked(i, true);
                    skillPointsLeft--;
                }
            }
            updatePersistAndUI();
            playSound();
        });
    }

    private void updatePersistAndUI() {
        updateSkillPointsText();
        updateSkillStateInSharedPreferences();
    }

    /** Set selection + overlay visibility for an index. */
    private void setPicked(int index, boolean picked) {
        skillsPicked[index] = picked;
        setOverlayVisible(index, picked);
    }

    private void setOverlayVisible(int index, boolean visible) {
        ImageView ov = findViewById(getOverlayViewIdForIndex(index));
        if (ov != null) ov.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /** Can pick if it's first in lane, or previous in lane is selected. */
    private boolean canPickSkill(int skillIndex) {
        int laneStart = getLaneStart(skillIndex);
        if (skillIndex == laneStart) return true;
        return skillsPicked[skillIndex - 1];
    }

    /** Deselect everything AFTER this index within its lane. */
    private void deselectSkillsAfter(int skillIndex) {
        int laneEnd = getLaneEnd(skillIndex);
        for (int i = skillIndex + 1; i <= laneEnd; i++) {
            if (skillsPicked[i]) setPicked(i, false);
        }
    }

    private int getLaneStart(int index) {
        return (index / LANE_SIZE) * LANE_SIZE; // 0, 12, 24
    }

    private int getLaneEnd(int index) {
        return getLaneStart(index) + LANE_SIZE - 1; // 11, 23, 35
    }

    private void recalcPointsFromSelection() {
        int selected = 0;
        for (boolean b : skillsPicked) if (b) selected++;
        skillPointsLeft = TOTAL_POINTS - selected;
        if (skillPointsLeft < 0) skillPointsLeft = 0; // safety
    }

    // Tooltips
    private void setupLongClickListener(int frameLayoutId, final int skillIndex) {
        FrameLayout frameLayout = findViewById(frameLayoutId);
        frameLayout.setOnLongClickListener(view -> {
            showSkillDescriptionPopup(view, skillIndex);
            return true;
        });
    }

    private void showSkillDescriptionPopup(View anchorView, int skillIndex) {
        String skillDescription = getSkillDescription(skillIndex);
        @SuppressLint("InflateParams") View popupView =
                getLayoutInflater().inflate(R.layout.popup_skill_description, null);
        TextView descriptionTextView = popupView.findViewById(R.id.textSkillDescription);
        descriptionTextView.setText(skillDescription);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.showAsDropDown(anchorView);
    }

    private String getSkillDescription(int skillIndex) {
        switch (skillIndex) {
            case 0:  return getString(R.string.skill_description_0);
            case 1:  return getString(R.string.skill_description_1);
            case 2:  return getString(R.string.skill_description_2);
            case 3:  return getString(R.string.skill_description_3);
            case 4:  return getString(R.string.skill_description_4);
            case 5:  return getString(R.string.skill_description_5);
            case 6:  return getString(R.string.skill_description_6);
            case 7:  return getString(R.string.skill_description_7);
            case 8:  return getString(R.string.skill_description_8);
            case 9:  return getString(R.string.skill_description_9);
            case 10: return getString(R.string.skill_description_10);
            case 11: return getString(R.string.skill_description_11);
            case 12: return getString(R.string.skill_description_12);
            case 13: return getString(R.string.skill_description_13);
            case 14: return getString(R.string.skill_description_14);
            case 15: return getString(R.string.skill_description_15);
            case 16: return getString(R.string.skill_description_16);
            case 17: return getString(R.string.skill_description_17);
            case 18: return getString(R.string.skill_description_18);
            case 19: return getString(R.string.skill_description_19);
            case 20: return getString(R.string.skill_description_20);
            case 21: return getString(R.string.skill_description_21);
            case 22: return getString(R.string.skill_description_22);
            case 23: return getString(R.string.skill_description_23);
            case 24: return getString(R.string.skill_description_24);
            case 25: return getString(R.string.skill_description_25);
            case 26: return getString(R.string.skill_description_26);
            case 27: return getString(R.string.skill_description_27);
            case 28: return getString(R.string.skill_description_28);
            case 29: return getString(R.string.skill_description_29);
            case 30: return getString(R.string.skill_description_30);
            case 31: return getString(R.string.skill_description_31);
            case 32: return getString(R.string.skill_description_32);
            case 33: return getString(R.string.skill_description_33);
            case 34: return getString(R.string.skill_description_34);
            case 35: return getString(R.string.skill_description_35);
            default: return getString(R.string.skill_description_default);
        }
    }

    // Map overlay view ids by lane
    private final Function<Integer, Integer> getOverlayViewIdLane1 = skillIndex -> {
        switch (skillIndex) {
            case 0: return R.id.overlayView1;
            case 1: return R.id.overlayView2;
            case 2: return R.id.overlayView3;
            case 3: return R.id.overlayView4;
            case 4: return R.id.overlayView5;
            case 5: return R.id.overlayView6;
            case 6: return R.id.overlayView7;
            case 7: return R.id.overlayView8;
            case 8: return R.id.overlayView9;
            case 9: return R.id.overlayView10;
            case 10: return R.id.overlayView11;
            case 11: return R.id.overlayView12;
            default: return -1;
        }
    };

    private final Function<Integer, Integer> getOverlayViewIdLane2 = skillIndex -> {
        switch (skillIndex) {
            case 0: return R.id.overlayView13;
            case 1: return R.id.overlayView14;
            case 2: return R.id.overlayView15;
            case 3: return R.id.overlayView16;
            case 4: return R.id.overlayView17;
            case 5: return R.id.overlayView18;
            case 6: return R.id.overlayView19;
            case 7: return R.id.overlayView20;
            case 8: return R.id.overlayView21;
            case 9: return R.id.overlayView22;
            case 10: return R.id.overlayView23;
            case 11: return R.id.overlayView24;
            default: return -1;
        }
    };

    private final Function<Integer, Integer> getOverlayViewIdLane3 = skillIndex -> {
        switch (skillIndex) {
            case 0: return R.id.overlayView25;
            case 1: return R.id.overlayView26;
            case 2: return R.id.overlayView27;
            case 3: return R.id.overlayView28;
            case 4: return R.id.overlayView29;
            case 5: return R.id.overlayView30;
            case 6: return R.id.overlayView31;
            case 7: return R.id.overlayView32;
            case 8: return R.id.overlayView33;
            case 9: return R.id.overlayView34;
            case 10: return R.id.overlayView35;
            case 11: return R.id.overlayView36;
            default: return -1;
        }
    };

    private int getOverlayViewIdForIndex(int skillIndex) {
        if (skillIndex < 0 || skillIndex >= 36) return -1;
        if (skillIndex < 12) {
            return getOverlayViewIdLane1.apply(skillIndex);
        } else if (skillIndex < 24) {
            return getOverlayViewIdLane2.apply(skillIndex - 12);
        } else {
            return getOverlayViewIdLane3.apply(skillIndex - 24);
        }
    }

    @SuppressLint("StringFormatMatches")
    private void updateSkillPointsText() {
        if (skillPointsLeft < 0) skillPointsLeft = 0;
        String text = getString(R.string.skill_points_left) + skillPointsLeft;
        skillPointsTextView.setText(text);
    }

    private void deselectAllSkills() {
        for (int i = 0; i < skillsPicked.length; i++) {
            if (skillsPicked[i]) setPicked(i, false);
        }
        skillPointsLeft = TOTAL_POINTS;
        updatePersistAndUI();
    }

    private void updateSkillStateInSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("SkillState", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < skillsPicked.length; i++) {
            editor.putBoolean("skill_" + i, skillsPicked[i]);
        }
        editor.putInt("skill_points_left", skillPointsLeft);
        editor.apply();
    }

    // Sound
    @Override
    protected void onStart() {
        super.onStart();
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
            if (status == 0 && sampleId == clickSfxId) clickSfxLoaded = true;
        });

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
            soundPool.play(clickSfxId, 0.25f, 0.25f, 1, 0, 1.0f);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSoundPool();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
