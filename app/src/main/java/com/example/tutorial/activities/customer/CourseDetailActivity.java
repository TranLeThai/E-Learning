package com.example.tutorial.activities.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tutorial.R;
import com.google.android.material.button.MaterialButton;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Ánh xạ nút "BẮT ĐẦU HỌC NGAY"
        // Lưu ý: Trong XML activity_course_detail của bạn chưa đặt ID cho nút này.
        // Bạn cần mở XML và thêm android:id="@+id/btnStartLearning" cho MaterialButton ở cuối file.
        MaterialButton btnStart = findViewById(R.id.btnStartLearning);

        // Xử lý nút Back trên Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Click Bắt đầu học -> Chuyển sang Flashcard
        if (btnStart != null) {
            btnStart.setOnClickListener(v -> {
                Intent intent = new Intent(CourseDetailActivity.this, FlashcardActivity.class);
                startActivity(intent);
            });
        }
    }
}