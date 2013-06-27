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
	
	String[] output = new String[9];
	for (int i = 0; i < 9; i++)
	    output[i] = "";
	int outlines = 0;
	
	for (int i = -1; i <= 1; i++)
	{
	    cal.set(Calendar.MONTH, month + i);
	    String[] lines = printCalendar(cal, locale, year, month, day).split("\n");
	    int m = lines.length;
	    for (int j = 0; j < m; j++)
	    {
		int len = lines[j].length() - lines[j].replace("\033", "").length();
		len = 29 + len * 8;
		String line = ("  " + lines[j] + "                           ").substring(0, len);
		output[j] += line;
	    }
	    if (outlines < m)
		outlines = m;
	    while (m < 9)
		output[m++] += "                             ";
	}
	
	for (int i = 0; i <= outlines; i++)
	    System.out.println(output[i]);
    }
    
    
    public static String printCalendar(final Calendar cal, final Locale locale, int y, int m, int d)
    {
	String rc = "";
	
	int month = cal.get(Calendar.MONTH);
	int year = cal.get(Calendar.YEAR);
	String head = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
	head += " " + year;
	head = "             ".substring(0, (26 - head.length()) >> 1) + head;
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
	
	rc += firstWeek + ")";
	for (int i = 0; i < weekOffset; i++)
	    rc += "   ";
	int day = 1, wd = firstWeekday, w = firstWeek;
	while (cal.get(Calendar.MONTH) == month)
	{
	    if ((y == year) && (m == month) && (d == day))
	    	rc += " \033[01;07m" + (day < 10 ? " " : "") + day + "\033[21;27m";
	    else
		rc += (day < 10 ? "  " : " ") + day;
	    day++;
	    wd++;
	    cal.add(Calendar.DATE, 1);
	    if ((wd == 7) && (cal.get(Calendar.MONTH) == month))
	    {   wd = 0;
		rc += (w < 10 ? "\n( " : "\n(") + w++ + ")";
	    }
	}
	
	return rc;
    }
    
}

