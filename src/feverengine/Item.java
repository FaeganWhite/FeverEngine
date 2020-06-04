package feverengine;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item
{
    private String name;
    String description;
    private double weight;
    private int size;
    boolean visible = true;
    Image itemImage;
    ImageView itemImageView;
    double[] drawPosition = {0, 0, 0};
    double scaleSize;
    
    public Item(String setName, String setDescription, double setWeight, int setSize, Image newImage, double newScaleSize){
        name = setName;
        description = setDescription;
        weight = setWeight;
        size = setSize;
        itemImage = newImage;
        itemImageView = new ImageView(itemImage);
        scaleSize = newScaleSize;
    }
    
    public String getSaveInfo() {
        // Initialise the item code
        String itemCode = new String();
        // Add the items name and description
        itemCode += (name+"/"+description+"/"+weight+"/"+size+"/"+visible);
        return itemCode;
    }
    
    public String getInvSaveInfo() {
        // Initialise the item code
        String itemCode = new String();
        // Add the items name and description
        itemCode += (name+"/"+description+"/"+weight+"/"+size+"/"+visible);
        return itemCode;
    }
    
    public ImageView getItemImageView() {
        return itemImageView;
    }
    
    // \\return the scaling factor for the image
    public double getScaleSize() {
        return scaleSize;
    }
    
    // Get the rooms image
    public Image getImage() {
        return itemImage;
    }
    
    // Get the rooms image
    public String getName() {
        return name;
    }
    
    // Get the rooms image
    public void setName(String newName) {
        name = newName;
    }
    
        // Get the rooms image
    public double getWeight() {
        return weight;
    }
    
    // Get the rooms image
    public void setWeight(double newWeight) {
        weight = newWeight;
    }
    
    // Get the room's size
    public int getSize() {
        return size;
    }
    
    // Set the room's size
    public void setSize(int newSize) {
        size = newSize;
    }
}
