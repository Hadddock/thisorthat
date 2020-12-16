package thisorthat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import thisorthat.Maze;
import thisorthat.Question;
import thisorthat.Room;

class TestMaze {
	Maze testMaze;
	Room[][] testRooms;
	Room testRoomGullenge;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@BeforeEach
	void buildUp() throws Exception{
		testRooms = new Room[3][3];
		// make Gullunge/basic question room
		testRoomGullenge = new Room(new Question(), false, false, false, false);
		testRooms[0][0] = new Room(testRoomGullenge);
		testRooms[0][2] = new Room(testRoomGullenge);
		testRooms[1][2] = new Room(testRoomGullenge);
		testRooms[2][0] = new Room(testRoomGullenge);
		testRooms[2][2] = new Room(testRoomGullenge);
		// make start room
		testRooms[1][1] = new Room(new Question(), true, false, false, false);

		// make goal room
		testRooms[0][1] = new Room(new Question(), true, false, true, false);
		// make key room
		testRooms[1][0] = new Room(new Question(), false, false, false, true);

		// make locked room
		testRooms[2][1] = new Room(new Question(), false, true, false, false);
		testMaze = new Maze(testRooms, 2, 2, false);
	}


	/*
	 * Construct Test maze with this layout before each test QGQ KPQ QLQ
	 * 
	 * Q = Question Room K = Key Room G = Goal Room P = Player's current position,
	 * accessible cleared room.
	 */
	@Test
	void testCheckWinPossible() throws Exception {
		assertTrue(testMaze.checkWinPossible());
	}

	@Test
	void testCheckWinPossible2() {
		// Make start room bottom right corner
		testRooms[2][2] = new Room(new Question(), true, false, false, false);
		// make locked rooms adjacent to bottom right corner
		testRooms[2][1] = new Room(new Question(), false, true, false, false);
		testRooms[1][2] = new Room(new Question(), false, true, false, false);
		testMaze = new Maze(testRooms, 2, 2, false);
		assertFalse(testMaze.checkWinPossible());
	}
	
	@Test
	void testCheckWinCondition() {
		// Check win condition false when not at goal state
		assertFalse(testMaze.checkWinCondition());
		//set current position to be goal state
		testRooms[1][1] =  new Room(new Question(), true, false, true, false);
		testMaze = new Maze(testRooms, 1, 1, false);
		assertTrue(testMaze.checkWinCondition());
	}
	@Test
	void testCheckInBounds() {
		assertFalse(testMaze.checkInBounds(-1, 0));
		assertFalse(testMaze.checkInBounds(0, -1));
		assertFalse(testMaze.checkInBounds(3, 0));
		assertFalse(testMaze.checkInBounds(0, 3));
		assertFalse(testMaze.checkInBounds(0, 100));
		assertFalse(testMaze.checkInBounds(100, 0));
		assertTrue(testMaze.checkInBounds(0, 0));
		assertTrue(testMaze.checkInBounds(2, 2));
	}
	
	@Test
	void testGetMyYPosition() {
		assertEquals(testMaze.getMyYPosition(),1);
	}
	
	@Test
	void testGetMyXPosition() {
		assertEquals(testMaze.getMyXPosition(),1);
	}
	@Test
	void testSetMyXPosition() {
		testMaze.setMyXPosition(0);
		assertEquals(testMaze.getMyXPosition(),0);
	}
	
	@Test
	void testSetMyYPosition() {
		testMaze.setMyYPosition(0);
		assertEquals(testMaze.getMyYPosition(),0);
	}
	
	

	
	
	
	

}
