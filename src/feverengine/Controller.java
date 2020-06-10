package feverengine;

/* A class to take the user input and work out what method to call in the model or
 * the output */
 
public class Controller
{
    
    // Establish the parser
    Parser parser = new Parser();
    
    // Establish controller's variables
    String action;
    String object;
    String tool;
    String[] inputArray;
    Model model;
    Output output;
    // Set the state of the controller, 0 is normal, 1 is yes/no
    // 2 is save game
    int state = 0;
    boolean yesNoBool = false;
    
    // Get the user input for the next move
    public void getInput(String input){
        System.out.println("controller activated, state: "+state);
        System.out.println(state);
        // If the controller state is normal
        switch (state) {      
            case 0: // Standardise the input and increase program-readability before splitting it in to an
                    // array
                    inputArray = parser.parse(input).split(" ",-1);
                    
                    
                    //------------------------------------------- Playing State
                    
                    System.out.println("Case 0");
                    // Check parsed input for commands
        
                    
                    //------------------------------------------ Player actions
                    
                    // If the user says go
                    if (inputArray[0].equals("go")) {
                    // Check they give a direction
                    if (inputArray.length > 1){
                        // Move the player
                        model.movePlayer(inputArray[1]);
                        // Update the game model
                        System.out.println("updating model");
                        model.update();
                    } else {
                        // Warn them if they haven't given a direction
                        output.unknownDirection();
                    }
                } else if (inputArray[0].equals("wait")) {
                    // Wait
                    model.waitPlayer();
                    // Update the game model
                    System.out.println("updating model");
                    model.update();
                }
                // Search a container
                else if (inputArray[0].equals("search")) {
                    // if the user has given a container
                    if (inputArray.length > 1){
                        // search the container
                        model.searchContainer(inputArray[1]);
                        // Update the game model
                        System.out.println("updating model");
                        model.update();
                    } else {
                        // otherwise ask for the container
                        output.unknownSearch();
                    }
                }
                // If the user wants to take an item
                else if (inputArray[0].equals("take")) {
                    // If they specifity what item
                    if (inputArray.length > 1){
                        // Take the item
                        model.takeItem(inputArray[1]);
                        // Update the game model
                        System.out.println("updating model");
                        model.update();
                    } else {
                        // Warn them if they haven't specified what to pick up
                        output.noItemPickUp();
                    }
                }
                // If the user wants to eat an item
                else if (inputArray[0].equals("eat")) {
                    // If they specify what item
                    if (inputArray.length > 1){
                        // Eat the item
                        model.eatItem(inputArray[1]);
                        // Update the game model
                        System.out.println("updating model");
                        model.update();
                    } else {
                        // Warn them if they haven't specified what to eat
                        output.noItemEat();
                    }
                }  
                // If the user wants to drop an item
                else if (inputArray[0].equals("drop")) {
                    // If they specify what item
                    // If they only specify the item
                    if (inputArray.length == 2){
                        // drop the item in the room
                        model.dropItem(inputArray[1]);
                        // Update the game model
                        System.out.println("updating model");
                        model.update();
                        // If they specify the item and the location to drop it in
                    } else if (inputArray.length == 3) {
                        // drop the item
                        model.dropItemIn(inputArray[1], inputArray[2]);
                        // Update the game model
                        System.out.println("updating model");
                        model.update();
                    } else {
                        // Warn them if they haven't specified what to drop
                        output.noItemDrop();
                    }
                }
                else if (inputArray[0].equals("open")) {
                    model.open(inputArray[1],inputArray[3]);
                    // Update the game model
                    System.out.println("updating model");
                    model.update();
                }
                // If the user wants to look at something
                else if (inputArray[0].equals("look")) {
                    // If the user specifies what to look at
                    if (inputArray.length > 1){
                        // Look at the object
                        model.lookAt(inputArray[1]);
                        // Update the game model
                        System.out.println("updating model");
                        model.update();
                    }
                    else {
                        // Otherwise look at the room
                        model.look();
                        // Update the game model
                        System.out.println("updating model");
                        model.update();
                    }
                }  else if (inputArray[0].equals("inventory")) {
                    output.checkInventory();
                    // Update the game model
                    System.out.println("updating model");
                    model.update();
                }
                
                
                //------------------------------------------------Game Functions
                
                else if (inputArray[0].equals("quit")) {
                    model.quit();
                }   
                else if (inputArray[0].equals("save")) {
                    output.getSaveName();
                }
                else if (inputArray[0].equals("load")) {
                    output.getLoadName();
                }
                else if (inputArray[0].equals("help")) {
                    output.help();
                } 
                
                
                
                //-----------------------------------------------Developer Commands
                
                else if (inputArray[0].equals("cmd")) {
                    if (inputArray.length > 1) {
                        System.out.println("***CMD***");
                        if (inputArray[1].equals("toggleitemareas")) {
                            output.view.toggleItemAreas();
                            output.toggleItemAreas();
                        } else if (inputArray[1].equals("togglelights")) {
                        output.view.toggleLights();
                        output.toggleLights();
                        } else if (inputArray[1].equals("go") && inputArray.length > 2) {
                            if (inputArray[2].equals("items")) {
                                output.view.toggleMoveItems();
                                output.toggleMoveItems();
                                System.out.println("Command enetered - move");
                            }
                        }
                    }
                }
                
                
                
                //----------------------------------------------No command
                
                else {
                    output.unknownCommand();
                }
                break;
                
                
                
            //---------------------------------------------------Yes/No State
                
                
            case 1: System.out.println("Getting yes or no");
                    System.out.println("Case 1");
                    // Standardise the input
                    String[] yesNo = parser.parse(input).split(" ");
                    // if yes
                    if (yesNo[0].equals("y")) {
                        System.out.println("yes selected, fowarding task");
                        // Return to the normal state
                        state = 0;
                        // set the yes value to true
                        yesNoBool = true;
                        // forward the task to the output
                        output.forwardTask();
                        // else if no
                    } else if (yesNo[0].equals("north")){
                        System.out.println("no selected, fowarding task");
                        // Return to the normal state
                        state = 0;
                        // set the yes value to false
                        yesNoBool = false;
                        // forward the task to the output
                        output.forwardTask();
                        // else if not recognised
                    } else {
                        // ask again
                        output.incorrectInput();
                    }
                    break;
                    
                    
                    
            //--------------------------------------------------Save Game State
                    
            case 2: System.out.println("getting save name");
                    System.out.println("Case 2");
                    // Return to the normal state
                    state = 0;                        // Save the game
                    model.saveGame(input);
                    break;
                    
                    
            //--------------------------------------------------Load Game State
                  
            case 3: System.out.println("getting load name");
                    System.out.println("Case 3");
                    // Return to the normal state
                    state = 0;
                    // Save the game
                    model.loadGame(input);
                    break;
        }
    }
}