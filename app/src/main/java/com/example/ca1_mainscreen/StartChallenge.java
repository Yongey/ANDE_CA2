package com.example.ca1_mainscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StartChallenge extends AppCompatActivity {

    private TextView lastClickedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_challenge);

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


}
