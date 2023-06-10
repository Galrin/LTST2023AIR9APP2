package com.example.ltst2023air9.ui.ncnn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ltst2023air9.R;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.Flat;
import com.example.ltst2023air9.model.House;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import wseemann.media.FFmpegMediaMetadataRetriever;
public class NCNNCameraInvestigationFragment extends Fragment {

    public NCNNCameraInvestigationFragment() {
        // Required empty public constructor
    }


    public static NCNNCameraInvestigationFragment newInstance(String param1, String param2) {
        return new NCNNCameraInvestigationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_n_c_n_n_camera_investigation, container, false);
    }
    //import static androidx.camera.core.internal.utils.ImageUtil.createBitmapFromImageProxy;

        public static int VIDEO_SPEED_MAX = 20 + 1;
        public static int VIDEO_SPEED_MIN = 1;
        public static int MOBILENETV2_YOLOV3_NANO = 3;
        public static int USE_MODEL = MOBILENETV2_YOLOV3_NANO;
        public static boolean USE_GPU = true;

        protected Bitmap mutableBitmap;
        ExecutorService service;
        Recording recording = null;
        VideoCapture<Recorder> videoCapture = null;
        ImageButton capture, toggleFlash;
        PreviewView previewView;
        int cameraFacing = CameraSelector.LENS_FACING_BACK;

        ImageView mPreviewImage;
        ExecutorService detectService = Executors.newSingleThreadExecutor();

        private final AtomicBoolean detectCamera = new AtomicBoolean(false);
        private final AtomicBoolean detectVideo = new AtomicBoolean(false);



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            mPreviewImage = view.findViewById(R.id.iv_prev_detector);
            ImageButton cameraInvestigation = view.findViewById(R.id.ib_camera_investigation);
            cameraInvestigation.setOnClickListener(v -> {

            });



//        startCamera(cameraFacing);


            service = Executors.newSingleThreadExecutor();
        }

        @Override
        public void onStart() {
            super.onStart();

            startCamera(cameraFacing);
        }


        public void startCamera(int cameraFacing) {
            ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(getActivity());

            processCameraProvider.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = processCameraProvider.get();
                    Preview preview = new Preview.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());

                    Recorder recorder = new Recorder.Builder()
                            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                            .build();
                    videoCapture = VideoCapture.withOutput(recorder);

                    cameraProvider.unbindAll();

                    CameraSelector cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(cameraFacing).build();

                    ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                            .setTargetRotation(previewView.getDisplay().getRotation())
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                            //.setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                            .build();

                    imageAnalysis.setAnalyzer(detectService, new ImageAnalysis.Analyzer() {
                        @Override
                        public void analyze(@NonNull ImageProxy image) {
                            //Log.v("anal", "atomic set is: " + mIsPreviewDetectorEnable.get());
                            //if (mIsPreviewDetectorEnable.get()) {

                                Bitmap bitmapBuffer = Bitmap.createBitmap(
                                        image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
                                try {
                                    bitmapBuffer.copyPixelsFromBuffer(image.getPlanes()[0].getBuffer());
                                } catch (Exception e) {}


                                //Bitmap bmp = previewView.getBitmap(); // ужастный вариант. скорее избавиться от него!

                                /*  по скорости хорошо, но глючный кадр */
                                //@SuppressLint("UnsafeOptInUsageError") Bitmap bmp = toBitmap(image.getImage());

                                // портировано со старой версии 1 в 1, на выходе полностью искажено
                                //Bitmap bmp = imageToBitmap(image);

                                new Handler(Looper.getMainLooper()).post(() -> {

                                    mPreviewImage.setImageBitmap(bitmapBuffer);
                                });
                            //}
                            image.close();

                        }
                    });

                    Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

//                    toggleFlash.setOnClickListener(view -> toggleFlash(camera));

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }, ContextCompat.getMainExecutor(getActivity()));
        }


        private void toggleFlash(Camera camera) {
            if (camera.getCameraInfo().hasFlashUnit()) {
                if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                    camera.getCameraControl().enableTorch(true);
                    toggleFlash.setImageResource(R.drawable.round_flash_off_24);
                } else {
                    camera.getCameraControl().enableTorch(false);
                    toggleFlash.setImageResource(R.drawable.round_flash_on_24);
                }
            } else {
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getActivity(), "Flash is not available currently", Toast.LENGTH_SHORT).show());
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            service.shutdown();
        }


    }