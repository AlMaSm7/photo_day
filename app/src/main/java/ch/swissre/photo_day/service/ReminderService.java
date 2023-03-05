package ch.swissre.photo_day.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;

import ch.swissre.photo_day.receiver.ReminderReceiver;

public class ReminderService extends Service {

    private final IBinder binder = new ReminderBinder();

    public class ReminderBinder extends Binder {
        public ReminderService getService() {
            return ReminderService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    @Override
    public void onCreate() {
        System.out.println("herelollolololol");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("got start activity");
        // Get the reminder time from SharedPreferences
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("time", MODE_PRIVATE);;
        int reminderHour = sharedPreferences.getInt("hrs", -1);
        int reminderMinute = sharedPreferences.getInt("min", -1);

        // If the reminder time has not been set, stop the service
        if (reminderHour == -1 || reminderMinute == -1) {
            stopSelf();
            return START_NOT_STICKY;
        }

        // Set up the alarm to trigger at the selected reminder time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminderHour);
        calendar.set(Calendar.MINUTE, reminderMinute);
        calendar.set(Calendar.SECOND, 0);
        long triggerTime = calendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent reminderIntent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, reminderIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        System.out.println("service will start");
        return START_STICKY;
    }
}