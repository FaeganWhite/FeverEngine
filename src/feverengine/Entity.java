package feverengine;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Entity
{
   // Establish entity variables
   
   String name;
   String description;
   private int health;
   int maxHealth;
   int x;
   int y;
   int strength;
   Image itemImage;
   ImageView itemImageView;
   double[] itemImagePosition = new double[2];
   double scaleSize;
   
   // Establish the inventory
   ArrayList<Item> inventory = new ArrayList<>();
   
   // Set player variables upon creation
   public Entity(String setName, String setDescription, int newHealth, int newStrength, Image newImage, double newScaleSize){
        name = setName;
        description = setDescription;
        health = newHealth;
        strength = newStrength;
        maxHealth = health;
        itemImage = newImage;
        itemImageView = new ImageView(itemImage);
        scaleSize = newScaleSize;
    }
   
   // Find out how much weight the player is carrying
   public double getInventoryWeight() {
       System.out.println("Strength: "+strength);
       System.out.println("Getting inventory weight");
       // Create a variable for the running total
       double addingWeight = 0;
       // Add the weight of every item in the inventory 
       for (Item item: inventory) {
            if (item!=null) {
                addingWeight += item.getWeight();
                System.out.println("Added "+item.getName()+", weight "+item.getWeight());
            }
       }
       // Return this number
       return addingWeight;
    }
   
   // Get/Set the entity health
   public void setHealth(int setHealth) {
       health = setHealth;
       if (health > maxHealth) {
           health = maxHealth+1;
       }
    }
   
   public int getHealth() {
       return health;
    }
    
}
