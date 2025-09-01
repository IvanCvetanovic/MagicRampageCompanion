package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

public class Dungeon14 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dungeon14);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}