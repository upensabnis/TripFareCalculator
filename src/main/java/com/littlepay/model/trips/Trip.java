package com.littlepay.model.trips;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Trip {
    private BigInteger tripId;
    private LocalDateTime tripDateTime;
    private TapType tapType;
    private String stopId;
    private CompanyId companyId;
    private BusId busId;
    private BigInteger pan;

    private Trip(TripBuilder tripBuilder) {
        this.tripId = tripBuilder.tripId;
        this.tripDateTime = tripBuilder.tripDateTime;
        this.tapType = tripBuilder.tapType;
        this.stopId = tripBuilder.stopId;
        this.companyId = tripBuilder.companyId;
        this.busId = tripBuilder.busId;
        this.pan = tripBuilder.pan;
    }

    public BigInteger getTripId() {
        return tripId;
    }

    public LocalDateTime getTripDateTime() {
        return tripDateTime;
    }

    public TapType getTapType() {
        return tapType;
    }

    public String getStopId() {
        return stopId;
    }

    public CompanyId getCompanyId() {
        return companyId;
    }

    public BusId getBusId() {
        return busId;
    }

    public BigInteger getPan() {
        return pan;
    }

    public static class TripBuilder {
        private BigInteger tripId;
        private LocalDateTime tripDateTime;
        private TapType tapType;
        private String stopId;
        private CompanyId companyId;
        private BusId busId;
        private BigInteger pan;

        public TripBuilder tripId(BigInteger tripId) {
            this.tripId = tripId;
            return this;
        }

        public TripBuilder tripDate(LocalDateTime tripDateTime) {
            this.tripDateTime = tripDateTime;
            return this;
        }

        public TripBuilder tapType(TapType tapType) {
            this.tapType = tapType;
            return this;
        }

        public TripBuilder stopId(String stopId) {
            this.stopId = stopId;
            return this;
        }

        public TripBuilder companyId(CompanyId companyId) {
            this.companyId = companyId;
            return this;
        }

        public TripBuilder busId(BusId busId) {
            this.busId = busId;
            return this;
        }

        public TripBuilder pan(BigInteger pan) {
            this.pan = pan;
            return this;
        }

        public Trip build() {
            return new Trip(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Trip)) {
            return false;
        }

        Trip trip = (Trip) o;

        return this.tripId.equals(trip.getTripId()) &&
                this.getTripDateTime().equals(trip.getTripDateTime()) &&
                this.getTapType().equals(trip.getTapType()) &&
                this.getStopId().equals(trip.getStopId()) &&
                this.getCompanyId().equals(trip.getCompanyId()) &&
                this.getBusId().equals(trip.getBusId()) &&
                this.pan.equals(trip.getPan());
    }
}
