package com.example.ltst2023air9.model;

import java.util.UUID;

public class Checkpoint {
    String name;
    public Checkpoint(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String videoPath;

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    final String uuid = UUID.randomUUID().toString();

    public String getUuid() {
        return uuid;
    }
}
