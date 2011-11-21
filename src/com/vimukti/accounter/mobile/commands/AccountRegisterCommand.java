package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class AccountRegisterCommand extends NewAbstractCommand {

	private static final String SHOWTRANSACTION_TYPE = "ShowTransactions Type";
	private static final String ACCOUNT = "account";
	private String selectedOption;
	private String selectedDateRange;
	private ClientFinanceDate startDate, todaydate, endDate;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		Account account = CommandUtils.getaccount(context.getCompany(), string);
		get(ACCOUNT).setValue(account);
		context.setString("");
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(SHOWTRANSACTION_TYPE).setDefaultValue(getMessages().all());
		selectedDateRange = getMessages().today();
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// account requirement
		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getMessages().account()), getMessages().account(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return new ArrayList<Account>(context.getCompany()
						.getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().account());
			}
		});

		list.add(new ShowListRequirement<AccountRegister>("Account Register",
				"Please Enter name or number", 10) {

			@Override
			protected String onSelection(AccountRegister value) {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return "No records to show";
			}

			@Override
			protected Record createRecord(AccountRegister accRegister) {
				Record record = new Record(accRegister);
				record.add("", accRegister.getDate());
				record.add("",
						CommandUtils.getTransactionName(accRegister.getType()));
				record.add("", accRegister.getNumber());
				record.add("", accRegister.getPayTo());
				record.add("", accRegister.getAmount());
				record.add("", accRegister.getMemo());
				record.add("", accRegister.getAccount());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				// TODO Auto-generated method stub
			}

			@Override
			protected boolean filter(AccountRegister e, String name) {
				return false;
			}

			@Override
			protected List<AccountRegister> getLists(Context context) {
				dateRangeChanged(context);

				return getAccountRegister(startDate, endDate,
						((Account) AccountRegisterCommand.this.get(ACCOUNT)
								.getValue()).getID());

			}

			@Override
			protected String getShowMessage() {
				// TODO Auto-generated method stub
				return null;
			}
		});

		list.add(new ActionRequirement(SHOWTRANSACTION_TYPE, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().all());
				list.add(getMessages().today());
				list.add(getMessages().last30Days());
				list.add(getMessages().last45Days());
				return list;
			}
		});

	}

	/**
	 * getting the account register.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param accountId
	 * @return
	 */
	public ArrayList<AccountRegister> getAccountRegister(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long accountId) {
		ArrayList<AccountRegister> accountRegisterList = new ArrayList<AccountRegister>();

		FinanceDate[] financeDates = CommandUtils.getMinimumAndMaximumDates(
				startDate, endDate, getCompanyId());
		try {
			accountRegisterList = new FinanceTool()
					.getAccountRegister(financeDates[0], financeDates[1],
							accountId, getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountRegisterList;
	}

	/**
	 * changed show transaction type
	 */
	private void dateRangeChanged(Context context) {

		todaydate = new ClientFinanceDate();
		selectedOption = get(SHOWTRANSACTION_TYPE).getValue();
		if (!selectedDateRange.equals(getMessages().all())
				&& selectedOption.equals(getMessages().all())) {
			startDate = CommandUtils.getCurrentFiscalYearStartDate(context
					.getPreferences());
			endDate = new ClientFinanceDate();
			selectedDateRange = getMessages().all();

		} else if (!selectedDateRange.equals(getMessages().today())
				&& selectedOption.equals(getMessages().today())) {
			startDate = todaydate;
			endDate = todaydate;
			selectedDateRange = getMessages().today();

		} else if (!selectedDateRange.equals(getMessages().last30Days())
				&& selectedOption.equals(getMessages().last30Days())) {
			selectedDateRange = getMessages().last30Days();
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 1, todaydate.getDay());
			endDate = todaydate;

		} else if (!selectedDateRange.equals(getMessages().last45Days())
				&& selectedOption.equals(getMessages().last45Days())) {

			selectedDateRange = getMessages().last45Days();
			startDate = new ClientFinanceDate(todaydate.getYear(),
					todaydate.getMonth() - 2, todaydate.getDay() + 16);
			endDate = todaydate;
		}
	}
}
