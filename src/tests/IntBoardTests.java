/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C12A-1 Clue Paths
 */
package tests;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import experiment.BoardCell;
import experiment.IntBoard;

public class IntBoardTests {
	private IntBoard board;
	
	@Before
	public void initialize() {
		board = new IntBoard();
	}

	// Tests the adjacency list for the cell at (0, 0)
	@Test
	public void testAdjacency0_0(){
		BoardCell cell = board.getCell(0, 0);
		HashSet<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}
	
	// Tests the adjacency list for the cell at (3, 3)
	@Test
	public void testAdjacency3_3(){
		BoardCell cell = board.getCell(3, 3);
		HashSet<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertEquals(2, testList.size());
	}
	
	// Tests the adjacency list for the cell at (1, 3)
	@Test
	public void testAdjacency1_3(){
		BoardCell cell = board.getCell(1, 3);
		HashSet<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(3, testList.size());
	}
	
	// Tests the adjacency list for the cell at (3, 0)
	@Test
	public void testAdjacency3_0(){
		BoardCell cell = board.getCell(3, 0);
		HashSet<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(3, 1)));
		assertEquals(2, testList.size());
	}
	
	// Tests the adjacency list for the cell at (1, 1)
	@Test
	public void testAdjacency1_1(){
		BoardCell cell = board.getCell(1, 1);
		HashSet<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(4, testList.size());
	}
	
	// Tests the adjacency list for the cell at (2, 2)
	@Test
	public void testAdjacency2_2(){
		BoardCell cell = board.getCell(2, 2);
		HashSet<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertEquals(4, testList.size());
	}
	
	// Tests the targets for cell (0, 0) with length 3
	@Test
	public void testTargets0_0_3()
	{
		BoardCell cell = board.getCell(0, 0);
		board.setVisitedToCurrentCell(cell);
		board.calcTargets(cell, 3);
		HashSet<BoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	// Tests the targets for cell (0, 1) with length 2
	@Test
	public void testTargets0_1_2()
	{
		BoardCell cell = board.getCell(0, 1);
		board.setVisitedToCurrentCell(cell);
		board.calcTargets(cell, 2);
		HashSet<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 2)));
	}
	
	// Tests the targets for cell (2, 2) with length 4
	@Test
	public void testTargets2_2_4()
	{
		BoardCell cell = board.getCell(2, 2);
		board.setVisitedToCurrentCell(cell);
		board.calcTargets(cell, 4);
		HashSet<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
	}
	
	// Tests the targets for cell (3, 0) with length 6
	@Test
	public void testTargets3_0_6()
	{
		BoardCell cell = board.getCell(3, 0);
		board.setVisitedToCurrentCell(cell);
		board.calcTargets(cell, 6);
		HashSet<BoardCell> targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(2, 3)));
	}
	
	// Tests the targets for cell (3, 2) with length 1
	@Test
	public void testTargets3_2_1()
	{
		BoardCell cell = board.getCell(3, 2);
		board.setVisitedToCurrentCell(cell);
		board.calcTargets(cell, 1);
		HashSet<BoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(2, 2)));
	}
	
	// Tests the targets for cell (1, 1) with length 5
	@Test
	public void testTargets1_1_5()
	{
		BoardCell cell = board.getCell(1, 1);
		board.setVisitedToCurrentCell(cell);
		board.calcTargets(cell, 5);
		HashSet<BoardCell> targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(3, 2)));
	}
	
}
