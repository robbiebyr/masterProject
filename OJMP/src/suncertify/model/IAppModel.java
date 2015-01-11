package suncertify.model;

import java.util.List;
import java.util.Map;
import java.util.Observer;

import suncertify.db.RecordNotAvailableException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;

/**
 * This interface defines the methods required by the Model in the application.
 * 
 * @author Robbie Byrne
 * 
 */
public interface IAppModel {

	/**
	 * This method defines how to get all records.
	 * 
	 * @return List of Room objects.
	 */
	public abstract List<HotelRoom> getAllRecords();

	/**
	 * This method defines how to query the Cache for a subset of records based
	 * on criteria.
	 * 
	 * @param name
	 *            String name of hotel.
	 * @param location
	 *            String location of hotel.
	 * @return A Map of record numbers for keys and Room objects for values.
	 */
	public abstract Map<Integer, HotelRoom> queryRecords(String name,
			String location);

	/**
	 * This method is used to obtain the field names of a record.
	 * 
	 * @return String array of field names.
	 */
	public abstract String[] getRoomFeilds();

	/**
	 * This method is used to update the owner of a record.
	 * 
	 * @param recNo
	 *            The record number of the record.
	 * @param owner
	 *            The owner id String to be applied to record.
	 * @throws RecordNotFoundException
	 *             Thrown if the record number does not exist.
	 * @throws RecordNotAvailableException
	 *             Thrown if the record is already booked.
	 * @throws SecurityException
	 *             Thrown if the cookie used to unlock the record is different
	 *             than the cookie generated when the record was locked
	 */
	public abstract void updateRecord(long recNo, String owner, long cookie)
			throws RecordNotFoundException, RecordNotAvailableException,
			SecurityException;

	/**
	 * This method is used to implement the Observer pattern by adding an
	 * Observer.
	 * 
	 * @param observer
	 *            Observer object.
	 */
	public abstract void addObserver(Observer observer);

	/**
	 * This method is used to remove an Observer.
	 * 
	 * @param observer
	 *            Observer object.
	 */
	public abstract void removeObserver(Observer observer);

	/**
	 * This method is used to get the corresponding record number for a
	 * selection in the JTable.
	 * 
	 * @param selection
	 *            JTable selection number.
	 * @return Record number corresponding to selection.
	 */
	public abstract Long getActualIndexMap(long selection);

	/**
	 * This method is used to return total number of records.
	 * 
	 * @return int representing total number of records.
	 */
	public abstract int getTotalNumberRecords();

	/**
	 * Allows the client to get the cookie that locked the record.
	 * 
	 * @return the cookie used to lock a record.
	 */
	public long getCookie();

}