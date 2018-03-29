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
	
	@Test
	public void testThirdPlayer() {
		Player cpu = board.getPlayerList()[2];
		assertEquals("Dr. Purple", cpu.getPlayerName());
		assertEquals(Color.magenta, cpu.getColor());
		assertEquals(0, cpu.getRow());
		assertEquals(10, cpu.getColumn());
		assertTrue(cpu instanceof ComputerPlayer);
	}
	
	@Test
	public void testSixthPlayer() {
		Player cpu = board.getPlayerList()[5];
		assertEquals("Cpt. Red", cpu.getPlayerName());
		assertEquals(Color.red, cpu.getColor());
		assertEquals(0, cpu.getRow());
		assertEquals(15, cpu.getColumn());
		assertTrue(cpu instanceof ComputerPlayer);
	}
}
