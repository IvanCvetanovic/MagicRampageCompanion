package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter3 extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3);

        mediaPlayer = MediaPlayer.create(this, R.raw.button);

        ImageButton dungeon21 = findViewById(R.id.Dungeon21Button);
            dungeon21.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
            openDungeon21();
                playSound();
            }
        });

        ImageButton dungeon22 = findViewById(R.id.Dungeon22Button);
        dungeon22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon22();
                playSound();
            }
        });

        ImageButton dungeon23 = findViewById(R.id.Dungeon23Button);
        dungeon23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon23();
                playSound();
            }
        });

        ImageButton dungeon23bonus = findViewById(R.id.Dungeon23BonusButton);
        dungeon23bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon23Bonus();
                playSound();
            }
        });

        ImageButton dungeon24 = findViewById(R.id.Dungeon24Button);
        dungeon24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon24();
                playSound();
            }
        });

        ImageButton dungeon25 = findViewById(R.id.Dungeon25Button);
        dungeon25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon25();
                playSound();
            }
        });

        ImageButton dungeon26 = findViewById(R.id.Dungeon26Button);
        dungeon26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon26();
                playSound();
            }
        });

        ImageButton dungeon27 = findViewById(R.id.Dungeon27Button);
        dungeon27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon27();
                playSound();
            }
        });

        ImageButton dungeon27Bonus = findViewById(R.id.Dungeon27BonusButton);
        dungeon27Bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon27Bonus();
                playSound();
            }
        });

        ImageButton dungeon28 = findViewById(R.id.Dungeon28Button);
        dungeon28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon28();
                playSound();
            }
        });

        ImageButton dungeon29 = findViewById(R.id.Dungeon29Button);
        dungeon29.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon29();
                playSound();
            }
        });

        ImageButton dungeon29bonus = findViewById(R.id.Dungeon29BonusButton);
        dungeon29bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon29bonus();
                playSound();
            }
        });

        ImageButton dungeon30 = findViewById(R.id.Dungeon30Button);
        dungeon30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDungeon30();
                playSound();
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