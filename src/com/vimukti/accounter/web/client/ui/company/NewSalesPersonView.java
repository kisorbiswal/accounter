package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.GridAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * @modified by Ravi Kiran.G
 * 
 */
public class NewSalesPersonView extends BaseView<ClientSalesPerson> {

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
	private DynamicForm salesPersonForm;

	private DynamicForm expenseAccountForm;
	private TextAreaItem memoArea;

	private DynamicForm salesPersonInfoForm, memoForm;
	private SelectCombo genderSelect;
	protected ClientAccount selectedExpenseAccount;
	private GridAccountsCombo expenseSelect;
	private TextItem fileAsText, employeeNameText, jobTitleText;
	DateField dateOfBirth, dateOfHire, dateOfLastReview, dateOfRelease;

	protected boolean isClose;
	private CheckboxItem statusCheck;
	private ClientSalesPerson takenSalesperson;
	private AddressForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;
	protected String gender;
	private List<String> listOfgenders;
	private String[] genderTypes = {
			FinanceApplication.getCompanyMessages().unspecified(),
			FinanceApplication.getCompanyMessages().male(),
			FinanceApplication.getCompanyMessages().female() };
	private List<ClientAccount> listOfAccounts;

	private ArrayList<DynamicForm> listforms;

	public NewSalesPersonView() {
		super();
		this.validationCount = 2;

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		employeeNameText = new TextItem(companyConstants.salesPersonName());
		employeeNameText.setWidth("205px");
		employeeNameText.setRequired(true);

		fileAsText = new TextItem(companyConstants.fileAs());
		fileAsText.setWidth("205px");
		employeeNameText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null) {
					String val = employeeNameText.getValue().toString();
					fileAsText.setValue(val);
				}
			}
		});

		jobTitleText = new TextItem(companyConstants.jobTitle());
		jobTitleText.setWidth("205px");

		salesPersonForm = UIUtils.form(companyConstants.salesPerson());
		salesPersonForm.setWidth("90%");
		salesPersonForm.setFields(employeeNameText, fileAsText, jobTitleText);
		salesPersonForm.getCellFormatter().setWidth(0, 0, "280px");

		expenseAccountForm = UIUtils.form(companyConstants.expenseAccount());
		expenseAccountForm.setWidth("90%");
		expenseSelect = new GridAccountsCombo(companyConstants.expenseAccount());
		expenseSelect.setWidth("205px");
		expenseSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedExpenseAccount = selectItem;

					}

				});

		expenseAccountForm.getCellFormatter().setWidth(0, 0, "250px");
		expenseAccountForm.setFields(expenseSelect);

		memoForm = new DynamicForm();
		memoForm.setWidth("50%");
		memoArea = new TextAreaItem();
		memoArea.setWidth(100);
		memoArea.setTitle(FinanceApplication.getCompanyMessages().memo());
		memoForm.setFields(memoArea);
		memoForm.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		// memoForm.getCellFormatter().getElement(0, 1).getStyle().setWidth(239,
		// Unit.PX);
		memoForm.getCellFormatter().setWidth(0, 0, "232");
		salesPersonInfoForm = UIUtils.form(companyConstants
				.salesPersonInformation());
		salesPersonInfoForm.setStyleName("align-form");
		salesPersonInfoForm.setWidth("100%");
		statusCheck = new CheckboxItem(companyConstants.active());
		statusCheck.setValue(true);
		genderSelect = new SelectCombo(companyConstants.gender());
		genderSelect.setWidth(45);
		listOfgenders = new ArrayList<String>();
		for (int i = 0; i < genderTypes.length; i++) {
			listOfgenders.add(genderTypes[i]);
		}
		genderSelect.initCombo(listOfgenders);
		genderSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						gender = selectItem;
					}
				});

		dateOfBirth = new DateField(companyConstants.dateofBirth());
		dateOfBirth.setEndDate(new ClientFinanceDate(19910101));
		dateOfBirth.setStartDate(new ClientFinanceDate(18910101));
		// dateOfBirth.setUseTextField(true);

		dateOfHire = new DateField(companyConstants.dateofHire());
		// dateOfHire.setUseTextField(true);

		dateOfLastReview = new DateField(companyConstants.dateofLastReview());
		// dateOfLastReview.setUseTextField(true);

		dateOfRelease = new DateField(companyConstants.dateofRelease());
		// dateOfRelease.setUseTextField(true);

		salesPersonInfoForm.setFields(statusCheck, genderSelect, dateOfBirth,
				dateOfHire, dateOfLastReview, dateOfRelease);

		if (takenSalesperson != null) {

			employeeNameText.setValue(takenSalesperson.getFirstName());
			jobTitleText
					.setValue(takenSalesperson.getJobTitle() != null ? takenSalesperson
							.getJobTitle()
							: "");
			fileAsText.setValue(takenSalesperson.getFileAs());

			addrsForm = new AddressForm(takenSalesperson.getAddress());
			addrsForm.setWidth("90%");
			addrsForm.getCellFormatter().setWidth(0, 0, "65");
			addrsForm.getCellFormatter().setWidth(0, 1, "125");
			fonFaxForm = new PhoneFaxForm(takenSalesperson.getPhoneNumbers(),
					takenSalesperson.getFaxNumbers());
			fonFaxForm.setWidth("90%");
			fonFaxForm.getCellFormatter().setWidth(0, 0, "");
			fonFaxForm.getCellFormatter().setWidth(0, 1, "125");

			emailForm = new EmailForm(takenSalesperson.getEmails(),
					takenSalesperson.getWebPageAddress());
			emailForm.setWidth("90%");
			emailForm.getCellFormatter().setWidth(0, 0, "189");
			emailForm.getCellFormatter().setWidth(0, 1, "125");

			statusCheck.setValue(takenSalesperson.isActive());
			if (takenSalesperson.getExpenseAccount() != null) {
				selectedExpenseAccount = FinanceApplication.getCompany()
						.getAccount(takenSalesperson.getExpenseAccount());
				expenseSelect.setComboItem(selectedExpenseAccount);
			}
			genderSelect.setComboItem(takenSalesperson.getGender());
			dateOfBirth.setValue(new ClientFinanceDate(takenSalesperson
					.getDateOfBirth()));
			dateOfHire.setValue(new ClientFinanceDate(takenSalesperson
					.getDateOfHire()));
			dateOfLastReview.setValue(new ClientFinanceDate(takenSalesperson
					.getDateOfLastReview()));
			dateOfRelease.setValue(new ClientFinanceDate(takenSalesperson
					.getDateOfRelease()));
			memoArea.setValue(takenSalesperson.getMemo());

		} else {
			addrsForm = new AddressForm(null);
			addrsForm.setWidth("90%");
			addrsForm.getCellFormatter().setWidth(0, 0, "65");
			addrsForm.getCellFormatter().setWidth(0, 1, "125");
			fonFaxForm = new PhoneFaxForm(null, null);
			fonFaxForm.setWidth("90%");
			fonFaxForm.getCellFormatter().setWidth(0, 0, "");
			fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
			emailForm = new EmailForm(null, null);
			emailForm.setWidth("90%");
			emailForm.getCellFormatter().setWidth(0, 0, "189");
			emailForm.getCellFormatter().setWidth(0, 1, "125");
			genderSelect.setDefaultToFirstOption(Boolean.TRUE);
			// gender = ClientSalesPerson.GENDER_UNSPECIFIED;
		}

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.add(salesPersonForm);
		leftVLay.add(addrsForm);
		addrsForm.getCellFormatter().addStyleName(0, 0, "addrsFormCellAlign");
		addrsForm.getCellFormatter().addStyleName(0, 1, "addrsFormCellAlign");
		leftVLay.add(fonFaxForm);
		leftVLay.add(expenseAccountForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.getElement().getStyle().setMarginLeft(35, Unit.PX);
		rightVLay.add(emailForm);
		rightVLay.add(salesPersonInfoForm);
		salesPersonInfoForm.getCellFormatter().setWidth(0, 0, "150");
		HorizontalPanel topHLay = new HorizontalPanel();
		// topHLay.setSpacing(5);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);

		VerticalPanel mainVlay = new VerticalPanel();
		mainVlay.setWidth("100%");
		mainVlay.setSpacing(10);
		mainVlay.add(topHLay);
		mainVlay.add(memoForm);
		if (UIUtils.isMSIEBrowser()) {
			emailForm.getCellFormatter().setWidth(0, 2, "150px");
			emailForm.getCellFormatter().setWidth(1, 0, "195px");
			emailForm.getCellFormatter().setWidth(1, 1, "150px");
		}
		canvas.add(mainVlay);

		/* Adding dynamic forms in list */
		listforms.add(salesPersonForm);

		listforms.add(addrsForm);
		listforms.add(fonFaxForm);
		listforms.add(expenseAccountForm);
		listforms.add(memoForm);
	}

	@Override
	public boolean validate() throws InvalidEntryException {

		switch (this.validationCount) {
		case 2:
			return AccounterValidator.validateForm(salesPersonForm);
		case 1:
			String name = employeeNameText.getValue().toString();
			if (((takenSalesperson == null && Utility.isObjectExist(
					FinanceApplication.getCompany().getSalesPersons(), name)) ? false
					: true)
					|| (takenSalesperson != null ? (takenSalesperson.getName()
							.equalsIgnoreCase(name) ? true : (Utility
							.isObjectExist(FinanceApplication.getCompany()
									.getSalesPersons(), name) ? false : true))
							: true)) {
				return true;
			} else
				throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);

		default:
			return true;

		}

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		ClientSalesPerson salesPerson = getSalesPersonObject();
		try {
			if (takenSalesperson == null)

				this.createObject(salesPerson);
			else
				this.alterObject(salesPerson);

		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		if (takenSalesperson == null)
			Accounter.showError(FinanceApplication.getCompanyMessages()
					.DuplicationOfSalesPesonNotAllowed());
		else
			Accounter.showError(FinanceApplication.getCompanyMessages()
					.salesPersonUpdationFailed());
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		ClientSalesPerson salesPerson = (ClientSalesPerson) result;
		if (salesPerson.getStringID() != null) {
			// if (takenSalesperson == null)
			// Accounter.showInformation(((ClientSalesPerson) result)
			// .getFirstName()
			// + FinanceApplication.getCompanyMessages()
			// .isCreatedSuccessfully());
			// else
			// Accounter.showInformation(((ClientSalesPerson) result)
			// .getFirstName()
			// + FinanceApplication.getCompanyMessages()
			// .isUpdatedSuccessfully());
			super.saveSuccess(result);

		} else
			saveFailed(new Exception(FinanceApplication.getCompanyMessages()
					.failed()));

	}

	@SuppressWarnings("unused")
	private void reload() {
		try {
			CompanyActionFactory.getNewSalesperSonAction().run(null, true);
		} catch (Throwable e) {

			e.printStackTrace();
		}
	}

	private ClientSalesPerson getSalesPersonObject()
			throws InvalidEntryException {

		ClientSalesPerson salesPerson;
		if (takenSalesperson != null)
			salesPerson = takenSalesperson;
		else
			salesPerson = new ClientSalesPerson();
		salesPerson.setMemo(UIUtils.toStr(memoArea.getValue()));
		if (selectedExpenseAccount != null)
			salesPerson.setExpenseAccount(selectedExpenseAccount.getStringID());
		salesPerson.setJobTitle(jobTitleText.getValue().toString());
		salesPerson.setFileAs(fileAsText.getValue().toString());
		if (dateOfBirth.getValue().getTime() != 0) {
			try {

				if (dateOfBirth.getValue().getDateAsObject().after(
						new ClientFinanceDate().getDateAsObject())) {
					throw new InvalidEntryException(FinanceApplication
							.getCompanyMessages().invalidDateOfBirth());

				}
				salesPerson.setDateOfBirth(UIUtils.toDate(dateOfBirth
						.getValue()));
			} catch (Exception e) {
				throw new InvalidEntryException(FinanceApplication
						.getCompanyMessages().invalidDateOfBirth());
			}
		}

		salesPerson.setDateOfHire(UIUtils.toDate(dateOfHire.getValue()));
		salesPerson.setDateOfLastReview(UIUtils.toDate(dateOfLastReview
				.getValue()));
		salesPerson.setDateOfRelease(UIUtils.toDate(dateOfRelease.getValue()));
		if (statusCheck.getValue() != null)
			salesPerson.setActive((Boolean) statusCheck.getValue());

		salesPerson.setFirstName(employeeNameText.getValue().toString());
		salesPerson.setAddress(addrsForm.getAddresss());
		salesPerson.setPhoneNumbers(fonFaxForm.getAllPhones());
		salesPerson.setGender(genderSelect.getSelectedValue());

		salesPerson.setFaxNumbers(fonFaxForm.getAllFaxes());

		salesPerson.setEmails(emailForm.getAllEmails());
		salesPerson.setWebPageAddress(emailForm.getWebTextValue());
		salesPerson.setType(ClientPayee.TYPE_EMPLOYEE);

		return salesPerson;
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {

		super.initData();
		initGridAccounts();

	}

	private void initGridAccounts() {
		listOfAccounts = expenseSelect.getAccounts();
		expenseSelect.initCombo(listOfAccounts);
	}

	@Override
	public void setData(ClientSalesPerson data) {
		super.setData(data);
		if (data != null)
			takenSalesperson = (ClientSalesPerson) data;
	}

	protected void adjustFormWidths(int titlewidth, int listBoxWidth) {

		addrsForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getCompanyMessages().width(),
				titlewidth + "");
		addrsForm.getCellFormatter().getElement(0, 1).setAttribute(
				FinanceApplication.getCompanyMessages().width(),
				listBoxWidth + "");

		fonFaxForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getCompanyMessages().width(),
				titlewidth + "");
		fonFaxForm.getCellFormatter().getElement(0, 1).setAttribute(
				FinanceApplication.getCompanyMessages().width(),
				listBoxWidth + "");

		salesPersonForm.getCellFormatter().getElement(0, 0).getStyle()
				.setWidth(titlewidth + listBoxWidth, Unit.PX);
		expenseAccountForm.getCellFormatter().getElement(0, 0).setAttribute(
				"width", titlewidth + listBoxWidth + "");
		memoForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getCompanyMessages().width(),
				titlewidth + listBoxWidth + "");
		emailForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getCompanyMessages().width(), "");
		emailForm.getCellFormatter().getElement(0, 1).setAttribute(
				FinanceApplication.getCompanyMessages().width(), "");

	}

	public static NewSalesPersonView getInstance() {

		return new NewSalesPersonView();
	}

	// @Override
	// protected void onLoad() {
	// int titlewidth = fonFaxForm.getCellFormatter().getElement(0, 0)
	// .getOffsetWidth();
	// int listBoxWidth = fonFaxForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth();
	//
	// adjustFormWidths(titlewidth, listBoxWidth);
	// super.onLoad();
	// }
	//
	// @Override
	// protected void onAttach() {
	//
	// int titlewidth = fonFaxForm.getCellFormatter().getElement(0, 0)
	// .getOffsetWidth();
	// int listBoxWidth = fonFaxForm.getCellFormatter().getElement(0, 1)
	// .getOffsetWidth();
	//
	// adjustFormWidths(titlewidth, listBoxWidth);
	//
	// super.onAttach();
	// }

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.employeeNameText.setFocus();
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
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stuba
		if (core.getStringID().equals(
				this.expenseSelect.getSelectedValue().getStringID())) {
			this.expenseSelect.addItemThenfireEvent((ClientAccount) core);
		}
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
}
