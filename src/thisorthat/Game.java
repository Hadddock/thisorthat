package thisorthat;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.TreeSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFrame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	
	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}

	public Game(Maze theMaze, Display theDisplay) {
		this.myMaze = theMaze;
		this.myDisplay = theDisplay;
		frame.addKeyListener(this);
		frame.setVisible(true);
		frame.toFront();
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
	}

	int promptMovement() {
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

	boolean promptQuestion(int theRoomY, int theRoomX) {
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

	void receiveMovementSelection(int selectedDirection)  {
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

	private void receivePauseSelection(int pause) {
		isInPauseMenu = true;
	}

	public boolean checkWinCondition() {
		return this.myMaze.getMyRooms()[this.myMaze.getMyYPosition()][this.myMaze.getMyXPosition()].getIsGoal();
	}

	public void saveGame() {
		Scanner console = new Scanner(System.in);
		System.out.println("Give a file name: ");
		myFileName = console.nextLine() + ".json";
		console.close();
		JSONObject obj3 = new JSONObject();
		int roomNum = 1;
		for (int i = 0; i < myMaze.getMyRooms().length; i++) {
			JSONObject obj4 = new JSONObject();
			for (int j = 0; j < myMaze.getMyRooms()[i].length; j++) {
				JSONObject obj2 = new JSONObject();
				obj2.put("isAcessible", myMaze.getMyRooms()[i][j].getIsAcessible());
				obj2.put("isLocked", myMaze.getMyRooms()[i][j].getIsLocked());
				obj2.put("isGoal", myMaze.getMyRooms()[i][j].getIsGoal());
				obj2.put("isKeyRoom", myMaze.getMyRooms()[i][j].getIsKeyRoom());
				JSONObject obj5 = new JSONObject();
				obj5.put("mySubject", myMaze.getMyRooms()[i][j].
						getMyQuestion().getMySubject());
				obj5.put("answer1", myMaze.getMyRooms()[i][j].
						getMyQuestion().getMyAnswers()[0]);
				obj5.put("answer2", myMaze.getMyRooms()[i][j].
						getMyQuestion().getMyAnswers()[1]);
				obj5.put("myCorrectAnswer", myMaze.getMyRooms()[i][j].
						getMyQuestion().getMyCorrectAnswer());
				obj2.put("Question", obj5);
				obj4.put("Room"+roomNum, obj2);
				roomNum++;
			}
			obj3.put("RoomArray"+(i+1), obj4);
			obj3.put("myXPosition", myMaze.getMyXPosition());
			obj3.put("myYPosition", myMaze.getMyYPosition());
			obj3.put("hasKey", myMaze.getHasKey());
		}
		try {
			JSONObject obj = new JSONObject();
			obj.put("Rooms", obj3);
			PrintWriter pw = new PrintWriter(myFileName);
			pw.write(obj.toJSONString()); 
			pw.flush(); 
			pw.close(); 
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	public void loadGame() {
		Scanner console = new Scanner(System.in);
		System.out.println("What file would you like to load?: ");
		String myFileName = console.nextLine() + ".json";
		console.close();
		try {
			Object parse = new JSONParser().parse(new FileReader(myFileName));
			JSONObject obj = (JSONObject) parse;
			Map map = ((Map)obj.get("Rooms"));
			Room[][] rooms = new Room[map.size()][map.size()];
			fileExists = true;
			int roomNum = 1;
			int mapSize = map.size()/2;
			for (int i = 0; i < mapSize; i++) {
				System.out.println(i);
				Map map2 = ((Map)map.get("RoomArray" + (i+1)));
				for (int j = 0; j < map2.size(); j++) {
					Map map3 = ((Map)map2.get("Room"+ roomNum));
					roomNum++;
				    boolean isAcessible = (Boolean)map3.get("isAcessible");
					boolean isLocked = (Boolean)map3.get("isLocked");
					boolean isGoal = (Boolean)map3.get("isGoal");
					boolean isKeyRoom = (Boolean)map3.get("isKeyRoom");
					Map map4 = (Map)map3.get("Question");
					String subject = (String)map4.get("mySubject");
					String[] answers = new String[2];
					answers[0] = (String)map4.get("answer1");
					answers[1] = (String)map4.get("answer2");
					int correctAnswer = Math.toIntExact((Long)map4.get("myCorrectAnswer"));
					rooms[i][j] = new Room(new Question(subject, answers,correctAnswer),
							isAcessible, isLocked, isGoal, isKeyRoom);
				}
			}
			myMaze.setMyXPosition(Math.toIntExact((Long)map.get("myXPosition")));
			myMaze.setMyYPosition(Math.toIntExact((Long)map.get("myYPosition")));
			myMaze.setHasKey((Boolean)map.get("hasKey"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void closeFrame() {
		this.frame.dispose();
	}


	int promptPauseMenu() {
		return 0;
	}


	public static void main(String[] args) throws IOException {
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
		Display test = new Display();
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
		Maze currentMaze = new Maze(testRooms, testYPosition, testXPositon, testKeyStatus);
		test.showMaze(currentMaze);
//		test.showPauseMenu();
//		test.displayWinScreen();
//		test.displayLoseScreen();

	}

}

