package com.littlepay;

import com.littlepay.model.stops.Route;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Java class to maintain fares between stops
 * It treats stops and fares as graph with weighted route
 * Maintains map of stopid to set of adjacent stopids
 */
public class Fares {
    private HashMap<String, HashSet<Route>> routeAdjacencyMap;
    public Fares() {
        routeAdjacencyMap = new HashMap<>();
    }

    public Optional<Route> getRouteIfPresent(String source, String destination) {
        if(isRoutePresent(source, destination)) {
            HashSet<Route> routes = routeAdjacencyMap.get(source);
            List<Route> matchedRoutes = routes.stream().filter(e ->
                e.getDestination().equals(destination)
            ).collect(Collectors.toList());
            return Optional.of(matchedRoutes.get(0));
        }
        return Optional.empty();
    }
    
    public boolean isRoutePresent(String source, String destination) {
        HashSet<Route> routes = routeAdjacencyMap.getOrDefault(source, new HashSet<>());
        if(!routes.isEmpty()) {
            List<Route> matchedRoutes = routes.stream().filter(e ->
                    e.getDestination().equals(destination)
            ).collect(Collectors.toList());
            return matchedRoutes.size() > 0;
        }
        return false;
    }

    public void addRoute(String source, String destination, double price) {
        // add route from source to destination
        Route newRoute1 = new Route(source, destination, price);
        HashSet<Route> routes = routeAdjacencyMap.getOrDefault(source, new HashSet<>());
        routes.add(newRoute1);
        routeAdjacencyMap.put(source, routes);

        // add route from destination to source
        Route newRoute2 = new Route(destination, source, price);
        routes = routeAdjacencyMap.getOrDefault(destination, new HashSet<>());
        routes.add(newRoute2);
        routeAdjacencyMap.put(destination, routes);
    }

    public double getPriceBetweenStops(String source, String destination) {
        Optional<Route> route = getRouteIfPresent(source, destination);
        return route.isPresent() ? route.get().getFare() : 0;
    }

    public HashSet<Route> getAllRoutesFromStop(String stopId) {
        return routeAdjacencyMap.getOrDefault(stopId, new HashSet<>());
    }

    public double getMaximumFareFromStop(String stopId) {
        HashSet<Route> routes = getAllRoutesFromStop(stopId);
        Optional<Route> route = routes
                .stream()
                .max(Comparator.comparing(Route::getFare));
        if(route.isPresent()) {
            return route.get().getFare();
        }
        return 0;
    }

    public double getMinimumFareFromStop(String stopId) {
        HashSet<Route> routes = getAllRoutesFromStop(stopId);
        Optional<Route> route = routes
                .stream()
                .min(Comparator.comparing(Route::getFare));
        if(route.isPresent()) {
            return route.get().getFare();
        }
        return 0;
    }

    public HashMap<String, HashSet<Route>> getRouteAdjacencyMap() {
        return routeAdjacencyMap;
    }
}
