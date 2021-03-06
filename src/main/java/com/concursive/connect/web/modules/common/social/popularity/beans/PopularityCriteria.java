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

package com.concursive.connect.web.modules.common.social.popularity.beans;

import com.concursive.connect.Constants;

import java.sql.Timestamp;

/**
 * Reusable properties used for querying popularity
 *
 * @author Kailash Bhoopalam
 * @created June 04, 2008
 */

public class PopularityCriteria {

  private Timestamp endDate = null;
  private Timestamp startDate = null;
  private int limit = -1;
  private String order = null;
  private boolean forProject = false;
  private int forPublic = Constants.UNDEFINED;
  private int forParticipant = Constants.UNDEFINED;
  private int instanceId = Constants.UNDEFINED;

  /**
   * @return the endDate
   */
  public Timestamp getEndDate() {
    return endDate;
  }

  /**
   * @param endDate the endDate to set
   */
  public void setEndDate(Timestamp endDate) {
    this.endDate = endDate;
  }

  /**
   * @return the startDate
   */
  public Timestamp getStartDate() {
    return startDate;
  }

  /**
   * @param startDate the startDate to set
   */
  public void setStartDate(Timestamp startDate) {
    this.startDate = startDate;
  }

  /**
   * @return the limit
   */
  public int getLimit() {
    return limit;
  }

  /**
   * @param limit the limit to set
   */
  public void setLimit(String limit) {
    this.limit = Integer.parseInt(limit);
  }

  /**
   * @param limit the limit to set
   */
  public void setLimit(int limit) {
    this.limit = limit;
  }

  /**
   * @return the order
   */
  public String getOrder() {
    return order;
  }

  /**
   * @param order the order to set
   */
  public void setOrder(String order) {
    this.order = order;
  }

  /**
   * @return the forProject
   */
  public boolean getForProject() {
    return forProject;
  }

  /**
   * @param forProject the forProject to set
   */
  public void setForProject(boolean forProject) {
    this.forProject = forProject;
  }

  /**
   * @return the forPublic
   */
  public int getForPublic() {
    return forPublic;
  }

  /**
   * @param forPublic the forPublic to set
   */
  public void setForPublic(int forPublic) {
    this.forPublic = forPublic;
  }

  public int getForParticipant() {
    return forParticipant;
  }

  public void setForParticipant(int forParticipant) {
    this.forParticipant = forParticipant;
  }

  public int getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(int instanceId) {
    this.instanceId = instanceId;
  }

  public void setInstanceId(String tmp) {
    this.instanceId = Integer.parseInt(tmp);
  }
}
