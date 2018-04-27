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
	// Used to display the text boxes
	private JTextField playerName;
	private JTextField rollNumber;
	private static JTextField lastSuggestion;
	private static JTextField disprovingResult;

	// Panels for the text boxes
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
		JButton nextPlayer = new JButton("Next player"); // "Next Player" Button
		nextPlayer.addActionListener(new ActionListener(){ // Listens for a button click of "Next Player"
			public void actionPerformed(ActionEvent e) {
				if(board.getIsHumanPlayersTurn()){ // If it is still the human's turn, they can't go to the next player until their turn is over
					JOptionPane.showMessageDialog(board, "You must make a move"); // Error message
				}
				else{
					playerName.setText(board.displayNextPlayer()); // Set the playerName JTextField to the current player
					board.movePlayer(rollNumber); // Allows the current player to move
					if (board.getPlayerList()[board.getCurrentPlayerIndex()] instanceof ComputerPlayer) { // If the current player is a cpu
						if (((ComputerPlayer)board.getPlayerList()[board.getCurrentPlayerIndex()]).getRecentlyWrong() == true) { // If the cpu has recently made an incorrect accusation
							((ComputerPlayer)board.getPlayerList()[board.getCurrentPlayerIndex()]).setRecentlyWrong(false); // Remove flag so that cpu doesn't try same accusation again
							board.endTurn(); // end cpu's turn because they were wrong
							return; // move is over
						}
					}
					if (board.checkCurrentPlayerIsHuman() == false) { // if the current player is not a human
						if (board.checkIfInRoom() == true) { // if the cpu is in a room
							Solution suggestion = board.makeSuggestion(); // cpu make suggestion
							findDisprovingCard(suggestion); // try to disprove suggestion
						}
						board.endTurn(); // turn is over
					}
				}
			}
		});

		JButton makeAccusation = new JButton("Make an accusation"); // "Make Accusation" button
		makeAccusation.addActionListener(new ActionListener(){ // Listens for a button click of "Make Accusation"
			public void actionPerformed(ActionEvent e) {
				if(!board.getIsHumanPlayersTurn()){ // If it is still the human's turn, they can't go to the next player until their turn is over
					JOptionPane.showMessageDialog(board, "It is not your turn"); // Error message
				}
				else{ // Display an accusation window
					AccusationDialog accusationDialog = new AccusationDialog();
					accusationDialog.setVisible(true);
				}
			}
		});


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
			if (board.getPlayerList()[board.getCurrentPlayerIndex()] instanceof ComputerPlayer){ // If the current player is a computer
				boolean matchingRoomCard = false; // need to check if cpu has the room card they're currently in
				for(Card nextCard : ((ComputerPlayer)board.getPlayerList()[board.getCurrentPlayerIndex()]).getMyCards()){ // for each of the cpu's cards
					if(nextCard.getCardName().equals(suggestion.room)){ // check if current card is the room card cpu is in
						matchingRoomCard = true; // cpu does have the room card they're in
					}
				}
				if(!matchingRoomCard){ // if cpu doesn't contain the room card and no-one could disprove
					((ComputerPlayer)board.getPlayerList()[board.getCurrentPlayerIndex()]).setShouldMakeAccusation(true); // cpu should make an accusation next turn
					((ComputerPlayer)board.getPlayerList()[board.getCurrentPlayerIndex()]).setRightAccusation(suggestion); // accusation should be their latest suggestion
				}
			}
			disprovingResult.setText("No new clue!"); // no new clue was found
		}
		else { // A disproving card was found
			if(board.getPlayerList()[board.getCurrentPlayerIndex()] instanceof ComputerPlayer){ // if they're a cpu we need to edit their unseen card lists
				((ComputerPlayer)board.getPlayerList()[board.getCurrentPlayerIndex()]).updateCards(board.getLatestDisprovingCard()); // edit the cpu's card list
			}
			disprovingResult.setText(board.getLatestDisprovingCard().getCardName()); // Set the JTextField of the disprovingResult to the card name
		}
	}

}
