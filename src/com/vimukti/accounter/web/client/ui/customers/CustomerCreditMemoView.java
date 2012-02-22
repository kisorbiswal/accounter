package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
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
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class CustomerCreditMemoView extends
		AbstractCustomerTransactionView<ClientCustomerCreditMemo> implements
		IPrintableView {
	private ShippingTermsCombo shippingTermsCombo;
	// private PriceLevelCombo priceLevelSelect;
	private TAXCodeCombo taxCodeSelect;
	private SalesPersonCombo salesPersonCombo;
	private Double salesTax = 0.0D;
	private ArrayList<DynamicForm> listforms;
	private TextAreaItem billToTextArea;
	private CustomerAccountTransactionTable customerAccountTransactionTable;
	private CustomerItemTransactionTable customerItemTransactionTable;
	protected ClientPriceLevel priceLevel;
	protected ClientSalesPerson salesPerson;
	private AmountLabel netAmountLabel;
	private TaxItemsForm taxTotalNonEditableText;
	private AddNewButton accountTableButton, itemTableButton;
	private DisclosurePanel accountsDisclosurePanel;
	private DisclosurePanel itemsDisclosurePanel;

	public CustomerCreditMemoView() {

		super(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(messages.customerCreditNote(Global.get()
				.Customer()));
		lab1.setStyleName("label-title");
		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							try {
								ClientFinanceDate newDate = transactionDateItem
										.getValue();
								if (newDate != null)
									setTransactionDate(newDate);
							} catch (Exception e) {
								Accounter.showError(messages
										.invalidTransactionDate());
								setTransactionDate(new ClientFinanceDate());
								transactionDateItem
										.setEnteredDate(getTransactionDate());
							}

						}
						updateNonEditableItems();
					}
				});

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(messages.creditNo());
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		if (!isTemplate) {
			dateNoForm.setFields(transactionDateItem, transactionNumber);
		}
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(messages.payeeName(Global.get()
				.Customer()));

		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		billToTextArea = new TextAreaItem();
		billToTextArea.setHelpInformation(true);
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(messages.billTo());
		billToTextArea.setDisabled(true);

		custForm = UIUtils.form(Global.get().customer());
		custForm.setFields(customerCombo, contactCombo, billToTextArea);
		custForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		// custForm.getCellFormatter().getElement(0, 0)
		// .setAttribute(Global.get().constants().width(), "190px");
		// custForm.setWidth("100%");
		custForm.setStyleName("align-form");

		phoneSelect = new TextItem(messages.phone());
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isInViewMode());
		salesPersonCombo = createSalesPersonComboItem();

		DynamicForm phoneForm = UIUtils.form(messages.phoneNumber());
		// phoneForm.setWidth("100%");
		if (locationTrackingEnabled)
			phoneForm.setFields(locationCombo);
		phoneForm.setStyleName("align-form");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			phoneForm.setFields(classListCombo);
		}
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo = createJobListCombo();
			jobListCombo.setDisabled(true);
			phoneForm.setFields(jobListCombo);
		}
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setTitle(messages.reasonForIssue());

		taxCodeSelect = createTaxCodeSelectItem();

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		prodAndServiceForm1.setNumCols(1);
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);

		taxTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableLabel();
		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getCompany()
				.getPrimaryCurrency());
		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();

		customerAccountTransactionTable = new CustomerAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CustomerCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CustomerCreditMemoView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CustomerCreditMemoView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				CustomerCreditMemoView.this.updateNonEditableItems();
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
				isDiscountPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CustomerCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CustomerCreditMemoView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CustomerCreditMemoView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				CustomerCreditMemoView.this.updateNonEditableItems();
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
		currencyWidget = createCurrencyFactorWidget();
		FlowPanel itemsFlowPanel = new FlowPanel();
		itemsDisclosurePanel = new DisclosurePanel(
				messages.ItemizebyProductService());
		itemsFlowPanel.add(customerItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");

		VerticalPanel nonEditablePanel = new VerticalPanel();
		// prodAndServiceForm2.setWidth("100%");
		DynamicForm netAmountForm = new DynamicForm();
		netAmountForm.setNumCols(2);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);

		DynamicForm form = new DynamicForm();
		form.setWidth("100%");
		nonEditablePanel.addStyleName("boldtext");

		discountField = getDiscountField();

		if (isTrackTax()) {
			netAmountForm.setFields(netAmountLabel);
			nonEditablePanel.add(netAmountForm);
			nonEditablePanel.add(taxTotalNonEditableText);
			if (!isTaxPerDetailLine()) {
				form.setFields(taxCodeSelect);
			}
			form.setFields(vatinclusiveCheck);
		}

		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				form.setFields(discountField);
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

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setHorizontalAlignment(ALIGN_RIGHT);
		vpanel.setWidth("100%");

		vpanel.add(nonEditablePanel);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(form);
		prodAndServiceHLay.add(nonEditablePanel);
		// prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "50%");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.add(vpanel);
		mainPanel.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);

		VerticalPanel rightVLay = new VerticalPanel();
		// rightVLay.setWidth("100%");
		rightVLay.setCellHorizontalAlignment(phoneForm, ALIGN_RIGHT);
		rightVLay.add(phoneForm);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			rightVLay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setSize("100%", "100%");
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
		mainVLay.add(mainPanel);

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
		} else {
			memoTextAreaItem.setWidth("400px");
		}

		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(netAmountForm);
		listforms.add(totalForm);

		settabIndexes();
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
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
		//
		if (transaction == null && customerItemTransactionTable != null) {
			customerItemTransactionTable.setPricingLevel(priceLevel);
			// customerAccountTransactionTable.updatePriceLevel();
		}
		updateNonEditableItems();

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
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(transaction);

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
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
		return result;
	}

	@Override
	public ClientCustomerCreditMemo saveView() {
		ClientCustomerCreditMemo saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (taxCode != null && transactionItems != null) {

			for (ClientTransactionItem item : transactionItems) {
				item.setTaxCode(taxCode.getID());

			}
		}
		if (customer != null)
			transaction.setCustomer(getCustomer().getID());
		if (contact != null)
			transaction.setContact(contact);
		if (salesPerson != null)
			transaction.setSalesPerson(salesPerson.getID());
		if (phoneNo != null)
			transaction.setPhone(phoneNo);
		if (billingAddress != null)
			transaction.setBillingAddress(billingAddress);
		if (priceLevel != null)
			transaction.setPriceLevel(priceLevel.getID());
		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());
		if (isTrackTax()) {
			transaction.setNetAmount(netAmountLabel.getAmount());
			setAmountIncludeTAX();
			transaction.setTaxTotal(this.salesTax);
		}
		if (isTrackDiscounts()) {
			if (discountField.getAmount() != 0.0 && transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getAmount());
				}
			}
		}
		transaction.setTotal(foreignCurrencyamountLabel.getAmount());

		if (getPreferences().isJobTrackingEnabled()) {
			if (jobListCombo.getSelectedValue() != null)
				transaction.setJob(jobListCombo.getSelectedValue().getID());
		}
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCustomerCreditMemo());
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
			initTransactionsItems();
			this.setCustomer(getCompany()
					.getCustomer(transaction.getCustomer()));
			customerSelected(this.customer);
			this.billingAddress = transaction.getBillingAddress();
			this.contact = transaction.getContact();
			this.phoneNo = transaction.getPhone();
			phoneSelect.setValue(this.phoneNo);
			this.salesPerson = getCompany().getSalesPerson(
					transaction.getSalesPerson());
			this.priceLevel = getCompany().getPriceLevel(
					transaction.getPriceLevel());
			this.transactionItems = transaction.getTransactionItems();

			initTransactionNumber();
			if (getCustomer() != null)
				customerCombo.setComboItem(getCustomer());
			// billToaddressSelected(this.billingAddress);
			contactSelected(this.contact);
			priceLevelSelected(this.priceLevel);
			salesPersonSelected(this.salesPerson);
			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(creditToBeEdited.getReference());
			if (billingAddress != null) {
				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");

			if (isTrackTax()) {
				if (isTaxPerDetailLine()) {
					netAmountLabel.setAmount(transaction.getNetAmount());
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect
								.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
						taxCodeSelected(taxCode);
					}
				}
				taxTotalNonEditableText.setTransaction(transaction);
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
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

			netAmountLabel.setAmount(transaction.getNetAmount());
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));

			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			memoTextAreaItem.setDisabled(true);
			initAccounterClass();
		}
		superinitTransactionViewData();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		if (getPreferences().isJobTrackingEnabled()) {
			jobSelected(Accounter.getCompany().getjob(transaction.getJob()));
		}

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customerCombo.initCombo(result);
		customerCombo.setDisabled(isInViewMode());

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

	@Override
	protected void initMemoAndReference() {
		if (this.transaction != null) {

			ClientCustomerCreditMemo creditMemo = transaction;

			if (creditMemo.getMemo() != null) {

				memoTextAreaItem.setValue(creditMemo.getMemo());
				memoTextAreaItem.setDisabled(isInViewMode());
				// if (creditMemo.getReference() != null)
				// refText.setValue(creditMemo.getReference());

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transaction != null) {
			Double salesTaxAmout = transaction.getTaxTotal();
			if (salesTaxAmout != null) {
				taxTotalNonEditableText.setTransaction(transaction);
			}

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = transaction.getTotal();
			if (transactionTotal != null) {
				transactionTotalBaseCurrencyText
						.setAmount(getAmountInBaseCurrency(transactionTotal));
				foreignCurrencyamountLabel.setAmount(transactionTotal);
			}

		}

	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			updateNonEditableItems();
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
		double total = customerAccountTransactionTable.getGrandTotal()
				+ customerItemTransactionTable.getGrandTotal();
		setTransactionTotal(total);
		if (isTrackTax()) {
			double totalTax = customerAccountTransactionTable.getTotalTax()
					+ customerItemTransactionTable.getTotalTax();

			setSalesTax(totalTax);

			// salesTax = Utility.getCalculatedSalesTax(transactionDateItem
			// .getEnteredDate(), taxableLineTotal, taxItemGroup);
			// setSalesTax(salesTax);
			//
			// this.salesTaxTextNonEditable.setAmount(salesTax != null ?
			// salesTax
			// : 0.0D);
			//
			// this.transactionTotalNonEditableText
			// .setAmount(customerTransactionGrid.getTotal()
			// + this.salesTax);
			double lineTotal = customerAccountTransactionTable.getLineTotal()
					+ customerItemTransactionTable.getLineTotal();
			netAmountLabel.setAmount(lineTotal);
			setSalesTax(total - lineTotal);

		}

		// this.paymentsNonEditableText.setValue(transactionGrid.);

		// this.balanceDueNonEditableText.setValue(""+UIUtils.getCurrencySymbol()
		// +"0.00");

	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		transactionTotalBaseCurrencyText
				.setAmount(getAmountInBaseCurrency(transactionTotal));
		foreignCurrencyamountLabel.setAmount(transactionTotal);
	}

	// @Override
	// public boolean validate() throws InvalidTransactionEntryException,
	// InvalidEntryException {
	// return super.validate();
	//
	// }
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
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null) {
			return;
		}

		// Job Tracking
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setDisabled(false);
			jobListCombo.setValue("");
			jobListCombo.setCustomer(customer);
		}
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientCustomerCreditMemo ent = this.transaction;

			if (ent != null && ent.getCustomer() == customer.getID()) {
				this.customerAccountTransactionTable
						.setAllRows(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.customerItemTransactionTable
						.setAllRows(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getCustomer() != customer.getID()) {
				// this.customerAccountTransactionTable.resetRecords();
				this.customerAccountTransactionTable.updateTotals();
				this.customerItemTransactionTable.updateTotals();
			} else if (ent == null)
				this.customerAccountTransactionTable.resetRecords();
		}
		super.customerSelected(customer);
		shippingTermSelected(shippingTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (this.taxCode != null && taxCodeSelect != null
				&& taxCodeSelect.getValue() != ""
				&& !taxCodeSelect.getName().equalsIgnoreCase(messages.none()))
			taxCodeSelect.setComboItem(this.taxCode);

		// if (this.priceLevel != null && priceLevelSelect != null)
		// priceLevelSelect.setComboItem(this.priceLevel);

		this.setCustomer(customer);
		if (customer != null) {
			customerCombo.setComboItem(customer);
		}

		this.addressListOfCustomer = customer.getAddress();
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		long currency = customer.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			if (clientCurrency != null) {
				currencyWidget
						.setSelectedCurrencyFactorInWidget(clientCurrency,
								transactionDateItem.getDate().getDate());
			}
		} else {
			ClientCurrency clientCurrency = getCompany().getPrimaryCurrency();
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(customer.getCurrency()));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
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
		customerCombo.setDisabled(isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setDisabled(isInViewMode());
		// priceLevelSelect.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		customerAccountTransactionTable.setDisabled(isInViewMode());
		customerItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		discountField.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setDisabled(isInViewMode());
		}
		super.onEdit();

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
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
			ClientBrandingTheme brandingTheme = themesList.get(0);
			UIUtils.downloadAttachment(transaction.getID(),
					ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
					brandingTheme.getID());

		}

	}

	public void resetFormView() {
		// custForm.getCellFormatter().setWidth(0, 1, "200px");
		// custForm.setWidth("75%");
		// refText.setWidth("200px");
		// priceLevelSelect.setWidth("150px");
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
		return messages.customerCreditNote(Global.get().Customer());
	}

	@Override
	public boolean canPrint() {
		EditMode mode = getMode();
		if (mode == EditMode.CREATE || mode == EditMode.EDIT) {
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

	public Double getSalesTax() {
		return salesTax;
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		billToTextArea.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		memoTextAreaItem.setTabIndex(6);
		// menuButton.setTabIndex(7);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(8);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(9);
		cancelButton.setTabIndex(10);

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
		customerAccountTransactionTable.updateAmountsFromGUI();
		customerItemTransactionTable.updateAmountsFromGUI();
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
}
