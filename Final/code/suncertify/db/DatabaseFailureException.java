package suncertify.db;

import java.io.IOException;

/**
 * This exception is used when there is a issue connecting to the database or
 * accessing records.
 * 
 * @author Robbie Byrne
 * 
 */
public class DatabaseFailureException extends IOException {

	private static final long serialVersionUID = 8078761267617868533L;

	/**
	 * Constructor for DatabaseFailureException which takes the cause of the
	 * exception as an input.
	 * 
	 * @param message
	 *            This is the cause of the exception.
	 */
	public DatabaseFailureException(final String message) {
		super(message);
	}

}
