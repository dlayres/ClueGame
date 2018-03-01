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
