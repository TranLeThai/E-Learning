package com.example.tutorial.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.tutorial.models.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileManager {
    private static final String PREFS = "tutorial_prefs_encrypted";
    private static final String KEY_PROFILE_PREFIX = "profile_"; // profile_<email>

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
            // Fallback to regular prefs if encrypted prefs unavailable
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        }
    }

    public static void saveProfile(Context context, UserProfile profile) {
        if (context == null || profile == null) return;
        SharedPreferences prefs = getPrefs(context);
        prefs.edit().putString(KEY_PROFILE_PREFIX + profile.getEmail(), profile.toJson()).apply();
    }

    public static UserProfile loadProfile(Context context, String email) {
        if (context == null || email == null) return null;
        SharedPreferences prefs = getPrefs(context);
        String json = prefs.getString(KEY_PROFILE_PREFIX + email, null);
        return UserProfile.fromJson(json);
    }

    public static void deleteProfile(Context context, String email) {
        if (context == null || email == null) return;
        SharedPreferences prefs = getPrefs(context);
        prefs.edit().remove(KEY_PROFILE_PREFIX + email).apply();
    }

    // Remote sync using Firestore
    public interface LoadCallback {
        void onLoaded(UserProfile profile);
        void onError(Exception e);
    }

    public static void saveProfileRemote(UserProfile profile) {
        if (profile == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_profiles")
                .document(profile.getEmail())
                .set(profileToMap(profile))
                .addOnSuccessListener(aVoid -> {
                    // no-op
                })
                .addOnFailureListener(e -> {
                    // should handle retry logic in production
                    e.printStackTrace();
                });
    }

    public static void loadProfileRemote(String email, LoadCallback callback) {
        if (email == null || callback == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_profiles").document(email).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        UserProfile profile = mapToProfile(documentSnapshot);
                        callback.onLoaded(profile);
                    } else {
                        callback.onLoaded(null);
                    }
                })
                .addOnFailureListener(e -> callback.onError(e));
    }

    private static java.util.Map<String, Object> profileToMap(UserProfile p) {
        java.util.Map<String, Object> m = new java.util.HashMap<>();
        m.put("email", p.getEmail());
        m.put("name", p.getName());
        m.put("avatarUrl", p.getAvatarUrl());
        m.put("courses", p.getEnrolledCourseIds());
        return m;
    }

    private static UserProfile mapToProfile(DocumentSnapshot doc) {
        if (doc == null) return null;
        String email = doc.getString("email");
        String name = doc.getString("name");
        String avatar = doc.getString("avatarUrl");
        java.util.List<String> courses = (java.util.List<String>) doc.get("courses");
        return new UserProfile(email != null ? email : "", name != null ? name : "", avatar != null ? avatar : "", courses != null ? courses : new java.util.ArrayList<>());
    }
}
