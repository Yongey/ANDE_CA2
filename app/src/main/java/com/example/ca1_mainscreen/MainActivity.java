package com.example.ca1_mainscreen;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ca1_mainscreen.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

FirebaseAuth auth;

TextView textView;
FirebaseUser user;
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
        auth = FirebaseAuth.getInstance();

       user = auth.getCurrentUser();
       if(user == null){
           Intent i = new Intent(getApplicationContext(),Login.class);
           startActivity(i);
           finish();
       }

        updateProgressBar();
        checkBox = findViewById(R.id.checkBox);
        RadioButton  buttonI1 = findViewById(R.id.r1);
        RadioButton  buttonI2 = findViewById(R.id.r2);
        RadioButton  buttonI3 = findViewById(R.id.r3);
        RadioButton  buttonI4 = findViewById(R.id.r4);
        RadioButton  buttonI5 = findViewById(R.id.r5);
        RadioButton  buttonI6 = findViewById(R.id.r6);
        RadioButton  buttonI7 = findViewById(R.id.r7);
        Button resetButton = findViewById(R.id.reset_button);
        CheckBox checkBox2 = findViewById(R.id.checkBox2);
        CheckBox checkBox3 = findViewById(R.id.checkBox3);
        CheckBox checkBox4 = findViewById(R.id.checkBox4);

        View underline2 = findViewById(R.id.underline2);
        View underline3 = findViewById(R.id.underline3);
        View underline4 = findViewById(R.id.underline4);
        underline2.setVisibility(checkBox2.isChecked() ? View.VISIBLE : View.INVISIBLE);
        underline3.setVisibility(checkBox3.isChecked() ? View.VISIBLE : View.INVISIBLE);
        underline4.setVisibility(checkBox4.isChecked() ? View.VISIBLE : View.INVISIBLE);

        checkBox2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            underline2.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        checkBox3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            underline3.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        checkBox4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            underline4.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home) {
                return true;
            }else if(item.getItemId()==R.id.add) {
                startActivity(new Intent(MainActivity.this, Add.class));
                finish();
                return true;

            }else if(item.getItemId()==R.id.settings) {
                startActivity(new Intent(MainActivity.this, Settings.class));
                finish();
                return true;
            }else{
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
        buttonI7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked6) {
                    progr += 10;
                    updateProgressBar();
                    buttonIncrClicked6 = true;
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetProgressBar();

            }
        });
        buttonI6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked5) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked5 = true;
                }
            }
        });

        buttonI5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked4) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked4 = true;
                }
            }
        });

        buttonI4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked3) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked3 = true;
                }
            }
        });
        buttonI3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked2) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked2 = true;
                }
            }
        });
        buttonI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90 && !buttonIncrClicked1) {
                    progr += 15;
                    updateProgressBar();
                    buttonIncrClicked1 = true;
                }
            }
        });



        buttonI1.setOnClickListener(new View.OnClickListener() {
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
//private void replaceFragment(Fragment fragment){
//    FragmentManager fragmentManager = getSupportFragmentManager();
//    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//    fragmentTransaction.replace(R.id.frame_layout,fragment);
//    fragmentTransaction.commit();
//}
    private void resetProgressBar() {
        progr = 0;
        resetClickedFlags(); // Reset the flags first
        updateProgressBar();
        checkBox.setChecked(false);
        checkBox.setEnabled(false);

        RadioButton buttonI1 = findViewById(R.id.r1);
        RadioButton buttonI2 = findViewById(R.id.r2);
        RadioButton buttonI3 = findViewById(R.id.r3);
        RadioButton buttonI4 = findViewById(R.id.r4);
        RadioButton buttonI5 = findViewById(R.id.r5);
        RadioButton buttonI6 = findViewById(R.id.r6);
        RadioButton buttonI7 = findViewById(R.id.r7);

        buttonI1.setChecked(false);
        buttonI2.setChecked(false);
        buttonI3.setChecked(false);
        buttonI4.setChecked(false);
        buttonI5.setChecked(false);
        buttonI6.setChecked(false);
        buttonI7.setChecked(false);
    }



    private void resetClickedFlags() {
        buttonIncrClicked = false;
        buttonIncrClicked1 = false;
        buttonIncrClicked2 = false;
        buttonIncrClicked3 = false;
        buttonIncrClicked4 = false;
        buttonIncrClicked5 = false;
        buttonIncrClicked6 = false;
    }




    private void updateProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView textViewProgress = findViewById(R.id.text_view_progress);
        Button resetButton = findViewById(R.id.reset_button);

        progressBar.setProgress(progr);
        textViewProgress.setText(progr + "%");

        if (progr == 100) {
            checkBox.setChecked(true);
            checkBox.setEnabled(false);
            resetButton.setVisibility(View.VISIBLE); // Show the reset button
        } else {
            resetButton.setVisibility(View.GONE); // Hide the reset button
        }
    }


}