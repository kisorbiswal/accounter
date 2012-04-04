package com.vimukti.accounter.core.reports.generators;

import java.util.Set;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;

public class VendorStatementRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VENDORSTATEMENT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		StatementServerReport statementReport1 = new StatementServerReport(
				true, this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(statementReport1);
		statementReport1.resetVariables();
		try {
			statementReport1.onResultSuccess(financeTool.getReportManager()
					.getPayeeStatementsList(true, getInputAsLong(0), 0,
							startDate, endDate, company.getID()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		 ReportGridTemplate<PayeeStatementsList> gridTemplate = statementReport1.getGridTemplate();
		 gridTemplate.addAdditionalDetails(getStatementReportDetails());
		 return gridTemplate;
	}
	private String[] getStatementReportDetails() {
		String address1 = "", street = "", city = "", state = "", country = "";
		Payee payee = (Payee) financeTool.getManager().getServerObjectForid(
				AccounterCoreType.PAYEE, getInputAsLong(0));
		String payeeName = payee.getName();
		Set<Address> address = payee.getAddress();
		StringBuffer dataBuffer = new StringBuffer();
		for (Address clientAddress : address) {
			address1 = clientAddress.getAddress1().concat(",");
			street = clientAddress.getStreet().concat(",");
			city = clientAddress.getCity().concat(" - ")
					.concat(clientAddress.getZipOrPostalCode().concat(","));
			state = clientAddress.getStateOrProvinence().concat(",");
			country = clientAddress.getCountryOrRegion().concat(".");
			break;
		}
		AccounterMessages messages = Global.get().messages();
		String fromDate=messages.fromDate() + ": "+ startDate.toString();
		String toDate=messages.toDate() + ": "+ endDate.toString();
		return  new String[]{payeeName,address1,street,city,state,country, fromDate, toDate};
		
	}

}
