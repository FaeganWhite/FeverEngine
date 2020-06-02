package feverengine;

import javafx.scene.image.Image;

public class Key extends Item
{
    // The door/container the key opens
    Item unlock;
    
     public Key(String setName, String setDescription, double setWeight, int setSize, Item unlockIn, Image setImage, double newScaleSize){
        super(setName, setDescription, setWeight, setSize, setImage, newScaleSize);
        unlock = unlockIn;
    }
}
