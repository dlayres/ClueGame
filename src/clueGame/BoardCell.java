/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C12A-1 Clue Paths
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;

public class BoardCell {
	private int row;
	private int column;
	private char initial;
	private DoorDirection doorDirection;
	
	private static final int CELL_WIDTH = 30;
	private static final int CELL_HEIGHT = 30;
	private static final int OFFSET = 90;
	
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
	
	public void draw(Graphics g) {
		if (this.isRoom()) {
			g.setColor(Color.lightGray);
			g.fillRect(column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
			return;
		}
		if (isWalkway()) {
			g.setColor(Color.YELLOW);
			g.fillRect(column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
		}
		else if (isCloset()) {
			g.setColor(Color.RED);
			g.fillRect(column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
		}
		else if (isDoorway()) {
			g.setColor(Color.CYAN);
			g.fillRect(column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
		}
		g.setColor(Color.BLACK);
		g.drawRect(column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT);
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

	public boolean isCloset() {
		return (initial == 'X');
	}
	
}
