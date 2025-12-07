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
import com.example.tutorial.models.UserProfile;
import com.example.tutorial.utils.ProfileManager;
import com.example.tutorial.utils.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnSignUp;
    private TextView tvBackToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        edtUsername = findViewById(R.id.edtSignUsername);
        edtEmail = findViewById(R.id.edtSignEmail);
        edtPassword = findViewById(R.id.editSignPassword);
        edtConfirmPassword = findViewById(R.id.edtPasswordConfirm);
        btnSignUp = findViewById(R.id.SignUpBtn);
        tvBackToLogin = findViewById(R.id.backToLogin);

        btnSignUp.setOnClickListener(e -> createUser());

        tvBackToLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void createUser() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (username.isEmpty()) {
            edtUsername.setError("Please enter a username");
            edtUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Please enter an email");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid email format");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Please enter a password");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Password must be at least 6 characters");
            edtPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Passwords do not match");
            edtConfirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(verificationTask -> {
                                        if (verificationTask.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, "Sign up successful. Please check your email for verification.", Toast.LENGTH_LONG).show();
                                            saveSessionAndProfile(email, username);
                                            mAuth.signOut(); // Sign out to force login after verification
                                            finish(); // Go back to LoginActivity
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        String err = task.getException() != null ? task.getException().getMessage() : "An unknown error occurred";
                        Toast.makeText(this, "Sign up failed: " + err, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveSessionAndProfile(String email, String username) {
        // This method now only creates the profile, but doesn't log the user in
        if (email != null && !email.isEmpty()) {
            UserProfile existing = ProfileManager.loadProfile(this, email);
            if (existing == null) {
                UserProfile defaultProfile = new UserProfile(email, username, "", new java.util.ArrayList<>());
                ProfileManager.saveProfile(this, defaultProfile);
                ProfileManager.saveProfileRemote(defaultProfile);
            }
        }
    }
}
