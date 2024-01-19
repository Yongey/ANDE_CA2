package com.example.ca1_mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Your existing TextView in LoginActivity layout
        TextView registerTextView = findViewById(R.id.tv_register);

        // Create a SpannableString
        String fullText = "Don't have an account? Sign Up here";
        SpannableString spannableString = new SpannableString(fullText);

        // Create a ClickableSpan for the "Sign Up here" part

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle click action, e.g., navigate to sign-up page
                // Add your code to navigate to the sign-up page
                navigateToSignUpPage();
                // Apply animation when clicked
                animateTextClick(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                // Disable the underline
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        };

        // Create a ForegroundColorSpan for the text color
        ForegroundColorSpan blueColorSpan = new ForegroundColorSpan(ContextCompat.getColor(Login.this, R.color.blue_color));

        // Set the ClickableSpan to the specific portion of the text
        spannableString.setSpan(clickableSpan, fullText.indexOf("Sign Up here"), fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the ForegroundColorSpan to the same portion of the text
        spannableString.setSpan(blueColorSpan, fullText.indexOf("Sign Up here"), fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        registerTextView.setText(spannableString);


        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());
        Button loginButton = findViewById(R.id.btn_login);

        // Set a click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click action, e.g., navigate to the main page
                navigateToMainPage();
                animateButtonOnClick(v);

            }
        });
    }
    private void navigateToSignUpPage() {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }
    private void navigateToMainPage() {
        Intent intent = new Intent(this, MainActivity.class); // Replace MainPage with the actual main page activity
        startActivity(intent);
    }
    private void animateTextClick(View view) {
        // Apply a scale animation
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100); // Set the duration of the animation
        scaleAnimation.setRepeatCount(1); // Repeat the animation
        scaleAnimation.setRepeatMode(Animation.REVERSE); // Reverse the animation after repeating

        view.startAnimation(scaleAnimation);
    }


    private void animateButtonOnClick(View view) {
        // Apply a scale animation
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(150); // Set the duration of the animation
        scaleAnimation.setRepeatCount(1); // Repeat the animation
        scaleAnimation.setRepeatMode(Animation.REVERSE); // Reverse the animation after repeating

        view.startAnimation(scaleAnimation);
    }





}
