package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagement;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
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
import com.vimukti.accounter.web.client.exception.AccounterException;

public interface IAccounterPayrollService extends RemoteService {

	public PaginationList<ClientEmployee> getEmployees(int start, int lenght)
			throws AccounterException;

	public PaginationList<ClientPayHead> getPayheads(int start, int length)
			throws AccounterException;

	public PaginationList<ClientPayrollUnit> getPayrollUnitsList(int start,
			int length) throws AccounterException;

	ArrayList<ClientEmployeePayHeadComponent> getEmployeePayHeadComponents(
			List<ClientAttendanceManagementItem> list,
			ClientPayStructureDestination selectItem,
			ClientFinanceDate startDate, ClientFinanceDate endDate)
			throws AccounterException;

	PaginationList<ClientPayStructure> getPayStructures(int start, int length)
			throws AccounterException;

	ArrayList<ClientAttendanceOrProductionType> getAttendanceProductionTypes()
			throws AccounterException;

	ArrayList<ClientEmployeeGroup> getEmployeeGroups()
			throws AccounterException;

	List<ClientPayStructureDestination> getEmployeesAndGroups()
			throws AccounterException;

	List<ClientEmployeeCategory> getEmployeeCategories()
			throws AccounterException;

	long getEmployeeAttendanceCurrentBal(long employee, long attendanceType)
			throws AccounterException;

}
