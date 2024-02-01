package com.example.ca1_mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;


import java.util.List;

public class Journal extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JournalAdapter adapter;
    private List<JournalEntry> journalEntries;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        db = new DatabaseHelper(this);
        journalEntries = db.getAllJournalEntries();

        recyclerView = findViewById(R.id.journalRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new JournalAdapter(journalEntries);
        recyclerView.setAdapter(adapter);

        Button addEntryButton = findViewById(R.id.addEntryButton);
        addEntryButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), addJournalEntry.class);
            startActivity(intent);
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        journalEntries.clear();
        journalEntries.addAll(db.getAllJournalEntries());
        adapter.notifyDataSetChanged();
    }
}