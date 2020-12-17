package thisorthat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
/*
 * Game is trivia maze program where players navigate to the exit of a maze
 * by successfully answering trivia questions.
 */
public class Game {

	/*
	 * Key Event code for up arrow key
	 */
	public static final int UP =  	38;
	/*
	 * Key Event code for right arrow key
	 */
	public static final int RIGHT = 39;
	/*
	 * Key Event code for down arrow key
	 */
	public static final int DOWN = 40;
	/*
	 * Key Event code for left arrow key
	 */
	public static final int LEFT = 37;
	/*
	 * Key Event code for escape key
	 */
	public static final int ESCAPE = 27;
	/*
	 * Key Event code for save key
	 */
	public static final int SAVE = 5;
	/*
	 * int to represent selecting Load option
	 */
	public static final int LOAD = 6;
	/*
	 * int to represent selecting Resume option
	 */
	public static final int RESUME = 7;
	/*
	 * int to represent selecting Exit option
	 */
	public static final int EXIT = 8;
	/*
	 * The Maze the user will navigate through
	 */
	private Maze myMaze;
	/*
	 * The Display that will display and receive input from the user
	 */
	private Display myDisplay;
	private boolean isFinished = false;
	/*
	 * Constructor for a Game
	 * @param theMaze theMaze to be used when
	 */
	public Game(Maze theMaze) {
		this.myMaze = theMaze;
		try {
			this.myDisplay = new Display(this.myMaze);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Main loop for running the game. Gets selected action from user, verifies the
	 * action and calls appropriate method, and repeats until goal is reached or
	 * inaccessible.
	 */
	private void playGame() throws IOException  {
		int selectedAction;
		int pauseSelection;
		while (!this.isFinished) {
			try {
				myDisplay.showMaze(this.myMaze);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//get action from GUI
			selectedAction = myDisplay.acceptMazeInput();
			//check if the action is valid, if so perform
			//if pausing, perform selected pause function
			if(selectedAction == ESCAPE) {
				pauseSelection = myDisplay.showPauseMenu();
				performPauseSelection(pauseSelection);
				if(pauseSelection == LOAD) {
					myDisplay.showMaze(this.myMaze);
				}
			}
			//if moving
			else {
				this.verifyAction(selectedAction);
				//check if game is over, either by reaching goal or locking off path to goal
				this.isFinished = this.myMaze.checkWinCondition();
				if(!this.isFinished) {
					this.isFinished = !this.myMaze.checkWinPossible();
				}
			}
			
		}
		//if at goal, display win screen
		if(this.myMaze.getMyRooms()[this.myMaze.getMyYPosition()][this.myMaze.getMyXPosition()].getIsGoal()) {
			this.myDisplay.displayWinScreen();
			System.out.println("\nTHE GOAL HAS BEEN REACHED. YOU ARE THE NEW HIGH PRIEST OF IKEA"); 
		}
		//display winScreen
		else {
			this.myDisplay.displayLoseScreen();
		}
	}
	/*
	 * Verifies if the action the user is attempting to make matches the options available to the user,
	 * and calls the appropriate method of the action is valid.
	 * @param theSelectedDirection the direction relative to the player's current coordinates the
	 * player is attempting to move to
	 */
	void verifyAction(int theSelectedDirection) {
		int dy = 0, dx = 0;
		int x = this.myMaze.getMyXPosition();
		int y = this.myMaze.getMyYPosition();
		switch (theSelectedDirection) {
		case UP:
			dy--;
			break;
		case RIGHT:
			dx++;
			break;
		case DOWN:
			dy++;
			break;
		case LEFT:
			dx--;
			break;
		case ESCAPE:
			
		}
		// performAction if InBounds
		if (x + dx >= 0 && x + dx <= this.myMaze.getMyRooms()[0].length - 1 && y + dy >= 0
				&& y + dy <= this.myMaze.getMyRooms().length - 1) {
			performAction(dy, dx);
		}
	}
	
	/*
	 * Handle the player attempting to access a room, calling the appropriate method based on the player's key status
	 * and the fields of the room the player is attempting to access
	 * @param theDY the difference in Y position from the player's current Y Position in the maze to the room they are
	 * attempting to access
	 * @param theDX the difference in X position from the player's current X Position in the maze to the room they are
	 * attempting to access
	 */
	private void performAction(int theDY, int theDX) {
		Room attemptedRoom = this.myMaze.getMyRooms()[this.myMaze.getMyYPosition() + theDY][this.myMaze.getMyXPosition()
				+ theDX];
		boolean moveRooms = false;

		//if room is freely accessible, move there
		if (attemptedRoom.getIsAcessible()) {
			moveRooms = true;
			if (attemptedRoom.getIsGoal())
				this.isFinished = true;
		}
		// if attempting to move to locked room, only go into room if you have the key
		else if (attemptedRoom.getIsLocked()) {
			moveRooms = this.myMaze.getHasKey();
		}
		// if moving to question room, prompt the question for that room
		else if (!attemptedRoom.getIsAcessible()) {
			boolean correct = myDisplay.showQuestion(attemptedRoom.getMyQuestion());
			moveRooms = correct;
			attemptedRoom.setIsAcessible(correct);
			attemptedRoom.setIsLocked(!correct);
		}

		if (moveRooms) {
			this.myMaze.setMyYPosition(this.myMaze.getMyYPosition() + theDY);
			this.myMaze.setMyXPosition(this.myMaze.getMyXPosition() + theDX);
			this.myMaze.useKey();
			this.myMaze.obtainKey();
		}
		
	}

	/*
	 * Calls methods available from pause menu based on thePauseSelection
	 * @param thePauseSelection identifies which pause menu function to call
	 */
	private void performPauseSelection(int thePauseSelection) {
		switch (thePauseSelection) {
		case SAVE:
			this.saveGame();
			break;
		case LOAD:
			this.loadGame();
			break;
		case RESUME:
			break;
		case EXIT:
			break;
		}
	}
	
	/*
	 * Saves the Game state to triviaMaze.ser
	 */
	void saveGame() {
		try {
			
			FileOutputStream fileOut = new FileOutputStream("./triviaMaze.ser");

			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(myMaze);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in /triviaMaze.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	/*
	 * Loads the Game state saved in triviaMaze.ser
	 */
	void loadGame() {
		Maze m = null;
		try {
			FileInputStream fileIn = new FileInputStream("./triviaMaze.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			m = (Maze) in.readObject();
			this.myMaze = m;
			in.close();
			fileIn.close();
			this.playGame();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return;
		}
	}

	
	//TODO Exit Game needs to be a window in GUI, not use the console
	private void exitGame() {
		System.exit(0);
	}

	/*
	 * Entry point to the Game, initializes a game and calls playGame()
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Game testGame = new Game(new Maze());
		testGame.myDisplay.showHowTo();
		testGame.playGame();

	}
}