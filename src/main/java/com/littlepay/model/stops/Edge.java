package com.littlepay.model.stops;

public class Edge {
    Stop source;
    Stop destination;
    double ticketPrice;

    public Edge(Stop source, Stop destination, double ticketPrice) {
        this.source = source;
        this.destination = destination;
        this.ticketPrice = ticketPrice;
    }

    public Stop getSource() {
        return source;
    }

    public Stop getDestination() {
        return destination;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
}
