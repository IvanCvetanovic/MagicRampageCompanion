package xyz.magicrampagecompanion.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleHelper {
    private static final String APP_PREFERENCES = "AppPrefs";
    private static final String KEY_LANGUAGE = "language";

    // Save user choice
    public static void saveLanguage(Context context, String langCode) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, langCode).apply();
    }

    // Get saved language
    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "system");
    }

    // Apply locale to a context
    public static Context applyLocale(Context context) {
        String langCode = getLanguage(context);

        Locale locale;
        if ("system".equals(langCode)) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = new Locale(langCode);
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }
}
