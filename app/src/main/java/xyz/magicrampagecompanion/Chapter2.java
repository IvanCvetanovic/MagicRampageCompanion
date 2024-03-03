package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter2 extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2);

        ImageButton dungeon11 = findViewById(R.id.Dungeon11Button);
        dungeon11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendungeon11();
            }
        });

        ImageButton dungeon11Bonus = findViewById(R.id.Dungeon11BonusButton);
        dungeon11Bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendungeon11bonus();
            }
        });

        ImageButton dungeon12 = findViewById(R.id.Dungeon12Button);
        dungeon12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                opendungeon12();
            }
        });

        ImageButton dungeon13 = findViewById(R.id.Dungeon13Button);
        dungeon13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendungeon13();
            }
        });

        ImageButton dungeon13bonus = findViewById(R.id.Dungeon13BonusButton);
        dungeon13bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendungeon13bonus();
            }
        });

        ImageButton dungeon14button = findViewById(R.id.Dungeon14Button);
        dungeon14button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendungeon14();
            }
        });

        ImageButton dungeon15button = findViewById(R.id.Dungeon15Button);
        dungeon15button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendungeon15();
            }
        });

        ImageButton dungeon16button = findViewById(R.id.Dungeon16Button);
        dungeon16button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendungeon16();
            }
        });

        ImageButton dungeon17button = findViewById(R.id.Dungeon17Button);
        dungeon17button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendungeon17();
            }
        });

        ImageButton dungeon17bonusbutton = findViewById(R.id.Dungeon17BonusButton);
        dungeon17bonusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendungeon17bonus();
            }
        });

        ImageButton dungeon18button = findViewById(R.id.Dungeon18Button);
        dungeon18button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendungeon18();
            }
        });

        ImageButton dungeon19button = findViewById(R.id.Dungeon19Button);
        dungeon19button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openDungeon19(); }
        });

        ImageButton dungeon19bonusbutton = findViewById(R.id.Dungeon19BonusButton);
        dungeon19bonusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openDungeon19Bonus(); }
        });

        ImageButton dungeon20button = findViewById(R.id.Dungeon20Button);
        dungeon20button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon20();
            }
        });
    }

    public void opendungeon11()
    {
        Intent intent = new Intent(this, Dungeon11.class);
        startActivity(intent);
    }

    public void opendungeon11bonus()
    {
        Intent intent = new Intent(this, Dungeon11Bonus.class);
        startActivity(intent);
    }

    public void opendungeon12()
    {
        Intent intent = new Intent(this, Dungeon12.class);
        startActivity(intent);
    }

    public void opendungeon13()
    {
        Intent intent = new Intent(this, Dungeon13.class);
        startActivity(intent);
    }

    public void opendungeon13bonus()
    {
        Intent intent = new Intent(this, Dungeon13Bonus.class);
        startActivity(intent);
    }

    public void opendungeon14()
    {
        Intent intent = new Intent(this, Dungeon14.class);
        startActivity(intent);
    }

    public void opendungeon15()
    {
        Intent intent = new Intent(this, Dungeon15.class);
        startActivity(intent);
    }

    public void opendungeon16()
    {
        Intent intent = new Intent(this, Dungeon16.class);
        startActivity(intent);
    }

    public void opendungeon17()
    {
        Intent intent = new Intent(this, Dungeon17.class);
        startActivity(intent);
    }

    public void opendungeon17bonus()
    {
        Intent intent = new Intent(this, Dungeon17Bonus.class);
        startActivity(intent);
    }

    public void opendungeon18()
    {
        Intent intent = new Intent(this, Dungeon18.class);
        startActivity(intent);
    }

    public void openDungeon19()
    {
        Intent intent = new Intent(this, Dungeon19.class);
        startActivity(intent);
    }

    public void openDungeon19Bonus()
    {
        Intent intent = new Intent(this, Dungeon19BonusButton.class);
        startActivity(intent);
    }

    public void openDungeon20()
    {
        Intent intent = new Intent(this, Dungeon20.class);
        startActivity(intent);
    }
}