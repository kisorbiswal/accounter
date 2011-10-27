package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.TAXItemDetailServerReportView;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxItemDetailReportView extends AbstractReportView<TAXItemDetail> {

	private long taxAgency;
	private int row;
	private boolean isExceptionReport = false;

	public TaxItemDetailReportView(boolean isExceptionReport) {
		super(false, Accounter.constants().noRecordsToShow());
		this.serverReport = new TAXItemDetailServerReportView(this);
		this.isExceptionReport = isExceptionReport;

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
		if (!isExceptionReport) {
			Accounter.createReportService().getTAXItemDetailReport(
					this.taxAgency, startDate, endDate, this);
		} else {
			// TODO call Exception report RPC method
		}
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

}
