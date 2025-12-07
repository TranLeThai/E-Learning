package com.example.tutorial.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tutorial.R;
import com.example.tutorial.models.UserProfile;
import com.example.tutorial.utils.ProfileManager;
import com.example.tutorial.utils.SessionManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class EditProfileFragment extends Fragment {

    private ImageView ivAvatarEdit;
    private Button btnChangeAvatar, btnSaveProfile;
    private EditText edtName;
    private LinearLayout llCourses;

    private UserProfile profile;

    private ActivityResultLauncher<String> pickImageLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ivAvatarEdit = view.findViewById(R.id.ivAvatarEdit);
        btnChangeAvatar = view.findViewById(R.id.btnChangeAvatar);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        edtName = view.findViewById(R.id.edtName);
        llCourses = view.findViewById(R.id.llCourses);

        // register image picker
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                uploadAvatar(uri);
            }
        });

        loadProfile();

        btnChangeAvatar.setOnClickListener(v -> {
            // Launch gallery picker for images
            pickImageLauncher.launch("image/*");
        });

        btnSaveProfile.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void loadProfile() {
        Context ctx = getContext();
        if (ctx == null) return;
        String email = SessionManager.getUserEmail(ctx);
        if (email == null) return;

        profile = ProfileManager.loadProfile(getContext(), email);
        if (profile == null) return;

        edtName.setText(profile.getName());
        String avatar = profile.getAvatarUrl();
        if (avatar != null && !avatar.isEmpty()) {
            if (avatar.startsWith("http")) {
                Glide.with(this).load(avatar).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(ivAvatarEdit);
            }
        }

        // Fill courses
        llCourses.removeAllViews();
        List<String> courses = profile.getEnrolledCourseIds();
        for (String c : courses) {
            View row = LayoutInflater.from(getContext()).inflate(R.layout.item_course_row, llCourses, false);
            TextView tv = row.findViewById(R.id.tvCourseId);
            Button btn = row.findViewById(R.id.btnUnenroll);
            tv.setText(c);
            btn.setOnClickListener(v -> {
                profile.removeCourse(c);
                ProfileManager.saveProfile(getContext(), profile);
                ProfileManager.saveProfileRemote(profile);
                loadProfile();
            });
            llCourses.addView(row);
        }
    }

    private void uploadAvatar(Uri uri) {
        Context ctx = getContext();
        if (ctx == null || profile == null) return;
        String email = SessionManager.getUserEmail(ctx);
        if (email == null) return;

        Toast.makeText(ctx, "Uploading avatar...", Toast.LENGTH_SHORT).show();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("avatars/" + email + "_" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = storageRef.putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
            String url = downloadUri.toString();
            profile.setAvatarUrl(url);
            ProfileManager.saveProfile(getContext(), profile);
            ProfileManager.saveProfileRemote(profile);
            if (getActivity() != null) {
                Glide.with(this).load(url).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(ivAvatarEdit);
            }
            Toast.makeText(ctx, "Uploaded avatar", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(ctx, "Failed to get avatar URL: " + e.getMessage(), Toast.LENGTH_LONG).show()))
        .addOnFailureListener(e -> {
            Toast.makeText(ctx, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void saveProfile() {
        if (profile == null) return;
        profile.setName(edtName.getText().toString().trim());
        ProfileManager.saveProfile(getContext(), profile);
        ProfileManager.saveProfileRemote(profile);
        Toast.makeText(getContext(), "Đã lưu profile", Toast.LENGTH_SHORT).show();
        // pop back
        if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
    }
}
