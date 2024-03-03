package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter5);

        ImageButton dungeon41 = findViewById(R.id.Dungeon41Button);
        dungeon41.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon41();
            }
        });

        ImageButton dungeon42 = findViewById(R.id.Dungeon42Button);
        dungeon42.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon42();
            }
        });

        ImageButton dungeon42bonus = findViewById(R.id.Dungeon42BonusButton);
        dungeon42bonus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon42bonus();
            }
        });

        ImageButton dungeon43 = findViewById(R.id.Dungeon43Button);
        dungeon43.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon43();
            }
        });

        ImageButton dungeon43bonus = findViewById(R.id.Dungeon43BonusButton);
        dungeon43bonus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon43bonus();
            }
        });

        ImageButton dungeon44 = findViewById(R.id.Dungeon44Button);
        dungeon44.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon44();
            }
        });
    }

    public void openDungeon41()
    {
        Intent intent = new Intent(this, Dungeon41.class);
        startActivity(intent);
    }

    public void openDungeon42()
    {
        Intent intent = new Intent(this, Dungeon42.class);
        startActivity(intent);
    }

    public void openDungeon42bonus()
    {
        Intent intent = new Intent(this, Dungeon42Bonus.class);
        startActivity(intent);
    }

    public void openDungeon43()
    {
        Intent intent = new Intent(this, Dungeon43.class);
        startActivity(intent);
    }

    public void openDungeon43bonus()
    {
        Intent intent = new Intent(this, Dungeon43Bonus.class);
        startActivity(intent);
    }

    public void openDungeon44()
    {
        Intent intent = new Intent(this, Dungeon44.class);
        startActivity(intent);
    }
}