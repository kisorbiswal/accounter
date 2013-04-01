package com.vimukti.accounter.text.commands.reports;

import java.util.ArrayList;

import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.AbstractReportCommand;
import com.vimukti.accounter.web.client.core.ReportInput;

/**
 * Profit and Loss Command
 * 
 * @author Lingarao.R
 * 
 */
public class ProfitAndLossCommand extends AbstractReportCommand {

	@Override
	public boolean parse(ITextData data, ITextResponse respnse) {
		// Parsing the Start and End Dates
		parseDates(data, respnse);
		return true;
	}

	@Override
	public void process(ITextResponse respnse) {
		// adding File name to response
		addReportFileNameToResponse(respnse, new ArrayList<ReportInput>(
				new ArrayList<ReportInput>()));
	}

	@Override
	public int getReportType() {
		return 111;
	}
}
