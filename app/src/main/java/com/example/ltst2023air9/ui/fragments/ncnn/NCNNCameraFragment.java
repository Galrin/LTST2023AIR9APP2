package com.example.ltst2023air9.ui.fragments.ncnn;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
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

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.util.MediaStorageUtil;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.Checkpoint;
import com.example.ltst2023air9.model.RealmCheckpoint;
import com.example.ltst2023air9.model.RealmFlat;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;

public class NCNNCameraFragment extends Fragment {

    ExecutorService service;
    Recording recording = null;
    VideoCapture<Recorder> videoCapture = null;
    ImageButton capture, toggleFlash;
    PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;

//    RealmHouse mRealmHouse;
//    RealmFlat mRealmFlat;
    private String currentRealmHouseId;
    private String currentRealmFlatId;
    private int mCurrentCheckpointNumber;

    public NCNNCameraFragment() {
        // Required empty public constructor
    }

    public static NCNNCameraFragment newInstance(String param1, String param2) {
        NCNNCameraFragment fragment = new NCNNCameraFragment();
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
        return inflater.inflate(R.layout.fragment_n_c_n_n_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
//        House house = appDelegate.getCurrentHouse();
//        Flat flat = appDelegate.getCurrentFlat();
        currentRealmHouseId = appDelegate.getCurrentRealmHouseId();
        currentRealmFlatId = appDelegate.getCurrentRealmFlatId();


        previewView = view.findViewById(R.id.viewFinder);
        capture = view.findViewById(R.id.capture);
        toggleFlash = view.findViewById(R.id.toggleFlash);
        capture.setOnClickListener(v -> {
            captureVideo();
        });
//
//        startCamera(cameraFacing);


        service = Executors.newSingleThreadExecutor();

        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> {

            RealmFlat realmFlat = r.where(RealmFlat.class).equalTo("id", currentRealmFlatId).findFirst();
            if (realmFlat != null) {
                mCurrentCheckpointNumber = realmFlat.getCurrentCheckpointNumber();
            }
        });

        ImageButton cameraInvestigation = view.findViewById(R.id.ib_camera_investigation);
        cameraInvestigation.setOnClickListener(v -> {
            NavHostFragment.findNavController(NCNNCameraFragment.this)
                    .navigate(R.id.action_NCNNCameraFragment_to_NCNNCameraInvestigationFragment);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        startCamera(cameraFacing);
    }


    public void captureVideo() {
        capture.setImageResource(R.drawable.round_stop_circle_24);
        Recording recording1 = recording;
        if (recording1 != null) {
            recording1.stop();
            recording = null;
            return;
        }

        //String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault()).format(System.currentTimeMillis());

        String name = currentRealmHouseId + "_" + currentRealmFlatId + "_" + String.valueOf(mCurrentCheckpointNumber);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/ltst2023air9");

        MediaStoreOutputOptions options = new MediaStoreOutputOptions.Builder(getActivity().getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues).build();

        recording = videoCapture.getOutput().prepareRecording(getActivity(), options).start(ContextCompat.getMainExecutor(getActivity()), videoRecordEvent -> {
            if (videoRecordEvent instanceof VideoRecordEvent.Start) {
                capture.setEnabled(true);
            } else if (videoRecordEvent instanceof VideoRecordEvent.Finalize) {
                if (!((VideoRecordEvent.Finalize) videoRecordEvent).hasError()) {
                    String msg = "Video capture succeeded: " + ((VideoRecordEvent.Finalize) videoRecordEvent).getOutputResults().getOutputUri();
                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    Uri outputUri = ((VideoRecordEvent.Finalize) videoRecordEvent).getOutputResults().getOutputUri();

                    final String y = MediaStorageUtil.getPath(getContext(), outputUri);
                    Log.i("VIDEOPATH", "Recorded video uri is "+ y);


                    final AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();


                    final String currentRealmFlatId = appDelegate.getCurrentRealmFlatId();

                    Realm db = Realm.getDefaultInstance();

                    Checkpoint checkpoint = appDelegate.getCheckpoints().get(mCurrentCheckpointNumber);
                    final String checkpointName = checkpoint.getName();
                    db.executeTransactionAsync(r -> {
                        RealmCheckpoint realmCheckpoint = r.createObject(RealmCheckpoint.class, UUID.randomUUID().toString());

                        realmCheckpoint.setName(checkpointName);
                        realmCheckpoint.setVideoPath(y);

                        RealmFlat realmFlat = r.where(RealmFlat.class).equalTo("id", currentRealmFlatId).findFirst();

                        if (realmFlat != null) {
                            realmFlat.getCheckpoints().add(realmCheckpoint);
                        }
                    });


                    if( mCurrentCheckpointNumber < ( appDelegate.getCheckpoints().size() -1 ) ) {
                        /// GOTO: STEP REPEAT
                        //flat.setCurrentCheckpoint(flat.getCurrentCheckpoint() + 1);

                        db.executeTransactionAsync(r -> {

                            RealmFlat realmFlat = r.where(RealmFlat.class).equalTo("id", currentRealmFlatId).findFirst();
                            if (realmFlat != null) {
                                realmFlat.setCurrentCheckpointNumber(mCurrentCheckpointNumber + 1);
                            }

                        });

                        NavHostFragment.findNavController(NCNNCameraFragment.this)
                                .navigate(R.id.action_NCNNCameraFragment_to_flatStepFragment);
                    } else { /// GOTO: ANAL
                        //flat.setCurrentCheckpoint( 0 );
                    db.executeTransactionAsync(r -> {

                        RealmFlat realmFlat = r.where(RealmFlat.class).equalTo("id", currentRealmFlatId).findFirst();
                        if (realmFlat != null) {
                            realmFlat.setCurrentCheckpointNumber(0);
                        }

                    });
                        NavHostFragment.findNavController(NCNNCameraFragment.this)
                                .navigate(R.id.action_NCNNCameraFragment_to_analyzeFragment);
                    }


                } else {
                    recording.close();
                    recording = null;
                    //Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
                capture.setImageResource(R.drawable.round_fiber_manual_record_24);
            }
        });
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

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);

                toggleFlash.setOnClickListener(view -> toggleFlash(camera));
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

    private String UriToString(Uri uri) {
        try {
            //Uri uri = data.getData();
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                Object t = cursor.getColumnNames();
                // String imgNo = cursor.getString(0); // 编号
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                String v_path = cursor.getString(column_index); // 文件路径
                //String v_size = cursor.getString(2); // 大小
                //String v_name = cursor.getString(3); // 文件名
                return v_path;
            } else {
                //Toast.makeText(getActivity(), "Video is null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getActivity(), "Video is null", Toast.LENGTH_SHORT).show();
        }
        finally {
            return "";
        }
    }

}