package com.example.ltst2023air9.ui;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.R;
import com.example.ltst2023air9.YoloV5Ncnn;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import wseemann.media.FFmpegMediaMetadataRetriever;

//import static androidx.camera.core.internal.utils.ImageUtil.createBitmapFromImageProxy;
public class NCNNHelperFragment extends Fragment {
//    @NonNull
//    default Bitmap toBitmap() {
//        return createBitmapFromImageProxy(this);
//    }

    public static int VIDEO_SPEED_MAX = 20 + 1;
    public static int VIDEO_SPEED_MIN = 1;
    public static int YOLOV5S = 1;
    public static int YOLOV4_TINY = 2;
    public static int MOBILENETV2_YOLOV3_NANO = 3;
    public static int DBFACE = 8;
    public static int YOLOV5_CUSTOM_LAYER = 11;
    public static int USE_MODEL = MOBILENETV2_YOLOV3_NANO;
    public static boolean USE_GPU = false;
    private final AtomicBoolean detectCamera = new AtomicBoolean(false);
    private final AtomicBoolean detectPhoto = new AtomicBoolean(false);
    private final AtomicBoolean detectVideo = new AtomicBoolean(false);
    private final long endTime = 0;
    private final double threshold = 0.3;
    private final double nms_threshold = 0.7;
    private final YoloV5Ncnn yolov5ncnn = new YoloV5Ncnn();
    protected float videoSpeed = 1.0f;
    protected long videoCurFrameLoc = 0;
    protected Bitmap mutableBitmap;
    ExecutorService service;
    Recording recording = null;
    VideoCapture<Recorder> videoCapture = null;
    ImageButton capture, toggleFlash;
    PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    FFmpegMediaMetadataRetriever mmr;
    double total_fps = 0;
    int fps_count = 0;
    ImageView mImageView;
    //ImageView mPreviewImage;
    // ExecutorService detectService = Executors.newSingleThreadExecutor();
    private SeekBar sbVideo;
    private SeekBar sbVideoSpeed;
    //private final AtomicBoolean mIsPreviewDetectorEnable = new AtomicBoolean(false);
    private long startTime = 0;
    private int width;
    private int height;
    private Toolbar toolbar;
    private ImageView resultImageView;
    private TextView tvNMS;
    private TextView tvThreshold;
    private SeekBar nmsSeekBar;
    private SeekBar thresholdSeekBar;
    private TextView tvNMNThreshold;
    private TextView tvInfo;
    private Button btnPhoto;
    private Button btnVideo;
    private TextureView viewFinder;

    public NCNNHelperFragment() {
        // Required empty public constructor
    }

    public static NCNNHelperFragment newInstance(String param1, String param2) {
        NCNNHelperFragment fragment = new NCNNHelperFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_n_c_n_n_video, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 111) {
            // video
            runByVideo(requestCode, resultCode, data);
            //Toast.makeText(getActivity(), "resultCode!! ", Toast.LENGTH_SHORT).show();
            Log.d("onresult", "" + resultCode);
        } else {
            Toast.makeText(getActivity(), "Error selected ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mImageView = view.findViewById(R.id.iv_video_detector);

        Button pickVideo = view.findViewById(R.id.b_pick_video);
        Button anal = view.findViewById(R.id.b_anal);
        anal.setEnabled(true);

        pickVideo.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, 111);
            //anal.setEnabled(true);
        });

        anal.setOnClickListener(v -> {
            NavHostFragment.findNavController(NCNNHelperFragment.this)
                    .navigate(R.id.action_NCNNCameraHelperFragment_to_analyzeFragment);
        });


        boolean ret_init = yolov5ncnn.Init(getActivity().getAssets());
        if (!ret_init) {
            Log.e("MainActivity", "yolov5ncnn Init failed");
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        service.shutdown();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LIB
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void runByVideo(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK || data == null) {
            Toast.makeText(getActivity(), "Video error", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Uri uri = data.getData();
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                // String imgNo = cursor.getString(0); // 编号
                String v_path = cursor.getString(1); // 文件路径
                String v_size = cursor.getString(2); // 大小
                String v_name = cursor.getString(3); // 文件名
                detectOnVideo(v_path);
            } else {
                Toast.makeText(getActivity(), "Video is null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Video is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void detectOnVideo(final String path) {
        if (detectVideo.get()) {
            Toast.makeText(getActivity(), "Video is running", Toast.LENGTH_SHORT).show();
            return;
        }
        detectVideo.set(true);
        Toast.makeText(getActivity(), "FPS is not accurate!", Toast.LENGTH_SHORT).show();
        sbVideo.setVisibility(View.VISIBLE);
        sbVideoSpeed.setVisibility(View.VISIBLE);
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
                sbVideo.setMax(duration * 1000);
                float frameDis = 1.0f / fps * 1000 * 1000 * videoSpeed;
                videoCurFrameLoc = 0;
                while (detectVideo.get() && (videoCurFrameLoc) < (duration * 1000L)) {
                    videoCurFrameLoc = (long) (videoCurFrameLoc + frameDis);
                    sbVideo.setProgress((int) videoCurFrameLoc);
                    final Bitmap b = mmr.getFrameAtTime(videoCurFrameLoc, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                    if (b == null) {
                        continue;
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotate);
                    width = b.getWidth();
                    height = b.getHeight();
                    final Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, width, height, matrix, false);
                    startTime = System.currentTimeMillis();

                    /// bitmap.copy(Bitmap.Config.ARGB_8888, true)

                    //detectAndDraw(bitmap.copy(Bitmap.Config.ARGB_8888, true));
                    //showResultOnUI();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        mImageView.setImageBitmap(bitmap);
                    });
                    frameDis = 1.0f / fps * 1000 * 1000 * videoSpeed;
                }
                mmr.release();
                if (detectVideo.get()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            sbVideo.setVisibility(View.GONE);
                            sbVideoSpeed.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Video end!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                detectVideo.set(false);
            }
        }, "video detect");
        thread.start();
//        startCamera();
    }

    public Bitmap getPicture(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        if (bitmap == null) {
            return null;
        }
        int rotate = readPictureDegree(picturePath);
        return rotateBitmapByDegree(bitmap, rotate);
    }

    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
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


    protected Bitmap detectAndDraw(Bitmap image) {
        //Box[] result = null;
        YoloV5Ncnn.Obj[] result = yolov5ncnn.Detect(image, true);

        if (result == null) {
            detectCamera.set(false);
            return image;
        }
        if (USE_MODEL == YOLOV5S || USE_MODEL == YOLOV4_TINY || USE_MODEL == MOBILENETV2_YOLOV3_NANO
                || USE_MODEL == YOLOV5_CUSTOM_LAYER) {
            mutableBitmap = drawBoxRects(image, result);
        }
        return mutableBitmap;
    }
}