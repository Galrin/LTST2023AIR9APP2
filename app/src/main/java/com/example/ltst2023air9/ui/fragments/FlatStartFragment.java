package com.example.ltst2023air9.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FlatStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlatStartFragment extends Fragment {


    public FlatStartFragment() {
        // Required empty public constructor
    }

    public static FlatStartFragment newInstance(String param1, String param2) {
        return new FlatStartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flat_start, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initHelper(view);

        view.findViewById(R.id.flat_start_fab).setOnClickListener(v -> {
            initHelper(view);
        });
    }

    private void initHelper(View view) {

        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(view.findViewById(R.id.flat_start_fab), "Новый осмотр", "Убедитесь что вы рядом с квартирой. Когда будете готовы - нажмите кнопку.")
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

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            NavHostFragment.findNavController(FlatStartFragment.this).navigate(R.id.action_flatStartFragment_to_flatStartMenuFragment);
                        }, 250);
                    }
                });
    }
}