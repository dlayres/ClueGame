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
		setModal(true); // Pauses code until the dialog box is closed
		
		JPanel suggestionButtons = new JPanel(); // Panel for the buttons
		suggestionButtons.setLayout(new GridLayout(1, 2));
		JButton suggestionSubmitButton = new JButton("Submit"); // Make a new "Submit" button
		suggestionSubmitButton.addActionListener(new ActionListener(){ // Listens for a button click
			public void actionPerformed(ActionEvent e) {
				dispose(); // Closes the dialog box and sets the selection to the selected items
				Solution newSuggestion = new Solution((String)playerChoiceBox.getSelectedItem(), roomChoiceLabel.getText(), (String)weaponChoiceBox.getSelectedItem());
				ControlGUI.findDisprovingCard(newSuggestion); // The ControlGUI updates the disprovingCard text field to display the card that disproved the suggestion
			}
		});
		JButton suggestionCancelButton = new JButton("Cancel"); // Make a new "Cancel" button
		suggestionCancelButton.addActionListener(new ActionListener(){ // Listens for a button click
			public void actionPerformed(ActionEvent e) {
				dispose(); // Closes the suggestion dialog when "Cancel" is clicked
			}
		});
		suggestionButtons.add(suggestionSubmitButton); // Add both buttons to the button panel
		suggestionButtons.add(suggestionCancelButton);
		add(suggestionButtons, BorderLayout.SOUTH); // Buttons are at the south of the dialog box

		JPanel suggestionChoices = new JPanel(); // Panels for all combo boxes relating to rooms, weapons, and players
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

		suggestionChoices.add(roomChoice); // Adds the combo box panels to the main dialog box
		suggestionChoices.add(playerChoice);
		suggestionChoices.add(weaponChoice);

		add(suggestionChoices, BorderLayout.CENTER); // Place the combo box panels in the center of the dialog box

	}
	
	public void setRoomLabel(String currentRoom){
		roomChoiceLabel.setText(currentRoom);
	}

}
