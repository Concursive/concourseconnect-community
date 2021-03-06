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

package com.concursive.connect.web.modules.lists.actions;

import com.concursive.commons.web.mvc.actions.ActionContext;
import com.concursive.connect.Constants;
import com.concursive.connect.cms.portal.dao.ProjectItemList;
import com.concursive.connect.web.controller.actions.GenericAction;
import com.concursive.connect.web.modules.lists.dao.TaskList;
import com.concursive.connect.web.modules.profile.dao.Project;

import java.sql.Connection;
import java.util.StringTokenizer;

/**
 * Description of the Class
 *
 * @author matt rajkowski
 * @version $Id: ProjectManagementListsConfig.java,v 1.12.4.1 2004/08/20 19:48:25
 *          matt Exp $
 * @created December 27, 2007
 */
public final class ProjectManagementListsConfig extends GenericAction {

  public String executeCommandOptions(ActionContext context) {
    Connection db = null;
    //Params
    String projectId = context.getRequest().getParameter("pid");
    try {
      db = this.getConnection(context);
      // Load project, check permissions
      Project thisProject = retrieveAuthorizedProject(Integer.parseInt(projectId), context);
      if (!hasProjectAccess(context, thisProject.getId(), "project-setup-customize")) {
        return "PermissionError";
      }
      context.getRequest().setAttribute("project", thisProject);
      context.getRequest().setAttribute("IncludeSection", "lists_config");
      return ("ProjectCenterOK");
    } catch (Exception errorMessage) {
      context.getRequest().setAttribute("Error", errorMessage);
      return ("SystemError");
    } finally {
      this.freeConnection(context, db);
    }
  }


  public String executeCommandConfigureItemList(ActionContext context) {
    // Load the list for display
    int projectId = -1;
    try {
      projectId = Integer.parseInt(context.getRequest().getParameter("pid"));
    } catch (Exception e) {
      // Not in a project
    }
    String list = context.getRequest().getParameter("list");
    if (!TaskList.isValidTable("lookup_task_" + list)) {
      return "PermissionError";
    }
    Connection db = null;
    try {
      if (projectId == -1 || list == null) {
        return "PermissionError";
      }
      db = getConnection(context);
      // Load project, check permissions
      Project thisProject = retrieveAuthorizedProject(projectId, context);
      if (!hasProjectAccess(context, thisProject.getId(), "project-setup-customize")) {
        return "PermissionError";
      }
      ProjectItemList itemList = new ProjectItemList();
      itemList.setProjectId(projectId);
      itemList.setEnabled(Constants.TRUE);
      itemList.buildList(db, "lookup_task_" + list);
      context.getRequest().setAttribute("editList", itemList.getHtmlSelect());

      // Edit List properties
      context.getRequest().setAttribute(
          "subTitle", "Modify this project's list: " + list);
      context.getRequest().setAttribute(
          "returnUrl", ctx(context) + "/ProjectManagementListsConfig.do?command=SaveItemList&pid=" + thisProject.getId() + "&list=" + list);

      context.getRequest().setAttribute("project", thisProject);
      context.getRequest().setAttribute("IncludeSection", "lists_config_list");
      return ("ProjectCenterOK");

    } catch (Exception errorMessage) {
      context.getRequest().setAttribute("Error", errorMessage);
      return ("SystemError");
    } finally {
      this.freeConnection(context, db);
    }
  }

  public String executeCommandSaveItemList(ActionContext context) {
    // Update the contents of the list
    int projectId = -1;
    try {
      projectId = Integer.parseInt(context.getRequest().getParameter("pid"));
    } catch (Exception e) {
      // Not in a project
    }
    String list = context.getRequest().getParameter("list");
    if (!TaskList.isValidTable("lookup_task_" + list)) {
      return "PermissionError";
    }
    Connection db = null;
    try {
      if (projectId == -1) {
        return "PermissionError";
      }
      db = getConnection(context);
      // Load project, check permissions
      Project thisProject = retrieveAuthorizedProject(projectId, context);
      if (!hasProjectAccess(context, thisProject.getId(), "project-setup-customize")) {
        return "PermissionError";
      }
      // Parse the request for items
      String[] params = context.getRequest().getParameterValues(
          "selectedList");
      String[] names = new String[params.length];
      int j = 0;
      StringTokenizer st = new StringTokenizer(
          context.getRequest().getParameter("selectNames"), "^");
      while (st.hasMoreTokens()) {
        names[j] = st.nextToken();
        if (System.getProperty("DEBUG") != null) {
          System.out.println("ProjectManagementListsConfig-> Item: " + names[j]);
        }
        j++;
      }
      // Load the previous category list
      ProjectItemList itemList = new ProjectItemList();
      itemList.setProjectId(thisProject.getId());
      itemList.buildList(db, "lookup_task_" + list);
      itemList.updateValues(db, params, names, "lookup_task_" + list);
      if ("true".equals(context.getRequest().getParameter("popup"))) {
        return "SaveItemListPopupOK";
      }
      return ("SaveItemListOK");
    } catch (Exception errorMessage) {
      context.getRequest().setAttribute("Error", errorMessage);
      return ("SystemError");
    } finally {
      this.freeConnection(context, db);
    }
  }

}