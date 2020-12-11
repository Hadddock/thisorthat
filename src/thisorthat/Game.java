package thisorthat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Game {
	public static final int UP =  	38;
	public static final int RIGHT = 39;
	public static final int DOWN = 40;
	public static final int LEFT = 37;
	public static final int ESCAPE = 27;
	public static final int SAVE = 5;
	public static final int LOAD = 6;
	public static final int RESUME = 7;
	public static final int EXIT = 8;
	private Maze myMaze;
	private Display myDisplay;
	private boolean isFinished = false;

	public Game(Maze theMaze) {
		this.myMaze = theMaze;
		this.myDisplay = new Display(this.myMaze);
	}
	
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
				this.isFinished = !this.myMaze.checkWinPossible();
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
	
	private void verifyAction(int selectedDirection) {
		int dy = 0, dx = 0;
		int x = this.myMaze.getMyXPosition();
		int y = this.myMaze.getMyYPosition();
		switch (selectedDirection) {
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
		}
		// performAction if InBounds
		if (x + dx >= 0 && x + dx <= this.myMaze.getMyRooms()[0].length - 1 && y + dy >= 0
				&& y + dy <= this.myMaze.getMyRooms().length - 1) {
			performAction(dy, dx);
		}
	}

	private void performAction(int dy, int dx) {
		Room attemptedRoom = this.myMaze.getMyRooms()[this.myMaze.getMyYPosition() + dy][this.myMaze.getMyXPosition()
				+ dx];
		boolean moveRooms = false;

		//if room is freely acessible, move there
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
			this.myMaze.setMyYPosition(this.myMaze.getMyYPosition() + dy);
			this.myMaze.setMyXPosition(this.myMaze.getMyXPosition() + dx);
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
		}
	}
	
	/*
	 * Saves the Game state to file triviaMaze.ser
	 */
	private void saveGame() {
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
	 * Loads the Game state to from triviaMaze.ser
	 */
	private void loadGame() {
		Maze m = null;
		try {
			FileInputStream fileIn = new FileInputStream("./triviaMaze.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			m = (Maze) in.readObject();
			this.myMaze = m;
			in.close();
			fileIn.close();
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
		Scanner console = new Scanner(System.in);
		System.out.println("Would you like to exit the game? (y or n)");
		String decision = console.next();
		console.close();
		while (decision.equalsIgnoreCase("y") || decision.equalsIgnoreCase("n")) {
			if (decision.equalsIgnoreCase("y")) {
				System.exit(0);
			} else {
				// TODO exit pause menu
				break;
			}
		}
	}

	// For testing purposes only, not to be part of game
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Game testGame = new Game(new Maze());
		testGame.playGame();

	}

}
