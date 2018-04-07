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
		// Create a layout with 2 rows
		setLayout(new GridLayout(1,2));
		JPanel panel = createNamePanel();
		add(panel);
		panel = createButtonPanel();
		add(panel);
	}

	 private JPanel createNamePanel() {
		 	JPanel panel = new JPanel();
		 	// Use a grid layout, 1 row, 2 elements (label, text)
			panel.setLayout(new GridLayout(2,1));
		 	JLabel nameLabel = new JLabel("Whose turn?");
			name = new JTextField(20);
			name.setEditable(false);
			panel.add(nameLabel);
			panel.add(name);
			return panel;
	}
	 
	private JPanel createButtonPanel() {
		// no layout specified, so this is flow
		JButton agree = new JButton("Next player");
		JButton disagree = new JButton("Make an accusation");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(agree);
		panel.add(disagree);
		return panel;
	}
	
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


}
