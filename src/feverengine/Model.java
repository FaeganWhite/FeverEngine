package feverengine;

import java.util.ArrayList;
/**
 * The model is used to simulate the game world.
 *
 * Faegan White
 */
public class Model
{
    
    //----------------------------------------------- Establish world objects
    
    Output output;
    // Establish the time
    private int time = 0;
    Controller controller;
    Map map;
    // Create the Player (name, health, strength)
    Player player = new Player("Placeholder", 20, 10);
    // Create the File Controller
    FileControl file = new FileControl(this, map, player);
    // Establish whether the users move should be counted by
    // the game counter
    boolean skipCount = false;
    
    
    // Set output and control upon creation
    public Model(Output setOutput, Controller setController, Map setMap){
        output = setOutput;
        output.file = file;
        controller = setController;
        map = setMap;
        // link the classes for the fileController
        file.map = map;
        file.output = output;
    }
    
    
    
    //----------------------------------------------- Starting and updating game
    
    // Set the player location
    public void setPlayerLocation() {
        player.setX(5);
        player.setY(4);
        map.gameGrid[player.getX()][player.getY()].visited = true;
    }
    
    // Establish the model
    public void startGame() {
        // Establish player to the output
        output.player = player;
        // Set the player's health
        player.setHealth(20);
        // Give the user a description of their location
        look();
    }
    
    public void update() {
        System.out.println("check if the player has health");
        System.out.println(player.getHealth());
        if (player.getHealth() <= 0) {
            System.out.println("no health");
            output.gameOver();
        }
        System.out.println("player has health, keep going");
        //controller.getInput();
        if (skipCount == false) {
            System.out.println("player.health-=1");
            // Decrease the health by 1
            player.setHealth(player.getHealth()-1);
            // Add the move to the game clock
            time += 1;
            // Update the NPC locations on the game map
            updateNPClocation();
        }
        // set the skip count to false so next move isn't skipped unles necessary
        skipCount = false;
    }
    
    
    
    
    //----------------------------------------------------- Player actions
    
    // Change the player's location
    public void movePlayer(String direction) {
        int x = player.getX();
        int y = player.getY();
        switch(direction) {
            case "north":
                // Check if the player can move from their location to the new location
                if (checkMove(x, y, x, y-1, direction)) {
                    // If so, move them and describe the new locatiuon to the user
                    player.setY(y-1);
                    look();
                    // set the current room to visited
                    map.gameGrid[x][y-1].visited = true;
                } else {
                    // If not, tell the player they can't go that way
                    output.cantMove();
                }
                break;
            case "south":
                if (checkMove(x, y, x, y+1, direction)) {
                    player.setY(y+1);
                    look();
                    // set the current room to visited
                    map.gameGrid[x][y+1].visited = true;
                } else {
                    output.cantMove();
                }
                break;
            case "east":
                if (checkMove(x, y, x+1, y, direction)) {
                    player.setX(x+1);
                    look();
                    // set the current room to visited
                    map.gameGrid[x+1][y].visited = true;
                } else {
                    output.cantMove();
                }
                break;
            case "west":
                if (checkMove(x, y, x-1, y, direction)) {
                    player.setX(x-1);
                    look();
                    // set the current room to visited
                    map.gameGrid[x-1][y].visited = true;
                } else {
                    output.cantMove();
                }
                break;
            default:
                output.unknownDirection();
        }
    }
    
    // Wait
    public void waitPlayer() {
        output.waitPrint();
    }
    
    // Pick up an item
    public void takeItem(String item) {
        // Get the player co-ordinates
        int x = player.getX();
        int y = player.getY();
        // Create an arrayList of items in the current room
        ArrayList<Item> itemsRoom = map.gameGrid[x][y].getItems();
        // Create an arrayList of items in the inventory
        ArrayList<Item> itemsInv = player.getItems();
        // Check if the user has referenced an item in the room
        if (itemReferencePresent(itemsRoom, item, false) != null) {
           // Establish the item to be taken
           Item takeItem = itemReferencePresent(itemsRoom, item, false);
           // If the player can carry the object
           if (player.getInventoryWeight()+takeItem.getWeight() < player.getStrength()) {
                // Output message
                output.itemTaken(takeItem.getName());
                // Add item to the inventory and remove the item from the room
                player.add(itemReferencePresent(map.gameGrid[x][y].getItems(), item, true));
            } else if (takeItem.getWeight() > player.getStrength()) {
                // If the item's too heavy, inform the player
                output.itemTooHeavy(item);
            } else {
                // If the player is too weighed down, inform the player
                output.tooMuchWeight(item);
            }
        } else if (itemReferencePresent(itemsInv, item, false) != null){
            output.alreadyCarrying(item);
        } else {
            output.itemPickFail(item);
        }
    }

    // Drop an item
    public void dropItem(String item) {
        // Get the player co-ordinates
        int x = player.getX();
        int y = player.getY();
        // Create an arrayList of items in the inventory
        ArrayList<Item> items = player.getItems();
        // Check if the user has referenced one of them
        if (itemReferencePresent(items, item, false) != null) {
            // Output message
            output.itemDropped(itemReferencePresent(items, item, false).getName());
            // Add item to the room and remove the item from the player's inventory
            map.gameGrid[x][y].add(itemReferencePresent(items, item, true));
            // Successful reference
        } else {
            // Aler the user the item couldn't be found
            output.itemCarryFail(item);
        }
    }
    
    // Put an item in a container
    public void dropItemIn(String item, String location) {
        System.out.println("Moving an item");
        // Get the player co-ordinates
        int x = player.getX();
        int y = player.getY();
        // Create an arrayList of items in the inventory
        ArrayList<Item> items = player.getItems();
        // Create an ArrayList of items in the room
        ArrayList<Item> mapItems = map.gameGrid[x][y].getItems();
        
        System.out.println("Got the list of all the items");
        // If the container is in the room
        if (itemReferencePresent(mapItems, location, false) != null) {
            System.out.println("foiund the container");
            // If the container is a container
            if (itemReferencePresent(mapItems, location, false) instanceof Container) {
                System.out.println("Container is a container");
                // Set the container
                Item container = itemReferencePresent(mapItems, location, false);
                System.out.println("container set");
                // If the user has referenced the item to drop from inventory
                if (itemReferencePresent(items, item, false) != null) {
                    System.out.println("item to move is in the inventory");
                    // Establish the item to move
                    Item itemToMove = itemReferencePresent(items, item, false);
                    System.out.println("get the item to move");
                    // If the item isn't too heavy to carry
                    if (canMove(itemToMove, container, player.getStrength())) {
                        System.out.println("item can be moved");
                        // Output message
                        output.itemDroppedIn(item, location);
                        System.out.println("output drop message");
                        // Add item to the container and remove it from the inventory/., ,m
                        ((Container) itemReferencePresent(mapItems, location, false)).add(itemToMove); 
                        System.out.println("add the item to the container and remove it form inventory");
                        // Remove the item from the player's inventory
                        itemToMove = itemReferencePresent(items, item, true);
                        System.out.println("item removed from player inventory");
                    } // else if the user has referenced an item in the room
                } else if (itemReferencePresent(mapItems, item, false) != null) {
                    System.out.println("the user has referenced and item in the room");
                    // Establish the item to move
                    Item itemToMove = itemReferencePresent(mapItems, item, false);
                    System.out.println("establish the item");
                    // If the item is too heavy to carry
                    if (canMove(itemToMove, container, player.getStrength())) {
                        System.out.println("can move the ");
                        // Output message
                        output.itemDroppedIn(item, location);
                        System.out.println("output message");
                        // Add item to the container
                        ((Container) itemReferencePresent(mapItems, location, false)).add(itemToMove);
                        System.out.println("item added to the container");
                        // Remove item
                        itemToMove = itemReferencePresent(mapItems, item, true);
                        System.out.println("remove the item");
                    }
                } else {
                    output.itemCarryFail(item);
                }
            }
                // Else if the container is in the inventory 
            } else if (itemReferencePresent(items, location, false) != null) {
                // If the container is a container
                if (itemReferencePresent(items, location, false) instanceof Container) {
                    // Set the container
                    Item container = itemReferencePresent(items, location, false);
                    // If the user has referenced the item to drop from inventory
                    if (itemReferencePresent(items, item, false) != null) {
                        // Establish the item to move
                        Item itemToMove = itemReferencePresent(items, item, false);
                        // If the item is too heavy to carry
                        if (canMove(itemToMove, container, player.getStrength())) {
                            // Output message
                            output.itemDroppedIn(item, location);
                            // Add item to the container and remove it from the inventory
                            ((Container) itemReferencePresent(items, location, false)).add(itemToMove);
                            // Remove the item from the player's inventory
                            itemToMove = itemReferencePresent(items, item, true);
                        } // else if the user has referenced an item in the room
                    } else if (itemReferencePresent(mapItems, item, false) != null) {
                        // Establish the item to move
                        Item itemToMove = itemReferencePresent(mapItems, item, false);
                        // If the item can be moved
                        if (canMove(itemToMove, container, player.getStrength())) {
                            // Output message
                            output.itemDroppedIn(item, location);
                            // Add item to the container
                            ((Container) itemReferencePresent(items, location, false)).add(itemToMove);
                            // Remove item
                            itemToMove = itemReferencePresent(mapItems, item, true);
                        }
                    
                } else {
                    output.itemCarryFail(item);
                }
            } else {
                output.containerTypeFail(location);
            }
        } else {
            output.itemPickFail(location);
        }
    }
    
    // Check if an object can be moved
    private boolean canMove(Item item, Item container, int strength ) {
        boolean move = false;
        if (item.getWeight() > strength) {
            output.itemTooHeavy(item.getName());
        } else if (item.getSize() > ((Container) container).getSpace()) {
            output.itemTooBig(item.getName(), container.getName());
        } else if (item.getSize() > ((Container) container).getSpace()) {
            output.notEnoughRoom(container.getName());
        } else {
            move = true;
        }
        return move;
    }
    
    // Eat an item
    public void eatItem(String item) {
        // Get the player co-ordinates
        int x = player.getX();
        int y = player.getY();
        // Create an arrayList of items in the current room
        ArrayList<Item> itemsRoom = map.gameGrid[x][y].getItems();
        // Create an arrayList of items in the inventory
        ArrayList<Item> itemsInv = player.getItems();
        // Check if the user has referenced an item in the room
        if (itemReferencePresent(itemsRoom, item, false) != null) {
            // If so, assign it to food
            Item food = itemReferencePresent(itemsRoom, item, false);
            // If food is an instance of Food
            if (food instanceof Food) {
                // Eat the food
                eat(itemsRoom, food);
                // Remove from the food item from the container
                food = itemReferencePresent(itemsRoom, item, true);
            } else {
                // Otherwise alert the player they can't eat it
                output.youCant("eat");
            }
        
        } 
        // If not in room, check if player referenced item in inventory
        else if (itemReferencePresent(itemsInv, item, false) != null) {
            // If found, assign it to food
            Item food = itemReferencePresent(itemsInv, item, false);
            // If food is an instance of Food
            if (food instanceof Food) {
                // Eat the food
                eat(itemsInv, food);
                // Remove from the food item from the container
                food = itemReferencePresent(itemsInv, item, true);
            } else {
                // Else alert the user they can't eat it
                output.youCant("eat");
            }
        
        } else {
            // If the player hasn't referenced any object present, alert them
            output.itemCarryFail(item);
        }
    }
    
    // Search a container
    public void searchContainer(String container) {
        // Get the player co-ordinates
        int x = player.getX();
        int y = player.getY();
        // Create an arrayList of items in the current room
        ArrayList<Item> itemsRoom = map.gameGrid[x][y].getItems();
        // Create an arrayList of items in the inventory
        ArrayList<Item> itemsInv = player.getItems();
        // Check if the user has referenced a container in the room
        if (itemReferencePresent(itemsRoom, container, false) != null) {
            // If so, assign it to an item
            Item containerItem = itemReferencePresent(itemsRoom, container, false);
            if (containerItem instanceof Container) {
                // Search the container
                output.search(((Container) containerItem).getItems(), container);
                // Reveal the containers within
                ((Container) containerItem).revealContainers();
            } else {
                // Otherwise alert the player they can't search that item
                output.youCant("search");
            }
        }  
        // If not in room, check if player referenced item in inventory
        else if (itemReferencePresent(itemsInv, container, false) != null) {
            // If found, assign it to a containerItem
            Item containerItem = itemReferencePresent(itemsInv, container, false);
            // If containerItem is an instance of Container
            if (containerItem instanceof Container) {
                // Output the search
                output.search(((Container) containerItem).getItems(), container);
                // Reveal the containers within
                ((Container) containerItem).revealContainers();
            } else {
                // Else alert the user they can't search that item
                output.youCant("search");
                System.out.println("brett");
            }
        } else {
            // If the player hasn't referenced any container present, alert them
            output.itemPickFail(container);
        }
    }
    
    // Examine an object
    public void lookAt(String item) {
        // Get the player co-ordinates
        int x = player.getX();
        int y = player.getY();
        // Create an arrayList of items in the current room
        ArrayList<Item> itemsRoom = map.gameGrid[x][y].getItems();
        // Create an arrayList of items in the inventory
        ArrayList<Item> itemsInv = player.getItems();
        // Check if the user has referenced a container in the room
        if (itemReferencePresent(itemsRoom, item, false) != null) {
            // If so, examine the object
            output.lookAt(itemReferencePresent(itemsRoom, item, false));
        }  
        // If not in room, check if player referenced item in inventory
        else if (itemReferencePresent(itemsInv, item, false) != null) {
            // If so, examine the object
            output.lookAt(itemReferencePresent(itemsInv, item, false));
        } else {
            // Else relay failure message
            output.itemSeeFail(item);
        }
    }
    
    // If the user has correctly referenced an item name, return item (ArrayList, item)
    public Item itemReference(ArrayList<Item> items, String item) {
        // Initialise the returned item as null
        Item success = null;
        // If the item is present in the given arrayList
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item)) {
                // Return the item
                success = items.get(i);
                break;
            }
        }
        return success;
    }
    
    // Check if the the referenced item is present in the current room/inventory
    private Item itemReferencePresent(ArrayList<Item> items, String item, boolean remove) {
        // Initialise the returned item as null
        Item success = null;
        // If the item is present in the given arrayList
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i)!=null) {
                if (items.get(i).getName().equals(item)) {
                    // Return the item
                    success = items.get(i);
                    // If remove is true
                    if (remove == true) {
                        // replace the item with a null
                        items.set(i, null);
                    }
                    break;
                } else if (items.get(i) instanceof Container && ((Container) items.get(i)).getItems().size() > 0) {
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
    
    // Examine surroundings
    public void look() {
        // Get the player co-ordinates
        int x = player.getX();
        int y = player.getY();
        Room currentRoom = map.gameGrid[x][y];
        ArrayList<String> surroundingsList = new ArrayList<String>(); 
        ArrayList<Door> doors = currentRoom.doors;
        for (int i = 0; i < doors.size(); i++) {
            switch (doors.get(i).direction) {
                case "north":
                    surroundingsList.add("north");
                    surroundingsList.add(map.gameGrid[x][y-1].name);
                    break;
                case "south":
                    surroundingsList.add("south");
                    surroundingsList.add(map.gameGrid[x][y+1].name);
                    break;
                case "east":
                    surroundingsList.add("east");
                    surroundingsList.add(map.gameGrid[x+1][y].name);
                    break;
                case "west":
                    surroundingsList.add("west");
                    surroundingsList.add(map.gameGrid[x-1][y].name);
                    break;
            }
        }
        output.describeRoom(map.gameGrid[x][y], surroundingsList);
    }
    
    // Open a container/door
    public void open(String object, String key) {
        
    }
    
    // Check the player's intended move
    public boolean checkMove(int setX, int setY, int testX, int testY, String direction) {
        // Get players intended move
        int xTest = testX;
        int yTest = testY;
        // Get players current location
        int x = setX;
        int y = setY;
        // Initialise return variable
        boolean success = false;
        /* If the player's intended x is within the map width and the player's intended Y
        is within the map height */
        if (xTest > -1 && xTest < map.gameGrid.length && yTest > -1 && yTest < map.gameGrid[0].length){
            // Get the doors at the current location
            ArrayList<Door> doors = map.gameGrid[x][y].doors;
            // For each door
            for (int i = 0; i < doors.size(); i++) {
                // If the direction of the door is the same as the users intended direction
                if (doors.get(i).direction.equals(direction)) {
                    // return true
                    success = true;
                    break;
                } else {
                    success = false;
                }
            }
        } else {
            success = false;
        }
        return success;
    }
    
    // Change an entities health
    public void changeHealth(Player entity, int health) {
        entity.setHealth(entity.getHealth()+health);
        output.healthChange(health);
    }
    
    // Eat an item (arrayList, location)
    public void eat2(ArrayList<Item> list, int i){
        // Output message
        output.itemEaten(list.get(i).getName());
        // Add the nutrition of the food to the player
        changeHealth(player, ((Food) list.get(i)).nutrition);
        // Remove the food item from the location
        list.remove(list.get(i));
    }
    
    // Eat an item (arrayList, location)
    public void eat(ArrayList<Item> list, Item food){
        // Output message
        output.itemEaten(food.getName());
        // Add the nutrition of the food to the player
        changeHealth(player, ((Food) food).nutrition);
    }
    
    
    
    
    // ----------------------------------------------------------- Game Operations
    
    
    // Set the player's location
    public void loadPlayer(String name, int maxHealth, int health, int x, int y, int strength) {
        // Set the players variables
        player.setName(name);
        player.setMaxHealth(maxHealth);
        player.setHealth(health);
        player.setX(x);
        player.setY(y);
        player.setStrength(strength);
    }
    
    // Quit the game
    public void quit() {
        // Don't count this move on the game clock
        skipCount = true;
        output.checkQuit();
    }
    
    // Save the game
    public void saveGame(String name) {
        // Don't count this move on the game clock
        skipCount = true;
        // Create the save file
        System.out.println("saving game in model");
        file.newFile(name);
    }
    
    // Load the game
    public void loadGame(String loadName) {
        System.out.println("model.loadgame");
        // Don't count this move on the game clock
        skipCount = true;
        // Empty the player's inventory
        player.empty();
        //Load the save
        file.getFile(loadName);
    }
    
        // Get the room's size
    public int getTime() {
        return time;
    }
    
    // Set the room's size
    public void setTime(int newTime) {
        time = newTime;
    }
    
    
    
    
    // ----------------------------------------------- Dealing with Entities
    
    
    // Return an array of all the entities present in the current room
    public ArrayList<Entity> presentEntities() {
        // Get the entities in the current room
        int x = player.getX();
        int y = player.getY();
        return map.gameGrid[x][y].entities;
    }
    
    // Update the location of the NPCs
    public void updateNPClocation() {
    }
}
