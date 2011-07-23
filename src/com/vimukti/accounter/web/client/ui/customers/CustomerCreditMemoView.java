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
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class CustomerCreditMemoView extends
		AbstractCustomerTransactionView<ClientCustomerCreditMemo> {

	private ArrayList<DynamicForm> listforms;
	private TextAreaItem billToTextArea;

	public CustomerCreditMemoView() {
		super(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
				CUSTOMER_TRANSACTION_GRID);
	}

	@Override
	protected void initTransactionViewData() {

		super.initTransactionViewData();

	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(customerConstants.customerCreditNote());
		lab1.setStyleName(Accounter.getCustomersMessages()
				.lableTitle());
		// lab1.setHeight("35px");
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
								Accounter
										.showError("Invalid Transaction date!");
								setTransactionDate(new ClientFinanceDate());
								transactionDateItem
										.setEnteredDate(getTransactionDate());
							}

						}
						updateNonEditableItems();
					}
				});

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(Accounter.getCustomersMessages()
				.creditNo());

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

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(customerConstants
				.customerName());

		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		// billToCombo = createBillToComboItem();
		billToTextArea = new TextAreaItem();
		billToTextArea.setHelpInformation(true);
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(Accounter.getCustomersMessages()
				.creditTo());
		billToTextArea.setDisabled(true);

		custForm = UIUtils.form(customerConstants.customer());
		custForm.setFields(customerCombo, contactCombo, billToTextArea);
		custForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		custForm.getCellFormatter().getElement(0, 0).setAttribute(
				Accounter.getCustomersMessages().width(), "190px");
		custForm.setWidth("100%");
		custForm.setStyleName("align-form");
		forms.add(custForm);

		phoneSelect = new TextItem(customerConstants.phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isEdit);
		salesPersonCombo = createSalesPersonComboItem();

		DynamicForm phoneForm = UIUtils.form(customerConstants.phoneNumber());
		phoneForm.setWidth("100%");
		// phoneForm.setFields(phoneSelect, salesPersonCombo);
		// phoneForm.setFields(salesPersonCombo);
		phoneForm.setStyleName("align-form");
		forms.add(phoneForm);

		@SuppressWarnings("unused")
		Label lab2 = new Label(customerConstants.productAndService());

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setTitle(Accounter.getCustomersMessages()
				.reasonForIssue());

		taxCodeSelect = createTaxCodeSelectItem();

		priceLevelSelect = createPriceLevelSelectItem();

		// refText = createRefereceText();

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);
		forms.add(prodAndServiceForm1);

		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();
		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();
		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();

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
		if (Accounter.getCompany().getAccountingType() == 1) {

			prodAndServiceForm2.setFields(disabletextbox, netAmountLabel,
					disabletextbox, vatTotalNonEditableText, disabletextbox,
					transactionTotalNonEditableText);
			prodAndServiceForm2.addStyleName("invoice-total");
		} else {
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

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setHorizontalAlignment(ALIGN_RIGHT);
		vpanel.setWidth("100%");
		vpanel.add(panel);

		menuButton.setType(AccounterButton.ADD_BUTTON);

		vpanel.add(prodAndServiceForm2);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);
		if (Accounter.getCompany().getAccountingType() == 1) {
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "30%");
		} else
			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "50%");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.add(vpanel);
		mainPanel.add(prodAndServiceHLay);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(custForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(phoneForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setSize("100%", "100%");
		topHLay.setSpacing(20);

		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(customerTransactionGrid);
		mainVLay.add(mainPanel);

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
		} else {
			memoTextAreaItem.setWidth("400px");
			// refText.setWidth(130);
		}

		canvas.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		if (priceLevel != null && priceLevelSelect != null) {

			priceLevelSelect.setComboItem(Accounter.getCompany()
					.getPriceLevel(priceLevel.getID()));

		}

		if (transactionObject == null && customerTransactionGrid != null) {
			customerTransactionGrid.priceLevelSelected(priceLevel);
			customerTransactionGrid.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		this.salesPerson = person;
		if (salesPerson != null && salesPersonCombo != null) {

			salesPersonCombo.setComboItem(Accounter.getCompany()
					.getSalesPerson(salesPerson.getID()));

		}

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		try {
			transactionObject = getCreditMemoObject();
			super.saveAndUpdateView();
			if (transactionObject.getID() == null)
				createObject((ClientCustomerCreditMemo) transactionObject);
			else
				alterObject((ClientCustomerCreditMemo) transactionObject);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private ClientTransaction getCreditMemoObject() {
		try {

			ClientCustomerCreditMemo creditMemo = transactionObject != null ? (ClientCustomerCreditMemo) transactionObject
					: new ClientCustomerCreditMemo();

			creditMemo.setCustomer(customer.getID());
			if (contact != null)
				creditMemo.setContact(contact);
			if (salesPerson != null)
				creditMemo.setSalesPerson(salesPerson.getID());
			if (phoneNo != null)
				creditMemo.setPhone(phoneNo);
			if (billingAddress != null)
				creditMemo.setBillingAddress(billingAddress);
			if (priceLevel != null)
				creditMemo.setPriceLevel(priceLevel.getID());
			creditMemo.setMemo(getMemoTextAreaItem());
			// creditMemo.setReference(getRefText());

			if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
				creditMemo.setNetAmount(netAmountLabel.getAmount());
				creditMemo.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
						.getValue());
			} else
				creditMemo.setSalesTax(this.salesTax);

			creditMemo.setTotal(transactionTotalNonEditableText.getAmount());
			transactionObject = creditMemo;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionObject;
	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		initTransactionViewData();
		ClientCustomerCreditMemo creditToBeEdited = (ClientCustomerCreditMemo) transactionObject;

		this.customer = Accounter.getCompany().getCustomer(
				creditToBeEdited.getCustomer());
		this.transactionObject = creditToBeEdited;
		this.billingAddress = creditToBeEdited.getBillingAddress();
		this.contact = creditToBeEdited.getContact();
		this.phoneNo = creditToBeEdited.getPhone();
		phoneSelect.setValue(this.phoneNo);
		this.salesPerson = Accounter.getCompany().getSalesPerson(
				creditToBeEdited.getSalesPerson());
		this.priceLevel = Accounter.getCompany().getPriceLevel(
				creditToBeEdited.getPriceLevel());
		this.transactionItems = creditToBeEdited.getTransactionItems();

		initTransactionNumber();
		if (customer != null)
			customerCombo.setComboItem(customer);
		// billToaddressSelected(this.billingAddress);
		contactSelected(this.contact);
		priceLevelSelected(this.priceLevel);
		salesPersonSelected(this.salesPerson);
		memoTextAreaItem.setValue(creditToBeEdited.getMemo());
		// refText.setValue(creditToBeEdited.getReference());
		if (billingAddress != null) {
			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmountLabel.setAmount(creditToBeEdited.getNetAmount());
			vatTotalNonEditableText.setAmount(creditToBeEdited.getTotal()
					- creditToBeEdited.getNetAmount());
		} else {
			this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
			if (taxCode != null) {
				this.taxCodeSelect
						.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			}
			salesTaxTextNonEditable.setAmount(creditToBeEdited.getSalesTax());
		}
		transactionTotalNonEditableText.setAmount(creditToBeEdited.getTotal());
		memoTextAreaItem.setDisabled(true);
		customerTransactionGrid.setCanEdit(false);
	}

	@Override
	protected void initMemoAndReference() {
		if (this.transactionObject != null) {

			ClientCustomerCreditMemo creditMemo = (ClientCustomerCreditMemo) transactionObject;

			if (creditMemo != null) {

				memoTextAreaItem.setValue(creditMemo.getMemo());
				// if (creditMemo.getReference() != null)
				// refText.setValue(creditMemo.getReference());

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transactionObject != null) {
			Double salesTaxAmout = ((ClientCustomerCreditMemo) transactionObject)
					.getSalesTax();
			if (salesTaxAmout != null) {
				salesTaxTextNonEditable.setAmount(salesTaxAmout);
			}

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transactionObject != null) {
			Double transactionTotal = ((ClientCustomerCreditMemo) transactionObject)
					.getTotal();
			if (transactionTotal != null) {
				transactionTotalNonEditableText.setAmount(transactionTotal);
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
		if (customerTransactionGrid == null)
			return;
		if (Accounter.getCompany().getAccountingType() == 0) {
			Double taxableLineTotal = customerTransactionGrid
					.getTaxableLineTotal();

			if (taxableLineTotal == null)
				return;
			Double salesTax = taxCode != null ? Utility.getCalculatedSalesTax(
					transactionDateItem.getEnteredDate(), taxableLineTotal,
					Accounter.getCompany().getTAXItemGroup(
							taxCode.getTAXItemGrpForSales())) : 0;

			setSalesTax(salesTax);

			setTransactionTotal(customerTransactionGrid.getTotal()
					+ this.salesTax);

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
		} else {
			if (customerTransactionGrid.getGrandTotal() != null
					&& customerTransactionGrid.getTotalValue() != null) {
				netAmountLabel.setAmount(customerTransactionGrid
						.getGrandTotal());
				vatTotalNonEditableText.setAmount(customerTransactionGrid
						.getTotalValue()
						- customerTransactionGrid.getGrandTotal());
				setTransactionTotal(customerTransactionGrid.getTotalValue());
			}
		}

		// TODO this.paymentsNonEditableText.setValue(transactionGrid.);

		// this.balanceDueNonEditableText.setValue(""+UIUtils.getCurrencySymbol()
		// +"0.00");

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		return super.validate();

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (this.customer != null && this.customer != customer) {
			ClientCustomerCreditMemo ent = (ClientCustomerCreditMemo) this.transactionObject;

			if (ent != null && ent.getCustomer().equals(customer.getID())) {
				this.customerTransactionGrid.removeAllRecords();
				this.customerTransactionGrid.setRecords(ent
						.getTransactionItems());
			} else if (ent != null
					&& !ent.getCustomer().equals(customer.getID())) {
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
		this.addressListOfCustomer = customer.getAddress();
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");
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

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.addComboItem((ClientPriceLevel) core);
			break;

		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.updateComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.updateComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.updateComboItem((ClientPriceLevel) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.removeComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonCombo.removeComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.removeComboItem((ClientPriceLevel) core);

			break;
		}

	}

	public AbstractTransactionGrid<ClientTransactionItem> getGridForPrinting() {
		return customerTransactionGrid;
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
		priceLevelSelect.setDisabled(isEdit);
		taxCodeSelect.setDisabled(isEdit);
		memoTextAreaItem.setDisabled(isEdit);
		customerTransactionGrid.setDisabled(isEdit);
		customerTransactionGrid.setCanEdit(true);
		super.onEdit();

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {

		UIUtils.downloadAttachment(
				((ClientCustomerCreditMemo) getCreditMemoObject())
						.getID(),
				ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);

	}

	public void resetFormView() {
		custForm.getCellFormatter().setWidth(0, 1, "200px");
		custForm.setWidth("75%");
		// refText.setWidth("200px");
		priceLevelSelect.setWidth("150px");
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect.setComboItem(Accounter.getCompany()
					.getTAXCode(taxCode.getID()));
			customerTransactionGrid.setTaxCode(taxCode.getID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

	@Override
	protected String getViewTitle() {
		return Accounter.getCustomersMessages().customerCreditNote();
	}
}
