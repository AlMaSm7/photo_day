package ch.swissre.photo_day;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

import ch.swissre.photo_day.activities.PictureTakerActivity;
import ch.swissre.photo_day.service.ReminderService;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Button next;
    private TimePicker timePicker;
    private TextView selectedTime;
    private final int REQUEST_CODE = 1;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.POST_NOTIFICATIONS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermissions();
        }
        setContentView(R.layout.activity_main);
        //init ui
        timePicker = findViewById(R.id.timePicker);
        next = findViewById(R.id.next);
        selectedTime = findViewById(R.id.selectedTime);

        sharedPreferences = getApplication().getSharedPreferences("time", MODE_PRIVATE);
        AtomicInteger hrs = new AtomicInteger();
        AtomicInteger min = new AtomicInteger();
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Log.d("TimePicker", "Selected time: " + hourOfDay + ":" + minute);
            hrs.set(hourOfDay);
            min.set(minute);
        });

        next.setOnClickListener(view -> {
            System.out.println(min);
            sharedPreferences.edit().putInt("hrs", hrs.get()).apply();
            sharedPreferences.edit().putInt("min", min.get()).apply();
            change();
        });
    }

    private void change() {
        if (getApplication().getSharedPreferences("", MODE_PRIVATE) != null) {
            // Start the ReminderService
            Intent intent = new Intent(this, ReminderService.class);
            startService(intent);
            System.out.println("Service has started");
            //change screen
            Intent imageIntent = new Intent(this, PictureTakerActivity.class);
            startActivity(imageIntent);
            //finish();
        } else {
            Context context = getApplicationContext();
            CharSequence text = "You need to add a time!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkPermissions(){
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_CODE
                );
            }
        }
    }
}