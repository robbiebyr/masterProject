package suncertify.db;

import java.util.HashMap;
import java.util.Map;

public class LockingManager {
	private final Map<Long, Long> lockTable = new HashMap<>();

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

	public boolean isLocked(final long recNo) {
		return lockTable.containsKey(recNo);
	}
}
