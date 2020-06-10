/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feverengine;
import javafx.scene.paint.Color;

/**
 *
 * @author faegan
 */
public class RoomLight {
    
    double[] position = new double[3]; // Location of the light source - x, y, z
    Color color; // Colour of the light source
    double intensity;
    double angle;
    String type;
    
    public RoomLight(double newAngle, double z, Color newColor, double newIntensity) {
        angle = newAngle;
        color = newColor;
        intensity = newIntensity;
        type = "directional";
    }
    
    public RoomLight(double x, double y, double z, Color newColor, double newIntensity) {
        position[0] = x;
        position[1] = y;
        position[2] = z;
        color = newColor;
        intensity = newIntensity;
        type = "point";
    }
}
