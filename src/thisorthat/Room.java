package thisorthat;

public class Room implements java.io.Serializable {
	
	/**
	 * The question for this current room
	 */
	private Question myQuestion;
	/**
	 * Whether or not this room is freely movable to and from, starting location should always be acessible,
	 * and cleared or unlocked room should become acessible. Any uncleared or locked room should be inacessible.
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

	public Question getMyQuestion() {
		return myQuestion;
	}

	boolean getIsAcessible() {
		return isAcessible;
	}

	boolean getIsLocked() {
		return isLocked;
	}

	boolean getIsGoal() {
		return isGoal;
	}

	boolean getIsKeyRoom() {
		return isKeyRoom;
	}

	int getMyXCoordinate() {
		return myXCoordinate;
	}	

	void setMyXCoordinate(int myXCoordinate) {
		this.myXCoordinate = myXCoordinate;
	}

	int getMyYCoordinate() {
		return myYCoordinate;
	}

	void setMyYCoordinate(int myYCoordinate) {
		this.myYCoordinate = myYCoordinate;
	}

	void setIsAcessible(boolean isAcessible) {
		this.isAcessible = isAcessible;
	}

	void setIsLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	void setIsKeyRoom(boolean containsKey) {
		this.isKeyRoom = containsKey;
	}

}
