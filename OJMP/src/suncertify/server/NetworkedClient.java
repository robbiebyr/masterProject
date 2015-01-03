package suncertify.server;

import java.rmi.Naming;

import javax.swing.SwingUtilities;

import suncertify.controller.AppController;
import suncertify.db.DBMainExtended;
import suncertify.model.AppModel;
import suncertify.model.AppModelInterface;
import suncertify.view.AppView;

/**
 * This class manages the networked client gui.
 * It is run by providing no arguments to the main jar.
 * @author Robbie Byrne
 *
 */
public class NetworkedClient {

	private DBMainRemote remoteDBAccess;
	private DBMainExtended data;

	/**
	 * This is the constructor of the NetworkedClient class.
	 * It creates the dialog to read in RMI server details from the user and if these are correct, 
	 * the MVC architecture is created and the networked client gui is displayed. 
	 * If the details are incorrect, the user is given another chance to enter the details or exit the program.
	 */
	public NetworkedClient() {

		ClientRMIDialog dialog = new ClientRMIDialog("init");
		AppModelInterface model = null;

		while (model == null) 
		{
			try 
			{
				remoteDBAccess = (DBMainRemote) Naming.lookup("rmi://"
						+ dialog.getHostname() + ":" + dialog.getPort()
						+ "/Server");
				
				data = new DataProxy(remoteDBAccess);
				
				model = new AppModel(data);
				
			} 
			catch (Exception e) 
			{
				/*
				 * User is given another chance to enter correct details.
				 */
				dialog = new ClientRMIDialog("error");
			}
		}
		
		final AppView view = new AppView(model);
		new AppController(model, view);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				view.createAndShowGUI();
				view.populateTableAllRecords();
			}
		});
	}
}