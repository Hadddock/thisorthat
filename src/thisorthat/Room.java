package thisorthat;

import java.io.Serializable;
import java.util.Observable;
/**
 * 
 * Room is an abstraction of a room for use in a Trivia maze program.
 *
 */
public class Room extends Observable implements Serializable{
	
	/**
	 * The question for this current room
	 */
	private Question myQuestion;
	/**
	 * Whether or not this room is freely movable to and from, starting location should always be accessible,
	 * and cleared or unlocked room should become accessible. Any uncleared or locked room should be inaccessible.
	 */
	private boolean isAcessible;
	/**
	 * If this room is locked. Rooms only become locked after failing the room's question.
	 */
	private boolean isLocked;
	/**
	 * If this room is the goal room. Game ends upon successfully reaching goal room.
	 */
	private boolean isGoal;
	/**
	 * If this room contains a key. Should become false after obtaining a key from this room.
	 */
	private boolean isKeyRoom;
	/**
	 * The X Coordinate of this room in its Maze.
	 */
	private int myXCoordinate;
	/**
	 * The Y Coordinate of this room in its Maze.
	 */
	private int myYCoordinate;

	/*
	 * Default Room for testing
	 * 
	 */
	public Room() {
		this.myQuestion = new Question();
		this.isAcessible = true;
		this.isLocked = false;
		this.isGoal = false;
		this.isKeyRoom = false;
	}
	
	public Room(Question theQuestion, boolean theAcessibleStatus, boolean theLockedStatus,boolean theGoalStatus, boolean theKeyRoomStatus) {
		this.myQuestion = theQuestion;
		this.isAcessible = theAcessibleStatus;
		this.isLocked = theLockedStatus;
		this.isGoal = theGoalStatus;
		this.isKeyRoom = theKeyRoomStatus;
	}
	
	public Room(Room copyRoom) {
		this.myQuestion = copyRoom.getMyQuestion();
		this.isAcessible = copyRoom.getIsAcessible();
		this.isLocked = copyRoom.getIsLocked();
		this.isGoal = copyRoom.getIsGoal();
		this.isKeyRoom = copyRoom.getIsKeyRoom();
	}
	
	/**
	 * Getter for myQuestion
	 * @return myQuestion
	 */
	public Question getMyQuestion() {
		return myQuestion;
	}
	/**
	 * Getter for isAcessible
	 * @return isAcessible
	 */
	boolean getIsAcessible() {
		return isAcessible;
	}

	/**
	 * Getter for isLocked
	 * @return isLocked
	 */
	boolean getIsLocked() {
		return isLocked;
	}
	
	
	/**
	 * Getter for isGoal
	 * @return isGoal
	 */
	boolean getIsGoal() {
		return isGoal;
	}
	

	/**
	 * Getter for isKeyRoom
	 * @return isKeyRoom
	 */
	boolean getIsKeyRoom() {
		return isKeyRoom;
	}
	/**
	 * Getter for myXCoordinate
	 * @return myXCoordinate
	 */
	int getMyXCoordinate() {
		return myXCoordinate;
	}
	
	/**
	 * Setter for myXCoordinate
	 * @param theXCoordinate, the new value for myXCoordinate
	 */
	void setMyXCoordinate(int theXCoordinate) {
		this.myXCoordinate = theXCoordinate;
		this.notifyObservers();
	}
	
	/**
	 * Getter for myYCoordinate
	 * @return myYCoordinate
	 */
	int getMyYCoordinate() {
		return myYCoordinate;
	}
	
	/**
	 * Setter for myYCoordinate
	 * @param theYCoordinate, the new value for myYCoordinate
	 */
	void setMyYCoordinate(int theYCoordinate) {
		this.myYCoordinate = theYCoordinate;
		this.notifyObservers();
	}
	
	
	/**
	 * Setter for isAcessible
	 * @param theAcessibility the new boolean value for isAcessible
	 */
	void setIsAcessible(boolean theAcessibility) {
		this.isAcessible = theAcessibility;
		this.notifyObservers();
	}
	
	/**
	 * Setter for isLocked
	 * @param theLockedStatus the new boolean value for isLocked
	 */
	void setIsLocked(boolean theLockedStatus) {
		this.isLocked = theLockedStatus;
		this.notifyObservers();
	}

	/**
	 * Setter for isKeyRoom
	 * @param theKeyStatus the new boolean value for isKeyRoom
	 */
	void setIsKeyRoom(boolean theKeyStatus) {
		this.isKeyRoom = theKeyStatus;
		this.notifyObservers();
	}

}
