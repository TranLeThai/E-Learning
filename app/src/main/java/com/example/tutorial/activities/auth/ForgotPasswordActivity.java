package com.example.tutorial.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorial.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnSend;
    private TextView tvBackToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        setupFirebase();
        setupClickListeners();
    }

    private void initViews() {
        edtEmail = findViewById(R.id.email);
        btnSend = findViewById(R.id.sendButton);
        tvBackToLogin = findViewById(R.id.backToLogin);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupClickListeners() {
        btnSend.setOnClickListener(v -> sendPasswordResetEmail());
        tvBackToLogin.setOnClickListener(v -> finish()); // Go back to LoginActivity
    }

    private void sendPasswordResetEmail() {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError("Please enter your email");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid email format");
            edtEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset link sent to your email.", Toast.LENGTH_LONG).show();
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Could not send email.";
                        Toast.makeText(ForgotPasswordActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
