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

package com.concursive.connect.web.modules.reports.dao;

import com.concursive.commons.db.DatabaseUtils;
import com.concursive.commons.web.mvc.beans.GenericBean;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a report that has been added to the system
 *
 * @author matt rajkowski
 * @version $Id$
 * @created October 1, 2003
 */
public class Report extends GenericBean {
  //Report properties
  private int id = -1;
  private int categoryId = -1;
  private int permissionId = -1;
  private String filename = null;
  private int type = -1;
  private String title = null;
  private String description = null;
  private java.sql.Timestamp entered = null;
  private int enteredBy = -1;
  private java.sql.Timestamp modified = null;
  private int modifiedBy = -1;
  private boolean enabled = true;
  private boolean custom = false;
  private boolean userReport = false;
  private boolean adminReport = false;


  /**
   * Constructor for the Report object
   */
  public Report() {
  }


  /**
   * Constructor for the Report object
   *
   * @param rs Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public Report(ResultSet rs) throws SQLException {
    buildRecord(rs);
  }


  /**
   * Constructor for the Report object
   *
   * @param db       Description of the Parameter
   * @param reportId Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public Report(Connection db, int reportId) throws SQLException {
    queryRecord(db, reportId);
  }


  /**
   * Reads the specified reportId and populates this object
   *
   * @param db       Description of the Parameter
   * @param reportId Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void queryRecord(Connection db, int reportId) throws SQLException {
    PreparedStatement pst = db.prepareStatement(
        "SELECT r.* " +
            "FROM report r " +
            "WHERE report_id = ? ");
    pst.setInt(1, reportId);
    ResultSet rs = pst.executeQuery();
    if (rs.next()) {
      buildRecord(rs);
    }
    rs.close();
    pst.close();
    if (id == -1) {
      throw new SQLException("Report record not found.");
    }
  }


  /**
   * Populates this object from a resultset
   *
   * @param rs Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  protected void buildRecord(ResultSet rs) throws SQLException {
    //report table
    id = rs.getInt("report_id");
    //categoryId = DatabaseUtils.getInt(rs, "category_id");
    //permissionId = DatabaseUtils.getInt(rs, "permission_id");
    filename = rs.getString("filename");
    type = rs.getInt("type");
    title = rs.getString("title");
    description = rs.getString("description");
    entered = rs.getTimestamp("entered");
    enteredBy = DatabaseUtils.getInt(rs, "enteredby");
    modified = rs.getTimestamp("modified");
    modifiedBy = DatabaseUtils.getInt(rs, "modifiedby");
    enabled = rs.getBoolean("enabled");
    custom = rs.getBoolean("custom");
    userReport = rs.getBoolean("user_report");
    adminReport = rs.getBoolean("admin_report");
  }


  /**
   * Sets the id attribute of the Report object
   *
   * @param tmp The new id value
   */
  public void setId(int tmp) {
    this.id = tmp;
  }


  /**
   * Sets the id attribute of the Report object
   *
   * @param tmp The new id value
   */
  public void setId(String tmp) {
    this.id = Integer.parseInt(tmp);
  }


  /**
   * Sets the categoryId attribute of the Report object
   *
   * @param tmp The new categoryId value
   */
  public void setCategoryId(int tmp) {
    this.categoryId = tmp;
  }


  /**
   * Sets the categoryId attribute of the Report object
   *
   * @param tmp The new categoryId value
   */
  public void setCategoryId(String tmp) {
    this.categoryId = Integer.parseInt(tmp);
  }


  /**
   * Sets the permissionId attribute of the Report object
   *
   * @param tmp The new permissionId value
   */
  public void setPermissionId(int tmp) {
    this.permissionId = tmp;
  }


  /**
   * Sets the permissionId attribute of the Report object
   *
   * @param tmp The new permissionId value
   */
  public void setPermissionId(String tmp) {
    this.permissionId = Integer.parseInt(tmp);
  }


  /**
   * Sets the filename attribute of the Report object
   *
   * @param tmp The new filename value
   */
  public void setFilename(String tmp) {
    this.filename = tmp;
  }


  /**
   * Sets the type attribute of the Report object
   *
   * @param tmp The new type value
   */
  public void setType(int tmp) {
    this.type = tmp;
  }


  /**
   * Sets the type attribute of the Report object
   *
   * @param tmp The new type value
   */
  public void setType(String tmp) {
    this.type = Integer.parseInt(tmp);
  }


  /**
   * Sets the title attribute of the Report object
   *
   * @param tmp The new title value
   */
  public void setTitle(String tmp) {
    this.title = tmp;
  }


  /**
   * Sets the description attribute of the Report object
   *
   * @param tmp The new description value
   */
  public void setDescription(String tmp) {
    this.description = tmp;
  }


  /**
   * Sets the entered attribute of the Report object
   *
   * @param tmp The new entered value
   */
  public void setEntered(java.sql.Timestamp tmp) {
    this.entered = tmp;
  }


  /**
   * Sets the entered attribute of the Report object
   *
   * @param tmp The new entered value
   */
  public void setEntered(String tmp) {
    this.entered = DatabaseUtils.parseTimestamp(tmp);
  }


  /**
   * Sets the enteredBy attribute of the Report object
   *
   * @param tmp The new enteredBy value
   */
  public void setEnteredBy(int tmp) {
    this.enteredBy = tmp;
  }


  /**
   * Sets the enteredBy attribute of the Report object
   *
   * @param tmp The new enteredBy value
   */
  public void setEnteredBy(String tmp) {
    this.enteredBy = Integer.parseInt(tmp);
  }


  /**
   * Sets the modified attribute of the Report object
   *
   * @param tmp The new modified value
   */
  public void setModified(java.sql.Timestamp tmp) {
    this.modified = tmp;
  }


  /**
   * Sets the modified attribute of the Report object
   *
   * @param tmp The new modified value
   */
  public void setModified(String tmp) {
    this.modified = DatabaseUtils.parseTimestamp(tmp);
  }


  /**
   * Sets the modifiedBy attribute of the Report object
   *
   * @param tmp The new modifiedBy value
   */
  public void setModifiedBy(int tmp) {
    this.modifiedBy = tmp;
  }


  /**
   * Sets the modifiedBy attribute of the Report object
   *
   * @param tmp The new modifiedBy value
   */
  public void setModifiedBy(String tmp) {
    this.modifiedBy = Integer.parseInt(tmp);
  }


  /**
   * Sets the enabled attribute of the Report object
   *
   * @param tmp The new enabled value
   */
  public void setEnabled(boolean tmp) {
    this.enabled = tmp;
  }


  /**
   * Sets the enabled attribute of the Report object
   *
   * @param tmp The new enabled value
   */
  public void setEnabled(String tmp) {
    this.enabled = DatabaseUtils.parseBoolean(tmp);
  }


  /**
   * Sets the custom attribute of the Report object
   *
   * @param tmp The new custom value
   */
  public void setCustom(boolean tmp) {
    this.custom = tmp;
  }


  /**
   * Sets the custom attribute of the Report object
   *
   * @param tmp The new custom value
   */
  public void setCustom(String tmp) {
    this.custom = DatabaseUtils.parseBoolean(tmp);
  }

  public boolean getUserReport() {
    return userReport;
  }

  public void setUserReport(boolean userReport) {
    this.userReport = userReport;
  }

  public void setUserReport(String tmp) {
    userReport = DatabaseUtils.parseBoolean(tmp);
  }

  public boolean getAdminReport() {
    return adminReport;
  }

  public void setAdminReport(boolean adminReport) {
    this.adminReport = adminReport;
  }

  public void setAdminReport(String tmp) {
    adminReport = DatabaseUtils.parseBoolean(tmp);
  }

  /**
   * Gets the id attribute of the Report object
   *
   * @return The id value
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the categoryId attribute of the Report object
   *
   * @return The categoryId value
   */
  public int getCategoryId() {
    return categoryId;
  }


  /**
   * Gets the permissionId attribute of the Report object
   *
   * @return The permissionId value
   */
  public int getPermissionId() {
    return permissionId;
  }


  /**
   * Gets the filename attribute of the Report object
   *
   * @return The filename value
   */
  public String getFilename() {
    return filename;
  }


  /**
   * Gets the type attribute of the Report object
   *
   * @return The type value
   */
  public int getType() {
    return type;
  }


  /**
   * Gets the title attribute of the Report object
   *
   * @return The title value
   */
  public String getTitle() {
    return title;
  }


  /**
   * Gets the description attribute of the Report object
   *
   * @return The description value
   */
  public String getDescription() {
    return description;
  }


  /**
   * Gets the entered attribute of the Report object
   *
   * @return The entered value
   */
  public java.sql.Timestamp getEntered() {
    return entered;
  }


  /**
   * Gets the enteredBy attribute of the Report object
   *
   * @return The enteredBy value
   */
  public int getEnteredBy() {
    return enteredBy;
  }


  /**
   * Gets the modified attribute of the Report object
   *
   * @return The modified value
   */
  public java.sql.Timestamp getModified() {
    return modified;
  }


  /**
   * Gets the modifiedBy attribute of the Report object
   *
   * @return The modifiedBy value
   */
  public int getModifiedBy() {
    return modifiedBy;
  }


  /**
   * Gets the enabled attribute of the Report object
   *
   * @return The enabled value
   */
  public boolean getEnabled() {
    return enabled;
  }


  /**
   * Gets the custom attribute of the Report object
   *
   * @return The custom value
   */
  public boolean getCustom() {
    return custom;
  }


  /**
   * Gets the id of the specified filename
   *
   * @param db       Description of the Parameter
   * @param filename Description of the Parameter
   * @return The idByFilename value
   * @throws SQLException Description of the Exception
   */
  public static int lookupId(Connection db, String filename) throws SQLException {
    int reportId = -1;
    PreparedStatement pst = db.prepareStatement(
        "SELECT report_id " +
            "FROM report " +
            "WHERE filename = ? ");
    pst.setString(1, filename);
    ResultSet rs = pst.executeQuery();
    if (rs.next()) {
      reportId = rs.getInt("report_id");
    }
    rs.close();
    pst.close();
    return reportId;
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @return Description of the Return Value
   * @throws SQLException Description of the Exception
   */
  public boolean insert(Connection db) throws SQLException {
    id = DatabaseUtils.getNextSeq(db, "report_report_id_seq", id);
    PreparedStatement pst = db.prepareStatement(
        "INSERT INTO report " +
            "(" + (id > -1 ? "report_id, " : "") + "filename, type, title, description, enteredby, modifiedby, enabled, custom, user_report, admin_report) " +
            "VALUES (" + (id > -1 ? "?, " : "") + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
    int i = 0;
    if (id > -1) {
      pst.setInt(++i, id);
    }
    //DatabaseUtils.setInt(pst, ++i, categoryId);
    //DatabaseUtils.setInt(pst, ++i, permissionId);
    pst.setString(++i, filename);
    pst.setInt(++i, type);
    pst.setString(++i, title);
    pst.setString(++i, description);
    DatabaseUtils.setInt(pst, ++i, enteredBy);
    DatabaseUtils.setInt(pst, ++i, modifiedBy);
    pst.setBoolean(++i, enabled);
    pst.setBoolean(++i, custom);
    pst.setBoolean(++i, userReport);
    pst.setBoolean(++i, adminReport);
    pst.execute();
    pst.close();
    id = DatabaseUtils.getCurrVal(db, "report_report_id_seq", id);
    return true;
  }

  public void activate(Connection db) throws SQLException {
    PreparedStatement pst = db.prepareStatement(
        "UPDATE report " +
            "SET enabled = ? " +
            "WHERE report_id = ? "
    );
    pst.setBoolean(1, true);
    pst.setInt(2, id);
    pst.executeUpdate();
    pst.close();
  }

  public void disable(Connection db) throws SQLException {
    PreparedStatement pst = db.prepareStatement(
        "UPDATE report " +
            "SET enabled = ? " +
            "WHERE report_id = ? "
    );
    pst.setBoolean(1, false);
    pst.setInt(2, id);
    pst.executeUpdate();
    pst.close();
  }

  public boolean delete(Connection db, String baseFilePath) throws SQLException {
    if (id == -1) {
      return false;
    }
    //Try to delete the file first
    if (filename != null) {
      String filePath = baseFilePath + getFilename();
      File file = new File(filePath);
      if (file.exists()) {
        file.delete();
      }
    }
    //Try to delete the compiled file
    if (filename != null) {
      String filePath = baseFilePath + getFilename();
      if (filePath.indexOf(".xml") > -1) {
        filePath = filePath.substring(
            0, filePath.lastIndexOf(".xml"));
      }
      File file = new File(filePath + ".jasper");
      if (file.exists()) {
        file.delete();
      }
    }
    //Delete the record and associated data
    boolean commit = true;
    try {
      commit = db.getAutoCommit();
      if (commit) {
        db.setAutoCommit(false);
      }

      // Delete any report queues?
      ReportQueueList queueList = new ReportQueueList();
      queueList.setReportId(id);
      queueList.buildList(db);
      String fs = System.getProperty("file.separator");
      File queuePath = new File(baseFilePath);
      String queuePathString = queuePath.getParentFile() + fs + "projects" + fs;
      if (System.getProperty("DEBUG") != null) {
        System.out.println("Report-> QueuePath: " + queuePathString);
      }
      queueList.delete(db, queuePathString);

      // Delete the report
      PreparedStatement pst = db.prepareStatement(
          "DELETE FROM report " +
              "WHERE report_id = ? ");
      pst.setInt(1, id);
      pst.execute();
      pst.close();
      if (commit) {
        db.commit();
      }
    } catch (Exception e) {
      if (commit) {
        db.rollback();
      }
      throw new SQLException(e.getMessage());
    } finally {
      if (commit) {
        db.setAutoCommit(true);
      }
    }
    return true;
  }

  public void updateAsUser(Connection db) throws SQLException {
    PreparedStatement pst = db.prepareStatement(
        "UPDATE report " +
            "SET user_report = ?, " +
            "admin_report = ? " +
            "WHERE report_id = ? "
    );
    pst.setBoolean(1, true);
    pst.setBoolean(2, false);
    pst.setInt(3, id);
    pst.executeUpdate();
    pst.close();
  }

  public void updateAsAdmin(Connection db) throws SQLException {
    PreparedStatement pst = db.prepareStatement(
        "UPDATE report " +
            "SET user_report = ?, " +
            "admin_report = ? " +
            "WHERE report_id = ? "
    );
    pst.setBoolean(1, false);
    pst.setBoolean(2, true);
    pst.setInt(3, id);
    pst.executeUpdate();
    pst.close();
  }
}

