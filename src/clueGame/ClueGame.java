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

	private static Board board;
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

		JPanel boardPanel = board;
		add(boardPanel);

		// Create the JPanel for the control GUI and add it to the JFrame
		ControlGUI gui = new ControlGUI();
		add(gui, BorderLayout.SOUTH);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		initializeDetectiveNotes();
	}

	private JMenu createFileMenu(){
		JMenu menu = new JMenu("File");
		menu.add(openDetectiveNotes());
		menu.add(createFileExitItem());
		return menu;
	}

	private JMenuItem openDetectiveNotes() {
		JMenuItem item = new JMenuItem("Detective Notes");
		class MenuItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				detectiveNotesGUI.setVisible(true);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}

	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	public void initializeDetectiveNotes(){
		detectiveNotesGUI = new JFrame();
		detectiveNotesGUI.setTitle("Detective Notes");
		detectiveNotesGUI.setSize(500, 575);
		detectiveNotesGUI.setLayout(new GridLayout(3, 1));
		
		JPanel peoplePanel = new JPanel(); 
		peoplePanel.setLayout(new GridLayout(1,2));
		JPanel peopleChecklistPanel = new JPanel();
		peopleChecklistPanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		Set<Card> playerCardSet = board.getPlayerCards();
		for(Card nextPlayerCard : playerCardSet){
			JCheckBox personCheckBox = new JCheckBox(nextPlayerCard.getCardName());
			personCheckBox.setSelected(false);
			peopleChecklistPanel.add(personCheckBox);
		}
		JPanel peopleGuessPanel = new JPanel();
		peopleGuessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Person Guess"));
		peoplePanel.add(peopleChecklistPanel);
		peoplePanel.add(peopleGuessPanel);
		
		
		detectiveNotesGUI.add(peoplePanel);
		
		JPanel roomPanel = new JPanel(); 
		roomPanel.setLayout(new GridLayout(1,2));
		JPanel roomChecklistPanel = new JPanel();
		roomChecklistPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		Set<Card> roomCardSet = board.getRoomCards();
		for(Card nextRoomCard : roomCardSet){
			JCheckBox roomCheckBox = new JCheckBox(nextRoomCard.getCardName());
			roomCheckBox.setSelected(false);
			roomChecklistPanel.add(roomCheckBox);
		}
		JPanel roomGuessPanel = new JPanel();
		roomGuessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Room Guess"));
		roomPanel.add(roomChecklistPanel);
		roomPanel.add(roomGuessPanel);
		detectiveNotesGUI.add(roomPanel);
		
		JPanel weaponPanel = new JPanel(); 
		weaponPanel.setLayout(new GridLayout(1,2));
		JPanel weaponChecklistPanel = new JPanel();
		weaponChecklistPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		JPanel weaponGuessPanel = new JPanel();
		Set<Card> weaponCardSet = board.getWeaponCards();
		for(Card nextWeaponCard : weaponCardSet){
			JCheckBox weaponCheckBox = new JCheckBox(nextWeaponCard.getCardName());
			weaponCheckBox.setSelected(false);
			weaponChecklistPanel.add(weaponCheckBox);
		}
		weaponGuessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapon Guess"));
		weaponPanel.add(weaponChecklistPanel);
		weaponPanel.add(weaponGuessPanel);
		detectiveNotesGUI.add(weaponPanel);
	}

	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);

	}
}
