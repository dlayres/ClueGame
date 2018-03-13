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
	private HashSet<BoardCell> targets;
	private HashSet<BoardCell> visited; // set of visited points for cell
	private String boardConfigFile;
	private String roomConfigFile;
	
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
	public HashSet<BoardCell> getVisited() {
		return visited;
	}
	
	public Set<BoardCell> getAdjList(int row, int col) {
		return adjMatrix.get(board[row][col]);
	}
	
	public Set<BoardCell> getTargets() {
		firstIteration = true;
		HashSet<BoardCell> oldTargets = new HashSet<BoardCell>();
		oldTargets = targets;
		targets = new HashSet<BoardCell>();
		visited.remove(board[tempRow][tempCol]);
		return oldTargets;
	}
	//------------------------------------------------------------------------------
	
	//------------------Member functions, currently stubs---------------------------
	
	
	/**
	 * Initialize was made first, so code has not been properly encapsulated yet.
	 * But it works.
	 */
	public void initialize() {
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		targets = new HashSet<BoardCell>();
		
		//------------------- loadRoomConfig(): ---------------------
		legend = new HashMap <Character, String>(); // initialize memory
		String next = "";
		try {
			FileReader roomReader = new FileReader(roomConfigFile); // make reading file the legend file
			Scanner in = new Scanner(roomReader);
			while(in.hasNextLine()) {
				next = in.nextLine();
				String [] splitString = (next.split(", ",0)); // split string after comma
				legend.put(splitString[0].charAt(0), splitString[1]); // put initial and room name into legend
			}
			in.close();
			try {
				roomReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find Room Configuration File");
		}
		//-----------------------------------------------------------
		
		//-------------------------loadBoardConfig(): ----------------------
		try {
			FileReader boardReader = new FileReader(boardConfigFile); // set reading file to the board configuration
			Scanner in = new Scanner(boardReader);
			next = in.nextLine();
			String [] splitString = next.split(",", -2); // split string at each comma
			this.numColumns = splitString.length; // each row should have same number of columns
			this.numRows = 0;
			this.numRows++; // increment number of rows
			while (in.hasNextLine()) { // with each new line iteration, increase number of rows by one
				next = in.nextLine();
				this.numRows++;
			}
			try {
				boardReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find Board Configuration File");
		}
		

		board = new BoardCell[this.numRows][this.numColumns]; // initialize the board grid
		int currentRow = 0;
		try {
			FileReader boardReader = new FileReader(boardConfigFile);
			Scanner in = new Scanner(boardReader);
			while (in.hasNextLine()) {
				next = in.nextLine();
				String [] splitString = next.split(",", -2); // split string at each comma
				for (int i = 0; i < splitString.length; ++i) { // for each entry in the row
					if (splitString[i].length() == 2) { // if the entry is formatted as "-N"
						if (splitString[i].charAt(1) == 'N') { // only put the first character
							board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0)); 
						}
						else { // this space is a door with a direction
							board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0), splitString[i].charAt(1));
						}
					}
					else { // this is just a regular space
						board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0));
					}
				}
				currentRow++; // move to next row
			}
			try {
				boardReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find Board Configuration File");
		}
		//------------------------------------------------------------------------
		calcAdjList();
		visited = new HashSet<BoardCell>();
	}
	
	/**
	 * See initialize() for function detail
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
	 * See initialize() for function information
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
	}
	
	public void calcAdjList(){
		for(int i = 0; i < getNumRows(); i++){
			for(int j = 0; j < getNumColumns(); j++){
				BoardCell currCell = board[i][j];
				HashSet<BoardCell> cellAdjList = new HashSet<BoardCell>(); // set of adjacencies for this cell
				if(currCell.getInitial() != 'W' && !currCell.isDoorway()){ // denotes a room space
					adjMatrix.put(currCell,  cellAdjList);
				}
				else if(currCell.isDoorway()){ // is a doorway
					switch(currCell.getDoorDirection()){
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
						if(bc.getInitial() == 'W'){
							cellAdjList.add(bc);
						}
						else if(bc.isDoorway()){
							boolean correctDirection = false;
							switch(bc.getDoorDirection()){
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
							if(correctDirection){
								cellAdjList.add(bc);
							}
						}
					}
				}
				adjMatrix.put(currCell, cellAdjList); // add to the adjacencies list
			}
		}
	}
	
	public void calcTargets(int row, int col, int pathLength) {
		if(firstIteration){
			tempRow = row;
			tempCol = col;
		}
		BoardCell currentCell = board[row][col];
		visited.add(currentCell);
		for (BoardCell nextCell : adjMatrix.get(currentCell)) { // for each adjacent cell to the current cell
			if (visited.contains(nextCell)) { // if we've been here already we just check the next possible cell
				continue;
			}
			else if (nextCell.isDoorway()){
				targets.add(nextCell);
				continue;
			}
			else if (nextCell.getInitial() != 'W') {
				continue;
			}
			else {
				visited.add(nextCell); // add to list if we haven't been here before
			}
			if (pathLength == 1) { // if at the end of our moving turn, then the cell should be a possible target
				targets.add(nextCell);
			}
			else { // otherwise move to next cell and continue target process
				firstIteration = false;
				calcTargets(nextCell.getRow(),nextCell.getColumn(),pathLength-1);
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
