package suncertify.db;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import suncertify.model.Room;

/**
 * This class is used to read the provided db file and to write the Cache to
 * file.
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
	private static int numberOfFields;
	private static String[] fieldNames;
	private static int[] fieldLengths;
	private static ArrayList<Long> recordFileLocationsInBytes;

	private byte[] DELETED_RECORD = new byte[RECORD_FLAG];

	/**
	 * This is the constructor which take a String value representing the
	 * database location.
	 * 
	 * @param location
	 *            String representing the location of the db file.
	 * @throws DatabaseFailureException
	 *             This is thrown if there is an issue accessing the db file.
	 */
	public DBFileIO(final String location) throws DatabaseFailureException {
		DATABASE_LOCATION = location;
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
	 *            type Room.
	 * @throws IOException
	 *             is thrown when there is an issue writing to the file.
	 */
	public static void writeAllRecordsToFile(
			final CopyOnWriteArrayList<Room> recordsCache) throws IOException {
		dbFileAccess = new RandomAccessFile(DATABASE_LOCATION, "rw");

		/*
		 * Clear all records from file.
		 */
		clearToEOF();

		/*
		 * Using the offset in bytes between the start of the first record and
		 * the start of the second record, I determine the value I can use to
		 * skip to the start of any record. This should be scalable beyond the
		 * amount of records currently in the file.
		 */
		final long recordSize = recordFileLocationsInBytes.get(1)
				- recordFileLocationsInBytes.get(0);

		for (int x = 0; x < recordsCache.size(); x++) {
			final Room room = recordsCache.get(x);
			dbFileAccess.getChannel().position((recordSize * x));

			/*
			 * Write a valid record flag
			 */
			dbFileAccess.write(new byte[2]);

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
	 * This method is used to get all the records from the .db file.
	 * 
	 * @return CopyOnWriteArrayList This CopyOnWriteArrayList contains all the
	 *         records as Room objects.
	 * @throws IOException
	 *             When there is a issue accessing the .db file.
	 */
	public CopyOnWriteArrayList<Room> getAllRecords() throws IOException {
		final CopyOnWriteArrayList<Room> records = new CopyOnWriteArrayList<Room>();
		recordFileLocationsInBytes = new ArrayList<Long>();
		DELETED_RECORD = "0xFF".getBytes();

		/*
		 * Creating a random access file stream to read and write to the
		 * database file.
		 */

		dbFileAccess = new RandomAccessFile(DATABASE_LOCATION, "r");

		/*
		 * Read in the ints and shorts from the db file which corresponding to
		 * the magic cookie, record length and number of fields per record.
		 */

		/*
		 * Magic cookie value is not being used.
		 */
		dbFileAccess.readInt();

		numberOfFields = dbFileAccess.readShort();

		fieldNames = new String[numberOfFields];
		fieldLengths = new int[numberOfFields];

		/*
		 * At this point, the meta data of the db file has been obtained,
		 * therefore the main point of this "for" loop is to get the name and
		 * length (in bytes) of each field
		 */
		for (int i = 0; i < numberOfFields; i++) {

			/*
			 * One bytes store the number of bytes that a field name will
			 * consume. This is read into an int.
			 */
			final int nameLengthNumOfBytes = dbFileAccess.readUnsignedByte();

			/*
			 * The int that was just read tells us how many bytes to read, we
			 * read that many bytes & cast to a String. This gives us the name
			 * of the field.
			 */
			final byte[] fieldNameByteArray = new byte[nameLengthNumOfBytes];
			dbFileAccess.readFully(fieldNameByteArray);
			fieldNames[i] = new String(fieldNameByteArray, ENCODING);

			/*
			 * We then read the number of bytes which will contain the field
			 * value
			 */
			final byte[] fieldValueNumOfBytes = new byte[FIELD_LENGTH];
			dbFileAccess.readFully(fieldValueNumOfBytes);
			fieldLengths[i] = byteArrayToInt(fieldValueNumOfBytes);
		}

		/*
		 * The actual content of each record is read here. Room objects are
		 * created as we go and added to a collection. The file is read until
		 * EOF is reached.
		 */

		while (true) {
			int EndOfFile;
			final Room room = new Room();

			try {
				EndOfFile = dbFileAccess.read();
			} catch (final EOFException e) {
				/*
				 * A EOFException indicates that the end of file has been
				 * reached, all records hence have been read so we break out of
				 * the while loop.
				 */
				break;
			}

			/*
			 * This collection is used to keep track of the location of records
			 * in the file.
			 */
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

			if (EndOfFile != byteArrayToInt(DELETED_RECORD)) {
				/*
				 * Only records not flagged as deleted, will be added to the
				 * cache.
				 */
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

	private static void clearToEOF() throws IOException {
		dbFileAccess.setLength(recordFileLocationsInBytes.get(0));
	}

	/*
	 * This method is used to convert a byte array to its corresponding int
	 * value.
	 */
	private static int byteArrayToInt(final byte[] byteArray) {
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