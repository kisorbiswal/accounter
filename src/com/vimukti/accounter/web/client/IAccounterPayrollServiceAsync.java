package com.vimukti.accounter.web.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.PaginationList;

public interface IAccounterPayrollServiceAsync {

	public void getEmployees(int start, int length,
			AsyncCallback<PaginationList<ClientEmployee>> callBack);

	public void getPayheads(int start, int length,
			AsyncCallback<PaginationList<ClientPayHead>> callBack);

	public void getPayrollUnitsList(int start, int length,
			AsyncCallback<PaginationList<ClientPayrollUnit>> callBack);

	public void getPayStructureItems(ClientPayStructureDestination selectItem,
			AsyncCallback<ArrayList<ClientPayStructureItem>> callback);

}
