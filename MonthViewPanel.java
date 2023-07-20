import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Assignment 4 Solution for the MonthViewPanel class
 * @author Henry Wahhab
 * @version 1.0 4/23/23
 */

/**
 * The MonthViewPanel class represents one of the Views and Controllers (combined) in our MVC Pattern for the Calendar GUI
 */

public class MonthViewPanel extends JPanel implements ChangeListener{
	
	private LocalDate date;
	private JTextArea monthYear;
	private JPanel daysPanel;
	private MyCalendar calendar;
	
	/**
	 * Constructs a MonthViewPanel with the associated layout and components
	 * @param cal - the MyCalendar object which the view will represent and the controller will take inputs to change the model
	 * precondition: the parameter is not null
	 */
	public MonthViewPanel(MyCalendar cal) {
		
		calendar = cal;
		date = calendar.getDate();
		JPanel monthPanel = new JPanel(new BorderLayout());
		
		daysPanel = new JPanel(new GridLayout(0, 7, 5, 5));
		daysPanel.setPreferredSize(new Dimension(400, 200));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
		
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton previous = new JButton("←");
		JButton next = new JButton("→");
		
		previous.setFont(new Font("Times New Roman", Font.BOLD, 20));
		next.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		previous.addActionListener(new 
				ActionListener()
				{
					/**
					 * When the previous button is pressed, change model which updates view
					 * @param event - The ActionEvent that occurred
					 */
					 public void actionPerformed(ActionEvent event)
					 {
						 date = date.minusDays(1);
						 calendar.setDate(date);
					 }
				});
		
		next.addActionListener(new 
				ActionListener()
				{
					/**
					 * When the next button is pressed, change model which updates view
					 * @param event - The ActionEvent that occurred
					 */
					 public void actionPerformed(ActionEvent event)
					 {
						 date = date.plusDays(1);
						 calendar.setDate(date);
					 }
				});
		
		buttonPanel.add(previous);
		buttonPanel.add(next);
		
		monthPanel.add(buttonPanel, BorderLayout.NORTH);
		
		monthYear = new JTextArea();
		monthYear.setEditable(false);
		monthYear.setBackground(null);
		monthYear.setText(date.getMonth().toString() + " " + date.getYear());
		monthYear.setFont(new Font("Consolas", Font.BOLD, 20));
		
		updateDaysPanel();
		
		monthPanel.add(daysPanel, BorderLayout.SOUTH);
		centerPanel.add(monthYear);
		monthPanel.add(centerPanel, BorderLayout.CENTER);
		add(monthPanel);
	}
	
	/**
	 * Helper method, updates this panel displaying the month and year, as well as the days in the calendar format
	 */
	private void updateDaysPanel()
	{
		
		daysPanel.removeAll();
		
		String[] daysOfWeek = {" Sun", " Mon", " Tue", " Wed", " Thu", " Fri", " Sat"};
		
		for (int i = 0; i < 7; i ++)
		{
			JTextArea day = new JTextArea();
			day.setFont(new Font("Monospaced", Font.PLAIN, 16));
			day.setEditable(false);
			day.setBackground(null);
			day.setText(daysOfWeek[i]);
			daysPanel.add(day);
		}
		
		// To figure out the day of week of the 1st day of the given month
		LocalDate x = LocalDate.of(date.getYear(), date.getMonth(), 1);
		int firstDay = x.getDayOfWeek().getValue();
		
		if (firstDay != 7) //Sunday corresponds to a value of 7
		{
			for (int i = 0; i < x.getDayOfWeek().getValue(); i ++) 
			{
				daysPanel.add(new JPanel()); //Adds empty JPanels as placeholders
			}
		}
		
		
		for (int i = 1; i <= date.lengthOfMonth(); i ++)
		{
			int j = i;
			
			JButton dayI = new JButton();
			dayI.setBorderPainted(false);
			
			if (i == date.getDayOfMonth())
			{
				dayI.setBorderPainted(true);
				dayI.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
			}
			
			dayI.setFocusPainted(false);
			dayI.setText(Integer.toString(i));
			
			
			dayI.addActionListener(new 
					ActionListener()
					{
				
						/**
						 * When a day of the calendar is pressed, change model which updates view
						 * @param event - The ActionEvent that occurred
						 */
						 public void actionPerformed(ActionEvent event)
						 {
							 date = x;
							 date = date.plusDays(j - 1);
							 calendar.setDate(date);
						 }
					});
			
			daysPanel.add(dayI);
		}
		
		monthYear.setText(date.getMonth().toString() + " " + date.getYear());
		repaint();
		
		daysPanel.revalidate();
		
	}

	@Override
	/**
	 * Calls the updateDaysPanel in order to update the View to represent the Model correctly
	 * @param e - the event representing the change
	 */
	public void stateChanged(ChangeEvent e) 
	{
		updateDaysPanel();
	}
	
}
