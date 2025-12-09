package com.example.tutorial.activities.customer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.tutorial.R;
import com.google.android.material.button.MaterialButton;

public class QuizActivity extends AppCompatActivity {

    // Khai báo view
    private Button btnOption1, btnOption2, btnOption3;
    private MaterialButton btnCheck;
    private TextView tvQuestion;

    // Biến lưu trạng thái
    private String selectedAnswer = ""; // Đáp án người dùng chọn
    private final String CORRECT_ANSWER = "git add ."; // Đáp án đúng (Hardcode cho ví dụ này)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnCheck = findViewById(R.id.btnCheckAnswer);
        tvQuestion = findViewById(R.id.tvQuestion);
    }

    private void setupListeners() {
        // Sự kiện click cho từng đáp án
        btnOption1.setOnClickListener(v -> selectOption(btnOption1, "git push"));
        btnOption2.setOnClickListener(v -> selectOption(btnOption2, "git add ."));
        btnOption3.setOnClickListener(v -> selectOption(btnOption3, "git commit"));

        // Sự kiện nút kiểm tra
        btnCheck.setOnClickListener(v -> checkAnswer());
    }

    // Hàm xử lý khi chọn 1 đáp án
    private void selectOption(Button selectedBtn, String answerValue) {
        // 1. Reset giao diện tất cả các nút về mặc định (chưa chọn)
        resetButtonStyles();

        // 2. Cập nhật giao diện nút ĐƯỢC CHỌN
        // Đổi background sang loại "selected" (viền xanh, hoặc nền xanh nhạt tùy file xml của bạn)
        selectedBtn.setBackgroundResource(R.drawable.bg_quiz_option_selected);

        // Đổi màu chữ cho nổi bật (ví dụ màu xanh Teal)
        selectedBtn.setTextColor(Color.parseColor("#009688"));

        // 3. Lưu giá trị đáp án
        selectedAnswer = answerValue;
    }

    // Hàm reset giao diện các nút
    private void resetButtonStyles() {
        // Danh sách các nút
        Button[] options = {btnOption1, btnOption2, btnOption3};

        for (Button btn : options) {
            // Đặt lại background bình thường
            btn.setBackgroundResource(R.drawable.bg_quiz_option_normal);
            // Đặt lại màu chữ đen (hoặc màu mặc định của bạn)
            btn.setTextColor(Color.BLACK);
        }
    }

    // Hàm kiểm tra đúng sai
    private void checkAnswer() {
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn một đáp án!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedAnswer.equals(CORRECT_ANSWER)) {
            // Xử lý khi ĐÚNG
            Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
            // Có thể thêm logic chuyển câu tiếp theo ở đây
        } else {
            // Xử lý khi SAI
            Toast.makeText(this, "Sai rồi, thử lại nhé!", Toast.LENGTH_SHORT).show();
        }
    }
}