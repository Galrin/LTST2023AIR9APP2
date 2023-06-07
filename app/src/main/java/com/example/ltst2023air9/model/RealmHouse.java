package com.example.ltst2023air9.model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmHouse extends RealmObject {
    @PrimaryKey
    String id = UUID.randomUUID().toString();

    String name;
    RealmList<RealmFlat> flats;

    float startLatitude;
    float startLongitude;

    float endLatitude;
    float endLongitude;

    int startTime;
    int endTime;
}
