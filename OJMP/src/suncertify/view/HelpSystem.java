package suncertify.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * This class is used to display a step by step guide to searching & booking a record. 
 * @author Robbie Byrne
 *
 */
public class HelpSystem extends JFrame {
	
	private static final long serialVersionUID = -8813001639216621181L;
	
	private ImageIcon icon = new ImageIcon(this.getClass().getResource(("/Info1.png")));
	private JLabel picture = new JLabel(icon);
	private ButtonGroup group = new ButtonGroup();
	private List <JRadioButton> radioButtons = new ArrayList<JRadioButton>();
	private JButton preButton = new JButton("Previous");	
	private JButton nextButton = new JButton("Next");
	private int selection = 1;
	private final int maxSelection = 5;
	private final int minSelection = 1;
	
	
	/**
	 * All the components of the GUI are created in this zero argument constructor.
	 */
	public HelpSystem()
	{
		JPanel parentPanel = new JPanel();
		JPanel infoPanel = new JPanel();
		JPanel radioButtonsPanel = new JPanel();
		
		JRadioButton radioButton1 = new JRadioButton();
		radioButton1.setActionCommand("1");
		radioButton1.addActionListener(new InfoListener());
		radioButton1.setSelected(true);
		
		JRadioButton radioButton2 = new JRadioButton();
		radioButton2.setActionCommand("2");
		radioButton2.addActionListener(new InfoListener());
		
		JRadioButton radioButton3 = new JRadioButton();
		radioButton3.setActionCommand("3");
		radioButton3.addActionListener(new InfoListener());
		
		JRadioButton radioButton4 = new JRadioButton();
		radioButton4.setActionCommand("4");
		radioButton4.addActionListener(new InfoListener());
		
		JRadioButton radioButton5 = new JRadioButton();
		radioButton5.setActionCommand("5");
		radioButton5.addActionListener(new InfoListener());
		
		Dimension frameDimen = new Dimension(650, 480);
		
		preButton.addActionListener(new InfoListener());
		preButton.setEnabled(false);
		
		nextButton.addActionListener(new InfoListener());
		
		infoPanel.add(picture);
		
		radioButtonsPanel.add(preButton);
		
		group.add(radioButton1);
		group.add(radioButton2);
		group.add(radioButton3);
		group.add(radioButton4);
		group.add(radioButton5);

		radioButtons.add(radioButton1);
		radioButtons.add(radioButton2);
		radioButtons.add(radioButton3);
		radioButtons.add(radioButton4);
		radioButtons.add(radioButton5);
		
		radioButtonsPanel.add(radioButton1);
		radioButtonsPanel.add(radioButton2);
		radioButtonsPanel.add(radioButton3);
		radioButtonsPanel.add(radioButton4);
		radioButtonsPanel.add(radioButton5);
    	radioButtonsPanel.add(nextButton);
    	
    	parentPanel.add(infoPanel);
    	parentPanel.add(radioButtonsPanel);
    	
    	this.setTitle("Search & Book a Record Step by Step.");
    	this.add(parentPanel);
    	this.setMinimumSize(frameDimen);
    	this.setResizable(false);
    	this.setLocationRelativeTo(null);
    	this.setVisible(true);	
	}
	
	private void setRadioButton(int radioButtonSelection)
	{
		radioButtons.get(radioButtonSelection - 1).setSelected(true);
		
		if (radioButtonSelection == minSelection)
		{
			preButton.setEnabled(false);
			nextButton.setEnabled(true);
		}
		else if (radioButtonSelection == maxSelection)
		{
			preButton.setEnabled(true);
			nextButton.setEnabled(false);
		}
		else
		{
			preButton.setEnabled(true);
			nextButton.setEnabled(true);
		}
	} 
	
	class InfoListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (arg0.getActionCommand().equalsIgnoreCase("next") )
			{
				if (selection < maxSelection)
				{
					selection++;
				}
			}
			else if (arg0.getActionCommand().equalsIgnoreCase("previous") )
			{
				if (selection > minSelection)
				{
					selection--;
				}
			}
			else
			{
				selection = Integer.parseInt(arg0.getActionCommand());
			}
			
			picture.setIcon(new ImageIcon(this.getClass().getResource("/Info" + selection + ".png")));
			setRadioButton(selection);
		}
	}
}
