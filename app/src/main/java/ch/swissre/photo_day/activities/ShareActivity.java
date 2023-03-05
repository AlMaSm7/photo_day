package ch.swissre.photo_day.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import ch.swissre.photo_day.R;

public class ShareActivity extends AppCompatActivity {

    private ImageView img;

    private Button share, gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        // Init ui
        share = findViewById(R.id.share);
        gallery = findViewById(R.id.gallery);
        img = findViewById(R.id.imageView);
        // Get bundle information
        Intent bundle = getIntent();

        // Set next screen
        Intent galleryScreen = new Intent(this, GalleryActivity.class);

        Bundle produced_img = bundle.getExtras();
        Uri uri = (Uri) produced_img.get("imageUri");
        System.out.println(uri);
        img.setImageURI(uri);

        share.setOnClickListener(view -> shareImage(uri));

        gallery.setOnClickListener(view -> startActivity(galleryScreen));
    }

    private void shareImage(Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, null));

    }
}