package suncertify.view;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import suncertify.model.HotelRoom;

/**
 * This class is used to populate the data in a Jtable.
 * 
 * @author Robbie Byrne
 * 
 */
public class HotelRoomTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -7650374801616779066L;
	private final List<HotelRoom> records;
	private final String[] headings;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd",
			Locale.ENGLISH);

	/**
	 * The constructor for the HotelRoomTableModel class. It requires the
	 * HotelRecords to display and the headings for the table.
	 * 
	 * @param records
	 *            A List of HotelRoom objects.
	 * @param headings
	 *            A String array of record headings.
	 */
	public HotelRoomTableModel(final List<HotelRoom> records,
			final String[] headings) {
		this.records = records;
		this.headings = headings;
	}

	/**
	 * This method is used to update the HotelRoomTableModel and ultimately the
	 * JTable when new data is to be displayed.
	 */
	public void updateTable() {
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return headings.length;
	}

	@Override
	public int getRowCount() {
		return records.size();
	}

	@Override
	public String getValueAt(final int rowIndex, final int columnIndex) {
		final HotelRoom room = records.get(rowIndex);
		String value = "";

		switch (columnIndex) {
		case 0:
			value = room.getName();
			break;
		case 1:
			value = room.getLocation();
			break;
		case 2:
			value = room.getSize();
			break;
		case 3:
			value = room.getSmoking();
			break;
		case 4:
			value = room.getRate();
			break;
		case 5:
			value = room.getDate();
			break;
		case 6:
			value = room.getOwner();
			break;
		}

		return value;
	}

	@Override
	public String getColumnName(final int col) {
		return headings[col];
	}
}