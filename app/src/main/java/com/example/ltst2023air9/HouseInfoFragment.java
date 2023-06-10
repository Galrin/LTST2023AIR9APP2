package com.example.ltst2023air9;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.model.RealmHouse;
import com.example.ltst2023air9.ui.fragments.houseInfo.reportlist.FlatListAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;


public class HouseInfoFragment extends Fragment {
    private TextView mTextObjectName;
    private TextView mTextObjectFlatCount;



    private RecyclerView mRecyclerView;

    private FlatListAdapter mAdapter;

    private RealmHouse flats;

//    private final RealmChangeListener<RealmResults<RealmHouse>> realmChangeListener = h -> {
//        // Set the cities to the adapter only when async query is loaded.
//        // It will also be called for any future writes made to the Realm.
//        h.get
//        mAdapter.setData(h);
//    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextObjectName = view.findViewById(R.id.tv_house_info_object_name);
        mTextObjectFlatCount = view.findViewById(R.id.tv_house_info_object_flat_count);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
        final String houseId = appDelegate.getCurrentRealmHouseId();
        Log.i("HouseInfo", "house id: " + appDelegate.getCurrentRealmHouseId());

        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> {
            RealmHouse realmHouse = r.where(RealmHouse.class).equalTo("id", houseId).findFirst();
            mTextObjectName.setText(realmHouse.getName());
            mTextObjectFlatCount.setText(String.valueOf(realmHouse.getFlats().stream().count()));
        });

        view.findViewById(R.id.ib_house_info_home).setOnClickListener(v -> {

            NavHostFragment.findNavController(HouseInfoFragment.this)
                    .navigate(R.id.action_houseInfoFragment_to_mainMenuFragment);
        });

        view.findViewById(R.id.ib_house_info_back).setOnClickListener(v -> {

            NavHostFragment.findNavController(HouseInfoFragment.this)
                    .navigate(R.id.action_houseInfoFragment_to_reportListFragment);
        });

        view.findViewById(R.id.b_house_info_next).setOnClickListener(v -> {

            NavHostFragment.findNavController(HouseInfoFragment.this)
                    .navigate(R.id.action_houseInfoFragment_to_flatStartFragment);
        });



        //AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
        mAdapter = new FlatListAdapter(this);//, appDelegate.getHouses()
        mRecyclerView = view.findViewById(R.id.rv_flat_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(10);

//        ImageButton imageButton = view.findViewById(R.id.ib_report_list_back);
//        imageButton.setOnClickListener(v -> {
//
//            NavHostFragment.findNavController(HouseInfoFragment.this)
//                    .navigate(R.id.action_reportListFragment_to_mainMenuFragment);
//        });

        //Realm db = Realm.getDefaultInstance();
        // Obtain the cities in the Realm with asynchronous query.

        ///TODO: добавить условие выборки квартир
        flats = db.where(RealmHouse.class).equalTo("id", houseId).findFirstAsync();

        // The RealmChangeListener will be called when the results are asynchronously loaded, and available for use.
        RealmChangeListener<RealmHouse> realmChangeListener = realmHouse -> {
            mAdapter.setData(realmHouse.getFlats());
        };
        flats.addChangeListener(realmChangeListener);
    }


    public void onRecyclerViewItemClick(int position) {
        Log.d("flatList", "position " + position);

        NavHostFragment.findNavController(HouseInfoFragment.this)
                .navigate(R.id.action_reportListFragment_to_houseInfoFragment);
    }


        public HouseInfoFragment() {
        // Required empty public constructor
    }

    public static HouseInfoFragment newInstance(String param1, String param2) {
        return new HouseInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_info, container, false);
    }
}