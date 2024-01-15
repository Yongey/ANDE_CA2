package com.example.ca1_mainscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int progr = 0;
    private CheckBox checkBox;
    private boolean buttonIncrClicked = false;
    private boolean buttonIncrClicked1 = false;
    private boolean buttonIncrClicked2 = false;
    private boolean buttonIncrClicked3 = false;
    private boolean buttonIncrClicked4 = false;
    private boolean buttonIncrClicked5 = false;
    private boolean buttonIncrClicked6 = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateProgressBar();
        checkBox = findViewById(R.id.checkBox);
        RadioButton  buttonIncr1 = findViewById(R.id.radioButton10);
        RadioButton  buttonIncr = findViewById(R.id.button_incr);
        RadioButton  buttonDecr = findViewById(R.id.radioButton14);
        RadioButton  buttonIncr2 = findViewById(R.id.radioButton13);
        RadioButton  buttonIncr3 = findViewById(R.id.radioButton15);
        RadioButton  buttonIncr4 = findViewById(R.id.radioButton16);
        RadioButton  buttonIncr5 = findViewById(R.id.radioButton17);
        buttonDecr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked6) {
                    progr += 10;
                    updateProgressBar();
                    buttonIncrClicked6 = true;
                }
            }
        });
        buttonIncr5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked5) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked5 = true;
                }
            }
        });

        buttonIncr4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked4) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked4 = true;
                }
            }
        });

        buttonIncr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked3) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked3 = true;
                }
            }
        });
        buttonIncr3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked2) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked2 = true;
                }
            }
        });
        buttonIncr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90 && !buttonIncrClicked1) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked1 = true;
                }
            }
        });



        buttonIncr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90 && !buttonIncrClicked) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked = true;
                }
            }
        });




    }





    private void updateProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView textViewProgress = findViewById(R.id.text_view_progress);

        progressBar.setProgress(progr);
        textViewProgress.setText(progr + "%");
        if (progr == 100) {
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
        }

    }



}