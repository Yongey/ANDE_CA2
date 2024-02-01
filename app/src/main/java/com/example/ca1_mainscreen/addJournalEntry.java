package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addJournalEntry extends AppCompatActivity {

    private EditText editTextTitle, editTextContent;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal_entry);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        Button buttonSave = findViewById(R.id.buttonSave);

        db = new DatabaseHelper(this);

        buttonSave.setOnClickListener(view -> saveJournalEntry());
    }

    private void saveJournalEntry() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please insert a title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the journal entry to the database
        db.addJournalEntry(title, content);

        // Finish the activity
        finish();
    }
}