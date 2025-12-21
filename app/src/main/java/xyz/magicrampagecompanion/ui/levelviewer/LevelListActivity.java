package xyz.magicrampagecompanion.ui.levelviewer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    private final List<String> levelFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        // ---- APPLY SYSTEM INSETS (FIX) ----
        View root = findViewById(R.id.levelRecyclerView);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft(),
                    sysBars.top,
                    v.getPaddingRight(),
                    sysBars.bottom
            );
            return WindowInsetsCompat.CONSUMED;
        });

        RecyclerView recyclerView = findViewById(R.id.levelRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        loadLevelFiles();

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
                holder.bind(levelFiles.get(position));
            }

            @Override
            public int getItemCount() {
                return levelFiles.size();
            }
        });
    }

    private void loadLevelFiles() {
        try {
            String[] files = getAssets().list("levels");
            if (files != null) {
                Arrays.sort(files);
                for (String f : files) {
                    if (f.endsWith(".esc")) {
                        levelFiles.add(f);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class LevelViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;

        LevelViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.levelName);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                String file = levelFiles.get(pos);

                Intent intent = new Intent(
                        LevelListActivity.this,
                        LevelViewerActivity.class
                );
                intent.putExtra("levelFile", file);
                startActivity(intent);
            });
        }

        void bind(String fileName) {
            name.setText(fileName.replace(".esc", ""));
        }
    }
}
