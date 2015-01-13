package suncertify.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import suncertify.common.PropertiesFileAccess;

/**
 * ServerRMIDialog is used to obtain server settings from end user.
 * 
 * @author Robbie Byrne
 * 
 */
public class ServerRMIDialog extends JFrame {

	private static final long serialVersionUID = -5778088721825879637L;
	private String dbLocation = "db-1x3.db";
	private String hostname = "localhost";
	private String port = "1099";

	private final JPanel parentPanel = new JPanel();
	private final JPanel inputPanel = new JPanel();
	private final JPanel labelsPanel = new JPanel();

	private final JTextField dbLocationFeild = new JTextField();
	private final JTextField portFeild = new JTextField();
	private final JTextField hostnameFeild = new JTextField();

	private final JLabel dbLocationLabel = new JLabel("DB Location");
	private final JLabel portLabel = new JLabel("Port");
	private final JLabel hostnameLabel = new JLabel("Hostname");

	private final JButton startButton = new JButton("START");
	private final JButton stopButton = new JButton("STOP");

	private final JLabel messageLabel = new JLabel("");
	private final String OFFLINE = "Server offline.";

	private final Border grayBorder = BorderFactory
			.createLineBorder(Color.gray);

	private final JFrame masterFrame = new JFrame();

	private final Dimension msgLabelSize = new Dimension(250, 20);
	private final Dimension labelsSize = new Dimension(120, 20);

	boolean result = false;

	/**
	 * No argument constructor of ServerRMIDialog
	 */
	public ServerRMIDialog() {
		reset();

		masterFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		masterFrame.setTitle("Please enter server details:");
		masterFrame.setSize(280, 155);
		masterFrame.setResizable(false);
		masterFrame.setLocationRelativeTo(null);

		messageLabel.setBorder(grayBorder);
		messageLabel.setMinimumSize(msgLabelSize);
		messageLabel.setPreferredSize(msgLabelSize);
		messageLabel.setMaximumSize(msgLabelSize);
		messageLabel.setText(OFFLINE);

		startButton.setEnabled(true);
		stopButton.setEnabled(false);

		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				reset();
				System.exit(0);
			}
		});

		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.PAGE_AXIS));

		dbLocationFeild.setPreferredSize(labelsSize);
		dbLocationFeild.setText(dbLocation);

		hostnameFeild.setPreferredSize(labelsSize);
		hostnameFeild.setText(hostname);

		portFeild.setPreferredSize(labelsSize);
		portFeild.setText(port);

		labelsPanel.add(dbLocationLabel);
		labelsPanel.add(hostnameLabel);
		labelsPanel.add(portLabel);

		inputPanel.add(dbLocationFeild);
		inputPanel.add(hostnameFeild);
		inputPanel.add(portFeild);

		parentPanel.add(labelsPanel);
		parentPanel.add(inputPanel);
		parentPanel.add(startButton);
		parentPanel.add(stopButton);
		parentPanel.add(messageLabel);

		masterFrame.add(parentPanel);
		masterFrame.setVisible(true);
	}

	/**
	 * Write the setting provide by user to the properties file.
	 */
	public void writeSettingsToPropertiesFile() {
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		dbLocationFeild.setEnabled(false);
		hostnameFeild.setEnabled(false);
		portFeild.setEnabled(false);

		dbLocation = dbLocationFeild.getText();
		port = portFeild.getText();
		hostname = hostnameFeild.getText();

		writePropertiesToFile();
	}

	/**
	 * Updates the message label on the ServerRMIDialog GUI.
	 * 
	 * @param message
	 *            Message to set.
	 */
	public void setMessageLabel(final String message) {
		messageLabel.setText(message);
	}

	/**
	 * Adds a ActionListener to the start button of the ServerRMIDialog GUI.
	 * 
	 * @param buttonListener
	 *            ActionListener.
	 */
	public void addStartButtonListener(final ActionListener buttonListener) {
		startButton.addActionListener(buttonListener);
	}

	private void writePropertiesToFile() {
		PropertiesFileAccess.setDbLocation(dbLocation);
		PropertiesFileAccess.setPort(port);
		PropertiesFileAccess.setHostname(hostname);
	}

	private void reset() {
		stopButton.setEnabled(false);
		startButton.setEnabled(true);
		dbLocationFeild.setEnabled(true);
		hostnameFeild.setEnabled(true);
		portFeild.setEnabled(true);
		messageLabel.setText(OFFLINE);
	}
}