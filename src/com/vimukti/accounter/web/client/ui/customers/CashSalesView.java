package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * gg
 * 
 * @author Fernandez
 * 
 */
public class CashSalesView extends
		AbstractCustomerTransactionView<ClientCashSales> {

	// private CashSales cashSale;

	private ArrayList<DynamicForm> listforms;
	private ShipToForm shipToAddress;

	public CashSalesView() {

		super(ClientTransaction.TYPE_CASH_SALES, CUSTOMER_TRANSACTION_GRID);
	}

	public CashSalesView(ClientCustomer customer) {
		super(ClientTransaction.TYPE_CASH_SALES, CUSTOMER_TRANSACTION_GRID);
	}

	public CashSalesView(ClientCashSales cashSale) {
		super(ClientTransaction.TYPE_CASH_SALES, CUSTOMER_TRANSACTION_GRID);

	}

	private void initCashSalesView() {

		initShippingTerms();
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
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
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

		customerCombo = createCustomerComboItem(customerConstants
				.customerName());

		contactCombo = createContactComboItem();

		phoneSelect = new TextItem(customerConstants.phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isEdit);

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

		custForm = UIUtils.form(customerConstants.customer());
		custForm.setFields(customerCombo, contactCombo, phoneSelect,
				billToTextArea);
		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		custForm.getCellFormatter().setWidth(0, 0, "226px");
		custForm.setNumCols(2);
		custForm.setStyleName("align-form");
		custForm.setWidth("100%");
		forms.add(custForm);
		salesPersonCombo = createSalesPersonComboItem();
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setWidth("92%");
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

		if (ClientCompanyPreferences.get().isSalesPersonEnabled()) {
			termsForm.setFields(salesPersonCombo, paymentMethodCombo,
					depositInCombo);
			if (ClientCompanyPreferences.get().isDoProductShipMents()) {
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
			}
		} else {
			termsForm.setFields(paymentMethodCombo, depositInCombo);
			if (ClientCompanyPreferences.get().isDoProductShipMents()) {
				termsForm.setFields(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
			}
		}

		termsForm.setStyleName("align-form");
		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "203px");
		forms.add(termsForm);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(160);

		taxCodeSelect = createTaxCodeSelectItem();

		priceLevelSelect = createPriceLevelSelectItem();

		// refText = createRefereceText();
		// refText.setWidth(160);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		// prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		forms.add(prodAndServiceForm1);

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();
		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();
		customerTransactionGrid = getGrid();
		customerTransactionGrid.setTransactionView(this);
		customerTransactionGrid.isEnable = false;
		customerTransactionGrid.init();
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("100%");
		prodAndServiceForm2.setNumCols(4);
		if (getCompany().getAccountingType() == 1) {

			// prodAndServiceForm2.setFields(priceLevelSelect, netAmountLabel,
			// disabletextbox, vatTotalNonEditableText, disabletextbox,
			// transactionTotalNonEditableText);
			prodAndServiceForm2.setFields(disabletextbox, netAmountLabel,
					disabletextbox, vatTotalNonEditableText, disabletextbox,
					transactionTotalNonEditableText);
			prodAndServiceForm2.addStyleName("invoice-total");
		} else {
			// prodAndServiceForm2.setFields(taxCodeSelect,
			// salesTaxTextNonEditable, priceLevelSelect,
			// transactionTotalNonEditableText);
			prodAndServiceForm2.setFields(taxCodeSelect,
					salesTaxTextNonEditable, disabletextbox,
					transactionTotalNonEditableText);
			prodAndServiceForm2.addStyleName("tax-form");
		}
		forms.add(prodAndServiceForm2);

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(createAddNewButton());
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);
		if (getCompany().getAccountingType() == 1) {
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "30%");
		} else
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "50%");

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(ALIGN_RIGHT);
		vPanel.setWidth("100%");
		vPanel.add(panel);

		menuButton.setType(AccounterButton.ADD_BUTTON);

		vPanel.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);
		if (ClientCompanyPreferences.get().isDoProductShipMents())
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
		mainVLay.add(customerTransactionGrid);
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

	}

	@Override
	protected void initTransactionViewData() {
		super.initTransactionViewData();
		initCashSalesView();

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientCashSales ent = (ClientCashSales) this.transaction;

			if (ent != null && ent.getCustomer() == customer.getID()) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.setRecords(ent
						.getTransactionItems());
			} else if (ent != null && ent.getCustomer() != customer.getID()) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.updateTotals();
			} else if (ent == null)
				this.customerTransactionGrid.removeAllRecords();
		}
		super.customerSelected(customer);
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
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setCustomerTaxCodetoAccount();

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
		if (priceLevel != null && priceLevelSelect != null) {

			priceLevelSelect.setComboItem(getCompany().getPriceLevel(
					priceLevel.getID()));

		}
		if (transaction == null && customerTransactionGrid != null) {
			customerTransactionGrid.priceLevelSelected(priceLevel);
			customerTransactionGrid.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	public void saveAndUpdateView() {

		ClientCashSales cashSale = transaction != null ? (ClientCashSales) transaction
				: new ClientCashSales();

		cashSale.setCustomer(getCustomer().getID());
		cashSale.setPaymentMethod(paymentMethodCombo.getSelectedValue());
		if (depositInAccount != null)
			cashSale.setDepositIn(depositInAccount.getID());
		if (contact != null)
			cashSale.setContact(contact);
		// if (phoneNo != null)
		cashSale.setPhone(phoneSelect.getValue().toString());
		if (salesPerson != null) {
			cashSale.setSalesPerson(salesPerson.getID());
		}
		if (billingAddress != null)
			cashSale.setBillingAddress(billingAddress);
		if (shippingAddress != null)
			cashSale.setShippingAdress(shippingAddress);
		if (shippingTerm != null)
			cashSale.setShippingTerm(shippingTerm.getID());
		if (shippingMethod != null)
			cashSale.setShippingMethod(shippingMethod.getID());
		if (deliveryDate != null && deliveryDate.getEnteredDate() != null)
			cashSale.setDeliverydate(deliveryDate.getEnteredDate().getDate());

		if (priceLevel != null)
			cashSale.setPriceLevel(priceLevel.getID());
		cashSale.setMemo(getMemoTextAreaItem());
		// cashSale.setReference(getRefText());

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			cashSale.setNetAmount(netAmountLabel.getAmount());
			cashSale.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		} else {
			if (salesTax != null)
				cashSale.setSalesTax(salesTax);

		}

		cashSale.setTotal(transactionTotalNonEditableText.getAmount());

		transaction = cashSale;

		super.saveAndUpdateView();

		saveOrUpdate(cashSale);

	}

	@Override
	public void updateNonEditableItems() {

		if (customerTransactionGrid == null)
			return;
		if (getCompany().getAccountingType() == 0) {

			Double taxableLineTotal = customerTransactionGrid
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;
			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(),
					taxableLineTotal,
					getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionGrid.getTotal()
					+ this.salesTax);

		} else {
			netAmountLabel.setAmount(customerTransactionGrid.getGrandTotal());
			vatTotalNonEditableText.setAmount(customerTransactionGrid
					.getTotalValue() - customerTransactionGrid.getGrandTotal());
			setTransactionTotal(customerTransactionGrid.getTotalValue());
		}

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		initTransactionViewData();
		ClientCashSales cashSale = (ClientCashSales) transactionObject;
		ClientCompany company = getCompany();
		if (cashSale == null) {
			UIUtils.err(Accounter.constants().unableToLoadRequiredQuote());
			return;
		}

		this.transaction = cashSale;
		this.setCustomer(company.getCustomer(cashSale.getCustomer()));
		// customerSelected(FinanceApplication.getCompany().getCustomer(
		// cashSale.getCustomer()));

		if (this.getCustomer() != null) {

			this.contacts = getCustomer().getContacts();
		}
		this.contact = cashSale.getContact();
		this.phoneNo = cashSale.getPhone();
		phoneSelect.setValue(this.phoneNo);

		this.billingAddress = cashSale.getBillingAddress();
		this.shippingAddress = cashSale.getShippingAdress();

		this.priceLevel = company.getPriceLevel(cashSale.getPriceLevel());

		this.salesPerson = company.getSalesPerson(cashSale.getSalesPerson());
		this.shippingTerm = company
				.getShippingTerms(cashSale.getShippingTerm());
		this.shippingMethod = company.getShippingMethod(cashSale
				.getShippingMethod());
		this.depositInAccount = company.getAccount(cashSale.getDepositIn());

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
		shippingTermSelected(this.shippingTerm);
		depositInAccountSelected(this.depositInAccount);

		this.transactionItems = cashSale.getTransactionItems();
		paymentMethodCombo.setComboItem(cashSale.getPaymentMethod());

		if (cashSale.getDeliverydate() != 0)
			this.deliveryDate.setEnteredDate(new ClientFinanceDate(cashSale
					.getDeliverydate()));

		if (cashSale.getID() != 0) {
			isEdit = Boolean.TRUE;
		}
		memoTextAreaItem.setValue(cashSale.getMemo());
		// refText.setValue(cashSale.getReference());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmountLabel.setAmount(cashSale.getNetAmount());
			vatTotalNonEditableText.setAmount(cashSale.getTotal()
					- cashSale.getNetAmount());
		} else {
			this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
			if (taxCode != null) {
				this.taxCodeSelect
						.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			}
			this.salesTaxTextNonEditable.setValue(cashSale.getSalesTax());
		}
		memoTextAreaItem.setDisabled(true);
		transactionTotalNonEditableText.setAmount(cashSale.getTotal());
		customerTransactionGrid.setCanEdit(false);
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

	@Override
	protected void initTransactionTotalNonEditableItem() {

		if (transaction != null) {
			this.transactionTotal = ((ClientCashSales) transaction)
					.getTotal();
			this.transactionTotalNonEditableText
					.setAmount(this.transactionTotal);

		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(super.validate());
		result.add(FormItem.validate(this.paymentMethodCombo,
				this.depositInCombo));
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
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				if (ClientCompanyPreferences.get().isSalesPersonEnabled())
					this.salesPersonCombo
							.addComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depositInCombo.addComboItem((ClientAccount) core);

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
				if (ClientCompanyPreferences.get().isSalesPersonEnabled())
					this.salesPersonCombo
							.updateComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depositInCombo.updateComboItem((ClientAccount) core);

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
				if (ClientCompanyPreferences.get().isSalesPersonEnabled())
					this.salesPersonCombo
							.removeComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depositInCombo.removeComboItem((ClientAccount) core);

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

	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(((InvalidOperationException) (caught))
							.getDetailedMessage());
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
		isEdit = false;
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		customerCombo.setDisabled(isEdit);
		if (ClientCompanyPreferences.get().isSalesPersonEnabled())
			salesPersonCombo.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		depositInCombo.setDisabled(isEdit);
		taxCodeSelect.setDisabled(isEdit);

		shippingTermsCombo.setDisabled(isEdit);
		shippingMethodsCombo.setDisabled(isEdit);

		deliveryDate.setDisabled(isEdit);
		memoTextAreaItem.setDisabled(isEdit);
		priceLevelSelect.setDisabled(isEdit);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setCanEdit(true);
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
		priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");
		memoTextAreaItem.setWidth("200px");
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect
					.setComboItem(getCompany().getTAXCode(taxCode.getID()));
			customerTransactionGrid.setTaxCode(taxCode.getID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().cashSales();
	}
}
