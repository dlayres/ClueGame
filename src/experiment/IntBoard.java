/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C12A-1 Clue Paths
 */
package experiment;

import java.util.HashMap;
import java.util.HashSet;

public class IntBoard {
	private HashSet<BoardCell> visited; // set of visited points for cell
	private HashSet<BoardCell> targets; // possible targets for that cell
	private HashMap<BoardCell, HashSet<BoardCell>> adjacencies; // adjacency list for each cell
	
	private BoardCell[][] grid; // grid board of points

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
				HashSet<BoardCell> cellAdjList = new HashSet<BoardCell>(); // set of adjacencies for this cell
				if(i == 0){ // if on the top row
					cellAdjList.add(grid[i + 1][j]);
				} else if(i == 3){ // if on the bottom row
					cellAdjList.add(grid[i - 1][j]);
				} else{
					cellAdjList.add(grid[i + 1][j]);
					cellAdjList.add(grid[i - 1][j]);
				}
				if(j == 0){ // if in the first column
					cellAdjList.add(grid[i][j + 1]);
				} else if(j == 3){ // if in the last column
					cellAdjList.add(grid[i][j - 1]);
				} else{
					cellAdjList.add(grid[i][j + 1]);
					cellAdjList.add(grid[i][j - 1]);
				}
				adjacencies.put(currCell, cellAdjList); // add to the adjacencies list
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
	
	// used to keep track of the visited cells when calculating targets
	public void setVisitedToCurrentCell(BoardCell startCell) {
		visited = new HashSet<BoardCell>(); // create new visited set
		targets = new HashSet<BoardCell>(); // create new target set
		visited.add(startCell); // add the starting cell (where we start moving from) to the visited set
	}
	
	// Calculates the possible cells the player can move to (targets)
	public void calcTargets(BoardCell startCell, int pathLength){
		for (BoardCell nextCell : adjacencies.get(startCell)) { // for each adjacent cell to the current cell
			if (visited.contains(nextCell)) { // if we've been here already we just check the next possible cell
				continue;
			}
			else {
				visited.add(nextCell); // add to list if we haven't been here before
			}
			if (pathLength == 1) { // if at the end of our moving turn, then the cell should be a possible target
				targets.add(nextCell);
			}
			else { // otherwise move to next cell and continue target process
				calcTargets(nextCell,pathLength-1);
			}
			visited.remove(nextCell); // once done processing cell remove it from the visited list
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
