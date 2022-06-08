package com.littlepay;

import com.littlepay.model.stops.Route;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class FaresTest {

    private Fares fares;
    private String stopId1, stopId2, stopId3, stopId4;

    @BeforeTest
    public void init() {
        fares = new Fares();
        stopId1 = "stop1";
        stopId2 = "stop2";
        stopId3 = "stop3";
        stopId4 = "stop4";
        fares.addRoute(stopId1, stopId2, 3.25);
        fares.addRoute(stopId2, stopId3, 5.50);
        fares.addRoute(stopId1, stopId3, 7.30);
    }

    @Test
    public void stopfares_addroute() {
        fares.addRoute(stopId4, stopId3, 4.5);
        HashMap<String, HashSet<Route>> stopFaresMap = fares.getRouteAdjacencyMap();
        Assert.assertEquals(4, stopFaresMap.keySet().size());
        Assert.assertEquals(2, stopFaresMap.get(stopId2).size());
    }

    @Test
    public void stopfares_isroutepresent() {
        boolean isEdgePresent = fares.isRoutePresent(stopId1, stopId4);
        Assert.assertFalse(isEdgePresent);

        isEdgePresent = fares.isRoutePresent(stopId3, stopId4);
        Assert.assertTrue(isEdgePresent);
    }

    @Test
    public void stopfares_getrouteifpresent() {
        Optional<Route> optionalEdge = fares.getRouteIfPresent(stopId2, stopId3);
        Assert.assertTrue(optionalEdge.isPresent());

        optionalEdge = fares.getRouteIfPresent(stopId2, stopId4);
        Assert.assertFalse(optionalEdge.isPresent());
    }

    @Test
    public void stopfares_getpricebetweenstops() {
        double fare = fares.getPriceBetweenStops(stopId1, stopId3);
        Assert.assertEquals(fare, 7.30);
    }

    @Test
    public void stopfares_getmaximumfarefromstop() {
        double fare = fares.getMaximumFareFromStop(stopId1);
        Assert.assertEquals(fare, 7.30);

        String stopId10 = "stop10";
        fare = fares.getMaximumFareFromStop(stopId10);
        Assert.assertEquals(fare, 0);
    }

    @Test
    public void stopfares_getallroutesfromstops() {
        HashSet<Route> routes = fares.getAllRoutesFromStop(stopId2);
        Assert.assertEquals(routes.size(), 2);
    }
}
