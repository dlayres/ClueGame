package clueGame;

/**
 * Card class is used to instantiate game cards
 */
public class Card {

	private String cardName;
	private CardType type; 
	
	// Default constructor (mainly for testing)
	public Card() {
		super();
	}
	
	/**
	 * Card constructor using the card name and the card type
	 * @param cardName - Name of the card
	 * @param type - The type for this card
	 */
	public Card(String cardName, CardType type) {
		super();
		this.cardName = cardName;
		this.type = type;
	}
	
	/**
	 * Return this card's name
	 * @return cardName
	 */
	public String getCardName() {
		return cardName;
	}

	/**
	 * Return this card's type
	 * @return type
	 */
	public CardType getType() {
		return type;
	}

	/**
	 * equals(Card c) : Determines if two cards are the same
	 * @param c - The card with which the current card object is being compared to
	 * Returns true if both cardNames and cardTypes are the same, false otherwise
	 */
	public boolean equals(Card c){
		return (cardName.equals(c.getCardName()) && type.equals(c.getType())); 	
	}
	
}
