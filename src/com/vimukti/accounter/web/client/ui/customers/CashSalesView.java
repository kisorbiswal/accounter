package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dev.util.Empty;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
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
		Label lab1 = new Label(FinanceApplication.getCustomersMessages()
				.newcashsale());
		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());
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
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(customerConstants
				.customerName());

		contactCombo = createContactComboItem();

		billToCombo = createBillToComboItem();
		phoneSelect = new SelectItem();
		phoneSelect.setTitle(customerConstants.phone());
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isEdit);
		phoneSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				phoneNo = phoneSelect.getValue().toString();
			}
		});
		shipToCombo = createShipToComboItem();
		custForm = UIUtils.form(customerConstants.customer());
		custForm.setFields(customerCombo, contactCombo, phoneSelect,
				billToCombo, shipToCombo);
		custForm.setNumCols(2);
		custForm.setStyleName("align-form");
		custForm.setWidth("100%");
		forms.add(custForm);

		salesPersonCombo = createSalesPersonComboItem();
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setWidth(100);
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
		termsForm.setFields(salesPersonCombo, paymentMethodCombo,
				depositInCombo, shippingTermsCombo, shippingMethodsCombo,
				deliveryDate);
		termsForm.setStyleName("align-form");
		forms.add(termsForm);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(160);

		taxCodeSelect = createTaxCodeSelectItem();

		priceLevelSelect = createPriceLevelSelectItem();

		refText = createRefereceText();
		refText.setWidth(160);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem, refText);
		forms.add(prodAndServiceForm1);

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();
		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();
		customerTransactionGrid = getGrid();
		customerTransactionGrid.setTransactionView(this);
		customerTransactionGrid.init();
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);

		DynamicForm prodAndServiceForm2 = new DynamicForm();
		prodAndServiceForm2.setWidth("70%");
		prodAndServiceForm2.setNumCols(4);
		if (FinanceApplication.getCompany().getAccountingType() == 1) {

//			prodAndServiceForm2.setFields(priceLevelSelect, netAmountLabel,
//					disabletextbox, vatTotalNonEditableText, disabletextbox,
//					transactionTotalNonEditableText);
			prodAndServiceForm2.setFields(disabletextbox, netAmountLabel,
					disabletextbox, vatTotalNonEditableText, disabletextbox,
					transactionTotalNonEditableText);
		} else {
//			prodAndServiceForm2.setFields(taxCodeSelect,
//					salesTaxTextNonEditable, priceLevelSelect,
//					transactionTotalNonEditableText);
			prodAndServiceForm2.setFields(taxCodeSelect,
					salesTaxTextNonEditable, disabletextbox,
					transactionTotalNonEditableText);
		}
		forms.add(prodAndServiceForm2);

		HorizontalPanel prodAndServiceHLay = new HorizontalPanel();
		prodAndServiceHLay.setWidth("100%");
		
		VerticalPanel vPanel =  new VerticalPanel();
		vPanel.add(createAddNewButton());
		vPanel.add(prodAndServiceForm1);
		vPanel.setWidth("50%");
		prodAndServiceHLay.add(vPanel);
		
		prodAndServiceHLay.add(prodAndServiceForm2);
		prodAndServiceHLay.setCellHorizontalAlignment(prodAndServiceForm2,
				ALIGN_RIGHT);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);

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
		topHLay.setCellWidth(rightVLay, "50%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");

		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(customerTransactionGrid);
		mainVLay.add(prodAndServiceHLay);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		canvas.add(mainVLay);

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
		if (this.customer != null && this.customer != customer) {
			ClientCashSales ent = (ClientCashSales) this.transactionObject;

			if (ent != null && ent.getCustomer().equals(customer.getStringID())) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.setRecords(ent
						.getTransactionItems());
			} else if (ent != null
					&& !ent.getCustomer().equals(customer.getStringID())) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.updateTotals();
			} else if (ent == null)
				this.customerTransactionGrid.removeAllRecords();
		}
		super.customerSelected(customer);
		this.customer = customer;
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

			salesPersonCombo.setComboItem(FinanceApplication.getCompany()
					.getSalesPerson(salesPerson.getStringID()));

		}

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		if (priceLevel != null && priceLevelSelect != null) {

			priceLevelSelect.setComboItem(FinanceApplication.getCompany()
					.getPriceLevel(priceLevel.getStringID()));

		}
		if (transactionObject == null && customerTransactionGrid != null) {
			customerTransactionGrid.priceLevelSelected(priceLevel);
			customerTransactionGrid.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	public void saveAndUpdateView() throws Exception {

		try {

			ClientCashSales cashSale = transactionObject != null ? (ClientCashSales) transactionObject
					: new ClientCashSales();

			cashSale.setCustomer(customer.getStringID());
			cashSale.setPaymentMethod(paymentMethod);
			if (depositInAccount != null)
				cashSale.setDepositIn(depositInAccount.getStringID());
			if (contact != null)
				cashSale.setContact(contact);
			if (phoneNo != null)
				cashSale.setPhone(phoneNo);
			if (salesPerson != null) {
				cashSale.setSalesPerson(salesPerson.getStringID());
			}
			if (billingAddress != null)
				cashSale.setBillingAddress(billingAddress);
			if (shippingAddress != null)
				cashSale.setShippingAdress(shippingAddress);
			if (shippingTerm != null)
				cashSale.setShippingTerm(shippingTerm.getStringID());
			if (shippingMethod != null)
				cashSale.setShippingMethod(shippingMethod.getStringID());
			if (deliveryDate != null && deliveryDate.getEnteredDate() != null)
				cashSale.setDeliverydate(deliveryDate.getEnteredDate()
						.getTime());

			if (priceLevel != null)
				cashSale.setPriceLevel(priceLevel.getStringID());
			cashSale.setMemo(getMemoTextAreaItem());
			cashSale.setReference(getRefText());

			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				cashSale.setNetAmount(netAmountLabel.getAmount());
				cashSale.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
						.getValue());
			} else {
				if (salesTax != null)
					cashSale.setSalesTax(salesTax);

			}

			cashSale.setTotal(transactionTotalNonEditableText.getAmount());

			transactionObject = cashSale;

			super.saveAndUpdateView();

			if (transactionObject.getStringID() == null)
				createObject(cashSale);
			else
				alterObject(cashSale);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void updateNonEditableItems() {

		if (customerTransactionGrid == null)
			return;
		if (FinanceApplication.getCompany().getAccountingType() == 0) {

			Double taxableLineTotal = customerTransactionGrid
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;
			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(), taxableLineTotal,
					FinanceApplication.getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionGrid.getTotal()
					+ this.salesTax);

		} else {
			netAmountLabel.setAmount(customerTransactionGrid.getGrandTotal());
			vatTotalNonEditableText.setAmount(customerTransactionGrid
					.getTotalValue()
					- customerTransactionGrid.getGrandTotal());
			setTransactionTotal(customerTransactionGrid.getTotalValue());
		}

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		initTransactionViewData();
		ClientCashSales cashSale = (ClientCashSales) transactionObject;
		ClientCompany company = FinanceApplication.getCompany();
		if (cashSale == null) {
			UIUtils.err(FinanceApplication.getCustomersMessages()
					.unableToLoadRequiredQuote());
			return;
		}

		this.transactionObject = cashSale;
		this.customer = company.getCustomer(cashSale.getCustomer());
		// customerSelected(FinanceApplication.getCompany().getCustomer(
		// cashSale.getCustomer()));

		if (this.customer != null) {

			this.contacts = customer.getContacts();
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
		if (customer != null) {
			customerCombo.setComboItem(customer);
		}
		billToaddressSelected(this.billingAddress);
		shipToAddressSelected(shippingAddress);
		contactSelected(this.contact);
		priceLevelSelected(this.priceLevel);
		salesPersonSelected(this.salesPerson);
		shippingMethodSelected(this.shippingMethod);
		shippingTermSelected(this.shippingTerm);
		depositInAccountSelected(this.depositInAccount);

		this.transactionItems = cashSale.getTransactionItems();
		paymentMethodCombo.setValue(cashSale.getPaymentMethod());

		if (cashSale.getDeliverydate() != 0)
			this.deliveryDate.setEnteredDate(new ClientFinanceDate(cashSale
					.getDeliverydate()));

		if (cashSale.getStringID() != null) {
			isEdit = Boolean.TRUE;
		}
		memoTextAreaItem.setValue(cashSale.getMemo());
		refText.setValue(cashSale.getReference());
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

		if (this.transactionObject != null) {

			ClientCashSales cashSales = (ClientCashSales) transactionObject;

			if (cashSales != null) {

				memoTextAreaItem
						.setValue(cashSales.getMemo() != null ? cashSales
								.getMemo() : "");
				refText.setValue(cashSales.getReference() != null ? cashSales
						.getReference() : "");

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transactionObject != null) {
			Double salesTaxAmout = ((ClientCashSales) transactionObject)
					.getSalesTax();
			setSalesTax(salesTaxAmout);

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {

		if (transactionObject != null) {
			this.transactionTotal = ((ClientCashSales) transactionObject)
					.getTotal();
			this.transactionTotalNonEditableText
					.setAmount(this.transactionTotal);

		}
	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		List<DynamicForm> forms = this.getForms();
		for (DynamicForm form : forms) {
			if (form != null) {
				form.validate();
			}
		}
		return super.validate();

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
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

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
				this.salesPersonCombo.addComboItem((ClientSalesPerson) core);

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
				this.salesPersonCombo.updateComboItem((ClientSalesPerson) core);

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
				this.salesPersonCombo.removeComboItem((ClientSalesPerson) core);

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
				Accounter.showError(((InvalidOperationException) (caught))
						.getDetailedMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.stringID,
				editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		customerCombo.setDisabled(isEdit);
		salesPersonCombo.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		depositInCombo.setDisabled(isEdit);
		taxCodeSelect.setDisabled(isEdit);

		shippingTermsCombo.setDisabled(isEdit);
		shippingMethodsCombo.setDisabled(isEdit);

		deliveryDate.setDisabled(isEdit);

		priceLevelSelect.setDisabled(isEdit);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setCanEdit(true);
		super.onEdit();
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	private void resetFormView() {
		custForm.getCellFormatter().setWidth(0, 1, "200px");
		custForm.setWidth("75%");
		priceLevelSelect.setWidth("150px");
		refText.setWidth("200px");
		memoTextAreaItem.setWidth("200px");
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect.setComboItem(FinanceApplication.getCompany()
					.getTAXCode(taxCode.getStringID()));
			customerTransactionGrid.setTaxCode(taxCode.getStringID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}
}
