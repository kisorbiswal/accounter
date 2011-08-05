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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.GridAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
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
	private ClientSalesPerson takenSalesperson;
	private DynamicForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;
	protected String gender;
	private List<String> listOfgenders;
	private String[] genderTypes = { Accounter.constants().unspecified(),
			Accounter.constants().male(), Accounter.constants().female() };
	private List<ClientAccount> listOfAccounts;

	private ArrayList<DynamicForm> listforms;
	private String salesPersonName;

	TextAreaItem addrArea;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;

	public NewSalesPersonView() {
		super();
		this.validationCount = 3;

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

		expenseAccountForm = UIUtils.form(Accounter.constants()
				.expenseAccount());
		expenseAccountForm.setWidth("90%");
		expenseSelect = new GridAccountsCombo(Accounter.constants()
				.expenseAccount());
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
		// dateOfBirth.setEndDate(new ClientFinanceDate(19910101));
		// dateOfBirth.setStartDate(new ClientFinanceDate(18910101));
		dateOfBirth.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				long mustdate = new ClientFinanceDate().getDate() - 180000;
				if (new ClientFinanceDate(mustdate).before(dateOfBirth
						.getEnteredDate())) {
					MainFinanceWindow.getViewManager().showError(
							Accounter.constants()
									.dateofBirthshouldshowmorethan18years());
				}
			}
		});

		dateOfHire = new DateField(Accounter.constants().dateofHire());
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

		if (takenSalesperson != null) {

			employeeNameText.setValue(takenSalesperson.getFirstName());
			salesPersonName = takenSalesperson.getFirstName();
			jobTitleText
					.setValue(takenSalesperson.getJobTitle() != null ? takenSalesperson
							.getJobTitle() : "");
			fileAsText.setValue(takenSalesperson.getFileAs());

			setAddresses(takenSalesperson.getAddress());

			ClientAddress toBeShown = allAddresses
					.get(ClientAddress.TYPE_BILL_TO);

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

			fonFaxForm = new PhoneFaxForm(null, null);
			fonFaxForm.setWidth("90%");
			fonFaxForm.getCellFormatter().setWidth(0, 0, "");
			fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
			fonFaxForm.businessPhoneText
					.setValue(takenSalesperson.getPhoneNo());
			fonFaxForm.businessFaxText.setValue(takenSalesperson.getFaxNo());
			emailForm = new EmailForm(null,
					takenSalesperson.getWebPageAddress());
			emailForm.setWidth("100%");
			emailForm.getCellFormatter().setWidth(0, 0, "159");
			emailForm.getCellFormatter().setWidth(0, 1, "125");
			emailForm.businesEmailText.setValue(takenSalesperson.getEmail());
			emailForm.webText.setValue(takenSalesperson.getWebPageAddress());
			statusCheck.setValue(takenSalesperson.isActive());
			if (takenSalesperson.getExpenseAccount() != 0) {
				selectedExpenseAccount = getCompany().getAccount(
						takenSalesperson.getExpenseAccount());
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
			// addrsForm = new AddressForm(null);
			fonFaxForm = new PhoneFaxForm(null, null);
			fonFaxForm.setWidth("90%");
			fonFaxForm.getCellFormatter().setWidth(0, 0, "");
			fonFaxForm.getCellFormatter().setWidth(0, 1, "125");
			emailForm = new EmailForm(null, null);
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
		case 3:
			long mustdate = new ClientFinanceDate().getDate() - 180000;
			return !(new ClientFinanceDate(mustdate).before(dateOfBirth
					.getEnteredDate()));
		case 2:
			return AccounterValidator.validateForm(salesPersonForm, false);
		case 1:
			String name = employeeNameText.getValue().toString();
			if ((takenSalesperson != null ? (takenSalesperson.getName()
					.equalsIgnoreCase(name) ? true : (Utility.isObjectExist(
					getCompany().getSalesPersons(), name) ? false : true))
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
			if (takenSalesperson == null) {
				long mustdate = new ClientFinanceDate().getDate() - 180000;
				if (new ClientFinanceDate(mustdate).before(dateOfBirth
						.getEnteredDate())) {
					MainFinanceWindow.getViewManager().showError(
							Accounter.constants()
									.dateofBirthshouldshowmorethan18years());
				} else {
					this.createObject(salesPerson);
				}
			} else
				this.alterObject(salesPerson);

		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		if (takenSalesperson == null)
			// BaseView.errordata.setHTML(FinanceApplication.constants()
			// .DuplicationOfSalesPesonNotAllowed());
			MainFinanceWindow.getViewManager().showError(
					Accounter.constants().duplicationOfSalesPersonNotAllowed());
		else
			// BaseView.errordata.setHTML(FinanceApplication.constants()
			// .salesPersonUpdationFailed());
			MainFinanceWindow.getViewManager().showError(
					Accounter.constants().salesPersonUpdationFailed());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
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
			saveFailed(new Exception(Accounter.constants().failed()));

	}

	private void reload() {
		try {
			ActionFactory.getNewSalesperSonAction().run(null, true);
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
			salesPerson.setExpenseAccount(selectedExpenseAccount.getID());
		salesPerson.setJobTitle(jobTitleText.getValue().toString());
		salesPerson.setFileAs(fileAsText.getValue().toString());
		if (dateOfBirth.getValue().getDate() != 0) {
			try {

				if (dateOfBirth.getValue().getDateAsObject()
						.after(new ClientFinanceDate().getDateAsObject())) {
					throw new InvalidEntryException(Accounter.constants()
							.invalidDateOfBirth());

				}
				salesPerson.setDateOfBirth(UIUtils.toDate(dateOfBirth
						.getValue()));
			} catch (Exception e) {
				throw new InvalidEntryException(Accounter.constants()
						.invalidDateOfBirth());
			}
		}

		salesPerson.setDateOfHire(UIUtils.toDate(dateOfHire.getValue()));
		salesPerson.setDateOfLastReview(UIUtils.toDate(dateOfLastReview
				.getValue()));
		salesPerson.setDateOfRelease(UIUtils.toDate(dateOfRelease.getValue()));
		if (statusCheck.getValue() != null)
			salesPerson.setActive((Boolean) statusCheck.getValue());

		salesPerson.setFirstName(employeeNameText.getValue().toString());
		salesPerson.setAddress(getAddresss());
		salesPerson.setPhoneNo(fonFaxForm.businessPhoneText.getValue()
				.toString());
		salesPerson.setGender(genderSelect.getSelectedValue());

		salesPerson.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());

		salesPerson.setEmail(emailForm.businesEmailText.getValue().toString());
		salesPerson.setWebPageAddress(emailForm.getWebTextValue());
		salesPerson.setType(ClientPayee.TYPE_EMPLOYEE);

		return salesPerson;
	}

	@Override
	public void init(ViewManager manager) {
		super.init(manager);
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
		else
			takenSalesperson = null;
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
	public void deleteFailed(Throwable caught) {
		// Not required for thos class

	}

	@Override
	public void deleteSuccess(Boolean result) {
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
		// Not required for this class.

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
				ClientAddress add = (ClientAddress) it.next();
				allAddresses.put(add.getType(), add);
			}
		}
	}

	public Set<ClientAddress> getAddresss() {
		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType("company"));
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses
					.put(UIUtils.getAddressType("company"), selectedAddress);
		}
		Collection add = allAddresses.values();
		Set<ClientAddress> toBeSet = new HashSet<ClientAddress>();
		Iterator it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return toBeSet;
	}
}
