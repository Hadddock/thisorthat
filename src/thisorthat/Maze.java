package thisorthat;
import java.util.Observable;
import java.util.Set;
import java.util.Stack;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("deprecation")
public class Maze extends Observable {
	private Room[][] myRooms;// the maze composed of a 2-D array of rooms
	private int myYPosition;// current y location of player in Room, [myYPosition][].
	private int myXPosition;// current x location of player in Room, [][myXPosition].
	private boolean hasKey; // does the player have a key in their possession

	public Maze(Room[][] theRooms, int theYPosition, int theXPosition, boolean theKeyStatus) {
		this.myRooms = theRooms;
		this.myYPosition = theYPosition;
		this.myXPosition = theXPosition;
		this.hasKey = theKeyStatus;
	}
	
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
	
	public boolean checkWinPossible() {
		int x = myXPosition;
		int y = myYPosition;
		Room current = this.myRooms[y][x];
		//check most common unsolvable condition, surrounded by locked or null rooms without a key
		if(!this.hasKey 
				&& (this.myRooms[y+1][x] == null || this.myRooms[y+1][x].getIsLocked())
				&& (this.myRooms[y-1][x] == null || this.myRooms[y-1][x].getIsLocked())
				&& (this.myRooms[y][x+1] == null || this.myRooms[y][x+1].getIsLocked())
				&& (this.myRooms[y][x-1] == null || this.myRooms[y][x-1].getIsLocked())) {
			return false;
		}
		
//		class mapRoom implements Comparator<mapRoom>{
//			int myXCoordinate;
//			int myYCoordinate;
//			int myScore;
//			public mapRoom(int theYCoordinate, int theXCoordinate, int theScore) {
//				this.myYCoordinate = theYCoordinate;
//				this.myXCoordinate = theXCoordinate;
//				this.myScore = theScore;
//			}
//			public int compare(mapRoom theMapRoomOne, mapRoom theMapRoomTwo) {
//				return theMapRoomOne.myScore - theMapRoomTwo.myScore;
//			}
//		}
////		
//		HashSet<mapRoom> visited = new HashSet<mapRoom>();
//		PriorityQueue<mapRoom> priorityQueue = new PriorityQueue<>();
//		
//		for(int i = 0; i < this.myRooms.length; i++) {
//			for(int j = 0; j < this.myRooms[0].length; j++) {
//				priorityQueue.add(new mapRoom(i,j,(Math.abs(myGoalXPosition - j) + Math.abs(myGoalYPosition - i))));
//			}
//		}
//		
//		while(!priorityQueue.isEmpty()) {
//			current = priorityQueue.;
//		}
		boolean potentialKey = this.hasKey;
		Set<Room> set = new HashSet<Room>();
		Set<Room> lockedNeighbors = new HashSet<Room>();
		Stack<Room> stack = new Stack<Room>();
		stack.add(current);
		
		
		while(!stack.isEmpty()) {
			Room element = stack.pop();
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
					set.add(n);
				}
				else {
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
		
		while(!stack.isEmpty()) {
			Room element = stack.pop();
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
					set.add(n);
				}
			}
		}
		return false;
	}
	
	public List<Room> getNeighbors(int y, int x) {
		List<Room> returnValue = new LinkedList<Room>();
		if(this.myRooms[y-1][x] != null) {
			returnValue.add(this.myRooms[y-1][x]);
		}
		if(this.myRooms[y][x+1] != null) {
			returnValue.add(this.myRooms[y][x+1]);
		}
		
		if(this.myRooms[y+1][x] != null) {
			returnValue.add(this.myRooms[y+1][x]);
		}
		
		if(this.myRooms[y][x-1] != null) {
			returnValue.add(this.myRooms[y][x-1]);
		}
		return returnValue;
	}

	


	/*
	 * If player has obtained a key from a room, set hasKey to true and remove the
	 * key from the room.
	 */
	public void obtainKey() {
		// if giving player key and current room contains a key
		if (this.myRooms[myYPosition][myXPosition].getIsKeyRoom()) {
			this.myRooms[myYPosition][myXPosition].setIsKeyRoom(false);
			this.hasKey = true;
		}
	}
	
	public void useKey() {
		if (this.myRooms[myYPosition][myXPosition].getIsLocked()) {
			this.myRooms[myYPosition][myXPosition].setIsLocked(false);
			this.myRooms[myYPosition][myXPosition].setIsAcessible(true);
			this.hasKey = false;
		}
	}

	public Room[][] getMyRooms() {
		return myRooms;
	}

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
