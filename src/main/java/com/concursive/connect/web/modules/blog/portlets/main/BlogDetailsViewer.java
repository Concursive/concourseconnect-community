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

import com.concursive.connect.web.modules.blog.dao.BlogPost;
import com.concursive.connect.web.modules.blog.dao.BlogPostCommentList;
import com.concursive.connect.web.modules.login.dao.User;
import com.concursive.connect.web.modules.profile.dao.Project;
import com.concursive.connect.web.modules.profile.utils.ProjectUtils;
import com.concursive.connect.web.portal.IPortletViewer;
import static com.concursive.connect.web.portal.PortalUtils.*;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.sql.Connection;

/**
 * Project blog details
 *
 * @author matt rajkowski
 * @created October 28, 2008
 */
public class BlogDetailsViewer implements IPortletViewer {

  // Pages
  private static final String VIEW_PAGE = "/projects_center_news_details.jsp";

  // Object Results
  private static final String BLOG = "blog";
  private static final String BLOG_POST_COMMENT_LIST = "commentList";
  private static final String HIDE_BLOG_DETAILS = "hideBlogDetails";

  public String doView(RenderRequest request, RenderResponse response) throws Exception {
    // The JSP to show upon success
    String defaultView = VIEW_PAGE;

    // Determine the project container to use
    Project project = findProject(request);

    // Check the user's permissions
    User user = getUser(request);
    if (!ProjectUtils.hasAccess(project.getId(), user, "project-news-view")) {
      throw new PortletException("Unauthorized to view this record");
    }

    // Determine the record to show
    int recordId = getPageViewAsInt(request);

    // Load the record
    Connection db = useConnection(request);
    BlogPost thisArticle = new BlogPost(db, recordId, project.getId());
    request.setAttribute(BLOG, thisArticle);

    // Check record to see if user has permission to view this type
    if (thisArticle.getStatus() == BlogPost.DRAFT && !ProjectUtils.hasAccess(project.getId(), user, "project-news-view-unreleased")) {
      throw new PortletException("Unauthorized to view this record");
    } else if (thisArticle.getStatus() == BlogPost.UNAPPROVED && !ProjectUtils.hasAccess(project.getId(), user, "project-news-view-unreleased")) {
      throw new PortletException("Unauthorized to view this record");
    }

    String pageParam = getPageParameter(request);
    if ("comments".equals(pageParam)) {
      request.setAttribute(HIDE_BLOG_DETAILS, "true");
    } else {
      request.setAttribute(HIDE_BLOG_DETAILS, "false");
    }
    if (thisArticle.getId() > -1) {
      // Load comments
      BlogPostCommentList commentList = new BlogPostCommentList();
      commentList.setNewsId(thisArticle.getId());
      commentList.buildList(db);
      request.setAttribute(BLOG_POST_COMMENT_LIST, commentList);
    }
    // Record that this record has been viewed
    processSelectHook(request, thisArticle);

    // JSP view
    return defaultView;
  }
}