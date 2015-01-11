package suncertify.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.model.Room;

public class DBCache {

	static CopyOnWriteArrayList<Room> recordsCache = new CopyOnWriteArrayList<Room>();
	String[] fieldNames;

	public DBCache(final CopyOnWriteArrayList<Room> records,
			final String[] fields) {
		recordsCache = records;
		this.fieldNames = fields;
	}

	public List<Room> getAllRecords() {
		return recordsCache;
	}

	public int addRecord(final Room room) {
		recordsCache.add(room);
		return recordsCache.size() + 1;
	}

	public Room readRecord(final long recNo) {
		return recordsCache.get((int) recNo);
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void deleteRecord(final long recNo) {
		recordsCache.remove(recNo);
	}

	public void updateRecord(final long recNo, final String owner) {
		final Room updatedRoom = recordsCache.get((int) recNo);
		updatedRoom.setOwner(owner);
		recordsCache.set((int) recNo, updatedRoom);
	}

	public long[] find(final String[] criteria) {

		final List<Long> recNumbers = new ArrayList<>();
		String name, location;
		long cachePosition = 0;

		name = criteria[0];
		location = criteria[1];

		final Iterator<Room> roomIterator = recordsCache.iterator();

		while (roomIterator.hasNext()) {
			final Room room = roomIterator.next();

			if (room.getName().toLowerCase().contains(name.toLowerCase())) {
				if (room.getLocation().toLowerCase()
						.contains(location.toLowerCase())) {
					recNumbers.add(cachePosition);
				}
			}
			cachePosition++;
		}

		return LongArrayTolongArray(recNumbers);
	}

	private long[] LongArrayTolongArray(final List<Long> LongResults) {
		final long results[] = new long[LongResults.size()];

		for (int i = 0; i < LongResults.size(); i++) {
			results[i] = LongResults.get(i);
		}

		return results;
	}

	public boolean containsRecord(final long recNo) {
		return recordsCache.size() >= recNo;
	}

	public static void dumpCacheToFile() {
		int retrys = 0;
		final int maxRetrys = 3;

		while (true) {
			try {
				if (recordsCache.size() > 0) {
					DBFileIO.writeAllRecordsToFile(recordsCache);
					break;
				}
			} catch (final IOException e) {
				if (++retrys == maxRetrys) {
					/*
					 * Attempts were made to write the Cache three times but an
					 * exception was thrown on each occasion and there is no
					 * option but to exit without saving the cache.
					 */
					break;
				}
			}
		}
	}
}
