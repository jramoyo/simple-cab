package com.datarepublic.simplecab.model;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public final class MedallionsSummary {
    private final Map<String, Integer> trips = new TreeMap<>();

    public void addTrip(String medallion, int count) {
        trips.put(medallion, count);
    }

    public Map<String, Integer> getTrips() {
        return Collections.unmodifiableMap(trips);
    }
}
