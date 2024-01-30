package com.example.ca1_mainscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SampleChallenge extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference,userChallengesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_challenge);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
    public void onStartChallengeClick(View view) {
        // Method called when the "Start Challenge" button is clicked

        // Get the selected checkboxes
        List<String> selectedLanguages = getSelectedLanguages();

        // Get the clicked text
        String clickedText = getIntent().getStringExtra("clickedText");

        // Pass data to Firebase
        if (user != null) {
            // Assuming you have a "challenges" node in your database
            userChallengesRef = databaseReference.child("challenges").child(user.getUid());

            userChallengesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User has already created a challenge, show a toast or handle accordingly
                        Toast.makeText(SampleChallenge.this, "You can only create one challenge", Toast.LENGTH_SHORT).show();
                    } else {
                        // User has not created a challenge, proceed to create and store in Firebase
                        DatabaseReference challengesRef = databaseReference.child("challenges").child(user.getUid());

                        // Create a new challenge object and push it to Firebase
                        Challenge challenge = new Challenge(clickedText, selectedLanguages);
                        challengesRef.push().setValue(challenge);

                        // You can also perform other actions or navigate to a new activity if needed
                        // For example:
                        // startActivity(new Intent(SampleChallenge.this, NextActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error if necessary
                }
            });
        }
    }

    private List<String> getSelectedLanguages() {
        List<String> selectedLanguages = new ArrayList<>();

        // Replace the individual CheckBox variables with an array or list
        int[] checkboxIds = {R.id.checkbox1, R.id.checkbox2, R.id.checkbox3, R.id.checkbox4, R.id.checkbox5, R.id.checkbox6};

        for (int checkboxId : checkboxIds) {
            CheckBox checkbox = findViewById(checkboxId);
            if (checkbox != null && checkbox.isChecked()) {
                selectedLanguages.add(checkbox.getText().toString());
            }
        }

        return selectedLanguages;
    }
}
