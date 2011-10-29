package com.vimukti.accounter.web.client.ui.reports;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.TAXItemDetailServerReportView;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TAXitemExceptionReport extends AbstractReportView<TAXItemDetail> {

	private long taxAgency;
	private int row;

	public TAXitemExceptionReport() {
		super(false, Accounter.constants().noRecordsToShow());
		this.serverReport = new TAXItemDetailServerReportView(this);

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
		this.taxAgency = vatAgency;

		Accounter.createReportService().getTAXItemDetailReport(this.taxAgency,
				startDate, endDate, this);
	}

	@Override
	public void OnRecordClick(TAXItemDetail record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());

	}

	@Override
	public void print() {
		UIUtils.generateReportPDF(
				Integer.parseInt(String.valueOf(startDate.getDate())),
				Integer.parseInt(String.valueOf(endDate.getDate())), 165, "",
				"", this.taxAgency);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {

		Object data = getData();
		if (data != null) {
			List<TAXItemDetail> detail = (List<TAXItemDetail>) data;
			ClientTAXAgency taxAgency2 = null;
			for (TAXItemDetail td : detail) {
				ClientTAXItem taxItemByName = Accounter.getCompany()
						.getTaxItemByName(td.getTaxItemName());
				taxAgency2 = Accounter.getCompany().getTaxAgency(
						taxItemByName.getTaxAgency());
				this.startDate = td.getStartDate();
				this.endDate = td.getEndDate();
			}
			TaxAgencyStartDateEndDateToolbar toolBar = (TaxAgencyStartDateEndDateToolbar) this.toolbar;
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
}
