
/**
 * Assignment 4 Solution for SimpleCalendarTester Class
 * @author Henry Wahhab
 * @version 1.0 4/23/23
 */

/**
 * A class for running the Calendar GUI to be actually used
 */

public class SimpleCalendarTester {

	/**
	 * Creates a MyCalendar object and attaches the associated listeners to it and creates the required panels/frames
	 * @param args - unused
	 */
	public static void main(String[] args)
	{
		CalendarFrame calFrame = new CalendarFrame();
		MyCalendar calendar = new MyCalendar("events.txt");
				
		MonthViewPanel month = new MonthViewPanel(calendar);
		DayViewPanel day = new DayViewPanel(calendar, calFrame);
		
		calFrame.addPanels(month, day);
		
		calendar.attach(month);
		calendar.attach(day);
	}
	
}
