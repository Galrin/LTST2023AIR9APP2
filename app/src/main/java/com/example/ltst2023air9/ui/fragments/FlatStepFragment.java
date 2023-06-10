package com.example.ltst2023air9.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.Checkpoint;
import com.example.ltst2023air9.model.RealmFlat;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import io.realm.Realm;


public class FlatStepFragment extends Fragment {

    private int mCurrentCheckpointNumber = 0;

    public FlatStepFragment() {
        // Required empty public constructor
    }

    public static FlatStepFragment newInstance(String param1, String param2) {
        FlatStepFragment fragment = new FlatStepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();


        final String currentRealmFlatId = appDelegate.getCurrentRealmFlatId();

        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> {
            Log.i("FlatStep", "------------------");

            RealmFlat realmFlat = r.where(RealmFlat.class).equalTo("id", currentRealmFlatId).findFirst();
            if (realmFlat != null) {
                mCurrentCheckpointNumber = realmFlat.getCurrentCheckpointNumber();
                Log.i("FlatStep", "current RealmFlatId: " + realmFlat.getId());
                Log.i("FlatStep", "current GLOBAL checkpoint: " + mCurrentCheckpointNumber);
            }
            Log.i("FlatStep", "//------------------");

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flat_step, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            initHelper(view);
        }, 300);


        view.findViewById(R.id.flat_step_fab).setOnClickListener(v -> {
//            NavHostFragment.findNavController(FlatStepFragment.this)
//                    .navigate(R.id.action_flatStepFragment_to_flatStepFragment);
            initHelper(view);
        });


//        Flat flat = appDelegate.getCurrentFlat();
//
//        Log.d("step", "flat uuid: " + flat.getUuid());
//        Log.d("step", "flat section: " + flat.getSection());
//        Log.d("step", "flat floor: " + flat.getFloor());

    }

    private void initHelper(View view) {

        AppDelegate appDelegate = (AppDelegate) view.getContext().getApplicationContext();
//        Flat flat = appDelegate.getCurrentFlat();

        Checkpoint checkpoint = appDelegate.getCheckpoints().get(mCurrentCheckpointNumber);

        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(view.findViewById(R.id.flat_step_fab),
                                "Точка осмотра",
                                "Подойдите к точке: " + checkpoint.getName().toUpperCase() + ". Когда будете готовы - запишите видео.")
                        // All options below are optional
                        .outerCircleColor(R.color.white)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.blue)   // Specify a color for the target circle
                        .titleTextSize(54)                  // Specify the size (in sp) of the title text
                        .titleTextColor(com.google.android.material.R.color.design_default_color_secondary_variant)      // Specify the color of the title text
                        .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.blue)  // Specify the color of the description text
                        .textColor(R.color.blue)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(false)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        //                   .icon(Drawable)                     // Specify a custom drawable to draw as the target
                        .targetRadius(40),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        //              doSomething();
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                            Intent intent = new Intent(view.getContext().getApplicationContext(), NCNNCameraActivity.class);
//                            Bundle extras = new Bundle();
//                            extras.putString("from", FlatStepFragment.class.getSimpleName());
//                            intent.putExtras(extras);
//
//                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//

                            NavHostFragment.findNavController(FlatStepFragment.this)
                                    .navigate(R.id.action_flatStepFragment_to_NCNNCameraFragment);
                        }, 250);
                    }
                });
    }
}