package com.example.ltst2023air9;

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

import com.example.ltst2023air9.model.RealmHouse;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView mTextObjectName;
    private TextView mTextObjectFlatCount;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HouseInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HouseInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HouseInfoFragment newInstance(String param1, String param2) {
        HouseInfoFragment fragment = new HouseInfoFragment();
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
        return inflater.inflate(R.layout.fragment_house_info, container, false);
    }

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
    }
}