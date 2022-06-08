package com.littlepay.model.trips;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OutputTrips {
    private LocalDateTime started;
    private LocalDateTime finished;
    private long durationSecs;
    private String fromStopId;
    private String toStopId;
    private double chargeAmount;
    private String companyId;
    private String busId;
    private BigInteger pan;
    private OutputStatus outputStatus;

    private OutputTrips(OutputTripsBuilder outputTripsBuilder) {
        this.started = outputTripsBuilder.started;
        this.finished = outputTripsBuilder.finished;
        this.durationSecs = outputTripsBuilder.durationSecs;
        this.fromStopId = outputTripsBuilder.fromStopId;
        this.toStopId = outputTripsBuilder.toStopId;
        this.chargeAmount = outputTripsBuilder.chargeAmount;
        this.companyId = outputTripsBuilder.companyId;
        this.busId = outputTripsBuilder.busId;
        this.pan = outputTripsBuilder.pan;
        this.outputStatus = outputTripsBuilder.outputStatus;
    }

    public String[] getStringArrayOfValues() {
        ArrayList<String> output = new ArrayList<>();
        output.add(started != null ? started.toString() : " -");
        output.add(finished != null ? " " + finished : " -");
        output.add(durationSecs > 0 ? " " + durationSecs : " -");
        output.add(fromStopId != null ? " " + fromStopId : " -");
        output.add(toStopId != null ? " " + toStopId : " -");
        output.add(chargeAmount > 0 ? " " + chargeAmount : " -");
        output.add(companyId != null ? " " + companyId : " -");
        output.add(busId != null ? " " + busId : " -");
        output.add(pan != null ? " " + pan : " -");
        output.add(outputStatus != null ? " " + outputStatus : " -");

        return output.toArray(new String[0]);
    }

    public String[] getOutputHeaderRow() {
        String[] headers = new String[10];
        headers[0] = "Started";
        headers[1] = " Finished";
        headers[2] = " DurationSecs";
        headers[3] = " FromStopId";
        headers[4] = " ToStopId";
        headers[5] = " ChargeAmount";
        headers[6] = " CompanyId";
        headers[7] = " BusID";
        headers[8] = " PAN";
        headers[9] = " Status";

        return headers;
    }


    public static class OutputTripsBuilder {
        private LocalDateTime started;
        private LocalDateTime finished;
        private long durationSecs;
        private String fromStopId;
        private String toStopId;
        private double chargeAmount;
        private String companyId;
        private String busId;
        private BigInteger pan;
        private OutputStatus outputStatus;

        public OutputTripsBuilder started(LocalDateTime started) {
            this.started = started;
            return this;
        }

        public OutputTripsBuilder finished(LocalDateTime finished) {
            this.finished = finished;
            return this;
        }

        public OutputTripsBuilder durationSecs(long durationSecs) {
            this.durationSecs = durationSecs;
            return this;
        }

        public OutputTripsBuilder fromStopId(String fromStopId) {
            this.fromStopId = fromStopId;
            return this;
        }

        public OutputTripsBuilder toStopId(String toStopId) {
            this.toStopId = toStopId;
            return this;
        }

        public OutputTripsBuilder chargeAmount(double chargeAmount) {
            this.chargeAmount = chargeAmount;
            return this;
        }

        public OutputTripsBuilder companyId(String companyId) {
            this.companyId = companyId;
            return this;
        }

        public OutputTripsBuilder busId(String busId) {
            this.busId = busId;
            return this;
        }

        public OutputTripsBuilder pan(BigInteger pan) {
            this.pan = pan;
            return this;
        }

        public OutputTripsBuilder outputStatus(OutputStatus outputStatus) {
            this.outputStatus = outputStatus;
            return this;
        }

        public OutputTrips build() {
            return new OutputTrips(this);
        }
    }
}
