package com.example.ca1_mainscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;
public class UpdateProfile extends AppCompatActivity {
    TextView tvUsername;

    private Button btnCFA;

    TextView tvEmail;
    FirebaseAuth mAuth;
    private ValueEventListener onlineStatusListener;
    private ImageView imgProfile;
    private Uri imagePath;
    private EditText etName;
    private Spinner spinnerGender;
    private String currentImageUrl = null;
    private String originalImageUrl = null;
    private TextView etDob;
    private TextView tvAge;
    private Button btnSave,btnEditUsername;
    private String dob;
    private View onlineIndicator;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());

            // Set the online status
            userRef.child("dob").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String dob = dataSnapshot.getValue(String.class);
                        etDob.setText(dob);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UpdateProfile.this, "Failed to load DOB", Toast.LENGTH_SHORT).show();
                }
            });
            userRef.child("age").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Assuming the age is stored as an Integer or Long in Firebase
                        Integer age = dataSnapshot.getValue(Integer.class);
                        if (age != null) {
                            tvAge.setText("Age: " + age.toString()); // Set age in TextView
                        } else {
                            tvAge.setText("Age not available");
                        }
                    } else {
                        tvAge.setText("Age not set"); // Handle case where age is not set
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UpdateProfile.this, "Failed to load AGE", Toast.LENGTH_SHORT).show();
                }
            });

        }
        loadGenderFromFirebase(spinnerGender);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (onlineStatusListener != null) {
            // Remove the listener when the activity is no longer visible
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserStatus").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userRef.child("isOnline").removeEventListener(onlineStatusListener);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        ImageView backIcon = findViewById(R.id.backIcon);
        etDob = findViewById(R.id.etDob);
        btnSave = findViewById(R.id.btnSave);
        etName = findViewById(R.id.etName);
        tvAge = findViewById(R.id.ageOfUser);
        onlineIndicator = findViewById(R.id.onlineIndicator);
        spinnerGender = findViewById(R.id.spinner_gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        loadUserName();
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes this activity and returns to the previous one
            }
        });
        if (users != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserStatus").child(users.getUid());
            userRef.child("isOnline").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        boolean isUserOnline = dataSnapshot.getValue(Boolean.class);
                        View onlineIndicator = findViewById(R.id.onlineIndicator);
                        if (isUserOnline) {
                            onlineIndicator.setVisibility(View.VISIBLE);
                        } else {
                            onlineIndicator.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("DBError", "Failed to read online status.", databaseError.toException());
                }
            });
        }
        etDob.setOnTouchListener((v, event) -> {
            // Assuming the drawable is at the end (right side)
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (etDob.getRight() - etDob.getCompoundDrawables()[2].getBounds().width())) {
                    showDatePickerDialog();
                    return true;
                }
            }
            return false;
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
            @Override
            public void onClick(View v) {
                // Get the edited email and selected gender
                String selectedGender = spinnerGender.getSelectedItem().toString();
                userRef.child("gender").setValue(selectedGender)
                        .addOnSuccessListener(aVoid -> Toast.makeText(UpdateProfile.this, "Gender saved successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(UpdateProfile.this, "Failed to save DOB", Toast.LENGTH_SHORT).show());
                saveDataToFirebase();
                saveUserName();
                // Optionally, show a message to indicate the data has been saved
                Toast.makeText(UpdateProfile.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(UpdateProfile.this, Profile.class);
                        startActivity(i);
                        finish(); // If you want to close the current activity
                    }
                }, 1300);
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    dob = sdf.format(selectedDate.getTime());
                    etDob.setText(dob);
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        // Customize the date picker (colors, buttons)
        dpd.setAccentColor(Color.parseColor("#5F44CF"));
        dpd.show(getSupportFragmentManager(), "Datepickerdialog"); // Use getSupportFragmentManager() here
    }


    private void saveDataToFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && dob != null) {
            int age = calculateAge(dob);
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
            userRef.child("dob").setValue(dob);
            userRef.child("age").setValue(age)
                    .addOnSuccessListener(aVoid -> Toast.makeText(UpdateProfile.this, "DOB saved successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(UpdateProfile.this, "Failed to save DOB", Toast.LENGTH_SHORT).show());

        }
    }
    private void setGenderSelection(Spinner spinnerGender, String gender) {
        if (gender != null) {
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerGender.getAdapter();
            int position = adapter.getPosition(gender);
            spinnerGender.setSelection(position);
        }
    }
    private void loadGenderFromFirebase(Spinner spinnerGender) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
            userRef.child("gender").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String gender = dataSnapshot.getValue(String.class);
                        setGenderSelection(spinnerGender, gender);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UpdateProfile.this, "Failed to load gender", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void loadUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
            userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.getValue(String.class);
                        etName.setText(name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UpdateProfile.this, "Failed to load name", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void saveUserName() {
        String newName = etName.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !newName.isEmpty()) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
            userRef.child("username").setValue(newName)
                    .addOnSuccessListener(aVoid -> Toast.makeText(UpdateProfile.this, "Name updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(UpdateProfile.this, "Failed to update name", Toast.LENGTH_SHORT).show());
        }
    }
    private int calculateAge(String dobString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(dobString, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(dob, currentDate).getYears();
    }
    private void calculateAndDisplayAge(String dobString) {
        // Make sure your app's minSdkVersion is 26 or higher to use java.time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(dobString, formatter);
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(dob, currentDate).getYears();

        // Assuming you have a TextView to display age
        tvAge = findViewById(R.id.ageOfUser);
        tvAge.setText("   Age: " + age);
    }

}