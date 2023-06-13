package com.example.ltst2023air9.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Index;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmCheckpoint extends RealmObject {

    // You can define inverse relationships.
    @LinkingObjects("checkpoints")
    public final RealmResults<RealmFlat> owners = null;
    RealmList<String> metrics = new RealmList<>();
    @PrimaryKey
    @Index
    private String id; // = UUID.randomUUID().toString();
    private int globalCheckpointId;
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

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public RealmList<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(RealmList<String> metrics) {
        this.metrics = metrics;
    }

    public int getGlobalCheckpointId() {
        return globalCheckpointId;
    }

    public void setGlobalCheckpointId(int globalCheckpointId) {
        this.globalCheckpointId = globalCheckpointId;
    }
}
