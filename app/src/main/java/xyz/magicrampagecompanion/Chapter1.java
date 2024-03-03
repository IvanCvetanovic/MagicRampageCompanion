package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter1 extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1);

        final ImageButton dungeon1 = findViewById(R.id.Dungeon1Button);
        dungeon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openDungeon1(); }
        });

        ImageButton dungeon2 = findViewById(R.id.Dungeon2Button);
        dungeon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon2();
            }
        });

        ImageButton dungeon3 = findViewById(R.id.Dungeon3Button);
        dungeon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon3();
            }
        });

        ImageButton dungeon3Bonus = findViewById(R.id.Dungeon3BonusButton);
        dungeon3Bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon3bonus();
            }
        });

        ImageButton dungeon4 = findViewById(R.id.Dungeon4Button);
        dungeon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon4();
            }
        });

        ImageButton dungeon5 = findViewById(R.id.Dungeon5Button);
        dungeon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon5();
            }
        });

        ImageButton dungeon5Bonus = findViewById(R.id.Dungeon5BonusButton);
        dungeon5Bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon5bonus();
            }
        });

        ImageButton dungeon6 = findViewById(R.id.Dungeon6Button);
        dungeon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon6();
            }
        });

        ImageButton dungeon7 = findViewById(R.id.Dungeon7Button);
        dungeon7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon7();
            }
        });

        ImageButton dungeon7Bonus = findViewById(R.id.Dungeon7BonusButton);
        dungeon7Bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon7bonus();
            }
        });

        ImageButton dungeon8 = findViewById(R.id.Dungeon8Button);
        dungeon8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon8();
            }
        });

        ImageButton dungeon9 = findViewById(R.id.Dungeon9Button);
        dungeon9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon9();
            }
        });

        ImageButton dungeon10 = findViewById(R.id.Dungeon10Button);
        dungeon10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDungeon10();
            }
        });
    }

    public void openDungeon1()
    {
        Intent intent = new Intent(this, Dungeon1.class);
        startActivity(intent);
    }

    public void openDungeon2()
    {
        Intent intent = new Intent(this, Dungeon2.class);
        startActivity(intent);
    }

    public void openDungeon3()
    {
        Intent intent = new Intent(this, Dungeon3.class);
        startActivity(intent);
    }

    public void openDungeon3bonus()
    {
        Intent intent = new Intent(this, Dungeon3Bonus.class);
        startActivity(intent);
    }

    public void openDungeon4()
    {
        Intent intent = new Intent(this, Dungeon4.class);
        startActivity(intent);
    }
    public void openDungeon5()
    {
        Intent intent = new Intent(this, Dungeon5.class);
        startActivity(intent);
    }
    public void openDungeon5bonus()
    {
        Intent intent = new Intent(this, Dungeon5Bonus.class);
        startActivity(intent);
    }

    public void openDungeon6()
    {
        Intent intent = new Intent(this, Dungeon6.class);
        startActivity(intent);
    }

    public void openDungeon7()
    {
        Intent intent = new Intent(this, Dungeon7.class);
        startActivity(intent);
    }

    public void openDungeon7bonus()
    {
        Intent intent = new Intent(this, Dungeon7Bonus.class);
        startActivity(intent);
    }

    public void openDungeon8()
    {
        Intent intent = new Intent(this, Dungeon8.class);
        startActivity(intent);
    }

    public void openDungeon9()
    {
        Intent intent = new Intent(this, Dungeon9.class);
        startActivity(intent);
    }

    public void openDungeon10()
    {
        Intent intent = new Intent(this, Dungeon10.class);
        startActivity(intent);
    }
    }