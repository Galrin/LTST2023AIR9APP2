package com.example.ltst2023air9.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmCheckpoint;
import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.ui.fragments.flatinfo.reportlist.CheckpointListAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;


public class FlatInfoFragment extends Fragment {

    private TextView mTextObjectName;
    private TextView mTextObjectFlatCount;

    private RecyclerView mRecyclerView;

    private CheckpointListAdapter mAdapter;

    private RealmFlat flats;

    public FlatInfoFragment() {
        // Required empty public constructor
    }

    public static FlatInfoFragment newInstance(String param1, String param2) {
        return new FlatInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flat_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextObjectName = view.findViewById(R.id.tv_flat_info_object_name);
        mTextObjectFlatCount = view.findViewById(R.id.tv_flat_info_object_flat_count);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
        final String flatId = appDelegate.getCurrentRealmFlatId();
        Log.i("FlatInfo", "flat id: " + appDelegate.getCurrentRealmFlatId());

        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> {
            RealmFlat realmFlat = r.where(RealmFlat.class).equalTo("id", flatId).findFirst();
            mTextObjectName.setText("Кв. " + realmFlat.getNumber() + " Эт. " + realmFlat.getFloor());
            mTextObjectFlatCount.setText(String.valueOf(realmFlat.getCheckpoints().stream().count()));
        });


        view.findViewById(R.id.ib_flat_info_back).setOnClickListener(v -> {

            NavHostFragment.findNavController(FlatInfoFragment.this)
                    .navigate(R.id.action_flatInfoFragment_to_houseInfoFragment);
        });

        mAdapter = new CheckpointListAdapter(this);//, appDelegate.getHouses()
        mRecyclerView = view.findViewById(R.id.rv_checkpoint_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(10);

        flats = db.where(RealmFlat.class).equalTo("id", flatId).findFirstAsync();

        // The RealmChangeListener will be called when the results are asynchronously loaded, and available for use.
        RealmChangeListener<RealmFlat> realmChangeListener = realmFlat -> {
            mAdapter.setData(realmFlat.getCheckpoints());
        };
        flats.addChangeListener(realmChangeListener);
    }


    public void onRecyclerViewItemClick(int position, RealmCheckpoint realmCheckpoint) {
        Log.d("flatList", "position " + position);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
        appDelegate.setCurrentRealmCheckpointId(realmCheckpoint.getId());

        NavHostFragment.findNavController(FlatInfoFragment.this)
                .navigate(R.id.action_flatInfoFragment_to_multiReportTableFragment);
    }
}