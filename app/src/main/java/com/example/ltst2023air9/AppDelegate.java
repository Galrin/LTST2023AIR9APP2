package com.example.ltst2023air9;

import android.app.Application;

import com.example.ltst2023air9.model.Checkpoint;
import com.example.ltst2023air9.ui.fragments.tableview.TableViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class AppDelegate extends Application {

    String currentRealmHouseId = "";
    String currentRealmFlatId = "";
    String currentRealmCheckpointId = "";



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

        tableViewModel.initCellList();

        Realm.init(this);

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

    public void setCurrentRealmCheckpointId(String currentRealmCheckpointId) {
        this.currentRealmCheckpointId = currentRealmCheckpointId;
    }

    public String getCurrentRealmCheckpointId() {
        return currentRealmCheckpointId;
    }
}
