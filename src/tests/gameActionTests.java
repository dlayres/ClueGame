package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.HashSet;
import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

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

	/**
	 * selectCPURecentlyLeftRoomAndEnterAnother() : CPU recently left a room, and another room is within reach, so it enters that room
	 */
	@Test
	public void selectCPURecentlyLeftRoomAndEnterAnother() {
		ComputerPlayer testCPU = new ComputerPlayer(18,15); // new cpu player at location row 18, column 15
		testCPU.setRecentlyLeftARoom(true, 'K');
		board.calcTargets(testCPU.getRow(),testCPU.getColumn(),2); // assume cpu rolls a 2
		BoardCell moveTo = testCPU.selectTarget(board.getTargets());
		testCPU.updateLocation(moveTo);
		assertEquals(18, testCPU.getRow());
		assertEquals(17, testCPU.getColumn());
	}

	/**
	 * selectCPURecentlyLeftRoomAndNoOtherRoomNear() : CPU recently left a room, and there is no other room nearby, so it chooses next target randomly
	 */
	@Test
	public void selectCPURecentlyLeftRoomAndNoOtherRoomNear() {
		ComputerPlayer testCPU = new ComputerPlayer(3,9); // new cpu player at location row 18, column 15
		testCPU.setRecentlyLeftARoom(true, 'B');
		board.calcTargets(testCPU.getRow(),testCPU.getColumn(),4); // assume cpu rolls a 4
		BoardCell moveTo = testCPU.selectTarget(board.getTargets());
		assertTrue(board.getTargets().contains(moveTo));
	}

	/**
	 * selectCPURandomWalkway() : There is no room for the CPU to go in, so it selects random walkway
	 */
	@Test
	public void selectCPURandomWalkway() {
		ComputerPlayer testCPU = new ComputerPlayer(14, 6); // new cpu player at location row 14, column 6
		board.calcTargets(testCPU.getRow(),testCPU.getColumn(),2); // assume cpu rolls a 2
		BoardCell moveTo = testCPU.selectTarget(board.getTargets());
		assertTrue(board.getTargets().contains(moveTo));
	}

	
	/**
	 * testCorrectAccusation() : Checks that a correct accusation returns as correct
	 */
	@Test
	public void testCorrectAccusation() {
		HumanPlayer testHuman = new HumanPlayer(); // new human to test with (not cpu)
		Solution proposedSolution = testHuman.makeAccusation("Test Player", "Test Weapon", "Test Room"); // set proposedSolution/accusation to correct solution
		board.setChosenAnswer("Test Player", "Test Weapon", "Test Room"); // set correct solution in board
		assertTrue(board.testAccusation(proposedSolution));
	}

	/**
	 * testWrongPlayerAccusation() : Tests an accusation with an incorrect player assertion
	 */
	@Test
	public void testWrongPlayerAccusation() {
		HumanPlayer testHuman = new HumanPlayer();
		Solution proposedSolution = testHuman.makeAccusation("Wrong Player", "Test Weapon", "Test Room");
		board.setChosenAnswer("Test Player", "Test Weapon", "Test Room");
		assertEquals(false, board.testAccusation(proposedSolution));
	}

	/**
	 * testWrongWeaponAccusation() : Tests an accusation with an incorrect weapon assertion
	 */
	@Test
	public void testWrongWeaponAccusation() {
		HumanPlayer testHuman = new HumanPlayer();
		Solution proposedSolution = testHuman.makeAccusation("Test Player", "Wrong Weapon", "Test Room");
		board.setChosenAnswer("Test Player", "Test Weapon", "Test Room");
		assertEquals(false, board.testAccusation(proposedSolution));
	}

	/**
	 * testWrongRoomAccusation() : Tests an accusation with an incorrect room assertion
	 */
	@Test
	public void testWrongRoomAccusation() {
		HumanPlayer testHuman = new HumanPlayer();
		Solution proposedSolution = testHuman.makeAccusation("Test Player", "Test Weapon", "Wrong Room");
		board.setChosenAnswer("Test Player", "Test Weapon", "Test Room");
		assertEquals(false, board.testAccusation(proposedSolution));
	}

	/**
	 * testSuggestionRoom() : See's if the cpu will make a suggestion with valid room (which is where the cpu is currently located)
	 */
	@Test
	public void testSuggestionRoom() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17); // a library door/room location for cpu to make suggestion
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend()); // make a suggestion given current location (room) and known information
		assertEquals("Library",testSuggestion.room); // in this test, the cpu is currently in the library
	}

	/**
	 * testSingleWeaponChoice() : See that the CPU will suggest the only remaining weapon
	 */
	@Test
	public void testSingleWeaponChoice() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17);
		HashSet<Card> testWeaponSet = new HashSet<Card>(); // make a new unknown weapon set
		Card testWeapon = new Card("Ballpoint Pen",CardType.WEAPON);  // make a single weapon (Ballpoint Pen)
		testWeaponSet.add(testWeapon); // add the weapon to the set (so set size is now 1)
		testCPU.setUnseenWeaponCards(testWeaponSet); // change the cpu's unknown weapon information to this new weapon set (still size 1)
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend());
		assertEquals("Ballpoint Pen",testSuggestion.weapon); // only unknown weapon in set should be "Ballpoint Pen"
	}

	/**
	 * testSinglePlayerChoice() : See that the CPU will suggest the only remaining player
	 */
	@Test
	public void testSinglePlayerChoice() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17);
		
		// Makes a new unknown player information set to only have a single player "Cpt. Red"  
		HashSet<Card> testPlayerSet = new HashSet<Card>();
		Card testPlayer = new Card("Cpt. Red",CardType.PLAYER);
		testPlayerSet.add(testPlayer);
		testCPU.setUnseenPlayerCards(testPlayerSet);
		
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend());
		assertEquals("Cpt. Red",testSuggestion.player); // only unknown player in set should be "Cpt. Red"
	}

	/**
	 * testRandomWeaponChoice() : See that the CPU will pick a random weapon to suggest when there is more than 1 option
	 */
	@Test
	public void testRandomWeaponChoice() {
		ComputerPlayer testCPU = (ComputerPlayer) board.getPlayerList()[1]; // board.getPlayerList()[1] returns the first ComputerPlayer object which was initialized earlier
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend()); // have the testCPU player make a suggestion
		Card testWeaponCard = new Card(testSuggestion.weapon, CardType.WEAPON); // make a new weapon card based off of the testCPU's suggestion
		boolean hasWeaponCard = false; // assume we have not picked a correct weapon
		for(Card nextCard : testCPU.getUnseenWeaponCards()){ // go through each of the possible options for unknown weapons and see if we find our suggestion 
			if(nextCard.equals(testWeaponCard)){ // if we have suggested a valid weapon card
				hasWeaponCard = true;
				break;
			}
		}
		assertTrue(hasWeaponCard);
	}

	/**
	 * testRandomPlayerChoice() : See that the CPU will pick a random player to suggest when there is more than 1 option
	 * Works almost exactly the same as testRandomWeaponChoice(), but with player card instead of weapon card
	 */
	@Test
	public void testRandomPlayerChoice() {
		ComputerPlayer testCPU = (ComputerPlayer) board.getPlayerList()[1];
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend());
		Card testPlayerCard = new Card(testSuggestion.player, CardType.PLAYER);
		boolean hasPlayerCard = false;
		for(Card nextCard : testCPU.getUnseenPlayerCards()){
			if(nextCard.equals(testPlayerCard)){
				hasPlayerCard = true;
				break;
			}
		}
		assertTrue(hasPlayerCard);
	}

	@Test
	public void testSingleCardMatch() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17);
		Solution testSolution = new Solution("Cpt. Red","Ballpoint Pen","Library");
		HashSet<Card> testCards = new HashSet<Card>();
		Card playerCard = new Card("Cpt. Red",CardType.PLAYER);
		Card weaponCard = new Card("Rock",CardType.WEAPON);
		Card roomCard = new Card("Dining Room",CardType.ROOM);
		testCards.add(playerCard);
		testCards.add(weaponCard);
		testCards.add(roomCard);
		testCPU.setMyCards(testCards);
		Card resultingCard = testCPU.disproveSuggestion(testSolution);
		assertEquals("Cpt. Red", resultingCard.getCardName());
	}

	@Test
	public void testMultipleCardMatch() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17);
		Solution testSolution = new Solution("Cpt. Red","Ballpoint Pen","Library");
		HashSet<Card> testCards = new HashSet<Card>();
		Card playerCard = new Card("Cpt. Red",CardType.PLAYER);
		Card weaponCard = new Card("Ballpoint Pen",CardType.WEAPON);
		Card roomCard = new Card("Dining Room",CardType.ROOM);
		testCards.add(playerCard);
		testCards.add(weaponCard);
		testCards.add(roomCard);
		int matchingName = 0;
		int matchingWeapon = 0;
		testCPU.setMyCards(testCards);
		for(int i = 0; i < 20; i++){
			Card resultingCard = testCPU.disproveSuggestion(testSolution);
			if (resultingCard.getCardName().equals("Cpt.Red")){
				matchingName++;
			} else if(resultingCard.getCardName().equals("Ballpoint Pen")){
				matchingWeapon++;
			}
		}
		assertTrue(matchingName >= 1 && matchingWeapon >= 1);
	}
	
	@Test
	public void testNoCardMatch() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17);
		Solution testSolution = new Solution("Cpt. Red","Ballpoint Pen","Library");
		HashSet<Card> testCards = new HashSet<Card>();
		Card playerCard = new Card("Mr. Orange",CardType.PLAYER);
		Card weaponCard = new Card("Rock",CardType.WEAPON);
		Card roomCard = new Card("Dining Room",CardType.ROOM);
		testCards.add(playerCard);
		testCards.add(weaponCard);
		testCards.add(roomCard);
		testCPU.setMyCards(testCards);
		Card resultingCard = testCPU.disproveSuggestion(testSolution);
		assertEquals(null, resultingCard);
	}
}
