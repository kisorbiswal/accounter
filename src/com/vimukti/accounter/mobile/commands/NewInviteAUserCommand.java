package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

public class NewInviteAUserCommand extends AbstractTransactionCommand {

	private static final String FIRST_NAME = "First Name";
	private static final String LAST_NAME = "Last Name";
	private static final String EMAIL = "E-mail";
	private static final String LEVEL_ACCESS = "accessLevel";
	private static final String PERMISSIONS = "permissions";

	@Override
	public String getId() {

		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(FIRST_NAME, false, true));
		list.add(new Requirement(LAST_NAME, false, true));
		list.add(new Requirement(EMAIL, false, true));

		list.add(new Requirement(LEVEL_ACCESS, true, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		setDefaultValues();

		Result makeResult = context.makeResult();
		makeResult.add(getMessages()
				.readyToCreate(getConstants().cashExpense()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		Result result = nameRequirement(context, list, FIRST_NAME,
				getConstants().firstName(),
				getMessages().pleaseEnter(getConstants().firstName()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, LAST_NAME, getConstants()
				.lastName(),
				getMessages().pleaseEnter(getConstants().lastName()));
		if (result != null) {
			return result;
		}

		result = nameRequirement(context, list, EMAIL, getConstants().email(),
				getMessages().pleaseEnter(getConstants().email()));
		if (result != null) {
			return result;
		}

		result = getOptionalRequirement(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createUser(context);

	}

	private void setDefaultValues() {
		get(LEVEL_ACCESS).setDefaultValue(RolePermissions.BASIC_EMPLOYEE);
	}

	private Result createUser(Context context) {

		String firstName = get(FIRST_NAME).getValue();
		String lastName = get(LAST_NAME).getValue();
		String email = get(EMAIL).getValue();

		String access = get(LEVEL_ACCESS).getValue();

		ClientUser user = new ClientUser();

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		RolePermissions rolePermissions = null;

		if (access.equals(RolePermissions.READ_ONLY)) {
			rolePermissions = getReadOnlyPermission();

		} else if (access.equals(RolePermissions.INVOICE_ONLY)) {
			// if ReadOnly is selected, get corresponding RolePermissions
			rolePermissions = getInvoiceOnlyPermission();

		} else if (access.equals(RolePermissions.BASIC_EMPLOYEE)) {
			// if BasicEmployee is selected, get corresponding RolePermissions
			rolePermissions = getBasicEmployeePermission();

		} else if (access.equals(RolePermissions.FINANCIAL_ADVISER)) {
			// if FinancialAdvisor is selected, get corresponding
			// RolePermissions
			rolePermissions = getFinancialAdviserPermission();

		} else if (access.equals(RolePermissions.FINANCE_ADMIN)) {
			// if FinanceAdmin is selected, get corresponding RolePermissions
			rolePermissions = getFinanceAdminPermission();

		} else if (access.equals(RolePermissions.ADMIN)) {

			// if Admin is selected, get corresponding RolePermissions
			rolePermissions = getAdminPermission();
		}
		user.setPermissions(getUserPermission(rolePermissions));

		user.setUserRole(rolePermissions.getRoleName());

		inviteUser(user, context);
		markDone();

		Result result = new Result();
		result.add(getConstants().userInvited());

		return result;
	}

	private void inviteUser(ClientUser user, Context context) {
		try {
			String clientClassSimpleName = user.getObjectType()
					.getClientClassSimpleName();

			OperationContext opContext = new OperationContext(context
					.getCompany().getID(), user, context.getIOSession()
					.getUserEmail());
			opContext.setArg2(clientClassSimpleName);

			new FinanceTool().getUserManager().inviteUser(opContext);
		} catch (AccounterException e) {
			e.printStackTrace();
		}

	}

	private ClientUserPermissions getUserPermission(
			RolePermissions rolePermission) {
		ClientUserPermissions userPermissions = new ClientUserPermissions();

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

	private Result getOptionalRequirement(Context context, ResultList list,
			ResultList actions, Result makeResult) {
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

		Result result = accessLevelOptionalRequirement(context, selection,
				list, LEVEL_ACCESS, getConstants().userPermissions(),
				getMessages().pleaseEnter(getConstants().userPermissions()));
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("",
				getMessages().finishToCreate(getConstants().cashExpense()));
		actions.add(finish);

		return makeResult;
	}

	private Result accessLevelOptionalRequirement(Context context,
			Object selection, ResultList list, String reqName, String name,
			String displayString) {
		Object permissionObj = context.getSelection(PERMISSIONS);
		if (permissionObj instanceof ActionNames) {
			permissionObj = null;
			selection = reqName;
		}

		Requirement permissionReq = get(reqName);
		String permission = (String) permissionReq.getValue();

		if (permissionObj != null) {
			permission = (String) permissionObj;
			permissionReq.setValue(permission);
		}

		if (selection != null)
			if (selection.equals(reqName)) {
				context.setAttribute(INPUT_ATTR, reqName);
				return permisions(context, displayString);

			}

		Record vendorRecord = new Record(reqName);
		vendorRecord.add("", name);
		vendorRecord.add("", permission);
		list.add(vendorRecord);

		return null;
	}

	private Result permisions(Context context, String displayString) {
		Result result = context.makeResult();

		ResultList supplierList = new ResultList(PERMISSIONS);

		Object last = context.getString();
		List<String> skipPermissions = new ArrayList<String>();
		if (last != null) {
			supplierList.add(createPermissionRecord((String) last));
			skipPermissions.add((String) last);
		}
		List<String> permissions = getPermissions();

		ResultList actions = new ResultList("actions");
		ActionNames selection = context.getSelection("actions");

		List<String> pagination = pagination(context, selection, actions,
				permissions, skipPermissions, VALUES_TO_SHOW);

		for (String permission : pagination) {
			supplierList.add(createPermissionRecord(permission));
		}

		int size = supplierList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append(displayString);
		}

		result.add(message.toString());
		result.add(supplierList);
		result.add(actions);
		return result;
	}

	private List<String> getPermissions() {
		List<String> permissions = new ArrayList<String>();
		permissions.add(RolePermissions.READ_ONLY);
		permissions.add(RolePermissions.INVOICE_ONLY);
		permissions.add(RolePermissions.BASIC_EMPLOYEE);
		permissions.add(RolePermissions.FINANCIAL_ADVISER);
		permissions.add(RolePermissions.FINANCE_ADMIN);
		permissions.add(RolePermissions.ADMIN);
		return permissions;
	}

	private Record createPermissionRecord(String last) {
		Record record = new Record(last);
		record.add("", last);
		return record;
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
