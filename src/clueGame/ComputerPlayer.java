package clueGame;

import java.util.Set;

/**
 * ComputerPlayer class is an extension of the Player class and is used for non-human players
 */
public class ComputerPlayer extends Player {
	
	private boolean recentlyLeftARoom;
	private char roomLeft = 'Z';
	
	/**
	 * ComputerPlayer constructor
	 * @param row - Initial Row location of cpu on map
	 * @param column - Initial Column location of cpu on map 
	 * @param playerName - Name of this cpu player
	 * @param color - Color for this cpu player
	 */
	public ComputerPlayer(int row, int column, String playerName, String color) {
		super(row, column, playerName, color);
		recentlyLeftARoom = false;
	}
	
	/**
	 * ComputerPlayer Testing constructor : makes a test cpu player for junit tests
	 * @param row - Initial row location of test cpu
	 * @param column - Initial column location of test cpu
	 */
	public ComputerPlayer(int row, int column) {
		super(row,column);
		recentlyLeftARoom = false;
	}

	public BoardCell selectTarget(Set<BoardCell> targets) {
		/*
		if (recentlyLeftARoom == false) {
			for (BoardCell nextTarget : targets) {
				if (nextTarget.isDoorway()) {
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
			int rand = (int)Math.floor((Math.random() * targets.size()));
			int iter = 0;
			for (BoardCell nextTarget : targets) {
				if (iter == rand) {
					return nextTarget;
				}
				iter++;
			}
		}
		*/
		return new BoardCell(0,0);
	}
	
	public void updateLocation(BoardCell moveTo) {
		this.row = moveTo.getRow();
		this.column = moveTo.getColumn();
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

	
}
