package org.linagora.linShare.service;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.linagora.linShare.core.domain.entities.Domain;
import org.linagora.linShare.core.domain.entities.DomainPattern;
import org.linagora.linShare.core.domain.entities.LDAPConnection;
import org.linagora.linShare.core.domain.entities.User;
import org.linagora.linShare.core.domain.vo.DomainPatternVo;
import org.linagora.linShare.core.domain.vo.DomainVo;
import org.linagora.linShare.core.domain.vo.LDAPConnectionVo;
import org.linagora.linShare.core.exception.BusinessException;
import org.linagora.linShare.core.service.DomainService;
import org.linagora.linShare.core.service.LDAPQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { 
		"classpath:springContext-datasource.xml",
		"classpath:springContext-repository.xml",
		"classpath:springContext-service.xml",
		"classpath:springContext-dao.xml",
		"classpath:springContext-facade.xml",
		"classpath:springContext-startopends.xml",
		"classpath:springContext-jackRabbit.xml",
		"classpath:springContext-test.xml"
		})
public class LDAPQueryServiceTest extends AbstractJUnit4SpringContextTests {
	
	private static final String DOMAIN_IDENTIFIER = "testDomain";

	@Autowired
	private DomainService domainService;
	
	@Autowired
	private LDAPQueryService ldapQueryService;
	
	private static boolean initialized = false;
	
	@Before
	public void setUp() throws Exception {
		if (!initialized) {
			LDAPConnectionVo ldapConn = new LDAPConnectionVo("testldap", "ldap://localhost:389", "anonymous");
			DomainPatternVo pattern = new DomainPatternVo("testPattern", "testPattern", 
					"ldap.entry(\"uid=\" + userId + \",ou=test,o=linShare,\" + domain, \"objectClass=*\");", 
					"ldap.list(\"ou=test,o=linShare,\" + domain, \"objectClass=*\");", 
					"", 
					"ldap.list(\"ou=test,o=linShare,\" + domain, \"(&(objectClass=*)(|(mail=\"+login+\")(uid=\"+login+\")))\");", 
					"ldap.list(\"ou=test,o=linShare,\" + domain, \"(&(objectClass=*)(mail=\"+mail+\")(givenName=\"+firstName+\")(sn=\"+lastName+\"))\");", 
					"postalAddress givenName sn", 
					"", 
					"", 
					"", 
					"");
			DomainVo domainVo = new DomainVo(DOMAIN_IDENTIFIER, "dc=nodomain,dc=com", pattern, ldapConn);
			domainService.createLDAPConnection(ldapConn);
			domainService.createDomainPattern(pattern);
			domainService.createDomain(domainVo);
			initialized = true;
		}
	}

	@Test
	public void testGetUser() throws BusinessException {
		User user = ldapQueryService.getUser("jvaljean", DOMAIN_IDENTIFIER, null);
		Assert.assertEquals("jvaljean@nodomain.com", user.getMail());
	}

	@Test
	public void testGetAllDomainUsers() throws BusinessException {
		List<User> users = ldapQueryService.getAllDomainUsers(DOMAIN_IDENTIFIER, null);
		Assert.assertEquals(4, users.size());
	}

	@Test
	public void testIsAdmin() {
		fail("Not yet implemented");
	}

	@Test
	public void testAuth() throws BusinessException, NamingException, IOException {
		boolean response = ldapQueryService.auth("rlaporte", "robert", DOMAIN_IDENTIFIER);
		Assert.assertEquals(true, response);
		response = ldapQueryService.auth("rlaporte", "robert2", DOMAIN_IDENTIFIER);
		Assert.assertEquals(false, response);
		response = ldapQueryService.auth("rlaporte@nodomain.com", "robert", DOMAIN_IDENTIFIER);
		Assert.assertEquals(true, response);
	}

	@Test
	public void testSearchUser() throws BusinessException {
		List<User> users = ldapQueryService.searchUser("rla", null, null, DOMAIN_IDENTIFIER, null);
		Assert.assertEquals(1, users.size());
	}

}
