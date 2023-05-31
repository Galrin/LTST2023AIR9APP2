package com.example.ltst2023air9.model;

import java.util.Set;
import java.util.UUID;

public class House {
    final String uuid = UUID.randomUUID().toString();
    Set<Flat> flats;

    public String getUuid() {
        return uuid;
    }

    public Set<Flat> getFlats() {
        return flats;
    }

    public void setFlats(Set<Flat> flats) {
        this.flats = flats;
    }
}
