package suncertify.db;

/**
 * This interface is used to add additional methods to ones provided in DBAccess
 * interface
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
	public String[] getFields();

	/**
	 * This method returns the clients cookie
	 * 
	 * @return long containing clients cookie
	 */
	public long getCookie();

	/**
	 * Checks if the passed record is locked or unlocked.
	 * 
	 * @param recNo
	 *            record to check
	 * @return Returns true if the record pass is locked, false otherwise
	 * @throws RecordNotFoundException
	 */
	public boolean isLocked(long recNo) throws RecordNotFoundException;

}
