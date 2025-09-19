package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
    }

    public void openDiscordInvite(View view) {
        final String inviteUrl = "https://discord.gg/HcGA9x5erx";
        Uri uri = Uri.parse(inviteUrl);

        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
        viewIntent.addCategory(Intent.CATEGORY_BROWSABLE);

        try {
            startActivity(Intent.createChooser(viewIntent, getString(R.string.open_with)));
        } catch (ActivityNotFoundException e) {
            // No browser/Discord available → try Play Store (Discord app)
            try {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.discord")
                ));
            } catch (ActivityNotFoundException e2) {
                // No Play Store either → copy to clipboard + toast
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) {
                    cm.setPrimaryClip(ClipData.newPlainText("Discord invite", inviteUrl));
                }
                Toast.makeText(
                        this,
                        getString(R.string.no_app_to_open_link_copied),
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
