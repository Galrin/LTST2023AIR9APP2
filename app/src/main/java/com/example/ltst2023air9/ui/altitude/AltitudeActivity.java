package com.example.ltst2023air9.ui.altitude;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ltst2023air9.R;

public class AltitudeActivity extends AppCompatActivity implements SensorEventListener {

//    private final SensorManager mSensorManager;
//
////    public AltitudeActivity() {
////
////        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
////        //mAltitude = mSensorManager.getDefaultSensor(Sensor.)
////    }
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altitude);

        mTextView = findViewById(R.id.tv_altitude);
        mTextView.setText("0");


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //mTextView.setText(String.valueOf(mSensorManager.));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}