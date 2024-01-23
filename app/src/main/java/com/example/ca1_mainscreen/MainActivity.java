package com.example.ca1_mainscreen;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

FirebaseAuth auth;

TextView textView;
FirebaseUser user;
    private int progr = 0;
    private int progress = 0;
    private int numProgr = 0;
    private int progrBox = 0;
    private int getProgrBoxTt = 3;
    private int DIW = 7;
    private CheckBox checkBox;
    private boolean CB = false;
    private boolean buttonIncrClicked = false;
    private boolean buttonIncrClicked1 = false;
    private boolean buttonIncrClicked2 = false;
    private boolean buttonIncrClicked3 = false;
    private boolean buttonIncrClicked4 = false;
    private boolean buttonIncrClicked5 = false;
    private boolean buttonIncrClicked6 = false;
   // private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Redirect to login if no user
        if (user == null) {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
            return; // Exit the onCreate method if there's no user
        }

        // You should initialize Firebase like this only if you have not set up the google-services.json file.
        // Otherwise, Firebase will automatically initialize itself.
//        if (FirebaseApp.getApps(this).isEmpty()) {
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setApiKey("AIzaSyC4qgXdHiLsDFWDhMBqCkm5lmEjq7SpLR4") // Your actual API key
//                    .setProjectId("ca2-ande")
//                    .setApplicationId("1:685500519655:android:fd8925a02ba2e360f17e42") // Your actual application ID
//                    .setDatabaseUrl("https://ca2-ande-default-rtdb.asia-southeast1.firebasedatabase.app")
//                    .build();
//            FirebaseApp.initializeApp(this /* Context */, options);
//        }

        // Since the user is not null, fetch user details from the database
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User userdetails = snapshot.getValue(User.class);
//                if (userdetails != null && userdetails.getImageUrl() != null) {
//                    Log.i("Settings", "onDataChange: Image URL = " + userdetails.getImageUrl());
//                } else {
//                    Log.i("Settings", "User details or image URL is null.");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Settings", "Failed to read image URL", error.toException());
//            }
//        });
       if(user == null){
           Intent i = new Intent(getApplicationContext(),Login.class);
           startActivity(i);
           finish();
       }
      //  readDataFromDatabase();

       checkBox = findViewById(R.id.checkBox);

        updateProgressBar();
        RadioButton  buttonI1 = findViewById(R.id.r1);
        RadioButton  buttonI2 = findViewById(R.id.r2);
        RadioButton  buttonI3 = findViewById(R.id.r3);
        RadioButton  buttonI4 = findViewById(R.id.r4);
        RadioButton  buttonI5 = findViewById(R.id.r5);
        RadioButton  buttonI6 = findViewById(R.id.r6);
        RadioButton  buttonI7 = findViewById(R.id.r7);


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
        ImageButton plusbtn = findViewById(R.id.plus_button2);
        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DailyTask.class);
                startActivity(i);
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Perform actions based on the checked state of the CheckBox
                if (isChecked) {
                   progrBox += 35;
                   numProgr +=1;
                   underline2.setVisibility(View.VISIBLE);
                } else {
                   progrBox -= 35;
                    numProgr -=1;
                    underline2.setVisibility(View.INVISIBLE);
                }
                updateProgressBar();
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Perform actions based on the checked state of the CheckBox
                if (isChecked) {
                    progrBox += 30;
                    numProgr +=1;
                    underline3.setVisibility(View.VISIBLE);
                } else {
                    progrBox -= 30;
                    numProgr -=1;
                    underline3.setVisibility(View.INVISIBLE);
                }
                updateProgressBar();
            }
        });
        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Perform actions based on the checked state of the CheckBox
                if (isChecked) {
                    progrBox += 35;
                    numProgr +=1;
                    underline4.setVisibility(View.VISIBLE);
                } else {
                    progrBox -= 35;
                    numProgr -=1;
                    underline4.setVisibility(View.INVISIBLE);
                }
                updateProgressBar();
            }
        });
        buttonI7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked6) {
                    progr += 10;
                    progress +=1;
                    updateProgressBar();
                    buttonIncrClicked6 = true;

                }else if (progr >= 10) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= 10;
                    progress -= 1;
                    buttonIncrClicked6 = false;
                    CB = false;
                }
                buttonI7.setChecked(buttonIncrClicked6);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
            }
        });

        buttonI6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked5) {
                    progr += 15;
                    progress +=1;
                    updateProgressBar();
                    buttonIncrClicked5 = true;

                }else if (progr >= 15) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= 15;
                    progress -= 1;
                    buttonIncrClicked5 = false;
                    CB = false;
                }
                buttonI6.setChecked(buttonIncrClicked5);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
               // updateDatabase();
            }
        });

        buttonI5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked4) {
                    progr += 15;
                    progress +=1;
                    updateProgressBar();
                    buttonIncrClicked4 = true;
                }else if (progr >= 15) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= 15;
                    progress -= 1;
                    buttonIncrClicked4 = false;
                    CB = false;
                }
                buttonI5.setChecked(buttonIncrClicked4);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
               // updateDatabase();
            }
        });

        buttonI4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked3) {
                    progr += 15;
                    progress +=1;
                    updateProgressBar();
                    buttonIncrClicked3 = true;
                }else if (progr >= 15) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= 15;
                    progress -= 1;
                    buttonIncrClicked3 = false;
                    CB = false;
                }
                buttonI4.setChecked(buttonIncrClicked3);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
            }
        });
        buttonI3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90&& !buttonIncrClicked2) {
                    progr += 15;
                    progress +=1;
                    updateProgressBar();
                    buttonIncrClicked2 = true;
                }else if (progr >= 15) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= 15;
                    progress -= 1;
                    buttonIncrClicked2 = false;
                    CB = false;
                }
                buttonI3.setChecked(buttonIncrClicked2);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
            }
        });
        buttonI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progr <= 90 && !buttonIncrClicked1) {
                    progr += 15;
                    progress +=1;
                    updateProgressBar();
                    buttonIncrClicked1 = true;
                }else if (progr >= 15) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= 15;
                    progress -= 1;
                    buttonIncrClicked1 = false;
                    CB = false;
                }
                buttonI2.setChecked(buttonIncrClicked1);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
               // updateDatabase();
            }
        });



        buttonI1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use a constant for the maximum progress value
                int maxProgress = 90;

                if (progr < maxProgress && !buttonIncrClicked) {
                    // Increment progress if it's below the maximum and button is not clicked
                    progr += 15;
                    progress += 1;
                    buttonIncrClicked = true;
                } else if (progr >= 15) {
                    // Decrement progress if it's greater than or equal to 15
                    progr -= 15;
                    progress -= 1;
                    buttonIncrClicked = false;
                    CB = false;
                }
                buttonI1.setChecked(buttonIncrClicked);
                checkBox.setChecked(CB);
                // Update the progress bar
                updateProgressBar();
                //updateDatabase();
            }
        });




    }


    private void updateProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        TextView textViewProgress = findViewById(R.id.text_view_progress);

        ProgressBar progressBar2 = findViewById(R.id.progress_bar2);
        progressBar2.setProgress(progrBox);
        TextView textViewProgress2 = findViewById(R.id.text_view_progress2);
        textViewProgress2.setText( numProgr+"/"+getProgrBoxTt);
        progressBar.setProgress(progr);
        textViewProgress.setText(progress +"/"+DIW);

        if (progr == 100) {
            checkBox.setChecked(!CB);
            checkBox.setEnabled(CB);

        }
        else {
            checkBox.setChecked(CB);

        }
    }


}