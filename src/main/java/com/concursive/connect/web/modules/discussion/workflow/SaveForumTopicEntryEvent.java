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
package com.concursive.connect.web.modules.discussion.workflow;

import com.concursive.commons.workflow.ComponentContext;
import com.concursive.commons.workflow.ComponentInterface;
import com.concursive.commons.workflow.ObjectHookComponent;
import com.concursive.connect.web.modules.activity.dao.ProjectHistory;
import com.concursive.connect.web.modules.activity.dao.ProjectHistoryList;
import com.concursive.connect.web.modules.discussion.dao.Topic;
import com.concursive.connect.web.modules.login.dao.User;
import com.concursive.connect.web.modules.login.utils.UserUtils;
import com.concursive.connect.web.modules.profile.dao.Project;
import com.concursive.connect.web.modules.profile.utils.ProjectUtils;
import com.concursive.connect.web.modules.wiki.utils.WikiLink;

import java.sql.Connection;

/**
 * Records a project forum topic entry event
 *
 * @author Ananth
 * @created Feb 19, 2009
 */
public class SaveForumTopicEntryEvent extends ObjectHookComponent implements ComponentInterface {
  public final static String HISTORY_QUESTION_TEXT = "history.question.text";
  public final static String HISTORY_TOPIC_TEXT = "history.topic.text";

  public String getDescription() {
    return "Records a project forum topic entry event";
  }

  public boolean execute(ComponentContext context) {
    boolean result = false;
    Connection db = null;

    try {
      db = getConnection(context);

      Topic thisTopic = (Topic) context.getThisObject();
      Topic prevTopic = (Topic) context.getPreviousObject();

      //load the user that submitted the forum topic entry
      User user = UserUtils.loadUser(thisTopic.getEnteredBy());
      Project userProfile = ProjectUtils.loadProject(user.getProfileProjectId());

      // load the project profile
      Project projectProfile = ProjectUtils.loadProject(thisTopic.getProjectId());

      if (prevTopic == null) {
        // Prepare the wiki links
        context.setParameter("user", WikiLink.generateLink(userProfile));
        context.setParameter("topic", WikiLink.generateLink(thisTopic));
        context.setParameter("profile", WikiLink.generateLink(projectProfile));

        // Insert the history
        ProjectHistory history = new ProjectHistory();
        history.setEnteredBy(user.getId());
        history.setProjectId(thisTopic.getProjectId());
        history.setLinkItemId(thisTopic.getId());
        if (thisTopic.getQuestion()) {
          history.setLinkObject(ProjectHistoryList.QUESTION_OBJECT);
          history.setEventType(ProjectHistoryList.POST_FORUM_QUESTION_EVENT);
          history.setDescription(context.getParameter(HISTORY_QUESTION_TEXT));
        } else {
          history.setLinkObject(ProjectHistoryList.TOPIC_OBJECT);
          history.setEventType(ProjectHistoryList.POST_FORUM_TOPIC_EVENT);
          history.setDescription(context.getParameter(HISTORY_TOPIC_TEXT));
        }
        history.insert(db);
      }

      result = true;
    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {
      freeConnection(context, db);
    }
    return result;
  }
}
