package thisorthat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Thisorthat_Test {
public static final int UP = 0;
public static final int RIGHT = 1;
public static final int DOWN = 2;
public static final int LEFT = 3;

Maze currentMaze;
Game currentGame;
Scanner scan = new Scanner(System.in);
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
		currentGame.closeFrame();
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

	@Test
	void testCheckWinPossible() {
		assertTrue(currentMaze.checkWinPossible());
	}
//CONTROLLER TESTS-----------------------------------------------------------------------------------CONTROLLER TESTS	

	
	
	@Test
	void testPromptMovement() {
		currentGame.promptMovement();
	}
	
	//be prepared for this test to fail once we implement the code that ends the game after winning
	@Test
	void testRecieveMovementSelectionUp() {

		currentGame.receiveMovementSelection(UP);
		//Assert.assertEquals(currentMaze.getMyRooms()[0][1], currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()]);
		Assert.assertTrue(currentMaze.getMyYPosition() == 0 && currentMaze.getMyXPosition() == 1);
	}
	
	@Test
	void testRecieveMovementSelectionLeft() {
		currentGame.receiveMovementSelection(LEFT);
		Assert.assertTrue(currentMaze.getMyYPosition() == 1 && currentMaze.getMyXPosition() == 0);
	}
	
	@Test
	void testRecieveMovementSelectionRight() {
		currentGame.receiveMovementSelection(RIGHT);
		Assert.assertTrue(currentMaze.getMyYPosition() == 1 && currentMaze.getMyXPosition() == 2);
	}
	
	@Test
	void testRecieveMovementSelectionDownLocked() {
		currentGame.receiveMovementSelection(DOWN);
		Assert.assertTrue(currentMaze.getMyYPosition() == 1 && currentMaze.getMyXPosition() == 1);
	}
	
	@Test
	void testRecieveMovementSelectionDownUnlocked() {
		currentMaze.setHasKey(true);
		currentGame.receiveMovementSelection(2);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(2, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionLeftRight() {
		currentGame.receiveMovementSelection(3);
		Assert.assertEquals(0, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
		currentGame.receiveMovementSelection(1);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}

	@Test
	void testPromptQuestionSelectionUnlocked1() {
		promptQuestionSelection(0, 0);
		promptQuestionSelection(1, 0);
		promptQuestionSelection(3, 0);
	}
	
	@Test
	void testPromptQuestionSelectionLocked() {
		currentGame.receiveMovementSelection(2);
		boolean questionSelect = currentGame.promptQuestion(currentMaze.getMyYPosition(), currentMaze.getMyXPosition());
		Assert.assertFalse(questionSelect);
	}
	
	void promptQuestionSelection(int moving, int status) {
		currentGame.receiveMovementSelection(moving);
		boolean questionSelect = currentGame.promptQuestion(currentMaze.getMyYPosition(), currentMaze.getMyXPosition());
		Assert.assertTrue(questionSelect);
	}
	
	@Test
	void testPauseMenu() {
		Assert.assertEquals(2, currentGame.promptPauseMenu());
	}
	
	@Test
	void testSaveGame() {
		//create a new save file
		currentGame.saveGame();
		Assert.assertTrue(currentGame.checkFileExists());
		File saveFile = new File("saveFile.txt");
		Assert.assertTrue(saveFile.exists());
	}
	
	@Test
	void testLoadGame() {
		//check that the save file from testSaveGame is available to access
		Room[][] roomComparison = currentMaze.getMyRooms();
		boolean keyComparison = currentMaze.getHasKey();
		int xComparison = currentMaze.getMyXPosition();
		int yComparison = currentMaze.getMyYPosition();
		
		currentGame.saveGame();
		currentGame.loadGame();
		
		assertTrue(Arrays.deepEquals(currentGame.getMyMaze().getMyRooms(), roomComparison));
		assertTrue(currentGame.getMyMaze().getHasKey() == keyComparison);
		assertTrue(currentGame.getMyMaze().getMyXPosition() == xComparison);
		assertTrue(currentGame.getMyMaze().getMyYPosition() == yComparison);

		currentGame.receiveMovementSelection(1);
		//confirm different
		assertTrue(Arrays.deepEquals(currentGame.getMyMaze().getMyRooms(), roomComparison));
	}
	@Test
	void testExitGame() {
		currentGame.exitGame();
	}
	
	@Test
	void testWinConditionTrue() {
		currentGame.receiveMovementSelection(UP);
		Assert.assertTrue(currentGame.checkWinCondition());

	}
	
	@Test
	void testWinConditionFalse() {
		Assert.assertFalse(currentGame.checkWinCondition());
	}
	
	@Test
	void testWinPosible() {
		//winnable maze
		Assert.assertTrue(currentGame.checkWinPossible());
		Room position = currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()];
		currentGame.receiveMovementSelection(1);
    //create unwinnable conditions
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()-1][currentMaze.getMyXPosition()].setIsLocked(true);
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()-1].setIsLocked(true);
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()+1][currentMaze.getMyXPosition()].setIsLocked(true);
		currentMaze.getMyRooms()[1][2].setIsAcessible(true);
		currentGame.receiveMovementSelection(RIGHT);
		currentMaze.getMyRooms()[1][1].setIsLocked(true);
		currentMaze.getMyRooms()[1][1].setIsAcessible(false);
		currentMaze.getMyRooms()[0][2].setIsLocked(true);
		currentMaze.getMyRooms()[2][2].setIsLocked(true);
		//confirm unwinnable
		Assert.assertFalse(currentGame.checkWinPossible());
	}
	
//DISPLAY TESTS-----------------------------------------------------------------------------------DISPLAY TESTS	

}
