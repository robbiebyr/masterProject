package suncertify.db;

import java.io.IOException;

/**
 * This exception is used when there is a issue accessing or creating a connection to the DB file.
 * @author Robbie Byrne
 *
 */
public class DatabaseFailureException extends IOException {
	
	private static final long serialVersionUID = 8078761267617868533L;

	/**
	 * Constructor for DatabaseFailureException which takes a String message as input.
	 * @param message This describes the cause of the exception.
	 */
	public DatabaseFailureException(String message)
	{
		super(message);
	}

}
