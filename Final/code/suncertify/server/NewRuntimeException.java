package suncertify.server;

/**
 * This exception was created to be thrown by the DataProxy class as an
 * replacement for RemoteException.
 * 
 * @author Robbie Byrne
 * 
 */
public class NewRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -4559666761635102468L;
	private String message;

	/**
	 * Default constructor
	 */
	public NewRuntimeException() {
	}

	/**
	 * Constructor which takes in a message containing the reason for the
	 * exception.
	 * 
	 * @param message
	 *            describes the reason for exception.
	 */
	public NewRuntimeException(final String message) {

		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
