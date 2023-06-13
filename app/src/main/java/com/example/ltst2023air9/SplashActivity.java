package com.example.ltst2023air9;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class SplashActivity extends AppCompatActivity {


    private final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.TIRAMISU
            ? new String[]{"android.permission.CAMERA", "android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_IMAGES"}
            : new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    AppDelegate appDelegate;
    boolean semaphore_click = false;

    boolean semaphore_onclicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.iv_logo).setOnClickListener(v -> {
            if (!semaphore_click) return;

            semaphore_onclicked = true;
            startActivity(new Intent(getApplicationContext(), MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            this.finish();
        });
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (allPermissionsGranted()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                this.finish();
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }

        }, 3800);
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                this.finish();
            } else {
                semaphore_click = true;
                if (semaphore_onclicked) return;
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                Toast.makeText(this, "Пользователь, выдай доступ!", Toast.LENGTH_LONG).show();
            }
        }

    }
}