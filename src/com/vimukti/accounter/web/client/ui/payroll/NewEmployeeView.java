package com.vimukti.accounter.web.client.ui.payroll;

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.EmployeeGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewEmployeeView extends BaseView<ClientEmployee> {

	private DynamicForm basicInfoForm, empDetailsInfoForm,
			empOtherDetailsInfoForm;
	private TextItem nameItem, employeeIdItem, designationItem, addressItem,
			streetItem, cityItem, stateItem, countryItem, postalCodeItem,
			panItem, bankNameItem, bankAccountNumberItem, bankBranchItem,
			locationItem, contactNumberItem, emailItem, passportNumberItem,
			countryOfIssueItem, emplVisaNumberItem;
	private DateItem dateOfBirthItem, dateOfHire, passportExpiryDateItem,
			emplVisaNumberDateItem;
	private RadioGroupItem genderGroupItem;
	private SelectCombo employeeCategoryCombo;
	private VerticalPanel mainPanel;
	private HorizontalPanel firstPanel, secondPanel;
	private EmployeeGroupCombo employeeGroupCombo;

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (getData() == null) {
			setData(new ClientEmployee());
		} else {
			initWarehouseData(getData());
		}
		super.initData();
	}

	private void initWarehouseData(ClientEmployee data) {
		// TODO Auto-generated method stub

	}

	private void createControls() {

		mainPanel = new VerticalPanel();
		firstPanel = new HorizontalPanel();

		firstPanel.add(getEmpBasicInfo());
		firstPanel.add(getEmpDetails());

		secondPanel = new HorizontalPanel();
		secondPanel.add(getEmpOtherDetailsInfo());

		mainPanel.add(firstPanel);
		mainPanel.add(secondPanel);

		this.add(mainPanel);

		setSize("100%", "100%");

	}

	private CaptionPanel getEmpOtherDetailsInfo() {
		CaptionPanel empOtherDetailsInfo = new CaptionPanel(
				messages.employeeDetailsInfo());
		DOM.setStyleAttribute(empOtherDetailsInfo.getElement(), "border",
				"1px solid #ccc");
		bankAccountNumberItem = new TextItem(messages.bankAccountNumber(),
				"bankAccountNumberItem");
		bankNameItem = new TextItem(messages.bankName(), "bankNameItem");
		bankBranchItem = new TextItem(messages.bankBranch(), "bankBranchItem");
		passportNumberItem = new TextItem(messages.passportNumber(),
				"passportNumberItem");
		passportExpiryDateItem = new DateItem(messages.passportExpiryDate(),
				"passportExpiryDateItem");
		countryOfIssueItem = new TextItem(messages.countryOfIssue(),
				"countryOfIssueItem");
		emplVisaNumberItem = new TextItem(messages.visaNumber(),
				"emplVisaNumberItem");
		emplVisaNumberDateItem = new DateItem(messages.visaExpiryDate(),
				"emplVisaNumberDateItem");
		empOtherDetailsInfoForm = new DynamicForm("empOtherDetailsInfoForm");
		empOtherDetailsInfoForm.add(bankAccountNumberItem, bankNameItem,
				bankBranchItem, passportNumberItem, passportExpiryDateItem,
				countryOfIssueItem, emplVisaNumberItem);
		empOtherDetailsInfo.setContentWidget(empOtherDetailsInfoForm);
		return empOtherDetailsInfo;
	}

	private CaptionPanel getEmpDetails() {
		CaptionPanel employeeDetailsInfo = new CaptionPanel(
				messages.employeeDetailsInfo());
		DOM.setStyleAttribute(employeeDetailsInfo.getElement(), "border",
				"1px solid #ccc");
		empDetailsInfoForm = new DynamicForm("empDetailsInfoForm");

		employeeIdItem = new TextItem(messages.employeeID(), "employeeIdItem");
		dateOfHire = new DateItem(messages.dateofHire(), "dateOfHire");
		panItem = new TextItem(messages.panOrEinNumber(), "panItem");
		employeeGroupCombo = new EmployeeGroupCombo(messages.employeeGroup(),
				true);
		employeeCategoryCombo = new SelectCombo(messages.employeeCategory());
		designationItem = new TextItem(messages.designation(),
				"designationItem");
		locationItem = new TextItem(messages.workingLocation(), "locationItem");

		empDetailsInfoForm.add(employeeIdItem, dateOfHire, panItem,
				employeeGroupCombo, employeeCategoryCombo, designationItem,
				locationItem);
		employeeDetailsInfo.setContentWidget(empDetailsInfoForm);
		return employeeDetailsInfo;
	}

	private CaptionPanel getEmpBasicInfo() {
		CaptionPanel employeeBasicInfo = new CaptionPanel(
				messages.employeeBasicInfo());
		DOM.setStyleAttribute(employeeBasicInfo.getElement(), "border",
				"1px solid #ccc");

		basicInfoForm = new DynamicForm("basicInfoForm");

		nameItem = new TextItem(messages.lastName(), "nameItem");

		dateOfBirthItem = new DateItem(messages.dateofBirth(),
				"dateOfBirthItem");

		genderGroupItem = new RadioGroupItem(messages.gender());
		genderGroupItem.setValueMap(messages.male(), messages.female());

		contactNumberItem = new TextItem(messages.contactNumber(),
				"contactNumberItem");
		emailItem = new TextItem(messages.email(), "emailItem");

		addressItem = new TextItem(messages.address(), "addressItem");
		streetItem = new TextItem(messages.streetName(), "streetItem");
		cityItem = new TextItem(messages.city(), "cityItem");
		stateItem = new TextItem(messages.stateOrProvince(), "stateItem");
		countryItem = new TextItem(messages.country(), "countryItem");
		postalCodeItem = new TextItem(messages.postalCode(), "postalCodeItem");

		basicInfoForm.add(nameItem, dateOfBirthItem, genderGroupItem,
				contactNumberItem, emailItem, addressItem, streetItem,
				cityItem, stateItem, countryItem, postalCodeItem);

		employeeBasicInfo.setContentWidget(basicInfoForm);
		return employeeBasicInfo;
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	private void updateData() {
		data.setName(nameItem.getValue());

		ClientAddress address = new ClientAddress();
		address.setAddress1(addressItem.getValue());
		address.setStreet(streetItem.getValue());
		address.setCity(cityItem.getValue());
		address.setStateOrProvinence(stateItem.getValue());
		address.setCountryOrRegion(countryItem.getValue());
		address.setZipOrPostalCode(postalCodeItem.getValue());

		data.setAddress(address);
		data.setBankAccountNumber(bankAccountNumberItem.getValue());
		data.setBankName(bankNameItem.getValue());
		data.setBranch(bankBranchItem.getValue());

		data.setCategory(employeeCategoryCombo.getSelectedIndex());
		data.setCountryOfIssue(countryOfIssueItem.getValue());

		data.setDateOfBirth(dateOfBirthItem.getValue().getDate());
		data.setDateofJoining(dateOfHire.getValue().getDate());

		data.setDesignation(designationItem.getValue());
		data.setEmail(emailItem.getValue());
		data.setContactDetail(new ClientContact());
		// data.setGroup(employeeGroupCombo.getSelectedValue().getID());
		data.setLocation(locationItem.getValue());
		data.setNumber(employeeIdItem.getValue());
		data.setPassportExpiryDate(passportExpiryDateItem.getValue().getDate());
		data.setPassportNumber(passportNumberItem.getValue());
		data.setVisaNumber(emplVisaNumberItem.getValue());
		data.setVisaExpiryDate(emplVisaNumberDateItem.getValue().getDate());

	}

	@Override
	public ValidationResult validate() {
		return super.validate();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.newEmployee();
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		nameItem.setFocus();
	}

}
