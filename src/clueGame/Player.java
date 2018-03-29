package clueGame;


import java.awt.Color; // BE SURE TO USE THIS IMPORT
// not the one Eclipse suggests
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {
	private String playerName;
	private int row;
	private int column;
	private Color color;
	private Set<Card> myCards;
	
	public Player() {
		this.row = 0;
		this.column = 0;
		this.playerName = "default";
		this.color = convertColor("white");
		this.myCards = new HashSet<Card>();
	}

	public Player(int row, int column, String playerName, String color) {
		this.row = row;
		this.column = column;
		this.playerName = playerName;
		this.color = convertColor(color);
		this.myCards = new HashSet<Card>();
	}
	
	// Be sure to trim the color, we don't want spaces around the name
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

	public String getPlayerName() {
		return playerName;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Color getColor() {
		return color;
	}
	
	public Set<Card> getMyCards(){
		return myCards;
	}
	
	public void setMyCards(Set<Card> cardList){
		myCards = cardList;
	}
}
