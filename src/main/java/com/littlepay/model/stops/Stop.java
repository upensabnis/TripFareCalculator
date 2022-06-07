package com.littlepay.model.stops;

public class Stop {
    private int stopId;
    private String stopName;

    public Stop(int stopId, String stopName) {
        this.stopId = stopId;
        this.stopName = stopName;
    }

    public int getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    @Override
    public String toString() {
        return this.stopId + ":" + this.stopName;
    }
}
