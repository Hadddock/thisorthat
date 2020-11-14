package thisorthat;

import java.util.Observable;

@SuppressWarnings("deprecation")
public class Maze extends Observable {
	private Room[][] myRooms;//the maze composed of a 2-D array of rooms
	private int myYPosition;//current y location of player in Room, [myYPosition][].
	private int myXPosition;//current x location of player in Room, [][myXPosition].
	private boolean hasKey; //does the player have a key in their possession

	public Maze(Room[][] theRooms, int theYPosition, int theXPosition,boolean theKeyStatus) {
		this.myRooms = theRooms;
		this.myYPosition = theYPosition;
		this.myXPosition = theXPosition;
		this.hasKey = theKeyStatus;
	}

	public int getMyYPosition() {
		return myYPosition;
	}
	
	public int getMyXPosition() {
		return myXPosition;
	}

	public void setMyYPosition(int myYPosition) {
		this.myYPosition = myYPosition;
	}

	public void setMyXPosition(int myXPosition) {
		this.myXPosition = myXPosition;
	}

	public boolean getHasKey() {
		return hasKey;
	}

	/*
	 * If player has obtained a key from a room, set hasKey to true and remove the key from the room.
	 */
	public void obtainKey() {
		//if giving player key and current room contains a key
		if(this.myRooms[myYPosition][myXPosition].getIsKeyRoom()) {
			this.hasKey = true;
			this.myRooms[myYPosition][myXPosition].setIsKeyRoom(false);
		}
	}

	public Room[][] getMyRooms() {
		return myRooms;
	}

}
