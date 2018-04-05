package tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.ComputerPlayer;

public class gameActionTests {

	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance there is
		board = Board.getInstance();
		board.setConfigFiles("boardLayout.csv", "ourLegend.txt");		
		// Initialize will load both of our board configuration files 
		board.initialize();
		// set the file names to use our player config file
		board.setGameSetupFiles("playerConfig.txt", "weaponConfig.txt");		
		// Will load our player config file 
		board.loadPlayerConfig();
		// Game card and solution handling
		board.loadCards();
		board.selectAnswer();
		board.dealCards();
	}

	/**
	 * selectCPURoom() : The CPU hasn't recently entered a room, but can move to a targeted room.
	 * The CPU should choose to enter this room
	 */
	@Test
	public void selectCPURoom() {
		ComputerPlayer testCPU = new ComputerPlayer(15,9); // new cpu player at location row 15, column 9
		board.calcTargets(testCPU.getRow(),testCPU.getColumn(),3); // assume cpu rolls a 3
		BoardCell moveTo = testCPU.selectTarget(board.getTargets());
		testCPU.updateLocation(moveTo);
		assertEquals(16, testCPU.getRow());
		assertEquals(11, testCPU.getColumn());
	}

}
