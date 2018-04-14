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
		board = board.getInstance();
		Set<Card> humanPlayerCards = board.getPlayerList()[0].getMyCards();

		setBorder(new TitledBorder (new EtchedBorder(), "My Cards"));
		setLayout(new GridLayout(3, 1));

		JPanel myPeoplePanel = new JPanel();
		myPeoplePanel.setLayout(new FlowLayout());
		myPeoplePanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		JPanel myRoomsPanel = new JPanel();
		myRoomsPanel.setLayout(new FlowLayout());
		myRoomsPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		JPanel myWeaponsPanel = new JPanel();
		myWeaponsPanel.setLayout(new FlowLayout());
		myWeaponsPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));

		for(Card nextCard : humanPlayerCards){
			JTextField cardText = new JTextField(nextCard.getCardName());
			cardText.setEditable(false);
			cardText.setPreferredSize(new Dimension(90, 50));
			cardText.setHorizontalAlignment(JTextField.CENTER);
			switch(nextCard.getType()){
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







		add(myPeoplePanel);
		add(myRoomsPanel);
		add(myWeaponsPanel);

	}

}
