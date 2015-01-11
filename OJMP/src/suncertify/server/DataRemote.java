package suncertify.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.db.DBCache;
import suncertify.db.DBFileIO;
import suncertify.db.DatabaseFailureException;
import suncertify.db.DuplicateKeyException;
import suncertify.db.LockingManager;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;
import suncertify.model.Room;

/**
 * This class is used by the client to send requests to the server over RMI.
 * 
 * @author Robbie Byrne
 * 
 */
public class DataRemote extends UnicastRemoteObject implements DBMainRemote {

	private static final long serialVersionUID = -8113387190165778794L;
	private final DBFileIO dao;
	private final DBCache cache;
	private final LockingManager lockingManager;
	private final int ownerIndex = 6;
	private Long clientId;
	private long cookie;

	/**
	 * The constructor for the DataRemote class takes a db file location as its
	 * only input.
	 * 
	 * @param dbLocation
	 *            Db file location as a String
	 * @throws RemoteException
	 *             Thrown if there is a client-server network issue.
	 * @throws DatabaseFailureException
	 *             This is thrown if there is an issue accessing the db file.
	 */
	public DataRemote(final String dbLocation) throws RemoteException,
			DatabaseFailureException {
		CopyOnWriteArrayList<Room> records;
		String[] fields;
		dao = new DBFileIO(dbLocation);

		try {
			records = dao.getAllRecords();
			fields = dao.getFieldNames();

		} catch (final IOException e) {
			throw new DatabaseFailureException("Can't find/read db file");
		}

		cache = new DBCache(records, fields);
		lockingManager = new LockingManager();
	}

	@Override
	public List<Room> getAllRecords() {
		return cache.getAllRecords();
	}

	@Override
	public String[] getFields() {
		return cache.getFieldNames();
	}

	@Override
	public String[] readRecord(final long recNo)
			throws RecordNotFoundException, RemoteException {
		return cache.readRecord(recNo).toStrArray();
	}

	@Override
	public void updateRecord(final long recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			RemoteException, SecurityException {
		cookie = lockRecord(recNo);
		cache.updateRecord(recNo, data[ownerIndex]);
		if (cookie == lockCookie) {
			unlock(recNo, lockCookie);
		} else {
			throw new SecurityException("Incorrect cookie to unlock record");
		}
	}

	@Override
	public void deleteRecord(final long recNo, final long cookie)
			throws RecordNotFoundException, RemoteException {
		cache.deleteRecord(recNo);
	}

	@Override
	public long[] findByCriteria(final String[] criteria)
			throws RemoteException {
		return cache.find(criteria);
	}

	@Override
	public long createRecord(final String[] data) throws DuplicateKeyException,
			RemoteException {
		return cache.addRecord(Room.strToRoom(data));
	}

	@Override
	public long lockRecord(final long recNo) throws RecordNotFoundException,
			RemoteException {
		if (cache.containsRecord(recNo)) {
			final Long clientId = lockingManager.lock(recNo);
			setClientId(clientId);
		} else {
			throw new RecordNotFoundException();
		}
		return clientId;
	}

	@Override
	public void unlock(final long recNo, final long cookie)
			throws RemoteException, SecurityException {
		if (cache.containsRecord(recNo)) {
			lockingManager.unlock(recNo, getClientId());
			setClientId(null);
		}
	}

	@Override
	public boolean isLocked(final long recNo) throws RecordNotFoundException,
			RemoteException {
		return lockingManager.isLocked(recNo);
	}

	public boolean alreadyBooked(final int recNo)
			throws RecordNotFoundException, RemoteException {
		final String[] room = readRecord(recNo);
		return room[ownerIndex].trim().length() > 0;
	}

	private void setClientId(final Long clientId) {
		this.clientId = clientId;
	}

	private Long getClientId() {
		return this.clientId;
	}

	@Override
	public boolean alreadyBooked(final long recNo)
			throws RecordNotFoundException, RemoteException {
		return lockingManager.isLocked(recNo);
	}
}