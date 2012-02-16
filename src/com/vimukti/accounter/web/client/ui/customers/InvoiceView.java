package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressDialog;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.BrandingThemeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.TransactionsTree;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author Fernandez
 * @modified by B.Srinivasa Rao
 * 
 */
public class InvoiceView extends AbstractCustomerTransactionView<ClientInvoice>
		implements IPrintableView {
	private ShippingTermsCombo shippingTermsCombo;
	private TAXCodeCombo taxCodeSelect;
	private SalesPersonCombo salesPersonCombo;
	private Double salesTax = 0.0D;
	// private final boolean locationTrackingEnabled;
	private DateField deliveryDate;
	protected ClientSalesPerson salesPerson;
	private TaxItemsForm vatTotalNonEditableText, salesTaxTextNonEditable;
	private AmountLabel netAmountLabel, balanceDueNonEditableText,
			paymentsNonEditableText;
	private DynamicForm termsForm;
	// private WarehouseAllocationTable table;
	// private DisclosurePanel inventoryDisclosurePanel;

	// private Double currencyfactor;
	// private ClientCurrency currencyCode;
	TransactionsTree<EstimatesAndSalesOrdersList> transactionsTree;

	private InvoiceView() {
		super(ClientTransaction.TYPE_INVOICE);

	}

	private BrandingThemeCombo brandingThemeTypeCombo;
	DateField dueDateItem;
	private Double payments = 0.0;
	private Double balanceDue = 0.0;
	private ArrayList<DynamicForm> listforms;
	private TextAreaItem billToTextArea;
	private ShipToForm shipToAddress;
	private TextItem orderNumText;
	HorizontalPanel hpanel;
	DynamicForm amountsForm;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	private Button emailButton;
	private CustomerItemTransactionTable customerTransactionTable;
	private ClientPriceLevel priceLevel;
	private List<ClientPaymentTerms> paymentTermsList;
	private AddNewButton itemTableButton;

	private void initBalanceDue() {

		if (transaction != null) {

			setBalanceDue(transaction.getBalanceDue());

		}

	}

	@Override
	protected boolean canAddAttachmentPanel() {
		return true;
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	private void initPayments() {

		if (transaction != null) {

			ClientInvoice invoice = transaction;

			setPayments(invoice.getPayments());
		}

	}

	protected void initPaymentTerms() {

		paymentTermsList = Accounter.getCompany().getPaymentsTerms();

		payTermsSelect.initCombo(paymentTermsList);
		for (ClientPaymentTerms paymentTerm : paymentTermsList) {
			if (paymentTerm.getName().equals("Due on Receipt")) {
				payTermsSelect.addItemThenfireEvent(paymentTerm);
				break;
			}
		}
		this.paymentTerm = payTermsSelect.getSelectedValue();
	}

	private void initDueDate() {

		if (isInViewMode()) {
			ClientInvoice invoice = transaction;
			if (invoice.getDueDate() != 0) {
				dueDateItem.setEnteredDate(new ClientFinanceDate(invoice
						.getDueDate()));
			} else if (invoice.getPaymentTerm() != 0) {
				ClientPaymentTerms terms = getCompany().getPaymentTerms(
						invoice.getPaymentTerm());
				ClientFinanceDate transactionDate = this.transactionDateItem
						.getEnteredDate();
				ClientFinanceDate dueDate = new ClientFinanceDate(
						invoice.getDueDate());
				dueDate = Utility.getCalculatedDueDate(transactionDate, terms);
				if (dueDate != null) {
					dueDateItem.setEnteredDate(dueDate);
				}

			}

		} else
			dueDateItem.setEnteredDate(new ClientFinanceDate());

	}

	@Override
	protected void createControls() {
		Label lab1;
		DynamicForm dateNoForm = new DynamicForm();
		termsForm = new DynamicForm();
		DynamicForm prodAndServiceForm1 = new DynamicForm();
		DynamicForm prodAndServiceForm2 = new DynamicForm();
		DynamicForm vatForm = new DynamicForm();
		amountsForm = new DynamicForm();
		DynamicForm priceLevelForm = new DynamicForm();

		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab1 = new Label(messages.invoice());
		else {
			lab1 = new Label(messages.invoice());
		}

		lab1.setStyleName("label-title");

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						setDateValues(date);
					}
				});
		transactionDateItem.setHelpInformation(true);
		transactionNumber = createTransactionNumberItem();

		transactionNumber.setTitle(messages.invoiceNo());
		listforms = new ArrayList<DynamicForm>();
		brandingThemeTypeCombo = new BrandingThemeCombo(
				messages.brandingTheme());

		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(true);
		// DATE form
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		if (!isTemplate) {
			dateNoForm.setFields(transactionDateItem, transactionNumber);
		}

		// ---date--
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(datepanel);

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		customerCombo = createCustomerComboItem(messages.payeeName(Global.get()
				.Customer()));
		customerCombo.setHelpInformation(true);
		customerCombo.setWidth("100%");
		LabelItem emptylabel = new LabelItem();
		emptylabel.setValue("");
		emptylabel.setWidth("100%");
		emptylabel.setShowTitle(false);
		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);

		billToTextArea = new TextAreaItem();
		billToTextArea.setHelpInformation(true);
		billToTextArea.setWidth(100);

		billToTextArea.setTitle(messages.billTo());
		billToTextArea.setDisabled(isInViewMode());
		billToTextArea.setHelpInformation(true);

		billToTextArea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("", "", billToTextArea, messages.billTo(),
						allAddresses);
			}
		});

		billToTextArea.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				new AddressDialog("", "", billToTextArea, messages.billTo(),
						allAddresses);

			}
		});

		shipToCombo = createShipToComboItem();

		shipToCombo.setHelpInformation(true);

		shipToAddress = new ShipToForm(null);
		shipToAddress.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);

		// shipToAddress.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "40px");
		shipToAddress.getCellFormatter().addStyleName(0, 1, "memoFormAlign");
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

		if (transaction != null)
			shipToAddress.setDisabled(true);

		custForm = UIUtils.form(Global.get().customer());
		custForm.setNumCols(3);
		// custForm.setWidth("100%");
		currencyWidget = createCurrencyFactorWidget();

		custForm.setFields(customerCombo, emptylabel, contactCombo, emptylabel,
				billToTextArea, emptylabel);
		custForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");

		// custForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "226px");
		custForm.setStyleName("align-form");

		salesPersonCombo = createSalesPersonComboItem();

		payTermsSelect = createPaymentTermsSelectItem();

		shippingTermsCombo = createShippingTermsCombo();

		shippingMethodsCombo = createShippingMethodCombo();

		dueDateItem = new DateField(messages.dueDate());
		dueDateItem.setToolTip(messages.selectDateUntilDue(this.getAction()
				.getViewName()));
		dueDateItem.setHelpInformation(true);
		dueDateItem.setEnteredDate(getTransactionDate());
		dueDateItem.setColSpan(1);
		dueDateItem.setTitle(messages.dueDate());
		dueDateItem.setDisabled(isInViewMode());
		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());

		orderNumText = new TextItem(messages.orderNumber());
		orderNumText.setHelpInformation(true);
		orderNumText.setWidth(38);
		if (transaction != null)
			orderNumText.setDisabled(true);

		if (locationTrackingEnabled)
			termsForm.setFields(locationCombo);
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo = createJobListCombo();
			termsForm.setFields(jobListCombo);
		}
		// termsForm.setWidth("100%");
		termsForm.setIsGroup(true);
		termsForm.setGroupTitle(messages.terms());
		termsForm.setNumCols(2);
		if (getPreferences().isSalesPersonEnabled()) {
			if (isTemplate) {
				termsForm.setFields(salesPersonCombo, payTermsSelect,
						orderNumText);
			} else {
				termsForm.setFields(salesPersonCombo, payTermsSelect,
						dueDateItem, orderNumText);
			}

			if (getPreferences().isDoProductShipMents())
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
		} else {
			if (isTemplate) {
				termsForm.setFields(payTermsSelect, orderNumText);
			} else {
				termsForm.setFields(payTermsSelect, dueDateItem, orderNumText);
			}
			if (getPreferences().isDoProductShipMents())
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);

		}

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		termsForm.setStyleName("align-form");

		// termsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "200px");
		// multi
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth("400px");

		Button printButton = new Button();

		printButton.setText(messages.print());
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				print();
				// InvoicePrintLayout printLt = new InvoicePrintLayout(
				// (ClientInvoice) getInvoiceObject());
				// printLt.setView(InvoiceView.this);
				// printLt.createTemplate();
				// printLt.print();
			}
		});

		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setNumCols(1);
		prodAndServiceForm1.setFields(memoTextAreaItem);

		// VerticalPanel vPanel = new VerticalPanel();
		// vPanel.add(prodAndServiceForm1);
		// vPanel.setWidth("100%");
		// forms.add(prodAndServiceForm1);

		// priceLevelSelect = createPriceLevelSelectItem();
		taxCodeSelect = createTaxCodeSelectItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();

		netAmountLabel = createNetAmountLabel();

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getCompany()
				.getPreferences().getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPreferences().getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableLabel();

		paymentsNonEditableText = new AmountLabel(messages.payments());
		paymentsNonEditableText.setDisabled(true);
		paymentsNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");
		balanceDueNonEditableText = new AmountLabel(messages.balanceDue());
		balanceDueNonEditableText.setDisabled(true);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		salesTaxTextNonEditable = new TaxItemsForm();// createSalesTaxNonEditableLabel();

		transactionsTree = new TransactionsTree<EstimatesAndSalesOrdersList>(
				this) {
			@Override
			public void updateTransactionTotal() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				InvoiceView.this.updateNonEditableItems();
			}

			@Override
			public void setTransactionDate(ClientFinanceDate transactionDate) {
				InvoiceView.this.setTransactionDate(transactionDate);
			}

			@Override
			public boolean isinViewMode() {
				return !(InvoiceView.this.isInViewMode());
			}
		};

		customerTransactionTable = new CustomerItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				InvoiceView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return InvoiceView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return InvoiceView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				InvoiceView.this.updateNonEditableItems();
			}
		};

		customerTransactionTable.setDisabled(isInViewMode());
		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		prodAndServiceForm2.setWidth("50%");
		prodAndServiceForm2.setNumCols(4);
		prodAndServiceForm2.setCellSpacing(5);
		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		// final TextItem disabletextbox = new TextItem();
		// disabletextbox.setVisible(false);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

		brandingThemeTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBrandingTheme>() {

					@Override
					public void selectedComboBoxItem(
							ClientBrandingTheme selectItem) {
					}
				});

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);

		priceLevelForm.setWidth("100%");
		amountsForm.setNumCols(2);
		amountsForm.setCellSpacing(5);

		VerticalPanel nonEditablePanel = new VerticalPanel();
		discountField = getDiscountField();

		if (isTrackTax()) {
			amountsForm.setFields(netAmountLabel);
			nonEditablePanel.add(amountsForm);
			if (!isTaxPerDetailLine()) {
				vatForm.setFields(taxCodeSelect);
				nonEditablePanel.add(salesTaxTextNonEditable);

			} else {
				nonEditablePanel.add(vatTotalNonEditableText);
			}
			vatForm.setFields(vatinclusiveCheck);
		}
		prodAndServiceHLay.add(vatForm);
		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				vatForm.setFields(discountField);
				prodAndServiceHLay.add(vatForm);
			}
		}

		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setCellSpacing(5);
		totalForm.setFields(transactionTotalBaseCurrencyText);
		if (isMultiCurrencyEnabled()) {
			totalForm.setFields(foreignCurrencyamountLabel);
		}
		if (isInViewMode()) {
			totalForm.setFields(paymentsNonEditableText,
					balanceDueNonEditableText);
		}
		nonEditablePanel.add(totalForm);
		nonEditablePanel.setStyleName("boldtext");
		nonEditablePanel.setWidth("100%");
		prodAndServiceHLay.add(nonEditablePanel);

		nonEditablePanel.setCellHorizontalAlignment(amountsForm, ALIGN_RIGHT);
		nonEditablePanel.setCellHorizontalAlignment(vatTotalNonEditableText,
				ALIGN_RIGHT);
		nonEditablePanel.setCellHorizontalAlignment(salesTaxTextNonEditable,
				ALIGN_RIGHT);
		nonEditablePanel.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);
		prodAndServiceHLay.setCellHorizontalAlignment(nonEditablePanel,
				ALIGN_RIGHT);

		/*
		 * if (getCompany().getPreferences().isRegisteredForVAT()) {
		 * 
		 * DynamicForm priceLevelForm = new DynamicForm(); //
		 * priceLevelForm.setCellSpacing(4); priceLevelForm.setWidth("70%"); //
		 * priceLevelForm.setFields(priceLevelSelect);
		 * amountsForm.setFields(netAmountLabel, vatTotalNonEditableText,
		 * transactionTotalNonEditableText, paymentsNonEditableText,
		 * balanceDueNonEditableText);
		 * amountsForm.setStyleName("invoice-total"); //
		 * forms.add(priceLevelForm); // prodAndServiceHLay.add(priceLevelForm);
		 * // prodAndServiceHLay.setCellHorizontalAlignment(priceLevelForm, //
		 * ALIGN_RIGHT); // prodAndServiceHLay.add(amountsForm); //
		 * prodAndServiceHLay.setCellHorizontalAlignment(amountsForm, //
		 * ALIGN_RIGHT); // listforms.add(priceLevelForm);
		 * 
		 * } else {
		 * 
		 * // prodAndServiceForm2.setFields(salesTaxTextNonEditable, //
		 * transactionTotalNonEditableText, paymentsNonEditableText, //
		 * balanceDueNonEditableText, taxCodeSelect, priceLevelSelect);
		 * amountsForm.setNumCols(4); amountsForm.addStyleName("boldtext");
		 * 
		 * if (getCompany().getPreferences().isChargeSalesTax()) {
		 * amountsForm.setFields(taxCodeSelect, salesTaxTextNonEditable,
		 * disabletextbox, transactionTotalNonEditableText, disabletextbox,
		 * paymentsNonEditableText, disabletextbox, balanceDueNonEditableText);
		 * } else { amountsForm.setFields(transactionTotalNonEditableText,
		 * disabletextbox, paymentsNonEditableText, disabletextbox,
		 * balanceDueNonEditableText); }
		 * 
		 * prodAndServiceHLay.add(amountsForm);
		 * prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
		 * ALIGN_RIGHT); }
		 */

		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.setWidth("100%");

		VerticalPanel panel11 = new VerticalPanel();
		panel11.setWidth("100%");
		panel11.add(panel);
		panel11.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setHorizontalAlignment(ALIGN_LEFT);
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);
		if (getCompany().getPreferences().isDoProductShipMents())
			leftVLay.add(shipToAddress);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);
		rightVLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			rightVLay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}
		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setSpacing(10);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(transactionsTree);
		mainVLay.add(customerTransactionTable);
		mainVLay.add(itemTableButton);

		// table = new WarehouseAllocationTable();
		// table.setDesable(isInViewMode());
		//
		// FlowPanel inventoryFlowPanel = new FlowPanel();
		// inventoryDisclosurePanel = new
		// DisclosurePanel("Warehouse Allocation");
		// inventoryFlowPanel.add(table);
		// inventoryDisclosurePanel.setContent(inventoryFlowPanel);
		// inventoryDisclosurePanel.setWidth("100%");
		// if (getCompany().getPreferences().isInventoryEnabled()
		// && getCompany().getPreferences().iswareHouseEnabled())
		// mainVLay.add(inventoryDisclosurePanel);
		// ---Inverntory table-----

		mainVLay.add(panel11);

		if (UIUtils.isMSIEBrowser())
			resetFromView();

		this.add(mainVLay);

		if (isMultiCurrencyEnabled()) {
			if (!isInViewMode()) {
				foreignCurrencyamountLabel.hide();
			}
		}
		settabIndexes();

	}

	private ShippingTermsCombo createShippingTermsCombo() {

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
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		if (isInViewMode()
				&& (data != null && !data.isTemplate() && data.getSaveStatus() != ClientTransaction.STATUS_DRAFT)) {
			emailButton = new Button(messages.email());
			buttonBar.add(emailButton);

			emailButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// ActionFactory.getEmailViewAction().run(transaction,
					// false);
					ArrayList<ClientBrandingTheme> themesList = Accounter
							.getCompany().getBrandingTheme();

					if (themesList.size() > 1) {
						// if there are more than one branding themes, then show
						// branding
						// theme dialog box
						ActionFactory.getEmailThemeComboAction().run(
								transaction, false);
					} else {
						ActionFactory.getEmailViewAction().run(transaction,
								themesList.get(0).getID(), false);
					}
				}
			});
		}
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.productOrServiceItem());

	}

	protected void setDateValues(ClientFinanceDate date) {
		if (date != null) {
			deliveryDate.setEnteredDate(date);
			dueDateItem.setValue(date);
			setTransactionDate(date);
			calculateDatesforPayterm(date);
			updateNonEditableItems();
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
		if (this.transaction == null || customerTransactionTable != null) {
			customerTransactionTable.setPricingLevel(priceLevel);
			// customerTransactionTable.updatePriceLevel();
		}
		updateNonEditableItems();

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
		if (customerTransactionTable == null)
			return;

		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : customerTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}

		if (isTrackTax()) {
			setSalesTax(customerTransactionTable.getTotalTax()
					+ transactionsTree.getTotalTax());
			List<ClientTransaction> selectedRecords = transactionsTree
					.getSelectedRecords();
			if (!isInViewMode()) {
				List<ClientSalesOrder> salesOrders = new ArrayList<ClientSalesOrder>();
				List<ClientEstimate> estimates = new ArrayList<ClientEstimate>();
				for (ClientTransaction clientTransaction : selectedRecords) {
					if (clientTransaction instanceof ClientSalesOrder) {
						salesOrders.add((ClientSalesOrder) clientTransaction);
					} else {
						estimates.add((ClientEstimate) clientTransaction);
					}
				}
				transaction.setEstimates(estimates);
				transaction.setSalesOrders(salesOrders);
				transaction.setTransactionItems(customerTransactionTable
						.getTransactionItems());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);
			salesTaxTextNonEditable.setTransaction(transaction);
		}
		netAmountLabel.setAmount(customerTransactionTable.getLineTotal()
				+ transactionsTree.getLineTotal());
		setTransactionTotal(customerTransactionTable.getGrandTotal()
				+ transactionsTree.getGrandTotal());

		// Double payments =
		// getAmountInBaseCurrency(this.paymentsNonEditableText
		// .getAmount());

		// if (transaction != null) {
		// payments = this.transactionTotal < payments ? this.transactionTotal
		// : payments;
		// setPayments(payments);
		// }
		// setBalanceDue((this.transactionTotal - payments));
	}

	@Override
	protected void customerSelected(final ClientCustomer customer) {
		ClientCurrency currency = getCurrency(customer.getCurrency());

		if (this.getCustomer() != null && !this.getCustomer().equals(customer)
				&& transaction.getID() == 0) {
			customerTransactionTable.resetRecords();
			transaction.setTransactionItems(customerTransactionTable
					.getRecords());
			if (taxCodeSelect.getSelectedValue() != null) {
				customerTransactionTable.setTaxCode(taxCodeSelect
						.getSelectedValue().getID(), true);
			}
			vatTotalNonEditableText.setTransaction(transaction);
			salesTaxTextNonEditable.setTransaction(transaction);
		}
		// Job Tracking
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setValue("");
			jobListCombo.setCustomer(customer);
		}
		this.setCustomer(customer);
		super.customerSelected(customer);
		shippingTermSelected(shippingTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(customer);
		}
		this.addressListOfCustomer = customer.getAddress();
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {

			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(customer.getAddress());
		shipToAddress.setAddress(addresses);

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		if (addressListOfCustomer != null) {
			Iterator it = addressListOfCustomer.iterator();
			while (it.hasNext()) {
				ClientAddress add = (ClientAddress) it.next();

				allAddresses.put(add.getType(), add);
			}
		}

		currencyWidget.setSelectedCurrencyFactorInWidget(currency,
				transactionDateItem.getDate().getDate());

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
		transaction.setEstimates(new ArrayList<ClientEstimate>());
		transaction.setSalesOrders(new ArrayList<ClientSalesOrder>());
		getEstimatesAndSalesOrder();
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
		if (salesPerson != null) {

			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

		}
		salesPersonCombo.setDisabled(isInViewMode());
	}

	@Override
	protected void initTransactionViewData() {
		ClientCompany company = Accounter.getCompany();
		initPaymentTerms();
		if (transaction == null) {
			setData(new ClientInvoice());
		} else {
			if (isMultiCurrencyEnabled()) {
				if (transaction.getCurrency() > 0) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPreferences()
							.getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				if (this.currency != null) {
					currencyWidget.setSelectedCurrency(this.currency);
				}
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}
			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			this.contact = transaction.getContact();

			if (transaction.getPhone() != null)
				this.phoneNo = transaction.getPhone();
			this.billingAddress = transaction.getBillingAddress();
			this.shippingAddress = transaction.getShippingAdress();
			this.transactionItems = transaction.getTransactionItems();
			this.priceLevel = company
					.getPriceLevel(transaction.getPriceLevel());
			this.payments = transaction.getPayments();
			this.salesPerson = company.getSalesPerson(transaction
					.getSalesPerson());
			this.shippingMethod = company.getShippingMethod(transaction
					.getShippingMethod());
			this.paymentTerm = company.getPaymentTerms(transaction
					.getPaymentTerm());
			this.shippingTerm = company.getShippingTerms(transaction
					.getShippingTerm());
			initTransactionNumber();
			initTransactionsItems();
			this.orderNumText
					.setValue(transaction.getOrderNum() != null ? transaction
							.getOrderNum() : "");
			if (getCustomer() != null && customerCombo != null) {
				customerCombo.setComboItem(getCustomer());
				if (!data.isTemplate()) {
					getEstimatesAndSalesOrder();
				}
			}

			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			if (getCustomer() != null)
				addresses.addAll(getCustomer().getAddress());

			Iterator<ClientAddress> it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress add = it.next();

				allAddresses.put(add.getType(), add);
			}
			shipToAddress.setListOfCustomerAdress(addresses);
			if (shippingAddress != null) {
				shipToAddress.businessSelect.setValue(UIUtils
						.getAddressesTypes(shippingAddress.getType()));
				shipToAddress.setAddres(shippingAddress);
			}
			if (getCustomer() != null) {
				this.addressListOfCustomer = getCustomer().getAddress();
			}

			if (billingAddress != null) {

				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");
			contactSelected(this.contact);
			paymentTermsSelected(this.paymentTerm);
			if (priceLevel != null) {
				priceLevelSelected(this.priceLevel);
			}
			salesPersonSelected(this.salesPerson);
			shippingMethodSelected(this.shippingMethod);
			if (shippingTerm != null && shippingTermsCombo != null) {
				shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
						shippingTerm.getID()));

				shippingTermsCombo.setDisabled(isInViewMode());
			}
			if (transaction.getMemo() != null)
				memoTextAreaItem.setValue(transaction.getMemo());
			memoTextAreaItem.setDisabled(isInViewMode());

			if (transaction.getDeliverydate() != 0)
				this.deliveryDate.setValue(new ClientFinanceDate(transaction
						.getDeliverydate()));
			this.dueDateItem
					.setValue(transaction.getDueDate() != 0 ? new ClientFinanceDate(
							transaction.getDueDate()) : getTransactionDate());
			netAmountLabel.setAmount(transaction.getNetAmount());
			if (isTrackTax()) {
				if (isTaxPerDetailLine()) {
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect.setComboItem(taxCode);
						taxCodeSelected(taxCode);
					} else {
						if (getCustomer() != null) {
							this.taxCode = getCompany().getTAXCode(
									getCustomer().getTAXCode());
							if (taxCode != null) {
								this.taxCodeSelect.setComboItem(taxCode);
								taxCodeSelected(taxCode);
							}
						}
					}
					this.salesTaxTextNonEditable.setTransaction(transaction);

				}
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

			if (locationTrackingEnabled)
				locationSelected(company.getLocation(transaction.getLocation()));

			if (getPreferences().isJobTrackingEnabled()) {
				jobSelected(company.getjob(transaction.getJob()));
			}
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());
			paymentsNonEditableText.setAmount(transaction.getPayments());
			balanceDueNonEditableText.setAmount(transaction.getBalanceDue());
			// memoTextAreaItem.setDisabled(true);
			initAccounterClass();
		}

		superinitTransactionViewData();

		ArrayList<ClientShippingTerms> shippingTerms = getCompany()
				.getShippingTerms();

		shippingTermsCombo.initCombo(shippingTerms);

		initShippingMethod();
		initDueDate();
		initPayments();
		initBalanceDue();

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
		if (customerCombo != null && customerCombo.getSelectedValue() != null
				&& !isInViewMode()) {
			if (contactCombo != null) {
				contactCombo.setDisabled(false);
			}
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

		initTransactionsItems();

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

	}

	@Override
	public void initTransactionsItems() {
		if (transaction.getTransactionItems() != null
				&& !transaction.getTransactionItems().isEmpty()) {
			ArrayList<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
			for (ClientTransactionItem item : transaction.getTransactionItems()) {
				// We should exclude those which come from quote/charge/credit
				if (item.getReferringTransactionItem() == 0) {
					list.add(item);
				}
			}

			customerTransactionTable.setAllRows(list);
		}
	}

	@Override
	protected void shipToAddressSelected(ClientAddress selectItem) {
		this.shippingAddress = selectItem;
		if (this.shippingAddress != null && shipToAddress != null)
			shipToCombo.setComboItem(this.shippingAddress);
	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transaction != null) {
			Double salesTaxAmout = transaction.getTaxTotal();
			setSalesTax(salesTaxAmout);

		}

	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;

		if ((transaction.getTransactionItems() != null && transaction
				.getTransactionItems().isEmpty()) && !isInViewMode())
			transaction.setTransactionItems(customerTransactionTable
					.getAllRows());
		if (currency != null) {
			transaction.setCurrency(currency.getID());
		}
		if (salesTaxTextNonEditable != null) {
			salesTaxTextNonEditable.setTransaction(transaction);
		}
		if (vatTotalNonEditableText != null) {
			vatTotalNonEditableText.setTransaction(transaction);
		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = transaction.getTotal();
			setTransactionTotal(transactionTotal);
		}
	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		if (transactionTotalBaseCurrencyText != null) {
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transactionTotal));
			foreignCurrencyamountLabel.setAmount(transactionTotal);
		}

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transaction != null) {

			ClientInvoice invoice = transaction;

			if (invoice.getMemo() != null) {
				memoTextAreaItem.setValue(invoice.getMemo());
			}

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
				dueDateItem.setValue(dueDate);
			}
		}
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		// No Need to update Customer Object separately It will be automatically
		// updated.
		// saveOrUpdate(getCustomer());
		saveOrUpdate(transaction);

	}

	@Override
	public ClientInvoice saveView() {
		ClientInvoice saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		List<ClientTransaction> selectedRecords = transactionsTree
				.getSelectedRecords();
		List<ClientSalesOrder> salesOrders = new ArrayList<ClientSalesOrder>();
		List<ClientEstimate> estimates = new ArrayList<ClientEstimate>();
		for (ClientTransaction clientTransaction : selectedRecords) {
			if (clientTransaction instanceof ClientSalesOrder) {
				salesOrders.add((ClientSalesOrder) clientTransaction);
			} else {
				ClientEstimate estimate = (ClientEstimate) clientTransaction;
				if (estimate.getEstimateType() == ClientEstimate.BILLABLEEXAPENSES
						&& estimate.getCurrency() != getCurrencycode().getID()) {
					estimate.setCurrency(getCurrencycode().getID());
					estimate.setTotal(estimate.getTotal() / getCurrencyFactor());
					estimate.setNetAmount(estimate.getNetAmount()
							/ getCurrencyFactor());
					estimate.setTaxTotal(estimate.getTaxTotal()
							/ getCurrencyFactor());
					for (ClientTransactionItem item : estimate
							.getTransactionItems()) {
						item.setLineTotal(item.getLineTotal()
								/ getCurrencyFactor());
						item.setDiscount(item.getDiscount()
								/ getCurrencyFactor());
						item.setUnitPrice(item.getUnitPrice()
								/ getCurrencyFactor());
						item.setVATfraction(item.getVATfraction()
								/ getCurrencyFactor());
					}

				}
				estimates.add(estimate);
			}
		}
		transaction.setEstimates(estimates);
		transaction.setSalesOrders(salesOrders);
		if (taxCode != null && transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setTaxCode(taxCode.getID());
			}
		}

		if (getCustomer() != null) {
			Set<ClientAddress> addr = shipToAddress.getAddresss();
			billingAddress = allAddresses.get(ClientAddress.TYPE_BILL_TO);
			if (billingAddress != null) {
				for (ClientAddress clientAddress : addr) {
					if (clientAddress.getType() == ClientAddress.TYPE_BILL_TO) {
						addr.remove(clientAddress);
					}
				}
				addr.add(billingAddress);
			}
			if (!addr.isEmpty()) {
				getCustomer().setAddress(addr);
				// Accounter.createOrUpdate(this, getCustomer());

				for (ClientAddress clientAddress : addr) {
					if (clientAddress.getType() == ClientAddress.TYPE_SHIP_TO)
						shippingAddress = clientAddress;
				}
			}

			transaction.setCustomer(getCustomer().getID());
		}

		if (dueDateItem.getEnteredDate() != null)
			transaction.setDueDate((dueDateItem.getEnteredDate()).getDate());
		if (deliveryDate.getEnteredDate() != null)
			transaction
					.setDeliverydate(deliveryDate.getEnteredDate().getDate());
		if (getCountryPreferences().isSalesTaxAvailable()) {

			transaction.setTaxTotal(salesTaxTextNonEditable.getTotalTax());
		}
		if (contactCombo.getSelectedValue() != null) {
			contact = contactCombo.getSelectedValue();
			transaction.setContact(contact);
		}
		transaction.setContact(contact);
		if (phoneNo != null)
			transaction.setPhone(phoneNo);
		if (billingAddress != null)
			transaction.setBillingAddress(billingAddress);
		if (shippingAddress != null)
			transaction.setShippingAdress(shippingAddress);
		if (salesPerson != null)
			transaction.setSalesPerson(salesPerson.getID());
		if (paymentTerm != null)
			transaction.setPaymentTerm(paymentTerm.getID());
		if (shippingTerm != null)
			transaction.setShippingTerm(shippingTerm.getID());
		if (shippingMethod != null)
			transaction.setShippingMethod(shippingMethod.getID());
		if (priceLevel != null)
			transaction.setPriceLevel(priceLevel.getID());

		if (orderNumText.getValue() != null
				&& !orderNumText.getValue().equals(""))
			orderNum = orderNumText.getValue().toString();

		if (orderNum != null)
			transaction.setOrderNum(orderNum);
		// if (taxItemGroup != null)
		// transaction.setTaxItemGroup(taxItemGroup);
		if (isTrackDiscounts()) {
			if (discountField.getAmount() != 0.0 && transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getAmount());
				}
			}
		}
		if (getPreferences().isJobTrackingEnabled()) {
			if(jobListCombo.getSelectedValue()!= null){
			transaction.setJob(jobListCombo.getSelectedValue().getID());
			}
		}
		transaction.setNetAmount(netAmountLabel.getAmount());
		if (isTrackTax()) {
			// if (isTaxPerDetailLine()) {
			setAmountIncludeTAX();
			// } else {
			// if (taxCode != null) {
			// for (ClientTransactionItem record : customerTransactionTable
			// .getAllRows()) {
			// record.setTaxItemGroup(taxCode.getID());
			// }
			transaction.setTaxTotal(this.salesTax);

			// if (getCompany().getPreferences().isInventoryEnabled()
			// && getCompany().getPreferences().iswareHouseEnabled())
			// transaction.setWareHouseAllocations(table.getAllRows());
			// }

		}
		transaction.setTotal(foreignCurrencyamountLabel.getAmount());

		// transaction.setBalanceDue(getBalanceDue());
		transaction.setPayments(getPayments());
		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());

		ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
				transactionDateItem.getEnteredDate(), paymentTerm);
		transaction.setDiscountDate(discountDate.getDate());
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		ClientCustomer previousCustomer = getCustomer();

		if (getCustomer() != null && getCustomer() != previousCustomer) {
			transaction.setEstimates(new ArrayList<ClientEstimate>());
			transaction.setSalesOrders(new ArrayList<ClientSalesOrder>());
			getEstimatesAndSalesOrder();
		}
		// result.add(super.validate());

		// Validations
		// 1. IF(!isValidDueOrDeliveryDates(dueDate, transactionDate)) ERROR

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				this.dueDateItem.getDate(), getTransactionDate())) {
			result.addError(this.dueDateItem,
					messages.the() + " " + messages.dueDate() + " " + " "
							+ messages.cannotbeearlierthantransactiondate());
		}

		boolean isSelected = transactionsTree.validateTree();
		if (!isSelected) {
			if (transaction.getTotal() <= 0
					&& customerTransactionTable.isEmpty()) {
				result.addError(this,
						messages.transactiontotalcannotbe0orlessthan0());
			}
			result.add(customerTransactionTable.validateGrid());
		} else {
			boolean hasTransactionItems = false;
			for (ClientTransactionItem clientTransactionItem : getAllTransactionItems()) {
				if (clientTransactionItem.getAccount() != 0
						|| clientTransactionItem.getItem() != 0) {
					hasTransactionItems = true;
					continue;
				}
			}
			if (hasTransactionItems) {
				result.add(customerTransactionTable.validateGrid());
			} else {
				transaction
						.setTransactionItems(new ArrayList<ClientTransactionItem>());
			}
		}
		if (!isSelected && isTrackTax() && !isTaxPerDetailLine()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						messages.pleaseSelect(messages.taxCode()));
			}
		} else if (isSelected && isTrackTax() && !isTaxPerDetailLine()
				&& !transaction.getTransactionItems().isEmpty()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						messages.pleaseSelect(messages.taxCode()));
			}
		}
		return result;
	}

	public void setPayments(Double payments) {
		if (payments == null)
			payments = 0.0D;
		this.payments = payments;
		paymentsNonEditableText.setAmount(payments);
	}

	public Double getPayments() {
		return payments;
	}

	public void setBalanceDue(Double balanceDue) {
		if (balanceDue == null)
			balanceDue = 0.0D;
		this.balanceDue = balanceDue;
		balanceDueNonEditableText.setAmount(balanceDue);
	}

	public static InvoiceView getInstance() {

		return new InvoiceView();
	}

	private void getEstimatesAndSalesOrder() {
		ClientCompanyPreferences preferences = getCompany().getPreferences();

		if (preferences.isDoyouwantEstimates()
				|| preferences.isBillableExpsesEnbldForProductandServices()
				|| preferences.isProductandSerivesTrackingByCustomerEnabled()
				|| preferences.isDelayedchargesEnabled()) {
			if (preferences.isDontIncludeEstimates()) {
				return;
			}
		}
		if (this.rpcUtilService == null)
			return;
		if (getCustomer() == null) {
			Accounter.showError(messages.pleaseSelect(Global.get().customer()));
		} else {

			AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>> callback = new AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>>() {

				@Override
				public void onFailure(Throwable caught) {
					return;
				}

				@Override
				public void onSuccess(
						ArrayList<EstimatesAndSalesOrdersList> result) {
					if (result == null)
						onFailure(new Exception());
					List<ClientTransaction> salesAndEstimates = transaction
							.getSalesAndEstimates();
					if (transaction.getID() != 0 && !result.isEmpty()) {
						ArrayList<EstimatesAndSalesOrdersList> estimatesList = new ArrayList<EstimatesAndSalesOrdersList>();
						ArrayList<ClientTransaction> notAvailableEstimates = new ArrayList<ClientTransaction>();

						for (ClientTransaction clientTransaction : salesAndEstimates) {
							boolean isThere = false;
							for (EstimatesAndSalesOrdersList estimatesalesorderlist : result) {
								if (estimatesalesorderlist.getTransactionId() == clientTransaction
										.getID()) {
									estimatesList.add(estimatesalesorderlist);
									isThere = true;
								}
							}
							if (!isThere) {
								notAvailableEstimates.add(clientTransaction);
							}
						}

						if (transaction.isDraft()) {
							for (ClientTransaction clientTransaction : notAvailableEstimates) {
								salesAndEstimates.remove(clientTransaction);
							}
						}

						for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : estimatesList) {
							result.remove(estimatesAndSalesOrdersList);
						}
					}
					transactionsTree.setAllrows(
							result,
							transaction.getID() == 0 ? true : salesAndEstimates
									.isEmpty());
					transactionsTree
							.quotesSelected((List<ClientTransaction>) (salesAndEstimates != null ? salesAndEstimates
									: new ArrayList<ClientTransaction>()));
					transactionsTree.setEnabled(!isInViewMode());
					refreshTransactionGrid();
				}
			};
			this.rpcUtilService.getEstimatesAndSalesOrdersList(getCustomer()
					.getID(), callback);
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

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {

		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
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

		// if (UIUtils.isMSIEBrowser())
		// custForm.setWidth("100%");

		setMode(EditMode.EDIT);

		if (!isInViewMode() && !data.isTemplate()
				&& data.getSaveStatus() != ClientTransaction.STATUS_DRAFT) {

			getButtonBar().remove(emailButton);
		}

		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		customerCombo.setDisabled(isInViewMode());
		shipToAddress.businessSelect.setDisabled(isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setDisabled(isInViewMode());
		payTermsSelect.setDisabled(isInViewMode());
		dueDateItem.setDisabled(isInViewMode());
		deliveryDate.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());
		orderNumText.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		customerTransactionTable.setDisabled(isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		currencyWidget.setDisabled(isInViewMode());
		discountField.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (shippingTermsCombo != null)
			shippingTermsCombo.setDisabled(isInViewMode());
		super.onEdit();
		if (isInViewMode()) {
			balanceDueNonEditableText.setVisible(isInViewMode());
			paymentsNonEditableText.setVisible(isInViewMode());
		} else {
			balanceDueNonEditableText.setVisible(isInViewMode());
			paymentsNonEditableText.setVisible(isInViewMode());
		}
		transactionsTree.setEnabled(!isInViewMode());
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setDisabled(isInViewMode());
		}
		enableAttachmentPanel(!isInViewMode());
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
					ClientTransaction.TYPE_INVOICE, clientBrandingTheme.getID());
		}
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	private void resetFromView() {
		// custForm.getCellFormatter().setWidth(0, 1, "200");
		//
		// shipToAddress.getCellFormatter().setWidth(0, 1, "100");
		// shipToAddress.getCellFormatter().setWidth(0, 2, "200");

		// priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");
	}

	@Override
	public void printPreview() {

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect.setComboItem(taxCode);
			customerTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
	}

	@Override
	protected String getViewTitle() {
		return messages.invoice();
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
	public List<ClientTransactionItem> getAllTransactionItems() {
		return customerTransactionTable.getAllRows();
	}

	@Override
	protected boolean isBlankTransactionGrid() {
		return customerTransactionTable.getAllRows().isEmpty();
	}

	@Override
	public void addNewData(ClientTransactionItem transactionItem) {
		customerTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
		customerTransactionTable.updateTotals();
		transactionsTree.updateTransactionTreeItemTotals();
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		billToTextArea.setTabIndex(3);
		shipToAddress.setTabIndexforShiptocombo(4);
		shipToAddress.setTabIndex(5);
		transactionDateItem.setTabIndex(6);
		transactionNumber.setTabIndex(7);
		payTermsSelect.setTabIndex(8);
		dueDateItem.setTabIndex(9);
		orderNumText.setTabIndex(10);
		memoTextAreaItem.setTabIndex(11);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(12);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(13);
		cancelButton.setTabIndex(15);
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		customerTransactionTable.add(item);
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		this.customerTransactionTable.updateAmountsFromGUI();
		transactionsTree.refreshBillableTransactionTree();
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
		if (netAmountLabel != null) {
			netAmountLabel.setTitle(messages.currencyNetAmount(formalName));
		}
	}

	@Override
	protected ValidationResult validateBaseRequirement() {
		ValidationResult result = new ValidationResult();
		result.add(custForm.validate());
		result.add(super.validateBaseRequirement());
		return result;
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getAmount() != null) {
			customerTransactionTable.setDiscount(discountField.getAmount());
		} else {
			discountField.setAmount(0d);
		}
	}
}
