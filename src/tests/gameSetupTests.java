package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

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
		board.setConfigFiles("boardLayout (worse rooms).csv", "ourLegend.txt");		
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
	 * testFirstPlayer()
	 * Tests if the first player's name is Mr. Orange,
	 * if the first player's color is orange,
	 * if the first player starts at row 19 column 6,
	 * and if the first player is a human.
	 */
	@Test
	public void testFirstPlayer() {
		Player human = board.getPlayerList()[0];
		assertEquals("Mr. Orange", human.getPlayerName());
		assertEquals(Color.orange, human.getColor());
		assertEquals(19, human.getRow());
		assertEquals(6, human.getColumn());
		assertTrue(human instanceof HumanPlayer);
	}

	/**
	 * testThirdPlayer()
	 * Tests if the third player's name is Dr. Purple,
	 * if the third player's color is magenta,
	 * if the third player's starting location is row 0 column 10,
	 * and if the third player is a cpu
	 */
	@Test
	public void testThirdPlayer() {
		Player cpu = board.getPlayerList()[2];
		assertEquals("Dr. Purple", cpu.getPlayerName());
		assertEquals(Color.magenta, cpu.getColor());
		assertEquals(0, cpu.getRow());
		assertEquals(10, cpu.getColumn());
		assertTrue(cpu instanceof ComputerPlayer);
	}

	/**
	 * testSixthPlayer()
	 * Tests if the sixth player's name is Cpt. Red,
	 * if the sixth player's color is red,
	 * if the sixth player's starting location is row 0 column 15,
	 * and if the sixth player is a cpu
	 */
	@Test
	public void testSixthPlayer() {
		Player cpu = board.getPlayerList()[5];
		assertEquals("Cpt. Red", cpu.getPlayerName());
		assertEquals(Color.red, cpu.getColor());
		assertEquals(0, cpu.getRow());
		assertEquals(15, cpu.getColumn());
		assertTrue(cpu instanceof ComputerPlayer);
	}

	/**
	 * testCardTotals()
	 * First checks if the have the correct total amount of cards,
	 * then checks if we have the correct number of each type of card.
	 */
	@Test
	public void testCardTotals(){
		assertEquals(21, board.getCards().size()); // Check total number of cards

		int totalPlayerCards = 0;
		int totalWeaponCards = 0;
		int totalRoomCards = 0;
		
		// For each card for the board, increment total card type counters based on the current card's type
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
		assertEquals(9, totalRoomCards); // Should have 9 room cards
		assertEquals(6, totalWeaponCards); // Should have 6 weapon cards
		assertEquals(6, totalPlayerCards); // Should have 6 player cards
	}

	/**
	 * testCardsInSet()
	 * First tests if the Dr. Purple - PLAYER card is in the deck
	 * Next tests if the Plastic Fork - WEAPON card is in the deck
	 * and finally tess if the Library - ROOM card is in the deck
	 */
	@Test
	public void testCardsInSet(){
		// The for-loops for this function basically go through each card and see if its the card
		// that we are looking for.
		
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
	
	/**
	 * testDealtCards()
	 * For each player in the board, tests if each player has 3 cards.
	 * After checking every player, checks if all non-solution cards have been dealt (18 total)
	 */
	@Test
	public void testDealtCards(){
		int totalCardsDealt = 0; // Test all cards are dealt
		Player[] playerList = board.getPlayerList();
		for(int i = 0; i < 6; i++){
			int size = playerList[i].getMyCards().size();
			assertEquals(3, size); // Tests if player has 3 cards
			totalCardsDealt+=size;
		}
		assertEquals(18, totalCardsDealt);
	}
	
	/**
	 * testNoRepeatedCards()
	 * First creates and initializes a copy set of the card deck
	 * Next goes through each player and removes their cards from the copy deck
	 * Since we have 21 total cards and 18 are dealt to the players, there should
	 * only be 3 cards left after removing all cards from the players (the solution cards)
	 */
	@Test
	public void testNoRepeatedCards(){
		// Make a copy of the card deck, initial size is 21
		Set<Card> cardsCopy = new HashSet<Card>();
		for(Card c : board.getCards()){
			cardsCopy.add(c);
		}
		// For each player in the game, remove their cards from the copy card deck
		for(int i = 0; i < 6; i++){
			for(Card c : board.getPlayerList()[i].getMyCards()){
				cardsCopy.remove(c);
			}
		}
		// 6 players each with 3 cards, so there should be 18 cards removed from the copy deck.
		// There should therefore be 21 - 18 = 3 cards left in the copy deck (the solution cards).
		assertEquals(3, cardsCopy.size());
	}

}
