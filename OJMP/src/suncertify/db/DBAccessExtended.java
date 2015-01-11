package suncertify.db;

/**
 * This class is used to additional methods to ones provided in DBMain interface
 * 
 * @author Robbie Byrne
 * 
 */
public interface DBAccessExtended extends DBAccess {

	/**
	 * This method returns the fields of a record
	 * 
	 * @return String array of fields.
	 */
	String[] getFields();

	long getCookie();

	boolean isLocked(long recNo) throws RecordNotFoundException;

}
