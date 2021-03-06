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
package com.concursive.connect.web.modules.classifieds.portlets.classifiedListByClassifiedCategory;

import com.concursive.commons.text.StringUtils;
import com.concursive.connect.Constants;
import com.concursive.connect.cms.portal.dao.DashboardPage;
import com.concursive.connect.indexer.IIndexerSearch;
import com.concursive.connect.indexer.IndexerQueryResultList;
import com.concursive.connect.web.modules.classifieds.dao.ClassifiedCategory;
import com.concursive.connect.web.modules.classifieds.dao.ClassifiedCategoryList;
import com.concursive.connect.web.modules.profile.dao.ProjectCategory;
import com.concursive.connect.web.modules.profile.dao.ProjectCategoryList;
import com.concursive.connect.web.modules.search.beans.SearchBean;
import com.concursive.connect.web.modules.search.utils.SearchUtils;
import com.concursive.connect.web.portal.IPortletViewer;
import com.concursive.connect.web.portal.PortalUtils;
import com.concursive.connect.web.utils.PagedListInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;

/**
 * Classifieds list by classifieds portlet
 *
 * @author Kailash Bhoopalam
 * @created April 09, 2009
 */
public class ClassifiedListByClassifiedCategoryViewer implements IPortletViewer {

  private static Log LOG = LogFactory.getLog(ClassifiedListByClassifiedCategoryViewer.class);

  // Pages
  private static final String CLASSIFIED_CATEGORY_VIEW_PAGE = "/portlets/classified_list_by_classified_category/classified_category_list_by_project_category-view.jsp";
  private static final String CLASSIFIED_VIEW_PAGE = "/portlets/classified_list_by_classified_category/classified_list_by_classified_category-view.jsp";
  // Preferences
  private static final String PREF_AUTO_SET_PAGE_TITLE = "autoSetPageTitle";
  private static final String PREF_CATEGORY_NAME = "category";
  private static final String PREF_TITLE = "title";
  private static final String PREF_LIMIT = "limit";
  private static final String PREF_HAS_MORE_TITLE = "hasMoreTitle";
  private static final String PREF_HAS_MORE_URL = "hasMoreURL";
  private static final String PREF_HAS_PAGING = "hasPaging";
  private static final String PREF_SHOW_CLASSIFIEDCATEGORIES = "showClassifiedCategories";
  private static final String PREF_SHOW_CLASSIFIEDS = "showClassifieds";
  private static final String PREF_COLUMNS = "columns";
  private static final String PREF_SHOW_CATEGORY_LANDING_PAGE_LINK = "showCategoryLandingPageLink";

  // Attribute names for objects available in the view
  private static final String CLASSIFIED_CATEGORY_LIST = "classifiedCategoryList";
  private static final String PROJECT_CATEGORY = "projectCategory";
  private static final String CLASSIFIED_CATEGORY = "classifiedCategory";
  private static final String CLASSIFIED_LIST = "classifiedList";
  private static final String TITLE = "title";
  private static final String HAS_MORE = "hasMore";
  private static final String HAS_MORE_TITLE = "hasMoreTitle";
  private static final String HAS_MORE_URL = "hasMoreURL";
  private static final String HAS_PAGING = "hasPaging";
  private static final String RECORD_LIMIT = "recordLimit";
  private static final String SORT_ORDER = "sortOrder";
  private static final String QUERY = "query";
  private static final String LOCATION = "location";
  private static final String SHOW_PROJECT_CATEGORY_NAME_IN_CATEGORY_LIST = "showProjectCategoryNameInCategoryList";
  private static final String CLASSIFIEDS_CATEGORY_COUNT_MAP = "classifiedsCategoryCountMap";

  public String doView(RenderRequest request, RenderResponse response)
      throws Exception {
    String defaultView = CLASSIFIED_CATEGORY_VIEW_PAGE;

    // Get preferences
    boolean autoSetPageTitle = Boolean.parseBoolean(request.getPreferences().getValue(PREF_AUTO_SET_PAGE_TITLE, "false"));
    String categoryValue = request.getPreferences().getValue(PREF_CATEGORY_NAME, PortalUtils.getPageView(request));
    LOG.debug("Category: " + categoryValue);

    if (categoryValue == null) {
      // @todo return a list of top-level categories to choose from
    }

    String title = request.getPreferences().getValue(PREF_TITLE, null);
    title = StringUtils.replace(title, "${category}", ProjectCategory.getCategoryNameFromNormalizedCategoryName(categoryValue));
    request.setAttribute(TITLE, title);
    LOG.debug("Title: " + title);

    String hasMoreUrl = request.getPreferences().getValue(PREF_HAS_MORE_URL, null);
    hasMoreUrl = StringUtils.replace(hasMoreUrl, "${category}", categoryValue);
    request.setAttribute(HAS_MORE_URL, hasMoreUrl);
    LOG.debug("HasMoreURL: " + hasMoreUrl);

    request.setAttribute(HAS_MORE_TITLE, request.getPreferences().getValue(PREF_HAS_MORE_TITLE, "Browse more categories"));
    boolean showClassifiedCategories = Boolean.parseBoolean(request.getPreferences().getValue(PREF_SHOW_CLASSIFIEDCATEGORIES, "false"));
    boolean showClassifieds = Boolean.parseBoolean(request.getPreferences().getValue(PREF_SHOW_CLASSIFIEDS, "false"));

    // Base the selected classified category off of the URL
    String classifiedCategoryString = PortalUtils.getPageParameter(request);
    // convert from normalized form
    classifiedCategoryString = ClassifiedCategory.getCategoryNameFromNormalizedCategoryName(classifiedCategoryString);
    LOG.debug("classifiedCategory: " + classifiedCategoryString);
    String classifiedCategoryId = null;
    if (StringUtils.hasText(classifiedCategoryString) && PortalUtils.getPageParameters(request).length > 1) {
      classifiedCategoryId = PortalUtils.getPageParameters(request)[1];
    }
    LOG.debug("classifiedCategoryId: " + classifiedCategoryId);

    // Get the top-level category
    ProjectCategoryList categories = (ProjectCategoryList) request.getAttribute(Constants.REQUEST_TAB_CATEGORY_LIST);
    ProjectCategory category = categories.getFromValue(ProjectCategory.getCategoryNameFromNormalizedCategoryName(categoryValue));
    if (category == null) {
      category = new ProjectCategory();
      category.setDescription("all");
    }
    request.setAttribute(PROJECT_CATEGORY, category);

    // Determine the database connection to use
    Connection db = PortalUtils.useConnection(request);

    // Determine the classified category to use for showing classifieds
    ClassifiedCategory classifiedCategory = null;
    if (showClassifiedCategories && StringUtils.hasText(categoryValue)) {
      ClassifiedCategoryList classifiedCategories = new ClassifiedCategoryList();
      classifiedCategories.setProjectCategoryId(category.getId());
      classifiedCategories.setEnabled(Constants.TRUE);
      classifiedCategories.buildList(db);
      if (classifiedCategories.size() > 0) {
        classifiedCategory = classifiedCategories.get(0);
      }
    }

    // This portlet can retrieve state data from other portlets
    boolean showThisPortlet = true;
    for (String event : PortalUtils.getDashboardPortlet(request).getConsumeDataEvents()) {
      // Detects if another instance of this portlet is showing categories
      if (event.equals("classifiedcategory")) {
        String classifiedcategoryBeingShownByAnotherPortlet = (String) PortalUtils.getGeneratedData(request, event);
        LOG.debug("Consumed a classified category: " + classifiedcategoryBeingShownByAnotherPortlet);
        if (!StringUtils.hasText(classifiedcategoryBeingShownByAnotherPortlet)) {
          showThisPortlet = false;
          defaultView = null;
        }
      }
    }

    // This portlet can provide state data to other portlets
    for (String event : PortalUtils.getDashboardPortlet(request).getGenerateDataEvents()) {
      if (event.toLowerCase().equals("classifiedcategory") && classifiedCategory != null) {
        PortalUtils.setGeneratedData(request, event, classifiedCategory.getItemName());
        LOG.debug("SetData: " + event + " - " + PortalUtils.getGeneratedData(request, event));
      } else if (event.toLowerCase().equals("category")) {
        PortalUtils.setGeneratedData(request, event, category.getDescription());
        LOG.debug("SetData: " + event + " - " + PortalUtils.getGeneratedData(request, event));
      }
    }

    // Generate a page title
    if (autoSetPageTitle) {
      DashboardPage dashboardPage = PortalUtils.getDashboardPage(request);
      String pageTitle = null;
      if (dashboardPage != null) {
        // Set the page's title
        if (classifiedCategory != null) {
          pageTitle = classifiedCategory.getItemName() + " - " + category.getLabel();
        } else {
          pageTitle = category.getLabel();
        }
        if (pageTitle != null) {
          request.setAttribute(Constants.REQUEST_GENERATED_TITLE, pageTitle);
        }
        // Set the page's category
//        request.setAttribute(Constants.REQUEST_GENERATED_CATEGORY, pageTitle);
      }
    }

    String query = PortalUtils.getQueryParameter(request, "query");
    String location = PortalUtils.getQueryParameter(request, "location");

    // Use a search bean to validate the search input
    SearchBean search = new SearchBean();
    search.setQuery(query);
    search.setLocation(location);
    search.parseQuery();
    if (!search.isValid()) {
      if (StringUtils.hasText(query)) {
        LOG.warn("Search invalid");
        return "SearchERROR";
      }
    }

    if (showThisPortlet) {
      String sortOrder = PortalUtils.getQueryParameter(request, "sort");

      boolean hasResults = false;
      if (showClassifieds) {
        request.setAttribute(PREF_SHOW_CATEGORY_LANDING_PAGE_LINK, request.getPreferences().getValue(PREF_SHOW_CATEGORY_LANDING_PAGE_LINK, "false"));

        ClassifiedCategoryList classifiedCategories = new ClassifiedCategoryList();
        classifiedCategory = new ClassifiedCategory();
        if (StringUtils.hasText(classifiedCategoryId)) {
          classifiedCategory = new ClassifiedCategory(db, Integer.parseInt(classifiedCategoryId));
        } else if (StringUtils.hasText(classifiedCategoryString)) {
          classifiedCategories.setProjectCategoryId(category.getId());
          classifiedCategories.setEnabled(Constants.TRUE);
          classifiedCategories.setCategoryLowercaseName(classifiedCategoryString);
          classifiedCategories.buildList(db);
          if (classifiedCategories.size() > 0) {
            classifiedCategory = classifiedCategories.get(0);
          }
        }

        // Show the classifieds
        boolean hasPaging = "true".equals(request.getPreferences().getValue(PREF_HAS_PAGING, "false"));
        PagedListInfo classifiedListInfo = new PagedListInfo();
        if (hasPaging) {
          // Use paged data for sorting
          String limit = request.getPreferences().getValue(PREF_LIMIT, "10");
          request.setAttribute(RECORD_LIMIT, limit);
          classifiedListInfo.setItemsPerPage(limit);

          String pageNumber = null;
          String[] params = PortalUtils.getPageParameters(request);
          if (classifiedCategory.getId() != -1) {
            if (params != null) {
              //E.g., {url}/page/${page name}/${project category}/${classified category}/${classified category Id}/${page number}
              if (params.length == 3) {
                pageNumber = params[2];
              }
            }
          } else {
            if (params != null) {
              //E.g., {url}/page/${page name}/${project category}/${page number}
              if (params.length == 1) {
                pageNumber = params[0];
              }
            }
          }
          if (StringUtils.hasText(pageNumber) && StringUtils.isNumber(pageNumber)) {
            classifiedListInfo.setCurrentPage(Integer.parseInt(pageNumber));
          }
        } else {
          classifiedListInfo.setItemsPerPage(-1);
        }
        request.setAttribute(HAS_PAGING, request.getPreferences().getValue(PREF_HAS_PAGING, "false"));
        if ("alpha".equals(sortOrder)) {
          classifiedListInfo.setColumnToSortBy("titleFull");
          classifiedListInfo.setSortOrder("asc");
        } else if ("expire".equals(sortOrder)) {
          classifiedListInfo.setColumnToSortBy("expired");
          classifiedListInfo.setSortOrder("asc");
        } else {
          classifiedListInfo.setColumnToSortBy("modified");
          classifiedListInfo.setSortOrder("desc");
        }
        classifiedListInfo.setContextPath(request.getContextPath());

        // Retrieve the user's allowed projects
        String projectListings = SearchUtils.generateValidProjects(db, PortalUtils.getUser(request).getId(), -1, category.getId());

        // Generate a valid data query string
        String dataQueryString = SearchUtils.generateDataQueryString(search, PortalUtils.getUser(request).getId(), PortalUtils.getInstance(request).getId(), projectListings);

        // Set the document type
        dataQueryString += " AND (type:classifieds) ";

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        // Fetch only those that are published. TODO: performance testing as this results in a query explosion
        dataQueryString += " AND published:[20030101 TO " + String.valueOf(formatter.format(currentTimestamp) + "]");
        // Fetch only those that have not expired
        dataQueryString += " AND NOT (expired:{20030101 TO " + String.valueOf(formatter.format(currentTimestamp) + "})");
        // fetch only classifieds in this instanceId
        if (PortalUtils.getInstance(request).getId() != -1) {
          dataQueryString += " AND (instanceId:" + PortalUtils.getInstance(request).getId() + ")";
        }
        if (classifiedCategory.getId() != -1) {
          dataQueryString += " AND (categoryId:" + classifiedCategory.getId() + ") ";
        }
        if (category.getId() != -1) {
          dataQueryString += " AND (projectCategoryId:" + category.getId() + ") ";
        }

        IndexerQueryResultList queryResultList = new IndexerQueryResultList();
        queryResultList.setQueryIndexType(Constants.INDEXER_FULL);
        queryResultList.setQueryString(dataQueryString);
        queryResultList.setPagedListInfo(classifiedListInfo);

        IIndexerSearch searcher = SearchUtils.retrieveSearcher(Constants.INDEXER_FULL);
        searcher.search(queryResultList);

        request.setAttribute(CLASSIFIED_LIST, queryResultList);
        request.setAttribute(CLASSIFIED_CATEGORY, classifiedCategory);
        defaultView = CLASSIFIED_VIEW_PAGE;

        if (queryResultList.size() > 0) {
          hasResults = true;
        }
      }

      if (showClassifiedCategories && !hasResults) {

        int columns = Integer.parseInt(request.getPreferences().getValue(PREF_COLUMNS, "1"));

        // Use paged list for limiting number of records, and for counting all
        String limit = request.getPreferences().getValue(PREF_LIMIT, "-1");
        PagedListInfo pagedListInfo = new PagedListInfo();
        pagedListInfo.setItemsPerPage(Integer.parseInt(limit));
        pagedListInfo.setColumnToSortBy("lpc.level ASC, cc.level ASC, cc.item_name ASC");
        ClassifiedCategoryList classifiedCategories = new ClassifiedCategoryList();
        classifiedCategories.setPagedListInfo(pagedListInfo);
        if (category.getId() != -1) {
          classifiedCategories.setProjectCategoryId(category.getId());
          request.setAttribute(SHOW_PROJECT_CATEGORY_NAME_IN_CATEGORY_LIST, "false");
        } else {
          request.setAttribute(SHOW_PROJECT_CATEGORY_NAME_IN_CATEGORY_LIST, "true");
        }
        classifiedCategories.setProjectCategoryEnabled(Constants.TRUE);
        classifiedCategories.setEnabled(Constants.TRUE);
        classifiedCategories.buildList(db);
        request.setAttribute(PREF_COLUMNS, columns);
        request.setAttribute(CLASSIFIED_CATEGORY_LIST, classifiedCategories);
        request.setAttribute(HAS_MORE, String.valueOf(pagedListInfo.getNumberOfPages() > 1));

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        LinkedHashMap<ClassifiedCategory, Integer> classifiedCategoryCountMap = new LinkedHashMap<ClassifiedCategory, Integer>();

        // Retrieve the user's allowed projects
        String projectListings = SearchUtils.generateValidProjects(db, PortalUtils.getUser(request).getId(), -1, category.getId());

        for (ClassifiedCategory thisClassifiedCategory : classifiedCategories) {

          // Generate a valid data query string
          String dataQueryString = SearchUtils.generateDataQueryString(search, PortalUtils.getUser(request).getId(), PortalUtils.getInstance(request).getId(), projectListings);
          dataQueryString += " AND (type:classifieds) ";

          // Fetch only those that are published.
          dataQueryString += " AND published:[20030101 TO " + String.valueOf(formatter.format(currentTimestamp) + "]");
          // Fetch only those that have not expired
          dataQueryString += " AND NOT (expired:{20030101 TO " + String.valueOf(formatter.format(currentTimestamp) + "})");
          // Fetch only promotions in this instanceId
          if (PortalUtils.getInstance(request).getId() != -1) {
            dataQueryString += " AND (instanceId:" + PortalUtils.getInstance(request).getId() + ")";
          }
          if (category.getId() != -1) {
            dataQueryString += " AND (projectCategoryId:" + category.getId() + ") ";
          }
          dataQueryString += " AND (categoryId:" + thisClassifiedCategory.getId() + ") ";

          IndexerQueryResultList classifiedCategoryHits = new IndexerQueryResultList();
          classifiedCategoryHits.setQueryIndexType(Constants.INDEXER_FULL);
          classifiedCategoryHits.setQueryString(dataQueryString);

          IIndexerSearch searcher = SearchUtils.retrieveSearcher(Constants.INDEXER_FULL);
          searcher.search(classifiedCategoryHits);

          if (classifiedCategoryHits.size() > 0) {
            classifiedCategoryCountMap.put(thisClassifiedCategory, classifiedCategoryHits.size());
          }
        }
        request.setAttribute(CLASSIFIEDS_CATEGORY_COUNT_MAP, classifiedCategoryCountMap);

        if (StringUtils.hasText(classifiedCategoryId)) {
          classifiedCategory = classifiedCategories.getCategoryFromId(Integer.parseInt(classifiedCategoryId));
          request.setAttribute(CLASSIFIED_CATEGORY, classifiedCategory);
        }
        if (classifiedCategories.size() == 0) {
          LOG.debug("This category doesn't have any subcategories, so hiding this portlet");
          defaultView = null;
        }
      }
      request.setAttribute(SORT_ORDER, sortOrder);
      request.setAttribute(QUERY, query);
      request.setAttribute(LOCATION, location);
    }
    return defaultView;
  }
}
