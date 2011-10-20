package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;

public class BankAccountsListsCommands extends AbstractTransactionCommand {
	private String VIEW_TYPE = getConstants().currentView();
	private String ACTIVE = getConstants().active();
	private String IN_ACTIVE = getConstants().inActive();
	private static final int BANK_ACCOUNT = ClientAccount.TYPE_BANK;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(VIEW_TYPE, true, true));

	}

	@Override
	public Result run(Context context) {

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = null;
		setDefaultValue();
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;

	}

	private void setDefaultValue() {
		get(VIEW_TYPE).setDefaultValue(ACTIVE);
	}

	private Result createOptionalResult(Context context) {

		List<String> viewType = new ArrayList<String>();
		viewType.add(ACTIVE);
		viewType.add(IN_ACTIVE);

		ResultList resultList = new ResultList("values");
		Object selection = context.getSelection(ACTIONS);
		ActionNames actionNames;
		if (selection != null) {
			actionNames = (ActionNames) selection;
			switch (actionNames) {
			case FINISH:
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		Result result = stringListOptionalRequirement(context, resultList,
				selection, VIEW_TYPE, VIEW_TYPE, viewType, getMessages()
						.pleaseSelect(getConstants().currentView()),
				BANK_ACCOUNT);
		if (result != null) {
			return result;
		}

		String view = get(VIEW_TYPE).getValue();
		result = getAccounts(context, view);
		result.add(resultList);
		return result;
	}

	private Result getAccounts(Context context, String view) {
		Result result = context.makeResult();
		ResultList resultList = new ResultList("accountsList");

		ArrayList<ClientAccount> accountsList = getClientCompany().getAccounts(
				ClientAccount.TYPE_BANK);

		ArrayList<ClientAccount> list = filterList(accountsList, view);
		for (ClientAccount account : list) {
			resultList.add(createAccountRecord(account));
		}

		result.add(resultList);
		CommandList commandList = new CommandList();
		commandList.add(getMessages().addNew(Global.get().Account()));
		result.add(commandList);
		return result;
	}

	private ArrayList<ClientAccount> filterList(
			ArrayList<ClientAccount> accountsList, String view) {

		ArrayList<ClientAccount> list = new ArrayList<ClientAccount>();

		for (ClientAccount account : accountsList) {

			if (account.getIsActive() && view.equals(ACTIVE)) {

				list.add(account);
			} else if (!account.getIsActive() && view.equals(IN_ACTIVE)) {
				list.add(account);
			}

		}
		return list;

	}

}
