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
import com.vimukti.accounter.web.client.ui.DataUtils;
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
import com.vimukti.accounter.web.client.ui.edittable.TransactionsTree;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyFactorWidget;
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
	private CurrencyFactorWidget currencyWidget;
	private boolean locationTrackingEnabled;
	private DateField deliveryDate;
	protected ClientSalesPerson salesPerson;
	private AmountLabel netAmountLabel, vatTotalNonEditableText,
			balanceDueNonEditableText, paymentsNonEditableText,
			salesTaxTextNonEditable;

	// private WarehouseAllocationTable table;
	// private DisclosurePanel inventoryDisclosurePanel;

	private AmountLabel transactionTotalinForeignCurrency,
			transactionTotalinBaseCurrency;

	// private Double currencyfactor;
	// private ClientCurrency currencyCode;
	TransactionsTree<EstimatesAndSalesOrdersList> transactionsTree;

	private InvoiceView() {
		super(ClientTransaction.TYPE_INVOICE);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
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

			setBalanceDue(((ClientInvoice) transaction).getBalanceDue());

		}

	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	private void initPayments() {

		if (transaction != null) {

			ClientInvoice invoice = (ClientInvoice) transaction;

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
			ClientInvoice invoice = (ClientInvoice) transaction;
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
		DynamicForm termsForm = new DynamicForm();
		DynamicForm prodAndServiceForm1 = new DynamicForm();
		DynamicForm prodAndServiceForm2 = new DynamicForm();
		DynamicForm vatForm = new DynamicForm();
		amountsForm = new DynamicForm();
		DynamicForm priceLevelForm = new DynamicForm();

		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab1 = new Label(Accounter.constants().invoice());
		else {
			lab1 = new Label(Accounter.constants().invoice());
		}

		lab1.setStyleName(Accounter.constants().labelTitle());

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

		transactionNumber.setTitle(Accounter.constants().invoiceNo());
		listforms = new ArrayList<DynamicForm>();
		brandingThemeTypeCombo = new BrandingThemeCombo(Accounter.constants()
				.brandingTheme());

		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(true);
		// DATE form
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
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
		customerCombo = createCustomerComboItem(Accounter.messages().payeeName(
				Global.get().Customer()));
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

		billToTextArea.setTitle(Accounter.constants().billTo());
		billToTextArea.setDisabled(isInViewMode());
		billToTextArea.setHelpInformation(true);

		billToTextArea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("", "", billToTextArea, "Bill to",
						allAddresses);

			}
		});

		billToTextArea.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				new AddressDialog("", "", billToTextArea, "Bill to",
						allAddresses);

			}
		});

		shipToCombo = createShipToComboItem();

		shipToCombo.setHelpInformation(true);

		shipToAddress = new ShipToForm(null);
		shipToAddress.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);

		// shipToAddress.getCellFormatter().getElement(0, 0).setAttribute(
		// Accounter.constants().width(), "40px");
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
		// Accounter.constants().width(), "226px");
		custForm.setStyleName("align-form");

		salesPersonCombo = createSalesPersonComboItem();

		payTermsSelect = createPaymentTermsSelectItem();

		shippingTermsCombo = createShippingTermsCombo();

		shippingMethodsCombo = createShippingMethodCombo();

		dueDateItem = new DateField(customerConstants.dueDate());
		dueDateItem.setToolTip(Accounter.messages().selectDateUntilDue(
				this.getAction().getViewName()));
		dueDateItem.setHelpInformation(true);
		dueDateItem.setEnteredDate(getTransactionDate());
		dueDateItem.setColSpan(1);
		dueDateItem.setTitle(customerConstants.dueDate());
		dueDateItem.setDisabled(isInViewMode());
		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());

		orderNumText = new TextItem(Accounter.constants().salesorderno());
		orderNumText.setHelpInformation(true);
		orderNumText.setWidth(38);
		if (transaction != null)
			orderNumText.setDisabled(true);

		if (locationTrackingEnabled)
			termsForm.setFields(locationCombo);
		// termsForm.setWidth("100%");
		termsForm.setIsGroup(true);
		termsForm.setGroupTitle(customerConstants.terms());
		termsForm.setNumCols(2);
		if (getPreferences().isSalesPersonEnabled()) {
			termsForm.setFields(salesPersonCombo, payTermsSelect, dueDateItem,
					orderNumText);

			if (getPreferences().isDoProductShipMents())
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
		} else {
			termsForm.setFields(payTermsSelect, dueDateItem, orderNumText);
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
		// Accounter.constants().width(), "200px");
		// multi
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth("400px");

		Button printButton = new Button();

		printButton.setText(Accounter.constants().print());
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
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setFields(memoTextAreaItem);

		// VerticalPanel vPanel = new VerticalPanel();
		// vPanel.add(prodAndServiceForm1);
		// vPanel.setWidth("100%");
		// forms.add(prodAndServiceForm1);

		// priceLevelSelect = createPriceLevelSelectItem();
		taxCodeSelect = createTaxCodeSelectItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();

		netAmountLabel = createNetAmountLabel();

		transactionTotalinBaseCurrency = createTransactionTotalNonEditableLabel(getCompany()
				.getPreferences().getPrimaryCurrency());

		transactionTotalinForeignCurrency = createForeignCurrencyAmountLable(getCompany()
				.getPreferences().getPrimaryCurrency());

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		paymentsNonEditableText = new AmountLabel(customerConstants.payments());
		paymentsNonEditableText.setDisabled(true);
		paymentsNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");
		balanceDueNonEditableText = new AmountLabel(
				customerConstants.balanceDue());
		balanceDueNonEditableText.setDisabled(true);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();

		transactionsTree = new TransactionsTree<EstimatesAndSalesOrdersList>(
				this) {
			@Override
			public void updateTransactionTotal() {
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
				isTrackTax(), isTaxPerDetailLine(), this) {

			@Override
			public boolean isShowPriceWithVat() {
				return InvoiceView.this.isShowPriceWithVat();
			}

			@Override
			public void updateNonEditableItems() {
				InvoiceView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isInViewMode() {
				return InvoiceView.this.isInViewMode();
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

		amountsForm.setWidth("100%");
		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);

		priceLevelForm.setWidth("100%");
		amountsForm.setStyleName("boldtext");

		if (isTrackTax()) {
			amountsForm.setFields(netAmountLabel);
			if (!isTaxPerDetailLine()) {
				vatForm.setFields(taxCodeSelect, vatinclusiveCheck);
				amountsForm.setFields(salesTaxTextNonEditable);
				prodAndServiceHLay.add(vatForm);
			} else {
				amountsForm.setFields(vatTotalNonEditableText);
			}
		}
		amountsForm.setFields(transactionTotalinBaseCurrency);
		if (isMultiCurrencyEnabled()) {
			amountsForm.setFields(transactionTotalinForeignCurrency);
		}
		if (isInViewMode()) {
			amountsForm.setFields(paymentsNonEditableText,
					balanceDueNonEditableText);
		}

		prodAndServiceHLay.add(amountsForm);
		prodAndServiceHLay.setCellHorizontalAlignment(amountsForm, ALIGN_RIGHT);

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
				transactionTotalinForeignCurrency.hide();
			}
		}
		settabIndexes();

	}

	private ShippingTermsCombo createShippingTermsCombo() {

		final ShippingTermsCombo shippingTermsCombo = new ShippingTermsCombo(
				Accounter.constants().shippingTerms());
		shippingTermsCombo.setHelpInformation(true);
		shippingTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {

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
		if (isInViewMode()) {
			emailButton = new Button(accounterConstants.email());
			buttonBar.add(emailButton);

			emailButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ActionFactory.getEmailViewAction().run(transaction, false);

				}
			});
		}
	}

	public void showMenu(Widget button) {
		setMenuItems(button, Accounter.constants().productOrServiceItem());

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
		// if (this.transaction == null || customerTransactionTable != null) {
		// customerTransactionTable.setPricingLevel(priceLevel);
		// customerTransactionTable.updatePriceLevel();
		// }
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
		if (isTrackTax()) {
			setSalesTax(customerTransactionTable.getTotalTax()
					+ transactionsTree.getTotalTax());
			vatTotalNonEditableText.setAmount(customerTransactionTable
					.getTotalTax() + transactionsTree.getTotalTax());
			netAmountLabel
					.setAmount(getAmountInTransactionCurrency(customerTransactionTable
							.getLineTotal() + transactionsTree.getLineTotal()));
		}

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
		if (this.getCustomer() != null && !this.getCustomer().equals(customer)
				&& transaction == null)
			customerTransactionTable.clear();

		this.setCustomer(customer);
		super.customerSelected(customer);
		shippingTermSelected(shippingTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		// for (ClientPaymentTerms paymentTerm : paymentTermsList) {
		// if (paymentTerm.getName().equals(
		// Accounter.constants().dueOnReceipt())) {
		// payTermsSelect.addItemThenfireEvent(paymentTerm);
		// break;
		// }
		// }

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

		// if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
		// super.setCustomerTaxCodetoAccount();

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		if (addressListOfCustomer != null) {
			Iterator it = addressListOfCustomer.iterator();
			while (it.hasNext()) {
				ClientAddress add = (ClientAddress) it.next();

				allAddresses.put(add.getType(), add);
			}
		}
		long currency = customer.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		} else {
			ClientCurrency clientCurrency = getCompany().getCurrency(
					getCompany().getPreferences().getPrimaryCurrency());
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}
		if (isMultiCurrencyEnabled()) {
			super.setCurrencycode(getCompany().getCurrency(
					customer.getCurrency()));
			setCurrencyFactor(1.0);
			updateAmountsFromGUI();
			// modifyForeignCurrencyTotalWidget();
		}

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
					this.currency = getCompany().getCurrency(
							getCompany().getPreferences().getPrimaryCurrency());
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
			this.orderNumText
					.setValue(transaction.getOrderNum() != null ? transaction
							.getOrderNum() : "");
			if (getCustomer() != null && customerCombo != null) {
				customerCombo.setComboItem(getCustomer());
				getEstimatesAndSalesOrder();
			}

			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			if (getCustomer() != null)
				addresses.addAll(getCustomer().getAddress());

			Iterator<ClientAddress> it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress add = (ClientAddress) it.next();

				allAddresses.put(add.getType(), add);
			}
			shipToAddress.setListOfCustomerAdress(addresses);
			if (shippingAddress != null) {
				shipToAddress.businessSelect.setValue(UIUtils
						.getAddressesTypes(shippingAddress.getType()));
				shipToAddress.setAddres(shippingAddress);
			}

			this.addressListOfCustomer = getCustomer().getAddress();

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

			if (transaction.getDeliverydate() != 0)
				this.deliveryDate.setValue(new ClientFinanceDate(transaction
						.getDeliverydate()));
			this.dueDateItem
					.setValue(transaction.getDueDate() != 0 ? new ClientFinanceDate(
							transaction.getDueDate()) : getTransactionDate());
			if (isTrackTax()) {
				if (isTaxPerDetailLine()) {
					netAmountLabel
							.setAmount(getAmountInTransactionCurrency(transaction
									.getNetAmount()));
					vatTotalNonEditableText
							.setAmount(getAmountInTransactionCurrency(transaction
									.getTotal() - transaction.getNetAmount()));
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect
								.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
					} else {
						this.taxCode = getCompany().getTAXCode(
								getCustomer().getTAXCode());
						if (taxCode != null) {
							this.taxCodeSelect.setComboItem(taxCode);
						}
					}
					this.salesTaxTextNonEditable.setValue(DataUtils
							.getAmountAsString(transaction.getTaxTotal()));

				}
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
				}
			}

			if (locationTrackingEnabled)
				locationSelected(company.getLocation(transaction.getLocation()));
			transactionTotalinBaseCurrency
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			transactionTotalinForeignCurrency
					.setAmount(getAmountInTransactionCurrency(transaction
							.getTotal()));
			paymentsNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction
							.getPayments()));
			balanceDueNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction
							.getBalanceDue()));
			memoTextAreaItem.setDisabled(true);
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

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

		initTransactionsItems();

	}

	@Override
	public void initTransactionsItems() {
		if (transaction.getTransactionItems() != null
				&& !transaction.getTransactionItems().isEmpty())
			customerTransactionTable.setAllRows(transaction
					.getTransactionItems());
	}

	protected void shipToAddressSelected(ClientAddress selectItem) {
		this.shippingAddress = selectItem;
		if (this.shippingAddress != null && shipToAddress != null)
			shipToCombo.setComboItem(this.shippingAddress);
	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transaction != null) {
			Double salesTaxAmout = ((ClientInvoice) transaction).getTaxTotal();
			setSalesTax(salesTaxAmout);

		}

	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;

		if (salesTaxTextNonEditable != null)
			salesTaxTextNonEditable
					.setAmount(getAmountInTransactionCurrency(salesTax));

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = ((ClientInvoice) transaction).getTotal();
			setTransactionTotal(transactionTotal);
		}
	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		if (transactionTotalinBaseCurrency != null) {
			transactionTotalinBaseCurrency.setAmount(transactionTotal);
			transactionTotalinForeignCurrency
					.setAmount(getAmountInTransactionCurrency(transactionTotal));
		}

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transaction != null) {

			ClientInvoice invoice = (ClientInvoice) transaction;

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
				estimates.add((ClientEstimate) clientTransaction);
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

			transaction
					.setTaxTotal(getAmountInBaseCurrency(salesTaxTextNonEditable
							.getAmount()));
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
		if (isTrackTax()) {
			// if (isTaxPerDetailLine()) {
			transaction.setNetAmount(getAmountInBaseCurrency(netAmountLabel
					.getAmount()));
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
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
		transaction.setTotal(transactionTotalinBaseCurrency.getAmount());

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
			getEstimatesAndSalesOrder();
		}
		// result.add(super.validate());

		// Validations
		// 1. IF(!isValidDueOrDeliveryDates(dueDate, transactionDate)) ERROR

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				((InvoiceView) this).dueDateItem.getDate(),
				getTransactionDate())) {
			result.addError(((InvoiceView) this).dueDateItem, Accounter
					.constants().the()
					+ " "
					+ customerConstants.dueDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());
		}

		boolean isSelected = transactionsTree.validateTree();
		if (!isSelected) {
			if (transaction.getTotal() <= 0) {
				result.addError(this, Accounter.constants()
						.transactiontotalcannotbe0orlessthan0());
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
						accounterConstants.enterTaxCode());
			}
		} else if (isSelected && isTrackTax() && !isTaxPerDetailLine()
				&& !transaction.getTransactionItems().isEmpty()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						accounterConstants.enterTaxCode());
			}
		}
		return result;
	}

	public void setPayments(Double payments) {
		if (payments == null)
			payments = 0.0D;
		this.payments = payments;
		paymentsNonEditableText
				.setAmount(getAmountInTransactionCurrency(payments));
	}

	public Double getPayments() {
		return payments;
	}

	public void setBalanceDue(Double balanceDue) {
		if (balanceDue == null)
			balanceDue = 0.0D;
		this.balanceDue = balanceDue;
		balanceDueNonEditableText
				.setAmount(getAmountInTransactionCurrency(balanceDue));
	}

	public static InvoiceView getInstance() {

		return new InvoiceView();
	}

	private void getEstimatesAndSalesOrder() {
		if (this.rpcUtilService == null)
			return;
		if (getCustomer() == null) {
			Accounter.showError(Accounter.messages().pleaseSelect(
					Global.get().customer()));
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
						for (ClientTransaction clientTransaction : salesAndEstimates) {
							for (EstimatesAndSalesOrdersList estimatesalesorderlist : result) {
								if (estimatesalesorderlist.getTransactionId() == clientTransaction
										.getID()) {
									estimatesList.add(estimatesalesorderlist);
								}
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
							.quotesSelected(transaction.getEstimates() != null ? transaction
									.getEstimates()
									: new ArrayList<ClientEstimate>());
					transactionsTree.salesOrdersSelected(transaction
							.getSalesOrders() != null ? transaction
							.getSalesOrders()
							: new ArrayList<ClientSalesOrder>());
					transactionsTree.setEnabled(!isInViewMode());
					refreshTransactionGrid();
				}
			};
			this.rpcUtilService.getEstimatesAndSalesOrdersList(getCustomer()
					.getID(), callback);
		}
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

		// if (UIUtils.isMSIEBrowser())
		// custForm.setWidth("100%");

		setMode(EditMode.EDIT);

		if (!isInViewMode()) {

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
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());

		}
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
			UIUtils.downloadAttachment(((ClientInvoice) transaction).getID(),
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
		return Accounter.constants().invoice();
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
		saveAndCloseButton.setTabIndex(13);
		saveAndNewButton.setTabIndex(14);
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
	}

	public void modifyForeignCurrencyTotalWidget() {
		if (currencyWidget.isShowFactorField()) {
			transactionTotalinForeignCurrency.hide();
		} else {
			transactionTotalinForeignCurrency.show();
			transactionTotalinForeignCurrency.setTitle(Accounter.messages()
					.currencyTotal(
							currencyWidget.getSelectedCurrency()
									.getFormalName()));
		}
	}
}
