package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class BillsAndExpensesListCommand extends AbstractTransactionListCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isKeepTrackofBills()) {
			addFirstMessage(context, "You do not have permissions to do this.");
			return "cancel";
		}
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
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().open());

	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);

		list.add(new ShowListRequirement<BillsList>("BillsAndExpenses", "", 20) {

			@Override
			protected String onSelection(BillsList value) {
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().bills();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().bills());
			}

			@Override
			protected Record createRecord(BillsList value) {
				Record rec = new Record(value);
				rec.add(getMessages().transactionName(),
						Utility.getTransactionName((value.getType())));
				rec.add(getMessages().number(), value.getNumber());
				rec.add(Global.get().Vendor(), value.getVendorName());
				rec.add(getMessages().originalAmount(),
						getCurrency(value.getCurrency()).getSymbol() + " "
								+ value.getOriginalAmount());
				rec.add(getMessages().balance(),
						getCurrency(value.getCurrency()).getSymbol() + " "
								+ value.getBalance());
				return rec;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newEnterBill");
			}

			@Override
			protected boolean filter(BillsList e, String name) {
				return e.getVendorName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<BillsList> getLists(Context context) {

				return getListData(context);
			}
		});
	}

	protected List<BillsList> getListData(Context context) {
		String viewBY = get(VIEW_BY).getValue();
		ArrayList<BillsList> list = new ArrayList<BillsList>();
		ArrayList<BillsList> allRecords = null;
		try {
			allRecords = new FinanceTool().getVendorManager().getBillsList(
					false, context.getCompany().getID(), 0,
					getStartDate().getDate(), getEndDate().getDate(),0,-1);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (viewBY.equalsIgnoreCase(getMessages().open())) {

			for (BillsList rec : allRecords) {
				if ((rec.getType() == ClientTransaction.TYPE_ENTER_BILL || rec
						.getType() == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO)
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					if (!rec.isDeleted() && !rec.isVoided())
						list.add(rec);
				}
			}

		} else if (viewBY.equalsIgnoreCase(getMessages().voided())) {
			for (BillsList rec : allRecords) {
				if (rec.isVoided() && !rec.isDeleted()) {
					list.add(rec);
				}
			}

		} else if (viewBY.equalsIgnoreCase(getMessages().overDue())) {
			for (BillsList rec : allRecords) {
				if (rec.getType() == ClientTransaction.TYPE_ENTER_BILL
						&& new ClientFinanceDate().after(rec.getDueDate())
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					list.add(rec);
				}
			}

		}
		if (viewBY.equalsIgnoreCase(getMessages().all())) {
			list.addAll(allRecords);
		}
		return list;
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().open());
		list.add(getMessages().voided());
		list.add(getMessages().overDue());
		list.add(getMessages().all());
		return list;
	}

}