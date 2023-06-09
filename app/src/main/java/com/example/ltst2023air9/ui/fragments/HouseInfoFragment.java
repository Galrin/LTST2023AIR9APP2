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
import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.model.RealmHouse;
import com.example.ltst2023air9.ui.fragments.houseInfo.reportlist.FlatListAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;


public class HouseInfoFragment extends Fragment {
    private TextView mTextObjectName;
    private TextView mTextObjectFlatCount;

    private RecyclerView mRecyclerView;

    private FlatListAdapter mAdapter;

    private RealmHouse flats;

    public HouseInfoFragment() {
        // Required empty public constructor
    }

    public static HouseInfoFragment newInstance(String param1, String param2) {
        return new HouseInfoFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextObjectName = view.findViewById(R.id.tv_house_info_object_name);
        mTextObjectFlatCount = view.findViewById(R.id.tv_house_info_object_flat_count);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
        final String houseId = appDelegate.getCurrentRealmHouseId();

        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> {
            RealmHouse realmHouse = r.where(RealmHouse.class).equalTo("id", houseId).findFirst();
            mTextObjectName.setText(realmHouse.getName());
            mTextObjectFlatCount.setText(String.valueOf(realmHouse.getFlats().stream().count()));
        });

        view.findViewById(R.id.ib_house_info_home).setOnClickListener(v -> {

            NavHostFragment.findNavController(HouseInfoFragment.this).navigate(R.id.action_houseInfoFragment_to_mainMenuFragment);
        });

        view.findViewById(R.id.ib_house_info_back).setOnClickListener(v -> {

            NavHostFragment.findNavController(HouseInfoFragment.this).navigate(R.id.action_houseInfoFragment_to_reportListFragment);
        });

        view.findViewById(R.id.b_house_info_next).setOnClickListener(v -> {

            NavHostFragment.findNavController(HouseInfoFragment.this).navigate(R.id.action_houseInfoFragment_to_flatStartFragment);
        });


        mAdapter = new FlatListAdapter(this);//, appDelegate.getHouses()
        mRecyclerView = view.findViewById(R.id.rv_flat_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(10);

        flats = db.where(RealmHouse.class).equalTo("id", houseId).findFirstAsync();

        // The RealmChangeListener will be called when the results are asynchronously loaded, and available for use.
        RealmChangeListener<RealmHouse> realmChangeListener = realmHouse -> {
            mAdapter.setData(realmHouse.getFlats());
        };
        flats.addChangeListener(realmChangeListener);
    }

    public void onRecyclerViewItemClick(int position, RealmFlat realmFlat) {

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
        appDelegate.setCurrentRealmFlatId(realmFlat.getId());

        NavHostFragment.findNavController(HouseInfoFragment.this).navigate(R.id.action_houseInfoFragment_to_flatInfoFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_info, container, false);
    }
}