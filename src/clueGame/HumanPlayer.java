package clueGame;

import java.util.HashSet;

/**
 * HumanPlayer class is an extension of the Player class and is used for human players
 */
public class HumanPlayer extends Player{
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
			if(nextCard.getCardName() == suggestion.player || nextCard.getCardName() == suggestion.weapon || nextCard.getCardName() == suggestion.room) { // if we find a matching card
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
				return cardToDisprove;
			}
		}
		else { // TODO: Currently have the card selection be random if more than two cards. This will be changed to allow player choice once more of code has been implemented
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
		}
		
		return cardToDisprove;
	}
	
	public void updateLocation(BoardCell moveTo) {
		this.row = moveTo.getRow();
		this.column = moveTo.getColumn();
	}
}
