import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Assignment 4 Solution for CalendarFrame, one of the View classes in the MVC Pattern 
 * @author Henry Wahhab
 * @version 1.0 4/23/23
 */

/*
 * CalendarFrame acts as our highest level container for our GUI and is part of the View in the MVC Pattern
 */

public class CalendarFrame extends JFrame{

	/**
	 * Constructs a CalendarFrame object using BorderLayout
	 */
	public CalendarFrame()
	{
		super("Simple Calendar GUI");
		setLayout(new BorderLayout());
		setSize(900, 400);
		setResizable(false);
	}
	
	/**
	 * Adds the required panels for the Calendar GUI
	 * @param month - the JPanel that holds month info and controls
	 * @param day - the JPanel that holds day info and controls
	 * precondition: The JPanel parameters are correctly displayed
	 */
	public void addPanels(JPanel month, JPanel day)
	{
		add(month, BorderLayout.WEST);
		add(day, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //pack();
	    setVisible(true);
	}
}
