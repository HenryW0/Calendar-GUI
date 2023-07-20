import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Assignment 4 Solution for the DayViewPanel class
 * @author Henry Wahhab
 * @version 1.0 4/23/23
 */

/**
 * The DayViewPanel class represents one of the Views and Controllers (combined) in our MVC Pattern for the Calendar GUI
 */

public class DayViewPanel extends JPanel implements ChangeListener {

	private LocalDate date;
	private JTextArea events;
	private JTextArea dateString;
	private JPanel dayView;
	private MyCalendar calendar;
	private CalendarFrame parent;
	
	/**
	 * Constructs a DayViewPanel with the associated layout and components
	 * @param cal - the MyCalendar object which the view will represent and the controller will take inputs to change the model
	 * @param f - the frame that this JPanel will be placed in (needed so that the quit button can dispose the frame)
	 * precondition: the parameters are not null
	 */
	public DayViewPanel(MyCalendar cal, CalendarFrame f)
	{
		calendar = cal;
		date = calendar.getDate();
		parent = f;
		
		dayView = new JPanel(new BorderLayout());
		
		JPanel dateTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
		dateString = new JTextArea();
		dateString.setEditable(false);
		dateString.setBackground(null);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton createButton = new JButton("Create");
		createButton.setBackground(new Color(190, 0, 0));
		createButton.setForeground(Color.WHITE);
		createButton.setPreferredSize(new Dimension(120, 35));
		
		createButton.addActionListener(new 
				ActionListener()
				{
			
					/**
					 * When the createButton is pressed, bring up a pop up menu
					 * @param event - The ActionEvent that occurred
					 */
					 public void actionPerformed(ActionEvent event)
					 {
						 //3 Text Fields for each part of the event
						 JTextField titleField = new JTextField();
					     JTextField startField = new JTextField();
					     JTextField endField = new JTextField();
						 
						 JPanel popUp = new JPanel();
					     popUp.setLayout(new GridLayout(3, 2));
					     
					     //Add all the text fields
					     popUp.add(new JLabel("Title:"));
					     popUp.add(titleField);
					     popUp.add(new JLabel("Start Time:"));
					     popUp.add(startField);
					     popUp.add(new JLabel("End Time:"));
					     popUp.add(endField);
					     
					     Object[] options = {"Save", "Cancel"};
					     
					     //Option Dialog to provide different options
					     int result = JOptionPane.showOptionDialog(null, popUp, "Create Event", JOptionPane.YES_NO_OPTION,
					    		    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					     
					     if (result == 0)
					     { 
					    	 DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:m");
					    	 
					    	 LocalTime st = LocalTime.parse(startField.getText(), formatterTime);
					    	 LocalTime et = LocalTime.parse(endField.getText(), formatterTime);
					    	 
					    	 TimeInterval createTI = new TimeInterval(st, et);
					    	 Event createEvent = new Event(titleField.getText(), createTI, date, null, null);
					    	 
					    	 try 
					    	 {
					    		 calendar.addOneTime(createEvent, date);
					    	 }
					    	 
					    	 catch (Exception e)
					    	 {
					    		 JOptionPane.showMessageDialog(null, e.getMessage() + "\nPlease enter an event without a time conflict.", 
					    				 "Time Conflict", JOptionPane.ERROR_MESSAGE);
					    	 }
					    	 
					     }
					     
					 }
				});
		
		JButton quitButton = new JButton("Quit");
		quitButton.setPreferredSize(new Dimension(120, 35));
		
		quitButton.addActionListener(new 
				ActionListener()
				{
					/**
					 * When the quitButton is pressed, save the contents of the model in a txt file and exit
					 * @param event - The ActionEvent that occurred
					 */
					 public void actionPerformed(ActionEvent event)
					 {
						 calendar.saveCalendar();
						 parent.dispose();
					 }
				});
		
		buttonPanel.add(createButton);
		buttonPanel.add(quitButton);
		
		events = new JTextArea();
		events.setFont(new Font("Monospaced", Font.PLAIN, 16));
		events.setEditable(false);
		events.setBackground(null);
		
		dateString.setFont(new Font("Consolas", Font.BOLD, 20));
		dateString.append(date.getDayOfWeek() + " ");
		dateString.append(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		dateTitle.add(dateString);
		
		
		setEventList();
		
		dayView.add(buttonPanel, BorderLayout.NORTH);
		dayView.add(dateTitle, BorderLayout.CENTER);
		dayView.add(events, BorderLayout.SOUTH);
		add(dayView);
	}

	/**
	 * Responsible for displaying all events that occur on this day, sorted chronologically
	 */
	public void setEventList()
	{
		events.setText("");
		
		if (calendar.getAllEvents().get(date) == null)
		{
			events.append("No Events Schedueled Today.");
		}
		
		else 
		{	
			ArrayList<Event> todayEvents = calendar.getAllEvents().get(date);
			
			Comparator<Event> comparatorByTime = new
				Comparator<Event>()
				{
				     /**
				      * Compares events based on time in order to sort chronologically
				      * @return Returns a positive integer if this even occurs after the other, a negative otherwise
				      */
					 public int compare(Event event1, Event event2)
					 { 
						 if (event1.getTime().getEnd().isBefore(event2.getTime().getStart()))
						 {
							 return -1;
						 }

						 return 1;
					 }
				};
			
			Collections.sort(todayEvents, comparatorByTime);
					 
			for (Event e : todayEvents)
			{
				events.append(e.toString() + "\n");
			}
		}
	}
	
	@Override
	/**
    * Called when MyCalendar (model) is changed, updates the view portion displaying the current date and events
    * @param e - the event representing the change
    */
	public void stateChanged(ChangeEvent e) 
	{
		date = calendar.getDate();
		dateString.setText("");
		dateString.append(date.getDayOfWeek() + " ");
		dateString.append(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		setEventList();
	}

	
}
