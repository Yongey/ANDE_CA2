package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class WeeklyHabit extends AppCompatActivity{

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
    }



}