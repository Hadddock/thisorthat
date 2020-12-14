package thisorthat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Thisorthat_Test {
private static final int UP =  	38;
private static final int RIGHT = 39;
private static final int DOWN = 40;
private static final int LEFT = 37;
private static final int ESCAPE = 27;

Maze currentMaze;
Game currentGame;
Display currentDisplay;
Scanner scan = new Scanner(System.in);
//SETUP-----------------------------------------------------------------------------------SETUP
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		
	}

	/*
	 * Construct Test maze with this layout before each test QGQ KPQ QLQ
	 * 
	 * Q = Question Room K = Key Room G = Goal Room P = Player's current position,
	 * accessible cleared room.
	 */
	@BeforeEach
	void setUp() throws Exception {
		Room[][] testRooms = new Room[3][3];
		// make Gullunge/basic question room
		Room testRoomGullenge = new Room(new Question(), false, false, false, false);
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

		int testYPosition = 1;
		int testXPositon = 1;
		boolean testKeyStatus = false;
		currentMaze = new Maze(testRooms, testYPosition, testXPositon, testKeyStatus);
		currentGame = new Game(currentMaze);
	}

	@AfterEach
	void tearDown() throws Exception {
		//TODO fix this
	}

//MODEL TESTS-----------------------------------------------------------------------------------MODEL TESTS	
	@Test
	void testObtainKey() {
		// false due to current room not containing key.
		currentMaze.obtainKey();
		Assert.assertFalse(currentMaze.getHasKey());
		// true due to moving to being in a room with a key
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()].setIsKeyRoom(true);
		currentMaze.obtainKey();
		// check that the player now has the key, and the room they got the key from no
		// longer has a key
		Assert.assertTrue(currentMaze.getHasKey());
		Assert.assertFalse(currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()].getIsKeyRoom());
		
	}
	
//CONTROLLER TESTS-----------------------------------------------------------------------------------CONTROLLER TESTS	

	@Test
	void testPromptMovement() {
//		currentGame.promptMovement();
	}
	
	@Test
	void testRecieveMovementSelectionUp() {
//		currentGame.receiveMovementSelection(UP);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionLeft() {
		//press left arrow key
//		currentGame.receiveMovementSelection(LEFT);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionRight() {
		//press right arrow key
//		currentGame.receiveMovementSelection(RIGHT);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionDownLocked() {
		//press down arrow key
//		currentGame.receiveMovementSelection(DOWN);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionDownUnlocked() {
		//press down arrow key, player now has the key
		currentMaze.setHasKey(true);
//		currentGame.receiveMovementSelection(DOWN);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionLeftRight() {
		//press left, then right arrow key
//		currentGame.receiveMovementSelection(LEFT);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
//		currentGame.receiveMovementSelection(RIGHT);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}


	@Test
	void testPromptQuestionSelectionLocked() {
//		currentGame.receiveMovementSelection(2);
//		boolean questionSelect = currentGame.promptQuestion(currentMaze.getMyYPosition(), currentMaze.getMyXPosition());
//		Assert.assertFalse(questionSelect);
	}
	
	void promptQuestionSelection(int moving, int status) {
//		currentGame.receiveMovementSelection(moving);
//		boolean questionSelect = currentGame.promptQuestion(currentMaze.getMyYPosition(), currentMaze.getMyXPosition());
//		Assert.assertTrue(questionSelect);
	}
	
	@Test
	void testPauseMenu() {
		//TODO fix
//		Assert.assertEquals(2, currentGame.promptPauseMenu());
	}
	
	@Test
	void testSaveGame() {
		//TODO fix (might use Serializable), create a new save file
//		currentGame.saveGame();
	}
	
	@Test
	void testLoadGame() {
		//TODO, fix if we use Serializable instead
		//check that the save file from testSaveGame is available to access
		Room[][] roomComparison = currentMaze.getMyRooms();
		boolean keyComparison = currentMaze.getHasKey();
		int xComparison = currentMaze.getMyXPosition();
		int yComparison = currentMaze.getMyYPosition();
		
//		currentGame.loadGame();
		
//		assertTrue(Arrays.deepEquals(currentGame.getMyMaze().getMyRooms(), roomComparison));
//		assertTrue(currentGame.getMyMaze().getHasKey() == keyComparison);
//		assertTrue(currentGame.getMyMaze().getMyXPosition() == xComparison);
//		assertTrue(currentGame.getMyMaze().getMyYPosition() == yComparison);
//		currentGame.receiveMovementSelection(1);
		//confirm different
//		assertTrue(Arrays.deepEquals(currentGame.getMyMaze().getMyRooms(), roomComparison));
	}
	@Test
	void testExitGame() {
		//TODO fix
//		currentGame.exitGame();
	}
	
	@Test
	void testWinCondition() {
		//TODO fix AssertionError
		Room goal = currentMaze.getMyRooms()[0][1];

	}
	
	@Test
	void testWinPosible() {
		Room goal = currentMaze.getMyRooms()[0][1];
		currentMaze.getMyRooms()[0][1].getIsAcessible();
		Assert.assertTrue(currentMaze.checkWinPossible());
		Room position = currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()];
//		currentGame.receiveMovementSelection(1);
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()-1][currentMaze.getMyXPosition()].setIsLocked(true);
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()-1].setIsLocked(true);
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()+1][currentMaze.getMyXPosition()].setIsLocked(true);
		Assert.assertFalse(currentMaze.checkWinPossible());
	}
	
//DISPLAY TESTS-----------------------------------------------------------------------------------DISPLAY TESTS	

	@Test
	void testShowMaze() {
		try {
			currentDisplay.showMaze(currentMaze);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testShowQuestion() {
		currentDisplay.showQuestion(currentMaze.getMyRooms()[0][0].getMyQuestion());
	}

	@Test
	void testShowPauseMenu() {
		currentDisplay.showPauseMenu();
	}

	@Test
	void testDisplayWinScreen() {
//		currentDisplay.displayWinScreen();
	}

	@Test
	void testDisplayLoseScreen() {
		currentDisplay.displayLoseScreen();
	}
}
