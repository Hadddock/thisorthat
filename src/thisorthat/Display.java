package thisorthat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

public class Display implements KeyListener {
	private JWindow myWindow;
	private JFrame myMazeFrame;
	private JFrame myQuestionFrame;
	private JFrame myPauseFrame;

	public Display() {
		myMazeFrame = new JFrame("Maze");
		myQuestionFrame = new JFrame("Question");
		myPauseFrame = new JFrame("Pause");
		myWindow = new JWindow(myMazeFrame);
	}

	// -----------------------------TEMPORARY STUFF-----------------------------
	public void keyPressed(KeyEvent e) {
		int keyPressed = e.getKeyCode();
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {
	}

	// ------------------------------ACTUAL STUFF-------------------------------

	public void showMaze(final Maze theMaze) {
		myWindow.addKeyListener(this);

		// Create a pause button
		JButton pause = new JButton("Pause");
		JPanel menu = new JPanel();
		// action listener for pause
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				showPauseMenu();
			}
		});
		menu.add(pause);
		
		// Create maze buttons
		JPanel mazePanel = new JPanel();
		Room[][] rooms = theMaze.getMyRooms();
		JButton[][] mazeButtons = new JButton[rooms.length][rooms[0].length];
		
		for(int i = 0; i < rooms.length; i++) {
			for(int j = 0; j < rooms[i].length; j++) {
				// determine if question room, locked, key room, or goal.
				Room currentRoom = rooms[i][j];
				if(currentRoom.getIsGoal()) {
					// is goal, label it goal
					mazeButtons[i][j] = new JButton("GOAL");
				} else if(currentRoom.getIsKeyRoom()) {
					// key room, label it key
					mazeButtons[i][j] = new JButton("KEY");
				} else if(currentRoom.getIsLocked()) { 
					// room is locked, label it locked
					mazeButtons[i][j] = new JButton("LOCKED");
				} else {
					// room is accessible, label it with subject
					mazeButtons[i][j] = new JButton(currentRoom.getMyQuestion().getMySubject());
				}
				// add to maze button panel
				mazePanel.add(mazeButtons[i][j]);
				// set action for button pressed
				// *** LISTENER NEEDS SOME WORK TO INCORPORATE DIFFERENT STYLES OF ROOMS ***
				addMazeListener(mazeButtons[i][j], rooms, i, j);
			}
		}
		mazePanel.setLayout(new GridLayout(rooms.length, rooms[0].length));
		myMazeFrame.add(mazePanel, BorderLayout.CENTER);
		
		
		

		// Frame setup
		myMazeFrame.add(menu, BorderLayout.SOUTH);
		myMazeFrame.setPreferredSize(new Dimension(800,600));
		myMazeFrame.pack();
		myMazeFrame.setLocationRelativeTo(null);
		myMazeFrame.setVisible(true);
		myWindow.setVisible(true);

	}
	
	private void addMazeListener(JButton theButton, Room[][] theRooms, int i, int j) {
		theButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				showQuestion(theRooms[i][j].getMyQuestion());
			}
		});
	}

	public void showQuestion(final Question theQuestion) {
		JLabel question = new JLabel(theQuestion.getMySubject(), SwingConstants.CENTER);
		// answerOne button and listener
		JButton answerOne = new JButton(theQuestion.getMyAnswers()[0]);
		answerOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				if(theQuestion.getMyCorrectAnswer() == 0) {
					JOptionPane.showMessageDialog(null, "Correct!");
				} else {
					JOptionPane.showMessageDialog(null, "Incorrect!");
				}
				myQuestionFrame.dispose();
			}
		});
		// answerTwo button and listener
		JButton answerTwo = new JButton(theQuestion.getMyAnswers()[1]);
		answerTwo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				if(theQuestion.getMyCorrectAnswer() == 1) {
					JOptionPane.showMessageDialog(null, "Correct!");
				} else {
					JOptionPane.showMessageDialog(null, "Incorrect!");
				}
				myQuestionFrame.dispose();
			}
		});
		
		JPanel answerMenu = new JPanel();
		answerMenu.add(answerOne);
		answerMenu.add(answerTwo);
		myQuestionFrame.add(answerMenu, BorderLayout.SOUTH);
		myQuestionFrame.add(question, BorderLayout.CENTER);
		myQuestionFrame.setPreferredSize(new Dimension(300, 200));
		myQuestionFrame.pack();
		myQuestionFrame.setVisible(true);
		myQuestionFrame.setLocationRelativeTo(null);
		myQuestionFrame.toFront();
		myQuestionFrame.requestFocus();
		myQuestionFrame.setDefaultCloseOperation(myQuestionFrame.DISPOSE_ON_CLOSE);
	}

	public void showPauseMenu() {
		JLabel paused = new JLabel("The game is paused.", SwingConstants.CENTER);
		// create resume button and its action listener
		JButton resume = new JButton("Resume");
		resume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				myPauseFrame.dispose();
			}
		});
		
		// create save button and its action listener
		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				// REPLACE WITH ACTUAL SAVE ACTION WHEN I HAVE IT
				JOptionPane.showMessageDialog(null, "Game saved!");
			}
		});
		
		// create load button and its action listener
		JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				// REPLACE WITH ACTUAL LOAD ACTION WHEN I HAVE IT
				JOptionPane.showMessageDialog(null, "Game loaded!");
			}
		});
		JPanel menu = new JPanel();

		// ACTION LISTENERS NEEDED FOR ALL BUTTONS
		menu.add(resume);
		menu.add(save);
		menu.add(load);
		myPauseFrame.add(menu, BorderLayout.SOUTH);
		myPauseFrame.add(paused, BorderLayout.CENTER);
		myPauseFrame.setPreferredSize(new Dimension(300, 100));
		myPauseFrame.pack();
		myPauseFrame.setLocationRelativeTo(null);
		myPauseFrame.setVisible(true);
		myPauseFrame.toFront();
		myPauseFrame.requestFocus();
		myPauseFrame.setDefaultCloseOperation(myPauseFrame.DISPOSE_ON_CLOSE);
	}

	public void displayWinScreen() {
		JFrame winFrame = new JFrame("A winner is you!");
		JLabel winner = new JLabel("THE GOAL HAS BEEN REACHED. YOU ARE THE NEW HIGH " + "PRIEST OF IKEA!",
				SwingConstants.CENTER);

		winFrame.add(winner);
		winFrame.setPreferredSize(new Dimension(600, 100));
		winFrame.pack();
		winFrame.setLocationRelativeTo(null);
		winFrame.setVisible(true);
		winFrame.toFront();
		winFrame.requestFocus();
		winFrame.setDefaultCloseOperation(winFrame.DISPOSE_ON_CLOSE);

	}

	public void displayLoseScreen() {
		JFrame loseFrame = new JFrame("You lost the game!");
		JLabel loser = new JLabel(
				"THE WINDING CORRIDORS OF IKEA HAVE CLAIMED YOUR SOUL, YOU LOST!",
				SwingConstants.CENTER);

		loseFrame.add(loser);
		loseFrame.setPreferredSize(new Dimension(600, 100));
		loseFrame.pack();
		loseFrame.setLocationRelativeTo(null);
		loseFrame.setVisible(true);
		loseFrame.toFront();
		loseFrame.requestFocus();
		loseFrame.setDefaultCloseOperation(loseFrame.DISPOSE_ON_CLOSE);
	}
}