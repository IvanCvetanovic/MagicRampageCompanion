package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Chapter5 extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter5);

        mediaPlayer = MediaPlayer.create(this, R.raw.button);

        ImageButton dungeon41 = findViewById(R.id.Dungeon41Button);
        dungeon41.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon41();
                playSound();
            }
        });

        ImageButton dungeon42 = findViewById(R.id.Dungeon42Button);
        dungeon42.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon42();
                playSound();
            }
        });

        ImageButton dungeon42bonus = findViewById(R.id.Dungeon42BonusButton);
        dungeon42bonus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon42bonus();
                playSound();
            }
        });

        ImageButton dungeon43 = findViewById(R.id.Dungeon43Button);
        dungeon43.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon43();
                playSound();
            }
        });

        ImageButton dungeon43bonus = findViewById(R.id.Dungeon43BonusButton);
        dungeon43bonus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon43bonus();
                playSound();
            }
        });

        ImageButton dungeon44 = findViewById(R.id.Dungeon44Button);
        dungeon44.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                openDungeon44();
                playSound();
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

    private void playSound() {
        // Check if MediaPlayer is null or not
        if (mediaPlayer != null) {
            // Reset MediaPlayer if it's already playing
            mediaPlayer.seekTo(0);

            // Set volume to 50%
            float volume = 0.25f; // 50%
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}