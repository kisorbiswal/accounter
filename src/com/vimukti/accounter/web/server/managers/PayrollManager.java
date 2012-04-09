package com.vimukti.accounter.web.server.managers;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Employee;
import com.vimukti.accounter.core.PayHead;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientPayHead;
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
		if (employees == null) {
			return null;
		}
		PaginationList<ClientPayHead> clientPayHeads = new PaginationList<ClientPayHead>();
		for (PayHead payHead : employees) {
			ClientPayHead clientPayHead;
			try {
				clientPayHead = new ClientConvertUtil().toClientObject(payHead,
						ClientPayHead.class);
				clientPayHeads.add(clientPayHead);
			} catch (AccounterException e) {
				e.printStackTrace();
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
		List<PayHead> employees = query.list();
		if (employees == null) {
			return null;
		}
		PaginationList<ClientPayrollUnit> clientPayrollUnits = new PaginationList<ClientPayrollUnit>();
		for (PayHead payHead : employees) {
			ClientPayrollUnit clientPayrollUnit;
			try {
				clientPayrollUnit = new ClientConvertUtil().toClientObject(payHead,
						ClientPayrollUnit.class);
				clientPayrollUnits.add(clientPayrollUnit);
			} catch (AccounterException e) {
				e.printStackTrace();
			}
		}
		return clientPayrollUnits;
	}

}
