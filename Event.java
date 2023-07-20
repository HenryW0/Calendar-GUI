
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * Borrowed from my Assignment 1 Solution for Event Class
 * @author Henry Wahhab
 * @version 1.0 4/23/23
 */

/**
 * The Event class represents a given event using a name and a TimeInterval
 */

public class Event implements Comparable<Event>{

	private String name;
	private TimeInterval ti;
	private LocalDate startingDate;
	private LocalDate endingDate; //Will be null if the event occurs only once
	private String daysOfWeek; //Will be null if the event is a one time event
	
	/**
	 * Constructs an Event using a string as the name and a TimeInterval
	 * @param n - Name of the Event
	 * @param t - TimeInterval associated with the event
	 * @param sd - The starting date for the event, if it is a one time event it is the date it occurs
	 * @param ed - The ending date for the event, if it is a one time event it is null
	 * @param days - The days of the week this recurring event happens on, if this is a one time event it is null
	 * precondition: Valid name for the event and TimeInterval
	 */
	public Event(String n, TimeInterval t, LocalDate sd, LocalDate ed, String days) 
	{
		name = n;
		ti = t;
		startingDate = sd;
		endingDate = ed;
		daysOfWeek = days;
	}
	
	/**
	 * Sets the name of the event using a String
	 * @param n - the String that the name of the event will be set to
	 * precondition: the parameter is a valid String
	 */
	public void setName(String n) 
	{
		name = n;
	}
	
	/**
	 * Sets the time of the event using a TimeInterval
	 * @param t - the TimeInterval that the event will be set to have
	 * precondition: the parameter is a valid TimeInterval
	 */
	public void setTime(TimeInterval t)
	{
		ti = t;
	}
	
	/**
	 * Sets the starting date of the event using a LocalDate, if this is a one time event, this is the day the event occurs on
	 * @param d - the LocalDate for the start of the event
	 * precondition: the parameter is a valid LocalDate
	 */
	public void setStartingDate(LocalDate d)
	{
		startingDate = d;
	}
	
	/**
	 * Sets the ending date of an event using a LocalDate, if this is a one time event it is null
	 * @param d - the LocalDate for the end of the event
	 * precondition: the parameter is a valid LocalDate
	 */
	public void setEndingDate(LocalDate d)
	{
		endingDate = d;
	}
	
	/**
	 * Sets the days of the week this event occurs on if recurring (Examples: MF, TR, WAS), otherwise it is null
	 * @param s - the String that days of the week will be set to
	 * precondition: the parameter is a valid String
	 */
	public void setDaysOfWeek(String s)
	{
		daysOfWeek = s;
	}
	
	/**
	 * Returns the name of this event
	 * @return the name of event
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the TimeInterval for the event 
	 * @return the TimeInterval of the event
	 */
	public TimeInterval getTime()
	{
		return ti;
	}
	
	/**
	 * Returns the start date for the event, this is the day the event occurs if it is a one time event
	 * @return the LocalDate for the start of the event
	 */
	public LocalDate getStartDate()
	{
		return startingDate;
	}
	
	/**
	 * Returns the end date for the event, this is null if it is a one time event
	 * @return the LocalDate for the end of the event
	 */
	public LocalDate getEndDate()
	{
		return endingDate;
	}
	
	/**
	 * Returns the days of the week this recurring event happens on, if it is a one time event it is null
	 * @return a String containing the days this event occurs on (Examples: MF, TR, WAS)
	 */
	public String getDaysOfWeek()
	{
		return daysOfWeek;
	}
	
	/**
	 * Compares which Event starts first and returns a negative integer if this event starts first and a positive integer otherwise
	 * @param other - the other Event being compared to 
	 * @return Returns a negative integer if this event begins first, otherwise a positive integer is returned
	 * precondition: other must be a valid Event with a correct TimeInterval and LocalDate
	 */
	public int compareTo(Event other) 
	{
				
		if (this.getStartDate().isEqual(other.getStartDate()))
		{
			
			if (this.getTime().getStart().isBefore(other.getTime().getStart()))
			{
				return -1;
			}
			
			else 
			{
				return 1;
			}
		}
		
		else 
		{	
			if (this.getStartDate().isBefore(other.getStartDate()))
			{
				return -1;
			}
			
			else 
			{
				return 1;
			}
		}
	}
	
	
	
	/**
	 * Displays the name and the starting and ending time of an event in a string
	 * @return a string in a formatted manner to give information about the event
	 */
	public String toString()
	{		
		String s = "";

			s += this.name + " : " + ti.getStart() + " - " + ti.getEnd();
		
		return s;
	}
	
	
	/**
	 * Displays a one time event in a specific format for the Event List
	 * @return a string containing information for the one time event
	 */
	public String toStringListOneTimeEvent() 
	{
		 
		DateTimeFormatter outputFormatTime = DateTimeFormatter.ofPattern("H:mm");
		
		LocalTime ldtStart = this.getTime().getStart();
		String timeStart = ldtStart.format(outputFormatTime);
		
		LocalTime ldtEnd = this.getTime().getEnd();
		String timeEnd = ldtEnd.format(outputFormatTime);
		
		String s = "";
		
		String day = this.getStartDate().getDayOfWeek().toString();
		day = day.substring(0, 1) + day.substring(1).toLowerCase();
		
		String month = this.getStartDate().getMonth().toString();
		month = month.substring(0, 1) + month.substring(1).toLowerCase();
		
		s += "  " + day + " " + month + " " + this.getStartDate().getDayOfMonth() + ", " + timeStart + " - " + 
		timeEnd + " " + this.getName();
				
		return s;
	}
	


}
