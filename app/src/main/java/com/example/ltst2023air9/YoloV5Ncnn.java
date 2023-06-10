package com.example.ltst2023air9;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

public class YoloV5Ncnn
{
    static {
        System.loadLibrary("yolov5ncnn");
    }
    public native boolean Init(AssetManager assetManager);
    public class Obj
    {
        public float x;
        public float y;
        public float w;
        public float h;
        public String label;
        public float prob;

        public String room_label;
        public float room_prob;
    }

    public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);


}

