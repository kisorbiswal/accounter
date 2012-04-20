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
import com.vimukti.accounter.core.EmployeeCategory;
import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.FlatRatePayHead;
import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.core.PayStructureItem;
import com.vimukti.accounter.core.PayrollUnit;
import com.vimukti.accounter.core.ProductionPayHead;
import com.vimukti.accounter.core.UserDefinedPayHead;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientAttendancePayHead;
import com.vimukti.accounter.web.client.core.ClientComputionPayHead;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeCategory;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientEmployeePayHeadComponent;
import com.vimukti.accounter.web.client.core.ClientFlatRatePayHead;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
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

	public ArrayList<ClientEmployeePayHeadComponent> getEmployeePayHeadComponents(
			ClientPayStructureDestination selectItem, Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPayStructureItem.by.employee")
				.setParameter("id", selectItem.getID())
				.setParameter("company", getCompany(companyId));
		List<PayStructureItem> list = query.list();

		if (list == null) {
			return null;
		}
		ArrayList<ClientEmployeePayHeadComponent> clientEmployeePayHeadComponents = new ArrayList<ClientEmployeePayHeadComponent>();
		for (PayStructureItem payStructureItem : list) {
			int type = payStructureItem.getPayHead().getCalculationType();
			PayHead payHead = payStructureItem.getPayHead();
			ClientPayHead clientPayHead = null;
			if (type == ClientPayHead.CALCULATION_TYPE_ON_ATTENDANCE) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientAttendancePayHead.class);
			} else if (type == ClientPayHead.CALCULATION_TYPE_AS_COMPUTED_VALUE) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientComputionPayHead.class);
			} else if (type == ClientPayHead.CALCULATION_TYPE_FLAT_RATE) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientFlatRatePayHead.class);
			} else if (type == ClientPayHead.CALCULATION_TYPE_ON_PRODUCTION) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientProductionPayHead.class);
			} else if (type == ClientPayHead.CALCULATION_TYPE_AS_USER_DEFINED) {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientUserDefinedPayHead.class);
			}
			ClientEmployeePayHeadComponent component = new ClientEmployeePayHeadComponent();
			component.setPayHead(clientPayHead);
			component.setRate(payStructureItem.getRate());
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
			ClientEmployeeGroup clientGroup;
			clientGroup = new ClientConvertUtil().toClientObject(group,
					ClientEmployeeGroup.class);
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

	public List<ClientEmployeeCategory> getEmployeeCategories(Long companyId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.EmployeeCategories")
				.setEntity("company", company);
		List<EmployeeCategory> categories = query.list();
		if (categories == null) {
			return null;
		}
		ArrayList<ClientEmployeeCategory> clientCategories = new ArrayList<ClientEmployeeCategory>();
		for (EmployeeCategory category : categories) {
			ClientEmployeeCategory clientCategory;
			clientCategory = new ClientConvertUtil().toClientObject(category,
					ClientEmployeeCategory.class);
			clientCategories.add(clientCategory);
		}
		return clientCategories;
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
}
