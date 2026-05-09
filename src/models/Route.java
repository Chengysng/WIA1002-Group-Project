/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.ArrayList;

/**
 *
 * @author Yim Zi Hao
 */
public class Route {

    public ArrayList<String> path;
    public int totalDistance;

    public Route(ArrayList<String> path, int totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }
}
