package com.vimukti.accounter.core;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.services.IAccounterClientService;
import com.vimukti.accounter.web.client.core.ClientUser;

public class AccounterClientServiceTest extends
		AbstractDependencyInjectionSpringContextTests {

	private IAccounterClientService clientService;

	public void setClientService(IAccounterClientService clientService) {
		this.clientService = clientService;
	}

	public void testUser() {

		ClientUser user = new ClientUser();

		user.setEmail("test@test1.com");
		user.setPasswordSha1Hash("some");

		try {

			clientService.createUser(user);

		} catch (DAOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected String getConfigPath() {
		return "/test-context.xml";
	}

}
