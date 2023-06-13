package com.example.ltst2023air9.ui.fragments.ncnn;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ltst2023air9.R;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.YoloV5Ncnn;
import com.example.ltst2023air9.ui.DetectorResultsAnalyzer;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class NCNNCameraInvestigationFragment extends Fragment {
    private final YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();
    public static DetectorResultsAnalyzer detectorAnalyzer = new DetectorResultsAnalyzer();

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
        boolean ret_init = yolov5ncnn.Init(getActivity().getAssets());
        if (!ret_init) {
            Log.e("NCNNCameraInvestigation", "yolov5ncnn Init failed");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPreviewImage = view.findViewById(R.id.iv_prev_detector);
        ImageButton cameraInvestigation = view.findViewById(R.id.ib_camera_investigation);
        cameraInvestigation.setOnClickListener(v -> {
            NavHostFragment.findNavController(NCNNCameraInvestigationFragment.this)
                    .navigate(R.id.action_NCNNCameraInvestigationFragment_to_NCNNCameraFragment);
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
                //preview.setSurfaceProvider(previewView.getSurfaceProvider());

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);

                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                        //.setTargetRotation(Surface.ROTATION_180)
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


//
//                        Matrix matrix = new Matrix();
//                        matrix.postRotate(180);
//
//                        int width = image.getWidth();
//                        int height = image.getHeight();
//
//                        final Bitmap bitmap = Bitmap.createBitmap(bitmapBuffer, 0, 0, width, height, matrix, false);
//


                        YoloV5Ncnn.Obj[] result = yolov5ncnn.Detect(bitmapBuffer, USE_GPU);
                        final Bitmap drawBitmap = drawBoxRects(bitmapBuffer.copy(Bitmap.Config.ARGB_8888, true), result);



                        if (result != null) {
                            detectorAnalyzer.add(result);
                        }
                        //Bitmap bmp = previewView.getBitmap(); // ужастный вариант. скорее избавиться от него!

                        /*  по скорости хорошо, но глючный кадр */
                        //@SuppressLint("UnsafeOptInUsageError") Bitmap bmp = toBitmap(image.getImage());

                        // портировано со старой версии 1 в 1, на выходе полностью искажено
                        //Bitmap bmp = imageToBitmap(image);

                        new Handler(Looper.getMainLooper()).post(() -> {
                            mPreviewImage.setImageBitmap(drawBitmap);
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


    ////////////////////////////////////////////////
    protected Bitmap drawBoxRects(Bitmap mutableBitmap, YoloV5Ncnn.Obj[] objects) {
        if (objects == null) {
            return mutableBitmap;
        }
        Canvas canvas = new Canvas(mutableBitmap);
        final Paint boxPaint = new Paint();

        final int[] colors = new int[]{
                Color.rgb(54, 67, 244),
                Color.rgb(99, 30, 233),
                Color.rgb(176, 39, 156),
                Color.rgb(183, 58, 103),
                Color.rgb(181, 81, 63),
                Color.rgb(243, 150, 33),
                Color.rgb(244, 169, 3),
                Color.rgb(212, 188, 0),
                Color.rgb(136, 150, 0),
                Color.rgb(80, 175, 76),
                Color.rgb(74, 195, 139),
                Color.rgb(57, 220, 205),
                Color.rgb(59, 235, 255),
                Color.rgb(7, 193, 255),
                Color.rgb(0, 152, 255),
                Color.rgb(34, 87, 255),
                Color.rgb(72, 85, 121),
                Color.rgb(158, 158, 158),
                Color.rgb(139, 125, 96)
        };

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        Paint textbgpaint = new Paint();
        textbgpaint.setColor(Color.WHITE);
        textbgpaint.setStyle(Paint.Style.FILL);

        Paint textpaint = new Paint();
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(26);
        textpaint.setTextAlign(Paint.Align.LEFT);

        for (int i = 0; i < objects.length; i++) {
            paint.setColor(colors[i % 19]);

            canvas.drawRect(objects[i].x, objects[i].y, objects[i].x + objects[i].w, objects[i].y + objects[i].h, paint);

            // draw filled text inside image
            {
                String text = objects[i].label + " = " + String.format("%.1f", objects[i].prob * 100) + "%";

                float text_width = textpaint.measureText(text);
                float text_height = -textpaint.ascent() + textpaint.descent();

                float x = objects[i].x;
                float y = objects[i].y - text_height;
                if (y < 0)
                    y = 0;
                if (x + text_width > mutableBitmap.getWidth())
                    x = mutableBitmap.getWidth() - text_width;

                canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);

                canvas.drawText(text, x, y - textpaint.ascent(), textpaint);
            }
        }
        return mutableBitmap;
    }

}