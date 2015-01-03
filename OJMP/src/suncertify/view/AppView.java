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
import suncertify.model.AppModelInterface;
import suncertify.model.Room;
import suncertify.server.NewRuntimeException;

/**
 * This class represents the View of the MVC architecture. It handles the GUI
 * aspects of the application in networked and standalone modes.
 * 
 * @author Robbie Byrne
 * 
 */
public class AppView extends JFrame implements Observer {

	private static final long serialVersionUID = 1158320825600438864L;

	private JLabel spacer, noteLabel;
	private JButton bookButton = new JButton("Book");
	private JButton searchButton = new JButton("Search");
	private Border greyBorder = BorderFactory.createLineBorder(Color.GRAY);
	private JScrollPane scrollPane;
	private Dimension panelSize = new Dimension(357, 150);
	private Dimension tableSize = new Dimension(704, 185);
	private DefaultListSelectionModel selectionModel = new ForcedListSelectionModel();
	private JTable table;
	private JTextField custId = new JTextField();
	private JTextField nameField, locationField, custIdTextField;
	private Color grayBlue = new Color(204, 204, 255, 255);

	private AppModelInterface model;
	private RoomTableModel tableModel;
	private Map<Integer, Room> queryResults;
	private String[] lastQuery = new String[2];
	private int MAX_ID_DIGITS = 8;

	/**
	 * This is the constructor for the AppView class.
	 * 
	 * @param model
	 *            This is a reference to the model of the MVC application.
	 * 
	 */
	public AppView(AppModelInterface model) {
		this.model = model;
		model.addObserver(this);
	}

	/**
	 * This public method is used to create and show the GUI. It is called when
	 * the foundation classes of the application have been instantiated and the
	 * application is ready to be graphically represented. It starts from
	 * setting parameters on the JFrame and then adds JPanels and other
	 * components required by the GUI.
	 */
	public void createAndShowGUI() {

		this.setTitle("URLy Bird (Version 1.3.2)");
		this.setSize(750, 490);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.manageWindowClose();
		this.setJMenuBar(new MenuBar());

		JPanel content = new JPanel();
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
		List<Room> rooms = null;
		String[] columnNames = null;
		try {
			rooms = model.getAllRecords();
			columnNames = model.getRoomFeilds();
		} catch (NewRuntimeException e) {
			LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
			lostConnectionDialog.processResponce();
		}

		tableModel = new RoomTableModel(rooms, columnNames);
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
	public void populateTableSearchQuery(String name, String location) {
		lastQuery[0] = name;
		lastQuery[1] = location;

		String[] columnNames = null;
		try {
			queryResults = model.queryRecords(name, location);
			columnNames = model.getRoomFeilds();
		} catch (NewRuntimeException e) {
			LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
			lostConnectionDialog.processResponce();
		}

		List<Room> results = new ArrayList<Room>();

		Iterator<Map.Entry<Integer, Room>> it = queryResults.entrySet()
				.iterator();

		while (it.hasNext()) {
			Map.Entry<Integer, Room> pairs = (Map.Entry<Integer, Room>) it
					.next();
			results.add(pairs.getValue());
		}

		tableModel = new RoomTableModel(results, columnNames);
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
		} catch (NewRuntimeException e) {
			LostConnectionDialog lostConnectionDialog = new LostConnectionDialog();
			lostConnectionDialog.processResponce();
		}

		List<Room> results = new ArrayList<Room>();

		Iterator<Map.Entry<Integer, Room>> it = queryResults.entrySet()
				.iterator();

		while (it.hasNext()) {
			Map.Entry<Integer, Room> pairs = (Map.Entry<Integer, Room>) it
					.next();
			results.add(pairs.getValue());
		}

		tableModel = new RoomTableModel(results, columnNames);
		table.setModel(tableModel);
	}

	/**
	 * This methods adds a keyListener to customer id field.
	 * 
	 * @param keyListener
	 *            KeyListner object.
	 */
	public void addCustIDFieldListener(KeyListener keyListener) {
		custId.addKeyListener(keyListener);
	}

	/**
	 * This methods adds a ListSelectionListener to the selection model of the
	 * JTable
	 * 
	 * @param selection
	 *            ListSelectionListener object.
	 */
	public void addTableListener(ListSelectionListener selection) {
		selectionModel.addListSelectionListener(selection);
	}

	/**
	 * This method adds a ActionListener to the Book button.
	 * 
	 * @param buttonListener
	 *            ActionListener object.
	 */
	public void addBookButtonListener(ActionListener buttonListener) {
		bookButton.addActionListener(buttonListener);
	}

	/**
	 * This method adds a ActionListener to the Search button.
	 * 
	 * @param buttonListener
	 *            ActionListener object.
	 */
	public void addSearchButtonListener(ActionListener buttonListener) {
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
	public void update(Observable o, Object arg) {
		tableModel.updateTable();
		try {
			refreshPopulateTableSearchQuery();
		} catch (RecordNotFoundException e) {
			/*
			 * No records returned so none are displayed.
			 */
		}
	}

	private void manageWindowClose() {

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent windowEvent) {
				int reply = new CloseDialog().getResponce();
				if (reply == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	private JPanel createSearchPanel() {
		JPanel searchPanel = new JPanel(new GridBagLayout());

		searchPanel.setBackground(grayBlue);
		searchPanel.setPreferredSize(panelSize);
		searchPanel.setVisible(true);
		searchPanel.setBorder(greyBorder);

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		JLabel searchHeading = new JLabel("SEARCH");
		searchPanel.add(searchHeading, c);

		JLabel nameLabel = new JLabel("Name: ");
		c.gridy = 1;
		searchPanel.add(nameLabel, c);

		nameField = new JTextField();
		c.gridx = 1;
		searchPanel.add(nameField, c);

		JLabel locationLabel = new JLabel("Location: ");
		c.gridx = 0;
		c.gridy = 2;
		searchPanel.add(locationLabel, c);

		locationField = new JTextField();
		c.gridx = 1;
		searchPanel.add(locationField, c);

		spacer = new JLabel();
		spacer.setPreferredSize(new Dimension(20, 30));
		searchPanel.add(spacer, c);

		c.gridy = 4;
		searchPanel.add(searchButton, c);

		return searchPanel;
	}

	private JPanel createBookingPanel() {

		JPanel bookingPanel = new JPanel(new GridBagLayout());

		bookingPanel.setBackground(grayBlue);
		bookingPanel.setPreferredSize(panelSize);
		bookingPanel.setVisible(true);
		bookingPanel.setBorder(greyBorder);

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		JLabel bookingHeading = new JLabel("BOOKING");
		bookingPanel.add(bookingHeading, c);

		JLabel custLabel = new JLabel("Customer ID: ");
		c.gridy = 1;

		bookingPanel.add(custLabel, c);

		c.gridx = 1;
		custIdTextField = getCustIdTextField();
		bookingPanel.add(custIdTextField, c);

		spacer = new JLabel();
		spacer.setPreferredSize(new Dimension(20, 30));

		c.gridx = 0;
		c.gridy = 2;
		bookingPanel.add(spacer, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		bookButton.setEnabled(false);
		bookingPanel.add(bookButton, c);

		return bookingPanel;
	}

	private JPanel createTablePanel() {
		JPanel tablePanel = new JPanel();
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
		JPanel messagePanel = new JPanel();
		noteLabel = new JLabel(
				"Search by Name and/or Location or leave blank to return all results.");
		noteLabel.setForeground(Color.BLUE);
		noteLabel.setPreferredSize(new Dimension(700, 20));
		messagePanel.add(noteLabel);
		messagePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return messagePanel;
	}
}