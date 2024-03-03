package xyz.magicrampagecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ChapterSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chapter_selection);

        ImageButton chapter1 = findViewById(R.id.Chapter1Button);
        chapter1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openChapter1();
            }
        });

        ImageButton chapter2 = findViewById(R.id.Chapter2Button);
        chapter2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openChapter2();
            }
        });

        ImageButton chapter3 = findViewById(R.id.Chapter3Button);
        chapter3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openChapter3();
            }
        });

        ImageButton chapter4 = findViewById(R.id.Chapter4Button);
        chapter4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openChapter4();
            }
        });

        ImageButton chapter5 = findViewById(R.id.Chapter5Button);
        chapter5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openChapter5();
            }
        });

    }
        public void openChapter1()
        {
            Intent intent = new Intent(this, Chapter1.class);
            startActivity(intent);
        }

        public void openChapter2()
        {
            Intent intent = new Intent(this, Chapter2.class);
            startActivity(intent);
        }

        public void openChapter3()
        {
            Intent intent = new Intent(this, Chapter3.class);
            startActivity(intent);
        }

    public void openChapter4()
    {
        Intent intent = new Intent(this, Chapter4.class);
        startActivity(intent);
    }

    public void openChapter5()
    {
        Intent intent = new Intent(this, Chapter5.class);
        startActivity(intent);
    }
}