package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void openDiscordInvite(View view) {
        // Define the Discord server invite URL
        String discordInviteUrl = "https://discord.gg/HcGA9x5erx";

        // Create an Intent with action view and set the URL as data
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(discordInviteUrl));

        // Start the activity (browser or Discord app)
        startActivity(intent);
    }
}