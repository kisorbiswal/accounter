package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CustomerRequirement;
import com.vimukti.accounter.web.client.Global;
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
	}

	protected Record createReportRecord(PayeeStatementsList record) {
		Record statementRecord = new Record(record);
		statementRecord.add(getMessages().date(), record.getTransactionDate());
		statementRecord.add(getMessages().transactionName(),
				Utility.getTransactionName(record.getTransactiontype()));
		statementRecord.add(getMessages().number(),
				record.getTransactionNumber());
		statementRecord.add(getMessages().amount(), record.getTotal());
		statementRecord.add(getMessages().name(), record.getBalance());
		return statementRecord;
	}

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
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().payeeStatement(Global.get().Customer()));
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(
				getMessages().payeeStatement(Global.get().Customer()));
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().Customer() + getMessages().statement());
	}

}