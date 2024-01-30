package com.example.ca1_mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleChallenge extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference, userChallengesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_challenge);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String clickedText = extras.getString("clickedText");
            TextView sampleLanguageTextView = findViewById(R.id.SampleLanguage);
            if (sampleLanguageTextView != null) {
                sampleLanguageTextView.setText(clickedText);
            }
        }
    }

    public void onStartChallengeClick(View view) {
        List<String> selectedLanguages = getSelectedLanguages();
        String clickedText = getIntent().getStringExtra("clickedText");

        if (selectedLanguages.isEmpty()) {
            Toast.makeText(SampleChallenge.this, "Please select at least one language", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user != null) {
            userChallengesRef = databaseReference.child("challenges").child(user.getUid());

            userChallengesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(SampleChallenge.this, "You can only create one challenge", Toast.LENGTH_SHORT).show();
                    } else {
                        createChallengeInFirebase(clickedText, selectedLanguages);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    handleDatabaseError(databaseError);
                }
            });
        }
    }

    private void createChallengeInFirebase(String clickedText, List<String> selectedLanguages) {
        DatabaseReference challengesRef = databaseReference.child("challenges").child(user.getUid());
        DatabaseReference newChallengeRef = challengesRef.push();

        Map<String, Boolean> defaultCheckedStatus = createDefaultCheckedStatus(selectedLanguages);
        Challenge challenge = new Challenge(clickedText, selectedLanguages, defaultCheckedStatus);

        newChallengeRef.setValue(challenge, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    handleChallengeCreationSuccess(newChallengeRef);
                } else {
                    handleDatabaseError(databaseError);
                }
            }
        });
    }
    private Map<String, Boolean> createDefaultCheckedStatus(List<String> languages) {
        Map<String, Boolean> checkedStatus = new HashMap<>();
        for (String language : languages) {
            checkedStatus.put(language, false);
        }
        return checkedStatus;
    }

    private void handleChallengeCreationSuccess(DatabaseReference challengeRef) {
        Toast.makeText(SampleChallenge.this, "Challenge created successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SampleChallenge.this, MainActivity.class));
    }



    private void handleDatabaseError(DatabaseError databaseError) {
        Toast.makeText(SampleChallenge.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private List<String> getSelectedLanguages() {
        List<String> selectedLanguages = new ArrayList<>();
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
