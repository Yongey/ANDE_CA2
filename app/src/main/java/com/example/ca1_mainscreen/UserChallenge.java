package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserChallenge extends AppCompatActivity {
    // Map to track the state of each checkbox within its container
    private Map<String, Boolean> checkboxStates = new HashMap<>();
    private String userId;
    private int remainingSaves = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_challenge);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userId = user.getUid();
            loadCheckboxes(userId); // Load checkboxes for the signed-in user

        } else {
            // Redirect to login activity or handle user not logged in
            redirectToLogin();
        }

        Button startCreateChallenge = findViewById(R.id.bottomButton);
        startCreateChallenge.setOnClickListener(view -> {
            Intent intent = new Intent(UserChallenge.this, StartChallenge.class);
            startActivity(intent);
        });

        Button saveStatusButton = findViewById(R.id.saveStatus);
        saveStatusButton.setOnClickListener(view -> saveCheckboxStatusToDatabase());


    }

    private void loadCheckboxes(String userId) {
        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("checkboxContainers").child(userId);

        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parentLayout.removeAllViews();
                for (DataSnapshot containerSnapshot : dataSnapshot.getChildren()) {
                    String containerId = containerSnapshot.getKey();
                    CardView cardView = new CardView(UserChallenge.this); // Create a new CardView
                    setupCardView(cardView); // Apply styling to CardView

                    LinearLayout containerLayout = new LinearLayout(UserChallenge.this);
                    containerLayout.setOrientation(LinearLayout.VERTICAL);
                    containerLayout.setPadding(10, 10, 10, 10);
                    cardView.addView(containerLayout);

                    TextView containerTitle = new TextView(UserChallenge.this);
                    containerTitle.setText(containerSnapshot.child("receivedText").getValue(String.class) + " in 30 Days Challenge");
                    containerTitle.setTypeface(null, Typeface.BOLD);
                    containerTitle.setTextColor(Color.BLACK);
                    containerTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    containerLayout.addView(containerTitle);

                    AtomicInteger checkBoxCount = new AtomicInteger();
                    AtomicInteger checkedCount = new AtomicInteger();

                    for (DataSnapshot checkboxSnapshot : containerSnapshot.child("checkboxDataList").getChildren()) {
                        CheckBox checkBox = new CheckBox(UserChallenge.this);
                        String checkboxId = checkboxSnapshot.getKey();
                        checkBox.setText(checkboxSnapshot.child("text").getValue(String.class));
                        Boolean isChecked = checkboxSnapshot.child("checked").getValue(Boolean.class);
                        checkBox.setChecked(isChecked != null ? isChecked : false);
                        checkBoxCount.getAndIncrement();
                        if (isChecked != null && isChecked) {
                            checkedCount.getAndIncrement();
                        }

                        checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
                            String key = containerId + "_" + checkboxId;
                            checkboxStates.put(key, isChecked1);
                            if (isChecked1) {
                                checkedCount.getAndIncrement();
                            } else {
                                checkedCount.getAndDecrement();
                            }

                        });

                        containerLayout.addView(checkBox);
                    }

                    // Add the delete button to the container layout

                    parentLayout.addView(cardView);
                    // Add a visual separator between containers for clarity
                    View separator = new View(UserChallenge.this);
                    separator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
                    separator.setBackgroundColor(Color.parseColor("#FF000000"));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }

    private void setupCardView(CardView cardView) {
        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardLayoutParams.setMargins(30, 20, 30, 20);
        cardView.setLayoutParams(cardLayoutParams);
        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
        cardView.setRadius(16);
        cardView.setCardElevation(8);
    }

    private void saveCheckboxStatusToDatabase() {
        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("checkboxContainers").child(userId);
        if (userId == null) {
            // Handle case where userId is null
            return;
        }
        Map<String, Object> updates = new HashMap<>();
        for (Map.Entry<String, Boolean> entry : checkboxStates.entrySet()) {
            String[] keys = entry.getKey().split("_");
            if (keys.length >= 2) {
                String containerId = keys[0];
                String checkboxId = keys[1];
                Boolean isChecked = entry.getValue();

                updates.put(containerId + "/checkboxDataList/" + checkboxId + "/checked", isChecked);
            }
            databaseUser.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        // Handle successful save here
                        Toast.makeText(UserChallenge.this, "All checkbox states saved successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failed save here
                        Toast.makeText(UserChallenge.this, "Failed to save checkbox states", Toast.LENGTH_SHORT).show();
                    });

        }


    }
    private void redirectToLogin() {
        // Redirect user to login activity
    }

}

