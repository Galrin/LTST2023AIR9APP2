package com.example.ltst2023air9;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ltst2023air9.databinding.ActivityMainBinding;
import com.example.ltst2023air9.model.RealmCheckpoint;
import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.model.RealmHouse;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //NavController navController = Navigation.findNavController(this, R.id.nav_graph);

        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> {
            RealmResults<RealmHouse> allRealmHouses = r.where(RealmHouse.class).findAll();
            for (RealmHouse rh : allRealmHouses) {
                Log.d("realm", "RealmHouse:" + rh.getId() + " " + rh.getName());
            }
            RealmResults<RealmFlat> allRealmFlats = r.where(RealmFlat.class).findAll();
            for (RealmFlat rf : allRealmFlats) {
                Log.d("realm", "RealmFlat:" + rf.getId() + " " + rf.getNumber());
            }
            RealmResults<RealmCheckpoint> allRealmCp = r.where(RealmCheckpoint.class).findAll();
            for (RealmCheckpoint rcp : allRealmCp) {
                Log.d("realm", "RealmCheckpoint:" + rcp.getId() + " " + rcp.getName());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

    }


}