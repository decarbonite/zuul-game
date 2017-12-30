
/**
 * A subclass of Item, makes a beamer that is not charged and uses methods
 * to charge it and fire it and when it's fired the played gets teleported to 
 * the room it was charged in
 *
 * @author Ahmed Romih
 * @version 2017-11-03
 */
public class Beamer extends Item
{
    private Room roomSaved;
    private boolean isCharged;    
    
    /**
     * Constructor for objects of class Beamer
     */
    public Beamer(String name, String description, double weight)
    {
        super(name, description, weight);  
        roomSaved = null;
        this.setRoomSaved(roomSaved);
        isCharged = false;
    }
    
    /**
     * Saves a room to go back to later
     */
    public void setRoomSaved(Room roomSaved){        
        this.roomSaved = roomSaved;   
    }
    
    /**
     * @return Returns the saved room
     */
    public Room getRoomSaved(){        
        return roomSaved;   
    }
    
    /**
     * @return Returns true if the beamer was charged, false otherwise
     */
    public boolean isCharged(){       
       return isCharged;       
    }
    
    /**
     * Charges the beamer and saves the current room
     */
    public void charge(Room currentRoom){
        isCharged = true; 
        setRoomSaved(currentRoom);
    }
    
    /**
     * fires the beamer and transports the player to the saved room
     */
    public Room fire(){
        isCharged = false;  
        return getRoomSaved();
    }
}
