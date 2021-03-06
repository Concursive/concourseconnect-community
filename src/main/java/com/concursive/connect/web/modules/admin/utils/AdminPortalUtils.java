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
package com.concursive.connect.web.modules.admin.utils;

import com.concursive.commons.xml.XMLUtils;
import com.concursive.connect.cms.portal.dao.DashboardPage;
import com.concursive.connect.cms.portal.dao.DashboardTemplateList;
import com.concursive.connect.cms.portal.utils.DashboardUtils;
import com.concursive.connect.web.modules.admin.beans.AdminPortalBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;

/**
 * Utilities used by the embedded portal and portlets
 *
 * @author matt rajkowski
 * @created October 24, 2008
 */
public class AdminPortalUtils {
  private static final Log LOG = LogFactory.getLog(AdminPortalUtils.class);

  public static DashboardPage retrieveDashboardPage(AdminPortalBean portalBean) throws Exception {

    // Use the admin-portal-config.xml to determine the dashboard page to use for the current url
    URL resource = AdminPortalUtils.class.getResource("/portal/admin-portal-config.xml");
    LOG.debug("portal config file: " + resource.toString());
    XMLUtils library = new XMLUtils(resource);

    // The nodes are listed under the <module> tag
    Element portal = XMLUtils.getFirstChild(library.getDocumentElement(), "portal", "type", "controller");

    String moduleLayout = null;
    String page = null;

    // Look through the modules
    NodeList moduleNodeList = portal.getElementsByTagName("module");
    for (int i = 0; i < moduleNodeList.getLength(); i++) {
      Node thisModule = moduleNodeList.item(i);

      NodeList urlNodeList = ((Element) thisModule).getElementsByTagName("url");
      for (int j = 0; j < urlNodeList.getLength(); j++) {
        Element urlElement = (Element) urlNodeList.item(j);

        // Construct a AdminPortalURL and add to the list for retrieving
        String name = urlElement.getAttribute("name");

        // <url name="/show" object="/" page="default"/>
        if (name.equals("/" + portalBean.getAction())) {
          String object = urlElement.getAttribute("object");
          if (object.equals("/" + portalBean.getDomainObject())) {
            // Set the module for tab highlighting
            portalBean.setModule(((Element) thisModule).getAttribute("name"));

            // Set the page for looking up
            page = urlElement.getAttribute("page");

            // Set the layout filename to find the referenced page
            moduleLayout = ((Element) thisModule).getAttribute("layout");

            break;
          }
        }
      }
    }

    URL moduleResource = AdminPortalUtils.class.getResource("/portal/" + moduleLayout);
    LOG.debug("module config file: " + moduleResource.toString());
    XMLUtils module = new XMLUtils(moduleResource);

    Element moduleElement = XMLUtils.getFirstChild(module.getDocumentElement(), "controller");

    NodeList pageNodeList = moduleElement.getElementsByTagName("page");
    if (pageNodeList.getLength() == 0) {
      LOG.warn("No pages found in moduleLayout: " + moduleLayout);
    }

    for (int j = 0; j < pageNodeList.getLength(); j++) {
      Node thisPage = pageNodeList.item(j);

      String pageName = ((Element) thisPage).getAttribute("name");

      if (pageName.equals(page)) {
        LOG.debug("Found page " + pageName);

        // Find a portal template to use
        LOG.debug("Trying a specific portal: " + page);
        DashboardPage dashboardPage = DashboardUtils.loadDashboardPage(DashboardTemplateList.TYPE_CONTROLLER, (portalBean.isPopup() ? page + "-popup" : page), moduleLayout);
        if (dashboardPage == null) {
          LOG.warn("Page not found: " + page);
          return null;
        }
        return dashboardPage;
      }
    }
    return null;
  }
}
