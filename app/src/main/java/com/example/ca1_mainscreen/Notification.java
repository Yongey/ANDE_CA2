package com.example.ca1_mainscreen;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Notification extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";
    private Switch notificationSwitch;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    subscribeToFirebaseMessaging();
                } else {
                    Toast.makeText(Notification.this, "Permission denied. Notifications disabled.", Toast.LENGTH_SHORT).show();
                }
            }
    );
    private DatabaseReference notificationSwitchRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        notificationSwitch = findViewById(R.id.notificationSwitch);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            notificationSwitchRef = usersRef.child(currentUser.getUid()).child("notificationSwitch");
            // Retrieve the current value from the database and update the switch
            notificationSwitchRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        boolean isEnabled = snapshot.getValue(Boolean.class);
                        notificationSwitch.setChecked(isEnabled);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error getting switch value", error.toException());
                }
            });
        } else {
            // Handle the case where the user is not authenticated
            // You may want to redirect the user to the login screen
        }

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableNotifications();
            } else {
                disableNotifications();
            }
        });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        askNotificationPermission();
    }

    private void askNotificationPermission() {
        // Permission handling for TIRAMISU and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                subscribeToFirebaseMessaging();
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            subscribeToFirebaseMessaging();
        }
    }

    private void showToastSafely(final String message) {
        runOnUiThread(() -> {
            if (!isFinishing()) {
                Toast.makeText(Notification.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void subscribeToFirebaseMessaging() {
        // Fetch and log FCM token, subscribe to "news" topic
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    String token = task.getResult();
                    Log.d(TAG, "FCM Token: " + token);
                    showToastSafely("FCM Token: " + token);

                    FirebaseMessaging.getInstance().subscribeToTopic("news")
                            .addOnCompleteListener(subscribeTask -> {
                                String msg = subscribeTask.isSuccessful() ? "Subscribed to news topic" : "Subscribe to news topic failed";
                                Log.d(TAG, msg);
                                showToastSafely(msg);
                            });
                });
    }

    private void enableNotifications() {
        subscribeToFirebaseMessaging();
        showToastSafely("Notifications enabled");
        updateNotificationSetting(true);
    }

    private void disableNotifications() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news")
                .addOnCompleteListener(unsubscribeTask -> {
                    String msg = unsubscribeTask.isSuccessful() ? "Unsubscribed from news topic" : "Unsubscribe from news topic failed";
                    Log.d(TAG, msg);
                    showToastSafely(msg);
                    updateNotificationSetting(false);
                });

        showToastSafely("Notifications disabled");
    }
    private void updateNotificationSetting(boolean isEnabled) {
        // Update the notification switch value in Firebase Database or Firestore
        notificationSwitchRef.setValue(isEnabled);
    }
    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyFirebaseMessagingService.ACTION_RECEIVE_NOTIFICATION)) {
                String title = intent.getStringExtra("title");
                String message = intent.getStringExtra("message");
                showInAppNotification(title, message);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(MyFirebaseMessagingService.ACTION_RECEIVE_NOTIFICATION);
        registerReceiver(notificationReceiver, filter);
    }
    private void showInAppNotification(String title, String message) {
        // You can use a dialog, snackbar, or a custom layout to show the notification
        Toast.makeText(this, title + ": " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(notificationReceiver);
    }
}
