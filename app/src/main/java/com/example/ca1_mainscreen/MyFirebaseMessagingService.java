package com.example.ca1_mainscreen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM";
    private static final String CHANNEL_ID = "Your_Channel_ID"; // Define a Channel ID
    public static final String ACTION_RECEIVE_NOTIFICATION = "com.example.ca1_mainscreen.ACTION_RECEIVE_NOTIFICATION";

    private void broadcastNotification(String title, String message) {
        Intent intent = new Intent(ACTION_RECEIVE_NOTIFICATION);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        sendBroadcast(intent);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Received message from: " + remoteMessage.getFrom());

        // Check if the message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleDataMessage(remoteMessage.getData());
        }

        // Check if the message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle(); // Get the title
            String messageBody = remoteMessage.getNotification().getBody(); // Get the body
            Log.d(TAG, "Message Notification Title: " + title);
            Log.d(TAG, "Message Notification Body: " + messageBody);

            // Display the notification
            showNotification(title, messageBody);
        }
    }

    private void handleDataMessage(Map<String, String> data) {
        // Assuming the data map contains title and body
        String title = data.get("title");
        String body = data.get("body");

        // Display the notification
        showNotification(title, body);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Implement this method to send token to your app server.
    }

    private void showNotification(String title, String body) {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24) // Make sure you have this icon.
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name); // Set the channel name
            String description = getString(R.string.channel_description); // Set the channel description
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
