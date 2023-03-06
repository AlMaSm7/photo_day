package ch.swissre.photo_day.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import java.time.LocalDate;

public class CheckDayService extends Service {

    private final IBinder binder = new CheckDayServiceBinder();

    public class CheckDayServiceBinder extends Binder {
        public CheckDayService getService() {
            return CheckDayService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public boolean isNewDay() {
        //Get localdatetime and deserialize
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("time", MODE_PRIVATE);
        String lastPhotoTakenString = sharedPreferences.getString("date", null);
        if(lastPhotoTakenString != null){
            LocalDate lastPhotoTaken = LocalDate.parse(lastPhotoTakenString);
            LocalDate now = LocalDate.now();
            System.out.println("today: " + now);
            //check if it's another day
            return lastPhotoTaken.isAfter(now);
        }else{
            sharedPreferences.edit().putString("date", LocalDate.now().toString()).apply();
            return false;
        }
    }

    public void updateSharedPreferences(){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("time", MODE_PRIVATE);
        sharedPreferences.edit().putString("date", LocalDate.now().toString()).apply();
    }
}