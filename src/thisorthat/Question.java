package thisorthat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Question implements java.io.Serializable {
	
	/**
	 * Generated serializable ID.
	 */
	private static final long serialVersionUID = -5975805758604528342L;
	/*
	 * The Subject to be placed in one of the categories in myAnswers
	 */
	private String mySubject;
	/*
	 * The Potential Categories to place the subject. The correct subject's index is myCorrectAnswer.
	 */
	private String[] myAnswers;
	/*
	 * The correct subject's index in myAnswers.
	 */
	private int myCorrectAnswer;
	/*
	 * The database of questions and answers
	 */
	private Database myDatabase = new Database();
//	/*
//	 * The way that the data from the database is stored
//	 */
//	private Map<String, List<String>> myData = myDatabase.getDatabase();
	/*
	 * The way of choosing questions for a maze
	 */
	private Random rand = new Random();
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

	public String getMySubject() {
		return mySubject;
	}

	public String[] getMyAnswers() {
		return myAnswers;
	}

	public int getMyCorrectAnswer() {
		return myCorrectAnswer;
	}

}
