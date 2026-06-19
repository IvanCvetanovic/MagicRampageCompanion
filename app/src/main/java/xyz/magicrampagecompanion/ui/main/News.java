package xyz.magicrampagecompanion.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.adapters.NewsAdapter;
import xyz.magicrampagecompanion.data.network.GistApi;
import xyz.magicrampagecompanion.data.storage.NewsCache;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class News extends BaseActivity {

    private RecyclerView newsRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private NewsAdapter newsAdapter;
    private TextView emptyStateTextView;
    private TextView translationLoadingText;
    private ProgressBar loadingSpinner;

    /** Synchronous "is there content on screen" flag (submitList is async). */
    private boolean hasContent = false;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String GIST_ID = "5670c559e5a930129aa03dfce7827306";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Edge-to-edge padding
        EdgeToEdge.enable(this);
        View root = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, 0);
            return insets;
        });

        emptyStateTextView = findViewById(R.id.emptyStateTextView);
        translationLoadingText = findViewById(R.id.loadingTranslationText);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);
        swipeRefresh.setOnRefreshListener(() -> loadNews(true));

        // Initial state
        swipeRefresh.setVisibility(View.GONE);
        translationLoadingText.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.GONE);

        // Show cached news instantly (also covers an offline launch).
        List<GistApi.GistNews> cached = NewsCache.load(this);
        if (cached != null && !cached.isEmpty()) {
            hasContent = true;
            emptyStateTextView.setVisibility(View.GONE);
            newsAdapter.submitList(cached);
        }

        loadNews(false);
        prepareTranslationModel();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void prepareTranslationModel() {
        String deviceLang = Locale.getDefault().getLanguage();
        String tmp = TranslateLanguage.fromLanguageTag(deviceLang);

        if (tmp == null) {
            tmp = TranslateLanguage.ENGLISH;
        }
        final String targetLang = tmp;

        // English → no translation needed
        if (TranslateLanguage.ENGLISH.equals(targetLang)) {
            newsAdapter.setTranslationReady(true, targetLang);
            swipeRefresh.setVisibility(View.VISIBLE);
            return;
        }

        // NOT English → show UI indicators
        translationLoadingText.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.GONE);

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLang)
                .build();

        com.google.mlkit.nl.translate.Translator modelManager =
                Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder().build();

        modelManager.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(v -> {
                    if (isFinishing() || isDestroyed()) return;
                    Log.d("NewsActivity", "Model for " + targetLang + " ready.");
                    newsAdapter.setTranslationReady(true, targetLang);
                    newsAdapter.notifyDataSetChanged();

                    // Hide loading UI
                    translationLoadingText.setVisibility(View.GONE);
                    loadingSpinner.setVisibility(View.GONE);
                    swipeRefresh.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    if (isFinishing() || isDestroyed()) return;
                    Toast.makeText(this,
                            R.string.translation_model_failed,
                            Toast.LENGTH_LONG).show();

                    // Show English version as fallback
                    translationLoadingText.setVisibility(View.GONE);
                    loadingSpinner.setVisibility(View.GONE);
                    swipeRefresh.setVisibility(View.VISIBLE);
                });

        getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event == Lifecycle.Event.ON_DESTROY) modelManager.close();
        });
    }

    private void loadNews(boolean userInitiated) {
        executor.execute(() -> {
            final List<GistApi.GistNews> newsList =
                    GistApi.fetchNewsFromComments(GIST_ID);

            handler.post(() -> {
                if (isFinishing() || isDestroyed()) return;
                swipeRefresh.setRefreshing(false);
                if (newsList != null && !newsList.isEmpty()) {
                    hasContent = true;
                    NewsCache.save(News.this, newsList);
                    emptyStateTextView.setVisibility(View.GONE);
                    newsAdapter.submitList(newsList);
                } else if (!hasContent) {
                    // Network failed and there is nothing cached to fall back on.
                    emptyStateTextView.setVisibility(View.VISIBLE);
                } else if (userInitiated) {
                    // Manual refresh failed, but cached content is still on screen.
                    Toast.makeText(News.this, R.string.news_refresh_failed, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

}
