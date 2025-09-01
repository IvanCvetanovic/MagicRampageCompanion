package xyz.magicrampagecompanion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class News extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private TextView emptyStateTextView;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String GIST_ID = "5670c559e5a930129aa03dfce7827306";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        emptyStateTextView = findViewById(R.id.emptyStateTextView);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);

        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);

        loadNews();
        prepareTranslationModel();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void prepareTranslationModel() {
        String targetLanguage = Locale.getDefault().getLanguage();
        if (targetLanguage.equals(TranslateLanguage.ENGLISH)) {
            newsAdapter.setTranslationReady(true, targetLanguage);
            return;
        }

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLanguage)
                .build();

        com.google.mlkit.nl.translate.Translator modelManager = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder().build();
        modelManager.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(v -> {
                    Log.d("NewsActivity", "Model for " + targetLanguage + " is ready.");
                    newsAdapter.setTranslationReady(true, targetLanguage);
                    newsAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to download translation model.", Toast.LENGTH_LONG).show();
                });

        getLifecycle().addObserver(new LifecycleObserver() {
            // Use @OnLifecycleEvent to specify which event to listen for
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            void onDestroy() {
                modelManager.close();
            }
        });
    }

    private void loadNews() {
        executor.execute(() -> {
            final List<GistApi.GistNews> newsList = GistApi.fetchNewsFromComments(GIST_ID);
            handler.post(() -> {
                if (newsList != null && !newsList.isEmpty()) {
                    newsRecyclerView.setVisibility(View.VISIBLE);
                    emptyStateTextView.setVisibility(View.GONE);
                    newsAdapter.submitList(newsList);
                } else {
                    newsRecyclerView.setVisibility(View.GONE);
                    emptyStateTextView.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}