package com.example.ca1_mainscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Profile extends AppCompatActivity {
    TextView tvUsername;
    private Button btnCFA;
    TextView tvEmail;
    FirebaseAuth mAuth;
    private ImageView imgProfile;
    private Uri imagePath;
    private String currentImageUrl = null;
    private String originalImageUrl = null;
    @Override
    protected void onStart() {
        super.onStart();
        loadProfileImage();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        imgProfile = findViewById(R.id.userprofile);
        btnCFA = findViewById(R.id.chooseFA);
        FirebaseUser user = mAuth.getCurrentUser();
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        ImageView backIcon = findViewById(R.id.backIcon);

        btnCFA.setOnClickListener(new View.OnClickListener() {
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
                finish(); // Closes this activity and returns to the previous one
            }
        });
        // Check if the user is not null and get the display name
        if (user != null) {
            String displayName = user.getDisplayName();
            String displayEmail =user.getEmail();
            if (displayName != null || displayEmail != null) {
                tvUsername.setText("Username: " +displayName);
                tvEmail.setText("User Email: "+displayEmail);
            } else {
                tvUsername.setText("No username set");
            }
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
            double progress = 100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
            progressDialog.setMessage("Uploaded "+(int)progress+"%");
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
        }
    }

}