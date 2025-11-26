package xyz.magicrampagecompanion.data.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import java.util.HashMap;
import java.util.Map;

import xyz.magicrampagecompanion.data.network.GistApi;
import xyz.magicrampagecompanion.R;

public class NewsAdapter extends ListAdapter<GistApi.GistNews, NewsAdapter.NewsViewHolder> {

    private boolean isTranslationReady = false;
    private String targetLanguage = TranslateLanguage.ENGLISH;
    private static final Map<String, String> translationCache = new HashMap<>();
    private static final String TAG = "NewsAdapter";

    public NewsAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setTranslationReady(boolean isReady, String language) {
        if (!this.targetLanguage.equals(language)) {
            translationCache.clear();
            Log.d(TAG, "Language changed to " + language + ". Cache cleared.");
        }
        this.isTranslationReady = isReady;
        this.targetLanguage = language;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        GistApi.GistNews newsItem = getItem(position);
        holder.bind(newsItem, isTranslationReady, targetLanguage);
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final TextView newsDate;
        private final TextView newsBody;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsDate = itemView.findViewById(R.id.newsDate);
            newsBody = itemView.findViewById(R.id.newsBody);
        }

        public void bind(GistApi.GistNews newsItem, boolean isTranslationReady, String targetLanguage) {
            newsDate.setText(newsItem.getDate());

            if (targetLanguage.equals(TranslateLanguage.ENGLISH)) {
                newsBody.setText(newsItem.getBody());
                return;
            }

            if (!isTranslationReady) {
                newsBody.setText(itemView.getContext().getString(R.string.translating));
                return;
            }

            String[] lines = newsItem.getBody().split("\n");
            final String[] translatedLines = new String[lines.length];

            for (int i = 0; i < lines.length; i++) {
                if (translationCache.containsKey(lines[i])) {
                    translatedLines[i] = translationCache.get(lines[i]);
                } else if (lines[i].trim().isEmpty()) {
                    translatedLines[i] = "";
                }
            }
            updateBodyText(translatedLines);

            for (int i = 0; i < lines.length; i++) {
                if (translatedLines[i] == null) {
                    final int index = i;
                    final String lineToTranslate = lines[i];

                    TranslatorOptions options = new TranslatorOptions.Builder()
                            .setSourceLanguage(TranslateLanguage.ENGLISH)
                            .setTargetLanguage(targetLanguage)
                            .build();
                    final Translator localTranslator = Translation.getClient(options);

                    localTranslator.translate(lineToTranslate)
                            .addOnSuccessListener(translatedText -> {
                                translationCache.put(lineToTranslate, translatedText);
                                translatedLines[index] = translatedText;
                                updateBodyText(translatedLines);
                                localTranslator.close();
                            })
                            .addOnFailureListener(e -> {
                                translatedLines[index] = lineToTranslate;
                                updateBodyText(translatedLines);
                                localTranslator.close();
                            });
                }
            }
        }

        private void updateBodyText(String[] lines) {
            StringBuilder finalBody = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                String lineToShow = (lines[i] != null) ? lines[i] : "...";
                finalBody.append(lineToShow);
                if (i < lines.length - 1) {
                    finalBody.append("\n");
                }
            }
            newsBody.setText(finalBody.toString());
        }
    }

    private static final DiffUtil.ItemCallback<GistApi.GistNews> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<GistApi.GistNews>() {
                @Override
                public boolean areItemsTheSame(@NonNull GistApi.GistNews oldItem, @NonNull GistApi.GistNews newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull GistApi.GistNews oldItem, @NonNull GistApi.GistNews newItem) {
                    return oldItem.getDate().equals(newItem.getDate()) &&
                            oldItem.getBody().equals(newItem.getBody());
                }
            };
}