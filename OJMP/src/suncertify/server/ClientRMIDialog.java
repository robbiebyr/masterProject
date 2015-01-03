package suncertify.server;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class is used to get RMI details from the user in order to create the required server.
 * A JFrame is displayed with three fields corresponding to db file location, hostname and port of the server.
 * @author Robbie Byrne
 *
 */
public class ClientRMIDialog extends JFrame {

	private static final long serialVersionUID = 1044429952328953745L;
	
	String message = "Please enter server details";
	String errorMessage = "DB Error, Please enter server details again";
	String error = "Please try again or press Cancel to exit: ";
	String hostname = "localhost";
	String port = "1099";
	
	int warning = JOptionPane.QUESTION_MESSAGE;

	JPanel parentPanel = new JPanel();
	JPanel inputPanel = new JPanel();
	JPanel labelsPanel = new JPanel();

	JTextField portFeild = new JTextField();
	JTextField hostnameFeild = new JTextField();

	JLabel portLabel = new JLabel("Port");
	JLabel hostnameLabel = new JLabel("Hostname");
	JLabel messageLabel = new JLabel();

	/**
	 * This is the constructor of the ClientRMIDialog class.
	 * @param command This string is used to determine if an error message should be displayed in the dialog.
	 */
	public ClientRMIDialog(String command) {

		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.PAGE_AXIS));

		hostnameFeild.setPreferredSize(new Dimension(120, 20));
		hostnameFeild.setText(hostname);

		portFeild.setPreferredSize(new Dimension(120, 20));
		portFeild.setText(port);

		labelsPanel.add(hostnameLabel);
		labelsPanel.add(portLabel);

		inputPanel.add(hostnameFeild);
		inputPanel.add(portFeild);
		inputPanel.add(messageLabel);

		parentPanel.add(labelsPanel);
		parentPanel.add(inputPanel);

		if (command.equalsIgnoreCase("error")) {
			message = error;
			messageLabel.setText(error);
		}

		int result = JOptionPane.showConfirmDialog(this, parentPanel,
				message, JOptionPane.OK_CANCEL_OPTION);
		
		if (result ==  JOptionPane.CANCEL_OPTION)
		{
			System.exit(0);
		}
		
		port = portFeild.getText();
		hostname = hostnameFeild.getText();		
	}

	/**
	 * This method is used to return the hostname entered by the user.
	 * @return hostname String.
	 */
	public String getHostname() {
		return hostname;
	}

	/** 
	 * This method is used to return the port number entered by the user.
	 * @return port number String.
	 */
	public String getPort() {
		return port;
	}
}