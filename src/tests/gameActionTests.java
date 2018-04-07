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
	
	
	@Test
	public void testCorrectAccusation() {
		HumanPlayer testHuman = new HumanPlayer();
		Solution proposedSolution = testHuman.makeAccusation("Test Player", "Test Weapon", "Test Room");
		board.setChosenAnswer("Test Player", "Test Weapon", "Test Room");
		assertTrue(board.testAccusation(proposedSolution));
	}
	
	@Test
	public void testWrongPlayerAccusation() {
		HumanPlayer testHuman = new HumanPlayer();
		Solution proposedSolution = testHuman.makeAccusation("Wrong Player", "Test Weapon", "Test Room");
		board.setChosenAnswer("Test Player", "Test Weapon", "Test Room");
		assertEquals(false, board.testAccusation(proposedSolution));
	}
	
	@Test
	public void testWrongWeaponAccusation() {
		HumanPlayer testHuman = new HumanPlayer();
		Solution proposedSolution = testHuman.makeAccusation("Test Player", "Wrong Weapon", "Test Room");
		board.setChosenAnswer("Test Player", "Test Weapon", "Test Room");
		assertEquals(false, board.testAccusation(proposedSolution));
	}
	
	@Test
	public void testWrongRoomAccusation() {
		HumanPlayer testHuman = new HumanPlayer();
		Solution proposedSolution = testHuman.makeAccusation("Test Player", "Test Weapon", "Wrong Room");
		board.setChosenAnswer("Test Player", "Test Weapon", "Test Room");
		assertEquals(false, board.testAccusation(proposedSolution));
	}
	
	@Test
	public void testSuggestionRoom() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17); // a library door/room location
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend());
		assertEquals("Library",testSuggestion.room);
	}
	
	@Test
	public void testSingleWeaponChoice() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17);
		HashSet<Card> testWeaponSet = new HashSet<Card>();
		Card testWeapon = new Card("Ballpoint Pen",CardType.WEAPON);
		testWeaponSet.add(testWeapon);
		testCPU.setUnseenWeaponCards(testWeaponSet);
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend());
		assertEquals("Ballpoint Pen",testSuggestion.weapon);
	}
	
	@Test
	public void testSinglePlayerChoice() {
		ComputerPlayer testCPU = new ComputerPlayer(11,17);
		HashSet<Card> testPlayerSet = new HashSet<Card>();
		Card testPlayer = new Card("Cpt. Red",CardType.PLAYER);
		testPlayerSet.add(testPlayer);
		testCPU.setUnseenPlayerCards(testPlayerSet);
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend());
		assertEquals("Cpt. Red",testSuggestion.player);
	}
	
	@Test
	public void testRandomWeaponChoice() {
		ComputerPlayer testCPU = (ComputerPlayer) board.getPlayerList()[1];
		Solution testSuggestion = testCPU.makeSuggestion(board.getCellAt(testCPU.getRow(), testCPU.getColumn()), board.getLegend());
		Card testWeaponCard = new Card(testSuggestion.weapon, CardType.WEAPON);
		boolean hasWeaponCard = false;
		for(Card nextCard : testCPU.getUnseenWeaponCards()){
			if(nextCard.equals(testWeaponCard)){
				hasWeaponCard = true;
				break;
			}
		}
		assertTrue(hasWeaponCard);
	}
	
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
	
}
