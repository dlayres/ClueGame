package clueGame;

import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotes extends JDialog{
	
	private static Board board;
	
	public DetectiveNotes(){
		board = Board.getInstance(); // Get the board instance
		
		setTitle("Detective Notes");
		setSize(500, 575);
		setLayout(new GridLayout(3, 1)); // 3 rows for players, rooms, and weapons
		
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
		JComboBox<String> peopleGuessBox = new JComboBox<String>(); // Combo box to store human's best guess for the player
		peopleGuessBox.addItem("Unsure"); // Default option
		for(Card nextPlayerCard : playerCardSet){ // For all player cards
			peopleGuessBox.addItem(nextPlayerCard.getCardName()); // Add the name of the card to the combo box as an option
		}
		peopleGuessPanel.add(peopleGuessBox); // Add the combo box to the guess panel
		peoplePanel.add(peopleChecklistPanel); // Add people checklist panel to the people panel
		peoplePanel.add(peopleGuessPanel); // Add player guess panel to the people panel
		add(peoplePanel); // Add people panel to the detective notes JDialog
		
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
		
		JComboBox<String> roomGuessBox = new JComboBox<String>(); // Combo box to store human's best guess for the room
		roomGuessBox.addItem("Unsure"); // Default option
		for(Card nextRoomCard : roomCardSet){ // For all room cards
			roomGuessBox.addItem(nextRoomCard.getCardName()); // Add the name of the card to the combo box as an option
		}
		roomGuessPanel.add(roomGuessBox); // Add the combo box to the guess panel
		roomPanel.add(roomChecklistPanel); // Add rooms checklist panel to the rooms panel
		roomPanel.add(roomGuessPanel); // Add room guess panel to the rooms panel
		add(roomPanel); // Add rooms panel to the detective notes JDialog
		
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
		JComboBox<String> weaponGuessBox = new JComboBox<String>(); // Combo box to store human's best guess for the weapon
		weaponGuessBox.addItem("Unsure"); // Default option
		for(Card nextWeaponCard : weaponCardSet){ // For all weapon cards
			weaponGuessBox.addItem(nextWeaponCard.getCardName()); // Add the name of the card to the combo box as an option
		}
		weaponGuessPanel.add(weaponGuessBox); // Add the combo box to the guess panel
		weaponPanel.add(weaponChecklistPanel); // Add weapons checklist to the weapons panel
		weaponPanel.add(weaponGuessPanel); // Add weapon guess panel to the weapons panel
		add(weaponPanel); // Add weapons panel to the detective notes JDialog
	}
}
