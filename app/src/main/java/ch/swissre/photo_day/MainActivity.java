package ch.swissre.photo_day;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import ch.swissre.photo_day.activities.PictureTakerActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Button next;
    private TimePicker timePicker;
    private TextView selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init ui
        timePicker = findViewById(R.id.timePicker);
        next = findViewById(R.id.next);
        selectedTime = findViewById(R.id.selectedTime);

        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            // Do something with the selected time
            Log.d("TimePicker", "Selected time: " + hourOfDay + ":" + minute);
            selectedTime.setText(hourOfDay + ":" + minute);
            sharedPreferences.edit().putInt("hrs", hourOfDay).apply();
            sharedPreferences.edit().putInt("min", minute).apply();
        });

        next.setOnClickListener(view -> change());
    }

    private void change() {
        if (getApplication().getSharedPreferences("", MODE_PRIVATE) != null) {
            Intent imageIntent = new Intent(this, PictureTakerActivity.class);
            startActivity(imageIntent);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "You need to add a time!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}