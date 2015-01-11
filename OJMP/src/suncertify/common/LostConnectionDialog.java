package suncertify.common;

import javax.swing.JOptionPane;

/**
 * This class is used to display a warning dialog when connection to the server
 * has been lost.
 * 
 * @author Robbie Byrne
 * 
 */
public class LostConnectionDialog {

	/**
	 * This method waits for the user to click OK on the dialog and exits the
	 * client.
	 */
	public void processResponce() {
		JOptionPane.showMessageDialog(null,
				"Connection has been Lost, Closing Client.");
		System.exit(1);
	}
}