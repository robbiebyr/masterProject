package suncertify.view;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import suncertify.model.Room;

/**
 * This class is used to populate the data in a Jtable.
 * 
 * @author Robbie Byrne
 * 
 */
public class RoomTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 2591068190089882195L;
	private List<Room> records;
	private String[] headings;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd",
			Locale.ENGLISH);

	/**
	 * The constructor for the RoomTableModel class. It requires two inputs.
	 * 
	 * @param records
	 *            A List of Room objects.
	 * @param headings
	 *            A String array of record headings.
	 */
	public RoomTableModel(List<Room> records, String[] headings) {
		this.records = records;
		this.headings = headings;
	}

	/**
	 * This method is used to update the RoomTableModel and ultimately the
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
	public String getValueAt(int rowIndex, int columnIndex) {
		Room room = records.get(rowIndex);
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
	public String getColumnName(int col) {
		return headings[col];
	}
}