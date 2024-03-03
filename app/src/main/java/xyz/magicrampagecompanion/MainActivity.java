package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String SKILL_PREFERENCES = "SkillState";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetSkills();

        Button chapterSelectionButton = findViewById(R.id.ChapterSelectionButton);
        chapterSelectionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openChapterSelection();
            }
        });

        Button survivalModeSelectionButton = findViewById(R.id.SurvivalModeSelectionButton);
        survivalModeSelectionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSurvivalModeSelection();
            }
        });

        Button equipmentTesterButton = findViewById(R.id.EquipmentTesterButton);
        equipmentTesterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openEquipmentTester();
            }
        });

        Button aboutButton = findViewById(R.id.AboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openAboutSection();
            }
        });

        Button achievementsButton = findViewById(R.id.AchievementButton);
        achievementsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openAchievementsSection();
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

}

