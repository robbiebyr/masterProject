package suncertify.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to lock record in the DBCache to prevent multiple clients modifying the
 * same record at the same time.
 * 
 * @author Robbie
 * 
 */
public class LockingManager {
	private final Map<Long, Long> lockTable = new HashMap<>();

	/**
	 * Allows the record passed in to be locked by a client so cannot be
	 * modified by another client.
	 * 
	 * @param recNo
	 *            record to lock.
	 * @return Cookie to allow the client that locked the record to unlock it at
	 *         a future time.
	 * @throws RecordNotFoundException
	 *             thrown if no record matching the recNo in DBCache.
	 */

	public synchronized Long lock(final long recNo)
			throws RecordNotFoundException {

		final Long cookie = System.nanoTime();

		try {
			while (isLocked(recNo)) {
				wait();
			}

			lockTable.put(recNo, cookie);
		} catch (final InterruptedException e) {
			throw new RecordNotFoundException(
					"Record could not be locked, try again or book another record.");
		}

		return cookie;
	}

	/**
	 * Allows locked records to be unlocked.
	 * 
	 * @param recNo
	 *            The record to unlock
	 * @param cookie
	 *            The cookie for the locked record. Prevents clients the didn't
	 *            lock the record from unlocking it.
	 * @throws SecurityException
	 *             Thrown if incorrect cookie used to unlock.
	 */
	public synchronized void unlock(final long recNo, final Long cookie)
			throws SecurityException {

		if (isLocked(recNo)) {
			if (lockTable.get(recNo) == cookie) {
				lockTable.remove(recNo);
				notifyAll();
			} else {
				throw new SecurityException(
						"Client does not have permission to unlock this record.");
			}
		}
	}

	/**
	 * Checks if the record passed in is locked or unlocked.
	 * 
	 * @param recNo
	 *            Record to check.
	 * @return true if the record is locked, false otherwise.
	 */
	public boolean isLocked(final long recNo) {
		return lockTable.containsKey(recNo);
	}
}
