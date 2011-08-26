package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.GridAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @modified by Ravi Kiran.G
 * 
 */
public class NewSalesPersonView extends BaseView<ClientSalesPerson> {

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
	private DynamicForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;
	protected String gender;
	private List<String> listOfgenders;
	private String[] genderTypes = { Accounter.constants().unspecified(),
			Accounter.constants().male(), Accounter.constants().female() };
	private List<ClientAccount> listOfAccounts;

	private ArrayList<DynamicForm> listforms;
	// private String salesPersonName;

	TextAreaItem addrArea;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;

	public NewSalesPersonView() {
		super();
	}

	private void createControls() {
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		listforms = new ArrayList<DynamicForm>();

		employeeNameText = new TextItem(Accounter.constants().salesPersonName());
		employeeNameText.setWidth("205px");
		employeeNameText.setRequired(true);

		fileAsText = new TextItem(Accounter.constants().fileAs());
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

		jobTitleText = new TextItem(Accounter.constants().jobTitle());
		jobTitleText.setWidth("205px");

		salesPersonForm = UIUtils.form(Accounter.constants().salesPerson());
		salesPersonForm.setWidth("90%");
		salesPersonForm.setFields(employeeNameText, fileAsText, jobTitleText);
		salesPersonForm.getCellFormatter().setWidth(0, 0, "280px");

		expenseAccountForm = UIUtils.form(Accounter.messages().expenseAccount(
				Global.get().Account()));
		expenseAccountForm.setWidth("90%");
		expenseSelect = new GridAccountsCombo(Accounter.messages()
				.expenseAccount(Global.get().Account()));
		expenseSelect.setWidth("180px");
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
		memoArea.setTitle(Accounter.constants().memo());
		memoForm.setFields(memoArea);
		memoForm.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		// memoForm.getCellFormatter().getElement(0, 1).getStyle().setWidth(239,
		// Unit.PX);
		memoForm.getCellFormatter().setWidth(0, 0, "232");
		salesPersonInfoForm = UIUtils.form(Accounter.constants()
				.salesPersonInformation());
		salesPersonInfoForm.setStyleName("align-form");
		salesPersonInfoForm.setWidth("100%");
		statusCheck = new CheckboxItem(Accounter.constants().active());
		statusCheck.setValue(true);
		genderSelect = new SelectCombo(Accounter.constants().gender());
		// genderSelect.setWidth(45);
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

		dateOfBirth = new DateField(Accounter.constants().dateofBirth());
		dateOfBirth
		.setToolTip(Accounter.messages()
				.selectDateOfBirth(
						this.getAction().getViewName()));
		// dateOfBirth.setEndDate(new ClientFinanceDate(19910101));
		// dateOfBirth.setStartDate(new ClientFinanceDate(18910101));
		dateOfBirth.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				long mustdate = new ClientFinanceDate().getDate() - 180000;
				if (new ClientFinanceDate(mustdate).before(dateOfBirth
						.getEnteredDate())) {
					addError(dateOfBirth, Accounter.constants()
							.dateofBirthshouldshowmorethan18years());
				} else {
					clearError(dateOfBirth);
				}
			}
		});

		dateOfHire = new DateField(Accounter.constants().dateofHire());
		dateOfHire.setToolTip(Accounter.messages().selectDateOfHire(this.getAction().getViewName()));
		// dateOfHire.setUseTextField(true);

		dateOfLastReview = new DateField(Accounter.constants()
				.dateofLastReview());
		// dateOfLastReview.setUseTextField(true);

		dateOfRelease = new DateField(Accounter.constants().dateofRelease());
		// dateOfRelease.setUseTextField(true);

		salesPersonInfoForm.setFields(statusCheck, genderSelect, dateOfBirth,
				dateOfHire, dateOfLastReview, dateOfRelease);

		// XXX
		addrsForm = new DynamicForm();
		addrArea = new TextAreaItem(Accounter.constants().address());
		addrArea.setWidth("205px");
		addrArea.setHelpInformation(true);
		addrArea.setWidth(100);
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

		if (getData() != null) {

			employeeNameText.setValue(data.getFirstName());
			// salesPersonName = data.getFirstName();
			jobTitleText.setValue(data.getJobTitle() != null ? data
					.getJobTitle() : "");
			fileAsText.setValue(data.getFileAs());
			Set<ClientAddress> addresses = new HashSet<ClientAddress>();
			addresses.add(data.getAddress());
			setAddresses(addresses);

			ClientAddress toBeShown = allAddresses
					.get(ClientAddress.TYPE_BILL_TO);
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

				if (toBeShown.getCity() != null
						&& !toBeShown.getCity().isEmpty()) {
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

			fonFaxForm = new PhoneFaxForm(null, null, this);
			fonFaxForm.setWidth("90%");
			fonFaxForm.getCellFormatter().setWidth(0, 0, "");
			fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
			fonFaxForm.businessPhoneText.setValue(data.getPhoneNo());
			fonFaxForm.businessFaxText.setValue(data.getFaxNo());
			emailForm = new EmailForm(null, data.getWebPageAddress(), this);
			emailForm.setWidth("100%");
			emailForm.getCellFormatter().setWidth(0, 0, "159");
			emailForm.getCellFormatter().setWidth(0, 1, "125");
			emailForm.businesEmailText.setValue(data.getEmail());
			emailForm.webText.setValue(data.getWebPageAddress());
			statusCheck.setValue(data.isActive());
			if (data.getExpenseAccount() != 0) {
				selectedExpenseAccount = getCompany().getAccount(
						data.getExpenseAccount());
				expenseSelect.setComboItem(selectedExpenseAccount);
			}
			genderSelect.setComboItem(data.getGender());
			dateOfBirth.setValue(new ClientFinanceDate(data.getDateOfBirth()));
			dateOfHire.setValue(new ClientFinanceDate(data.getDateOfHire()));
			dateOfLastReview.setValue(new ClientFinanceDate(data
					.getDateOfLastReview()));
			dateOfRelease.setValue(new ClientFinanceDate(data
					.getDateOfRelease()));
			memoArea.setValue(data.getMemo());

		} else {
			setData(new ClientSalesPerson());
			// addrsForm = new AddressForm(null);
			fonFaxForm = new PhoneFaxForm(null, null, this);
			fonFaxForm.setWidth("90%");
			fonFaxForm.getCellFormatter().setWidth(0, 0, "");
			fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
			emailForm = new EmailForm(null, null, this);
			emailForm.setWidth("100%");
			emailForm.getCellFormatter().setWidth(0, 0, "150");
			emailForm.getCellFormatter().setWidth(0, 1, "125");
			genderSelect.setDefaultToFirstOption(Boolean.TRUE);
			// gender = ClientSalesPerson.GENDER_UNSPECIFIED;
		}
		addrsForm.setWidth("90%");
		// addrsForm.getCellFormatter().setWidth(0, 0, "65");
		addrsForm.getCellFormatter().setWidth(0, 1, "125");
		addrsForm.setFields(addrArea);
		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.add(salesPersonForm);
		leftVLay.add(addrsForm);
		addrsForm.getCellFormatter().addStyleName(0, 0, "addrsFormCellAlign");
		addrsForm.getCellFormatter().addStyleName(0, 1, "addrsFormCellAlign");
		leftVLay.add(fonFaxForm);
		fonFaxForm.setStyleName("phone-fax-formatter");
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
		this.add(mainVlay);

		/* Adding dynamic forms in list */
		listforms.add(salesPersonForm);

		listforms.add(addrsForm);
		listforms.add(fonFaxForm);
		listforms.add(expenseAccountForm);
		listforms.add(memoForm);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// check whether the sales person is already added or not
		// valid date of birth?

		String name = employeeNameText.getValue().toString();
		ClientSalesPerson clientSalesPerson = getCompany()
				.getSalesPersonByName(name);
		if (!(isInViewMode() ? (data.getName().equalsIgnoreCase(name) ? true
				: clientSalesPerson == null) : true)) {
			result.addError(employeeNameText, Accounter.constants()
					.alreadyExist());
			return result;
		}

		if (dateOfBirth.getValue().getDate() != 0) {
			long mustdate = new ClientFinanceDate().getDate() - 180000;
			if (dateOfBirth.getValue().getDateAsObject()
					.after(new ClientFinanceDate().getDateAsObject())) {
				result.addError(dateOfBirth, Accounter.constants()
						.invalidDateOfBirth());
			} else if ((new ClientFinanceDate(mustdate).before(dateOfBirth
					.getEnteredDate()))) {
				result.addError(dateOfBirth,
						"Sales Person should have 18 years");
			}
		}
		result.add(salesPersonForm.validate());

		long mustdate = new ClientFinanceDate().getDate() - 180000;
		if (new ClientFinanceDate(mustdate)
				.before(dateOfBirth.getEnteredDate())) {
			addError(this, Accounter.constants()
					.dateofBirthshouldshowmorethan18years());
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		updateSalesPersonObject();
		saveOrUpdate(getData());

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// if (!isEdit)
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .DuplicationOfSalesPesonNotAllowed());
		// addError(this, Accounter.constants()
		// .duplicationOfSalesPersonNotAllowed());
		// else
		// BaseView.errordata.setHTML(FinanceApplication.constants()
		// .salesPersonUpdationFailed());
		// addError(this, Accounter.constants().salesPersonUpdationFailed());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		ClientSalesPerson salesPerson = (ClientSalesPerson) result;
		if (salesPerson.getID() != 0) {
			// if (takenSalesperson == null)
			// Accounter.showInformation(((ClientSalesPerson) result)
			// .getFirstName()
			// + FinanceApplication.constants()
			// .isCreatedSuccessfully());
			// else
			// Accounter.showInformation(((ClientSalesPerson) result)
			// .getFirstName()
			// + FinanceApplication.constants()
			// .isUpdatedSuccessfully());
			super.saveSuccess(result);

		} else
			saveFailed(new AccounterException(Accounter.constants().failed()));

	}

	private void reload() {
		try {
			ActionFactory.getNewSalesperSonAction().run(null, true);
		} catch (Throwable e) {

			e.printStackTrace();
		}
	}

	private void updateSalesPersonObject() {

		data.setMemo(UIUtils.toStr(memoArea.getValue()));
		if (selectedExpenseAccount != null)
			data.setExpenseAccount(selectedExpenseAccount.getID());
		data.setJobTitle(jobTitleText.getValue().toString());
		data.setFileAs(fileAsText.getValue().toString());
		data.setDateOfBirth(dateOfBirth.getValue());
		data.setDateOfHire(UIUtils.toDate(dateOfHire.getValue()));
		data.setDateOfLastReview(UIUtils.toDate(dateOfLastReview.getValue()));
		data.setDateOfRelease(UIUtils.toDate(dateOfRelease.getValue()));
		if (statusCheck.getValue() != null)
			data.setActive((Boolean) statusCheck.getValue());

		data.setFirstName(employeeNameText.getValue().toString());
		data.setAddress(getAddresss());
		data.setPhoneNo(fonFaxForm.businessPhoneText.getValue().toString());
		data.setGender(genderSelect.getSelectedValue());

		data.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());

		data.setEmail(emailForm.businesEmailText.getValue().toString());
		data.setWebPageAddress(emailForm.getWebTextValue());

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

	protected void adjustFormWidths(int titlewidth, int listBoxWidth) {

		addrsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), titlewidth + "");
		addrsForm.getCellFormatter().getElement(0, 1)
				.setAttribute(Accounter.constants().width(), listBoxWidth + "");

		fonFaxForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), titlewidth + "");
		fonFaxForm.getCellFormatter().getElement(0, 1)
				.setAttribute(Accounter.constants().width(), listBoxWidth + "");

		salesPersonForm.getCellFormatter().getElement(0, 0).getStyle()
				.setWidth(titlewidth + listBoxWidth, Unit.PX);
		expenseAccountForm.getCellFormatter().getElement(0, 0)
				.setAttribute("width", titlewidth + listBoxWidth + "");
		memoForm.getCellFormatter()
				.getElement(0, 0)
				.setAttribute(Accounter.constants().width(),
						titlewidth + listBoxWidth + "");
		emailForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "");
		emailForm.getCellFormatter().getElement(0, 1)
				.setAttribute(Accounter.constants().width(), "");

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
	public void deleteFailed(AccounterException caught) {
		// Not required for thos class

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// Not required for this class

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		if (core.getID() == (this.expenseSelect.getSelectedValue().getID())) {
			this.expenseSelect.addItemThenfireEvent((ClientAccount) core);
		}
	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);

	}

	@Override
	public void print() {
		// Not required for this class

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void printPreview() {
		// Not required for this class.

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().newSalesPerson();
	}

	private void setAddresses(Set<ClientAddress> addresses) {
		if (addresses != null) {
			Iterator it = addresses.iterator();
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
		Collection add = allAddresses.values();
		Iterator it = add.iterator();
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
}
