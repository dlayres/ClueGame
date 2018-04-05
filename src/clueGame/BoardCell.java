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
	
	
	/**
	 * BoardCell test constructor
	 */
	public BoardCell(int row, int column) {
		this.row = row;
		this.column = column;
		this.initial = 'Z';
		this.doorDirection = DoorDirection.NONE;
	}
	/**
	 * If a cell is not a door cell
	 * @param row
	 * @param column
	 * @param initial
	 */
	public BoardCell (int row, int column, char initial) {
		this.row = row;
		this.column = column;
		this.initial = initial;
		this.doorDirection = DoorDirection.NONE;
	}
	
	/**
	 * If a cell is a door cell
	 * @param row
	 * @param column
	 * @param initial
	 * @param direction
	 */
	public BoardCell (int row, int column, char initial, char direction) {
		this.row = row;
		this.column = column;
		this.initial = initial;
		if (direction == 'R') {
			this.doorDirection = DoorDirection.RIGHT;
		}
		else if (direction == 'L') {
			this.doorDirection = DoorDirection.LEFT;
		}
		else if (direction == 'U') {
			this.doorDirection = DoorDirection.UP;
		}
		else if (direction == 'D') {
			this.doorDirection = DoorDirection.DOWN;
		}
		else {
			this.doorDirection = DoorDirection.NONE;
		}
	}
	
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	public boolean isWalkway() {
		return (initial == 'W');
	}
	
	public boolean isRoom() {
		return (initial != 'W' && doorDirection == DoorDirection.NONE && initial != 'X');
	}
	
	// returns whether or not this cell is a door
	public boolean isDoorway() {
		return (doorDirection != DoorDirection.NONE);
	}
	
	public char getInitial() {
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
