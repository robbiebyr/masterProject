package suncertify.server;

import java.rmi.Naming;

import javax.swing.SwingUtilities;

import suncertify.controller.AppController;
import suncertify.db.DBAccessExtended;
import suncertify.model.AppModel;
import suncertify.model.IAppModel;
import suncertify.view.AppView;

/**
 * This class manages the networked client gui. It is run by providing no
 * arguments to the main jar.
 * 
 * @author Robbie Byrne
 * 
 */
public class NetworkedClient {

	private DBAccessRemote remoteDBAccess;
	private DBAccessExtended data;

	/**
	 * This is the constructor of the NetworkedClient class. It creates the
	 * dialog to read in RMI server details from the user and if they are
	 * correct, the MVC architecture is created and the networked client gui is
	 * displayed. If the details are incorrect, the user is given another chance
	 * to enter the details or exit the program.
	 */
	public NetworkedClient() {

		ClientRMIDialog dialog = new ClientRMIDialog("init");
		IAppModel model = null;

		while (model == null) {
			try {
				remoteDBAccess = (DBAccessRemote) Naming.lookup("rmi://"
						+ dialog.getHostname() + ":" + dialog.getPort()
						+ "/Server");

				data = new DataProxy(remoteDBAccess);

				model = new AppModel(data);

			} catch (final Exception e) {
				dialog = new ClientRMIDialog("error");
			}
		}

		final AppView view = new AppView(model);
		new AppController(model, view);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				view.createAndShowGUI();
				view.populateTableAllRecords();
			}
		});
	}
}