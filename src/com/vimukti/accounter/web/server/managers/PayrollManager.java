package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AttendanceOrProductionType;
import com.vimukti.accounter.core.AttendancePayHead;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.ComputionPayHead;
import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FlatRatePayHead;
import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.core.PayStructureItem;
import com.vimukti.accounter.core.PayrollUnit;
import com.vimukti.accounter.core.ProductionPayHead;
import com.vimukti.accounter.core.UserDefinedPayHead;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientAttendanceManagementItem;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientAttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientComputionPayHead;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.ClientProductionPayHead;
import com.vimukti.accounter.web.client.core.ClientUserDefinedPayHead;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class PayrollManager extends Manager {

	public PaginationList<ClientEmployee> getEmployees(int start, int lenght,
			Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.All.Employees").setEntity(
				"company", company);
		List<Employee> employees = query.list();
		if (employees == null) {
			return null;
		}
		PaginationList<ClientEmployee> clientEmployees = new PaginationList<ClientEmployee>();
		for (Employee employee : employees) {
			ClientEmployee clientEmployee;
			clientEmployee = new ClientConvertUtil().toClientObject(employee,
					ClientEmployee.class);
			clientEmployees.add(clientEmployee);
		}
		return clientEmployees;
	}

	public PaginationList<ClientPayHead> getPayheadsList(int start, int length,
			Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.Payhead").setEntity(
				"company", company);
		List<PayHead> employees = query.list();
		PaginationList<ClientPayHead> clientPayHeads = new PaginationList<ClientPayHead>();
		if (employees != null) {
			for (PayHead payHead : employees) {
				ClientPayHead clientPayHead = null;
				if (payHead instanceof AttendancePayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientAttendancePayHead.class);
				} else if (payHead instanceof ComputionPayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientComputionPayHead.class);
				} else if (payHead instanceof FlatRatePayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientFlatRatePayHead.class);
				} else if (payHead instanceof ProductionPayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientProductionPayHead.class);
				} else if (payHead instanceof UserDefinedPayHead) {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientUserDefinedPayHead.class);
				}
				if (clientPayHead != null) {
					clientPayHeads.add(clientPayHead);
				}
			}
		}
		return clientPayHeads;
	}

	public PaginationList<ClientPayrollUnit> getPayrollUnitsList(int start,
			int length, Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.PayrollUnit").setEntity(
				"company", company);
		List<PayrollUnit> units = query.list();
		if (units == null) {
			return null;
		}
		PaginationList<ClientPayrollUnit> clientPayrollUnits = new PaginationList<ClientPayrollUnit>();
		for (PayrollUnit unit : units) {
			ClientPayrollUnit clientPayrollUnit;
			clientPayrollUnit = new ClientConvertUtil().toClientObject(unit,
					ClientPayrollUnit.class);
			clientPayrollUnits.add(clientPayrollUnit);
		}
		return clientPayrollUnits;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ClientEmployeePayHeadComponent> getEmployeeGroupPayHeadComponents(
			FinanceDate startDate, FinanceDate endDate,
			ClientPayStructureDestination selectItem, Long companyId,
			List<ClientAttendanceManagementItem> attendanceItems)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		List<Employee> employees = session.getNamedQuery("getEmployeesByGroup")
				.setParameter("groupId", selectItem.getID()).list();
		ArrayList<ClientEmployeePayHeadComponent> groupComponents = new ArrayList<ClientEmployeePayHeadComponent>();
		for (Employee employee : employees) {
			ArrayList<ClientEmployeePayHeadComponent> employeePayHeadComponents = getEmployeePayHeadComponents(
					startDate, endDate, employee, companyId,
					getEmployeeAttendanceItems(employee, attendanceItems));
			groupComponents.addAll(employeePayHeadComponents);
		}
		return new ArrayList<ClientEmployeePayHeadComponent>(groupComponents);
	}

	private List<ClientAttendanceManagementItem> getEmployeeAttendanceItems(
			Employee employee,
			List<ClientAttendanceManagementItem> attendanceItems) {
		List<ClientAttendanceManagementItem> empItems = new ArrayList<ClientAttendanceManagementItem>();
		for (ClientAttendanceManagementItem item : attendanceItems) {
			if (item.getEmployee().getID() == employee.getID()) {
				empItems.add(item);
			}
		}
		return empItems;
	}

	public ArrayList<ClientEmployeePayHeadComponent> getEmployeePayHeadComponents(
			FinanceDate startDate, FinanceDate endDate, Employee selectItem,
			Long companyId, List<ClientAttendanceManagementItem> attendanceItems)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPayStructureItem.by.employee")
				.setParameter("employee", selectItem)
				.setParameter("group", selectItem.getGroup())
				.setParameter("company", getCompany(companyId));
		List<PayStructureItem> list = query.list();

		if (list == null) {
			return null;
		}

		double earnings = 0.0;
		double deductions = 0.0;

		long[] attendance = { 0, 0, 0 };

		if (attendanceItems != null && attendanceItems.size() > 0) {
			for (ClientAttendanceManagementItem attendanceManagementItem : attendanceItems) {
				if (attendanceManagementItem.getAttendanceType().getType() == AttendanceOrProductionType.TYPE_LEAVE_WITH_PAY) {
					attendance[0] += attendanceManagementItem.getNumber();
				} else if (attendanceManagementItem.getAttendanceType()
						.getType() == AttendanceOrProductionType.TYPE_LEAVE_WITHOUT_PAY) {
					attendance[1] += attendanceManagementItem.getNumber();
				} else if (attendanceManagementItem.getAttendanceType()
						.getType() == AttendanceOrProductionType.TYPE_USER_DEFINED_CALENDAR) {
					attendance[2] += attendanceManagementItem.getNumber();
				}
			}
		}

		ArrayList<ClientEmployeePayHeadComponent> clientEmployeePayHeadComponents = new ArrayList<ClientEmployeePayHeadComponent>();

		for (PayStructureItem payStructureItem : list) {
			int calcType = payStructureItem.getPayHead().getCalculationType();
			PayHead payHead = payStructureItem.getPayHead();

			ClientPayHead clientPayHead = null;
			if (calcType == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientAttendancePayHead.class);
			} else if (calcType == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientComputionPayHead.class);
			} else if (calcType == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientFlatRatePayHead.class);
			} else if (calcType == ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientProductionPayHead.class);
			} else if (calcType == ClientPayHead.CALCULATION_TYPE_AS_USER_DEFINED) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientUserDefinedPayHead.class);
			}

			ClientEmployeePayHeadComponent component = new ClientEmployeePayHeadComponent();
			component.setPayHead(clientPayHead);
			payStructureItem.setStartDate(startDate);
			payStructureItem.setEndDate(endDate);
			payStructureItem.setAttendance(attendance);
			double calculatedAmount = payHead.calculatePayment(
					payStructureItem, deductions, earnings);
			if (payHead.isEarning()) {
				earnings += calculatedAmount;
			} else {
				deductions += calculatedAmount;
			}

			component.setRate(calculatedAmount);
			component.setEmployee(selectItem.getName());
			clientEmployeePayHeadComponents.add(component);
		}
		return clientEmployeePayHeadComponents;
	}

	public PaginationList<ClientPayStructure> getPayrollStructuresList(
			int start, int length, Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.PayStructure").setEntity(
				"company", company);
		List<PayStructure> paystructures = query.list();
		if (paystructures == null) {
			return null;
		}
		PaginationList<ClientPayStructure> clientPayStructures = new PaginationList<ClientPayStructure>();
		for (PayStructure payStructure : paystructures) {
			ClientPayStructure clientPayStructure;
			clientPayStructure = new ClientConvertUtil().toClientObject(
					payStructure, ClientPayStructure.class);
			clientPayStructures.add(clientPayStructure);
		}
		return clientPayStructures;
	}

	public ArrayList<ClientAttendanceOrProductionType> getAttendanceProductionTypes(
			Long companyId) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.AttendanceProductionType")
				.setEntity("company", company);
		List<AttendanceOrProductionType> types = query.list();
		if (types == null) {
			return null;
		}
		ArrayList<ClientAttendanceOrProductionType> clientTypes = new ArrayList<ClientAttendanceOrProductionType>();
		for (AttendanceOrProductionType type : types) {
			ClientAttendanceOrProductionType clientType;
			clientType = new ClientConvertUtil().toClientObject(type,
					ClientAttendanceOrProductionType.class);
			clientTypes.add(clientType);
		}
		return clientTypes;
	}

	public ArrayList<ClientEmployeeGroup> getEmployeeGroups(Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.EmployeeGroups").setEntity(
				"company", company);
		List<EmployeeGroup> groups = query.list();
		if (groups == null) {
			return null;
		}
		ArrayList<ClientEmployeeGroup> clientGroups = new ArrayList<ClientEmployeeGroup>();
		for (EmployeeGroup group : groups) {
			List<Employee> employees = session
					.getNamedQuery("getEmployeesByGroup")
					.setParameter("groupId", group.getID()).list();
			group.setEmployees(employees);
			ClientEmployeeGroup clientGroup = new ClientConvertUtil()
					.toClientObject(group, ClientEmployeeGroup.class);
			clientGroups.add(clientGroup);
		}
		return clientGroups;
	}

	public List<ClientPayStructureDestination> getEmployeesAndGroups(
			Long companyId) throws AccounterException {
		ArrayList<ClientPayStructureDestination> arrayList = new ArrayList<ClientPayStructureDestination>();
		PaginationList<ClientEmployee> employees = getEmployees(0, 0, companyId);
		arrayList.addAll(employees);
		ArrayList<ClientEmployeeGroup> employeeGroups = getEmployeeGroups(companyId);
		arrayList.addAll(employeeGroups);

		return arrayList;
	}

	public long getEmployeeAttendanceCurrentBal(long employee,
			long attendanceType, long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("EmployeeAttendanceCurrentBal")
				.setParameter("company", companyId)
				.setParameter("employee", employee)
				.setParameter("attendanceType", attendanceType);
		Object result = query.uniqueResult();
		if (result == null) {
			return 0;
		}
		return (Long) result;
	}

	public ArrayList<ClientEmployee> getEmployeesByGroup(
			ClientPayStructureDestination group, Long companyId)
			throws AccounterException {
		ArrayList<ClientEmployee> list = new ArrayList<ClientEmployee>();
		Session session = HibernateUtil.getCurrentSession();
		List<Employee> employees = session.getNamedQuery("getEmployeesByGroup")
				.setParameter("groupId", group.getID()).list();
		for (Employee employee : employees) {
			ClientEmployee clientEmployee = new ClientConvertUtil()
					.toClientObject(employee, ClientEmployee.class);
			list.add(clientEmployee);
		}
		return list;
	}
}
