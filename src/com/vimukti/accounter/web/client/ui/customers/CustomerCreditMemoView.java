package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
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
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.CustomerTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class CustomerCreditMemoView extends
		AbstractCustomerTransactionView<ClientCustomerCreditMemo> implements
		IPrintableView {

	private ArrayList<DynamicForm> listforms;
	private TextAreaItem billToTextArea;
	private boolean locationTrackingEnabled;
	private CustomerTransactionGrid customerTransactionGrid;

	public CustomerCreditMemoView() {

		super(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(Accounter.messages().customerCreditNote(
				Global.get().Customer()));
		lab1.setStyleName(Accounter.constants().labelTitle());
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
								Accounter.showError(Accounter.constants()
										.invalidTransactionDate());
								setTransactionDate(new ClientFinanceDate());
								transactionDateItem
										.setEnteredDate(getTransactionDate());
							}

						}
						updateNonEditableItems();
					}
				});

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(Accounter.constants().creditNo());
		locationCombo = createLocationCombo();
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

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(Accounter.messages()
				.customerName(Global.get().Customer()));

		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		// billToCombo = createBillToComboItem();
		billToTextArea = new TextAreaItem();
		billToTextArea.setHelpInformation(true);
		billToTextArea.setWidth(100);
		billToTextArea.setTitle(Accounter.constants().creditTo());
		billToTextArea.setDisabled(true);

		custForm = UIUtils.form(Global.get().customer());
		custForm.setFields(customerCombo, contactCombo, billToTextArea);
		custForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		custForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Global.get().constants().width(), "190px");
		custForm.setWidth("100%");
		custForm.setStyleName("align-form");

		phoneSelect = new TextItem(customerConstants.phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(isInViewMode());
		salesPersonCombo = createSalesPersonComboItem();

		DynamicForm phoneForm = UIUtils.form(customerConstants.phoneNumber());
		phoneForm.setWidth("100%");
		// phoneForm.setFields(phoneSelect, salesPersonCombo);
		// phoneForm.setFields(salesPersonCombo);
		phoneForm.setStyleName("align-form");

		Label lab2 = new Label(customerConstants.productAndService());

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setTitle(Accounter.constants().reasonForIssue());

		taxCodeSelect = createTaxCodeSelectItem();

		priceLevelSelect = createPriceLevelSelectItem();

		// refText = createRefereceText();

		DynamicForm prodAndServiceForm1 = new DynamicForm();
		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
				"memoFormAlign");
		prodAndServiceForm1.setNumCols(2);
		prodAndServiceForm1.setWidth("100%");
		prodAndServiceForm1.setFields(memoTextAreaItem);

		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();
		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();
		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();

		customerTransactionGrid = new CustomerTransactionGrid();
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

			prodAndServiceForm2.setFields(disabletextbox, netAmountLabel,
					disabletextbox, vatTotalNonEditableText, disabletextbox,
					transactionTotalNonEditableText);
			prodAndServiceForm2.addStyleName("invoice-total");
		} else {

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

		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setHorizontalAlignment(ALIGN_RIGHT);
		vpanel.setWidth("100%");
		vpanel.add(panel);

		vpanel.add(prodAndServiceForm2);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);
		if (getCompany().getAccountingType() == 1) {
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

		this.add(mainVLay);

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
	protected void salesPersonSelected(ClientSalesPerson person) {
		this.salesPerson = person;
		if (salesPerson != null && salesPersonCombo != null) {

			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

		}

	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(transaction);

	}

	protected void updateTransaction() {
		super.updateTransaction();
		if (customer != null)

			transaction.setCustomer(getCustomer().getID());
		if (contact != null)
			transaction.setContact(contact);
		if (salesPerson != null)
			transaction.setSalesPerson(salesPerson.getID());
		if (phoneNo != null)
			transaction.setPhone(phoneNo);
		if (billingAddress != null)
			transaction.setBillingAddress(billingAddress);
		if (priceLevel != null)
			transaction.setPriceLevel(priceLevel.getID());
		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			transaction.setNetAmount(netAmountLabel.getAmount());
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		} else
			transaction.setSalesTax(this.salesTax);

		transaction.setTotal(transactionTotalNonEditableText.getAmount());

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCustomerCreditMemo());
		} else {

			this.setCustomer(getCompany()
					.getCustomer(transaction.getCustomer()));
			this.billingAddress = transaction.getBillingAddress();
			this.contact = transaction.getContact();
			this.phoneNo = transaction.getPhone();
			phoneSelect.setValue(this.phoneNo);
			this.salesPerson = getCompany().getSalesPerson(
					transaction.getSalesPerson());
			this.priceLevel = getCompany().getPriceLevel(
					transaction.getPriceLevel());
			this.transactionItems = transaction.getTransactionItems();

			initTransactionNumber();
			if (getCustomer() != null)
				customerCombo.setComboItem(getCustomer());
			// billToaddressSelected(this.billingAddress);
			contactSelected(this.contact);
			priceLevelSelected(this.priceLevel);
			salesPersonSelected(this.salesPerson);
			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(creditToBeEdited.getReference());
			if (billingAddress != null) {
				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");

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
				salesTaxTextNonEditable.setAmount(transaction.getSalesTax());
			}
			transactionTotalNonEditableText.setAmount(transaction.getTotal());
			memoTextAreaItem.setDisabled(true);
			customerTransactionGrid.setCanEdit(false);
		}
		super.initTransactionViewData();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
	}

	@Override
	protected void initMemoAndReference() {
		if (this.transaction != null) {

			ClientCustomerCreditMemo creditMemo = (ClientCustomerCreditMemo) transaction;

			if (creditMemo.getMemo() != null) {

				memoTextAreaItem.setValue(creditMemo.getMemo());
				// if (creditMemo.getReference() != null)
				// refText.setValue(creditMemo.getReference());

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transaction != null) {
			Double salesTaxAmout = ((ClientCustomerCreditMemo) transaction)
					.getSalesTax();
			if (salesTaxAmout != null) {
				salesTaxTextNonEditable.setAmount(salesTaxAmout);
			}

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = ((ClientCustomerCreditMemo) transaction)
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

		// this.paymentsNonEditableText.setValue(transactionGrid.);

		// this.balanceDueNonEditableText.setValue(""+UIUtils.getCurrencySymbol()
		// +"0.00");

	}

	// @Override
	// public boolean validate() throws InvalidTransactionEntryException,
	// InvalidEntryException {
	// return super.validate();
	//
	// }

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientCustomerCreditMemo ent = (ClientCustomerCreditMemo) this.transaction;

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
		this.setCustomer(customer);
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
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	public AbstractTransactionGrid<ClientTransactionItem> getGridForPrinting() {
		return customerTransactionGrid;
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
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
		priceLevelSelect.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		customerTransactionGrid.setDisabled(isInViewMode());
		customerTransactionGrid.setCanEdit(true);
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		super.onEdit();

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
	}

	@Override
	public void print() {
		updateTransaction();
		ActionFactory.getBrandingThemeComboAction().run(transaction, false);
		// UIUtils.downloadAttachment(transaction.getID(),
		// ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);

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

			taxCodeSelect
					.setComboItem(getCompany().getTAXCode(taxCode.getID()));
			customerTransactionGrid.setTaxCode(taxCode.getID());
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().customerCreditNote(Global.get().Customer());
	}

	@Override
	public boolean canPrint() {

		return true;
	}

	@Override
	public boolean canExportToCsv() {

		return false;
	}

	@Override
	public AbstractTransactionGrid<ClientTransactionItem> getTransactionGrid() {
		return customerTransactionGrid;
	}
}
