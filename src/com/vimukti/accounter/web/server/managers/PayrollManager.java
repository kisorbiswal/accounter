package com.vimukti.accounter.web.server.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.AttendanceOrProductionType;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.EmployeeCategory;
import com.vimukti.accounter.core.EmployeeGroup;
import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.core.PayStructure;
import com.vimukti.accounter.core.PayrollUnit;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEmployeeCategory;
import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;
import com.vimukti.accounter.web.client.core.ClientPayStructureItem;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class PayrollManager extends Manager {

	public PaginationList<ClientEmployee> getEmployees(int start, int lenght,
			Long companyId) {
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
			try {
				clientEmployee = new ClientConvertUtil().toClientObject(
						employee, ClientEmployee.class);
				clientEmployees.add(clientEmployee);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientEmployees;
	}

	public PaginationList<ClientPayHead> getPayheadsList(int start, int length,
			Long companyId) {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany(companyId);
		Query query = session.getNamedQuery("list.Payhead").setEntity(
				"company", company);
		List<PayHead> employees = query.list();
		PaginationList<ClientPayHead> clientPayHeads = new PaginationList<ClientPayHead>();
		if (employees != null) {
			for (PayHead payHead : employees) {
				ClientPayHead clientPayHead;
				try {
					clientPayHead = new ClientConvertUtil().toClientObject(
							payHead, ClientPayHead.class);
					clientPayHeads.add(clientPayHead);
				} catch (AccounterException e) {
					e.printStackTrace();
				}
			}
		}
		return clientPayHeads;
	}

	public PaginationList<ClientPayrollUnit> getPayrollUnitsList(int start,
			int length, Long companyId) {
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
			try {
				clientPayrollUnit = new ClientConvertUtil().toClientObject(
						unit, ClientPayrollUnit.class);
				clientPayrollUnits.add(clientPayrollUnit);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientPayrollUnits;
	}

	public ArrayList<ClientPayStructureItem> getPayStructureItems(
			ClientPayStructureDestination selectItem, Long companyId) {
		return null;
	}

	public PaginationList<ClientPayStructure> getPayrollStructuresList(
			int start, int length, Long companyId) {
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
			try {
				clientPayStructure = new ClientConvertUtil().toClientObject(
						payStructure, ClientPayStructure.class);
				clientPayStructures.add(clientPayStructure);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientPayStructures;
	}

	public ArrayList<ClientAttendanceOrProductionType> getAttendanceProductionTypes(
			Long companyId) {
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
			try {
				clientType = new ClientConvertUtil().toClientObject(type,
						ClientAttendanceOrProductionType.class);
				clientTypes.add(clientType);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientTypes;
	}

	public ArrayList<ClientEmployeeGroup> getEmployeeGroups(Long companyId) {
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
			try {
				clientGroup = new ClientConvertUtil().toClientObject(group,
						ClientEmployeeGroup.class);
				clientGroups.add(clientGroup);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientGroups;
	}

	public List<ClientPayStructureDestination> getEmployeesAndGroups(
			Long companyId) {
		ArrayList<ClientPayStructureDestination> arrayList = new ArrayList<ClientPayStructureDestination>();
		PaginationList<ClientEmployee> employees = getEmployees(0, 0, companyId);
		arrayList.addAll(employees);
		ArrayList<ClientEmployeeGroup> employeeGroups = getEmployeeGroups(companyId);
		arrayList.addAll(employeeGroups);

		return arrayList;
	}

	public List<ClientEmployeeCategory> getEmployeeCategories(Long companyId) {
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
			try {
				clientCategory = new ClientConvertUtil().toClientObject(
						category, ClientEmployeeCategory.class);
				clientCategories.add(clientCategory);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientCategories;
	}
}
