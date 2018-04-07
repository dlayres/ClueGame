package clueGame;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ComputerPlayer class is an extension of the Player class and is used for non-human players
 */
public class ComputerPlayer extends Player {
	
	private boolean recentlyLeftARoom;
	private char roomLeft = 'Z';
	
	private Set<Card> unseenWeaponCards;
	private Set<Card> unseenPlayerCards;
	
	/**
	 * ComputerPlayer constructor
	 * @param row - Initial Row location of cpu on map
	 * @param column - Initial Column location of cpu on map 
	 * @param playerName - Name of this cpu player
	 * @param color - Color for this cpu player
	 */
	public ComputerPlayer(int row, int column, String playerName, String color) {
		super(row, column, playerName, color);
		unseenWeaponCards = new HashSet<Card>();
		unseenPlayerCards = new HashSet<Card>();
		recentlyLeftARoom = false;
	}
	
	/**
	 * ComputerPlayer Testing constructor : makes a test cpu player for junit tests
	 * @param row - Initial row location of test cpu
	 * @param column - Initial column location of test cpu
	 */
	public ComputerPlayer(int row, int column) {
		super(row,column);
		unseenWeaponCards = new HashSet<Card>();
		unseenPlayerCards = new HashSet<Card>();
		recentlyLeftARoom = false;
	}

	public BoardCell selectTarget(Set<BoardCell> targets) {
		
		if (recentlyLeftARoom == false) {
			for (BoardCell nextTarget : targets) {
				if (nextTarget.isDoorway()) {
					setRecentlyLeftARoom(true,nextTarget.getInitial());
					return nextTarget;
				}
			}
			int rand = (int)Math.floor((Math.random() * targets.size()));
			int iter = 0;
			for (BoardCell nextTarget : targets) {
				if (iter == rand) {
					return nextTarget;
				}
				iter++;
			}
		}
		else {
			for (BoardCell nextTarget : targets) {
				if (nextTarget.isDoorway() && (nextTarget.getInitial() != roomLeft)) {
					setRecentlyLeftARoom(true,nextTarget.getInitial());
					return nextTarget;
				}
			}
			int rand = (int)Math.floor((Math.random() * targets.size()));
			int iter = 0;
			for (BoardCell nextTarget : targets) {
				if (iter == rand) {
					if (nextTarget.isDoorway()) {
						setRecentlyLeftARoom(true,nextTarget.getInitial());
					}
					else {
						setRecentlyLeftARoom(false,'Z');
					}
					return nextTarget;
				}
				iter++;
			}
		}
		
		return new BoardCell(0,0);
	}
	
	public void updateLocation(BoardCell moveTo) {
		this.row = moveTo.getRow();
		this.column = moveTo.getColumn();
	}
	
	
	public Solution makeSuggestion(BoardCell locationCell, Map<Character, String> legend) {
		Solution suggestion = new Solution();
		int randWeapon = (int)Math.floor((Math.random() * unseenWeaponCards.size()));
		int randPlayer = (int)Math.floor((Math.random() * unseenPlayerCards.size()));

		int i = 0;
		for(Card next : unseenWeaponCards) { // Iterates through weapon card set until i equals the randomly chosen number
			if (i == randWeapon) {
				suggestion.weapon = next.getCardName();
			}
			i++;
		}
		i = 0;
		for(Card next : unseenPlayerCards) { // Iterates through weapon card set until i equals the randomly chosen number
			if (i == randPlayer) {
				suggestion.player = next.getCardName();
			}
			i++;
		}
		suggestion.room = legend.get(locationCell.getInitial());
		
		return suggestion;
	}
	
	public Card disproveSuggestion(Solution suggestion) {
		return new Card();
	}
	
	/**
	 * @return if cpu has recentlyLeftARoom
	 */
	public boolean getRecentlyLeftARoom() {
		return recentlyLeftARoom;
	}

	/**
	 * @param recentlyLeftARoom : True if the CPU has just left a room, False if otherwise
	 * @param roomLeftInitial : Initial of the room the CPU just left
	 */
	public void setRecentlyLeftARoom(boolean recentlyLeftARoom, char roomLeftInitial) {
		this.recentlyLeftARoom = recentlyLeftARoom;
		this.roomLeft = roomLeftInitial;
	}

	/**
	 * @return the unseenWeaponCards
	 */
	public Set<Card> getUnseenWeaponCards() {
		return unseenWeaponCards;
	}

	/**
	 * @param unseenWeaponCards the unseenWeaponCards to set
	 */
	public void setUnseenWeaponCards(Set<Card> unseenWeaponCards) {
		this.unseenWeaponCards = unseenWeaponCards;
	}

	/**
	 * @return the unseenPlayerCards
	 */
	public Set<Card> getUnseenPlayerCards() {
		return unseenPlayerCards;
	}

	/**
	 * @param unseenPlayerCards the unseenPlayerCards to set
	 */
	public void setUnseenPlayerCards(Set<Card> unseenPlayerCards) {
		this.unseenPlayerCards = unseenPlayerCards;
	}

	
}
