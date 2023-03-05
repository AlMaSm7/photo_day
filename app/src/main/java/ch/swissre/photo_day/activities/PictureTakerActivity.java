package ch.swissre.photo_day.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.camera.lifecycle.ProcessCameraProvider;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import ch.swissre.photo_day.R;

public class PictureTakerActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ImageCapture imageCapture;

    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};

    private Button takePicture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        // add elements
        previewView = findViewById(R.id.previewView);
        takePicture = findViewById(R.id.taker);
        //intent to next view

        intent = new Intent(this, ShareActivity.class);

        // Request camera permissions
        if (allPermissionsGranted()) {
            System.out.println("granted");
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
            );
        }

        // Wait for the camera provider to be available
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCamera(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Error binding preview", e);
            }
        }, getExecutor());

        takePicture.setOnClickListener(view -> {
            takePicture();
        });
    }

    private void startCamera(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        //Select camera to use
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        // Set preview
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        //Build photo camera

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void takePicture() {
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");


        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        System.out.println(outputFileResults.getSavedUri());
                        System.out.println("Sucesss");
                        intent.putExtra("imageUri", outputFileResults.getSavedUri());
                        Toast.makeText(PictureTakerActivity.this, "Photo has been saved successfully.", Toast.LENGTH_SHORT).show();
                        addImage(outputFileResults.getSavedUri());
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(PictureTakerActivity.this, "Error saving photo: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(exception.getMessage());
                    }
                }
        );
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void addImage(Uri img) {
        SharedPreferences sharedPreferences = getSharedPreferences("images", MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonList = sharedPreferences.getString("images", "");
        System.out.println(jsonList);
        List<String> imgList;
        if (!jsonList.isEmpty()) {
            imgList = gson.fromJson(jsonList, new TypeToken<ArrayList<String>>() {
            }.getType());
            imgList.add(img.toString());
        } else {
            System.out.println("Creating new list...");
            imgList = new ArrayList<>();
            imgList.add(img.toString());
        }
        // Convert back to json
        String updatedJsonList = gson.toJson(imgList);

        sharedPreferences.edit().putString("images", updatedJsonList).apply();

    }
}