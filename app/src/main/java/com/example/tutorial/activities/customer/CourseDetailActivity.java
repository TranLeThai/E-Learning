package com.example.tutorial.activities.customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorial.R;
import com.example.tutorial.models.UserProfile;
import com.example.tutorial.utils.ProfileManager;
import com.example.tutorial.utils.SessionManager;

import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_COURSE_ID = "EXTRA_COURSE_ID";

    private TextView tvTitle, tvDesc;
    private Button btnEnroll;

    private String courseId;
    private UserProfile profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        tvTitle = findViewById(R.id.tvCourseTitle);
        tvDesc = findViewById(R.id.tvCourseDesc);
        btnEnroll = findViewById(R.id.btnEnroll);

        courseId = getIntent().getStringExtra(EXTRA_COURSE_ID);
        if (courseId == null) courseId = "course_unknown";

        tvTitle.setText("Khóa học: " + courseId);
        tvDesc.setText("Mô tả cho " + courseId);

        loadProfile();

        btnEnroll.setOnClickListener(v -> toggleEnroll());
        updateEnrollButton();
    }

    private void loadProfile() {
        String email = SessionManager.getUserEmail(this);
        if (email == null) return;
        profile = ProfileManager.loadProfile(this, email);
    }

    private void updateEnrollButton() {
        if (profile != null) {
            List<String> courses = profile.getEnrolledCourseIds();
            if (courses.contains(courseId)) {
                btnEnroll.setText("Hủy đăng ký");
            } else {
                btnEnroll.setText("Đăng ký");
            }
        }
    }

    private void toggleEnroll() {
        if (profile == null) {
            Toast.makeText(this, "Không tìm thấy profile", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> courses = profile.getEnrolledCourseIds();
        if (courses.contains(courseId)) {
            profile.removeCourse(courseId);
            Toast.makeText(this, "Đã hủy đăng ký", Toast.LENGTH_SHORT).show();
        } else {
            profile.addCourse(courseId);
            Toast.makeText(this, "Đã đăng ký", Toast.LENGTH_SHORT).show();
        }

        ProfileManager.saveProfile(this, profile);
        ProfileManager.saveProfileRemote(profile);
        updateEnrollButton();
    }
}
