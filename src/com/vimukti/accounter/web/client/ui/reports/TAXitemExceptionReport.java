package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.TAXItemExceptionDetailServerReport;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TAXitemExceptionReport extends AbstractReportView<TAXItemDetail> {

	private long taxAgency;
	private long taxReturnId;
	private int row;
	private TaxAgencyStartDateEndDateToolbar toolBar;
	boolean fromReport = false;

	public TAXitemExceptionReport() {
		super(false, messages.noRecordsToShow());
		this.serverReport = new TAXItemExceptionDetailServerReport(this);

	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_TAXAGENCY;
	}

	@Override
	public void makeReportRequest(long vatAgency, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		this.row = -1;
		// this.taxAgency = vatAgency;
		toolBar = (TaxAgencyStartDateEndDateToolbar) this.toolbar;
		this.taxAgency = toolBar.taxAgencyCombo.getSelectedValue().getID();
		fromReport = true;
		Accounter.createReportService().getTAXItemExceptionDetailReport(
				this.taxAgency, startDate.getDate(), endDate.getDate(),
				fromReport, this);

	}

	@Override
	public void OnRecordClick(TAXItemDetail record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());

	}

	@Override
	public void print() {
		this.startDate = toolbar.getStartDate();
		this.endDate = toolbar.getEndDate();

		if (fromReport) {
			// pass agency
			UIUtils.generateReportPDF(
					Integer.parseInt(String.valueOf(this.startDate.getDate())),
					Integer.parseInt(String.valueOf(this.endDate.getDate())),
					171, "true", "", this.taxAgency);
		} else {
			// pass tax_return_id
			UIUtils.generateReportPDF(
					Integer.parseInt(String.valueOf(this.startDate.getDate())),
					Integer.parseInt(String.valueOf(this.endDate.getDate())),
					171, "false", "", String.valueOf(taxReturnId));
		}
	}

	@Override
	public void exportToCsv() {
		this.startDate = toolbar.getStartDate();
		this.endDate = toolbar.getEndDate();
		if (fromReport) {
			// pass agency
			UIUtils.exportReport(
					Integer.parseInt(String.valueOf(startDate.getDate())),
					Integer.parseInt(String.valueOf(endDate.getDate())), 171,
					"true", String.valueOf(this.taxAgency));
		} else {
			// pass tax_return_id
			UIUtils.exportReport(
					Integer.parseInt(String.valueOf(startDate.getDate())),
					Integer.parseInt(String.valueOf(endDate.getDate())), 171,
					"false", String.valueOf(taxReturnId));
		}
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// toolBar = (TaxAgencyStartDateEndDateToolbar) this.toolbar;
		// this.taxAgency = toolBar.taxAgencyCombo.getSelectedValue().getID();
		// this.startDate = toolbar.getStartDate();
		// this.endDate = toolbar.getEndDate();
		// Accounter.createReportService().getTAXItemExceptionDetailReport(
		// this.taxAgency, startDate.getDate(), endDate.getDate(), this);

	}

	@Override
	public void initData() {

		Object data = getData();
		if (data != null) {
			List<TAXItemDetail> detail = (List<TAXItemDetail>) data;
			// TAXItemDetail obj = detail.get(detail.size() - 1);
			// startDate = this.serverReport.getStartDate(obj);
			// endDate = this.serverReport.getEndDate(obj);
			ClientTAXAgency taxAgency2 = null;
			for (TAXItemDetail td : detail) {
				ClientTAXItem taxItemByName = Accounter.getCompany()
						.getTaxItemByName(td.getTaxItemName());
				taxAgency2 = Accounter.getCompany().getTaxAgency(
						taxItemByName.getTaxAgency());
				this.startDate = td.getStartDate();
				this.endDate = td.getEndDate();
				this.taxAgency = taxAgency2.getID();
			}
			toolBar = (TaxAgencyStartDateEndDateToolbar) this.toolbar;
			toolBar.taxAgencyCombo.setComboItem(taxAgency2);
			toolBar.taxAgencyCombo.setDisabled(true);
			toolBar.fromItem.setEnteredDate(this.startDate);
			toolBar.toItem.setEnteredDate(this.endDate);
			toolBar.fromItem.setDisabled(true);
			toolBar.toItem.setDisabled(true);
			toolBar.updateButton.setEnabled(false);
			this.serverReport.initRecords(detail);

		} else {
			super.initData();
		}
	}

	public long getTaxReturnId() {
		return taxReturnId;
	}

	public void setTaxReturnId(long taxReturnId) {
		this.taxReturnId = taxReturnId;
	}

}
