package suncertify.model;

import java.io.Serializable;

/**
 * This is a HotelRoom object representing a HotelRoom record.
 * 
 * @author Robbie Byrne
 * 
 */
public class HotelRoom implements Serializable {

	private static final long serialVersionUID = -7767316088754306088L;
	private String name;
	private String Location;
	private String size;
	private String smoking;
	private String rate;
	private String date;
	private String owner;
	static int numOfFeilds = 7;

	/**
	 * This method is used to convert a Room object to a String array to conform
	 * with supplied interface DBAccess.
	 * 
	 * @return String array representing a HotelRoom object/record.
	 */
	public String[] toStrArray() {
		String[] toStr = new String[numOfFeilds];
		toStr = new String[] { getName(), getLocation(), getSize(),
				getSmoking(), getRate().toString(), getDate().toString(),
				getOwner() };

		return toStr;
	}

	/**
	 * This method is used to convert a String array into a HotelRoom object.
	 * 
	 * @param roomData
	 *            String array of room record data.
	 * @return HotelRoom object corresponding to a room record.
	 */
	public static HotelRoom strToRoom(final String[] roomData) {
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
	 * 
	 * @return HotelRoom name String.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set room name.
	 * 
	 * @param name
	 *            HotelRoom name String.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Get room location.
	 * 
	 * @return HotelRoom location String.
	 */
	public String getLocation() {
		return Location;
	}

	/**
	 * Set room location.
	 * 
	 * @param location
	 *            HotelRoom location String.
	 */
	public void setLocation(final String location) {
		Location = location;
	}

	/**
	 * Get room size
	 * 
	 * @return HotelRoom size String
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Set Room size
	 * 
	 * @param size
	 *            HotelRoom size String.
	 */
	public void setSize(final String size) {
		this.size = size;
	}

	/**
	 * Get room if smoking is allowed in the room.
	 * 
	 * @return String Y/N smoking String
	 */
	public String getSmoking() {
		return smoking;
	}

	/**
	 * Set if smoking allowed in room.
	 * 
	 * @param smoking
	 *            Y/N String
	 */
	public void setSmoking(final String smoking) {
		this.smoking = smoking;
	}

	/**
	 * Get room rate.
	 * 
	 * @return HotelRoom rate String.
	 */
	public String getRate() {
		return rate;
	}

	/**
	 * Set room rate.
	 * 
	 * @param rate
	 *            HotelRoom rate String.
	 */
	public void setRate(final String rate) {
		this.rate = rate;
	}

	/**
	 * Get room available date
	 * 
	 * @return date String.
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Set room available date.
	 * 
	 * @param date
	 *            HotelRoom available date String
	 */
	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * Get room owner.
	 * 
	 * @return Owner String.
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Set room owner.
	 * 
	 * @param owner
	 *            HotelRoom owner String.
	 */
	public void setOwner(final String owner) {
		this.owner = owner;
	}

	private static HotelRoom returnRoomObject(final String[] roomData) {
		final HotelRoom room = new HotelRoom();

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
