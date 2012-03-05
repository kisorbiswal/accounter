package com.vimukti.accounter.developer.api.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.developer.api.process.reports.ReportProcessor;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompanyidsProcessor extends ReportProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		// TODO Auto-generated method stub

	}

	private void sendCompanyIds(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
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