package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class IntBoard {
	private HashSet<BoardCell> visited;
	private HashSet<BoardCell> targets;
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
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				BoardCell currCell = grid[i][j];
				HashSet<BoardCell> cellAdjList = new HashSet<BoardCell>();
				if(i == 0){
					cellAdjList.add(grid[i + 1][j]);
				} else if(i == 3){
					cellAdjList.add(grid[i - 1][j]);
				} else{
					cellAdjList.add(grid[i + 1][j]);
					cellAdjList.add(grid[i - 1][j]);
				}
				if(j == 0){
					cellAdjList.add(grid[i][j + 1]);
				} else if(j == 3){
					cellAdjList.add(grid[i][j - 1]);
				} else{
					cellAdjList.add(grid[i][j + 1]);
					cellAdjList.add(grid[i][j - 1]);
				}
				adjacencies.put(currCell, cellAdjList);
			}
		}
	}
		
		
	public void printAdjs() {
		for(HashMap.Entry<BoardCell, HashSet<BoardCell>> entry : adjacencies.entrySet()){
			System.out.println(entry.getKey() + "/ " + entry.getValue());
		}
	}
		

	
	// Gets the list of adjacent cells for a given cell
	public HashSet<BoardCell> getAdjList(BoardCell cell){
		return adjacencies.get(cell);
	}
	
	public void setVisitedToCurrentCell(BoardCell startCell) {
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		visited.add(startCell);
	}
	
	// Calculates the possible cells the player can move to (targets)
	public void calcTargets(BoardCell startCell, int pathLength){
		for (BoardCell nextCell : adjacencies.get(startCell)) {
			if (visited.contains(nextCell)) {
				continue;
			}
			else {
				visited.add(nextCell);
			}
			if (pathLength == 1) {
				targets.add(nextCell);
			}
			else {
				calcTargets(nextCell,pathLength-1);
			}
			visited.remove(nextCell);
		}
	}
	
	// Gets the possible cells the player can move to (targets)
	public HashSet<BoardCell> getTargets(){
		return targets;
	}
	
	// Gets a cell from a given row and column
	public BoardCell getCell(int row, int col){
		return grid[row][col];
	}
}
