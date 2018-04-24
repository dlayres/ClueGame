package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CardSelectionDialog extends JDialog{

	private static Board board;

	
	public CardSelectionDialog(Set<Card> matchingCards){
		
		board = board.getInstance();

		setTitle("Select a card to disprove");
		setSize(650, 200);
		setLayout(new GridLayout(3, 1));
		
		JLabel instructionLabel = new JLabel("A player made a suggestion and you have to disprove it! Select a card you want to show");
		
		add(instructionLabel);
		
		JComboBox cardOptions = new JComboBox();
		for(Card nextCard : matchingCards){
			cardOptions.addItem(nextCard);
		}
		
		add(cardOptions);
		
		
		// CODE FROM THE COMPUTING GODS
		// DO NOT CONTINUE FUNCTION UNTIL THE BOX HAS BEEN DISPOSED!!!!!
		// WHICH WILL PREVENT SUSEQUENT CODE FROM RUNNING
		setModal(true);
		
		JButton selectionSubmitButton = new JButton("Submit");
		selectionSubmitButton.addActionListener(new ActionListener(){ // Listens for a button click
			public void actionPerformed(ActionEvent e) {
				((HumanPlayer)board.getPlayerList()[0]).setDisprovingCard((Card)cardOptions.getSelectedItem());
				dispose();
			}
		});
		
		add(selectionSubmitButton);
		
	}
	

}
