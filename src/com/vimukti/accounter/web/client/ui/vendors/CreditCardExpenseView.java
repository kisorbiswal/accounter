package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CreditCardExpenseView extends
		AbstractBankTransactionView<ClientCreditCardCharge> {

	VendorCombo Ccard;

	protected List<String> selectedComboList;
	protected DateField date, delivDate;;
	protected TextItem cheqNoText;
	// protected TextItem refText;
	AmountField totText;
	AccounterConstants accounterConstants = Accounter.constants();
	List<String> idPhoneNumberForContacts = new ArrayList<String>();
	List<String> idNamesForContacts = new ArrayList<String>();

	protected DynamicForm vendorForm, addrForm, phoneForm, termsForm, memoForm;
	protected SelectCombo payMethSelect;
	// VendorCombo vendorNameSelect;

	private TextAreaItem addrArea;

	protected PayFromAccountsCombo payFrmSelect;

	protected String selectPaymentMethod;

	// protected ClientVendor selectedVendor;

	private DynamicForm totForm;

	private HorizontalPanel botPanel, addLinkPanel;
	HorizontalPanel totPanel;

	private VerticalPanel leftVLay, botVLay;

	private ArrayList<DynamicForm> listforms;
	protected ClientContact contact;
	protected Label titlelabel;
	protected TextAreaItem billToAreaItem;
	private List<ClientAccount> listOfAccounts;

	private boolean locationTrackingEnabled;

	private VendorTransactionTable vendorTransactionTable;

	public CreditCardExpenseView() {

		super(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	//
	// @Override
	// protected void initViewType() {
	//
	// titlelabel.setText(Accounter.constants().creditCardExpense());
	//
	// vendorForm.clear();
	// termsForm.clear();
	// Ccard = new VendorCombo(Accounter.constants().supplierName(), true) {
	// @Override
	// public void initCombo(List<ClientVendor> list) {
	// Iterator<ClientVendor> iterator = list.iterator();
	// while (iterator.hasNext()) {
	// ClientVendor vdr = iterator.next();
	// if (vdr.getVendorGroup() != 0) {
	// ClientVendorGroup vendorGrougp = Accounter.getCompany()
	// .getVendorGroup(vdr.getVendorGroup());
	// if (!vendorGrougp.getName().equals(
	// AccounterClientConstants.CREDIT_CARD_COMPANIES)) {
	// iterator.remove();
	// }
	// } else {
	// iterator.remove();
	// }
	//
	// }
	// super.initCombo(list);
	// }
	//
	// @Override
	// public void onAddNew() {
	// NewVendorAction action = ActionFactory.getNewVendorAction();
	//
	// action.setCallback(new ActionCallback<ClientVendor>() {
	//
	// @Override
	// public void actionResult(ClientVendor result) {
	// if (result.getDisplayName() != null)
	// addItemThenfireEvent(result);
	//
	// }
	// });
	// action.setOpenedFrom(NewVendorAction.FROM_CREDIT_CARD_EXPENSE);
	// action.run(null, true);
	//
	// }
	// };
	// Ccard.setHelpInformation(true);
	// Ccard.addSelectionChangeHandler(new
	// IAccounterComboSelectionChangeHandler<ClientVendor>() {
	//
	// @Override
	// public void selectedComboBoxItem(ClientVendor selectItem) {
	// selectedVendor = selectItem;
	// Ccard.setComboItem(selectItem);
	// addPhonesContactsAndAddress();
	// }
	// });
	//
	// Ccard.setRequired(true);
	// String listString[] = new String[] {
	// Accounter.constants().cash(),
	// UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
	// .constants().check()),
	// Accounter.constants().creditCard(),
	// Accounter.constants().directDebit(),
	// Accounter.constants().masterCard(),
	// Accounter.constants().onlineBanking(),
	// Accounter.constants().standingOrder(),
	// Accounter.constants().switchMaestro() };
	//
	// selectedComboList = new ArrayList<String>();
	// for (int i = 0; i < listString.length; i++) {
	// selectedComboList.add(listString[i]);
	// }
	// payMethSelect.initCombo(selectedComboList);
	//
	// termsForm.setFields(payMethSelect, payFrmSelect, cheqNoText, delivDate);
	// HorizontalPanel hPanel = (HorizontalPanel) termsForm.getParent();
	// termsForm.removeFromParent();
	// termsForm.setWidth("100%");
	// termsForm.getCellFormatter().getElement(0, 0)
	// .setAttribute(Accounter.constants().width(), "203px");
	// hPanel.add(termsForm);
	//
	// if (isEdit) {
	// ClientCreditCardCharge creditCardCharge = (ClientCreditCardCharge)
	// transaction;
	// Ccard.setComboItem(getCompany().getVendor(
	// creditCardCharge.getVendor()));
	// Ccard.setDisabled(true);
	// }
	// vendorForm.setFields(Ccard, contactNameSelect, phoneSelect,
	// billToAreaItem);
	// vendorForm.getCellFormatter().setWidth(0, 0, "180px");
	// vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
	// VerticalPanel verticalPanel = (VerticalPanel) vendorForm.getParent();
	// vendorForm.removeFromParent();
	// verticalPanel.add(vendorForm);
	// // verticalPanel.setSpacing(10);
	//
	// }

	@Override
	protected void createControls() {
		String vendorString = null;

		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			vendorString = Accounter.messages().vendorName(
					Global.get().Vendor());
		} else if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			vendorString = Accounter.messages().vendorName(
					Global.get().Vendor());
		}

		Ccard = new VendorCombo(vendorString, true) {
			@Override
			public void initCombo(List<ClientVendor> list) {
				List<ClientVendor> ccVendors = new ArrayList<ClientVendor>();
				for (ClientVendor vdr : list) {
					if (vdr.getVendorGroup() != 0) {
						ClientVendorGroup vendorGrougp = Accounter.getCompany()
								.getVendorGroup(vdr.getVendorGroup());
						if (vendorGrougp.getName().equals(
								AccounterClientConstants.CREDIT_CARD_COMPANIES)) {
							ccVendors.add(vdr);
						}
					}
				}
				super.initCombo(ccVendors);
			}

			@Override
			public void onAddNew() {
				NewVendorAction action = ActionFactory.getNewVendorAction();

				action.setCallback(new ActionCallback<ClientVendor>() {

					@Override
					public void actionResult(ClientVendor result) {
						if (result.getName() != null)
							addItemThenfireEvent(result);

					}
				});
				action.setOpenedFrom(NewVendorAction.FROM_CREDIT_CARD_EXPENSE);
				action.run(null, true);

			}
		};
		Ccard.setHelpInformation(true);
		Ccard.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

			@Override
			public void selectedComboBoxItem(ClientVendor selectItem) {
				selectedVendor = selectItem;
				Ccard.setComboItem(selectItem);
				addPhonesContactsAndAddress();
			}
		});

		Ccard.setRequired(true);
		String listString[] = new String[] {
				Accounter.constants().cash(),
				UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
						.constants().check()),
				Accounter.constants().creditCard(),
				Accounter.constants().directDebit(),
				Accounter.constants().masterCard(),
				Accounter.constants().onlineBanking(),
				Accounter.constants().standingOrder(),
				Accounter.constants().switchMaestro() };

		if (isInViewMode()) {
			ClientCreditCardCharge creditCardCharge = (ClientCreditCardCharge) transaction;
			Ccard.setComboItem(getCompany().getVendor(
					creditCardCharge.getVendor()));
			Ccard.setDisabled(true);
		}
		// vendorForm.setFields(Ccard, contactNameSelect, phoneSelect,
		// billToAreaItem);
		// vendorForm.getCellFormatter().setWidth(0, 0, "180px");
		// vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// VerticalPanel verticalPanel = (VerticalPanel) vendorForm.getParent();
		// vendorForm.removeFromParent();
		// verticalPanel.add(vendorForm);
		// verticalPanel.setSpacing(10);

		titlelabel = new Label(Accounter.constants().creditCardCharge());
		titlelabel.removeStyleName("gwt-Label");
		titlelabel.addStyleName(Accounter.constants().labelTitle());
		// titlelabel.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		if (locationTrackingEnabled)
			dateNoForm.setFields(locationCombo);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			dateNoForm.setFields(classListCombo);
		}

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		VerticalPanel regPanel = new VerticalPanel();
		regPanel.setCellHorizontalAlignment(dateNoForm, ALIGN_RIGHT);
		regPanel.add(dateNoForm);
		regPanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.add(regPanel);
		labeldateNoLayout.setCellHorizontalAlignment(regPanel, ALIGN_RIGHT);

		contactCombo = new ContactCombo(Accounter.constants().contact(), true);
		contactCombo.setHelpInformation(true);
		contactCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientContact>() {

					public void selectedComboBoxItem(ClientContact selectItem) {

						contactSelected(selectItem);

					}

				});
		contactCombo.addNewContactHandler(new ValueCallBack<ClientContact>() {

			@Override
			public void execute(ClientContact value) {
				addContactToVendor(value);
			}
		});
		contactCombo.setDisabled(isInViewMode());

		// formItems.add(contactCombo);

		// contactNameSelect = new
		// SelectCombo(Accounter.constants().contactName());
		// contactNameSelect.setHelpInformation(true);
		// contactNameSelect
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		// contactNameSelect.setSelected(selectItem);
		//
		// int i = 0;
		// while (i < idNamesForContacts.size()) {
		// String s = idNamesForContacts.get(i);
		// if (s.equals(selectItem))
		// phoneSelect.setValue(idPhoneNumberForContacts
		// .get(i));
		//
		// i++;
		// }
		//
		// }
		// });
		// contactNameSelect.setWidth(100);
		// formItems.add(contactNameSelect);
		// billToCombo = createBillToComboItem();
		billToAreaItem = new TextAreaItem(Accounter.constants().billTo());
		billToAreaItem.setWidth(100);
		billToAreaItem.setDisabled(true);
		// formItems.add(billToCombo);
		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		// formItems.add(phoneSelect);

		vendorForm = UIUtils.form(Accounter.constants().vendor());
		vendorForm.setWidth("100%");
		vendorForm.setFields(Ccard, contactCombo, phoneSelect, billToAreaItem);
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		vendorForm.getCellFormatter().setWidth(0, 0, "180px");

		payMethSelect = createPaymentMethodSelectItem();
		payMethSelect.setTitle(Accounter.constants().paymentMethod());
		payMethSelect.setWidth(90);
		List<String> paymentMthds = new ArrayList<String>();
		paymentMthds.add(Accounter.constants().creditCard());
		payMethSelect.initCombo(paymentMthds);
		payMethSelect.setDefaultToFirstOption(true);
		payMethSelect.setDisabled(true);
		// payMethSelect.setComboItem(UIUtils
		// .getpaymentMethodCheckBy_CompanyType(Accounter.constants()
		// .check()));

		payFrmSelect = createPayFromselectItem();
		payFrmSelect.setWidth(90);
		payFrmSelect.setPopupWidth("510px");
		payFrmSelect.setTitle(Accounter.constants().payFrom());
		payFromAccount = 0;
		payFrmSelect.setColSpan(0);
		// formItems.add(payFrmSelect);

		cheqNoText = new TextItem(
				getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? Accounter
						.constants().chequeNo() : Accounter.constants()
						.checkNo());
		cheqNoText.setHelpInformation(true);
		cheqNoText.setDisabled(isInViewMode());
		cheqNoText.setWidth(100);
		// formItems.add(cheqNoText);

		delivDate = new DateField(Accounter.constants().deliveryDate());
		delivDate.setHelpInformation(true);
		delivDate.setColSpan(1);
		delivDate.setValue(new ClientFinanceDate());
		// formItems.add(delivDate);

		termsForm = UIUtils.form(Accounter.constants().terms());
		termsForm.setWidth("100%");
		termsForm.setFields(payMethSelect, payFrmSelect, delivDate);
		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "203px");

		Label lab2 = new Label(Accounter.constants().itemsAndExpenses());

		AddNewButton addButton = createAddNewButton();// new
		// Button(FinanceApplication

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue(Accounter.constants().atozero());
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		vatinclusiveCheck = new CheckboxItem(Accounter.constants()
				.amountIncludesVat());
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vendorTransactionTable = new VendorTransactionTable() {

			@Override
			protected void updateNonEditableItems() {
				CreditCardExpenseView.this.updateNonEditableItems();
			}

			@Override
			protected ClientTransaction getTransactionObject() {
				return CreditCardExpenseView.this.getTransactionObject();
			}

			@Override
			protected ClientVendor getSelectedVendor() {
				return CreditCardExpenseView.this.getSelectedVendor();
			}
		};
		vendorTransactionTable.setDisabled(isInViewMode());

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		// refText = new TextItem(Accounter.constants().reference());
		//
		// refText.setWidth(100);
		// refText.setDisabled(false);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);

		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("invoice-total");
		// totText = new AmountField(FinanceApplication.constants()
		// .total());
		// totText.setWidth(100);

		totForm = new DynamicForm();
		totForm.setWidth("100%");
		totForm.addStyleName("unused-payments");
		totForm.getElement().getStyle().setMarginTop(10, Unit.PX);

		botPanel = new HorizontalPanel();
		botPanel.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(createAddNewButton());
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			totalForm.setFields(netAmount, vatTotalNonEditableText,
					transactionTotalNonEditableText);
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.setWidth("100%");
			vPanel.add(panel);
			vPanel.add(totalForm);

			botPanel.add(memoForm);
			botPanel.add(totalForm);
			botPanel.setCellWidth(totalForm, "30%");

			bottompanel.add(vPanel);
			bottompanel.add(botPanel);

			// totalForm.setFields(netAmount, vatTotalNonEditableText,
			// transactionTotalNonEditableText);
			// // botPanel.add(memoForm);
			// botPanel.add(vPanel);
			// botPanel.add(vatCheckform);
			// botPanel.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// botPanel.add(totalForm);
			// botPanel.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			totForm.setFields(transactionTotalNonEditableText);

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setWidth("100%");
			hPanel.add(memoForm);
			hPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
			hPanel.add(totForm);
			hPanel.setCellHorizontalAlignment(totForm, ALIGN_RIGHT);

			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setWidth("100%");
			vpanel.add(panel);
			vpanel.setCellHorizontalAlignment(panel, ALIGN_RIGHT);
			vpanel.add(hPanel);

			bottompanel.add(vpanel);
		}

		leftVLay = new VerticalPanel();
		// leftVLay.setWidth("80%");
		leftVLay.add(vendorForm);

		HorizontalPanel rightHLay = new HorizontalPanel();
		// rightHLay.setWidth("80%");
		rightHLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		rightHLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.setSpacing(20);
		topHLay.setCellHorizontalAlignment(rightHLay, ALIGN_RIGHT);
		topHLay.add(rightHLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightHLay, "42%");

		VerticalPanel vLay1 = new VerticalPanel();
		// vLay1.add(lab2);
		// vLay1.add(addButton);
		// multi currency combo
		vLay1.add(vendorTransactionTable);
		vLay1.setWidth("100%");
		vLay1.add(bottompanel);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(titlelabel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(vLay1);

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);
		listforms.add(totForm);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		if (isInViewMode()) {
			payFrmSelect.setComboItem(getCompany().getAccount(
					transaction.getPayFrom()));
		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		if (Ccard.getSelectedValue() == null)
			result.addError(
					Ccard,
					Accounter.messages().pleaseSelectVendor(
							Global.get().vendor()));

		if (payFrmSelect.getSelectedValue() == null)
			result.addError(payFrmSelect, Accounter.messages()
					.pleaseSelectVendor(Accounter.constants().payFrom()));
		return result;
	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCreditCardCharge());
			resetElements();
			initpayFromAccountCombo();
		} else {
			ClientVendor vendor = getCompany().getVendor(
					transaction.getVendor());
			// if (vendor != null) {
			// vendorNameSelect.setComboItem(vendor);
			// phoneSelect.setValue(vendor.getPhoneNo());
			// }
			transactionDateItem.setValue(transaction.getDate());
			contact = transaction.getContact();
			if (contact != null) {
				contactCombo.setValue(contact.getName());
			}
			transactionDateItem.setValue(transaction.getDate());
			transactionDateItem.setDisabled(isInViewMode());
			transactionNumber.setValue(transaction.getNumber());
			transactionNumber.setDisabled(isInViewMode());
			delivDate.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));
			delivDate.setDisabled(isInViewMode());
			phoneSelect.setValue(transaction.getPhone());
			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				netAmount.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
			}
			transactionTotalNonEditableText.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}
			if (transaction.getPayFrom() != 0)
				payFromAccountSelected(transaction.getPayFrom());
			payFrmSelect.setComboItem(getCompany().getAccount(payFromAccount));
			payFrmSelect.setDisabled(isInViewMode());
			cheqNoText.setDisabled(isInViewMode());
			cheqNoText.setValue(transaction.getCheckNumber());
			paymentMethodSelected(transaction.getPaymentMethod());
			payMethSelect.setComboItem(transaction.getPaymentMethod());
			payMethSelect.setDisabled(isInViewMode());
			cheqNoText.setDisabled(isInViewMode());
			vendorTransactionTable.removeAllRecords();
			vendorTransactionTable
					.setAllRows(transaction.getTransactionItems());

		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		initTransactionNumber();
		addVendorsList();
	}

	private void initpayFromAccountCombo() {

		// listOfAccounts = Utility.getPayFromAccounts(FinanceApplication
		// .getCompany());
		// getPayFromAccounts();
		listOfAccounts = payFrmSelect.getAccounts();

		payFrmSelect.initCombo(listOfAccounts);
		payFrmSelect.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFrmSelect.setAccounts();
		payFrmSelect.setDisabled(isInViewMode());

		account = payFrmSelect.getSelectedValue();

		if (account != null)
			payFrmSelect.setComboItem(account);
	}

	private void addVendorsList() {
		List<ClientVendor> result = getCompany().getActiveVendors();
		if (result != null) {
			initVendorsList(result);

		}
	}

	protected void initVendorsList(List<ClientVendor> result) {
		// First identify existing selected vendor
		for (ClientVendor vendor : result) {
			if (isInViewMode())
				if (vendor.getID() == transaction.getVendor()) {
					selectedVendor = vendor;
				}
		}
		Ccard.initCombo(result);

		if (isInViewMode()) {
			Ccard.setComboItem(selectedVendor);
			billToaddressSelected(selectedVendor.getSelectedAddress());
			addPhonesContactsAndAddress();
		}
		Ccard.setDisabled(isInViewMode());
	}

	protected void addPhonesContactsAndAddress() {
		// Set<Address> allAddress = selectedVendor.getAddress();
		addressList = selectedVendor.getAddress();
		initBillToCombo();
		// billToCombo.setDisabled(isEdit);
		Set<ClientContact> allContacts;
		allContacts = selectedVendor.getContacts();
		Iterator<ClientContact> it = allContacts.iterator();
		// List<String> phones = new ArrayList<String>();
		ClientContact primaryContact = null;

		int i = 0;
		while (it.hasNext()) {
			ClientContact contact = it.next();
			if (contact.isPrimary())
				primaryContact = contact;
			idNamesForContacts.add(contact.getName());
			idPhoneNumberForContacts.add(contact.getBusinessPhone());
			// phones.add(contact.getBusinessPhone());
			i++;
		}

		// contactCombo.initCombo(idNamesForContacts);

		// phoneSelect.initCombo(phones);

		// ClientVendor cv = FinanceApplication.getCompany().getVendor(
		// creditCardChargeTaken.getVendor());
		if (transaction.getContact() != null)
			contactCombo.setSelected(transaction.getContact().getName());
		if (transaction.getPhone() != null)
			// FIXME check and fix the below code
			phoneSelect.setValue(transaction.getPhone());

		contactCombo.setDisabled(isInViewMode());
		phoneSelect.setDisabled(isInViewMode());
		return;
	}

	private void resetElements() {
		selectedVendor = null;
		// transaction = null;
		billingAddress = null;
		addressList = null;
		// billToCombo.setDisabled(isEdit);
		paymentMethod = UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
				.constants().check());
		payFromAccount = 0;
		// phoneSelect.setValueMap("");
		setMemoTextAreaItem("");
		// refText.setValue("");
		cheqNoText.setValue("");

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		// setting date
		if (transactionDateItem != null)

			transaction.setDate((transactionDateItem.getValue()).getDate());
		// setting number
		if (transactionNumber != null)
			transaction.setNumber(transactionNumber.getValue().toString());
		ClientVendor vendor = Ccard.getSelectedValue();
		if (vendor != null)
			transaction.setVendor(vendor.getID());
		// setting contact
		if (contact != null) {
			transaction.setContact(contact);
		}
		// if (contactNameSelect.getValue() != null) {
		// // ClientContact contact = getContactBasedOnId(contactNameSelect
		// // .getValue().toString());
		// transaction
		// .setContact(getContactBasedOnId(contactNameSelect
		// .getValue().toString()));
		//
		// }

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting payment method

		transaction.setPaymentMethod(paymentMethod);

		// Setting pay from
		if (payFrmSelect.getSelectedValue() != null)
			payFromAccount = payFrmSelect.getSelectedValue().getID();
		if (payFromAccount != 0)
			transaction.setPayFrom(getCompany().getAccount(payFromAccount)
					.getID());

		// setting check no
		if (cheqNoText.getValue() != null)
			transaction.setCheckNumber(cheqNoText.getValue().toString());

		if (vatinclusiveCheck != null) {
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		}

		// setting delivery date
		transaction.setDeliveryDate(UIUtils.toDate(delivDate.getValue()));

		// Setting transactions
		transaction.setTransactionItems(vendorTransactionTable.getAllRows());

		// setting total
		transaction.setTotal(vendorTransactionTable.getTotal());
		// setting memo
		transaction.setMemo(getMemoTextAreaItem());
		// setting ref
		// transaction.setReference(UIUtils.toStr(refText.getValue()));

		if (selectedVendor != null) {

			// setting vendor
			transaction.setVendor(selectedVendor.getID());

			// setting contact
			if (contact != null) {
				transaction.setContact(contact);
			}
		}
	}

	public void createAlterObject() {
		saveOrUpdate((ClientCreditCardCharge) transaction);

	}

	/*
	 * @Override public ValidationResult validate() { ValidationResult result =
	 * super.validate();
	 * 
	 * if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
	 * result.addError(transactionDate,
	 * accounterConstants.invalidateTransactionDate()); }
	 * 
	 * if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
	 * result.addError(transactionDate,
	 * accounterConstants.invalidateTransactionDate()); }
	 * 
	 * result.add(vendorForm.validate()); result.add(termsForm.validate()); if
	 * (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
	 * result.addError(vendorTransactionGrid,
	 * accounterConstants.blankTransaction()); }
	 * result.add(vendorTransactionGrid.validateGrid()); return result; }
	 */

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		// payMethSelect.setDisabled(isEdit);
		if (paymentMethod.equals(Accounter.constants().check())
				|| paymentMethod.equals(Accounter.constants().cheque())) {
			cheqNoText.setDisabled(isInViewMode());
		} else {
			cheqNoText.setDisabled(!isInViewMode());
		}
		delivDate.setDisabled(isInViewMode());
		// billToCombo.setDisabled(isEdit);
		Ccard.setDisabled(isInViewMode());
		contactCombo.setDisabled(isInViewMode());
		phoneSelect.setDisabled(isInViewMode());
		payFrmSelect.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		vendorTransactionTable.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		super.onEdit();
	}

	@Override
	public void showMenu(Widget button) {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button,
					Accounter.messages().accounts(Global.get().Account()),
					Accounter.constants().serviceItem());
		else
			setMenuItems(button,
					Accounter.messages().accounts(Global.get().Account()),
					Accounter.constants().serviceItem());
	}

	public void saveAndUpdateView() {

		updateTransaction();

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			transaction.setNetAmount(netAmount.getAmount());
		// creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		super.saveAndUpdateView();

		createAlterObject();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().creditCardExpense();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected void initMemoAndReference() {
		if (isInViewMode()) {
			memoTextAreaItem.setDisabled(true);
			setMemoTextAreaItem(((ClientCreditCardCharge) transaction)
					.getMemo());
		}
	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNonEditableItems() {

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			transactionTotalNonEditableText.setAmount(vendorTransactionTable
					.getTotal());
			netAmount.setAmount(vendorTransactionTable.getGrandTotal());
			vatTotalNonEditableText.setAmount(vendorTransactionTable.getTotal()
					- vendorTransactionTable.getGrandTotal());
		} else {
			transactionTotalNonEditableText.setAmount(vendorTransactionTable
					.getTotal());
		}

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	/**
	 * @param value
	 */
	protected void addContactToVendor(final ClientContact contact) {
		final ClientVendor selectedVendor = Ccard.getSelectedValue();
		if (selectedVendor == null) {
			return;
		}
		selectedVendor.addContact(contact);
		AccounterAsyncCallback<Long> asyncallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				caught.printStackTrace();
			}

			public void onResultSuccess(Long result) {
				selectedVendor.setVersion(selectedVendor.getVersion() + 1);
				contactSelected(contact);
			}

		};
		Accounter.createCRUDService().update(selectedVendor, asyncallBack);
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
		// vendorTransactionTable.refreshAllRecords();
	}

	@Override
	public void setFocus() {
		this.Ccard.setFocus();

	}
}
