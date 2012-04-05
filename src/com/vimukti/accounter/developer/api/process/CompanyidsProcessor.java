package com.vimukti.accounter.developer.api.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompanyidsProcessor extends ApiProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		Map<String, Long> companyIds = new HashMap<String, Long>();
		String emailId = (String) req.getAttribute("emailId");
		Client client = getClient(emailId);
		Set<User> users = client.getUsers();
		for (User user : users) {
			Company company = user.getCompany();
			companyIds.put(company.getTradingName(), company.getID());
		}
		sendResult(companyIds);
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Client client = (Client) session.getNamedQuery("getClient.by.mailId")
				.setString("emailId", emailId).uniqueResult();
		return client;
	}

}