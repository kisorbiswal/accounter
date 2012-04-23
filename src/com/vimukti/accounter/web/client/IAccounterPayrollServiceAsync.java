package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeCategory;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.PaginationList;

public interface IAccounterPayrollServiceAsync {

	public void getEmployees(int start, int length,
			AsyncCallback<PaginationList<ClientEmployee>> callBack);

	public void getPayheads(int start, int length,
			AsyncCallback<PaginationList<ClientPayHead>> callBack);

	public void getPayrollUnitsList(int start, int length,
			AsyncCallback<PaginationList<ClientPayrollUnit>> callBack);

	public void getEmployeePayHeadComponents(
			ClientPayStructureDestination selectItem,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<ClientEmployeePayHeadComponent>> callback);

	public void getPayStructures(int start, int length,
			AsyncCallback<PaginationList<ClientPayStructure>> callback);

	public void getAttendanceProductionTypes(
			AsyncCallback<ArrayList<ClientAttendanceOrProductionType>> asyncCallback);

	public void getEmployeeGroups(
			AsyncCallback<ArrayList<ClientEmployeeGroup>> asyncCallback);

	public void getEmployeesAndGroups(
			AsyncCallback<List<ClientPayStructureDestination>> asyncCallback);

	public void getEmployeeCategories(
			AsyncCallback<List<ClientEmployeeCategory>> asyncCallback);

	public void getEmployeeAttendanceCurrentBal(long employee,
			long attendanceType, AsyncCallback<Long> asyncCallback);

}
