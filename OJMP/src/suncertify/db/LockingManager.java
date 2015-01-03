package suncertify.db;

import java.util.HashMap;
import java.util.Map;

/**
 * LockingManager class is used to lock individual records in the Cache to prevent data corruption
 * by multiple clients during a booking. 
 * @author Robbie Byrne
 *
 */
public class LockingManager {

	private Map<Integer, Long> lockTable = new HashMap<Integer, Long>();
	
	/**
	 * This lock method is used to lock a record of the Cache so only one client can operate on a record at a time.
	 * @param recNo The record number of the record a client wishes to lock.
	 * @return A cookie which will be used to identify the client as the owner of the lock on a record.
	 * @throws RecordNotFoundException When an InterruptedException occurs on the waiting thread, a
	 * RecordNotFoundException will be thrown instead.
	 */
	public synchronized Long lock(int recNo) throws RecordNotFoundException {
		
		Long clientId = System.nanoTime();
		
		try 
		{
			while (isLocked(recNo)) 
			{
				wait();
			}
			
			lockTable.put(recNo, clientId);
		} 
		catch (InterruptedException e) 
		{
			throw new RecordNotFoundException("Record could not be locked, try again or book another record.");
		} 
		
		return clientId;
	}

	/**
	 * This method is used to unlock a record by the client who originally locked it.
	 * @param recNo The record number of the record a client wishes to unlock.
	 * @param clientId A cookie which will be used to identify the client as the owner of the lock on a record.
	 * @throws RecordNotFoundException This exception is thrown when a client tries to unlock a record it does not have locked.
	 */
	public synchronized void unlock(int recNo, Long clientId) throws RecordNotFoundException {
		
		if (isLocked(recNo))
		{	
			if (lockTable.get(recNo) == clientId)
			{
				lockTable.remove(recNo);
				notifyAll();
			}
			else
			{
				throw new RecordNotFoundException("Client does not have permission to lock this record.");
			}
		}
	}

	/** This method is used to determine if a record is currently locked.
	 * @param recNo The record number of the record in question.
	 * @return Boolean corresponding to the records current status.
	 */
	public boolean isLocked(int recNo) {
		return lockTable.containsKey(recNo);
	}
}