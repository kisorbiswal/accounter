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
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
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
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.EditMode;
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

		custForm = UIUtils.form(customerConstants.customer());
		custForm.setFields(customerCombo, contactCombo, phoneSelect,
				billToTextArea);
		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		custForm.getCellFormatter().setWidth(0, 0, "226px");
		custForm.setNumCols(2);
		custForm.setStyleName("align-form");
		custForm.setWidth("100%");
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
		customerTransactionGrid.setDisabled(isInViewMode());
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

			if (getPreferences().getDoYouPaySalesTax()) {
				prodAndServiceForm2.setFields(taxCodeSelect,
						salesTaxTextNonEditable, disabletextbox,
						transactionTotalNonEditableText);
			} else {
				prodAndServiceForm2.setFields(disabletextbox,
						transactionTotalNonEditableText);
			}
			prodAndServiceForm2.addStyleName("tax-form");
		}

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

		updateTransaction();

		saveOrUpdate(transaction);

	}

	protected void updateTransaction() {
		super.updateTransaction();
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

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
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
			shippingTermSelected(this.shippingTerm);
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
			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				netAmountLabel.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
			} else {
				this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
				if (taxCode != null) {
					this.taxCodeSelect
							.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
				}
				this.salesTaxTextNonEditable.setValue(String
						.valueOf(transaction.getSalesTax()));
			}
			memoTextAreaItem.setDisabled(true);
			transactionTotalNonEditableText.setAmount(transaction.getTotal());
			customerTransactionGrid.setCanEdit(false);
		}
		super.initTransactionViewData();
		initCashSalesView();
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

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				if (getPreferences().isSalesPersonEnabled())
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
				if (getPreferences().isSalesPersonEnabled())
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
				if (getPreferences().isSalesPersonEnabled())
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
					Accounter.showMessage(Global.get().constants()
							.sessionExpired());
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
		priceLevelSelect.setDisabled(isInViewMode());
		customerTransactionGrid.setDisabled(isInViewMode());
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
