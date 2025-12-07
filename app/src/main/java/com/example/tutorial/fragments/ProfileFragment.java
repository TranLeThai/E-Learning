package com.example.tutorial.fragments; // Hoặc package tương ứng của bạn

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tutorial.R;
import com.example.tutorial.activities.auth.LoginActivity;
import com.example.tutorial.activities.customer.QuizActivity;
import com.example.tutorial.models.UserProfile;
import com.example.tutorial.utils.ProfileManager;
import com.example.tutorial.utils.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvLevel, tvSavedWordsCount, tvStreakCount, tvLogout;
    private Button btnFixGrammar, btnEditProfile;
    private ImageView ivAvatar;

    private static final String PREFS_NAME = "tutorial_prefs";
    private static final String KEY_USER_EMAIL = "user_email";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gắn layout fragment_profile vào Fragment này
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews(view);
        loadUserData();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        tvName = view.findViewById(R.id.tvName);
        tvLevel = view.findViewById(R.id.tvLevel);
        tvSavedWordsCount = view.findViewById(R.id.tvSavedWordsCount);
        tvStreakCount = view.findViewById(R.id.tvStreakCount);
        tvLogout = view.findViewById(R.id.tvLogout);
        btnFixGrammar = view.findViewById(R.id.btnFixGrammar);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        // add edit button (if layout available)
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        if (ivAvatar != null) {
            ivAvatar.setContentDescription(getStringSafe(R.string.profile_avatar_desc));
        }
    }

    private void loadUserData() {
        Context ctx = getContext();
        String email = SessionManager.getUserEmail(ctx);

        UserProfile profile = null;
        if (email != null) profile = ProfileManager.loadProfile(getContext(), email);

        if (profile != null) {
            tvName.setText(profile.getName() != null && !profile.getName().isEmpty() ? profile.getName() : getStringSafe(R.string.profile_name_example));
            tvSavedWordsCount.setText(String.valueOf(profile.getEnrolledCourseIds().size()));
            tvLevel.setText(getStringSafe(R.string.profile_level_example));
            tvStreakCount.setText(getStringSafe(R.string.profile_streak_example));

            String avatar = profile.getAvatarUrl();
            if (avatar != null && !avatar.isEmpty()) {
                // Use Glide to load URL or resource name
                if (avatar.startsWith("http")) {
                    Glide.with(this)
                            .load(avatar)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(ivAvatar);
                } else {
                    int resId = getResources().getIdentifier(avatar, "drawable", getContext().getPackageName());
                    if (resId != 0) ivAvatar.setImageResource(resId);
                    else ivAvatar.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else {
                ivAvatar.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } else {
            // fallback to defaults
            tvName.setText(getStringSafe(R.string.profile_name_example));
            tvLevel.setText(getStringSafe(R.string.profile_level_example));
            tvSavedWordsCount.setText(getStringSafe(R.string.profile_saved_words_example));
            tvStreakCount.setText(getStringSafe(R.string.profile_streak_example));
            ivAvatar.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    private void setupListeners() {
        btnFixGrammar.setOnClickListener(v -> {
            if (getActivity() == null) {
                Log.w("ProfileFragment", "Activity is null, cannot start QuizActivity");
                return;
            }
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            intent.putExtra("TOPIC", "Past Simple Tense");
            startActivity(intent);
        });

        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                // open EditProfileFragment (replace), create if not exists
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new EditProfileFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        tvLogout.setOnClickListener(v -> {
            if (getContext() != null) {
                SessionManager.clearSession(getContext());
                try { FirebaseAuth.getInstance().signOut(); } catch (Exception ignored) {}
                try {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                    GoogleSignInClient googleClient = GoogleSignIn.getClient(getContext(), gso);
                    googleClient.signOut();
                } catch (Exception ignored) {}
                Toast.makeText(getContext(), getStringSafe(R.string.profile_logged_out), Toast.LENGTH_SHORT).show();
            }
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private String getStringSafe(int resId) {
        try { return getString(resId);} catch (Exception e) { Log.w("ProfileFragment","getStringSafe failed", e); return ""; }
    }
}
