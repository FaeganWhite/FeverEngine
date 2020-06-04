package feverengine;

import java.util.ArrayList;
import javafx.scene.image.Image;
/**
 * The map class generates the rooms and items at the beginning of the game as well as storing
 * the positions of the rooms throughout the game.
 *
 * Faegan White
 * 
 */
public class Map
{
    // Establish a library of every known Item
    ArrayList<Item> itemLibrary = new ArrayList<Item>();
    
    // Establish the player
    Player player;
    
    // Establish the main
    Model model;
    
    // Establish all the rooms in the game
    Room frontRoom = new Room("Front Room", "dust coats every surface", new Image("feverengine/images/backgrounds/frontroom1.jpg"), this);
    Room bedroom = new Room("Bedroom", "a slither of light from the curtains cuts through the darkness.", new Image("feverengine/images/backgrounds/bedroom1.jpg"), this);
    Room kitchen = new Room("Kitchen", "dirty dishes cover the sideboards.", new Image("feverengine/images/backgrounds/kitchen1.jpg"), this);
    Room hallway = new Room("Hallway", "the faded wallpaper peels from the walls.", new Image("feverengine/images/backgrounds/test1.jpg"), this);
    Room bathroom = new Room("Bathroom", "the tap drips slowly drips into the sink which is begging to fill.", new Image("feverengine/images/backgrounds/bathroom1.jpg"), this);
    Room stairs = new Room("Stairs", "the steps below you creak with every step.", new Image("feverengine/images/backgrounds/test1.jpg"), this);
        
    // Establish the game grid
    Room[][] gameGrid = new Room[11][11];
    
    public Map() {
        // Build the map
        buildGameGrid();
        // Add the items and entities to the map
        populate();
    }
    
    private void buildGameGrid() {
        // Set the room positions
        gameGrid[4][4] = bedroom;
        gameGrid[5][4] = bathroom;
        gameGrid[5][5] = stairs;
        gameGrid[4][5] = kitchen;
        gameGrid[4][6] = frontRoom;
        gameGrid[5][6] = hallway;
        
        // Establish all the item positions in the rooms
        double[] square = {-1, 0.8, 1, 1};
        bedroom.addItemPosition(square);
        
        double[] square2 = {-0.6, 0.7, 1, 0.6};
        bathroom.addItemPosition(square2);
        
        double[] trianglek = {-0.2, 0.5, 0.7, 0.95, -1, 0.95};
        kitchen.addItemPosition(trianglek);

        double[] square4 = {0, 0.7, 1, 1};
        frontRoom.addItemPosition(square);
    }
    
    // Establish the items in the game
    Item lamp = new Item("lamp","A rusty oil lamp",0.5, 3, new Image("feverengine/images/items/lamp1.png"), 2);
    Item cheese = new Food("cheese","Some smelly cheese.",0.3, 1, 5, new Image("feverengine/images/items/cheese1.png"), 1);
    
    // Establish the containers in the game (name, description, weight, size, open, locked, image, image scale)
    Item wardrobe = new Container("wardrobe","An old, battered wardrobe.", 30, 20, false, false, null, 1);
    Item jacket = new Container("jacket","A moth-bitten jacket",2, 2, false, false, new Image("feverengine/images/items/jacket1.png"), 4);
    Item safe = new Container("safe","A tough looking safe with a key hole on the front.",100, 10, false, false, new Image("feverengine/images/items/safe2.png"), 4);
    Item pot = new Container("pot","A small metal pot.",1, 1, false, false, new Image("feverengine/images/items/pot1.png"), 1.2);
    Item bed = new Container("bed","A dirty bed. The bare matress is covered in many stains",30, 30, false, false, null, 1);
    
    // Establish the doors (name, description, locked, direction, )
    Door doorNorth = new Door("door","A heavy door.","north", false, false);
    Door doorSouth = new Door("door","A heavy door.","south", false, false);
    Door doorEast = new Door("door","A heavy door.","east", false, false);
    Door doorWest = new Door("door","A heavy door.","west", false, false);

    // Establish the keys (name, description, weight, size, item it unlocks, image, image scale)
    Item key1 = new Key("key","A tiny key. It looks as if it will fit a very small lock.", 0.1, 1, safe, new Image("feverengine/images/items/key1.png"), 1);
    
    
    
    // Establish the entities in the game world (name, description, health, strength, image, image scale)
    
    Entity mutant = new Entity("Zorg","A mutant. They look angry.", 10, 10, new Image("feverengine/images/entities/MutantTest.png"), 1);
    
    private void populate() {
        
        // Add items to the map
        populate(lamp, bedroom);
        populate(bed,bedroom);
        populate(wardrobe, bedroom);
        populate(cheese, kitchen);
        populate(safe, frontRoom);
        populate(wardrobe, frontRoom);
        
        // Add items to the containers
        fill(jacket, wardrobe);
        fill(pot, jacket);
        fill(key1, pot);
        
        // Add doors to the map
        frontRoom.doors.add(doorNorth);
        frontRoom.doors.add(doorEast);
        kitchen.doors.add(doorNorth);
        kitchen.doors.add(doorSouth);
        bedroom.doors.add(doorEast);
        bedroom.doors.add(doorSouth);
        bathroom.doors.add(doorWest);
        hallway.doors.add(doorWest);
        hallway.doors.add(doorNorth);
        stairs.doors.add(doorSouth);
        
        // Add the entities to the map
        
    }
    
    // Load a new map from a string
    public void loadMap(String[] mapCode) {
        System.out.println("map.loadingmap");
        // Clear all the items from the game
        clearMap(gameGrid);
        System.out.println("Map cleared");
        // Initialise the current loading room name
        String currentRoomName;
        // Initialise the current loading room
        Room currentRoom = null;
        // Get save file map dimensions
        System.out.println("get map dimentions");
        // For every line in the save file (after the initial dimensions line
        for (int i = 1; i < mapCode.length; i++) {
            // Split line into its component parts
            String[] line = mapCode[i].split("/");
            System.out.println("line found");
            // If the line is a room
            if (line[0].startsWith("-")) {
                System.out.println("adding item");
                // Get the name of the item and remove the -
                String itemName = line[0].replace("-","");
                // If the item is in the itemLibrary
                for (int a = 0; a < itemLibrary.size(); a++) {
                    if (itemLibrary.get(a).getName().equals(itemName)) {
                        // Add the item from the item library to the current room  
                        currentRoom.add(itemLibrary.get(a));
                        // Load the attributes from the file
                        addAttributes(currentRoom.getItems().get(currentRoom.getItems().size()-1), line);
                    }
                }
                System.out.println("item added to room");
                // else if the line is an item in a container
            } else if (line[0].startsWith("*")) {
                System.out.println("adding item to container");
                // Remove the star
                line[0] = line[0].replace("*","");
                // Get the name of the item and the container
                String[] items = line[0].split("<");
                // If the container is in the room
                if (itemReferencePresent(currentRoom.getItems(), items[0], false)!=null) {
                    // Set the container as the current container
                    Item currentContainer = itemReferencePresent(currentRoom.getItems(), items[0], false);
                    //If the item is in the itemLibrary
                    for (int b = 0; b < itemLibrary.size(); b++) {
                        if (itemLibrary.get(b).getName().equals(items[1])) {
                            // Add the item from the item library to the current room  
                            ((Container) currentContainer).add(itemLibrary.get(b));
                            // Load the attributes from the file
                            addAttributes(((Container) currentContainer).getItems().get(((Container) currentContainer).getItems().size()-1), line);
                        }
                    }
                } 
                System.out.println("item added to container");
            } else if (line[0].startsWith("+")) {
                System.out.println("adding item to inventory");
                // Get the name of the item and remove the +
                String itemName = line[0].replace("+","");
                System.out.println("Checking if item is in item library");
                // If the item is in the itemLibrary
                for (Item item: itemLibrary) {
                    if (item.getName().equals(itemName)) {
                        System.out.println("in library");
                        // Add the item from the item library to the inventory
                        player.add(item);
                        System.out.println(item.getName());
                        // Load the attributes from the file
                        addAttributes(player.getItems().get(player.getItems().size()-1), line);
                        for (Item printItem: player.getItems()) {
                            System.out.print(printItem.getName()+" ");
                        }
                    }
                }
                // else if the line is an item in a container
            } else if (line[0].startsWith("#")) {
                System.out.println("add item");
                // Remove the #
                line[0] = line[0].replace("#","");
                // Get the name of the item and the container
                String[] items = line[0].split("<");
                // If the container is in the player's inventory
                if (itemReferencePresent(player.getItems(), items[0], false)!=null) {
                    // Set the container as the current container
                    Item currentContainer = itemReferencePresent(player.getItems(), items[0], false);
                    //If the item is in the itemLibrary
                    for (int b = 0; b < itemLibrary.size(); b++) {
                        if (itemLibrary.get(b).getName().equals(items[1])) {
                            // Add the item from the item library to the current room  
                            ((Container) currentContainer).add(itemLibrary.get(b));
                            // Load the attributes from the file
                            addAttributes(((Container) currentContainer).getItems().get(((Container) currentContainer).getItems().size()-1), line);
                        }
                    }
                } 
            } else {
                System.out.println("adding room");
                // Set the current room to the line
                currentRoomName = line[0];
                // For every room in the game grid
                for (int y   = 0; y<gameGrid[0].length; y++) {
                    for (int x = 0; x<gameGrid.length; x++) {
                        // If the room name matches the current room name
                        if (gameGrid[x][y] != null) {
                            if (currentRoomName.equals(gameGrid[x][y].name)) {
                                // Set the current room to the room in te game grid
                                currentRoom = gameGrid[x][y];
                                // Add whether room has been visited
                                gameGrid[x][y].visited = Boolean.parseBoolean(line[2]);
                            }
                        }
                    }
                }
            }
            System.out.println("Map loaded");
        }
    }
    
    // Check if an item is present within a list
    private Item itemReferencePresent(ArrayList<Item> items, String item, boolean remove) {
        // Initialise the returned item as null
        Item success = null;
        // If the item is present in the given arrayList
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item)) {
                // Return the item
                success = items.get(i);
                if (remove == true) {
                    items.remove(i);
                }
                break;
                // Otherwise for every present container which contains items
            } else if (items.get(i) instanceof Container && ((Container) items.get(i)).getItems().size() > 0) {
                // Return a search of its items
                success = ((Container) items.get(i)).loadCheckItem(item, remove);
                // If it found the item being checked for, break the loop
                if (success != null) {
                    break;
                }
            }
        }
        return success;
    }
    
    private void addAttributes(Item newItem, String[] line) {
        if (line[0].contains("<")) {
            newItem.setName(line[0].replace("-","").substring(line[0].lastIndexOf("<") + 1));
        } else if (line[0].contains("-")) {
            newItem.setName(line[0].replace("-",""));
        } else {
            newItem.setName(line[0].replace("+",""));
        }
        newItem.description = line[1];
        newItem.setWeight(Double.parseDouble(line[2]));
        newItem.setSize(Integer.parseInt(line[3]));
        newItem.visible = Boolean.parseBoolean(line[4]);
        if (newItem instanceof Container) {
            ((Container) newItem).setLocked(Boolean.parseBoolean(line[5]));
        }
    }
    
    // Clear every object from the map
    private void clearMap(Room[][] gameGrid) {
        System.out.println("Map.clearMap");
        // For every space in the map grid
        for (int y = 0; y < gameGrid[0].length; y++) {
            for (int x = 0; x < gameGrid.length; x++) {
                System.out.println("co-ordinate selected");
                // if a room is present
                if (gameGrid[x][y] != null) {
                    // Clear the items from the room
                    gameGrid[x][y].empty();
                    System.out.println("Room cleared");
                }
            }
        }
        System.out.println("Rooms cleared");
        // For every object in the itemLibrary
        for (int i = 0; i < itemLibrary.size(); i++) {
            // If its a container
            if (itemLibrary.get(i) instanceof Container) {
                // Clear its contents
                ((Container) (itemLibrary.get(i))).empty();
            }
        }
        System.out.println("item library cleared");
    }
    
    // Add the item to the room and library of all items
    private void populate(Item item, Room room) {
        // Add the item to the room
        room.add(item);
        // If the item isn't already present in the Library of items
        if (!itemLibrary.contains(item)) {
            // Add the item to the library of items
            itemLibrary.add(item);
        }
    }
    
    
    private void fill(Item item, Item container) {
        // Add the item to the container
        ((Container) container).add(item);
        // Make the item hidden
        item.visible = false;
        // If the item isn't already present in the Library of items
        if (!itemLibrary.contains(item)) {
            // Add the item to the library of items
            itemLibrary.add(item);
        }
    }
}