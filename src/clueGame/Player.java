package clueGame;


import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {
	private String playerName; // Name of player
	protected int row; // Location of player (row/column)
	protected int column;
	private Color color; // Color object designated to player
	protected Set<Card> myCards; // Player's current possessed cards
	
	// Default constructor for player (Mainly for testing)
	public Player() {
		this.row = 0;
		this.column = 0;
		this.playerName = "default";
		this.color = convertColor("white");
		this.myCards = new HashSet<Card>();
	}
	
	/**
	 * Player test constructor : used to make a test player
	 * @param row
	 * @param column
	 */
	public Player(int row, int column) {
		this.row = row;
		this.column = column;
		this.playerName = "Test Guy";
		this.color = convertColor("white");
		this.myCards = new HashSet<Card>();
	}

	/**
	 * Constructor for player that gives the player its location (row/column), name, and color
	 * @param row
	 * @param column
	 * @param playerName
	 * @param color
	 */
	public Player(int row, int column, String playerName, String color) {
		this.row = row;
		this.column = column;
		this.playerName = playerName;
		this.color = convertColor(color); // Takes a color as a string and converts to Color object
		this.myCards = new HashSet<Card>();
	}
	
	// Code taken from PDF (originally from StackExchange) to convert color string to a Color object
	public Color convertColor(String strColor) {
		Color color;
		try {
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());
			color = (Color)field.get(null);
		} catch (Exception e) {
			color = null; // Not defined
		}
		return color;
	}
	
	/**
	 * Gets the name of the player as a String
	 * @return playerName
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * Gets the current row of the player's location
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the current column of the player's location
	 * @return column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Gets the player's color as a Color object
	 * @return color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Gets the cards in possession of the player
	 * @return myCards
	 */
	public Set<Card> getMyCards(){
		return myCards;
	}
	
	/**
	 * Sets the cards in possession of the player to the set passed in
	 * @param cardList
	 */
	public void setMyCards(Set<Card> cardList){
		myCards = cardList;
	}
}
