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
		// initialize grid
		grid = new BoardCell[4][4]; // nrows = 4, ncols = 4
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				grid[i][j] = new BoardCell(i,j);
			}
		}
		adjacencies = new HashMap<BoardCell, HashSet<BoardCell>>();
		calcAdjacencies();
	}
	
	// Calculates the adjacent cells for each cell in the board
	public void calcAdjacencies(){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				BoardCell currCell = new BoardCell(i, j);
				HashSet<BoardCell> cellAdjList = new HashSet<BoardCell>();
			}
		}
		
		
		
		
		/*
		// The 4 corners
		HashSet<BoardCell> nextSet00 = new HashSet<BoardCell>();
		nextSet00.add(new BoardCell(0,1));
		nextSet00.add(new BoardCell(1,0));
		adjacencies.put(new BoardCell(0,0), nextSet00);

		HashSet<BoardCell> nextSet03 = new HashSet<BoardCell>();
		nextSet03.add(new BoardCell(0,2));
		nextSet03.add(new BoardCell(1,3));
		adjacencies.put(new BoardCell(0,3), nextSet03);
		
		HashSet<BoardCell> nextSet30 = new HashSet<BoardCell>();
		nextSet30.add(new BoardCell(0,2));
		nextSet30.add(new BoardCell(1,3));
		adjacencies.put(new BoardCell(3,0), nextSet30);
		
		HashSet<BoardCell> nextSet33 = new HashSet<BoardCell>();
		nextSet33.add(new BoardCell(2,3));
		nextSet33.add(new BoardCell(3,2));
		adjacencies.put(new BoardCell(3,3), nextSet33);
		*/
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
