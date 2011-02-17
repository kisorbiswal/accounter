package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.IssuePaymentDialog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;

public class TransactionIssuePaymentGrid extends
		AbstractTransactionGrid<ClientTransactionIssuePayment> {

	private double total = 0.0;
	private IssuePaymentDialog issuePaymentView;

	public TransactionIssuePaymentGrid() {
		super(true, true);
	}

	@Override
	protected String[] getColumns() {
		// addFooterValue("Total", 3);
		return new String[] { FinanceApplication.getVendorsMessages().Date(),
				FinanceApplication.getVendorsMessages().number(),
				FinanceApplication.getVendorsMessages().name(),
				FinanceApplication.getVendorsMessages().memo(),
				FinanceApplication.getVendorsMessages().Amount(),
				FinanceApplication.getVendorsMessages().Paymentmethod() };
	}

	public boolean isSelected(ClientTransactionIssuePayment transactionList) {
		return ((CheckBox) getWidget(indexOf(transactionList), 0)).getValue();
	}

	@Override
	public boolean validateGrid() {
		boolean flag = true;

		if (this.getRecords().isEmpty()) {
			flag = false;
		} else if (flag) {
			flag = isEmptyGrid();
		}
		if (!flag)
			Accounter.showError(AccounterErrorType.blankTransaction);
		return flag;
	}

	public boolean isEmptyGrid() {
		for (ClientTransactionIssuePayment bill : getRecords()) {
			if (isSelected(bill)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Object getColumnValue(ClientTransactionIssuePayment issuepayment,
			int index) {
		switch (index) {
		case 0:
			return issuepayment.getDate() != 0 ? UIUtils
					.getDateByCompanyType(new ClientFinanceDate(issuepayment
							.getDate())) : null;
		case 1:
			return issuepayment.getNumber();
		case 2:
			return issuepayment.getName();
		case 3:
			return issuepayment.getMemo();
		case 4:
			return DataUtils.getAmountAsString(issuepayment.getAmount());
		case 5:
			return issuepayment.getPaymentMethod() != null ? issuepayment
					.getPaymentMethod() : FinanceApplication
					.getVendorsMessages().check();
		default:
			return null;
		}
	}

	public void setIssuePaymentView(IssuePaymentDialog issuePaymentView) {
		this.issuePaymentView = issuePaymentView;
	}

	@Override
	public void onDoubleClick(ClientTransactionIssuePayment obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionIssuePayment obj,
			int colIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_DATE;
		default:
			return ListGrid.COLUMN_TYPE_TEXT;
		}

	}

	@Override
	protected boolean isEditable(ClientTransactionIssuePayment obj, int row,
			int index) {
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected void onSelectionChanged(ClientTransactionIssuePayment obj,
			int row, boolean isChecked) {
		if (isChecked) {
			total += obj.getAmount();
			issuePaymentView.totalAmount = total;
			updateFooterValues(DataUtils.getAmountAsString(total), 4);
		} else {
			total -= obj.getAmount();
			issuePaymentView.totalAmount = total;
			updateFooterValues(DataUtils.getAmountAsString(total), 4);
		}
		super.onSelectionChanged(obj, row, isChecked);
	}

	@Override
	public void onHeaderCheckBoxClick(boolean isChecked) {
		total = 0;
		if (isChecked) {
			for (ClientTransactionIssuePayment rec : getRecords())
				total += rec.getAmount();
			issuePaymentView.totalAmount = total;
		} else {
			total = 0;
			issuePaymentView.totalAmount = total;
		}
		updateFooterValues(DataUtils.getAmountAsString(total), 4);
		super.onHeaderCheckBoxClick(isChecked);
	}

	@Override
	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTaxCode(String taxCode) {
		// TODO Auto-generated method stub
		
	}

}
