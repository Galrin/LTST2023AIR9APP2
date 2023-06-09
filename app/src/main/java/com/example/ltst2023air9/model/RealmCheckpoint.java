package com.example.ltst2023air9.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Index;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmCheckpoint extends RealmObject {

    @PrimaryKey
    @Index
    private String id; // = UUID.randomUUID().toString();

    private String name;

    private String videoPath;

    private float startLatitude;
    private float startLongitude;

    private float endLatitude;
    private float endLongitude;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // You can define inverse relationships.
    @LinkingObjects("checkpoints")
    public final RealmResults<RealmFlat> owners = null;
}
