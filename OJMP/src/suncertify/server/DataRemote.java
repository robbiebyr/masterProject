package suncertify.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.db.DBCache;
import suncertify.db.DatabaseFailureException;
import suncertify.db.DuplicateKeyException;
import suncertify.db.LockingManager;
import suncertify.db.DBFileIO;
import suncertify.db.RecordNotFoundException;
import suncertify.model.Room;


/**
 * This class is used by the client to send requests to the server over RMI.
 * @author Robbie Byrne
 *
 */
public class DataRemote extends UnicastRemoteObject implements
		DBMainRemote {
	
	private static final long serialVersionUID = -8113387190165778794L;
	private DBFileIO dao;
	private DBCache cache;
	private LockingManager lockingManager;
	private int ownerIndex = 6;
	private Long clientId;

	/**
	 * The constructor for the DataRemote class takes a db file location as its only input.
	 * @param dbLocation Db file location as a String
	 * @throws RemoteException Thrown if there is a client-server network issue.
	 * @throws DatabaseFailureException This is thrown if there is an issue accessing the db file.
	 */
	public DataRemote(String dbLocation) throws RemoteException,
			DatabaseFailureException {
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

	public List<Room> getAllRecords() {
		return cache.getAllRecords();
	}

	public String[] getFields() {
		return cache.getFieldNames();
	}

	@Override
	public String[] read(int recNo) throws RecordNotFoundException,
			RemoteException {
		return cache.readRecord(recNo).toStrArray();
	}

	@Override
	public void update(int recNo, String[] data)
			throws RecordNotFoundException, RemoteException {
		lock(recNo);
		cache.updateRecord(recNo, data[ownerIndex]);
		unlock(recNo);
	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException,
			RemoteException {
		cache.deleteRecord(recNo);
	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException,
			RemoteException {
		return cache.find(criteria);
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException,
			RemoteException {
		return cache.addRecord(Room.strToRoom(data));
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException, RemoteException
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
	public void unlock(int recNo) throws RecordNotFoundException, RemoteException {
		if (cache.containsRecord(recNo))
		{
			lockingManager.unlock(recNo, getClientId());	
			setClientId(null);
		}
	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException, RemoteException {
		return lockingManager.isLocked(recNo);
	}
	
	public boolean alreadyBooked(int recNo) throws RecordNotFoundException, RemoteException
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