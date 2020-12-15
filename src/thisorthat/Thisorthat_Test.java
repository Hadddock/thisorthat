package thisorthat;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Thisorthat_Test {
Maze currentMaze;
Game currentGame;
Display currentDisplay;
Robot robot;
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
		currentDisplay = new Display(currentMaze);
		currentGame = new Game(currentMaze);
		robot = new Robot();
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
		//true due to moving to being in a room with a key
		currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()].setIsKeyRoom(true);
		currentMaze.obtainKey();
		//check that the player now has the key, and the room they got the key from no longer has a key
		Assert.assertTrue(currentMaze.getHasKey());
		Assert.assertFalse(currentMaze.getMyRooms()[currentMaze.getMyYPosition()][currentMaze.getMyXPosition()].getIsKeyRoom());
		
	}
	
//CONTROLLER TESTS-----------------------------------------------------------------------------------CONTROLLER TESTS	
	
	@Test
	void testRecieveMovementSelectionUp() {
		currentGame.verifyAction(KeyEvent.VK_KP_UP);
		Assert.assertEquals(0, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionLeft() {
		currentMaze.getMyRooms()[1][0].setIsAcessible(true);
		//press left arrow key
		currentGame.verifyAction(KeyEvent.VK_LEFT);
		Assert.assertEquals(0, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionRight() {
		currentMaze.getMyRooms()[1][2].setIsAcessible(true);
		//press right arrow key
		currentGame.verifyAction(KeyEvent.VK_RIGHT);
		Assert.assertEquals(2, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionDownLocked() {
		//press down arrow key
		currentGame.verifyAction(KeyEvent.VK_DOWN);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionDownUnlocked() {
		//press down arrow key, player now has the key
		currentMaze.setHasKey(true);
		currentGame.verifyAction(KeyEvent.VK_DOWN);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(2, currentMaze.getMyYPosition());
	}
	
	@Test
	void testRecieveMovementSelectionLeftRight() {
		currentMaze.getMyRooms()[1][0].setIsAcessible(true);
		//press left, then right arrow key
		currentGame.verifyAction(KeyEvent.VK_LEFT);
		Assert.assertEquals(0, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
		currentGame.verifyAction(KeyEvent.VK_RIGHT);
		Assert.assertEquals(1, currentMaze.getMyXPosition());
		Assert.assertEquals(1, currentMaze.getMyYPosition());
	}
	
	@Test
	void testSaveGame() {
		currentGame.saveGame();
		File save = new File("triviaMaze.ser");
		Assert.assertTrue(save.exists());
	}
	
	@Test
	void testLoadGame() {
		Maze m;
		try {
			FileInputStream fileIn = new FileInputStream("./triviaMaze.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			m = (Maze) in.readObject();
	        in.close();
	        fileIn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//check that the save file from testSaveGame is available to access
		Room[][] roomComparison = currentMaze.getMyRooms();
		boolean keyComparison = currentMaze.getHasKey();
		int xComparison = currentMaze.getMyXPosition();
		int yComparison = currentMaze.getMyYPosition();
		
		currentGame.loadGame();
		
		assertTrue(Arrays.deepEquals(currentMaze.getMyRooms(), roomComparison));
		assertTrue(currentMaze.getHasKey() == keyComparison);
		assertTrue(currentMaze.getMyXPosition() == xComparison);
		assertTrue(currentMaze.getMyYPosition() == yComparison);
		currentGame.verifyAction(1);
		//confirm different
		assertTrue(Arrays.deepEquals(currentMaze.getMyRooms(), roomComparison));
	}
	
	@Test
	void testWinCondition() {
		currentMaze.setMyXPosition(1);
		currentMaze.setMyYPosition(0);
		boolean winCondition = currentMaze.checkWinCondition();
		Assert.assertTrue(winCondition);
	}
	
	@Test
	void testWinPosible() {
		Room goal = currentMaze.getMyRooms()[0][1];
		goal.getIsAcessible();
		Assert.assertTrue(currentMaze.checkWinPossible());
		currentGame.verifyAction(1);
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
		currentDisplay.displayWinScreen();
	}
	
	@Test
	void testDisplayLoseScreen() {
		currentDisplay.displayLoseScreen();
	}
	
}
