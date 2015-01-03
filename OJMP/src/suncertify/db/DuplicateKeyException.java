package suncertify.db;

/**
 * Unimplemented exception. 
 * @author Robbie Byrne
 *
 */
public class DuplicateKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 101L;

	/**
	 *  Zero argument constructor for DuplicateKeyException
	 */
	public DuplicateKeyException() {
		super();
	}

	/**
	 *  Constructor that takes a String that serves as the exception's description
	 * @param message
	 */
	public DuplicateKeyException(String message) {

		super(message);
	}
}
