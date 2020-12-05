package thisorthat;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

	public Game(Maze theMaze) {
		this.myMaze = theMaze;
		this.myDisplay = new Display(this.myMaze);
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
		Scanner console = new Scanner(System.in);
		System.out.println("Give a file name: ");
		String myFileName = console.nextLine() + ".json";
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

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Game testGame = new Game(new Maze());
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

