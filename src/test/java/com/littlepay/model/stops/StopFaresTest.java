package com.littlepay.model.stops;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class StopFaresTest {

    private StopFares stopFares;
    private Stop stop1, stop2, stop3, stop4;

    @BeforeTest
    public void init() {
        stopFares = new StopFares();
        stop1 = new Stop(1, "stop1");
        stop2 = new Stop(2, "stop2");
        stop3 = new Stop(3, "stop3");
        stop4 = new Stop(4, "stop4");
        stopFares.addEdge(stop1, stop2, 3.25);
        stopFares.addEdge(stop2, stop3, 5.50);
        stopFares.addEdge(stop1, stop3, 7.30);
    }

    @Test
    public void stopfares_addedge() {
        stopFares.addEdge(stop4, stop3, 4.5);
        HashMap<Stop, LinkedList<Edge>> stopFaresMap = stopFares.getEdgeAdjacencyMap();
        Assert.assertEquals(4, stopFaresMap.keySet().size());
    }

    @Test
    public void stopfares_isedgepresent() {
        boolean isEdgePresent = stopFares.isEdgePresent(stop1, stop4);
        Assert.assertFalse(isEdgePresent);

        isEdgePresent = stopFares.isEdgePresent(stop3, stop4);
        Assert.assertTrue(isEdgePresent);
    }

    @Test
    public void stopfares_getedgeifpresent() {
        Optional<Edge> optionalEdge = stopFares.getEdgeIfPresent(stop2, stop3);
        Assert.assertTrue(optionalEdge.isPresent());

        optionalEdge = stopFares.getEdgeIfPresent(stop2, stop4);
        Assert.assertFalse(optionalEdge.isPresent());
    }
}
