/**
 * @author Johnathan Castillo
 * @author David Ayres
 * C13A-1 Clue Paths
 */

package clueGame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Board extends JPanel implements MouseListener{
	private int numRows;
	private int numColumns;
	public static final int MAX_BOARD_SIZE = 50;
	public static final int NUM_PLAYERS = 6;
	private int currentPlayer = 0; // Player 0 is the human player and should go first
	private Player nextPlayer; // Player whose turn is next
	public static boolean isHumanPlayersTurn = false;
	private int mouseX; // Stores the x and y position of the mouse when board is clicked
	private int mouseY;
	private SuggestionDialog suggestionDialog;
	public Solution latestAnswer;
	public Card lastestDisprovingCard = new Card();

	private BoardCell[][] board; // The grid of the board
	private HashMap<Character, String> legend; // Used for determining room identity
	private Map<BoardCell, Set<BoardCell>> adjMatrix; // Map of cell adjacencies
	private Set<BoardCell> targets; // Possible targets to move to for a given cell
	private Set<BoardCell> visited; // Set of visited cells (used for calculating targets)
	
	private String boardConfigFile; // Board Configuration File Name
	private String roomConfigFile; // Room Configuration File Name
	private String playerConfigFile; // Player Configuration File Name
	private String weaponConfigFile; // Weapon Configuration File Name
	
	public static Player[] playerList; // Array of players in the game
	private Set<Card> cards; // Set of every card in the game
	private Solution answer; // The three cards randomly chosen as the game's solution
	
	private Set<Card> weaponCards; // Set of weapon cards
	private Set<Card> roomCards; // Set of room cards
	private Set<Card> playerCards; // Set of player cards
	
	private MyCardsGUI myCards; // GUI that displays the cards dealt to the human player
	
	private Set<BoardCell> doorways = new HashSet<BoardCell>(); // Set containing all the doorway cells

	// variable used for singleton pattern
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		addMouseListener(this); // adds mouse listener to detect clicks
	}
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
	 * The targets for a board cell after calculating them with calcTargets()
	 * @return getTargets
	 */
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	/**
	 * Gets the array of all the players
	 * @return playerList
	 */
	public Player[] getPlayerList(){
		return playerList;
	}
	
	/**
	 * Gets the set of all cards in the game
	 * @return cards
	 */
	public Set<Card> getCards(){
		return cards;
	}
	
	/**
	 * Gets the set of all weapon cards in the game
	 * @return weaponCards
	 */
	public Set<Card> getWeaponCards() {
		return weaponCards;
	}
	
	/**
	 * Gets the set of all room cards in the game
	 * @return roomCards
	 */
	public Set<Card> getRoomCards() {
		return roomCards;
	}
	
	/**
	 * Gets the set of all player cards in the game
	 * @return playerCards
	 */
	public Set<Card> getPlayerCards() {
		return playerCards;
	}
	
	/**
	 * Gets the value for whether or not it is currently the human player's turn
	 * @return isHumanPlayersTurn
	 */
	public boolean getIsHumanPlayersTurn() {
		return isHumanPlayersTurn;
	}
	
	public int getCurrentPlayerIndex() {
		return currentPlayer;
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
					if (splitString[i].charAt(1) == 'N') { // N indicates where the room name is displayed on the board
						board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0)); // make a new cell using: current row, current column, board character
						board[currentRow][i].setNameMe();
					}
					else { // This is doorway, make a new cell using: current row, current column, board character, door direction
						board[currentRow][i] = new BoardCell(currentRow, i, splitString[i].charAt(0), splitString[i].charAt(1));
						doorways.add(board[currentRow][i]);
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
	
	/**
	 * Used to construct the array of players with their corresponding info (name, starting location, color)
	 */
	public void loadPlayerConfig(){
		playerList = new Player[NUM_PLAYERS]; // Creates an array of 6 players for the game
		for(int i = 0; i < NUM_PLAYERS; i++){
			playerList[i] = new HumanPlayer();
		}
		try {
			String next = "";
			FileReader playerReader = new FileReader(playerConfigFile);
			Scanner in = new Scanner(playerReader);
			int i = 0;
			while(in.hasNextLine()) {
				next = in.nextLine();
				String [] splitString = (next.split(", ",0)); // Each line has 5 pieces of information for the player separated by a comma + space (", ")

				String name = splitString[0]; // First string is player's name
				String color = splitString[1]; // Second string is player's color
				int row = Integer.parseInt(splitString[2]); // Third string is player's starting row
				int col = Integer.parseInt(splitString[3]); // Fourth string is player's starting column
				String type = splitString[4]; // Fifth string is player's type (Human/Computer)

				if(type.equals("Human")){ // If the type is a human, creates new HumanPlayer
					HumanPlayer humanPlayer = new HumanPlayer(row, col, name, color);
					playerList[i] = humanPlayer;
				}
				else{ // Type is computer, creates new ComputerPlayer
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

	/**
	 * Used to construct the set of all cards in the game
	 */
	public void loadCards(){
		cards = new HashSet<Card>();
		FileReader weaponReader; // First creates the weapon cards by reading the weapon configuration file
		try {
			weaponReader = new FileReader(weaponConfigFile);
			Scanner in = new Scanner(weaponReader);
			String next = "";
			while(in.hasNextLine()) {
				next = in.nextLine();
				Card nextCard = new Card(next,CardType.WEAPON); // Creates card based on weapon string in config file
				cards.add(nextCard);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Next, creates the player cards based on the player array already created
		for (int i = 0; i < NUM_PLAYERS; ++i) {
			Card nextCard = new Card(playerList[i].getPlayerName(),CardType.PLAYER); // Names the card as the player's name
			cards.add(nextCard);
		}
		// Last, creates the room cards based on the legend file
		for (char key : legend.keySet()) {
			if (key == 'X' || key == 'W') { // X: Closet, W: Walkway, so these are not rooms, and shouldn't be cards
				continue;
			}
			Card nextCard = new Card(legend.get(key),CardType.ROOM);
			cards.add(nextCard);
		}

	}

	/**
	 * Randomly chooses one of each card type as the answer to the current game at the start
	 */
	public void selectAnswer() {
		answer = new Solution(); // Solution object to store answer
		// First separates cards into three sets based on card type
		weaponCards = new HashSet<Card>(); 
		roomCards = new HashSet<Card>();
		playerCards = new HashSet<Card>();
		for(Card next : cards) {
			switch(next.getType()) {
			case WEAPON: // Card type is weapon, add to weapon cards
				weaponCards.add(next);
				break;
			case PLAYER: // Card type is player, add to player cards
				playerCards.add(next);
				break;
			case ROOM: // Card type is room, add to room cards
				roomCards.add(next);
				break;
			}
		}
		// Pick a random number for each type of card
		int rand1 = (int)Math.floor((Math.random() * weaponCards.size()));
		int rand2 = (int)Math.floor((Math.random() * playerCards.size()));
		int rand3 = (int)Math.floor((Math.random() * roomCards.size()));

		int i = 0;
		for(Card next : weaponCards) { // Iterates through weapon card set until i equals the randomly chosen number
			if (i == rand1) {
				answer.weapon = next.getCardName(); // This is the card chosen as the weapon answer
			}
			i++;
		}
		i = 0;
		for(Card next : playerCards) { // Iterates through player card set until i equals the randomly chosen number
			if (i == rand2) {
				answer.player = next.getCardName(); // This is the card chosen as the player answer
			}
			i++;
		}
		i = 0;
		for(Card next : roomCards) { // Iterates through room card set until i equals the randomly chosen number
			if (i == rand3) {
				answer.room = next.getCardName(); // This is the card chosen as the room answer
			}
			i++;
		}
	}
	
	/**
	 * Deals an even number of leftover cards (not part of answer) randomly to each player
	 */
	public void dealCards(){
		Set<Card> cardsCopy = new HashSet<Card>(); // Creates temporary copy of card set used to deal cards
		for(Card c : cards){
			cardsCopy.add(c);
		}
		Set<Card> cardsToRemove = new HashSet<Card>(); // Set of cards to remove after being dealt
		for(Card c : cardsCopy){ // Gets rid of answer cards so they aren't dealt
			if(c.getCardName() == answer.weapon || c.getCardName() == answer.player || c.getCardName() == answer.room){
				cardsToRemove.add(c);
			}
		}
		cardsCopy.removeAll(cardsToRemove);

		for(int k = 0; k < 6; k++){ // For every player
			if (playerList[k] instanceof ComputerPlayer) {
				ComputerPlayer thisPlayer = (ComputerPlayer) playerList[k];
				thisPlayer.setUnseenWeaponCards(weaponCards);
				thisPlayer.setUnseenPlayerCards(playerCards);
				playerList[k] = thisPlayer;
			}
			Set<Card> cardList = new HashSet<Card>(); // Create list of cards to deal to the player
			for(int j = 0; j < 3; j++){ // Repeat three times (for weapon, player, and room card)
				int rand = (int)Math.floor((Math.random() * cardsCopy.size())); // Random number to pick the card
				int i = 0;
				Card chosenCard = new Card();
				for(Card next : cardsCopy){ // Iterates through card set until i equals the random number
					if(i == rand){
						cardList.add(next);
						chosenCard = next;
						break;
					}
					i++;
				}
				cardsCopy.remove(chosenCard); // Remove chosen card so it isn't dealt again
				
			}
			playerList[k].setMyCards(cardList); // Set the player's card list to the chosen cards
		}
		myCards = new MyCardsGUI(); // All initialization is done, can now make the human cards GUI
		suggestionDialog = new SuggestionDialog(); // Can also now make the suggestion dialog GUI with all the information it needs
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
	
	/**
	 * Sets the configuration files that will create the board with rooms
	 * @param boardConfigFile
	 * @param roomConfigFile
	 */
	public void setConfigFiles(String boardConfigFile, String roomConfigFile) {
		this.boardConfigFile = boardConfigFile;
		this.roomConfigFile = roomConfigFile;
	}
	
	/**
	 * Sets the configuration files that will setup the players and weapons
	 * @param playerConfigFile
	 * @param weaponConfigFile
	 */
	public void setGameSetupFiles(String playerConfigFile, String weaponConfigFile) {
		this.playerConfigFile = playerConfigFile;
		this.weaponConfigFile = weaponConfigFile;
	}
	//-----------------------------------------------------------------------------
	
	/**
	 * Sets the solution for the game
	 * @param player
	 * @param weapon
	 * @param room
	 */
	public void setChosenAnswer(String player, String weapon, String room) {
		answer = new Solution();
		answer.player = player;
		answer.weapon = weapon;
		answer.room = room;
	}
	
	/**
	 * testAccusation() : Given an accusation, tests if the accusation is correct or wrong
	 * @param proposedSolution
	 * @return
	 */
	public boolean testAccusation(Solution proposedSolution) {
		return ((proposedSolution.player == answer.player) && (proposedSolution.weapon == answer.weapon) && (proposedSolution.room == answer.room));
	}
	
	/**
	 * handleSuggestion() : takes the suggesting players index in the array, a (their) suggestion, and the array of players
	 * Loops through the players to see if anyone can disprove the suggestion
	 * @param indexOfSuggestingPlayer
	 * @param suggestion
	 * @param playerList
	 * @return
	 */
	public Card handleSuggestion(int indexOfSuggestingPlayer, Solution suggestion, Player[] playerList){
		Card disprovingCard = null;
		int temp = (indexOfSuggestingPlayer+(NUM_PLAYERS-1)) % NUM_PLAYERS;
		/*
		String name = playerList[temp].getPlayerName();
		System.out.println(name);
		*/
		for (int i = (temp + 1) % playerList.length; i != temp; i = (i+1) % playerList.length) { // start at player after the suggesting player, iterate through other players until back at suggesting player
			if (playerList[i] instanceof ComputerPlayer) { // if the next player is a cpu
				ComputerPlayer nextCPU = (ComputerPlayer) playerList[i];
				Card cardCheck = nextCPU.disproveSuggestion(suggestion); // check if this cpu can disprove the suggestion
				if (cardCheck != null) { // if can disprove, then return the disproving card
					disprovingCard = cardCheck;
					break;
				}
			}
			else { // if next player is a human
				HumanPlayer human = (HumanPlayer) playerList[i];
				Card cardCheck = human.disproveSuggestion(suggestion); // check if human can disprove the suggestion and get their response
				if (cardCheck != null) { // if can disprove, then return the disproving card
					disprovingCard = cardCheck;
					break;
				}
			}
		}
		
		return disprovingCard;
	}
	
	/**
	 * paintComponent() : Draws the board, each cell draws itself
	 * @param g Graphics object for enabling draw functions
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Always called
		setLayout(new BorderLayout());
		
		add(myCards, BorderLayout.EAST); // Add the myCards GUI to the right side of the board
		
		// Segment to initialize grid background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(BoardCell.OFFSET, 0, numColumns * BoardCell.CELL_WIDTH, numRows * BoardCell.CELL_HEIGHT); // Create light gray background rectangle for entire board first
		
		// Draw each board cell, and some of these cells will also draw the room name
		for (int i = 0; i < numRows; ++i) {
			for (int j = 0; j < numColumns; ++j) {
				board[i][j].draw(g,legend); // Draw them using their draw method
			}
		}
		
		// Draw doorway indication
		for(BoardCell doorwayCell : doorways){ // Draws all doorways last so they have proper border (not overlapping)
			doorwayCell.draw(g,legend);
		}
		
		// Segment to give the board a black outline
		g.setColor(Color.BLACK);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(1));
		g.drawLine(BoardCell.OFFSET, 0, numColumns * BoardCell.CELL_WIDTH + BoardCell.OFFSET, 0); // Draw top border of the board
		g.drawLine(BoardCell.OFFSET, numRows * BoardCell.CELL_HEIGHT, numColumns * BoardCell.CELL_WIDTH + BoardCell.OFFSET, numRows * BoardCell.CELL_HEIGHT); // Draw bottom border of the board
		g.drawLine(BoardCell.OFFSET, 0, BoardCell.OFFSET, numRows * BoardCell.CELL_HEIGHT); // Draw left border of the board
		g.drawLine(numColumns * BoardCell.CELL_WIDTH + BoardCell.OFFSET, 0, numColumns * BoardCell.CELL_WIDTH + BoardCell.OFFSET, numRows * BoardCell.CELL_HEIGHT); // Draw right border of the board

		// Draw the players at their initial starting locations
		for (int i = 0; i < NUM_PLAYERS; i++) {
			playerList[i].draw(g);
		}
		
		// If it is the human player's turn, it should draw the possible targets using a special draw function in BoardCell
		if (isHumanPlayersTurn) {
			targets = ((HumanPlayer)nextPlayer).checkTargets(targets); // Check targets for possible rooms with 2 doors
			for (BoardCell nextTarget : targets) {
				nextTarget.targetDraw(g);
			}
		}
		
	}
	
	/**
	 * Function that executes when mouse is clicked on board
	 */
	public void mouseClicked(MouseEvent e){
		if(isHumanPlayersTurn){ // If it's the human player's turn, get the mouseX and mouseY position, and check if any of the possible targets contain that point
			mouseX = e.getX();
			mouseY = e.getY();
			for(BoardCell nextTarget : targets){
				if(nextTarget.contains(mouseX, mouseY)){
					if (nextTarget.isDoorway()) { // if the target we choose is a room
						nextPlayer.currentlyInRoom = true; // keep track that we are in a room
					}
					else {
						nextPlayer.currentlyInRoom = false; // otherwise we know we are not in a room
					}
					((HumanPlayer) nextPlayer).updateLocation(nextTarget); // If a target contains the click position, the human moves to that cell and the turn is over. Also updates the last visited cell initial
					isHumanPlayersTurn = false;
					repaint();
					if(nextTarget.isDoorway()){
						suggestionDialog.setRoomLabel(legend.get(board[playerList[0].getRow()][playerList[0].getColumn()].getInitial())); // Sets the suggestion dialog to use the name of the room the player is currently in
						suggestionDialog.setVisible(true);
					}
					break;
				}
			}
			if(isHumanPlayersTurn){ // If the loop exited and it is still the human's turn, they did not click on a possible target
				JOptionPane.showMessageDialog(this, "Invalid location"); // Gives error message
			}
		}
	}
	// Functions that must be created for MouseListener
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	
	/**
	 * Updates the JTextField for displaying the current player's turn
	 * @return nextPlayerName
	 */
	public String displayNextPlayer() {
		String nextPlayerName = new String(playerList[currentPlayer].getPlayerName());
		return nextPlayerName;
	}
	
	/**
	 * Picks a random roll, updates the JTextField to display the roll value, and if it's the computer's turn, they move to a random target
	 * If it's the player's turn, the targets are displayed and they have to click one to progress the game
	 * @param rollNumber Text field to display the value of the roll, gets updated from ControlGUI
	 */
	public void movePlayer(JTextField rollNumber){
		int randomRoll = (int)Math.floor((Math.random() * 6) + 1); // Picks a random roll 1 - 6
		rollNumber.setText(String.valueOf(randomRoll)); // Updates rollNumber JTextField
		nextPlayer = playerList[currentPlayer]; // Gets the current player, and sets currentPlayer to the next player
		currentPlayer = (currentPlayer + 1) % playerList.length;
		if(nextPlayer instanceof ComputerPlayer){ // If player is computer,
			isHumanPlayersTurn = false; // It is not the human's turn
			calcTargets(nextPlayer.getRow(), nextPlayer.getColumn(), randomRoll); // Calculates the targets and selects a location to move to
			BoardCell targetCell = ((ComputerPlayer) nextPlayer).selectTarget(getTargets());
			((ComputerPlayer) nextPlayer).updateLocation(targetCell);
			repaint(); // Redraws board to display computer player's new location
		}
		else {
			isHumanPlayersTurn = true; // It is the human player's turn
			calcTargets(nextPlayer.getRow(), nextPlayer.getColumn(), randomRoll); // Calculates the targets
			repaint(); // Draws the targets for the human player to see
		}
	}
	
	public boolean checkIfInRoom() {
		return nextPlayer.currentlyInRoom;
	}
	
	public boolean checkNextPlayer() {
		return (nextPlayer instanceof HumanPlayer);
	}
	
	public Solution makeSuggestion() {
		return ((ComputerPlayer)nextPlayer).makeSuggestion(this.getCellAt(nextPlayer.getRow(), nextPlayer.getColumn()), legend);
	}
	
}
