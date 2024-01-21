package com.example.ca1_mainscreen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ResetPassword extends AppCompatActivity {

    private EditText editTextEmail;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        editTextEmail = findViewById(R.id.editTextEmail);
        Button btnResetPassword = findViewById(R.id.btnResetPassword);
        auth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = editTextEmail.getText().toString().trim();

        // Validate email
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ResetPassword.this, "Enter your registered email.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(ResetPassword.this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for internet connectivity
        if (!isNetworkConnected()) {
            Toast.makeText(ResetPassword.this, "No internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send password reset email
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the case where the user is not found
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                String errorCode = ((FirebaseAuthInvalidUserException) task.getException()).getErrorCode();
                                if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                                    Toast.makeText(ResetPassword.this, "User not registered with this email.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetPassword.this, "Failed to send password reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ResetPassword.this, "Failed to send password reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
