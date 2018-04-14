package clueGame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MyCardsGUI extends JPanel {

	private static Board board;

	public MyCardsGUI(){
		board = board.getInstance(); // Get the board instance
		Set<Card> humanPlayerCards = board.getPlayerList()[0].getMyCards(); // List of cards dealt to the human player

		setBorder(new TitledBorder (new EtchedBorder(), "My Cards"));
		setLayout(new GridLayout(3, 1)); // 3 rows for players, rooms, and weapons cards

		// Makes a panel for people, rooms, and weapons, and sets the border and title
		JPanel myPeoplePanel = new JPanel();
		myPeoplePanel.setLayout(new FlowLayout());
		myPeoplePanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		JPanel myRoomsPanel = new JPanel();
		myRoomsPanel.setLayout(new FlowLayout());
		myRoomsPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		JPanel myWeaponsPanel = new JPanel();
		myWeaponsPanel.setLayout(new FlowLayout());
		myWeaponsPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));

		for(Card nextCard : humanPlayerCards){ // For each of the cards dealt to the human
			JTextField cardText = new JTextField(nextCard.getCardName()); // Create a text field with the name of the card displayed
			cardText.setEditable(false); // Shouldn't be able to edit the text field
			cardText.setPreferredSize(new Dimension(90, 50));
			cardText.setHorizontalAlignment(JTextField.CENTER);
			switch(nextCard.getType()){ // Add the card to its appropriate panel based on its type
			case PLAYER:
				myPeoplePanel.add(cardText);
				break;
			case ROOM:
				myRoomsPanel.add(cardText);
				break;
			case WEAPON:
				myWeaponsPanel.add(cardText);
				break;
			default:
				break;

			}
		}

		// Add the three panels to the MyCardsGUI JPanel
		add(myPeoplePanel);
		add(myRoomsPanel);
		add(myWeaponsPanel);

	}

}
