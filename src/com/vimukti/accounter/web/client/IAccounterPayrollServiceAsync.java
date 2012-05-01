package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.reports.PaySheet;
import com.vimukti.accounter.web.client.core.reports.PaySlipDetail;
import com.vimukti.accounter.web.client.core.reports.PaySlipSummary;

public interface IAccounterPayrollServiceAsync {

	public void getEmployees(boolean isActive, int start, int length,
			AsyncCallback<PaginationList<ClientEmployee>> callBack);

	public void getPayheads(int start, int length,
			AsyncCallback<PaginationList<ClientPayHead>> callBack);

	public void getPayrollUnitsList(int start, int length,
			AsyncCallback<PaginationList<ClientPayrollUnit>> callBack);

	public void getEmployeePayHeadComponents(
			List<ClientAttendanceManagementItem> list,
			ClientPayStructureDestination selectItem,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			Long noOfWorkingDays,
			AsyncCallback<ArrayList<ClientEmployeePayHeadComponent>> callback);

	public void getPayStructures(int start, int length,
			AsyncCallback<PaginationList<ClientPayStructure>> callback);

	public void getAttendanceProductionTypes(
			int start,
			int length,
			AsyncCallback<PaginationList<ClientAttendanceOrProductionType>> asyncCallback);

	public void getEmployeeGroups(
			AsyncCallback<ArrayList<ClientEmployeeGroup>> asyncCallback);

	public void getEmployeesAndGroups(
			AsyncCallback<List<ClientPayStructureDestination>> asyncCallback);

	public void getEmployeeAttendanceCurrentBal(long employee,
			long attendanceType, AsyncCallback<Long> asyncCallback);

	void getEmployeesByGroup(ClientPayStructureDestination employeeGroup,
			AsyncCallback<ArrayList<ClientEmployee>> asyncCallback);

	public void getPaySlipSummary(ClientFinanceDate start,
			ClientFinanceDate clientFinanceDate,
			AsyncCallback<ArrayList<PaySlipSummary>> asyncCallback);

	public void getPaySheet(ClientFinanceDate start, ClientFinanceDate end,
			AsyncCallback<ArrayList<PaySheet>> asyncCallback);

	void getPaySlipDetail(long employeeId, ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<PaySlipDetail>> asyncCallback);

}
