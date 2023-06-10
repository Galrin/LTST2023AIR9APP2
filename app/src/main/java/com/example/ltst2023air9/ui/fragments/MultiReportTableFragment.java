package com.example.ltst2023air9.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.evrencoskun.tableview.TableView;
import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmCheckpoint;
import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.ui.fragments.tableview.TableViewAdapter;
import com.example.ltst2023air9.ui.fragments.tableview.TableViewModel;

import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MultiReportTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultiReportTableFragment extends Fragment {

    private static List<RealmCheckpoint> realmCheckpointList =  Collections.emptyList();
    private static int selectedCheckpoint = 0;
    private TableView mTableView;
    private TextView mTextHeader;

    public MultiReportTableFragment() {
        // Required empty public constructor
    }

    public static MultiReportTableFragment newInstance(String param1, String param2) {
        MultiReportTableFragment fragment = new MultiReportTableFragment();
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
        final String currentFlatId = appDelegate.getCurrentRealmFlatId();
        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> {
            RealmFlat realmFlat = r.where(RealmFlat.class).equalTo("id", currentFlatId).findFirst();
            if(realmFlat != null) {
                RealmList<RealmCheckpoint> checkpoints = realmFlat.getCheckpoints();
                realmCheckpointList = checkpoints;

                
            }
        });
        mTextHeader = view.findViewById(R.id.tv_table_header);
        mTextHeader.setText(getActivity().getString(R.string.table_header_text) + ": " + realmCheckpointList.get(selectedCheckpoint).getName());

        ImageButton buttonBack = view.findViewById(R.id.b_anal_back);
        buttonBack.setOnClickListener(v -> {
            appDelegate.getTableViewModel().reinitCellList();

            NavHostFragment.findNavController(MultiReportTableFragment.this)
                    .navigate(R.id.action_multiReportTableFragment_to_mainMenuFragment);
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