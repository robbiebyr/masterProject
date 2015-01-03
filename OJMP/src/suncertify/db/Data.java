package suncertify.db;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.model.Room;

/**
 * This Data class implements the provided DBMain interface.
 * This class defines all record accessing methods 
 * and also provides a lock and unlock facility on records.
 * @author Robbie Byrne
 *
 */
public class Data implements DBMainExtended {

	private DBFileIO dao;
	private DBCache cache;
	private LockingManager lockingManager;
	private int ownerIndex = 6;
	private Long clientId;

	/**
	 * This is the constructor of the Data class and takes the db file location as input.
	 * @param dbLocation String representing the location of the db file.
	 * @throws DatabaseFailureException This is thrown if there is an issue accessing the db file.
	 */
	public Data(String dbLocation) throws DatabaseFailureException {

		CopyOnWriteArrayList<Room> records;
		String[] fields;
		dao = new DBFileIO(dbLocation);

		try {
			records = dao.getAllRecords();
			fields = dao.getFieldNames();

		} catch (IOException e) {
			throw new DatabaseFailureException("Can't find/read db file");
		}

		cache = new DBCache(records, fields);
		lockingManager = new LockingManager();
	}

	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		return cache.readRecord(recNo).toStrArray();
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		lock(recNo);
		cache.updateRecord(recNo, data[ownerIndex]);
		unlock(recNo);
	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		lock(recNo);
		cache.deleteRecord(recNo);
		unlock(recNo);
	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		return cache.find(criteria);
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		return cache.addRecord(Room.strToRoom(data));
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException 
	{	
		if(cache.containsRecord(recNo))
		{
			Long clientId = lockingManager.lock(recNo);
			setClientId(clientId);
		}
		else
		{
			throw new RecordNotFoundException();
		}
	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		if (cache.containsRecord(recNo))
		{
			lockingManager.unlock(recNo, getClientId());	
		}
	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		return lockingManager.isLocked(recNo);
	}
	
	/**
	 * This method returns all records from the Cache
	 * @return A List of Room objects.
	 */
	public List<Room> getAllRecords() {
		return cache.getAllRecords();
	}

	/**
	 * This method returns the record field names from the Cache.
	 * @return String array of field names.
	 */
	public String[] getFields() {
		return cache.getFieldNames();
	}
	
	/**
	 * This method checks if a record is already booked.
	 * @param recNo Number of record.
	 * @return True if the record is booked, false otherwise.
	 * @throws RecordNotFoundException Thrown if the record number could not be found.
	 */
	public boolean alreadyBooked(int recNo) throws RecordNotFoundException
	{
		String [] room = read(recNo);
		return room[ownerIndex].trim().length() > 0;
	}
	
	private void setClientId(Long clientId)
	{
		this.clientId = clientId;
	}
	
	private Long getClientId()
	{
		return this.clientId;
	}
}
