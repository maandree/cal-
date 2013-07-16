/**
 * cal+ – like cal, but with events
 * 
 * Copyright © 2013  Mattias Andrée (maandree@member.fsf.org)
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.*;


/**
 * A schedule is a table of time spans and time points of events
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@member.fsf.org">maandree@member.fsf.org</a>
 */
public class Schedule
{
    /* Has default constructor */
    
    
    
    /**
     * Events retrievable by {@link #forDay}
     */
    private final HashMap<Integer, ArrayList<Event>> days = new HashMap<Integer, ArrayList<Event>>();
    
    /**
     * Events retrievable by {@link #forWeek}
     */
    private final HashMap<Integer, ArrayList<Event>> weeks = new HashMap<Integer, ArrayList<Event>>();
    
    
    
    /**
     * Gets all events mark for a whole month
     * 
     * @param   year   The year in question
     * @param   month  The month in question
     * @return         Events
     */
    public ArrayList<Event> forMonth(int year, int month)
    {
	return forDay(year, month, 0);
    }
    
    /**
     * Gets all events mark for a whole month
     * 
     * @param   year   The year in question
     * @param   week   The week in question
     * @return         Events
     */
    public ArrayList<Event> forWeek(int year, int week)
    {
	return this.weeks.get(Integer.valueOf(year * 60 + week));
    }
    
    /**
     * Gets all events that starts during a day and is not mark for a whole week or month
     * 
     * @param   year   The year in question
     * @param   month  The month in question
     * @param   day    The day in question, 0 for the whole month (programmer's sugar)
     * @return         Events
     */
    public ArrayList<Event> forDay(int year, int month, int day)
    {
	return this.days.get(Integer.valueOf(year * 40 * 12 + month * 40 + day));
    }
    
    
    /**
     * Adds an event mark for a whole month
     * 
     * @param   year   The year in question
     * @param   month  The month in question
     * @param   event  The event mark
     * @return         Events
     */
    public void addMonth(int year, int month, Event event)
    {
	addDay(year, month, 0, event);
    }
    
    /**
     * Adds an event mark for a whole month
     * 
     * @param   year   The year in question
     * @param   week   The week in question
     * @param   event  The event mark
     * @return         Events
     */
    public void addWeek(int year, int week, Event event)
    {
	Integer key = Integer.valueOf(year * 60 + week);
	if (this.weeks.get(key) == null)
	    this.weeks.put(key, new ArrayList<Event>());
	this.days.get(key).add(event);
    }
    
    /**
     * Adds an event that starts during a day and is not mark for a whole week or month
     * 
     * @param   year   The year in question
     * @param   month  The month in question
     * @param   day    The day in question, 0 for the whole month (programmer's sugar)
     * @param   event  The event mark
     * @return         Events
     */
    public void addDay(int year, int month, int day, Event event)
    {
	Integer key = Integer.valueOf(year * 40 * 12 + month * 40 + day);
	if (this.days.get(key) == null)
	    this.days.put(key, new ArrayList<Event>());
	this.days.get(key).add(event);
    }
    
    
    
    /**
     * An event a text mark on a time span or time point, these include the time but not the date
     */
    public static class Event
    {
	/**
	 * Constructor
	 * 
	 * @param  importance  Importance level, 0 to 3, inclusive
	 * @param  text        The text 
	 * @param  hour        The beginning hour, -1 for the entire day
	 * @param  minute      The beginning minute, -1 for none
	 * @param  second      The beginning second, -1 for none
	 * @param  hourEnd     The end hour, -1 for the entire day
	 * @param  minuteEnd   The end minute, -1 for none
	 * @param  secondEnd   The end second, -1 for none
	 */
	public Event(byte importance, String text, byte hour, byte minute, byte second, byte hourEnd, byte minuteEnd, byte secondEnd)
	{
	    this.importance = importance;
	    this.text = text;
	    this.hour = hour;
	    this.minute = minute;
	    this.second = second;
	    this.hourEnd = hourEnd;
	    this.minuteEnd = minuteEnd;
	    this.secondEnd = secondEnd;
	}
	
	
	
	/**
	 * Importance level, 0 to 3, inclusive
	 */
	public final byte importance;
	
	/**
	 * The text
	 */
	public final String text;
	
	/**
	 * The beginning hour, -1 for the entire day
	 */
	public final byte hour;
	
	/**
	 * The beginning minute, -1 for none
	 */
	public final byte minute;
	
	/**
	 * The beginning second, -1 for none
	 */
	public final byte second;
	
	/**
	 * The end hour, -1 for the entire day
	 */
	public final byte hourEnd;
	
	/**
	 * The end minute, -1 for none
	 */
	public final byte minuteEnd;
	
	/**
	 * The end second, -1 for none
	 */
	public final byte secondEnd;
	
    }
    
}

