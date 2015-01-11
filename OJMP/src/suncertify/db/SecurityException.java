package suncertify.db;

public class SecurityException extends Exception {
	

	private static final long serialVersionUID = 8944297262620274042L;
	private String message;
	
	/**
	 * Zero argument constructor
	 */
	public SecurityException() {
	}

	/**
	 * Second constructor
	 * @param message describes the reason for exception.
	 */
	public SecurityException(String message) {

		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
