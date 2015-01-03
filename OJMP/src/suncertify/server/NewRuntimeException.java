package suncertify.server;


/**
 * This exception was created to be thrown by the DataProxy class as an replacement for RemoteException.
 * @author Robbie Byrne
 *
 */
public class NewRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = -4559666761635102468L;

	private String message;
	
	/**
	 * Zero argument constructor
	 */
	public NewRuntimeException() {
	}

	/**
	 * Second constructor
	 * @param message describes the reason for exception.
	 */
	public NewRuntimeException(String message) {

		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
