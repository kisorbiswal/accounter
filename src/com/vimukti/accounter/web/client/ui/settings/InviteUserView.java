package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class InviteUserView extends BaseView<ClientUserInfo> {

	TextItem firstNametext;
	TextItem lastNametext;
	EmailField emailField;
	DynamicForm custForm;
	UserRoleGrid grid;
	CheckBox userManagementBox;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		custForm = new DynamicForm();
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		firstNametext = new TextItem(Accounter.constants().firstName());
		firstNametext.setRequired(true);
		lastNametext = new TextItem(Accounter.constants().lastName());
		lastNametext.setRequired(true);
		emailField = new EmailField(Accounter.constants().email());
		emailField.setRequired(true);
		emailField.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					final String em = emailField.getValue().toString().trim();
					if (em.length() != 0) {
						if (!UIUtils.isValidEmail(em)) {
							Accounter.showError(Accounter.constants()
									.invalidEmail());
							emailField.setText("");
						} else {
							// ClientEmail email = new ClientEmail();
							// email.setType(UIUtils.getEmailType(businesEmailSelect
							// .getSelectedValue()));
							// email.setEmail(em);
							// allEmails.put(UIUtils.getEmailType(businesEmailSelect
							// .getSelectedValue()), email);
							Accounter
									.createHomeService()
									.getAllUsers(
											new AccounterAsyncCallback<ArrayList<ClientUserInfo>>() {

												@Override
												public void onResultSuccess(
														ArrayList<ClientUserInfo> result) {
													for (int i = 0; i < result
															.size(); i++) {
														if (em.equals(result
																.get(i)
																.getEmail()))
															Accounter
																	.showError(Accounter
																			.constants()
																			.mailExistedAlready());
														emailField.setText("");
													}
												}

												@Override
												public void onException(
														AccounterException caught) {
													Accounter
															.showError(Accounter
																	.constants()
																	.failedtoloadusersList());
												}
											});

						}
					}
				}
			}
		});
		userManagementBox = new CheckBox(Accounter.constants()
				.allowThisUsertoAddorRemoveusers());
		// userManagementBox.getElement().getStyle().setPadding(5, Unit.PX);
		String choose = Accounter.constants()
				.chooselevelaccessyouwantthisusertohave();
		Label chooseLabel = new Label(choose);
		// chooseLabel.getElement().getStyle().setPadding(5, Unit.PX);
		Label setPerLabel = new Label(Accounter.constants()
				.setUserpermissions());
		setPerLabel.addStyleName("inviteUserLabel");

		Label manageLabel = new Label(Accounter.constants().manageUsers());
		manageLabel.addStyleName("inviteUserLabel");

		custForm.setFields(firstNametext, lastNametext, emailField);
		Element element2 = custForm.getCellFormatter().getElement(0, 0);
		element2.setAttribute("width", "150px");
		initGrid();
		vPanel.add(custForm);
		vPanel.add(setPerLabel);
		vPanel.add(chooseLabel);
		vPanel.add(grid);
		// vPanel.add(manageLabel);
		// vPanel.add(userManagementBox);
		this.add(vPanel);
	}

	@Override
	public void initData() {
		super.initData();
		if (getData() == null) {
			setData(new ClientUserInfo());
		}
		firstNametext.setValue(data.getFirstName());
		lastNametext.setValue(data.getLastName());
		emailField.setValue(data.getEmail());
		// userManagementBox.setValue(takenUser.isCanDoUserManagement());
		grid.setRecords(getRolePermissionsForUser(data));
		// if (data.isActive()) {
		// firstNametext.setDisabled(true);
		// lastNametext.setDisabled(true);
		// emailField.setDisabled(true);
		// // if (takenUser.isAdmin()) {
		// // userManagementBox.setEnabled(false);
		// // }
		// }
		// if(takenUser.isAdmin()) {
		// grid.setDisabled(true);
		// }
	}

	private void initGrid() {
		grid = new UserRoleGrid(false);
		grid.isEnable = false;
		grid.init();
		grid.setView(this);
		grid.setSize("100%", "100%");
	}

	@Override
	public List<DynamicForm> getForms() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub
	}

	public void checkBoxClicked(RolePermissions obj) {
		if (canDoUserManagement(obj)) {
			userManagementBox.setValue(true);
			if (isInViewMode() && data.isAdmin())
				userManagementBox.setEnabled(false);
			else
				userManagementBox.setEnabled(true);
		} else {
			userManagementBox.setValue(false);
			userManagementBox.setEnabled(false);
		}
	}

	public boolean canDoUserManagement(RolePermissions role) {
		if (role.getRoleName().equals(RolePermissions.BASIC_EMPLOYEE)
				|| role.getRoleName().equals(RolePermissions.FINANCIAL_ADVISER))
			return true;

		return false;
	}

	@Override
	public void saveAndUpdateView() {
		data.setFirstName(firstNametext.getValue().toString());
		data.setLastName(lastNametext.getValue().toString());
		data.setFullName(data.getName());
		data.setEmail(emailField.getValue().toString());
		// user.setCanDoUserManagement(userManagementBox.getValue());
		RolePermissions selectedRole = getSelectedRolePermission();
		if (selectedRole != null) {
			data.setUserRole(selectedRole.getRoleName());
			if (selectedRole.getRoleName().equals(RolePermissions.ADMIN)) {
				data.setAdmin(true);
			}
			ClientUserPermissions permissions = new ClientUserPermissions();
			permissions.setTypeOfBankReconcilation(selectedRole
					.getTypeOfBankReconcilation());
			permissions.setTypeOfInvoices(selectedRole.getTypeOfInvoices());
			permissions.setTypeOfExpences(selectedRole.getTypeOfExpences());
			permissions.setTypeOfSystemSettings(selectedRole
					.getTypeOfSystemSettings());
			permissions.setTypeOfViewReports(selectedRole
					.getTypeOfViewReports());
			permissions.setTypeOfPublishReports(selectedRole
					.getTypeOfPublishReports());
			permissions.setTypeOfLockDates(selectedRole.getTypeOfLockDates());

			data.setPermissions(permissions);

			data.setCanDoUserManagement(selectedRole.isCanDoUserManagement());
		}

		saveOrUpdateUser(data);
	}

	private RolePermissions getSelectedRolePermission() {
		for (int i = 0; i < grid.getRowCount(); i++) {
			if (((CheckBox) grid.getWidget(i, 0)).getValue() == true)
				return grid.getRecordByIndex(i);
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

	public List<RolePermissions> getRolePermissionsForUser(ClientUserInfo user) {
		List<RolePermissions> defaultRoles = getDefaultRolesAndPermissions();
		List<RolePermissions> roles = new ArrayList<RolePermissions>();
		// for (RolePermissions role : defaultRoles) {
		// if (user.isAdmin()
		// && (!role.getRoleName().equals(RolePermissions.READ_ONLY) && !role
		// .getRoleName().equals(RolePermissions.INVOICE_ONLY))) {
		// roles.add(role);
		// }
		// }
		roles = roles.isEmpty() ? defaultRoles : roles;
		for (RolePermissions role : roles) {
			if (role.getRoleName().equals(user.getUserRole())) {
				int index = roles.indexOf(role);
				roles.remove(index);
				RolePermissions toBeAdded = new RolePermissions();
				toBeAdded.setRoleName(user.getUserRole());
				toBeAdded.setTypeOfBankReconcilation(user.getPermissions()
						.getTypeOfBankReconcilation());
				toBeAdded.setTypeOfInvoices(user.getPermissions()
						.getTypeOfInvoices());
				toBeAdded.setTypeOfExpences(user.getPermissions()
						.getTypeOfExpences());
				toBeAdded.setTypeOfSystemSettings(user.getPermissions()
						.getTypeOfSystemSettings());
				toBeAdded.setTypeOfViewReports(user.getPermissions()
						.getTypeOfViewReports());
				toBeAdded.setTypeOfPublishReports(user.getPermissions()
						.getTypeOfPublishReports());
				toBeAdded.setTypeOfLockDates(user.getPermissions()
						.getTypeOfLockDates());
				toBeAdded.setCanDoUserManagement(user.isCanDoUserManagement());
				roles.add(index, toBeAdded);
				break;
			}
		}
		return roles;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(FormItem.validate(firstNametext, lastNametext, emailField));
		if (isEmailIDExist(getData())) {
			result.addError(emailField, Accounter.constants()
					.userExistsWithThisMailId());
		}
		return result;
	}

	private boolean isEmailIDExist(ClientUserInfo object) {
		List<ClientUserInfo> list = getCompany().getUsersList();
		if (list == null || list.isEmpty())
			return false;
		for (ClientUserInfo user : list) {
			if (user.getEmail().equals(object.getEmail())) {
				if (user.getID() != object.getID()) {
					return true;
				}
				if (user.getID() == object.getID()) {
					continue;
				} else {
					if (user.getEmail() != null && object.getEmail() != null
							&& user.getEmail().equals(object.getEmail())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().inviteUser();
	}

}
