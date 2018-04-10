package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ControlGUI extends JPanel {
	private JTextField name;
	
	public ControlGUI()
	{
		// Create a GUI layout with 2 rows and 2 columns
		setLayout(new GridLayout(2,2));
		JPanel panel = createButtonPanel();
		add(panel);
		panel = createInformationPanels();
		add(panel);
	}
	
	/**
	 * Creates all the info panels (player turn, die roll, suggestion, response) and adds them to the GUI
	 * @return panel
	 */
	private JPanel createInformationPanels() {
		JPanel panel = new JPanel();
		// Use a grid layout, 2 row, 2 elements (label, text)
		panel.setLayout(new GridLayout(2,2));

		JPanel namePanel = new JPanel(); // Adds a text field to display the current player's turn
		JLabel nameLabel = new JLabel("Whose turn?");
		namePanel.add(nameLabel);
		name = new JTextField(17);
		name.setEditable(false);
		namePanel.add(name);

		JPanel rollPanel = new JPanel(); // Adds a text field to display the current die roll value
		JLabel rollNameLabel = new JLabel("Die Roll");
		rollPanel.add(rollNameLabel);
		name = new JTextField(20);
		name.setEditable(false);
		rollPanel.add(name);

		JPanel guessPanel = new JPanel(); // Adds a text field to display the last suggestion made
		JLabel guessNameLabel = new JLabel("Suggestion");
		guessPanel.add(guessNameLabel);
		name = new JTextField(21);
		name.setEditable(false);
		guessPanel.add(name);

		JPanel resultPanel = new JPanel(); // Adds a text field to display the response card to the suggestion (if any)
		JLabel resultNameLabel = new JLabel("Disproving Card");
		resultPanel.add(resultNameLabel);
		name = new JTextField(19);
		name.setEditable(false);
		resultPanel.add(name);

		panel.add(namePanel);
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
		// no layout specified, so this is flow
		JButton nextPlayer = new JButton("Next player");
		JButton makeAccusation = new JButton("Make an accusation");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(nextPlayer);
		panel.add(makeAccusation);
		return panel;
	}
	
	/*
	public static void main(String[] args) {
		// Create a JFrame with all the normal functionality
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ClueGame");
		frame.setSize(800, 300);	
		// Create the JPanel and add it to the JFrame
		ControlGUI gui = new ControlGUI();
		frame.add(gui, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
	}
	*/

}
