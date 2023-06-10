package com.example.ltst2023air9.model;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.RealmList;
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

    private int pointId;
    private String name;

    private String videoPath;

    private float startLatitude;
    private float startLongitude;

    private float endLatitude;
    private float endLongitude;

    RealmList<String> metrics = new RealmList<>();


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

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public RealmList<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(RealmList<String> metrics) {
        this.metrics = metrics;
    }

    // You can define inverse relationships.
    @LinkingObjects("checkpoints")
    public final RealmResults<RealmFlat> owners = null;
}
