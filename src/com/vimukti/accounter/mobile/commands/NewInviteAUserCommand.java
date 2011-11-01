package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

public class NewInviteAUserCommand extends NewAbstractCommand {

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

		list.add(new NameRequirement(FIRST_NAME, getMessages().pleaseEnter(
				getConstants().firstName()), getConstants().firstName(), false,
				true));

		list.add(new NameRequirement(LAST_NAME, getMessages().pleaseEnter(
				getConstants().lastName()), getConstants().lastName(), false,
				true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getConstants().email()), getConstants().email(), false, true));

		list.add(new StringListRequirement(LEVEL_ACCESS, getMessages()
				.pleaseEnter(getConstants().permissions()), getConstants()
				.permissions(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().permissions());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPermissions();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().permissions());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {

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
		return null;

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

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().user());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().user());
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().user());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {

		get(LEVEL_ACCESS).setDefaultValue(RolePermissions.BASIC_EMPLOYEE);

	}

}
