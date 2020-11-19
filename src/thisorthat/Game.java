package thisorthat;

public class Game {
	
	public Maze myMaze;
	public Display myDisplay;
	
	boolean isInMaze;
	boolean isInQuestionMenu;
	boolean isInPauseMenu;
	boolean fileExists;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public Game(Maze theMaze, Display theDisplay) {
		
	}
	
	public int promptMovement() {
		return 0;
	}
	
	public boolean promptQuestion(int theRoomY, int theRoomX) {
		return isInQuestionMenu;
	}
	
	public int promptPauseMenu() {
		return 0;
	}
	
	public void receiveMovementSelection(int move) {
		if (isInMaze) {
//			move;
		}
	}
	
	public void receiveAnswerSelection(int answer) {
		
	}
	
	public void receivePauseSelection(int pause) {
		isInPauseMenu = true;
	}
	
	public boolean checkWinCondition() {
		return false;
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

}
