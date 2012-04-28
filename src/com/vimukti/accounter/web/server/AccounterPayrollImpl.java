package com.vimukti.accounter.web.server;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.IAccounterPayrollService;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class AccounterPayrollImpl extends AccounterRPCBaseServiceImpl implements
		IAccounterPayrollService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public PaginationList<ClientEmployee> getEmployees(int start, int lenght)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool.getPayrollManager().getEmployees(start, lenght,
				getCompanyId());
	}

	@Override
	public PaginationList<ClientPayHead> getPayheads(int start, int length)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool.getPayrollManager().getPayheadsList(start, length,
				getCompanyId());
	}

	@Override
	public PaginationList<ClientPayrollUnit> getPayrollUnitsList(int start,
			int length) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool.getPayrollManager().getPayrollUnitsList(start, length,
				getCompanyId());
	}

	@Override
	public ArrayList<ClientPayStructureItem> getPayStructureItems(
			ClientPayStructureDestination selectItem) throws AccounterException {
		return getFinanceTool().getPayrollManager().getPayStructureItems(
				selectItem, getCompanyId());
	}
}
