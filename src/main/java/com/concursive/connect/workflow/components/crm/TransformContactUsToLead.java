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

package com.concursive.connect.workflow.components.crm;

import com.concursive.commons.text.StringUtils;
import com.concursive.commons.workflow.ComponentContext;
import com.concursive.commons.workflow.ComponentInterface;
import com.concursive.commons.workflow.ObjectHookComponent;
import com.concursive.connect.web.modules.contactus.dao.ContactUsBean;
import com.concursive.crm.api.client.DataRecord;

/**
 * Prepares the ContactUs information for sending to ConcourseSuite
 *
 * @author matt rajkowski
 * @version $Id$
 * @created Apr 11, 2005
 */

public class TransformContactUsToLead extends ObjectHookComponent implements ComponentInterface {

  public String getDescription() {
    return "Prepares the ContactUs information for sending to ConcourseSuite";
  }

  public boolean execute(ComponentContext context) {
    if (System.getProperty("DEBUG") != null) {
      System.out.println("TransformContactUsToLead-> Step 1");
    }
    ContactUsBean bean = (ContactUsBean) context.getThisObject();
    // Transform the base contact info
    DataRecord record = new DataRecord();
    record.setName("contact");
    record.setAction(DataRecord.INSERT);
    record.addField("nameFirst", bean.getNameFirst());
    record.addField("nameLast", bean.getNameLast());
    record.addField("company", bean.getOrganization());
    // Process any notes...
    if (StringUtils.hasText(bean.getDescription())) {
      StringBuffer notes = new StringBuffer(bean.getDescription());
      if (bean.getFormData() != null) {
        for (int i = 0; i < bean.getFormData().length; i++) {
          if (notes.length() > 0) {
            notes.append("; ");
            notes.append(bean.getFormData()[i]);
          }
        }
      }
      record.addField("notes", notes.toString());
    }
    context.setAttribute(SaveLead.LEAD, record);
    // Transform the email
    if (StringUtils.hasText(bean.getEmail())) {
      DataRecord email = new DataRecord();
      email.setName("contactEmailAddress");
      email.setAction(DataRecord.INSERT);
      email.addField("email", bean.getEmail());
      context.setAttribute(SaveLead.LEAD_EMAIL, email);
    }
    // Transform the phone number
    if (StringUtils.hasText(bean.getBusinessPhone())) {
      DataRecord phone = new DataRecord();
      phone.setName("contactPhoneNumber");
      phone.setAction(DataRecord.INSERT);
      phone.addField("number", bean.getBusinessPhone());
      phone.addField("extension", bean.getBusinessPhoneExt());
      context.setAttribute(SaveLead.LEAD_PHONE, phone);
    }
    // Transform the address
    if (StringUtils.hasText(bean.getAddressLine1()) || StringUtils.hasText(bean.getState())) {
      DataRecord address = new DataRecord();
      address.setName("contactAddress");
      address.setAction(DataRecord.INSERT);
      address.addField("streetAddressLine1", bean.getAddressLine1());
      address.addField("streetAddressLine2", bean.getAddressLine2());
      address.addField("city", bean.getCity());
      address.addField("state", bean.getState());
      address.addField("zip", bean.getPostalCode());
      address.addField("country", bean.getCountry());
      context.setAttribute(SaveLead.LEAD_ADDRESS, address);
    }
    return true;
  }
}
