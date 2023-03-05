package ch.swissre.photo_day.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.gridlayout.widget.GridLayout;

import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ch.swissre.photo_day.R;

public class GalleryActivity extends AppCompatActivity {

    private List<String> uris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //init ui
        GridLayout gridLayout = findViewById(R.id.grid_layout);
        gridLayout.setColumnCount(5);
        // Get photos
        SharedPreferences sharedPreferences = getSharedPreferences("images", MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonList = sharedPreferences.getString("images", "");
        System.out.println(jsonList);
        uris = gson.fromJson(jsonList, new TypeToken<ArrayList<String>>() {
        }.getType());

        for (int i = uris.size() - 1; i >= 0; i--) {
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(Uri.parse(uris.get(i)));

            System.out.println("here");

            // Set the layout parameters for the ImageView so it fits in the GridLayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            params.columnSpec = GridLayout.spec(i % 5);
            params.rowSpec = GridLayout.spec(i / 5);
            imageView.setLayoutParams(params);

            // Add the ImageView to the GridLayout
            gridLayout.addView(imageView);
        }
    }
}