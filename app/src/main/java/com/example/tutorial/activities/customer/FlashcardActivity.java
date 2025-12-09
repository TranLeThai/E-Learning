package com.example.tutorial.activities.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.tutorial.R;

public class FlashcardActivity extends AppCompatActivity {

    private boolean isFlipped = false;
    private TextView tvWord, tvMeaning, tvPronunciation;
    private CardView cardFlashcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // Ánh xạ
        ImageView btnClose = findViewById(R.id.btnClose);
        cardFlashcard = findViewById(R.id.cardFlashcard);
        tvWord = findViewById(R.id.tvWord);
        tvMeaning = findViewById(R.id.tvMeaning);
        tvPronunciation = findViewById(R.id.tvPronunciation);

        // Xử lý đóng
        btnClose.setOnClickListener(v -> finish());

        // Xử lý lật thẻ (Logic đơn giản: Ẩn hiện TextView)
        cardFlashcard.setOnClickListener(v -> flipCard());

        // Nút "Đã thuộc" -> Chuyển sang Quiz (Ví dụ)
        // Trong thực tế, nút này sẽ chuyển sang từ tiếp theo
        findViewById(R.id.layoutButtons).setOnClickListener(v -> {
            // Logic chuyển từ tiếp theo ở đây
            Toast.makeText(this, "Đã lưu kết quả!", Toast.LENGTH_SHORT).show();

            // Demo: Chuyển sang Quiz sau khi học xong
            Intent intent = new Intent(FlashcardActivity.this, QuizActivity.class);
            startActivity(intent);
        });
    }

    private void flipCard() {
        if (isFlipped) {
            // Đang hiện nghĩa -> Quay về hiện từ
            tvMeaning.setVisibility(View.GONE);
            tvWord.setVisibility(View.VISIBLE);
            tvPronunciation.setVisibility(View.VISIBLE);
        } else {
            // Đang hiện từ -> Hiện nghĩa
            tvMeaning.setVisibility(View.VISIBLE);
            tvWord.setVisibility(View.GONE);
            tvPronunciation.setVisibility(View.GONE); // Tùy chọn
        }
        isFlipped = !isFlipped;
    }
}