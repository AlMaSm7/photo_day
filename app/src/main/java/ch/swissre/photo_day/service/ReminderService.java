package ch.swissre.photo_day;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

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
}