package xyz.magicrampagecompanion.data.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GistApi {

    private static final String TAG = "GistApi";

    // Represents a single news item
    public static class GistNews {
        private final String id;
        private final String date;
        private final String body;

        public GistNews(String date, String body) {
            this.date = date;
            this.body = body;
            // Create a simple unique ID from the content
            this.id = String.valueOf((date + body).hashCode());
        }
        public String getId() { return id; }
        public String getDate() { return date; }
        public String getBody() { return body; }
    }

    // Fetches and parses the comments from the GitHub API
    public static List<GistNews> fetchNewsFromComments(String gistId) {
        String apiUrl = "https://api.github.com/gists/" + gistId + "/comments";
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        result.append(line);
                    }
                }
                return parseCommentsJson(result.toString());
            }
            Log.w(TAG, "HTTP " + conn.getResponseCode() + " fetching gist comments");
        } catch (Exception e) {
            Log.e(TAG, "Failed to fetch news", e);
        } finally {
            if (conn != null) conn.disconnect();
        }
        return null;
    }

    private static List<GistNews> parseCommentsJson(String jsonContent) {
        List<GistNews> newsList = new ArrayList<>();
        try {
            JSONArray commentsArray = new JSONArray(jsonContent);
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject comment = commentsArray.getJSONObject(i);
                String fullBody = comment.getString("body");
                String[] parts = fullBody.trim().split("\\r?\\n", 2);
                if (parts.length > 0) {
                    String dateLine = parts[0];
                    String bodyContent = (parts.length > 1) ? parts[1].trim() : "";
                    newsList.add(new GistNews(dateLine, bodyContent));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse news JSON", e);
            return null;
        }
        Collections.reverse(newsList); // Show newest first
        return newsList;
    }
}