package com.example.ltst2023air9.ui.fragments;

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

import com.example.ltst2023air9.AppDelegate;
import com.example.ltst2023air9.R;
import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.model.RealmHouse;

import java.util.UUID;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link flatStartMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class flatStartMenuFragment extends Fragment {
public static final int FLAT_MAX = 720;
public static final int FLOOR_MAX = 35;


    public flatStartMenuFragment() {
        // Required empty public constructor
    }

    public static flatStartMenuFragment newInstance(String param1, String param2) {
        return new flatStartMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                if (realmHouse != null) {
                    realmHouse.getFlats().add(realmFlat);
                }

                appDelegate.setCurrentRealmFlatId(realmFlat.getId());
            });

            NavHostFragment.findNavController(flatStartMenuFragment.this)
                    .navigate(R.id.action_flatStartMenuFragment_to_flatStepFragment);
        });
    }
}