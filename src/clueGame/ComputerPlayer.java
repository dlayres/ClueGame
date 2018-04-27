package clueGame;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ComputerPlayer class is an extension of the Player class and is used for non-human players
 */
public class ComputerPlayer extends Player {
	private Set<Card> unseenWeaponCards;
	private Set<Card> unseenPlayerCards;

	private boolean shouldMakeAccusation = false; // flag that says whether or not cpu should make accusation next turn
	private boolean recentlyWrong = false; // flag that says whether an accusation the cpu just made is true or false
	private Solution rightAccusation = new Solution(); // used to store cpu's suggestion that none of the other players could disprove

	/**
	 * @return the shouldMakeAccusation
	 */
	public boolean getShouldMakeAccusation() {
		return shouldMakeAccusation;
	}


	/**
	 * @param shouldMakeAccusation the shouldMakeAccusation to set
	 */
	public void setShouldMakeAccusation(boolean shouldMakeAccusation) {
		this.shouldMakeAccusation = shouldMakeAccusation;
	}


	/**
	 * @return the rightAccusation
	 */
	public Solution getRightAccusation() {
		return rightAccusation;
	}


	/**
	 * @param rightAccusation the rightAccusation to set
	 */
	public void setRightAccusation(Solution rightAccusation) {
		this.rightAccusation = rightAccusation;
	}


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
		currentlyInRoom = false;
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
		currentlyInRoom = false;
	}

	/**
	 * selectTarget(Set<BoardCell>) : Used to select a target for the CPU to move to given a set of targets  
	 * @param targets
	 * @return Target BoardCell to move to
	 */
	public BoardCell selectTarget(Set<BoardCell> targets) {
		// if we are currently able to enter any room
		if (recentlyLeftARoom == false) {
			for (BoardCell nextTarget : targets) { // go through each possible target
				if (nextTarget.isDoorway()) { // if this target is a doorway then pick this target
					currentlyInRoom = true;
					setRecentlyLeftARoom(true,nextTarget.getInitial()); // set recentlyLeftARoom to true since we have just entered a room (used for next move), along with the room's initial
					return nextTarget; // go to the proposed room
				}
			}
			// if we didn't find a door way, go to a random target on the board
			currentlyInRoom = false;
			int rand = (int)Math.floor((Math.random() * targets.size())); // index of random spot in the target set
			int iter = 0;
			for (BoardCell nextTarget : targets) { // go through the targets
				if (iter == rand) { // if the iteration for this target is the correct random index, choose this target
					return nextTarget;
				}
				iter++;
			}
		}
		else { // if we have recently left a room
			for (BoardCell nextTarget : targets) { // go through each possible target
				if (nextTarget.isDoorway() && (nextTarget.getInitial() != roomLeft)) { // if the proposed target is a doorway/room and we did not just leave this room
					currentlyInRoom = true;
					setRecentlyLeftARoom(true,nextTarget.getInitial()); // set recentlyLeftARoom to true, along with the entered room's initial
					return nextTarget; // go to the proposed room
				}
			}
			// if we didn't find any different doorways/rooms to enter from our last move, find a random target to move to (may include the previous room)
			int rand = (int)Math.floor((Math.random() * targets.size()));
			int iter = 0;
			for (BoardCell nextTarget : targets) {
				if (iter == rand) {
					if (nextTarget.isDoorway()) {
						currentlyInRoom = true;
						setRecentlyLeftARoom(true,nextTarget.getInitial());
					}
					else {
						if(!currentlyInRoom){
							setRecentlyLeftARoom(false,'Z'); // this denotes a walkway: set recently left a room to false and put dummy char initial which doesn't represent any rooms
						}
					}
					currentlyInRoom = false;
					return nextTarget;
				}
				iter++;
			}
		}

		return new BoardCell(0,0);
	}

	/**
	 * updateLocation() : changes the CPU's row and column location
	 * @param moveTo
	 */
	public void updateLocation(BoardCell moveTo) {
		this.row = moveTo.getRow();
		this.column = moveTo.getColumn();
	}


	/**
	 * makeSuggestion() : Given the current location BoardCell for the CPU, makes a suggestion based off of unknown weapon and player cards 
	 * @param locationCell
	 * @param legend
	 * @return
	 */
	public Solution makeSuggestion(BoardCell locationCell, Map<Character, String> legend) {
		Solution suggestion = new Solution();
		int randWeapon = (int)Math.floor((Math.random() * unseenWeaponCards.size()));
		int randPlayer = (int)Math.floor((Math.random() * unseenPlayerCards.size()));

		int i = 0;
		for(Card next : unseenWeaponCards) { // Iterates through weapon card set until i equals the randomly chosen number (can equal 0, representing only 1 card in set)
			if (i == randWeapon) {
				suggestion.weapon = next.getCardName(); // make the cpu's weapon suggestion based on the randomly chosen weapon card
			}
			i++;
		}
		i = 0;
		for(Card next : unseenPlayerCards) { // Iterates through weapon card set until i equals the randomly chosen number (can equal 0, representing only 1 card in set)
			if (i == randPlayer) {
				suggestion.player = next.getCardName(); // make the cpu's player suggestion based on the randomly chosen player card
			}
			i++;
		}
		suggestion.room = legend.get(locationCell.getInitial()); // make the cpu's room suggestion based on which cell/room the cpu is currently located at (supplies char initial as input to map)
		
		return suggestion;
	}

	/**
	 * disproveSuggestion() : Given a suggestion from another player, cpu attempts to disprove the suggestion using one of its cards
	 * @param suggestion
	 * @return
	 */
	@Override
	public Card disproveSuggestion(Solution suggestion) {
		Card cardToDisprove = null; // set disproving card to null initially
		HashSet<Card> matchingCards = new HashSet<Card>();
		for(Card nextCard : myCards){ // for each of the cpu's cards
			if(nextCard.getCardName() == suggestion.player || nextCard.getCardName() == suggestion.weapon || nextCard.getCardName() == suggestion.room) { // if we find a matching card
				matchingCards.add(nextCard); // add to list of matching cards
			}
		}

		// pick a random card to choose from the matching cards
		int randMatchingCard = (int)Math.floor((Math.random() * matchingCards.size()));
		int i = 0;
		for(Card next : matchingCards) { // Iterates through weapon card set until i equals the randomly chosen number
			if (i == randMatchingCard) {
				cardToDisprove = next; // set disproving card
				break;
			}
			i++;
		}
		return cardToDisprove; // returns the disproving card (may still be null if no matches found)
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


	public boolean getRecentlyWrong() {
		return recentlyWrong;
	}
	
	public void setRecentlyWrong(boolean result) {
		recentlyWrong = result;
	}

	/**
	 * updateCards() : If a card has been shown to disprove the computer's latest suggestion,
	 * need to remove this card from unseen list so cpu doesn't suggest it again
	 * @param latestDisprovingCard - The card used that disproved the suggestion
	 */
	public void updateCards(Card latestDisprovingCard) {
		switch(latestDisprovingCard.getType()){
		case WEAPON:
			unseenWeaponCards.remove(latestDisprovingCard);
			break;
		case PLAYER:
			unseenPlayerCards.remove(latestDisprovingCard);
			break;
		default:
			break;
		}
		return;
	}

}
