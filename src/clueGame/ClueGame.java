package clueGame;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGame extends JFrame {

	private static Board board; // Board GUI JPanel
	private JFrame detectiveNotesGUI;

	public ClueGame() {
		// Board is singleton, get the only instance there is
		board = Board.getInstance();
		board.setConfigFiles("boardLayout.csv", "ourLegend.txt");		
		// Initialize will load both of our board configuration files 
		board.initialize();
		// set the file names to use our player config file
		board.setGameSetupFiles("playerConfig.txt", "weaponConfig.txt");		
		// Will load our player config file 
		board.loadPlayerConfig();
		// Game card and solution handling
		board.loadCards();
		board.selectAnswer();
		board.dealCards();

		// Create a JFrame with all the normal functionality
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("ClueGame");
		setSize(1000, 800);

		// Create the JPanel for the board GUI and add it to the JFrame
		JPanel boardPanel = board;
		add(boardPanel);

		// Create the JPanel for the control GUI and add it to the JFrame
		ControlGUI gui = new ControlGUI();
		add(gui, BorderLayout.SOUTH);

		// Create a menu bar with "File" drop-down option
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		initializeDetectiveNotes(); // Creates detective notes GUI
	}
	
	/**
	 * Creates the "File" drop-down with two options for exiting and opening the detective notes GUI
	 * @return menu
	 */
	private JMenu createFileMenu(){
		JMenu menu = new JMenu("File");
		menu.add(openDetectiveNotes());
		menu.add(createFileExitItem());
		return menu;
	}

	/**
	 * Displays the detective notes GUI when the option is clicked
	 * @return item
	 */
	private JMenuItem openDetectiveNotes() {
		JMenuItem item = new JMenuItem("Detective Notes");
		class MenuItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){ // Listens for click
				detectiveNotesGUI.setVisible(true); // Displays GUI when clicked
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}

	/**
	 * Exits the program when the exit option is clicked
	 * @return item
	 */
	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){ // Listens for click
				System.exit(0); // Exits program when clicked
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	/**
	 * Creates the detective notes GUI with checklists and combo boxes corresponding to players, rooms, and weapons
	 */
	public void initializeDetectiveNotes(){
		// Create the basic JFrame for the detective notes GUI
		detectiveNotesGUI = new JFrame();
		detectiveNotesGUI.setTitle("Detective Notes");
		detectiveNotesGUI.setSize(500, 575);
		detectiveNotesGUI.setLayout(new GridLayout(3, 1));
		
		// Add a panel for the people in the game
		JPanel peoplePanel = new JPanel(); 
		peoplePanel.setLayout(new GridLayout(1,2)); // Left column for people checklist, right column for people combo box
		JPanel peopleChecklistPanel = new JPanel();
		peopleChecklistPanel.setLayout(new GridLayout(1, 2)); // Checklist panel has two columns to display all the people
		peopleChecklistPanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		JPanel peopleChecklistLeft = new JPanel(); // Left checklist column
		JPanel peopleChecklistRight = new JPanel(); // Right checklist column
		peopleChecklistPanel.add(peopleChecklistLeft); // Add both to the checklist panel
		peopleChecklistPanel.add(peopleChecklistRight); 
		Set<Card> playerCardSet = board.getPlayerCards(); // Use player cards to get player names
		int i = 0; // Determines if check box should be in the left or right column
		for(Card nextPlayerCard : playerCardSet){ // For all player cards
			JCheckBox personCheckBox = new JCheckBox(nextPlayerCard.getCardName()); // Make a check box with that name
			personCheckBox.setSelected(false);
			if(i < (Math.ceil(playerCardSet.size() / 2.0))) { // Put half on the left and half on the right
				peopleChecklistLeft.add(personCheckBox);
			}
			else {
				peopleChecklistRight.add(personCheckBox);
			}
			i++;
		}
		JPanel peopleGuessPanel = new JPanel();
		peopleGuessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Person Guess"));
		peoplePanel.add(peopleChecklistPanel); // Add people checklist panel to the people panel
		peoplePanel.add(peopleGuessPanel);
		detectiveNotesGUI.add(peoplePanel); // Add people panel to the detective notes JFrame
		
		// Add a panel for the rooms in the game
		JPanel roomPanel = new JPanel(); 
		roomPanel.setLayout(new GridLayout(1,2)); // Left column for rooms checklist, right column for rooms combo box
		JPanel roomChecklistPanel = new JPanel();
		roomChecklistPanel.setLayout(new GridLayout(1, 2)); // Checklist panel has two columns to display all the rooms
		roomChecklistPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		JPanel roomChecklistLeft = new JPanel(); // Left checklist column
		JPanel roomChecklistRight = new JPanel(); // Right checklist column
		roomChecklistPanel.add(roomChecklistLeft); // Add both to the checklist panel
		roomChecklistPanel.add(roomChecklistRight);
		Set<Card> roomCardSet = board.getRoomCards(); // Use room cards to get room names
		i = 0; // Determines if check box should be in the left or right column
		for(Card nextRoomCard : roomCardSet){ // For all room cards
			JCheckBox roomCheckBox = new JCheckBox(nextRoomCard.getCardName()); // Make a check box with that name
			roomCheckBox.setSelected(false);
			if(i < (Math.ceil(roomCardSet.size() / 2.0))) { // Put half on the left and half on the right
				roomChecklistLeft.add(roomCheckBox);
			}
			else {
				roomChecklistRight.add(roomCheckBox);
			}
			i++;
		}
		JPanel roomGuessPanel = new JPanel();
		roomGuessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Room Guess"));
		roomPanel.add(roomChecklistPanel); // Add rooms checklist panel to the rooms panel
		roomPanel.add(roomGuessPanel);
		detectiveNotesGUI.add(roomPanel); // Add rooms panel to the detective notes JFrame
		
		// Add a panel for the weapons in the game
		JPanel weaponPanel = new JPanel(); 
		weaponPanel.setLayout(new GridLayout(1,2)); // Left column for weapons checklist, right column for weapons combo box
		JPanel weaponChecklistPanel = new JPanel();
		weaponChecklistPanel.setLayout(new GridLayout(1, 2)); // Checklist panel has two columns to display all the weapons
		weaponChecklistPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons")); 
		JPanel weaponChecklistLeft = new JPanel(); // Left checklist column
		JPanel weaponChecklistRight = new JPanel(); // Right checklist column
		weaponChecklistPanel.add(weaponChecklistLeft); // Add both to the checklist panel
		weaponChecklistPanel.add(weaponChecklistRight);
		Set<Card> weaponCardSet = board.getWeaponCards(); // Use weapon cards to get weapon names
		i = 0; // Determines if check box should be in the left or right column
		for(Card nextWeaponCard : weaponCardSet){ // For all weapon cards
			JCheckBox weaponCheckBox = new JCheckBox(nextWeaponCard.getCardName()); // Make a check box with that name
			weaponCheckBox.setSelected(false);
			if(i < (Math.ceil(weaponCardSet.size() / 2.0))) { // Put half on the left and half on the right
				weaponChecklistLeft.add(weaponCheckBox);
			}
			else {
				weaponChecklistRight.add(weaponCheckBox);
			}
			i++;
		}
		JPanel weaponGuessPanel = new JPanel();
		weaponGuessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapon Guess"));
		weaponPanel.add(weaponChecklistPanel); // Add weapons checklist to the weapons panel
		weaponPanel.add(weaponGuessPanel);
		detectiveNotesGUI.add(weaponPanel); // Add weapons panel to the detective notes JFrame
	}

	public static void main(String[] args) {
		ClueGame game = new ClueGame(); // Make ClueGame GUI
		game.setVisible(true); // Display the GUI

	}
}
