package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SurvivalModeSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survival_mode_selection);

        ImageButton survivalButton1 = findViewById(R.id.SurvivalButton1);
        survivalButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSurvivalDungeon1();
            }
        });

        ImageButton survivalButton2 = findViewById(R.id.SurvivalButton2);
        survivalButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSurvivalDungeon2();
            }
        });

        ImageButton survivalButton3 = findViewById(R.id.SurvivalButton3);
        survivalButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSurvivalDungeon3();
            }
        });

        ImageButton survivalButton4 = findViewById(R.id.SurvivalButton4);
        survivalButton4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSurvivalDungeon4();
            }
        });

        ImageButton survivalButton5 = findViewById(R.id.SurvivalButton5);
        survivalButton5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSurvivalDungeon5();
            }
        });

        ImageButton survivalButton6 = findViewById(R.id.SurvivalButton6);
        survivalButton6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openSurvivalDungeon6();
            }
        });
    }

    public void openSurvivalDungeon1()
    {
        Intent intent = new Intent(this, SurvivalDungeon1.class);
        startActivity(intent);
    }

    public void openSurvivalDungeon2()
    {
        Intent intent = new Intent(this, SurvivalDungeon2.class);
        startActivity(intent);
    }

    public void openSurvivalDungeon3()
    {
        Intent intent = new Intent(this, SurvivalDungeon3.class);
        startActivity(intent);
    }

    public void openSurvivalDungeon4()
    {
        Intent intent = new Intent(this, SurvivalDungeon4.class);
        startActivity(intent);
    }

    public void openSurvivalDungeon5()
    {
        Intent intent = new Intent(this, SurvivalDungeon5.class);
        startActivity(intent);
    }

    public void openSurvivalDungeon6()
    {
        Intent intent = new Intent(this, SurvivalDungeon6.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}