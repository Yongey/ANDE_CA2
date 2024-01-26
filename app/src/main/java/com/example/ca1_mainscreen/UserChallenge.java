package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserChallenge extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_challenge);
        Button gotoNewPageButton = findViewById(R.id.bottomButton);
        gotoNewPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to run when the button is clicked
                Intent intent = new Intent(UserChallenge.this, Test.class);
                startActivity(intent);
            }
        });
        // Initialize Firebase Database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("checkboxContainers");

        // Create a layout to hold all the containers
        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        // Listen for changes in Firebase data
        databaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing containers
                parentLayout.removeAllViews();

                // List to hold CheckboxContainer objects with their timestamps
                List<Pair<Long, DataSnapshot>> containerList = new ArrayList<>();

                // Retrieve containers and add them to the list with their timestamp
                for (DataSnapshot containerSnapshot : dataSnapshot.getChildren()) {
                    CheckboxContainer container = containerSnapshot.getValue(CheckboxContainer.class);
                    if (container != null) {
                        long timestamp = container.getTimestamp();
                        containerList.add(new Pair<>(timestamp, containerSnapshot));
                    }
                }

                // Sort the list by the timestamp
                Collections.sort(containerList, new Comparator<Pair<Long, DataSnapshot>>() {
                    @Override
                    public int compare(Pair<Long, DataSnapshot> o1, Pair<Long, DataSnapshot> o2) {
                        return o1.first.compareTo(o2.first);
                    }
                });

                // Now create the containers in sorted order
                for (Pair<Long, DataSnapshot> pair : containerList) {
                    DataSnapshot containerSnapshot = pair.second;
                    CheckboxContainer checkboxContainer = containerSnapshot.getValue(CheckboxContainer.class);
                    List<CheckboxData> checkboxes = checkboxContainer.getCheckboxDataList();

                    LinearLayout containerLayout = new LinearLayout(UserChallenge.this);
                    containerLayout.setOrientation(LinearLayout.VERTICAL);
                    containerLayout.setBackgroundColor(getResources().getColor(R.color.nav)); // Set the container background color to purple

                    // Add layout parameters to the container with bottom margin
                    LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    containerParams.bottomMargin = 30; // Add bottom margin for spacing
                    containerLayout.setLayoutParams(containerParams);

                    // Add checkboxes to the container
                    for (CheckboxData checkboxData : checkboxes) {
                        CheckBox checkBox = new CheckBox(UserChallenge.this);
                        checkBox.setText(checkboxData.getText());
                        containerLayout.addView(checkBox);
                    }

                    parentLayout.addView(containerLayout);

                    // Add a divider View
                    View divider = new View(UserChallenge.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 20);
                    layoutParams.setMargins(0, 10, 0, 10);
                    divider.setLayoutParams(layoutParams);
                    divider.setBackgroundColor(getResources().getColor(android.R.color.white));
                    parentLayout.addView(divider);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }
    private void saveCheckboxStates(String containerId, List<CheckboxData> checkboxes) {
        DatabaseReference checkboxesRef = FirebaseDatabase.getInstance().getReference("checkboxContainers")
                .child(containerId).child("checkboxDataList");

        for (CheckboxData checkboxData : checkboxes) {
            checkboxesRef.child(checkboxData.getId()).child("isChecked").setValue(checkboxData.isChecked());
        }
    }
}
