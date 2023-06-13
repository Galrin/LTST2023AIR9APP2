package com.example.ltst2023air9.ui.fragments.reportlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmHouse;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;

    private ReportListAdapter mAdapter;

    private RealmResults<RealmHouse> houses;
    private final RealmChangeListener<RealmResults<RealmHouse>> realmChangeListener = h -> {
        // Set the cities to the adapter only when async query is loaded.
        // It will also be called for any future writes made to the Realm.
        mAdapter.setData(h);
    };

    public ReportListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportListFragment newInstance(String param1, String param2) {
        ReportListFragment fragment = new ReportListFragment();
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
        return inflater.inflate(R.layout.fragment_report_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();
        mAdapter = new ReportListAdapter(this);//, appDelegate.getHouses()
        mRecyclerView = view.findViewById(R.id.rv_house_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(10);

        ImageButton imageButton = view.findViewById(R.id.ib_report_list_back);
        imageButton.setOnClickListener(v -> {

            NavHostFragment.findNavController(ReportListFragment.this)
                    .navigate(R.id.action_reportListFragment_to_mainMenuFragment);
        });

        Realm db = Realm.getDefaultInstance();
        // Obtain the cities in the Realm with asynchronous query.
        houses = db.where(RealmHouse.class).findAllAsync();

        // The RealmChangeListener will be called when the results are asynchronously loaded, and available for use.
        houses.addChangeListener(realmChangeListener);
    }


    public void onRecyclerViewItemClick(int position) {
        //Toast.makeText(getContext(), "aaa" + position, Toast.LENGTH_SHORT).show();

        NavHostFragment.findNavController(ReportListFragment.this)
                .navigate(R.id.action_reportListFragment_to_houseInfoFragment);
        //getParentFragmentManager().beginTransaction().replace(R.id.ActivityMain_fragment_container, DeviceFragment.newInstance()).commit();

    }
}