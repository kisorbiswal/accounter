package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.PriceLevelCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class QuoteView extends AbstractCustomerTransactionView<ClientEstimate> {
	private SalesPersonCombo salesPersonCombo;
	private ShippingTermsCombo shippingTermsCombo;
	private PriceLevelCombo priceLevelSelect;
	private TAXCodeCombo taxCodeSelect;
	private PaymentTermsCombo payTermsSelect;
	protected DateField quoteExpiryDate;
	private HorizontalPanel panel;
	private ArrayList<DynamicForm> listforms;
	private boolean locationTrackingEnabled;

	private CustomerTransactionTable customerTransactionTable;

	protected ClientPriceLevel priceLevel;
	protected ClientTAXCode taxCode;
	protected DateField deliveryDate;
	private List<ClientPaymentTerms> paymentTermsList;
	protected ClientSalesPerson salesPerson;
	protected ClientPaymentTerms paymentTerm;
	private AmountLabel transactionTotalNonEditableText, netAmountLabel,
			vatTotalNonEditableText, salesTaxTextNonEditable;
	private Double salesTax;

	public QuoteView() {
		super(ClientTransaction.TYPE_ESTIMATE);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	private void initAllItems() {
		paymentTermsList = getCompany().getPaymentsTerms();
		payTermsSelect.initCombo(paymentTermsList);

		if (this.transaction != null) {
			this.quoteExpiryDate.setValue(new ClientFinanceDate(
					this.transaction.getExpirationDate()));
			this.deliveryDate.setValue(new ClientFinanceDate(this.transaction
					.getDeliveryDate()));
		}

	}

	public QuoteView(ClientCustomer customer) {
		super(ClientTransaction.TYPE_ESTIMATE);
		locationTrackingEnabled = ClientCompanyPreferences.get()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientEstimate ent = (ClientEstimate) this.transaction;

			if (ent != null && ent.getCustomer() == (customer.getID())) {
				this.customerTransactionTable.clear();
				this.customerTransactionTable.setAllRows(ent
						.getTransactionItems());
			} else if (ent != null
					&& !(ent.getCustomer() == (customer.getID()))) {
				this.customerTransactionTable.clear();
				this.customerTransactionTable.updateTotals();
			} else if (ent == null)
				this.customerTransactionTable.clear();
		}
		super.customerSelected(customer);
		shippingTermSelected(shippingTerm);

		if (this.paymentTerm != null && payTermsSelect != null)
			payTermsSelect.setComboItem(this.paymentTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (this.taxCode != null
				&& taxCodeSelect != null
				&& taxCodeSelect.getValue() != ""
				&& !taxCodeSelect.getName().equalsIgnoreCase(
						Accounter.constants().none()))
			taxCodeSelect.setComboItem(this.taxCode);
		if (this.priceLevel != null && priceLevelSelect != null)
			priceLevelSelect.setComboItem(this.priceLevel);

		if (customer.getPhoneNo() != null)
			phoneSelect.setValue(customer.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {

			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		this.setCustomer(customer);
		if (customer != null) {
			customerCombo.setComboItem(customer);
		}
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			for (ClientTransactionItem item : customerTransactionTable
					.getAllRows()) {
				if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT)
					customerTransactionTable.setCustomerTaxCode(item);
			}
		}

	}

	@Override
	public void showMenu(Widget button) {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button,
			// Accounter.messages().accounts(Global.get().Account()),
					Accounter.constants().serviceItem(), Accounter.constants()
							.productItem());
		// FinanceApplication.constants().salesTax());
		else
			setMenuItems(button,
			// Accounter.messages().accounts(Global.get().Account()),
					Accounter.constants().serviceItem(), Accounter.constants()
							.productItem());
		// FinanceApplication.constants().comment(),
		// FinanceApplication.constants().VATItem());

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
	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
		if (this.paymentTerm != null && payTermsSelect != null) {

			payTermsSelect.setComboItem(getCompany().getPaymentTerms(
					paymentTerm.getID()));
		}
		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();
		calculateDatesforPayterm(transDate);
	}

	private void calculateDatesforPayterm(ClientFinanceDate transDate) {
		if (transDate != null && this.paymentTerm != null) {
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(transDate,
					this.paymentTerm);
			if (dueDate != null) {
				quoteExpiryDate.setValue(dueDate);
			}
		}
	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson salesPerson2) {
		this.salesPerson = salesPerson2;
		if (salesPerson != null) {

			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

		} else
			salesPersonCombo.setValue("");

		salesPersonCombo.setDisabled(isInViewMode());

	}

	@Override
	public void saveAndUpdateView() {

		ClientEstimate quote = transaction != null ? (ClientEstimate) transaction
				: new ClientEstimate();

		if (quoteExpiryDate.getEnteredDate() != null)
			quote.setExpirationDate(quoteExpiryDate.getEnteredDate().getDate());
		if (getCustomer() != null)
			quote.setCustomer(getCustomer());
		if (contact != null)
			quote.setContact(contact);
		if (phoneSelect.getValue() != null)
			quote.setPhone(phoneSelect.getValue().toString());

		if (deliveryDate.getEnteredDate() != null)
			quote.setDeliveryDate(deliveryDate.getEnteredDate().getDate());

		if (salesPerson != null)
			quote.setSalesPerson(salesPerson);

		if (priceLevel != null)
			quote.setPriceLevel(priceLevel);

		quote.setMemo(memoTextAreaItem.getValue().toString());

		if (billingAddress != null)
			quote.setAddress(billingAddress);

		// quote.setReference(this.refText.getValue() != null ? this.refText
		// .getValue().toString() : "");
		quote.setPaymentTerm(Utility.getID(paymentTerm));
		quote.setNetAmount(netAmountLabel.getAmount());

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			quote.setAmountsIncludeVAT((Boolean) vatinclusiveCheck.getValue());
		} else
			quote.setSalesTax(this.salesTax);

		quote.setTotal(transactionTotalNonEditableText.getAmount());
		transaction = quote;

		super.saveAndUpdateView();

		saveOrUpdate((ClientEstimate) transaction);

	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(customerConstants.quote()));

		Label lab1 = new Label(Accounter.constants().newQuote());
		// + "(" + getTransactionStatus() + ")");
		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						setDateValues(date);

					}
				});

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(true);

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		if (locationTrackingEnabled)
			dateNoForm.setFields(locationCombo);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(15, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(Accounter.messages()
				.customerName(Global.get().customer()));
		contactCombo = createContactComboItem();
		billToTextArea = new TextAreaItem();
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(Accounter.constants().billTo());
		billToTextArea.setDisabled(true);

		phoneSelect = new TextItem(customerConstants.phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isInViewMode());

		custForm = UIUtils.form(Global.get().customer());
		custForm.setCellSpacing(5);
		custForm.setWidth("100%");

		custForm.setFields(customerCombo, contactCombo, phoneSelect,
				billToTextArea);
		custForm.getCellFormatter().setWidth(0, 0, "150");
		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		custForm.setStyleName("align-form");

		DynamicForm phoneForm = UIUtils.form(customerConstants.phoneNumber());
		phoneForm.setWidth("100%");
		phoneForm.setNumCols(2);
		phoneForm.setCellSpacing(3);
		salesPersonCombo = createSalesPersonComboItem();

		payTermsSelect = createPaymentTermsSelectItem();
		quoteExpiryDate = new DateField(customerConstants.expirationDate());
		quoteExpiryDate.setHelpInformation(true);
		quoteExpiryDate.setEnteredDate(getTransactionDate());
		// formItems.add(quoteExpiryDate);
		quoteExpiryDate.setDisabled(isInViewMode());

		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());
		// formItems.add(deliveryDate);
		if (getPreferences().isSalesPersonEnabled()) {
			phoneForm.setFields(salesPersonCombo, payTermsSelect,
					quoteExpiryDate, deliveryDate);
		} else {
			phoneForm.setFields(payTermsSelect, quoteExpiryDate, deliveryDate);
		}
		phoneForm.setStyleName("align-form");
		phoneForm.getCellFormatter().getElement(0, 0).setAttribute(
				Accounter.constants().width(), "203px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			phoneForm.setFields(classListCombo);
		}

		Label lab2 = new Label(customerConstants.productAndService());

		HorizontalPanel buttLabHLay = new HorizontalPanel();
		buttLabHLay.add(lab2);

		menuButton = createAddNewButton();

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		taxCodeSelect = createTaxCodeSelectItem();

		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();

		priceLevelSelect = createPriceLevelSelectItem();
		// refText = createRefereceText();
		// refText.setWidth(100);
		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		customerTransactionTable = new CustomerTransactionTable() {

			@Override
			public void updateNonEditableItems() {
				QuoteView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return QuoteView.this.isShowPriceWithVat();
			}

			@Override
			protected ClientCustomer getCustomer() {
				return customer;
			}
		};
		customerTransactionTable.setDisabled(isInViewMode());

		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("100%");
		prodAndServiceForm2.setNumCols(4);
		prodAndServiceForm2.setCellSpacing(5);

		int accountType = getCompany().getAccountingType();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			// prodAndServiceForm2.setFields(priceLevelSelect, netAmountLabel,
			// disabletextbox, vatTotalNonEditableText, disabletextbox,
			// transactionTotalNonEditableText);
			prodAndServiceForm2.setFields(disabletextbox, netAmountLabel,
					disabletextbox, vatTotalNonEditableText, disabletextbox,
					transactionTotalNonEditableText);
			prodAndServiceForm2.addStyleName("invoice-total");
		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
			// prodAndServiceForm2.setFields(taxCodeSelect,
			// salesTaxTextNonEditable, priceLevelSelect,
			// transactionTotalNonEditableText);
			prodAndServiceForm2.setFields(taxCodeSelect,
					salesTaxTextNonEditable, disabletextbox,
					transactionTotalNonEditableText);
			prodAndServiceForm2.addStyleName("tax-form");
		}

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(createAddNewButton());
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(ALIGN_RIGHT);
		vPanel.setWidth("100%");
		vPanel.add(panel);

		vPanel.add(prodAndServiceForm2);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "30%");
		} else
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "50%");

		VerticalPanel mainpanel = new VerticalPanel();
		mainpanel.setWidth("100%");
		mainpanel.add(vPanel);
		mainpanel.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.setHorizontalAlignment(ALIGN_LEFT);

		leftVLay.add(custForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.setHorizontalAlignment(ALIGN_CENTER);
		rightVLay.add(phoneForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "40%");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(buttLabHLay);
		VerticalPanel gridPanel = new VerticalPanel();

		gridPanel.add(customerTransactionTable);
		mainVLay.add(gridPanel);
		mainVLay.add(mainpanel);
		gridPanel.setWidth("100%");

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
			phoneForm.setWidth("60%");
		}

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (taxCode != null && transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setTaxCode(taxCode.getID());
			}
		}
		transaction.setTotal(transactionTotalNonEditableText.getAmount());
	}

	protected void setDateValues(ClientFinanceDate date) {
		if (date != null) {
			super.setTransactionDate(date);
			deliveryDate.setEnteredDate(date);
			quoteExpiryDate.setValue(date);
			calculateDatesforPayterm(date);
			updateNonEditableItems();
		}
	}

	@Override
	protected void initMemoAndReference() {

		if (this.transaction != null) {

			String memo = ((ClientEstimate) transaction).getMemo();
			if (memo != null) {
				memoTextAreaItem.setValue(memo);
			}
			// refText.setValue(quote.getReference());
		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transaction != null) {
			Double salesTaxAmout = ((ClientEstimate) transaction).getSalesTax();
			if (salesTaxAmout != null) {
				salesTaxTextNonEditable.setAmount(salesTaxAmout);
			}

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = ((ClientEstimate) transaction).getTotal();
			if (transactionTotal != null) {
				transactionTotalNonEditableText.setAmount(transactionTotal);
			}

		}

	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientEstimate());
		} else {
			ClientCompany company = getCompany();
			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			if (this.getCustomer() != null) {
				this.contacts = getCustomer().getContacts();
			}

			// customerSelected(FinanceApplication.getCompany().getCustomer(
			// estimate.getCustomer()));
			this.contact = transaction.getContact();

			this.phoneNo = transaction.getPhone();
			phoneSelect.setValue(this.phoneNo);
			this.billingAddress = transaction.getAddress();
			this.paymentTerm = company.getPaymentTerms(transaction
					.getPaymentTerm());
			this.priceLevel = company
					.getPriceLevel(transaction.getPriceLevel());
			this.salesPerson = company.getSalesPerson(transaction
					.getSalesPerson());
			initTransactionNumber();
			if (getCustomer() != null) {
				customerCombo.setComboItem(getCustomer());
			}
			// billToaddressSelected(this.billingAddress);
			if (billingAddress != null) {

				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");
			contactSelected(this.contact);
			paymentTermsSelected(this.paymentTerm);
			priceLevelSelected(this.priceLevel);
			salesPersonSelected(this.salesPerson);
			this.transactionItems = transaction.getTransactionItems();

			if (transaction.getDeliveryDate() != 0)
				this.deliveryDate.setValue(new ClientFinanceDate(transaction
						.getDeliveryDate()));
			if (transaction.getExpirationDate() != 0)
				this.quoteExpiryDate.setValue(new ClientFinanceDate(transaction
						.getExpirationDate()));

			if (transaction.getID() != 0) {
				setMode(EditMode.VIEW);
			}
			this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
			// taxCodeSelected(this.taxCode);
			if (taxCode != null) {
				this.taxCodeSelect
						.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			}

			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(estimate.getReference());
			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				netAmountLabel.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setValue(String.valueOf(transaction
						.getTotal()
						- transaction.getNetAmount()));
			}
			memoTextAreaItem.setDisabled(true);
			transactionTotalNonEditableText.setAmount(transaction.getTotal());

			customerTransactionTable.setDisabled(isInViewMode());
			transactionDateItem.setDisabled(isInViewMode());
			transactionNumber.setDisabled(isInViewMode());
			phoneSelect.setDisabled(isInViewMode());
			billToTextArea.setDisabled(isInViewMode());
			customerCombo.setDisabled(isInViewMode());
			payTermsSelect.setDisabled(isInViewMode());
			salesPersonCombo.setDisabled(isInViewMode());
			memoTextAreaItem.setDisabled(isInViewMode());
			contactCombo.setDisabled(isInViewMode());
			quoteExpiryDate.setDisabled(isInViewMode());
			deliveryDate.setDisabled(isInViewMode());
			taxCodeSelect.setDisabled(isInViewMode());
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		superinitTransactionViewData();
		initAllItems();
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

		priceLevelSelect.initCombo(priceLevels);

		ArrayList<ClientTAXCode> taxCodes = getCompany().getTaxCodes();

		taxCodeSelect.initCombo(taxCodes);

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

		initTransactionsItems();

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		if (priceLevel != null && priceLevelSelect != null) {

			priceLevelSelect.setComboItem(getCompany().getPriceLevel(
					priceLevel.getID()));

		}
		if (transaction == null && customerTransactionTable != null) {
			customerTransactionTable.priceLevelSelected(this.priceLevel);
			customerTransactionTable.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	public void updateNonEditableItems() {
		if (customerTransactionTable == null)
			return;
		int accountType = getCompany().getAccountingType();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
			Double taxableLineTotal = customerTransactionTable
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;

			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(), taxableLineTotal,
					getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionTable.getTotal()
					+ this.salesTax);
			netAmountLabel.setAmount(customerTransactionTable.getGrandTotal());

		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmountLabel.setAmount(customerTransactionTable.getGrandTotal());
			vatTotalNonEditableText.setAmount(customerTransactionTable
					.getTotalValue()
					- customerTransactionTable.getGrandTotal());
			setTransactionTotal(customerTransactionTable.getTotalValue());
		}

	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		transactionTotalNonEditableText.setAmount(transactionTotal);
	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;
		salesTaxTextNonEditable.setAmount(salesTax);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. isValidDueOrDeliveryDate?

		if (!AccounterValidator.isValidDueOrDelivaryDates(this.quoteExpiryDate
				.getEnteredDate(), this.transactionDate)) {
			result.addError(this.quoteExpiryDate, Accounter.constants().the()
					+ " "
					+ customerConstants.expirationDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());
		}
		result.add(customerTransactionTable.validateGrid());
		return result;

	}

	public static QuoteView getInstance() {
		return new QuoteView();
	}

	@Override
	protected void onAddNew(String item) {
		super.onAddNew(item);
	}

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
					Accounter.showMessage(Accounter.constants()
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
		payTermsSelect.setDisabled(isInViewMode());
		deliveryDate.setDisabled(isInViewMode());
		quoteExpiryDate.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		priceLevelSelect.setDisabled(isInViewMode());
		customerTransactionTable.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		shippingTermsCombo.setDisabled(isInViewMode());
		super.onEdit();
	}

	private void resetFormView() {

		custForm.getCellFormatter().setWidth(0, 1, "200px");
		custForm.setWidth("75%");
		priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect
					.setComboItem(getCompany().getTAXCode(taxCode.getID()));
			customerTransactionTable.setTaxCode(taxCode.getID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().quote();
	}

	@Override
	protected void initTransactionsItems() {
		if (transaction.getTransactionItems() != null)
			customerTransactionTable.setAllRows(transaction
					.getTransactionItems());
	}

	@Override
	protected boolean isBlankTransactionGrid() {
		return customerTransactionTable.getAllRows().isEmpty();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		customerTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		return customerTransactionTable.getAllRows();
	}

}
