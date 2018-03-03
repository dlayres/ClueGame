/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C12A-1 Clue Paths
 */
package clueGame;

public class BoardCell {
	private int row;
	private int column;
	private char initial;
	private DoorDirection doorDirection;
	
	BoardCell (int row, int col, char init) {
		this.row = row;
		column = col;
		initial = init;
		doorDirection = DoorDirection.NONE;
	}
	
	BoardCell (int row, int col, char init, char direction) {
		this.row = row;
		column = col;
		initial = init;
		if (direction == 'R') {
			doorDirection = DoorDirection.RIGHT;
		}
		else if (direction == 'L') {
			doorDirection = DoorDirection.LEFT;
		}
		else if (direction == 'U') {
			doorDirection = DoorDirection.UP;
		}
		else if (direction == 'D') {
			doorDirection = DoorDirection.DOWN;
		}
		else {
			doorDirection = DoorDirection.NONE;
		}
	}
	
	public boolean isWalkway() {
		return true;
	}
	
	public boolean isRoom() {
		return true;
	}
	
	public boolean isDoorway() {
		return (doorDirection != DoorDirection.NONE);
	}
	
	public int getInitial() {
		return initial;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	@Override
	public String toString() {
		return "row=" + row + ", column=" + column;
	}
	
	
}
