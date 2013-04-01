package com.vimukti.accounter.text.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.web.client.core.ReportInput;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.managers.ExportManager;

public abstract class AbstractReportCommand implements ITextCommand {

	private FinanceDate startDate;
	private FinanceDate endDate;

	/**
	 * Parse The Start Date And End Date
	 * 
	 * @param data
	 * @param respnse
	 */
	public void parseDates(ITextData data, ITextResponse respnse) {
		// START DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
		}
		// if next date is null,then set the default present date
		startDate = data.nextDate(new FinanceDate());

		// END DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
		}
		// if next date is null,then set the default present date
		endDate = data.nextDate(new FinanceDate());
	}

	/**
	 * Get the Company
	 * 
	 * @return
	 */
	protected Company getCompany() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get Finance Tool
	 * 
	 * @return
	 */
	protected FinanceTool getFinanceTool() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Export Report To File
	 * 
	 * @param respnse
	 * @param arrayList
	 * 
	 * @return {@link List} File Names
	 */
	protected void addReportFileNameToResponse(ITextResponse respnse,
			ArrayList<ReportInput> inputs) {
		ExportManager manager = getFinanceTool().getExportManager();
		// Checking Start Date is null or not.
		if (startDate == null) {
			startDate = new FinanceDate();
		}
		// Checking End Date is null or not.
		if (endDate == null) {
			endDate = new FinanceDate();
		}
		try {
			List<String> exportReportToFiles = manager.exportReportToFile(
					getCompany().getId(), ReportInput.REPORT_EXPORT_TYPE_PDF,
					getReportType(), startDate.getDate(), endDate.getDate(),
					inputs);
			// adding File name to response
			for (String file : exportReportToFiles) {
				respnse.addFile(file);
			}
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the Report Type
	 * 
	 * @return
	 */
	public abstract int getReportType();
}
