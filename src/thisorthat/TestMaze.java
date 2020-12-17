package thisorthat;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import thisorthat.Maze;
import thisorthat.Question;
import thisorthat.Room;

class TestMaze {
	Maze testMaze;
	Room[][] testRooms;
	Room testRoomGullenge;
	/*
	 * 
	 * Key for mazes
	 * K = Key Room
	 * P = Player Position
	 * C = Cleared Room
	 * Q = Question Room
	 * L = Locked Room
	 * 
	 * Default maze layout for testing
	 * QGQ 
	 * KPQ
	 * QLQ
	 */
	@BeforeEach
	void buildUp() throws Exception {
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
		testMaze = new Maze(testRooms, 1, 1, false);
	}

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
	void testCheckWinPossible3() {
				// Make start room bottom right corner
				testRooms[2][2] = new Room(new Question(), true, false, false, false);
				
				//make left adjacent room unlocked
				testRooms[2][1] = new Room(new Question(), true, false, false, false);
				// make line of locked rooms blocking exit
				testRooms[1][2] = new Room(new Question(), false, true, false, false);
				testRooms[1][1] = new Room(new Question(), false, true, false, false);
				testRooms[1][0] = new Room(new Question(), false, true, false, false);
				// make key room
				testRooms[2][0] = new Room(new Question(), false, false, false, true);
				testMaze = new Maze(testRooms, 2, 2, false);
				assertTrue(testMaze.checkWinPossible());
	}
	/*
	 * 
	 * Check if win is possible when player has a key, and there is at most 
	 * one locked room between the player and a potentially winnable Game state
	 * without a key
	 * 
	 */
	@Test
	void testCheckWinPossible4() {
				// Make start room bottom right corner
				testRooms[2][2] = new Room(new Question(), true, false, false, true);
				
				// make line of locked rooms blocking exit
				testRooms[1][2] = new Room(new Question(), false, true, false, false);
				testRooms[1][1] = new Room(new Question(), false, true, false, false);
				testRooms[1][0] = new Room(new Question(), false, true, false, false);
				// make key room
				testRooms[2][0] = new Room(new Question(), false, false, false, true);
				testMaze = new Maze(testRooms, 2, 2, false);
				testMaze.obtainKey();
				assertTrue(testMaze.checkWinPossible());
	}

	@Test
	void testCheckWinCondition() {
		// Check win condition false when not at goal state
		assertFalse(testMaze.checkWinCondition());
		// set current position to be goal state
		testRooms[1][1] = new Room(new Question(), true, false, true, false);
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
		assertEquals(testMaze.getMyYPosition(), 1);
	}

	@Test
	void testGetMyXPosition() {
		assertEquals(testMaze.getMyXPosition(), 1);
	}

	@Test
	void testSetMyXPosition() {
		testMaze.setMyXPosition(0);
		assertEquals(testMaze.getMyXPosition(), 0);
		assertNotEquals(testMaze.getMyXPosition(), 1);
	}

	@Test
	void testSetMyYPosition() {
		testMaze.setMyYPosition(0);
		assertEquals(testMaze.getMyYPosition(), 0);
		assertNotEquals(testMaze.getMyYPosition(), 1);
	}

	@Test
	void testObtainKey() {
		// try and obtain key from room that isn't a key room
		testMaze.obtainKey();
		assertFalse(testMaze.getHasKey());
		testMaze.setMyXPosition(0);
		testMaze.obtainKey();
		assertTrue(testMaze.getHasKey());
	}

	@Test
	void testGetHasKey() {
		assertFalse(testMaze.getHasKey());
		// move to key position
		testMaze.setMyXPosition(0);
		testMaze.obtainKey();
		assertTrue(testMaze.getHasKey());
	}

	@Test
	void testUseKey() {
		testMaze.setMyXPosition(0);
		testMaze.obtainKey();
		assertTrue(testMaze.getHasKey());
		testMaze.useKey();
		// trying to use key on unlocked room intentionally doesn't work
		assertTrue(testMaze.getHasKey());
		assertFalse(testMaze.getMyRooms()[testMaze.getMyYPosition()][testMaze.getMyXPosition()].getIsLocked());
		// move to locked room
		testMaze.setMyXPosition(1);
		testMaze.setMyYPosition(2);
		// check that the room is locked before using
		assertTrue(testMaze.getMyRooms()[testMaze.getMyYPosition()][testMaze.getMyXPosition()].getIsLocked());
		// use the key, lose key, room unlocks
		testMaze.useKey();
		assertFalse(testMaze.getHasKey());
		assertFalse(testMaze.getMyRooms()[testMaze.getMyYPosition()][testMaze.getMyXPosition()].getIsLocked());
	}

	@Test
	void testGetMyRooms() {
		assertEquals(testMaze.getMyRooms(), testRooms);
	}

	@Test
	void testGetNeighbors() {
		List<Room> testList = new LinkedList<Room>();
		testList.add(testRooms[0][1]);
		testList.add(testRooms[1][2]);
		testList.add(testRooms[2][1]);
		testList.add(testRooms[1][0]);

		List<Room> testList2 = testMaze.getNeighbors(testMaze.getMyYPosition(), testMaze.getMyXPosition());
		for (int i = 0; i < testList2.size(); i++) {
			assertEquals(testList.get(i), testList2.get(i));
		}
		testList.clear();
		testList2.clear();
		// add corner rooms to list
		testList.add(testRooms[0][1]);
		testList.add(testRooms[1][0]);
		// move to corner
		testMaze.setMyXPosition(0);
		testMaze.setMyYPosition(0);
		testList2 = testMaze.getNeighbors(testMaze.getMyYPosition(), testMaze.getMyXPosition());
		for (int i = 0; i < testList2.size(); i++) {
			assertEquals(testList.get(i), testList2.get(i));
		}

	}

}
