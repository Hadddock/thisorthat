package thisorthat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 * 
 * @author Edward Robinson, Justin Haddock, and Elisabeth Jewett
 *
 */
public class Display implements KeyListener {
	/*
	 * Key Event code for up arrow key
	 */
	public static final int UP = 38;
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
	 * Window holding myMazeFrame.
	 */
	private JWindow myWindow;
	/*
	 * JFrame containing all Components used to generate and display the maze.
	 */
	private JFrame myMazeFrame;
	/*
	 * JFrame containing all Components used to generate and display questions.
	 */
	private JFrame myQuestionFrame;
	/*
	 * JFrame containing all Components used to generate and display the pause menu.
	 */
	private JFrame myPauseFrame;
	/**
	 * int to represent the pause selection made in pause menu.
	 */
	private int pauseSelection = 0;
	/*
	 * boolean to represent player's answer to a question.
	 */
	private boolean correct;
	/*
	 * boolean to represent if a player has made an answer to a question yet.
	 */
	private boolean unanswered = true;
	/*
	 * GridBagConstraints object for GridBagLayout of the mazePanel.
	 */
	private GridBagConstraints gbc;
	/*
	 * JPanel containing the Components for the maze itself.
	 */
	private JPanel mazePanel;
	/*
	 * int to represent key pressed by player.
	 */
	int keyPressed;

	/**
	 * Public constructor to initialize all the fields in Display class.
	 * 
	 * @param theMaze the Maze that will be generated onto the GUI.
	 * @throws IOException when any files are not found when images are created.
	 */
	public Display(Maze theMaze) throws IOException {
		myMazeFrame = new JFrame("Maze");
		myMazeFrame.addKeyListener(this);
		myMazeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		myMazeFrame.setUndecorated(true);
		myMazeFrame.setVisible(true);
		myMazeFrame.toFront();
		myMazeFrame.setDefaultCloseOperation(myMazeFrame.DISPOSE_ON_CLOSE);
		myQuestionFrame = new JFrame("Question");
		myQuestionFrame.setDefaultCloseOperation(myQuestionFrame.DISPOSE_ON_CLOSE);
		myQuestionFrame.setResizable(false);
		myPauseFrame = new JFrame("Pause");
		buildPauseFrame();
		myPauseFrame.setDefaultCloseOperation(myPauseFrame.DISPOSE_ON_CLOSE);
		myWindow = new JWindow(myMazeFrame);
	}

	/**
	 * Method to record what key is pressed.
	 */
	public void keyPressed(KeyEvent e) {
		keyPressed = e.getKeyCode();
	}

	/**
	 * Method to indicate when a key has been released.
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Method to indicate when a key has been pressed.
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Accepts user input and relays it back to the controller, Game class.
	 * 
	 * @return the selected action of the player indicated by keypress.
	 */
	int acceptMazeInput() {
		System.out.println("\nChoose which direction to go: \n");
		myMazeFrame.toFront();
		myMazeFrame.requestFocus();
		this.keyPressed = -1;
		int selectedAction = -1;
		while (selectedAction != LEFT && selectedAction != RIGHT && selectedAction != UP && selectedAction != DOWN
				&& selectedAction != ESCAPE) {
			this.myMazeFrame.isAutoRequestFocus();
			selectedAction = this.keyPressed;
			// go to pause menu
		}
		return selectedAction;
	}

	/**
	 * A popup window to introduce and explain the game.
	 */
	void showHowTo() {
		try {
			JFrame howToFrame = new JFrame("How to play!");
			howToFrame.setResizable(true);
			howToFrame.setPreferredSize(new Dimension(800, 800));
			howToFrame.setAlwaysOnTop(true);
			howToFrame.getContentPane().setLayout(null);

			// Sets up right button and its listener
			JButton stopYelling = new JButton("");
			stopYelling.setIcon(new ImageIcon(ImageIO.read(new File("./images/stopYelling.png"))));
			stopYelling.setBounds(461, 668, 205, 60);
			howToFrame.getContentPane().add(stopYelling);
			stopYelling.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent theEvent) {
					try {
						stopYelling.setIcon(new ImageIcon(ImageIO.read(new File("./images/no.png"))));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			// sets up left button and its listener
			JButton findOut = new JButton("");
			findOut.setIcon(new ImageIcon(ImageIO.read(new File("./images/FindOut.png"))));
			findOut.setBounds(111, 668, 205, 60);
			howToFrame.getContentPane().add(findOut);
			findOut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent theEvent) {
					try {
						howToFrame.getContentPane().removeAll();
						howToFrame.getContentPane().setLayout(null);
						howToFrame.setDefaultCloseOperation(howToFrame.DISPOSE_ON_CLOSE);

						JLabel overlay = new JLabel("");
						overlay.setIcon(new ImageIcon(ImageIO.read(new File("./images/howToP2Overlay.png"))));
						overlay.setBounds(0, 0, 813, 812);
						howToFrame.getContentPane().add(overlay);

						JLabel background = new JLabel("");
						background.setIcon(new ImageIcon(ImageIO.read(new File("./images/howToBG.png"))));
						background.setBounds(0, 0, 799, 774);
						howToFrame.getContentPane().add(background);

						howToFrame.revalidate();
						howToFrame.repaint();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			JLabel overlay = new JLabel("");
			overlay.setBounds(0, 0, 794, 809);
			overlay.setIcon(new ImageIcon(ImageIO.read(new File("./images/howToP1Overlay.png"))));
			howToFrame.getContentPane().add(overlay);

			JLabel background = new JLabel("");
			background.setIcon(new ImageIcon(ImageIO.read(new File("./images/howToBG.png"))));
			background.setBounds(0, 0, 794, 771);
			howToFrame.getContentPane().add(background);
			howToFrame.setVisible(true);
			prioritizeFrame(howToFrame);
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Generates the maze display and updates player position, locked rooms, and
	 * cleared rooms.
	 * 
	 * @param theMaze the Maze being generated into the GUI.
	 * @throws IOException when files are not found when creating images.
	 */
	public void showMaze(final Maze theMaze) throws IOException {
		// Create maze w/ images
		mazePanel = new JPanel();
		Room[][] rooms = theMaze.getMyRooms();
		JLabel[][] picLabels = new JLabel[rooms.length][rooms[0].length];
		// Preferred size of the images on the maze
		Dimension prefSize = new Dimension(75, 75);
		// Loop to create images for the maze
		for (int i = 0; i < rooms.length; i++) {
			for (int j = 0; j < rooms[i].length; j++) {
				// determine if question room, locked, key room, or goal.
				Room currentRoom = rooms[i][j];
				if (currentRoom.getIsLocked()) {
					// room is locked, label it locked
					picLabels[i][j] = new JLabel(new ImageIcon(ImageIO.read(new File("./images/locked.jpg"))));
				} else if (currentRoom.getIsGoal()) {
					// is the goal, goal image
					picLabels[i][j] = new JLabel(new ImageIcon(ImageIO.read(new File("./images/goal.jpg"))));
				} else if (currentRoom.getIsAcessible()) {
					picLabels[i][j] = new JLabel(new ImageIcon(ImageIO.read(new File("./images/complete.jpg"))));
				} else if (currentRoom.getIsKeyRoom()) {
					// key room, label it key
					picLabels[i][j] = new JLabel(new ImageIcon(ImageIO.read(new File("./images/key.jpg"))));
				} else {
					// room is accessible, label it with subject
					picLabels[i][j] = new JLabel(new ImageIcon(ImageIO.read(new File("./images/question.jpg"))));
				}
				// XXX display current position
				if (i == theMaze.getMyYPosition() && j == theMaze.getMyXPosition()) {
					picLabels[i][j] = new JLabel(new ImageIcon(ImageIO.read(new File("./images/player.jpg"))));
				}
				// set preferred size of each image
				picLabels[i][j].setPreferredSize(prefSize);
			}
		}

		// Setting up the GridBagLayOut
		mazePanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		// Images for spacers in the maze
		BufferedImage vertLine = ImageIO.read(new File("./images/vertLine.png"));
		BufferedImage blank = ImageIO.read(new File("./images/blank.png"));
		BufferedImage horLine = ImageIO.read(new File("./images/horizontalLine.png"));
		int acc;
		int rowLength = 2 * picLabels[0].length - 1;
		int columnLength = 2 * picLabels.length - 1;
		for (int i = 0; i < columnLength; i++) {
			acc = 0;
			for (int j = 0; j < rowLength; j++) {
				if (i % 2 == 0) { // Image row
					if (j % 2 == 0) {
						gbc.gridx = j;
						gbc.gridy = i;
						mazePanel.add(picLabels[i / 2][j - acc], gbc);
						acc++;
					} else if (j != rowLength) {
						JLabel horLineLabel = new JLabel(new ImageIcon(horLine));
						horLineLabel.setPreferredSize(prefSize);
						gbc.gridx = j;
						gbc.gridy = i;
						mazePanel.add(horLineLabel, gbc);
					}
				} else { // blank row
					if (j % 2 == 0) {
						JLabel vertLineLabel = new JLabel(new ImageIcon(vertLine));
						vertLineLabel.setPreferredSize(prefSize);
						gbc.gridx = j;
						gbc.gridy = i;
						mazePanel.add(vertLineLabel, gbc);
					} else if (j != rowLength) {
						JLabel blankLabel = new JLabel(new ImageIcon(blank));
						blankLabel.setPreferredSize(prefSize);
						gbc.gridx = j;
						gbc.gridy = i;
						mazePanel.add(blankLabel, gbc);
					}

				}
			}
		}

		JLabel pause = new JLabel("Press ESC to access the pause menu");
		mazePanel.setBackground(Color.decode("#0058A2"));
		// add to maze
		myMazeFrame.add(mazePanel, BorderLayout.CENTER);
		myMazeFrame.add(pause, BorderLayout.SOUTH);
		prioritizeFrame(myMazeFrame);
		myWindow.setVisible(true);
	}

	/**
	 * Generates the question frame and gives the players their two options to
	 * answer from.
	 * 
	 * @param theQuestion the Question being displayed to the player.
	 * @return true if player is correct, false otherwise
	 */
	public boolean showQuestion(final Question theQuestion) {
		myQuestionFrame.removeAll();
		myQuestionFrame = new JFrame("Question");
		JLabel question = new JLabel(theQuestion.getMySubject(), SwingConstants.CENTER);
		// answerOne button and listener
		JButton answers[] = new JButton[2];
		JPanel answerMenu = new JPanel();
		for (int i = 0; i < answers.length; i++) {
			answers[i] = new JButton(theQuestion.getMyAnswers()[i]);
			final int index = i;
			answers[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent theEvent) {
					if (theQuestion.getMyCorrectAnswer() == index) {
						JOptionPane.showMessageDialog(null, "Correct!");
						correct = true;

					} else {
						JOptionPane.showMessageDialog(null, "Incorrect!");
						correct = false;
					}
					myQuestionFrame.dispose();
					unanswered = false;
				}
			});
			answerMenu.add(answers[i]);
			answers[i].setOpaque(true);
		}
		question.setOpaque(true);
		myQuestionFrame.add(answerMenu, BorderLayout.SOUTH);
		myQuestionFrame.add(question, BorderLayout.CENTER);
		myQuestionFrame.setPreferredSize(new Dimension(300, 150));
		myQuestionFrame.revalidate();
		myQuestionFrame.repaint();
		prioritizeFrame(myQuestionFrame);
		while (unanswered) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		unanswered = true;
		return correct;
	}

	/**
	 * Displays the pause menu.
	 * 
	 * @return the button pressed by the player indicated by an int value.
	 */
	public int showPauseMenu() {
		prioritizeFrame(myPauseFrame);
		pauseSelection = -1;
		while (pauseSelection == -1) {
			try {
				myPauseFrame.requestFocus();
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.myMazeFrame.isAutoRequestFocus();
		}
		myMazeFrame.requestFocus();
		return pauseSelection;
	}

	/**
	 * Generates myPauseFrame.
	 */
	private void buildPauseFrame() {
		JLabel paused = new JLabel("The game is paused.", SwingConstants.CENTER);
		// create resume button and its action listener
		JButton resume = new JButton();
		Dimension prefButtonSize = new Dimension(107, 25);
		resume.setPreferredSize(prefButtonSize);
		JButton save = new JButton();
		save.setPreferredSize(prefButtonSize);
		JButton load = new JButton();
		load.setPreferredSize(prefButtonSize);
		JButton help = new JButton();
		help.setPreferredSize(prefButtonSize);
		JButton exit = new JButton();
		exit.setPreferredSize(prefButtonSize);
		try {
			resume.setIcon(new ImageIcon(ImageIO.read(new File("./images/resume.png"))));
			save.setIcon(new ImageIcon(ImageIO.read(new File("./images/save.png"))));
			load.setIcon(new ImageIcon(ImageIO.read(new File("./images/load.png"))));
			help.setIcon(new ImageIcon(ImageIO.read(new File("./images/help.png"))));
			exit.setIcon(new ImageIcon(ImageIO.read(new File("./images/exit.png"))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				pauseSelection = RESUME;
				myPauseFrame.dispose();
			}
		});

		// create save button and its action listener
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				pauseSelection = SAVE;
				myPauseFrame.dispose();
				JOptionPane.showMessageDialog(null, "Game saved!");
			}
		});

		// create load button and its action listener
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				pauseSelection = LOAD;
				myPauseFrame.dispose();
				JOptionPane.showMessageDialog(null, "Game loaded!");
			}
		});

		// create pause action listener and build the frame for it
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				try {
					JFrame helpFrame = new JFrame("Help");
					helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					helpFrame.setPreferredSize(new Dimension(800, 800));
					helpFrame.getContentPane().setPreferredSize(new Dimension(800, 800));
					helpFrame.getContentPane().setLayout(null);

					JPanel panel = new JPanel();
					panel.setBounds(0, 0, 787, 764);
					helpFrame.getContentPane().add(panel);
					panel.setBackground(Color.decode("#0058A2"));
					panel.setLayout(null);

					JLabel overlay = new JLabel("");
					overlay.setIcon(new ImageIcon(ImageIO.read(new File("./images/helpOverlay.png"))));
					overlay.setBounds(0, 0, 787, 764);
					panel.add(overlay);

					JLabel goal = new JLabel("");
					goal.setToolTipText(
							"This is the goal! Upon answering the question correctly and entering the exit room, you have won  the game and are the new High Priest of IKEA!");
					goal.setIcon(new ImageIcon(ImageIO.read(new File("./images/helpGoal.jpg"))));
					goal.setBounds(690, 310, 90, 84);
					panel.add(goal);

					JLabel locked = new JLabel("");
					locked.setToolTipText(
							"This indicates the door to this room is locked! A door will be locked if you answer a question incorrectly! If you get surrounded by all locked doors, the game is over so be careful!");
					locked.setIcon(new ImageIcon(ImageIO.read(new File("./images/helpLocked.png"))));
					locked.setBounds(681, 386, 80, 78);
					panel.add(locked);

					JLabel player = new JLabel("");
					player.setToolTipText(
							"This little goober is you! Wherever you see him, this is your position on the map!");
					player.setIcon(new ImageIcon(ImageIO.read(new File("./images/helpPlayer.jpg"))));
					player.setBounds(690, 220, 90, 96);
					panel.add(player);

					JLabel question = new JLabel("");
					question.setToolTipText(
							"This denotes a question room! If a room has this symbol on it, it means you haven't answered a question there yet and it is a room you can enter upon a correct answer to a trivia question!");
					question.setIcon(new ImageIcon(ImageIO.read(new File("./images/helpQuestion.png"))));
					question.setBounds(690, 450, 80, 78);
					panel.add(question);

					JLabel key = new JLabel("");
					key.setToolTipText(
							"This is a key! When you encounter a key room, if you answer the question correctly, you now have a key in your possession! A key is a one-time use item that allows you to enter a locked door!");
					key.setIcon(new ImageIcon(ImageIO.read(new File("./images/helpKey.png"))));
					key.setBounds(681, 540, 80, 78);
					panel.add(key);

					helpFrame.pack();
					helpFrame.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {

				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?",
						"Exit Program Message Box", JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					pauseSelection = EXIT;
				} else {
					pauseSelection = RESUME;
				}
				myPauseFrame.dispose();
			}
		});

		// create the panel for the menu buttons and add them
		JPanel menu = new JPanel();
		menu.add(resume);
		menu.add(save);
		menu.add(load);
		menu.add(help);
		menu.add(exit);
		myPauseFrame.add(menu, BorderLayout.SOUTH);
		myPauseFrame.add(paused, BorderLayout.CENTER);
		myPauseFrame.setPreferredSize(new Dimension(600, 100));
	}

	/**
	 * Generates and displays the win screen when the player wins the game. Also
	 * gives a button for the player to exit the game.
	 * 
	 * @throws IOException
	 */
	public void displayWinScreen() throws IOException {
		JFrame winFrame = new JFrame("A winner is you!");
		JLabel winner = new JLabel("THE GOAL HAS BEEN REACHED. YOU ARE THE NEW HIGH PRIEST OF IKEA!",
				SwingConstants.CENTER);
		BufferedImage image = ImageIO.read(new File("./images/highPriest.png"));
		JLabel highPriest = new JLabel(new ImageIcon(image));
		JButton endGame = new JButton("End Game");
		endGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				winFrame.dispose();
				myMazeFrame.dispose();
			}
		});

		winFrame.setDefaultCloseOperation(winFrame.EXIT_ON_CLOSE);
		winFrame.add(highPriest, BorderLayout.CENTER);
		winFrame.add(endGame, BorderLayout.SOUTH);
		winFrame.add(winner, BorderLayout.NORTH);
		winFrame.pack();
		prioritizeFrame(winFrame);
	}

	/**
	 * Generates and displays the losing screen when the player loses the game. Also
	 * gives a button for the player to end the game after a loss.
	 */
	public void displayLoseScreen() {
		JFrame loseFrame = new JFrame("You lost the game!");
		JLabel loser = new JLabel("THE WINDING CORRIDORS OF IKEA HAVE CLAIMED YOUR SOUL, YOU LOST!",
				SwingConstants.CENTER);
		JButton endGame = new JButton("End Game");
		endGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				loseFrame.dispose();
				myMazeFrame.dispose();
			}
		});
		loseFrame.add(endGame, BorderLayout.SOUTH);
		loseFrame.add(loser, BorderLayout.CENTER);
		loseFrame.setPreferredSize(new Dimension(600, 100));
		prioritizeFrame(loseFrame);
	}

	/**
	 * Private helper method to prioritize a frame to the front and hold focus.
	 * 
	 * @param theFrame the JFrame to be focused.
	 */
	private void prioritizeFrame(JFrame theFrame) {
		theFrame.pack();
		theFrame.setLocationRelativeTo(null);
		theFrame.setVisible(true);
		theFrame.toFront();
		theFrame.requestFocus();
		theFrame.setDefaultCloseOperation(0);
	}
}