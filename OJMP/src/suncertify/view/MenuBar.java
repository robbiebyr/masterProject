package suncertify.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * This class is used to add a menu bar to the main window of application.
 * @author Robbie Byrne
 *
 */
public class MenuBar extends JMenuBar{
	
	private static final long serialVersionUID = 2600312520925084830L;

	/**
	 * Zero argument constructor which creates menu bar
	 */
	public MenuBar()
	{
		JMenu file = new JMenu("File");
		JMenu help = new JMenu("Help");
		
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		JMenuItem helpMenuItem = new JMenuItem("Search & Book a Record Step by Step");
		
		exitMenuItem.setMnemonic(KeyEvent.VK_E);
		exitMenuItem.setToolTipText("Exit application");
		exitMenuItem.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent event) {
            	
            	int reply = new CloseDialog().getResponce();
            	if (reply == JOptionPane.YES_OPTION)
            	{
            	   System.exit(0);
            	}
            }
        });
		
		helpMenuItem.setMnemonic(KeyEvent.VK_S);
		helpMenuItem.setToolTipText("Application Help");
		helpMenuItem.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent event) {
            	
            	new HelpSystem();
            	
            }
        });
		
		file.add(exitMenuItem);
		help.add(helpMenuItem);
		
		this.add(file);
		this.add(help);
		this.setVisible(true);
	}
}