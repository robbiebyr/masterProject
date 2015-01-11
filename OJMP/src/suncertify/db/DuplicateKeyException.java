package suncertify.db;

/**
 * Duplicate Key exception.
 * 
 * @author Robbie Byrne
 * 
 */
public class DuplicateKeyException extends Exception {

	private static final long serialVersionUID = -6689165809485807888L;

	/**
	 * Default constructor for DuplicateKeyException
	 */
	public DuplicateKeyException() {
		super();
	}

	/**
	 * Constructor that takes a String that serves as the exception's
	 * description
	 * 
	 * @param message
	 */
	public DuplicateKeyException(final String message) {

		super(message);
	}
}
