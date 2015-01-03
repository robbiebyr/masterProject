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
 * @author Robbie Byrne
 *
 */
public class ServerRMIDialog extends JFrame {

	private static final long serialVersionUID = 7123856555620056822L;

	private String dbLocation = "db-1x2.db";
	private String hostname = "localhost";
	private String port = "1099";

	private JPanel parentPanel = new JPanel();	
	private JPanel inputPanel = new JPanel();
	private JPanel labelsPanel = new JPanel();

	private JTextField dbLocationFeild = new JTextField();
	private JTextField portFeild = new JTextField();
	private JTextField hostnameFeild = new JTextField();

	private JLabel dbLocationLabel = new JLabel("DB Location");
	private JLabel portLabel = new JLabel("Port");
	private JLabel hostnameLabel = new JLabel("Hostname");
	
	private JButton startButton = new JButton("START");
	private JButton stopButton = new JButton("STOP");
	
	private JLabel messageLabel = new JLabel("");
	private String OFFLINE = "Server offline.";
	
	private Border grayBorder = BorderFactory.createLineBorder(Color.gray);
	
	private JFrame masterFrame = new JFrame();
	
	private Dimension msgLabelSize = new Dimension(250, 20);
	private Dimension labelsSize = new Dimension(120, 20);
	
	boolean result = false;
	
	/**
	 * No argument constructor of ServerRMIDialog
	 */
	public ServerRMIDialog()
	{
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
			 
            public void actionPerformed(ActionEvent e)
            {
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
	 * Settings which were entered by the user are written to the properties file.
	 */
	public void writeSettingsToPropertiesFile()
	{
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
	 * @param message Message to set.
	 */
	public void setMessageLabel(String message)
	{
		messageLabel.setText(message);
	}
	
	/**
	 * Adds a ActionListener to the start button of the ServerRMIDialog GUI.
	 * @param buttonListener ActionListener.
	 */
	public void addStartButtonListener(ActionListener buttonListener) {
		startButton.addActionListener(buttonListener);
	}
	
	private void writePropertiesToFile()
	{
		PropertiesFileAccess.setDbLocation(dbLocation);
		PropertiesFileAccess.setPort(port);
		PropertiesFileAccess.setHostname(hostname);
	}
	
	private void reset()
	{	
		stopButton.setEnabled(false);
    	startButton.setEnabled(true);
    	dbLocationFeild.setEnabled(true);
    	hostnameFeild.setEnabled(true);
    	portFeild.setEnabled(true);
    	messageLabel.setText(OFFLINE);
	}
}