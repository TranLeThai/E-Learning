package com.example.tutorial.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SessionManager {
    private static final String PREFS = "tutorial_prefs_encrypted";
    private static final String KEY_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_EMAIL = "user_email";

    private static SharedPreferences getPrefs(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    context,
                    PREFS,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        }
    }

    public static void saveSession(Context context, String email) {
        SharedPreferences prefs = getPrefs(context);
        prefs.edit().putBoolean(KEY_LOGGED_IN, true).putString(KEY_USER_EMAIL, email != null ? email : "").apply();
    }

    public static void clearSession(Context context) {
        SharedPreferences prefs = getPrefs(context);
        prefs.edit().remove(KEY_LOGGED_IN).remove(KEY_USER_EMAIL).apply();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences prefs = getPrefs(context);
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = getPrefs(context);
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }
}

