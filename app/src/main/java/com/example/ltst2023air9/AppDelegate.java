package com.example.ltst2023air9;

import android.app.Application;

import com.example.ltst2023air9.model.Checkpoint;
import com.example.ltst2023air9.model.Flat;
import com.example.ltst2023air9.model.House;

import java.util.ArrayList;
import java.util.List;

public class AppDelegate extends Application {
    List<House> houses = new ArrayList<>();;

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

    @Override
    public void onCreate() {
        super.onCreate();
//
//        File grousDir = getApplicationContext().getDir("groups", Context.MODE_PRIVATE); //Creating an internal dir;
//        if (!grousDir.exists())
//        {
//            grousDir.mkdirs();
//        }
    }

//    public void revalidateGroups() {
//        checkpoints.clear();
//        for(String fileName : getApplicationContext().fileList()) {
//            Log.d("FILE: ", fileName);
//            if(fileName.startsWith(".")) {
//                checkpoints.add(new Group(fileName));
//            }
//        }
//    }
    public List<Checkpoint> getCheckpoints() {
//        revalidateGroups();
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

}
