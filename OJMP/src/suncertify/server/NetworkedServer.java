package suncertify.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import suncertify.common.PropertiesFileAccess;
import suncertify.db.DatabaseFailureException;

/**
 * This class manages the setting up of the Server in networked mode.
 * 
 * @author Robbie Byrne
 * 
 */
public class NetworkedServer {

	private static String port = "";
	private static String dbLocation = "";
	private static String hostname = "";
	private static DataRemote dbRemote;
	private static ServerRMIDialog dialog;
	private static boolean portCreated = false;
	private static int MAX_PORT_NUMBER = 65535;

	/**
	 * No argument constructor of NetworkedServer class. It creates a dialog to
	 * request server info from user.
	 * 
	 */
	public NetworkedServer() {
		dialog = new ServerRMIDialog();
		dialog.addStartButtonListener(new StartButtonListener());
	}

	class StartButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			dialog.writeSettingsToPropertiesFile();

			try {
				if (!validServerSettings()) {
					throw new DatabaseFailureException(
							"Problem with server info");
				}

				dialog.setMessageLabel("Starting...");
				int intPort = Integer.parseInt(port);
				dbRemote = new DataRemote(dbLocation);
				createRegistry(intPort);
				Naming.rebind("rmi://" + hostname + "/Server", dbRemote);

				ServerClosingHook serverFinalOps = new ServerClosingHook();
				serverFinalOps.attachHook();
				dialog.setMessageLabel("Server Online");

			} catch (RemoteException | MalformedURLException
					| DatabaseFailureException | NumberFormatException e) {
				dialog.setMessageLabel("Problem with Server Settings, please try again or close.");
				dbRemote = null;
				portCreated = false;
			}
		}
	}

	private static void createRegistry(int intPort) throws RemoteException {
		if (!portCreated) {
			LocateRegistry.createRegistry(intPort);
		}
		portCreated = true;
	}

	/*
	 * Following methods are all to do with validating user input for setting up
	 * the server.
	 */

	private static boolean validServerSettings() {

		dbLocation = PropertiesFileAccess.getDbLocation();
		hostname = PropertiesFileAccess.getHostname();
		port = PropertiesFileAccess.getPort();

		return noEmptyFields() && dbFilePresent(dbLocation) && validPort(port);
	}

	private static boolean validPort(String port) {
		return (Integer.parseInt(port) > 0 && Integer.parseInt(port) < MAX_PORT_NUMBER);
	}

	private static boolean dbFilePresent(String dbLocation) {
		File file = new File(dbLocation);
		return (file.exists() && !file.isDirectory());
	}

	private static boolean noEmptyFields() {
		boolean result = true;
		if (dbLocation.trim().length() == 0 || hostname.trim().length() == 0
				|| port.trim().length() == 0) {
			result = false;
		}
		return result;
	}
}