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
 * Mane class
 * 
 * @author  Mattias Andrée, <a href="mailto:maandree@member.fsf.org">maandree@member.fsf.org</a>
 */
public class Program
{
    /**
     * Mane method
     * 
     * @param  args  Command line, excluding executable
     */
    public static void main(final String... args)
    {
	Locale locale = Locale.getDefault();
	Calendar cal = Calendar.getInstance(locale);
	cal.setLenient(true);
	
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH) + 1;
	String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale);
	int day = cal.get(Calendar.DAY_OF_MONTH);
	
	int hour = cal.get(Calendar.HOUR_OF_DAY);
	int minute = cal.get(Calendar.MINUTE);
	int second = cal.get(Calendar.SECOND);
	int milli = cal.get(Calendar.MILLISECOND);
	
	String rep = "";
	cal.add(Calendar.HOUR_OF_DAY, 1);
	if (cal.get(Calendar.HOUR_OF_DAY) == hour)
	    rep = "'";
	cal.add(Calendar.HOUR_OF_DAY, -2);
	if (cal.get(Calendar.HOUR_OF_DAY) == hour)
	    rep = "''";
	cal.add(Calendar.HOUR_OF_DAY, 1);
	
	int weekday = (cal.get(Calendar.DAY_OF_WEEK) % 7 + 5) % 7 + 1;
	String weekdayName = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale);
	int week = cal.get(Calendar.WEEK_OF_YEAR);
	
	TimeZone tz = cal.getTimeZone();
	int offset = tz.getOffset(cal.getTime().getTime());
	boolean negOffset = offset < 0;
	offset = (offset < 0 ? -offset : offset) / (60 * 1000);
	offset = offset / 60 * 100 + offset % 60;
	String timeZoneName = tz.getDisplayName(tz.inDaylightTime(cal.getTime()), TimeZone.SHORT, locale);
	
	System.out.printf("Current time:  %03d-(%02d)%s-%02d %02d:%02d:%02d.%03d%s, %s(%d) w%d, %s%04d %s\n\n",
			  year, month, monthName, day, hour, minute, second, milli, rep,
			  weekdayName, weekday, week,
			  negOffset ? "-" : "+", offset, timeZoneName);
	
	cal.add(Calendar.DATE, 1 - day);
	month--;
	
	Schedule schedule = new Schedule(); /* TODO populate */
	
	int show_year = (1 << 31) - 1;
	for (String arg : args)
	{
	    if (arg.equals("--year"))
		show_year = year;
	    else if (arg.startsWith("--year="))
		show_year = Integer.parseInt(arg.substring(7));
	}
	
	System.out.print("┌───────────────────────────┬");
	System.out.print("───────────────────────────┬");
	System.out.println("───────────────────────────┐");
	
	if (show_year == (1 << 31) - 1)
	    printThreeMonths(cal, locale, -1, year, month, day, schedule);
	else
	{   int m = -month;
	    cal.set(Calendar.YEAR, show_year);
	    for (int i = 0; i < 4; i++)
	    {
		printThreeMonths(cal, locale, m + 3 * i, year, month, day, schedule);
		if (i < 3)
		{   System.out.print("├───────────────────────────┼");
		    System.out.print("───────────────────────────┼");
		    System.out.println("───────────────────────────┤");
	}   }	}
	
	System.out.print("└───────────────────────────┴");
	System.out.print("───────────────────────────┴");
	System.out.println("───────────────────────────┘");
    }
    
    
    /**
     * Colours a text based on events
     * 
     * @param   text    The text to colour
     * @param   events  The events to base the colouring on
     * @return          The text coloured
     */
    public static String colour(String text, ArrayList<Schedule.Event> events)
    {
	int importance = -1;
	if (events != null)
	    for (Schedule.Event event : events)
		importance = event.importance > importance ? event.importance : importance;
	
	if (importance < 0)
	    return text;
	if (importance == 0)
	    return "\033[01;34m" + text + "\033[00m";
	if (importance == 1)
	    return "\033[01;32m" + text + "\033[00m";
	if (importance == 2)
	    return "\033[01;33m" + text + "\033[00m";
	return "\033[01;31m" + text + "\033[00m";
    }
    
    
    /**
     * Uncolours a text
     * 
     * @param   text  The text
     * @return        The text without colour
     */
    public static String uncolour(String text)
    {
	StringBuilder rc = new StringBuilder();
	boolean esc = false;
	char c;
	for (int i = 0, n = text.length(); i < n; i++)
	    if (esc)
		esc = text.charAt(i) != 'm';
	    else if ((c = text.charAt(i)) == '\033')
		esc = true;
	    else
		rc.append(c);
	return rc.toString();
    }
    
    
    /**
     * Prints three months
     * 
     * @param   cal     The calendar
     * @param   locale  The locale
     * @param   first   The first month to print
     * @param   year    The current year
     * @param   month   The current month
     * @param   dday    The current day
     * @return          String to print
     */
    public static void printThreeMonths(Calendar cal, Locale locale, int first, int year, int month, int day, Schedule schedule)
    {
	String[] output = new String[8];
	for (int i = 0; i < 8; i++)
	    output[i] = "│";
	int outlines = 0;
	
	int last = first + 3;
	for (int i = first; i < last; i++)
	{
	    cal.set(Calendar.MONTH, month + i);
	    String[] lines = printMonth(cal, locale, year, month, day, schedule).split("\n");
	    int m = lines.length;
	    for (int j = 0; j < m; j++)
	    {
		int len = 27 + lines[j].length() - uncolour(lines[j]).length();
		String line = (" " + lines[j] + "                          ").substring(0, len);
		output[j] += line + "│";
	    }
	    if (outlines < m)
		outlines = m;
	    while (m < 8)
		output[m++] += "                           │";
	}
	
	for (int i = 0; i < outlines; i++)
	    System.out.println(output[i]);
    }
    
    
    /**
     * Prints a month
     * 
     * @param   cal     The calendar
     * @param   locale  The locale
     * @param   y       The current year
     * @param   m       The current month
     * @param   d       The current day
     * @return          String to print
     */
    public static String printMonth(final Calendar cal, final Locale locale, int y, int m, int d, Schedule schedule)
    {
	String rc = "";
	
	int month = cal.get(Calendar.MONTH);
	int year = cal.get(Calendar.YEAR);
	String head = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
	head += " " + year;
	int monthlen = head.length();
	if ((month == m) && (year == y))
	    head = "\033[01;07m" + head + "\033[21;27m";
	head = "             ".substring(0, (26 - monthlen) >> 1) + colour(head, schedule.forMonth(year, month));
	rc += head + "\n";
	
	int epochWeekday = (cal.getFirstDayOfWeek() % 7 + 5) % 7;
	int firstWeekday = (cal.get(Calendar.DAY_OF_WEEK) % 7 + 5) % 7;
	int firstWeek = cal.get(Calendar.WEEK_OF_YEAR);
	int weekOffset = (7 - epochWeekday + firstWeekday) % 7;
	
	cal.add(Calendar.DATE, -weekOffset);
	rc += "Week";
	for (int i = 0; i < 7; i++)
	{
	    rc += " " + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, locale).substring(0, 2);
	    cal.add(Calendar.DATE, 1);
	}
	cal.add(Calendar.DATE, weekOffset - 7);
	rc += firstWeek < 10 ? "\n( " : "\n(";
	
	rc += colour(Integer.toString(firstWeek), schedule.forWeek(year, firstWeek)) + ")";
	for (int i = 0; i < weekOffset; i++)
	    rc += "   ";
	int day = 1, wd = firstWeekday, w = firstWeek;
	while (cal.get(Calendar.MONTH) == month)
	{
	    if ((d == day) && (m == month) && (y == year))
	    	rc += " " + colour("\033[01;07m" + (day < 10 ? " " : "") + day + "\033[21;27m", schedule.forDay(year, month, day));
	    else
		rc += colour((day < 10 ? "  " : " ") + day, schedule.forDay(year, month, day));
	    day++;
	    wd++;
	    cal.add(Calendar.DATE, 1);
	    if ((wd == 7) && (cal.get(Calendar.MONTH) == month))
	    {   wd = 0;
		/* Last week is 53, not 1 */
		rc += (++w < 10 ? "\n( " : "\n(") + colour(Integer.toString(w), schedule.forWeek(year, w)) + ")";
	    }
	}
	
	return rc;
    }
    
}

