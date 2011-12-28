package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSResponsiblePersonDetailsView extends
		BaseView<ClientTDSResponsiblePerson> {

	private IntegerRangeValidator integerRangeValidator;
	private TextItem responsiblePersonName;
	private TextItem branchName;
	private TextItem flatNo;
	private TextItem buildingName;
	private TextItem streetName;
	private TextItem areaName;
	private TextItem cityName;
	private SelectCombo stateCombo;
	private IntegerField pinNumber;
	private IntegerField telephoneNumber;
	private IntegerField faxNumber;
	private SelectCombo addressChangeCombo;
	private DynamicForm taxDynamicForm;
	private EmailField email;
	private DynamicForm otherDynamicForm;
	private SelectCombo financialYearCombo;
	private SelectCombo returnType;
	private SelectCombo existingTdsassess;
	private IntegerField panCode;
	private IntegerField tanRegistration;

	private SelectCombo assessmentYearCombo;
	private TextItem designation;
	private IntegerField stdNumber;
	private IntegerField mobileNumber;

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	public void initData() {
		super.initData();
		if (data == null) {
			ClientTDSResponsiblePerson personDetails = new ClientTDSResponsiblePerson();
			setData(personDetails);
		}

	}

	private void createControls() {

		integerRangeValidator = new IntegerRangeValidator();
		integerRangeValidator.setMin(0);

		responsiblePersonName = new TextItem("Name");
		responsiblePersonName.setHelpInformation(true);
		responsiblePersonName.setRequired(true);
		responsiblePersonName.setDisabled(isInViewMode());

		designation = new TextItem("Designation");
		designation.setHelpInformation(true);
		designation.setRequired(true);
		designation.setDisabled(isInViewMode());

		branchName = new TextItem("Branch/Division");
		branchName.setHelpInformation(true);
		branchName.setDisabled(isInViewMode());

		flatNo = new TextItem("Flat No.");
		flatNo.setHelpInformation(true);
		flatNo.setRequired(true);
		flatNo.setDisabled(isInViewMode());

		buildingName = new TextItem("Name of Premisis/Building");
		buildingName.setHelpInformation(true);
		buildingName.setDisabled(isInViewMode());

		streetName = new TextItem("Street/Road/lane");
		streetName.setHelpInformation(true);
		streetName.setDisabled(isInViewMode());

		areaName = new TextItem("Area/Location");
		areaName.setHelpInformation(true);
		areaName.setDisabled(isInViewMode());

		cityName = new TextItem("City/Town/District	");
		cityName.setHelpInformation(true);
		cityName.setRequired(true);
		cityName.setDisabled(isInViewMode());

		stateCombo = new SelectCombo("State Name");
		stateCombo.setHelpInformation(true);
		stateCombo.initCombo(getStatesList());
		stateCombo.setSelectedItem(0);
		stateCombo.setDisabled(isInViewMode());
		stateCombo.setRequired(true);
		stateCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		pinNumber = new IntegerField(this, "Pin Code");
		pinNumber.setHelpInformation(true);
		pinNumber.setRequired(true);
		pinNumber.setDisabled(isInViewMode());
		pinNumber.setValidators(integerRangeValidator);

		addressChangeCombo = new SelectCombo(
				"Has Address changed since last return");
		addressChangeCombo.setHelpInformation(true);
		addressChangeCombo.initCombo(getYESNOList());
		addressChangeCombo.setDisabled(isInViewMode());
		addressChangeCombo.setRequired(true);
		addressChangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		stdNumber = new IntegerField(this, "Std Code");
		stdNumber.setHelpInformation(true);
		stdNumber.setDisabled(isInViewMode());
		stdNumber.setValidators(integerRangeValidator);

		telephoneNumber = new IntegerField(this, "Telephone No.");
		telephoneNumber.setHelpInformation(true);
		telephoneNumber.setDisabled(isInViewMode());
		telephoneNumber.setValidators(integerRangeValidator);

		mobileNumber = new IntegerField(this, "Mobile No.");
		mobileNumber.setHelpInformation(true);
		mobileNumber.setRequired(true);
		mobileNumber.setDisabled(isInViewMode());
		mobileNumber.setValidators(integerRangeValidator);

		faxNumber = new IntegerField(this, "Fax No.");
		faxNumber.setHelpInformation(true);
		faxNumber.setDisabled(isInViewMode());
		faxNumber.setValidators(integerRangeValidator);

		email = new EmailField("Email");
		email.setHelpInformation(true);
		email.setDisabled(isInViewMode());

		taxDynamicForm = new DynamicForm();
		taxDynamicForm.setFields(responsiblePersonName, designation,
				branchName, flatNo, buildingName, streetName, areaName,
				cityName, stateCombo, pinNumber, addressChangeCombo);

		financialYearCombo = new SelectCombo("Financial Year");
		financialYearCombo.setHelpInformation(true);
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setDisabled(isInViewMode());
		financialYearCombo.setRequired(true);
		financialYearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						assessmentYearCombo.setSelected(getFinancialYearList()
								.get(financialYearCombo.getSelectedIndex() + 1));
					}
				});

		assessmentYearCombo = new SelectCombo("Assessment Year");
		assessmentYearCombo.setHelpInformation(true);
		assessmentYearCombo.initCombo(getFinancialYearList());
		assessmentYearCombo.setDisabled(true);

		returnType = new SelectCombo("Return Type");
		returnType.setHelpInformation(true);
		returnType.initCombo(getReturnTypeList());
		returnType.setDisabled(isInViewMode());
		returnType.setSelectedItem(0);
		returnType.setRequired(true);
		returnType
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		existingTdsassess = new SelectCombo("Existing TDS assesse");
		existingTdsassess.setHelpInformation(true);
		existingTdsassess.initCombo(getYESNOList());
		existingTdsassess.setSelectedItem(0);
		existingTdsassess.setDisabled(isInViewMode());
		existingTdsassess.setRequired(true);
		existingTdsassess
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		panCode = new IntegerField(this, "PAN");
		panCode.setHelpInformation(true);
		panCode.setRequired(true);
		panCode.setDisabled(isInViewMode());
		panCode.setValidators(integerRangeValidator);

		tanRegistration = new IntegerField(this, "TAN Registration");
		tanRegistration.setHelpInformation(true);
		tanRegistration.setRequired(true);
		tanRegistration.setDisabled(isInViewMode());
		tanRegistration.setValidators(integerRangeValidator);

		otherDynamicForm = new DynamicForm();
		otherDynamicForm.setFields(stdNumber, telephoneNumber, mobileNumber,
				faxNumber, email, financialYearCombo, assessmentYearCombo,
				returnType, existingTdsassess, panCode, tanRegistration);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.add(taxDynamicForm);
		horizontalPanel.add(otherDynamicForm);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(horizontalPanel);

		this.add(verticalPanel);

	}

	private List<String> getReturnTypeList() {
		List<String> names = new ArrayList<String>();
		names.add("Select");
		names.add("Electronic");
		names.add("Digital");
		return names;
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
		return "Particular for Person Responsible for Tax Deduction";
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(taxDynamicForm.validate());
		result.add(otherDynamicForm.validate());

		if (!UIUtils.isValidEmail(email.getValue())) {
			result.addError(email, "Invalid email id.");
		}
		return result;

	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(getData());

	}

	private void updateObject() {

		data.setResponsibleName(responsiblePersonName.getValue());

		data.setBranch(branchName.getValue());

		data.setFlatNo(flatNo.getValue());

		data.setBuildingName(buildingName.getValue());

		data.setStreet(streetName.getValue());

		data.setArea(areaName.getValue());

		data.setCity(cityName.getValue());

		data.setStateName(stateCombo.getSelectedValue());

		data.setPinCode(pinNumber.getNumber());

		if (addressChangeCombo.getSelectedValue().equals("YES")) {
			data.setAddressChanged(true);
		} else {
			data.setAddressChanged(false);
		}
		if (telephoneNumber.getValue().length() > 0) {
			data.setTelephoneNumber(telephoneNumber.getNumber());
		} else {
			data.setTelephoneNumber(0);
		}

		if (faxNumber.getValue().length() > 0) {
			data.setFaxNo(faxNumber.getNumber());
		} else {
			data.setFaxNo(0);
		}

		data.setEmailAddress(email.getValue());

		data.setFinancialYear(financialYearCombo.getSelectedValue());

		data.setAssesmentYear(assessmentYearCombo.getSelectedValue());

		data.setPanNumber(panCode.getNumber());

		data.setPanRegistrationNumber(tanRegistration.getNumber());

		data.setMobileNumber(mobileNumber.getNumber());

		if (stdNumber.getValue().length() > 0) {
			data.setStdCode(stdNumber.getNumber());
		} else {
			data.setStdCode(0);
		}

		// private int returnType;
		// private boolean existingTDSassesse;

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateObject();

	}

	@Override
	protected void initRPCService() {
		super.initRPCService();

		Accounter
				.createHomeService()
				.getResponsiblePersonDetails(
						new AccounterAsyncCallback<ArrayList<ClientTDSResponsiblePerson>>() {

							@Override
							public void onException(AccounterException exception) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onResultSuccess(
									ArrayList<ClientTDSResponsiblePerson> result) {
								// TODO Auto-generated method stub

							}
						});

	}

}
