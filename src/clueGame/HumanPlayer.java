package clueGame;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * HumanPlayer class is an extension of the Player class and is used for human players
 */
public class HumanPlayer extends Player{
	private Card chosenDisprovingCard;

	// Default constructor (used for testing)
	public HumanPlayer() {
		super();
	}

	public HumanPlayer(int row, int column) {
		super(row,column);
	}

	/**
	 * HumanPlayer constructor
	 * @param row - Initial row location for this player
	 * @param column - Initial column location for this player
	 * @param playerName - This player's name
	 * @param color - This player's color
	 */
	public HumanPlayer(int row, int column, String playerName, String color) {
		super(row, column, playerName, color);
	}

	/**
	 * makeAccusation() : make accusation given input from the user
	 * @param player
	 * @param weapon
	 * @param room
	 * @return
	 */
	public Solution makeAccusation(String player, String weapon, String room) {
		Solution proposedSolution = new Solution();
		proposedSolution.player = player;
		proposedSolution.weapon = weapon;
		proposedSolution.room = room;
		return proposedSolution;
	}

	/**
	 * disproveSuggestion() : Used by human to disprove a suggestion
	 */
	@Override
	public Card disproveSuggestion(Solution suggestion) {
		Card cardToDisprove = null; // set disproving card to null initially
		HashSet<Card> matchingCards = new HashSet<Card>();
		for(Card nextCard : myCards){ // for each of the cpu's cards
			if(nextCard.getCardName().equals(suggestion.player) || nextCard.getCardName().equals(suggestion.weapon) || nextCard.getCardName().equals(suggestion.room)) { // if we find a matching card
				matchingCards.add(nextCard); // add to list of matching cards
			}
		}
		// if player has no matching cards
		if (matchingCards.size() == 0) {
			return cardToDisprove; // null
		}
		// if player has a single matching card, return the only item in the set
		else if (matchingCards.size() == 1) {
			
			
			for(Card pickCard : matchingCards) {
				cardToDisprove = pickCard;
				JOptionPane.showMessageDialog(Board.getInstance(), "You disproved the latest suggestion using your " + cardToDisprove.getCardName() + " card!"); // Gives error message
				return cardToDisprove;
			}
		}
		else { // The human player has more than one card they can show, allow them to choose
			chosenDisprovingCard = null; // Set the card they choose to null first
			CardSelectionDialog cardOptionsGUI = new CardSelectionDialog(matchingCards); // Dialog box allowing them to choose the card
			cardOptionsGUI.setVisible(true);
			if(chosenDisprovingCard != null){ // If the card isn't null, they picked one, so return that card
				return chosenDisprovingCard;
			}
			else{ // If the card is still null, they didn't pick one (clicked the X), so pick a random card from the options
				int randMatchingCard = (int)Math.floor((Math.random() * matchingCards.size()));
				int i = 0;
				for(Card next : matchingCards) { // Iterates through weapon card set until i equals the randomly chosen number
					if (i == randMatchingCard) {
						cardToDisprove = next; // set disproving card
						break;
					}
					i++;
				}
			}
		}

		return cardToDisprove;
	}

	/**
	 * updateLocation() : changes the human player's row and column location
	 * @param moveTo
	 */
	public void updateLocation(BoardCell moveTo) {
		this.row = moveTo.getRow();
		this.column = moveTo.getColumn();
		if (this.currentlyInRoom == true) { // if we're currently in a room
			this.roomLeft = moveTo.getInitial(); // set our player's board initial to the room initial, preventing us from exiting and entering room on the same turn
		}
		else { // otherwise not in a room
			this.roomLeft = 'Z'; // set our player's board initial to dummy (non-board) value, allowing us access to all cells again
		}
	}

	/**
	 * checkTargets() : Removes doorway targets that are the same as the room we are already in
	 * @param targets
	 * @return
	 */
	public Set<BoardCell> checkTargets(Set<BoardCell> targets) {
		Set<BoardCell> invalidTargets = new HashSet<BoardCell>();
		for (BoardCell nextTarget : targets) {
			if (nextTarget.getInitial() == this.roomLeft) { // if the target's board initial is the same as the room we are in/need to leave
				invalidTargets.add(nextTarget); // this doorway should not be a target
			}
		}
		targets.removeAll(invalidTargets); // remove any invalid doorways
		return targets;
	}
	
	public void setDisprovingCard(Card cardToDisprove){
		chosenDisprovingCard = cardToDisprove;
	}

}
