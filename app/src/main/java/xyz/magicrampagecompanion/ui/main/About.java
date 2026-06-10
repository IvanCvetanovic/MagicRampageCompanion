package xyz.magicrampagecompanion.ui.main;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class About extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
    }

    // ---------- Shared safe-open helper ----------
    private void openUrlWithFallback(String url, String playStorePkg, String clipboardLabel) {
        Uri uri = Uri.parse(url);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
        viewIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        try {
            startActivity(Intent.createChooser(viewIntent, getString(R.string.open_with)));
        } catch (ActivityNotFoundException e) {
            // Try Play Store for the corresponding app
            try {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + playStorePkg)
                ));
            } catch (ActivityNotFoundException e2) {
                // No Play Store either → copy link + toast
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText(clipboardLabel, url));
                }
                Toast.makeText(
                        this,
                        getString(R.string.no_app_to_open_link_copied),
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    // ---------- Social onClick handlers (referenced from XML) ----------
    public void openFacebook(View view) {
        playClick();
        // Facebook group
        openUrlWithFallback(
                "https://www.facebook.com/groups/magicrampage",
                "com.facebook.katana",
                "Facebook Group"
        );
    }

    public void openInstagram(View view) {
        playClick();
        openUrlWithFallback(
                "https://www.instagram.com/ivan_cvetanovich/",
                "com.instagram.android",
                "Instagram"
        );
    }

    public void openTwitter(View view) {
        playClick();
        // X/Twitter
        openUrlWithFallback(
                "https://x.com/PrOfS3S",
                "com.twitter.android",
                "Twitter"
        );
    }

    public void openDiscordInvite(View view) {
        playClick();
        openUrlWithFallback(
                "https://discord.gg/HcGA9x5erx",
                "com.discord",
                "Discord invite"
        );
    }
}
