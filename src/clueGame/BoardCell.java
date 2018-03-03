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
	
	BoardCell (int row, int col, char init) {
		this.row = row;
		column = col;
		initial = init;
	}
	
	public boolean isWalkway() {
		return true;
	}
	
	public boolean isRoom() {
		return true;
	}
	
	public boolean isDoorway() {
		return true;
	}
	
	@Override
	public String toString() {
		return "row=" + row + ", column=" + column;
	}
	
	
}
