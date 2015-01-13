package suncertify.db;

/**
 * This exception is thrown went a record does not exist or is marked as deleted
 * in the database file.
 * 
 * @author Robbie Byrne
 * 
 */
public class RecordNotFoundException extends Exception {

	private static final long serialVersionUID = -1113819940024152572L;
	private String message;

	/**
	 * Default constructor
	 */
	public RecordNotFoundException() {
	}

	/**
	 * Constructor which takes in a message containing the reason for the
	 * exception.
	 * 
	 * @param message
	 *            describes the reason for exception.
	 */
	public RecordNotFoundException(final String message) {

		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
