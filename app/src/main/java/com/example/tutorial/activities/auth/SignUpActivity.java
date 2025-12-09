package com.example.tutorial.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorial.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private Button signUp;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtSignEmail);
        edtPassword = findViewById(R.id.editSignPassword);
        edtConfirmPassword = findViewById(R.id.edtPasswordConfirm);
        signUp = findViewById(R.id.SignUpBtn);

        signUp.setOnClickListener(e -> CreateUser());
    }
    private void CreateUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                edtEmail.setError("vui Lòng nhập email");
                edtEmail.requestFocus(); // focus the cursor
                return;
            }

            if (password.isEmpty()) {
                edtPassword.setError("vui lòng nhập mật khẩu");
                edtPassword.requestFocus();
                return;
            }
            Toast.makeText(this,"vui lòng nhập vào email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (edtPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu của bạn phải 6 kí tự trở lên", Toast.LENGTH_SHORT).show();
        }

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        if (!hasUpper || !hasLower || !hasDigit || !hasSpecial) {
            Toast.makeText(this,
                    "Mật khẩu phải có: chữ hoa, chữ thường, số và ký tự đặc biệt",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(edtConfirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu không khớp");
            edtConfirmPassword.requestFocus();
            return;
        }

        // Nếu vượt qua tất cả -> tiến hành đăng nhập
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar2.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        String err = task.getException().getMessage();
                        Toast.makeText(this, "Đăng ký thất bại: " +
                                err, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
