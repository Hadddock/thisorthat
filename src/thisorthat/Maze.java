package thisorthat;
import java.util.Set;
import java.util.Stack;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Maze is an abstraction of a collection of rooms and the status of a player in
 * the Maze for use in a Trivia maze program.
 *
 */
public class Maze implements Serializable {
	/*
	 * 2-D array of rooms composing the Maze
	 */
	private Room[][] myRooms;// the maze composed of a 2-D array of rooms
	/*
	 * the current Y location of the player in the Maze
	 */
	private int myYPosition;
	/*
	 * the current X location of the player in the Maze
	 */
	private int myXPosition;
	/*
	 * whether or not the player in the Maze currently has a key
	 */
	private boolean hasKey; 

	/*
	 * Constructor for a Maze
	 * @param theRooms the 2-D array of rooms to compose the Maze
	 * @param theYPosition the starting Y position of the player in the Maze
	 * @param theXPosition the starting X position of the player in the Maze
	 * @param theKeyStatus whether or not the player starts the Maze with a key
	 */
	public Maze(Room[][] theRooms, int theYPosition, int theXPosition, boolean theKeyStatus) {
		this.myRooms = theRooms;
		this.myYPosition = theYPosition;
		this.myXPosition = theXPosition;
		this.hasKey = theKeyStatus;
	}
	/*
	 * XXX constructor for Maze for use in tests
	 */
	public Maze() {
		Room[][] testRooms = new Room[3][3];
		// make Gullunge/basic question room
		Room testRoomGullenge = new Room(new Question(), false, false, false, false);
		testRooms[0][0] = new Room(testRoomGullenge);
		testRooms[0][2] = new Room(testRoomGullenge);
		testRooms[1][2] = new Room(testRoomGullenge);
		testRooms[2][0] = new Room(testRoomGullenge);
		testRooms[2][2] = new Room(testRoomGullenge);
		//make start room
		testRooms[1][1] = new Room(new Question(), true, false, false, false);

		// make goal room
		testRooms[0][1] = new Room(new Question(), true, false, true, false);
		// make key room
		testRooms[1][0] = new Room(new Question(), false, false, false, true);

		// make locked room
		testRooms[2][1] = new Room(new Question(), false, true, false, false);

		//TODO replace null with the test display variable
		this.myRooms = testRooms;
		this.myYPosition = 1;
		this.myXPosition = 1;
	}

	
	/**
	 * Getter for myYPosition
	 * @return myYPosition
	 */
	public int getMyYPosition() {
		return myYPosition;
	}
	/**
	 * Getter for myXPosition
	 * @return myXPosition
	 */
	public int getMyXPosition() {
		return myXPosition;
	}
	/**
	 * Setter for myYPosition
	 */
	public void setMyYPosition(int myYPosition) {
		this.myYPosition = myYPosition;

	}
	/**
	 * Setter for myXPosition
	 */
	public void setMyXPosition(int myXPosition) {
		this.myXPosition = myXPosition;

	}
	/**
	 * Getter for hasKey
	 * @return hasKey
	 */
	public boolean getHasKey() {
		return hasKey;
	}
	/**
	 * Check if there exists a room at the specified coordinates in the maze
	 * @param theYPosition checked to see if it exists in the bounds of the maze
	 * @param theXPosition checked to see if it exists in the bounds of the maze
	 * @return whether or not there exists a room at the specified coordinates in the maze
	 */
	public boolean checkInBounds(int theYPosition, int theXPosition) {
		if (theXPosition >= 0 && theXPosition <= this.myRooms[0].length - 1 && theYPosition >= 0 && theYPosition <= this.myRooms.length - 1) {
			return true;
		}
		return false;
	}
	/**
	 * Check if the current state of the Maze is potentially completable by the player
	 * @return if the current state of the Maze is potentially completable by the player
	 */
	public boolean checkWinPossible() {
		int x = myXPosition;
		int y = myYPosition;
		Room current = this.myRooms[y][x];
		//check most common unsolvable condition, surrounded by locked or null rooms without a key
		if(!this.hasKey 
				&& (!checkInBounds(y+1,x) || this.myRooms[y+1][x].getIsLocked())
				&& (!checkInBounds(y-1,x) || this.myRooms[y-1][x].getIsLocked())
				&& (!checkInBounds(y,x+1) || this.myRooms[y][x+1].getIsLocked())
				&& (!checkInBounds(y,x-1) || this.myRooms[y][x-1].getIsLocked())) {
			return false;
		}
		//begin depth first search on key
		boolean potentialKey = this.hasKey;
		Set<Room> set = new HashSet<Room>();
		Set<Room> lockedNeighbors = new HashSet<Room>();
		Stack<Room> stack = new Stack<Room>();
		stack.add(current);
		
		while(!stack.isEmpty()) {
			Room element = stack.pop();
			x = element.getMyXCoordinate();
			y = element.getMyYCoordinate();
			if (element.getIsKeyRoom()){
				potentialKey = true;
			}
			if(!set.contains(element)) {
				set.add(element);
				
			}
			if(element.getIsGoal()) {
				return true;
			}
			List<Room> neighbors = getNeighbors(y,x);
			for(int i = 0; i < neighbors.size(); i++) {
				Room n = neighbors.get(i);
				if(!n.getIsLocked()) {
					if(!set.contains(n) && !stack.contains(n)) {
						stack.add(n);
					}
				}
				
				else if (!lockedNeighbors.contains(n)) {
						lockedNeighbors.add(n);
				}
			}
		}
		//if a key is in posession or is acessible,
		if(potentialKey) {
			for(Room lockedRoom: lockedNeighbors) {
				stack.add(lockedRoom);
			}
		}
		//second time around assuming key has been used somewhere
		while(!stack.isEmpty()) {
			Room element = stack.pop();
			x = element.getMyXCoordinate();
			y = element.getMyYCoordinate();
			if(!set.contains(element)) {
				set.add(element);
			}
			if(element.getIsGoal()) {
				return true;
			}
			List<Room> neighbors = getNeighbors(y,x);
			for(int i = 0; i < neighbors.size(); i++) {
				Room n = neighbors.get(i);
				if(!n.getIsLocked() && !set.contains(n) && !stack.contains(n)) {
						set.add(n);
				}
			}
		}
		return false;
	}
	/**
	 * Check if if the player has completed the maze by reaching a goal room
	 * @return if the player has completed the maze by reaching a goal room
	 */
	public boolean checkWinCondition() {
		return this.myRooms[this.myYPosition][this.myXPosition].getIsGoal();
	}

	/**
	 * Check if if the player has completed the maze by reaching a goal room
	 * @param theYPosition the Y position of the room whose neighbors will be returned in a list
	 * @param theXPosition the Y position of the room whose neighbors will be returned in a list
	 * @return A List of rooms adjacent to the room at the specified coordinates
	 */
	public List<Room> getNeighbors(int theYPosition, int theXPosition) {
		List<Room> returnValue = new LinkedList<Room>();
		if(checkInBounds(theYPosition-1,theXPosition)) {
			
			this.myRooms[theYPosition-1][theXPosition].setMyYCoordinate(theYPosition-1);
			this.myRooms[theYPosition-1][theXPosition].setMyXCoordinate(theXPosition);
			returnValue.add(this.myRooms[theYPosition-1][theXPosition]);
		}
		if(checkInBounds(theYPosition,theXPosition+1)) {
			
			this.myRooms[theYPosition][theXPosition+1].setMyYCoordinate(theYPosition);
			this.myRooms[theYPosition][theXPosition+1].setMyXCoordinate(theXPosition+1);
			returnValue.add(this.myRooms[theYPosition][theXPosition+1]);
		}
		
		if(checkInBounds(theYPosition+1,theXPosition)) {
			this.myRooms[theYPosition+1][theXPosition].setMyYCoordinate(theYPosition+1);
			this.myRooms[theYPosition+1][theXPosition].setMyXCoordinate(theXPosition);
			returnValue.add(this.myRooms[theYPosition+1][theXPosition]);
		}
		
		if(checkInBounds(theYPosition,theXPosition-1)) {
			this.myRooms[theYPosition][theXPosition-1].setMyYCoordinate(theYPosition);
			this.myRooms[theYPosition][theXPosition-1].setMyXCoordinate(theXPosition-1);
			returnValue.add(this.myRooms[theYPosition][theXPosition-1]);
		}
		return returnValue;
	}

	/*
	 * Checks if the player is currently in a room containing a key, and if so removes the key from
	 * the room and gives it to the player
	 */
	public void obtainKey() {
		// if giving player key and current room contains a key
		if (this.myRooms[myYPosition][myXPosition].getIsKeyRoom()) {
			this.myRooms[myYPosition][myXPosition].setIsKeyRoom(false);
			this.hasKey = true;
			
		}
	}
	/*
	 * Checks if the player is attempting to access a locked room, and if so removes the key from the player
	 * and unlocks the room, making it accessible
	 */
	public void useKey() {
		if (this.myRooms[myYPosition][myXPosition].getIsLocked()) {
			this.myRooms[myYPosition][myXPosition].setIsLocked(false);
			this.myRooms[myYPosition][myXPosition].setIsAcessible(true);
			this.hasKey = false;
			
		}
	}
	/*
	 * Setter for hasKey
	 * @param the new boolean value assigned to hasKey
	 */
	public void setHasKey(boolean theKeyStatus) {
		this.hasKey = theKeyStatus;
		
	}
	/*
	 * Getter for myRooms
	 * @return myRooms
	 */
	public Room[][] getMyRooms() {
		return myRooms;
	}
	/*
	 * XXX toString method for this Maze for use in temporary CLI
	 */
	public String toString() {
		StringBuilder returnValue = new StringBuilder();
		Room currentRoom;
		char currentChar;
		// K = Key room
		// L = Locked room
		// C = Completed room
		// Q = Question room
		// G = Goal room
		// P = Player position
		for (int i = 0; i < myRooms.length; i++) {
			for (int j = 0; j < myRooms[i].length; j++) {
				currentRoom = myRooms[i][j];
				if (i == this.myYPosition && j == this.myXPosition) {
					currentChar = 'P';
				} else if (currentRoom.getIsLocked()) {
					currentChar = 'L';
				} else if (currentRoom.getIsGoal()) {
					currentChar = 'G';
				} else if (currentRoom.getIsAcessible()) {
					currentChar = 'C';
				} else if (currentRoom.getIsKeyRoom()) {
					currentChar = 'K';
				} else {
					currentChar = 'Q';
				}
				returnValue.append(currentChar);
			}
			returnValue.append('\n');
		}
		returnValue.append('\n');
		returnValue.append("Player has Key: " + this.hasKey);
		return returnValue.toString();

	}

	

}
