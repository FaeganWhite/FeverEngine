package feverengine;

import java.io.PrintWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

/**
* The file class is designed to read and write data to external files in order to save and 
* load the game.
*
* Faegan White
* 
*/
public class FileControl
{
    // Initialise the save code
    String saveCode = new String();
    // Initialise the game map
    Map map;
    // Initialise the game map
    Player player;
    // Initialise the model
    Model model;
    // Establish the controller
    Controller controller = new Controller();
    // Establish the output
    Output output;
    String currentName;
    
    int futureFunction = 0;
    
    // Set the model
    public FileControl(Model newModel, Map newMap, Player newPlayer) {
        model = newModel;
        map = newMap;
        player = newPlayer;
    }
    
    // Create a save file
    public void newFile(String name) {
        System.out.println("creating a file");
        // Establish the file
        File f = new File("src/feverengine/saves/"+name+".txt");
        // save the intended name of the new file
        currentName = name;
        // If the file already exists
        if (f.exists() && !f.isDirectory()) {
            System.out.println("exists, check overwrite");
            output.checkOverwrite();
        } else {
            System.out.println("doesn't exist, will save");
            saveCurrentGame();
        }
    }
   
    // Load the game from a file
    public void getFile(String name) {
        // save the intended name of the new file
        currentName = name;
        System.out.println("file.getFile");
        // Establish the file
        File f = new File("src/feverengine/saves/"+name+".txt");
        // Save the current name used for the load
        currentName = name;
        // If the file exists
        if (f.exists() && !f.isDirectory()) {
            System.out.println("file exists");
            loadGame(name);
        } else {
            System.out.println("file doesnt exist");
            output.fileDoesNotExist();
        }
    }
    
    public void saveCurrentGame() {
        System.out.println("saving the 'current' game");
        saveGame(currentName);
    }
    
    private void saveGame(String name) {
        System.out.println("saving the game");
        try {
            // Create a new file with users file name
            PrintWriter writer = new PrintWriter("src/feverengine/saves/"+name+".txt", "UTF-8");
            System.out.println("file created");
            // add the map dimentions to save file
            writer.println(map.gameGrid.length + "/" + map.gameGrid[0].length + "/" + model.getTime());
            System.out.println("adding dimentions");
            // For every room in the game grid
            for (int y = 0; y<map.gameGrid[0].length; y++) {
                for (int x = 0; x<map.gameGrid.length; x++) {
                    System.out.println("adding room data");
                    // Get the room
                    Room room = map.gameGrid[x][y];
                    // if the room is present
                    if (room != null) {
                        // Get the details of the items in the room
                        System.out.println("get room");
                        writer.println(room.getSaveInfo());
                        System.out.println("adding room data to document");
                    }
                }
            }
            System.out.println("map data added");
            // For every item in the inventory
            for (Item item: player.getItems()) {
                if (item!=null) {
                    // Add the details of each item
                    writer.println("+"+item.getInvSaveInfo());
                }
            }
            // Log the player info
            writer.println(player.getName()+"/"+player.getMaxHealth()+"/"+player.getHealth()+"/"+player.getX()+"/"+player.getY()+"/"+player.getStrength());
            System.out.println("log player data");
            writer.close();
            // Inform the user of a successful save
            System.out.println("Save successfull");
            output.saveSuccessful();
            // Inform the user of their location
            model.look();
        }
        catch(Exception e) {
            // Inform the user the save wasn't successful
            System.out.println("Save unsuccessful");
            // check if they want to try again
            output.checkSaveTryAgain();
        }
    }
    
    private void loadGame(String name) {
        System.out.println("Loading game");
        try {
            System.out.println("Getting byte array");
            // Get the string from the text file
            byte[] encoded = Files.readAllBytes(Paths.get("src/feverengine/saves/"+name+".txt"));
            System.out.println("Got byte array");
            String input = new String(encoded, StandardCharsets.UTF_8);
            System.out.println("converted to string");
            // Split the string in to an array on each new line
            String[] loadArray = input.split("\n");
            System.out.println("Get the string");
            // Get the player info from the file
            String[] stringPlayer = loadArray[loadArray.length-1].split("/");
            System.out.println("Get the player info. Array size: "+stringPlayer.length);
            // Get the new player x
            int x = Integer.parseInt(stringPlayer[3]);
            // Get the new player y
            int y = Integer.parseInt(stringPlayer[4]);
            System.out.println("Get the dimentions");
            // Get the new player health
            int health = Integer.parseInt(stringPlayer[2]);
            // Get the new player max health
            int maxHealth = Integer.parseInt(stringPlayer[1]);
            // remove the character on the end of the string and set the player strength
            String getStrength = stringPlayer[5].trim();
            int strength = Integer.parseInt(getStrength);
            System.out.println("got player info");
            // Set the players new position
            model.loadPlayer(stringPlayer[0], maxHealth, health, x, y, strength);
            // Set the map's player to the player
            map.player = player;
            System.out.println("Loading player info 2");
            // Set the game time
            String[] gameVariables = loadArray[0].split("/");
            model.setTime(Integer.parseInt(gameVariables[2].trim()));
            // Load the map from the file data
            map.loadMap(loadArray);
            System.out.println("load map info");
            // Inform the user the file has been successfully loaded
            System.out.println("Loading successful\n");
            output.loadSuccessful();
            // Inform the user of their location
            model.look();
        } catch (Exception e) {
            System.out.println("Load failed");
            output.checkLoadTryAgain();
        } 
    }
}
