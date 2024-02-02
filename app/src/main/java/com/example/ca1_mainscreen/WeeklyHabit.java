package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.ca1_mainscreen.Adapter.HabitAdapter;
import com.example.ca1_mainscreen.Model.HabitModel;
import com.example.ca1_mainscreen.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class WeeklyHabit extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HabitAdapter habitAdapter;
    private List<HabitModel> habitList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_habit);

        db = new DatabaseHandler(this);
        db.openDatabase();

        ImageButton newhabit = findViewById(R.id.newhabit);
        newhabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewHabitDialog();
            }
        });


        recyclerView = findViewById(R.id.weeklyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        habitList = db.getAllHabits();

        if (habitList == null) {
            habitList = new ArrayList<>(); // Handle the case where habitList is null
        }

        habitAdapter = new HabitAdapter(habitList);
        recyclerView.setAdapter(habitAdapter);
    }

    private void openNewHabitDialog() {
        NewHabit newHabitDialog = new NewHabit();
        newHabitDialog.show(getSupportFragmentManager(), NewHabit.TAG);
    }
}