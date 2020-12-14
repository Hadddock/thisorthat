package thisorthat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({})
public class TestQuestion {
	
	@Test
	void testConstructor(){
		Question testQuestion = new Question("Subject", new String[]{"Incorrect Answer", "Correct Answer"}, 1);
		assertTrue(testQuestion.getMySubject().equals("Subject"));
		assertTrue(testQuestion.getMyAnswers()[0].equals("Incorrect Answer"));
		assertTrue(testQuestion.getMyAnswers()[1].equals("Correct Answer"));
		assertTrue(testQuestion.getMyCorrectAnswer() == 1);
	}
	@Test
	void testGetMySubject() {
		Question testQuestion = new Question("Subject", new String[]{"Incorrect Answer", "Correct Answer"}, 1);
		assertTrue(testQuestion.getMySubject().equals("Subject"));
	}
	@Test
	void testGetMyAnswers() {
		Question testQuestion = new Question("Subject", new String[]{"Incorrect Answer", "Correct Answer"}, 1);
		assertTrue(testQuestion.getMyAnswers()[0].equals("Incorrect Answer"));
		assertTrue(testQuestion.getMyAnswers().length == 2);
	}
	@Test
	void testGetMySubjectCorrectAnswer() {
		Question testQuestion = new Question("Subject", new String[]{"Incorrect Answer", "Correct Answer"}, 1);
		assertTrue(testQuestion.getMyCorrectAnswer() == 1);
	}
	
	

}
