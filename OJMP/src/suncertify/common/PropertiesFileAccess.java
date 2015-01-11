package suncertify.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class is used in server and standalone mode to persist and also read the
 * suncertify.properties file which is located in the current working directory
 * 
 * @author Robbie Byrne
 * 
 */
public class PropertiesFileAccess {

	static Properties properties = new Properties();;
	static String fileLocation = "suncertify.properties";

	private static String loadPropertyFromFile(String property) {

		String result = null;
		String dbLocation = "";
		String hostname = "";
		String port = "";

		/**
		 * try-with-resources Statement automatically closes resource so there
		 * isn't any resource leaks and no need for a finally block.
		 */

		try (FileInputStream in = new FileInputStream(fileLocation)) {
			properties.load(in);

			dbLocation = properties.getProperty("dbLocation");
			hostname = properties.getProperty("hostname");
			port = properties.getProperty("port");

		} catch (IOException e) {
			System.out.println("Could not read properties, closing program.");
			System.exit(1);
		}

		switch (property.toLowerCase()) {
		case "dblocation":
			result = dbLocation;
			break;
		case "hostname":
			result = hostname;
			break;
		case "port":
			result = port;
			break;
		}

		return result;
	}

	private static void savePropertiesToFile(String property, String value) {
		try (FileOutputStream out = new FileOutputStream(fileLocation)) {

			switch (property.toLowerCase()) {
			case "dblocation":
				properties.setProperty("dbLocation", value);
				break;
			case "hostname":
				properties.setProperty("hostname", value);
				break;
			case "port":
				properties.setProperty("port", value);
				break;
			}

			properties.store(out, property + " updated.");

		} catch (IOException e) {
			System.out
					.println("Could not write to properties file, closing program.");
			System.exit(1);
		}
	}

	/**
	 * Sets the db file location to the properties file.
	 * 
	 * @param dbLocation
	 *            The db file location
	 */
	public static void setDbLocation(String dbLocation) {
		savePropertiesToFile("dblocation", dbLocation);
	}

	/**
	 * Sets the port number to the properties file.
	 * 
	 * @param port
	 *            String port number
	 */
	public static void setPort(String port) {
		savePropertiesToFile("port", port);
	}

	/**
	 * Sets the hostname to the properties file.
	 * 
	 * @param hostname
	 *            String hostname
	 */
	public static void setHostname(String hostname) {
		savePropertiesToFile("hostname", hostname);
	}

	/**
	 * Gets the db file location from the properties file.
	 * 
	 * @return The db file location
	 */
	public static String getDbLocation() {
		return loadPropertyFromFile("dbLocation");
	}

	/**
	 * Gets the port number from the properties file.
	 * 
	 * @return String port number
	 */
	public static String getPort() {
		return loadPropertyFromFile("port");
	}

	/**
	 * Gets the hostname from the properties file.
	 * 
	 * @return The hostname.
	 */
	public static String getHostname() {
		return loadPropertyFromFile("hostname");
	}
}
