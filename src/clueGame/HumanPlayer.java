package clueGame;
/**
 * HumanPlayer class is an extension of the Player class and is used for human players
 */
public class HumanPlayer extends Player{
	// Default constructor (used for testing)
	public HumanPlayer() {
		super();
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
	
}
