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

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.adapters.NewsAdapter;
import xyz.magicrampagecompanion.data.network.GistApi;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;

public class News extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private TextView emptyStateTextView;
    private TextView translationLoadingText;
    private ProgressBar loadingSpinner;

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

        newsAdapter = new NewsAdapter();
        newsRecyclerView.setAdapter(newsAdapter);

        // Initial state
        newsRecyclerView.setVisibility(View.GONE);
        translationLoadingText.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.GONE);

        loadNews();
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
            newsRecyclerView.setVisibility(View.VISIBLE);
            return;
        }

        // NOT English → show UI indicators
        translationLoadingText.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.VISIBLE);
        newsRecyclerView.setVisibility(View.GONE);

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLang)
                .build();

        com.google.mlkit.nl.translate.Translator modelManager =
                Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder().build();

        modelManager.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(v -> {
                    Log.d("NewsActivity", "Model for " + targetLang + " ready.");
                    newsAdapter.setTranslationReady(true, targetLang);
                    newsAdapter.notifyDataSetChanged();

                    // Hide loading UI
                    translationLoadingText.setVisibility(View.GONE);
                    loadingSpinner.setVisibility(View.GONE);
                    newsRecyclerView.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Failed to download translation model.",
                            Toast.LENGTH_LONG).show();

                    // Show English version as fallback
                    translationLoadingText.setVisibility(View.GONE);
                    loadingSpinner.setVisibility(View.GONE);
                    newsRecyclerView.setVisibility(View.VISIBLE);
                });

        getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            void onDestroy() { modelManager.close(); }
        });
    }

    private void loadNews() {
        executor.execute(() -> {
            final List<GistApi.GistNews> newsList =
                    GistApi.fetchNewsFromComments(GIST_ID);

            handler.post(() -> {
                if (newsList != null && !newsList.isEmpty()) {
                    emptyStateTextView.setVisibility(View.GONE);
                    newsAdapter.submitList(newsList);
                } else {
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
