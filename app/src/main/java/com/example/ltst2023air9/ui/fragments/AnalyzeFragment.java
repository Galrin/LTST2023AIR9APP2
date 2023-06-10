package com.example.ltst2023air9.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.YoloV5Ncnn;
import com.example.ltst2023air9.model.Checkpoint;
import com.example.ltst2023air9.ui.DetectorResultsAnalyzer;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalyzeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalyzeFragment extends Fragment {
    public static boolean USE_GPU = true;

    public static DetectorResultsAnalyzer detectorAnalyzer = new DetectorResultsAnalyzer();
    private final YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();
    LinearLayout mRootLayout;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    protected long videoCurFrameLoc = 0;
    protected float videoSpeed = 5.0f;
    FFmpegMediaMetadataRetriever mmr;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnalyzeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnalyzeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalyzeFragment newInstance(String param1, String param2) {
        AnalyzeFragment fragment = new AnalyzeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        boolean ret_init = yolov5ncnn.Init(getActivity().getAssets());
        if (!ret_init) {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analyze, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.b_anal_next).setOnClickListener(v -> {
            // переход к кнопке старт комнаты
            NavHostFragment.findNavController(AnalyzeFragment.this)
                    .navigate(R.id.action_analyzeFragment_to_flatStartFragment);

        });
        view.findViewById(R.id.b_anal_button).setOnClickListener(v -> {

            // сохраняем всё в flat, house
            // в меню
            NavHostFragment.findNavController(AnalyzeFragment.this)
                    .navigate(R.id.action_analyzeFragment_to_mainMenuFragment);

        });

        mRootLayout = view.findViewById(R.id.ll_analyze_content);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();

        for(Checkpoint cp: appDelegate.getCheckpoints()) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setPadding(10, 10, 10, 10);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            ProgressBar progressBar = new ProgressBar(getActivity());
            progressBar.setProgress(4, true);
            progressBar.setMax(100);

            TextView textView = new TextView(getActivity());
            textView.setText(cp.getName());
            textView.setTextSize(26);
            textView.setTextColor(Color.WHITE);
            textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

            linearLayout.addView(progressBar);
            linearLayout.addView(textView);


            mRootLayout.addView(linearLayout);

            //detectOnVideo(cp.getVideoPath());
        }

    }

    public void startInvestigation(String videoPath) {
                detectOnVideo(videoPath);

    }


    public void detectOnVideo(final String path) {
        if (isRunning.get()) {
            Toast.makeText(getActivity(), "Video is running", Toast.LENGTH_SHORT).show();
            return;
        }
        isRunning.set(true);
        detectorAnalyzer.clear();
        //Toast.makeText(getActivity(), "FPS is not accurate!", Toast.LENGTH_SHORT).show();
        //sbVideo.setVisibility(View.VISIBLE);
        //sbVideoSpeed.setVisibility(View.VISIBLE);
        try {
            ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(getActivity());

            ProcessCameraProvider cameraProvider = processCameraProvider.get();

            cameraProvider.unbindAll();
        } catch (Exception e) {
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mmr = new FFmpegMediaMetadataRetriever();

                mmr.setDataSource(path);
                String dur = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);  // ms
                String sfps = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE);  // fps
//                String sWidth = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);  // w
//                String sHeight = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);  // h
                String rota = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);  // rotation
                int duration = Integer.parseInt(dur);
                float fps = Float.parseFloat(sfps);
                float rotate = 0;
                if (rota != null) {
                    rotate = Float.parseFloat(rota);
                }
                //sbVideo.setMax(duration * 1000);
                float frameDis = 1.0f / fps * 1000 * 1000 * videoSpeed;
                videoCurFrameLoc = 0;

                Handler handler = new Handler(Looper.getMainLooper());
                AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();

                while (isRunning.get() && (videoCurFrameLoc) < (duration * 1000L)) {
                    videoCurFrameLoc = (long) (videoCurFrameLoc + frameDis);
                    //sbVideo.setProgress((int) videoCurFrameLoc);
                    final Bitmap b = mmr.getFrameAtTime(videoCurFrameLoc, FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC );
                    if (b == null) {
                        continue;
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotate);
                    int width = b.getWidth();
                    int height = b.getHeight();
                    final Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, width, height, matrix, false);
                    //startTime = System.currentTimeMillis();

                    /// bitmap.copy(Bitmap.Config.ARGB_8888, true)

                    //Bitmap drawBitmap = detectAndDraw(bitmap.copy(Bitmap.Config.ARGB_8888, true));


                    YoloV5Ncnn.Obj[] result = yolov5ncnn.Detect(bitmap, USE_GPU);
                    //final Bitmap drawBitmap = drawBoxRects(bitmap.copy(Bitmap.Config.ARGB_8888, true), result);

                    if (result != null) {
                        detectorAnalyzer.add(result);
                    }


//                    //showResultOnUI();
//                    handler.post(() -> {
//                        mImageView.setImageBitmap(drawBitmap);
//                    });
                    frameDis = 1.0f / fps * 1000 * 1000 * videoSpeed;

                    //appDelegate.getTableViewModel().updateRow(11, "hello % for..");
                }
                mmr.release();
                ArrayList<String> metrics = detectorAnalyzer.detect();
                int row = 0;
                for (String val : metrics) {
                    appDelegate.getTableViewModel().updateRow(row, val);
                    row += 1;
                }

                if (isRunning.get()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //sbVideo.setVisibility(View.GONE);
                            //sbVideoSpeed.setVisibility(View.GONE);
                           // anal.setEnabled(true);
                            Log.i("Investigate", "end video " + path);
                            Toast.makeText(getActivity(), "Video end!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                isRunning.set(false);
            }
        }, "video detect");
        thread.start();
//        startCamera();
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