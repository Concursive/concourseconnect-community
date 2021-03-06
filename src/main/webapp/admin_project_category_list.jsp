<%--
  ~ ConcourseConnect
  ~ Copyright 2009 Concursive Corporation
  ~ http://www.concursive.com
  ~
  ~ This file is part of ConcourseConnect, an open source social business
  ~ software and community platform.
  ~
  ~ Concursive ConcourseConnect is free software: you can redistribute it and/or
  ~ modify it under the terms of the GNU Affero General Public License as published
  ~ by the Free Software Foundation, version 3 of the License.
  ~
  ~ Under the terms of the GNU Affero General Public License you must release the
  ~ complete source code for any application that uses any part of ConcourseConnect
  ~ (system header files and libraries used by the operating system are excluded).
  ~ These terms must be included in any work that has ConcourseConnect components.
  ~ If you are developing and distributing open source applications under the
  ~ GNU Affero General Public License, then you are free to use ConcourseConnect
  ~ under the GNU Affero General Public License.
  ~
  ~ If you are deploying a web site in which users interact with any portion of
  ~ ConcourseConnect over a network, the complete source code changes must be made
  ~ available.  For example, include a link to the source archive directly from
  ~ your web site.
  ~
  ~ For OEMs, ISVs, SIs and VARs who distribute ConcourseConnect with their
  ~ products, and do not license and distribute their source code under the GNU
  ~ Affero General Public License, Concursive provides a flexible commercial
  ~ license.
  ~
  ~ To anyone in doubt, we recommend the commercial license. Our commercial license
  ~ is competitively priced and will eliminate any confusion about how
  ~ ConcourseConnect can be used and distributed.
  ~
  ~ ConcourseConnect is distributed in the hope that it will be useful, but WITHOUT
  ~ ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  ~ FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
  ~ details.
  ~
  ~ You should have received a copy of the GNU Affero General Public License
  ~ along with ConcourseConnect.  If not, see <http://www.gnu.org/licenses/>.
  ~
  ~ Attribution Notice: ConcourseConnect is an Original Work of software created
  ~ by Concursive Corporation
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/concourseconnect-taglib.tld" prefix="ccp" %>
<%@ page import="com.concursive.connect.web.modules.profile.dao.ProjectCategory" %>
<jsp:useBean id="SKIN" class="java.lang.String" scope="application"/>
<jsp:useBean id="projectCategoryList" class="com.concursive.connect.web.modules.profile.dao.ProjectCategoryList" scope="request" />
<%@ include file="initPage.jsp" %>
<%-- Initialize the drop-down menus --%>
<%@ include file="initPopupMenu.jsp" %>
<%@ include file="admin_project_category_list_menu.jspf" %>
<script language="JavaScript" type="text/javascript">
  loadImages('select_<%= SKIN %>');
</script>
<a href="<%= ctx %>/admin">System Administration</a> >
<a href="<%= ctx %>/AdminApplication.do">Manage Application Settings</a> > Site Categories<br />
<br />
<a href="<%= ctx %>/AdminProjectCategories.do?command=Modify">Add a Site Category</a><br /><br />
<table class="pagedList">
  <thead>
    <tr>
      <th>
          Action
      </th>
      <th width="10%">
          Logo
      </th>
      <th width="40%">
          Label
      </th>
      <th width="30%">
          Parent
      </th>
      <th width="20%">
          Enabled
      </th>
    </tr>
  </thead>
  <tbody>
	<%
  if (projectCategoryList.size() == 0) {
%>
	<tr class="row2">
		<td colspan="5">No categories to display.</td>
	</tr>
	<%
  }
  int count = 0;
  int rowid = 0;
  Iterator<ProjectCategory> i = projectCategoryList.iterator();
  while (i.hasNext()) {
    ++count;
    rowid = (rowid != 1?1:2);
    ProjectCategory thisProjectCategory = i.next();
%>
  <tr class="row<%=rowid%>">
    <td valign="top" align="center" nowrap>
      <a href="javascript:displayMenu('select_<%= SKIN %><%= thisProjectCategory.getId() %>','menuItem',<%= thisProjectCategory.getId() %>);"
         onMouseOver="over(0, <%= thisProjectCategory.getId() %>)"
         onmouseout="out(0, <%= thisProjectCategory.getId() %>); hideMenu('menuItem');"><img
         src="<%= ctx %>/images/select_<%= SKIN %>.gif" name="select_<%= SKIN %><%= thisProjectCategory.getId() %>" id="select_<%= SKIN %><%= thisProjectCategory.getId() %>" align="absmiddle" border="0"></a>
    </td>
    <td>
      <ccp:evaluate if="<%= thisProjectCategory.getLogoId() != -1 %>">
	      <%= thisProjectCategory.getLogo().getFullImageFromAdmin(ctx) %>
      </ccp:evaluate>&nbsp;
    </td>
    <td>
      <a href="<%= ctx %>/AdminProjectCategories.do?command=Modify&projectCategoryId=<%= thisProjectCategory.getId() %>"><c:out value="<%= thisProjectCategory.getLabel() %>"/></a>
      <c:if test="<%= !thisProjectCategory.getLabel().equals(thisProjectCategory.getDescription())%>">(<c:out value="<%= thisProjectCategory.getDescription() %>"/>)</c:if>
    </td>
    <td>
      <ccp:evaluate if="<%= thisProjectCategory.getParentCategoryId() > -1 %>">
        <a href="<%= ctx %>/AdminProjectCategories.do?command=Modify&projectCategoryId=<%= thisProjectCategory.getParentCategoryId() %>"><c:out value="<%= projectCategoryList.getValueFromId(thisProjectCategory.getParentCategoryId()) %>"/></a>
      </ccp:evaluate>&nbsp;
    </td>
    <td>
		  <c:out value='<%= thisProjectCategory.getEnabled()?"Yes":"No" %>'/>
    </td>
  </tr>
	<%
  }
%>
  </tbody>
</table>
