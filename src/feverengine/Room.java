package feverengine;

import java.util.ArrayList;
import javafx.scene.image.Image;
import java.util.Random;

public class Room
{
    //---------------------------------------------------- Establish the variables
    String name;
    String description;
    private ArrayList<Item> items = new ArrayList<>();
    ArrayList<Entity> entities = new ArrayList<>();
    ArrayList<Door> doors = new ArrayList<>();
    Image roomImage;
    boolean visited = false;
    ArrayList<ItemArea> itemAreas = new ArrayList<>();
    Random rand = new Random();
    Map map;
    ArrayList<RoomLight> lights = new ArrayList<>();
    
    // Set the variables on creation
    public Room(String setName, String setDescription,Image newRoomImage, Map newMap){
        name = setName;
        description = setDescription;
        roomImage = newRoomImage;
        map = newMap;
    }
    
    
    //----------------------------------------------------------Room Save Info
    
    //Return the details of the room contents
    public String getSaveInfo() {
        System.out.println("getting save data from room");
        // Initialise the room code
        String roomCode = new String();
        // Add the rooms name and description
        roomCode += (name+"/"+description+"/"+visited);
        // For every item in the room
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i)!=null) {
                // Add a new line
                roomCode += "\n";
                // Add the details of each item
                roomCode += ("-"+items.get(i).getSaveInfo());
            }
        }
        return roomCode;
    }
    
    
    //--------------------------------------------------------- Gets and Sets
    
    // Get the rooms image
    public Image getImage() {
        return roomImage;
    }
    
    // Get the rooms items
    public ArrayList<Item> getItems() {
        return items;
    }
    
    
    
    
    //-------------------------------------------------------- Adjust item's within
    
    // set the rooms items
    public void setItems(ArrayList<Item> setItem) {
        items = setItem;
    }
    
    // remove an item from the room
    public void remove(Item removeItem) {
        items.remove(removeItem);
    }
    
    // clear all the items from the room
    public void empty() {
        items.clear();
    }
    
    
    // add an item from the room
    public boolean add(Item addItem, int areaNum) {
        if (addItem.permenantPosition == null) {
            if (itemAreas.size() > 0) {
                System.out.println("Attempting to add "+addItem.getName());
                   
                int itemAreaNum;
            
                if (areaNum != 9999) {
                    itemAreaNum = areaNum;
                } else {
                    // Choose random area to add the item to
                    itemAreaNum = (int)(Math.floor(rand.nextDouble()*itemAreas.size()));
                }
            
                double[] area = itemAreas.get(itemAreaNum).coordinates;
            
                switch (area.length) {
                    // if the position shape has 4 values (rectangle)
                    case 4: 
                        System.out.println("RECTANGLE");
                    
                        // Generate random co-ordinates within the rectangle
                        double x = area[0]+(Math.abs(area[2]-area[0])*rand.nextDouble());
                        double y = area[1]+((area[3]-area[1])*rand.nextDouble());
                    
                        double[] itemAddPosition = new double[3];
                    
                        // Set the x position of the item
                        itemAddPosition[0] = x;
                        // Set the y position of the item
                        itemAddPosition[1] = y;
                        // Set the scale of the item
                        itemAddPosition[2] = y;
                    
                        System.out.println("Item in item area."+itemAddPosition[0]+","+itemAddPosition[1]+","+itemAddPosition[2]+",");
                        // Set the items's coordinates to the randomly generated ones
                        addItem.drawPosition = itemAddPosition;
                        // Add the item to the room
                        items.add(addItem);
                    
                        return true;
                    // For 6 value shapes (triangles)
                    case 6:
                        System.out.println("TRIANGLE");
                    
                        // Establish the boundaries of the rectangle
                        double xleft = 1;
                        double xright = -1;
                        double ytop = 1;
                        double ybottom = -1;
                    
                        // Get the boundaries of the rectangle containing the triangle
                        for (int i = 0; i < area.length; i++) {
                            // if the number is even (x-coord)
                            if ((i & 1) == 0) {
                                // if the co-oordinate extends past the current outer bounds
                                if (area[i] > xright) {
                                    // update them
                                    xright = area[i];
                                }
                                    if (area[i] < xleft) {
                                    xleft = area[i];
                                }
                            } else {
                                // if the co-oordinate extends past the current outer bounds
                                if (area[i] > ybottom) {
                                    // update them
                                    ybottom = area[i];
                                }
                                if (area[i] < ytop) {
                                    ytop = area[i];
                                }
                            }
                        }
                    
                        System.out.println("Boundaries of triangle are: "+xleft+","+ytop+" -- "+xright+","+ybottom);
                    
                        // Generate random co-ordinates within the outer bounds
                        x = xleft+(Math.abs(xright-xleft)*rand.nextDouble());
                        y = ytop+(Math.abs(ybottom-ytop)*rand.nextDouble());
                    
                        System.out.println(x);
                        System.out.println(y);
                    
                        double[] itemAddPositionTriangle = new double[3];
                    
                        // Convert doubles to int for more precise math
                        int[] intTriangle = new int[6];
                        for (int i = 0; i < area.length; i++) {
                            intTriangle[i] = (int)(area[i]*1000);
                        }
                    
                        int intx = (int)(x*1000);
                        int inty = (int)(y*1000);
                       
                        // Area of the trinagle
                        double a1 = area(intTriangle[0], intTriangle[1], intTriangle[2], intTriangle[3], intTriangle[4], intTriangle[5]);
                        double a2 = area(intx, inty,intTriangle[2],intTriangle[3],intTriangle[4],intTriangle[5]);
                        double a3 = area(intTriangle[0],intTriangle[1],intx,inty,intTriangle[4],intTriangle[5]);
                        double a4 = area(intTriangle[0],intTriangle[1],intTriangle[2],intTriangle[3],intx,inty);
                        
                        // If the point is within the triangle
                        if (a1 == a2 + a3 + a4) {
                            
                            System.out.println("TRIANGLE - SUCCESS");
                            
                            // Set the x position of the item
                            itemAddPositionTriangle[0] = x;
                            // Set the y position of the item
                            itemAddPositionTriangle[1] = y;
                            // Set the scale of the item
                            itemAddPositionTriangle[2] = y;
                                
                                
                            System.out.println(x);
                            System.out.println(y);
            
                            System.out.println("Item in item area."+itemAddPositionTriangle[0]+","+itemAddPositionTriangle[1]+","+itemAddPositionTriangle[2]+",");
                            // Set the items's coordinates to the randomly generated ones
                            addItem.drawPosition = itemAddPositionTriangle;
                            // Add the item to the room
                            items.add(addItem);
                            // return true
                            return true;
                        } else {
                            // Otherwise try again using the same itemAreaNum
                            return add(addItem, itemAreaNum);
                        }
                    
                    case 8:
                        System.out.println("Quadrilateral");
                    
                        // Establish the boundaries of the rectangle
                        xleft = 1;
                        xright = -1;
                        ytop = 1;
                        ybottom = -1;
                    
                        // Get the boundaries of the rectangle containing the triangle
                        for (int i = 0; i < area.length; i++) {
                            // if the number is even (x-coord)
                            if ((i & 1) == 0) {
                                // if the co-oordinate extends past the current outer bounds
                                if (area[i] > xright) {
                                    // update them
                                    xright = area[i];
                                }
                                if (area[i] < xleft) {
                                    xleft = area[i];
                                }
                            } else {
                                // if the co-oordinate extends past the current outer bounds
                                if (area[i] > ybottom) {
                                    // update them
                                    ybottom = area[i];
                                }
                                if (area[i] < ytop) {
                                    ytop = area[i];
                                }
                            }
                        }
                    
                        System.out.println("Boundaries of quad are: "+xleft+","+ytop+" -- "+xright+","+ybottom);
                    
                        // Generate random co-ordinates within the outer bounds
                        x = xleft+(Math.abs(xright-xleft)*rand.nextDouble());
                        y = ytop+(Math.abs(ybottom-ytop)*rand.nextDouble());
                    
                        System.out.println(x);
                        System.out.println(y);
                    
                        double[] itemAddPositionQuad = new double[3];
                    
                        // Convert doubles to int for more precise math
                        int[] intQuad = new int[8];
                        for (int i = 0; i < area.length; i++) {
                            intQuad[i] = (int)(area[i]*1000);
                        }
                    
                        intx = (int)(x*1000);
                        inty = (int)(y*1000);
                       
                        // Area of the 2 trinagles which make up the quad
                        a1 = area(intQuad[0], intQuad[1], intQuad[2], intQuad[3], intQuad[6], intQuad[7]);
                        a2 = area(intx, inty,intQuad[2],intQuad[3],intQuad[6],intQuad[7]);
                        a3 = area(intQuad[0],intQuad[1],intx,inty,intQuad[6],intQuad[7]);
                        a4 = area(intQuad[0],intQuad[1],intQuad[2],intQuad[3],intx,inty);
                    
                        double a5 = area(intQuad[2], intQuad[3], intQuad[4], intQuad[5], intQuad[6], intQuad[7]);
                        double a6 = area(intx, inty,intQuad[4],intQuad[5],intQuad[6],intQuad[7]);
                        double a7 = area(intQuad[2],intQuad[3],intx,inty,intQuad[6],intQuad[7]);
                        double a8 = area(intQuad[2],intQuad[3],intQuad[4],intQuad[5],intx,inty);
                    
                        // If the point is within either triangle
                        if (a1 == a2 + a3 + a4 || a5 == a6 + a7 + a8) {
                            
                            System.out.println("QUAD - SUCCESS");
                            
                            // Set the x position of the item
                            itemAddPositionQuad[0] = x;
                            // Set the y position of the item
                            itemAddPositionQuad[1] = y;
                            // Set the scale of the item
                            itemAddPositionQuad[2] = y;
                                
                                
                            System.out.println(x);
                            System.out.println(y);
            
                            System.out.println("Item in item area."+itemAddPositionQuad[0]+","+itemAddPositionQuad[1]+","+itemAddPositionQuad[2]+",");
                            // Set the items's coordinates to the randomly generated ones
                            addItem.drawPosition = itemAddPositionQuad;
                            // Add the item to the room
                            items.add(addItem);
                            // return true
                            return true;
                        } else {
                            // Otherwise try again using the same itemAreaNum
                            return add(addItem, itemAreaNum);
                        }
                } 
                
            }
            return false;
        } else {
            // Otherwise if the item has a permenant position
            double[] itemAddPosition = new double[3];
            // Set the x position of the item
            itemAddPosition[0] = addItem.permenantPosition[0];
            // Set the y position of the item
            itemAddPosition[1] = addItem.permenantPosition[1];
            // Set the scale of the item
            itemAddPosition[2] = 1;
            // Set the items's coordinates to the randomly generated ones
            addItem.drawPosition = itemAddPosition;
            // Add the item to the room
            items.add(addItem);
            return true;
        }
    }
    
    // add an entity from the room
    public boolean addEntity(Entity addEntity, int areaNum) {
        
        if (itemAreas.size() > 0) {
            System.out.println("Attempting to add "+addEntity.name);
                   
            int itemAreaNum;
            
            int itemNum = 0;
            
            if (areaNum != 9999) {
                itemAreaNum = areaNum;
            } else {
                // Get the number of floors in the room
                int floorNum = 0;
                for (ItemArea areas: itemAreas) {
                    if (areas.type.equals("floor")) {
                        floorNum++;
                    }
                }
                // Generate random number to decide which floor to use
                itemAreaNum = (int)(Math.floor(rand.nextDouble()*floorNum));
                
                // Pic the floor corresponding to the random number
                for (ItemArea areas: itemAreas) {
                    if (areas.type.equals("floor")) {
                        if (itemAreaNum == 0) {
                            break;
                        }
                        itemAreaNum --;
                    }
                    itemNum++;
                }
                
            }
            
            double[] area = itemAreas.get(itemNum).coordinates;
            
            switch (area.length) {
                // if the position shape has 4 values (rectangle)
                case 4: 
                    System.out.println("RECTANGLE");
                    
                    // Generate random co-ordinates within the rectangle
                    double x = area[0]+(Math.abs(area[2]-area[0])*rand.nextDouble());
                    double y = area[1]+((area[3]-area[1])*rand.nextDouble());
                    
                    double[] itemAddPosition = new double[3];
                    
                    // Set the x position of the item
                    itemAddPosition[0] = x;
                    // Set the y position of the item
                    itemAddPosition[1] = y;
                    // Set the scale of the item
                    itemAddPosition[2] = y;
                    
                    System.out.println("Item in item area."+itemAddPosition[0]+","+itemAddPosition[1]+","+itemAddPosition
                            [2]+",");
                    // Set the items's coordinates to the randomly generated ones
                    addEntity.drawPosition = itemAddPosition;
                    // Add the item to the room
                    entities.add(addEntity);
                    
                    return true;
                // For 6 value shapes (triangles)
                case 6:
                    System.out.println("TRIANGLE");
                    
                    // Establish the boundaries of the rectangle
                    double xleft = 1;
                    double xright = -1;
                    double ytop = 1;
                    double ybottom = -1;
                    
                    // Get the boundaries of the rectangle containing the triangle
                    for (int i = 0; i < area.length; i++) {
                        // if the number is even (x-coord)
                        if ((i & 1) == 0) {
                            // if the co-oordinate extends past the current outer bounds
                            if (area[i] > xright) {
                                // update them
                                xright = area[i];
                            }
                            if (area[i] < xleft) {
                                xleft = area[i];
                            }
                        } else {
                            // if the co-oordinate extends past the current outer bounds
                            if (area[i] > ybottom) {
                                // update them
                                ybottom = area[i];
                            }
                            if (area[i] < ytop) {
                                ytop = area[i];
                            }
                        }
                    }
                    
                    System.out.println("Boundaries of triangle are: "+xleft+","+ytop+" -- "+xright+","+ybottom);
                    
                    // Generate random co-ordinates within the outer bounds
                    x = xleft+(Math.abs(xright-xleft)*rand.nextDouble());
                    y = ytop+(Math.abs(ybottom-ytop)*rand.nextDouble());
                    
                    System.out.println(x);
                    System.out.println(y);
                    
                    double[] itemAddPositionTriangle = new double[3];
                    
                    // Convert doubles to int for more precise math
                    int[] intTriangle = new int[6];
                    for (int i = 0; i < area.length; i++) {
                        intTriangle[i] = (int)(area[i]*1000);
                    }
                    
                    int intx = (int)(x*1000);
                    int inty = (int)(y*1000);
                       
                    // Area of the trinagle
                    double a1 = area(intTriangle[0], intTriangle[1], intTriangle[2], intTriangle[3], intTriangle[4], intTriangle[5]);
                    double a2 = area(intx, inty,intTriangle[2],intTriangle[3],intTriangle[4],intTriangle[5]);
                    double a3 = area(intTriangle[0],intTriangle[1],intx,inty,intTriangle[4],intTriangle[5]);
                    double a4 = area(intTriangle[0],intTriangle[1],intTriangle[2],intTriangle[3],intx,inty);
                        
                    // If the point is within the triangle
                    if (a1 == a2 + a3 + a4) {
                            
                        System.out.println("TRIANGLE - SUCCESS");
                            
                        // Set the x position of the item
                        itemAddPositionTriangle[0] = x;
                        // Set the y position of the item
                        itemAddPositionTriangle[1] = y;
                        // Set the scale of the item
                        itemAddPositionTriangle[2] = y;
                                
                                
                        System.out.println(x);
                        System.out.println(y);
            
                        System.out.println("Item in item area."+itemAddPositionTriangle[0]+","+itemAddPositionTriangle[1]+","+itemAddPositionTriangle[2]+",");
                        // Set the items's coordinates to the randomly generated ones
                        addEntity.drawPosition = itemAddPositionTriangle;
                        // Add the item to the room
                        entities.add(addEntity);
                        // return true
                        return true;
                    } else {
                        // Otherwise try again using the same itemAreaNum
                        return addEntity(addEntity, itemAreaNum);
                    }
                    
                case 8:
                    System.out.println("Quadrilateral");
                    
                    // Establish the boundaries of the rectangle
                    xleft = 1;
                    xright = -1;
                    ytop = 1;
                    ybottom = -1;
                    
                    // Get the boundaries of the rectangle containing the triangle
                    for (int i = 0; i < area.length; i++) {
                        // if the number is even (x-coord)
                        if ((i & 1) == 0) {
                            // if the co-oordinate extends past the current outer bounds
                            if (area[i] > xright) {
                                // update them
                                xright = area[i];
                            }
                            if (area[i] < xleft) {
                                xleft = area[i];
                            }
                        } else {
                            // if the co-oordinate extends past the current outer bounds
                            if (area[i] > ybottom) {
                                // update them
                                ybottom = area[i];
                            }
                            if (area[i] < ytop) {
                                ytop = area[i];
                            }
                        }
                    }
                    
                    System.out.println("Boundaries of quad are: "+xleft+","+ytop+" -- "+xright+","+ybottom);
                    
                    // Generate random co-ordinates within the outer bounds
                    x = xleft+(Math.abs(xright-xleft)*rand.nextDouble());
                    y = ytop+(Math.abs(ybottom-ytop)*rand.nextDouble());
                    
                    System.out.println(x);
                    System.out.println(y);
                    
                    double[] itemAddPositionQuad = new double[3];
                    
                    // Convert doubles to int for more precise math
                    int[] intQuad = new int[8];
                    for (int i = 0; i < area.length; i++) {
                        intQuad[i] = (int)(area[i]*1000);
                    }
                    
                    intx = (int)(x*1000);
                    inty = (int)(y*1000);
                       
                    // Area of the 2 trinagles which make up the quad
                    a1 = area(intQuad[0], intQuad[1], intQuad[2], intQuad[3], intQuad[6], intQuad[7]);
                    a2 = area(intx, inty,intQuad[2],intQuad[3],intQuad[6],intQuad[7]);
                    a3 = area(intQuad[0],intQuad[1],intx,inty,intQuad[6],intQuad[7]);
                    a4 = area(intQuad[0],intQuad[1],intQuad[2],intQuad[3],intx,inty);
                    
                    double a5 = area(intQuad[2], intQuad[3], intQuad[4], intQuad[5], intQuad[6], intQuad[7]);
                    double a6 = area(intx, inty,intQuad[4],intQuad[5],intQuad[6],intQuad[7]);
                    double a7 = area(intQuad[2],intQuad[3],intx,inty,intQuad[6],intQuad[7]);
                    double a8 = area(intQuad[2],intQuad[3],intQuad[4],intQuad[5],intx,inty);
                    
                    // If the point is within either triangle
                    if (a1 == a2 + a3 + a4 || a5 == a6 + a7 + a8) {
                            
                        System.out.println("QUAD - SUCCESS");
                            
                        // Set the x position of the item
                        itemAddPositionQuad[0] = x;
                        // Set the y position of the item
                        itemAddPositionQuad[1] = y;
                        // Set the scale of the item
                        itemAddPositionQuad[2] = y;
                                
                                
                        System.out.println(x);
                        System.out.println(y);
            
                        System.out.println("Item in item area."+itemAddPositionQuad[0]+","+itemAddPositionQuad[1]+","+itemAddPositionQuad[2]+",");
                        // Set the items's coordinates to the randomly generated ones
                        addEntity.drawPosition = itemAddPositionQuad;
                        // Add the item to the room
                        entities.add(addEntity);
                        // return true
                        return true;
                    } else {
                        // Otherwise try again using the same itemAreaNum
                        return addEntity(addEntity, itemAreaNum);
                    }
            } 
                
        }
        return false;
    }

    
    // Return the area of a triange
    public double area(int x1, int y1, int x2, int y2, int x3, int y3) 
    { 
       return Math.abs((x1*(y2-y3) + x2*(y3-y1)+x3*(y1-y2))/2.0); 
    } 
    
    // add an item location to the room
    public void addItemPosition(ItemArea itemArea) {
        System.out.println("Adding an item area to "+this.name);
        // add this to the arrayList of co-ordinates
        itemAreas.add(itemArea);
        System.out.println(itemArea.type);
        System.out.println(itemArea.coordinates);
    }
}
