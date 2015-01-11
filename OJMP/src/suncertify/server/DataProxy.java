package suncertify.server;

import java.rmi.RemoteException;

import suncertify.db.DBAccessExtended;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;

/**
 * This class is used as a wrapper around the DBMainRemote interface which is
 * used for RMI.
 * 
 * @author Robbie Byrne
 * 
 */
public class DataProxy implements DBAccessExtended {

	private final DBMainRemote dataRemote;
	private long cookie;

	/**
	 * Constructor for DataProxy takes the interface used for RMI .
	 * 
	 * @param remoteDBAccess
	 *            Interface used for RMI
	 */
	public DataProxy(final DBMainRemote remoteDBAccess) {
		dataRemote = remoteDBAccess;
	}

	@Override
	public String[] readRecord(final long recNo) throws RecordNotFoundException {
		String[] result = null;
		try {
			result = dataRemote.readRecord(recNo);
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}
		return result;
	}

	@Override
	public void updateRecord(final long recNo, final String[] data,
			final long cookie) throws RecordNotFoundException,
			SecurityException {
		try {
			dataRemote.updateRecord(recNo, data, cookie);
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}
	}

	@Override
	public void deleteRecord(final long recNo, final long cookie)
			throws RecordNotFoundException {
		try {
			dataRemote.deleteRecord(recNo, cookie);
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}

	}

	@Override
	public long[] findByCriteria(final String[] criteria) {
		long[] result;
		try {
			result = dataRemote.findByCriteria(criteria);
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}
		return result;
	}

	@Override
	public long createRecord(final String[] data) throws DuplicateKeyException {
		long result;
		try {
			result = dataRemote.createRecord(data);
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}
		return result;
	}

	@Override
	public long lockRecord(final long recNo) throws RecordNotFoundException {
		try {
			cookie = dataRemote.lockRecord(recNo);
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}
		return cookie;
	}

	@Override
	public void unlock(final long recNo, final long cookie)
			throws SecurityException {
		try {
			dataRemote.unlock(recNo, cookie);
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}
	}

	@Override
	public boolean isLocked(final long recNo) throws RecordNotFoundException {
		boolean result;
		try {
			result = dataRemote.isLocked(recNo);
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}
		return result;
	}

	@Override
	public String[] getFields() {
		try {
			return dataRemote.getFields();
		} catch (final RemoteException e) {
			throw new NewRuntimeException();
		}
	}

	@Override
	public long getCookie() {
		return cookie;
	}
}
