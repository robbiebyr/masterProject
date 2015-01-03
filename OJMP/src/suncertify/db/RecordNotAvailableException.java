package suncertify.db;

/**
 * This exception is thrown when a record was booked by another client and is no longer available.
 * @author Robbie Byrne
 *
 */
public class RecordNotAvailableException extends Exception {

	private static final long serialVersionUID = 8156568899362343831L;
	private String message;

	/**
	 * Zero argument constructor
	 */
	public RecordNotAvailableException() {
	}

	/**
	 * Second constructor
	 * @param message this message describes the exception being thrown. 
	 * Record was booked by another client and is no longer available.
	 */
	public RecordNotAvailableException(String message) {

		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}
}
