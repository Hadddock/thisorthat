package thisorthat;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import thisorthat.Question;
import thisorthat.Room;

class TestRoom {
	static Room lockedRoom;
	static Room accessibleRoom;
	static Room goalRoom;
	static Room keyRoom;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		lockedRoom = new Room(new Question(), false, true, false, false);
		accessibleRoom = new Room(new Question(), true, false, false, false);
		goalRoom = new Room(new Question(), true, false, true, false);
		keyRoom = new Room(new Question(), false, false, false, true);
	}

	@Test
	void testGetAcessible() {
		assertFalse(lockedRoom.getIsAcessible());
		assertTrue(accessibleRoom.getIsAcessible());
		assertTrue(goalRoom.getIsAcessible());
		assertFalse(keyRoom.getIsAcessible());
	}

	@Test
	void testGetIsGoal() {
		assertFalse(lockedRoom.getIsGoal());
		assertFalse(accessibleRoom.getIsGoal());
		assertTrue(goalRoom.getIsGoal());
		assertFalse(keyRoom.getIsGoal());
	}

	@Test
	void testGetIsKeyRoom() {
		assertFalse(lockedRoom.getIsKeyRoom());
		assertFalse(accessibleRoom.getIsKeyRoom());
		assertFalse(goalRoom.getIsKeyRoom());
		assertTrue(keyRoom.getIsKeyRoom());
	}

	@Test
	void testGetMyXCoordinate() {
		lockedRoom.setMyXCoordinate(1);
		assertEquals(lockedRoom.getMyXCoordinate(), 1);
		lockedRoom.setMyXCoordinate(0);
	}

	@Test
	void testGetMyYCoordinate() {
		lockedRoom.setMyYCoordinate(1);
		assertEquals(lockedRoom.getMyYCoordinate(), 1);
		lockedRoom.setMyYCoordinate(0);
	}

	@Test
	void testSetMyXCoordinate() {
		lockedRoom.setMyXCoordinate(1);
		assertEquals(lockedRoom.getMyXCoordinate(), 1);
		lockedRoom.setMyXCoordinate(0);
	}

	@Test
	void testSetMyYCoordinate() {
		lockedRoom.setMyYCoordinate(1);
		assertEquals(lockedRoom.getMyYCoordinate(), 1);
		lockedRoom.setMyYCoordinate(0);
	}

	@Test
	void testSetIsLocked() {
		accessibleRoom.setIsLocked(true);
		assertTrue(accessibleRoom.getIsLocked());
		accessibleRoom.setIsLocked(false);
		assertFalse(accessibleRoom.getIsLocked());

		lockedRoom.setIsLocked(false);
		assertFalse(lockedRoom.getIsLocked());
		lockedRoom.setIsLocked(true);
		assertTrue(lockedRoom.getIsLocked());

	}

	@Test
	void testGetIsLocked() {
		assertFalse(accessibleRoom.getIsLocked());
		assertFalse(goalRoom.getIsLocked());
		assertFalse(keyRoom.getIsLocked());
		assertTrue(lockedRoom.getIsLocked());
	}

	@Test
	void testSetIsKeyRoom() {
		keyRoom.setIsKeyRoom(false);
		assertFalse(keyRoom.getIsKeyRoom());
		keyRoom.setIsKeyRoom(true);
		assertTrue(keyRoom.getIsKeyRoom());
	}

}
