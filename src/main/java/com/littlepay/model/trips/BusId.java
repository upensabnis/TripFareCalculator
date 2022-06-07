package com.littlepay.model.trips;

public class BusId {
    private static int counter = 100;
    private String id;

    public BusId() {
        id = "bus" + (++counter);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof BusId)) {
            return false;
        }

        BusId busId = (BusId) o;

        return this.id.equals(busId.getId());
    }
}
