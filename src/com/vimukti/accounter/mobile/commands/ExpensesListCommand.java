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
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ExpensesListCommand extends NewAbstractCommand {

	protected static final String ALL = "all";
	protected static final String CREDIT_CARD = "Creditcard";
	protected static final String VOIDED = "voided";
	protected static final String CASH = "cash";
	protected static final String CURRENT_VIEW = "currentView";

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
		get(CURRENT_VIEW).setDefaultValue(getConstants().all());

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
		list.add(new ShowListRequirement<BillsList>(getConstants()
				.expensesList(), getMessages().pleaseSelect(
				getConstants().expensesList()), 5) {

			@Override
			protected String onSelection(BillsList value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().expensesList();
			}

			@Override
			protected String getEmptyString() {
				return getConstants().noRecordsToShow();
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
				List<BillsList> list = new ArrayList<BillsList>();
				List<BillsList> completeList = getExpenses(context);
				String type = ExpensesListCommand.this.get(CURRENT_VIEW)
						.getValue();
				for (BillsList order : completeList) {
					if (type.equals(getConstants().all())) {
						list.add(order);
					}
					if (type.equals(getConstants().cash())) {
						list.add(order);
					}
					if (type.equals(getConstants().creditCard())) {
						list.add(order);
					}
					if (type.equals(getConstants().voided())) {
						list.add(order);
					}
				}
				return list;
			}

		});
		list.add(new ActionRequirement(CURRENT_VIEW, null) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getConstants().all());
				list.add(getConstants().cash());
				list.add(getConstants().creditCard());
				list.add(getConstants().voided());
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

}
