package feverengine;

import java.util.ArrayList;

public class Player
{
   // Establish player variables
   private String name;
   private int health;
   private int maxHealth;
   private int x;
   private int y;
   private int strength;
   // Establish the inventory
   private ArrayList<Item> inventory = new ArrayList<>();
   
   // Set player variables upon creation
   public Player(String setName, int newHealth, int newStrength){
        name = setName;
        health = newHealth;
        strength = newStrength;
        maxHealth = health;
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
   
   // Get the player co-ordiantes
   public int getX() {
       return x;
   }
   
   public int getY() {
       return y;
   }
   
   // Set the player location
   public void setX(int newX) {
       x = newX;
    }
   
   public void setY(int newY) {
       y = newY;
    }
   
   // Get/Set the player health
   public void setHealth(int setHealth) {
       health = setHealth;
       if (health > maxHealth) {
           health = maxHealth+1;
       }
    }
   
   public int getHealth() {
       return health;
    }
   
      // Get/Set the player max health
   public void setMaxHealth(int setHealth) {
       maxHealth = setHealth;
    }
   
   public int getMaxHealth() {
       return maxHealth;
    }
    
    // Get/Set the player strength
   public void setStrength(int setStrength) {
       strength = setStrength;
    }
   
   public int getStrength() {
       return strength;
    }
   
       // Get the rooms items
    public ArrayList<Item> getItems() {
        return inventory;
    }
    
    // set the rooms items
    public void setItems(ArrayList<Item> setItem) {
        inventory = setItem;
    }
    
    // remove an item from the room
    public void remove(Item removeItem) {
        inventory.remove(removeItem);
    }
    
    // clear all the items from the room
    public void empty() {
        inventory.clear();
        System.out.println("inventiry emptied");
    }
    
    // remove an item from the room
    public void add(Item addItem) {
        inventory.add(addItem);
        System.out.println("added "+addItem.getName()+ "to inventory");
    }
    
    // Get/Set the player name
   public void setName(String setName) {
       name = setName;
    }
   
   public String getName() {
       return name;
    }
   
}
