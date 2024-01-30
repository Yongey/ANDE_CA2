package com.example.ca1_mainscreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StartChallenge extends AppCompatActivity {

    private TextView lastClickedTextView;
private LinearLayout learnNewLanguageLayout;
    private LinearLayout LoremLayout;
    private LinearLayout movieLayout;
    private LinearLayout WorkoutLayout;
    private LinearLayout studyLayout;
    private LinearLayout studyPlanLayout;
    private LinearLayout sampleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_challenge);
        learnNewLanguageLayout = findViewById(R.id.learnNewLanguageLayout);
        movieLayout= findViewById(R.id.movieLayout);
        WorkoutLayout = findViewById(R.id.WorkoutLayout);
        studyLayout = findViewById(R.id.studyLayout);
        LoremLayout=findViewById(R.id.LoremLayout);
        sampleLayout = findViewById(R.id.SampleLayout);
        // Find your TextViews by their IDs
        TextView newText = findViewById(R.id.newText);
        TextView health = findViewById(R.id.health);
        TextView learning = findViewById(R.id.learning);
        TextView enjoymen = findViewById(R.id.enjoymen);
        TextView learning1 = findViewById(R.id.learning1);
        TextView learning2 = findViewById(R.id.learning2);

        // Set the text to be clickable
        setClickableText(newText);
        setClickableText(health);
        setClickableText(learning);
        setClickableText(enjoymen);
        setClickableText(learning1);
        setClickableText(learning2);

        newText.setOnClickListener(v -> {
            toggleVisibility(learnNewLanguageLayout);
        });
        health.setOnClickListener(v -> {
            toggleVisibility(WorkoutLayout);
        });
        learning.setOnClickListener(v -> {
            toggleVisibility(studyLayout);
        });
        enjoymen.setOnClickListener(v -> {
            toggleVisibility( movieLayout);
        });
        learning1.setOnClickListener(v -> {
            toggleVisibility( LoremLayout);
        });
        learning2.setOnClickListener(v->{
            toggleVisibilityAll();
        });
        TextView sample = findViewById(R.id.sample);
        String textToPassSample = sample.getText().toString();
        TextView studyTextView = findViewById(R.id.studys);
        String textToPass = studyTextView.getText().toString();
        TextView gymTextView = findViewById(R.id.workout);
        String textToPass1 = gymTextView.getText().toString();
        TextView movieTextView = findViewById(R.id.movie);
        String textToPass2 = movieTextView.getText().toString();
        TextView languageTextView = findViewById(R.id.language);
        String textToPass3 = languageTextView.getText().toString();
        TextView loremTextView = findViewById(R.id.Lorem);
        String textToPass4 = loremTextView.getText().toString();
        sampleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartChallenge.this, SampleChallenge.class);
                intent.putExtra("clickedText",textToPassSample);
                startActivity(intent);
            }
        });
        studyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartChallenge.this, Test.class);
                intent.putExtra("clickedText",textToPass);
                startActivity(intent);
            }
        });
        movieLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartChallenge.this, Test.class);
                intent.putExtra("clickedText",textToPass2);
                startActivity(intent);
            }
        });
        WorkoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartChallenge.this, Test.class);
                intent.putExtra("clickedText",textToPass1);
                startActivity(intent);
            }
        });
        learnNewLanguageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartChallenge.this, Test.class);
                intent.putExtra("clickedText",textToPass3);
                startActivity(intent);
            }
        });
        LoremLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartChallenge.this, Test.class);
                intent.putExtra("clickedText",textToPass4);
                startActivity(intent);
            }
        });
    }

    private void setClickableText(final TextView textView) {
        final int originalColor = textView.getCurrentTextColor(); // Store the original text color
        final SpannableString spannableString = new SpannableString(textView.getText());

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                int colorPressed = getResources().getColor(R.color.colorPressed);

                // Reset the last clicked TextView to its original color and remove underline
                if (lastClickedTextView != null && lastClickedTextView != textView) {
                    SpannableString lastSpannable = new SpannableString(lastClickedTextView.getText());
                    removeSpans(lastSpannable, UnderlineSpan.class);
                    lastSpannable.setSpan(new ForegroundColorSpan(lastClickedTextView.getCurrentTextColor()), 0, lastSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lastClickedTextView.setText(lastSpannable);
                }

                // Apply underline and new color to the currently clicked TextView
                SpannableString currentSpannable = new SpannableString(textView.getText());
                currentSpannable.setSpan(new UnderlineSpan(), 0, currentSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                currentSpannable.setSpan(new ForegroundColorSpan(colorPressed), 0, currentSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(currentSpannable);
                textView.setTag(originalColor);
                // Update the last clicked TextView reference
                lastClickedTextView = textView;
            }
        };

        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT); // Prevent background color change on click
    }

    // Utility method to remove specific type of spans
    private <T> void removeSpans(SpannableString spannable, Class<T> type) {
        T[] spans = spannable.getSpans(0, spannable.length(), type);
        for (T span : spans) {
            spannable.removeSpan(span);
        }
    }
    private void toggleVisibility(LinearLayout layoutToShow) {
        // First, make all layouts gone
        learnNewLanguageLayout.setVisibility(View.GONE);
        LoremLayout.setVisibility(View.GONE);
        movieLayout.setVisibility(View.GONE);
        WorkoutLayout.setVisibility(View.GONE);
        studyLayout.setVisibility(View.GONE);

        // ... set other layouts to GONE ...

        // Now, make the selected layout visible
        layoutToShow.setVisibility(View.VISIBLE);
    }
    private void toggleVisibilityAll() {
        // First, make all layouts gone
        learnNewLanguageLayout.setVisibility(View.VISIBLE);
        LoremLayout.setVisibility(View.VISIBLE);
        movieLayout.setVisibility(View.VISIBLE);
        WorkoutLayout.setVisibility(View.VISIBLE);
        studyLayout.setVisibility(View.VISIBLE);
    }

}
