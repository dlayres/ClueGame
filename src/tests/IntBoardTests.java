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
	public void testAdjacency00(){
		BoardCell cell = board.getCell(0, 0);
		HashSet<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}

}
