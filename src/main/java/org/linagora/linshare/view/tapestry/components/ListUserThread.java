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

package org.linagora.linshare.view.tapestry.components;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.linagora.linshare.core.domain.vo.ThreadVo;
import org.linagora.linshare.core.domain.vo.UserVo;
import org.linagora.linshare.core.exception.BusinessException;
import org.linagora.linshare.core.facade.ThreadEntryFacade;
import org.linagora.linshare.core.facade.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ListUserThread {

	private static final Logger logger = LoggerFactory
			.getLogger(ListUserThread.class);

	@SessionState
	@Property
	private UserVo userVo;

	@Parameter(required = true, defaultPrefix = BindingConstants.PROP)
	@Property
	private ThreadVo thread;

	@Persist
	@Property
	private List<UserVo> users;

	@Property
	private UserVo user;

	@Persist
	@Property
	private boolean show;

	@Persist
	@Property
	private String pattern;

	@Inject
	private UserFacade userFacade;

	@Inject
	private ThreadEntryFacade threadEntryFacade;


	public void onActivate() {
		if (!show) {
			users = Lists.newArrayList();
		}
	}

	@SetupRender
	public void init() throws BusinessException {
		if (show) {
			updateUserList();
		}
	}
	
	public void onSuccessFromUserSearch() throws BusinessException {
		StringUtils.trim(pattern);
		updateUserList();
		show = true;
	}

	public void onActionFromAdd(String uuid) throws BusinessException {
		UserVo u = Iterables.find(users, UserVo.equalTo(uuid));
		threadEntryFacade.addMember(userVo, thread, u.getDomainIdentifier(),
				u.getMail());
	}

	public void updateUserList() throws BusinessException {
		users = ImmutableList.copyOf(Iterables.filter(
				threadEntryFacade.searchAmongUsers(userVo, pattern),
				isNotMember()));
	}
	
	private Predicate<UserVo> isNotMember() {
		return new Predicate<UserVo>() {
			@Override
			public boolean apply(UserVo input) {
				try {
					return !threadEntryFacade.userIsMember(input, thread);
				} catch (BusinessException e) {
					logger.error(e.getMessage());
					return true;
				}
			}
		};
	}
}
