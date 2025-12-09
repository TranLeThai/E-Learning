package com.example.tutorial.fragments; // Hoặc package tương ứng của bạn

import android.content.Intent;
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

import com.example.tutorial.R;
import com.example.tutorial.activities.auth.LoginActivity;
import com.example.tutorial.activities.customer.QuizActivity;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvLevel, tvSavedWordsCount, tvStreakCount, tvLogout;
    private Button btnFixGrammar;
    private ImageView ivAvatar;

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

        // Set content description for accessibility and use the image view so the field is accessed
        if (ivAvatar != null) {
            ivAvatar.setContentDescription(getStringSafe(R.string.profile_avatar_desc));
        }
    }

    private void loadUserData() {
        // Tại đây bạn sẽ lấy dữ liệu từ SharedPreference, Database hoặc API
        // Ví dụ giả lập: sử dụng chuỗi trong resources để tránh cảnh báo không dịch được
        tvName.setText(getStringSafe(R.string.profile_name_example));
        tvLevel.setText(getStringSafe(R.string.profile_level_example));
        tvSavedWordsCount.setText(getStringSafe(R.string.profile_saved_words_example));
        tvStreakCount.setText(getStringSafe(R.string.profile_streak_example));

        // Sử dụng ivAvatar để tránh cảnh báo field unused và cung cấp avatar mặc định
        if (ivAvatar != null) {
            try {
                ivAvatar.setImageResource(R.drawable.ic_launcher_foreground);
            } catch (Exception e) {
                // Nếu drawable không tồn tại thì bỏ qua
                Log.w("ProfileFragment", "Failed to set avatar drawable", e);
            }
        }
    }

    private void setupListeners() {
        // 1. Sự kiện click nút "Luyện tập khắc phục ngay"
        btnFixGrammar.setOnClickListener(v -> {
            // Chuyển sang màn hình Quiz hoặc bài học ôn tập
            // Giả sử bạn muốn chuyển sang QuizActivity để luyện tập
            if (getActivity() == null) {
                Log.w("ProfileFragment", "Activity is null, cannot start QuizActivity");
                return;
            }
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            intent.putExtra("TOPIC", "Past Simple Tense"); // Gửi kèm chủ đề cần ôn
            startActivity(intent);
        });

        // 2. Sự kiện click "Đăng xuất"
        tvLogout.setOnClickListener(v -> {
            // Xóa dữ liệu đăng nhập (nếu có lưu SharedPref)
            // ... logic xóa token ...

            if (getContext() != null) {
                Toast.makeText(getContext(), getStringSafe(R.string.profile_logged_out), Toast.LENGTH_SHORT).show();
            }

            // Chuyển về màn hình Login và xóa hết các màn hình cũ trong Stack
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Log.w("ProfileFragment", "Activity is null, cannot start LoginActivity");
            }
        });
    }

    /**
     * Helper để lấy string an toàn khi context null — trả fallback nếu cần
     */
    private String getStringSafe(int resId) {
        try {
            return getString(resId);
        } catch (Exception e) {
            // Nếu fragment chưa được attach, fallback ngắn
            Log.w("ProfileFragment", "getStringSafe failed", e);
            return "";
        }
    }
}