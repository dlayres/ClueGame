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
	private DetectiveNotes detectiveNotes;
	private MyCardsGUI myCardsGUI;
	private ControlGUI gui;
	
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
		setLayout(new BorderLayout());

		// Create the JPanel for the board GUI and add it to the JFrame
		JPanel boardPanel = board;
		add(board, BorderLayout.CENTER);

		// Create a menu bar with "File" drop-down option
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		
		detectiveNotes = new DetectiveNotes();
		
		// Create the JPanel for the control GUI and add it to the JFrame
		gui = new ControlGUI();
		gui.setSize(800, 600);
		add(gui, BorderLayout.SOUTH);
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
				detectiveNotes.setVisible(true);
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


	public static void main(String[] args) {
		ClueGame game = new ClueGame(); // Make ClueGame GUI
		game.setVisible(true); // Display the GUI
		SplashScreen splash = new SplashScreen(game); // Display the splash box at the start of the game for the user
	}
}
