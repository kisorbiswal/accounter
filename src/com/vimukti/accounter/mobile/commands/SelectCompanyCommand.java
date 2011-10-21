package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * Selects the Company
 * 
 * @author vimutki35
 * 
 */
public class SelectCompanyCommand extends Command {

	private static final String COMPANY = "company";

	@Override
	public String getId() {
		return "selectCompany";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(COMPANY, false, true));

	}

	@Override
	public Result run(Context context) {
		Requirement companyReq = get(COMPANY);

		Company selection = context.getSelection(COMPANY);
		if (selection != null) {
			companyReq.setValue(selection);
		}

		Client client = context.getIOSession().getClient();
		if (!companyReq.isDone()) {
			if (client != null) {
				Result result = context.makeResult();
				result.add("Select a company");
				ResultList companyList = new ResultList(COMPANY);

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
		}
		Company value = companyReq.getValue();
		context.selectCompany(value, client);
		Result makeResult = context.makeResult();
		makeResult.add("Enter a Command");
		markDone();
		return makeResult;
	}

	private String getCompanyTypeAsString(int companyType) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}
}
