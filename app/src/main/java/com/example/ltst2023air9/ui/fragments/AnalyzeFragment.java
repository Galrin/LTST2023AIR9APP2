package com.example.ltst2023air9.ui.fragments;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.YoloV5Ncnn;
import com.example.ltst2023air9.model.RealmCheckpoint;
import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.ui.DetectorResultsAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import io.realm.Realm;


public class AnalyzeFragment extends Fragment {
    public static boolean USE_GPU = true;

    public static DetectorResultsAnalyzer detectorAnalyzer = new DetectorResultsAnalyzer();
    private final YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();
    private final Stack<Thread> mThreadList = new Stack<>();
    LinearLayout mRootLayout;
    private Realm db;

    public AnalyzeFragment() {
        // Required empty public constructor
    }

    public static AnalyzeFragment newInstance(String param1, String param2) {
        return new AnalyzeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        view.findViewById(R.id.b_anal_reportlist).setOnClickListener(v -> {

            // сохраняем всё в flat, house
            // в меню
            NavHostFragment.findNavController(AnalyzeFragment.this)
                    .navigate(R.id.action_analyzeFragment_to_reportListFragment);

        });

        mRootLayout = view.findViewById(R.id.ll_analyze_content);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
        final String currentRealmFlatId = appDelegate.getCurrentRealmFlatId();
        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> {
            RealmFlat realmFlat = r.where(RealmFlat.class).equalTo("id", currentRealmFlatId).findFirst();
            if (realmFlat != null) {
                for (RealmCheckpoint rcp : realmFlat.getCheckpoints()) {


                    LinearLayout linearLayout = new LinearLayout(getActivity());
                    linearLayout.setPadding(20, 20, 20, 20);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                    ProgressBar progressBar = new ProgressBar(getActivity(), null,
                            android.R.attr.progressBarStyleHorizontal);
                    progressBar.setProgress(0, true);

                    final int vid = View.generateViewId();
                    progressBar.setId(vid);
                    progressBar.setMax(100);
                    progressBar.setMin(0);
                    progressBar.setProgress(0);

                    TextView textView = new TextView(getActivity());
                    textView.setText(rcp.getName());
                    textView.setTextSize(26);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                    linearLayout.addView(progressBar);
                    linearLayout.addView(textView);


                    new Handler(Looper.getMainLooper()).post(() -> mRootLayout.addView(linearLayout));

                    detectOnVideo(rcp.getVideoPath(), rcp.getId(), vid);


                }
                if (!mThreadList.isEmpty()) {
                    Thread next = mThreadList.pop();
                    next.start();
                }
            }
        });

    }


    public void detectOnVideo(final String path, final String checkpointId, final int vid) {

        detectorAnalyzer.clear();

        Thread thread = new Thread(new Runnable() {


            @Override
            public void run() {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();

                mmr.setDataSource(path);

                int numFrames = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT));

                String dur = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);  // ms
//                String sWidth = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);  // w
//                String sHeight = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);  // h
                String rota = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);  // rotation
                int duration = Integer.parseInt(dur);
                //float fps = Float.parseFloat(sfps);
                float rotate = 0;
                if (rota != null) {
                    rotate = Float.parseFloat(rota);
                }
                //sbVideo.setMax(duration * 1000);
                //float frameDis = 1.0f / fps * 1000 * 1000 * videoSpeed;

                Handler handler = new Handler(Looper.getMainLooper());

                for (int i = 0; i < numFrames; i++) {

                    final Bitmap b = mmr.getFrameAtIndex(i);//getFrameAtTime(videoCurFrameLoc, MediaMetadataRetriever.OPTION_CLOSEST_SYNC );
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
                    final Bitmap drawBitmap = drawBoxRects(bitmap.copy(Bitmap.Config.ARGB_8888, true), result);

                    if (result != null) {
                        detectorAnalyzer.add(result);
                    }

                    final int finalI = i;
                    handler.post(() -> {
                        ProgressBar progressBar = mRootLayout.findViewById(vid);
                        progressBar.setMax(numFrames);
                        progressBar.setProgress(finalI, true);
                    });

                    //showResultOnUI();
                    try {
                        Thread.sleep(3); // 30 fps :D
                    } catch (InterruptedException e) {

                    }
                }
                try {
                    mmr.release();
                } catch (IOException e) {

                }
                final ArrayList<String> metrics = detectorAnalyzer.detect();

                db = Realm.getDefaultInstance();
                //Realm db = this.db;
                db.executeTransactionAsync(realm -> {
                    RealmCheckpoint realmCheckpoint = realm.where(RealmCheckpoint.class).equalTo("id", checkpointId).findFirst();
                    if (realmCheckpoint != null) {

                        int row = 0;
                        for (String val : metrics) {
                            //appDelegate.getTableViewModel().updateRow(row, val);
                            realmCheckpoint.getMetrics().add(val);
                            row += 1;
                        }
                    }
                });

                new Handler(Looper.getMainLooper()).post(() -> {
                    ProgressBar progressBar = mRootLayout.findViewById(vid);

                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

                    if (!mThreadList.isEmpty()) {
                        Thread next = mThreadList.pop();
                        next.start();
                    }
                });

            }


        }, "video detect");
        mThreadList.push(thread); //.start();
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