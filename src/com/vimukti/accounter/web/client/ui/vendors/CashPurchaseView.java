package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author vimukti1
 * 
 */
public class CashPurchaseView extends
		AbstractVendorTransactionView<ClientCashPurchase> {

	protected DynamicForm vendorForm;
	protected DynamicForm termsForm;
	public List<String> selectedComboList;
	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;
	private TextAreaItem billToAreaItem;
	protected VendorAccountTransactionTable vendorAccountTransactionTable;
	protected VendorItemTransactionTable vendorItemTransactionTable;
	protected AddNewButton accountTableButton, itemTableButton;
	protected DisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;
	private TextItem checkNoText;
	private CheckboxItem printCheck;
	private boolean isChecked = false;

	// private WarehouseAllocationTable inventoryTransactionTable;
	// private DisclosurePanel inventoryDisclosurePanel;

	public CashPurchaseView() {
		super(ClientTransaction.TYPE_CASH_PURCHASE);
	}

	protected CashPurchaseView(int type) {
		super(type);
	}

	@Override
	protected void createControls() {

		titlelabel = new Label(messages.cashPurchase());
		titlelabel.setStyleName("label-title");
		// titlelabel.setHeight("50px");
		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							deliveryDateItem.setEnteredDate(date);
							setTransactionDate(date);
						}
					}
				});
		transactionDateItem.setWidth(100);

		transactionNumber = createTransactionNumberItem();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		if (!isTemplate) {
			dateNoForm.setFields(transactionDateItem, transactionNumber);
		}
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.setHorizontalAlignment(ALIGN_RIGHT);
		labeldateNoLayout.setCellHorizontalAlignment(datepanel, ALIGN_RIGHT);
		labeldateNoLayout.add(datepanel);

		if (this.isInViewMode())
			// --the form need to be disabled here
			dateNoForm.setDisabled(true);

		// formItems.add(transactionDateItem);
		// formItems.add(transactionNumber);

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(false);
		// vendorCombo.setWidth(100);
		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		// contactCombo.setWidth(100);
		billToAreaItem = new TextAreaItem(messages.billTo());
		billToAreaItem.setHelpInformation(true);
		billToAreaItem.setWidth(100);
		billToAreaItem.setDisabled(true);
		phoneSelect = new TextItem(messages.phone());
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		if (isInViewMode())
			phoneSelect.setDisabled(true);

		vendorForm = UIUtils.form(Global.get().Vendor());

		// vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect,
				billToAreaItem);
		// vendorForm.getCellFormatter().setWidth(0, 0, "160px");
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// formItems.add(contactCombo);
		// formItems.add(billToCombo);

		payFromCombo = createPayFromCombo(messages.payFrom());
		// payFromCombo.setWidth(100);
		payFromCombo.setPopupWidth("500px");
		// checkNo = createCheckNumberItem(messages.chequeNo());
		// checkNo.setDisabled(true);
		// checkNo.setWidth(100);
		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);

		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		paymentMethodCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentMethodSelected(paymentMethodCombo
								.getSelectedValue());
					}
				});

		printCheck = new CheckboxItem(messages.toBePrinted());
		printCheck.setValue(true);
		printCheck.setWidth(100);
		printCheck.setDisabled(true);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNoText.setValue(messages.toBePrinted());
						checkNoText.setDisabled(true);
					} else {
						if (payFromCombo.getValue() == null)
							checkNoText.setValue(messages.toBePrinted());
						else if (transaction != null) {
							checkNoText.setValue(transaction.getCheckNumber());
						}
					}
				} else
					checkNoText.setValue("");
				checkNoText.setDisabled(false);

			}
		});

		checkNoText = new TextItem(messages.chequeNo());
		checkNoText.setValue(messages.toBePrinted());
		checkNoText.setHelpInformation(true);
		checkNoText.setWidth(100);
		if (paymentMethodCombo.getSelectedValue() != null
				&& !paymentMethodCombo.getSelectedValue().equals(
						UIUtils.getpaymentMethodCheckBy_CompanyType(messages
								.check())))
			checkNoText.setDisabled(true);
		checkNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNoText.getValue().toString();
			}
		});

		termsForm = new DynamicForm();
		if (locationTrackingEnabled)
			termsForm.setFields(locationCombo);
		// termsForm.setWidth("100%");
		if (isTemplate) {
			termsForm.setFields(paymentMethodCombo, printCheck, checkNoText,
					payFromCombo);
		} else {
			termsForm.setFields(paymentMethodCombo, printCheck, checkNoText,
					payFromCombo, deliveryDateItem);
		}

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		// termsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "203px");

		// formItems.add(checkNo);
		// formItems.add(deliveryDateItem);

		Label lab2 = new Label(messages.itemsAndExpenses());
		// menuButton = createAddNewButton();

		netAmount = new AmountLabel(
				messages.currencyNetAmount(getBaseCurrency().getFormalName()));
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem(getCompany()
				.getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		taxCodeSelect = createTaxCodeSelectItem();
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashPurchaseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashPurchaseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashPurchaseView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				CashPurchaseView.this.updateNonEditableItems();
			}
		};

		vendorAccountTransactionTable.setDisabled(isInViewMode());

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});

		FlowPanel accountFlowPanel = new FlowPanel();
		accountsDisclosurePanel = new DisclosurePanel(
				messages.ItemizebyAccount());
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");

		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashPurchaseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashPurchaseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashPurchaseView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				CashPurchaseView.this.updateNonEditableItems();
			}
		};

		vendorItemTransactionTable.setDisabled(isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});
		currencyWidget = createCurrencyFactorWidget();
		FlowPanel itemsFlowPanel = new FlowPanel();
		itemsDisclosurePanel = new DisclosurePanel(
				messages.ItemizebyProductService());
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");

		// Inventory table..
		// inventoryTransactionTable = new WarehouseAllocationTable();
		// inventoryTransactionTable.setDesable(isInViewMode());
		//
		// FlowPanel inventoryFlowPanel = new FlowPanel();
		// inventoryDisclosurePanel = new
		// DisclosurePanel("Warehouse Allocation");
		// inventoryFlowPanel.add(inventoryTransactionTable);
		// inventoryDisclosurePanel.setContent(inventoryFlowPanel);
		// inventoryDisclosurePanel.setWidth("100%");
		// ---Inverntory table-----

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);

		DynamicForm memoForm = new DynamicForm();
		// memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		discountField = getDiscountField();

		VerticalPanel totalForm = new VerticalPanel();
		totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		// rightVLay.setWidth("100%");
		rightVLay.add(termsForm);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			rightVLay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}

		DynamicForm form = new DynamicForm();

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);

		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		DynamicForm transactionTotalForm = new DynamicForm();
		transactionTotalForm.setNumCols(2);

		if (isTrackTax() && isTrackPaidTax()) {
			DynamicForm netAmountForm = new DynamicForm();
			netAmountForm.setNumCols(2);
			netAmountForm.setFields(netAmount);
			totalForm.add(netAmountForm);
			totalForm.add(vatTotalNonEditableText);
			totalForm.setCellHorizontalAlignment(netAmountForm, ALIGN_RIGHT);
			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.setFields(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.setFields(transactionTotalNonEditableText);
			}
			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setWidth("100%");
			vpanel.setHorizontalAlignment(ALIGN_RIGHT);
			vpanel.add(totalForm);

			bottomLayout.add(memoForm);
			if (!isTaxPerDetailLine()) {
				// taxCodeSelect.setVisible(isInViewMode());
				form.setFields(taxCodeSelect);
			}
			form.setFields(vatinclusiveCheck);
			bottomLayout.add(form);
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					form.setFields(discountField);
					bottomLayout.add(form);
				}

			}
			bottomLayout.add(totalForm);
			bottomLayout.setCellWidth(totalForm, "30%");

			bottompanel.add(vpanel);
			bottompanel.add(bottomLayout);

			// VerticalPanel vPanel = new VerticalPanel();
			// vPanel.add(menuButton);
			// vPanel.add(memoForm);
			// vPanel.setWidth("100%");
			//
			// bottomLayout.add(vPanel);
			// bottomLayout.add(vatCheckform);
			// bottomLayout.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// bottomLayout.add(totalForm);
			// bottomLayout.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			memoForm.setStyleName("align-form");
			bottomLayout.add(memoForm);
			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.setFields(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.setFields(transactionTotalNonEditableText);
			}

			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					form.setFields(discountField);
					bottomLayout.add(form);
				}

			}

			bottomLayout.add(totalForm);
			bottompanel.add(bottomLayout);
		}
		totalForm.add(transactionTotalForm);
		totalForm.setCellHorizontalAlignment(transactionTotalForm, ALIGN_RIGHT);
		totalForm.setCellHorizontalAlignment(vatTotalNonEditableText,
				ALIGN_RIGHT);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(titlelabel);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);

		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		mainVLay.add(bottompanel);

		// setOverflow(Overflow.SCROLL);
		this.add(mainVLay);
		// addChild(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(transactionTotalForm);

		// if (UIUtils.isMSIEBrowser())
		// resetFormView();
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}
		initViewType();

		settabIndexes();

	}

	@Override
	protected void accountSelected(ClientAccount account) {

		if (account == null) {
			payFromCombo.setValue("");
			return;
		}
		this.payFromAccount = account;
		payFromCombo.setComboItem(payFromAccount);
		if (account != null
				&& paymentMethod.equalsIgnoreCase(messages.cheque())
				&& isInViewMode()) {
			ClientCashPurchase cashPurchase = transaction;
			// setCheckNumber();
		}
	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientCashPurchase());
		} else {
			if (currencyWidget != null) {
				if (transaction.getCurrency() > 1) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}
			super.vendorSelected(getCompany()
					.getVendor(transaction.getVendor()));
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			this.billingAddress = transaction.getVendorAddress();
			if (billingAddress != null) {

				billToAreaItem.setValue(getValidAddress(billingAddress));

			} else
				billToAreaItem.setValue("");
			// paymentMethodSelected(cashPurchaseToBeEdited.getPaymentMethod()
			// !=
			// null ? cashPurchaseToBeEdited
			// .getPaymentMethod()
			// : "");
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			if (transaction.getPaymentMethod().equals(messages.check())) {
				printCheck.setDisabled(isInViewMode());
				checkNoText.setDisabled(isInViewMode());
			} else {
				printCheck.setDisabled(true);
				checkNoText.setDisabled(true);
			}

			if (transaction.getCheckNumber() != null) {
				if (transaction.getCheckNumber().equals(messages.toBePrinted())) {
					checkNoText.setValue(messages.toBePrinted());
					printCheck.setValue(true);
				} else {
					checkNoText.setValue(transaction.getCheckNumber());
					printCheck.setValue(false);
				}
			}

			accountSelected(getCompany().getAccount(transaction.getPayFrom()));
			// transactionDateItem.setEnteredDate(cashPurchaseToBeEdited.get)
			initMemoAndReference();
			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					netAmount.setAmount(transaction.getNetAmount());
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					selectTAXCode();
				}
			}
			if (transaction.getTransactionItems() != null) {
				if (isTrackDiscounts()) {
					if (!isDiscountPerDetailLine()) {
						this.discountField.setAmount(getdiscount(transaction
								.getTransactionItems()));
					}
				}
			}

			// if (isTrackTax()) {
			// if (isTaxPerDetailLine()) {
			// netAmountLabel.setAmount(transaction.getNetAmount());
			// taxTotalNonEditableText.setAmount(transaction.getTotal()
			// - transaction.getNetAmount());
			// } else {
			// this.taxCode =
			// getTaxCodeForTransactionItems(this.transactionItems);
			// if (taxCode != null) {
			// this.taxCodeSelect
			// .setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			// }
			// this.taxTotalNonEditableText.setValue(String
			// .valueOf(transaction.getTaxTotla()));
			// }
			// }
			//
			transactionTotalNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(isAmountIncludeTAX());
			}
			initAccounterClass();
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		super.initTransactionViewData();
		initTransactionNumber();
		initPayFromAccounts();
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientCashPurchase ent = this.transaction;

			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorAccountTransactionTable
						.setRecords(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setRecords(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorAccountTransactionTable.resetRecords();
				this.vendorAccountTransactionTable.updateTotals();
				this.vendorItemTransactionTable.updateTotals();
			}
		}
		super.vendorSelected(vendor);
		if (vendor == null) {
			return;
		}
		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billToAreaItem.setValue(getValidAddress(billingAddress));
		} else
			billToAreaItem.setValue("");
		long code = vendor.getTAXCode();
		if (code == 0 && taxCodeSelect != null) {
			code = Accounter.getCompany().getDefaultTaxCode();
			taxCodeSelect.setComboItem(getCompany().getTAXCode(code));
		}

		long currency = vendor.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
					transactionDateItem.getDate().getDate());
		} else {
			ClientCurrency clientCurrency = getCompany().getPrimaryCurrency();
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}

		vendorAccountTransactionTable.setTaxCode(code, false);
		vendorItemTransactionTable.setTaxCode(code, false);

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(vendor.getCurrency()));
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
				printCheck.setDisabled(false);
				checkNoText.setDisabled(false);
			} else {
				// paymentMethodCombo.setComboItem(paymentMethod);
				printCheck.setDisabled(true);
				checkNoText.setDisabled(true);
			}
		}

	}

	// private void setDisableStateForCheckNo(String paymentMethod) {
	//
	// if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
	// checkNo.setDisabled(false);
	// } else {
	// checkNo.setValue("");
	// checkNo.setDisabled(true);
	//
	// }
	// if (isInViewMode()) {
	// if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
	// checkNo.setDisabled(false);
	// } else {
	// checkNo.setDisabled(true);
	// }
	// }
	// }

	@Override
	public void saveAndUpdateView() {
		updateTransaction();

		super.saveAndUpdateView();

		createAlterObject();
	}

	@Override
	public ClientCashPurchase saveView() {
		ClientCashPurchase saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CASH_PURCHASE);

		if (this.getVendor() != null) {
			// Setting Vendor
			transaction.setVendor(this.getVendor().getID());
		}

		// Setting Contact
		if (contact != null)
			transaction.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress((billingAddress));

		// Setting Phone
		// if (phoneNo != null)
		transaction.setPhone(phoneSelect.getValue().toString());

		// Setting Payment Methods
		transaction.setPaymentMethod(paymentMethodCombo.getValue().toString());
		if (checkNoText.getValue() != null
				&& !checkNoText.getValue().equals("")) {
			transaction.setCheckNumber(getCheckValue());
		} else
			transaction.setCheckNumber("");

		// transaction.setIsToBePrinted(isChecked);

		// Setting Pay From Account
		transaction
				.setPayFrom(payFromCombo.getSelectedValue() != null ? payFromCombo
						.getSelectedValue().getID() : 0);

		// transaction
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getDate());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());
		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// cashPurchase.setReference(getRefText());
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		// if (getCompany().getPreferences().isInventoryEnabled()
		// && getCompany().getPreferences().iswareHouseEnabled())
		// transaction.setWareHouseAllocations(inventoryTransactionTable
		// .getAllRows());
		if (isTrackDiscounts()) {
			if (discountField.getAmount() != 0.0 && transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getAmount());
				}
			}
		}

		if (isTrackPaidTax()) {
			transaction.setNetAmount(netAmount.getAmount());
			setAmountIncludeTAX();
		}
	}

	private String getCheckValue() {
		String value;
		if (!isInViewMode()) {
			if (checkNoText.getValue().equals(messages.toBePrinted())) {
				value = String.valueOf(messages.toBePrinted());

			} else
				value = String.valueOf(checkNoText.getValue());
		} else {
			String checknumber;
			checknumber = this.checkNumber;
			if (checknumber == null) {
				checknumber = messages.toBePrinted();
			}
			if (checknumber.equals(messages.toBePrinted()))
				value = messages.toBePrinted();
			else
				value = String.valueOf(checknumber);
		}
		return value;
	}

	public void createAlterObject() {

		saveOrUpdate(transaction);

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(true);
		setMemoTextAreaItem(transaction.getMemo());
		// setRefText(((ClientCashPurchase) transactionObject).getReference());
	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : vendorAccountTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
			for (ClientTransactionItem item : vendorItemTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}
		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		netAmount.setAmount(lineTotal);
		if (getCompany().getPreferences().isTrackPaidTax()) {
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
				transaction.setTransactionItems(vendorAccountTransactionTable
						.getAllRows());
				transaction.getTransactionItems().addAll(
						vendorItemTransactionTable.getAllRows());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);
		}
		transactionTotalNonEditableText
				.setAmount(getAmountInBaseCurrency(grandTotal));
		foreignCurrencyamountLabel.setAmount(grandTotal);
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = super.validate();
		// Validations
		// 1. isValidTransactionDate?
		// 2. isInPreventPostingBeforeDate?
		// 3. form validations
		// 4. isValidDueOrDeliveryDate?
		// 5. isBlankTransaction?
		// 6. validateGrid?

		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}

		result.add(vendorForm.validate());
		result.add(termsForm.validate());

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				deliveryDateItem.getEnteredDate(), this.transactionDate)) {
			result.addError(deliveryDateItem,
					messages.the() + " " + messages.deliveryDate() + " " + " "
							+ messages.cannotbeearlierthantransactiondate());
		}

		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable,
					messages.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}
		return result;

	}

	public static CashPurchaseView getInstance() {
		return new CashPurchaseView();
	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorCombo.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	protected void onLoad() {
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
		vendorCombo.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNoText.setValue(messages.toBePrinted());
		}
		deliveryDateItem.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		discountField.setDisabled(isInViewMode());
		if (locationTrackingEnabled) {
			locationCombo.setDisabled(isInViewMode());
		}
		if (taxCodeSelect != null) {
			taxCodeSelect.setDisabled(isInViewMode());
		}
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}
		super.onEdit();

	}

	protected void initViewType() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return this.transactionTotalNonEditableText.getAmount();
	}

	private void resetFormView() {
		// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// vendorForm.setWidth("75%");
		// refText.setWidth("200px");

	}

	@Override
	protected String getViewTitle() {
		return messages.cashPurchases();
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else {
			taxCodeSelect.setValue("");
		}
	}

	@Override
	protected void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems) {
		vendorAccountTransactionTable
				.setRecords(getAccountTransactionItems(transactionItems));
		vendorItemTransactionTable
				.setRecords(getItemTransactionItems(transactionItems));
	}

	@Override
	protected void removeAllRecordsFromGrid() {
		vendorAccountTransactionTable.removeAllRecords();
		vendorItemTransactionTable.removeAllRecords();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);

	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void refreshTransactionGrid() {
		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billToAreaItem.setTabIndex(4);
		transactionDateItem.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		paymentMethodCombo.setTabIndex(7);
		payFromCombo.setTabIndex(8);
		checkNoText.setTabIndex(9);
		deliveryDateItem.setTabIndex(10);
		memoTextAreaItem.setTabIndex(11);
		// menuButton.setTabIndex(12);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(13);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(14);
		cancelButton.setTabIndex(15);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		vendorAccountTransactionTable.updateAmountsFromGUI();
		vendorItemTransactionTable.updateAmountsFromGUI();
	}

	public void modifyForeignCurrencyTotalWidget() {
		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(formalName));
		}
		netAmount.setTitle(messages.currencyNetAmount(formalName));
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getAmount() != null) {
			vendorItemTransactionTable.setDiscount(discountField.getAmount());
			vendorAccountTransactionTable
					.setDiscount(discountField.getAmount());
		} else {
			discountField.setAmount(0d);
		}
	}
}
