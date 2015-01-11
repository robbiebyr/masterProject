package driver;

import suncertify.server.NetworkedClient;
import suncertify.server.NetworkedServer;
import suncertify.standalone.StandaloneServer;

/**
 * This is the entry class to the application. It takes as input the mode flag
 * which the jar is required to run. The mode flag must be either "server",
 * indicating the server program must run, "alone", indicating standalone mode,
 * or left out entirely, in which case the network client and gui must run.
 * 
 * @author Robbie Byrne
 * 
 */
public class Driver {

	/**
	 * This is the main method of the driver class.
	 * 
	 * @param args
	 *            This string array must contain only one element which must be
	 *            "server", "alone" or blank.
	 */
	public static void main(final String[] args) {

		String mode = "";

		if (args.length > 1) {
			System.out
					.println("Please enter either \"alone\" for Standalone mode, \"server\" to run the server, or leave blank for networked client");
			System.exit(0);
		}

		if (args.length != 0) {
			mode = args[0];
		}

		if (mode.trim().equalsIgnoreCase("server")) {
			new NetworkedServer();
		} else if (mode.trim().equalsIgnoreCase("alone")) {
			new StandaloneServer();
		} else if (mode.trim().equalsIgnoreCase("")) {
			new NetworkedClient();
		}
	}
}
