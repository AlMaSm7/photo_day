package ch.swissre.photo_day.receiver;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ch.swissre.photo_day.R;

public class ReminderReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "reminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Perform the reminder action
        // Send reminder notification
        System.out.println("received!");
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, // Channel ID
                "reminder", // Channel name
                NotificationManager.IMPORTANCE_HIGH // Channel importance
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel.getId())
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Take photo!")
                .setContentText("It's time to take your daily photo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("no permission");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(1, builder.build());
    }

}
