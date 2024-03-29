package com.example.ca1_mainscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Settings extends AppCompatActivity {
    Button button;
    FirebaseAuth mAuth;
    @Override
    protected void onStart() {
        super.onStart();
        updateOnlineStatus(true); // User goes online
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        button = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        TextView profileTextView = findViewById(R.id.tv_profile);
        TextView notificationTextView = findViewById(R.id.tv_notification);
        TextView ps = findViewById(R.id.tv_ps);
        TextView hs = findViewById(R.id.tv_hs);
        TextView about = findViewById(R.id.tv_about);
        ps.setOnClickListener(v -> {
           Intent intent = new Intent(Settings.this, PrivacyAndSecurity.class);
           startActivity(intent);
        });
        hs.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, HelpAndSupport.class);
            startActivity(intent);
        });
        about.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, About.class);
            startActivity(intent);
        });
        notificationTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, Notification.class);
            startActivity(intent);
        });

        profileTextView.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.this, Profile.class);
            startActivity(intent);
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOnlineStatus(false);
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.home) {
                startActivity(new Intent(Settings.this, MainActivity.class));
                finish();
                return true;
            }else if(item.getItemId()==R.id.add) {
                startActivity(new Intent(Settings.this, Add.class));
                finish();
                return true;

            }else if(item.getItemId()==R.id.settings) {
                return true;
            }else{
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.settings);
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

}