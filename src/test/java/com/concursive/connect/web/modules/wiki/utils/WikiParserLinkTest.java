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

import com.concursive.commons.db.AbstractConnectionPoolTest;
import com.concursive.connect.Constants;
import com.concursive.connect.cache.utils.CacheUtils;
import com.concursive.connect.web.modules.documents.dao.ImageInfo;
import com.concursive.connect.web.modules.profile.dao.Project;
import com.concursive.connect.web.modules.wiki.dao.Wiki;

import java.util.HashMap;

/**
 * Tests common project database access
 *
 * @author matt rajkowski
 * @created April 15, 2008
 */
public class WikiParserLinkTest extends AbstractConnectionPoolTest {

  protected final static String htmlSample1 =
      "<p>ConcourseSuite is a platform and database independent Java web application executing within a Java Servlet Container, and is licensed under the <a class=\"wikiLink external\" href=\"http://www.centriccrm.com/Portal.do?key=approach&amp;nid=117\" target=\"_blank\">Centric Public License</a>.</p>\n" +
          "<p>Here you will find the most recent technical information concerning ConcourseSuite Community Edition.</p>\n" +
          "<p>The <a class=\"wikiLink\" href=\"/show/some-project/wiki/Subversion+code+repository\">Subversion code repository</a> is where you will find the latest code.</p>\n" +
          "<p><img src=\"/show/some-project/wiki-image/Campus-Shield-Logo.gif\" alt=\"Campus-Shield-Logo.gif\" width=\"386\" height=\"117\" /></p>\n" +
          "<p>" +
          "<a class=\"wikiLink\" href=\"/show/some-project/wiki/Technical+Documentation\">Technical Documentation</a> (Owner)<br />" +
          "<a class=\"wikiLink\" href=\"/show/some-project/wiki/Security%2C+Registration%2C+Invitation\">Installation Options</a><br />" +
          "<a class=\"wikiLink\" href=\"/show/some-project/wiki/Frequently+Asked+Questions\">Frequently Asked Questions</a> " +
          "<a href=\"/show/some-project/topic/3353\">info</a> for you" +
          "</p>\n" +
          "<h3><span>How do I get it?</span></h3>\n" +
          "<p>View our <a class=\"wikiLink external\" href=\"https://www.concursive.com/show/concourseconnect/wiki/Versions\" target=\"_blank\">comparison chart</a> to select the version of ConcourseConnect that&rsquo;s right for you.</p>\n" +
          "<p><a href=\"https://www.concursive.com/try-buy.shtml\"><img src=\"/show/main-profile/wiki-image/Screen+shot+2010-02-04+at+1.59.16+PM.png\" alt=\"Screen shot 2010-02-04 at 1.59.16 PM.png\" width=\"189\" height=\"34\" /></a></p>" +
          "<p>&nbsp;</p>";

  public void testHTMLToWikiLinks() throws Exception {
    // Stage a project for the cache
    Project project = new Project();
    project.setId(9999999);
    project.setTitle("Some Project");
    project.setUniqueId("some-project");
    CacheUtils.updateValue(Constants.SYSTEM_PROJECT_CACHE, 9999999, project);
    CacheUtils.updateValue(Constants.SYSTEM_PROJECT_UNIQUE_ID_CACHE, "some-project", 9999999);
    // Parse it
    String wiki = HTMLToWikiUtils.htmlToWiki(htmlSample1, "", project.getId());
    assertEquals("" +
        "ConcourseSuite is a platform and database independent Java web application executing within a Java Servlet Container, " +
        "and is licensed under the [[http://www.centriccrm.com/Portal.do?key=approach&nid=117 Centric Public License]].\n" +
        "\n" +
        "Here you will find the most recent technical information concerning ConcourseSuite Community Edition.\n" +
        "\n" +
        "The [[Subversion code repository]] is where you will find the latest code.\n" +
        "\n" +
        "[[Image:Campus-Shield-Logo.gif]]\n" +
        "\n" +
        "[[Technical Documentation]] (Owner)\n" +
        "[[Security, Registration, Invitation|Installation Options]]\n" +
        "[[Frequently Asked Questions]] " +
        "[[|9999999:topic|3353|info]] for you\n" +
        "\n" +
        "=== How do I get it? ===\n" +
        "\n" +
        "View our [[https://www.concursive.com/show/concourseconnect/wiki/Versions comparison chart]] to select the version of ConcourseConnect that�s right for you.\n" +
        "\n" +
        "[[Image:Screen shot 2010-02-04 at 1.59.16 PM.png|link=https://www.concursive.com/try-buy.shtml]]\n", wiki);
  }

  public void testWikiLinksToHtml() throws Exception {
    // Test converting a link to html
    String wikiSample = "This is a [[page with a link]].";
    String htmlSample = "<p>This is a <a class=\"wikiLink newWiki\" href=\"/show//wiki/page+with+a+link\">page with a link</a>.</p>\n";
    Wiki wiki = new Wiki();
    wiki.setContent(wikiSample);
    // Parse it
    WikiToHTMLContext wikiContext = new WikiToHTMLContext(wiki, new HashMap<String, ImageInfo>(), -1, false, "");
    String html = WikiToHTMLUtils.getHTML(wikiContext, null);
    assertEquals(htmlSample, html);
  }

  public void testInternalWikiLinksToHtml() throws Exception {
    // Test converting a link to html
    String wikiSample = "Look at [[|our wiki]].";
    String htmlSample = "<p>Look at <a class=\"wikiLink newWiki\" href=\"/show//wiki\">our wiki</a>.</p>\n";
    Wiki wiki = new Wiki();
    wiki.setContent(wikiSample);
    // Parse it
    WikiToHTMLContext wikiContext = new WikiToHTMLContext(wiki, new HashMap<String, ImageInfo>(), -1, false, "");
    String html = WikiToHTMLUtils.getHTML(wikiContext, null);
    assertEquals(htmlSample, html);
  }

  public void testWikiLinkSpacingToHtml() throws Exception {
    // Stage a project and ticket for the cache
    Project project = new Project();
    project.setId(9999999);
    project.setTitle("Some Time");
    project.setUniqueId("some-time");
    CacheUtils.updateValue(Constants.SYSTEM_PROJECT_CACHE, 9999999, project);
    CacheUtils.updateValue(Constants.SYSTEM_PROJECT_UNIQUE_ID_CACHE, "some-project", 9999999);
    CacheUtils.updateValue(Constants.SYSTEM_PROJECT_TICKET_ID_CACHE, "9999999-1", 200);
    // Test converting a link to html
    String wikiSample =
        "This is [[some content]] to " +
            "[[see what|see what ]]happens at " +
            "[[|9999999:profile|some-time|Some Time]] today.";
    String htmlSample =
        "<p>This is <a class=\"wikiLink newWiki\" href=\"/show/some-time/wiki/some+content\">some content</a> to " +
            "<a class=\"wikiLink newWiki\" href=\"/show/some-time/wiki/see+what\">see what</a> happens at " +
            "<a class=\"wikiLink external\" href=\"/show/some-time\">Some Time</a> today.</p>\n";

    Wiki wiki = new Wiki();
    wiki.setProjectId(project.getId());
    wiki.setContent(wikiSample);
    // Parse it
    WikiToHTMLContext wikiContext = new WikiToHTMLContext(wiki, new HashMap<String, ImageInfo>(), -1, false, "");
    String html = WikiToHTMLUtils.getHTML(wikiContext, null);
    assertEquals(htmlSample, html);
  }
}