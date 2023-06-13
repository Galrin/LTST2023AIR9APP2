package com.example.ltst2023air9.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmHouse extends RealmObject {
    @PrimaryKey
    @Index
    private String id; // = UUID.randomUUID().toString();

    private String name;
    private RealmList<RealmFlat> flats = new RealmList<>();

    private float startLatitude;
    private float startLongitude;

    private float endLatitude;
    private float endLongitude;

    private int startTime;
    private int endTime;

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

    public RealmList<RealmFlat> getFlats() {
        return flats;
    }

    public void setFlats(RealmList<RealmFlat> flats) {
        this.flats = flats;
    }
}
