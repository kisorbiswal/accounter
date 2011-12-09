package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
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
	ClientUser user;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(FIRST_NAME, getMessages().pleaseEnter(
				getMessages().firstName()), getMessages().firstName(), false,
				true));

		list.add(new NameRequirement(LAST_NAME, getMessages().pleaseEnter(
				getMessages().lastName()), getMessages().lastName(), false,
				true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), false, true) {
			@Override
			public void setValue(Object value) {
				String emailId = (String) value;
				if (emailId == null) {
					return;
				} else if (NewInviteAUserCommand.this.isUserExists(emailId)) {
					addFirstMessage(getMessages().userExistsWithThisMailId());
					return;
				}
				addFirstMessage(getMessages()
						.pleaseEnter(getMessages().email()));
				super.setValue(value);
			}
		});

		list.add(new StringListRequirement(LEVEL_ACCESS, getMessages()
				.pleaseEnter(getMessages().permissions()), getMessages()
				.permissions(), true, true, null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().permissions());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPermissions();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().permissions());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
	}

	protected boolean isUserExists(String emailId) {
		if (user.getID() != 0 && emailId.equals(user.getEmail())) {
			return false;
		}
		User userByUserEmail = getCompany().getUserByUserEmail(emailId);
		if (userByUserEmail != null) {
			return true;
		}
		return false;
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		String firstName = get(FIRST_NAME).getValue();
		String lastName = get(LAST_NAME).getValue();
		String email = get(EMAIL).getValue();
		String access = get(LEVEL_ACCESS).getValue();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		RolePermissions rolePermissions = getUserPermissions(access);
		user.setPermissions(getUserPermission(rolePermissions));

		user.setUserRole(rolePermissions.getRoleName());
		user.setCanDoUserManagement(rolePermissions.isCanDoUserManagement());
		if (user.getID() == 0) {
			inviteUser(user, context);
		} else {
			updateUser(user, context);
		}
		return null;

	}

	private RolePermissions getUserPermissions(String access) {
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
			rolePermissions = getAdminPermission();
		}
		return rolePermissions;
	}

	private void updateUser(ClientUser user2, Context context) {
		try {
			String clientClassSimpleName = user.getObjectType()
					.getClientClassSimpleName();

			OperationContext opContext = new OperationContext(context
					.getCompany().getID(), user, context.getIOSession()
					.getUserEmail());
			opContext.setArg2(clientClassSimpleName);

			new FinanceTool().getUserManager().updateUser(opContext);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
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

	private RolePermissions getReadOnlyPermission() {
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

	private RolePermissions getInvoiceOnlyPermission() {
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

	private RolePermissions getBasicEmployeePermission() {
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

	private RolePermissions getFinancialAdviserPermission() {
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

	private RolePermissions getFinanceAdminPermission() {
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

	private RolePermissions getAdminPermission() {
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
		return user.getID() == 0 ? getMessages().readyToCreate(
				getMessages().user()) : getMessages().readyToUpdate(
				getMessages().user());
	}

	@Override
	public String getSuccessMessage() {
		return user.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().user()) : getMessages().updateSuccessfully(
				getMessages().user());
	}

	@Override
	protected String getWelcomeMessage() {
		return user.getID() == 0 ? getMessages().creating(getMessages().user())
				: "Updating user";
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		User currentUser = context.getUser();
		if (!currentUser.isCanDoUserManagement()) {
			addFirstMessage(context, getMessages().youdontHavepermissiosToinviteUser());
			return "Users List";
		}
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().user()));
				return "Users List";
			}
			User userByUserEmail = context.getCompany().getUserByUserEmail(
					string);
			if (userByUserEmail == null) {
				addFirstMessage(context, getMessages()
						.selectATransactionToUpdate(getMessages().user()));
				return "Users List " + string;
			}
			user = userByUserEmail.getClientUser();
			setValues(context);
		} else {
			user = new ClientUser();
		}
		return null;
	}

	private void setValues(Context context) {
		get(EMAIL).setValue(user.getEmail());
		get(EMAIL).setEditable(false);
		get(FIRST_NAME).setValue(user.getFirstName());
		get(LAST_NAME).setValue(user.getLastName());
		get(LEVEL_ACCESS).setValue(user.getUserRole());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(LEVEL_ACCESS).setDefaultValue(RolePermissions.BASIC_EMPLOYEE);
	}

	@Override
	protected String getDeleteCommand(Context context) {
		User currentUser = context.getUser();
		if (currentUser.isCanDoUserManagement()
				&& user.getID() != currentUser.getID()) {
			return "delete User " + user.getID();
		}
		return null;
	}
}
