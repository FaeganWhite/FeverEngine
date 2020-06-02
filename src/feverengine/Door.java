package feverengine;

public class Door
{
    String direction;
    boolean open;
    boolean locked;
    
     public Door(String setName, String setDescription, String directionIn, boolean setOpen, boolean setLocked){
        direction = directionIn;
        open = setOpen;
        locked = setLocked;
    }
}
