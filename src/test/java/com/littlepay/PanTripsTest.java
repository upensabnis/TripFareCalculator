package com.littlepay;

import com.littlepay.model.stops.StopId;
import com.littlepay.model.trips.*;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class PanTripsTest {
    private PanTrips panTrips;
    private Trip trip1, trip2, trip3, trip4;

    @BeforeTest
    public void init() {
        panTrips = new PanTrips();

        trip1 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(1))
                .tripDate(Date.from(Instant.now()))
                .tripType(TripType.ON)
                .stopId(new StopId())
                .companyId(new CompanyId())
                .busId(new BusId())
                .pan(new BigInteger("5500005555555559"))
                .build();

        trip2 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(2))
                .tripDate(Date.from(Instant.now()))
                .tripType(TripType.ON)
                .stopId(new StopId())
                .companyId(new CompanyId())
                .busId(new BusId())
                .pan(new BigInteger("5500005555555560"))
                .build();

        trip3 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(3))
                .tripDate(Date.from(Instant.now()))
                .tripType(TripType.ON)
                .stopId(new StopId())
                .companyId(new CompanyId())
                .busId(new BusId())
                .pan(new BigInteger("5500005555555560"))
                .build();

        trip4 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(3))
                .tripDate(Date.from(Instant.now()))
                .tripType(TripType.OFF)
                .stopId(new StopId())
                .companyId(new CompanyId())
                .busId(new BusId())
                .pan(new BigInteger("5500005555555565"))
                .build();

        panTrips.addTrip(trip1);
        panTrips.addTrip(trip2);
    }

    @Test
    public void testpantrips_addtrip() {
        panTrips.addTrip(trip3);

        HashMap<BigInteger, HashSet<Trip>> allTrips = panTrips.getAllTrips();
        Assert.assertEquals(allTrips.keySet().size(), 2);
    }

    @Test
    public void testpantrips_getalltripsforpan() {
        HashSet<Trip> trips = panTrips.getAllTripsForPan(trip2.getPan());
        Assert.assertEquals(trips.size(), 2);

        trips = panTrips.getAllTripsForPan(trip4.getPan());
        Assert.assertEquals(trips.size(), 0);
    }

    @Test
    public void testpantrips_istripassociatedwithpan() {
        boolean isAssociated = panTrips.isTripAssociatedWithPan(trip3, trip2.getPan());
        Assert.assertTrue(isAssociated);

        isAssociated = panTrips.isTripAssociatedWithPan(trip1, trip2.getPan());
        Assert.assertFalse(isAssociated);
    }
}
