/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C13A-1 Clue Paths
 */
package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class BoardTests {
	// Constants to test whether the file was loaded correctly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLUMNS = 20;

	// Only need 1 board to test
	private static Board board;

	@BeforeClass
	public static void setup() {
		// Board is singleton, get the only instance there is
		board = Board.getInstance();
		// set the file names to use our board layout files
		board.setConfigFiles("boardLayout (worse rooms).csv", "ourLegend.txt");		
		// Initialize will load BOTH of our configuration files 
		board.initialize();
	}

	@Test
	public void testRooms() {
		// Get the map of initial => room 
		Map<Character, String> legend = board.getLegend();
		// Ensure we read the correct number of rooms
		assertEquals(LEGEND_SIZE, legend.size());
		// To ensure data is correctly loaded, test retrieving a few rooms 
		// from the hash, including the first and last in the file and a few others
		assertEquals("Laundry Room", legend.get('L'));
		assertEquals("Library", legend.get('I'));
		assertEquals("Basement", legend.get('A'));
		assertEquals("Office", legend.get('O'));
		assertEquals("Walkway", legend.get('W'));
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), and
	// two cells that are not a doorway.
	@Test
	public void FourDoorDirections() {
		// Test if cell(2,9) is being made as a RIGHT doorway
		BoardCell room = board.getCellAt(2, 9);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());

		// Test if cell(3,2) is being made as a DOWN doorway
		room = board.getCellAt(3, 2);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());

		// Test if cell(16,11) is a LEFT doorway
		room = board.getCellAt(16, 11);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());

		// Test if cell(6,17) is an UP doorway
		room = board.getCellAt(6, 17);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());

		// Test if cell(17,14) is not a doorway
		room = board.getCellAt(17, 14);
		assertFalse(room.isDoorway());	

		// Test if cell(14,16) is not a doorway
		BoardCell cell = board.getCellAt(14, 16);
		assertFalse(cell.isDoorway());		
	}

	// Test that we have the correct number of doorway in our board
	@Test
	public void testNumberOfDoorways() 	{
		int numDoors = 0;
		// Check if each cell is a doorway. If the cell is doorway, increment door count by 1
		for (int row = 0; row < board.getNumRows(); row++) {
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway()) {	
					numDoors++;
				}	
			}
		}
		Assert.assertEquals(18, numDoors);
	}


	// Test if 7 different board cells have the correct initial
	@Test
	public void testRoomInitials() {
		assertEquals('L', board.getCellAt(0, 0).getInitial());

		assertEquals('B', board.getCellAt(1, 7).getInitial());

		assertEquals('C', board.getCellAt(3, 11).getInitial());

		assertEquals('P', board.getCellAt(0, 19).getInitial());

		assertEquals('D', board.getCellAt(12, 0).getInitial());

		assertEquals('X', board.getCellAt(9, 9).getInitial());

		assertEquals('O', board.getCellAt(16,17).getInitial());
	}

}
