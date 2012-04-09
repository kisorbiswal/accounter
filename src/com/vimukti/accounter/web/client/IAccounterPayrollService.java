package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;

public interface IAccounterPayrollService extends RemoteService {

	public PaginationList<ClientEmployee> getEmployees(int start, int lenght)
			throws AccounterException;

	public PaginationList<ClientPayHead> getPayheads(int start, int length)
			throws AccounterException;

	public PaginationList<ClientPayrollUnit> getPayrollUnitsList(int start,
			int length) throws AccounterException;
}
