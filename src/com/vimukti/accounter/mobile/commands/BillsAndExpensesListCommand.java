package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.services.DAOException;
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
public class BillsAndExpensesListCommand extends AbstractTransactionCommand {

	private static final String BILLS_VIEW_BY = "Current View";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(BILLS_VIEW_BY, true, true));
	}

	@Override
	public Result run(Context context) {
		Result result = context.makeResult();

		setDefaultValues();
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		return result;
	}

	private void setDefaultValues() {
		get(BILLS_VIEW_BY).setDefaultValue(ALL);
	}

	private Result createOptionalResult(Context context) {

		List<String> viewType = new ArrayList<String>();
		viewType.add(NOT_ISSUED);
		viewType.add(ISSUED);
		viewType.add(VOIDED);
		viewType.add(ALL);

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
				selection, BILLS_VIEW_BY, "Current View", viewType,
				"Select View type", ITEMS_TO_SHOW);
		if (result != null) {
			return result;
		}

		String view = get(BILLS_VIEW_BY).getValue();
		result = getBillsList(context, view);
		result.add(resultList);
		return result;

	}

	private Result getBillsList(Context context, String view) {

		try {
			ArrayList<BillsList> billsList = new FinanceTool()
					.getVendorManager().getBillsList(false,
							context.getCompany().getID());
			ArrayList<BillsList> filterList = filterList(view, billsList);

			Result result = context.makeResult();
			ResultList resultList = new ResultList("billsList");
			for (BillsList bill : filterList) {
				resultList.add(createBillRecord(bill));
			}

			StringBuilder message = new StringBuilder();
			if (resultList.size() == 0) {
				message.append("Add Bill Record");
			}

			result.add(message.toString());
			result.add(resultList);

			CommandList commandList = new CommandList();
			commandList.add("Add a New Bill");
			result.add(commandList);
			return result;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected ArrayList<BillsList> filterList(String text,
			List<BillsList> allRecords) {
		ArrayList<BillsList> list = new ArrayList<BillsList>();
		if (text.equalsIgnoreCase(OPEN)) {

			for (BillsList rec : allRecords) {
				if ((rec.getType() == ClientTransaction.TYPE_ENTER_BILL || rec
						.getType() == ClientTransaction.TYPE_VENDOR_CREDIT_MEMO)
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					if (!rec.isDeleted() && !rec.isVoided())
						list.add(rec);
				}
			}

		} else if (text.equalsIgnoreCase(VOIDED)) {
			for (BillsList rec : allRecords) {
				if (rec.isVoided() && !rec.isDeleted()) {
					list.add(rec);
				}
			}

		} else if (text.equalsIgnoreCase(OVER_DUE)) {
			for (BillsList rec : allRecords) {
				if (rec.getType() == ClientTransaction.TYPE_ENTER_BILL
						&& new ClientFinanceDate().after(rec.getDueDate())
						&& DecimalUtil.isGreaterThan(rec.getBalance(), 0)) {
					list.add(rec);
				}
			}

		}
		if (text.equalsIgnoreCase(ALL)) {
			list.addAll(allRecords);
		}
		return list;
	}

	private Record createBillRecord(BillsList bill) {

		Record rec = new Record(bill);
		rec.add("Type", bill.getType());
		rec.add("No", bill.getNumber());
		rec.add("VendorName", bill.getVendorName());
		rec.add("OrginalAmount", bill.getOriginalAmount());
		rec.add("Balance", bill.getBalance());

		return rec;
	}

}
