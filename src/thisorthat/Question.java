package thisorthat;

public class Question implements java.io.Serializable {
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
	 * Default Constructor for testing purposes
	 */
	public Question() {
		this.mySubject = "Gullunge";
		this.myAnswers = new String[] { "An Ikea Product", "A Type of Cheese" };
		this.myCorrectAnswer = 1;
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
