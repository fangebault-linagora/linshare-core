package org.linagora.linshare.core.service;

import java.util.List;

import org.linagora.linshare.core.domain.entities.Account;
import org.linagora.linshare.core.domain.entities.Tag;
import org.linagora.linshare.core.domain.entities.TagFilter;
import org.linagora.linshare.core.domain.entities.Thread;
import org.linagora.linshare.core.domain.entities.ThreadMember;
import org.linagora.linshare.core.domain.entities.ThreadView;
import org.linagora.linshare.core.domain.entities.User;
import org.linagora.linshare.core.exception.BusinessException;

public interface ThreadService {
	
	public Thread findByLsUuid(String uuid);

	public List<Thread> findAll();

	public void create(Account actor, String name) throws BusinessException;
	
	public ThreadMember getThreadMemberById(String id) throws BusinessException;
	
	public ThreadMember getThreadMemberFromUser(Thread thread, User user) throws BusinessException;

	public List<Thread> getThreadListIfAdmin(User user) throws BusinessException;

	public void addMember(Thread thread, User user, boolean readOnly);

	public boolean hasAnyThreadWhereIsAdmin(User user);

	public void updateMember(ThreadMember member, boolean admin, boolean canUpload);

	public void deleteMember(Thread thread, ThreadMember member);
	
	public void deleteAllMembers(Thread thread);

	public void deleteAllUserMemberships(User user);

	public void deleteThread(User user, Thread thread);

	public void deleteThreadView(User user, Thread thread, ThreadView threadView);
	
	public void deleteAllThreadViews(User user, Thread thread);

	public void deleteTagFilter(User user, Thread thread, TagFilter filter);

	public void deleteTag(User user, Thread thread, Tag tag);

	public void deleteAllTags(User user, Thread thread);

	public void rename(Thread thread, String threadName) throws BusinessException;
}