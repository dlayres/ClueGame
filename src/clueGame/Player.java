package clueGame;


import java.awt.Color; // BE SURE TO USE THIS IMPORT
// not the one Eclipse suggests
import java.lang.reflect.Field;

public abstract class Player {
	private String playerName;
	private int row;
	private int column;
	private Color color;

	public Player(int row, int column, String playerName, String color) {
		this.row = row;
		this.column = column;
		this.playerName = playerName;
		this.color = convertColor(color);
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
}
