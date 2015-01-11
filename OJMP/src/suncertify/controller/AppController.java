package suncertify.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import suncertify.db.RecordNotAvailableException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;
import suncertify.model.AppModelInterface;
import suncertify.view.AppView;

/**
 * This is the AppController class which is the controller of the MVC
 * architecture of this application. It has a reference to both the view and the
 * model of the application. - ActionListeners are used by the controller to
 * react to user operations on the view. - The controller may update the model
 * based on these user operations. - The controller may tell the view to change
 * what/how components are shown to end user based on user operations.
 * 
 * @author Robbie Byrne
 * 
 */
public class AppController {

	AppModelInterface model;
	AppView view;
	String owner = "";
	long selectedRecord;
	String enterNumberMessage = "Please enter a 8 digit Customer ID";
	String selectRoomMessage = "Please select a room to book from table";
	int MAX_ID_DIGITS = 8;

	/**
	 * This is the constructor of the AppController class. It has two inputs.
	 * 
	 * @param model
	 *            This is the AppModelInterface.
	 * @param view
	 *            This is the AppView.
	 */
	public AppController(final AppModelInterface model, final AppView view) {
		this.model = model;
		this.view = view;

		view.addTableListener(new TableListener());
		view.addCustIDFieldListener(new custIDListener());
		view.addBookButtonListener(new BookButtonListener());
		view.addSearchButtonListener(new SearchButtonListener());
	}

	/*
	 * Below are inner class ActionListeners used to react to operations by the
	 * end user on the view.
	 */

	class SearchButtonListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			final String name = view.getNameField().getText();
			final String location = view.getLocationField().getText();

			if ((name + location).trim().equals("")) {
				view.populateTableAllRecords();
			} else {
				view.populateTableSearchQuery(name, location);
			}

			if (view.getTable().getRowCount() == 0) {
				view.getNoteLabel()
						.setText(
								"Search by Name and/or Location or leave blank to return all results.");
			} else {
				view.getNoteLabel().setText(selectRoomMessage);
			}

			view.getLocationField().setText("");
			view.getNameField().setText("");
			view.getCustIdField().setText("");
			view.getCustIdField().setEnabled(false);
			view.getTable().getSelectionModel().clearSelection();
			view.getBookButton().setEnabled(false);
		}
	}

	class BookButtonListener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent arg0) {
			owner = view.getCustIdField().getText();
			selectedRecord = view.getTable().getSelectedRow();

			if (view.getTable().getModel().getRowCount() < model
					.getTotalNumberRecords()) {
				selectedRecord = model.getActualIndexMap(selectedRecord);
			}

			try {
				model.updateRecord(selectedRecord, owner, model.getCookie());
				view.getNoteLabel().setText(selectRoomMessage);

			} catch (final RecordNotAvailableException e) {
				view.getNoteLabel().setText(e.getMessage());
				view.update(null, null);
			} catch (final RecordNotFoundException e) {
				view.getNoteLabel().setText(e.getMessage());
				view.update(null, null);
			} catch (final SecurityException e) {
				view.getNoteLabel().setText(e.getMessage());
				view.update(null, null);
			}

			finally {
				view.getBookButton().setEnabled(false);
				view.getCustIdField().setEnabled(false);
				view.getCustIdField().setText("");
			}
		}
	}

	class custIDListener implements KeyListener {

		@Override
		public void keyReleased(final KeyEvent e) {
			final JLabel noteLabel = view.getNoteLabel();

			noteLabel.setText("");

			if (view.getCustIdField().getText().length() == MAX_ID_DIGITS) {
				try {
					final int cust = Integer.parseInt(view.getCustIdField()
							.getText());
					noteLabel
							.setText("Click \"Book\" to Reserve room for Customer: "
									+ cust);
					view.getBookButton().setEnabled(true);
				} catch (final NumberFormatException excep) {
					noteLabel.setText(enterNumberMessage);
					view.getBookButton().setEnabled(false);
				}
			} else if (view.getCustIdField().getText().length() > MAX_ID_DIGITS
					|| view.getCustIdField().getText().length() < MAX_ID_DIGITS) {
				noteLabel.setText(enterNumberMessage);
				view.getBookButton().setEnabled(false);
			}
		}

		@Override
		public void keyPressed(final KeyEvent e) {/* NOT REQUIRED */
		}

		@Override
		public void keyTyped(final KeyEvent e) {/* NOT REQUIRED */
		}
	}

	class TableListener implements ListSelectionListener {

		@Override
		public void valueChanged(final ListSelectionEvent event) {
			if (view.getTable().getSelectedRow() > -1) {
				view.getCustIdField().setEnabled(true);
				view.getNoteLabel().setText(enterNumberMessage);
			}
		}
	}
}
