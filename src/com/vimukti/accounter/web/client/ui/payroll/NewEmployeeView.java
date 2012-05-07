package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomFieldValue;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.CustomFieldDialog;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.EmployeeGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.CustomFieldForm;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewEmployeeView extends BaseView<ClientEmployee> {

	private DynamicForm basicInfoForm, empDetailsInfoForm,
			empOtherDetailsInfoForm, empOtherDetailsLeftForm,
			empOtherDetailsRightForm;
	private TextItem nameItem, employeeIdItem, designationItem, panItem,
			bankNameItem, bankAccountNumberItem, bankBranchItem, locationItem,
			contactNumberItem, emailItem, passportNumberItem,
			countryOfIssueItem, emplVisaNumberItem;
	private DateField dateOfBirthItem, dateOfHire, passportExpiryDateItem,
			emplVisaNumberDateItem, lastDateItem;
	private StyledPanel mainPanel;
	private StyledPanel firstPanel, secondPanel;
	private EmployeeGroupCombo employeeGroupCombo;
	private final String[] genderTypes = { messages.unspecified(),
			messages.male(), messages.female() };
	private ArrayList<String> listOfgenders, listOfReaons;
	private SelectCombo genderSelect, reasonCombo;
	private TextAreaItem addrArea;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	private CheckboxItem activeOrInactive;
	private final String[] reasons = { messages2.gotNewJobOffer(),
			messages2.quitWithOutAjob(), messages2.lackofPerformance(),
			messages2.disputesbetweenCoworkers(),
			messages2.nosatisfactionwithJob(), messages2.notenoughHours(),
			messages2.jobwasTemporary(), messages2.contractended(),
			messages2.workwasSeasonal(), messages2.betteropportunity(),
			messages2.seekinggrowth(), messages2.careerchange(),
			messages2.returnedtoSchool(), messages2.relocated(),
			messages2.raisedaFamily(), messages.other() };
	CustomFieldForm customFieldForm;
	Button addCustomFieldButton;
	CustomFieldDialog customFieldDialog;

	public NewEmployeeView() {
		this.getElement().setId("NewEmployeeView");
	}

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
			initViewData(getData());
		}
		super.initData();
	}

	private void initViewData(ClientEmployee data) {
		nameItem.setValue(data.getName());
		dateOfBirthItem.setValue(new ClientFinanceDate(data.getDateOfBirth()));
		if (data.getGender() != -1) {
			genderSelect.setComboItem(genderTypes[data.getGender()]);
		}
		contactNumberItem.setValue(data.getPhoneNo());
		emailItem.setValue(data.getEmail());
		activeOrInactive.setValue(data.isActive());
		lastDateItem.setVisible(!activeOrInactive.getValue());
		reasonCombo.setVisible(!activeOrInactive.getValue());
		panItem.setValue(data.getPanNumber());

		setAddresses(data.getAddress());

		ClientAddress toBeShown = allAddresses.get(ClientAddress.TYPE_BILL_TO);
		if (toBeShown != null) {
			String toToSet = new String();
			if (toBeShown.getAddress1() != null
					&& !toBeShown.getAddress1().isEmpty()) {
				toToSet = toBeShown.getAddress1().toString() + "\n";
			}

			if (toBeShown.getStreet() != null
					&& !toBeShown.getStreet().isEmpty()) {
				toToSet += toBeShown.getStreet().toString() + "\n";
			}

			if (toBeShown.getCity() != null && !toBeShown.getCity().isEmpty()) {
				toToSet += toBeShown.getCity().toString() + "\n";
			}

			if (toBeShown.getStateOrProvinence() != null
					&& !toBeShown.getStateOrProvinence().isEmpty()) {
				toToSet += toBeShown.getStateOrProvinence() + "\n";
			}
			if (toBeShown.getZipOrPostalCode() != null
					&& !toBeShown.getZipOrPostalCode().isEmpty()) {
				toToSet += toBeShown.getZipOrPostalCode() + "\n";
			}
			if (toBeShown.getCountryOrRegion() != null
					&& !toBeShown.getCountryOrRegion().isEmpty()) {
				toToSet += toBeShown.getCountryOrRegion();
			}
			addrArea.setValue(toToSet);
		}

		employeeIdItem.setValue(data.getNumber());
		dateOfHire.setValue(new ClientFinanceDate(data.getPayeeSince()));
		employeeGroupCombo.setGroupValue(data.getGroup());
		designationItem.setValue(data.getDesignation());
		locationItem.setValue(data.getLocation());
		bankAccountNumberItem.setValue(data.getBankAccountNo());
		bankBranchItem.setValue(data.getBankBranch());
		bankNameItem.setValue(data.getBankName());
		passportNumberItem.setValue(data.getPassportNumber());
		passportExpiryDateItem.setValue(new ClientFinanceDate(data
				.getPassportExpiryDate()));
		lastDateItem.setValue(new ClientFinanceDate(data.getLastDate()));
		countryOfIssueItem.setValue(data.getCountryOfIssue());
		emplVisaNumberItem.setValue(data.getVisaNumber());
		if (data.getReasonType() != -1) {
			reasonCombo.setValue(reasons[data.getReasonType()]);
		}
	}

	private void setAddresses(Set<ClientAddress> addresses) {
		if (addresses != null) {
			Iterator<ClientAddress> it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress addr = (ClientAddress) it.next();
				if (addr != null) {
					allAddresses.put(addr.getType(), addr);
				}
			}
		}
	}

	public void createCustomFieldControls() {
		if (data != null && data.getCustomFieldValues() != null) {
			customFieldForm.updateValues(data.getCustomFieldValues(),
					getCompany(), ClientPayee.TYPE_EMPLOYEE);
		}
		customFieldForm.createControls(getCompany(),
				data == null ? null : data.getCustomFieldValues(),
				ClientPayee.TYPE_EMPLOYEE);
		Set<ClientCustomFieldValue> customFieldValues = data == null ? new HashSet<ClientCustomFieldValue>()
				: data.getCustomFieldValues();
		Set<ClientCustomFieldValue> deleteCustomFieldValues = new HashSet<ClientCustomFieldValue>();
		for (ClientCustomFieldValue value : customFieldValues) {
			if (getCompany().getClientCustomField(value.getID()) == null) {
				deleteCustomFieldValues.add(value);
			}
		}

		for (ClientCustomFieldValue clientCustomFieldValue : deleteCustomFieldValues) {
			customFieldValues.remove(clientCustomFieldValue);
		}
		customFieldForm.setEnabled(!isInViewMode());
	}

	public Set<ClientAddress> getAddresss() {
		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType("company"));
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses
					.put(UIUtils.getAddressType("company"), selectedAddress);
		}
		Collection<ClientAddress> add = allAddresses.values();
		Iterator<ClientAddress> it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			Set<ClientAddress> hashSet = new HashSet<ClientAddress>();
			hashSet.add(a);
			return hashSet;
			// toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return null;
	}

	private void createControls() {
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		mainPanel = new StyledPanel("mainPanel");

		firstPanel = new StyledPanel("firstPanel");

		firstPanel.add(getEmpBasicInfo());
		firstPanel.add(getEmpDetails());

		secondPanel = new StyledPanel("secondPanel");
		secondPanel.add(getEmpOtherDetailsInfo());
		createCustomFieldControls();

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
		bankAccountNumberItem.setEnabled(!isInViewMode());
		bankNameItem = new TextItem(messages.bankName(), "bankNameItem");
		bankNameItem.setEnabled(!isInViewMode());
		bankBranchItem = new TextItem(messages.bankBranch(), "bankBranchItem");
		bankBranchItem.setEnabled(!isInViewMode());
		passportNumberItem = new TextItem(messages.passportNumber(),
				"passportNumberItem");
		passportNumberItem.setEnabled(!isInViewMode());
		passportExpiryDateItem = new DateField(messages.passportExpiryDate(),
				"passportExpiryDateItem");
		passportExpiryDateItem.setEnteredDate(new ClientFinanceDate());
		passportExpiryDateItem.setEnabled(!isInViewMode());
		countryOfIssueItem = new TextItem(messages.countryOfIssue(),
				"countryOfIssueItem");
		countryOfIssueItem.setEnabled(!isInViewMode());
		emplVisaNumberItem = new TextItem(messages.visaNumber(),
				"emplVisaNumberItem");
		emplVisaNumberItem.setEnabled(!isInViewMode());
		emplVisaNumberDateItem = new DateField(messages.visaExpiryDate(),
				"emplVisaNumberDateItem");
		emplVisaNumberDateItem.setEnteredDate(new ClientFinanceDate());
		emplVisaNumberDateItem.setEnabled(!isInViewMode());
		empOtherDetailsInfoForm = new DynamicForm("empOtherDetailsInfoForm");
		customFieldDialog = new CustomFieldDialog(this, messages.CustomField(),
				messages.ManageCustomFields());

		empOtherDetailsRightForm = new DynamicForm("empOtherDetailsLeftForm");
		empOtherDetailsRightForm.add(passportExpiryDateItem,
				countryOfIssueItem, emplVisaNumberItem);

		empOtherDetailsLeftForm = new DynamicForm("empOtherDetailsLeftForm");
		empOtherDetailsLeftForm.add(bankAccountNumberItem, bankNameItem,
				bankBranchItem, passportNumberItem);
		// empOtherDetailsLeftForm.add(addCustomFieldButton);
		// empOtherDetailsLeftForm.add(customFieldForm);

		empOtherDetailsInfoForm.add(empOtherDetailsLeftForm);
		empOtherDetailsInfoForm.add(empOtherDetailsRightForm);

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
		employeeIdItem.setEnabled(!isInViewMode());
		dateOfHire = new DateField(messages.dateofHire(), "dateOfHire");
		dateOfHire.setEnteredDate(new ClientFinanceDate());
		dateOfHire.setEnabled(!isInViewMode());
		panItem = new TextItem(messages.panOrEinNumber(), "panItem");
		panItem.setEnabled(!isInViewMode());
		employeeGroupCombo = new EmployeeGroupCombo(messages.employeeGroup(),
				true);
		employeeGroupCombo.setEnabled(!isInViewMode());
		designationItem = new TextItem(messages.designation(),
				"designationItem");
		designationItem.setEnabled(!isInViewMode());
		locationItem = new TextItem(messages.workingLocation(), "locationItem");
		locationItem.setEnabled(!isInViewMode());
		addCustomFieldButton = new Button();
		addCustomFieldButton.setText(messages.ManageCustomFields());
		addCustomFieldButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				customFieldDialog.show();
				customFieldDialog.center();
			}
		});
		addCustomFieldButton.setEnabled(!isInViewMode());
		customFieldForm = UIUtils.CustomFieldsform(messages.terms());

		empDetailsInfoForm.add(employeeIdItem, dateOfHire, panItem,
				employeeGroupCombo, designationItem, locationItem);
		empDetailsInfoForm.add(addCustomFieldButton);
		empDetailsInfoForm.add(customFieldForm);
		employeeDetailsInfo.setContentWidget(empDetailsInfoForm);
		return employeeDetailsInfo;
	}

	private CaptionPanel getEmpBasicInfo() {
		CaptionPanel employeeBasicInfo = new CaptionPanel(
				messages.employeeBasicInfo());
		DOM.setStyleAttribute(employeeBasicInfo.getElement(), "border",
				"1px solid #ccc");

		basicInfoForm = new DynamicForm("basicInfoForm");

		nameItem = new TextItem(messages.name(), "nameItem");
		nameItem.setRequired(true);
		nameItem.setEnabled(!isInViewMode());

		dateOfBirthItem = new DateField(messages.dateofBirth(),
				"dateOfBirthItem");
		dateOfBirthItem.setEnteredDate(new ClientFinanceDate());
		dateOfBirthItem.setEnabled(!isInViewMode());

		genderSelect = new SelectCombo(messages.gender());
		genderSelect.setEnabled(!isInViewMode());
		listOfgenders = new ArrayList<String>();
		for (int i = 0; i < genderTypes.length; i++) {
			listOfgenders.add(genderTypes[i]);
		}
		genderSelect.initCombo(listOfgenders);

		activeOrInactive = new CheckboxItem(messages.active(),
				"activeOrInactive");
		activeOrInactive.setValue(true);
		activeOrInactive.setEnabled(!isInViewMode());

		reasonCombo = new SelectCombo(messages2.reasonForInactive());
		reasonCombo.setEnabled(!isInViewMode());
		listOfReaons = new ArrayList<String>();
		for (int i = 0; i < reasons.length; i++) {
			listOfReaons.add(reasons[i]);
		}
		reasonCombo.initCombo(listOfReaons);

		lastDateItem = new DateField(messages2.lastDate(), "employeeLastDate");
		lastDateItem.setEnteredDate(new ClientFinanceDate());
		lastDateItem.setEnabled(!isInViewMode());
		lastDateItem.setVisible(!activeOrInactive.getValue());
		reasonCombo.setVisible(!activeOrInactive.getValue());
		contactNumberItem = new TextItem(messages.contactNumber(),
				"contactNumberItem");
		contactNumberItem.setEnabled(!isInViewMode());
		emailItem = new TextItem(messages.email(), "emailItem");
		emailItem.setEnabled(!isInViewMode());
		addrArea = new TextAreaItem(messages.address(), "addrArea");
		addrArea.setDisabled(isInViewMode());
		addrArea.setEnabled(!isInViewMode());
		addrArea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("", "", addrArea, "Bill to", allAddresses);

			}
		});

		addrArea.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				new AddressDialog("", "", addrArea, "Bill to", allAddresses);

			}
		});

		activeOrInactive.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				lastDateItem.setVisible(!activeOrInactive.getValue());
				reasonCombo.setVisible(!activeOrInactive.getValue());

			}
		});
		basicInfoForm.add(nameItem, dateOfBirthItem, genderSelect,
				activeOrInactive, lastDateItem, reasonCombo, contactNumberItem,
				emailItem, addrArea);

		employeeBasicInfo.setContentWidget(basicInfoForm);
		return employeeBasicInfo;
	}

	@Override
	public void saveAndUpdateView() {
		updateData();
		saveOrUpdate(getData());
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		String errorString = AccounterExceptions
				.getErrorString(accounterException);
		Accounter.showError(errorString);
	}

	private void updateData() {

		customFieldForm.updateValues(data.getCustomFieldValues(), getCompany(),
				ClientPayee.TYPE_EMPLOYEE);

		data.setName(nameItem.getValue());

		data.setAddress(getAddresss());
		data.setBankAccountNo(bankAccountNumberItem.getValue());
		data.setBankName(bankNameItem.getValue());
		data.setBankBranch(bankBranchItem.getValue());
		data.setActive(activeOrInactive.getValue());
		data.setPhoneNo(contactNumberItem.getValue());
		data.setPanNumber(panItem.getValue());
		data.setCountryOfIssue(countryOfIssueItem.getValue());

		data.setDateOfBirth(dateOfBirthItem.getValue().getDate());
		data.setPayeeSince(dateOfHire.getValue().getDate());

		data.setDesignation(designationItem.getValue());
		data.setEmail(emailItem.getValue());
		data.setContacts(new HashSet<ClientContact>());
		if (employeeGroupCombo.getSelectedValue() != null) {
			data.setGroup(employeeGroupCombo.getSelectedValue().getID());
		}
		data.setLocation(locationItem.getValue());
		data.setNumber(employeeIdItem.getValue());
		data.setPassportExpiryDate(passportExpiryDateItem.getValue().getDate());
		data.setPassportNumber(passportNumberItem.getValue());
		data.setVisaNumber(emplVisaNumberItem.getValue());
		data.setVisaExpiryDate(emplVisaNumberDateItem.getValue().getDate());
		data.setLastDate(lastDateItem.getValue().getDate());
		data.setReasonType(getReasonType());
		int genderType = -1;
		String selectedValue = genderSelect.getSelectedValue();
		if (selectedValue != null) {
			if (selectedValue.equals(messages.unspecified())) {
				genderType = 0;
			} else if (selectedValue.equals(messages.male())) {
				genderType = 1;
			} else if (selectedValue.equals(messages.female())) {
				genderType = 2;
			}
		}
		data.setGender(genderType);
	}

	private int getReasonType() {
		int reasonType = -1;
		String selectedValue = reasonCombo.getSelectedValue();
		if (selectedValue != null) {
			if (selectedValue.equals(messages2.gotNewJobOffer())) {
				reasonType = 0;
			} else if (selectedValue.equals(messages2.quitWithOutAjob())) {
				reasonType = 1;
			} else if (selectedValue.equals(messages2.lackofPerformance())) {
				reasonType = 2;
			} else if (selectedValue.equals(messages2
					.disputesbetweenCoworkers())) {
				reasonType = 3;
			} else if (selectedValue.equals(messages2.nosatisfactionwithJob())) {
				reasonType = 4;
			} else if (selectedValue.equals(messages2.notenoughHours())) {
				reasonType = 5;
			} else if (selectedValue.equals(messages2.jobwasTemporary())) {
				reasonType = 6;
			} else if (selectedValue.equals(messages2.contractended())) {
				reasonType = 7;
			} else if (selectedValue.equals(messages2.workwasSeasonal())) {
				reasonType = 8;
			} else if (selectedValue.equals(messages2.betteropportunity())) {
				reasonType = 9;
			} else if (selectedValue.equals(messages2.seekinggrowth())) {
				reasonType = 10;
			} else if (selectedValue.equals(messages2.careerchange())) {
				reasonType = 11;
			} else if (selectedValue.equals(messages2.returnedtoSchool())) {
				reasonType = 12;
			} else if (selectedValue.equals(messages2.relocated())) {
				reasonType = 13;
			} else if (selectedValue.equals(messages2.raisedaFamily())) {
				reasonType = 14;
			} else if (selectedValue.equals(messages.other())) {
				reasonType = 15;
			}

		}
		return reasonType;

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(basicInfoForm.validate());

		return result;
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();

				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.EMPLOYEE, data.getID(),
				editCallBack);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		nameItem.setEnabled(!isInViewMode());
		employeeIdItem.setEnabled(!isInViewMode());
		designationItem.setEnabled(!isInViewMode());
		panItem.setEnabled(!isInViewMode());
		bankNameItem.setEnabled(!isInViewMode());
		bankAccountNumberItem.setEnabled(!isInViewMode());
		bankBranchItem.setEnabled(!isInViewMode());
		locationItem.setEnabled(!isInViewMode());
		contactNumberItem.setEnabled(!isInViewMode());
		emailItem.setEnabled(!isInViewMode());
		passportNumberItem.setEnabled(!isInViewMode());
		countryOfIssueItem.setEnabled(!isInViewMode());
		emplVisaNumberItem.setEnabled(!isInViewMode());
		dateOfBirthItem.setEnabled(!isInViewMode());
		dateOfHire.setEnabled(!isInViewMode());
		passportExpiryDateItem.setEnabled(!isInViewMode());
		emplVisaNumberDateItem.setEnabled(!isInViewMode());
		employeeGroupCombo.setEnabled(!isInViewMode());
		genderSelect.setEnabled(!isInViewMode());
		addrArea.setEnabled(!isInViewMode());
		activeOrInactive.setEnabled(!isInViewMode());
		lastDateItem.setEnabled(!isInViewMode());
		reasonCombo.setEnabled(!isInViewMode());
		customFieldForm.setEnabled(!isInViewMode());
		addCustomFieldButton.setEnabled(!isInViewMode());
		super.onEdit();

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
