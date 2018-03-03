package clueGame;

import java.util.Map;
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
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public BoardCell getCellAt(int row, int col) {
		return board[row][col];
	}
	
	/**
	 * @return the legend
	 */
	public Map<Character, String> getLegend() {
		return legend;
	}
	
	/**
	 * @return the adjMatrix
	 */
	public Map<BoardCell, Set<BoardCell>> getAdjMatrix() {
		return adjMatrix;
	}
	
	/**
	 * @return the targets
	 */
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	
	public void initialize(){
		return;
	}
	
	public void loadRoomConfig(){
		return;
	}
	
	public void loadBoardConfig(){
		return;
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
}
