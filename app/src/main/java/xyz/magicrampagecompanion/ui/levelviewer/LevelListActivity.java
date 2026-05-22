package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import xyz.magicrampagecompanion.core.utils.LocaleHelper;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.magicrampagecompanion.R;

public class LevelListActivity extends AppCompatActivity {

    private final List<String> storyFiles  = new ArrayList<>();
    private final List<String> otherFiles  = new ArrayList<>();
    private List<String> activeList        = new ArrayList<>();

    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private Button tabStory, tabOthers;

    private SoundPool soundPool;
    private int clickSfxId = 0;
    private boolean clickSfxLoaded = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().post(this::initSoundPoolIfNeeded);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseSoundPool();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSoundPool();
    }

    private void initSoundPoolIfNeeded() {
        if (soundPool != null) return;
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build())
                .build();
        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0 && sampleId == clickSfxId) clickSfxLoaded = true;
        });
        clickSfxId = soundPool.load(this, R.raw.click, 1);
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
            clickSfxLoaded = false;
            clickSfxId = 0;
        }
    }

    private void playSound() {
        if (soundPool != null && clickSfxLoaded)
            soundPool.play(clickSfxId, 0.25f, 0.25f, 1, 0, 1.0f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        recyclerView   = findViewById(R.id.levelRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        tabStory       = findViewById(R.id.tabStory);
        tabOthers      = findViewById(R.id.tabOthers);

        // Apply system insets: status bar to tab bar top, nav bar to recycler bottom
        View root    = findViewById(R.id.levelListRoot);
        View tabBar  = findViewById(R.id.tabBar);
        final int baseTabT = tabBar.getPaddingTop();
        final int baseTabL = tabBar.getPaddingLeft();
        final int baseTabR = tabBar.getPaddingRight();
        final int baseTabB = tabBar.getPaddingBottom();
        final int baseRvB  = recyclerView.getPaddingBottom();
        final int baseRvL  = recyclerView.getPaddingLeft();
        final int baseRvT  = recyclerView.getPaddingTop();
        final int baseRvR  = recyclerView.getPaddingRight();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            tabBar.setPadding(baseTabL, baseTabT + sysBars.top, baseTabR, baseTabB);
            recyclerView.setPadding(baseRvL, baseRvT, baseRvR, baseRvB + sysBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        ViewCompat.requestApplyInsets(root);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        loadLevelFiles();

        tabStory.setOnClickListener(v -> { playSound(); setActiveTab(true); });
        tabOthers.setOnClickListener(v -> { playSound(); setActiveTab(false); });

        // Default to Story tab
        setActiveTab(true);
    }

    private void setActiveTab(boolean story) {
        tabStory.setSelected(story);
        tabOthers.setSelected(!story);
        activeList = story ? storyFiles : otherFiles;
        showList(activeList);
    }

    private void showList(List<String> files) {
        boolean empty = files.isEmpty();
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        emptyStateText.setVisibility(empty ? View.VISIBLE : View.GONE);

        recyclerView.setAdapter(new RecyclerView.Adapter<LevelViewHolder>() {
            @NonNull
            @Override
            public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_level_item, parent, false);
                return new LevelViewHolder(v);
            }

            @Override
            public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
                holder.bind(files.get(position));
            }

            @Override
            public int getItemCount() { return files.size(); }
        });
    }

    private void loadLevelFiles() {
        try {
            String[] files = getAssets().list("levels");
            if (files != null) {
                Arrays.sort(files, LevelListActivity::compareNatural);
                for (String f : files) {
                    if (!f.endsWith(".esc")) continue;
                    if (f.matches("dungeon\\d+.*\\.esc")) {
                        storyFiles.add(f);
                    } else {
                        otherFiles.add(f);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int compareNatural(String a, String b) {
        int ia = 0, ib = 0;
        while (ia < a.length() && ib < b.length()) {
            char ca = a.charAt(ia), cb = b.charAt(ib);
            if (Character.isDigit(ca) && Character.isDigit(cb)) {
                int sa = ia, sb = ib;
                while (ia < a.length() && Character.isDigit(a.charAt(ia))) ia++;
                while (ib < b.length() && Character.isDigit(b.charAt(ib))) ib++;
                int cmp = Integer.compare(
                        Integer.parseInt(a.substring(sa, ia)),
                        Integer.parseInt(b.substring(sb, ib)));
                if (cmp != 0) return cmp;
            } else {
                if (ca != cb) return Character.compare(ca, cb);
                ia++;
                ib++;
            }
        }
        return a.length() - b.length();
    }

    private class LevelViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;

        LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.levelName);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                playSound();

                String file = activeList.get(pos);
                Intent intent = new Intent(LevelListActivity.this, LevelViewerActivity.class);
                intent.putExtra("levelFile", file);
                startActivity(intent);
            });
        }

        void bind(String fileName) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("dungeon(\\d+)")
                    .matcher(fileName.replace(".esc", ""));
            String key = m.find()
                    ? m.replaceFirst("dungeon_" + (Integer.parseInt(m.group(1)) + 1)).replace(".", "_")
                    : fileName.replace(".esc", "").replace("-", "_").replace(".", "_").toLowerCase();
            int resId = getResources().getIdentifier(key, "string", getPackageName());
            name.setText(resId != 0 ? getString(resId) : fileName.replace(".esc", ""));
        }
    }
}
