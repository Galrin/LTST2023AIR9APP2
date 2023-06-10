package com.example.ltst2023air9;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ltst2023air9.model.Flat;
import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.model.RealmHouse;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link flatStartMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class flatStartMenuFragment extends Fragment {
public static final int FLAT_MAX = 720;
public static final int FLOOR_MAX = 35;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public flatStartMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment flatStartMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static flatStartMenuFragment newInstance(String param1, String param2) {
        flatStartMenuFragment fragment = new flatStartMenuFragment();
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
        return inflater.inflate(R.layout.fragment_flat_start_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button save = view.findViewById(R.id.b_flat_save);
        //EditText text = view.findViewById(R.id.et_house_name);
        com.shawnlin.numberpicker.NumberPicker floor = view.findViewById(R.id.np_flat_start_foor);
        floor.setMinValue(1);
        floor.setValue(1);
        floor.setMaxValue(FLOOR_MAX);

        com.shawnlin.numberpicker.NumberPicker number = view.findViewById(R.id.np_flat_start_number);
        number.setMinValue(1);
        number.setValue(1);
        number.setMaxValue(FLAT_MAX);

        AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();

        save.setOnClickListener(v -> {

            Realm db = Realm.getDefaultInstance();

            final String houseId = appDelegate.getCurrentRealmHouseId();
            db.executeTransactionAsync(r -> {
                RealmFlat realmFlat = r.createObject(RealmFlat.class, UUID.randomUUID().toString());
                realmFlat.setFloor(floor.getValue());
                realmFlat.setNumber(number.getValue());

                RealmHouse realmHouse = r.where(RealmHouse.class).equalTo("id", houseId).findFirst();

                Log.i("flat start menu", "=====" );
                Log.i("flat start menu", "ap house id "  + houseId);

                if (realmHouse != null) {
                    Log.i("flat start menu", "" + realmHouse.getName());

                    realmHouse.getFlats().add(realmFlat);
                }
                Log.i("flat start menu", "//=====" );

                appDelegate.setCurrentRealmFlatId(realmFlat.getId());
            });

            NavHostFragment.findNavController(flatStartMenuFragment.this)
                    .navigate(R.id.action_flatStartMenuFragment_to_flatStepFragment);
        });
    }
}