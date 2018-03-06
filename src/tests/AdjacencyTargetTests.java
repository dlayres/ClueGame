package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;

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
		board.setConfigFiles("boardLayout.csv", "ourLegend.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}
	
	
	

}