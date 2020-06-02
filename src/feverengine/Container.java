package feverengine;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class Container extends Item 
{
    private boolean locked;
    private ArrayList<Item> items = new ArrayList<>();
    
    
     public Container(String setName, String setDescription, double setWeight, int setSize, boolean setVisible, boolean setLocked, Image setImage, double newScaleSize){
        super(setName, setDescription, setWeight, setSize, setImage, newScaleSize);
        visible = setVisible;
        locked = setLocked;
    }
    
    int getSpace() {
        int returnSpace = getSize();
        for (int i = 0; i < items.size(); i++) {
            returnSpace -= items.get(i).getSize();
        }
        return returnSpace;
    }
    
    // Check for item
    public Item checkItem(String item, boolean remove) {
        // Initialise the returned item as null
        Item success = null;
        // For the entire list of items
        for (int i = 0; i < items.size(); i++) {
            // If the current item is the item being checked for
            if (items.get(i).getName().equals(item)) {
                // If the item looked for is visible
                if (items.get(i).visible == true) {
                    // Return the item and break the loop
                    success = items.get(i);
                    // Remove the item if remove = true
                    if (remove == true) {
                        items.remove(i);
                    }
                    break;
                } 
                // Else if the current item is a container and has items in its item list
            } else if (items.get(i) instanceof Container && ((Container) items.get(i)).items.size() > 0) {
                if (((Container) items.get(i)).visible == true) {
                    // Return a search of its items
                    success = ((Container) items.get(i)).checkItem(item, remove);
                    // If it found the item being checked for, break the loop
                    if (success != null) {
                        break;
                    }
                }
            }
        }
        return success;
    }
    
    // Check for item when loading a game
    public Item loadCheckItem(String item, boolean remove) {
        // Initialise the returned item as null
        Item success = null;
        // For the entire list of items
        for (int i = 0; i < items.size(); i++) {
            // If the current item is the item being checked for
            if (items.get(i).getName().equals(item)) {
                // If the item looked for is a container
                if (items.get(i) instanceof Container) {
                    // Return the item and break the loop
                    success = items.get(i);
                    // Remove the item if remove = true
                    if (remove == true) {
                        items.remove(i);
                    }
                    break;
                // Otherwise oif the item isnt a container
                } else if (!(items.get(i) instanceof Container)) {
                    // Return the item and break the loop
                    success = items.get(i);
                    //Remove the item if remove = true
                    if (remove == true) {
                        items.remove(i);
                    }
                    break;
                }
                // Else if the current item is a container and has items in its item list
            } else if (items.get(i) instanceof Container && ((Container) items.get(i)).items.size() > 0) {
                // Return a search of its items
                success = ((Container) items.get(i)).checkItem(item, remove);
                // If it found the item being checked for, break the loop
                if (success != null) {
                    break;
                }
            }
        }
        return success;
    }
    
    // Reveal all of the containers within self
    public void revealContainers() {
        // For the entire list of items
        for (int i = 0; i < items.size(); i++) {
            // Set it to be seen
            items.get(i).visible = true;
        }
    }
    
    @Override
    public String getSaveInfo() {
        // Initialise the item code
        String containerCode = new String();
        // Add the rooms name and description
        containerCode += (getName()+"/"+description+"/"+getWeight()+"/"+getSize()+"/"+visible+"/"+locked);
        // For every item in the container
        for (int i = 0; i < items.size(); i++) {
            // Add a new line
            containerCode += "\n";
            // Add the details of each item
            containerCode += ("*"+getName()+"<"+items.get(i).getSaveInfo());
        }
        return containerCode;
    }
    
    @Override
    public String getInvSaveInfo() {
        // Initialise the item code
        String containerCode = new String();
        // Add the rooms name and description
        containerCode += (getName()+"/"+description+"/"+getWeight()+"/"+getSize()+"/"+visible+"/"+locked);
        // For every item in the container
        for (int i = 0; i < items.size(); i++) {
            // Add a new line
            containerCode += "\n";
            // Add the details of each item
            containerCode += ("#"+getName()+"<"+items.get(i).getInvSaveInfo());
        }
        return containerCode;
    }
    
    // Get the container's items
    public ArrayList<Item> getItems() {
        return items;
    }
    
    // set the container's items
    public void setItems(ArrayList<Item> setItem) {
        items = setItem;
    }
    
    // remove an item from the container
    public void remove(Item removeItem) {
        items.remove(removeItem);
    }
    
    // clear all the items from the container
    public boolean getLocked() {
        return locked;
    }
    
    // add an item to the container
    public void setLocked(boolean setLocked) {
        locked = setLocked;
    }
    
        // clear all the items from the container
    public void empty() {
        items.clear();
    }
    
    // add an item to the container
    public void add(Item addItem) {
        items.add(addItem);
    }
}
