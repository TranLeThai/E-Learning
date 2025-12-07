package com.example.tutorial.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorial.R;
import com.example.tutorial.activities.customer.MainActivity;
import com.example.tutorial.models.UserProfile;
import com.example.tutorial.utils.ProfileManager;
import com.example.tutorial.utils.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private TextView tvForgotPassword;
    private Button logInBtn, signUpBtn;
    private MaterialButton btnGoogle;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    if (e.getStatusCode() == CommonStatusCodes.CANCELED) {
                        Toast.makeText(this, "Sign-in canceled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Google Sign-In error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        tvForgotPassword = findViewById(R.id.forgot_password);
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
        if (SessionManager.isLoggedIn(this)) {
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
            Toast.makeText(this, "Could not get account information", Toast.LENGTH_SHORT).show();
            return;
        }

        com.google.firebase.auth.AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveSessionAndProfile(user.getEmail(), user.getDisplayName());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "An unknown error occurred";
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loginUserWithEmail() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            saveSessionAndProfile(email, null);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Please verify your email address.", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "An unknown error occurred";
                        Toast.makeText(this, "Login failed: " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setupClickListeners() {
        logInBtn.setOnClickListener(v -> loginUserWithEmail());
        signUpBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        btnGoogle.setOnClickListener(v -> signInWithGoogle());
        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
    }

    private void saveSessionAndProfile(String email, String username) {
        SessionManager.saveSession(this, email);
        if (email != null && !email.isEmpty()) {
            UserProfile existing = ProfileManager.loadProfile(this, email);
            if (existing == null) {
                String name = (username != null && !username.isEmpty()) ? username : "User";
                UserProfile defaultProfile = new UserProfile(email, name, "", new java.util.ArrayList<>());
                ProfileManager.saveProfile(this, defaultProfile);
                ProfileManager.saveProfileRemote(defaultProfile);
            }
        }
    }
}
