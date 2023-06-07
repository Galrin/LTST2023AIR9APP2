package com.example.ltst2023air9.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public class House implements Serializable {
    final String uuid = UUID.randomUUID().toString();
    Set<Flat> flats;

    String name;

    float startLatitude;
    float startLongtitude;

    int timestamp;

    public String getUuid() {
        return uuid;
    }

    public Set<Flat> getFlats() {
        return flats;
    }

    public void setFlats(Set<Flat> flats) {
        this.flats = flats;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
