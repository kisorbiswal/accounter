package com.vimukti.accounter.text.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.AbstractReportCommand;
import com.vimukti.accounter.web.client.core.ReportInput;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.managers.ExportManager;

/**
 * Profit and Loss Command
 * 
 * @author Lingarao.R
 * 
 */
public class ProfitAndLossCommand extends AbstractReportCommand {

	private FinanceDate startDate;
	private FinanceDate endDate;

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {

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

		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		ExportManager manager = getFinanceTool().getExportManager();
		try {
			List<String> exportReportToFile = manager.exportReportToFile(
					getCompany().getId(), ReportInput.REPORT_EXPORT_TYPE_PDF,
					111, startDate.getDate(), endDate.getDate(),
					new ArrayList<ReportInput>());
			// adding File name to response
			for (String file : exportReportToFile) {
				respnse.addFile(file);
			}
		} catch (AccounterException e) {
			e.printStackTrace();
		}
	}
}
