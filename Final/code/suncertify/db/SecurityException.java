package suncertify.db;

/**
 * Security exception thrown if incorrect cookie used to try unlock a locked
 * record.
 * 
 * @author Robbie
 * 
 */
public class SecurityException extends Exception {

	private static final long serialVersionUID = 8944297262620274042L;
	private String message;

	/**
	 * Default constructor
	 */
	public SecurityException() {
	}

	/**
	 * Constructor which takes in a message containing the reason for the
	 * exception.
	 * 
	 * @param message
	 *            describes the reason for exception.
	 */
	public SecurityException(final String message) {

		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
