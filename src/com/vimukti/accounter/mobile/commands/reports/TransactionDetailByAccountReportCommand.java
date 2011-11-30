package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.managers.ReportManager;

public class TransactionDetailByAccountReportCommand extends
		NewAbstractReportCommand<TransactionDetailByAccount> {
	private double accountBalance;
	private String currentsectionName;
	ClientAccount account;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
	}

	protected Record createReportRecord(TransactionDetailByAccount record) {
		Record transactionRecord = new Record(record);
		transactionRecord.add("", "");
		transactionRecord.add("Name", record.getName());
		transactionRecord.add("Date", record.getTransactionDate());
		transactionRecord.add("",
				Utility.getTransactionName(record.getTransactionType()));
		transactionRecord.add("No.", record.getTransactionNumber());
		transactionRecord.add("Amount", record.getTotal());
		transactionRecord.add(getMessages().name(), record.getName());
		transactionRecord
				.add(getMessages().date(), record.getTransactionDate());
		transactionRecord.add(getMessages().transactionName(),
				Utility.getTransactionName(record.getTransactionType()));
		transactionRecord.add(getMessages().number(),
				record.getTransactionNumber());
		transactionRecord.add(getMessages().total(), record.getTotal());
		if (currentsectionName != null
				&& !currentsectionName.equals(record.getAccountName())) {
			currentsectionName = record.getAccountName();
			accountBalance = 0.0D;
		}
		transactionRecord.add(getMessages().balance(),
				accountBalance += record.getTotal());
		return transactionRecord;
	}

	protected List<TransactionDetailByAccount> getRecords() {
		List<TransactionDetailByAccount> transactionDetailsByAc = new ArrayList<TransactionDetailByAccount>();
		ReportManager reportManager = new FinanceTool().getReportManager();
		if (account == null) {
			try {
				transactionDetailsByAc = reportManager
						.getTransactionDetailByAccount(getStartDate(),
								getEndDate(), getCompanyId());
			} catch (DAOException e) {
				e.printStackTrace();
				transactionDetailsByAc = new ArrayList<TransactionDetailByAccount>();
			}
		} else {
			try {
				transactionDetailsByAc = reportManager
						.getTransactionDetailByAccount(account.getName(),
								getStartDate(), getEndDate(), getCompanyId());
			} catch (DAOException e) {
				e.printStackTrace();
				transactionDetailsByAc = new ArrayList<TransactionDetailByAccount>();
			}
		}
		return transactionDetailsByAc;
	}

	protected String addCommandOnRecordClick(
			TransactionDetailByAccount selection) {
		return "update transaction " + selection.getTransactionId();
	}

	@Override
	protected String getWelcomeMessage() {
		return "Transaction detail by account report command activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "Transaction detail by account report details as follows:";
	}

	@Override
	public String getSuccessMessage() {
		return "Transaction detail by account report command closed successfully";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String accountName = null;
		String string = context.getString();
		if (string != null) {
			String[] split = string.split(",");
			if (split.length > 1) {
				context.setString(split[0]);
				accountName = split[1];
			}
		}
		if (accountName != null) {
			account = CommandUtils
					.getAccountByNumber(getCompany(), accountName);
			if (account == null) {
				account = CommandUtils.getAccountByName(getCompany(),
						accountName);
			}
		}
		endDate = new ClientFinanceDate();
		get(TO_DATE).setValue(endDate);
		get(DATE_RANGE).setValue(getMessages().financialYearToDate());
		dateRangeChanged(getMessages().financialYearToDate());
		return null;
	}
}
