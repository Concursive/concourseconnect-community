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

package com.concursive.connect.web.modules.timesheet.dao;

import com.concursive.commons.date.DateUtils;
import com.concursive.commons.db.DatabaseUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Description of the Class
 *
 * @author matt rajkowski
 * @version $Id$
 * @created November 8, 2004
 */
public class DailyTimesheetList extends HashMap {

  private int userId = -1;
  private Timestamp startDate = null;
  private Timestamp endDate = null;
  private int enteredBy = -1;
  private int modifiedBy = -1;


  /**
   * Constructor for the DailyTimesheetList object
   */
  public DailyTimesheetList() {
  }


  /**
   * Constructor for the ProjectTimesheetList object
   *
   * @param request Description of the Parameter
   */
  public DailyTimesheetList(HttpServletRequest request) {
    int count = -1;
    String projectValue = null;
    // Check project hours
    while ((projectValue = (String) request.getParameter("project" + (++count))) != null) {
      int projectId = Integer.parseInt(projectValue);
      for (int j = 0; j < 7; j++) {
        // Date based
        String dateValue = (String) request.getParameter("line" + count + "day" + j + "date");
        long dateMillis = Long.parseLong(dateValue);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateMillis);
        DailyTimesheet dailyTimesheet = this.getTimesheet(cal);
        // Add any hours
        String hoursValue = ((String) request.getParameter("line" + count + "day" + j));
        if (hoursValue != null) {
          hoursValue = hoursValue.trim();
          if (!"".equals(hoursValue)) {
            try {
              double hours = Double.parseDouble(hoursValue);
              dailyTimesheet.add(hours);
              // Store the hours per day for this project
              dailyTimesheet.getProjectTimesheetList().add(projectId, hours);
            } catch (Exception e) {
              // Invalid character input
            }
          }
        }
        // Check availability
        dailyTimesheet.setUnavailable((String) request.getParameter("unavailable" + j));
        dailyTimesheet.setVacation((String) request.getParameter("vacation" + j));
        dailyTimesheet.setVerified((String) request.getParameter("verified" + j));
      }
    }
  }


  /**
   * Gets the userId attribute of the DailyTimesheetList object
   *
   * @return The userId value
   */
  public int getUserId() {
    return userId;
  }


  /**
   * Sets the userId attribute of the DailyTimesheetList object
   *
   * @param tmp The new userId value
   */
  public void setUserId(int tmp) {
    this.userId = tmp;
  }


  /**
   * Sets the userId attribute of the DailyTimesheetList object
   *
   * @param tmp The new userId value
   */
  public void setUserId(String tmp) {
    this.userId = Integer.parseInt(tmp);
  }


  /**
   * Gets the startDate attribute of the DailyTimesheetList object
   *
   * @return The startDate value
   */
  public Timestamp getStartDate() {
    return startDate;
  }


  /**
   * Sets the startDate attribute of the DailyTimesheetList object
   *
   * @param tmp The new startDate value
   */
  public void setStartDate(Timestamp tmp) {
    this.startDate = tmp;
  }


  /**
   * Sets the startDate attribute of the DailyTimesheetList object
   *
   * @param tmp The new startDate value
   */
  public void setStartDate(String tmp) {
    this.startDate = DatabaseUtils.parseTimestamp(tmp);
  }


  /**
   * Gets the endDate attribute of the DailyTimesheetList object
   *
   * @return The endDate value
   */
  public Timestamp getEndDate() {
    return endDate;
  }


  /**
   * Sets the endDate attribute of the DailyTimesheetList object
   *
   * @param tmp The new endDate value
   */
  public void setEndDate(Timestamp tmp) {
    this.endDate = tmp;
  }


  /**
   * Sets the endDate attribute of the DailyTimesheetList object
   *
   * @param tmp The new endDate value
   */
  public void setEndDate(String tmp) {
    this.endDate = DatabaseUtils.parseTimestamp(tmp);
  }


  /**
   * Gets the enteredBy attribute of the DailyTimesheetList object
   *
   * @return The enteredBy value
   */
  public int getEnteredBy() {
    return enteredBy;
  }


  /**
   * Sets the enteredBy attribute of the DailyTimesheetList object
   *
   * @param tmp The new enteredBy value
   */
  public void setEnteredBy(int tmp) {
    this.enteredBy = tmp;
  }


  /**
   * Sets the enteredBy attribute of the DailyTimesheetList object
   *
   * @param tmp The new enteredBy value
   */
  public void setEnteredBy(String tmp) {
    this.enteredBy = Integer.parseInt(tmp);
  }


  /**
   * Gets the modifiedBy attribute of the DailyTimesheetList object
   *
   * @return The modifiedBy value
   */
  public int getModifiedBy() {
    return modifiedBy;
  }


  /**
   * Sets the modifiedBy attribute of the DailyTimesheetList object
   *
   * @param tmp The new modifiedBy value
   */
  public void setModifiedBy(int tmp) {
    this.modifiedBy = tmp;
  }


  /**
   * Sets the modifiedBy attribute of the DailyTimesheetList object
   *
   * @param tmp The new modifiedBy value
   */
  public void setModifiedBy(String tmp) {
    this.modifiedBy = Integer.parseInt(tmp);
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void buildList(Connection db) throws SQLException {
    if (startDate == null || endDate == null || userId == -1) {
      throw new SQLException("Invalid parameters");
    }
    // Query the database
    int i = 0;
    PreparedStatement pst = db.prepareStatement(
        "SELECT * " +
            "FROM timesheet " +
            "WHERE user_id = ? " +
            "AND entry_date >= ? " +
            "AND entry_date <= ? " +
            "ORDER BY entry_date ");
    pst.setInt(++i, userId);
    pst.setTimestamp(++i, startDate);
    pst.setTimestamp(++i, endDate);
    ResultSet rs = pst.executeQuery();
    while (rs.next()) {
      DailyTimesheet thisTimesheet = new DailyTimesheet(rs);
      add(thisTimesheet);
    }
    rs.close();
    pst.close();
  }


  /**
   * Gets the timesheet attribute of the DailyTimesheetList object
   *
   * @param cal Description of the Parameter
   * @return The timesheet value
   */
  public DailyTimesheet getTimesheet(Calendar cal) {
    SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
    formatter.applyPattern("M/d/yyyy");
    DailyTimesheet thisTimesheet = (DailyTimesheet) this.get(formatter.format(cal.getTime()));
    if (thisTimesheet == null) {
      thisTimesheet = new DailyTimesheet();
      thisTimesheet.setEntryDate(new Timestamp(cal.getTimeInMillis()));
      this.put(formatter.format(cal.getTime()), thisTimesheet);
    }
    return thisTimesheet;
  }

  public boolean isWorkable(Calendar cal) {
    DailyTimesheet thisTimesheet = getTimesheet(cal);
    if (thisTimesheet == null) {
      if (DateUtils.isWeekend(cal)) {
        return false;
      }
      return true;
    } else {
      if (thisTimesheet.getVacation()) {
        return false;
      }
      if (thisTimesheet.getTotalHours() > 0 &&
          DateUtils.isWeekend(cal)) {
        return true;
      }
      if (DateUtils.isWeekend(cal)) {
        return false;
      }
      return true;
    }
  }


  /**
   * Description of the Method
   *
   * @param dailyTimesheet Description of the Parameter
   */
  public void add(DailyTimesheet dailyTimesheet) {
    DailyTimesheet temp = (DailyTimesheet) this.get(dailyTimesheet.getFormattedEntryDate());
    if (temp == null) {
      this.put(dailyTimesheet.getFormattedEntryDate(), dailyTimesheet);
    } else {
      temp.add(dailyTimesheet.getTotalHours());
    }
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void save(Connection db) throws SQLException {
    // TODO: Transaction needed here
    // TODO: Warn if the dailytimesheet was previously submitted before insert
    Iterator i = this.values().iterator();
    while (i.hasNext()) {
      DailyTimesheet dailyTimesheet = (DailyTimesheet) i.next();
      dailyTimesheet.setUserId(userId);
      dailyTimesheet.setEnteredBy(enteredBy);
      dailyTimesheet.setModifiedBy(modifiedBy);
      if (!dailyTimesheet.isSubmitted(db)) {
        dailyTimesheet.delete(db);
        dailyTimesheet.insert(db);
      }
    }
  }

  public static void delete(Connection db, int projectId) throws SQLException {
    // Merge dates into one... or create a new one...
    PreparedStatement pst = db.prepareStatement(
        "UPDATE timesheet_projects " +
            "SET project_id = NULL " +
            "WHERE project_id = ? ");
    pst.setInt(1, projectId);
    pst.execute();
    pst.close();
  }
}

