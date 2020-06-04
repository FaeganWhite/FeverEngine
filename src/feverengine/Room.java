package feverengine;

import java.util.ArrayList;
import javafx.scene.image.Image;
import java.util.Random;

public class Room
{
    //---------------------------------------------------- Establish the variables
    String name;
    String description;
    private ArrayList<Item> items = new ArrayList<>();
    ArrayList<Entity> entities = new ArrayList<>();
    ArrayList<Door> doors = new ArrayList<>();
    Image roomImage;
    boolean visited = false;
    ArrayList<double[]> itemPositions = new ArrayList<>();
    Random rand = new Random();
    Map map;
    
    // Set the variables on creation
    public Room(String setName, String setDescription,Image newRoomImage, Map newMap){
        name = setName;
        description = setDescription;
        roomImage = newRoomImage;
        map = newMap;
    }
    
    
    //----------------------------------------------------------Room Save Info
    
    //Return the details of the room contents
    public String getSaveInfo() {
        System.out.println("getting save data from room");
        // Initialise the room code
        String roomCode = new String();
        // Add the rooms name and description
        roomCode += (name+"/"+description+"/"+visited);
        // For every item in the room
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i)!=null) {
                // Add a new line
                roomCode += "\n";
                // Add the details of each item
                roomCode += ("-"+items.get(i).getSaveInfo());
            }
        }
        return roomCode;
    }
    
    
    //--------------------------------------------------------- Gets and Sets
    
    // Get the rooms image
    public Image getImage() {
        return roomImage;
    }
    
    // Get the rooms items
    public ArrayList<Item> getItems() {
        return items;
    }
    
    
    
    
    //-------------------------------------------------------- Adjust item's within
    
    // set the rooms items
    public void setItems(ArrayList<Item> setItem) {
        items = setItem;
    }
    
    // remove an item from the room
    public void remove(Item removeItem) {
        items.remove(removeItem);
    }
    
    // clear all the items from the room
    public void empty() {
        items.clear();
    }
    
    
    // add an item from the room
    public boolean add(Item addItem) {
        
        if (itemPositions.size() > 0) {
            System.out.println("Attempting to add "+addItem.getName());
                   
            // Generate random co-ordinates
            double x = 1-(2*rand.nextDouble());
            double y = 1-(2*rand.nextDouble());
            double[] itemAddPosition = new double[3];
            System.out.println("Generated random co-ordinates");
            
            // Establish a success variable to test if the generated co-ordinates are within
            // suitable boundaries
            boolean success = false;
            
        
            // Check whether the co-ordinates are available
            for (double[] positions: itemPositions) {
               // Check how many nodes a position has
               System.out.println("Testing position");
               switch (positions.length) {
                    // if the position shape has 4 values (square)
                    case 4: 
                        System.out.println("RECTANGLE");
                        // if the point is within the area
                        if (positions[1] <= y && y <= positions[3]) {
                            if (positions[0] <= x && x <= positions[2]) {
                                // Set the x position of the item
                                itemAddPosition[0] = x;
                                // Set the y position of the item
                                itemAddPosition[1] = y;
                                // Set the scale of the item
                                itemAddPosition[2] = 1;
                                
                                System.out.println("SUCCESS");
                                
                                System.out.println(x);
                                System.out.println(y);
                                    
                               // return true
                               success = true;
                           }
                       }
                       break;
                    case 6:
                        System.out.println("TRIANGLE");
                        // Convert doubles to int for more precise math
                        int[] intTriangle = new int[6];
                        for (int i = 0; i < positions.length; i++) {
                            intTriangle[i] = (int)(positions[i]*1000);
                        }
                        int intx = (int)(x*1000);
                        int inty = (int)(y*1000);
                        
                        // Area of the trinagle
                        double a1 = area(intTriangle[0], intTriangle[1], intTriangle[2], intTriangle[3], intTriangle[4], intTriangle[5]);
                        double a2 = area(intx, inty,intTriangle[2],intTriangle[3],intTriangle[4],intTriangle[5]);
                        double a3 = area(intTriangle[0],intTriangle[1],intx,inty,intTriangle[4],intTriangle[5]);
                        double a4 = area(intTriangle[0],intTriangle[1],intTriangle[2],intTriangle[3],intx,inty);
                        
                        // If the point is within the triangle
                        if (a1 == a2 + a3 + a4) {
                            
                            System.out.println("TRIANGLE - SUCCESS");
                            
                            // Set the x position of the item
                            itemAddPosition[0] = x;
                            // Set the y position of the item
                            itemAddPosition[1] = y;
                            // Set the scale of the item
                            itemAddPosition[2] = 1;
                                
                                
                            System.out.println(x);
                            System.out.println(y);
            
                            // return true
                            success = true;
                       }
                       break;
                }  
            }
            
            // If the randomly generated point is within at least 1 positon
            if (success == true) {
                System.out.println("Item in item area."+itemAddPosition[0]+","+itemAddPosition[1]+","+itemAddPosition[2]+",");
                // Set the items's coordinates to the randomly generated ones
                addItem.drawPosition = itemAddPosition;
                // Add the item to the room
                items.add(addItem);
                return true;
            } else {
                // Otherwise try again
                return add(addItem);
            }
        } else {
            return false;
        }
    }
    
    // Return the area of a triange
    public double area(int x1, int y1, int x2, int y2, int x3, int y3) 
    { 
       return Math.abs((x1*(y2-y3) + x2*(y3-y1)+x3*(y1-y2))/2.0); 
    } 
    
    // add an item location to the room
    public void addItemPosition(double[] coordinates) {
        System.out.println("Adding an item area to "+this.name);
        // add this to the arrayList of co-ordinates
        itemPositions.add(coordinates);
    }
}
