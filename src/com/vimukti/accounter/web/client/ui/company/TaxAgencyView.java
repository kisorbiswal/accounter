//package com.vimukti.accounter.web.client.ui.company;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.dom.client.Style.Unit;
//import com.google.gwt.event.dom.client.ChangeEvent;
//import com.google.gwt.event.dom.client.ChangeHandler;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.TextBox;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.vimukti.accounter.web.client.core.AccounterCommand;
//import com.vimukti.accounter.web.client.core.messages;
//import com.vimukti.accounter.web.client.core.AccounterCoreType;
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.ClientContact;
//import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
//import com.vimukti.accounter.web.client.core.IAccounterCore;
//import com.vimukti.accounter.web.client.core.Utility;
//import com.vimukti.accounter.web.client.ui.AddressForm;
//import com.vimukti.accounter.web.client.ui.EmailForm;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
//import com.vimukti.accounter.web.client.ui.combo.TaxAgencyAccountsCombo;
//import com.vimukti.accounter.web.client.ui.core.Accounter;
//import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
//import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
//import com.vimukti.accounter.web.client.ui.core.BaseView;
//import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
//import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//import com.vimukti.accounter.web.client.ui.grids.ContactGrid;
//
///**
// * 
// * @author G.Ravi Kiran
// * @param <T>
// */
//public class TaxAgencyView extends BaseView<ClientTaxAgency> {
//
//	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
//
//	TextItem taxAgencyText, fileAsText;
//
//	private TextItem linksText;
//	private TextAreaItem memoArea;
//	private CheckboxItem statusCheck;
//	private DynamicForm memoForm, accInfoForm, taxAgencyForm;
//
//	private AddressForm addrsForm;
//	private PhoneFaxForm phoneFaxForm;
//	private EmailForm emailForm;
//
//	private PaymentTermsCombo paymentTermsCombo;
//	private TaxAgencyAccountsCombo liabilityAccountCombo;
//
//	private ContactGrid gridView;
//
//	private ClientTaxAgency takenTaxAgency;
//
//	private ClientPaymentTerms selectedPaymentTerm;
//	private ClientAccount selectedAccount;
//
//	private Button addButton;
//
//	private ArrayList<DynamicForm> listforms;
//
//	public TaxAgencyView() {
//		super();
//		this.validationCount = 4;
//
//	}
//
//	private void initPaymentTermsCombo() {
//
//		paymentTermsCombo.initCombo(FinanceApplication.getCompany()
//				.getPaymentsTerms());
//		if (takenTaxAgency != null && (takenTaxAgency.getPaymentTerm()) != null) {
//			selectedPaymentTerm = FinanceApplication.getCompany()
//					.getPaymentTerms(takenTaxAgency.getPaymentTerm());
//			paymentTermsCombo.setComboItem(selectedPaymentTerm);
//		}
//
//	}
//
//	private void createControls() {
//		listforms = new ArrayList<DynamicForm>();
//
//		VerticalPanel topLayout = getTopLayout();
//
//		VerticalPanel mainVLay = new VerticalPanel();
//		mainVLay.setSize("100%", "100%");
//		mainVLay.add(topLayout);
//
//		canvas.add(mainVLay);
//		setSize("100%", "100%");
//	}
//
//	@Override
//	public void saveAndUpdateView() throws Exception {
//		try {
//			ClientTaxAgency taxAgency = getTaxAgency();
//			if (takenTaxAgency == null) {
//				if (Utility.isObjectExist(FinanceApplication.getCompany()
//						.getTaxAgencies(), taxAgency.getName())) {
//					throw new InvalidEntryException(
//							AccounterErrorType.ALREADYEXIST);
//				}
//				this.createObject(taxAgency);
//			}
//
//			else
//				this.alterObject(taxAgency);
//
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	@Override
//	public void saveFailed(AccounterException exception) {
//		super.saveFailed(exception);
//		if (takenTaxAgency == null)
//			Accounter.showError(FinanceApplication.constants()
//					.duplicationTaxAgencyNamesNotAllowed());
//		else
//			Accounter.showError(FinanceApplication.constants()
//					.failedToUpdate());
//	}
//
//	@Override
//	public void saveSuccess(IAccounterCore result) {
//		ClientTaxAgency taxAgency = (ClientTaxAgency) result;
//		if (taxAgency.getID() != null) {
//			// if (takenTaxAgency == null)
//			// Accounter.showInformation(taxAgency.getName()
//			// + FinanceApplication.constants()
//			// .isCreatedSuccessfully());
//			// else
//			// Accounter.showInformation(taxAgency.getName()
//			// + FinanceApplication.constants()
//			// .isUpdatedSuccessfully());
//			super.saveSuccess(result);
//		} else
//			saveFailed(new Exception(FinanceApplication.constants()
//					.failed()));
//
//	}
//
//	@Override
//	public boolean validate() throws InvalidEntryException {
//
//		switch (this.validationCount) {
//		case 4:
//			String name = taxAgencyText.getValue().toString() != null ? taxAgencyText
//					.getValue().toString()
//					: "";
//			if (takenTaxAgency != null ? (takenTaxAgency.getName()
//					.equalsIgnoreCase(name) ? true
//					: (Utility.isObjectExist(FinanceApplication.getCompany()
//							.gettaxAgencies(), name) ? false : true)) : true) {
//				return true;
//			} else
//				throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);
//		case 3:
//			return AccounterValidator.validateForm(taxAgencyForm);
//		case 2:
//			return AccounterValidator.validateForm(accInfoForm);
//		case 1:
//			return gridView.validateGrid();
//		default:
//			return false;
//
//		}
//
//	}
//
//	private ClientTaxAgency getTaxAgency() {
//		ClientTaxAgency taxAgency;
//		if (takenTaxAgency != null)
//			taxAgency = takenTaxAgency;
//		else
//			taxAgency = new ClientTaxAgency();
//		// Setting Company
//
//		// Setting TaxAgency
//		taxAgency.setName(taxAgencyText.getValue().toString());
//
//		// Setting File As
//		taxAgency.setFileAs(fileAsText.getValue().toString());
//
//		// Setting Addresses
//		taxAgency.setAddress(addrsForm.getAddresss());
//
//		// Setting Phone
//		taxAgency.setPhoneNumbers(phoneFaxForm.getAllPhones());
//
//		// Setting Fax
//		taxAgency.setFaxNumbers(phoneFaxForm.getAllFaxes());
//
//		// Setting Email and Internet
//		taxAgency.setEmails(emailForm.getAllEmails());
//
//		// Setting web page Address
//		taxAgency.setWebPageAddress(emailForm.getWebTextValue());
//
//		// Setting Active
//		taxAgency.setActive((Boolean) statusCheck.getValue());
//
//		// Setting Payment Terms
//		taxAgency.setPaymentTerm(selectedPaymentTerm.getID());
//
//		// Setting Liability account
//		taxAgency.setLiabilityAccount(selectedAccount.getID());
//
//		// Setting Contacts
//
//		Set<ClientContact> allContacts = new HashSet<ClientContact>();
//
//		// 
//		for (ClientContact record : gridView.getRecords()) {
//			ClientContact contact = new ClientContact();
//			if (record.isPrimary())
//				contact.setPrimary(true);
//			else
//				contact.setPrimary(false);
//			contact.setName(record.getName());
//			contact.setTitle(record.getTitle());
//			contact.setBusinessPhone(record.getBusinessPhone());
//			contact.setEmail(record.getEmail());
//			allContacts.add(contact);
//		}
//		taxAgency.setContacts(allContacts);
//
//		// Setting Memo
//		taxAgency.setMemo(UIUtils.toStr(memoArea.getValue()));
//
//		return taxAgency;
//	}
//
//	private VerticalPanel getTopLayout() {
//		Label lab = new Label(FinanceApplication.constants()
//				.taxAgency());
//		lab.addStyleName(FinanceApplication.constants().lableTitle());
//		taxAgencyText = new TextItem(companyConstants.taxAgency());
//		taxAgencyText.setWidth(100);
//		taxAgencyText.setRequired(true);
//
//		fileAsText = new TextItem(companyConstants.fileAs());
//		fileAsText.setWidth(100);
//		taxAgencyText.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				if (((TextBox) event.getSource()).getValue() != null) {
//					String val = ((TextBox) event.getSource()).getValue()
//							.toString();
//					fileAsText.setValue(val);
//				}
//			}
//		});
//
//		taxAgencyForm = new DynamicForm();
//		taxAgencyForm = UIUtils.form(companyConstants.taxAgency());
//		taxAgencyForm.setFields(taxAgencyText, fileAsText);
//		taxAgencyForm.setWidth("100%");
//
//		accInfoForm = new DynamicForm();
//		accInfoForm = UIUtils.form(companyConstants.accountInformation());
//
//		statusCheck = new CheckboxItem(companyConstants.active());
//		statusCheck.setValue(true);
//
//		paymentTermsCombo = new PaymentTermsCombo(companyConstants
//				.paymentTerm());
//		paymentTermsCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {
//
//					@Override
//					public void selectedComboBoxItem(
//							ClientPaymentTerms selectItem) {
//						selectedPaymentTerm = (ClientPaymentTerms) selectItem;
//
//					}
//				});
//
//		paymentTermsCombo.setRequired(true);
//
//		liabilityAccountCombo = new TaxAgencyAccountsCombo(companyConstants
//				.liabilityAccount());
//
//		liabilityAccountCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
//
//					@Override
//					public void selectedComboBoxItem(ClientAccount selectItem) {
//						selectedAccount = (ClientAccount) selectItem;
//					}
//
//				});
//
//		liabilityAccountCombo.setRequired(true);
//
//		accInfoForm.setFields(statusCheck, paymentTermsCombo,
//				liabilityAccountCombo);
//		accInfoForm.setWidth("100%");
//
//		Label contacts = new Label(companyConstants.contacts());
//		initListGrid();
//
//		memoForm = new DynamicForm();
//		memoForm.setWidth("100%");
//		memoForm.setIsGroup(true);
//		memoForm.setGroupTitle(companyConstants.memo());
//		// 
//		memoArea = new TextAreaItem();
//		memoArea.setTitle(FinanceApplication.constants().memo());
//		memoArea.setWidth(100);
//
//		
//		Button addLinksButt = new Button(companyConstants.addLinks());
//		linksText = new TextItem(companyConstants.links());
//		linksText.setColSpan(3);
//
//		linksText.setShowTitle(false);
//		// linksText.setWidth("*");
//
//		addButton = new Button(FinanceApplication.constants().add());
//		// addButton.setStyleName("addButton");
//		addButton.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				ClientContact clientContact = new ClientContact();
//				// clientContact.setName("");
//				gridView.addData(clientContact);
//			}
//		});
//
//		// For Editing taxAgency
//		if (takenTaxAgency != null) {
//
//			// Setting TaxAgency Name
//			taxAgencyText
//					.setValue(takenTaxAgency.getName() != null ? takenTaxAgency
//							.getName() : "");
//
//			// Setting File as
//			fileAsText
//					.setValue(takenTaxAgency.getFileAs() != null ? takenTaxAgency
//							.getFileAs()
//							: "");
//
//			// Setting AddressForm
//			addrsForm = new AddressForm(takenTaxAgency.getAddress());
//			// addrsForm.setWidth("95%");
//
//			// Setting Phone Fax Form
//			phoneFaxForm = new PhoneFaxForm(takenTaxAgency.getPhoneNumbers(),
//					takenTaxAgency.getFaxNumbers());
//			// phoneFaxForm.setWidth("95%");
//
//			// Setting Email Form
//			emailForm = new EmailForm(takenTaxAgency.getEmails(),
//					takenTaxAgency.getWebPageAddress());
//
//			// Setting Status Check
//			statusCheck.setValue(takenTaxAgency.isActive());
//
//			// // Setting Payment terms Combo
//			// selectedPaymentTerm = takenTaxAgency.getPaymentTerm();
//			// paymentTermsCombo.setPaymentTerms(selectedPaymentTerm);
//
//			// Setting Liability accounts Combo
//			// selectedAccount = takenTaxAgency.getLiabilityAccount();
//			// liabilityAccountCombo.setComboItem(selectedAccount);
//
//			// Setting contacts
//			Set<ClientContact> contactsOfEditableTaxAgency = takenTaxAgency
//					.getContacts();
//			ClientContact records[] = new ClientContact[contactsOfEditableTaxAgency
//					.size()];
//			int i = 0;
//			
//			ClientContact temp = null;
//			for (ClientContact contact : contactsOfEditableTaxAgency) {
//				records[i] = new ClientContact();
//				// 
//
//				if (contact.isPrimary()) {
//					temp = records[i];
//					records[i].setPrimary(Boolean.TRUE);
//				}
//				records[i].setTitle(contact.getTitle());
//				records[i].setName(contact.getName());
//				records[i].setBusinessPhone(contact.getBusinessPhone());
//				records[i++].setEmail(contact.getEmail());
//
//			}
//
//			// gridView.
//			gridView.setRecords(Arrays.asList(records));
//			gridView.setRecords(Arrays.asList(records));
//			// if (temp != null)
//			// gridView.selectRecord(temp);
//
//			// Setting Memo
//			memoArea.setValue(takenTaxAgency.getMemo() != null ? takenTaxAgency
//					.getMemo() : "");
//
//		} else { // For Creating TaxAgency
//			addrsForm = new AddressForm(null);
//			addrsForm.setWidth("100%");
//			phoneFaxForm = new PhoneFaxForm(null, null);
//			phoneFaxForm.setWidth("100%");
//			emailForm = new EmailForm(null, null);
//			emailForm.setWidth("100%");
//		}
//
//		addrsForm.setWidth("100%");
//		// phoneFaxForm.setWidth("100%");
//		// emailForm.setWidth("97%");
//
//		VerticalPanel leftVLay = new VerticalPanel();
//		leftVLay.setWidth("100%");
//		leftVLay.add(taxAgencyForm);
//		leftVLay.add(addrsForm);
//		leftVLay.add(phoneFaxForm);
//		VerticalPanel rightVLay = new VerticalPanel();
//		rightVLay.setWidth("100%");
//		rightVLay.add(emailForm);
//		rightVLay.add(accInfoForm);
//
//		HorizontalPanel topHLay = new HorizontalPanel();
//		topHLay.setWidth("100%");
//		topHLay.add(leftVLay);
//		topHLay.add(rightVLay);
//		HorizontalPanel contHLay = new HorizontalPanel();
//		contHLay.setSpacing(5);
//		contHLay.add(contacts);
//
//		memoForm.setFields(memoArea);
//
//		VerticalPanel mainVlay = new VerticalPanel();
//		mainVlay.add(lab);
//		mainVlay.add(topHLay);
//		mainVlay.add(contHLay);
//		mainVlay.add(addButton);
//		mainVlay.add(gridView);
//		mainVlay.add(memoForm);
//
//		/* Adding dynamic forms in list */
//		listforms.add(taxAgencyForm);
//		listforms.add(accInfoForm);
//		listforms.add(memoForm);
//
//		return mainVlay;
//	}
//
//	private void initListGrid() {
//		gridView = new ContactGrid();
//		gridView.setCanEdit(true);
//		gridView.init();
//
//	}
//
//	@Override
//	public void init() {
//		super.init();
//		createControls();
//		setSize("100%", "100%");
//	}
//
//	@Override
//	public void initData() {
//
//		initPaymentTermsCombo();
//		initLiabilityAccounts();
//		super.initData();
//
//	}
//
//	/*
//	 * This method initialzes the combo with accounts.And selects a default
//	 * account
//	 */
//	private void initLiabilityAccounts() {
//
//		liabilityAccountCombo.initCombo(liabilityAccountCombo.getAccounts());
//		if (takenTaxAgency != null
//				&& (takenTaxAgency.getLiabilityAccount()) != null) {
//			selectedAccount = FinanceApplication.getCompany().getAccount(
//					takenTaxAgency.getLiabilityAccount());
//			liabilityAccountCombo.setComboItem(selectedAccount);
//		} else {
//			selectedAccount = FinanceApplication.getCompany().getAccountByName(
//					messages.SALES_TAX_PAYABLE);
//			liabilityAccountCombo.setComboItem(selectedAccount);
//		}
//
//	}
//
//	public static TaxAgencyView getInstance() {
//
//		return new TaxAgencyView();
//	}
//
//	@Override
//	public void setData(ClientTaxAgency data) {
//		super.setData(data);
//		if (data != null)
//			takenTaxAgency = data;
//	}
//
//	protected void adjustFormWidths(int titlewidth, int listBoxWidth) {
//
//		addrsForm.getCellFormatter().getElement(0, 0).setAttribute(
//				FinanceApplication.constants().width(),
//				titlewidth + "");
//		addrsForm.getCellFormatter().getElement(0, 1).setAttribute(
//				FinanceApplication.constants().width(),
//				listBoxWidth + "");
//
//		phoneFaxForm.getCellFormatter().getElement(0, 0).setAttribute(
//				FinanceApplication.constants().width(),
//				titlewidth + "");
//		phoneFaxForm.getCellFormatter().getElement(0, 1).setAttribute(
//				FinanceApplication.constants().width(),
//				listBoxWidth + "");
//
//		taxAgencyForm.getCellFormatter().getElement(0, 0).getStyle().setWidth(
//				titlewidth + listBoxWidth, Unit.PX);
//		emailForm.getCellFormatter().getElement(0, 0).setAttribute(
//				FinanceApplication.constants().width(),
//				titlewidth + titlewidth + "");
//		emailForm.getCellFormatter().getElement(0, 1).setAttribute(
//				FinanceApplication.constants().width(),
//				listBoxWidth + "");
//		accInfoForm.getCellFormatter().getElement(0, 0).setAttribute(
//				FinanceApplication.constants().width(),
//				listBoxWidth + "");
//		memoForm.getCellFormatter().getElement(0, 0).setAttribute(
//				FinanceApplication.constants().width(),
//				titlewidth + "");
//
//	}
//
//	@Override
//	protected void onLoad() {
//		int titlewidth = phoneFaxForm.getCellFormatter().getElement(0, 0)
//				.getOffsetWidth();
//		int listBoxWidth = phoneFaxForm.getCellFormatter().getElement(0, 1)
//				.getOffsetWidth();
//
//		adjustFormWidths(titlewidth, listBoxWidth);
//		super.onLoad();
//	}
//
//	@Override
//	protected void onAttach() {
//
//		int titlewidth = phoneFaxForm.getCellFormatter().getElement(0, 0)
//				.getOffsetWidth();
//		int listBoxWidth = phoneFaxForm.getCellFormatter().getElement(0, 1)
//				.getOffsetWidth();
//
//		adjustFormWidths(titlewidth, listBoxWidth);
//
//		super.onAttach();
//	}
//
//	public List<DynamicForm> getForms() {
//
//		return listforms;
//	}
//
//	/**
//	 * call this method to set focus in View
//	 */
//	@Override
//	public void setFocus() {
//		this.taxAgencyText.setFocus();
//	}
//
//	@Override
//	public void deleteFailed(AccounterException caught) {
//		
//
//	}
//
//	@Override
//	public void deleteSuccess(IAccounterCore result){
//		
//
//	}
//
//	@Override
//	public void fitToSize(int height, int width) {
//		
//
//	}
//
//	@Override
//	public void processupdateView(IAccounterCore core, int command) {
//		
//		switch (command) {
//		case AccounterCommand.CREATION_SUCCESS:
//
//			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
//				this.paymentTermsCombo.addComboItem((ClientPaymentTerms) core);
//
//			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
//				this.liabilityAccountCombo.addComboItem((ClientAccount) core);
//
//			break;
//
//		case AccounterCommand.DELETION_SUCCESS:
//
//			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
//				this.paymentTermsCombo
//						.removeComboItem((ClientPaymentTerms) core);
//
//			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
//				this.liabilityAccountCombo
//						.removeComboItem((ClientAccount) core);
//
//			break;
//		case AccounterCommand.UPDATION_SUCCESS:
//			break;
//		}
//	}
//
//	@Override
//	public void onEdit() {
//		
//		
//	}
//
//	@Override
//	public void print() {
//		
//		
//	}
//
//	@Override
//	public void printPreview() {
//		
//		
//	}
// }
