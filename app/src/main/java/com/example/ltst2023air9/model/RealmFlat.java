package com.example.ltst2023air9.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Index;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmFlat extends RealmObject {

    // You can define inverse relationships.
    @LinkingObjects("flats")
    public final RealmResults<RealmHouse> owners = null;
    @PrimaryKey
    @Index
    private String id; // = UUID.randomUUID().toString();
    private int floor;
    private int number;
    private RealmList<RealmCheckpoint> checkpoints = new RealmList<>();
    private int currentCheckpointNumber = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCurrentCheckpointNumber() {
        return currentCheckpointNumber;
    }

    public void setCurrentCheckpointNumber(int currentCheckpointNumber) {
        this.currentCheckpointNumber = currentCheckpointNumber;
    }

    public RealmList<RealmCheckpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(RealmList<RealmCheckpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

}
