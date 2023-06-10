package com.example.ltst2023air9;

import android.app.Application;

import com.example.ltst2023air9.model.Checkpoint;
import com.example.ltst2023air9.model.Flat;
import com.example.ltst2023air9.model.House;
import com.example.ltst2023air9.model.RealmCheckpoint;
import com.example.ltst2023air9.model.RealmFlat;
import com.example.ltst2023air9.model.RealmHouse;
import com.example.ltst2023air9.ui.fragments.tableview.TableViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppDelegate extends Application {

    String currentRealmHouseId = "";
    String currentRealmFlatId = "";

    List<Checkpoint> checkpoints = new ArrayList<>();

    TableViewModel tableViewModel = new TableViewModel();

    {
        checkpoints.add(new Checkpoint("прихожая"));
        checkpoints.add(new Checkpoint("туалет"));
        checkpoints.add(new Checkpoint("кухня"));

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);


        tableViewModel.initCellList();

        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> r.delete(RealmCheckpoint.class));


    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public TableViewModel getTableViewModel() {
        return tableViewModel;
    }

    public String getCurrentRealmFlatId() {
        return currentRealmFlatId;
    }

    public void setCurrentRealmFlatId(String currentRealmFlatId) {
        this.currentRealmFlatId = currentRealmFlatId;
    }

    public String getCurrentRealmHouseId() {
        return currentRealmHouseId;
    }

    public void setCurrentRealmHouseId(String currentRealmHouseId) {
        this.currentRealmHouseId = currentRealmHouseId;
    }

}
