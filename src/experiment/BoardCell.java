/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C12A-1 Clue Paths
 */
package experiment;

public class BoardCell {
	private int row;
	private int col;
	
	BoardCell (int row, int col) {
		this.row = row;
		this.col = col;
	}
	

	@Override
	public String toString() {
		return "row=" + row + ", col=" + col;
	}
	
	
}
