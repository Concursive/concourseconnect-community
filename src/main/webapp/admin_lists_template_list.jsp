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
<%@ taglib uri="/WEB-INF/concourseconnect-taglib.tld" prefix="ccp" %>
<%@ page import="com.concursive.connect.web.modules.lists.dao.ListsTemplate" %>
<jsp:useBean id="SKIN" class="java.lang.String" scope="application"/>
<jsp:useBean id="listsTemplateList" class="com.concursive.connect.web.modules.lists.dao.ListsTemplateList" scope="request" />
<jsp:useBean id="projectCategoryList" class="com.concursive.connect.web.modules.profile.dao.ProjectCategoryList" scope="request"/>
<%@ include file="initPage.jsp" %>
<%-- Initialize the drop-down menus --%>
<%@ include file="initPopupMenu.jsp" %>
<%@ include file="admin_lists_template_list_menu.jspf" %>
<script language="JavaScript" type="text/javascript">
  loadImages('select_<%= SKIN %>');
</script>
<a href="<%= ctx %>/admin">System Administration</a> >
<a href="<%= ctx %>/AdminApplication.do">Manage Application Settings</a> > Lists Templates<br />
<br />
<a href="<%= ctx %>/AdminListsTemplates.do?command=Modify">Add a Lists Template</a><br /><br />
<table class="pagedList">
  <thead>
    <tr>
      <th nowrap>
          Action
      </th>
      <th width="30%" nowrap>
          Site Category
      </th>
      <th width="60%">
          List Names
      </th>
      <th width="10%">
          Enabled
      </th>
    </tr>
  </thead>
  <tbody>
	<%
  if (listsTemplateList.size() == 0) {
%>
	<tr class="row2">
		<td colspan="4">No templates to display.</td>
	</tr>
	<%
  }
  int count = 0;
  int rowid = 0;
for(ListsTemplate listsTemplate : listsTemplateList) {
    ++count;
    rowid = (rowid != 1?1:2);
%>
  <tr class="row<%=rowid%>">
    <td valign="top" align="center" nowrap>
      <a href="javascript:displayMenu('select_<%= SKIN %><%= listsTemplate.getId() %>','menuItem',<%= listsTemplate.getId() %>);"
         onMouseOver="over(0, <%= listsTemplate.getId() %>)"
         onmouseout="out(0, <%= listsTemplate.getId() %>); hideMenu('menuItem');"><img
         src="<%= ctx %>/images/select_<%= SKIN %>.gif" name="select_<%= SKIN %><%= listsTemplate.getId() %>" id="select_<%= SKIN %><%= listsTemplate.getId() %>" align="absmiddle" border="0"></a>
    </td>
    <td valign="top">
    	<%= toHtml(projectCategoryList.getValueFromId(listsTemplate.getProjectCategoryId())) %>
    </td>
    <td>
      <a href="<%= ctx %>/AdminListsTemplates.do?command=Modify&id=<%= listsTemplate.getId() %>"><%= toHtml(listsTemplate.getListNames()) %></a>
    </td>
    <td valign="top">
		<%= listsTemplate.getEnabled()?"Yes":"No" %>
    </td>
  </tr>
	<%
  }
%>
  </tbody>
</table>
