package ch.swissre.photo_day.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import ch.swissre.photo_day.R;

public class ReminderReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "reminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Perform the reminder action
        // Send reminder notification
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, // Channel ID
                "reminder", // Channel name
                NotificationManager.IMPORTANCE_LOW // Channel importance
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Take photo!")
                .setContentText("It's time to take your daily photo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

}

