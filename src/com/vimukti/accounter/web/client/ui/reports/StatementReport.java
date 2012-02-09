package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;
import java.util.Map;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;

public class StatementReport extends AbstractReportView<PayeeStatementsList> {
	public int precategory = 1001;
	private long payeeId = 0;
	private boolean isVendor;

	public StatementReport(boolean isVendor) {
		this.isVendor = isVendor;
		this.serverReport = new StatementServerReport(isVendor, this);
	}

	public StatementReport(boolean isVendor, long payeeId) {
		this.setPayeeId(payeeId);
		this.isVendor = isVendor;
		this.serverReport = new StatementServerReport(isVendor, this);
	}

	@Override
	public void init() {

		super.init();
		this.toolbar.setPayeeId(this.getPayeeId());
		// this.makeReportRequest(payeeId, toolbar.getStartDate(),
		// toolbar.getEndDate());

	}

	@Override
	protected ClientCurrency getCurrency() {
		ClientCompany company = Accounter.getCompany();
		ClientPayee payee = company.getPayee(getPayeeId());
		if (payee != null) {
			return company.getCurrency(payee.getCurrency());
		} else {
			return super.getCurrency();
		}
	}

	@Override
	public int getToolbarType() {
		if (isVendor)
			return TOOLBAR_TYPE_VENDOR;
		else
			return TOOLBAR_TYPE_CUSTOMER;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		this.makeReportRequest(getPayeeId(), start, end);
	}

	@Override
	public void makeReportRequest(long payee, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		// resetReport(endDate, endDate);
		grid.clear();
		// grid.addLoadingImagePanel();
		if (payee != 0) {
			setPayeeId(payee);
		}
		Accounter.createReportService().getStatements(isVendor, getPayeeId(),
				toolbar.getViewId(), startDate, endDate, this);

	}

	@Override
	public void OnRecordClick(PayeeStatementsList record) {
		record.setStartDate(toolbar.getEndDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactiontype(),
					record.getTransactionId());

	}

	@Override
	public void print() {

		if (getPayeeId() == 0) {
			if (isVendor) {
				Accounter.showError(messages
						.pleaseSelect(Global.get().Vendor()));
			} else {
				Accounter.showError(messages.pleaseSelect(Global.get()
						.Customer()));
			}
		} else {
			if (isVendor) {
				UIUtils.generateReportPDF(
						Integer.parseInt(String.valueOf(startDate.getDate())),
						Integer.parseInt(String.valueOf(endDate.getDate())),
						167, "", "", getPayeeId());
			} else {
				UIUtils.generateReportPDF(
						Integer.parseInt(String.valueOf(startDate.getDate())),
						Integer.parseInt(String.valueOf(endDate.getDate())),
						150, "", "", getPayeeId());
			}
		}
	}

	@Override
	public void exportToCsv() {
		if (getPayeeId() == 0) {
			if (isVendor) {
				Accounter.showError(messages
						.pleaseSelect(Global.get().Vendor()));
			} else {
				Accounter.showError(messages.pleaseSelect(Global.get()
						.Customer()));
			}
		} else {
			if (isVendor) {
				UIUtils.exportReport(
						Integer.parseInt(String.valueOf(startDate.getDate())),
						Integer.parseInt(String.valueOf(endDate.getDate())),
						167, "", "", getPayeeId());
			} else {
				UIUtils.exportReport(
						Integer.parseInt(String.valueOf(startDate.getDate())),
						Integer.parseInt(String.valueOf(endDate.getDate())),
						150, "", "", getPayeeId());
			}
		}
	}

	@Override
	public void printPreview() {

	}

	@Override
	public PayeeStatementsList getObject(PayeeStatementsList parent,
			PayeeStatementsList child) {
		return super.getObject(parent, child);
	}

	@Override
	public int sort(PayeeStatementsList obj1, PayeeStatementsList obj2, int col) {
		int ret = UIUtils.compareInt(obj1.getCategory(), obj2.getCategory());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return UIUtils.compareTo(obj1.getTransactionNumber(),
						obj2.getTransactionNumber());

		case 1:
			return UIUtils.compareInt(obj1.getTransactiontype(),
					obj2.getTransactiontype());

		case 3:
			return UIUtils.compareDouble(obj1.getAgeing(), obj2.getAgeing());

		case 0:
			return UIUtils.compareTo(obj1.getTransactionDate(),
					obj2.getTransactionDate());

		case 4:
			return UIUtils.compareDouble(
					obj1.getTotal() * obj1.getCurrencyFactor(), obj2.getTotal()
							* obj1.getCurrencyFactor());

		}
		return 0;
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public void restoreView(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		long status1 = ((Long) map.get("statement"));
		setPayeeId(status1);
		toolbar.setPayeeId(status1);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		Boolean isVendor = (Boolean) map.get("isVendor");
		this.isVendor = isVendor == null ? false : isVendor;
		isDatesArranged = true;
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		long status = getPayeeId();
		map.put("selectedDateRange", selectedDateRange);
		map.put("statement", status);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("isVendor", isVendor);
		return map;
	}

	public long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(long payeeId) {
		this.payeeId = payeeId;
	}

}
