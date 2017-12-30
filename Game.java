import java.util.*;
import java.lang.*;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * 
 * @author Ahmed Romih
 * @version October 20, 2017
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Room prevRoom; //previous room
    private Stack<Room> prevRooms;
    private int count = 0; // counts the items that have been picked so far
    private boolean isCharged= false; // true if the beamer has been charged, false otherwise
    private boolean isEaten=false; // true if the cookie has been eaten, false otherwise
    private Beamer beamer;
    private Beamer beamer2;
    private Item oneItem; //contains the item the player carrying
    private TransporterRoom transportRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office, transportRoom;

        //creating items
        Item stick = new Item("Stick","Red & Long Stick", 300);    
        Item beer = new Item("Beer","Cold Beer", 111);
        Item cookie = new Item("Cookie","Tasty Cookie ", 999);
        Item phone = new Item("Phone","New Phone", 244);
        Item soap = new Item("Soap","Hot Soap", 50);       
        Item computer = new Item("Computer","Big PC", 99);
        beamer = new Beamer("Beamer","Laser Beam", 155);       
        beamer2 = new Beamer("Beamer","Laser Beam", 155);

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transportRoom = new TransporterRoom("in the transporter room");

        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", transportRoom);

        transportRoom.setExit("south", outside);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        //adding items to various rooms
        pub.addItem(stick);
        pub.addItem(beer);
        lab.addItem(cookie);
        lab.addItem(phone);        
        theatre.addItem(soap);        
        theatre.addItem(cookie);
        pub.addItem(cookie);
        office.addItem(computer);
        office.addItem(beamer);
        lab.addItem(beamer2); 

        currentRoom = outside;  // start game outside
        prevRoom = null;
        prevRooms = new Stack<Room>();
        oneItem = null;
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if(commandWord.equals("look")){
            look(command);
        }
        else if(commandWord.equals("eat")){
            eat(command);
        }
        else if(commandWord.equals("back")){
            back(command);
        }
        else if(commandWord.equals("stackBack")){
            stackBack(command);
        }
        else if(commandWord.equals("take")){
            take(command);
        }
        else if(commandWord.equals("drop")){
            drop(command);
        }
        else if(commandWord.equals("charge")){
            charge(command);
        }
        else if(commandWord.equals("fire")){
            fire(command);
        }        
        //else command not recognised.
        return wantToQuit;
    }

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            prevRooms.push(currentRoom);
            prevRoom = currentRoom;
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Outputs the details of the current room 
     * 
     * @param command The command to be processed
     */
    private void look(Command command){
        if(command.hasSecondWord()){
            System.out.println("Look what?");             
        }else{
            System.out.println(currentRoom.getLongDescription()); 
            if(oneItem != null){
                System.out.println("You're carrying "+ oneItem.getName());                
            }else{
                System.out.println("You're not carrying anything");
            }
        }
    }

    /**
     * feeds the player   
     * @param command The command to be processed     
     */
    private void eat(Command command){
        if(command.hasSecondWord()){
            System.out.println("Eat what?");             
        }else if(oneItem != null && oneItem.getName().equals("Cookie")){
            isEaten = true;
            count = 1;
            oneItem = null;
            System.out.println("The player has eaten the cookie");            
        }else {
            System.out.println("The player has no food");
        }
    }

    /**
     * moves the player to the previous room that the player visited
     * @param command The command to be processed
     */
    private void back(Command command){
        if(command.hasSecondWord()){
            System.out.println("Back what?");   
        }else if(prevRoom == null){
            System.out.println("This is your start point, you can't go back");    
        }else{
            prevRooms.push(currentRoom);
            Room temp = currentRoom;
            currentRoom = prevRoom;
            prevRoom = temp;

            System.out.println("You have gone back"); 
            System.out.println(currentRoom.getLongDescription());            
        }
    }

    /**
     * takes the player back one room at a time
     * @param command The command to be processed
     */
    private void stackBack(Command command){
        if(command.hasSecondWord()){
            System.out.println("stackBack what?");             
        }else if(prevRooms.empty()){
            System.out.println("This is your start point, you can't go back");           
        }else{            
            currentRoom = prevRooms.pop();
            System.out.println("You have gone back"); 
            System.out.println(currentRoom.getLongDescription());            
        }
    }

    /**
     * Searches for a cookie at the beginning of the game and eats it to be
     * able to pick up 5 items, whenever the player eats a cookie, the player
     * will be able to pick up another 5 items
     * @param command The command to be processed
     */
    private void take(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Take what?");      
        }

        String itemName = command.getSecondWord();
        Item newItem = currentRoom.getItem(itemName);

        if(newItem == null){
            System.out.println("The item is not in the room");
        }

        //take first item -cookie-
        else if(oneItem == null && itemName.equals("Cookie")){
            oneItem = newItem;
            currentRoom.removeItem(newItem); 
            System.out.println("You picked up a/an "+ oneItem.getName());
            count++;
        }else if(oneItem == null && count > 0 && count < 6 && isEaten){
            oneItem = newItem;
            currentRoom.removeItem(newItem); 
            System.out.println("You picked up a/an "+ oneItem.getName());
            count++;
            //reset count to pick and eat a cookie and 5 items
            if(count == 6){            
                count = 0;    
                isEaten =false;
            }                      
        }
        else if(oneItem != null){
            System.out.println("You're already holding something");            
        }
        else{
            System.out.println("You need to pick and eat a cookie first");
        }                
    }

    /**
     * drops the item that the player is carrying
     * @param command The command to be processed
     */    
    private void drop(Command command){
        if(command.hasSecondWord()){
            System.out.println("Drop what?");             
        }
        else if(oneItem == null){
            System.out.println("You have nothing to drop");
        }
        else if(oneItem != null){            
            System.out.println("You have dropped a/an " +oneItem.getName());
            currentRoom.addItem(oneItem);
            oneItem = null;; 
        }
    }

    /**
     * Charges the beamer and recognizes the room it was charged in
     * 
     * @param command The command to be processed
     */
    private void charge(Command command){
        if(command.hasSecondWord()){
            System.out.println("Charge what?");             
        }else if(oneItem == null){
            System.out.println("You need to pick the Beamer first");
        }else if(oneItem.getName().equals("Beamer")){
            Beamer beamer = (Beamer) oneItem;
            if(!beamer.isCharged()){               
                beamer.charge(currentRoom);
                System.out.println("You now memorize this room"); 
            }
            else{
                System.out.println("It is already charged");
            }
        }        
    }

    /**
     * Fires the beamer and sends the player to the room the charger was charged in
     * 
     * @param command The command to be processed
     */
    private void fire(Command command){        
        if(command.hasSecondWord()){
            System.out.println("Fire what?");             
        }else if(oneItem == null){
            System.out.println("You need to have the Beamer in hand first");
        }else if(oneItem.getName().equals("Beamer")){
            Beamer beamer = (Beamer) oneItem;
            if(beamer.isCharged()){
                prevRoom = currentRoom;  
                currentRoom = beamer.fire();                
                System.out.println("You have been transported");    
                System.out.println("You're now " +currentRoom.getShortDescription());
            }else{
                System.out.println("You have to charge the beamer");
        
            }
        }       
    }    
}

