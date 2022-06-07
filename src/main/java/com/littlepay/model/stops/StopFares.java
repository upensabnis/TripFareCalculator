package com.littlepay.model.stops;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StopFares {
    private HashMap<Stop, LinkedList<Edge>> edgeAdjacencyMap;

    public StopFares() {
        edgeAdjacencyMap = new HashMap<>();
    }

    public Optional<Edge> getEdgeIfPresent(Stop source, Stop destination) {
        if(isEdgePresent(source, destination)) {
            LinkedList<Edge> edges = edgeAdjacencyMap.get(source);
            List<Edge> matchedEdges = edges.stream().filter(e ->
                e.getDestination().getStopId() == destination.getStopId() &&
                e.getDestination().getStopName().equals(destination.getStopName())
            ).collect(Collectors.toList());
            return Optional.of(matchedEdges.get(0));
        }
        return Optional.empty();
    }
    
    public boolean isEdgePresent(Stop source, Stop destination) {
        LinkedList<Edge> edges = edgeAdjacencyMap.getOrDefault(source, new LinkedList<>());
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
        LinkedList<Edge> edges = edgeAdjacencyMap.getOrDefault(source, new LinkedList<>());
        edges.add(newEdge1);
        edgeAdjacencyMap.put(source, edges);

        // add edge from stop2 to stop1
        Edge newEdge2 = new Edge(destination, source, price);
        edges = edgeAdjacencyMap.getOrDefault(destination, new LinkedList<>());
        edges.add(newEdge2);
        edgeAdjacencyMap.put(destination, edges);
    }

    public HashMap<Stop, LinkedList<Edge>> getEdgeAdjacencyMap() {
        return edgeAdjacencyMap;
    }
}
