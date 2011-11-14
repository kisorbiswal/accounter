package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.Global;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class AccountsListCommand extends NewAbstractCommand {

	private static final String ACCOUNT_TYPE = "accountType";
	private String destType;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new ShowListRequirement<Account>("Accounts",
				"Please Enter name or number", 10) {
			@Override
			protected void setSelectCommands(CommandList commandList,
					Account value) {
				if (destType == null) {
					commandList.add(new UserCommand("Edit account", value
							.getName()));
					commandList.add(new UserCommand("Delete account", value
							.getName()));
					commandList.add(new UserCommand("Register account", value
							.getName()));
				}
			}

			@Override
			protected Record createRecord(Account value) {
				Record record = new Record(value);
				record.add("", value.getName());
				record.add("", value.getTotalBalance());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create Account");
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> list = new ArrayList<Account>();
				Set<Account> accounts = context.getCompany().getAccounts();
				String type = AccountsListCommand.this.get(ACCOUNT_TYPE)
						.getValue();
				if (type.equals("All Accounts")) {
					return new ArrayList<Account>(accounts);
				}
				if (type.equals("Active Accounts")) {
					for (Account a : accounts) {
						if (a.getIsActive()) {
							list.add(a);
						}
					}
				} else {
					for (Account a : accounts) {
						if (!a.getIsActive()) {
							list.add(a);
						}
					}
				}
				return list;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().payeesList(Global.get().Accounts());
			}

			@Override
			protected String getEmptyString() {
				return "There are no accounts";
			}

			@Override
			protected String onSelection(Account value) {
				if (destType.equals("update")) {
					return "Update account " + value.getName();
				}
				if (destType.equals("register")) {
					return "Register account " + value.getName();
				}

				return null;
			}
		});

		list.add(new ActionRequirement(ACCOUNT_TYPE, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add("Active Accounts");
				list.add("In Active Accounts");
				list.add("All Accounts");
				return list;
			}
		});

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
		get(ACCOUNT_TYPE).setDefaultValue("All Accounts");
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		String[] split = string.split(",");
		if (split.length > 2) {
			destType = split[1];
			context.setString(split[0]);
		}
		return null;
	}
}
