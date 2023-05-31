package com.example.ltst2023air9.model;

import java.util.UUID;

public class Flat {
    final String uuid = UUID.randomUUID().toString();

    int currentCheckpoint = 0;

    int floor;

    public String getUuid() {
        return uuid;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getCurrentCheckpoint() {
        return currentCheckpoint;
    }

    public void setCurrentCheckpoint(int currentCheckpoint) {
        this.currentCheckpoint = currentCheckpoint;
    }
}
