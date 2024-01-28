package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        // Retrieve the passed value
        String clickedText = getIntent().getStringExtra("clickedText");

        // Use the clickedText value, e.g., set it as a TextView text
        TextView textView = findViewById(R.id.study);
        textView.setText(clickedText);
    }
}