package xyz.magicrampagecompanion.data.storage;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.data.network.GistApi;

/**
 * Small offline cache for the News screen. Persists the last successfully
 * fetched news list as JSON in SharedPreferences so the screen can show
 * content instantly and survive an offline launch.
 */
public final class NewsCache {
    private NewsCache() {}

    private static final String PREFS = "news_cache";
    private static final String KEY_JSON = "news_json";

    /** Persist the given news list (no-op on null/empty). */
    public static void save(Context ctx, List<GistApi.GistNews> news) {
        if (news == null || news.isEmpty()) return;
        JSONArray arr = new JSONArray();
        try {
            for (GistApi.GistNews n : news) {
                JSONObject o = new JSONObject();
                o.put("date", n.getDate());
                o.put("body", n.getBody());
                arr.put(o);
            }
        } catch (Exception ignored) {
            return;
        }
        ctx.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_JSON, arr.toString())
                .apply();
    }

    /** @return the cached news list, or null if nothing is cached / parse failed. */
    public static List<GistApi.GistNews> load(Context ctx) {
        String json = ctx.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getString(KEY_JSON, null);
        if (json == null) return null;
        try {
            JSONArray arr = new JSONArray(json);
            List<GistApi.GistNews> list = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                list.add(new GistApi.GistNews(o.optString("date"), o.optString("body")));
            }
            return list.isEmpty() ? null : list;
        } catch (Exception e) {
            return null;
        }
    }
}
