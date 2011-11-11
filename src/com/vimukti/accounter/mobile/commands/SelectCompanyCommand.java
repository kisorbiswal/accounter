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
import com.vimukti.accounter.mobile.MobileSession;
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
		Result makeResult = context.makeResult();
		Object choice = context.getSelection("Choice");
		if (choice != null) {
			if (choice.equals("No")) {
				markDone();
				makeResult = context.makeResult();
				makeResult
						.add("Selecting Company has been canceled. Continue with your Company.");
				return makeResult;
			} else {
				makeResult.add("Your commands are canceled.");
				MobileSession ioSession = context.getIOSession();
				Command currentCommand = ioSession.getCurrentCommand();
				while (currentCommand != null) {
					currentCommand.markDone();
					ioSession.refreshCurrentCommand();
					currentCommand = ioSession.getCurrentCommand();
				}
			}
		}
		if (context.getString() == null || context.getString().isEmpty()) {
			Company company = context.getCompany();
			if (company != null && choice == null) {
				makeResult = context.makeResult();
				makeResult
						.add("If you want to change company, your previuos commands are getting canceled. proccede?");
				ResultList list = new ResultList("Choice");
				Record e = new Record("Yes");
				e.add("", "Yes (Cancel all my previous commands)");
				list.add(e);

				e = new Record("No");
				e.add("", "No (Continue with '" + company.getDisplayName()
						+ "')");
				list.add(e);
				makeResult.add(list);
				return makeResult;
			}
		}
		Requirement companyReq = get(COMPANY);

		Company selection = context.getSelection(COMPANY);
		if (selection != null) {
			makeResult.add(selection.getDisplayName() + " has been selected.");
			companyReq.setValue(selection);
		}
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
					makeResult.add("You have only '" + company.getDisplayName()
							+ "' company.");
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
					CommandList commandList = new CommandList();
					commandList.add("Create Company");
					makeResult.add(commandList);
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
