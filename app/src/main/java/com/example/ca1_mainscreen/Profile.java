package com.example.ca1_mainscreen;

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

public class Profile extends AppCompatActivity {
    TextView tvUsername,tvGender;
    EditText etUsername;
    private Button btnCFA;
    TextView tvEmail;
    FirebaseAuth mAuth;
    private ValueEventListener onlineStatusListener;
    private ImageView imgProfile;
    private Uri imagePath;
    private Spinner spinnerGender;
    private String currentImageUrl = null;
    private String originalImageUrl = null;
    private TextView etDob;
    private TextView tvAge;
    private Button btnEditUser;
    private String dob;
    @Override
    protected void onStart() {
        super.onStart();
        updateOnlineStatus(true); // User goes online
        //test
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(currentUser.getUid());

            // Set the online status
            userRef.child("dob").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String dob = dataSnapshot.getValue(String.class);
                        etDob.setText("Date Of Birth: " + dob);
                        calculateAndDisplayAge(dob); // Calculate and display age
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Profile.this, "Failed to load DOB", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Profile.this, "Failed to load AGE", Toast.LENGTH_SHORT).show();
                }
            });
            // Load the username
            userRef.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.getValue(String.class);
                        etUsername.setText(username); // Set the username in the EditText
                        tvUsername.setText(username); // Set the username in the TextView
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("DBError", "Failed to read username.", databaseError.toException());
                }
            });
        }

        loadGenderFromFirebase(spinnerGender);
        loadProfileImage();
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
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        imgProfile = findViewById(R.id.userprofile);
        ImageView penIcon = findViewById(R.id.penIcon);
        spinnerGender = findViewById(R.id.spinner_gender);
        FirebaseUser user = mAuth.getCurrentUser();
        etUsername = findViewById(R.id.etUsername);
        btnEditUser = findViewById(R.id.btnEditUsername);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvAge = findViewById(R.id.ageOfUser);
        ImageView backIcon = findViewById(R.id.backIcon);
        etDob = findViewById(R.id.etDob);
        tvGender = findViewById(R.id.tvGender);
        View onlineIndicator = findViewById(R.id.onlineIndicator);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);


        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        if (users != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserStatus").child(users.getUid());
            userRef.child("isOnline").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        boolean isUserOnline = dataSnapshot.getValue(Boolean.class);
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
        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this,UpdateProfile.class);
                startActivity(i);
            }
        });


        penIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(new Intent(Profile.this,Settings.class));
                startActivity(i);
            }
        });
        // Check if the user is not null and get the display name
        if (user != null) {
            String displayName = user.getDisplayName();
            String displayEmail =user.getEmail();
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);
            if (displayName != null || displayEmail != null) {
                tvUsername.setText(displayName);
                tvEmail.setText(displayEmail);
                etUsername.setHint("Current username: " + tvUsername.getText().toString());
            } else {
                tvUsername.setText("No username set");
            }

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
                    Toast.makeText(Profile.this, "Failed to load DOB", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Save the original image URL when loading for the first time
            if (originalImageUrl == null) {
                originalImageUrl = currentImageUrl;
            }
            imagePath=data.getData();
            getImageInImageView();
            uploadImage();
        }
    }
    private void uploadImage(){
    FirebaseUser user = mAuth.getCurrentUser();
    if (user == null || imagePath == null) {
        Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        return;
    }
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Uploading...");
    progressDialog.show();

    String imageRef = "images/" + UUID.randomUUID().toString();
    FirebaseStorage.getInstance().getReference(imageRef).putFile(imagePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
            progressDialog.dismiss();
            if(task.isSuccessful()){
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful()){
                       String imageUrl = task.getResult().toString();
                       updateProfilePicture(imageUrl);
                       Toast.makeText(Profile.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                   }else {
                       // Restore the original image URL if getting download URL fails
                       currentImageUrl = originalImageUrl;
                       Toast.makeText(Profile.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                   }
                    }
                });

            }else{
                currentImageUrl = originalImageUrl;
                Toast.makeText(Profile.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
            Log.d("UploadProgress", "Uploaded: " + snapshot.getBytesTransferred() + " / " + snapshot.getTotalByteCount());
            progressDialog.setMessage("Uploaded " + (int) progress + "%");
        }
    });

}
    private void updateProfilePicture(String url){
        FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/imageUrl").setValue(url) .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    currentImageUrl = url;
                    Toast.makeText(Profile.this, "Profile image updated.", Toast.LENGTH_SHORT).show();
                    // Load the updated image
                    loadProfileImage();
                } else {
                    Toast.makeText(Profile.this, "Failed to update profile image in Firebase.", Toast.LENGTH_SHORT).show();
                    revertToOriginalImage();
                }
            }
        });
}
    private void revertToOriginalImage() {
        if (originalImageUrl != null) {
            Glide.with(Profile.this)
                    .load(originalImageUrl)
                    .into(imgProfile);
            currentImageUrl = originalImageUrl;
        }
    }
    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);

            if (bitmap != null) {
                imgProfile.setImageBitmap(bitmap);
            } else {
                // Handle the case where bitmap is null (error loading image)
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            // Handle the IOException
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadProfileImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user/" + userId);
            FirebaseDatabase.getInstance().getReference("user/" + userId + "/imageUrl")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String imageUrl = dataSnapshot.getValue(String.class);
                                if (currentImageUrl == null) {
                                    originalImageUrl = imageUrl; // Save the original image URL when loading for the first time
                                }
                                currentImageUrl = imageUrl;
                                Glide.with(Profile.this)
                                        .load(currentImageUrl)
                                        .into(imgProfile);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Profile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            userRef.child("dob").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dob = dataSnapshot.getValue(String.class);
                        etDob.setText(dob);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Profile.this, "Failed to load DOB", Toast.LENGTH_SHORT).show();
                }
            });


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
                        tvGender.setText(gender != null ? gender : "Not set");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Profile.this, "Failed to load gender", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void updateOnlineStatus(boolean isOnline) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userStatusRef = FirebaseDatabase.getInstance()
                    .getReference("UserStatus")
                    .child(currentUser.getUid())
                    .child("isOnline");
            userStatusRef.setValue(isOnline);
        }
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