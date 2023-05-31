package com.example.ltst2023air9.ui.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCapture.OutputFileOptions;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ltst2023air9.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Executor;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    Button mTakePicture;
    Button mVideo;

    ImageCapture mImageCapture;
    VideoCapture mVideoCapture;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView mPreview;
//    private final SensorManager mSensorManager;
//    private final Sensor mAccelerometer;
//    private final Sensor mGyroscope;
//
//    public CameraActivity() {
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//    }

//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    protected void onPause() {
//        super.onPause();
//        mSensorManager.unregisterListener(this);
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mTakePicture = findViewById(R.id.b_take_picture);
        mVideo = findViewById(R.id.b_video);
        mPreview = findViewById(R.id.fl_preview_view);

        mTakePicture.setOnClickListener(this);
        mVideo.setOnClickListener(this);


    }

    private void cameraInit() {

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                StartCameraX(cameraProvider);
            } catch (Exception e) {
            }
        }, getExecutor());
    }

    @Override
    protected void onStart() {
        super.onStart();
//
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//
//        }, 5000);
        if (allPermissionsGranted()) {
            cameraInit();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.b_take_picture) {
            capturePhoto();
        }
        if (v.getId() == R.id.b_video) {
            if (mVideo.getTag() != null) {
                Toast.makeText(getApplicationContext(), "флаг есть, отключение.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getApplicationContext(), "флага нет, включение..", Toast.LENGTH_SHORT).show();
            mVideo.setTag(new Object());
            recordVideo();
        }

    }

    protected void capturePhoto() {
        try {

            File photoPath = getApplicationContext().getFilesDir(); //new File("/mnt/sdcard/Pictures/" + "androidXPhoto/");
            //if(!photoPath.exists()) photoPath.mkdir();

            Date date = new Date();
            String timestamp = String.valueOf(date.getTime());

            File outputPhotoFile = new File(photoPath, timestamp + ".jpg");

            mImageCapture.takePicture(
                    new OutputFileOptions.Builder(outputPhotoFile).build(),
                    getExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            Toast.makeText(CameraActivity.this, timestamp + ".jpg", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            Toast.makeText(getApplicationContext(), "picture was no saved. error.", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Log.e("Camera.error.picfile", e.getMessage());
        }
    }

    protected void recordVideo() {

    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void StartCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(mPreview.getSurfaceProvider());

        mImageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
//        mVideoCapture = new VideoCapture.Builder()
//                .setTargetRotation(mPreview.getDisplay().getRotation())
//                .setVideoFrameRate(25)
//                .setBitRate(3 * 1024 * 1024)
//                .build();
        //mVideoCapture = new VideoCapture.Builder().build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, mImageCapture);
    }

    private boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraInit();
            } else {
                Toast.makeText(this, "Пользователь не предоставил разрешений. Запись невозможна.", Toast.LENGTH_LONG).show();
                this.finish();
            }
        }
    }
}