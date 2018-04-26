package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ControlGUI extends JPanel {
	private JTextField playerName;
	private JTextField rollNumber;
	private static JTextField lastSuggestion;
	private static JTextField disprovingResult;
	
	private JPanel namePanel;
	private JPanel rollPanel;
	private JPanel guessPanel;
	private JPanel resultPanel;
	
	private static Board board;
	
	public ControlGUI()
	{
		// Create a GUI layout with 2 rows and 2 columns
		setLayout(new GridLayout(2,2));
		JPanel panel = createButtonPanel();
		add(panel);
		panel = createInformationPanels();
		add(panel);
		board = Board.getInstance(); // Board is singleton, get the only instance there is
	}
	
	/**
	 * Creates all the info panels (player turn, die roll, suggestion, response) and adds them to the GUI
	 * @return panel
	 */
	private JPanel createInformationPanels() {
		JPanel panel = new JPanel();
		// Use a grid layout, 2 row, 2 elements (label, text)
		panel.setLayout(new GridLayout(2,2));

		namePanel = new JPanel(); // Adds a text field to display the current player's turn
		JLabel nameLabel = new JLabel("Whose turn?");
		namePanel.add(nameLabel);
		playerName = new JTextField(17);
		playerName.setEditable(false);
		namePanel.add(playerName);

		rollPanel = new JPanel(); // Adds a text field to display the current die roll value
		JLabel rollNameLabel = new JLabel("Die Roll");
		rollPanel.add(rollNameLabel);
		rollNumber = new JTextField(20);
		rollNumber.setEditable(false);
		rollPanel.add(rollNumber);

		guessPanel = new JPanel(); // Adds a text field to display the last suggestion made
		JLabel guessNameLabel = new JLabel("Suggestion");
		guessPanel.add(guessNameLabel);
		lastSuggestion = new JTextField(21);
		lastSuggestion.setEditable(false);
		guessPanel.add(lastSuggestion);

		resultPanel = new JPanel(); // Adds a text field to display the response card to the suggestion (if any)
		JLabel resultNameLabel = new JLabel("Disproving Card");
		resultPanel.add(resultNameLabel);
		disprovingResult = new JTextField(19);
		disprovingResult.setEditable(false);
		resultPanel.add(disprovingResult);

		panel.add(namePanel); // Adds each of the text fields to the control panel
		panel.add(guessPanel);
		panel.add(rollPanel);
		panel.add(resultPanel);
		return panel;
	}

	/**
	 * Creates the buttons and adds them to the GUI
	 * @return panel
	 */
	private JPanel createButtonPanel() { // Creates two buttons to end turn (go to next player) and make accusation
		JButton nextPlayer = new JButton("Next player");
		nextPlayer.addActionListener(new ActionListener(){ // Listens for a button click of "Next Player"
			public void actionPerformed(ActionEvent e) {
				if(board.getIsHumanPlayersTurn()){ // If it is still the human's turn, they can't go to the next player until their turn is over
					JOptionPane.showMessageDialog(board, "You must make a move"); // Error message
				}
				else{
					playerName.setText(board.displayNextPlayer()); // Set the playerName JTextField to the current player
					board.movePlayer(rollNumber); // Allows the current player to move
					// make suggestion based on move
					if (board.checkNextPlayer() == false) {
						if (board.checkIfInRoom() == true) {
							Solution suggestion = board.makeSuggestion();
							findDisprovingCard(suggestion);
						}
					}
				}
			}
		});
		
		JButton makeAccusation = new JButton("Make an accusation");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(nextPlayer);
		panel.add(makeAccusation);
		return panel;
	}

	/**
	 * findDisprovingCard : calls handleSuggestion on a given suggestion so that it can update its JTextField for disprovingResult
	 * @param suggestion
	 */
	public static void findDisprovingCard(Solution suggestion) {
		lastSuggestion.setText(suggestion.player + ", " + suggestion.room + ", " + suggestion.weapon); // Sets the suggestion JTextField
		board.setLatestDisprovingCard(board.handleSuggestion(board.getCurrentPlayerIndex(), suggestion, board.getPlayerList())); // Uses board to handle the suggestion and gets a card that disproves it
		if (board.getLatestDisprovingCard() == null) { // If a disproving card wasn't found
			disprovingResult.setText("No new clue!");
		}
		else { // A disproving card was found
			disprovingResult.setText(board.getLatestDisprovingCard().getCardName()); // Set the JTextField of the disprovingResult to the card name
		}
	}
	
}
