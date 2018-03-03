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
	public void initialize() {
		legend = new HashMap <Character, String>();
		String next = "";
		try {
			FileReader roomReader = new FileReader(roomConfigFile);
			Scanner in = new Scanner(roomReader);
			while(in.hasNextLine()) {
				next = in.nextLine();
				String [] splitString = (next.split(", ",0));
				legend.put(splitString[0].charAt(0), splitString[1]);
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
		
		try {
			FileReader boardReader = new FileReader(boardConfigFile);
			Scanner in = new Scanner(boardReader);
			next = in.nextLine();
			String [] splitString = next.split(",", -2);
			this.numColumns = splitString.length;
			this.numRows++;
			while (in.hasNextLine()) {
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

		board = new BoardCell[this.numRows][this.numColumns];
		int currentRow = 0;
		try {
			FileReader boardReader = new FileReader(boardConfigFile);
			Scanner in = new Scanner(boardReader);
			while (in.hasNextLine()) {
				next = in.nextLine();
				String [] splitString = next.split(",", -2);
				for (int i = 0; i < splitString.length; ++i) {
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
			try {
				boardReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find Board Configuration File");
		}
	}
	
	public void loadRoomConfig() throws FileNotFoundException, BadConfigFormatException {
		legend = new HashMap <Character, String>();
		String next = "";
		FileReader roomReader = new FileReader(roomConfigFile);
		Scanner in = new Scanner(roomReader);
		while(in.hasNextLine()) {
			next = in.nextLine();
			String [] splitString = (next.split(", ",0));
			legend.put(splitString[0].charAt(0), splitString[1]);
			System.out.println(splitString[2]);
			if (splitString[2] != "Card" && splitString[2] != "Other") {
				System.out.println("Hello");
				throw new BadConfigFormatException("Bad Room Legend File Configuration");
			}
		}
		in.close();
	}
	
	public void loadBoardConfig() throws FileNotFoundException, BadConfigFormatException {
		String next = "";
		FileReader boardReader = new FileReader(boardConfigFile);
		Scanner in = new Scanner(boardReader);
		next = in.nextLine();
		String [] splitString = next.split(",", -2);
		this.numColumns = splitString.length;
		this.numRows++;
		while (in.hasNextLine()) {
			next = in.nextLine();
			this.numRows++;
		}
		
		board = new BoardCell[this.numRows][this.numColumns];
		int currentRow = 0;
		FileReader boardReader_copy = new FileReader(boardConfigFile);
		Scanner in2 = new Scanner(boardReader_copy);
		while (in.hasNextLine()) {
			next = in.nextLine();
			splitString = next.split(",", -2);
			for (int i = 0; i < splitString.length; ++i) {
				if (legend.containsKey(splitString[i].charAt(0)) == false) {
					System.out.println("Goodbye");
					throw new BadConfigFormatException("Board Layout Configuration Error");
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
