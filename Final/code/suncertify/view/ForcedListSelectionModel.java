package suncertify.view;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

/**
 * This class is used to limit the selection of row on JTable to 1.
 * 
 * @author Robbie Byrne
 * 
 */
public class ForcedListSelectionModel extends DefaultListSelectionModel {

	private static final long serialVersionUID = -184296999983037580L;

	/**
	 * Zero argument constructor which sets the selection mode to single.
	 */
	public ForcedListSelectionModel() {
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@Override
	public void clearSelection() {
		super.clearSelection();
	}

	@Override
	public void removeSelectionInterval(final int index0, final int index1) {
	}
}
