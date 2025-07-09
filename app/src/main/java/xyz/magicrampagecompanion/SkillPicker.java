package xyz.magicrampagecompanion;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Arrays;
import java.util.function.Function;

public class SkillPicker extends AppCompatActivity {
    private int skillPointsLeft = 22;
    private TextView skillPointsTextView;
    private boolean[] skillsPicked = new boolean[36];
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_picker);

        mediaPlayer = MediaPlayer.create(this, R.raw.click);

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
        selectSkillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                playSound();
            }
        });

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
        btnDeselectAllSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deselectAllSkills();
                playSound();
            }
        });

        Arrays.fill(skillsPicked, false);
        SharedPreferences preferences = getSharedPreferences("SkillState", MODE_PRIVATE);
        for (int i = 0; i < skillsPicked.length; i++) {
            skillsPicked[i] = preferences.getBoolean("skill_" + i, false);
            Log.d("SkillPicker", "Skill " + i + " picked: " + skillsPicked[i]);
            if (skillsPicked[i]) {
                ImageView overlayView = findViewById(getOverlayViewIdForIndex(i));
                overlayView.setVisibility(View.VISIBLE);
            }
        }

        skillPointsLeft = preferences.getInt("skill_points_left", 22); // Default to 22 if not found
        Log.d("SkillPicker", "Skill points left: " + skillPointsLeft);

        updateSkillPointsText();
    }

    private void setupButtonClickListener(int frameLayoutId, int overlayViewId, final int skillIndex) {
        FrameLayout frameLayout = findViewById(frameLayoutId);
        final ImageView overlayView = frameLayout.findViewById(overlayViewId);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SkillPicker", "onClick called for skillIndex: " + skillIndex);

                if (skillsPicked[skillIndex]) {
                    // Deselect the skill directly
                    skillsPicked[skillIndex] = false;
                    toggleSelection(overlayView);
                    skillPointsLeft++; // Add back the skill point
                    // Deselect all skills after the tapped skill
                    deselectSkillsAfter(skillIndex);
                    updateSkillStateInSharedPreferences();
                    Log.d("SkillPicker", "Deselected skill " + skillIndex + " and all skills after it");
                } else if (canPickSkill(skillIndex)) {
                    if (skillPointsLeft > 0) {
                        skillsPicked[skillIndex] = true;
                        toggleSelection(overlayView);
                        skillPointsLeft--;
                        updateSkillStateInSharedPreferences();
                        Log.d("SkillPicker", "Selected skill " + skillIndex);
                    } else {
                        Toast.makeText(SkillPicker.this, "You have no skill points left", Toast.LENGTH_SHORT).show();
                    }
                    updateSkillPointsText();
                } else {
                    Log.d("SkillPicker", "Cannot pick skill " + skillIndex);
                    Toast.makeText(SkillPicker.this, "You need to pick the previous skill first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupLongClickListener(int frameLayoutId, final int skillIndex) {
        FrameLayout frameLayout = findViewById(frameLayoutId);

        frameLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showSkillDescriptionPopup(view, skillIndex);
                return true;
            }
        });
    }

    private void showSkillDescriptionPopup(View anchorView, int skillIndex) {
        // Retrieve the skill description based on the skill index
        String skillDescription = getSkillDescription(skillIndex);

        // Create a PopupWindow with the skill description
        @SuppressLint("InflateParams") View popupView = getLayoutInflater().inflate(R.layout.popup_skill_description, null);
        TextView descriptionTextView = popupView.findViewById(R.id.textSkillDescription);
        descriptionTextView.setText(skillDescription);

        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAsDropDown(anchorView);
    }

    private String getSkillDescription(int skillIndex) {
        switch (skillIndex) {
            case 0:
                return getString(R.string.skill_description_0);
            case 1:
                return getString(R.string.skill_description_1);
            case 2:
                return getString(R.string.skill_description_2);
            case 3:
                return getString(R.string.skill_description_3);
            case 4:
                return getString(R.string.skill_description_4);
            case 5:
                return getString(R.string.skill_description_5);
            case 6:
                return getString(R.string.skill_description_6);
            case 7:
                return getString(R.string.skill_description_7);
            case 8:
                return getString(R.string.skill_description_8);
            case 9:
                return getString(R.string.skill_description_9);
            case 10:
                return getString(R.string.skill_description_10);
            case 11:
                return getString(R.string.skill_description_11);



            case 12:
                return getString(R.string.skill_description_12);
            case 13:
                return getString(R.string.skill_description_13);
            case 14:
                return getString(R.string.skill_description_14);
            case 15:
                return getString(R.string.skill_description_15);
            case 16:
                return getString(R.string.skill_description_16);
            case 17:
                return getString(R.string.skill_description_17);
            case 18:
                return getString(R.string.skill_description_18);
            case 19:
                return getString(R.string.skill_description_19);
            case 20:
                return getString(R.string.skill_description_20);
            case 21:
                return getString(R.string.skill_description_21);
            case 22:
                return getString(R.string.skill_description_22);
            case 23:
                return getString(R.string.skill_description_23);

            case 24:
                return getString(R.string.skill_description_24);
            case 25:
                return getString(R.string.skill_description_25);
            case 26:
                return getString(R.string.skill_description_26);
            case 27:
                return getString(R.string.skill_description_27);
            case 28:
                return getString(R.string.skill_description_28);
            case 29:
                return getString(R.string.skill_description_29);
            case 30:
                return getString(R.string.skill_description_30);
            case 31:
                return getString(R.string.skill_description_31);
            case 32:
                return getString(R.string.skill_description_32);
            case 33:
                return getString(R.string.skill_description_33);
            case 34:
                return getString(R.string.skill_description_34);
            case 35:
                return getString(R.string.skill_description_35);

            default:
                return getString(R.string.skill_description_default);
        }
    }




    private boolean canPickSkill(int skillIndex) {
        if (skillIndex == 0 || skillIndex == 12 || skillIndex == 24) {
            Log.d("SkillPicker", "Can pick skill: " + skillIndex);
            return true;
        }

        int previousSkillIndex = skillIndex - 1;
        if (previousSkillIndex < 0) {
            Log.d("SkillPicker", "Cannot pick skill: " + skillIndex + ", Invalid skill index");
            return false; // Invalid skill index
        }

        if (!skillsPicked[previousSkillIndex]) {
            Log.d("SkillPicker", "Cannot pick skill: " + skillIndex + ", Previous skill not picked");
            return false; // Previous skill must be picked
        }

        Log.d("SkillPicker", "Can pick skill: " + skillIndex);
        return true;
    }

    private void deselectSkillsAfter(int skillIndex) {
        Log.d("SkillPicker", "Deselecting skills after skillIndex: " + skillIndex);

        int maxDeselectIndex = -1;

        if (skillIndex <= 11) {
            // First lane, deselect to maximum index of 11
            maxDeselectIndex = 11;
        } else if (skillIndex <= 23) {
            // Second lane, deselect to maximum index of 23
            maxDeselectIndex = 23;
        } else if (skillIndex <= 35) {
            // Third lane, deselect to maximum index of 35
            maxDeselectIndex = 35;
        }

        for (int i = skillIndex + 1; i <= maxDeselectIndex; i++) {
            if (skillsPicked[i]) {
                ImageView overlayView = findViewById(getOverlayViewIdForIndex(i));
                toggleSelection(overlayView);
                skillsPicked[i] = false;
            }
        }

        // Update the skill points left after deselecting skills
        skillPointsLeft = 22 - countSelectedSkills();
        updateSkillPointsText();
    }

    private int countSelectedSkills() {
        int count = 0;
        for (boolean skillPicked : skillsPicked) {
            if (skillPicked) {
                count++;
            }
        }
        return count;
    }


    private Function<Integer, Integer> getOverlayViewIdLane1 = skillIndex -> {
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

    private Function<Integer, Integer> getOverlayViewIdLane2 = skillIndex -> {
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

    private Function<Integer, Integer> getOverlayViewIdLane3 = skillIndex -> {
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



    private void toggleSelection(ImageView overlayView) {
        overlayView.setVisibility(overlayView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    @SuppressLint("StringFormatMatches")
    private void updateSkillPointsText() {
        if (skillPointsLeft < 0) {
            skillPointsLeft = 0;
        }

        String help = getString(R.string.skill_points_left);
        help += String.valueOf(skillPointsLeft);
        skillPointsTextView.setText(help);
    }

    private void deselectAllSkills() {
        for (int i = 0; i < skillsPicked.length; i++) {
            if (skillsPicked[i]) {
                ImageView overlayView = findViewById(getOverlayViewIdForIndex(i));
                toggleSelection(overlayView);
                skillsPicked[i] = false;
            }
        }
        skillPointsLeft = 22;
        updateSkillPointsText();
        updateSkillStateInSharedPreferences();
    }

    private void updateSkillStateInSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("SkillState", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Save skills picked array
        for (int i = 0; i < skillsPicked.length; i++) {
            editor.putBoolean("skill_" + i, skillsPicked[i]);
        }

        // Save remaining skill points
        editor.putInt("skill_points_left", skillPointsLeft);

        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSkillsPickedToSharedPreferences();
    }

    private void saveSkillsPickedToSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("SkillState", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < skillsPicked.length; i++) {
            editor.putBoolean("skillsPicked_" + i, skillsPicked[i]);
        }

        editor.apply();
    }

    private void playSound() {
        // Check if MediaPlayer is null or not
        if (mediaPlayer != null) {
            // Reset MediaPlayer if it's already playing
            mediaPlayer.seekTo(0);

            // Set volume to 50%
            float volume = 0.25f; // 50%
            mediaPlayer.setVolume(volume, volume);

            mediaPlayer.start();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}