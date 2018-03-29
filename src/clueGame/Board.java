/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C13A-1 Clue Paths
 */

package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class Board {
	private int numRows;
	private int numColumns;
	public static final int MAX_BOARD_SIZE = 50;

	private BoardCell[][] board;
	private Map<Character, String> legend;
	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited; // set of visited points for cell
	private String boardConfigFile;
	private String roomConfigFile;

	/**
	 * firstIteration, tempRow, and tempCol are used for managing the "visited" set for a cell
	 * Used in calcTargets and getTargets
	 */
	private boolean firstIteration;
	private int tempRow;
	private int tempCol;

	// variable used for singleton pattern
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}

	//----------------Getters for many of the instance variables---------------------
	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCellAt(int row, int col) {
		return board[row][col];
	}

	public Map<Character, String> getLegend() {
		return legend;
	}

	/**
	 * @return the visited
	 */
	public Set<BoardCell> getVisited() {
		return visited;
	}

	public Set<BoardCell> getAdjList(int row, int col) {
		return adjMatrix.get(board[row][col]);
	}

	/**
	 * getTargets
	 * Only called after the targets have been fully calculated for the initial cell
	 * This function and calcTargets are dependent upon one another
	 * @return The targets for this board cell
	 */
	public Set<BoardCell> getTargets() {
		firstIteration = true; // variable used for calcTargets function
		Set<BoardCell> oldTargets = new HashSet<BoardCell>(); // allocate new memory space
		oldTargets = targets; // put targets for this cell into the newly allocated memory
		targets = new HashSet<BoardCell>(); // reset the Set of targets to be used for the next cell
		visited.remove(board[tempRow][tempCol]); // Removes the initial cell as a possible target (essentially clears the visited set to be used for a new initial cell) 
		return oldTargets; // oldTargets = targets (before deletion), and so contains all the possible targets from calcTargets
	}
	//------------------------------------------------------------------------------

	//------------------------------Member functions--------------------------------


	/**
	 * 
	 */
	public void initialize() {
		try {
			loadRoomConfig();
			loadBoardConfig();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @throws FileNotFoundException
	 * @throws BadConfigFormatException
	 */
	public void loadRoomConfig() throws FileNotFoundException, BadConfigFormatException {
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		targets = new HashSet<BoardCell>();

		legend = new HashMap <Character, String>();
		String next = "";
		FileReader roomReader = new FileReader(roomConfigFile);
		Scanner in = new Scanner(roomReader);
		while(in.hasNextLine()) {
			next = in.nextLine();
			String [] splitString = (next.split(", ",0));
			legend.put(splitString[0].charAt(0), splitString[1]);
			if (splitString[2].equals("Card") == false && splitString[2].equals("Other") == false) { // if there is an invalid legend description
				throw new BadConfigFormatException("Bad Room Legend File Configuration: Description is not Card nor Other");
			}
		}
		in.close();
	}
	
	
	/**
	 * @throws FileNotFoundException
	 * @throws BadConfigFormatException
	 */
	public void loadBoardConfig() throws FileNotFoundException, BadConfigFormatException {
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		targets = new HashSet<BoardCell>();

		String next = "";
		FileReader boardReader = new FileReader(boardConfigFile);
		Scanner in = new Scanner(boardReader);
		next = in.nextLine();
		String [] splitString = next.split(",", -2);
		this.numColumns = splitString.length;
		this.numRows = 0;
		this.numRows++;
		while (in.hasNextLine()) {
			next = in.nextLine();
			this.numRows++;
		}

		board = new BoardCell[this.numRows][this.numColumns];
		int currentRow = 0;
		FileReader boardReader_copy = new FileReader(boardConfigFile);
		Scanner in2 = new Scanner(boardReader_copy);
		while (in2.hasNextLine()) {
			next = in2.nextLine();
			splitString = next.split(",", -2);
			if (splitString.length != numColumns) { // if there is a row with an incorrect number of columns
				throw new BadConfigFormatException("Board Layout Configuration Error: Row with invalid Columns");
			}
			for (int i = 0; i < splitString.length; ++i) {
				if (legend.get(splitString[i].charAt(0)) == null) {
					throw new BadConfigFormatException("Board Layout Configuration Error: Invalid Cell Initial (Letter) Entry");
				}
				if (splitString[i].length() == 2) {
					if (splitString[i].charAt(1) == 'N') {
						board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0));
					}
					else {
						board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0), splitString[i].charAt(1));
					}
				}
				else {
					board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0));
				}
			}
			currentRow++;
		}
		
		calcAdjList();
		visited = new HashSet<BoardCell>();
	}

	
	

	/**
	 * Calc adjacency list for each cell
	 */
	public void calcAdjList(){
		for(int i = 0; i < getNumRows(); i++){
			for(int j = 0; j < getNumColumns(); j++){
				BoardCell currCell = board[i][j];
				HashSet<BoardCell> cellAdjList = new HashSet<BoardCell>(); // set of adjacencies for this cell
				if(currCell.getInitial() != 'W' && !currCell.isDoorway()){ // denotes a room space
					adjMatrix.put(currCell,  cellAdjList);
				}
				else if(currCell.isDoorway()){ // is a doorway
					switch(currCell.getDoorDirection()){ // for the doorway, add the correct adjacent cell depending on door direction
					case LEFT:
						cellAdjList.add(board[i][j - 1]);
						break;
					case RIGHT:
						cellAdjList.add(board[i][j + 1]);
						break;
					case UP:
						cellAdjList.add(board[i - 1][j]);
						break;
					case DOWN:
						cellAdjList.add(board[i + 1][j]);
						break;
					default:
						break;
					}
				}
				else{ // is a walkway
					HashSet<BoardCell> candidates = new HashSet<BoardCell>();
					if(i == 0){ // if on the top row
						candidates.add(board[i + 1][j]);
					} else if(i == getNumRows() - 1){ // if on the bottom row
						candidates.add(board[i - 1][j]);
					} else{
						candidates.add(board[i + 1][j]);
						candidates.add(board[i - 1][j]);
					}
					if(j == 0){ // if in the first column
						candidates.add(board[i][j + 1]);
					} else if(j == getNumColumns() - 1){ // if in the last column
						candidates.add(board[i][j - 1]);
					} else{
						candidates.add(board[i][j + 1]);
						candidates.add(board[i][j - 1]);
					}
					for(BoardCell bc: candidates){
						if(bc.getInitial() == 'W'){ // if this candidate cell is a walkway, simply add to the adjacency set for this cell
							cellAdjList.add(bc);
						}
						else if(bc.isDoorway()){ // if this candidate cell is a doorway, need to check direction
							boolean correctDirection = false;
							switch(bc.getDoorDirection()){ // depending on door direction, check if the current cell is actually next to the door entrance
							case LEFT:
								if(bc.getColumn() == currCell.getColumn() + 1){
									correctDirection = true;
								}
								break;
							case RIGHT:
								if(bc.getColumn() == currCell.getColumn() - 1){
									correctDirection = true;
								}
								break;
							case UP:
								if(bc.getRow() == currCell.getRow() + 1){
									correctDirection = true;
								}
								break;
							case DOWN:
								if(bc.getRow() == currCell.getRow() - 1){
									correctDirection = true;
								}
								break;
							default:
								break;
							}
							if(correctDirection){ // if the current cell is able to enter the candidate doorway
								cellAdjList.add(bc);
							}
						}
					}
				}
				adjMatrix.put(currCell, cellAdjList); // add this cell's set of adjacencies to the adjacencies list matrix
			}
		}
	}

	/**
	 * calcTargets : calculates where a player can move from the current cell
	 * This function is dependent on getTargets
	 * @param row Row of the current cell
	 * @param col Column of the current cell
	 * @param pathLength How many spaces player can still move from current cell
	 */
	public void calcTargets(int row, int col, int pathLength) {
		if(firstIteration){ // determines if this iteration of calcTargets is the first iteration call (aka if this is where we start our turn from
			// keep track of this cell to remove from visited later on in the getTargets function
			tempRow = row; 
			tempCol = col;
		}
		BoardCell currentCell = board[row][col];
		visited.add(currentCell);
		for (BoardCell nextCell : adjMatrix.get(currentCell)) { // for each adjacent cell to the current cell
			if (visited.contains(nextCell)) { // if we've been here already we just check the next possible cell
				continue;
			}
			else if (nextCell.isDoorway()){ // we can automatically enter adjacent doorways
				targets.add(nextCell);
				continue;
			}
			else if (nextCell.getInitial() != 'W') { // cell is not a walkway nor a doorway
				continue;
			}
			else {
				visited.add(nextCell); // add to list if we haven't been here before
			}
			if (pathLength == 1) { // if at the end of our moving turn, then the cell should be a possible target
				targets.add(nextCell);
			}
			else { // otherwise move to next cell and continue target process
				firstIteration = false; // denotes that we are currently in a recursive function call of calcTargets
				calcTargets(nextCell.getRow(),nextCell.getColumn(),pathLength-1); // calculate the targets from the next cell
			}
			visited.remove(nextCell); // once done processing cell remove it from the visited list
		}
	}

	public void setConfigFiles(String boardConfigFile, String roomConfigFile) {
		this.boardConfigFile = boardConfigFile;
		this.roomConfigFile = roomConfigFile;
	}
	//-----------------------------------------------------------------------------
}
