package suncertify.db;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.model.Room;

public class Data implements DBAccessExtended {

	private final DBFileIO dao;
	private final DBCache cache;
	private final LockingManager lockingManager;
	private final int ownerIndex = 6;
	private long cookie;

	public Data(final String dbLocation) throws DatabaseFailureException {
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
	public String[] readRecord(final long recNo) throws RecordNotFoundException {
		return cache.readRecord(recNo).toStrArray();
	}

	@Override
	public void updateRecord(final long recNo, final String[] data,
			final long lockCookie) throws RecordNotFoundException,
			SecurityException {
		cookie = lockRecord(recNo);
		cache.updateRecord(recNo, data[ownerIndex]);
		if (cookie == lockCookie) {
			unlock(recNo, lockCookie);
		} else {
			throw new SecurityException("Incorrect cookie to unlock record");
		}
	}

	@Override
	public void deleteRecord(final long recNo, final long lockCookie)
			throws RecordNotFoundException, SecurityException {
		final long cookie = lockRecord(recNo);
		cache.deleteRecord(recNo);
		if (cookie == lockCookie) {
			unlock(recNo, lockCookie);
		} else {
			throw new SecurityException("Incorrect cookie to unlock record");
		}
	}

	@Override
	public long[] findByCriteria(final String[] criteria) {
		return cache.find(criteria);
	}

	@Override
	public long createRecord(final String[] data) throws DuplicateKeyException {
		return cache.addRecord(Room.strToRoom(data));
	}

	@Override
	public long lockRecord(final long recNo) throws RecordNotFoundException {
		Long cookie;
		if (cache.containsRecord(recNo)) {
			cookie = lockingManager.lock(recNo);
		} else {
			throw new RecordNotFoundException();
		}
		return cookie;
	}

	@Override
	public void unlock(final long recNo, final long cookie)
			throws SecurityException {
		if (cache.containsRecord(recNo)) {
			lockingManager.unlock(recNo, cookie);
		}
	}

	@Override
	public String[] getFields() {
		return cache.getFieldNames();
	}

	@Override
	public long getCookie() {
		return cookie;
	}

	@Override
	public boolean isLocked(final long recNo) throws RecordNotFoundException {
		return lockingManager.isLocked(recNo);
	}
}
