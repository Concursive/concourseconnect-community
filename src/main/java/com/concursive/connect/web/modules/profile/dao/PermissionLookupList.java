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

package com.concursive.connect.web.modules.profile.dao;

import com.concursive.connect.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Represents a list of possible permissions for a permission category
 *
 * @author matt rajkowski
 * @version $Id: PermissionLookupList.java,v 1.1 2003/08/12 03:53:02 matt Exp
 *          $
 * @created August 11, 2003
 */
public class PermissionLookupList extends ArrayList<PermissionLookup> {

  private int categoryId = -1;
  private int includeEnabled = Constants.UNDEFINED;
  private String permission = null;


  /**
   * Constructor for the PermissionLookupList object
   */
  public PermissionLookupList() {
  }


  /**
   * Constructor for the PermissionLookupList object
   *
   * @param db             Description of the Parameter
   * @param categoryId     Description of the Parameter
   * @param includeEnabled Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public PermissionLookupList(Connection db, int categoryId, int includeEnabled) throws SQLException {
    this.categoryId = categoryId;
    this.includeEnabled = includeEnabled;
    this.buildList(db);
  }


  /**
   * Sets the categoryId attribute of the PermissionLookupList object
   *
   * @param tmp The new categoryId value
   */
  public void setCategoryId(int tmp) {
    this.categoryId = tmp;
  }


  /**
   * Sets the includeEnabled attribute of the PermissionLookupList object
   *
   * @param tmp The new includeEnabled value
   */
  public void setIncludeEnabled(int tmp) {
    this.includeEnabled = tmp;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void buildList(Connection db) throws SQLException {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "SELECT * " +
            "FROM lookup_project_permission " +
            "WHERE code > 0 ");
    createFilter(sql);
    sql.append("ORDER BY category_id, level, description ");
    PreparedStatement pst = db.prepareStatement(sql.toString());
    prepareFilter(pst);
    ResultSet rs = pst.executeQuery();
    while (rs.next()) {
      PermissionLookup thisPermission = new PermissionLookup(rs);
      this.add(thisPermission);
    }
    rs.close();
    pst.close();
  }


  /**
   * Description of the Method
   *
   * @param sqlFilter Description of the Parameter
   */
  private void createFilter(StringBuffer sqlFilter) {
    if (sqlFilter == null) {
      sqlFilter = new StringBuffer();
    }
    if (categoryId > -1) {
      sqlFilter.append("AND category_id = ? ");
    }
    if (includeEnabled != Constants.UNDEFINED) {
      sqlFilter.append("AND enabled = ? ");
    }
    if (permission != null) {
      sqlFilter.append("AND permission = ? ");
    }
  }


  /**
   * Description of the Method
   *
   * @param pst Description of the Parameter
   * @return Description of the Return Value
   * @throws SQLException Description of the Exception
   */
  private int prepareFilter(PreparedStatement pst) throws SQLException {
    int i = 0;
    if (categoryId > -1) {
      pst.setInt(++i, categoryId);
    }
    if (includeEnabled != Constants.UNDEFINED) {
      pst.setBoolean(++i, includeEnabled == Constants.TRUE);
    }
    if (permission != null) {
      pst.setString(++i, permission);
    }
    return i;
  }

  public PermissionLookup getByPermission(String permission) {
    for (PermissionLookup permissionLookup : this) {
      if (permission.equals(permissionLookup.getPermission())) {
        return permissionLookup;
      }
    }
    return null;
  }
}

