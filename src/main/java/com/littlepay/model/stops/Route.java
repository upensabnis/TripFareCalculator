package com.littlepay.model.stops;

public class Route {
    String source;
    String destination;
    double fare;

    public Route(String source, String destination, double fare) {
        this.source = source;
        this.destination = destination;
        this.fare = fare;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getFare() {
        return fare;
    }
}
