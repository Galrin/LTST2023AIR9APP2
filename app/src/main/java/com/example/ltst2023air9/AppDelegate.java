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
    List<House> houses = new ArrayList<>();

    House currentHouse;
    Flat currentFlat;
    String currentRealmHouseId = "";
    String currentRealmFlatId = "";

    List<Checkpoint> checkpoints = new ArrayList<>();
    List<Flat> flats = new ArrayList<>();
    //    {
//        flats.add(new Device("Колонка Алиса", R.drawable.alice));
//    }
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
//        Realm.setDefaultConfiguration(
//                new RealmConfiguration.Builder()
////                        .allowWritesOnUiThread(true)
////                        .allowQueriesOnUiThread(true)
//                        .deleteRealmIfMigrationNeeded()
//                        .build());

        tableViewModel.initCellList();

        Realm db = Realm.getDefaultInstance();
        db.executeTransactionAsync(r -> r.delete(RealmCheckpoint.class));

        //if(db.where(RealmCheckpoint.class).findFirst() == null) {
//        db.executeTransaction(new Realm.Transaction() {
//                                  @Override
//                                  public void execute(Realm realm) {
//                                      for (Checkpoint cp : getCheckpoints()) {
//                                          RealmCheckpoint checkpoint = realm.createObject(RealmCheckpoint.class, UUID.randomUUID().toString());
//                                          checkpoint.setName(String.valueOf(cp.getName()));
//                                      }
//                                  }
//                              }
//        );

        // }

//        db.beginTransaction();
//        //for (Checkpoint cp : getCheckpoints()) {
//            RealmCheckpoint checkpoint = db.createObject(RealmCheckpoint.class, UUID.randomUUID().toString());
//            checkpoint.setName("кухня");
//        //}
//        db.commitTransaction();
//

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

    public House getCurrentHouse() {
        return currentHouse;
    }

    public void setCurrentHouse(House house) {
        this.currentHouse = house;
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
