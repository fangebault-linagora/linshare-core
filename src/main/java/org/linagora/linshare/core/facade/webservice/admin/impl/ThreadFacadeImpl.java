/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2014 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2014. Contribute to
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

package org.linagora.linshare.core.facade.webservice.admin.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.linagora.linshare.core.domain.constants.Role;
import org.linagora.linshare.core.domain.entities.Thread;
import org.linagora.linshare.core.domain.entities.ThreadMember;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.webservice.admin.ThreadFacade;
import org.linagora.linshare.core.service.AccountService;
import org.linagora.linshare.core.service.ThreadService;
import org.linagora.linshare.webservice.dto.ThreadDto;
import org.linagora.linshare.webservice.dto.ThreadMemberDto;

public class ThreadFacadeImpl extends AdminGenericFacadeImpl implements
		ThreadFacade {

	private ThreadService threadService;

	public ThreadFacadeImpl(final AccountService accountService,
			final ThreadService threadService) {
		super(accountService);
		this.threadService = threadService;
	}

	@Override
	public Set<ThreadDto> findAll() throws BusinessException {
		User actor = checkAuthentication(Role.SUPERADMIN);
		Set<ThreadDto> ret = new HashSet<ThreadDto>();

		for (Thread t : threadService.findAll())
			ret.add(new ThreadDto(t));
		return ret;
	}

	@Override
	public ThreadDto find(String uuid) throws BusinessException {
		User actor = checkAuthentication(Role.SUPERADMIN);
		Validate.notEmpty(uuid, "uuid must be set.");
		return new ThreadDto(threadService.findByLsUuid(uuid));
	}

	@Override
	public Set<ThreadMemberDto> members(String uuid) throws BusinessException {
		User actor = checkAuthentication(Role.SUPERADMIN);
		Validate.notEmpty(uuid, "uuid must be set.");
		Set<ThreadMemberDto> ret = new HashSet<ThreadMemberDto>();

		for (ThreadMember m : threadService.findByLsUuid(uuid).getMyMembers())
			ret.add(new ThreadMemberDto(m));
		return ret;
	}

	@Override
	public ThreadDto update(ThreadDto threadDto) throws BusinessException {
		User actor = checkAuthentication(Role.SUPERADMIN);
		Validate.notNull(threadDto, "thread must be set.");
		Thread thread = threadService.findByLsUuid(threadDto.getUuid());

		return new ThreadDto(threadService.rename(actor, thread, threadDto.getName()));
	}

	@Override
	public void delete(String uuid) throws BusinessException {
		User actor = checkAuthentication(Role.SUPERADMIN);
		Validate.notEmpty(uuid, "uuid must be set.");
		Thread thread = threadService.findByLsUuid(uuid);

		threadService.deleteThread(actor, thread);
	}
}
