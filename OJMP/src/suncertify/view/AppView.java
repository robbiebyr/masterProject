package suncertify.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;

import suncertify.common.LostConnectionDialog;
import suncertify.db.RecordNotFoundException;
import suncertify.model.HotelRoom;
import suncertify.model.IAppModel;
import suncertify.server.NewRuntimeException;

/**
 * This class represents the View for the MVC architecture. It handles the GUI
 * aspects of the application in networked and standalone modes.
 * 
 * @author Robbie Byrne
 * 
 */
public class AppView extends JFrame implements Observer {

	private static final long serialVersionUID = -3394046902872034481L;
	private JLabel spacer, noteLabel;
	private final JButton bookButton = new JButton("Book");
	private final JButton searchButton = new JButton("Search");
	private final Border greyBorder = BorderFactory
			.createLineBorder(Color.GRAY);
	private JScrollPane scrollPane;
	private final Dimension panelSize = new Dimension(357, 150);
	private final Dimension tableSize = new Dimension(704, 185);
	private final DefaultListSelectionModel selectionModel = new ForcedListSelectionModel();
	private JTable table;
	private final JTextField custId = new JTextField();
	private JTextField nameField, locationField, custIdTextField;
	private final Color grayBlue = new Color(204, 204, 255, 255);

	private final IAppModel model;
	private HotelRoomTableModel tableModel;
	private Map<Integer, HotelRoom> queryResults;
	private final String[] lastQuery = new String[2];
	private final int MAX_ID_DIGITS = 8;

	/**
	 * This is the constructor for the AppView class.
	 * 
	 * @param model
	 *            This is a reference to the model of the MVC application.
	 * 
	 */
	public AppView(final IAppModel model) {
		this.model = model;
		model.addObserver(this);
	}

	/**
	 * This method is used to create and show the GUI. It is called when the
	 * foundation classes of the application have been instantiated and the
	 * application is ready to be graphically represented. It starts from
	 * setting parameters on the JFrame and then adds JPanels and other
	 * components required by the GUI.
	 */
	public void createAndShowGUI() {

		this.setTitle("URLy Bird (Version 1.2.3)");
		this.setSize(750, 490);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.manageWindowClose();
		this.setJMenuBar(new MenuBar());

		final JPanel content = new JPanel();
		content.add(createSearchPanel());
		content.add(createBookingPanel());
		content.add(createTablePanel());
		content.add(createMessageLabel());

		this.setContentPane(content);
		this.setVisible(true);
	}

	/**
	 * This method is used to populate the JTable with all records in the cache.
	 */
	public void populateTableAllRecords() {
		lastQuery[0] = "";
		lastQuery[1] = "";
		List<HotelRoom> rooms = null;
		String[] columnNames = null;
		try {
			rooms = model.getAllRecords();
			columnNames = model.getRoomFeilds();
		} catch (final NewRuntimeException e) {
			final LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
			lostConnectionDialog.processResponce();
		}

		tableModel = new HotelRoomTableModel(rooms, columnNames);
		table.setModel(tableModel);
	}

	/**
	 * This method is used to populate the JTable with a subset of the records
	 * depending on the criteria for name and location.
	 * 
	 * @param name
	 *            Search term for name field.
	 * @param location
	 *            Search term for location field.
	 */
	public void populateTableSearchQuery(final String name,
			final String location) {
		lastQuery[0] = name;
		lastQuery[1] = location;

		String[] columnNames = null;
		try {
			queryResults = model.queryRecords(name, location);
			columnNames = model.getRoomFeilds();
		} catch (final NewRuntimeException e) {
			final LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
			lostConnectionDialog.processResponce();
		}

		final List<HotelRoom> results = new ArrayList<HotelRoom>();

		final Iterator<Map.Entry<Integer, HotelRoom>> it = queryResults
				.entrySet().iterator();

		while (it.hasNext()) {
			final Map.Entry<Integer, HotelRoom> pairs = it.next();
			results.add(pairs.getValue());
		}

		tableModel = new HotelRoomTableModel(results, columnNames);
		table.setModel(tableModel);
	}

	/**
	 * This method is used to refresh a JTable when it has a subset of Room
	 * records.
	 * 
	 * @throws RecordNotFoundException
	 *             Thrown when search criteria returns no results.
	 */
	public void refreshPopulateTableSearchQuery()
			throws RecordNotFoundException {
		String[] columnNames = null;
		try {
			queryResults = model.queryRecords(lastQuery[0], lastQuery[1]);
			columnNames = model.getRoomFeilds();
		} catch (final NewRuntimeException e) {
			final LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
			lostConnectionDialog.processResponce();
		}

		final List<HotelRoom> results = new ArrayList<HotelRoom>();

		final Iterator<Map.Entry<Integer, HotelRoom>> it = queryResults
				.entrySet().iterator();

		while (it.hasNext()) {
			final Map.Entry<Integer, HotelRoom> pairs = it.next();
			results.add(pairs.getValue());
		}

		tableModel = new HotelRoomTableModel(results, columnNames);
		table.setModel(tableModel);
	}

	/**
	 * This methods adds a keyListener to customer id field.
	 * 
	 * @param keyListener
	 *            KeyListner object.
	 */
	public void addCustIDFieldListener(final KeyListener keyListener) {
		custId.addKeyListener(keyListener);
	}

	/**
	 * This methods adds a ListSelectionListener to the selection model of the
	 * JTable
	 * 
	 * @param selection
	 *            ListSelectionListener object.
	 */
	public void addTableListener(final ListSelectionListener selection) {
		selectionModel.addListSelectionListener(selection);
	}

	/**
	 * This method adds a ActionListener to the Book button.
	 * 
	 * @param buttonListener
	 *            ActionListener object.
	 */
	public void addBookButtonListener(final ActionListener buttonListener) {
		bookButton.addActionListener(buttonListener);
	}

	/**
	 * This method adds a ActionListener to the Search button.
	 * 
	 * @param buttonListener
	 *            ActionListener object.
	 */
	public void addSearchButtonListener(final ActionListener buttonListener) {
		searchButton.addActionListener(buttonListener);
	}

	/**
	 * Method to provide JTable access to the controller.
	 * 
	 * @return Reference to JTable
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * Method to provide customer ID field access to the controller.
	 * 
	 * @return Reference to customer ID field.
	 */
	public JTextField getCustIdField() {
		return custId;
	}

	/**
	 * Method to provide note label access to the controller.
	 * 
	 * @return Reference to note label.
	 */
	public JLabel getNoteLabel() {
		return noteLabel;
	}

	/**
	 * Method to provide Book button access to the controller.
	 * 
	 * @return Reference to book button.
	 */
	public JButton getBookButton() {
		return bookButton;
	}

	/**
	 * Method to provide ScrollPane access to the controller.
	 * 
	 * @return Reference to ScrollPane.
	 */
	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	/**
	 * Method to provide name field access to the controller.
	 * 
	 * @return Reference to name field.
	 */
	public JTextField getNameField() {
		return nameField;
	}

	/**
	 * Method to provide location field access to the controller.
	 * 
	 * @return Reference to location field.
	 */
	public JTextField getLocationField() {
		return locationField;
	}

	@Override
	public void update(final Observable o, final Object arg) {
		tableModel.updateTable();
		try {
			refreshPopulateTableSearchQuery();
		} catch (final RecordNotFoundException e) {
		}
	}

	private void manageWindowClose() {

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent windowEvent) {
				final int reply = new CloseDialog().getResponce();
				if (reply == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	private JPanel createSearchPanel() {
		final JPanel searchPanel = new JPanel(new GridBagLayout());

		searchPanel.setBackground(grayBlue);
		searchPanel.setPreferredSize(panelSize);
		searchPanel.setVisible(true);
		searchPanel.setBorder(greyBorder);

		final GridBagConstraints gridConstraints = new GridBagConstraints();
		gridConstraints.weightx = 0.5;
		gridConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 0;

		final JLabel searchHeading = new JLabel("SEARCH");
		searchPanel.add(searchHeading, gridConstraints);

		final JLabel nameLabel = new JLabel("Name: ");
		gridConstraints.gridy = 1;
		searchPanel.add(nameLabel, gridConstraints);

		nameField = new JTextField();
		gridConstraints.gridx = 1;
		searchPanel.add(nameField, gridConstraints);

		final JLabel locationLabel = new JLabel("Location: ");
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 2;
		searchPanel.add(locationLabel, gridConstraints);

		locationField = new JTextField();
		gridConstraints.gridx = 1;
		searchPanel.add(locationField, gridConstraints);

		spacer = new JLabel();
		spacer.setPreferredSize(new Dimension(20, 30));
		searchPanel.add(spacer, gridConstraints);

		gridConstraints.gridy = 4;
		searchPanel.add(searchButton, gridConstraints);

		return searchPanel;
	}

	private JPanel createBookingPanel() {

		final JPanel bookingPanel = new JPanel(new GridBagLayout());

		bookingPanel.setBackground(grayBlue);
		bookingPanel.setPreferredSize(panelSize);
		bookingPanel.setVisible(true);
		bookingPanel.setBorder(greyBorder);

		final GridBagConstraints gridConstraints = new GridBagConstraints();
		gridConstraints.weightx = 0.5;
		gridConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridConstraints.gridx = 0;
		gridConstraints.gridy = 0;

		final JLabel bookingHeading = new JLabel("BOOKING");
		bookingPanel.add(bookingHeading, gridConstraints);

		final JLabel custLabel = new JLabel("Customer ID: ");
		gridConstraints.gridy = 1;

		bookingPanel.add(custLabel, gridConstraints);

		gridConstraints.gridx = 1;
		custIdTextField = getCustIdTextField();
		bookingPanel.add(custIdTextField, gridConstraints);

		spacer = new JLabel();
		spacer.setPreferredSize(new Dimension(20, 30));

		gridConstraints.gridx = 0;
		gridConstraints.gridy = 2;
		bookingPanel.add(spacer, gridConstraints);

		gridConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridConstraints.gridx = 1;
		gridConstraints.gridy = 3;
		bookButton.setEnabled(false);
		bookingPanel.add(bookButton, gridConstraints);

		return bookingPanel;
	}

	private JPanel createTablePanel() {
		final JPanel tablePanel = new JPanel();
		tablePanel.add(createTable());
		tablePanel.setVisible(true);
		return tablePanel;
	}

	private JTextField getCustIdTextField() {
		custId.setEnabled(false);
		custId.setColumns(MAX_ID_DIGITS);
		return custId;
	}

	private JScrollPane createTable() {
		table = new JTable();
		table.setSelectionModel(selectionModel);
		table.setPreferredScrollableViewportSize(tableSize);
		table.setFillsViewportHeight(true);

		// Create the scroll pane and add the table to it.
		scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		return scrollPane;
	}

	private JPanel createMessageLabel() {
		final JPanel messagePanel = new JPanel();
		noteLabel = new JLabel(
				"Search by Name and/or Location or leave blank to return all results.");
		noteLabel.setForeground(Color.BLUE);
		noteLabel.setPreferredSize(new Dimension(700, 20));
		messagePanel.add(noteLabel);
		messagePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return messagePanel;
	}
}