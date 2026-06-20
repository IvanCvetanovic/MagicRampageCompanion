package xyz.magicrampagecompanion.ui.character;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

/**
 * Browse screen for the Character editor. Mirrors {@code LevelListActivity}: an "All" tab over the
 * bundled {@code assets/entities/*.character} files and a "My Characters" tab over
 * {@code filesDir/usercharacters}. Adds a search box (257 files). Tapping a row opens
 * {@link CharacterEditorActivity} with a {@code characterFile} (asset) or {@code characterPath}
 * (storage) extra, the same asset-vs-storage split the level tools use.
 */
public class CharacterListActivity extends BaseActivity {

    private static final String TAG = "CharacterListActivity";

    private final List<String> allFiles = new ArrayList<>(); // asset filenames ("skeleton.character")
    private final List<String> myPaths  = new ArrayList<>(); // absolute storage paths
    private final List<String> visible  = new ArrayList<>(); // current tab, filtered by search
    private int activeTab = 0; // 0 = all, 1 = my characters
    private String query = "";

    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private EditText searchBox;
    private Button tabAll, tabMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        recyclerView   = findViewById(R.id.characterRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        searchBox      = findViewById(R.id.characterSearch);
        tabAll         = findViewById(R.id.tabAll);
        tabMine        = findViewById(R.id.tabMine);

        View root   = findViewById(R.id.characterListRoot);
        View tabBar = findViewById(R.id.tabBar);
        final int baseTabL = tabBar.getPaddingLeft(), baseTabT = tabBar.getPaddingTop(),
                baseTabR = tabBar.getPaddingRight(), baseTabB = tabBar.getPaddingBottom();
        final int baseRvL = recyclerView.getPaddingLeft(), baseRvT = recyclerView.getPaddingTop(),
                baseRvR = recyclerView.getPaddingRight(), baseRvB = recyclerView.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            tabBar.setPadding(baseTabL, baseTabT + bars.top, baseTabR, baseTabB);
            recyclerView.setPadding(baseRvL, baseRvT, baseRvR, baseRvB + bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        ViewCompat.requestApplyInsets(root);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        loadAllCharacters();
        loadMyCharacters();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                query = s.toString().trim().toLowerCase(Locale.ROOT);
                applyFilter();
            }
        });

        tabAll.setOnClickListener(v -> { playClick(); setActiveTab(0); });
        tabMine.setOnClickListener(v -> { playClick(); setActiveTab(1); });

        setActiveTab(0);
    }

    private void setActiveTab(int tab) {
        activeTab = tab;
        tabAll.setSelected(tab == 0);
        tabMine.setSelected(tab == 1);
        emptyStateText.setText(tab == 1 ? R.string.empty_no_my_characters : R.string.empty_no_characters);
        applyFilter();
    }

    /** Rebuild {@link #visible} from the active tab, filtered by the search query. */
    private void applyFilter() {
        visible.clear();
        List<String> source = (activeTab == 0) ? allFiles : myPaths;
        for (String entry : source) {
            if (query.isEmpty() || displayName(entry).toLowerCase(Locale.ROOT).contains(query)) {
                visible.add(entry);
            }
        }
        showList();
    }

    private void showList() {
        boolean empty = visible.isEmpty();
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        emptyStateText.setVisibility(empty ? View.VISIBLE : View.GONE);

        recyclerView.setAdapter(new RecyclerView.Adapter<Holder>() {
            @NonNull @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_level_item, parent, false);
                return new Holder(v);
            }
            @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
                holder.bind(visible.get(position));
            }
            @Override public int getItemCount() { return visible.size(); }
        });
    }

    private void loadAllCharacters() {
        try {
            String[] files = getAssets().list("entities");
            if (files != null) {
                Arrays.sort(files, String::compareToIgnoreCase);
                for (String f : files) if (f.endsWith(".character")) allFiles.add(f);
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to list character assets", e);
        }
    }

    private void loadMyCharacters() {
        myPaths.clear();
        File dir = new File(getFilesDir(), "usercharacters");
        File[] files = dir.listFiles((d, n) -> n.endsWith(".character"));
        if (files != null) {
            Arrays.sort(files, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            for (File f : files) myPaths.add(f.getAbsolutePath());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyCharacters();
        if (activeTab == 1) applyFilter();
    }

    /** Filename stem shown in the list (e.g. "zombie-guard"). */
    private static String displayName(String entry) {
        String name = entry.contains("/") ? new File(entry).getName() : entry;
        return name.replaceAll("\\.character$", "");
    }

    private class Holder extends RecyclerView.ViewHolder {
        private final TextView name;
        Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.levelName);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;
                playClick();
                String entry = visible.get(pos);
                Intent intent = new Intent(CharacterListActivity.this, CharacterEditorActivity.class);
                if (activeTab == 1) intent.putExtra("characterPath", entry);
                else intent.putExtra("characterFile", entry);
                startActivity(intent);
            });
        }
        void bind(String entry) { name.setText(displayName(entry)); }
    }
}
