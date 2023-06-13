package com.example.ltst2023air9.model;

import java.util.UUID;

public class Checkpoint {
    final String uuid = UUID.randomUUID().toString();
    String name;
    private String videoPath;

    public Checkpoint(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getUuid() {
        return uuid;
    }
}
