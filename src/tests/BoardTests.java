package tests;

import static org.junit.Assert.*;

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
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("boardLayout.csv", "ourLegend.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus 
	// two cells that are not a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		BoardCell room = board.getCellAt(2, 9);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		
		room = board.getCellAt(3, 2);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		
		room = board.getCellAt(16, 11);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		
		room = board.getCellAt(6, 17);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(17, 14);
		assertFalse(room.isDoorway());	
		
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(14, 16);
		assertFalse(cell.isDoorway());		
	}


	@Test
	public void testNumberOfDoorways() 	{
		int numDoors = 0;
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
	

	// Test a few room cells to ensure the room initial is correct.
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
