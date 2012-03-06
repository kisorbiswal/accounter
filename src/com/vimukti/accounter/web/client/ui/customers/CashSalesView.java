package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * gg
 * 
 * @author Fernandez
 * 
 */
public class CashSalesView extends
		AbstractCustomerTransactionView<ClientCashSales> implements
		IPrintableView {
	private ShippingTermsCombo shippingTermsCombo;
	private TAXCodeCombo taxCodeSelect;
	private SalesPersonCombo salesPersonCombo;
	private Double salesTax = 0.0D;
	protected DateField deliveryDate;
	private ArrayList<DynamicForm> listforms;
	private ShipToForm shipToAddress;
	private CustomerAccountTransactionTable customerAccountTransactionTable;
	private CustomerItemTransactionTable customerItemTransactionTable;
	private ClientPriceLevel priceLevel;
	private ClientSalesPerson salesPerson;
	private AmountLabel netAmountLabel;
	private TaxItemsForm taxTotalNonEditableText;
	private Double transactionTotal = 0.0D;
	private AddNewButton accountTableButton, itemTableButton;
	private DisclosurePanel accountsDisclosurePanel;
	private DisclosurePanel itemsDisclosurePanel;

	private TextItem checkNoText;
	private CheckboxItem printCheck;
	private boolean isChecked = false;

	// private WarehouseAllocationTable inventoryTransactionTable;
	// private DisclosurePanel inventoryDisclosurePanel;

	public CashSalesView() {
		super(ClientTransaction.TYPE_CASH_SALES);
	}

	private void initCashSalesView() {

		ArrayList<ClientShippingTerms> shippingTerms = getCompany()
				.getShippingTerms();

		shippingTermsCombo.initCombo(shippingTerms);

		initDepositInAccounts();
		initShippingMethod();

	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(messages.cashSale());
		lab1.setStyleName("label-title");
		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						deliveryDate.setEnteredDate(date);
						setTransactionDate(date);
					}
				});

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		locationCombo = createLocationCombo();
		if (!isTemplate) {
			dateNoForm.setFields(transactionDateItem, transactionNumber);
		}

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(messages.payeeName(Global.get()
				.Customer()));
		customerCombo.setRequired(false);
		contactCombo = createContactComboItem();

		phoneSelect = new TextItem(messages.phone());
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isInViewMode());

		billToTextArea = new TextAreaItem(messages.billTo());
		billToTextArea.setDisabled(true);
		shipToAddress = new ShipToForm(null);
		shipToAddress.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		// shipToAddress.getCellFormatter().getElement(0, 0)
		// .setAttribute(messages.width(), "40px");
		shipToAddress.getCellFormatter().addStyleName(0, 1, "memoFormAlign");
		shipToAddress.addrArea.setDisabled(true);
		shipToAddress.businessSelect.setDisabled(true);
		shipToAddress.businessSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						shippingAddress = shipToAddress.getAddress();
						if (shippingAddress != null)
							shipToAddress.setAddres(shippingAddress);
						else
							shipToAddress.addrArea.setValue("");
					}
				});

		custForm = UIUtils.form(Global.get().customer());
		custForm.setFields(customerCombo, contactCombo, phoneSelect,
				billToTextArea);
		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// custForm.getCellFormatter().setWidth(0, 0, "226px");
		custForm.setNumCols(2);
		custForm.setStyleName("align-form");
		// custForm.setWidth("100%");
		salesPersonCombo = createSalesPersonComboItem();
		paymentMethodCombo = createPaymentMethodSelectItem();

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
		printCheck.setVisible(false);
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
		checkNoText.setVisible(false);
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
		depositInCombo = createDepositInComboItem(null);
		depositInCombo.setPopupWidth("500px");
		shippingTermsCombo = createShippingTermsCombo();
		shippingMethodsCombo = createShippingMethodCombo();
		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());

		DynamicForm termsForm = new DynamicForm();
		// termsForm.setWidth("100%");
		termsForm.setIsGroup(true);
		termsForm.setNumCols(2);
		if (locationTrackingEnabled)
			termsForm.setFields(locationCombo);
		if (getPreferences().isSalesPersonEnabled()) {
			termsForm.setFields(salesPersonCombo, paymentMethodCombo,
					printCheck, checkNoText, depositInCombo);
			if (getPreferences().isDoProductShipMents()) {
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
			}
		} else {
			termsForm.setFields(paymentMethodCombo, printCheck, checkNoText,
					depositInCombo);
			if (getPreferences().isDoProductShipMents()) {
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
			}
		}

		termsForm.setStyleName("align-form");
		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			termsForm.setFields(classListCombo);
		}
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo = createJobListCombo();
			jobListCombo.setDisabled(true);
			termsForm.setFields(jobListCombo);
		}

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(160);

		taxCodeSelect = createTaxCodeSelectItem();

		// priceLevelSelect = createPriceLevelSelectItem();

		// refText = createRefereceText();
		// refText.setWidth(160);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");

		taxTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		netAmountLabel = createNetAmountLabel();

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getCompany()
				.getPrimaryCurrency());
		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		customerAccountTransactionTable = new CustomerAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashSalesView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashSalesView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashSalesView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				CashSalesView.this.updateNonEditableItems();
			}
		};

		customerAccountTransactionTable.setDisabled(isInViewMode());

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
		accountFlowPanel.add(customerAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");

		customerItemTransactionTable = new CustomerItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashSalesView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashSalesView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashSalesView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				CashSalesView.this.updateNonEditableItems();
			}
		};
		customerItemTransactionTable.setDisabled(isInViewMode());
		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		FlowPanel itemsFlowPanel = new FlowPanel();
		itemsDisclosurePanel = new DisclosurePanel(
				messages.ItemizebyProductService());
		itemsFlowPanel.add(customerItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");

		// Inventory table..
		// inventoryTransactionTable = new WarehouseAllocationTable();
		// inventoryTransactionTable.setDesable(isInViewMode());
		// FlowPanel inventoryFlowPanel = new FlowPanel();
		// inventoryDisclosurePanel = new
		// DisclosurePanel("Warehouse Allocation");
		// inventoryFlowPanel.add(inventoryTransactionTable);
		// inventoryDisclosurePanel.setContent(inventoryFlowPanel);
		// inventoryDisclosurePanel.setWidth("100%");
		// ---Inverntory table-----

		// final TextItem disabletextbox = new TextItem();
		// disabletextbox.setVisible(false);

		DynamicForm taxForm = new DynamicForm();
		VerticalPanel nonEditablePanel = new VerticalPanel();
		nonEditablePanel.setWidth("100%");
		nonEditablePanel.addStyleName("boldtext");

		DynamicForm netAmountForm = new DynamicForm();
		netAmountForm.setNumCols(2);

		discountField = getDiscountField();
		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		if (isTrackTax()) {
			netAmountForm.setFields(netAmountLabel);
			nonEditablePanel.add(netAmountForm);
			nonEditablePanel.add(taxTotalNonEditableText);
			if (!isTaxPerDetailLine()) {
				taxForm.setFields(taxCodeSelect);
			}
			taxForm.setFields(vatinclusiveCheck);
		}
		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				taxForm.setFields(discountField);
			}
		}
		totalForm.setFields(transactionTotalBaseCurrencyText);
		if (isMultiCurrencyEnabled()) {
			totalForm.setFields(foreignCurrencyamountLabel);
		}
		nonEditablePanel.add(totalForm);
		nonEditablePanel.addStyleName("boldtext");

		nonEditablePanel.setCellHorizontalAlignment(netAmountForm, ALIGN_RIGHT);
		nonEditablePanel.setCellHorizontalAlignment(taxTotalNonEditableText,
				ALIGN_RIGHT);
		nonEditablePanel.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);

		currencyWidget = createCurrencyFactorWidget();

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(taxForm);
		prodAndServiceHLay.add(nonEditablePanel);
		prodAndServiceHLay.setCellHorizontalAlignment(nonEditablePanel,
				ALIGN_RIGHT);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(ALIGN_RIGHT);
		vPanel.setWidth("100%");

		vPanel.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);
		if (getPreferences().isDoProductShipMents())
			leftVLay.add(shipToAddress);
		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay.setWidth("100%");
		rightVLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		rightVLay.add(termsForm);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			rightVLay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.setSpacing(20);

		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);

		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		mainVLay.add(vPanel);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(netAmountForm);
		listforms.add(totalForm);

		settabIndexes();
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}

	}

	protected ShippingTermsCombo createShippingTermsCombo() {

		final ShippingTermsCombo shippingTermsCombo = new ShippingTermsCombo(
				messages.shippingTerms());
		shippingTermsCombo.setHelpInformation(true);
		shippingTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientShippingTerms selectItem) {
						shippingTerm = selectItem;
						if (shippingTerm != null && shippingTermsCombo != null) {
							shippingTermsCombo.setComboItem(getCompany()
									.getShippingTerms(shippingTerm.getID()));
							shippingTermsCombo.setDisabled(isInViewMode());
						}
					}

				});

		shippingTermsCombo.setDisabled(isInViewMode());
		return shippingTermsCombo;
	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null) {
			return;
		}
		ClientCurrency currency = getCurrency(customer.getCurrency());
		// Job Tracking
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setDisabled(isInViewMode());
			jobListCombo.setValue("");
			jobListCombo.setCustomer(customer);
		}
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientCashSales ent = this.transaction;

			if (ent != null && ent.getCustomer() == customer.getID()) {
				this.customerAccountTransactionTable
						.setAllRows(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.customerItemTransactionTable
						.setAllRows(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getCustomer() != customer.getID()) {
				this.customerAccountTransactionTable.updateTotals();
				this.customerItemTransactionTable.updateTotals();
			} else if (ent == null)
				this.customerAccountTransactionTable.resetRecords();
		}

		super.customerSelected(customer);
		if (this.shippingTerm != null && shippingTermsCombo != null)
			shippingTermsCombo.setComboItem(this.shippingTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (this.taxCode != null && taxCodeSelect != null
				&& taxCodeSelect.getValue() != ""
				&& !taxCodeSelect.getName().equalsIgnoreCase(messages.none()))
			taxCodeSelect.setComboItem(this.taxCode);

		if (customer.getPhoneNo() != null)
			phoneSelect.setValue(customer.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {

			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(customer.getAddress());
		shipToAddress.setAddress(addresses);
		this.setCustomer(customer);
		if (customer != null) {
			customerCombo.setComboItem(customer);
		}

		if (currency.getID() != 0) {
			if (currency != null) {
				currencyWidget.setSelectedCurrencyFactorInWidget(currency,
						transactionDateItem.getDate().getDate());
			}
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}

	}

	private void shippingTermSelected(ClientShippingTerms shippingTerm2) {
		this.shippingTerm = shippingTerm2;
		if (shippingTerm != null && shippingTermsCombo != null) {
			shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
					shippingTerm.getID()));
			shippingTermsCombo.setDisabled(isInViewMode());
		}
	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		this.salesPerson = person;
		if (salesPerson != null && salesPersonCombo != null) {

			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

		}

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		// if (priceLevel != null && priceLevelSelect != null) {
		//
		// priceLevelSelect.setComboItem(getCompany().getPriceLevel(
		// priceLevel.getID()));
		//
		// }
		if (this.transaction == null || customerItemTransactionTable != null) {
			customerItemTransactionTable.setPricingLevel(priceLevel);
			// customerTransactionTable.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();

		saveOrUpdate(transaction);

	}

	@Override
	public ClientCashSales saveView() {
		ClientCashSales saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
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

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (taxCode != null && transactionItems != null) {

			for (ClientTransactionItem item : transactionItems) {
				// if (taxCode instanceof ClientTAXItem)
				// item.setTaxItem((ClientTAXItem) taxCode);
				// if (taxCode instanceof ClientTAXGroup)
				// item.setTaxGroup((ClientTAXGroup) taxCode);
				item.setTaxCode(taxCode.getID());

			}
		}
		if (!getPreferences().isClassPerDetailLine() && accounterClass != null
				&& transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setAccounterClass(accounterClass.getID());
			}
		}
		if (discountField.getAmount() != 0) {
			transaction.setDiscountTotal(discountField.getAmount());
		}
		transaction.setTransactionDate(transactionDate.getDate());
		if (getCustomer() != null)
			transaction.setCustomer(getCustomer().getID());
		transaction.setPaymentMethod(paymentMethodCombo.getSelectedValue());
		if (checkNoText.getValue() != null
				&& !checkNoText.getValue().equals("")) {
			transaction.setCheckNumber(getCheckValue());
		} else
			transaction.setCheckNumber("");

		if (depositInAccount != null)
			transaction.setDepositIn(depositInAccount.getID());
		if (contact != null)
			transaction.setContact(contact);
		transaction.setPhone(phoneSelect.getValue().toString());
		if (salesPerson != null) {
			transaction.setSalesPerson(salesPerson.getID());
		}
		if (billingAddress != null)
			transaction.setBillingAddress(billingAddress);
		if (shippingAddress != null)
			transaction.setShippingAdress(shippingAddress);
		if (shippingTerm != null)
			transaction.setShippingTerm(shippingTerm.getID());
		if (shippingMethod != null)
			transaction.setShippingMethod(shippingMethod.getID());
		if (deliveryDate != null && deliveryDate.getEnteredDate() != null)
			transaction
					.setDeliverydate(deliveryDate.getEnteredDate().getDate());

		if (priceLevel != null)
			transaction.setPriceLevel(priceLevel.getID());
		transaction.setMemo(getMemoTextAreaItem());
		if (isTrackTax()) {
			transaction.setNetAmount(netAmountLabel.getAmount());
			setAmountIncludeTAX();
			transaction.setTaxTotal(salesTax);
		}

		transaction.setTotal(foreignCurrencyamountLabel.getAmount());
		if (getPreferences().isJobTrackingEnabled()) {
			if (jobListCombo.getSelectedValue() != null)
				transaction.setJob(jobListCombo.getSelectedValue().getID());
		}
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		// if (getCompany().getPreferences().isInventoryEnabled()
		// && getCompany().getPreferences().iswareHouseEnabled())
		// transaction.setWareHouseAllocations(inventoryTransactionTable
		// .getAllRows());
	}

	@Override
	public void updateNonEditableItems() {

		if (customerAccountTransactionTable == null
				|| customerItemTransactionTable == null)
			return;

		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : customerAccountTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
			for (ClientTransactionItem item : customerItemTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}
		double lineTotal = customerAccountTransactionTable.getLineTotal()
				+ customerItemTransactionTable.getLineTotal();
		double totalTax = customerAccountTransactionTable.getTotalTax()
				+ customerItemTransactionTable.getTotalTax();
		double total = customerAccountTransactionTable.getGrandTotal()
				+ customerItemTransactionTable.getGrandTotal();
		if (getCompany().getPreferences().isTrackTax()) {
			netAmountLabel.setAmount(lineTotal);
			setSalesTax(totalTax);
		}

		setTransactionTotal(total);

		// } else {
		// double lineTotal = customerAccountTransactionTable.getLineTotal()
		// + customerItemTransactionTable.getLineTotal();
		// double grandTotal = customerAccountTransactionTable.getGrandTotal()
		// + customerItemTransactionTable.getGrandTotal();
		// double totalTax = customerAccountTransactionTable.getTotalTax()
		// + customerItemTransactionTable.getTotalTax();
		// netAmountLabel.setAmount(lineTotal);
		// taxTotalNonEditableText.setAmount(grandTotal - lineTotal);
		// if (getCompany().getPreferences().isTrackTax()) {
		// netAmountLabel.setAmount(lineTotal);
		// setSalesTax(totalTax);
		// }
		// setTransactionTotal(grandTotal);
		// }

	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		this.transactionTotal = transactionTotal;
		if (transactionTotalBaseCurrencyText != null) {
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transactionTotal));
			foreignCurrencyamountLabel.setAmount(transactionTotal);
		}

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCashSales());
		} else {
			if (isMultiCurrencyEnabled()) {
				if (transaction.getCurrency() > 0) {
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
			ClientCompany company = getCompany();
			initTransactionsItems();
			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			customerSelected(this.customer);
			if (this.getCustomer() != null) {

				this.contacts = getCustomer().getContacts();
			}
			this.contact = transaction.getContact();
			this.phoneNo = transaction.getPhone();
			phoneSelect.setValue(this.phoneNo);

			this.billingAddress = transaction.getBillingAddress();
			this.shippingAddress = transaction.getShippingAdress();

			this.priceLevel = company
					.getPriceLevel(transaction.getPriceLevel());

			this.salesPerson = company.getSalesPerson(transaction
					.getSalesPerson());
			this.shippingTerm = company.getShippingTerms(transaction
					.getShippingTerm());
			this.shippingMethod = company.getShippingMethod(transaction
					.getShippingMethod());
			this.depositInAccount = company.getAccount(transaction
					.getDepositIn());

			initTransactionNumber();
			if (getCustomer() != null) {
				customerCombo.setComboItem(getCustomer());
			}
			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			if (getCustomer() != null)
				addresses.addAll(getCustomer().getAddress());
			shipToAddress.setListOfCustomerAdress(addresses);
			if (shippingAddress != null) {
				shipToAddress.businessSelect.setValue(shippingAddress
						.getAddressTypes().get(shippingAddress.getType()));
				shipToAddress.setAddres(shippingAddress);
			}
			if (billingAddress != null) {

				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");
			contactSelected(this.contact);
			priceLevelSelected(this.priceLevel);
			salesPersonSelected(this.salesPerson);
			shippingMethodSelected(this.shippingMethod);
			shippingTermSelected(shippingTerm);
			depositInAccountSelected(this.depositInAccount);

			this.transactionItems = transaction.getTransactionItems();
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			paymentMethodSelected(transaction.getPaymentMethod());

			if (transaction.getDeliverydate() != 0)
				this.deliveryDate.setEnteredDate(new ClientFinanceDate(
						transaction.getDeliverydate()));

			if (transaction.getID() != 0) {
				setMode(EditMode.VIEW);
			}
			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(cashSale.getReference());
			if (isTrackTax()) {
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
				}
				if (isTaxPerDetailLine()) {
					netAmountLabel.setAmount(transaction.getNetAmount());
					taxTotalNonEditableText.setTransaction(transaction);
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect
								.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
						taxCodeSelected(taxCode);
					}
					taxTotalNonEditableText.setTransaction(transaction);
				}
			}
			if (isTrackClass()) {
				if (!isClassPerDetailLine()) {
					this.accounterClass = getClassForTransactionItem(this.transactionItems);
					if (accounterClass != null) {
						this.classListCombo.setComboItem(accounterClass);
						classSelected(accounterClass);
					}
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

			memoTextAreaItem.setDisabled(true);

			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));

			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		if (getPreferences().isJobTrackingEnabled()) {
			if (customer != null) {
				jobListCombo.setCustomer(customer);
			}
			jobSelected(Accounter.getCompany().getjob(transaction.getJob()));
		}
		superinitTransactionViewData();
		initCashSalesView();

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void superinitTransactionViewData() {

		initTransactionNumber();

		initCustomers();

		ArrayList<ClientSalesPerson> salesPersons = getCompany()
				.getActiveSalesPersons();

		salesPersonCombo.initCombo(salesPersons);

		ArrayList<ClientPriceLevel> priceLevels = getCompany().getPriceLevels();

		// priceLevelSelect.initCombo(priceLevels);

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

		initTransactionsItems();

	}

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customerCombo.initCombo(result);
		customerCombo.setDisabled(isInViewMode());

	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			updateNonEditableItems();
	}

	@Override
	protected void initMemoAndReference() {

		if (this.transaction != null) {

			ClientCashSales cashSales = transaction;

			if (cashSales != null) {

				memoTextAreaItem
						.setValue(cashSales.getMemo() != null ? cashSales
								.getMemo() : "");
				memoTextAreaItem.setDisabled(isInViewMode());
				// refText.setValue(cashSales.getReference() != null ? cashSales
				// .getReference() : "");

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transaction != null) {
			Double salesTaxAmout = transaction.getTaxTotla();
			setSalesTax(salesTaxAmout);

		}

	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;

		if (taxTotalNonEditableText != null) {
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
				transaction.setTransactionItems(customerAccountTransactionTable
						.getAllRows());
				transaction.getTransactionItems().addAll(
						customerItemTransactionTable.getAllRows());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			taxTotalNonEditableText.setTransaction(transaction);
		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {

		if (transaction != null) {
			this.transactionTotal = transaction.getTotal();
			this.transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(this.transactionTotal));
			this.foreignCurrencyamountLabel.setAmount(this.transactionTotal);

		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. paymentMethodCombo validation
		// 2. depositInCombo validation i.e form items
		result.add(FormItem.validate(this.paymentMethodCombo,
				this.depositInCombo));
		result.add(customerAccountTransactionTable.validateGrid());
		result.add(customerItemTransactionTable.validateGrid());

		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect,
							messages.pleaseSelect(messages.taxCode()));
				}

			}
		}
		ClientAccount bankAccount = depositInCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());

			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(depositInCombo,
						messages.selectProperBankAccount());
			}
		}
		return result;

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
		this.customerCombo.setFocus();
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
	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(Global.get().messages()
							.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));
				}
			}

			@Override
			public void onSuccess(Boolean result) {
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
		customerCombo.setDisabled(isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNoText.setValue(messages.toBePrinted());
		}
		depositInCombo.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());

		shippingTermsCombo.setDisabled(isInViewMode());
		shippingMethodsCombo.setDisabled(isInViewMode());

		shipToAddress.businessSelect.setDisabled(isInViewMode());

		deliveryDate.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		// priceLevelSelect.setDisabled(isInViewMode());
		customerAccountTransactionTable.setDisabled(isInViewMode());
		customerItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		discountField.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (shippingTermsCombo != null)
			shippingTermsCombo.setDisabled(isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}
		classListCombo.setDisabled(isInViewMode());
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setDisabled(isInViewMode());
		}
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		super.onEdit();
	}

	@Override
	protected void paymentMethodSelected(String paymentmethod) {
		this.paymentMethod = paymentmethod;
		if (paymentMethod == null) {
			return;
		}

		if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
			printCheck.setDisabled(isInViewMode());
			checkNoText.setDisabled(isInViewMode());
			printCheck.setVisible(true);
			checkNoText.setVisible(true);
		} else {
			// paymentMethodCombo.setComboItem(paymentMethod);
			printCheck.setDisabled(true);
			printCheck.setVisible(false);
			checkNoText.setDisabled(true);
			checkNoText.setVisible(false);
		}

	}

	@Override
	public void print() {
		updateTransaction();
		ArrayList<ClientBrandingTheme> themesList = Accounter.getCompany()
				.getBrandingTheme();
		if (themesList.size() > 1) {
			// if there are more than one branding themes, then show branding
			// theme combo box
			ActionFactory.getBrandingThemeComboAction().run(transaction, false);
		} else {
			// if there is only one branding theme
			ClientBrandingTheme clientBrandingTheme = themesList.get(0);
			UIUtils.downloadAttachment(transaction.getID(),
					ClientTransaction.TYPE_CASH_SALES,
					clientBrandingTheme.getID());
		}
	}

	@Override
	public void printPreview() {

	}

	private void resetFormView() {
		// custForm.getCellFormatter().setWidth(0, 1, "200px");
		// custForm.setWidth("75%");
		// priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");
		// memoTextAreaItem.setWidth("200px");
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect
					.setComboItem(getCompany().getTAXCode(taxCode.getID()));
			customerAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			customerItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

	@Override
	protected String getViewTitle() {
		return messages.cashSales();
	}

	@Override
	protected void initTransactionsItems() {
		if (transaction.getTransactionItems() != null) {
			List<ClientTransactionItem> list = getAccountTransactionItems(transaction
					.getTransactionItems());
			if (!list.isEmpty()) {
				customerAccountTransactionTable.setAllRows(list);
			}
			list = getItemTransactionItems(transaction.getTransactionItems());
			if (!list.isEmpty()) {
				customerItemTransactionTable.setAllRows(list);
			}
		}
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));
	}

	@Override
	protected boolean isBlankTransactionGrid() {
		return getAllTransactionItems().isEmpty();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		customerAccountTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
		customerAccountTransactionTable.updateTotals();
		customerItemTransactionTable.updateTotals();
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(customerAccountTransactionTable.getRecords());
		list.addAll(customerItemTransactionTable.getRecords());
		return list;
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billToTextArea.setTabIndex(4);
		transactionDateItem.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		paymentMethodCombo.setTabIndex(7);
		depositInCombo.setTabIndex(8);
		memoTextAreaItem.setTabIndex(9);
		// menuButton.setTabIndex(10);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(11);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(12);

		cancelButton.setTabIndex(13);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		customerAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		customerItemTransactionTable.add(item);
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		customerItemTransactionTable.updateAmountsFromGUI();
		customerAccountTransactionTable.updateAmountsFromGUI();
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
		netAmountLabel.setTitle(messages.currencyNetAmount(formalName));
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getAmount() != null) {
			customerAccountTransactionTable.setDiscount(discountField
					.getAmount());
			customerItemTransactionTable.setDiscount(discountField.getAmount());
		} else {
			discountField.setAmount(0d);
		}
	}

	@Override
	public boolean canPrint() {
		EditMode mode = getMode();
		if (mode == EditMode.CREATE || mode == EditMode.EDIT
				|| data.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean canExportToCsv() {
		return false;
	}

	@Override
	protected void classSelected(ClientAccounterClass accounterClass) {
		this.accounterClass = accounterClass;
		if (accounterClass != null) {
			classListCombo.setComboItem(accounterClass);
			customerAccountTransactionTable.setClass(accounterClass.getID(),
					true);
			customerItemTransactionTable.setClass(accounterClass.getID(), true);
		} else {
			classListCombo.setValue("");
		}
	}

}
