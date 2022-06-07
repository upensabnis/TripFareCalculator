package com.littlepay;

import com.littlepay.model.stops.Edge;
import com.littlepay.model.stops.Stop;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Java class to maintain fares between stops
 * It treats stops and fares as graph with weighted edges
 * Maintains map of stop to set of adjacent edges
 */
public class Fares {
    private HashMap<Stop, HashSet<Edge>> edgeAdjacencyMap;

    public Fares() {
        edgeAdjacencyMap = new HashMap<>();
    }

    public Optional<Edge> getEdgeIfPresent(Stop source, Stop destination) {
        if(isEdgePresent(source, destination)) {
            HashSet<Edge> edges = edgeAdjacencyMap.get(source);
            List<Edge> matchedEdges = edges.stream().filter(e ->
                e.getDestination().getStopId() == destination.getStopId() &&
                e.getDestination().getStopName().equals(destination.getStopName())
            ).collect(Collectors.toList());
            return Optional.of(matchedEdges.get(0));
        }
        return Optional.empty();
    }
    
    public boolean isEdgePresent(Stop source, Stop destination) {
        HashSet<Edge> edges = edgeAdjacencyMap.getOrDefault(source, new HashSet<>());
        if(!edges.isEmpty()) {
            List<Edge> matchedEdges = edges.stream().filter(e ->
                    e.getDestination().getStopId() == destination.getStopId() &&
                            e.getDestination().getStopName().equals(destination.getStopName())
            ).collect(Collectors.toList());
            return matchedEdges.size() > 0;
        }
        return false;
    }

    public void addEdge(Stop source, Stop destination, double price) {
        // add edge from stop1 to stop2
        Edge newEdge1 = new Edge(source, destination, price);
        HashSet<Edge> edges = edgeAdjacencyMap.getOrDefault(source, new HashSet<>());
        edges.add(newEdge1);
        edgeAdjacencyMap.put(source, edges);

        // add edge from stop2 to stop1
        Edge newEdge2 = new Edge(destination, source, price);
        edges = edgeAdjacencyMap.getOrDefault(destination, new HashSet<>());
        edges.add(newEdge2);
        edgeAdjacencyMap.put(destination, edges);
    }

    public HashMap<Stop, HashSet<Edge>> getEdgeAdjacencyMap() {
        return edgeAdjacencyMap;
    }
}
