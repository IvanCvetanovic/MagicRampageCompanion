package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class LevelListActivity extends BaseActivity {

    private static final String TAG = "LevelListActivity";

    private final List<String> storyFiles  = new ArrayList<>();
    private final List<String> otherFiles  = new ArrayList<>();
    private final List<String> myPaths     = new ArrayList<>(); // absolute paths of saved levels
    private List<String> activeList        = new ArrayList<>();
    private int activeTab = 0; // 0 = story, 1 = others, 2 = my levels

    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private Button tabStory, tabOthers, tabMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        recyclerView   = findViewById(R.id.levelRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        tabStory       = findViewById(R.id.tabStory);
        tabOthers      = findViewById(R.id.tabOthers);
        tabMine        = findViewById(R.id.tabMine);

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
        loadMyLevels();

        tabStory.setOnClickListener(v -> { playClick(); setActiveTab(0); });
        tabOthers.setOnClickListener(v -> { playClick(); setActiveTab(1); });
        tabMine.setOnClickListener(v -> { playClick(); setActiveTab(2); });

        // Default to Story tab
        setActiveTab(0);
    }

    private void setActiveTab(int tab) {
        activeTab = tab;
        tabStory.setSelected(tab == 0);
        tabOthers.setSelected(tab == 1);
        tabMine.setSelected(tab == 2);
        activeList = (tab == 0) ? storyFiles : (tab == 1) ? otherFiles : myPaths;
        emptyStateText.setText(tab == 2 ? R.string.empty_no_my_levels : R.string.empty_no_levels);
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
            Log.e(TAG, "Failed to list level assets", e);
        }
    }

    private void loadMyLevels() {
        myPaths.clear();
        File dir = new File(getFilesDir(), "userlevels");
        File[] files = dir.listFiles((d, n) -> n.endsWith(".esc"));
        if (files != null) {
            Arrays.sort(files, (a, b) -> compareNatural(a.getName(), b.getName()));
            for (File f : files) myPaths.add(f.getAbsolutePath());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyLevels(); // pick up levels saved since we last looked
        if (activeTab == 2) showList(activeList);
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

                playClick();

                String entry = activeList.get(pos);
                Intent intent = new Intent(LevelListActivity.this, LevelViewerActivity.class);
                if (activeTab == 2) intent.putExtra("levelPath", entry);
                else intent.putExtra("levelFile", entry);
                startActivity(intent);
            });
        }

        void bind(String fileName) {
            if (activeTab == 2) { // My Levels: arbitrary saved file names — show as-is
                name.setText(new File(fileName).getName().replaceAll("\\.esc$", ""));
                return;
            }
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
