package suncertify.standalone;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import suncertify.common.PropertiesFileAccess;

/**
 * This class is used to obtain the standalone setup details from the end user.
 * 
 * @author Robbie Byrne
 * 
 */
public class StandaloneSetupDialog extends JFrame {

	private static final long serialVersionUID = -5574802687830435034L;
	private String dbLocation = "";
	private String message = "Please enter db file location: ";
	private final String error = "Db file not found, please try again or press Cancel to exit: ";
	private final String locationPrompt = "db-1x3.db";
	private int warning = JOptionPane.QUESTION_MESSAGE;

	/**
	 * Constructor of StandaloneSetupDialog
	 * 
	 * @param command
	 *            String value which changes the message to the user if they
	 *            have previously entered incorrect setup info.
	 */
	public StandaloneSetupDialog(final String command) {

		if (command.equalsIgnoreCase("error")) {
			message = error;
			warning = JOptionPane.WARNING_MESSAGE;
		}

		dbLocation = (String) JOptionPane.showInputDialog(this, message,
				"URLy Bird (Version 1.3.2)", warning, null, null,
				locationPrompt);

		if (dbLocation == null) {
			System.exit(0);
		} else if (dbLocation.length() > 0) {
			PropertiesFileAccess.setDbLocation(dbLocation);
		} else if (dbLocation.length() == 0) {
			new StandaloneSetupDialog("error");
		}
	}
}