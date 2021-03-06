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
<%@ page import="java.util.*" %>
<%@ page import="com.concursive.connect.web.modules.productcatalog.dao.Product" %>
<jsp:useBean id="productList" class="com.concursive.connect.web.modules.productcatalog.dao.ProductList" scope="request"/>
<jsp:useBean id="productListInfo" class="com.concursive.connect.web.utils.PagedListInfo" scope="session"/>
<%@ include file="initPage.jsp" %>

<div class="spacerContainer">
  <div class="portletWindowBackground">
    <div class="portletWrapper">
      <%= showError(request, "actionError", false) %>
      <ccp:pagedListStatus label="Products" title="Choose a product for more information..." object="productListInfo"/>
      <table cellpadding="10" cellspacing="0" border="0" width="100%">
        <%
          if (productList.size() == 0) {
        %>
        <tr>
          <td class="ImageList" valign="center">
            No products to display.
          </td>
        </tr>
        <%
        } else {
          int rowcount = 0;
          int count = 0;
          int multiplier = 3;
          if (productList.size() == 4) {
            multiplier = 2;
          }
          //Show the products
          Iterator i = productList.iterator();
          while (i.hasNext()) {
            Product thisItem = (Product) i.next();
            ++count;
            if ((count + (multiplier - 1)) % multiplier == 0) {
              ++rowcount;
            }
        %>
        <ccp:evaluate exp="<%= (count + (multiplier - 1)) % multiplier == 0 %>">
        <tr>
          </ccp:evaluate>
          <td class="ImageList<%= (rowcount == 1?"":"AdditionalRow") %>">
      <span>
          <a href="<%= ctx %>/Order.do?command=Details&pid=<%= thisItem.getId() %>"><img src="<%= thisItem.getSmallImage() %>" border="0" /></a><br>
        <%= toHtml(thisItem.getName()) %>
      </span>
          </td>
          <ccp:evaluate exp="<%= count % multiplier == 0 %>">
        </tr>
        </ccp:evaluate>
        <%
          }
        %>
        <ccp:evaluate exp="<%= count % 3 != 0 %>">
          </tr>
        </ccp:evaluate>
        <%}%>
      </table>
      <br>
      <ccp:pagedListControl object="productListInfo"/>
    </div>
  </div>
</div>
