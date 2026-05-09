/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import datastructures.CustomGraph;
import models.Route;

/**
 *
 * @author Yim Zi Hao
 */
public class RouteManager {

    private CustomGraph graph;
    private DijkstraSolver solver;

    public RouteManager(DijkstraSolver solver) {
        this.solver = solver;
    }

    public void setGraph(CustomGraph graph) {
        this.graph = graph;
    }

    /**
     * Get the shortest route from GATE to a specific slot or vertex.
     *
     * @param destination
     * @return
     */
    public Route getRoute(String destination) {
        if (graph == null) {
            System.out.println("No graph loaded.");
            return null;
        }
        if (!graph.hasVertex(destination)) {
            System.out.println("Destination not found in graph: " + destination);
            return null;
        }
        return solver.solve(graph, "Gate", destination);
    }

    /**
     * Prints turn-by-turn directions for a given route. Output: GATE → INT_A →
     * INT_C → S03 Total distance: 18 m
     *
     * @param route
     */
    public void printDirections(Route route) {
        if (route == null) {
            System.out.println("No route available");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < route.path.size(); i++) {
            sb.append(route.path.get(i));
            if (i < route.path.size() - 1) {
                sb.append(" -> ");
            }
        }
        sb.append("   Total distance: ").append(route.totalDistance).append(" m");
        System.out.println(sb.toString());
    }

    /**
     * Convenience method — get and print in one call.
     *
     * @param destination
     */
    public void navigateTo(String destination) {
        Route route = getRoute(destination);
        printDirections(route);
    }
}
