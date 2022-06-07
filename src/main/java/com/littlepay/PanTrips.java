package com.littlepay;

import com.littlepay.model.trips.Trip;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Java class to maintain mapping between pan and corresponding trips
 * For example :
 *      key : 5500005555555559
 *      value : {tripObject1, tripObject2, tripObject3}
 */
public class PanTrips {
    HashMap<BigInteger, HashSet<Trip>> allTrips;

    public PanTrips() {
        allTrips = new HashMap<>();
    }

    public void addTrip(Trip trip) {
        HashSet<Trip> trips = allTrips.getOrDefault(trip.getPan(), new HashSet<>());
        trips.add(trip);
        allTrips.put(trip.getPan(), trips);
    }

    public HashSet<Trip> getAllTripsForPan(BigInteger pan) {
        return allTrips.getOrDefault(pan, new HashSet<>());
    }

    public boolean isTripAssociatedWithPan(Trip trip, BigInteger pan) {
        HashSet<Trip> trips = allTrips.getOrDefault(pan, new HashSet<>());
        if(!trips.isEmpty()) {
            Set<Trip> matchedTrips = trips.stream().filter(t ->
                    t.getTripId().equals(trip.getTripId())
            ).collect(Collectors.toSet());
            return matchedTrips.size() > 0;
        }
        return false;
    }

    public HashMap<BigInteger, HashSet<Trip>> getAllTrips() {
        return allTrips;
    }
}
