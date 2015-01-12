package suncertify.db;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.model.HotelRoom;

/**
 * This class is used to read the provided db file and to write the DBCache back
 * to the file.
 * 
 * @author Robbie Byrne
 * 
 */
public class DBFileIO {

	private static final int FIELD_LENGTH = 1;
	private static final int RECORD_FLAG = 1;
	private static String DATABASE_LOCATION = "";
	private static final String ENCODING = "US-ASCII";
	private static RandomAccessFile dbFileAccess;
	private static long NON_CHANGING_DATA_LENGTH;
	private static int numberOfFields;
	private static String[] fieldNames;
	private static int[] fieldLengths;
	private static ArrayList<Long> recordFileLocationsInBytes;

	private byte[] DELETED_RECORD = new byte[RECORD_FLAG];

	/**
	 * Constructor which take in the database location.
	 * 
	 * @param dbLocation
	 *            String representing the location of the db file.
	 * @throws DatabaseFailureException
	 *             This is thrown if there is an issue accessing the db file.
	 */
	public DBFileIO(final String dbLocation) throws DatabaseFailureException {
		DATABASE_LOCATION = dbLocation;
	}

	/**
	 * @return String array containing all the field names of each record.
	 */
	public String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * This method is used to write the Cache of the application to file.
	 * 
	 * @param recordsCache
	 *            This is a CopyOnWriteArrayList collection with all elements of
	 *            type HotelRoom.
	 * @throws IOException
	 *             is thrown when there is an issue writing to the file.
	 */
	public static void writeAllRecordsToFile(
			final CopyOnWriteArrayList<HotelRoom> recordsCache)
			throws IOException {

		dbFileAccess.seek(0);
		final byte[] startOfFile = new byte[(int) NON_CHANGING_DATA_LENGTH];
		dbFileAccess.read(startOfFile);
		dbFileAccess = new RandomAccessFile(DATABASE_LOCATION, "rw");

		clearFileAfterHeaders();

		dbFileAccess.write(startOfFile);

		for (int x = 0; x < recordsCache.size(); x++) {
			final HotelRoom room = recordsCache.get(x);

			dbFileAccess.write(new byte[1]);

			for (int i = 0; i < numberOfFields; i++) {
				byte[] buffer = new byte[fieldLengths[i]];
				String temp = "";

				switch (i) {
				case 0:
					temp = room.getName();
					break;
				case 1:
					temp = room.getLocation();
					break;
				case 2:
					temp = room.getSize();
					break;
				case 3:
					temp = room.getSmoking();
					break;
				case 4:
					temp = room.getRate();
					break;
				case 5:
					temp = room.getDate();
					break;
				case 6:
					temp = room.getOwner();
					break;
				}

				buffer = addPadding(temp, fieldLengths[i]);
				dbFileAccess.write(buffer);
			}
		}

		dbFileAccess.close();
	}

	/**
	 * This method is used to read in all the records from the .db file.
	 * 
	 * @return CopyOnWriteArrayList This CopyOnWriteArrayList contains all the
	 *         records as Room objects.
	 * @throws IOException
	 *             When there is a issue accessing the .db file.
	 */
	public CopyOnWriteArrayList<HotelRoom> getAllRecords() throws IOException {
		final CopyOnWriteArrayList<HotelRoom> records = new CopyOnWriteArrayList<HotelRoom>();
		recordFileLocationsInBytes = new ArrayList<Long>();
		DELETED_RECORD = "0xFF".getBytes();

		dbFileAccess = new RandomAccessFile(DATABASE_LOCATION, "r");

		dbFileAccess.readInt();

		numberOfFields = dbFileAccess.readShort();

		fieldNames = new String[numberOfFields];
		fieldLengths = new int[numberOfFields];

		for (int i = 0; i < numberOfFields; i++) {

			final int nameLengthNumOfBytes = dbFileAccess.read();

			final byte[] fieldNameByteArray = new byte[nameLengthNumOfBytes];
			dbFileAccess.readFully(fieldNameByteArray);
			fieldNames[i] = new String(fieldNameByteArray, ENCODING);

			final byte[] fieldValueNumOfBytes = new byte[FIELD_LENGTH];
			dbFileAccess.readFully(fieldValueNumOfBytes);
			fieldLengths[i] = convertByteArrayToInt(fieldValueNumOfBytes);
		}

		NON_CHANGING_DATA_LENGTH = dbFileAccess.getFilePointer();

		// Read data for HotelRooms from here.
		while (true) {
			int EndOfFile;
			final HotelRoom room = new HotelRoom();

			try {
				EndOfFile = dbFileAccess.readByte();
			} catch (final EOFException e) {
				// Look for EOFException which tells us we have finished the
				// read.
				break;
			}

			recordFileLocationsInBytes.add(dbFileAccess.getFilePointer());

			for (int i = 0; i < numberOfFields; i++) {
				final byte[] buffer = new byte[fieldLengths[i]];

				dbFileAccess.read(buffer);
				final String data = new String(buffer, ENCODING);

				switch (i) {
				case 0:
					room.setName(data.trim());
					break;
				case 1:
					room.setLocation(data.trim());
					break;
				case 2:
					room.setSize(data.trim());
					break;
				case 3:
					room.setSmoking(data.trim());
					break;
				case 4:
					room.setRate(data.trim());
					break;
				case 5:
					room.setDate(data.trim());
					break;
				case 6:
					room.setOwner(data.trim());
					break;
				}
			}

			if (EndOfFile != convertByteArrayToInt(DELETED_RECORD)) {
				records.add(room);
			}
		}
		return records;
	}

	private static byte[] addPadding(final String currentField,
			final int requiredSize) {
		final char[] paddingChar = new char[requiredSize
				- currentField.length()];
		final String padding = new String(paddingChar);

		final String combined = currentField + padding;
		return combined.getBytes();
	}

	private static void clearFileAfterHeaders() throws IOException {
		dbFileAccess.setLength(0);
	}

	private static int convertByteArrayToInt(final byte[] byteArray) {
		int value = 0, i = 0;
		final int totalByteLength = byteArray.length;

		while (i < totalByteLength) {
			final int shift = (totalByteLength - 1 - i) * 8;
			value += (byteArray[i] & 0x000000FF) << shift;
			i++;
		}

		return value;
	}
}