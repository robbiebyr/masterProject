package suncertify.model;

import java.io.Serializable;

/**
 * This is a Room object representing a Room record.
 * @author Robbie Byrne
 *
 */
public class Room implements Serializable {

	private static final long serialVersionUID = 5852402373343540028L;

	private String name;
	private String Location;
	private String size;
	private String smoking;
	private String rate;
	private String date;
	private String owner;
	static int numOfFeilds = 7;

	/**
	 * This method is used to convert a Room object to a String array to conform with supplied interface DBMain.
	 * @return String array representing a Room object/record.
	 */
	public String[] toStrArray() {
		String[] toStr = new String[numOfFeilds];
		toStr = new String[] { getName(), getLocation(), getSize(),
				getSmoking(), getRate().toString(), getDate().toString(),
				getOwner() };

		return toStr;
	}

	/**
	 * This method us used to convert a String array into a Room object.
	 * @param roomData String array of room record data.
	 * @return Room object corresponding to a room record.
	 */
	public static Room strToRoom(String[] roomData) {
		return returnRoomObject(roomData);
	}
	
	@Override
	public String toString() {
		return this.getName() + " " + this.getLocation() + " " + this.getDate()
				+ " " + this.getRate() + " " + this.getSize() + " "
				+ this.getSmoking() + " " + this.getOwner();
	}

	/**
	 * Get room name.
	 * @return Room name String.
	 */
	public String getName() {
		return name;
	}

	/** Set room name.
	 * @param name Room name String.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get room location.
	 * @return Room location String.
	 */
	public String getLocation() {
		return Location;
	}

	/**
	 * Set room location.
	 * @param location Room location String.
	 */
	public void setLocation(String location) {
		Location = location;
	}

	/**
	 * Get room size
	 * @return Room size String
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Set Room size
	 * @param size Room size String.
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Get room smoking allowed.
	 * @return String Y/N smoking String 
	 */
	public String getSmoking() {
		return smoking;
	}

	/**
	 * Set smoking allowed
	 * @param smoking Y/N String 
	 */
	public void setSmoking(String smoking) {
		this.smoking = smoking;
	}

	/**
	 * Get room rate.
	 * @return room rate String.
	 */
	public String getRate() {
		return rate;
	}

	/**
	 * Set room rate.
	 * @param rate Room rate String.
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

	/**
	 * Get room available date
	 * @return date String.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Set room available date.
	 * @param date Room available date String
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Get room owner.
	 * @return Owner String.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Set room owner.
	 * @param owner Room owner String.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	private static Room returnRoomObject(String[] roomData) {
		Room room = new Room();

		for (int i = 0; i < numOfFeilds; i++) {
			switch (i) {
			case 0:
				room.setName(roomData[i]);
				break;
			case 1:
				room.setLocation(roomData[i]);
				break;
			case 2:
				room.setSize(roomData[i]);
				break;
			case 3:
				room.setSmoking(roomData[i]);
				break;
			case 4:
				room.setRate(roomData[i]);
				break;
			case 5:
				room.setDate(roomData[i]);
				break;
			case 6:
				room.setOwner(roomData[i]);
				break;
			}
		}
		return room;
	}
}
