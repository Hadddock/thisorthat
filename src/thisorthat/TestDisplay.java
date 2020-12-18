package thisorthat;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Robot;
import java.awt.event.InputEvent;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestDisplay {
	static Maze testMaze;
	static Room[][] testRooms;
	static Room testRoomGullenge;
	static Display testDisplay;
	static Robot bot;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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
		testDisplay = new Display(testMaze);
		bot = new Robot();
	}

	// REQUIRES USER INPUT IN THIS SEQUENCE
	// UP KEY
	// RIGHT KEY
	// DOWN KEY
	// LEFT KEY
	// ESC KEY
	@Test
	void testAcceptMazeInput() {
		assertEquals(testDisplay.acceptMazeInput(), 38); // UP
		assertEquals(testDisplay.acceptMazeInput(), 39); // RIGHT
		assertEquals(testDisplay.acceptMazeInput(), 40); // DOWN
		assertEquals(testDisplay.acceptMazeInput(), 37); // LEFT
		assertEquals(testDisplay.acceptMazeInput(), 27); // ESC
		assertNotEquals(testDisplay.acceptMazeInput(), -1);
	}

	// REQUIRES USER INPUT IN THIS SEQUENCE
	// SELECT CORRECT ANSWER
	// SELECT INCORRECT ANSWER
	@Test
	void testShowQuestion() {
		assertTrue(testDisplay.showQuestion(testRoomGullenge.getMyQuestion()));
		
		assertFalse(testDisplay.showQuestion(testRoomGullenge.getMyQuestion()));
	}

	// REQUIRES USER INPUT IN THIS SEQUENCE
	// RESUME BUTTON
	// SAVE BUTTON
	// LOAD BUTTON
	// EXIT BUTTON -> YES BUTTON
	// EXIT BUTTON -> NO BUTTON
	@Test
	void testShowPauseMenu() {
		assertEquals(testDisplay.showPauseMenu(), 7); // RESUME
		assertEquals(testDisplay.showPauseMenu(), 5); // SAVE
		assertEquals(testDisplay.showPauseMenu(), 6); // LOAD
		assertEquals(testDisplay.showPauseMenu(), 8); // EXIT - YES PATH
		assertEquals(testDisplay.showPauseMenu(), 7); // EXIT - NO PATH
	}

}
