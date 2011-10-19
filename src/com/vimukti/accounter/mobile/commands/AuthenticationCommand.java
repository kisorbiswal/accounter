/**
 * 
 */
package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.sun.org.apache.regexp.internal.recompile;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AuthenticationCommand extends AbstractCommand {

	private static final String USER_NAME = "userName";
	private static final String PASSWORD = "password";
	private static final String COMPANY_NAME = "companyName";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(USER_NAME, false, true));
		list.add(new Requirement(PASSWORD, false, true));
		list.add(new Requirement(COMPANY_NAME, false, true));
	}

	@Override
	public Result run(Context context) {

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "");
		}
		ResultList list = new ResultList("values");
		Result result = nameRequirement(context, list, USER_NAME,
				"Enter User Name.");
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, PASSWORD, "Enter Password");
		if (result != null) {
			return result;
		}
		result = companyNameRequirement(context, list);
		if (result != null) {
			return result;
		}
		Result makeResult = context.makeResult();
		makeResult.add("Your successfully logged in.");
		return makeResult;
	}

	private Result companyNameRequirement(Context context, ResultList list) {
		Requirement userNameReq = get(USER_NAME);
		String userName = userNameReq.getValue();
		Requirement passwordReq = get(PASSWORD);
		String password = passwordReq.getValue();
		Client client = getClient(userName, password);
		if (client == null) {
			Result makeResult = context.makeResult();
			makeResult.add("Wrong Username or Password");
			makeResult.add(list);
			return makeResult;
		}
		Requirement companyReq = get(COMPANY_NAME);
		Company selection = context.getSelection(COMPANY_NAME);
		if (selection != null) {
			companyReq.setValue(selection);
		}
		if (companyReq.isDone()) {
			Company value = companyReq.getValue();
			markDone();
			return null;
		}

		Result result = context.makeResult();
		result.add("Select a company");
		ResultList companyList = new ResultList(COMPANY_NAME);

		Set<User> users = client.getUsers();
		List<Company> companies = new ArrayList<Company>();
		for (User user : users) {
			if (!user.isDeleted()) {
				companies.add(user.getCompany());
			}
		}

		for (Company company : companies) {
			Record record = new Record(company);
			record.add("", company.getDisplayName());
			record.add("", company.getCountry());
			companyList.add(record);
		}
		result.add(companyList);
		return result;
	}

	private Client getClient(String emailId, String password) {
		if (emailId == null || password == null) {
			return null;
		}
		emailId = emailId.trim();
		password = HexUtil.bytesToHex(Security.makeHash(emailId
				+ password.trim()));

		Session session = HibernateUtil.getCurrentSession();
		try {
			Client client = null;
			Query query = session
					.getNamedQuery("getclient.from.central.db.using.emailid.and.password");
			query.setParameter("emailId", emailId);
			query.setParameter("password", password);
			client = (Client) query.uniqueResult();
			return client;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}
}
