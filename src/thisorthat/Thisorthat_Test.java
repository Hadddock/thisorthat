package thisorthat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Thisorthat_Test {
Maze currentMaze;
Game currentGame;
//SETUP-----------------------------------------------------------------------------------SETUP
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/*
	 * Construct Test maze with this layout before each test
	 * QGQ
	 * KPQ
	 * QLQ
	 * 
	 * Q = Question Room
	 * K = Key Room
	 * G = Goal Room
	 * P = Player's current position, accessible cleared room.
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
		
		//make start room
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
		//TODO replace null with the test display variable
		currentGame = new Game(currentMaze, null);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

//MODEL TESTS-----------------------------------------------------------------------------------MODEL TESTS	
	@Test
	void testObtainKey() {
		// false due to current room not containing key.
		currentMaze.obtainKey();
		Assert.assertFalse(currentMaze.getHasKey());
		//true due to moving to being in a room with a key
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()].setIsKeyRoom(true);
		currentMaze.obtainKey();
		//check that the player now has the key, and the room they got the key from no longer has a key
		Assert.assertTrue(currentMaze.getHasKey());
		Assert.assertFalse(currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()].getIsKeyRoom());
		
	}
	
//CONTROLLER TESTS-----------------------------------------------------------------------------------CONTROLLER TESTS	

	@Test
	void testPromptMovement() {
		currentGame.promptMovement();
	}
	
	@Test
	void testRecieveMovementSelectionUp() {
		currentGame.receiveMovementSelection(0);
		Assert.assertEquals([0][1], currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()]);
	}
	
	@Test
	void testRecieveMovementSelectionLeft() {
		currentGame.receiveMovementSelection(3);
		Assert.assertEquals([1][0], currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()]);
	}
	
	@Test
	void testRecieveMovementSelectionRight() {
		currentGame.receiveMovementSelection(1);
		Assert.assertEquals([1][2], currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()]);
	}
	
	@Test
	void testRecieveMovementSelectionDownLocked() {
		currentGame.receiveMovementSelection(2);
		Assert.assertEquals([1][1], currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()]);
	}
	
	@Test
	void testRecieveMovementSelectionDownUnlocked() {
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()].setIsKeyRoom(true);
		currentMaze.obtainKey();
		currentGame.receiveMovementSelection(2);
		Assert.assertEquals([2][1], currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()]);
	}

	@Test
	void testPromptQuestionSelectionUnlocked1() {
		promptQuestionSelection(0, 0);
		promptQuestionSelection(1, 0);
		promptQuestionSelection(3, 0);
	}
	
	@Test
	void testPromptQuestionSelectionLocked() {
		promptQuestionSelection(2, 1);
	}
	
	void promptQuestionSelection(int moving, int status) {
		currentGame.receiveMovementSelection(moving);
		int questionSelect = currentGame.promptQuestion(currentMaze.getMyYPosition, currentMaze.getMyXPosition);
		Assert.assertEquals(status, questionSelect);
	}
	
	@Test
	void testPauseMenu() {
		Assert.assertEquals(2, currentGame.promptPauseMenu());
	}
	
	@Test
	void testSaveGame() {
		//create a new save file
		currentGame.saveGame();
		Assert.assertEquals("saveFile.txt", currentGame.fileExists);
	}
	
	@Test
	void testLoadGame() {
		//check that the save file from testSaveGame is available to access
		currentGame.loadGame();
		currentGame.checkFileExists = true;
	}
	
	@Test
	void testExitGame() {
		currentGame.exitGame();
		
	}
	
	@Test
	void testWinCondition() {
		Room goal = currentMaze.getMyRooms()[0][1];
		Room current = currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()];
		Assert.assertFalse(currentGame.checkWinCondition());
		currentGame.receiveMovementSelection(1);
		Assert.assertFalse(currentGame.checkWinCondition());
		currentGame.receiveMovementSelection(0);
		Assert.assertFalse(currentGame.checkWinCondition());
		currentGame.receiveMovementSelection(3);
		Assert.assertTrue(currentGame.checkWinCondition());		
	}
	
	@Test
	void testWinPosible() {
		Room goal = currentMaze.getMyRooms()[0][1];
		currentMaze.getMyRooms()[0][1].getIsAcessible();
		Assert.assertTrue(currentGame.checkWinPossible());
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()];
		currentGame.receiveMovementSelection(1);
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()-1][currentMaze.getMyXPosition()].setIsLocked(true);
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()-1].setIsLocked(true);
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()+1][currentMaze.getMyXPosition()].setIsLocked(true);
		Assert.assertFalse(currentGame.checkWinPossible());
	}
	
//DISPLAY TESTS-----------------------------------------------------------------------------------DISPLAY TESTS	

}
