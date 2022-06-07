package com.littlepay.model.stops;

public class Stop {
    private StopId stopId;
    private String stopName;

    public Stop(StopId stopId, String stopName) {
        this.stopId = stopId;
        this.stopName = stopName;
    }

    public StopId getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    @Override
    public String toString() {
        return this.stopId + ":" + this.stopName;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Stop)) {
            return false;
        }

        Stop stop = (Stop) o;

        return this.stopId.equals(stop.getStopId()) && this.stopName.equals(stop.getStopName());
    }
}
