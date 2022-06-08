package com.littlepay.model.trips;

public class BusId {
    private String id;

    public BusId(String busId) {
        id = busId;
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
