
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Borrowed from my Assignment 1 Solution for MyCalendar, code has been adapted to fit as the Model in the MVC Pattern
 * @author Henry Wahhab
 * @version 1.0 4/23/23
 */

/**
 * MyCalendar class stores all types of events and allows for events to be created or removed as well as retrieving events (now acts as Model in MVC)
 */

public class MyCalendar {

	//Whenever calendar is mutated by any mutator method, the update() method will notify the ChangeListeners
	
	private LocalDate date; 
	private ArrayList<ChangeListener> listeners;
	private ArrayList<Event> oneTimeEvents;
	private ArrayList<Event> recEvents;
	private HashMap<LocalDate, ArrayList<Event>> allEvents;
	
	/**
	 * Constructs a Calendar with one time events, recurring events, and all the events in separate data structures
	 * @param oneTime - An ArrayList containing all of the one time events
	 * @param repEvent - An ArrayList containing all of the recurring events
	 * @param totalEvent - A HashMap using LocalDates as keys and ArrayList<Event> as values to contain all of the events
	 * precondition: All ArrayLists and the HashMap must contain valid events 
	 */
	public MyCalendar(ArrayList<Event> oneTime, ArrayList<Event> repeatEvent, HashMap<LocalDate, ArrayList<Event>> totalEvent)
	{
		date = LocalDate.now();
		oneTimeEvents = oneTime;
		recEvents = repeatEvent;
		allEvents = totalEvent;
	}
	
	/**
	 * Constructs a Calendar with one time events, recurring events, and all the events in separate data structures
	 * @param s - A String that is the name of the file that the Calendar will read from to add events
	 * precondition: the parameter lists a valid file name
	*/
	public MyCalendar(String s)
	{
		date = LocalDate.now();
		oneTimeEvents = new ArrayList<Event>();
		recEvents = new ArrayList<Event>();
		allEvents = new HashMap<LocalDate, ArrayList<Event>>();
		listeners = new ArrayList<ChangeListener>();
		
		MyCalendar calendar = new MyCalendar(oneTimeEvents, recEvents, allEvents);
		
		Scanner sc = new Scanner(System.in);
		File inputFile = new File(s);
		
		
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("M/d/yyyy"); 
		DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("H:m");
		
		try 
		{
			Scanner in = new Scanner(inputFile);
			
			while (in.hasNextLine()) 
			{
				
				Event currEvent = new Event(in.nextLine(), null, null, null, null);
				TimeInterval currTime = new TimeInterval(null, null);
				
				String[] strSplit = in.nextLine().split("[ ]+"); //Split string up by any amount of spaces
				
				
				if (strSplit.length == 0) //Checks that line is not empty
				{
					break;
				}
				
				ArrayList<String> strList = new ArrayList<String>(Arrays.asList(strSplit));
				
				boolean oneTime = false;
				
				currTime.setStart(LocalTime.parse(strList.get(1), formatterTime));
				currTime.setEnd(LocalTime.parse(strList.get(2), formatterTime));
				
				currEvent.setTime(currTime);
				
				if (strList.get(0).contains("/")) 
				{
					oneTime = true;
				}
				
				if (oneTime) //One time events
				{	
					calendar.addOneTime(currEvent, LocalDate.parse(strList.get(0), formatterDate));
				}
				
				
				else //Recurring Events
				{
					String days = strList.get(0); //List of days from txt file, for example MW 

					LocalDate firstRec = LocalDate.parse(strList.get(3), formatterDate); //first date of recurring
					LocalDate lastRec = LocalDate.parse(strList.get(4), formatterDate); //last date for recurring
					
					LocalDate currRec = firstRec;
					
					String enumValues = " MTWRFAS";
					ArrayList<Integer> dayValues = new ArrayList<Integer>();
					
					for (int i = 0; i < days.length(); i ++) 
					{
						dayValues.add(i, (enumValues.indexOf("" + days.charAt(i))));
					}
					
					while (currRec.isBefore(lastRec) || currRec.isEqual(lastRec))
					{
						if (dayValues.contains(currRec.getDayOfWeek().getValue()))
						{
							calendar.addRecEvent(currEvent, currRec, firstRec, lastRec, days);
						}	
						currRec = currRec.plusDays(1);
					}
				}
			}
			
			in.close();
			sc.close();
			
		}
		
		catch (FileNotFoundException e) 
		{
		      System.out.println("File could not be found");
		}
	}
	
	
	/**
	 * Adds a one time event to the One Time Events ArrayList if it does not overlap with other events, also adds it to the allEvents HashMap
	 * @param e - the Event to be added to the calendar
	 * @param d - the LocalDate of the event
	 * @throws IllegalArgumentException if the time of the event overlaps with another on that date
	 */
	public void addOneTime(Event e, LocalDate d)
	{
		
		e.setStartingDate(d);
		
		if (allEvents.containsKey(d)) 
		{
			for (Event i : allEvents.get(d))
			{
				TimeInterval j = i.getTime();
				if (e.getTime().compareTo(j) == -1)
				{
					throw new IllegalArgumentException(e.getName() + " occurs at the same time as " + i.getName());
				}
			}
			
			allEvents.get(d).add(e);
		}
		
		else 
		{
			allEvents.put(d, new ArrayList<Event>());
			allEvents.get(d).add(e);
		}
		
		oneTimeEvents.add(e);
		update();
	}
	
	/**
	 * Adds a one time event to the Recurring Events ArrayList if it does not overlap with other events, also adds it to the allEvents HashMap
	 * @param e - the Event to be added to the calendar
	 * @param d - the LocalDate of the event
	 * @throws IllegalArgumentException if the time of the event overlaps with another on that date
	 */
	public void addRecEvent(Event e, LocalDate currDate, LocalDate startDate, LocalDate endDate, String days) 
	{
		
		e.setStartingDate(startDate);
		e.setEndingDate(endDate);
		e.setDaysOfWeek(days);
		
		
		if (allEvents.containsKey(currDate)) 
		{
			for (Event i : allEvents.get(currDate))
			{
				TimeInterval j = i.getTime();
				if (e.getTime().compareTo(j) == -1)
				{
					throw new IllegalArgumentException(e.getName() + " occurs at the same time as " + i.getName());
				}
			}
			
			allEvents.get(currDate).add(e);
		}
		
		else 
		{
			allEvents.put(currDate, new ArrayList<Event>());
			allEvents.get(currDate).add(e);
		}
		
		if (!recEvents.contains(e))
		{
			recEvents.add(e);
		}
		update();
	}
	
	/**
	 * Removes a One Time Event from the calendar by deleting it from the oneTimeEvents ArrayList and the allEvents HashMap
	 * @param e - the event to be removed from the calendar
	 * @param d - the LocalDate in which the event is on 
	 * @throws IllegalArgumentException if the event trying to be deleted does not exist
	 */
	public void removeOneTime(Event e, LocalDate d)
	{
		if (oneTimeEvents.contains(e)) 
		{
			oneTimeEvents.remove(e);
			allEvents.get(d).remove(e);
			
			if (allEvents.get(d).isEmpty())
			{
				allEvents.remove(d);
			}
			
			update();
		}
		
		else
		{
			throw new IllegalArgumentException("No such event exists.");
		}
	}
	
	
	/**
	 * Removes a Recurring Event from the calendar by deleting it from the recEvents ArrayList and the allEvents HashMap
	 * @param e - the event to be removed from the calendar
	 * @param d - the LocalDate in which the event is on 
	 * @throws IllegalArgumentException if the event trying to be deleted does not exist
	 */
	public void removeRecEvent(Event e, LocalDate d)
	{
		try 
		{
			if (recEvents.contains(e))
			{
				recEvents.remove(e);
			}
			
			allEvents.get(d).remove(e);
			
			if (allEvents.get(d).isEmpty())
			{
				allEvents.remove(d);
			}
			
			update();
		}
		
		catch (IllegalArgumentException err)
		{
			throw new IllegalArgumentException("No such event exists.");
		}
	}
	
	/**
	 * Returns all of the events in a HashMap
	 * @return all events
	 */
	public HashMap<LocalDate, ArrayList<Event>> getAllEvents()
	{
		return allEvents;
	}
	
	/**
	 * Returns all of the one time events in an ArrayList
	 * @return all one time events
	 */
	public ArrayList<Event> getOneTimeEvents()
	{
		return oneTimeEvents;
	}
	
	/**
	 * Returns all of the recurring events in an ArrayList
	 * @return all recurring events
	 */
	public ArrayList<Event> getRecEvents()
	{
		return recEvents;
	}
	
	/**
	 * Returns the current date of the calendar
	 * @return the date of the current calendar
	 */
	public LocalDate getDate()
	{
		return date;
	}
	
	/**
	 * Sets the current date of the calendar, notifies the Views after
	 * @param d - the LocalDate that the calendar will be set to
	 */
	public void setDate(LocalDate d)
	{
		date = d;
		update();
	}
	
	/**
	 * Saves the calendar as a text file called events.txt in the specified format that it was read from
	 */
	public void saveCalendar()
	{
		try
		{
			FileWriter out = new FileWriter("events.txt");
			
			Collections.sort(recEvents);
			Collections.sort(oneTimeEvents);
			

			DateTimeFormatter outputFormatDate = DateTimeFormatter.ofPattern("M/d/yyyy"); 
			DateTimeFormatter outputFormatTime = DateTimeFormatter.ofPattern("H:mm");
			
			for (Event e : oneTimeEvents)
			{
				LocalDate ld = e.getStartDate();
				String s1 = ld.format(outputFormatDate);
				
				LocalTime ldtStart = e.getTime().getStart();
				String s2 = ldtStart.format(outputFormatTime);
				
				LocalTime ldtEnd = e.getTime().getEnd();
				String s3 = ldtEnd.format(outputFormatTime);
				
				out.write(e.getName() + "\n" + s1 + " " +s2 + " " + s3 + "\n");
			}
			
			int i = 0;
			
			for (Event e: recEvents)
			{
				LocalTime ldtStart = e.getTime().getStart();
				String s1 = ldtStart.format(outputFormatTime);
				
				LocalTime ldtEnd = e.getTime().getEnd();
				String s2 = ldtEnd.format(outputFormatTime);
				
				LocalDate ldStart = e.getStartDate();
				String s3 = ldStart.format(outputFormatDate);
				
				LocalDate ldEnd = e.getEndDate();
				String s4 = ldEnd.format(outputFormatDate);
				
				out.write(e.getName() + "\n" + e.getDaysOfWeek() + " " + s1 + " "  + s2 + " " + s3 + " " + s4);
				
				if (i != recEvents.size() - 1)
				{
					out.write("\n");
				}
				
				i ++;
			}
			
			out.close();
		}
		
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Error in saving file.");
		}
		
		//System.out.println("events.txt file has been saved.");
	}
	
	 /**
	 * Attach a listener to the Calendar (Model)
	 * @param c - the ChangeListener to be attached
	 */
	 public void attach(ChangeListener c)
	 {
	    listeners.add(c);
	 }
	
	/**
	* Notifies all change listeners when the Calendar is changed
	 */
	 public void update()
	 {
		if (listeners != null)
		{
			for (ChangeListener l : listeners)
		    {
		       l.stateChanged(new ChangeEvent(this));
		    }
		}
	 }
	 
}
