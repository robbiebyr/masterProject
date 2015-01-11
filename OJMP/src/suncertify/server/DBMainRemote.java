package suncertify.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;
import suncertify.model.Room;

/**
 * This interface is identical to the DBMain interface except all the methods
 * also throw RemoteException.
 * 
 * @author Robbie Byrne
 * 
 */
public interface DBMainRemote extends Remote {
	//
	/**
	 * This method reads a record from the Cache.
	 * 
	 * @param recNo
	 *            Number of record
	 * @return Returns an array where each element is a record value.
	 * @throws RecordNotFoundException
	 *             Thrown if the record number could not be found.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public String[] readRecord(long recNo) throws RecordNotFoundException,
			RemoteException;

	/**
	 * This method modifies the fields of a record. The new value for field n
	 * appears in data[n].
	 * 
	 * @param recNo
	 *            Number of record
	 * @param data
	 *            String array containing field values of a record.
	 * @throws RecordNotFoundException
	 *             Thrown if the record number could not be found.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public void updateRecord(long recNo, String[] data, long cookie)
			throws RecordNotFoundException, RemoteException, SecurityException;

	/**
	 * This method deletes a record from the Cache
	 * 
	 * @param recNo
	 *            Number of record
	 * @throws RecordNotFoundException
	 *             Thrown if the record number could not be found.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public void deleteRecord(long recNo, long cookie)
			throws RecordNotFoundException, RemoteException;

	/**
	 * This method returns an int array corresponding to records which match the
	 * search criteria based on name and location.
	 * 
	 * @param criteria
	 *            String array containing search criteria.
	 * @return int array of record numbers.
	 * @throws RecordNotFoundException
	 *             Thrown if the record number could not be found.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public long[] findByCriteria(String[] criteria) throws RemoteException;

	/**
	 * This method creates a new record in the Cache.
	 * 
	 * @param data
	 *            String array containing field values of a record.
	 * @return Record number of new record.
	 * @throws DuplicateKeyException
	 *             Never thrown in this application.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public long createRecord(String[] data) throws DuplicateKeyException,
			RemoteException;

	/**
	 * This method locks a record so that it can only be updated or deleted by
	 * this client.
	 * 
	 * @param recNo
	 *            Number of record.
	 * @throws RecordNotFoundException
	 *             Thrown if the record number could not be found.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public long lockRecord(long recNo) throws RecordNotFoundException,
			RemoteException;

	/**
	 * This method releases the lock on a record.
	 * 
	 * @param recNo
	 *            Number of record.
	 * @throws RecordNotFoundException
	 *             Thrown if the record number could not be found.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public void unlock(long recNo, long cookie) throws SecurityException,
			RemoteException;

	/**
	 * This method determines if a record is currently locked.
	 * 
	 * @param recNo
	 *            Number of record.
	 * @return True if the record is locked, false otherwise.
	 * @throws RecordNotFoundException
	 *             Thrown if the record number could not be found.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public boolean isLocked(long recNo) throws RecordNotFoundException,
			RemoteException;

	/**
	 * This method returns all the Room records from the Cache.
	 * 
	 * @return List of Room records.
	 * @throws RemoteException
	 */
	public List<Room> getAllRecords() throws RemoteException;

	/**
	 * This methods returns all the field names of a Room record.
	 * 
	 * @return String array of field names.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public String[] getFields() throws RemoteException;

	/**
	 * This method checks if a record is already booked.
	 * 
	 * @param recNo
	 *            Number of record.
	 * @return True if the record is booked, false otherwise.
	 * @throws RecordNotFoundException
	 *             Thrown if the record number could not be found.
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 */
	public boolean alreadyBooked(long recNo) throws RecordNotFoundException,
			RemoteException;
}