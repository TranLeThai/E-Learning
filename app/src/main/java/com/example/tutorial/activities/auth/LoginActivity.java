package com.example.tutorial.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorial.activities.customer.MainActivity;
import com.example.tutorial.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private CheckBox checkBox;
    private Button logInBtn, signUpBtn;
    private MaterialButton btnGoogle;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    // Launcher cho Google Sign-In
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    if (e.getStatusCode() == CommonStatusCodes.CANCELED) {
                        Toast.makeText(this, "Đăng nhập bị hủy", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Lỗi Google Sign-In: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupFirebase();
        setupGoogleSignIn();
        setupClickListeners();

        checkIfLoggedIn();
    }

    private void initViews() {
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);
        logInBtn = findViewById(R.id.loginButton);
        signUpBtn = findViewById(R.id.signupButton);
        btnGoogle = findViewById(R.id.btn_google);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void checkIfLoggedIn() {
        // Kiểm tra Firebase trước
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if (account == null) {
            android.util.Log.e("LoginActivity", "Google account is null");
            Toast.makeText(this, "Không thể lấy thông tin tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        String idToken = account.getIdToken();
        if (idToken == null) {
            android.util.Log.e("LoginActivity", "ID Token is null");
            Toast.makeText(this, "Không thể lấy ID Token - Kiểm tra SHA-1 fingerprint", Toast.LENGTH_LONG).show();
            return;
        }

        android.util.Log.d("LoginActivity", "ID Token received: " + idToken.substring(0, 20) + "...");

        com.google.firebase.auth.AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        android.util.Log.d("LoginActivity", "Firebase auth successful");
                        Toast.makeText(LoginActivity.this, "Đăng nhập Google thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                        android.util.Log.e("LoginActivity", "Firebase auth failed: " + errorMsg, task.getException());
                        Toast.makeText(LoginActivity.this, "Xác thực thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loginUserWithEmail() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validate input
        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        if (!checkBox.isChecked()) {
            Toast.makeText(this, "Bạn chưa xác nhận không phải robot", Toast.LENGTH_SHORT).show();
            return;
        }

        // Special case - bypass Firebase
        if (email.equals("tlt@gmail.com") && password.equals("Thai123")) {
            Toast.makeText(this, "Chủ nhân đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return; // Quan trọng: phải return ở đây để không chạy code Firebase bên dưới
        }

        // Normal Firebase authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                        Toast.makeText(this, "Đăng nhập thất bại: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setupClickListeners() {
        logInBtn.setOnClickListener(v -> loginUserWithEmail());

        signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });

        btnGoogle.setOnClickListener(v -> signInWithGoogle());
    }
}