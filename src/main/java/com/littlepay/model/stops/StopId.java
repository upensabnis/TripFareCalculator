package com.littlepay.model.stops;

public class StopId {
    private static int counter = 0;
    private String id;

    public StopId() {
        id = "stop" + (++counter);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof StopId)) {
            return false;
        }

        StopId stopId = (StopId) o;

        return this.id.equals(stopId.getId());
    }
}
