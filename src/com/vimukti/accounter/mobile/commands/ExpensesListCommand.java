package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ActionRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ExpensesListCommand extends NewAbstractCommand {

	protected static final String CURRENT_VIEW = "currentView";
	String type = " ";

	String name = "";

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();

		String[] split = string.split(",");

		if (split.length > 0) {
			name = split[0];
			context.setString(name);
		}
		if (split.length > 1) {
			type = split[1];
		}
		if (type.isEmpty()) {
			type = getMessages().all();
		}
		get(CURRENT_VIEW).setDefaultValue(type);
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
		get(CURRENT_VIEW).setDefaultValue(getMessages().all());

	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<BillsList>(getMessages()
				.expensesList(), getMessages().pleaseSelect(
				getMessages().expensesList()), 5) {

			@Override
			protected String onSelection(BillsList value) {

				return null;
			}

			@Override
			protected String getShowMessage() {
				return getMessages().expensesList();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(BillsList value) {
				Record record = new Record(value);
				record.add("", Utility.getTransactionName(value.getType()));
				record.add("", value.getDate());
				record.add("", value.getNumber());
				record.add("", value.getVendorName());
				record.add("", value.getOriginalAmount());
				record.add("", value.getBalance());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("New Cash Expense");
				list.add("New Credit Card Expense");

			}

			@Override
			protected boolean filter(BillsList e, String name) {
				return e.getVendorName().startsWith(name);
			}

			@Override
			protected List<BillsList> getLists(Context context) {
				return filterList(context);
			}

		});
		list.add(new ActionRequirement(CURRENT_VIEW, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().all());
				list.add(getMessages().cash());
				list.add(getMessages().creditCard());
				list.add(getMessages().voided());
				return list;
			}
		});
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private List<BillsList> getExpenses(Context context) {
		FinanceTool tool = new FinanceTool();
		try {
			ArrayList<BillsList> billsList = tool.getVendorManager()
					.getBillsList(true, context.getCompany().getId());
			return billsList;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;

	}

	protected List<BillsList> filterList(Context context) {
		List<BillsList> initialRecords = getExpenses(context);
		String text = ExpensesListCommand.this.get(CURRENT_VIEW).getValue();
		if (text.equalsIgnoreCase(getMessages().employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			return records;
		} else if (text.equalsIgnoreCase(getMessages().cash())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CASH_EXPENSE)
					records.add(record);
			}
			return records;
		} else if (text.equalsIgnoreCase(getMessages().creditCard())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_CREDIT_CARD_EXPENSE)
					records.add(record);
			}
			return records;
		} else if (text.equalsIgnoreCase(getMessages().employee())) {
			List<BillsList> records = new ArrayList<BillsList>();
			for (BillsList record : initialRecords) {
				if (record.getType() == ClientTransaction.TYPE_EMPLOYEE_EXPENSE)
					records.add(record);
			}
			return records;
		} else if (text.equalsIgnoreCase(getMessages().Voided())) {
			List<BillsList> voidedRecs = new ArrayList<BillsList>();
			List<BillsList> allRecs = initialRecords;
			for (BillsList rec : allRecs) {
				if (rec.isVoided() && !rec.isDeleted()) {
					voidedRecs.add(rec);
				}
			}
			return voidedRecs;

		} else if (text.equalsIgnoreCase(getMessages().all())) {
			return initialRecords;
		}
		return new ArrayList<BillsList>();

	}

}
