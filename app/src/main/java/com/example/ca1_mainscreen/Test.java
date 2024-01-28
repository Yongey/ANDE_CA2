package com.example.ca1_mainscreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {
    private List<CheckboxData> checkboxDataList = new ArrayList<>();
    private DatabaseReference databaseRef;
    private LinearLayout containerLayout;
    private String userId;
    private static final int MAX_CHECKBOX_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();

        }
        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("checkboxContainers").child(userId);

        // Container layout with purple background
        containerLayout = findViewById(R.id.containerLayout);
        containerLayout.setBackgroundColor(getResources().getColor(R.color.purple_200)); // Use your purple color here

        // Button to add checkboxes dynamically
        Button addCheckboxButton = findViewById(R.id.addCheckboxButton);
        addCheckboxButton.setOnClickListener(view -> addCheckboxToContainer(containerLayout));

        // Button to save checkboxes to Firebase
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> saveCheckboxStatesToFirebase());
    }

    private void addCheckboxToContainer(LinearLayout containerLayout) {
        if (checkboxDataList.size() >= MAX_CHECKBOX_COUNT) {
            // Show a message or toast to inform the user
            Toast.makeText(this, "Maximum of 6 checkboxes allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Checkbox Text");

        // Create a LinearLayout to hold the checkbox and delete button
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        CheckBox checkBox = new CheckBox(this);

        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f)); // Use layout_weight to make the checkbox expand
        rowLayout.addView(checkBox);

        // Create an EditText for text input in the AlertDialog
        final EditText inputText = new EditText(this);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT); // Set input type to single-line
        builder.setView(inputText);

        // Set positive and negative buttons for the AlertDialog
        builder.setPositiveButton("OK", (dialog, which) -> {
            String text = inputText.getText().toString();
            if (!text.isEmpty()) {
                // Text is not empty, create the checkbox
                CheckboxData checkboxData = new CheckboxData(text, false);
                checkboxDataList.add(checkboxData);

                checkBox.setText(text);
                checkBox.setEnabled(false); // Disable the checkbox

                // Create a delete button
                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                rowLayout.addView(deleteButton);

                // Set an OnClickListener for deleting the checkbox
                deleteButton.setOnClickListener(view -> {
                    checkboxDataList.remove(checkboxData);
                    containerLayout.removeView(rowLayout);
                });

                containerLayout.addView(rowLayout);
            } else {
                // Show a message to inform the user that text is required
                Toast.makeText(this, "Text is required for the checkbox", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the AlertDialog for text input
        builder.show();
    }


    private void saveCheckboxStatesToFirebase() {
        String containerId = databaseRef.push().getKey();
        CheckboxContainer checkboxContainer = new CheckboxContainer(containerId, checkboxDataList);
        databaseRef.child(containerId).setValue(checkboxContainer);
        if (containerLayout != null) {
            containerLayout.removeAllViews();
        }
        checkboxDataList.clear();
        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }
}
