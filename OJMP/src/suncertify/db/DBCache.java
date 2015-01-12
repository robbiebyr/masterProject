package suncertify.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.model.HotelRoom;

/**
 * DBCache is the local cached copy of the database. It is used by the
 * standalone and networed clients.
 * 
 * @author Robbie
 * 
 */
public class DBCache {

	static CopyOnWriteArrayList<HotelRoom> recordsCache = new CopyOnWriteArrayList<>();
	String[] fieldNames;

	/**
	 * Constructor for DBCache. Takes a collection of HotelRooms and String
	 * array of record fields.
	 * 
	 * @param records
	 *            This is a CopyOnWriteArrayList of Room objects.
	 * @param fields
	 *            This is a String array of the field names for all records.
	 */
	public DBCache(final CopyOnWriteArrayList<HotelRoom> records,
			final String[] fields) {
		recordsCache = records;
		this.fieldNames = fields;
	}

	/**
	 * Returns all available HotelRooms records from DBCache as a list.
	 * 
	 * @return all available HotelRooms in DBCache.
	 */
	public List<HotelRoom> getAllRecords() {
		return recordsCache;
	}

	/**
	 * Allows new HotelRooms to be added to DBCache.
	 * 
	 * @param room
	 *            to add to DBCache
	 * @return record number for new HotelRoom
	 */
	public int addRecord(final HotelRoom room) {
		recordsCache.add(room);
		return recordsCache.size() + 1;
	}

	/**
	 * Read the passed record from the DBCache.
	 * 
	 * @param recNo
	 *            to read
	 * @return The HotelRoom object related to the recNo passed
	 */
	public HotelRoom readRecord(final long recNo) {
		return recordsCache.get((int) recNo);
	}

	/**
	 * Used to get field names.
	 * 
	 * @return Array of fieldNames.
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * Allows the passed record to be deleted from DBCache.
	 * 
	 * @param recNo
	 *            the record to delete from DBCache
	 */
	public void deleteRecord(final long recNo) {
		recordsCache.remove(recNo);
	}

	/**
	 * Allows the passed record to be updated with the customer who booked the
	 * HotelRoom.
	 * 
	 * @param recNo
	 *            Record to update
	 * @param customer
	 *            Customer who has booked the room.
	 */
	public void updateRecord(final long recNo, final String customer) {
		final HotelRoom updatedRoom = recordsCache.get((int) recNo);
		updatedRoom.setOwner(customer);
		recordsCache.set((int) recNo, updatedRoom);
	}

	/**
	 * Is used to find a HotelRoom by the name or location.
	 * 
	 * @param criteria
	 *            contains the name and/or the location to search for.
	 * @return Array of recNo that match the search criteria
	 */
	public long[] findByCriteria(final String[] criteria) {

		final List<Long> recNumbers = new ArrayList<>();
		String name, location;
		long cachePosition = 0;

		name = criteria[0];
		location = criteria[1];

		final Iterator<HotelRoom> roomIterator = recordsCache.iterator();

		while (roomIterator.hasNext()) {
			final HotelRoom room = roomIterator.next();

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

	/**
	 * Checks if record is in the DBCache
	 * 
	 * @param recNo
	 *            to check if in DBCache
	 * @return true if in DBCache, false otherwise
	 */
	public boolean isRecordInDBCache(final long recNo) {
		return recordsCache.size() >= recNo;
	}

	/**
	 * Used when server is shutdown. Writes the DBCache back to the db file.
	 */
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
