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
package com.concursive.connect.web.modules.members.portlets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.*;
import java.io.IOException;

import com.concursive.connect.web.modules.profile.dao.Project;
import com.concursive.connect.web.modules.login.dao.User;
import com.concursive.connect.web.modules.members.utils.TeamMemberUtils;
import com.concursive.connect.web.modules.members.dao.TeamMember;
import static com.concursive.connect.web.portal.PortalUtils.findProject;
import static com.concursive.connect.web.portal.PortalUtils.getUser;
import com.concursive.connect.web.utils.LookupList;
import com.concursive.connect.cache.utils.CacheUtils;

/**
 * Display a member's status and statistics for the profile the user is currently viewing
 *
 * @author Ananth
 * @created Jan 4, 2010
 */
public class MemberProfilePortlet extends GenericPortlet {
  private static Log LOG = LogFactory.getLog(MemberProfilePortlet.class);
  //Pages
  private static final String MEMBER_PROFILE_STATUS = "/portlets/member_profile_status/member_profile_status-view.jsp";
  // Attribute names for objects available in the view
  private static final String MEMBER = "member";
  private static final String MEMBER_ROLE = "memberRole";

  public void doView(RenderRequest request, RenderResponse response)
          throws PortletException, IOException {
    // Determine the project container to use
    Project project = findProject(request);

    // Check the user's permissions
    User user = getUser(request);

    boolean isActive = TeamMemberUtils.isActiveMember(user, project);
    if (isActive) {
      if (user.getProfileProjectId() != project.getId()) {
        TeamMember member = project.getTeam().getTeamMember(user.getId());
        request.setAttribute(MEMBER, member);

        LookupList roleList = CacheUtils.getLookupList("lookup_project_role");
        String roleName = roleList.getValueFromId(roleList.getIdFromLevel(member.getRoleId()));
        request.setAttribute(MEMBER_ROLE, roleName);

        PortletContext context = getPortletContext();
        PortletRequestDispatcher requestDispatcher = context.getRequestDispatcher(MEMBER_PROFILE_STATUS);
        requestDispatcher.include(request, response);
      }
    }
  }
}
