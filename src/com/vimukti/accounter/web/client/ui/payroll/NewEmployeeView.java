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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.EmployeeGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewEmployeeView extends BaseView<ClientEmployee> {

	private DynamicForm basicInfoForm, empDetailsInfoForm,
			empOtherDetailsInfoForm;
	private TextItem nameItem, employeeIdItem, designationItem, panItem,
			bankNameItem, bankAccountNumberItem, bankBranchItem, locationItem,
			contactNumberItem, emailItem, passportNumberItem,
			countryOfIssueItem, emplVisaNumberItem;
	private DateItem dateOfBirthItem, dateOfHire, passportExpiryDateItem,
			emplVisaNumberDateItem;
	private SelectCombo employeeCategoryCombo;
	private VerticalPanel mainPanel;
	private HorizontalPanel firstPanel, secondPanel;
	private EmployeeGroupCombo employeeGroupCombo;
	private final String[] genderTypes = { messages.unspecified(),
			messages.male(), messages.female() };
	private ArrayList<String> listOfgenders;
	private SelectCombo genderSelect;
	private TextAreaItem addrArea;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;

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
		genderSelect.setValue(genderTypes[data.getGender()]);
		contactNumberItem.setValue(data.getContactNumber());
		emailItem.setValue(data.getEmail());

		Set<ClientAddress> addresses = new HashSet<ClientAddress>();
		addresses.add(data.getAddress());
		setAddresses(addresses);

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
		dateOfHire.setValue(new ClientFinanceDate(data.getDateofJoining()));
		employeeGroupCombo.setValue(getCompany().getEmployeeGroup(
				data.getGroup()));
		employeeCategoryCombo.setValue(getCompany().getEmployeeCategory(
				data.getCategory()));
		designationItem.setValue(data.getDesignation());
		locationItem.setValue(data.getLocation());
		bankAccountNumberItem.setValue(data.getBankAccountNumber());
		bankBranchItem.setValue(data.getBranch());
		bankNameItem.setValue(data.getBankName());
		passportNumberItem.setValue(data.getPassportNumber());
		passportExpiryDateItem.setValue(new ClientFinanceDate(data
				.getPassportExpiryDate()));
		countryOfIssueItem.setValue(data.getCountryOfIssue());
		emplVisaNumberItem.setValue(data.getVisaNumber());
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

	public ClientAddress getAddresss() {
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
			return a;
			// toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return null;
	}

	private void createControls() {
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

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

		nameItem = new TextItem(messages.name(), "nameItem");

		dateOfBirthItem = new DateItem(messages.dateofBirth(),
				"dateOfBirthItem");

		genderSelect = new SelectCombo(messages.gender());
		listOfgenders = new ArrayList<String>();
		for (int i = 0; i < genderTypes.length; i++) {
			listOfgenders.add(genderTypes[i]);
		}
		genderSelect.initCombo(listOfgenders);

		contactNumberItem = new TextItem(messages.contactNumber(),
				"contactNumberItem");
		emailItem = new TextItem(messages.email(), "emailItem");

		addrArea = new TextAreaItem(messages.address(), "addrArea");
		addrArea.setDisabled(isInViewMode());
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

		basicInfoForm.add(nameItem, dateOfBirthItem, genderSelect,
				contactNumberItem, emailItem, addrArea);

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

		data.setAddress(getAddresss());
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
