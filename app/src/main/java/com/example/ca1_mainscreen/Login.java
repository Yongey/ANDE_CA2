package com.example.ca1_mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.res.ColorStateList;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView tvForget = findViewById(R.id.tv_forget);
        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, navigate to ResetPasswordActivity or any other activity
                Intent intent = new Intent(Login.this, ResetPassword.class);
                startActivity(intent);
            }
        });
        initializeViews();
        setupClickableText();
    }

    private void initializeViews() {
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        loginButton.setOnClickListener(v -> handleLogin());

        TextInputLayout passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout.setEndIconOnClickListener(v -> editTextPassword.getText().clear());
    }

    private void handleLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // Show an error message
            editTextEmail.setError("Email is required.");
            editTextEmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        showToast("Login Successfully!");
                        navigateToMainPage();
                    } else {
                        showToast("Authentication failed.");
                    }
                });
    }

    private void setupClickableText() {
        TextView registerTextView = findViewById(R.id.tv_register);
        String fullText = "Don't have an account? Sign Up here";
        SpannableString spannableString = new SpannableString(fullText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                navigateToSignUpPage();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(Login.this, R.color.blue_color));
            }
        };

        spannableString.setSpan(clickableSpan, fullText.indexOf("Sign Up here"), fullText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerTextView.setText(spannableString);
        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void navigateToSignUpPage() {
        startActivity(new Intent(this, Register.class));
    }

    private void navigateToMainPage() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            navigateToMainPage();
        }
    }
}
