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
	private String boardConfigFile;
	private String roomConfigFile;
	
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

	public Map<BoardCell, Set<BoardCell>> getAdjMatrix() {
		return adjMatrix;
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
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
	
	public void calcAdjacencies(){
		return;
	}
	
	public void calcTargets(BoardCell cell, int pathLength){
		return;
	}
	
	public void setConfigFiles(String boardConfigFile, String roomConfigFile) {
		this.boardConfigFile = boardConfigFile;
		this.roomConfigFile = roomConfigFile;
	}
	//-----------------------------------------------------------------------------
}
