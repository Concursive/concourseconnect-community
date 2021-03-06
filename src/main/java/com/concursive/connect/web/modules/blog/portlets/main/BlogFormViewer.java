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
package com.concursive.connect.web.modules.blog.portlets.main;

import com.concursive.commons.date.DateUtils;
import com.concursive.connect.Constants;
import com.concursive.connect.web.modules.blog.dao.BlogPost;
import com.concursive.connect.web.modules.blog.dao.BlogPostCategoryList;
import com.concursive.connect.web.modules.lists.dao.TaskCategoryList;
import com.concursive.connect.web.modules.login.dao.User;
import com.concursive.connect.web.modules.profile.dao.Project;
import com.concursive.connect.web.modules.profile.utils.ProjectUtils;
import com.concursive.connect.web.portal.IPortletViewer;
import com.concursive.connect.web.portal.PortalUtils;
import static com.concursive.connect.web.portal.PortalUtils.*;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.sql.Connection;

/**
 * Project blog form
 *
 * @author matt rajkowski
 * @created October 28, 2008
 */
public class BlogFormViewer implements IPortletViewer {

  // Pages
  private static final String VIEW_PAGE = "/projects_center_news_add.jsp";

  // Object Results
  private static final String BLOG = "blog";
  private static final String CATEGORY_LIST = "blogCategoryList";
  private static final String LISTS = "taskCategoryList";


  public String doView(RenderRequest request, RenderResponse response) throws Exception {
    // The JSP to show upon success
    String defaultView = VIEW_PAGE;

    // Determine the project container to use
    Project project = findProject(request);

    // Determine the record to show
    int recordId = getPageViewAsInt(request);

    // Check the user's permissions
    User user = getUser(request);
    if (recordId == -1 && !ProjectUtils.hasAccess(project.getId(), user, "project-news-add")) {
      throw new PortletException("Unauthorized to add this record");
    } else if (recordId > -1 && !ProjectUtils.hasAccess(project.getId(), user, "project-news-edit")) {
      throw new PortletException("Unauthorized to modify this record");
    }

    // Determine the database connection
    Connection db = useConnection(request);

    // Check the request for the record and provide a value for the request scope
    BlogPost thisArticle = (BlogPost) PortalUtils.getFormBean(request, BLOG, BlogPost.class);

    // Load the record
    if (recordId > -1) {
      thisArticle.queryRecord(db, recordId);
      // Verify the project id since the request cannot be trusted
      if (thisArticle.getProjectId() != project.getId()) {
        throw new PortletException("Project mismatch");
      }
    }

    // Default the date for the post
    if (thisArticle.getStartDate() == null) {
      thisArticle.setStartDate(DateUtils.roundUpToNextFive());
    }

    // Prepare the list of categories to display
    BlogPostCategoryList categoryList = new BlogPostCategoryList();
    categoryList.setProjectId(project.getId());
    categoryList.setEnabled(Constants.TRUE);
    categoryList.setIncludeId(thisArticle.getCategoryId());
    categoryList.buildList(db);
    request.setAttribute(CATEGORY_LIST, categoryList);

    // Prepare the list of Lists to display
    TaskCategoryList taskCategoryList = new TaskCategoryList();
    taskCategoryList.setProjectId(project.getId());
    taskCategoryList.buildList(db);
    request.setAttribute(LISTS, taskCategoryList);

    // JSP view
    return defaultView;
  }
}