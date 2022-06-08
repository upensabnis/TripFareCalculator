package com.littlepay;

import com.littlepay.model.trips.BusId;
import com.littlepay.model.trips.CompanyId;
import com.littlepay.model.trips.TapType;
import com.littlepay.model.trips.Trip;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;

public class PanTripsTest {
    private PanTrips panTrips;
    private Trip trip1, trip2, trip3, trip4;

    @BeforeTest
    public void init() {
        panTrips = new PanTrips();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        trip1 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(1))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop1")
                .companyId(new CompanyId("companyId1"))
                .busId(new BusId("B37"))
                .pan(new BigInteger("5500005555555559"))
                .build();

        trip2 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(2))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop2")
                .companyId(new CompanyId("companyId2"))
                .busId(new BusId("B40"))
                .pan(new BigInteger("5500005555555560"))
                .build();

        trip3 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(3))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:15", dateTimeFormatter))
                .tapType(TapType.OFF)
                .stopId("stop3")
                .companyId(new CompanyId("companyId2"))
                .busId(new BusId("B40"))
                .pan(new BigInteger("5500005555555560"))
                .build();

        trip4 = new Trip.TripBuilder()
                .tripId(BigInteger.valueOf(3))
                .tripDate(LocalDateTime.parse("22-01-2018 13:05:00", dateTimeFormatter))
                .tapType(TapType.ON)
                .stopId("stop4")
                .companyId(new CompanyId("companyId4"))
                .busId(new BusId("B50"))
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
