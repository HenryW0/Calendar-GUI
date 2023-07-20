
import java.time.LocalTime;

/**
 * Borrowed from my Assignment 1 Solution for TimeInterval Class
 * @author Henry Wahhab
 * @version 1.0 4/23/23
 */


/**
 * The TimeInterval class creates periods of time defined by a start and end with LocalTime
 */

public class TimeInterval implements Comparable<TimeInterval>{

	private LocalTime start;
	private LocalTime end;
		
	
	/**
	 * Constructs a interval of time using LocalTime
	 * @param st - Start time of the time interval
	 * @param et - End time of the time interval
	 * precondition: The start LocalTime must be before the end LocalTime
	 */
	public TimeInterval(LocalTime st, LocalTime et) 
	{	
		start = st;
		end = et;
	}
	
	/**
	 * Sets the start time of a time interval
	 * @param s - This value will be set to the start of the time interval
	 * precondition: The start LocalTime must be before the end LocalTime
	 */
	public void setStart(LocalTime s) 
	{
		start = s;
	}
	
	/**
	 * Sets the end time of a time interval
	 * @param e - This value will be set to the end of the time interval
	 * precondition: The end LocalTime must be after the start LocalTime
	 */
	public void setEnd(LocalTime e)
	{
		end = e;
	}
	
	/**
	 * Returns the start LocalTime of this TimeInterval
	 * @return the start LocalTime
	 */
	public LocalTime getStart()
	{
		return start;
	}
	
	/**
	 * Returns the end LocalTime of this TimeInterval
	 * @return the end LocalTime
	 */
	public LocalTime getEnd()
	{
		return end;
	}
	
	
	/**
	 * Compares two TimeIntervals to determine whether or not they overlap
	 * @param other - The other TimeInterval being compared to
	 * @return a positive integer if the TimeIntervals do not overlap, otherwise a negative integer
	 * precondition: other must be a valid TimeInterval
	 */
	public int compareTo(TimeInterval other) 
	{
		
		if (this.start.isAfter(other.start) && this.start.isBefore(other.end))
		{
			return -1; 
		}
		
		if (this.end.isAfter(other.start) && this.end.isBefore(other.end))
		{
			return -1;
		}
		
		if (this.start.equals(other.getStart()))
		{
			return -1;
		}
		 
		if (this.end.equals(other.getStart()))
		{
			return -1;
		}
		
		if (this.start.equals(other.getEnd()))
		{
			return -1;
		}
		
		return 1;
	}
	
	
}
