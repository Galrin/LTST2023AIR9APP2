package com.example.ltst2023air9;

import android.app.Application;
import android.util.Log;

import com.example.ltst2023air9.model.Checkpoint;
import com.example.ltst2023air9.model.Flat;
import com.example.ltst2023air9.model.House;
import com.example.ltst2023air9.ui.fragments.tableview.TableViewModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class AppDelegate extends Application {
    List<House> houses = new ArrayList<>();

    House currentHouse;
    Flat currentFlat;

    List<Checkpoint> checkpoints = new ArrayList<>();
    {
        checkpoints.add(new Checkpoint("прихожая"));
        checkpoints.add(new Checkpoint("туалет"));
        checkpoints.add(new Checkpoint("кухня"));
        checkpoints.add(new Checkpoint("спальня"));

    }

    List<Flat> flats = new ArrayList<>();
//    {
//        flats.add(new Device("Колонка Алиса", R.drawable.alice));
//    }
    TableViewModel tableViewModel = new TableViewModel();

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        tableViewModel.initCellList();

//        tableViewModel.updateRow(5, "hello!");
//
//        File grousDir = getApplicationContext().getDir("groups", Context.MODE_PRIVATE); //Creating an internal dir;
//        if (!grousDir.exists())
//        {
//            grousDir.mkdirs();
//        }
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public List<Flat> getFlats() {
        return flats;
    }

    public Flat getCurrentFlat() {
        return currentFlat;
    }

    public void setCurrentFlat(Flat currentFlat) {
        this.currentFlat = currentFlat;
    }

    public List<House> getHouses() {
        return houses;
    }

    public void setCurrentHouse(House house) {
        this.currentHouse = house;
    }

    public House getCurrentHouse() {
        return currentHouse;
    }

    public TableViewModel getTableViewModel() {
        return tableViewModel;
    }
}
