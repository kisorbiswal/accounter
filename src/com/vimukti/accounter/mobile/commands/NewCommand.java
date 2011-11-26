package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;

public abstract class NewCommand extends Command {
	private long companyId;
	private Company company;
	private ClientCompanyPreferences preferences;
	private List<Integer> requirementSequence = new ArrayList<Integer>();

	@SuppressWarnings("unchecked")
	@Override
	public Result run(Context context) {
		setCompany(context.getCompany());
		setPreferences(context.getPreferences());
		companyId = getCompany() == null ? 0 : getCompany().getID();
		Result result = process(context);
		List<String> first = (List<String>) context
				.getAttribute("firstMessage");
		for (String f : first) {
			result.add(0, f);
		}
		// result.setTitle(getTitle());
		return result;
	}

	private int requirementNumber;

	// @SuppressWarnings("unchecked")
	public Result process(Context context) {
		context.setAttribute("firstMessage", new ArrayList<String>());
		setDefaultValues(context);
		Result makeResult = context.makeResult();
		if (getAttribute("input") == null) {
			String lowerCase = context.getCommandString().toLowerCase();
			String nextCommandString = initObject(
					context,
					lowerCase.startsWith("update")
							|| lowerCase.startsWith("edit"));
			if (nextCommandString != null) {
				Result result = new Result();
				result.setNextCommand(nextCommandString);
				markDone();
				return result;
			}
			setAttribute("input", "");
		}

		ResultList actions = new ResultList("actions");

		List<Requirement> allRequirements = getRequirements();
		String backString = context.getString();
		if (backString != null && backString.equalsIgnoreCase("back")) {
			context.setString("");
			for (int i = requirementNumber; i >= 0; i--) {
				context.setAttribute("processAttr", "");
				Requirement requirement = allRequirements.get(i);
				context.putSelection("values", requirement.getName());
				ResultList list2 = new ResultList("");
				Result process = requirement.process(context,
						context.makeResult(), list2, actions);
				if (process != null) {
					requirementNumber = i - 1;
					// if (i != 0) {
					// process.setShowBack(true);
					// }
					return process;
				}
			}
			// TODO if user types back for first requirement
		}
		ResultList list = new ResultList("values");
		makeResult.add(list);
		actions = new ResultList("actions");
		for (int i = 0; i < allRequirements.size(); i++) {
			Result result = allRequirements.get(i).process(context,
					makeResult, list, actions);
			if (result != null) {
				requirementNumber = i - 1;
				// if (i != 0) {
				// result.setShowBack(true);
				// }
				return result;
			}
		}
		list.setTitle(getDetailsMessage());
		requirementNumber = -1;
		makeResult.add(actions);
		String finish = getFinishCommandString();
		if (finish != null) {
			Record record = new Record(ActionNames.FINISH_COMMAND);
			record.add("", finish);
			actions.add(record);
		}
		Object selection = context.getSelection("actions");
		beforeFinishing(context, makeResult);
		if (selection != ActionNames.FINISH_COMMAND) {
			return makeResult;
		}

		Result result = onCompleteProcess(context);
		if (result != null) {
			List<Object> resultParts = makeResult.getResultParts();
			for (Object object : result.getResultParts()) {
				resultParts.add(0, object);
			}
			return makeResult;
		}

		Result finishResult = context.makeResult();
		String success = getSuccessMessage();
		if (success != null) {
			finishResult.add(success);
		}
		markDone();
		return finishResult;
	}

	protected abstract String initObject(Context context, boolean isUpdate);

	protected abstract String getWelcomeMessage();

	protected Result onCompleteProcess(Context context) {
		return null;
	}

	public void beforeFinishing(Context context, Result makeResult) {
	}

	public String getFinishCommandString() {
		return "Finish";
	}

	protected abstract String getDetailsMessage();

	protected abstract void setDefaultValues(Context context);

	public abstract String getSuccessMessage();

	public long getCompanyId() {
		return companyId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}
}