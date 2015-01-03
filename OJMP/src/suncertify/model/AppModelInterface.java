package suncertify.model;

import java.util.List;
import java.util.Map;
import java.util.Observer;

import suncertify.db.RecordNotAvailableException;
import suncertify.db.RecordNotFoundException;

/**
 * This interface defines the methods required of the Model in this application.
 * @author Robbie Byrne
 *
 */
public interface AppModelInterface {

	/**
	 * This method defines how to get all records.
	 * @return List of Room objects.
	 */
	public abstract List<Room> getAllRecords();

	/**
	 * This method defines how to query the Cache for a subset of records based on criteria.
	 * @param name String name of hotel.
	 * @param location String location of hotel.
	 * @return A Map of record numbers for keys and Room objects for values.
	 */
	public abstract Map<Integer, Room> queryRecords(String name, String location);

	/**
	 * This method is used to obtain the field names of a record.
	 * @return String array of field names.
	 */
	public abstract String[] getRoomFeilds();

	/**
	 * This method is used to update the owner of a record.
	 * @param recNo The record number of the record.
	 * @param owner The owner id String to be applied to record.
	 * @throws RecordNotFoundException Thrown if the record number does not exist.
	 * @throws RecordNotAvailableException Thrown if the record is already booked.
	 */
	public abstract void updateRecord(int recNo, String owner)
			throws RecordNotFoundException, RecordNotAvailableException;

	/**
	 * This method is used to implement the Observer pattern by adding an Observer.
	 * @param observer Observer object.
	 */
	public abstract void addObserver(Observer observer);

	/**
	 * This method is used to remove an Observer.
	 * @param observer Observer object.
	 */
	public abstract void removeObserver(Observer observer);

	/**
	 * This method is used to get the corresponding record number for a selection in the JTable.
	 * @param selection JTable selection number.
	 * @return Record number corresponding to selection.
	 */
	public abstract Integer getActualIndexMap(int selection);

	/** This method is used to return total number of records.
	 * @return int representing total number of records.
	 */
	public abstract int getTotalNumberRecords();

}