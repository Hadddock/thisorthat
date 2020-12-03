package thisorthat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

@SuppressWarnings("deprecation")
public class Display implements Observer {
	private JWindow myWindow;
	public JFrame myMazeFrame;
	private JFrame myQuestionFrame;
	private JFrame myPauseFrame;
	private boolean correct;
	private boolean unanswered = true;

	public Display() {
		myMazeFrame = new JFrame("Maze");
		myQuestionFrame = new JFrame("Question");
		myPauseFrame = new JFrame("Pause");
		myWindow = new JWindow(myMazeFrame);
		
	}
	
	

	public void showMaze(final Maze theMaze) throws IOException {
		// Create a pause button
		JButton pause = new JButton();
		pause.setPreferredSize(new Dimension(107, 43));
		BufferedImage img = ImageIO.read(new File("./images/pauseButton.png"));
	    pause.setIcon(new ImageIcon(img));
		JPanel menu = new JPanel();
		menu.setBackground(Color.BLACK);
		// action listener for pause
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				showPauseMenu();
			}
		});
		menu.add(pause);
		
		// Create maze w/ images
		JPanel mazePanel = new JPanel();
		Room[][] rooms = theMaze.getMyRooms();
		JLabel[][] picLabels = new JLabel[rooms.length][rooms[0].length];
		String name;
		
		Dimension prefSize = new Dimension(100,100);
		BufferedImage image;
		
		for(int i = 0; i < rooms.length; i++) {
			for(int j = 0; j < rooms[i].length; j++) {
				// determine if question room, locked, key room, or goal.
				Room currentRoom = rooms[i][j];
				if(currentRoom.getIsGoal()) {
					// is the goal, goal image
					image = ImageIO.read(new File("./images/goal.jpg"));
					picLabels[i][j] =  new JLabel(new ImageIcon(image));
					name = "goal";
				} else if(currentRoom.getIsKeyRoom()) {
					// key room, label it key
					image = ImageIO.read(new File("./images/key.jpg"));
					picLabels[i][j] =  new JLabel(new ImageIcon(image));
					name = "key";
				} else if(currentRoom.getIsLocked()) { 
					// room is locked, label it locked
					image = ImageIO.read(new File("./images/locked.jpg"));
					picLabels[i][j] =  new JLabel(new ImageIcon(image));
					name = "locked";
				} else {
					// room is accessible, label it with subject
					image = ImageIO.read(new File("./images/IKEA.jpg"));
					picLabels[i][j] =  new JLabel(new ImageIcon(image));
					name = "IKEA";
				}				
				// set mouse listeners and preferred size
				picLabels[i][j].setPreferredSize(prefSize);
				setImageMouseListener(picLabels[i][j], name, rooms[i][j].getMyQuestion());
			}
		}
		
		// Setting up the GridBagLayOut
		mazePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		// Images for parts of maze
		BufferedImage vertLine = ImageIO.read(new File("./images/vertLine.png"));
		BufferedImage blank = ImageIO.read(new File("./images/blank.png"));
		BufferedImage horLine = ImageIO.read(new File("./images/horizontalLine.png"));
		int acc;
		int rowLength = 2 * picLabels[0].length -1;
		for(int i = 0; i < 2 * picLabels.length -1; i++) {
			acc = 0;
			for(int j = 0; j < rowLength; j++) {
				if(i % 2 == 0) { //Image row
					if(j % 2 == 0) {
						gbc.gridx = j;
						gbc.gridy = i;
						mazePanel.add(picLabels[i/2][j-acc], gbc);
						acc++;						
					} else if(j != rowLength) {
						JLabel horLineLabel = new JLabel(new ImageIcon(horLine));
						horLineLabel.setPreferredSize(new Dimension(200,100));
						gbc.gridx = j;
						gbc.gridy = i;
						mazePanel.add(horLineLabel, gbc);
					}
				} else { //blank row
					if(j % 2 == 0) {
						JLabel vertLineLabel = new JLabel(new ImageIcon(vertLine));
						vertLineLabel.setPreferredSize(prefSize);
						gbc.gridx = j;
						gbc.gridy = i;
						mazePanel.add(vertLineLabel, gbc);
					} else if(j != rowLength) {
						JLabel blankLabel = new JLabel(new ImageIcon(blank));
						blankLabel.setPreferredSize(prefSize);
						gbc.gridx = j;
						gbc.gridy = i;
						mazePanel.add(blankLabel, gbc);
				    }
					
				}
			}
		}
		
		mazePanel.setBackground(Color.BLACK);
		myMazeFrame.add(mazePanel, BorderLayout.CENTER);
		
		// Frame setup
		myMazeFrame.add(menu, BorderLayout.SOUTH);
		myMazeFrame.setPreferredSize(new Dimension(800,600));
		myMazeFrame.pack();
		myMazeFrame.setLocationRelativeTo(null);
		myMazeFrame.setVisible(true);
		myWindow.setVisible(true);
		myMazeFrame.toFront();
		myMazeFrame.requestFocus();
		myMazeFrame.setDefaultCloseOperation(myMazeFrame.DISPOSE_ON_CLOSE);
	}
	
	private void setImageMouseListener(JLabel theImage, final String theName, final Question theQuestion) {
		theImage.addMouseListener((new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if(theName.equals("goal") || theName.equals("IKEA") || theName.equals("key")) {
		        	showQuestion(theQuestion);
		        } else if(theName.equals("locked")) {
		        	JOptionPane.showMessageDialog(null, "Door is locked!");
		        }
		    }
		}));
	}
	
	public boolean showQuestion(final Question theQuestion) {
		JLabel question = new JLabel(theQuestion.getMySubject(), SwingConstants.CENTER);
		// answerOne button and listener
		JButton answerOne = new JButton(theQuestion.getMyAnswers()[0]);
		answerOne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				if(theQuestion.getMyCorrectAnswer() == 0) {
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
		// answerTwo button and listener
		JButton answerTwo = new JButton(theQuestion.getMyAnswers()[1]);
		answerTwo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				if(theQuestion.getMyCorrectAnswer() == 1) {
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
		while(unanswered) {
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

	public void showPauseMenu(){
		JLabel paused = new JLabel("The game is paused.", SwingConstants.CENTER);
		// create resume button and its action listener
		JButton resume = new JButton();
		Dimension prefButtonSize = new Dimension(107,25);
		resume.setPreferredSize(prefButtonSize);
		JButton save = new JButton("Save");
		save.setPreferredSize(prefButtonSize);
		JButton load = new JButton("Load");
		load.setPreferredSize(prefButtonSize);
		
		
		try {
			BufferedImage img = ImageIO.read(new File("./images/resume.png"));
			resume.setIcon(new ImageIcon(img));
			img = ImageIO.read(new File("./images/save.png"));
			save.setIcon(new ImageIcon(img));
			img = ImageIO.read(new File("./images/load.png"));
			load.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				myPauseFrame.dispose();
			}
		});
		
		// create save button and its action listener
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent theEvent) {
				// REPLACE WITH ACTUAL SAVE ACTION WHEN I HAVE IT
				
				JOptionPane.showMessageDialog(null, "Game saved!");
			}
		});
		
		// create load button and its action listener
		
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
		myPauseFrame.setPreferredSize(new Dimension(400, 100));
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

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}