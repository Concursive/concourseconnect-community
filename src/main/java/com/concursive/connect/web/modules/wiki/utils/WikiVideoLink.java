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

package com.concursive.connect.web.modules.wiki.utils;

import com.concursive.commons.video.EmbedVideo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Processes the wiki link as a video
 *
 * @author matt rajkowski
 * @created July 2, 2009
 */
public class WikiVideoLink {

  private static Log LOG = LogFactory.getLog(WikiVideoLink.class);

  public static String CRLF = System.getProperty("line.separator");
  private String value = "";
  private boolean needsCRLF = true;

  public WikiVideoLink(String link, boolean editMode, String contextPath) {
    String video = link.substring(6);
    String title = null;
    int thumbnail = -1;
    if (video.indexOf("|") > 0) {
      // the video is first
      video = video.substring(0, video.indexOf("|"));
      // any directives are next
      thumbnail = link.indexOf("|thumb");
      // the optional caption is last
      int last = link.lastIndexOf("|");
      if (last > thumbnail) {
        title = link.substring(last + 1);
      }
    }

    // A video, including alternate text:
    // [[Video:http://www.youtube.com/watch?v=3LkNlTNHZzE|Alternate text]]
    // [[Video:http://www.youtube.com/v/8ab67NJ6wGk]]
    // [[Video:http://www.youtube.com/v/8ab67NJ6wGk|thumb]]

    // Output the video
    value = EmbedVideo.embed(video, thumbnail > -1, editMode) + (editMode ? "&nbsp;" : "");
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean getNeedsCRLF() {
    return needsCRLF;
  }

  public void setNeedsCRLF(boolean needsCRLF) {
    this.needsCRLF = needsCRLF;
  }
}