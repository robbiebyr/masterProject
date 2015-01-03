package suncertify.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.model.Room;

/**
 * DBCache is used to store a local copy of all the Room records in memory for
 * access to standalone and networked clients.
 * @author Robbie Byrne 
 */
public class DBCache {

	static CopyOnWriteArrayList<Room> recordsCache = new CopyOnWriteArrayList<Room>();
	String[] fieldNames;

	/**
	 * This is the constructor for the Cache object. It takes a collection of
	 * Room records and String array of record fields.
	 * 
	 * @param records
	 *            This is a CopyOnWriteArrayList of Room objects.
	 * @param fields
	 *            This is a String array of the field names for all records.
	 */
	public DBCache(CopyOnWriteArrayList<Room> records, String[] fields) {
		recordsCache = records;
		this.fieldNames = fields;
	}

	/**
	 * Returns all the available Room records in the Cache as a List.
	 * 
	 * @return All Room available records returned in a List.
	 */
	public List<Room> getAllRecords() {
		return recordsCache;
	}

	/**
	 * This method is used to add a Room record to the Cache
	 * 
	 * @param room
	 *            Room object.
	 * @return returns This is the new Room record number.
	 */
	public int addRecord(Room room) {
		recordsCache.add(room);
		return recordsCache.size() + 1;
	}

	/**
	 * Read a Room record from the Cache.
	 * 
	 * @param recNo
	 *            int corresponding to record number
	 * @return Record Room object.
	 */
	public Room readRecord(int recNo) {
		return recordsCache.get(recNo);
	}

	/**
	 * This method is used to get field names.
	 * 
	 * @return String [] of field names.
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * This method is used to delete a record from the cache.
	 * 
	 * @param recNo
	 *            int corresponding to a record in the Cache.
	 */
	public void deleteRecord(int recNo) {
		recordsCache.remove(recNo);
	}

	/**
	 * This method is used to find a Room record matching a Name and/or
	 * Location.
	 * 
	 * @param criteria
	 *            This is a string array containing the search terms Name and/or
	 *            Location.
	 * @return int array corresponding to records which matched the criteria
	 * @throws RecordNotFoundException
	 *             Thrown if the find method returns no results
	 */
	public int[] find(String[] criteria) throws RecordNotFoundException {

		List<Integer> recNumbers = new ArrayList<Integer>();
		String name, location;
		int cachePosition = 0;

		name = criteria[0];
		location = criteria[1];

		Iterator<Room> roomIterator = recordsCache.iterator();

		while (roomIterator.hasNext()) {
			Room room = roomIterator.next();

			if (room.getName().toLowerCase().contains(name.toLowerCase())) {
				if (room.getLocation().toLowerCase()
						.contains(location.toLowerCase())) {
					recNumbers.add(cachePosition);
				}
			}
			cachePosition++;
		}

		if (recNumbers.size() == 0) {
			throw new RecordNotFoundException();
		}
		return IntegerArrayToIntArray(recNumbers);
	}

	/**
	 * This method is used to book a record by updating the Owner of the Room
	 * record.
	 * 
	 * @param recNo
	 *            This int corresponds to the record in question.
	 * @param owner
	 */
	public void updateRecord(int recNo, String owner) {
		Room updatedRoom = recordsCache.get(recNo);
		updatedRoom.setOwner(owner);
		recordsCache.set(recNo, updatedRoom);
	}

	/**
	 * This method determines if a record is in the Cache.
	 * 
	 * @param recNo
	 *            The record number of the record in question
	 * @return True/false depending on record occurrence in Cache.
	 */
	public boolean containsRecord(int recNo) {
		return recordsCache.size() >= recNo;
	}

	/**
	 * Static method used to write (dump) the contents of the Cache to file when
	 * the server is shutdown.
	 */
	public static void dumpCacheToFile() {
		int retrys = 0;
		int maxRetrys = 3;
			
		while (true) {
			try {
				if (recordsCache.size() > 0) {
					DBFileIO.writeAllRecordsToFile(recordsCache);
					break;
				}
			} catch (IOException e) {
				if (++retrys == maxRetrys)
				{
					/*
					 * Attempts were made to write the Cache three times but an exception
					 * was thrown on each occasion and there is no option but to exit without 
					 * saving the cache.
					 */
					break;
				}
			}
		}
	}

	private int[] IntegerArrayToIntArray(List<Integer> IntegerResults) {
		int results[] = new int[IntegerResults.size()];

		for (int i = 0; i < IntegerResults.size(); i++) {
			results[i] = IntegerResults.get(i);
		}

		return results;
	}
}