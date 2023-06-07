package com.example.ltst2023air9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


public class SplashActivity extends AppCompatActivity {


    AppDelegate appDelegate;
    private final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.TIRAMISU
            ? new String[]{"android.permission.CAMERA", "android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_AUDIO", "android.permission.READ_MEDIA_IMAGES"}
            : new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    boolean semaphore_click = false;

    boolean semaphore_onclicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.iv_logo).setOnClickListener(v -> {
            if(!semaphore_click) return;

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

        appDelegate = (AppDelegate) getApplicationContext();

       // initInstanceState();

    }
//
//    public void initInstanceState() { // во время splashscreen
//
//        String[] fileList = getApplicationContext().fileList();
//        int fileListSize = fileList.length;
//
//        appDelegate.getHouses().clear();
//
//        for(String houseUUID : fileList) {
//            Log.d("FILE: ", houseUUID);
//            if(houseUUID.startsWith("+")) {
//                //.add(new House(fileUUID));
//
//
//                // загружаем json text. превращаем в class instance
//
//                // заносим полученый класс в список
//                //houses.add(house);
//
//                --fileListSize;
//
//                // progressBar>> ?
//            }
//        }
//    }
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
                if(semaphore_onclicked) return;
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                Toast.makeText(this, "Пользователь, выдай доступ!", Toast.LENGTH_LONG).show();
            }
        }

    }
}