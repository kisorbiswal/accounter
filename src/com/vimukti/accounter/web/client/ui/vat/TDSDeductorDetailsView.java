package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSDeductorDetailsView extends BaseView<ClientTDSDeductorMasters> {

	private IntegerRangeValidator integerRangeValidator;
	private TextItem deductorName;
	private TextItem branchName;
	private TextItem flatNo;
	private TextItem buildingName;
	private TextItem streetName;
	private TextItem areaName;
	private TextItem cityName;
	private IntegerField pinNumber;
	private IntegerField telephoneNumber;
	private IntegerField faxNumber;
	private SelectCombo addressChangeCombo;
	private DynamicForm taxDynamicForm;
	private EmailField email;
	private DynamicForm otherDynamicForm;
	private SelectCombo statusCombo;
	private SelectCombo deductorTypeOther;
	private SelectCombo govtState;
	private IntegerField paoCode;
	private IntegerField paoRegistration;
	private IntegerField ddoCode;
	private IntegerField ddoRegistration;
	private SelectCombo ministryCombo;
	private TextItem ministryNameOtehr;
	private SelectCombo deductorTypeGovernment;
	protected String stateSelected;
	private boolean true_falseValue;
	protected String statusSelected;
	private String deductorTypeSelected;

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	public void initData() {
		super.initData();
		if (data == null) {
			ClientTDSDeductorMasters deductorMasterDetails = new ClientTDSDeductorMasters();
			setData(deductorMasterDetails);
		}

	}

	private void createControls() {

		integerRangeValidator = new IntegerRangeValidator();
		integerRangeValidator.setMin(0);

		deductorName = new TextItem("Name");
		deductorName.setHelpInformation(true);
		deductorName.setRequired(true);
		deductorName.setDisabled(isInViewMode());

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

		streetName = new TextItem("Street/Road Name");
		streetName.setHelpInformation(true);
		streetName.setDisabled(isInViewMode());

		areaName = new TextItem("Area");
		areaName.setHelpInformation(true);
		areaName.setDisabled(isInViewMode());

		cityName = new TextItem("City/Town/District");
		cityName.setHelpInformation(true);
		cityName.setDisabled(isInViewMode());

		// stateCombo = new SelectCombo("State");
		// stateCombo.setHelpInformation(true);
		// stateCombo.initCombo(getStatesList());
		// stateCombo.setSelectedItem(0);
		// stateCombo.setDisabled(isInViewMode());
		// stateCombo.setRequired(true);
		// stateCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		// stateSelected = selectItem;
		// }
		// });

		pinNumber = new IntegerField(this, "Pin Code");
		pinNumber.setHelpInformation(true);
		pinNumber.setDisabled(isInViewMode());
		pinNumber.setRequired(true);
		pinNumber.setValidators(integerRangeValidator);

		addressChangeCombo = new SelectCombo(
				"Has Address changed since last return");
		addressChangeCombo.setHelpInformation(true);
		addressChangeCombo.initCombo(getYESNOList());
		addressChangeCombo.setSelectedItem(0);
		addressChangeCombo.setDisabled(isInViewMode());
		addressChangeCombo.setRequired(true);
		addressChangeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(getYESNOList().get(1))) {
							true_falseValue = true;
						} else {
							true_falseValue = false;
						}
					}
				});

		telephoneNumber = new IntegerField(this, "Telephone No.");
		telephoneNumber.setHelpInformation(true);
		telephoneNumber.setDisabled(isInViewMode());
		telephoneNumber.setValidators(integerRangeValidator);

		faxNumber = new IntegerField(this, "Fax No.");
		faxNumber.setHelpInformation(true);
		faxNumber.setDisabled(isInViewMode());
		faxNumber.setValidators(integerRangeValidator);

		email = new EmailField("Email");
		email.setHelpInformation(true);
		email.setDisabled(isInViewMode());
		taxDynamicForm = new DynamicForm();
		taxDynamicForm.setFields(deductorName, branchName, flatNo,
				buildingName, streetName, areaName, cityName, pinNumber,
				addressChangeCombo, telephoneNumber, faxNumber, email);

		statusCombo = new SelectCombo("Status");
		statusCombo.setHelpInformation(true);
		statusCombo.initCombo(getStatusTypes());
		statusCombo.setSelectedItem(0);
		statusCombo.setDisabled(isInViewMode());
		statusCombo.setRequired(true);
		statusCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						statusSelected = selectItem;

						if (statusSelected.equals(getStatusTypes().get(1))) {
							deductorTypeOther.show();
							deductorTypeGovernment.hide();

							paoCode.setDisabled(true);
							paoRegistration.setDisabled(true);
							ddoCode.setDisabled(true);
							ddoRegistration.setDisabled(true);
							ministryCombo.setDisabled(true);
							ministryNameOtehr.setDisabled(true);

						} else {
							deductorTypeOther.hide();
							deductorTypeGovernment.show();

							paoCode.setDisabled(false);
							paoRegistration.setDisabled(false);
							ddoCode.setDisabled(false);
							ddoRegistration.setDisabled(false);
							ministryCombo.setDisabled(false);
							ministryNameOtehr.setDisabled(false);

						}
					}
				});

		deductorTypeOther = new SelectCombo("Deductor Type");
		deductorTypeOther.setHelpInformation(true);
		deductorTypeOther.initCombo(getOthersList());
		deductorTypeOther.setSelectedItem(0);
		deductorTypeOther.setDisabled(isInViewMode());
		deductorTypeOther.setRequired(true);
		deductorTypeOther
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						deductorTypeSelected = selectItem;
					}
				});

		deductorTypeGovernment = new SelectCombo("Deductor Type");
		deductorTypeGovernment.setHelpInformation(true);
		deductorTypeGovernment.initCombo(getGovtList());
		deductorTypeGovernment.setSelectedItem(0);
		deductorTypeGovernment.setDisabled(isInViewMode());
		deductorTypeGovernment.setRequired(true);
		deductorTypeGovernment
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						deductorTypeSelected = selectItem;
					}
				});

		govtState = new SelectCombo("State");
		govtState.setHelpInformation(true);
		govtState.initCombo(getStatesList());
		govtState.setSelectedItem(0);
		govtState.setDisabled(isInViewMode());
		govtState.setRequired(true);
		govtState
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						stateSelected = selectItem;
					}
				});

		paoCode = new IntegerField(this, "PAO Code");
		paoCode.setHelpInformation(true);
		paoCode.setDisabled(isInViewMode());
		paoCode.setValidators(integerRangeValidator);

		paoRegistration = new IntegerField(this, "PAO Registration No.");
		paoRegistration.setHelpInformation(true);
		paoRegistration.setDisabled(isInViewMode());
		paoRegistration.setValidators(integerRangeValidator);

		ddoCode = new IntegerField(this, "DDO Code");
		ddoCode.setHelpInformation(true);
		ddoCode.setDisabled(isInViewMode());
		ddoCode.setValidators(integerRangeValidator);

		ddoRegistration = new IntegerField(this, "DDO Registration No.");
		ddoRegistration.setHelpInformation(true);
		ddoRegistration.setDisabled(isInViewMode());
		ddoRegistration.setValidators(integerRangeValidator);

		ministryCombo = new SelectCombo("Ministry/Dept. Name");
		ministryCombo.setHelpInformation(true);
		ministryCombo.initCombo(getMinistryType());
		ministryCombo.setSelectedItem(0);
		ministryCombo.setDisabled(isInViewMode());
		ministryCombo.setRequired(true);
		ministryCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		ministryNameOtehr = new TextItem("Ministry/Dept. Name(Other)");
		ministryNameOtehr.setHelpInformation(true);
		ministryNameOtehr.setDisabled(isInViewMode());

		otherDynamicForm = new DynamicForm();
		otherDynamicForm.setFields(statusCombo, deductorTypeOther,
				deductorTypeGovernment, govtState, paoCode, paoRegistration,
				ddoCode, ddoRegistration, ministryCombo, ministryNameOtehr);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.add(taxDynamicForm);
		horizontalPanel.add(otherDynamicForm);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(horizontalPanel);

		this.add(verticalPanel);

		deductorTypeOther.hide();
		deductorTypeGovernment.hide();

		if (data != null) {

			deductorName.setValue(data.getDeductorName());
			branchName.setValue(data.getBranch());
			flatNo.setValue(data.getFlatNo());
			buildingName.setValue(data.getBuildingName());
			streetName.setValue(data.getRoadName());
			areaName.setValue(data.getArea());
			cityName.setValue(data.getCity());
			// stateCombo.setValue(data.getState());
			pinNumber.setValue(Long.toString(data.getPinCode()));
			telephoneNumber.setValue(Long.toString(data.getTelephoneNumber()));
			faxNumber.setValue(Long.toString(data.getFaxNo()));
			addressChangeCombo.setValue(data.isAddressdChanged());
			email.setValue(data.getEmailID());

			statusCombo.setValue(data.getStatus());

			govtState.setValue(data.getGovtState());
			paoCode.setValue(Long.toString(data.getPaoCode()));
			paoRegistration.setValue(Long.toString(data.getPaoRegistration()));
			ddoCode.setValue(Long.toString(data.getDdoCode()));
			ddoRegistration.setValue(Long.toString(data.getDdoRegistration()));
			ministryCombo.setValue(data.getMinistryDeptName());
			ministryNameOtehr.setValue(data.getMinistryDeptOtherName());

			if (data.getStatus().equals(getStatusTypes().get(0))) {
				deductorTypeOther.setValue(data.getDeductorType());
				deductorTypeGovernment.setSelectedItem(0);
			} else {
				deductorTypeGovernment.setValue(data.getDeductorType());
				deductorTypeOther.setSelectedItem(0);
			}

		} else {
			ClientCompany company = getCompany();
			deductorName.setValue(company.getName());
			cityName.setValue(company.getRegisteredAddress().getCity());
			streetName.setValue(company.getRegisteredAddress().getStreet());
			areaName.setValue(company.getRegisteredAddress()
					.getCountryOrRegion());
			govtState.setValue(company.getRegisteredAddress()
					.getStateOrProvinence());
			pinNumber.setValue(company.getRegisteredAddress()
					.getZipOrPostalCode());
			telephoneNumber.setValue(company.getPhone());
			faxNumber.setValue(company.getFax());
		}

	}

	private List<String> getMinistryType() {
		List<String> names = new ArrayList<String>();
		names.add("Select");
		names.add("Ministry name");
		names.add("Agriculture");
		names.add("Atomic Energy");
		names.add("Fertilizers");
		names.add("Chemicals and Petrochemicals");
		names.add("Civil Aviation and Tourism");
		names.add("Coal");
		names.add("Consumer Affairs, Food and Public Distribution");
		names.add("Commerce and Textiles");
		names.add("Environment and Forests and Ministry of Earth Science");
		names.add("External Affairs and Overseas Indian Affairs");
		names.add("Finance");
		names.add("Central Board of Direct Taxes");
		names.add("Central Board of Excise and Customs");
		names.add("Contoller of Aid Accounts and Audit");
		names.add("Central Pension Accounting Office");
		names.add("Food Processing Industries");
		names.add("Health and Family Welfare");
		names.add("Home Affairs and Development of North Eastern Region");
		names.add("Human Resource Development");
		names.add("Industry");
		names.add("Information and Broadcasting");
		names.add("Telecommunication and Information Technology");
		names.add("Labour");
		names.add("Law and Justice and Company Affairs");
		names.add("Personnel, Public Grievances and Pensions");
		names.add("Petroleum and Natural Gas");
		names.add("Plannning, Statistics and Programme Implementation");
		names.add("Power");
		names.add("New and Renewable Energy");
		names.add("Rural Development and Panchayati Raj");
		names.add("Science And Technology");
		names.add("Space");
		names.add("Steel");
		names.add("Mines");
		names.add("Social Justice and Empowerment");
		names.add("Tribal Affairs");
		names.add("D/o Commerce (Supply Division)");
		names.add("Shipping and Road Transport and Highways");
		names.add("Urban Development, Urban Employment and Poverty Alleviation");
		names.add("Water Resources");
		names.add("President's Secretariat");
		names.add("Lok Sabha Secretariat");
		names.add("Rajya Sabha secretariat");
		names.add("Election Commission");
		names.add("Ministry of Defence (Controller General of Defence Accounts)");
		names.add("Ministry of Railways");
		names.add("Department of Posts");
		names.add("Department of Telecommunications");
		names.add("Andaman and Nicobar Islands Administration ");
		names.add("Chandigarh Administration");
		names.add("Dadra and Nagar Haveli");
		names.add("Goa, Daman and Diu");
		names.add("Lakshadweep");
		names.add("Pondicherry Administration");
		names.add("Pay and Accounts Officers (Audit)");
		names.add("Ministry of Non-conventional energy sources");
		names.add("Government Of NCT of Delhi ");
		names.add("Others");

		return names;
	}

	private List<String> getOthersList() {

		List<String> names = new ArrayList<String>();
		names.add("Select");
		names.add("Company");
		names.add("Branch/Divison of Company");
		names.add("Association of Person (AOP)");
		names.add("Association of Person (Trust)");
		names.add("Artificial Juridicial Person");
		names.add("Body of Indivisuals");
		names.add("Individual/HUF");
		names.add("Firm");

		return names;
	}

	private List<String> getGovtList() {

		List<String> names = new ArrayList<String>();
		names.add("Select");
		names.add("Central Government");
		names.add("State Government");
		names.add("Statutory body (Central Govt.)");
		names.add("Statutory body (State Govt.)");
		names.add("Autonomous body (Central Govt.)");
		names.add("Autonomous body (State Govt.)");
		names.add("Local Authority (Central Govt.)");
		names.add("Local Authority (State Govt.)");

		return names;
	}

	private List<String> getStatusTypes() {
		List<String> names = new ArrayList<String>();
		names.add("Select");
		names.add("Others");
		names.add("Government");
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
		return "Particular of Deductor";
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

		if (stateSelected == null) {
			result.addError(govtState, "Select a state");
		}
		if (deductorTypeSelected == null) {
			result.addError(statusCombo, "Select a Deductor Type");
		}
		if (statusSelected == null) {
			result.addError(statusCombo, "Select the status of Deductor");
		}
		return result;

	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(getData());

	}

	private void updateObject() {

		data.setDeductorName(deductorName.getValue());

		data.setBranch(branchName.getValue());

		data.setFlatNo(flatNo.getValue());

		data.setBuildingName(buildingName.getValue());

		data.setRoadName(streetName.getValue());

		data.setArea(areaName.getValue());

		data.setCity(cityName.getValue());

		data.setState(stateSelected);

		data.setPinCode(pinNumber.getNumber());

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

		data.setAddressdChanged(true_falseValue);

		data.setEmailID(email.getValue());

		data.setStatus(statusSelected);

		data.setDeductorType(deductorTypeSelected);

		data.setGovtState(stateSelected);

		if (paoCode.getValue().length() > 0) {
			data.setPaoCode(paoCode.getNumber());
		} else {
			data.setPaoCode(0);
		}

		if (paoRegistration.getValue().length() > 0) {
			data.setPaoRegistration(paoRegistration.getNumber());
		} else {
			data.setPaoRegistration(0);
		}

		if (ddoCode.getValue().length() > 0) {
			data.setDdoCode(ddoCode.getNumber());
		} else {
			data.setDdoCode(0);
		}

		if (ddoRegistration.getValue().length() > 0) {
			data.setDdoRegistration(ddoRegistration.getNumber());
		} else {
			data.setDdoRegistration(0);
		}

		data.setMinistryDeptName(ministryCombo.getSelectedValue());

		data.setMinistryDeptOtherName(ministryNameOtehr.getValue());

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

		
		Accounter.createHomeService().getDeductorMasterDetails(new AccounterAsyncCallback<ArrayList<ClientTDSDeductorMasters>>() {

			@Override
			public void onException(AccounterException exception) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onResultSuccess(
					ArrayList<ClientTDSDeductorMasters> result) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}	

}
