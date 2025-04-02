package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String SKILL_PREFERENCES = "SkillState";
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ItemData.init(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.click);

        resetSkills();

        Button chapterSelectionButton = findViewById(R.id.ChapterSelectionButton);
        chapterSelectionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openChapterSelection();
                playSound();
            }
        });

        Button survivalModeSelectionButton = findViewById(R.id.SurvivalModeSelectionButton);
        survivalModeSelectionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSurvivalModeSelection();
                playSound();
            }
        });

        Button equipmentTesterButton = findViewById(R.id.EquipmentTesterButton);
        equipmentTesterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openEquipmentTester();
                playSound();
            }
        });

        Button aboutButton = findViewById(R.id.AboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openAboutSection();
                playSound();
            }
        });

        Button achievementsButton = findViewById(R.id.AchievementButton);
        achievementsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openAchievementsSection();
                playSound();
            }
        });

    }

    private void resetSkills() {
        SharedPreferences preferences = getSharedPreferences(SKILL_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < 27; i++) {
            editor.putBoolean("skillsPicked_" + i, false);
        }
        editor.apply();
    }

    public void openChapterSelection()
    {
        Intent intent = new Intent(this, ChapterSelection.class);
        startActivity(intent);
    }

    public void openSurvivalModeSelection()
    {
        Intent intent = new Intent(this, SurvivalModeSelection.class);
        startActivity(intent);
    }

    public void openEquipmentTester()
    {
        Intent intent = new Intent(this, EquipmentTester.class);
        startActivity(intent);
    }

    public void openAboutSection()
    {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    public void openAchievementsSection()
    {
        Intent intent = new Intent(this, AchievementsPage.class);
        startActivity(intent);
    }

    private void playSound() {
        // Check if MediaPlayer is null or not
        if (mediaPlayer != null) {
            // Reset MediaPlayer if it's already playing
            mediaPlayer.seekTo(0);

            // Set volume to 50%
            float volume = 0.5f; // 50%
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

