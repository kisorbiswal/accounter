package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.IAccounterPayrollService;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeCategory;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
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
	public ArrayList<ClientEmployeePayHeadComponent> getEmployeePayHeadComponents(
			ClientPayStructureDestination selectItem) throws AccounterException {
		return getFinanceTool().getPayrollManager()
				.getEmployeePayHeadComponents(selectItem, getCompanyId());
	}

	@Override
	public PaginationList<ClientPayStructure> getPayStructures(int start,
			int length) throws AccounterException {
		return getFinanceTool().getPayrollManager().getPayrollStructuresList(
				start, length, getCompanyId());
	}

	@Override
	public ArrayList<ClientAttendanceOrProductionType> getAttendanceProductionTypes()
			throws AccounterException {
		return getFinanceTool().getPayrollManager()
				.getAttendanceProductionTypes(getCompanyId());
	}

	@Override
	public ArrayList<ClientEmployeeGroup> getEmployeeGroups()
			throws AccounterException {
		return getFinanceTool().getPayrollManager().getEmployeeGroups(
				getCompanyId());
	}

	@Override
	public List<ClientPayStructureDestination> getEmployeesAndGroups()
			throws AccounterException {
		return getFinanceTool().getPayrollManager().getEmployeesAndGroups(
				getCompanyId());
	}

	@Override
	public List<ClientEmployeeCategory> getEmployeeCategories()
			throws AccounterException {
		return getFinanceTool().getPayrollManager().getEmployeeCategories(
				getCompanyId());
	}

	@Override
	public long getEmployeeAttendanceCurrentBal(long employee,
			long attendanceType) throws AccounterException {
		return getFinanceTool().getPayrollManager()
				.getEmployeeAttendanceCurrentBal(employee, attendanceType,
						getCompanyId());
	}
}
