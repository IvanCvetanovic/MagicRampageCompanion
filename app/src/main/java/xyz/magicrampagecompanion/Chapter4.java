package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4);

        ImageButton dungeon31 = findViewById(R.id.Dungeon31Button);
        dungeon31.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon31();
            }
        });

        ImageButton dungeon31bonus = findViewById(R.id.Dungeon31BonusButton);
        dungeon31bonus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon31bonus();
            }
        });

        ImageButton dungeon32 = findViewById(R.id.Dungeon32Button);
        dungeon32.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon32();
            }
        });

        ImageButton dungeon33 = findViewById(R.id.Dungeon33Button);
        dungeon33.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon33();
            }
        });

        ImageButton dungeon34 = findViewById(R.id.Dungeon34Button);
        dungeon34.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon34();
            }
        });

        ImageButton dungeon35 = findViewById(R.id.Dungeon35Button);
        dungeon35.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon35();
            }
        });

        ImageButton dungeon36 = findViewById(R.id.Dungeon36Button);
        dungeon36.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon36();
            }
        });

        ImageButton dungeon37 = findViewById(R.id.Dungeon37Button);
        dungeon37.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon37();
            }
        });

        ImageButton dungeon38 = findViewById(R.id.Dungeon38Button);
        dungeon38.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon38();
            }
        });

        ImageButton dungeon39 = findViewById(R.id.Dungeon39Button);
        dungeon39.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon39();
            }
        });

        ImageButton dungeon40 = findViewById(R.id.Dungeon40Button);
        dungeon40.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon40();
            }
        });
    }

    public void openDungeon31()
    {
        Intent intent = new Intent(this, Dungeon31.class);
        startActivity(intent);
    }

    public void openDungeon31bonus()
    {
        Intent intent = new Intent(this, Dungeon31Bonus.class);
        startActivity(intent);
    }

    public void openDungeon32()
    {
        Intent intent = new Intent(this, Dungeon32.class);
        startActivity(intent);
    }

    public void openDungeon33()
    {
        Intent intent = new Intent(this, Dungeon33.class);
        startActivity(intent);
    }

    public void openDungeon34()
    {
        Intent intent = new Intent(this, Dungeon34.class);
        startActivity(intent);
    }

    public void openDungeon35()
    {
        Intent intent = new Intent(this, Dungeon35.class);
        startActivity(intent);
    }

    public void openDungeon36()
    {
        Intent intent = new Intent(this, Dungeon36.class);
        startActivity(intent);
    }

    public void openDungeon37()
    {
        Intent intent = new Intent(this, Dungeon37.class);
        startActivity(intent);
    }

    public void openDungeon38()
    {
        Intent intent = new Intent(this, Dungeon38.class);
        startActivity(intent);
    }

    public void openDungeon39()
    {
        Intent intent = new Intent(this, Dungeon39.class);
        startActivity(intent);
    }

    public void openDungeon40()
    {
        Intent intent = new Intent(this, Dungeon40.class);
        startActivity(intent);
    }
}