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

import java.util.ArrayList;
import java.util.List;

public class WeeklyHabit extends AppCompatActivity{

    private RecyclerView recyclerView;
    private HabitAdapter adapter;
    private List<HabitModel> habitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_habit);

        ImageButton newhabit = findViewById(R.id.newhabit);
        newhabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when TextView is clicked
                Intent intent = new Intent(getApplicationContext(), NewHabit.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.weeklyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        habitList = new ArrayList<>();
        adapter = new HabitAdapter(habitList);
        recyclerView.setAdapter(adapter);
    }


}