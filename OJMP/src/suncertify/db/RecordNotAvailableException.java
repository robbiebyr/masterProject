package suncertify.db;

/**
 * This exception is thrown went a record is not available
 * 
 * @author Robbie Byrne
 * 
 */
public class RecordNotAvailableException extends Exception {

	private static final long serialVersionUID = 8156568899362343831L;
	private String message;

	/**
	 * Default constructor
	 */
	public RecordNotAvailableException() {
	}

	/**
	 * Constructor which takes in a message containing the reason for the
	 * exception.
	 * 
	 * @param message
	 *            describes the reason for exception.
	 */
	public RecordNotAvailableException(final String message) {

		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
