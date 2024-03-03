package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter4 extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4);

        mediaPlayer = MediaPlayer.create(this, R.raw.button);

        ImageButton dungeon31 = findViewById(R.id.Dungeon31Button);
        dungeon31.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon31();
                playSound();
            }
        });

        ImageButton dungeon31bonus = findViewById(R.id.Dungeon31BonusButton);
        dungeon31bonus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon31bonus();
                playSound();
            }
        });

        ImageButton dungeon32 = findViewById(R.id.Dungeon32Button);
        dungeon32.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon32();
                playSound();
            }
        });

        ImageButton dungeon33 = findViewById(R.id.Dungeon33Button);
        dungeon33.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon33();
                playSound();
            }
        });

        ImageButton dungeon34 = findViewById(R.id.Dungeon34Button);
        dungeon34.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon34();
                playSound();
            }
        });

        ImageButton dungeon35 = findViewById(R.id.Dungeon35Button);
        dungeon35.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon35();
                playSound();
            }
        });

        ImageButton dungeon36 = findViewById(R.id.Dungeon36Button);
        dungeon36.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon36();
                playSound();
            }
        });

        ImageButton dungeon37 = findViewById(R.id.Dungeon37Button);
        dungeon37.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon37();
                playSound();
            }
        });

        ImageButton dungeon38 = findViewById(R.id.Dungeon38Button);
        dungeon38.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon38();
                playSound();
            }
        });

        ImageButton dungeon39 = findViewById(R.id.Dungeon39Button);
        dungeon39.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon39();
                playSound();
            }
        });

        ImageButton dungeon40 = findViewById(R.id.Dungeon40Button);
        dungeon40.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon40();
                playSound();
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