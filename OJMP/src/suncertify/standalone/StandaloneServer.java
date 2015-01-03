package suncertify.standalone;

import javax.swing.SwingUtilities;

import suncertify.common.PropertiesFileAccess;
import suncertify.controller.AppController;
import suncertify.db.Data;
import suncertify.db.DatabaseFailureException;
import suncertify.model.AppModel;
import suncertify.model.AppModelInterface;
import suncertify.server.ServerClosingHook;
import suncertify.view.AppView;

/**
 * This class manages the StandaloneServer. It is run by providing the "alone"
 * argument to the main jar.
 * 
 * @author Robbie Byrne
 * 
 */
public class StandaloneServer {

	/**
	 * This is the constructor of the StandaloneServer class. It creates the
	 * dialog to read in the db file location from the user and if it is
	 * correct, the MVC architecture is created and standalone GUI is displayed.
	 * If the details are incorrect, the user is given another chance to enter
	 * the details or exit the program.
	 */
	public StandaloneServer() {
		new StandaloneSetupDialog("init");
		AppModelInterface model = null;

		while (model == null) {
			try {
				Data data = new Data(PropertiesFileAccess.getDbLocation());
				model = new AppModel(data);
				ServerClosingHook serverFinalOps = new ServerClosingHook();
				serverFinalOps.attachHook();
			} catch (DatabaseFailureException e) {

				new StandaloneSetupDialog("error");
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