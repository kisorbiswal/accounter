package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class CustomerStatementReportCommand extends
		NewAbstractReportCommand<PayeeStatementsList> {
	private Customer customer;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new CustomerRequirement(CUSTOMER, getMessages().pleaseEnter(
				getMessages().customer() + getMessages().name()), getMessages()
				.customer() + getMessages().name(), false, true,
				new ChangeListner<Customer>() {

					@Override
					public void onSelection(Customer value) {
						customer = value;
					}
				}) {

			@Override
			protected List<Customer> getLists(Context context) {
				return getCustomers();
			}
		});
		addDateRangeFromToDateRequirements(list);
		super.addRequirements(list);
	}

	@Override
	protected Record createReportRecord(PayeeStatementsList record) {
		Record statementRecord = new Record(record);
		statementRecord.add("Date", record.getTransactionDate());
		statementRecord.add("Type",
				Utility.getTransactionName(record.getTransactiontype()));
		statementRecord.add("No.", record.getTransactionNumber());
		statementRecord.add("Aging", record.getDueDate());
		if (record.getTransactiontype() == Transaction.TYPE_INVOICE)
			statementRecord.add("Amount", record.getTotal());
		if (record.getTransactiontype() == Transaction.TYPE_RECEIVE_PAYMENT)
			statementRecord.add("Amount", record.getTotal());
		statementRecord.add("Amount", record.getBalance());

		return statementRecord;
	}

	@Override
	public Result run(Context context) {

		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<PayeeStatementsList> getRecords() {
		ArrayList<PayeeStatementsList> payeeStatementList = new ArrayList<PayeeStatementsList>();
		try {
			payeeStatementList = new FinanceTool().getCustomerManager()
					.getCustomerStatement(customer.getID(),
							getStartDate().getDate(), getEndDate().getDate(),
							getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payeeStatementList;
	}

	@Override
	protected String addCommandOnRecordClick(PayeeStatementsList selection) {
		return "update transaction" + selection.getTransactionId();
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAnyReports();
	}

	@Override
	protected String getShowMessage() {
		return "";
	}

	@Override
	protected String getSelectRecordString() {
		return getMessages().reportSelected(
				getMessages().Customer() + getMessages().statement());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().Customer() + getMessages().statement());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().Customer() + getMessages().statement());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().Customer() + getMessages().statement());
	}

}