package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.UserPermissions;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class InviteAUserCommand extends AbstractTransactionCommand {

	private static final String FIRST_NAME = "First Name";
	private static final String LAST_NAME = "Last Name";
	private static final String EMAIL = "E-mail";
	private static final String READ_ONLY = "Read Only";
	private static final String INVOICE_ONLY = "Invoice Only";
	private static final String BASIC_EMPLOYEE = "Basic Employee";
	private static final String FINANCIAL_ADVISOR = "Financial Advisor";
	private static final String FINANCE_ADMIN = "Finance Admin";
	private static final String ADMIN = "Admin";

	@Override
	public String getId() {

		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(FIRST_NAME, false, true));
		list.add(new Requirement(LAST_NAME, false, true));
		list.add(new Requirement(EMAIL, false, true));

		list.add(new Requirement(READ_ONLY, true, true));
		list.add(new Requirement(INVOICE_ONLY, true, true));
		list.add(new Requirement(BASIC_EMPLOYEE, true, true));
		list.add(new Requirement(FINANCIAL_ADVISOR, true, true));
		list.add(new Requirement(FINANCE_ADMIN, true, true));
		list.add(new Requirement(ADMIN, true, true));

	}

	@Override
	public Result run(Context context) {

		Result result = null;
		result = getFirstNameRequirement(context);
		if (result != null) {
			return result;
		}

		result = getLastNameRequirement(context);
		if (result != null) {
			return result;
		}

		result = getEmailRequirement(context);
		if (result != null) {
			return result;
		}

		result = getOptionalRequirement(context);
		if (result != null) {
			return result;
		}

		return createUser(context);

	}

	private Result createUser(Context context) {

		String firstName = get(FIRST_NAME).getValue();
		String lastName = get(LAST_NAME).getValue();
		String email = get(EMAIL).getValue();

		boolean readOnly = get(READ_ONLY).getValue();
		boolean invoiceOnly = get(INVOICE_ONLY).getValue();
		boolean basicEmployee = get(BASIC_EMPLOYEE).getValue();
		boolean financialAdvisor = get(FINANCIAL_ADVISOR).getValue();
		boolean financeAdmin = get(FINANCE_ADMIN).getValue();
		boolean admin = get(ADMIN).getValue();

		User user = new User();

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);

		if (readOnly) {

			RolePermissions readOnlyPer = getReadOnlyPermission();
			user.setPermissions(getUserPermission(readOnlyPer));

		} else if (invoiceOnly) {

			// if ReadOnly is selected, get corresponding RolePermissions
			RolePermissions invoiceOnlyPer = getInvoiceOnlyPermission();
			user.setPermissions(getUserPermission(invoiceOnlyPer));

		} else if (basicEmployee) {

			// if BasicEmployee is selected, get corresponding RolePermissions
			RolePermissions basicEmployeePer = getBasicEmployeePermission();
			user.setPermissions(getUserPermission(basicEmployeePer));

		} else if (financialAdvisor) {

			// if FinancialAdvisor is selected, get corresponding
			// RolePermissions
			RolePermissions basicEmployeePerPer = getFinancialAdviserPermission();
			user.setPermissions(getUserPermission(basicEmployeePerPer));

		} else if (financeAdmin) {

			// if FinanceAdmin is selected, get corresponding RolePermissions
			RolePermissions financeAdminPer = getFinanceAdminPermission();
			user.setPermissions(getUserPermission(financeAdminPer));

		} else if (admin) {

			// if Admin is selected, get corresponding RolePermissions
			RolePermissions adminPer = getAdminPermission();
			user.setPermissions(getUserPermission(adminPer));

		}

		Session session = context.getHibernateSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(user);
		transaction.commit();

		markDone();

		Result result = new Result();
		result.add("User invited successfully");

		return result;
	}

	private UserPermissions getUserPermission(RolePermissions rolePermission) {
		UserPermissions userPermissions = new UserPermissions();

		userPermissions.setTypeOfBankReconcilation(rolePermission
				.getTypeOfBankReconcilation());
		userPermissions.setTypeOfInvoices(rolePermission.getTypeOfInvoices());
		userPermissions.setTypeOfExpences(rolePermission.getTypeOfExpences());
		userPermissions.setTypeOfSystemSettings(rolePermission
				.getTypeOfSystemSettings());
		userPermissions.setTypeOfViewReports(rolePermission
				.getTypeOfViewReports());
		userPermissions.setTypeOfLockDates(rolePermission.getTypeOfLockDates());
		// TODO
		// userPermissions.setTypeOfPublishReports(readOnlyPer.getTypeOfPublishReports());

		return userPermissions;
	}

	private Result getOptionalRequirement(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);

		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Result result = readOnlyRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = invoiceOnlyRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = basicEmployeeRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = financialAdvisorRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = financeAdminRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = adminRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		Requirement readOnlyReq = get(READ_ONLY);
		Boolean readOnly = readOnlyReq.getValue();

		Requirement invoiceOnlyReq = get(INVOICE_ONLY);
		Boolean invoiceOnly = invoiceOnlyReq.getValue();

		Requirement basicEmployeeReq = get(BASIC_EMPLOYEE);
		Boolean basicEmployee = basicEmployeeReq.getValue();

		Requirement financialAdvisorReq = get(FINANCIAL_ADVISOR);
		Boolean financialAdvisor = financialAdvisorReq.getValue();

		Requirement financeAdminReq = get(FINANCE_ADMIN);
		Boolean financeAdmin = financeAdminReq.getValue();

		Requirement adminReq = get(ADMIN);
		Boolean admin = adminReq.getValue();

		if (selection != readOnly || selection != invoiceOnly
				|| selection != basicEmployee || selection != financialAdvisor
				|| selection != financeAdmin || selection != admin) {
			return text(context, "Please select any user permission", null);
		} else {

			// TODO if any user permission is selected

		}

		return null;

	}

	protected Result readOnlyRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(READ_ONLY);
		Boolean readOnly = req.getValue();

		if (selection == readOnly) {
			context.setAttribute(INPUT_ATTR, READ_ONLY);
			readOnly = !readOnly;
			req.setValue(readOnly);
		}

		Record balanceRecord = new Record(readOnly);
		balanceRecord.add("Name", READ_ONLY);
		balanceRecord.add("Value", readOnly);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	protected Result invoiceOnlyRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(INVOICE_ONLY);
		Boolean invoiceOnly = req.getValue();

		if (selection == invoiceOnly) {
			context.setAttribute(INPUT_ATTR, INVOICE_ONLY);
			invoiceOnly = !invoiceOnly;
			req.setValue(invoiceOnly);
		}

		Record balanceRecord = new Record(invoiceOnly);
		balanceRecord.add("Name", INVOICE_ONLY);
		balanceRecord.add("Value", invoiceOnly);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	protected Result basicEmployeeRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(BASIC_EMPLOYEE);
		Boolean basicEmployee = req.getValue();

		if (selection == basicEmployee) {
			context.setAttribute(INPUT_ATTR, BASIC_EMPLOYEE);
			basicEmployee = !basicEmployee;
			req.setValue(basicEmployee);
		}

		Record balanceRecord = new Record(basicEmployee);
		balanceRecord.add("Name", BASIC_EMPLOYEE);
		balanceRecord.add("Value", basicEmployee);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	protected Result financialAdvisorRequirement(Context context,
			ResultList list, Object selection) {
		Requirement req = get(FINANCIAL_ADVISOR);
		Boolean financialAdvisor = req.getValue();

		if (selection == financialAdvisor) {
			context.setAttribute(INPUT_ATTR, FINANCIAL_ADVISOR);
			financialAdvisor = !financialAdvisor;
			req.setValue(financialAdvisor);
		}

		Record balanceRecord = new Record(financialAdvisor);
		balanceRecord.add("Name", FINANCIAL_ADVISOR);
		balanceRecord.add("Value", financialAdvisor);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	protected Result financeAdminRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(FINANCE_ADMIN);
		Boolean financialAdvisor = req.getValue();

		if (selection == financialAdvisor) {
			context.setAttribute(INPUT_ATTR, FINANCE_ADMIN);
			financialAdvisor = !financialAdvisor;
			req.setValue(financialAdvisor);
		}

		Record balanceRecord = new Record(financialAdvisor);
		balanceRecord.add("Name", FINANCE_ADMIN);
		balanceRecord.add("Value", financialAdvisor);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	protected Result adminRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get(ADMIN);
		Boolean financialAdvisor = req.getValue();

		if (selection == financialAdvisor) {
			context.setAttribute(INPUT_ATTR, ADMIN);
			financialAdvisor = !financialAdvisor;
			req.setValue(financialAdvisor);
		}

		Record balanceRecord = new Record(financialAdvisor);
		balanceRecord.add("Name", ADMIN);
		balanceRecord.add("Value", financialAdvisor);
		list.add(balanceRecord);
		Result result = new Result();
		result.add(list);
		return result;

	}

	private Result getFirstNameRequirement(Context context) {
		Requirement requirement = get(FIRST_NAME);
		if (!requirement.isDone()) {
			String firstName = context.getSelection(TEXT);
			if (firstName != null) {
				requirement.setValue(firstName);
			} else {
				return text(context, "Please enter first name", null);
			}
		}
		String name = (String) context.getAttribute(INPUT_ATTR);
		if (name.equals(FIRST_NAME)) {
			requirement.setValue(name);
		}
		return null;
	}

	private Result getLastNameRequirement(Context context) {
		Requirement requirement = get(LAST_NAME);
		if (!requirement.isDone()) {
			String lastName = context.getSelection(TEXT);
			if (lastName != null) {
				requirement.setValue(lastName);
			} else {
				return text(context, "Please enter last name", null);
			}
		}
		String name = (String) context.getAttribute(INPUT_ATTR);
		if (name.equals(LAST_NAME)) {
			requirement.setValue(name);
		}
		return null;
	}

	private Result getEmailRequirement(Context context) {
		Requirement requirement = get(EMAIL);
		if (!requirement.isDone()) {
			String email = context.getSelection(TEXT);
			if (email != null) {
				requirement.setValue(email);
			} else {
				return text(context, "Please enter email", null);
			}
		}
		String name = (String) context.getAttribute(INPUT_ATTR);
		if (name.equals(EMAIL)) {
			requirement.setValue(name);
		}
		return null;
	}

	public RolePermissions getReadOnlyPermission() {

		RolePermissions readOnly = new RolePermissions();
		readOnly.setRoleName(RolePermissions.READ_ONLY);
		readOnly.setTypeOfBankReconcilation(RolePermissions.TYPE_NO);
		readOnly.setTypeOfInvoices(RolePermissions.TYPE_READ_ONLY);
		readOnly.setTypeOfExpences(RolePermissions.TYPE_NO);
		readOnly.setTypeOfSystemSettings(RolePermissions.TYPE_NO);
		readOnly.setTypeOfViewReports(RolePermissions.TYPE_READ_ONLY);
		readOnly.setTypeOfPublishReports(RolePermissions.TYPE_NO);
		readOnly.setTypeOfLockDates(RolePermissions.TYPE_NO);
		readOnly.setCanDoUserManagement(false);

		return readOnly;
	}

	public RolePermissions getInvoiceOnlyPermission() {
		RolePermissions invoiceOnly = new RolePermissions();
		invoiceOnly.setRoleName(RolePermissions.INVOICE_ONLY);
		invoiceOnly.setTypeOfBankReconcilation(RolePermissions.TYPE_NO);
		invoiceOnly.setTypeOfInvoices(RolePermissions.TYPE_YES);
		invoiceOnly.setTypeOfExpences(RolePermissions.TYPE_NO);
		invoiceOnly.setTypeOfSystemSettings(RolePermissions.TYPE_NO);
		invoiceOnly.setTypeOfViewReports(RolePermissions.TYPE_NO);
		invoiceOnly.setTypeOfPublishReports(RolePermissions.TYPE_NO);
		invoiceOnly.setTypeOfLockDates(RolePermissions.TYPE_NO);
		invoiceOnly.setCanDoUserManagement(false);
		return invoiceOnly;
	}

	public RolePermissions getBasicEmployeePermission() {
		RolePermissions basicEmployee = new RolePermissions();
		basicEmployee.setRoleName(RolePermissions.BASIC_EMPLOYEE);
		basicEmployee.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		basicEmployee.setTypeOfInvoices(RolePermissions.TYPE_YES);
		basicEmployee.setTypeOfExpences(RolePermissions.TYPE_DRAFT_ONLY);
		basicEmployee.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		basicEmployee.setTypeOfViewReports(RolePermissions.TYPE_YES);
		basicEmployee.setTypeOfPublishReports(RolePermissions.TYPE_NO);
		basicEmployee.setTypeOfLockDates(RolePermissions.TYPE_NO);
		basicEmployee.setCanDoUserManagement(false);
		return basicEmployee;
	}

	public RolePermissions getFinancialAdviserPermission() {
		RolePermissions financialAdviser = new RolePermissions();
		financialAdviser.setRoleName(RolePermissions.FINANCIAL_ADVISER);
		financialAdviser.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfInvoices(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfExpences(RolePermissions.TYPE_APPROVE);
		financialAdviser.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfViewReports(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfPublishReports(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfLockDates(RolePermissions.TYPE_YES);
		financialAdviser.setCanDoUserManagement(false);
		return financialAdviser;
	}

	public RolePermissions getFinanceAdminPermission() {
		RolePermissions financeAdmin = new RolePermissions();
		financeAdmin.setRoleName(RolePermissions.FINANCE_ADMIN);
		financeAdmin.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		financeAdmin.setTypeOfInvoices(RolePermissions.TYPE_YES);
		financeAdmin.setTypeOfExpences(RolePermissions.TYPE_APPROVE);
		financeAdmin.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		financeAdmin.setTypeOfViewReports(RolePermissions.TYPE_YES);
		financeAdmin.setTypeOfPublishReports(RolePermissions.TYPE_YES);
		financeAdmin.setTypeOfLockDates(RolePermissions.TYPE_YES);
		financeAdmin.setCanDoUserManagement(false);

		return financeAdmin;
	}

	public RolePermissions getAdminPermission() {
		RolePermissions admin = new RolePermissions();
		admin.setRoleName(RolePermissions.ADMIN);
		admin.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		admin.setTypeOfInvoices(RolePermissions.TYPE_YES);
		admin.setTypeOfExpences(RolePermissions.TYPE_APPROVE);
		admin.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		admin.setTypeOfViewReports(RolePermissions.TYPE_YES);
		admin.setTypeOfPublishReports(RolePermissions.TYPE_YES);
		admin.setTypeOfLockDates(RolePermissions.TYPE_YES);
		admin.setCanDoUserManagement(true);

		return admin;
	}

}
