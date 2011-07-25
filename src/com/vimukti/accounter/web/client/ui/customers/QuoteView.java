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
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class QuoteView extends AbstractCustomerTransactionView<ClientEstimate> {

	protected DateField quoteExpiryDate;
	private ClientEstimate estimate;
	private HorizontalPanel panel;

	private ArrayList<DynamicForm> listforms;

	public QuoteView() {
		super(ClientTransaction.TYPE_ESTIMATE, CUSTOMER_TRANSACTION_GRID);

	}

	@Override
	protected void initTransactionViewData() {
		super.initTransactionViewData();
		initAllItems();
	}

	private void initAllItems() {
		initPaymentTerms();
		if (this.estimate != null) {
			this.quoteExpiryDate.setValue(new ClientFinanceDate(this.estimate
					.getExpirationDate()));
			this.deliveryDate.setValue(new ClientFinanceDate(this.estimate
					.getDeliveryDate()));
		}

	}

	public QuoteView(ClientCustomer customer) {
		super(ClientTransaction.TYPE_ESTIMATE, CUSTOMER_TRANSACTION_GRID);
	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (this.customer != null && this.customer != customer) {
			ClientEstimate ent = (ClientEstimate) this.transactionObject;

			if (ent != null && ent.getCustomer() == (customer.getID())) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.setRecords(ent
						.getTransactionItems());
			} else if (ent != null
					&& !(ent.getCustomer() == (customer.getID()))) {
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

		this.customer = customer;
		if (customer != null) {
			customerCombo.setComboItem(customer);
		}
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			super.setCustomerTaxCodetoAccount();

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

		salesPersonCombo.setDisabled(isEdit);

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		try {

			ClientEstimate quote = transactionObject != null ? (ClientEstimate) transactionObject
					: new ClientEstimate();

			if (quoteExpiryDate.getEnteredDate() != null)
				quote.setExpirationDate(quoteExpiryDate.getEnteredDate()
						.getTime());
			if (customer != null)
				quote.setCustomer(customer);
			if (contact != null)
				quote.setContact(contact);
			if (phoneSelect.getValue() != null)
				quote.setPhone(phoneSelect.getValue().toString());

			if (deliveryDate.getEnteredDate() != null)
				quote.setDeliveryDate(deliveryDate.getEnteredDate().getTime());

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
				quote.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
						.getValue());
			} else
				quote.setSalesTax(this.salesTax);

			quote.setTotal(transactionTotalNonEditableText.getAmount());
			transactionObject = quote;

			super.saveAndUpdateView();

			if (transactionObject.getID() == 0)
				createObject((ClientEstimate) transactionObject);
			else
				alterObject((ClientEstimate) transactionObject);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(customerConstants.quote()));

		Label lab1 = new Label(Accounter.getCustomersMessages().newQuote());
		// + "(" + getTransactionStatus() + ")");
		lab1.setStyleName(Accounter.getCustomersMessages().lableTitle());
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
		datepanel.getElement().getStyle().setPaddingRight(15, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(Accounter
				.getCustomersMessages().customerName());
		contactCombo = createContactComboItem();
		billToTextArea = new TextAreaItem();
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(Accounter.getCustomersMessages().billTo());
		billToTextArea.setDisabled(true);

		phoneSelect = new TextItem(customerConstants.phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isEdit);

		custForm = UIUtils.form(customerConstants.customer());
		custForm.setCellSpacing(5);
		custForm.setWidth("100%");

		custForm.setFields(customerCombo, contactCombo, phoneSelect,
				billToTextArea);
		custForm.getCellFormatter().setWidth(0, 0, "150");
		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		custForm.setStyleName("align-form");
		forms.add(custForm);

		DynamicForm phoneForm = UIUtils.form(customerConstants.phoneNumber());
		phoneForm.setWidth("100%");
		phoneForm.setNumCols(2);
		phoneForm.setCellSpacing(3);
		salesPersonCombo = createSalesPersonComboItem();

		payTermsSelect = createPaymentTermsSelectItem();
		quoteExpiryDate = new DateField(customerConstants.expirationDate());
		quoteExpiryDate.setHelpInformation(true);
		quoteExpiryDate.setEnteredDate(getTransactionDate());
		formItems.add(quoteExpiryDate);
		quoteExpiryDate.setDisabled(isEdit);

		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());
		formItems.add(deliveryDate);

		phoneForm.setFields(salesPersonCombo, payTermsSelect, quoteExpiryDate,
				deliveryDate);
		phoneForm.setStyleName("align-form");
		phoneForm
				.getCellFormatter()
				.getElement(0, 0)
				.setAttribute(Accounter.getCustomersMessages().width(), "203px");
		forms.add(phoneForm);

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

		customerTransactionGrid = getGrid();
		customerTransactionGrid.setTransactionView(this);
		customerTransactionGrid.isEnable = false;
		customerTransactionGrid.init();
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);

		final TextItem disabletextbox = new TextItem();
		disabletextbox.setVisible(false);

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);
		forms.add(prodAndServiceForm1);

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
		forms.add(prodAndServiceForm2);

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

		menuButton.setType(AccounterButton.ADD_BUTTON);

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

		gridPanel.add(customerTransactionGrid);
		mainVLay.add(gridPanel);
		mainVLay.add(mainpanel);
		gridPanel.setWidth("100%");

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
			phoneForm.setWidth("60%");
		}

		canvas.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

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

		if (this.transactionObject != null) {

			ClientEstimate quote = (ClientEstimate) transactionObject;

			if (quote != null) {

				memoTextAreaItem.setValue(quote.getMemo());
				// refText.setValue(quote.getReference());

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transactionObject != null) {
			Double salesTaxAmout = ((ClientEstimate) transactionObject)
					.getSalesTax();
			if (salesTaxAmout != null) {
				salesTaxTextNonEditable.setAmount(salesTaxAmout);
			}

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transactionObject != null) {
			Double transactionTotal = ((ClientEstimate) transactionObject)
					.getTotal();
			if (transactionTotal != null) {
				transactionTotalNonEditableText.setAmount(transactionTotal);
			}

		}

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {

		initTransactionViewData();
		ClientEstimate estimate = (ClientEstimate) transactionObject;

		estimate = (ClientEstimate) transactionObject;

		ClientCompany company = getCompany();
		this.customer = company.getCustomer(estimate.getCustomer());
		this.transactionObject = estimate;
		if (this.customer != null) {

			this.contacts = customer.getContacts();
		}

		// customerSelected(FinanceApplication.getCompany().getCustomer(
		// estimate.getCustomer()));
		this.contact = estimate.getContact();

		this.phoneNo = estimate.getPhone();
		phoneSelect.setValue(this.phoneNo);
		this.billingAddress = estimate.getAddress();
		this.paymentTerm = company.getPaymentTerms(estimate.getPaymentTerm());
		this.priceLevel = company.getPriceLevel(estimate.getPriceLevel());
		this.salesPerson = company.getSalesPerson(estimate.getSalesPerson());
		initTransactionNumber();
		if (customer != null) {
			customerCombo.setComboItem(customer);
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
		this.transactionItems = estimate.getTransactionItems();

		if (estimate.getDeliveryDate() != 0)
			this.deliveryDate.setValue(new ClientFinanceDate(estimate
					.getDeliveryDate()));
		if (estimate.getExpirationDate() != 0)
			this.quoteExpiryDate.setValue(new ClientFinanceDate(estimate
					.getExpirationDate()));

		if (estimate.getID() != 0) {
			isEdit = Boolean.TRUE;
		}
		this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
		// taxCodeSelected(this.taxCode);
		if (taxCode != null) {
			this.taxCodeSelect
					.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
		}
		memoTextAreaItem.setValue(estimate.getMemo());
		// refText.setValue(estimate.getReference());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmountLabel.setAmount(estimate.getNetAmount());
			vatTotalNonEditableText.setValue(estimate.getTotal()
					- estimate.getNetAmount());
		}
		memoTextAreaItem.setDisabled(true);
		transactionTotalNonEditableText.setAmount(estimate.getTotal());
		customerTransactionGrid.setCanEdit(false);
	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		if (priceLevel != null && priceLevelSelect != null) {

			priceLevelSelect.setComboItem(getCompany().getPriceLevel(
					priceLevel.getID()));

		}
		if (transactionObject == null && customerTransactionGrid != null) {
			customerTransactionGrid.priceLevelSelected(this.priceLevel);
			customerTransactionGrid.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	public void updateNonEditableItems() {
		if (customerTransactionGrid == null)
			return;
		int accountType = getCompany().getAccountingType();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
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
			netAmountLabel.setAmount(customerTransactionGrid.getGrandTotal());

		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmountLabel.setAmount(customerTransactionGrid.getGrandTotal());
			vatTotalNonEditableText.setAmount(customerTransactionGrid
					.getTotalValue() - customerTransactionGrid.getGrandTotal());
			setTransactionTotal(customerTransactionGrid.getTotalValue());
		}

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		return super.validate();

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

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.addComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.addComboItem((ClientPriceLevel) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.updateComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.updateComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.updateComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.updateComboItem((ClientPriceLevel) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.removeComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.removeComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.removeComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.removeComboItem((ClientPriceLevel) core);

			break;
		}

	}

	@Override
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

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.id, editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		customerCombo.setDisabled(isEdit);
		salesPersonCombo.setDisabled(isEdit);
		payTermsSelect.setDisabled(isEdit);
		deliveryDate.setDisabled(isEdit);
		quoteExpiryDate.setDisabled(isEdit);
		taxCodeSelect.setDisabled(isEdit);
		memoTextAreaItem.setDisabled(isEdit);
		priceLevelSelect.setDisabled(isEdit);
		customerTransactionGrid.setCanEdit(true);
		customerTransactionGrid.setDisabled(isEdit);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub
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
		return Accounter.getCustomersMessages().quote();
	}

}
