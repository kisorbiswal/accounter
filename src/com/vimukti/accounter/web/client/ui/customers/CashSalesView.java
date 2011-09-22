package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
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
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
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
		AbstractCustomerTransactionView<ClientCashSales> {
	private ShippingTermsCombo shippingTermsCombo;
	// private PriceLevelCombo priceLevelSelect;
	private TAXCodeCombo taxCodeSelect;
	private SalesPersonCombo salesPersonCombo;
	private Double salesTax = 0.0D;
	protected DateField deliveryDate;
	private ArrayList<DynamicForm> listforms;
	private ShipToForm shipToAddress;
	private boolean locationTrackingEnabled;
	private CustomerAccountTransactionTable customerAccountTransactionTable;
	private CustomerItemTransactionTable customerItemTransactionTable;
	private ClientPriceLevel priceLevel;
	private ClientTAXCode taxCode;
	private ClientSalesPerson salesPerson;
	private AmountLabel transactionTotalNonEditableText, netAmountLabel,
			taxTotalNonEditableText;
	private Double transactionTotal = 0.0D;
	private AddNewButton accountTableButton, itemTableButton;

	public CashSalesView() {
		super(ClientTransaction.TYPE_CASH_SALES);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
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

		Label lab1 = new Label(Accounter.constants().newCashSale());
		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");
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
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		if (locationTrackingEnabled)
			dateNoForm.setFields(locationCombo);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(Accounter.messages()
				.customerName(Global.get().Customer()));

		contactCombo = createContactComboItem();

		phoneSelect = new TextItem(customerConstants.phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isInViewMode());

		billToTextArea = new TextAreaItem(Accounter.constants().billTo());
		billToTextArea.setDisabled(true);
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

		custForm = UIUtils.form(Global.get().customer());
		custForm.setFields(customerCombo, contactCombo, phoneSelect,
				billToTextArea);
		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		custForm.getCellFormatter().setWidth(0, 0, "226px");
		custForm.setNumCols(2);
		custForm.setStyleName("align-form");
		custForm.setWidth("100%");
		salesPersonCombo = createSalesPersonComboItem();
		paymentMethodCombo = createPaymentMethodSelectItem();
		depositInCombo = createDepositInComboItem();
		depositInCombo.setPopupWidth("500px");
		shippingTermsCombo = createShippingTermsCombo();
		shippingMethodsCombo = createShippingMethodCombo();
		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());

		DynamicForm termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setIsGroup(true);
		termsForm.setNumCols(2);

		if (getPreferences().isSalesPersonEnabled()) {
			termsForm.setFields(salesPersonCombo, paymentMethodCombo,
					depositInCombo);
			if (getPreferences().isDoProductShipMents()) {
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
			}
		} else {
			termsForm.setFields(paymentMethodCombo, depositInCombo);
			if (getPreferences().isDoProductShipMents()) {
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
			}
		}

		termsForm.setStyleName("align-form");
		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "203px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(160);

		taxCodeSelect = createTaxCodeSelectItem();

		// priceLevelSelect = createPriceLevelSelectItem();

		// refText = createRefereceText();
		// refText.setWidth(160);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		// prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");

		taxTotalNonEditableText = createVATTotalNonEditableLabel();

		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();
		customerAccountTransactionTable = new CustomerAccountTransactionTable() {

			@Override
			public void updateNonEditableItems() {
				CashSalesView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashSalesView.this.isShowPriceWithVat();
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
		DisclosurePanel accountsDisclosurePanel = new DisclosurePanel(
				"Itemize by Account");
		accountFlowPanel.add(customerAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");

		customerItemTransactionTable = new CustomerItemTransactionTable() {

			@Override
			public void updateNonEditableItems() {
				CashSalesView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashSalesView.this.isShowPriceWithVat();
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
		DisclosurePanel itemsDisclosurePanel = new DisclosurePanel(
				"Itemize by Product/Service");
		itemsFlowPanel.add(customerItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");

		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("100%");
		prodAndServiceForm2.setNumCols(4);
		if (getPreferences().isTrackTax()) {

			if (getPreferences().isTaxPerDetailLine()) {
				prodAndServiceForm2.setFields(disabletextbox, netAmountLabel,
						disabletextbox, taxTotalNonEditableText,
						disabletextbox, transactionTotalNonEditableText);
				prodAndServiceForm2.addStyleName("invoice-total");
			} else {
				prodAndServiceForm2.setFields(taxCodeSelect,
						taxTotalNonEditableText, disabletextbox,
						transactionTotalNonEditableText);
			}
		} else {
			prodAndServiceForm2.setFields(disabletextbox,
					transactionTotalNonEditableText);
		}
		prodAndServiceForm2.addStyleName("tax-form");

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);
		if (getCompany().getAccountingType() == 1) {
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "30%");
		} else
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "50%");

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
		rightVLay.setHorizontalAlignment(ALIGN_LEFT);
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(20);

		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "42%");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);

		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		// mainVLay.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
		mainVLay.add(vPanel);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

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

		// formItems.add(shippingTermsCombo);

		return shippingTermsCombo;
	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientCashSales ent = (ClientCashSales) this.transaction;

			if (ent != null && ent.getCustomer() == customer.getID()) {
				this.customerAccountTransactionTable.clear();
				this.customerAccountTransactionTable
						.setAllRows(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.customerItemTransactionTable
						.setAllRows(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getCustomer() != customer.getID()) {
				this.customerAccountTransactionTable.clear();
				this.customerAccountTransactionTable.updateTotals();
				this.customerItemTransactionTable.updateTotals();
			} else if (ent == null)
				this.customerAccountTransactionTable.clear();
		}
		super.customerSelected(customer);
		if (this.shippingTerm != null && shippingTermsCombo != null)
			shippingTermsCombo.setComboItem(this.shippingTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (this.taxCode != null
				&& taxCodeSelect != null
				&& taxCodeSelect.getValue() != ""
				&& !taxCodeSelect.getName().equalsIgnoreCase(
						Accounter.constants().none()))
			taxCodeSelect.setComboItem(this.taxCode);

		// if (this.priceLevel != null && priceLevelSelect != null)
		// priceLevelSelect.setComboItem(this.priceLevel);

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
		long taxCode = customer.getTAXCode();
		if (taxCode != 0) {
			customerAccountTransactionTable.setTaxCode(taxCode, false);
			customerItemTransactionTable.setTaxCode(taxCode, false);
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
		// if (transaction == null && customerAccountTransactionTable != null) {
		// customerAccountTransactionTable.priceLevelSelected(priceLevel);
		// customerAccountTransactionTable.updatePriceLevel();
		// }
		updateNonEditableItems();

	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();

		saveOrUpdate(transaction);

	}

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
		transaction.setTransactionDate(transactionDate.getDate());
		if (getCustomer() != null)
			transaction.setCustomer(getCustomer().getID());
		transaction.setPaymentMethod(paymentMethodCombo.getSelectedValue());
		if (depositInAccount != null)
			transaction.setDepositIn(depositInAccount.getID());
		if (contact != null)
			transaction.setContact(contact);
		// if (phoneNo != null)
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
		// transaction.setReference(getRefText());
		if (getCompany().getPreferences().isRegisteredForVAT()) {
			transaction.setNetAmount(netAmountLabel.getAmount());
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		} else {
			if (salesTax != null)
				transaction.setSalesTax(salesTax);

		}

		transaction.setTotal(transactionTotalNonEditableText.getAmount());
	}

	@Override
	public void updateNonEditableItems() {

		if (customerAccountTransactionTable == null
				|| customerItemTransactionTable == null)
			return;
		if (getCompany().getAccountingType() == 0) {

			double totalTax = customerAccountTransactionTable.getTotalTax()
					+ customerItemTransactionTable.getTotalTax();
			double total = customerAccountTransactionTable.getGrandTotal()
					+ customerItemTransactionTable.getGrandTotal();

			setSalesTax(totalTax);

			setTransactionTotal(total);

		} else {
			double lineTotal = customerAccountTransactionTable.getLineTotal()
					+ customerItemTransactionTable.getLineTotal();
			double grandTotal = customerAccountTransactionTable.getGrandTotal()
					+ customerItemTransactionTable.getGrandTotal();
			netAmountLabel.setAmount(lineTotal);
			taxTotalNonEditableText.setAmount(grandTotal - lineTotal);
			setTransactionTotal(grandTotal);
		}

	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		this.transactionTotal = transactionTotal;
		if (transactionTotalNonEditableText != null)
			transactionTotalNonEditableText.setAmount(transactionTotal);

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCashSales());
		} else {

			ClientCompany company = getCompany();

			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			// customerSelected(FinanceApplication.getCompany().getCustomer(
			// cashSale.getCustomer()));

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

			if (transaction.getDeliverydate() != 0)
				this.deliveryDate.setEnteredDate(new ClientFinanceDate(
						transaction.getDeliverydate()));

			if (transaction.getID() != 0) {
				setMode(EditMode.VIEW);
			}
			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(cashSale.getReference());
			if (getPreferences().isTrackTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					netAmountLabel.setAmount(transaction.getNetAmount());
					taxTotalNonEditableText.setAmount(transaction.getTotal()
							- transaction.getNetAmount());
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect
								.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
					}
					this.taxTotalNonEditableText.setValue(String
							.valueOf(transaction.getSalesTax()));
				}
			}
			memoTextAreaItem.setDisabled(true);
			transactionTotalNonEditableText.setAmount(transaction.getTotal());

			this.clientAccounterClass = transaction.getAccounterClass();
			if (getPreferences().isClassTrackingEnabled()
					&& getPreferences().isClassOnePerTransaction()
					&& this.clientAccounterClass != null
					&& classListCombo != null) {
				classListCombo.setComboItem(this.getClientAccounterClass());
			}
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		superinitTransactionViewData();
		initCashSalesView();
	}

	private void superinitTransactionViewData() {

		initTransactionNumber();

		initCustomers();

		ArrayList<ClientSalesPerson> salesPersons = getCompany()
				.getActiveSalesPersons();

		salesPersonCombo.initCombo(salesPersons);

		ArrayList<ClientPriceLevel> priceLevels = getCompany().getPriceLevels();

		// priceLevelSelect.initCombo(priceLevels);

		ArrayList<ClientTAXCode> taxCodes = getCompany().getTaxCodes();

		taxCodeSelect.initCombo(taxCodes);

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

			ClientCashSales cashSales = (ClientCashSales) transaction;

			if (cashSales != null) {

				memoTextAreaItem
						.setValue(cashSales.getMemo() != null ? cashSales
								.getMemo() : "");
				// refText.setValue(cashSales.getReference() != null ? cashSales
				// .getReference() : "");

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transaction != null) {
			Double salesTaxAmout = ((ClientCashSales) transaction)
					.getSalesTax();
			setSalesTax(salesTaxAmout);

		}

	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;

		if (taxTotalNonEditableText != null)
			taxTotalNonEditableText.setAmount(salesTax);

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {

		if (transaction != null) {
			this.transactionTotal = ((ClientCashSales) transaction).getTotal();
			this.transactionTotalNonEditableText
					.setAmount(this.transactionTotal);

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
		return result;

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

	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(Global.get().constants()
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
		depositInCombo.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());

		shippingTermsCombo.setDisabled(isInViewMode());
		shippingMethodsCombo.setDisabled(isInViewMode());

		deliveryDate.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		// priceLevelSelect.setDisabled(isInViewMode());
		customerAccountTransactionTable.setDisabled(isInViewMode());
		customerItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (shippingTermsCombo != null)
			shippingTermsCombo.setDisabled(isInViewMode());
		super.onEdit();
	}

	@Override
	public void print() {
	}

	@Override
	public void printPreview() {

	}

	private void resetFormView() {
		custForm.getCellFormatter().setWidth(0, 1, "200px");
		custForm.setWidth("75%");
		// priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");
		memoTextAreaItem.setWidth("200px");
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
		return Accounter.constants().cashSales();
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
		saveAndCloseButton.setTabIndex(11);
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
}
