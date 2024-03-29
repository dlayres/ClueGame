/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C14A-1 Clue Board III
 */
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class AdjacencyTargetTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use our config files
		board.setConfigFiles("boardLayout (worse rooms).csv", "ourLegend.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}

	/**
	 * Edge Test Locations: 
	 * Left Edge - testAdjacenciesInsideRooms()
	 * Right Edge - testAdjacentDoorways()
	 * Top Edge - testAdjacencyWalkways()
	 * Bottom Edge - testAdjacencyWalkways()
	 */
	
	// Ensure that player does not move around within room
	// These cells are ORANGE on the planning spreadsheet
	/**
	 * 3 cells tested
	 */
	@Test
	public void testAdjacenciesInsideRooms() // test cells inside rooms
	{
		// Test a cell on LEFT EDGE
		Set<BoardCell> testList = board.getAdjList(0, 0);
		assertEquals(0, testList.size());

		// Test one that has walkway above
		testList = board.getAdjList(6, 15);
		assertEquals(0, testList.size());

		// Test a location near center of room
		testList = board.getAdjList(18,9);
		assertEquals(0, testList.size());
	}

	// Ensure that the adjacency list from a doorway is only the
	// walkway. NOTE: This test could be merged with door 
	// direction test. 
	// These tests are PURPLE on the planning spreadsheet
	/**
	 * 3 cells tested
	 */
	@Test
	public void testAdjacencyRoomExit() // test doorway as the current location
	{
		// Test a doorway right, where there is a walkway below
		Set<BoardCell> testList = board.getAdjList(2, 9);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(2, 10)));

		// Test a doorway right
		testList = board.getAdjList(9, 3);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(9, 4)));

		// Test a doorway down
		testList = board.getAdjList(11, 17);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(12, 17)));
	}

	// Test adjacency at entrance to rooms
	// These tests are GREEN in planning spreadsheet
	/**
	 * 4 cells tested
	 */
	@Test
	public void testAdjacencyDoorways() // test cells next to a doorway
	{
		// Test beside a door direction DOWN
		Set<BoardCell> testList = board.getAdjList(14, 1);
		assertTrue(testList.contains(board.getCellAt(13, 1))); // door
		assertTrue(testList.contains(board.getCellAt(14, 0)));
		assertTrue(testList.contains(board.getCellAt(15, 1)));
		assertEquals(3, testList.size());

		// Test beside a door direction LEFT
		testList = board.getAdjList(16, 10);
		assertTrue(testList.contains(board.getCellAt(16, 11))); // door
		assertTrue(testList.contains(board.getCellAt(16, 9)));
		assertTrue(testList.contains(board.getCellAt(15, 10)));
		assertEquals(3, testList.size());

		// Test a cell that is above an UP door, also at the RIGHT EDGE
		testList = board.getAdjList(13,19);
		assertTrue(testList.contains(board.getCellAt(14, 19))); // door
		assertTrue(testList.contains(board.getCellAt(12, 19)));
		assertTrue(testList.contains(board.getCellAt(13, 18)));
		assertEquals(3, testList.size());
		
		// Test a cell next to a right door
		testList = board.getAdjList(0, 15);
		assertTrue(testList.contains(board.getCellAt(0, 14))); // door
		assertTrue(testList.contains(board.getCellAt(1, 15)));
	}

	// Test a variety of walkway scenarios
	// These tests are LIGHT PURPLE on the planning spreadsheet
	/**
	 * 3 cells tested
	 * Also tests cells that have rooms next to them
	 */
	@Test
	public void testAdjacencyWalkways() 
	{
		// Walkway with only 1 W under it, room cells around the other spots, also on the TOP EDGE
		Set<BoardCell> testList = board.getAdjList(0, 10);
		assertTrue(testList.contains(board.getCellAt(1, 10)));
		assertEquals(1, testList.size());

		// A walkway with 4 other walkway spots around it
		testList = board.getAdjList(14, 7);
		assertTrue(testList.contains(board.getCellAt(13, 7)));
		assertTrue(testList.contains(board.getCellAt(15, 7)));
		assertTrue(testList.contains(board.getCellAt(14, 6)));
		assertTrue(testList.contains(board.getCellAt(14, 8)));
		assertEquals(4, testList.size());

		// A walkway on the BOTTOM EDGE, 2 walkway spots around it, and a room cell around it
		testList = board.getAdjList(19, 15);
		assertTrue(testList.contains(board.getCellAt(19, 16)));
		assertTrue(testList.contains(board.getCellAt(18, 15)));
		assertEquals(2, testList.size());

	}


	// Tests of just walkways, 1 step, includes on edge of board
	// and beside room
	// These are LIGHT BLUE on the planning spreadsheet
	/**
	 * 1 cell tested
	 */
	@Test
	public void testTargetsOneStep() {
		// 
		board.calcTargets(19, 6, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(18, 6)));
		assertTrue(targets.contains(board.getCellAt(19, 7)));	
			
	}

	// Tests of just walkways, 2 steps
	// These are LIGHT BLUE on the planning spreadsheet
	/**
	 * 1 cell tested
	 */
	@Test
	public void testTargetsTwoSteps() {
		board.calcTargets(8, 5, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(7, 4)));
		assertTrue(targets.contains(board.getCellAt(9, 4)));
		assertTrue(targets.contains(board.getCellAt(6, 5)));
		assertTrue(targets.contains(board.getCellAt(10, 5)));
		
	}

	// Tests of just walkways, 3 steps
	// These are LIGHT BLUE on the planning spreadsheet
	/**
	 * 1 cell tested
	 */
	@Test
	public void testTargetsThreeSteps() {
		board.calcTargets(13, 15, 3);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCellAt(10, 15)));
		assertTrue(targets.contains(board.getCellAt(11, 14)));
		assertTrue(targets.contains(board.getCellAt(12, 15)));
		assertTrue(targets.contains(board.getCellAt(12, 17)));
		assertTrue(targets.contains(board.getCellAt(13, 12)));
		assertTrue(targets.contains(board.getCellAt(13, 14)));
		assertTrue(targets.contains(board.getCellAt(13, 16)));
		assertTrue(targets.contains(board.getCellAt(13, 18)));
		assertTrue(targets.contains(board.getCellAt(14, 13)));
		assertTrue(targets.contains(board.getCellAt(14, 15)));
		assertTrue(targets.contains(board.getCellAt(15, 14)));
		assertTrue(targets.contains(board.getCellAt(15, 16)));
		assertTrue(targets.contains(board.getCellAt(16, 15)));

	}	

	// Tests of just walkways, 5 steps
	// These are LIGHT BLUE on the planning spreadsheet
	/**
	 * 1 cell tested
	 */
	@Test
	public void testTargetsFiveSteps() {
		board.calcTargets(19, 7, 5);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCellAt(14, 7)));
		assertTrue(targets.contains(board.getCellAt(15, 6)));	
		assertTrue(targets.contains(board.getCellAt(15, 8)));	
		assertTrue(targets.contains(board.getCellAt(16, 7)));	
		assertTrue(targets.contains(board.getCellAt(16, 9)));
		assertTrue(targets.contains(board.getCellAt(17, 6)));	
		assertTrue(targets.contains(board.getCellAt(18, 7)));	
		assertTrue(targets.contains(board.getCellAt(19, 6)));	
	}	

	/**
	 * Total of 4 walkways tested using varying steps
	 */
	
	
	// Test getting into a room
	// These are LIGHT BLUE on the planning spreadsheet
	/**
	 * 1 cell tested
	 */
	@Test 
	public void testTargetsIntoRoom()
	{
		board.calcTargets(17, 16, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(17, 17))); // door
		assertTrue(targets.contains(board.getCellAt(16, 16)));
		assertTrue(targets.contains(board.getCellAt(18, 16)));
		assertTrue(targets.contains(board.getCellAt(17, 15)));
	}

	// Test getting into room, doesn't always require all steps
	// These are LIGHT BLUE on the planning spreadsheet
	
	/**
	 * 2 cells tested
	 */
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(4, 17, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(4, 15)));
		assertTrue(targets.contains(board.getCellAt(4, 19)));
		assertTrue(targets.contains(board.getCellAt(5, 16)));
		assertTrue(targets.contains(board.getCellAt(5, 18)));
		assertTrue(targets.contains(board.getCellAt(3, 18))); // door		
		assertTrue(targets.contains(board.getCellAt(6, 17))); // door	
		
		board.calcTargets(17, 16, 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCellAt(14, 16)));
		assertTrue(targets.contains(board.getCellAt(15, 15)));
		assertTrue(targets.contains(board.getCellAt(16, 14)));
		assertTrue(targets.contains(board.getCellAt(16, 16)));
		assertTrue(targets.contains(board.getCellAt(17, 15))); 		
		assertTrue(targets.contains(board.getCellAt(18, 16)));	
		assertTrue(targets.contains(board.getCellAt(19, 15)));
		assertTrue(targets.contains(board.getCellAt(17, 17))); // door 		
		assertTrue(targets.contains(board.getCellAt(18, 17))); // door	
		assertTrue(targets.contains(board.getCellAt(18, 14))); // door

	}

	// Test getting out of a room
	// These are LIGHT BLUE on the planning spreadsheet
	/**
	 * 2 cells tested
	 */
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(3, 2, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(4, 2)));
		// Take two steps
		board.calcTargets(13, 1, 1);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(14, 1)));
	}

}

