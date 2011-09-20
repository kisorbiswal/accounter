package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class InviteAUserCommand extends AbstractTransactionCommand {

	private static final String FIRST_NAME = "FirstName";
	private static final String LAST_NAME = "LastName";
	private static final String EMAIL = "E-mail";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(FIRST_NAME, false, true));
		list.add(new Requirement(LAST_NAME, false, true));
		list.add(new Requirement(EMAIL, false, true));

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

		return null;
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

	public List<RolePermissions> getDefaultRolesAndPermissions() {
		List<RolePermissions> list = new ArrayList<RolePermissions>();

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
		list.add(readOnly);

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
		list.add(invoiceOnly);

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
		list.add(basicEmployee);

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
		list.add(financialAdviser);

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
		list.add(financeAdmin);

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
		list.add(admin);

		return list;
	}

}
