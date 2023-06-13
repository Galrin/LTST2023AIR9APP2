package com.example.ltst2023air9.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.evrencoskun.tableview.TableView;
import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.ui.fragments.tableview.TableViewAdapter;
import com.example.ltst2023air9.ui.fragments.tableview.TableViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportTableFragment extends Fragment {

    private TableView mTableView;

    public ReportTableFragment() {
        // Required empty public constructor
    }

    public static ReportTableFragment newInstance(String param1, String param2) {
        ReportTableFragment fragment = new ReportTableFragment();
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
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();

        ImageButton buttonBack = view.findViewById(R.id.b_anal_back);
        buttonBack.setOnClickListener(v -> {
            appDelegate.getTableViewModel().reinitCellList();

            NavHostFragment.findNavController(ReportTableFragment.this)
                    .navigate(R.id.action_reportsFragment_to_mainMenuFragment);
        });
        mTableView = view.findViewById(R.id.tableview);
        mTableView.setRowHeaderWidth(0);
        mTableView.setShowCornerView(false);
        mTableView.setClickable(false);


        TableViewModel tableViewModel = appDelegate.getTableViewModel();//new TableViewModel();
//        tableViewModel.updateRow(5, "hello!");

        TableViewAdapter tableViewAdapter = new TableViewAdapter(tableViewModel);
        mTableView.setAdapter(tableViewAdapter);
        tableViewAdapter.setAllItems(tableViewModel.getColumnHeaderList(), tableViewModel
                .getRowHeaderList(), tableViewModel.getCellList());

        //tableViewAdapter.updateRow(2, "hello!");


    }
}