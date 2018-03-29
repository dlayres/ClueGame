package tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;
import clueGame.Player;

public class gameSetupTests {

	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance there is
		board = Board.getInstance();
		// set the file names to use our player config file
		board.setGameSetupFiles("playerConfig.txt");		
		// Will load our player config file 
		board.loadPlayerConfig();
	}

	@Test
	public void testFirstPlayer() {
		Player human = board.getPlayerList()[0];
		assertEquals("Mr. Orange", human.getPlayerName());
		assertEquals(Color.orange, human.getColor());
		assertEquals(19, human.getRow());
		assertEquals(6, human.getColumn());
		assertTrue(human instanceof HumanPlayer);
	}

}
