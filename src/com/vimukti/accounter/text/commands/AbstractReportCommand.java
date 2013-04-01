package com.vimukti.accounter.text.commands;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class AbstractReportCommand implements ITextCommand {
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
		// TODO
		return null;
	}
}
