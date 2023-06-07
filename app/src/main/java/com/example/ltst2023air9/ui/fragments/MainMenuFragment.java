package com.example.ltst2023air9.ui.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.MainActivity;
import com.example.ltst2023air9.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance(String param1, String param2) {
        MainMenuFragment fragment = new MainMenuFragment();
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
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.b_main_create).setOnClickListener(v -> {
            NavHostFragment.findNavController(MainMenuFragment.this)
                    .navigate(R.id.action_mainMenuFragment_to_houseStartFragment);
        });

        view.findViewById(R.id.b_main_reports).setOnClickListener(v -> {
            NavHostFragment.findNavController(MainMenuFragment.this)
                    .navigate(R.id.action_mainMenuFragment_to_reportListFragment);
        });

        view.findViewById(R.id.b_main_legend).setOnClickListener(v -> {
            NavHostFragment.findNavController(MainMenuFragment.this)
                    .navigate(R.id.action_mainMenuFragment_to_legendFragment);
        });

        view.findViewById(R.id.b_main_test).setOnClickListener(v -> {

            NavHostFragment.findNavController(MainMenuFragment.this)
                    .navigate(R.id.action_mainMenuFragment_to_NCNNCameraHelperFragment);

            //startActivity(new Intent(getActivity(), TestCameraNCNNYoloActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

        });
    }
}