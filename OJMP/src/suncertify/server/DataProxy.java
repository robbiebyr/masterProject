package suncertify.server;

import java.rmi.RemoteException;

import suncertify.db.DBMainExtended;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;

/**
 * This class is used as a wrapper around the DBMainRemote interface which is used for RMI.
 * @author Robbie Byrne
 *
 */
public class DataProxy implements DBMainExtended {

	private DBMainRemote dataRemote;

	/**
	 * Constructor for DataProxy takes the interface used for RMI .
	 * @param remoteDBAccess Interface used for RMI
	 */
	public DataProxy(DBMainRemote remoteDBAccess) {
		dataRemote = remoteDBAccess;
	}

	@Override
	public String[] read(int recNo) throws RecordNotFoundException {
		String[] result = null;
		try {
			result = dataRemote.read(recNo);
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}
		return result;
	}

	@Override
	public void update(int recNo, String[] data) throws RecordNotFoundException {
		try {
			dataRemote.update(recNo, data);
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}
	}

	@Override
	public void delete(int recNo) throws RecordNotFoundException {
		try {
			dataRemote.delete(recNo);
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}

	}

	@Override
	public int[] find(String[] criteria) throws RecordNotFoundException {
		int[] result;
		try {
			result = dataRemote.find(criteria);
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}
		return result;
	}

	@Override
	public int create(String[] data) throws DuplicateKeyException {
		int result;
		try {
			result = dataRemote.create(data);
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}
		return result;
	}

	@Override
	public void lock(int recNo) throws RecordNotFoundException {
		try {
			dataRemote.lock(recNo);
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}
	}

	@Override
	public void unlock(int recNo) throws RecordNotFoundException {
		try {
			dataRemote.lock(recNo);
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}
	}

	@Override
	public boolean isLocked(int recNo) throws RecordNotFoundException {
		boolean result;
		try {
			result = dataRemote.isLocked(recNo);
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}
		return result;
	}

	@Override
	public String[] getFields() {
		try {
			return dataRemote.getFields();
		} catch (RemoteException e) {
			throw new NewRuntimeException();
		}
	}
}
