package ch.swissre.photo_day.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.gridlayout.widget.GridLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.swissre.photo_day.R;

public class GalleryActivity extends AppCompatActivity {

    private List<String> uris;
    private LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        //init ui
        container = findViewById(R.id.container);
        container.removeAllViews();
        // Get photos
        SharedPreferences sharedPreferences = getSharedPreferences("images", MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonList = sharedPreferences.getString("images", "");
        System.out.println(jsonList);
        uris = gson.fromJson(jsonList, new TypeToken<ArrayList<String>>() {
        }.getType());
        generatePhotos();
    }

    private void generatePhotos() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());
        executor.execute(() -> handler.post(() -> {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            addPhotosToView(layoutParams);
        })
        );
    }

    private void addPhotosToView(ViewGroup.LayoutParams layoutParams){
        LinearLayout firstContainer = new LinearLayout(this);
        firstContainer.setOrientation(LinearLayout.HORIZONTAL);
        firstContainer.setLayoutParams(layoutParams);
        firstContainer.setId(View.generateViewId());
        int picturesContainedInBox = 0;
        LinearLayout horizontal;
        for (int i = uris.size() - 1; i >= 0; i--) {
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(Uri.parse(uris.get(i)));
            int widthInDp = 100;
            int heightInDp = 150;
            float scale = getResources().getDisplayMetrics().density;
            int widthInPixels = (int) (widthInDp * scale + 0.5f);
            int heightInPixels = (int) (heightInDp * scale + 0.5f);
            ViewGroup.LayoutParams layoutParamsPicture = new ViewGroup.LayoutParams(widthInPixels, heightInPixels);
            imageView.setLayoutParams(layoutParamsPicture);

            if (picturesContainedInBox % 5 == 0) { //create new horizontal layout every 5 images
                horizontal = new LinearLayout(this);
                horizontal.setOrientation(LinearLayout.HORIZONTAL);
                horizontal.setLayoutParams(layoutParams);
                horizontal.setId(View.generateViewId());
                container.addView(horizontal); //add the new horizontal layout to the container
            }

            LinearLayout hors = (LinearLayout) container.getChildAt(container.getChildCount() - 1); //get the current horizontal layout
            hors.addView(imageView);
            picturesContainedInBox++;
        }
    }
}