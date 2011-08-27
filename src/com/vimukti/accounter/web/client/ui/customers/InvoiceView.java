package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
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
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
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
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyWidget;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author Fernandez
 * @modified by B.Srinivasa Rao
 * 
 */
public class InvoiceView extends AbstractCustomerTransactionView<ClientInvoice>
		implements IPrintableView {

	private CurrencyWidget currencyWidget;
	private Button recurringButton;

	private InvoiceView() {
		super(ClientTransaction.TYPE_INVOICE, CUSTOMER_TRANSACTION_GRID);

	}

	private BrandingThemeCombo brandingThemeTypeCombo;
	DateField dueDateItem;
	private Double payments = 0.0;
	private Double balanceDue = 0.0;
	private CustomerQuoteListDialog dialog;
	private LabelItem quoteLabel;
	private long selectedEstimateId;
	private long selectedSalesOrder;
	private ArrayList<DynamicForm> listforms;
	private ArrayList<ClientTransaction> selectedOrdersAndEstimates;
	private TextAreaItem billToTextArea;
	private ShipToForm shipToAddress;
	private TextItem orderNumText;
	HorizontalPanel hpanel;
	DynamicForm amountsForm;
	private LinkedHashMap<Integer, ClientAddress> allAddresses;
	private Button emailButton;

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

	@Override
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

		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)

			lab1 = new Label(Accounter.constants().invoice());

		else {
			// lab1 = new Label("Invoice(" + getTransactionStatus() + ")");
			lab1 = new Label(Accounter.constants().invoice());
		}

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
		transactionDateItem.setHelpInformation(true);
		transactionNumber = createTransactionNumberItem();

		transactionNumber.setTitle(Accounter.constants().invoiceNo());
		listforms = new ArrayList<DynamicForm>();
		brandingThemeTypeCombo = new BrandingThemeCombo(Accounter.constants()
				.brandingTheme());

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);
		/*
		 * if (ClientCompanyPreferences.get().isEnableMultiCurrency() == false)
		 * { if (currencyWidget == null) {
		 * 
		 * currencyWidget = bulidCurrencyWidget(); } else currencyWidget =
		 * getCurrencyWidget();
		 * 
		 * forms.add(currencyWidget); currencyWidget.setListener(new
		 * CurrencyChangeListener() {
		 * 
		 * @Override public void currencyChanged(ClientCurrency currency, double
		 * factor) { customerTransactionGrid.refreshAllRecords();
		 * vendorTransactionGrid.refreshAllRecords(); } });
		 * 
		 * }
		 */

		customerCombo = createCustomerComboItem(Accounter.messages()
				.customerName(Global.get().Customer()));
		customerCombo.setHelpInformation(true);
		customerCombo.setWidth("100%");
		quoteLabel = new LabelItem();
		if (getPreferences().isDoyouwantEstimates()
				&& getPreferences().isSalesOrderEnabled()) {
			quoteLabel.setValue(Accounter.constants().quotesandsalesOrder());
		} else if (getPreferences().isSalesOrderEnabled()) {
			quoteLabel.setValue(Accounter.constants().salesOrder());

		} else if (getPreferences().isDoyouwantEstimates()) {
			quoteLabel.setValue(Accounter.constants().quotes());
		}
		quoteLabel.setWidth("100%");
		quoteLabel.addStyleName("falseHyperlink");
		quoteLabel.setShowTitle(false);
		quoteLabel.setDisabled(isInViewMode());
		LabelItem emptylabel = new LabelItem();
		emptylabel.setValue("");
		emptylabel.setWidth("100%");
		emptylabel.setShowTitle(false);

		quoteLabelListener();
		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		// billToCombo = createBillToComboItem();

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

		shipToAddress.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "40px");
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
		// phoneSelect = new SelectItem();
		// phoneSelect.setWidth(100);
		// phoneSelect.setTitle(customerConstants.phone());
		// phoneSelect.setDisabled(isEdit);
		// phoneSelect.addChangeHandler(new ChangeHandler() {
		//
		// public void onChange(ChangeEvent event) {
		//
		// phoneNo = phoneSelect.getValue().toString();
		//
		// }
		// });
		custForm = UIUtils.form(Global.get().customer());
		custForm.setNumCols(3);
		custForm.setWidth("100%");
		currencyWidget = createCurrencyWidget();
		currencyWidget.setListener(new CurrencyChangeListener() {

			@Override
			public void currencyChanged(String currency, double factor) {
				// TODO the modify the changing items here upon currency
				// 1) update grid fields
				// 2) update off-grid-fields [total amount, toatl vat, net
				// total].
				System.out.println("Currency Changed: " + currency + " Factor:"
						+ factor);
			}
		});
		custForm.setFields(customerCombo, quoteLabel, contactCombo, emptylabel,
				billToTextArea, emptylabel);
		custForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");

		custForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "226px");
		custForm.setStyleName("align-form");

		if (UIUtils.isMSIEBrowser()) {
			if (transaction != null)
				custForm.setWidth("74%");
		}

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

		DynamicForm termsForm = new DynamicForm();
		termsForm.setWidth("100%");
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

		termsForm.setStyleName("align-form");

		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "200px");
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

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setFields(memoTextAreaItem);
		// memoTextAreaItem.getMainWidget().getParent().setWidth("70%");

		// VerticalPanel vPanel = new VerticalPanel();
		// vPanel.add(prodAndServiceForm1);
		// vPanel.setWidth("100%");
		// forms.add(prodAndServiceForm1);

		priceLevelSelect = createPriceLevelSelectItem();
		taxCodeSelect = createTaxCodeSelectItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();

		netAmountLabel = createNetAmountLabel();

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

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

		customerTransactionGrid = getGrid();
		customerTransactionGrid.setTransactionView(this);

		customerTransactionGrid.isEnable = false;

		customerTransactionGrid.init();
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isInViewMode());
		customerTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("50%");
		prodAndServiceForm2.setNumCols(4);
		prodAndServiceForm2.setCellSpacing(5);
		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);

		brandingThemeTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBrandingTheme>() {

					@Override
					public void selectedComboBoxItem(
							ClientBrandingTheme selectItem) {
					}
				});
		amountsForm = new DynamicForm();
		amountsForm.setWidth("100%");
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {

			DynamicForm priceLevelForm = new DynamicForm();
			// priceLevelForm.setCellSpacing(4);
			priceLevelForm.setWidth("70%");
			priceLevelForm.setFields(priceLevelSelect);
			amountsForm.setFields(netAmountLabel, vatTotalNonEditableText,
					transactionTotalNonEditableText, paymentsNonEditableText,
					balanceDueNonEditableText);
			amountsForm.setStyleName("invoice-total");
			// forms.add(priceLevelForm);
			// prodAndServiceHLay.add(priceLevelForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(priceLevelForm,
			// ALIGN_RIGHT);
			// prodAndServiceHLay.add(amountsForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
			// ALIGN_RIGHT);
			// listforms.add(priceLevelForm);

		} else {

			// prodAndServiceForm2.setFields(salesTaxTextNonEditable,
			// transactionTotalNonEditableText, paymentsNonEditableText,
			// balanceDueNonEditableText, taxCodeSelect, priceLevelSelect);
			amountsForm.setNumCols(4);
			amountsForm.addStyleName("tax-form");

			if (getPreferences().getDoYouPaySalesTax()) {
				amountsForm.setFields(taxCodeSelect, salesTaxTextNonEditable,
						disabletextbox, transactionTotalNonEditableText,
						disabletextbox, paymentsNonEditableText,
						disabletextbox, balanceDueNonEditableText);
			} else {
				amountsForm.setFields(transactionTotalNonEditableText,
						disabletextbox, paymentsNonEditableText,
						disabletextbox, balanceDueNonEditableText);
			}

			prodAndServiceHLay.add(amountsForm);
			prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
					ALIGN_RIGHT);
		}
		hpanel = new HorizontalPanel();
		hpanel.setHorizontalAlignment(ALIGN_RIGHT);
		hpanel.add(createAddNewButton());

		hpanel.getElement().getStyle().setMarginTop(8, Unit.PX);

		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.setWidth("100%");
		panel.add(hpanel);

		panel.add(amountsForm);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(amountsForm);
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			prodAndServiceHLay.setCellWidth(amountsForm, "30%");
		} else
			prodAndServiceHLay.setCellWidth(amountsForm, "50%");
		VerticalPanel panel11 = new VerticalPanel();
		panel11.setWidth("100%");
		panel11.add(panel);
		panel11.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setHorizontalAlignment(ALIGN_LEFT);
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);
		leftVLay.add(shipToAddress);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setHorizontalAlignment(ALIGN_RIGHT);
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setSpacing(10);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "44%");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(printButton);

		mainVLay.add(customerTransactionGrid);

		mainVLay.add(panel11);

		if (UIUtils.isMSIEBrowser())
			resetFromView();

		// recurringButton = new Button();
		// recurringButton.setText("Make it recurring");
		// recurringButton.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		//
		// if (transaction.getRecurringTransaction() == 0) {
		// // create new recurring for this transaction
		// openRecurringDialog();
		// } else {
		// // open existing recurring transaction.
		// Accounter.createGETService().getObjectById(
		// AccounterCoreType.RECURRING_TRANSACTION,
		// transaction.getRecurringTransaction(),
		// new AsyncCallback<ClientRecurringTransaction>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// Accounter
		// .showError("Unable to copen recurring transaction "
		// + caught);
		// }
		//
		// @Override
		// public void onSuccess(
		// ClientRecurringTransaction result) {
		// openRecurringDialog(result);
		// }
		// });
		// }
		//
		// }
		// });
		// mainVLay.add(recurringButton);

		this.add(mainVLay);

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

	// for new recurring
	private void openRecurringDialog() {
		openRecurringDialog(null);
	}

	// for editing existing recurring
	private void openRecurringDialog(ClientRecurringTransaction result) {

		RecurringTransactionDialog dialog = null;
		if (result == null) {
			dialog = new RecurringTransactionDialog(this);
		} else {
			dialog = new RecurringTransactionDialog(result);
		}

		dialog.setCallback(new ActionCallback<ClientRecurringTransaction>() {

			@Override
			public void actionResult(ClientRecurringTransaction result) {
				System.out.println("Recurring result" + result);
			}
		});
		dialog.show();
	}

	public void showMenu(Widget button) {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			setMenuItems(button, Accounter.constants().serviceItem(), Accounter
					.constants().productItem());
		else
			setMenuItems(button, Accounter.constants().serviceItem(), Accounter
					.constants().productItem());

	}

	private void quoteLabelListener() {
		if (!isInViewMode()) {
			quoteLabel.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					getEstimatesAndSalesOrder();
				}
			});
		}

	}

	public AbstractTransactionGrid<ClientTransactionItem> getGridForPrinting() {
		return customerTransactionGrid;
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
		if (priceLevel != null && priceLevelSelect != null) {

			priceLevelSelect.setComboItem(getCompany().getPriceLevel(
					priceLevel.getID()));

		}
		if (this.transaction == null || customerTransactionGrid != null) {
			customerTransactionGrid.priceLevelSelected(priceLevel);
			customerTransactionGrid.updatePriceLevel();
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

		if (customerTransactionGrid == null)
			return;
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			Double taxableLineTotal = customerTransactionGrid
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;

			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(),
					taxableLineTotal,
					Accounter.getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionGrid.getTotal()
					+ this.salesTax);
		} else {
			if (customerTransactionGrid.getGrandTotal() != 0
					&& customerTransactionGrid.getTotalValue() != 0) {
				netAmountLabel.setAmount(customerTransactionGrid
						.getGrandTotal());
				vatTotalNonEditableText.setAmount(customerTransactionGrid
						.getTotalValue()
						- customerTransactionGrid.getGrandTotal());
				setTransactionTotal(customerTransactionGrid.getTotalValue());
			}
		}
		Double payments = this.paymentsNonEditableText.getAmount();
		if (transaction != null) {
			payments = this.transactionTotal < payments ? this.transactionTotal
					: payments;
			setPayments(payments);
		}
		setBalanceDue((this.transactionTotal - payments));
	}

	@Override
	protected void customerSelected(final ClientCustomer customer) {

		updateSalesOrderOrEstimate(customer);

		if (this.getCustomer() != null && !this.getCustomer().equals(customer)
				&& transaction == null)
			customerTransactionGrid.removeAllRecords();

		this.setCustomer(customer);
		super.customerSelected(customer);
		selectedOrdersAndEstimates = new ArrayList<ClientTransaction>();

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

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setCustomerTaxCodetoAccount();

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		if (addressListOfCustomer != null) {
			Iterator it = addressListOfCustomer.iterator();
			while (it.hasNext()) {
				ClientAddress add = (ClientAddress) it.next();

				allAddresses.put(add.getType(), add);
			}
		}
	}

	/**
	 * Update sales orders and estimates when customer has been changed during
	 * edit of transactions. if customer changed remove present records in grid
	 * or if customer revert to old customer.than Reinitialise records in grid
	 * again.
	 * 
	 * @param customer
	 */
	private void updateSalesOrderOrEstimate(ClientCustomer customer) {
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientInvoice inv = (ClientInvoice) this.transaction;

			if (inv.getCustomer() == customer.getID()) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.setRecords(inv
						.getTransactionItems());
				selectedSalesOrder = inv.getSalesOrder();
				selectedEstimateId = inv.getEstimate();

			} else {

				selectedSalesOrder = 0;
				selectedEstimateId = 0;
			}
		}

	}

	protected void showQuotesDialog(List<EstimatesAndSalesOrdersList> result) {
		// if (result == null)
		// return;

		List<EstimatesAndSalesOrdersList> filteredList = new ArrayList<EstimatesAndSalesOrdersList>();
		filteredList.addAll(result);

		if (dialog == null) {
			dialog = new CustomerQuoteListDialog(this, filteredList);
		}

		dialog.setQuoteList(filteredList);
		dialog.show();

		if (filteredList.isEmpty()) {
			dialog.grid.addEmptyMessage("No records to show");
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

	public void selectedQuote(ClientEstimate selectedEstimate) {
		if (selectedEstimate == null)
			return;
		for (ClientTransactionItem record : this.customerTransactionGrid
				.getRecords()) {
			for (ClientTransactionItem salesRecord : selectedEstimate
					.getTransactionItems())
				if (record.getReferringTransactionItem() == salesRecord.getID())
					customerTransactionGrid.deleteRecord(record);

		}
		// if (dialog.preCustomer == null || dialog.preCustomer !=
		// this.customer) {
		// dialog.preCustomer = this.customer;
		// } else {
		// return;
		// }

		if (selectedOrdersAndEstimates != null)
			selectedOrdersAndEstimates.add(selectedEstimate);

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : selectedEstimate
				.getTransactionItems()) {
			if (item.getLineTotal() - item.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (item.getLineTotal() != 0.0) {
				clientItem.setDescription(item.getDescription());
				clientItem.setType(item.getType());
				clientItem.setAccount(item.getAccount());
				clientItem.setItem(item.getItem());
				clientItem.setVatItem(item.getVatItem());
				clientItem.setVATfraction(item.getVATfraction());
				// clientItem.setVatCode(item.getTaxCode());
				clientItem.setTaxCode(item.getTaxCode());
				clientItem.setDescription(item.getDescription());
				clientItem.setQuantity(item.getQuantity());
				clientItem.setUnitPrice(item.getUnitPrice());
				clientItem.setDiscount(item.getDiscount());
				clientItem.setLineTotal(item.getLineTotal()
						- item.getInvoiced());
				clientItem.setTaxable(item.isTaxable());
				clientItem.setReferringTransactionItem(item.getID());
				itemsList.add(clientItem);
			}

		}
		selectedEstimateId = selectedEstimate.getID();
		orderNum = selectedEstimate.getNumber();
		orderNumText.setValue(orderNum);
		customerTransactionGrid.setAllTransactionItems(itemsList);

		// if (selectedEstimate == null)
		// return;
		// if (selectedOrdersAndEstimates != null)
		// selectedOrdersAndEstimates.add(selectedEstimate);
		//
		// ClientInvoice convertedIinvoice = convertToInvoice(selectedEstimate);
		//
		// selectedEstimateId = selectedEstimate.getID();
		//
		// if (convertedIinvoice == null) {
		// Accounter.showError("Could Not Load the Quote....");
		// return;
		// }
		//
		// // initTransactionViewData(convertedIinvoice);
		// this.transactionItems = convertedIinvoice.getTransactionItems();
		// customerTransactionGrid.setAllTransactions(transactionItems);
		// // customerTransactionGrid.updateData(obj)

	}

	private ClientInvoice convertToInvoice(ClientEstimate selectedEstimate) {

		ClientInvoice invoice = new ClientInvoice(selectedEstimate);
		setShippingAdress(invoice);
		for (ClientTransactionItem item : invoice.getTransactionItems()) {

			item.setID(0);
		}

		return invoice;
	}

	private void setShippingAdress(ClientInvoice invoice) {
		ClientCustomer customer = Accounter.getCompany().getCustomer(
				invoice.getCustomer());
		this.addressListOfCustomer = customer.getAddress();
		ClientAddress shippingAdressValue = getAddress(ClientAddress.TYPE_SHIP_TO);
		invoice.setShippingAdress(shippingAdressValue);

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientInvoice());
		} else {
			ClientCompany company = Accounter.getCompany();
			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			this.contact = transaction.getContact();
			// customerSelected(company.getCustomer(invoiceToBeEdited.getCustomer()));

			if (transaction.getPhone() != null)
				this.phoneNo = transaction.getPhone();
			// phoneSelect.setValue(this.phoneNo);
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
			// this.taxCode =
			// getTaxItemGroupForTransactionItems(this.transactionItems);
			if (getCustomer() != null && customerCombo != null) {
				customerCombo.setComboItem(getCustomer());
			}

			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			if (getCustomer() != null)
				addresses.addAll(getCustomer().getAddress());
			allAddresses = new LinkedHashMap<Integer, ClientAddress>();
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
			// billToaddressSelected(this.billingAddress);
			// shipToAddressSelected(this.shippingAddress);
			contactSelected(this.contact);
			paymentTermsSelected(this.paymentTerm);
			priceLevelSelected(this.priceLevel);
			salesPersonSelected(this.salesPerson);
			shippingMethodSelected(this.shippingMethod);
			shippingTermSelected(this.shippingTerm);
			taxCodeSelected(this.taxCode);
			if (transaction.getMemo() != null)
				memoTextAreaItem.setValue(transaction.getMemo());
			// if (invoiceToBeEdited.getReference() != null)
			// refText.setValue(invoiceToBeEdited.getReference());

			if (transaction.getDeliverydate() != 0)
				this.deliveryDate.setValue(new ClientFinanceDate(transaction
						.getDeliverydate()));
			this.dueDateItem
					.setValue(transaction.getDueDate() != 0 ? new ClientFinanceDate(
							transaction.getDueDate()) : getTransactionDate());

			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				netAmountLabel.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
				// vatinclusiveCheck.setValue(invoiceToBeEdited.isAmountsIncludeVAT());
			} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
				this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
				if (taxCode != null) {
					this.taxCodeSelect
							.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
				}
				this.salesTaxTextNonEditable.setValue(String
						.valueOf(transaction.getSalesTaxAmount()));
			}

			transactionTotalNonEditableText.setAmount(transaction.getTotal());
			paymentsNonEditableText.setAmount(transaction.getPayments());
			balanceDueNonEditableText.setAmount(transaction.getBalanceDue());
			quoteLabel.setDisabled(true);
			customerTransactionGrid.setCanEdit(false);
			memoTextAreaItem.setDisabled(true);
		}
		super.initTransactionViewData();

		initPaymentTerms();
		initShippingTerms();
		initShippingMethod();
		initDueDate();
		initPayments();
		initBalanceDue();
	}

	protected void shipToAddressSelected(ClientAddress selectItem) {
		this.shippingAddress = selectItem;
		if (this.shippingAddress != null && shipToAddress != null)
			shipToCombo.setComboItem(this.shippingAddress);
	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transaction != null) {
			Double salesTaxAmout = ((ClientInvoice) transaction)
					.getSalesTaxAmount();
			setSalesTax(salesTaxAmout);

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = ((ClientInvoice) transaction).getTotal();
			setTransactionTotal(transactionTotal);

		}

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transaction != null) {

			ClientInvoice invoice = (ClientInvoice) transaction;

			if (invoice.getMemo() != null) {
				memoTextAreaItem.setValue(invoice.getMemo());
				// refText.setValue(invoice.getReference());
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
		if (Accounter.getCompany().getAccountingType() == 0)
			transaction.setSalesTaxAmount(salesTaxTextNonEditable.getAmount());
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

		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			if (taxCode != null) {
				for (ClientTransactionItem record : customerTransactionGrid
						.getRecords()) {
					record.setTaxItemGroup(taxCode.getID());

				}
			}
			transaction.setSalesTaxAmount(this.salesTax);
		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			transaction.setNetAmount(netAmountLabel.getAmount());
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		}
		transaction.setTotal(transactionTotalNonEditableText.getAmount());
		// transaction.setBalanceDue(getBalanceDue());
		transaction.setPayments(getPayments());
		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());

		ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
				transactionDateItem.getEnteredDate(), paymentTerm);
		transaction.setDiscountDate(discountDate.getDate());

		if (selectedEstimateId != 0)
			transaction.setEstimate(selectedEstimateId);
		if (selectedSalesOrder != 0)
			transaction.setSalesOrder(selectedSalesOrder);

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		ClientCustomer previousCustomer = getCustomer();

		if (getCustomer() != null && getCustomer() != previousCustomer) {
			getEstimatesAndSalesOrder();
		}
		result.add(super.validate());

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

		// FIXME :: do we require orderNumText? if not remove the related code.
		if (!orderNumText.getValue().equals("")) {
			if (isNumberCorrect((String) orderNumText.getValue()) == 1) {
				result.addError(orderNumText, Accounter.constants()
						.salesOrderNumberGrater0());
			} else if (isNumberCorrect((String) orderNumText.getValue()) == 2) {
				result.addError(orderNumText, Accounter.constants()
						.salesOrderNumberPositive());
			} else if (isNumberCorrect((String) orderNumText.getValue()) == 3) {
				result.addError(orderNumText, Accounter.constants()
						.salesOrderNumberGrater0());
			}
		}
		return result;
	}

	private int isNumberCorrect(String value) {
		try {
			if (checkIfNotNumber(value)) {
				throw new NumberFormatException(Accounter.constants()
						.salesOrderNumber());
			}
		} catch (Exception e) {
			return 1;
		}
		try {
			if (Integer.parseInt(value) < 0) {
				throw new InvalidEntryException(Accounter.constants()
						.salesOrderNumberPositive());
			}
		} catch (Exception e) {
			return 2;
		}

		try {
			if (Integer.parseInt(value) == 0) {
				throw new InvalidEntryException(Accounter.constants()
						.salesOrderNumberGrater0());
			}
		} catch (Exception e) {
			return 3;
		}
		return 0;
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
		if (this.rpcUtilService == null)
			return;
		if (getCustomer() == null) {
			Accounter.showError(Accounter.messages().pleaseSelectCustomer(
					Global.get().customer()));
		} else {

			// if (dialog != null && dialog.preCustomer != null
			// && dialog.preCustomer == this.customer) {
			// return;
			// }
			AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>> callback = new AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>>() {

				@Override
				public void onFailure(Throwable caught) {
					// Accounter.showError(Accounter
					// .constants()
					// .noQuotesAndSalesOrderForCustomer()
					// + " " + customer.getName());
					return;
				}

				@Override
				public void onSuccess(
						ArrayList<EstimatesAndSalesOrdersList> result) {
					if (result == null)
						onFailure(new Exception());

					// if (result.size() > 0) {
					showQuotesDialog(result);
					// } else {
					// showQuotesDialog(result);
					// }

				}

			};

			this.rpcUtilService.getEstimatesAndSalesOrdersList(getCustomer()
					.getID(), callback);

		}
	}

	public void selectedSalesOrder(ClientSalesOrder salesOrder) {
		// this.transactionItems = salesOrder.getTransactionItems();
		if (salesOrder == null)
			return;
		for (ClientTransactionItem record : this.customerTransactionGrid
				.getRecords()) {
			for (ClientTransactionItem salesRecord : salesOrder
					.getTransactionItems())

				if (record.getReferringTransactionItem() == salesRecord.getID())
					customerTransactionGrid.deleteRecord(record);

		}
		// if (dialog.preCustomer == null || dialog.preCustomer !=
		// this.customer) {
		// dialog.preCustomer = this.customer;
		// } else {
		// return;
		// }

		if (selectedOrdersAndEstimates != null)
			selectedOrdersAndEstimates.add(salesOrder);

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : salesOrder.getTransactionItems()) {
			if (item.getLineTotal() - item.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (item.getLineTotal() != 0.0) {
				clientItem.setDescription(item.getDescription());
				clientItem.setType(item.getType());
				clientItem.setAccount(item.getAccount());
				clientItem.setItem(item.getItem());
				clientItem.setVatItem(item.getVatItem());
				clientItem.setVATfraction(item.getVATfraction());
				// clientItem.setVatCode(item.getTaxCode());
				clientItem.setTaxCode(item.getTaxCode());
				clientItem.setDescription(item.getDescription());
				clientItem.setQuantity(item.getQuantity());
				clientItem.setUnitPrice(item.getUnitPrice());
				clientItem.setDiscount(item.getDiscount());
				clientItem.setLineTotal(item.getLineTotal()
						- item.getInvoiced());
				clientItem.setTaxable(item.isTaxable());
				clientItem.setReferringTransactionItem(item.getID());
				itemsList.add(clientItem);
			}

		}
		selectedSalesOrder = salesOrder.getID();
		orderNum = salesOrder.getNumber();
		orderNumText.setValue(orderNum);
		customerTransactionGrid.setAllTransactionItems(itemsList);
		customerTransactionGrid.refreshVatValue();
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
					int errorCode = ((AccounterException) caught).getErrorCode();
					Accounter.showError(AccounterExceptions.getErrorString(errorCode));

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

		if (UIUtils.isMSIEBrowser())
			custForm.setWidth("100%");

		setMode(EditMode.EDIT);

		if (!isInViewMode()) {

			getButtonBar().remove(emailButton);
		}

		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());

		customerCombo.setDisabled(isInViewMode());
		quoteLabel.setDisabled(isInViewMode());
		quoteLabelListener();

		shipToAddress.businessSelect.setDisabled(isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setDisabled(isInViewMode());
		payTermsSelect.setDisabled(isInViewMode());

		dueDateItem.setDisabled(isInViewMode());
		deliveryDate.setDisabled(isInViewMode());

		priceLevelSelect.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());

		orderNumText.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());

		customerTransactionGrid.setDisabled(isInViewMode());
		customerTransactionGrid.setCanEdit(true);
		super.onEdit();

	}

	@Override
	public void print() {
		updateTransaction();
		ActionFactory.getBrandingThemeComboAction().run(transaction, false);
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				if (getPreferences().isSalesPersonEnabled())
					this.salesPersonCombo
							.addComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.addComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_TERM)
				this.shippingTermsCombo
						.addComboItem((ClientShippingTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shippingMethodsCombo
						.addComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.addComboItem((ClientPriceLevel) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.updateComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				if (getPreferences().isSalesPersonEnabled())
					this.salesPersonCombo
							.updateComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.updateComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_TERM)
				this.shippingTermsCombo
						.updateComboItem((ClientShippingTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shippingMethodsCombo
						.updateComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.updateComboItem((ClientPriceLevel) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.removeComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				if (getPreferences().isSalesPersonEnabled())
					this.salesPersonCombo
							.removeComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.removeComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_TERM)
				this.shippingTermsCombo
						.removeComboItem((ClientShippingTerms) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shippingMethodsCombo
						.removeComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.removeComboItem((ClientPriceLevel) core);

			break;
		}

	}

	private void resetFromView() {
		custForm.getCellFormatter().setWidth(0, 1, "200");

		shipToAddress.getCellFormatter().setWidth(0, 1, "100");
		shipToAddress.getCellFormatter().setWidth(0, 2, "200");

		priceLevelSelect.setWidth("150px");
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
			customerTransactionGrid.setTaxCode(taxCode.getID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().invoice();
	}

	@Override
	public boolean canPrint() {

		return true;
	}

	@Override
	public boolean canExportToCsv() {

		return false;
	}
}
