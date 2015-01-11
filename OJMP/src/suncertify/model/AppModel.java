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
 * This class represents the Model for the MVC. It manages the state of the
 * application.
 * 
 * @author Robbie Byrne
 * 
 */
public class AppModel extends Observable implements IAppModel {

	private final DBAccessExtended data;
	private Map<Integer, Long> actualIndexMap;
	private final int numOfFeilds = 7;
	private final int ownerIndex = numOfFeilds - 1;
	private int totalRecords;

	private static List<Observer> observers = new ArrayList<Observer>();

	/**
	 * This constructor takes an implementation of the DBAccessExtended
	 * interface as input.
	 * 
	 * @param dbAccess
	 *            implementation of the DBMainExtended interface.
	 */
	public AppModel(final DBAccessExtended dbAccess) {
		data = dbAccess;
	}

	@Override
	public List<HotelRoom> getAllRecords() {
		final List<HotelRoom> temp = getAllRecordsFromCache();
		totalRecords = temp.size();
		return temp;
	}

	@Override
	public Map<Integer, HotelRoom> queryRecords(final String name,
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
	 * This method is used to create a HotelRoom record.
	 * 
	 * @param room
	 *            HotelRoom object.
	 * @return int representing new room's record number
	 * @throws DuplicateKeyException
	 */
	public long create(final HotelRoom room) throws DuplicateKeyException {
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

	private Map<Integer, HotelRoom> getMultipleRecords(final long[] recNumbers) {
		final Map<Integer, HotelRoom> temp = new LinkedHashMap<Integer, HotelRoom>();

		if (recNumbers == null) {
			return temp;
		} else {
			actualIndexMap = new LinkedHashMap<Integer, Long>();

			for (int i = 0; i < recNumbers.length; i++) {
				HotelRoom room = null;
				try {
					room = HotelRoom.strToRoom(data.readRecord(recNumbers[i]));
				} catch (final RecordNotFoundException e) {
				}
				temp.put(i, room);
				actualIndexMap.put(i, recNumbers[i]);
			}
			return temp;
		}
	}

	private List<HotelRoom> getAllRecordsFromCache() {
		final List<HotelRoom> records = new ArrayList<HotelRoom>(10);
		int recNo = 0;
		while (true) {
			try {
				final HotelRoom room = HotelRoom.strToRoom(data
						.readRecord(recNo));
				records.add(room);
				recNo++;
			} catch (final NewRuntimeException e) {
				final LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
				lostConnectionDialog.processResponce();
			} catch (final Exception e) {
				// All record have been read.
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