package clueGame;

/**
 * ComputerPlayer class is an extension of the Player class and is used for non-human players
 */
public class ComputerPlayer extends Player {
	/**
	 * ComputerPlayer constructor
	 * @param row - Initial Row location of cpu on map
	 * @param column - Initial Row location of cpu on map 
	 * @param playerName - Name of this cpu player
	 * @param color - Color for this cpu player
	 */
	public ComputerPlayer(int row, int column, String playerName, String color) {
		super(row, column, playerName, color);
	}

}
