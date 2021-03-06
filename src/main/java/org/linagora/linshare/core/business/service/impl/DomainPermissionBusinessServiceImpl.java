package org.linagora.linshare.core.business.service.impl;

import org.linagora.linshare.core.business.service.DomainBusinessService;
import org.linagora.linshare.core.business.service.DomainPermissionBusinessService;
import org.linagora.linshare.core.domain.entities.AbstractDomain;
import org.linagora.linshare.core.domain.entities.Account;
import org.linagora.linshare.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainPermissionBusinessServiceImpl implements DomainPermissionBusinessService {

	private final Logger logger = LoggerFactory.getLogger(DomainPermissionBusinessServiceImpl.class);
	
	private DomainBusinessService domainBusinessService;
	
	public DomainPermissionBusinessServiceImpl(
			DomainBusinessService domainBusinessService
			) {
		super();
		this.domainBusinessService = domainBusinessService;
	}
	
	@Override
	public boolean isAdminforThisDomain(Account actor, String domainId)
			throws BusinessException {
		AbstractDomain domain = domainBusinessService.findById(domainId);
		return isAdminforThisDomain(actor, domain);
	}

	@Override
	public boolean isAdminforThisDomain(Account actor, AbstractDomain domain) {
		if(!actor.isSuperAdmin()) {
			if (!domain.isManagedBy(actor)) {
				logger.error("You do not have the right to managed parameters of this domain : " + domain.toString());
				return false; 
			}
		}
		return true;
	}
}
