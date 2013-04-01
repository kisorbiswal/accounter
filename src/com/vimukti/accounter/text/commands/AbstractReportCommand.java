package com.vimukti.accounter.text.commands;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.web.client.core.ReportInput;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.managers.ExportManager;

public abstract class AbstractReportCommand extends AbstractTextCommand {

	private FinanceDate startDate;
	private FinanceDate endDate;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Parsing the Start and End Dates
		parseDates(data, respnse);
		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		// adding File name to response
		addReportFileNameToResponse(respnse);
	}

	/**
	 * Parse The Start Date And End Date
	 * 
	 * @param data
	 * @param respnse
	 */
	public boolean parseDates(ITextData data, ITextResponse respnse) {
		// START DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		startDate = data.nextDate(new FinanceDate());

		// END DATE
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		// if next date is null,then set the default present date
		endDate = data.nextDate(new FinanceDate());
		return true;
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
			ReportInput... inputs) {
		ExportManager manager = getFinanceTool().getExportManager();
		// Checking Start Date is null or not.
		if (startDate == null) {
			startDate = new FinanceDate();
		}
		// Checking End Date is null or not.
		if (endDate == null) {
			endDate = new FinanceDate();
		}
		List<ReportInput> reportInputs = Arrays.asList(inputs);
		try {
			List<String> exportReportToFiles = manager.exportReportToFile(
					getCompany().getId(), ReportInput.REPORT_EXPORT_TYPE_PDF,
					getReportType(), startDate.getDate(), endDate.getDate(),
					reportInputs);
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
