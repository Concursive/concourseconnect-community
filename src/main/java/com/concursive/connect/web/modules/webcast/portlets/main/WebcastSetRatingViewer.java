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
package com.concursive.connect.web.modules.webcast.portlets.main;

import com.concursive.connect.web.portal.IPortletViewer;
import com.concursive.connect.web.portal.PortalUtils;
import static com.concursive.connect.web.portal.PortalUtils.findProject;
import static com.concursive.connect.web.portal.PortalUtils.getUser;
import com.concursive.connect.web.modules.profile.dao.Project;
import com.concursive.connect.web.modules.profile.utils.ProjectUtils;
import com.concursive.connect.web.modules.login.dao.User;
import com.concursive.connect.web.modules.common.social.rating.dao.Rating;
import com.concursive.connect.web.modules.webcast.dao.Webcast;
import com.concursive.connect.Constants;

import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;
import javax.portlet.PortletException;
import java.sql.Connection;

/**
 * Webcast rating ajax command
 *
 * @author Ananth
 * @created Apr 30, 2010
 */
public class WebcastSetRatingViewer implements IPortletViewer {

  public String doView(RenderRequest request, RenderResponse response) throws Exception {

    // Determine the project container to use
    Project project = findProject(request);

    // Check the user's permissions
    User user = getUser(request);
    if (!ProjectUtils.hasAccess(project.getId(), user, "project-webcasts-view")) {
      throw new PortletException("Unauthorized to rate in this project");
    }

    // Determine the database connection
    Connection db = PortalUtils.useConnection(request);

    // Determine the video that is being rated
    int recordId = Integer.parseInt(request.getParameter("id"));

    Webcast webcast = new Webcast(db, recordId);
    if (webcast.getProjectId() != project.getId()) {
      throw new PortletException("PermissionError");
    }

    // The owner of the livestream should not be allowed to rate his own livestream webcast
    if (webcast.getEnteredBy() == user.getId()) {
      throw new PortletException("PermissionError");
    }

    // Parameters
    String vote = request.getParameter("v");

    // Cast the user's vote
    Rating.save(db, user.getId(), project.getId(), webcast.getId(), vote, Webcast.TABLE, Webcast.PRIMARY_KEY, Constants.UNDEFINED);

    // JSP view
    return null;
  }
  
}
