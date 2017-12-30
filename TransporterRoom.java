import java.util.*;
/**
 * A subclass of Room, when the player tries to leave this transporter room, he
 * gets transported to a random room
 *
 * @author Ahmed Romih
 * @version 2017-11-03
 */
public class TransporterRoom extends Room
{
    /**
     * Constructor for objects of class TransporterRoom
     */
    public TransporterRoom(String description)
    {
        super(description);        
    }
    
    /**
     * Returns a random room, independent of the direction parameter.
     *
     * @param direction Ignored.
     * @return A randomly selected room.
     */
    public Room getExit(String direction)
    {
        return findRandomRoom();
    }
    
    /**
     * Choose a random room.
     *
     * @return The room we end up in upon leaving this one.
     */
    public Room findRandomRoom()
    {
        Random r = new Random();
        return allRooms.get(r.nextInt(allRooms.size() -1));        
    }
}
