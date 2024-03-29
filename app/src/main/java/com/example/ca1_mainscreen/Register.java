package com.example.ca1_mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    TextInputEditText editTextUsername, editTextEmail, editTextPassword;
    Button buttonReg;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // If the user is already logged in, redirect to the Login screen
        if (currentUser != null) {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progress_bar);


        buttonReg.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        mAuth = FirebaseAuth.getInstance();

        TextInputLayout passwordLayout = findViewById(R.id.passwordforsignup);

        // Set click listener to toggle password visibility and icon state
        passwordLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                if (editTextPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordLayout.setEndIconDrawable(R.drawable.ic_eye_show);
                } else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordLayout.setEndIconDrawable(R.drawable.ic_eye_hide);
                }
                editTextPassword.setSelection(editTextPassword.getText().length()); // Maintain cursor position
            }
        });
        TextView loginTextView = findViewById(R.id.tv_login);
        setupLoginTextView(loginTextView);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editTextUsername.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();

                // Input validation
                // ... [Your existing input validation code]

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Registration successful
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String userId = firebaseUser.getUid(); // Get the user ID
                                    FirebaseDatabase.getInstance().getReference("user/" + userId)
                                            .setValue(new User(username, email, ""))
                                            .addOnFailureListener(e -> {
                                                // Handle the error here
                                                Log.e("DatabaseError", "Failed to write data: " + e.getMessage());
                                                Toast.makeText(Register.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            });
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build();

                                    firebaseUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                                                    editTextUsername.setText("");
                                                    editTextEmail.setText("");
                                                    editTextPassword.setText("");
                                                    mAuth.signOut(); // Sign out the user after registration
                                                    navigateToLoginPage(); // Redirect to login page
                                                }
                                            });
                                }
                            } else {
                                // Registration failed
                                if (task.getException() != null) {
                                    Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(Register.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
    private void setupLoginTextView(TextView loginTextView) {
        String fullText = "Already have an account? Login here"; // This should match your text in the TextView
        SpannableString spannableString = new SpannableString(fullText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                navigateToLoginPage();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(Register.this, R.color.blue_color)); // Set color directly in the ClickableSpan
            }
        };

        int loginHereIndex = fullText.indexOf("Login here");
        if (loginHereIndex != -1) {
            spannableString.setSpan(clickableSpan, loginHereIndex, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            loginTextView.setText(spannableString);
            loginTextView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            loginTextView.setText(fullText); // Fallback in case the text doesn't match
        }
    }
    private void navigateToLoginPage() {
        startActivity(new Intent(this, Login.class));
        finish();
    }
}
