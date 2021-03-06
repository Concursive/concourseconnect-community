/*
 * ConcourseConnect
 * Copyright 2009 Concursive Corporation
 * http://www.concursive.com
 *
 * This file is part of ConcourseConnect, an open source social business
 * software and community platform.
 *
 * Concursive ConcourseConnect is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, version 3 of the License.
 *
 * Under the terms of the GNU Affero General Public License you must release the
 * complete source code for any application that uses any part of ConcourseConnect
 * (system header files and libraries used by the operating system are excluded).
 * These terms must be included in any work that has ConcourseConnect components.
 * If you are developing and distributing open source applications under the
 * GNU Affero General Public License, then you are free to use ConcourseConnect
 * under the GNU Affero General Public License.
 *
 * If you are deploying a web site in which users interact with any portion of
 * ConcourseConnect over a network, the complete source code changes must be made
 * available.  For example, include a link to the source archive directly from
 * your web site.
 *
 * For OEMs, ISVs, SIs and VARs who distribute ConcourseConnect with their
 * products, and do not license and distribute their source code under the GNU
 * Affero General Public License, Concursive provides a flexible commercial
 * license.
 *
 * To anyone in doubt, we recommend the commercial license. Our commercial license
 * is competitively priced and will eliminate any confusion about how
 * ConcourseConnect can be used and distributed.
 *
 * ConcourseConnect is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ConcourseConnect.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Attribution Notice: ConcourseConnect is an Original Work of software created
 * by Concursive Corporation
 */

package com.concursive.connect.web.modules.calendar.utils.holidays;


import com.concursive.connect.web.modules.calendar.utils.CalendarEvent;
import com.concursive.connect.web.modules.calendar.utils.CalendarEventList;
import com.concursive.connect.web.modules.calendar.utils.CalendarView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * US Holidays for the CalendarView class
 *
 * @author matt rajkowski
 * @version $Id$
 * @created November 17, 2004
 */
public class USHolidays {

  /**
   * Adds a feature to the To attribute of the USHolidays class
   *
   * @param calendarView The feature to be added to the To attribute
   * @param theYear      The feature to be added to the To attribute
   */
  public final static void addTo(CalendarView calendarView, int theYear) {

    Calendar tmpCal = new GregorianCalendar();
    CalendarEvent thisEvent = null;
    int dayOfWeek = -1;

    //New Year's Day
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("New Year's Day");
    calendarView.addEvent(
        "1/1/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);

    //Martin Luther Kings birthday : third Monday in January;
    thisEvent = new CalendarEvent();
    tmpCal.set(theYear, Calendar.JANUARY, 1);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    while (dayOfWeek != Calendar.MONDAY) {
      tmpCal.add(Calendar.DATE, 1);
      dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    }
    thisEvent.setSubject("Martin Luther King's Birthday");
    calendarView.addEvent(
        "1/" + (tmpCal.get(Calendar.DATE) + 14) + "/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);

    //Washington's birthday : third Monday in February; (President's Day)
    tmpCal.set(theYear, Calendar.FEBRUARY, 1);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    while (dayOfWeek != Calendar.MONDAY) {
      tmpCal.add(Calendar.DATE, 1);
      dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    }
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("President's Day");
    calendarView.addEvent(
        "2/" + (tmpCal.get(Calendar.DATE) + 14) + "/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);

    //Easter Sunday
    tmpCal = EasterHoliday.getCalendar(theYear);
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("Easter");
    calendarView.addEvent(
        (tmpCal.get(Calendar.MONTH) + 1) + "/" + tmpCal.get(Calendar.DATE) + "/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);

    //Memorial Day : last Monday in May;
    tmpCal.set(theYear, Calendar.MAY, 1);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    while (dayOfWeek != Calendar.MONDAY) {
      tmpCal.add(Calendar.DATE, 1);
      dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    }
    //With the first Monday, see if May has 4 or 5 Mondays
    tmpCal.add(Calendar.DATE, 28);
    if (tmpCal.get(Calendar.MONTH) != Calendar.MAY) {
      tmpCal.add(Calendar.DATE, -7);
    }
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("Memorial Day");
    calendarView.addEvent(
        "5/" + (tmpCal.get(Calendar.DATE)) + "/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);

    //Independence Day : July 4 (moved to Monday if Sunday, Friday if Saturday);
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("Independence Day");
    calendarView.addEvent(
        "7/4/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    tmpCal.set(theYear, Calendar.JULY, 4);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == Calendar.SUNDAY) {
      thisEvent.setSubject("Independence Day (Bank Holiday)");
      calendarView.addEvent(
          "7/5/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    } else if (dayOfWeek == Calendar.SATURDAY) {
      thisEvent.setSubject("Independence Day (Bank Holiday)");
      calendarView.addEvent(
          "7/3/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    }

    //Labor Day : first Monday in September;
    tmpCal.set(theYear, Calendar.SEPTEMBER, 1);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    while (dayOfWeek != Calendar.MONDAY) {
      tmpCal.add(Calendar.DATE, 1);
      dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    }
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("Labor Day");
    calendarView.addEvent(
        "9/" + (tmpCal.get(Calendar.DATE)) + "/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);

    //Columbus Day : second Monday in October;
    tmpCal.set(theYear, Calendar.OCTOBER, 1);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    while (dayOfWeek != Calendar.MONDAY) {
      tmpCal.add(Calendar.DATE, 1);
      dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    }
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("Columbus Day");
    calendarView.addEvent(
        "10/" + (tmpCal.get(Calendar.DATE) + 7) + "/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);

    //Veteran's Day : November 11 (moved to Monday if Sunday, Friday if Saturday);
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("Veteran's Day");
    calendarView.addEvent(
        "11/11/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    tmpCal.set(theYear, Calendar.NOVEMBER, 11);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == Calendar.SUNDAY) {
      thisEvent.setSubject("Veteran's Day (Bank Holiday)");
      calendarView.addEvent(
          "11/12/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    } else if (dayOfWeek == Calendar.SATURDAY) {
      thisEvent.setSubject("Veteran's Day (Bank Holiday)");
      calendarView.addEvent(
          "11/10/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    }

    //Thanksgiving Day : fourth Thursday in November;
    tmpCal.set(theYear, Calendar.NOVEMBER, 1);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    while (dayOfWeek != Calendar.THURSDAY) {
      tmpCal.add(Calendar.DATE, 1);
      dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    }
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("Thanksgiving Day");
    calendarView.addEvent(
        "11/" + (tmpCal.get(Calendar.DATE) + 21) + "/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);

    //Christmas : December 25 (moved to Monday if Sunday or Friday if Saturday);
    thisEvent = new CalendarEvent();
    thisEvent.setSubject("Christmas Day");
    calendarView.addEvent(
        "12/25/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    //*
    thisEvent = new CalendarEvent();
    tmpCal.set(theYear, Calendar.DECEMBER, 25);
    dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == Calendar.SUNDAY) {
      thisEvent.setSubject("Christmas Day (Bank Holiday)");
      calendarView.addEvent(
          "12/26/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    } else if (dayOfWeek == Calendar.SATURDAY) {
      thisEvent.setSubject("Christmas Day (Bank Holiday)");
      calendarView.addEvent(
          "12/24/" + theYear, CalendarEventList.EVENT_TYPES[7], thisEvent);
    }
  }
}

