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
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class Board {
	private int numRows;
	private int numColumns;
	public static final int MAX_BOARD_SIZE = 50;
	public static final int NUM_PLAYERS = 6;

	private BoardCell[][] board; // The grid of the board
	private Map<Character, String> legend; // Used for determining room identity
	private Map<BoardCell, Set<BoardCell>> adjMatrix; // Map of cell adjacencies
	private Set<BoardCell> targets; // Possible targets to move to for a given cell
	private Set<BoardCell> visited; // Set of visited cells (used for calculating targets)
	private String boardConfigFile; // Board Configuration File Name
	private String roomConfigFile; // Room Configuration File Name
	private String playerConfigFile;
	private String weaponConfigFile;
	private Player[] playerList;
	private Set<Card> cards;
	private Solution answer;

	// variable used for singleton pattern
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}

	//----------------Getters for many of the instance variables---------------------
	/**
	 * Returns the number of rows for the board
	 * @return numRows
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * Returns the number of columns for the board
	 * @return numColumns
	 */
	public int getNumColumns() {
		return numColumns;
	}

	/**
	 * Used to return a specific board cell
	 * @param row
	 * @param col
	 * @return board[row][col]
	 */
	public BoardCell getCellAt(int row, int col) {
		return board[row][col];
	}

	/**
	 * Used to get the character -> string legend
	 * @return legend
	 */
	public Map<Character, String> getLegend() {
		return legend;
	}

	/**
	 * Returns the visited cell set
	 * @return visited
	 */
	public Set<BoardCell> getVisited() {
		return visited;
	}

	/**
	 * Returns the adjacency list for a given cell
	 * @param row
	 * @param col
	 * @return adjMatrix.get(board[row][col])
	 */
	public Set<BoardCell> getAdjList(int row, int col) {
		return adjMatrix.get(board[row][col]);
	}

	/**
	 * getTargets
	 * @return The targets for a board cell after calculating them with calcTargets()
	 */
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public Player[] getPlayerList(){
		return playerList;
	}

	public Set<Card> getCards(){
		return cards;
	}

	//------------------------------------------------------------------------------

	//------------------------------Member functions--------------------------------


	/**
	 * initialize() is used to call both the loadRoomConfig() and loadBoardConfig() functions
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
	 * loadRoomConfig() is used to construct the room/board legend
	 * @throws FileNotFoundException
	 * @throws BadConfigFormatException
	 */
	public void loadRoomConfig() throws FileNotFoundException, BadConfigFormatException {
		legend = new HashMap<Character, String>();
		String next = "";
		FileReader roomReader = new FileReader(roomConfigFile);
		Scanner in = new Scanner(roomReader);
		while(in.hasNextLine()) {
			next = in.nextLine();
			String [] splitString = (next.split(", ",0)); // Each line has 3 pieces of information separated by a comma + space (", ")
			legend.put(splitString[0].charAt(0), splitString[1]); // splitString[0] is a string version of the latest room character, splitString[1] is the room name
			if (splitString[2].equals("Card") == false && splitString[2].equals("Other") == false) { // Each Room Description should be a "Card" or "Other"
				throw new BadConfigFormatException("Bad Room Legend File Configuration: Description is neither Card or Other");
			}
		}
		in.close();
	}


	/**
	 * loadBoardConfig() is used to construct the game board
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
		String [] splitString = next.split(",", -2); // Each new board character is separated by a comma for each line
		this.numColumns = splitString.length; // Number of columns for the board is equal to the total number of rooms in a row
		this.numRows = 0;
		this.numRows++; // Already read the first line (above)
		while (in.hasNextLine()) { // The number of rows for the board is equal to the number of lines in the file
			next = in.nextLine();
			this.numRows++;
		}

		board = new BoardCell[this.numRows][this.numColumns];
		int currentRow = 0;
		FileReader boardReader_copy = new FileReader(boardConfigFile);
		Scanner in2 = new Scanner(boardReader_copy);
		while (in2.hasNextLine()) {
			next = in2.nextLine();
			splitString = next.split(",", -2); // Each new board character is separated by a comma for each line
			if (splitString.length != numColumns) { // if there is a row with an incorrect number of columns
				throw new BadConfigFormatException("Board Layout Configuration Error: Row with invalid Columns");
			}
			for (int i = 0; i < splitString.length; ++i) { // i is the current column
				if (legend.get(splitString[i].charAt(0)) == null) {
					throw new BadConfigFormatException("Board Layout Configuration Error: Invalid Cell Initial (Letter) Entry");
				}
				if (splitString[i].length() == 2) {
					if (splitString[i].charAt(1) == 'N') { // 'N' is not needed right now (used for where the room name will display)
						board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0)); // make a new cell using: current row, current column, board character
					}
					else { // This is doorway, make a new cell using: current row, current column, board character, door direction
						board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0), splitString[i].charAt(1));
					}
				}
				else { // This is not a doorway, make a new cell using: current row, current column, board character
					board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0));
				}
			}
			currentRow++; // done with this iteration, move to next row
		}

		calcAdjList();
		visited = new HashSet<BoardCell>();
	}


	public void loadPlayerConfig(){
		playerList = new Player[NUM_PLAYERS];
		for(int i = 0; i < 6; i++){
			playerList[i] = new HumanPlayer();
		}
		try {
			String next = "";
			FileReader playerReader = new FileReader(playerConfigFile);
			Scanner in = new Scanner(playerReader);
			int i = 0;
			while(in.hasNextLine()) {
				next = in.nextLine();
				String [] splitString = (next.split(", ",0)); // Each line has 5 pieces of information separated by a comma + space (", ")

				String name = splitString[0];
				String color = splitString[1];
				int row = Integer.parseInt(splitString[2]);
				int col = Integer.parseInt(splitString[3]);
				String type = splitString[4];

				if(type.equals("Human")){
					HumanPlayer humanPlayer = new HumanPlayer(row, col, name, color);
					playerList[i] = humanPlayer;
				}
				else{
					ComputerPlayer computerPlayer = new ComputerPlayer(row, col, name, color);
					playerList[i] = computerPlayer;
				}
				i++;

			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void loadCards(){
		cards = new HashSet<Card>();
		FileReader weaponReader;
		try {
			weaponReader = new FileReader(weaponConfigFile);
			Scanner in = new Scanner(weaponReader);
			String next = "";
			while(in.hasNextLine()) {
				next = in.nextLine();
				Card nextCard = new Card(next,CardType.WEAPON);
				cards.add(nextCard);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < NUM_PLAYERS; ++i) {
			Card nextCard = new Card(playerList[i].getPlayerName(),CardType.PLAYER);
			cards.add(nextCard);
		}

		for (char key : legend.keySet()) {
			if (key == 'X' || key == 'W') { // X: Closet, W: Walkway, so these are not cards.
				continue;
			}
			Card nextCard = new Card(legend.get(key),CardType.ROOM);
			cards.add(nextCard);
		}

	}

	public void selectAnswer() {
		answer = new Solution();
		Set<Card> weaponCards = new HashSet<Card>();
		Set<Card> roomCards = new HashSet<Card>();
		Set<Card> playerCards = new HashSet<Card>();
		for(Card next : cards) {
			switch(next.getType()) {
			case WEAPON:
				weaponCards.add(next);
				break;
			case PLAYER:
				playerCards.add(next);
				break;
			case ROOM:
				roomCards.add(next);
				break;
			}
		}
		int rand1 = (int)Math.floor((Math.random() * weaponCards.size()));
		int rand2 = (int)Math.floor((Math.random() * playerCards.size()));
		int rand3 = (int)Math.floor((Math.random() * roomCards.size()));

		int i = 0;
		for(Card next : weaponCards) {
			if (i == rand1) {
				answer.weapon = next.getCardName();
			}
			i++;
		}
		i = 0;
		for(Card next : playerCards) {
			if (i == rand2) {
				answer.player = next.getCardName();
			}
			i++;
		}
		i = 0;
		for(Card next : roomCards) {
			if (i == rand3) {
				answer.room = next.getCardName();
			}
			i++;
		}
	}

	public void dealCards(){
		Set<Card> cardsCopy = new HashSet<Card>();
		for(Card c : cards){
			cardsCopy.add(c);
		}
		System.out.println(cardsCopy.size());
		Set<Card> cardsToRemove = new HashSet<Card>();
		for(Card c : cardsCopy){
			if(c.getCardName() == answer.weapon || c.getCardName() == answer.player || c.getCardName() == answer.room){
				cardsToRemove.add(c);
			}
		}
		cardsCopy.removeAll(cardsToRemove);

		for(int k = 0; k < 6; k++){
			Set<Card> cardList = new HashSet<Card>();
			for(int j = 0; j < 3; j++){
				int rand = (int)Math.floor((Math.random() * cardsCopy.size()));
				int i = 0;
				Card chosenCard = new Card();
				for(Card next : cardsCopy){
					if(i == rand){
						cardList.add(next);
						chosenCard = next;
						break;
					}
					i++;
				}
				cardsCopy.remove(chosenCard);
				
			}
			playerList[k].setMyCards(cardList);
		}
		
	}


	/**
	 * calcAdjList() is used to calculate the adjacent cells for each cell
	 */
	public void calcAdjList(){
		for(int i = 0; i < getNumRows(); i++){
			for(int j = 0; j < getNumColumns(); j++){
				BoardCell currCell = board[i][j];
				HashSet<BoardCell> cellAdjList = new HashSet<BoardCell>(); // set of adjacencies for this cell
				if(currCell.getInitial() != 'W' && !currCell.isDoorway()){ // denotes a room space
					adjMatrix.put(currCell,  cellAdjList);
				}
				else if(currCell.isDoorway()){ // is cell a doorway
					switch(currCell.getDoorDirection()){ // for the doorway, add the correct adjacent cell depending on door direction
					case LEFT:
						cellAdjList.add(board[i][j - 1]); // add cell to left of door
						break;
					case RIGHT:
						cellAdjList.add(board[i][j + 1]); // add cell to right of door
						break;
					case UP:
						cellAdjList.add(board[i - 1][j]); // add cell above the door
						break;
					case DOWN:
						cellAdjList.add(board[i + 1][j]); // add cell below the door
						break;
					default:
						break;
					}
				}
				else{ // is a walkway
					HashSet<BoardCell> candidates = new HashSet<BoardCell>();
					if(i == 0){ // if on the top row
						candidates.add(board[i + 1][j]); // add cell below
					} else if(i == getNumRows() - 1){ // if on the bottom row
						candidates.add(board[i - 1][j]); // add cell above
					} else{ // add cell below and above
						candidates.add(board[i + 1][j]); 
						candidates.add(board[i - 1][j]);
					}
					if(j == 0){ // if in the first column
						candidates.add(board[i][j + 1]); // add cell to right
					} else if(j == getNumColumns() - 1){ // if in the last column
						candidates.add(board[i][j - 1]); // add cell to left
					} else{ // add cell to right and left
						candidates.add(board[i][j + 1]);
						candidates.add(board[i][j - 1]);
					}
					for(BoardCell bc: candidates){ // for each candidate adjacent cell for this board
						if(bc.getInitial() == 'W'){ // if this candidate cell is a walkway, simply add to the adjacency set for this cell
							cellAdjList.add(bc);
						}
						else if(bc.isDoorway()){ // if this candidate cell is a doorway, need to check direction
							boolean correctDirection = false; // Need to check the correct direction first
							switch(bc.getDoorDirection()){ // depending on door direction, check if the current cell is actually next to the door entrance
							case LEFT: // if the door is a left door
								if(bc.getColumn() == currCell.getColumn() + 1){ // if the current cell is to the left of the door
									correctDirection = true;
								}
								break;
							case RIGHT:
								if(bc.getColumn() == currCell.getColumn() - 1){ // if the current cell is to the right of the door
									correctDirection = true;
								}
								break;
							case UP:
								if(bc.getRow() == currCell.getRow() + 1){ // if the current cell above the door
									correctDirection = true;
								}
								break;
							case DOWN:
								if(bc.getRow() == currCell.getRow() - 1){ // if the current cell is below the door
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
	 * @param row Row of the current cell
	 * @param col Column of the current cell
	 * @param pathLength How many spaces player can still move from current cell
	 */
	public void calcTargets(int row, int col, int pathLength) { // Calculates targets for given cell
		visited = new HashSet<BoardCell>(); // Initialize visited and targets as empty sets to get ready for finding new targets
		targets = new HashSet<BoardCell>();
		findAllTargets(row, col, pathLength); // Looks for targets corresponding to specified cell and pathLength
	}

	public void findAllTargets(int row, int col, int pathLength){ // Finds all possible targets for given cell and pathLength
		BoardCell currentCell = board[row][col];
		visited.add(currentCell);
		for (BoardCell nextCell : adjMatrix.get(currentCell)) { // for each adjacent cell to the current cell
			if (visited.contains(nextCell)) { // if we've been here already we just check the next possible cell
				continue;
			}
			else if (nextCell.isDoorway()){ // we can automatically enter adjacent doorways
				targets.add(nextCell); // so add cell as a possible target
				continue;
			}
			else if (nextCell.getInitial() != 'W') { // cell is not a walkway nor a doorway, so cannot be a target
				continue;
			}
			else {
				visited.add(nextCell); // add to visited list if we haven't been here before
			}
			if (pathLength == 1) { // if at the end of our moving turn, then the cell should be a possible target
				targets.add(nextCell);
			}
			else { // otherwise move to next cell and continue finding more targets
				findAllTargets(nextCell.getRow(),nextCell.getColumn(),pathLength-1); // calculate the targets from the next cell
			}
			visited.remove(nextCell); // once done processing cell remove it from the visited list
		}
	}

	public void setConfigFiles(String boardConfigFile, String roomConfigFile) {
		this.boardConfigFile = boardConfigFile;
		this.roomConfigFile = roomConfigFile;
	}

	public void setGameSetupFiles(String playerConfigFile, String weaponConfigFile) {
		this.playerConfigFile = playerConfigFile;
		this.weaponConfigFile = weaponConfigFile;
	}
	//-----------------------------------------------------------------------------
}
