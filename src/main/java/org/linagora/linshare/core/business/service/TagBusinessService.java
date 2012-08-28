package org.linagora.linshare.core.business.service;

import java.util.Set;

import org.linagora.linshare.core.domain.entities.Account;
import org.linagora.linshare.core.domain.entities.Tag;
import org.linagora.linshare.core.domain.entities.TagFilter;
import org.linagora.linshare.core.domain.entities.TagFilterRule;
import org.linagora.linshare.core.domain.entities.TagFilterRuleTagAssociation;
import org.linagora.linshare.core.domain.entities.Thread;
import org.linagora.linshare.core.domain.entities.ThreadEntry;
import org.linagora.linshare.core.exception.BusinessException;

public interface TagBusinessService {
	
	public Tag findByOwnerAndName(Account owner, String name) throws BusinessException;
	
	public void setTagToThreadEntry(Thread owner, ThreadEntry threadEntry, Tag tag, String optionalValue) throws BusinessException;
	
	public void runTagFiltersOnThreadEntry(Account actor, Thread owner, ThreadEntry threadEntry) throws BusinessException;

}