package thisorthat;

import java.io.Serializable;

/*
 * Question is an abstraction of a question, its potential answers, and its correct answer 
 * for use in a Trivia maze program.
 */
public class Question implements Serializable {
	/*
	 * The Subject to be placed in one of the categories in myAnswers
	 */
	private String mySubject;
	/*
	 * The Potential Categories to place the subject. The correct subject's index is
	 * myCorrectAnswer.
	 */
	private String[] myAnswers;
	/*
	 * The correct subject's index in myAnswers.
	 */
	private int myCorrectAnswer;

	/*
	 * XXX Default Constructor for testing purposes
	 */
	public Question() {
		this.mySubject = "Gullunge";
		this.myAnswers = new String[] { "An Ikea Product", "A Type of Cheese" };
		this.myCorrectAnswer = 1;
	}

	/*
	 * Constructor for Question
	 * 
	 * @param theSubject the String assigned to mySubject
	 * 
	 * @param theAnswers the String array assigned to myAnswers
	 * 
	 * @param theCorrectAnswer the integer assigned to myCorrectAnswer
	 */
	public Question(String theSubject, String[] theAnswers, int theCorrectAnswer) {
		this.mySubject = theSubject;
		this.myAnswers = theAnswers;
		this.myCorrectAnswer = theCorrectAnswer;
	}

	/**
	 * Getter for mySubject
	 * 
	 * @return mySubject
	 */
	public String getMySubject() {
		return mySubject;
	}

	/**
	 * Getter for myAnswers
	 * 
	 * @return myAnswers
	 */
	public String[] getMyAnswers() {
		return myAnswers;
	}

	/**
	 * Getter for myCorrectAnswer
	 * 
	 * @return myCorrectAnswer
	 */
	public int getMyCorrectAnswer() {
		return myCorrectAnswer;
	}

}
