package com.example.ltst2023air9.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmFlat  extends RealmObject {

    @PrimaryKey
    String id = UUID.randomUUID().toString();

    int currentCheckpoint = 0;

    int floor;

    int number;

}
