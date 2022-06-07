package com.littlepay;

import com.littlepay.model.stops.Edge;
import com.littlepay.model.stops.Stop;
import com.littlepay.model.stops.StopId;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class FaresTest {

    private Fares fares;
    private Stop stop1, stop2, stop3, stop4;

    @BeforeTest
    public void init() {
        fares = new Fares();
        stop1 = new Stop(new StopId(), "stop1");
        stop2 = new Stop(new StopId(), "stop2");
        stop3 = new Stop(new StopId(), "stop3");
        stop4 = new Stop(new StopId(), "stop4");
        fares.addEdge(stop1, stop2, 3.25);
        fares.addEdge(stop2, stop3, 5.50);
        fares.addEdge(stop1, stop3, 7.30);
    }

    @Test
    public void stopfares_addedge() {
        fares.addEdge(stop4, stop3, 4.5);
        HashMap<Stop, HashSet<Edge>> stopFaresMap = fares.getEdgeAdjacencyMap();
        Assert.assertEquals(4, stopFaresMap.keySet().size());
    }

    @Test
    public void stopfares_isedgepresent() {
        boolean isEdgePresent = fares.isEdgePresent(stop1, stop4);
        Assert.assertFalse(isEdgePresent);

        isEdgePresent = fares.isEdgePresent(stop3, stop4);
        Assert.assertTrue(isEdgePresent);
    }

    @Test
    public void stopfares_getedgeifpresent() {
        Optional<Edge> optionalEdge = fares.getEdgeIfPresent(stop2, stop3);
        Assert.assertTrue(optionalEdge.isPresent());

        optionalEdge = fares.getEdgeIfPresent(stop2, stop4);
        Assert.assertFalse(optionalEdge.isPresent());
    }
}
