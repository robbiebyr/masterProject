package suncertify.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import suncertify.common.LostConnectionDialog;
import suncertify.db.DBAccessExtended;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotAvailableException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;
import suncertify.server.NewRuntimeException;

/**
 * This class represents the Model of the MVC. It manages the state of the
 * application.
 * 
 * @author Robbie Byrne
 * 
 */
public class AppModel extends Observable implements AppModelInterface {

	private final DBAccessExtended data;
	private Map<Integer, Long> actualIndexMap;
	private final int numOfFeilds = 7;
	private final int ownerIndex = numOfFeilds - 1;
	private int totalRecords;

	private static List<Observer> observers = new ArrayList<Observer>();

	/**
	 * This constructor takes an implementation of the DBMainExtended interface
	 * as input.
	 * 
	 * @param dbAccess
	 *            implementation of the DBMainExtended interface.
	 */
	public AppModel(final DBAccessExtended dbAccess) {
		data = dbAccess;
	}

	@Override
	public List<Room> getAllRecords() {
		final List<Room> temp = getAllRecordsFromCache();
		totalRecords = temp.size();
		return temp;
	}

	@Override
	public Map<Integer, Room> queryRecords(final String name,
			final String location) {
		final String[] criteria = new String[numOfFeilds];
		criteria[0] = name;
		criteria[1] = location;
		long recordNums[] = null;

		recordNums = data.findByCriteria(criteria);

		return getMultipleRecords(recordNums);
	}

	@Override
	public String[] getRoomFeilds() throws NewRuntimeException {
		return data.getFields();
	}

	@Override
	public void updateRecord(final long recNo, final String owner,
			final long cookie) throws RecordNotFoundException,
			RecordNotAvailableException, SecurityException {

		if (!alreadyBooked(recNo)) {
			final String[] record = new String[numOfFeilds];
			record[ownerIndex] = owner;

			data.updateRecord(recNo, record, cookie);
			fireNotification();
		} else {
			throw new RecordNotAvailableException(
					"ROOM NO LONGER AVAILABLE - PLEASE SELECT ANOTHER");
		}
	}

	/**
	 * This method is used to create a Room record.
	 * 
	 * @param room
	 *            Room object.
	 * @return int representing new room's record number
	 * @throws DuplicateKeyException
	 *             Exception can't be thrown due to Cache implementation.
	 */
	public long create(final Room room) throws DuplicateKeyException {
		return data.createRecord(room.toStrArray());
	}

	@Override
	public void addObserver(final Observer observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public void removeObserver(final Observer observer) {
		observers.remove(observer);
	}

	protected void fireNotification() {
		for (final Observer observer : observers) {
			observer.update(this, new Object());
		}
	}

	@Override
	public Long getActualIndexMap(final long selection) {
		return actualIndexMap.get(selection);
	}

	@Override
	public int getTotalNumberRecords() {
		return totalRecords;
	}

	private Map<Integer, Room> getMultipleRecords(final long[] recNumbers) {
		final Map<Integer, Room> temp = new LinkedHashMap<Integer, Room>();

		if (recNumbers == null) {
			return temp;
		} else {
			actualIndexMap = new LinkedHashMap<Integer, Long>();

			for (int i = 0; i < recNumbers.length; i++) {
				Room room = null;
				try {
					room = Room.strToRoom(data.readRecord(recNumbers[i]));
				} catch (final RecordNotFoundException e) {
					/*
					 * Record numbers were just obtained from Cache so should
					 * exist.
					 */
				}
				temp.put(i, room);
				actualIndexMap.put(i, recNumbers[i]);
			}
			return temp;
		}
	}

	private List<Room> getAllRecordsFromCache() {
		final List<Room> records = new ArrayList<Room>(10);
		int recNo = 0;
		while (true) {
			try {
				final Room room = Room.strToRoom(data.readRecord(recNo));
				records.add(room);
				recNo++;
			} catch (final NewRuntimeException e) {
				final LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
				lostConnectionDialog.processResponce();
			} catch (final Exception e) {
				/*
				 * All records from Cache have been read.
				 */
				break;
			}
		}
		return records;
	}

	private boolean alreadyBooked(final long recNo)
			throws RecordNotFoundException {
		boolean result = true;
		String[] record = null;
		try {
			record = data.readRecord(recNo);
		} catch (final NewRuntimeException e) {
			final LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
			lostConnectionDialog.processResponce();
		}

		if (record[ownerIndex].trim().length() == 0) {
			result = false;
		}
		return result;
	}

	@Override
	public long getCookie() {
		return data.getCookie();
	}
}