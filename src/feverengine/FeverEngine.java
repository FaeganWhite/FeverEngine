/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feverengine;

import javafx.application.Application;
import javafx.stage.Stage;

public class FeverEngine extends Application
{
    public static void main( String args[] ) {
        // Only used when launching from the command line
        launch(args);
    }
    
    
    @Override
    public void start(Stage window) {
       
       // Establish the map
       Map map1 = new Map();
        
       // Establish the controller
       Controller controller = new Controller();
       
       // Establish the output (view)
       Output output = new Output(controller);
       
       // Establish the model - linking it to the controller and output previously created
       Model model = new Model(output, controller, map1);
       controller.model = model;
       controller.output = output;
       
       // Establish the view for the game
       View view = new View();
       view.controller = controller;
       output.view = view;
       view.model = model;
       
       // set up the player location and map
       model.setPlayerLocation();
       
       // Start the view
       view.start(window);
       
       // Start the game
       model.startGame();
    }
}
