package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class InviteUserView extends BaseView<ClientUser> {

	TextItem firstNametext;
	TextItem lastNametext;
	EmailField emailField;
	DynamicForm custForm;
	UserRoleGrid grid;
	CheckBox userManagementBox;

	boolean isEditMode;

	public ClientUser takenUser;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		custForm = new DynamicForm();

		VerticalPanel vPanel = new VerticalPanel();
		firstNametext = new TextItem("First Name");
		firstNametext.setRequired(true);
		lastNametext = new TextItem("Last Name");
		lastNametext.setRequired(true);
		emailField = new EmailField("E-Mail");
		emailField.setRequired(true);
		emailField.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					String em = emailField.getValue().toString();
					if (!UIUtils.isValidEmail(em)) {
						Accounter.showError(AccounterErrorType.INVALID_EMAIL);
						emailField.setText("");
					} else {
						// ClientEmail email = new ClientEmail();
						// email.setType(UIUtils.getEmailType(businesEmailSelect
						// .getSelectedValue()));
						// email.setEmail(em);
						// allEmails.put(UIUtils.getEmailType(businesEmailSelect
						// .getSelectedValue()), email);
					}
				}

			}
		});
		userManagementBox = new CheckBox(
				"Allow this user to add and remove users and change permissions");
		// userManagementBox.getElement().getStyle().setPadding(5, Unit.PX);
		String choose = "Choose the level of access you want this user to have.";
		Label chooseLabel = new Label(choose);
		// chooseLabel.getElement().getStyle().setPadding(5, Unit.PX);
		Label setPerLabel = new Label("Set the User permissions");
		setPerLabel.addStyleName("inviteUserLabel");
		initGrid();
		Label manageLabel = new Label("Manage Users");
		manageLabel.addStyleName("inviteUserLabel");

		custForm.setFields(firstNametext, lastNametext, emailField);
		custForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getCompanyMessages().width(), "150px");

		vPanel.add(custForm);
		vPanel.add(setPerLabel);
		vPanel.add(chooseLabel);
		vPanel.add(grid);
		vPanel.add(manageLabel);
		vPanel.add(userManagementBox);
		canvas.add(vPanel);
	}

	@Override
	public void initData() {
		super.initData();
		if (takenUser != null) {
			firstNametext.setValue(takenUser.getFirstName());
			lastNametext.setValue(takenUser.getLastName());
			emailField.setValue(takenUser.getEmailId());
			userManagementBox.setValue(takenUser.isCanDoUserManagement());
			grid.setRecords(getRolePermissionsForUser(takenUser));
			if (takenUser.isActive()) {
				firstNametext.setDisabled(true);
				lastNametext.setDisabled(true);
				emailField.setDisabled(true);
				if (takenUser.isAdmin()) {
					userManagementBox.setEnabled(false);
				}
			}
		}
	}

	private void initGrid() {
		grid = new UserRoleGrid(false);
		grid.init();
		grid.setView(this);
		grid.setRecords(getDefaultRolesAndPermissions());
		grid.setSize("100%", "100%");
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public void checkBoxClicked(RolePermissions obj) {
		if (canDoUserManagement(obj)) {
			userManagementBox.setValue(true);
			if (takenUser != null && takenUser.isAdmin())
				userManagementBox.setEnabled(false);
			else
				userManagementBox.setEnabled(true);
		} else {
			userManagementBox.setValue(false);
			userManagementBox.setEnabled(false);
		}
	}

	public boolean canDoUserManagement(RolePermissions role) {
		if (role.getRoleName().equals(RolePermissions.STANDARD)
				|| role.getRoleName().equals(RolePermissions.FINANCIAL_ADVISER))
			return true;

		return false;
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		ClientUser user = takenUser != null ? takenUser : new ClientUser();
		String prevoiusEmail = takenUser != null ? takenUser.getEmailId() : "";
		user.setFirstName(firstNametext.getValue().toString());
		user.setLastName(lastNametext.getValue().toString());
		user.setFullName(user.getName());
		user.setEmailId(emailField.getValue().toString());
		user.setCanDoUserManagement(userManagementBox.getValue());

		RolePermissions selectedRole = getSelectedRolePermission();
		if (selectedRole != null) {
			user.setUserRole(selectedRole.getRoleName());

			ClientUserPermissions permissions = new ClientUserPermissions();
			permissions.setTypeOfBankReconcilation(selectedRole
					.getTypeOfBankReconcilation());
			permissions.setTypeOfInvoicesAndExpenses(selectedRole
					.getTypeOfInvoicesAndExpenses());
			permissions.setTypeOfSystemSettings(selectedRole
					.getTypeOfSystemSettings());
			permissions.setTypeOfViewReports(selectedRole
					.getTypeOfViewReports());
			permissions.setTypeOfPublishReports(selectedRole
					.getTypeOfPublishReports());
			permissions.setTypeOfLockDates(selectedRole.getTypeOfLockDates());

			user.setPermissions(permissions);
		}

		if (user.getStringID() != null)
			if (!user.getEmailId().equals(prevoiusEmail))
				if (isExist(user))
					throw new InvalidEntryException(
							"An User already exists with this Email ID");
				else
					alterObject(user);
			else
				alterObject(user);
		else {
			if (isExist(user))
				throw new InvalidEntryException(
						"An User already exists with this Email ID");
			else
				createObject(user);
		}
	}

	private RolePermissions getSelectedRolePermission() {
		for (int i = 0; i < grid.getRowCount(); i++) {
			if (((CheckBox) grid.getWidget(i, 0)).getValue() == true)
				return grid.getRecordByIndex(i);
		}
		return null;
	}

	@Override
	public void setData(ClientUser data) {
		super.setData(data);
		if (data != null)
			takenUser = data;
		else
			takenUser = null;
	}

	public List<RolePermissions> getDefaultRolesAndPermissions() {
		List<RolePermissions> list = new ArrayList<RolePermissions>();

		RolePermissions readOnly = new RolePermissions();
		readOnly.setRoleName(RolePermissions.READ_ONLY);
		readOnly.setTypeOfBankReconcilation(RolePermissions.TYPE_NO);
		readOnly.setTypeOfInvoicesAndExpenses(RolePermissions.TYPE_READ_ONLY);
		readOnly.setTypeOfSystemSettings(RolePermissions.TYPE_NO);
		readOnly.setTypeOfViewReports(RolePermissions.TYPE_READ_ONLY);
		readOnly.setTypeOfPublishReports(RolePermissions.TYPE_NO);
		readOnly.setTypeOfLockDates(RolePermissions.TYPE_NO);
		list.add(readOnly);

		RolePermissions employee = new RolePermissions();
		employee.setRoleName(RolePermissions.INVOICE_ONLY);
		employee.setTypeOfBankReconcilation(RolePermissions.TYPE_NO);
		employee.setTypeOfInvoicesAndExpenses(RolePermissions.TYPE_YES);
		employee.setTypeOfSystemSettings(RolePermissions.TYPE_NO);
		employee.setTypeOfViewReports(RolePermissions.TYPE_NO);
		employee.setTypeOfPublishReports(RolePermissions.TYPE_NO);
		employee.setTypeOfLockDates(RolePermissions.TYPE_NO);
		list.add(employee);

		RolePermissions standard = new RolePermissions();
		standard.setRoleName(RolePermissions.STANDARD);
		standard.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		standard.setTypeOfInvoicesAndExpenses(RolePermissions.TYPE_YES);
		standard.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		standard.setTypeOfViewReports(RolePermissions.TYPE_YES);
		standard.setTypeOfPublishReports(RolePermissions.TYPE_NO);
		standard.setTypeOfLockDates(RolePermissions.TYPE_NO);
		list.add(standard);

		RolePermissions financialAdviser = new RolePermissions();
		financialAdviser.setRoleName(RolePermissions.FINANCIAL_ADVISER);
		financialAdviser.setTypeOfBankReconcilation(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfInvoicesAndExpenses(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfSystemSettings(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfViewReports(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfPublishReports(RolePermissions.TYPE_YES);
		financialAdviser.setTypeOfLockDates(RolePermissions.TYPE_YES);
		list.add(financialAdviser);

		return list;
	}

	public List<RolePermissions> getRolePermissionsForUser(ClientUser user) {
		List<RolePermissions> defaultRoles = getDefaultRolesAndPermissions();
		List<RolePermissions> roles = new ArrayList<RolePermissions>();
		for (RolePermissions role : defaultRoles) {
			if (user.isAdmin()
					&& (!role.getRoleName().equals(RolePermissions.READ_ONLY) && !role
							.getRoleName().equals(RolePermissions.INVOICE_ONLY))) {
				roles.add(role);
			}
		}
		roles = roles.isEmpty() ? defaultRoles : roles;
		for (RolePermissions role : roles) {
			if (role.getRoleName().equals(user.getUserRole())) {
				int index = roles.indexOf(role);
				roles.remove(index);
				RolePermissions toBeAdded = new RolePermissions();
				toBeAdded.setRoleName(user.getUserRole());
				toBeAdded.setTypeOfBankReconcilation(user.getPermissions()
						.getTypeOfBankReconcilation());
				toBeAdded.setTypeOfInvoicesAndExpenses(user.getPermissions()
						.getTypeOfInvoicesAndExpenses());
				toBeAdded.setTypeOfSystemSettings(user.getPermissions()
						.getTypeOfSystemSettings());
				toBeAdded.setTypeOfViewReports(user.getPermissions()
						.getTypeOfViewReports());
				toBeAdded.setTypeOfPublishReports(user.getPermissions()
						.getTypeOfPublishReports());
				toBeAdded.setTypeOfLockDates(user.getPermissions()
						.getTypeOfLockDates());
				roles.add(index, toBeAdded);
				break;
			}
		}
		return roles;
	}

	@Override
	public boolean validate() throws Exception {
		return AccounterValidator.validateFormItem(false, firstNametext,
				lastNametext, emailField);
		// return super.validate();
	}

	private boolean isExist(ClientUser object) {
		List<ClientUser> list = FinanceApplication.getCompany().getUsersList();
		if (list == null || list.isEmpty())
			return false;
		for (ClientUser user : list) {
			if (user.getStringID() != object.getStringID()
					&& user.getEmailId() != null
					&& user.getEmailId().equals(object.getEmailId())) {
				return true;
			}
		}
		return false;
	}

}
