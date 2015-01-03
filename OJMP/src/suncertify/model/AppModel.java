package suncertify.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import suncertify.common.LostConnectionDialog;
import suncertify.db.DBMainExtended;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotAvailableException;
import suncertify.db.RecordNotFoundException;
import suncertify.server.NewRuntimeException;

/**
 * This class represents the Model of the MVC. It manages the state of the
 * application.
 * 
 * @author Robbie Byrne
 * 
 */
public class AppModel extends Observable implements AppModelInterface {

	private DBMainExtended data;
	private Map<Integer, Integer> actualIndexMap;
	private int numOfFeilds = 7;
	private int ownerIndex = numOfFeilds - 1;
	private int totalRecords;

	private static List<Observer> observers = new ArrayList<Observer>();

	/**
	 * This constructor takes an implementation of the DBMainExtended interface
	 * as input.
	 * 
	 * @param dbAccess
	 *            implementation of the DBMainExtended interface.
	 */
	public AppModel(DBMainExtended dbAccess) {
		data = dbAccess;
	}

	@Override
	public List<Room> getAllRecords() {
		List<Room> temp = getAllRecordsFromCache();
		totalRecords = temp.size();
		return temp;
	}

	@Override
	public Map<Integer, Room> queryRecords(String name, String location) {
		String[] criteria = new String[numOfFeilds];
		criteria[0] = name;
		criteria[1] = location;
		int recordNums[] = null;
		try {
			recordNums = data.find(criteria);
		} catch (RecordNotFoundException e) {
			recordNums = null;
		}

		return getMultipleRecords(recordNums);
	}

	@Override
	public String[] getRoomFeilds() throws NewRuntimeException {
		return data.getFields();
	}

	@Override
	public void updateRecord(int recNo, String owner)
			throws RecordNotFoundException, RecordNotAvailableException {

		if (!alreadyBooked(recNo)) {
			String[] record = new String[numOfFeilds];
			record[ownerIndex] = owner;

			data.update(recNo, record);
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
	public int create(Room room) throws DuplicateKeyException {
		return data.create(room.toStrArray());
	}

	@Override
	public void addObserver(Observer observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	protected void fireNotification() {
		for (Observer observer : observers) {
			observer.update(this, new Object());
		}
	}

	@Override
	public Integer getActualIndexMap(int selection) {
		return actualIndexMap.get(selection);
	}

	@Override
	public int getTotalNumberRecords() {
		return totalRecords;
	}

	private Map<Integer, Room> getMultipleRecords(int[] recNumbers) {
		Map<Integer, Room> temp = new LinkedHashMap<Integer, Room>();

		if (recNumbers == null) {
			return temp;
		} else {
			actualIndexMap = new LinkedHashMap<Integer, Integer>();

			for (int i = 0; i < recNumbers.length; i++) {
				Room room = null;
				try {
					room = Room.strToRoom(data.read(recNumbers[i]));
				} catch (RecordNotFoundException e) {
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
		List<Room> records = new ArrayList<Room>(10);
		int recNo = 0;
		while (true) {
			try {
				Room room = Room.strToRoom(data.read(recNo));
				records.add(room);
				recNo++;
			} catch (NewRuntimeException e) {
				LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
				lostConnectionDialog.processResponce();
			} catch (Exception e) {
				/*
				 * All records from Cache have been read.
				 */
				break;
			}
		}
		return records;
	}

	private boolean alreadyBooked(int recNo) throws RecordNotFoundException {
		boolean result = true;
		String[] record = null;
		try {
			record = data.read(recNo);
		} catch (NewRuntimeException e) {
			LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
			lostConnectionDialog.processResponce();
		}

		if (record[ownerIndex].trim().length() == 0) {
			result = false;
		}
		return result;
	}
}