package thisorthat;
import java.util.Set;
import java.util.Stack;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 
 * Maze is an abstraction of a collection of rooms and the status of a player in
 * the Maze for use in a Trivia maze program.
 */
@SuppressWarnings("serial")
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
	 * Size of the maze (width and height)
	 */
	private static final int MAZE_SIZE = 9;
	
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
	 * Constructor for Maze for use in tests
	 */
	public Maze() {
		Room[][] maze = new Room[MAZE_SIZE][MAZE_SIZE];
		Random rand = new Random();
		int keyX = generateRandomTen();
		int keyY = generateRandomTen();
		int goalX = generateRandomTen();
		int goalY = generateRandomTen();
		// loop for creating a random  maze with dimensions MAZE_SIZE x MAZE_SIZE 
		for(int i = 0; i < MAZE_SIZE; i++) {
			for(int j = 0; j < MAZE_SIZE; j++) {
				// make key room
				if((i == keyX && j == keyY)) {
					maze[i][j] = new Room(new Question(), false, false, false, true);
				} else if (i == goalX && j == goalY) { // make goal room
					maze[i][j] = new Room(new Question(), false, false, true, false);
				} else if (i == MAZE_SIZE/2 && j == MAZE_SIZE/2) { // make start room
					maze[i][j] = new Room(new Question(), true, false, false, false);
				} else {
					// make a random question room
					maze[i][j] = new Room(new Question(),false,false,false,false);
				}
			}
		}
		this.myRooms = maze;
		this.myYPosition =  MAZE_SIZE/2;
		this.myXPosition = MAZE_SIZE/2;
	}

	/**
	 * helper method to generate key and goal positions
	 * @return random int between 0 and 8
	 */
	int generateRandomTen() {
		Random rand = new Random();
		int randPosition = rand.nextInt(8);
		while(randPosition == MAZE_SIZE/2) {
			randPosition = rand.nextInt(8);
		}
		return randPosition;
	}

	
	/**
	 * Getter for myYPosition
	 * @return myYPosition
	 */
	int getMyYPosition() {
		return myYPosition;
	}
	/**
	 * Getter for myXPosition
	 * @return myXPosition
	 */
	int getMyXPosition() {
		return myXPosition;
	}
	/**
	 * Setter for myYPosition
	 */
	void setMyYPosition(int myYPosition) {
		this.myYPosition = myYPosition;

	}
	/**
	 * Setter for myXPosition
	 */
	void setMyXPosition(int myXPosition) {
		this.myXPosition = myXPosition;

	}
	/**
	 * Getter for hasKey
	 * @return hasKey
	 */
	boolean getHasKey() {
		return hasKey;
	}
	/**
	 * Check if there exists a room at the specified coordinates in the maze
	 * @param theYPosition checked to see if it exists in the bounds of the maze
	 * @param theXPosition checked to see if it exists in the bounds of the maze
	 * @return whether or not there exists a room at the specified coordinates in the maze
	 */
	boolean checkInBounds(int theYPosition, int theXPosition) {
		if (theXPosition >= 0 && theXPosition <= this.myRooms[0].length - 1 && theYPosition >= 0 && theYPosition <= this.myRooms.length - 1) {
			return true;
		}
		return false;
	}
	/**
	 * Check if the current state of the Maze is potentially completable by the player
	 * @return if the current state of the Maze is potentially completable by the player
	 */
	boolean checkWinPossible() {

		Room currentRoom = this.myRooms[myYPosition][myXPosition];
		currentRoom.setMyXCoordinate(myXPosition);
		currentRoom.setMyYCoordinate(myYPosition);

		// begin depth first search on key
		boolean potentialKey = this.hasKey;
		Set<Room> processedRooms = new HashSet<Room>();
		Set<Room> lockedNeighbors = new HashSet<Room>();
		Stack<Room> roomsToBeProcessed = new Stack<Room>();
		
		int currentXPosition;
		int currentYPosition;
		roomsToBeProcessed.add(currentRoom);
		
		do {
			while (!roomsToBeProcessed.isEmpty() ) {
				currentRoom = roomsToBeProcessed.pop();
				currentXPosition = currentRoom.getMyXCoordinate();
				currentYPosition = currentRoom.getMyYCoordinate();
				if (currentRoom.getIsGoal()) {
					return true;
				}
				
				if (currentRoom.getIsKeyRoom()) {
					potentialKey = true;
				}
				processedRooms.add(currentRoom);
				
				List<Room> neighbors = getNeighbors(currentYPosition, currentXPosition);
				for (int i = 0; i < neighbors.size(); i++) {
					Room n = neighbors.get(i);
					if (!n.getIsLocked()) {
						if (!processedRooms.contains(n) && !roomsToBeProcessed.contains(n)) {
							roomsToBeProcessed.add(n);
						}
					}

					else if (!lockedNeighbors.contains(n)) {
						lockedNeighbors.add(n);
					}
				}
			}
			// if a key is in posession or is acessible,
			if (potentialKey) {
				for (Room lockedRoom : lockedNeighbors) {
					roomsToBeProcessed.add(lockedRoom);
				}
				potentialKey = false;
			}
		} while (potentialKey || !roomsToBeProcessed.isEmpty());
		//second time around assuming key has been used somewhere
		return false;
	}
	/**
	 * Check if if the player has completed the maze by reaching a goal room
	 * @return if the player has completed the maze by reaching a goal room
	 */
	boolean checkWinCondition() {
		return this.myRooms[this.myYPosition][this.myXPosition].getIsGoal();
	}

	/**
	 * Check if if the player has completed the maze by reaching a goal room
	 * @param theYPosition the Y position of the room whose neighbors will be returned in a list
	 * @param theXPosition the Y position of the room whose neighbors will be returned in a list
	 * @return A List of rooms adjacent to the room at the specified coordinates
	 */
	List<Room> getNeighbors(int theYPosition, int theXPosition) {
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
	void obtainKey() {
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
	void useKey() {
		if (this.myRooms[myYPosition][myXPosition].getIsLocked()) {
			this.myRooms[myYPosition][myXPosition].setIsLocked(false);
			this.myRooms[myYPosition][myXPosition].setIsAcessible(true);
			this.hasKey = false;
		}
	}

	/*
	 * Getter for myRooms
	 * @return myRooms
	 */
	Room[][] getMyRooms() {
		return myRooms;
	}
	/*
	 * toString method for this Maze for use in debugging
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
