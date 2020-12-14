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
public class TestRoom {
	
	@Test
	void testConstructor(){
		Question testQuestion = new Question("Subject", new String[]{"Incorrect Answer", "Correct Answer"}, 1);
		Room lockedRoom = new Room(testQuestion, false, false,false, false);

		assertFalse(lockedRoom.getIsAcessible());
		assertFalse(lockedRoom.getIsLocked());
		assertFalse(lockedRoom.getIsGoal());
		assertFalse(lockedRoom.getIsKeyRoom());
	}

	
	

}
