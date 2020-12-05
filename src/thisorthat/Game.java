package thisorthat;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;

public class Game {
private static final int UP =  	38;
private static final int RIGHT = 39;
private static final int DOWN = 40;
private static final int LEFT = 37;
private static final int ESCAPE = 27;

	public Maze myMaze;
	public Display myDisplay;
	boolean isInMaze;
	boolean isInQuestionMenu;
	boolean isInPauseMenu;
	boolean isFinished;
	boolean fileExists;

	public Game(Maze theMaze, Display theDisplay) {
		this.myMaze = theMaze;
		this.myDisplay = theDisplay;
		myDisplay.getGame(this);
	}

	void receiveMovementSelection(int selectedDirection)  {
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
		case ESCAPE:
			
		}
		//handleMovement if InBounds
		if (x + dx >= 0 && x + dx <= this.myMaze.getMyRooms()[0].length - 1 && y + dy >= 0
				&& y + dy <= this.myMaze.getMyRooms().length - 1) {
			handleMovement(dy,dx);
		}
	}
	
	private void handleMovement(int dy, int dx) {
		Room attemptedRoom = this.myMaze.getMyRooms()[this.myMaze.getMyYPosition() +dy][this.myMaze.getMyXPosition() + dx];
		boolean moveRooms = false;

		if (attemptedRoom.getIsAcessible()) {
			moveRooms = true;
			if (attemptedRoom.getIsGoal())
				this.isFinished = true;
		}
		// if attempting to move to locked room
		else if (attemptedRoom.getIsLocked()) {
			moveRooms = this.myMaze.getHasKey();
		}
		// if moving to question room, prompt the question for that room
		else if (!attemptedRoom.getIsAcessible()) {
			boolean correct  = myDisplay.showQuestion(attemptedRoom.getMyQuestion());
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

	public void saveGame() {
		try {
	         FileOutputStream fileOut =
	         new FileOutputStream("./triviaMaze.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(myMaze);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in /triviaMaze.ser");
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
	}

	public void loadGame() {
		Maze m = null;
		try {
	         FileInputStream fileIn = new FileInputStream("./triviaMaze.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         m = (Maze) in.readObject();
	         in.close();
	         fileIn.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	         return;
	      } catch (ClassNotFoundException c) {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	         return;
	      }
		}

	Maze getMyMaze() {
		return this.myMaze;
	}

	public void exitGame() {
		Scanner console = new Scanner(System.in);
		System.out.println("Would you like to exit the game? (y or n)");
		String decision = console.next();
		console.close();
		while (decision.equalsIgnoreCase("y")||decision.equalsIgnoreCase("n")) {
			if (decision.equalsIgnoreCase("y")) {
				System.exit(0);
			} else {
				//TODO exit pause menu
				break;
			}
		}
	}
	
	//For testing purposes only, not to be part of game

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Game testGame = new Game(new Maze(), new Display());
		//keep going until goal is reached
		while(!testGame.isFinished) {
			//replace with observer observable
			testGame.myDisplay.showMaze(testGame.myMaze);
			testGame.receiveMovementSelection(testGame.myDisplay.acceptMazeInput()); 
		};
		//display WinScreen
		testGame.myDisplay.displayWinScreen();
		System.out.println("\nTHE GOAL HAS BEEN REACHED. YOU ARE THE NEW HIGH PRIEST OF IKEA");
		testGame.myDisplay.myMazeFrame.dispose();
		testGame = null;
	}

}