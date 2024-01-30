package com.example.ca1_mainscreen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SampleChallenge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_challenge);

        // Get the intent that started this activity
        Bundle extras = getIntent().getExtras();

        // Check if the intent has extra data
        if (extras != null) {
            // Retrieve the text passed via intent
            String clickedText = extras.getString("clickedText");

            // Find the TextView with id SampleLanguage
            TextView sampleLanguageTextView = findViewById(R.id.SampleLanguage);

            // Set the text to the TextView
            if (sampleLanguageTextView != null) {
                sampleLanguageTextView.setText(clickedText);
            }
        }
    }
}
