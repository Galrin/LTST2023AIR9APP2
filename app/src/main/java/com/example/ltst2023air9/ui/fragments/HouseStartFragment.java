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
import com.example.ltst2023air9.model.House;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseStartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HouseStartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HouseStartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HouseStartFragment newInstance(String param1, String param2) {
        HouseStartFragment fragment = new HouseStartFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_start, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initHelper(view);

        view.findViewById(R.id.house_start_fab).setOnClickListener(v -> {        initHelper(view);

        });
    }

    private void initHelper(View view) {

        TapTargetView.showFor(getActivity(),                 // `this` is an Activity
                TapTarget.forView(view.findViewById(R.id.house_start_fab),
                                "Начало пути",
                                "Убедитесь что вы перед зданием. Когда будете готовы - нажмите кнопку.")
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


                        ///
                        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();

                        List<House> houses = appDelegate.getHouses();

                        House house = new House();
                        houses.add(house);

                        Log.d("House uuid: ", house.getUuid());

                        appDelegate.setCurrentHouse(house);
                        Log.d("House uuid: ", house.getUuid());
                        ///

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            NavHostFragment.findNavController(HouseStartFragment.this)
                                    .navigate(R.id.action_houseStartFragment_to_flatStartFragment);
                        }, 250);
                        //              doSomething();
                    }
                });
    }
}