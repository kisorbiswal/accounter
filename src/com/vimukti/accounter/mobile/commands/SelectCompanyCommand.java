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
import com.vimukti.accounter.mobile.CommandList;
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
		Result makeResult = context.makeResult();
		Client client = context.getIOSession().getClient();
		if (!companyReq.isDone()) {
			if (client != null) {
				Set<User> users = client.getUsers();
				List<Company> companies = new ArrayList<Company>();

				for (User user : users) {
					if (!user.isDeleted()) {
						companies.add(user.getCompany());
					}
				}
				if (companies.size() == 1) {
					Company company = companies.get(0);
					companyReq.setValue(company);
					makeResult.add("You have only " + company.getDisplayName()
							+ " company is there.");
					makeResult.add("It has been selected.");
					makeResult.add("Press 'a' to create Another Company.");
					CommandList commandList = new CommandList();
					commandList.add("Create Company");
					makeResult.add(commandList);
				} else {
					ResultList companyList = new ResultList(COMPANY);
					if (companies.isEmpty()) {
						makeResult.add("You don't have any companies.");
						makeResult.setNextCommand("Create Company");
						return makeResult;
					}
					makeResult.add("Select a company");
					for (Company company : companies) {
						Record record = new Record(company);
						record.add("", company.getDisplayName());
						record.add("", company.getCountry());
						companyList.add(record);
					}
					makeResult.add(companyList);
					return makeResult;
				}
			}
		}
		Company value = companyReq.getValue();
		context.selectCompany(value, client);
		markDone();
		return makeResult;
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}
}
