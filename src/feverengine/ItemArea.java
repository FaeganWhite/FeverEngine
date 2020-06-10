/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feverengine;
import java.util.ArrayList;

/**
 *
 * @author faegan
 */
public class ItemArea {
    String type = new String();
    double[] coordinates;
    
    public ItemArea(String newType, double a, double b, double c, double d) {
        type = newType;
        coordinates = new double[4];
        coordinates[0] = a;
        coordinates[1] = b;
        coordinates[2] = c;
        coordinates[3] = d;
    }
    
    public ItemArea(String newType, double a, double b, double c, double d, double e, double f) {
        type = newType;
        coordinates = new double[6];
        coordinates[0] = a;
        coordinates[1] = b;
        coordinates[2] = c;
        coordinates[3] = d;
        coordinates[4] = e;
        coordinates[5] = f;
    }
    
    public ItemArea(String newType, double a, double b, double c, double d, double e, double f, double g, double h) {
        type = newType;
        coordinates = new double[8];
        coordinates[0] = a;
        coordinates[1] = b;
        coordinates[2] = c;
        coordinates[3] = d;
        coordinates[4] = e;
        coordinates[5] = f;
        coordinates[6] = g;
        coordinates[7] = h;
    }
}
