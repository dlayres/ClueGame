package tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.HumanPlayer;
import clueGame.ComputerPlayer;
import clueGame.Player;

public class gameSetupTests {

	private static Board board;

	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance there is
		board = Board.getInstance();
		board.setConfigFiles("boardLayout.csv", "ourLegend.txt");		
		// Initialize will load BOTH of our configuration files 
		board.initialize();
		// set the file names to use our player config file
		board.setGameSetupFiles("playerConfig.txt", "weaponConfig.txt");		
		// Will load our player config file 
		board.loadPlayerConfig();
		board.loadCards();
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


	@Test
	public void testCardTotals(){
		assertEquals(21, board.getCards().size());

		int totalPlayerCards = 0;
		int totalWeaponCards = 0;
		int totalRoomCards = 0;

		for(Card c: board.getCards()){
			switch(c.getType()){
			case ROOM:
				totalRoomCards++;
				break;
			case WEAPON:
				totalWeaponCards++;
				break;
			case PLAYER:
				totalPlayerCards++;
				break;
			default:
				break;
			}
		}
		assertEquals(9, totalRoomCards);
		assertEquals(6, totalWeaponCards);
		assertEquals(6, totalPlayerCards);
	}

	@Test
	public void testCardsInSet(){
		boolean contains = false;
		Card testCard = new Card("Dr. Purple", CardType.PLAYER);
		for(Card c: board.getCards()){
			if(c.equals(testCard)){
				contains = true;
			}
		}
		assertTrue(contains);

		contains = false;
		testCard = new Card("Plastic Fork", CardType.WEAPON);
		for(Card c: board.getCards()){
			if(c.equals(testCard)){
				contains = true;
			}
		}
		assertTrue(contains);

		contains = false;
		testCard = new Card("Library", CardType.ROOM);
		for(Card c: board.getCards()){
			if(c.equals(testCard)){
				contains = true;
			}
		}
		assertTrue(contains);
	}

}
