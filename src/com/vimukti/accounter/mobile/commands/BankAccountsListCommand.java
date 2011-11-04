package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ClientAccount;

public class BankAccountsListCommand extends NewAbstractCommand {
	private static final String VIEW_TYPE = "Current View";
	private static final String ACTIVE = "Active";
	private static final String IN_ACTIVE = "In-Active";
	private static final int BANK_ACCOUNT = ClientAccount.TYPE_BANK;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<Account>("AccountsList",
				"Please Select ", 5) {
			@Override
			protected Record createRecord(Account value) {
				Record record = new Record(value);
				record.add("", value.getName());
				record.add("", value.getCurrentBalance());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create New Bank Account");
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> completeList = getAccounts(context);
				List<Account> result = new ArrayList<Account>();

				String type = get(VIEW_TYPE).getValue();

				for (Account account : completeList) {

					if (type.equals("Active")) {
						if (account.getIsActive())

							result.add(account);
					}
					if (type.equals("In-Active")) {
						if (!account.getIsActive())
							result.add(account);
					}

				}
				return result;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().vatCodeList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected String onSelection(Account value) {
				return null;
			}

		});

		list.add(new ActionRequirement(VIEW_TYPE, null) {
			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().active());
				list.add(getConstants().inActive());
				return list;
			}
		});
	}

	private List<Account> getAccounts(Context context) {
		Set<Account> accounts = context.getCompany().getAccounts();
		List<Account> result = new ArrayList<Account>();
		for (Account account : accounts) {
			if (account.getType() == Account.TYPE_BANK) {
				result.add(account);
			}
		}
		return result;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(VIEW_TYPE).setDefaultValue(getConstants().active());

	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

}
