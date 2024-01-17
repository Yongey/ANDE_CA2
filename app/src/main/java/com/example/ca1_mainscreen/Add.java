package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home) {
                startActivity(new Intent(Add.this, MainActivity.class));
                finish();
                return true;
            }else if(item.getItemId()==R.id.add) {

                return true;

            }else if(item.getItemId()==R.id.settings) {
                startActivity(new Intent(Add.this, Settings.class));
                finish();
                return true;
            }else{
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.add);
    }

}