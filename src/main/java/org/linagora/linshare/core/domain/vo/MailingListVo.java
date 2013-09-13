/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2013 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2013. Contribute to
 * Linshare R&D by subscribing to an Enterprise offer!” infobox and in the
 * e-mails sent with the Program, (ii) retain all hypertext links between
 * LinShare and linshare.org, between linagora.com and Linagora, and (iii)
 * refrain from infringing Linagora intellectual property rights over its
 * trademarks and commercial brands. Other Additional Terms apply, see
 * <http://www.linagora.com/licenses/> for more details.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License and
 * its applicable Additional Terms for LinShare along with this program. If not,
 * see <http://www.gnu.org/licenses/> for the GNU Affero General Public License
 * version 3 and <http://www.linagora.com/licenses/> for the Additional Terms
 * applicable to LinShare software.
 */

package org.linagora.linshare.core.domain.vo;

import java.util.ArrayList;
import java.util.List;

import org.linagora.linshare.core.domain.entities.MailingList;
import org.linagora.linshare.core.domain.entities.MailingListContact;

public class MailingListVo {

	private String identifier;
	private String description;
	private boolean isPublic;
	private UserVo owner;
	private String domainId;
	private List<MailingListContactVo> mails;
	private String uuid;
	

	public MailingListVo() {
	}

	public MailingListVo(MailingList list) {

		this.uuid = list.getUuid();
		this.identifier = list.getIdentifier();
		this.description = list.getDescription();
		this.isPublic = list.isPublic();
		this.owner = new UserVo(list.getOwner());
		this.domainId = list.getDomain().getIdentifier();
		mails = new ArrayList<MailingListContactVo>();
		
		for(MailingListContact current : list.getMails()) {
			mails.add(new MailingListContactVo(current));
		}
	}

	public MailingListVo(MailingListVo list) {
		this.uuid= list.getUuid();
		this.identifier = list.getIdentifier();
		this.description = list.getListDescription();
		this.isPublic = list.isPublic();
		this.owner = list.getOwner();
		this.domainId = list.getDomainId();
		this.mails = list.getMails();
	}

	public MailingListVo(String uuid, String identifier, String description,
			boolean isPublic, UserVo owner, String domain,
			List<MailingListContactVo> mails) {
		this.uuid = uuid;
		this.identifier = identifier;
		this.description = description;
		this.isPublic = isPublic;
		this.owner = owner;
		this.domainId = domain;
		this.mails = mails;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getListDescription() {
		return description;
	}

	public void setListDescription(String description) {
		this.description = description;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public UserVo getOwner() {
		return owner;
	}

	public void setOwner(UserVo owner) {
		this.owner = owner;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domain) {
		this.domainId = domain;
	}

	public List<MailingListContactVo> getMails() {
		return mails;
	}
	
	public void addContact(MailingListContactVo contact) {
		mails.add(contact);
	}

	public void setMails(List<MailingListContactVo> mails) {
		this.mails = mails;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MailingListVo other = (MailingListVo) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
	

}
