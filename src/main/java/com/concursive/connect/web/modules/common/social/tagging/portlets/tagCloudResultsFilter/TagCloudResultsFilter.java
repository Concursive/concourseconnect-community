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
package com.concursive.connect.web.modules.common.social.tagging.portlets.tagCloudResultsFilter;

import com.concursive.commons.text.StringUtils;
import com.concursive.connect.Constants;
import com.concursive.connect.web.modules.profile.dao.ProjectCategory;
import com.concursive.connect.web.portal.IPortletViewer;
import com.concursive.connect.web.portal.PortalUtils;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

/**
 * Tag Cloud
 *
 * @author Kailash Bhoopalam
 * @created November 10, 2008
 */
public class TagCloudResultsFilter implements IPortletViewer {

  // Pages
  private static final String VIEW_PAGE = "/portlets/tag_filter_portlet/tag_filter_portlet-view.jsp";

  // Object Results
  private static final String CATEGORY_NAME_ATTRIBUTE = "categoryName";
  private static final String NORMALIZED_TAG_ATTRIBUTE = "normalizedTag";
  private static final String PAGE_URL_ATTRIBUTE = "pageURL";
  private static final String SORT_CRITERIA_ATTRIBUTE = "sortCriteria";

  //Preferences
  private static final String PREF_PAGE_URL = "pageURL";

  public String doView(RenderRequest request, RenderResponse response)
      throws PortletException, IOException {
    try {
      String defaultView = VIEW_PAGE;

      String pageURL = request.getPreferences().getValue(PREF_PAGE_URL, null);
      request.setAttribute(PAGE_URL_ATTRIBUTE, pageURL);

      String tagName = PortalUtils.getPageParameter(request);
      request.setAttribute(NORMALIZED_TAG_ATTRIBUTE, StringUtils.replace(tagName, " ", "-"));
      request.setAttribute(Constants.REQUEST_GENERATED_TITLE, tagName);

      String categoryName = PortalUtils.getPageView(request);
      if (StringUtils.hasText(categoryName)) {
        request.setAttribute(CATEGORY_NAME_ATTRIBUTE, categoryName);
      } else {
        request.setAttribute(CATEGORY_NAME_ATTRIBUTE, ProjectCategory.CATEGORY_NAME_ALL);
      }

      String[] params = PortalUtils.getPageParameters(request);
      // page/tag/{categoryName}/{tagName}/{pageOffset} OR page/tag/{categoryName}/{tagName}/{sortCriteria}
      if (params != null && params.length == 2) {
        String paramString = params[1];
        if (!StringUtils.isNumber(paramString)) {
          request.setAttribute(SORT_CRITERIA_ATTRIBUTE, params[1]);
        }
      }
      // page/tag/{categoryName}/{tagName}/{sortCriteria}/{pageOffset}
      if (params != null && params.length == 3) {
        String sortCriteria = params[1];
        request.setAttribute(SORT_CRITERIA_ATTRIBUTE, sortCriteria);
      }

      // JSP view
      return defaultView;

    } catch (Exception e) {
      e.printStackTrace(System.out);
      throw new PortletException(e.getMessage());
    }
  }
}
