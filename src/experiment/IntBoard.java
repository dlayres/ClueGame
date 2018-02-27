package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class IntBoard {
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	private HashMap<BoardCell, HashSet<BoardCell>> adjacencies;
	
	private BoardCell[][] grid;

	// Default constructor
	public IntBoard() {
		super();
		calcAdjacencies();
	}
	
	// Calculates the adjacent cells for each cell in the board
	public void calcAdjacencies(){
		return;
	}
	
	// Gets the list of adjacent cells for a given cell
	public HashSet<BoardCell> getAdjList(BoardCell cell){
		return adjacencies.get(cell);
	}
	
	// Calculates the possible cells the player can move to (targets)
	public void calcTargets(BoardCell startCell, int pathLength){
		return;
	}
	
	// Gets the possible cells the player can move to (targets)
	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	// Gets a cell from a given row and column
	public BoardCell getCell(int row, int col){
		return grid[row][col];
	}
}
