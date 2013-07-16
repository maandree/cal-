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


/**
 * iCalendar parser
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@member.fsf.org">maandree@member.fsf.org</a>
 */
public class ICalendar
{
    /**
     * Constructor
     * 
     * @param  schedule  The schedule to populate
     */
    public ICalendar(Schedule schedule)
    {
	this.schedule = schedule;
    }
    
    
    
    /**
     * The schedule to populate
     */
    private final Schedule schedule;
    
    
    
    /**
     * Feed a line for parsing
     * 
     * @param  line  The line
     */
    public void line(String line)
    {
	while (line.startsWith(" "))
	    line = line.substring(1);
	while (line.endsWith(" "))
	    line = line.substring(0, line.length() - 1);
	/**
	   BEGIN:VEVENT
	   DTSTAMP:19970714T170000Z
	   DTSTART:19970714T170000Z
	   DTEND:19970715T035959Z
	   X-WEEK:201329
	   X-MONTH:201307
	   SUMMARY:Bastille Day Party
	   END:VEVENT
	   
	   how is recurring events done?
	 */
    }
    
}

