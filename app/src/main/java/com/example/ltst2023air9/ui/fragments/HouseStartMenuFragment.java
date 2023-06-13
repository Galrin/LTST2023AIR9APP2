package com.example.ltst2023air9.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.ltst2023air9.model.RealmHouse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HouseStartMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HouseStartMenuFragment extends Fragment {

    public HouseStartMenuFragment() {
        // Required empty public constructor
    }

    public static HouseStartMenuFragment newInstance(String param1, String param2) {
        return new HouseStartMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_house_start_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button save = view.findViewById(R.id.b_house_save);
        TextInputEditText text = view.findViewById(R.id.tiet_house_name);

        save.setOnClickListener(v -> {
            final String defaultName = TextUtils.isEmpty(text.getText().toString())
                    ? "ЖК Черёмушки"
                    : text.getText().toString();
            AppDelegate appDelegate = (AppDelegate) getActivity().getApplicationContext();

            Realm db = Realm.getDefaultInstance();

            db.executeTransactionAsync(r -> {
                RealmHouse realmHouse = r.createObject(RealmHouse.class, UUID.randomUUID().toString());
                realmHouse.setName(defaultName);

                appDelegate.setCurrentRealmHouseId(realmHouse.getId());
            });

            NavHostFragment.findNavController(HouseStartMenuFragment.this)
                    .navigate(R.id.action_houseStartMenuFragment_to_flatStartFragment);
        });
    }
}