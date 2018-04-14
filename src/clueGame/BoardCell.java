/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C12A-1 Clue Paths
 */
package clueGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;

public class BoardCell {
	private int row;
	private int column;
	private char initial;
	private DoorDirection doorDirection;
	private boolean nameMe;

	public static final int CELL_WIDTH = 30;
	public static final int CELL_HEIGHT = 30;
	public static final int OFFSET = 3 * CELL_WIDTH;

	/**
	 * BoardCell test constructor
	 */
	public BoardCell(int row, int column) {
		this.row = row;
		this.column = column;
		this.initial = 'Z';
		this.doorDirection = DoorDirection.NONE;
		this.nameMe = false;
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
		this.nameMe = false;
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
		this.nameMe = false;
	}
	
	/**
	 * Draws a single board cell
	 * @param g Graphics object to get access to drawing functions
	 * @param legend Legend map to display room names based on cell initial
	 * Cell Color Notes:
	 * Walkways - Brown
	 * Rooms - Gray
	 * Room Names - Purple
	 * Closet - Red-orange
	 * Doorways - Purple
	 */
	public void draw(Graphics g, HashMap<Character, String> legend) {
		Graphics2D g2D = (Graphics2D) g;
		g.setColor(new Color(122, 16, 229)); // set color to purple
		if (this.isRoom()) { // if the cell is a room
			if (this.nameMe == true) { // and it's where we should put the name
				g.drawString(legend.get(this.getInitial()), column * BoardCell.CELL_HEIGHT + BoardCell.OFFSET, row * BoardCell.CELL_WIDTH); // Draw the room name at that cell
			}
			return;
		}
		if (isWalkway()) { // if the cell is a walkway
			g.setColor(new Color(178,142,89)); // set color to brown
			g.fillRect(column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT); // Draw a brown cell
		}
		else if (isCloset()) { // if the cell is a closet
			g.setColor(new Color(226, 85, 29)); // set color to red-orange
			g.fillRect(column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH, CELL_WIDTH, CELL_HEIGHT); // Draw a red-orange cell
		}
		else if (isDoorway()) { // if the cell is a doorway
			g.setColor(new Color(122, 16, 229)); // set color to purple
			int lineOffset = 2;
			g2D.setStroke(new BasicStroke(5)); // Draw a thick purple line
			switch(doorDirection){ // Draw a line above, below, to the left, or to the right of the cell depending on the door direction
			case UP:
				g.drawLine(column*CELL_HEIGHT+OFFSET + lineOffset, row*CELL_WIDTH, column*CELL_HEIGHT+OFFSET + CELL_WIDTH - lineOffset, row*CELL_WIDTH);
				break;
			case DOWN:
				g.drawLine(column*CELL_HEIGHT+OFFSET + lineOffset, row*CELL_WIDTH + CELL_HEIGHT, column*CELL_HEIGHT+OFFSET + CELL_WIDTH - lineOffset, row*CELL_WIDTH + CELL_HEIGHT);
				break;
			case LEFT:
				g.drawLine(column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH + lineOffset, column*CELL_HEIGHT+OFFSET, row*CELL_WIDTH + CELL_HEIGHT - lineOffset);
				break;
			case RIGHT:
				g.drawLine(column*CELL_HEIGHT+OFFSET + CELL_WIDTH, row*CELL_WIDTH + lineOffset, column*CELL_HEIGHT+OFFSET + CELL_WIDTH, row*CELL_WIDTH + CELL_HEIGHT - lineOffset);
				break;
			default:
				break;
			}
			return;
		}
		
		// Segment to outline closet and walkway cells with a thin black border
		g2D.setStroke(new BasicStroke(1));
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
	
	/**
	 * sets nameMe to true (indicates to display the room name at this cell)
	 */
	public void setNameMe() {
		this.nameMe = true;
	}

}
