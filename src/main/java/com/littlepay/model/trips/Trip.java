package com.littlepay.model.trips;

import com.littlepay.model.stops.StopId;

import java.math.BigInteger;
import java.util.Date;

public class Trip {
    private BigInteger tripId;
    private Date tripDate;

    private Enum tripType;
    private StopId stopId;
    private CompanyId companyId;
    private BusId busId;
    private BigInteger pan;

    private Trip(TripBuilder tripBuilder) {
        this.tripId = tripBuilder.tripId;
        this.tripDate = tripBuilder.tripDate;
        this.tripType = tripBuilder.tripType;
        this.stopId = tripBuilder.stopId;
        this.companyId = tripBuilder.companyId;
        this.busId = tripBuilder.busId;
        this.pan = tripBuilder.pan;
    }

    public BigInteger getTripId() {
        return tripId;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public Enum getTripType() {
        return tripType;
    }

    public StopId getStopId() {
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
        private Date tripDate;
        private Enum tripType;
        private StopId stopId;
        private CompanyId companyId;
        private BusId busId;
        private BigInteger pan;

        public TripBuilder tripId(BigInteger tripId) {
            this.tripId = tripId;
            return this;
        }

        public TripBuilder tripDate(Date tripDate) {
            this.tripDate = tripDate;
            return this;
        }

        public TripBuilder tripType(Enum tripType) {
            this.tripType = tripType;
            return this;
        }

        public TripBuilder stopId(StopId stopId) {
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

        return this.pan.equals(trip.getPan());
    }
}
