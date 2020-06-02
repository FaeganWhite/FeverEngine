package feverengine;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class Room
{
    // Establish the variables
    String name;
    String description;
    private ArrayList<Item> items = new ArrayList<>();
    ArrayList<Entity> entities = new ArrayList<>();
    ArrayList<Door> doors = new ArrayList<>();
    Image roomImage;
    boolean visited = false;
    ArrayList<double[]> itemPositions = new ArrayList<>();
    
    // Set the variables on creation
    public Room(String setName, String setDescription,Image newRoomImage){
        name = setName;
        description = setDescription;
        roomImage = newRoomImage;
    }
    
    // Return the details of the room contents
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
    
    // Get the rooms image
    public Image getImage() {
        return roomImage;
    }
    
    // Get the rooms items
    public ArrayList<Item> getItems() {
        return items;
    }
    
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
    
    // remove an item from the room
    public void add(Item addItem) {
        items.add(addItem);
    }
    
    // add an item location to the room
    public void addItemPosition(double x, double y, double distance) {
        // make an array of the inputted co-ordinates
        double[] coordinate = {x, y, distance};
        // add this to the arrayList of co-ordinates
        itemPositions.add(coordinate);
    }
}
