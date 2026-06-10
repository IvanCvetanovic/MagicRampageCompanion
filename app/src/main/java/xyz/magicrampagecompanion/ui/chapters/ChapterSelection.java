package xyz.magicrampagecompanion.ui.chapters;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class ChapterSelection extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter_selection);

        bindChapterButton(R.id.Chapter1Button, Chapter1.class);
        bindChapterButton(R.id.Chapter2Button, Chapter2.class);
        bindChapterButton(R.id.Chapter3Button, Chapter3.class);
        bindChapterButton(R.id.Chapter4Button, Chapter4.class);
        bindChapterButton(R.id.Chapter5Button, Chapter5.class);
    }

    private void bindChapterButton(int buttonId, Class<? extends BaseActivity> chapter) {
        ImageButton button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            startActivity(new Intent(this, chapter));
            playClick();
        });
    }
}
