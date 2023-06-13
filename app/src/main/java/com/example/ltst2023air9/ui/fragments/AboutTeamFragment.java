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
 * Use the {@link AboutTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutTeamFragment extends Fragment {


    public AboutTeamFragment() {
        // Required empty public constructor
    }

    public static AboutTeamFragment newInstance(String param1, String param2) {
        return new AboutTeamFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.ib_back).setOnClickListener(v -> {
            NavHostFragment.findNavController(AboutTeamFragment.this)
                    .navigate(R.id.action_aboutTeamFragment_to_legendFragment);
        });
    }
}