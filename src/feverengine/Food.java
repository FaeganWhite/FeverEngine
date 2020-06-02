package feverengine;

import javafx.scene.image.Image;

public class Food extends Item
{
    int nutrition;
    
     public Food(String setName, String setDescription, double setWeight, int setSize, int nutritionIn, Image setImage, double newScaleSize){
        super(setName, setDescription, setWeight, setSize, setImage, newScaleSize);
        nutrition = nutritionIn;
    }
}
