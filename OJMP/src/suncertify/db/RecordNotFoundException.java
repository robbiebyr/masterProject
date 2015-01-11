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
	 * Zero argument constructor
	 */
	public RecordNotFoundException() {
	}

	/**
	 * Second constructor
	 * 
	 * @param message
	 *            describes the reason for exception.
	 */
	public RecordNotFoundException(String message) {

		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
