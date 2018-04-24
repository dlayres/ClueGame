package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SuggestionDialog extends JDialog{

	private static Board board;
	private JLabel roomChoiceLabel;
	private JComboBox playerChoiceBox;
	private JComboBox weaponChoiceBox;
	
	public SuggestionDialog(){
		
		board = board.getInstance();

		setTitle("Make a suggestion");
		setSize(300, 250);
		setLayout(new BorderLayout());
		
		JPanel suggestionButtons = new JPanel();
		suggestionButtons.setLayout(new GridLayout(1, 2));
		JButton suggestionSubmitButton = new JButton("Submit");
		suggestionSubmitButton.addActionListener(new ActionListener(){ // Listens for a button click
			public void actionPerformed(ActionEvent e) {
				Solution newSuggestion = new Solution((String)playerChoiceBox.getSelectedItem(), roomChoiceLabel.getText(), (String)weaponChoiceBox.getSelectedItem());
				ClueGame.setSuggestionText((String)playerChoiceBox.getSelectedItem() + ", " + roomChoiceLabel.getText() + ", " + (String)weaponChoiceBox.getSelectedItem());
				
				dispose();
				
				
				board.lastestDisprovingCard = board.handleSuggestion((board.getCurrentPlayerIndex()+(Board.NUM_PLAYERS-1)) % Board.NUM_PLAYERS, newSuggestion, board.getPlayerList());
				if (board.lastestDisprovingCard == null) {
					ControlGUI.disprovingResult.setText("No new clue!");
				}
				else {
					ControlGUI.disprovingResult.setText(board.lastestDisprovingCard.getCardName());
				}
			}
		});
		JButton suggestionCancelButton = new JButton("Cancel");
		suggestionCancelButton.addActionListener(new ActionListener(){ // Listens for a button click
			public void actionPerformed(ActionEvent e) {
				dispose(); // Closes the suggestion dialog when "Cancel" is clicked
			}
		});
		suggestionButtons.add(suggestionSubmitButton);
		suggestionButtons.add(suggestionCancelButton);
		add(suggestionButtons, BorderLayout.SOUTH);

		JPanel suggestionChoices = new JPanel();
		suggestionChoices.setLayout(new GridLayout(3, 1));
		JPanel playerChoice = new JPanel();
		playerChoice.setLayout(new GridLayout(1, 2));
		JPanel roomChoice = new JPanel();
		roomChoice.setLayout(new GridLayout(1, 2));
		JPanel weaponChoice = new JPanel();
		weaponChoice.setLayout(new GridLayout(1, 2));

		JLabel roomSuggestionLabel = new JLabel("Current room");
		JLabel playerSuggestionLabel = new JLabel("Player choice");
		JLabel weaponSuggestionLabel = new JLabel("Weapon choice");
		roomChoiceLabel = new JLabel();
		playerChoiceBox = new JComboBox();
		for(Card nextPlayerCard : board.getPlayerCards()){ // For all player cards
			playerChoiceBox.addItem(nextPlayerCard.getCardName()); // Add the name of the card to the combo box as an option
		}
		weaponChoiceBox = new JComboBox();
		for(Card nextWeaponCard : board.getWeaponCards()){ // For all weapon cards
			weaponChoiceBox.addItem(nextWeaponCard.getCardName()); // Add the name of the card to the combo box as an option
		}
		roomChoice.add(roomSuggestionLabel);
		roomChoice.add(roomChoiceLabel);
		playerChoice.add(playerSuggestionLabel);
		playerChoice.add(playerChoiceBox);
		weaponChoice.add(weaponSuggestionLabel);
		weaponChoice.add(weaponChoiceBox);

		suggestionChoices.add(roomChoice);
		suggestionChoices.add(playerChoice);
		suggestionChoices.add(weaponChoice);

		add(suggestionChoices, BorderLayout.CENTER);

	}
	
	public void setRoomLabel(String currentRoom){
		roomChoiceLabel.setText(currentRoom);
	}

}
