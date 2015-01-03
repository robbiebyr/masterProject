package suncertify.db;

/**
 * This class is used to additional methods to ones provided in DBMain interface 
 * @author Robbie Byrne
 *
 */
public interface DBMainExtended extends DBMain {
	
	/**
	 * This method returns the fields of a record
	 * @return String array of fields.
	 */
	public String [] getFields();

}
