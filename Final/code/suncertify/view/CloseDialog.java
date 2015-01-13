package suncertify.view;

import javax.swing.JOptionPane;

/**
 * This class gives end user a Close or cancel close option via pop up dialog.
 * 
 * @author Robbie Byrne
 * 
 */
public class CloseDialog {

	/**
	 * This method returns the end users decision to either close or cancel the
	 * close operation.
	 * 
	 * @return int representing yes/no.
	 */
	public int getResponce() {
		return JOptionPane.showConfirmDialog(null,
				"Are you sure you want to close?", "Close?",
				JOptionPane.YES_NO_OPTION);
	}

}
