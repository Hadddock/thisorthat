package thisorthat;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFrame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
		Scanner console = new Scanner(System.in);
		System.out.println("Give a file name: ");
		String fileName = console.nextLine() + ".json";
		console.close();
		JSONObject jsonOb = new JSONObject();
		JSONArray jsonAr = new JSONArray();
		for (Room[] y: myMaze.getMyRooms()) {
			jsonAr.add("RoomArray"+1);
			JSONArray jsonAr2 = new JSONArray();
			for (Room x: y) {
				final Map m = new LinkedHashMap<String, String>(5);
				m.put("isAcessible", myMaze.getHasKey());
				m.put("isLocked", myMaze.getHasKey());
				m.put("isGoal", myMaze.getHasKey());
				m.put("isKeyRoom", myMaze.getHasKey());
				jsonAr2.add(m);
				jsonAr.add(jsonAr2);
			}
			//jsonAr.add(m);
		}
		jsonOb.put("Rooms",jsonAr);
		try {
			final PrintWriter pw = new PrintWriter(fileName);
			pw.write(jsonOb.toJSONString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//write to file in that order (and save it in the src file here)
	}

	public void loadGame() {
		Scanner console = new Scanner(System.in);
		System.out.println("What file would you like to load?: ");
		String fileName = console.nextLine();
		console.close();
		try {
			BufferedReader readFile = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Maze getMyMaze() {
		return this.myMaze;
	}

	public void exitGame() {
		Scanner console = new Scanner(System.in);
		System.out.println("Would you like to exit the game? (y or n)");
		String decision = console.next();
		console.close();
		while (decision.equalsIgnoreCase("y")||decision.equalsIgnoreCase("n")) {
			if (decision.equalsIgnoreCase("y")) {
				this.frame.dispose();
			} else {
				//TODO exit save or load section and return to maze
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game testGame = new Game(new Maze(), new Display());
		//keep going until goal is reached
		while(!testGame.isFinished) {
			testGame.receiveMovementSelection(testGame.promptMovement()); 
			testGame.saveGame();
		};
		System.out.println(testGame.myMaze);
		System.out.println("\nTHE GOAL HAS BEEN REACHED. YOU ARE THE NEW HIGH PRIEST OF IKEA");
		testGame.frame.dispose();
		testGame = null;
	}

}

/*{
"RoomArray1":[
	"Room1":{
		"isAcessible": the boolean,
		"isLocked": the boolean,
		"isGoal": the boolean,
		"isKeyRoom": the boolean,
		"Question": {
			"mySubject": the String,
			"myAnswers": [
				"answer1" theString,
				"answer2" theString,
			]
			"myCorrectAnswer": the int,
		}
	}
	"Room2":{
		"isAcessible": the boolean,
		"isLocked": the boolean,
		"isGoal": the boolean,
		"isKeyRoom": the boolean,
		"Question": {
			"mySubject": the String,
			"myAnswers": [
				"answer1" theString,
				"answer2" theString,
			]
			"myCorrectAnswer": the int,
		}
	}
]
],
"myXPosition":the int,
"myYPosition":the int,
"hasKey":the boolean,
}*/