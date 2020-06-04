package feverengine;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;
import javafx.scene.transform.Scale;

public class View extends Application {
    
    public static void main(String[] args) {
        // Only used when launching from the command line
        launch(args);
    }
    
    
    
    //------------------------------------------------------ Initialise the game window
    
    // Establish the window sizes
    double gridSize;
    double gridMultiply;
    double widerGridSize;
    double UIHeight;
    double UIWidth;
    double scaleFactor = 0.9; // scale factor for the windowed window
    double screenWidth;
    double screenHeight;
    
    // Establish the player input
    String inputText = "";
    ArrayList<String> previousInputText = new ArrayList<>();
    // Establish whether the user is scrolling through past inputs
    boolean scroll = false;
    // Establish the scroll position
    int scrollPosition = 0;
    
    // Create grid to layout the interface
    public GridPane mainGrid = new GridPane();
    
    // Establish the text interface
    GridPane textGrid = new GridPane();
    HBox inputBox = new HBox();
    TextField input = new TextField();
    TextFlow outputTextFlow = new TextFlow();
    ScrollPane textScroll = new ScrollPane();
    
    // Create a grid to layout the sensor interface
    // public GridPane dataGrid = new GridPane();
    public VBox sensorVBox = new VBox();
    public VBox sensorTitleBox = new VBox();
    
    // Create a grid to layout the inventory interface
    public VBox inventoryBox = new VBox();
    
    // Create a grid to layout the map
    public GridPane mapGrid = new GridPane();
    
    // Establish the map
    public GridPane tvGrid = new GridPane();
    Canvas mapCanvas;
    
    // Establish the labels
    public Label textDisplay = new Label("");
    public Label invTitle = new Label("Inventory");
    public Label invList = new Label("");
    public Label mapTitle = new Label(String.valueOf("Map"));
    public Label sensorsLabel = new Label("Sensors");
    public Label clock = new Label("  Time: 0");
    public Label health = new Label("  Health: 0");
    public Label inputMarker = new Label(">");
    
    // Establish the images
    Image mainImage = new Image("feverengine/images/backgrounds/bathroom1.jpg");
    Image staticImage = new Image("feverengine/images/tv/staticStill.jpg");
    
    // Establish the main picture stack
    StackPane mainImageStack = new StackPane();
    ImageView mainImageView = new ImageView(mainImage);
    Canvas mainImageCanvas;
    
    // Establish the TV
    StackPane tvStack = new StackPane();
    ImageView tvView = new ImageView(staticImage);
    Canvas tvCanvas;
    
    // Establish the scene
    Scene mainScene;
    
    // Establish the canvas elements
    Canvas sensorCanvas;
    
    // Establish the connected classes
    Controller controller;
    Model model;
    Stage mainWindow;
    
    boolean fullscreen = true;
    
    // Developer variables
    boolean showItemAreas = false; // Should the item positions be highlighted in red?
    
    @Override
    public void start(Stage window) {
        System.out.println("Opening view");
        // Establish the window
        mainWindow = window;
        mainWindow.setFullScreen(true);
        // Get the screen dimentions
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = primaryScreenBounds.getWidth();
        screenHeight = primaryScreenBounds.getHeight();
        
        // set the UI dimentions
        setUIdimentions(screenWidth, screenHeight);
        // Set the canvases
        setCanvasses();
        
        // Establish the styling
        mainScene.getStylesheets().add("feverengine/css/stylesheet.css");
        input.setId("input");
        textGrid.setId("textGrid");
        textDisplay.setId("textDisplay");
        mainGrid.setId("mainGrid");
        mapGrid.getStyleClass().add("UIGrid");
        inventoryBox.getStyleClass().add("UIGrid");
        mainImageView.getStyleClass().add("UIGrid");
        textGrid.getStyleClass().add("UIGrid");
        invTitle.getStyleClass().add("heading");
        sensorsLabel.getStyleClass().add("heading");
        window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        sensorVBox.getStyleClass().add("UIGrid");
        sensorVBox.setId("dataGrid");
        mainScene.setCursor(Cursor.NONE);
        clock.getStyleClass().add("text");
        health.getStyleClass().add("text");
        inputMarker.getStyleClass().add("userText");
        tvGrid.getStyleClass().add("UIGrid");
        textScroll.setId("textScroll");
        inputBox.setId("inputBox");
        
        // Set the title on the window
        window.setTitle("FeverEngine v.1");
        
        // Set the properties of the the inventory box
        mapGrid.setAlignment(Pos.TOP_CENTER);
        inventoryBox.setAlignment(Pos.TOP_CENTER);
        
        // Set the properties of the text grid
        textGrid.setAlignment(Pos.BOTTOM_LEFT);
        textDisplay.setWrapText(true);
        textDisplay.setAlignment(Pos.TOP_LEFT);

        // Add elements to the main GUI grid
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.add(textGrid, 2, 2, 2, 1);
        mainGrid.add(inventoryBox, 4, 0, 1, 2);
        mainGrid.add(mapGrid, 1, 2, 1, 1);
        mainGrid.add(sensorVBox, 1, 0, 1, 2);
        mainGrid.add(mainImageStack, 2, 0, 2, 2);
        mainGrid.add(tvGrid, 4, 2, 1, 1);
        mainGrid.setGridLinesVisible(false);
        
        // Set the column and row dimensions
        setGridDimentions();
        
        // Construct the text interface
        textGrid.add(textScroll,0,0);
        textGrid.add(inputBox,0,1);
        // Add the the TextFlow to the ScrollPane
        textScroll.setContent(outputTextFlow);
        // Construct the input (comprised of a > symbol and the tex field
        inputBox.getChildren().add(inputMarker);
        inputBox.getChildren().add(input);
        // Show and hide the vertical and horizontal scroll bars
        textScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        textScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        // Listener to keep the scroll pane scrolled to the bottom 
        outputTextFlow.heightProperty().addListener((observable) -> textScroll.setVvalue(1.0));
        
        // Construct the map
        mapGrid.add(mapCanvas,0,0);
        
        // Construct the sensors
        sensorVBox.setAlignment(Pos.TOP_LEFT);
        sensorTitleBox.setAlignment(Pos.TOP_CENTER);
        sensorVBox.getChildren().add(sensorTitleBox);
        sensorVBox.setSpacing(gridSize/10);
        sensorTitleBox.getChildren().add(sensorsLabel);
        
        sensorVBox.getChildren().add(clock);
        sensorVBox.getChildren().add(health);
        sensorVBox.getChildren().add(sensorCanvas);
        clock.setTranslateX(gridSize/20);
        health.setTranslateX(gridSize/20);
        sensorCanvas.setTranslateX(gridSize/20);
        
        // Construct the inventory
        inventoryBox.getChildren().add(invTitle);
        inventoryBox.getChildren().add(invList);
        
        // Construct the tv
        tvGrid.add(tvStack,0,0);
        tvStack.getChildren().add(tvView);
        tvStack.getChildren().add(tvCanvas);
        addLines(tvCanvas);
        
        // Construct the main image
        mainImageStack.getChildren().add(mainImageView);
        mainImageStack.getChildren().add(mainImageCanvas);
        addLines(mainImageCanvas);
        addBorder(mainImageCanvas);
        
        // Set the action events
        input.setOnAction(this::inputPressEnter);
        input.setOnKeyReleased(this::escapePressed);
        input.setOnKeyPressed(this::keyPressed);
        
        // Update the UI
        updateUI();
        
        // Populate the window
        window.setScene(mainScene);
        // Size the window
        window.setMinWidth((screenHeight/3)*4*scaleFactor);
        window.setMinHeight(screenHeight*scaleFactor);
        //window.setPreserveRatio(true);
        window.show();
    }
    
        
    private void setGridDimentions() {
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(gridSize);
        col1.setMinWidth(gridSize);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        col1.setMinWidth(0);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(widerGridSize);
        col3.setMinWidth(widerGridSize);
        col3.setMaxWidth(widerGridSize);
        mainGrid.getColumnConstraints().addAll(col2, col1, col3, col3, col1, col2);
        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(gridSize);
        row1.setMaxHeight(gridSize);
        row1.setPrefHeight(gridSize);
        mainGrid.getRowConstraints().addAll(row1, row1, row1);
        
        // Set the dimentions
        input.setPrefWidth(widerGridSize*2);
        outputTextFlow.setPrefWidth((widerGridSize*2)*0.95);
        textScroll.setPrefHeight(gridSize);
        
        // Set the sensor title box width
        sensorTitleBox.setPrefWidth(gridSize);
        
        // Set the tv View dimentions
        tvView.setFitWidth(gridSize-2);
        tvView.setFitHeight(gridSize-2);
        
        // Set the image size
        mainImageView.setFitWidth(widerGridSize*2);
        mainImageView.setFitHeight(gridSize*2);
    }
    
    private void setUIdimentions(double width, double height) {
        gridSize = height/3;
        widerGridSize = Math.ceil(gridSize*(1.333333));
        UIHeight = (gridSize*3);
        UIWidth = gridSize*2+widerGridSize*2;
    }
    
    private void setCanvasses() {
        mainScene  = new Scene(mainGrid, UIWidth, UIHeight);
        sensorCanvas = new Canvas(gridSize, gridSize);
        tvCanvas = new Canvas(gridSize, gridSize);
        mapCanvas = new Canvas(gridSize, gridSize);
        mainImageCanvas = new Canvas(widerGridSize*2, gridSize*2);
    }
    
    
  
    
    //--------------------------------------------------------- Keypress Events ---/
    
    
    // Toggle fullscreen if ESC is pressed
    private void escapePressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            //toggleFullscreen();
        }
    }
    
    // Event for handling key pressing
    private void keyPressed(KeyEvent event) {
        // If up is pressed
        if (event.getCode() == KeyCode.UP) {
            // Scroll up
            textScroll.setVvalue(textScroll.getVvalue()-(35/outputTextFlow.getHeight()));
        } 
        // If down is pressed
        else if (event.getCode() == KeyCode.DOWN) {
            // Scroll down
            textScroll.setVvalue(textScroll.getVvalue()+(35/outputTextFlow.getHeight()));
        } 
        // If left is pressed
        else if (event.getCode() == KeyCode.LEFT) {
            // If the input is blannk
            if (input.getText().equals("")) {
                // set the input text to the previous input
                input.setText(previousInputText.get(previousInputText.size()-1));
                // move the input cursor to the end
                input.end();
                // set scrolling = true
                scroll = true;
            } else {
                // if the scroll will be within bounds
                if (scrollPosition < previousInputText.size()) {
                    // and scrolling is true
                    if (scroll == true) {
                        // move the scroll position along
                        scrollPosition += 1;
                        // set the input to the previous input at the scroll position
                        input.setText(previousInputText.get(previousInputText.size()-(1+scrollPosition)));
                        // move the cursor to the end
                        input.end();
                    }
                }
            }
        } 
        // if right arrow key is pressed
        else if (event.getCode() == KeyCode.RIGHT) {
            // if the input isn't blank
            if (!input.getText().equals("")) {
                // if scrolling is true
                if (scroll == true) {
                    // if the scroll will be within bounds
                    if (scrollPosition-1 >= 0) {
                        // move the scroll position along
                        scrollPosition -= 1;
                        // set the input to the previous input at the scrolling position
                        input.setText(previousInputText.get(previousInputText.size()-(1+scrollPosition)));
                        // move the cursor to the end
                        input.end();
                    } 
                    // otherwise if the move will be out of bounds
                    else {
                        // set the input text field to blank
                        input.setText("");
                        // set scrolling to false
                        scroll = false;
                    }
                } 
            }
        }
        if (event.getCode() != KeyCode.LEFT && event.getCode() != KeyCode.RIGHT) {
            scroll = false;
        }
    }

    // Print the user input and send it to the controller when
    // Enter is pressed
    private void inputPressEnter(ActionEvent event) {
        // Get the input text
        inputText = input.getText();
        // Set the previous input text to the new input text
        previousInputText.add(inputText);
        // Add the input text to the text flow
        Text inText = new Text("\n>"+inputText);
        inText.setFill(Color.web("0x41FF00"));
        inText.getStyleClass().add("text");
        
        // Add glow effect
        addTextGlow(inText, Color.LIME);
        
        outputTextFlow.getChildren().add(inText);
        // Run the controller class witht the inputted string
        controller.getInput(input.getText());
        // Clear the input
        input.clear();
        // Reset the scrolling position
        scrollPosition = 0;
        // update the UI
        updateUI();
    }
    
    
    
    
    //----------------------------------------------------------- Updating the graphics -----/
    
    // update GUI when a game is loaded
    public void loadUpdateGUI() {
        // update the UI
        updateUI();
    }
    
    private void updateUI() {
        // Update clock
        clock.setText(" Time: "+model.getTime());
        // Update health
        health.setText(" Health: "+model.player.getHealth());
        // Update inventory
        updateInventory();
        // Draw the map
        drawMap();
        // Draw the health bar
        drawHealthBar();
        // Add scan lines to the tv
        addLines(tvCanvas);
        // Update the main image view
        updateMainImage();
    }
    
    public void printResponse(String output, Color color, boolean newLine) {
        // Establish the output text
        Text outText;
        // Add a new line if selected and it isn't the first line in the game
        if (newLine == true && outputTextFlow.getChildren().size() > 0) {
            outText = new Text("\n"+output);
        } else {
            outText = new Text(output);
        }
        // Style text and add it to the text flow
        outText.setFill(color);
        outText.getStyleClass().add("text");
        addTextGlow(outText, color);
        outputTextFlow.getChildren().add(outText);
        System.out.println("finish print response method");
    }
    
    // Create the players inventory
    private void updateInventory() {
        // Empty the inventory list
        inventoryBox.getChildren().clear();
        // Add the title
        inventoryBox.getChildren().add(invTitle);
        inventoryBox.setSpacing(gridSize/15);
        // For every item in the inventory
        for (Item item: model.player.getItems()) {
            if (item!=null) {
                // Create  hbox to display the item name and image
                HBox hbox = new HBox();
                hbox.setSpacing(gridSize/10);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setTranslateX(gridSize/10);
                // Add it to the inventory window
                inventoryBox.getChildren().add(hbox);
                // Add the item name to the hbox
                Label itemLabel = new Label(item.getName());
                itemLabel.getStyleClass().add("inventoryText");
                hbox.getChildren().add(itemLabel);
                // Add the item image to the hbox
                hbox.getChildren().add(item.getItemImageView());
            
                // Move the item's image position to match that of the room co-ordinates
                item.getItemImageView().setTranslateX(0);
                item.getItemImageView().setTranslateY(0);     
            
                // Scale the item image according to its size and distance
                item.getItemImageView().setFitHeight(gridSize/4);
                item.getItemImageView().setPreserveRatio(true);
            }
        }
    }
    
    private void drawHealthBar() {
        GraphicsContext gc = sensorCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, sensorCanvas.getWidth(), sensorCanvas.getHeight());
        gc.setFill(Color.RED);
        gc.fillRect(0, 1, gridSize*(Double.valueOf(model.player.getHealth())/model.player.getMaxHealth())*0.8, gridSize/10+1);
        gc.setStroke(Color.WHITE);
        gc.strokeRect(0, 1, gridSize*0.8, gridSize/10+1);
        addLines(sensorCanvas);
    }

    // Add black lines to a canvas+
    private void addBorder(Canvas canvas) {
        // Create a graphics context
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Set the color
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        // For the height of the canvas
        gc.strokeRect(1, 1, canvas.getWidth()-1, canvas.getHeight()-1);
    }
    
    private void updateMainImage() {
        
        //------------------------------------ clear the main image stack and add the background
        
        mainImageStack.getChildren().clear();
        mainImageStack.getChildren().add(mainImageView);
        
        // Establish and clear the graphics context
        GraphicsContext gc = mainImageCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mainImageCanvas.getWidth(), mainImageCanvas.getHeight());
        gc.setGlobalAlpha(0.3);
        
        // Get the player location
        int x = model.player.getX();
        int y = model.player.getY();
        // Get the image for the current room
        Image currentRoomImage = model.map.gameGrid[x][y].getImage();
        // Post the image in the main image view
        mainImageView.setImage(currentRoomImage);
        
        
        //---------------------------------------------- Draw the item areas
        
        // If they item areas should be shown
        if (showItemAreas == true) {
            // Get the item positions from the rooms
            ArrayList<double[]> itemAreas = model.map.gameGrid[x][y].itemPositions;
            // For every item position
            for (double[] area: itemAreas) {
               // Check how many nodes a position has
               switch (area.length) {
                    // if the position shape has 4 values (square)
                    case 4:
                        // Draw a red rectangle
                        double x1 = widerGridSize+(area[0]*widerGridSize);
                        double y1 = gridSize+(area[1]*gridSize);
                        double x2 = widerGridSize+(area[2]*widerGridSize);
                        double y2 = gridSize+(area[3]*gridSize);
                        gc.setFill(Color.RED);
                        gc.fillRect(x1, y1, x2-x1, y2-y1);
                        break;
                    case 6:
                        // Draw a red triangle
                        x1 = widerGridSize+(area[0]*widerGridSize);
                        y1 = gridSize+(area[1]*gridSize);
                        x2 = widerGridSize+(area[2]*widerGridSize);
                        y2 = gridSize+(area[3]*gridSize);
                        double x3 = widerGridSize+(area[4]*widerGridSize);
                        double y3 = gridSize+(area[5]*gridSize);
                        double[] xPoints = {x1, x2, x3};
                        double[] yPoints = {y1, y2, y3};
                        gc.setFill(Color.RED);
                        gc.fillPolygon(xPoints, yPoints,3);
                        break;
               }
            }
                        
        }
        
        //-------------------------------------------- Add the items to the room

        // For every item in the room
        for (Item item: model.map.gameGrid[x][y].getItems()) {
            if (item != null) {
                if (item.itemImage != null) {
                    // Add the item to the main image stack
                    mainImageStack.getChildren().add(item.getItemImageView());
            
                    // Get the transform array for the current item position 
                    double[] transformArray = item.drawPosition;
                
                    // Scale the item image according to its size and distance
                    item.getItemImageView().setFitWidth(transformArray[2]*item.getScaleSize()*widerGridSize*0.1);
                    item.getItemImageView().setPreserveRatio(true);
                
                    double shadowHeight = gridSize/10;
                    double shadowWidth = shadowHeight/2;
                
                    // Add the shadow effect
                    DropShadow shadow = new DropShadow();  
                    shadow.setBlurType(BlurType.GAUSSIAN);  
                    shadow.setColor(Color.BLACK);  
                    shadow.setHeight(shadowHeight);  
                    shadow.setRadius(12);  
                    shadow.setWidth(shadowWidth);
                    shadow.setOffsetY(shadowHeight/10);
                    item.getItemImageView().setEffect(shadow);  
                
                    // Set the current X to the room's item position
                    double imageX = transformArray[0]*widerGridSize;
                    // Set the current y to the room's item position minus half the item height to account for different sized items
                    double imageY = transformArray[1]*gridSize - ((item.getItemImageView().getBoundsInParent().getHeight()-shadowHeight)/2);
            
                    // Move the item's image position to match that of the room co-ordinates
                    item.getItemImageView().setTranslateX(imageX);
                    item.getItemImageView().setTranslateY(imageY);           
                }
            }
        }
        
        
        
        //-------------------------------------------Draw the entities in the room
        
        // Get the entites present
        ArrayList<Entity> entities = model.presentEntities();
        
        // For every item in the room
        for (Entity entity: entities) {
            if (entity != null) {
                if (entity.entityImage != null) {
                    
                    // Add the item to the main image stack
                    mainImageStack.getChildren().add(entity.entityImageView);
            
                    // Get the transform array for the current item position 
                    double[] transformArray = entity.drawPosition;
                
                    // Scale the item image according to its size and distance
                    entity.entityImageView.setFitWidth(transformArray[2]*entity.scaleSize*widerGridSize*0.1);
                    entity.entityImageView.setPreserveRatio(true);
                
                    double shadowHeight = gridSize/10;
                    double shadowWidth = shadowHeight/2;
                
                    // Add the shadow effect
                    DropShadow shadow = new DropShadow();  
                    shadow.setBlurType(BlurType.GAUSSIAN);  
                    shadow.setColor(Color.BLACK);  
                    shadow.setHeight(shadowHeight);  
                    shadow.setRadius(12);  
                    shadow.setWidth(shadowWidth);
                    shadow.setOffsetY(shadowHeight/10);
                    entity.entityImageView.setEffect(shadow);  
                
                    // Set the current X to the room's item position
                    double imageX = transformArray[0]*widerGridSize;
                    // Set the current y to the room's item position minus half the item height to account for different sized items
                    double imageY = transformArray[1]*gridSize - ((entity.entityImageView.getBoundsInParent().getHeight()-shadowHeight)/2);
            
                    // Move the item's image position to match that of the room co-ordinates
                    entity.entityImageView.setTranslateX(imageX);
                    entity.entityImageView.setTranslateY(imageY);  
                    System.out.println("Object drawn");
                }
            }
        }
        
        gc.setGlobalAlpha(1);
        addLines(mainImageCanvas);
        // Add the lines over top of the image
        mainImageStack.getChildren().add(mainImageCanvas);
    }
    
    // Method to draw the map
    private void drawMap() {
        // Establish the grid square size
        double size;
        // If the map width > the map height
        if (model.map.gameGrid.length > model.map.gameGrid[0].length) {
            // set the grid square size according to the map width
            size = gridSize/model.map.gameGrid.length;
        } else {
            // set the grid square size according to the map height
             size = gridSize/model.map.gameGrid[0].length;
        }
        int x = model.player.getX();
        int y = model.player.getY();
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
        for (int b = 0; b < model.map.gameGrid[0].length; b++) {
            for (int a = 0; a < model.map.gameGrid.length; a++) {
                if (model.map.gameGrid[a][b] != null) {
                    if (model.map.gameGrid[a][b].visited == true) {
                        gc.setFill(Color.WHITE);
                        gc.fillRect(a*size, b*size, size, size);
                    }
                } 
                if (a == x && y == b) {
                    gc.setFill(Color.RED);
                    gc.fillOval((a*size)+(size/4), (b*size)+(size/4), size/2, size/2);
                }
            }
        }
        addLines(mapCanvas);
    }
    
    
    
    
    //------------------------------------------------------ Developer Controls
    
    // Toggle whether to draw the item positions on to the main image
    public void toggleItemAreas() {
        if (showItemAreas == false) {
            showItemAreas = true;
        } else {
            showItemAreas = false;
        }
    }
    
    
    
    
    //--------------------------------------------------------------- Effects
    
        
    // Add glow to text
    private void addTextGlow(Text text, Color color) {
        // Create a drop shadow
        DropShadow shadow = new DropShadow();
        // Set the drop shadow's properties
        shadow.setBlurType(BlurType.GAUSSIAN);  
        shadow.setColor(color);  
        shadow.setHeight(2);  
        shadow.setRadius(2);  
        shadow.setWidth(2);
        // Add the drop shadow to the text
        text.setEffect(shadow); 
    }
    
    // Add black lines to a canvas
    private void addLines(Canvas canvas) {
        // Create a graphics context
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // set the scanline width
        int scanSize = 1;
        // Set the color
        gc.setFill(Color.BLACK);
        gc.setGlobalAlpha(0.9);
        // For the height of the canvas
        for (int i = 1; i < canvas.getHeight(); i+= (scanSize)*2) {
            // draw a line across the width of the canvas
            gc.fillRect(0, i, canvas.getWidth()-1, scanSize);
        }
        gc.setGlobalAlpha(1);
    }
    
}
