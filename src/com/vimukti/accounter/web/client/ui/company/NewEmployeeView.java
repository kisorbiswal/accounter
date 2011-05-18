package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientEmployeeDetails;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class NewEmployeeView extends BaseView<ClientEmployeeDetails> {

	private VerticalPanel mainPanel;
	private DecoratedTabPanel detailsTabPanel;
	private VerticalPanel employeePanel;
	private TextItem employeeDetailsText;
	private TextItem firstNameText;
	private TextItem lastNameText;
	private TextItem employeeNumberText;
	private TextItem jobTitleText;
	private SelectCombo titleListCombo;
	private SelectCombo employeeStatusListCombo;
	private SelectCombo employeeTypeListCombo;
	private TextItem managerNameText;
	private VerticalPanel contactDetailsTab;
	private TextItem titleText;
	private TextItem employeeTypeText;
	private ClientEmployeeDetails employee;
	private boolean isEdit;
	private TextItem homeAddress1;
	private TextItem homeAddress2;
	private TextItem homeCity;
	private TextItem homeZip;
	private TextItem homeState;
	private TextItem homeCountry;
	private TextItem homePhone;
	private TextItem mobilePhone;
	private SelectCombo fulltimePartTimeList;
	private DateField employeeLeaveDate;
	private DateField employeeStartDate;
	private TextItem fulltimePartTimeText;
	private DateField employeStatusDate;
	private DynamicForm leftDynamicForm;
	private DynamicForm rightDynamicForm;
	private HorizontalPanel mainHlayout;
	private HorizontalPanel contactDetailsForm;
	private DynamicForm leftContactDetailsForm;
	private DynamicForm rightContactDetailsForm;
	private TextItem homeEmail;
	private String employeeNextNumber;
	private List<ClientEmployeeDetails> listofAllEmployees;
	private ValueCallBack<ClientEmployeeDetails> valueCallback;
	private HTML savedContactDetailsStatusLabel;
	private HTML savedEmpDetailsStatusLabel;
	private static final String OTHER = "Other";
	Date currentDate = new Date();

	public NewEmployeeView() {
		super();
	}

	public void NewEmployeeView(ClientEmployeeDetails employee,
			ValueCallBack<ClientEmployeeDetails> valueCallback, boolean isEdit) {
		this.employee = employee;
		this.isEdit = isEdit;
		// hrService = BizantraClient.getHrService();
		// listofAllEmployees = HrManagmentView.getListofAllEmployees();
		this.valueCallback = valueCallback;
		if (employee != null) {
			initEmployeeDetails();
			initEmployeeContactDetails();
		}
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	/**
	 * This method will initializes the employee contact details controls .
	 */
	private void initEmployeeContactDetails() {
		if (employee != null) {
			homeAddress1.setValue(employee.getAddress1());
			homeAddress2.setValue(employee.getAddress2());
			homeCity.setValue(employee.getCity());
			homeZip.setValue(employee.getPostalCode());
			homeState.setValue(employee.getState());
			homeCountry.setValue(employee.getCountry());
			homePhone.setValue(employee.getHomePhone());
			mobilePhone.setValue(employee.getMobilePhone());
			homeEmail.setValue(employee.getHomeMailID());
		}
		homeAddress1.setDisabled(!isEdit);
		homeAddress2.setDisabled(!isEdit);
		homeCity.setDisabled(!isEdit);
		homeZip.setDisabled(!isEdit);
		homeState.setDisabled(!isEdit);
		homeCountry.setDisabled(!isEdit);
		homePhone.setDisabled(!isEdit);
		mobilePhone.setDisabled(!isEdit);
		homeEmail.setDisabled(!isEdit);
	}

	/**
	 * This method will initialise the data to the employee detail form controls
	 * while editing , view details .
	 */
	private void initEmployeeDetails() {

		initiateLeftSideFormData();
		initiateRightSideFormData();

	}

	private void initiateRightSideFormData() {
		employeeNumberText.setValue(employee.getEmployeeNumber());
		this.employeeNextNumber = employee.getEmployeeNumber();
		employeeNumberText.setDisabled(false);
		if (employee.getEmployeeType().equalsIgnoreCase("")) {
			employeeTypeListCombo.setSelectedItem(0);
		} else if (!employee.getEmployeeType().equalsIgnoreCase(
				AbstractActionFactory.actionsConstants.permanent())
				&& !employee.getEmployeeType().equalsIgnoreCase(
						AbstractActionFactory.actionsConstants.temporary())
				&& !employee.getEmployeeType().equalsIgnoreCase(
						AbstractActionFactory.actionsConstants.placement())) {
			employeeTypeListCombo
					.setValue(AbstractActionFactory.actionsConstants.other());
			employeeTypeText.setValue(employee.getEmployeeType());
			employeeTypeText.setVisible(true);
		} else {
			employeeTypeListCombo.setValue(employee.getEmployeeType());
		}
		employeeTypeText.setDisabled(!isEdit);
		if (employee.getTitle() != null && !employee.getTitle().equals("")) {
			boolean isExistedTitle = false;

			String[] title = { "Mr", "Miss", "Ms", "Mrs", "Other" };
			for (String string : title) {
				if (employee.getTitle().equalsIgnoreCase(string)) {
					isExistedTitle = true;
					titleListCombo.setValue(employee.getTitle());
					break;
				}
			}
			if (!isExistedTitle) {
				titleListCombo.setValue("Other");
				titleText.setValue(employee.getTitle());
				titleText.setVisible(true);
			}
		}
		titleListCombo.setDisabled(!isEdit);
		if (isEdit) {
			titleListCombo.setFocus();
		}
		if (employee.getEmployeeStatus().equals(
				ClientEmployeeDetails.PAST_EMPLOYEE)) {
			employeeStatusListCombo
					.setValue(AbstractActionFactory.actionsConstants
							.pastEmployee());
		}
		if (employee.getEmployeeStatus().equals(
				ClientEmployeeDetails.FUTURE_EMPLOYEE)) {
			employeeStatusListCombo
					.setValue(AbstractActionFactory.actionsConstants
							.futureEmployee());
		}

		if (employee.getEmployeeStatus().equals(
				ClientEmployeeDetails.CURRENT_EMPLOYEE)) {
			employeeStatusListCombo
					.setValue(AbstractActionFactory.actionsConstants
							.currentEmployee());

		}

		employeeStatusListCombo.setDisabled(true);
		employeStatusDate.setDisabled(!isEdit);
		if (employee.getEmployeeType().equalsIgnoreCase(""))
			employeeTypeListCombo.setValue(employee.getEmployeeType());
		employeeTypeListCombo.setDisabled(!isEdit);
		if (!employee.natureOfJob
				.equalsIgnoreCase(AbstractActionFactory.actionsConstants
						.fullTime())
				&& !employee.natureOfJob
						.equalsIgnoreCase(AbstractActionFactory.actionsConstants
								.partTime())) {
			fulltimePartTimeList
					.setValue(AbstractActionFactory.actionsConstants.other());
			fulltimePartTimeText.setValue(employee.natureOfJob);
			fulltimePartTimeText.setVisible(true);
		} else {
			fulltimePartTimeList.setValue(employee.natureOfJob);
		}
		fulltimePartTimeList.setDisabled(!isEdit);
		fulltimePartTimeText.setDisabled(!isEdit);
		managerNameText.setValue(employee.getManagerName());
		managerNameText.setDisabled(!isEdit);
	}

	private void initiateLeftSideFormData() {
		employeeDetailsText.setValue(employee.getEmployeeName());
		firstNameText.setValue(employee.getFirstName());
		firstNameText.setDisabled(!isEdit);
		lastNameText.setValue(employee.getLastName());
		lastNameText.setDisabled(!isEdit);
		if (employee.getJobTitle() != null) {
			jobTitleText.setValue(employee.getJobTitle().trim());
		}
		jobTitleText.setDisabled(!isEdit);
		if (employee.getStartDate() != null) {
			employeeStartDate.setValue(employee.getStartDate());
		}
		employeeStartDate.setDisabled(!isEdit);
		if (employee.getEndDate() != null) {
			employeeLeaveDate.setValue(employee.getEndDate());
		}
		employeeLeaveDate.setDisabled(!isEdit);

	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "100%");
		employeePanel = new VerticalPanel();
		detailsTabPanel = new DecoratedTabPanel();
		detailsTabPanel.addStyleName("employeeDetailsTab");
		DeckPanel deck = detailsTabPanel.getDeckPanel();
		employeePanel.add(getEmployeDetails());
		employeePanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contactDetailsTab = new VerticalPanel();
		VerticalPanel contactDetailsVpanel = createContactDetailsForm();
		contactDetailsTab.add(contactDetailsVpanel);
		contactDetailsTab.setCellHorizontalAlignment(contactDetailsVpanel,
				HasHorizontalAlignment.ALIGN_CENTER);
		contactDetailsTab.setCellVerticalAlignment(contactDetailsVpanel,
				HasVerticalAlignment.ALIGN_MIDDLE);
		detailsTabPanel.add(employeePanel,
				AbstractActionFactory.actionsConstants.employeeDetails());
		detailsTabPanel.add(contactDetailsTab,
				AbstractActionFactory.actionsConstants.contactDetails());

		detailsTabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem() == 0) {
					titleListCombo.setFocus();
				} else {
					homeAddress1.setFocus();
				}
			}
		});

		detailsTabPanel.selectTab(0);
		mainPanel.add(detailsTabPanel);
		mainPanel.setCellHorizontalAlignment(detailsTabPanel,
				HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellVerticalAlignment(detailsTabPanel,
				HasVerticalAlignment.ALIGN_MIDDLE);
		canvas.add(mainPanel);
		canvas.getParent().removeStyleName("main-class-pannel");
		detailsTabPanel.setSize("100%", "100%");
		// canvas.setWidth("975px");
	}

	/**
	 * This method will create contact details form controls for the employee
	 * details tab in the employee page dialog .
	 * 
	 * @return DynamicForm
	 */
	private VerticalPanel createContactDetailsForm() {
		HTML homeAddress = new HTML();
		homeAddress.setHTML(AbstractActionFactory.actionsConstants
				.homeAddress());
		homeAddress1 = new TextItem();
		homeAddress1
				.setTitle(AbstractActionFactory.actionsConstants.address1());
		homeAddress1.setName(AbstractActionFactory.actionsConstants
				.homeAddress1());
		homeAddress2 = new TextItem();
		homeAddress2
				.setTitle(AbstractActionFactory.actionsConstants.address2());
		homeCity = new TextItem();
		homeCity.setTitle(AbstractActionFactory.actionsConstants.city());
		homeZip = new TextItem();
		homeZip.setTitle(AbstractActionFactory.actionsConstants.zipcode());
		homeState = new TextItem();
		homeState.setTitle(AbstractActionFactory.actionsConstants.provience());
		homeCountry = new TextItem();
		homeCountry.setTitle(AbstractActionFactory.actionsConstants.country());
		homePhone = new TextItem();
		homePhone.setTitle(AbstractActionFactory.actionsConstants.homePhone());
		mobilePhone = new TextItem();
		mobilePhone.setTitle(AbstractActionFactory.actionsConstants
				.mobilePhone());
		homeEmail = new TextItem();
		homeEmail.setTitle(AbstractActionFactory.actionsConstants.homeEmail());

		HorizontalPanel homeAddressPanel = new HorizontalPanel();
		homeAddressPanel.add(homeAddress);
		leftContactDetailsForm = new DynamicForm();
		leftContactDetailsForm.setFields(homeAddress1, homeAddress2, homeCity,
				homeState, homeCountry, homeZip);
		rightContactDetailsForm = new DynamicForm();
		rightContactDetailsForm.setFields(homePhone, mobilePhone, homeEmail);
		homeAddress1.setFocus();
		contactDetailsForm = new HorizontalPanel();
		contactDetailsForm.add(leftContactDetailsForm);
		contactDetailsForm.add(rightContactDetailsForm);
		contactDetailsForm.setCellWidth(leftContactDetailsForm, "50%");
		contactDetailsForm.setCellWidth(contactDetailsForm, "50%");
		contactDetailsForm.setCellHorizontalAlignment(leftContactDetailsForm,
				HasHorizontalAlignment.ALIGN_CENTER);
		contactDetailsForm.setCellHorizontalAlignment(rightContactDetailsForm,
				HasHorizontalAlignment.ALIGN_CENTER);
		/*
		 * Button saveButton = new Button();
		 * saveButton.setText(AbstractActionFactory.actionsConstants.save());
		 * saveButton.setWidth("75px"); saveButton.addClickHandler(new
		 * ClickHandler() { AsyncCallback<ClientEmployeeDetails> callback = new
		 * AsyncCallback<ClientEmployeeDetails>() {
		 * 
		 * @Override public void onFailure(Throwable caught) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onSuccess(ClientEmployeeDetails result) { if
		 * (result != null) {
		 * 
		 * if (checkValidation()) {
		 * savedContactDetailsStatusLabel.setVisible(true); }
		 * 
		 * } } };
		 * 
		 * @Override public void onClick(ClickEvent event) {
		 * 
		 * if (!leftContactDetailsForm.validate() ||
		 * !rightContactDetailsForm.validate()) {
		 * SC.say(AbstractActionFactory.actionsConstants .headerInformation(),
		 * AbstractActionFactory.actionsConstants .pleaseEnterRequiredFields());
		 * } else { if (checkValidation()) { employee.employeeContact =
		 * getEmployeeContactDetails();
		 * hrService.modifyEmployeeDetail(employee.employeeId, employee,
		 * callback); } }
		 * 
		 * } });
		 * 
		 * savedContactDetailsStatusLabel = new HTML();
		 * savedContactDetailsStatusLabel
		 * .setHTML(AbstractActionFactory.actionsConstants
		 * .employeeDetailsAreUpdatedSuccesfully
		 * (AbstractActionFactory.actionsConstants .contact()));
		 * savedContactDetailsStatusLabel.setVisible(false);
		 * savedContactDetailsStatusLabel.addStyleName("successMessage");
		 * 
		 * HorizontalPanel buttonPanel = new HorizontalPanel();
		 * buttonPanel.setSpacing(10); buttonPanel.setWidth("100%");
		 * buttonPanel. // *
		 * setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT); //
		 * buttonPanel.add(savedContactDetailsStatusLabel);
		 * buttonPanel.add(saveButton);
		 */
		VerticalPanel mainLayout = new VerticalPanel();
		// mainLayout.setSpacing(10);
		mainLayout.add(homeAddressPanel);
		mainLayout.add(contactDetailsForm);
		// if (employee != null) {
		// mainLayout.add(buttonPanel);
		// }
		return mainLayout;

	}

	/**
	 * This method will create form controls about employee details in the the
	 * employee page dialog .
	 * 
	 * @return HorizontalPanel
	 */
	private VerticalPanel getEmployeDetails() {

		addContentToLeftForm();

		addContentToRightForm();

		leftDynamicForm = new DynamicForm();
		leftDynamicForm.setWidth("100%");
		leftDynamicForm.setCellSpacing(1);
		leftDynamicForm.setStyleName("employee");
		leftDynamicForm.setFields(titleListCombo, titleText, firstNameText,
				lastNameText, employeeNumberText, jobTitleText,
				managerNameText, employeeTypeListCombo, employeeTypeText,
				fulltimePartTimeList, fulltimePartTimeText);
		rightDynamicForm = new DynamicForm();
		rightDynamicForm.setStyleName("employee");
		rightDynamicForm.setWidth("100%");
		rightDynamicForm.setCellPadding(1);
		rightDynamicForm.setFields(employeeStartDate, employeeLeaveDate,
				employeeStatusListCombo, employeStatusDate);

		VerticalPanel leftVPanel = new VerticalPanel();
		leftVPanel.add(leftDynamicForm);
		mainHlayout = new HorizontalPanel();
		mainHlayout.setSize("100%", "100%");
		mainHlayout.add(leftVPanel);
		mainHlayout.add(rightDynamicForm);
		mainHlayout.setCellWidth(leftVPanel, "50%");
		mainHlayout.setCellWidth(rightDynamicForm, "50%");
		mainHlayout.setCellHorizontalAlignment(leftVPanel,
				HasHorizontalAlignment.ALIGN_CENTER);
		mainHlayout.setCellHorizontalAlignment(rightDynamicForm,
				HasHorizontalAlignment.ALIGN_CENTER);
		/*
		 * Button saveButton = new Button();
		 * saveButton.setText(AbstractActionFactory.actionsConstants.save());
		 * saveButton.setWidth("75px"); saveButton.addClickHandler(new
		 * ClickHandler() { AsyncCallback<ClientEmployeeDetails> callback = new
		 * AsyncCallback<ClientEmployeeDetails>() {
		 * 
		 * @Override public void onFailure(Throwable caught) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onSuccess(ClientEmployeeDetails result) { try {
		 * if (result != null) { valueCallback.execute(result);
		 * 
		 * if (checkValidation()) { savedEmpDetailsStatusLabel.setVisible(true);
		 * }
		 * 
		 * }
		 * 
		 * } catch (Exception e) {
		 * 
		 * } } };
		 * 
		 * @Override public void onClick(ClickEvent event) {
		 * 
		 * if (!leftDynamicForm.validate() || !rightDynamicForm.validate()) {
		 * SC.say(AbstractActionFactory.actionsConstants .headerInformation(),
		 * AbstractActionFactory.actionsConstants .pleaseEnterRequiredFields());
		 * } else { checkValidation(); hrService.modifyEmployeeDetail(
		 * employee.getEmployeeNumber(), getEmployeeDetailsObject(), callback);
		 * } } });
		 */
		/*
		 * savedEmpDetailsStatusLabel = new HTML(); savedEmpDetailsStatusLabel
		 * .setHTML(AbstractActionFactory.actionsConstants
		 * .employeeDetailsAreUpdatedSuccesfully
		 * (AbstractActionFactory.actionsConstants .employee()));
		 * savedEmpDetailsStatusLabel.setVisible(false);
		 * savedEmpDetailsStatusLabel.addStyleName("successMessage");
		 */
		// HorizontalPanel buttonPanel = new HorizontalPanel();
		// buttonPanel.setSpacing(0);
		// buttonPanel.setWidth("100%");
		// buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		// // buttonPanel.add(savedEmpDetailsStatusLabel);
		// buttonPanel.add(saveButton);
		VerticalPanel mainLayout = new VerticalPanel();
		// mainLayout.setSpacing(2);
		//
		mainLayout.add(mainHlayout);
		// if (employee != null) {
		// mainLayout.add(buttonPanel);
		// }
		return mainLayout;
	}

	/**
	 * Add the fields to right side form of the employee details panel
	 */
	private void addContentToRightForm() {
		fulltimePartTimeList = new SelectCombo(
				AbstractActionFactory.actionsConstants.fullTimePartTime());
		fulltimePartTimeList.setRequired(true);
		List<String> fulTimeList = new ArrayList<String>();
		fulTimeList.add(AbstractActionFactory.actionsConstants.fullTime());
		fulTimeList.add(AbstractActionFactory.actionsConstants.partTime());
		fulTimeList.add(AbstractActionFactory.actionsConstants.other());
		fulltimePartTimeList.initCombo(fulTimeList);
		fulltimePartTimeText = new TextItem();
		fulltimePartTimeText.setVisible(false);
		addHandlerForFullTimePartTimeText();
		employeeDetailsText = new TextItem();
		employeeDetailsText.setTitle(AbstractActionFactory.actionsConstants
				.employeeName());
		employeeDetailsText.setDisabled(true);
		if (employee == null) {
			// dispatchCallToGetNextEmployeeNumber();
		}

		employeeNumberText.setTitle(AbstractActionFactory.actionsConstants
				.employeeNumber());
		titleText = new TextItem();
		titleText.setVisible(false);
		employeeTypeText = new TextItem();
		employeeTypeText.setVisible(false);

		employeStatusDate = new DateField(
				AbstractActionFactory.actionsConstants.employeeStartDate());
		employeStatusDate.setVisible(false);

		employeeTypeListCombo = new SelectCombo(
				AbstractActionFactory.actionsConstants.employeeType());
		List<String> employeeTypList = new ArrayList<String>();

		employeeTypList.add(AbstractActionFactory.actionsConstants.permanent());
		employeeTypList.add(AbstractActionFactory.actionsConstants.temporary());
		employeeTypList.add(AbstractActionFactory.actionsConstants.placement());
		employeeTypList.add(AbstractActionFactory.actionsConstants.other());
		employeeTypeListCombo
				.setDefaultValue(AbstractActionFactory.actionsConstants
						.select());
		employeeTypeListCombo.initCombo(employeeTypList);
		employeeTypeListCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (employeeTypeListCombo.getSelectedValue().equalsIgnoreCase(
						OTHER)) {
					employeeTypeText.setVisible(true);
					employeeTypeText.setFocus();
				} else {
					employeeTypeText.setVisible(false);
				}
			}
		});
		managerNameText = new TextItem();
		managerNameText.setTitle(AbstractActionFactory.actionsConstants
				.managerName());
		/*
		 * Calendar c = Calendar.getInstance();
		 * 
		 * String holidayStartDate[] = FinanceApplication.getClientIdentity()
		 * .getCompany().holidayStartDate.split(","); c.set(Calendar.YEAR,
		 * c.get(Calendar.YEAR)); c.set(Calendar.MONTH,
		 * Integer.parseInt(holidayStartDate[1].trim())); c.set(Calendar.DATE,
		 * Integer.parseInt(holidayStartDate[0].trim())); c.set(Calendar.HOUR,
		 * 0); c.set(Calendar.MINUTE, 0); c.set(Calendar.SECOND, 0);
		 * 
		 * c.set(Calendar.MILLISECOND, 0);
		 */

	}

	/**
	 * Add the fields to left side form of the employee details panel
	 */
	private void addContentToLeftForm() {
		titleListCombo = new SelectCombo(AbstractActionFactory.actionsConstants
				.title());
		titleListCombo.setFocus();
		List<String> titleList = new ArrayList<String>();
		titleList.add("Mr");
		titleList.add("Miss");
		titleList.add("Ms");
		titleList.add("Mrs");
		titleList.add("Other");
		titleListCombo.initCombo(titleList);

		titleListCombo.setDefaultValue(AbstractActionFactory.actionsConstants
				.select());
		titleListCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (titleListCombo.getSelectedValue().equalsIgnoreCase(
								OTHER)) {
							titleText.setVisible(true);
							titleText.setFocus();
						} else
							titleText.setVisible(false);
					}
				});

		employeeNumberText = new TextItem();
		employeeNumberText.setRequired(true);
		employeeNumberText.setDisabled(false);
		employeeNumberText.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String empNumber = employeeNumberText.getValue().toString();

				if ((employee != null)
						&& (employee.getEmployeeNumber()
								.equalsIgnoreCase(empNumber))) {
					employeeNumberText.setValue((employee.getEmployeeNumber()));
				} else {
					dispatchCallToCheckExistance(empNumber);
				}
			}

		});

		employeeStatusListCombo = new SelectCombo(
				AbstractActionFactory.actionsConstants.employeeStatus());
		employeeStatusListCombo.setRequired(true);
		List<String> employeeStatusList = new ArrayList<String>();
		employeeStatusList.add(AbstractActionFactory.actionsConstants
				.currentEmployee());
		employeeStatusList.add(AbstractActionFactory.actionsConstants
				.futureEmployee());
		employeeStatusList.add(AbstractActionFactory.actionsConstants
				.pastEmployee());
		employeeStatusListCombo.initCombo(employeeStatusList);
		employeeStatusListCombo
				.setDefaultValue(ClientEmployeeDetails.CURRENT_EMPLOYEE);
		employeeStatusListCombo.setDisabled(true);
		firstNameText = new TextItem();
		firstNameText.setRequired(true);
		firstNameText.setTitle(AbstractActionFactory.actionsConstants
				.firstName());
		addBlurHandler(firstNameText);
		lastNameText = new TextItem();
		lastNameText.setRequired(true);
		lastNameText
				.setTitle(AbstractActionFactory.actionsConstants.lastName());
		addBlurHandler(lastNameText);
		jobTitleText = new TextItem();
		jobTitleText
				.setTitle(AbstractActionFactory.actionsConstants.jobTitle());
		employeeStartDate = new DateField(
				AbstractActionFactory.actionsConstants.employeeStartDate());
		employeeStartDate.setRequired(true);
		employeeStartDate.setValue(currentDate);
		employeeLeaveDate = new DateField(
				AbstractActionFactory.actionsConstants.employeeLeaveDate());
		employeeLeaveDate.setValue("DD-MM-YYYY");
		handleEmployeeStartDateValueChange();

	}

	private void dispatchCallToCheckExistance(String empNumber) {
		/*
		 * hrService.isEmployeeExist(empNumber, new
		 * BizantraAsyncCallback<Boolean>() {
		 * 
		 * @Override public void onResponse(Boolean result) { if (result ==
		 * true) { SC .say( AbstractActionFactory.actionsConstants
		 * .employeeNumber(), AbstractActionFactory.actionsConstants
		 * .employeeWithTheGivenNumberIsAlreadyExists());
		 * employeeNumberText.setValue(String .valueOf(employeeNextNumber)); } }
		 * });
		 */
	}

	/**
	 * Add blur handler to given control
	 * 
	 * @param firstNameText
	 */
	private void addBlurHandler(final TextItem firstNameText) {
		firstNameText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				employeeDetailsText.setValue(firstNameText.getValue()
						.toString()
						+ " " + lastNameText.getValue().toString());

			}
		});
	}

	private void addHandlerForFullTimePartTimeText() {
		fulltimePartTimeList
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (fulltimePartTimeList.getSelectedValue()
								.equalsIgnoreCase("Other")) {
							fulltimePartTimeText.setVisible(true);
							fulltimePartTimeText.setFocus();
						} else {
							fulltimePartTimeText.setVisible(false);
						}

					}
				});
	}

	private void handleEmployeeStartDateValueChange() {
		employeeStartDate
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {

						if (employeeStartDate.getDate() != null
								&& employeeStartDate.getDate().compareTo(
										new ClientFinanceDate()) <= 0) {
							employeeStatusListCombo.setSelectedItem(1);
						} else {
							employeeStatusListCombo.setSelectedItem(2);
						}
						if (employeeLeaveDate.getDate() != null) {
							if (employeeStartDate.getDate().after(
									employeeLeaveDate.getDate())) {
								// TODO
								/*
								 * String message =
								 * AbstractActionFactory.actionsConstants.
								 * employeeStartDateShouldBeBeforeTheEmployeeLeaveDate
								 * ();
								 * SC.say(AbstractActionFactory.actionsConstants
								 * .information(), message);
								 */
								employeeStartDate.setValue("");
							}
						}
					}
				});
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

}
