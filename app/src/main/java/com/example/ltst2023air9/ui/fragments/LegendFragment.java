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
 * Use the {@link LegendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LegendFragment extends Fragment {

    public LegendFragment() {
        // Required empty public constructor
    }

    public static LegendFragment newInstance(String param1, String param2) {
        return new LegendFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_legend, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.b_legend_back).setOnClickListener(v -> {
            NavHostFragment.findNavController(LegendFragment.this).navigate(R.id.action_legendFragment_to_mainMenuFragment);
        });

        view.findViewById(R.id.tv_show_our_team).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NavHostFragment.findNavController(LegendFragment.this).navigate(R.id.action_legendFragment_to_aboutTeamFragment);
                return false;
            }
        });
    }
}