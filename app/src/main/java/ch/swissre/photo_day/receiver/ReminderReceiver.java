package ch.swissre.photo_day.receiver;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ch.swissre.photo_day.R;
import ch.swissre.photo_day.activities.PictureTakerActivity;

public class ReminderReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "reminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Perform the reminder action
        // Send reminder notification
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, // Channel ID
                "reminder", // Channel name
                NotificationManager.IMPORTANCE_HIGH // Channel importance
        );

        Intent taker = new Intent(context, PictureTakerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, taker, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel.getId())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Take photo!")
                .setContentText("It's time to take your daily photo")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("no permission");
            return;
        }
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(1, builder.build());
    }
}

