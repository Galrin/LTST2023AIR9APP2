package com.example.ltst2023air9.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {

    public MainMenuFragment() {
        // Required empty public constructor
    }

    public static MainMenuFragment newInstance(String param1, String param2) {
        return new MainMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        });
    }
}