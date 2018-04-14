package clueGame;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * SplashScreen class notifies user of who they are and how to start the game through a message box
 */
public class SplashScreen extends JComponent {
	
	/**
	 * SplashScreen constructor to display the splash box/screen
	 * @param frame - the JFrame used to display the box/screen
	 */
	public SplashScreen(JFrame frame) {
		// Create a splash screen/box with the given frame, displaying the message "You are Mr. Orange, press Next Player to start." in the box
		// The screen/box name is "Welcome to Clue"
		JOptionPane.showMessageDialog(frame, "You are Mr. Orange, press Next Player to start.", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
		
	}
}
