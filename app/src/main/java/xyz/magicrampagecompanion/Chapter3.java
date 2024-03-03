package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3);

        ImageButton dungeon21 = findViewById(R.id.Dungeon21Button);
            dungeon21.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
            openDungeon21();
            }
        });

        ImageButton dungeon22 = findViewById(R.id.Dungeon22Button);
        dungeon22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon22();
            }
        });

        ImageButton dungeon23 = findViewById(R.id.Dungeon23Button);
        dungeon23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon23();
            }
        });

        ImageButton dungeon23bonus = findViewById(R.id.Dungeon23BonusButton);
        dungeon23bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon23Bonus();
            }
        });

        ImageButton dungeon24 = findViewById(R.id.Dungeon24Button);
        dungeon24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon24();
            }
        });

        ImageButton dungeon25 = findViewById(R.id.Dungeon25Button);
        dungeon25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon25();
            }
        });

        ImageButton dungeon26 = findViewById(R.id.Dungeon26Button);
        dungeon26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon26();
            }
        });

        ImageButton dungeon27 = findViewById(R.id.Dungeon27Button);
        dungeon27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon27();
            }
        });

        ImageButton dungeon27Bonus = findViewById(R.id.Dungeon27BonusButton);
        dungeon27Bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon27Bonus();
            }
        });

        ImageButton dungeon28 = findViewById(R.id.Dungeon28Button);
        dungeon28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon28();
            }
        });

        ImageButton dungeon29 = findViewById(R.id.Dungeon29Button);
        dungeon29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon29();
            }
        });

        ImageButton dungeon29bonus = findViewById(R.id.Dungeon29BonusButton);
        dungeon29bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon29bonus();
            }
        });

        ImageButton dungeon30 = findViewById(R.id.Dungeon30Button);
        dungeon30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon30();
            }
        });
}
    public void openDungeon21()
    {
        Intent intent = new Intent(this, Dungeon21.class);
        startActivity(intent);
    }

    public void openDungeon22()
    {
        Intent intent = new Intent(this, Dungeon22.class);
        startActivity(intent);
    }

    public void openDungeon23()
    {
        Intent intent = new Intent(this, Dungeon23.class);
        startActivity(intent);
    }

    public void openDungeon23Bonus()
    {
        Intent intent = new Intent(this, Dungeon23Bonus.class);
        startActivity(intent);
    }

    public void openDungeon24()
    {
        Intent intent = new Intent(this, Dungeon24.class);
        startActivity(intent);
    }

    public void openDungeon25()
    {
        Intent intent = new Intent(this, Dungeon25.class);
        startActivity(intent);
    }

    public void openDungeon26()
    {
        Intent intent = new Intent(this, Dungeon26.class);
        startActivity(intent);
    }

    public void openDungeon27()
    {
        Intent intent = new Intent(this, Dungeon27.class);
        startActivity(intent);
    }

    public void openDungeon27Bonus()
    {
        Intent intent = new Intent(this, Dungeon27Bonus.class);
        startActivity(intent);
    }

    public void openDungeon28()
    {
        Intent intent = new Intent(this, Dungeon28.class);
        startActivity(intent);
    }
    public void openDungeon29()
    {
        Intent intent = new Intent(this, Dungeon29.class);
        startActivity(intent);
    }

    public void openDungeon29bonus()
    {
        Intent intent = new Intent(this, Dungeon29Bonus.class);
        startActivity(intent);
    }

    public void openDungeon30()
    {
        Intent intent = new Intent(this, Dungeon30.class);
        startActivity(intent);
    }
}