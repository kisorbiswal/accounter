package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
	private Stack<Integer> requirementSequence = new Stack<Integer>();
	private int lastRequirement = -1;

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
		result.setShowBack(!isDone());
		return result;
	}

	public Result process(Context context) {
		context.setAttribute("firstMessage", new ArrayList<String>());
		Result makeResult = context.makeResult();
		if (getAttribute("input") == null) {
			setDefaultValues(context);
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
			if (requirementSequence.size() > 1) {
				context.setAttribute("processAttr", "");
				Requirement requirement = allRequirements.get(lastRequirement);
				context.putSelection("values", requirement.getName());
				ResultList list2 = new ResultList("");
				Result process = requirement.process(context,
						context.makeResult(), list2, actions);
				if (process != null) {
					return process;
				}
				requirementSequence.pop();
				lastRequirement = requirementSequence.firstElement();
			} else {
				Result result = new Result();
				result.setNextCommand("cancel");
				return result;
			}
		}
		ResultList list = new ResultList("values");
		makeResult.add(list);
		actions = new ResultList("actions");
		for (int i = 0; i < allRequirements.size(); i++) {
			Result result = allRequirements.get(i).process(context, makeResult,
					list, actions);
			if (result != null) {
				if (lastRequirement != i && i != 0) {
					requirementSequence.push(i);
				}
				lastRequirement = i;
				return result;
			}
		}
		list.setTitle(getDetailsMessage());
		makeResult.add(actions);
		String finish = getFinishCommandString();
		if (finish != null) {
			Record record = new Record(ActionNames.FINISH_COMMAND);
			record.add(finish);
			actions.add(record);
		}
		String deleteCommand = getDeleteCommand(context);

		Object selection = context.getSelection("actions");
		if (selection == ActionNames.DELETE_COMMAND) {
			if (deleteCommand != null) {
				Result result = new Result();
				result.setNextCommand(deleteCommand);
				context.getIOSession().getCurrentCommand().markDone();
				return result;
			}
		}

		if (deleteCommand != null) {
			Record record = new Record(ActionNames.DELETE_COMMAND);
			record.add("Delete");
			actions.add(record);
		}

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

	protected String getDeleteCommand(Context context) {
		return null;
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