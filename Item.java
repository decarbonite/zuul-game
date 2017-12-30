import java.util.*;
/** 
 * This class makes the items that is picked up by the player
 *
 * @author Ahmed Romih
 * @version October 20, 2017
 */
public class Item
{    
    private String description;
    private double weight;   
    private String name;
    
    /**
     * Constructor for objects of class Item
     */
    public Item(String name,String description, double weight)
    {
        this.description = description;
        this.weight = weight;
        this.name = name;
    }
    
    /**
     * @return Returns a string representation of the item and its weight
     */
    public String toString()
    {        
        return "Item name: " + name+ "    Description: " +description + 
               "    It weighs: " + weight +".\n";
    } 
    
    /**
     * @return Returns the name of the item
     */
    public String getName()
    {        
        return name;
    } 
}















