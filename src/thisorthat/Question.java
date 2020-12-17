package thisorthat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Question is an abstraction of a question, its potential answers, and its correct answer 
 * for use in a Trivia maze program.
 */
@SuppressWarnings("serial")
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
	 * Random used to select random question from database
	 */
	private Random rand = new Random();

	/*
	 * The database of questions and answers
	 */
	private Database myDatabase = new Database();

	/*
	 * Default Constructor for testing purposes
	 */
	public Question() {
		List<String> keys = new ArrayList<String>(myDatabase.getDatabase().keySet());
		int randomInt = rand.nextInt(keys.size());
		String prompt = keys.get(randomInt);
		List<String> questions = myDatabase.getDatabase().get(prompt);
		this.mySubject = prompt;
		this.myAnswers = new String[] { questions.get(1), questions.get(2) };
		System.out.println(questions.get(1) + "," + questions.get(2));
		this.myCorrectAnswer = Integer.valueOf(questions.get(3));
	}

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
