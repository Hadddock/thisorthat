package thisorthat;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Game implements KeyListener {
private static final int UP =  	38;
private static final int RIGHT = 39;
private static final int DOWN = 40;
private static final int LEFT = 37;

	public Maze myMaze;
	public Display myDisplay;

	boolean isInMaze;
	boolean isInQuestionMenu;
	boolean isInPauseMenu;
	boolean isFinished;
	boolean fileExists;
	int keyPressed;
	JFrame frame = new JFrame();

	public void keyPressed(KeyEvent e) {
		keyPressed = e.getKeyCode();
	}
	
	public void keyReleased(KeyEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {}
	

	public Game(Maze theMaze, Display theDisplay) {
		this.myMaze = theMaze;
		this.myDisplay = theDisplay;
		frame.addKeyListener(this);
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
	}

	public int promptMovement() {
		System.out.println("\nChoose which direction to go: \n" + this.myMaze);
		this.keyPressed = -1;
		int selectedDirection = -1;
		while(selectedDirection != LEFT && selectedDirection !=RIGHT && selectedDirection!= UP && selectedDirection!= DOWN) {
			frame.isAutoRequestFocus();
			selectedDirection = this.keyPressed;
		}
		switch (selectedDirection) {
		case UP:
			selectedDirection = 0;
			break;
		case RIGHT:
			selectedDirection = 1;
			break;
		case DOWN:
			selectedDirection = 2;
			break;
		case LEFT:
			selectedDirection = 3;
			break;
		}
		return selectedDirection;	
	}

	public boolean promptQuestion(int theRoomY, int theRoomX) {
		Question currentQuestion =(myMaze.getMyRooms()[theRoomY][theRoomX].getMyQuestion());
		System.out.println(currentQuestion.getMySubject());
		//currently only supports two answer questions
		System.out.println("PRESS LEFT IF IT'S " + currentQuestion.getMyAnswers()[0] );
		System.out.println("PRESS RIGHT IF IT'S " + currentQuestion.getMyAnswers()[1] );
		this.keyPressed = 0;
		int choice = -1;
		while(choice != LEFT &&  choice!=RIGHT) {
			frame.isAutoRequestFocus();
			choice = this.keyPressed;
		}
		if(choice == LEFT) {
			choice = 0;
		}
		else {
			choice = 1;
		}
		return (choice == currentQuestion.getMyCorrectAnswer());	
	}

	public int promptPauseMenu() {
		return 0;
	}

	public void receiveMovementSelection(int selectedDirection)  {
		int dy = 0, dx = 0;
		int x = this.myMaze.getMyXPosition();
		int y = this.myMaze.getMyYPosition();
		switch (selectedDirection) {
		case 0:
			dy--;
			break;
		case 1:
			dx++;
			break;
		case 2:
			dy++;
			break;
		case 3:
			dx--;
			break;
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
		// check out of bounds

		// if moving to cleared room
		if (attemptedRoom.getIsAcessible()) {
			moveRooms = true;
			// if at goal
			if (attemptedRoom.getIsGoal())
				this.isFinished = true;
		}
		// if attempting to move to locked room
		else if (attemptedRoom.getIsLocked()) {
			moveRooms = this.myMaze.getHasKey();
		}
		// if moving to question room, prompt the question for that room
		else if (!attemptedRoom.getIsAcessible()) {
			boolean correct = promptQuestion(this.myMaze.getMyYPosition() + dy, this.myMaze.getMyXPosition() + dx);
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

	public void receivePauseSelection(int pause) {
		isInPauseMenu = true;
	}

	public boolean checkWinCondition() {
		return this.myMaze.getMyRooms()[this.myMaze.getMyYPosition()][this.myMaze.getMyXPosition()].getIsGoal();
	}

	public boolean checkWinPossible() {
		return false;
	}

	public void saveGame() {
		fileExists = true;
	}

	public void loadGame() {

	}

	public Maze getMyMaze() {
		return this.myMaze;
	}

	public boolean checkFileExists() {
		return fileExists;
	}

	public void exitGame() {

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game testGame = new Game(new Maze(), new Display());
		//keep going until goal is reached
		while(!testGame.isFinished) {
			testGame.receiveMovementSelection(testGame.promptMovement()); 
		};
		System.out.println(testGame.myMaze);
		System.out.println("\nTHE GOAL HAS BEEN REACHED. YOU ARE THE NEW HIGH PRIEST OF IKEA");
		testGame.frame.dispose();
		testGame = null;
	}

}
