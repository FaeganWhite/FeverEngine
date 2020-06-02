package feverengine;

import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 * Write a description of class Output here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Output
{
    // Establish the classes
    Map map;
    Controller controller;
    Player player;
    View view;
    FileControl file;
    
    // Establish the method to follow when forwarding a task
    int futureFunction = 0;
    
    // On creation, set the controller to the game controller
    public Output(Controller setController){
        controller = setController;
    }
    
    // Find the method to follow when called by the controller in 
    // the next input cycle
    public void forwardTask(){
        System.out.println("in output, forwarding tasks");
        switch (futureFunction) {
            case 0: quit();
                    break;
            case 1: System.out.println("ff1, save game selected");
                    saveGame();
                    break;
            case 2: System.out.println("ff2, get save name selected");
                    getSaveName();
                    break;
            case 3: System.out.println("ff3, get load name selected");
                    loadGame(); 
                    break;
        }
    }
    
    // Describe the room to the user
    public void describeRoom(Room room,ArrayList<String> surroundingsList){
        // Print the description of the room
        view.printResponse("You are in the ", Color.WHITE, true);
        view.printResponse(room.name, Color.AQUA, false);
        view.printResponse(", "+room.description, Color.WHITE, false);
        // Get the list of items in the room from the room object
        ArrayList<Item> items = room.getItems();
        // For every door in the room
        for (int i = 0; i < surroundingsList.size(); i+=2) {
            // If it's the first door
            if (i == 0) {
                // Tell the player the direction and the room 
                view.printResponse("To the ", Color.WHITE, false);
                view.printResponse(surroundingsList.get(i), Color.MAGENTA, false);
                view.printResponse(" is the ", Color.WHITE, false);
                view.printResponse(surroundingsList.get(i+1), Color.AQUA, false);
            // otherwise if it's at the end of the list
            } else if (i == (surroundingsList.size()-2)) {
                // tell the player the direction and the room
                view.printResponse(" and ", Color.WHITE, false);
                view.printResponse(surroundingsList.get(i), Color.MAGENTA, false);
                view.printResponse(" is the ", Color.WHITE, false);
                view.printResponse(surroundingsList.get(i+1), Color.AQUA, false);
            } else {
                // otherwise if it's in the middle of the list, print the direction and the room
                view.printResponse(surroundingsList.get(i), Color.MAGENTA, false);
                view.printResponse(" is the ", Color.WHITE, false);
                view.printResponse(surroundingsList.get(i+1), Color.AQUA, false);
            }
            // if in the middle of the list
            if (i != (surroundingsList.size()-2) && i != (surroundingsList.size()-4)) {
                // print a comma
                view.printResponse(", ", Color.WHITE, false);
            // otherwise if at the end of the list
            } else if (i == surroundingsList.size()-2) {
                // print a full stop
                view.printResponse(".", Color.WHITE, false);
            }
        }
        // If there are more than one objects in the room
        if (items.size() > 1) {
            view.printResponse("In this room there is ", Color.WHITE, true);
            // List each object in the room
            listItems(items);  
        } 
        // If there is only one object
        else if (items.size() == 1) {
            // Print the object
            view.printResponse("In this room there is a ", Color.WHITE, true);
            view.printResponse(items.get(0).getName(), Color.YELLOW, false);
            view.printResponse(".", Color.WHITE, false);
        }
    }
    
    // List the items in the player's inventory
    public void checkInventory(){
        view.printResponse("You are carrying ", Color.WHITE, true);
        // If the inventory is empty
        if (player.getItems().isEmpty()){
            // Say it's empty
            view.printResponse("nothing.", Color.YELLOW, false);
        } else {
            listItems(player.getItems());
        }
    }
    
    // List the items in the player's inventory
    public void incorrectInput(){
        // Send the output to the view
        view.printResponse("Please respond with ", Color.WHITE, true);
        view.printResponse("(y/n)", Color.RED, false);
    }
    
    // End the game for the user when they quit
    public void checkQuit(){
        // Print the question
        view.printResponse("Are you sure you want to quit? ", Color.WHITE, true);
        view.printResponse("(y/n)", Color.RED, false);
        // Make the controller in the yes/no state
        controller.state = 1;
        // Set the function to quit in the next user input cycle
        futureFunction = 0;
    }
    
    // End the game for the user when they quit
    public void quit(){
        if (controller.yesNoBool == true) {
            System.exit(0);
        } else {
            controller.model.look();
        }
    }
          
    // Get the name for the game save
    public void getSaveName(){
        System.out.println("asking for save name");
        // Print the question
        view.printResponse("What would you like to name your ", Color.WHITE, true);
        view.printResponse("save", Color.RED, false);
        view.printResponse("?", Color.WHITE, false);
        // Set the controller state to get the saves name
        controller.state = 2;
    }
    
    // Get the name for the game save
    public void getLoadName(){
        System.out.println("asking for load name");
        // Print the question
        view.printResponse("What is the name of the file you would like to ", Color.WHITE, true);
        view.printResponse("load", Color.RED, false);
        view.printResponse("?", Color.WHITE, false);
        // Set the controller state to get the saves name
        controller.state = 3;
    }
    
    // Get the name for the game save
    public void help(){
        System.out.println("printing help");
        // Print the help
        view.printResponse("Welcome to FeverEngine v1.0, here is a list of commands that can be used to interact with the console...", Color.LIGHTGRAY, true);
        view.printResponse("GO", Color.MAGENTA, true);
        view.printResponse(" (", Color.LIGHTGRAY, false);
        view.printResponse("NORTH", Color.LIGHTGREEN, false);
        view.printResponse("/", Color.LIGHTGRAY, false);
        view.printResponse("SOUTH", Color.LIGHTGREEN, false);
        view.printResponse("/", Color.LIGHTGRAY, false);
        view.printResponse("EAST", Color.LIGHTGREEN, false);
        view.printResponse("/", Color.LIGHTGRAY, false);
        view.printResponse("WEST", Color.LIGHTGREEN, false);
        view.printResponse(") > MOVE IN A DIRECTION", Color.LIGHTGRAY, false);
        view.printResponse("WAIT", Color.MAGENTA, true);
        view.printResponse(" > WAIT FOR A TURN", Color.LIGHTGRAY, false);
        view.printResponse("SEARCH", Color.MAGENTA, true);
        view.printResponse(" X", Color.YELLOW, false);
        view.printResponse(" > SEARCH OBJECT ", Color.LIGHTGRAY, false);
        view.printResponse("X", Color.YELLOW, false);
        view.printResponse("TAKE", Color.MAGENTA, true);
        view.printResponse(" X", Color.YELLOW, false);
        view.printResponse(" > PICK UP OBJECT ", Color.LIGHTGRAY, false);
        view.printResponse("X", Color.YELLOW, false);
        view.printResponse("DROP", Color.MAGENTA, true);
        view.printResponse(" X", Color.YELLOW, false);
        view.printResponse(" > DROP OBJECT ", Color.LIGHTGRAY, false);
        view.printResponse("X ", Color.YELLOW, false);
        view.printResponse("FROM INVENTORY", Color.LIGHTGRAY, false);
        view.printResponse("EAT", Color.MAGENTA, true);
        view.printResponse(" X", Color.YELLOW, false);
        view.printResponse(" > EAT OBJECT ", Color.LIGHTGRAY, false);
        view.printResponse("X", Color.YELLOW, false);
        view.printResponse("OPEN", Color.MAGENTA, true);
        view.printResponse(" X", Color.YELLOW, false);
        view.printResponse(" > UNLOCK OBJECT ", Color.LIGHTGRAY, false);
        view.printResponse("X", Color.YELLOW, false);
        view.printResponse("LOOK", Color.MAGENTA, true);
        view.printResponse(" (AT ", Color.LIGHTGRAY, false);
        view.printResponse("X", Color.YELLOW, false);
        view.printResponse(")", Color.LIGHTGRAY, false);
        view.printResponse(" > LOOK AT SURROUNDINGS (OR OBJECT ", Color.LIGHTGRAY, false);
        view.printResponse("X", Color.YELLOW, false);
        view.printResponse(")", Color.LIGHTGRAY, false);
        view.printResponse("INVENTORY", Color.MAGENTA, true);
        view.printResponse(" > LIST THE ITEMS IN THE INVENTORY", Color.LIGHTGRAY, false);
        view.printResponse("QUIT", Color.MAGENTA, true);
        view.printResponse(" > QUIT THE GAME", Color.LIGHTGRAY, false);
        view.printResponse("SAVE", Color.MAGENTA, true);
        view.printResponse(" > SAVE THE GAME", Color.LIGHTGRAY, false);
        view.printResponse("LOAD", Color.MAGENTA, true);
        view.printResponse(" > LOAD THE GAME", Color.LIGHTGRAY, false);
        view.printResponse("HELP", Color.MAGENTA, true);
        view.printResponse(" > LIST THE GAME COMMANDS", Color.LIGHTGRAY, false);
    }
    
    // load the game or return to game
    public void saveGame(){
        System.out.println("Save game method");
        if (controller.yesNoBool == true) {
            try {
                System.out.println("overwrite yes, saving game");
                file.saveCurrentGame();
            } catch (Exception e) {
                view.printResponse("Saving unsuccessful", Color.RED, false); 
            }
        } else {
            System.out.println("overwrite no, looking");
            controller.model.look();
        }
    }
    
        // load the game or return to game
    public void loadGame(){
        System.out.println("Load game method");
        if (controller.yesNoBool == true) {
            System.out.println("asking for load name");
            // Print the question
            view.printResponse("What is the name of the file you would like to ", Color.WHITE, true);
            view.printResponse("load", Color.RED, false);
            view.printResponse("?", Color.WHITE, false);
            // Set the controller state to get the saves name
            controller.state = 3;
        } else {
            System.out.println("overwrite no, looking");
            controller.model.look();
        }
    }
    
    // Tell the user the game saved succesfully
    public void saveSuccessful() {
        view.printResponse("Save successful\n", Color.WHITE, true);
    }
    
    // Tell the user the game saved succesfully
    public void loadSuccessful() {
        view.printResponse("Load successful\n", Color.WHITE, true);
        view.loadUpdateGUI();
    }
    
    // End the game for the user when they quit
    public void checkOverwrite(){
        System.out.println("checking if should overwrite");
        // Print the question
        view.printResponse("This save already exists, are you sure you want to overide? ", Color.WHITE, true);
        view.printResponse("(y/n)", Color.RED, false);
        // Set the controller to the yes/no state 
        controller.state = 1;
        // Set the next input cycle to save the game
        futureFunction = 1;
    }
    
    // End the game for the user when they quit
    public void checkSaveTryAgain(){
        System.out.println("checking if should try again");
        // Print the question
        view.printResponse("The save was unsuccessful, would you like to try again? ", Color.WHITE, true);
        view.printResponse("(y/n)", Color.RED, false);
        // Set the controller to the yes/no state 
        controller.state = 1;
        // Set the next input cycle to save the game
        futureFunction = 2;
    }
    
    // End the game for the user when they quit
    public void fileDoesNotExist(){
        // Print the question
        view.printResponse("This file cannot be found, would you like to try again? ", Color.WHITE, true);
        view.printResponse("(y/n)", Color.RED, false);
        // Set the controller to the yes/no state 
        controller.state = 1;
        // Set the next input cycle to save the game
        futureFunction = 3;
    }
    
    // End the game for the user when they quit
    public void checkLoadTryAgain(){
        System.out.println("checking if should try again");
        // Print the question
        view.printResponse("The load was unsuccessful, would you like to try again? ", Color.WHITE, true);
        view.printResponse("(y/n)", Color.RED, false);
        // Set the controller to the yes/no state 
        controller.state = 1;
        // Set the next input cycle to save the game
        futureFunction = 3;
    }
    
    // Alert the user that the direction they entered wasn't in the library
    public void unknownDirection(){
        view.printResponse("I don't understand that direction.", Color.WHITE, true);
    }
    
    // Alert the user that they cant move in the desired direction
    public void cantMove(){
        view.printResponse("You cannot move in this direction.", Color.WHITE, true);
    }
    
    // List items from array
    public void listItems(ArrayList<Item> items) {
        // For every item in the list
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i)!=null) {
                // print the item
                view.printResponse("a ", Color.WHITE, false);
                view.printResponse(items.get(i).getName(), Color.YELLOW, false);
                // Add a comma if listing items
                if (i < items.size()-2){
                    view.printResponse(", ", Color.WHITE, false);
                }
                // Add an 'and' if it's the last item in the list
                if (items.size()-3 < i && i < items.size()-1){
                    view.printResponse(" and ", Color.WHITE, false);
                }
            }
        }
        view.printResponse(".", Color.WHITE, false);
    }
    
    // Draw a text based map to aid in navigation and debugging
    public void drawMap(Map newMap,int newX,int newY){
        map = newMap;
        int x = newX;
        int y = newY;
        for (int b = 0; b < map.gameGrid[0].length; b++) {
            for (int a = 0; a < map.gameGrid.length; a++) {
                if (a == x && y == b) {
                    view.printResponse("x", Color.WHITE, true);
                }
                else {
                    view.printResponse(".", Color.WHITE, true);
                }
            }
            view.printResponse("", Color.WHITE, true);
        }
    }
    
    // Inform the user they successfully picked up an item
    public void itemTaken(String name){
        view.printResponse("You picked up the ", Color.WHITE, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(".", Color.WHITE, false);
    }
    
    // Inform the user they successfully dropped up an item
    public void itemDropped(String name){
        view.printResponse("You dropped the ", Color.WHITE, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(".", Color.WHITE, false);
    }
    
    // Inform the user they successfully put an item in container
    public void itemDroppedIn(String name, String container){
        view.printResponse("You put the ", Color.WHITE, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(" in the ", Color.WHITE, false);
        view.printResponse(container, Color.YELLOW, false);
        view.printResponse(".", Color.WHITE, false);
    }
    
    // Inform the user they successfully dropped up an item
    public void itemEaten(String name){
        view.printResponse("You ate the ", Color.WHITE, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(".", Color.WHITE, false);
    }
    
    // Alert the user that the item they entered wasn't in the library
    public void itemPickFail(String name){
        view.printResponse("There is no ", Color.WHITE, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(" here.", Color.WHITE, false);
    }
    
    // Alert the user that the item they entered wasn't in the library
    public void itemCarryFail(String name){
        view.printResponse("You aren't carrying a ", Color.WHITE, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(".", Color.WHITE, false);
    }
    
    // Alert the user that the item they entered wasn't in the library
    public void itemSeeFail(String name){
        view.printResponse("I can't see a ", Color.WHITE, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(" here.", Color.WHITE, false);
    }
    
    // Alert the user that they didn't enter an item
    public void noItemPickUp(){
        view.printResponse("What you you like to pick up?", Color.WHITE, true);
    }
    
    // Alert the user that they didn't enter an item
    public void noItemDrop(){
        view.printResponse("What you you like to drop?", Color.WHITE, true);
    }
    
    // Alert the user that they didn't enter an item
    public void noItemEat(){
        view.printResponse("What you you like to eat?", Color.WHITE, true);
    }
    
    // Alert the user that they didn't enter an item
    public void youCant(String verb){
        view.printResponse("You can't " + verb +" that!", Color.WHITE, true);
    }
    
    // Alert the user that they didn't enter an item
    public void alreadyCarrying(String noun){
        view.printResponse("You are already carrying the ", Color.WHITE, true);
        view.printResponse(noun, Color.YELLOW, false);
        view.printResponse(".", Color.WHITE, false);
    }
    
    // Alert the user that they didn't enter an item
    public void unknownSearch(){
        view.printResponse("What would you like to search?", Color.WHITE, true);
    }
    
    // List the items in the search
    public void search(ArrayList<Item> items, String container){
        // If there are items in the container
        if (items.size() > 0) {
            // print the items in the room
            view.printResponse("In the ", Color.WHITE, true);
            view.printResponse(container, Color.YELLOW, false);
            view.printResponse(" there is ", Color.WHITE, false);
            listItems(items);
        } else {
            // otherwise say the container is empty
            view.printResponse("The ", Color.WHITE, true);
            view.printResponse(container, Color.YELLOW, false);
            view.printResponse(" is empty.", Color.WHITE, false); 
        }
    }
    
    // Increase the health
    public void healthChange(int health){
        view.printResponse("Health ", Color.WHITE, true);
        view.printResponse("+" + health, Color.RED, false);
    }
    
    // Alert the user that the container isn't a container
    public void containerTypeFail(String container){
        view.printResponse("The ", Color.RED, true);
        view.printResponse(container, Color.YELLOW, false);
        view.printResponse(" can't hold anything.", Color.RED, false);
    }
    
    // Alert the user that the item they want to carry is too heavy to pick up
    public void itemTooHeavy(String name){
        view.printResponse("The ", Color.RED, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(" is too heavy to carry!", Color.RED, false);
    }
    
    // Alert the user that the item they want to carry is too heavy to pick up
    public void itemTooBig(String name, String container){
        view.printResponse("The ", Color.RED, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(" is too big to put in the ", Color.RED, false);
        view.printResponse(container, Color.YELLOW, false);
        view.printResponse(".", Color.RED, false);
    }
    
        // Alert the user that the item they want to carry is too heavy to pick up
    public void notEnoughRoom(String name){
        view.printResponse("There is not enough room in the ", Color.RED, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(".", Color.RED, false);
        
    }
    
    // Alert the user that the item they want to carry is too heavy to pcik up
    public void tooMuchWeight(String name){
        view.printResponse("You are carrying too much weight to carry the ", Color.RED, true);
        view.printResponse(name, Color.YELLOW, false);
        view.printResponse(".", Color.RED, false);
        
    }
    
    // Look at an item
    public void lookAt(Item item){
        view.printResponse(item.description, Color.WHITE, true);
    }
    
    // Wait
    public void waitPrint(){
        view.printResponse("You wait around for a little while.", Color.WHITE, true);
    }
    
    // Alert the user that they have died and the game is over
    public void gameOver(){
        view.printResponse("You died! Game over.", Color.RED, true);
    }
    
    // Tess the user that their input wasn't recognised
    public void unknownCommand() {
	view.printResponse("That input was not recognised!", Color.RED, true);
    }
}
