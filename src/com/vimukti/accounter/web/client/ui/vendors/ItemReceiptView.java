package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class ItemReceiptView extends
		AbstractVendorTransactionView<ClientItemReceipt> {

	private DynamicForm vendorForm;
	private DynamicForm termsForm;
	private PaymentTermsCombo payTermsSelect;
	private ClientPaymentTerms paymentTerm;
	private AmountField transactionTotalItem;
	private LinkItem purchaseLabel;
	private VendorPurchaseListDialog dialog;

	private long deliveryDate;
	private long selectedPurchaseOrder;

	private ArrayList<DynamicForm> listforms;
	private ArrayList<ClientPurchaseOrder> selectedPurchaseOrders;

	public ItemReceiptView() {
		super(ClientTransaction.TYPE_ITEM_RECEIPT, VENDOR_TRANSACTION_GRID);
	}

	@Override
	protected void createControls() {
		// setTitle(UIUtils.title(vendorConstants.cashPurchase()));

		HTML lab1 = new HTML(Accounter.constants().itemReceipt());

		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem.setWidth(100);

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setWidth(100);

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(lab1);
		labeldateNoLayout.setHorizontalAlignment(ALIGN_RIGHT);
		labeldateNoLayout.setCellHorizontalAlignment(datepanel, ALIGN_RIGHT);
		labeldateNoLayout.add(datepanel);

		if (this.transactionObject != null)

			dateNoForm.setDisabled(true);

		forms.add(dateNoForm);
		formItems.add(transactionDateItem);
		formItems.add(transactionNumber);

		vendorCombo = createVendorComboItem(UIUtils
				.getVendorString(Accounter.constants().supplierName(),
						Accounter.constants().vendorName()));
		vendorCombo.setWidth(100);
		purchaseLabel = new LinkItem();
		purchaseLabel.setLinkTitle(Accounter.constants().purchaseOrders());
		purchaseLabel.setShowTitle(false);
		purchaseLabel.setDisabled(isEdit);
		purchaseLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getPurchaseOrders();
			}
		});

		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		contactCombo = createContactComboItem();
		contactCombo.setWidth(100);
		billToCombo = createBillToComboItem();
		billToCombo.setWidth(100);
		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(false);

		formItems.add(phoneSelect);

		vendorForm = UIUtils.form(Accounter.constants().supplier());
		vendorForm.setNumCols(3);
		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, purchaseLabel, contactCombo,
				emptylabel, phoneSelect, emptylabel, billToCombo, emptylabel);

		forms.add(vendorForm);
		formItems.add(contactCombo);
		formItems.add(billToCombo);

		payTermsSelect = createPaymentTermsSelectItem();

		deliveryDateItem = createTransactionDeliveryDateItem();

		termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setFields(payTermsSelect, deliveryDateItem);

		netAmount = new AmountLabel("Net Amount");
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);
		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		HTML lab2 = new HTML("<strong>"
				+ Accounter.constants().itemsAndExpenses() + "</strong>");
		menuButton = createAddNewButton();
		vendorTransactionGrid = getGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.isEnable = false;
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isEdit);

		vatinclusiveCheck = getVATInclusiveCheckBox();

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);
		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setWidth("80%");
		totalForm.setNumCols(2);
		totalForm.setWidth("80%");
		totalForm.setFields(netAmount, vatTotalNonEditableText,
				transactionTotalNonEditableText);
		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		forms.add(memoForm);

		transactionTotalItem = new AmountField(Accounter.constants().total(),
				this);
		transactionTotalItem.setDisabled(true);
		DynamicForm amountForm = new DynamicForm();
		amountForm.setFields(transactionTotalItem);

		// transactionTotalNonEditableText =
		// createTransactionTotalNonEditableItem();

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);
		rightVLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");
		int accountType = getCompany().getAccountingType();
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			bottomLayout.add(memoForm);
			bottomLayout.add(vatCheckform);
			bottomLayout.setCellHorizontalAlignment(vatCheckform, ALIGN_RIGHT);
			bottomLayout.add(totalForm);
			bottomLayout.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);
		} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
			memoForm.setStyleName("align-form");
			bottomLayout.add(memoForm);
			// bottomLayout.add(amountForm);
			// bottomLayout.setCellHorizontalAlignment(amountForm, ALIGN_RIGHT);
		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.setSpacing(2);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(lab2);
		mainVLay.add(menuButton);
		mainVLay.add(vendorTransactionGrid);
		mainVLay.add(bottomLayout);

		this.add(mainVLay);

		menuButton.setType(AccounterButton.ADD_BUTTON);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);
		listforms.add(memoForm);
		listforms.add(amountForm);

	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		super.vendorSelected(vendor);

		if (transactionObject == null)
			vendorTransactionGrid.removeAllRecords();
		selectedPurchaseOrders = new ArrayList<ClientPurchaseOrder>();

		vendorCombo.setComboItem(vendor);
		paymentTermsSelected(getCompany().getPaymentTerms(
				vendor.getPaymentTerms()));
		if (transactionObject == null)
			getPurchaseOrders();
	}

	private PaymentTermsCombo createPaymentTermsSelectItem() {

		PaymentTermsCombo comboItem = new PaymentTermsCombo(Accounter
				.constants().paymentTerms());

		comboItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {

						paymentTermsSelected(selectItem);

					}

				});
		comboItem.setDisabled(isEdit);
		// comboItem.setShowDisabled(false);
		//
		return comboItem;
	}

	private void paymentTermsSelected(ClientPaymentTerms paymentTerm) {
		this.paymentTerm = paymentTerm;
		if (this.paymentTerm != null && payTermsSelect != null) {

			payTermsSelect.setComboItem(paymentTerm);
		}
		// Date transDate = this.transactionDateItem.getEnteredDate();
		//
		// if (transDate != null && paymentTerm != null) {
		// Date dueDate = Utility.getCalculatedDueDate(transDate, paymentTerm);
		// if (dueDate != null) {
		// dueDateItem.setValue(dueDate);
		// }
		// }

	}

	@Override
	protected void initTransactionViewData() {
		super.initTransactionViewData();
		initTransactionNumber();
		initPaymentTerms();
		// initPayFromAccounts();
	}

	private void initPaymentTerms() {

		List<ClientPaymentTerms> paymentTermsList = Accounter.getCompany()
				.getPaymentsTerms();

		payTermsSelect.initCombo(paymentTermsList);

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transactionObject != null) {

			ClientItemReceipt itemReceipt = (ClientItemReceipt) transactionObject;

			if (itemReceipt != null) {

				memoTextAreaItem.setValue(itemReceipt.getMemo());
				// refText.setValue(itemReceipt.getReference());

			}

		}

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		ClientItemReceipt itemReceipt = (ClientItemReceipt) transactionObject;
		ClientCompany company = getCompany();
		this.setVendor(company.getVendor(itemReceipt.getVendor()));
		this.contact = itemReceipt.getContact();
		if (itemReceipt.getPhone() != null)
			this.phoneNo = itemReceipt.getPhone();
		phoneSelect.setValue(this.phoneNo);
		// this.ve = itemReceipt.getVendorAddress();
		this.billingAddress = itemReceipt.getVendorAddress();
		this.paymentTerm = company
				.getPaymentTerms(itemReceipt.getPaymentTerm());
		if (itemReceipt.getDeliveryDate() != 0) {
			deliveryDateItem.setValue(new ClientFinanceDate(itemReceipt
					.getDeliveryDate()));
		}
		this.deliveryDate = itemReceipt.getDeliveryDate();
		this.transactionItems = itemReceipt.getTransactionItems();

		initTransactionNumber();
		vendorSelected(this.getVendor());
		paymentTermsSelected(this.paymentTerm);

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmount.setAmount(itemReceipt.getNetAmount());
			vatTotalNonEditableText.setAmount(itemReceipt.getTotal()
					- itemReceipt.getNetAmount());
		}
		transactionTotalNonEditableText.setAmount(itemReceipt.getTotal());

		if (vatinclusiveCheck != null) {
			setAmountIncludeChkValue(itemReceipt.isAmountsIncludeVAT());
		}
		if (itemReceipt.getMemo() != null)
			memoTextAreaItem.setValue(itemReceipt.getMemo());
		// if (itemReceipt.getReference() != null)
		// refText.setValue(itemReceipt.getReference());
	}

	@Override
	public void updateNonEditableItems() {
		transactionTotalNonEditableText.setAmount(vendorTransactionGrid
				.getTotal());
		netAmount.setAmount(vendorTransactionGrid.getGrandTotal());
		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			vatTotalNonEditableText.setAmount(vendorTransactionGrid.getTotal()
					- vendorTransactionGrid.getGrandTotal());
		}
	}

	@Override
	public void saveAndUpdateView() {

		ClientItemReceipt itemReceipt = transactionObject != null ? (ClientItemReceipt) transactionObject
				: new ClientItemReceipt();

		// Setting Vendor
		itemReceipt.setVendor(this.getVendor().getID());

		// Setting Contact
		if (contact != null)
			itemReceipt.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			itemReceipt.setVendorAddress((billingAddress));

		// Setting Phone
		if (phoneNo != null)
			itemReceipt.setPhone(phoneNo);

		if (paymentTerm != null)
			itemReceipt.setPaymentTerm(paymentTerm.getID());
		if (deliveryDateItem != null)
			itemReceipt.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getDate());

		itemReceipt.setMemo(getMemoTextAreaItem());
		// itemReceipt.setReference(getRefText());
		itemReceipt.setTotal(vendorTransactionGrid.getTotal());

		if (vatinclusiveCheck != null)
			itemReceipt.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());

		itemReceipt.setPurchaseOrder(selectedPurchaseOrder);

		transactionObject = itemReceipt;

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			itemReceipt.setNetAmount(netAmount.getAmount());
		// itemReceipt.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		super.saveAndUpdateView();

		if (transactionObject.getID() != 0) {
			alterObject(itemReceipt);

		} else {
			createObject(itemReceipt);
		}
	}

	public void selectedPurchaseOrder(ClientPurchaseOrder purchaseOrder) {

		if (purchaseOrder != null) {
			selectedPurchaseOrders.add(purchaseOrder);
			List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
			for (ClientTransactionItem item : purchaseOrder
					.getTransactionItems()) {
				ClientTransactionItem clientItem = new ClientTransactionItem();
				if (item.getLineTotal() != 0.0) {
					clientItem.setType(item.getType());
					clientItem.setItem(item.getItem());
					clientItem.setAccount(item.getAccount());
					if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
						clientItem.setVatItem(item.getVatItem());
						clientItem.setTaxCode(item.getTaxCode());
						clientItem.setTaxable(item.isTaxable());
					}

					clientItem.setQuantity(item.getQuantity());
					clientItem.setUnitPrice(item.getUnitPrice());
					clientItem.setDiscount(item.getDiscount());
					clientItem.setLineTotal(item.getLineTotal()
							- item.getInvoiced());

					clientItem.setReferringTransactionItem(item.getID());

					itemsList.add(clientItem);
				}

			}

			selectedPurchaseOrder = purchaseOrder.getID();
			vendorTransactionGrid.isItemRecieptView = true;
			vendorTransactionGrid.setAllTransactions(itemsList);
		}

	}

	private void getPurchaseOrders() {
		if (this.rpcUtilService == null)
			return;
		if (getVendor() == null) {
			Accounter.showError(UIUtils.getVendorString(Accounter.constants()
					.pleaseSelectSupplier(), Accounter.constants()
					.pleaseSelectVendor()));
		} else {
			this.rpcUtilService.getNotReceivedPurchaseOrdersList(
					getVendor().getID(),
					new AccounterAsyncCallback<List<PurchaseOrdersList>>() {

						public void onException(AccounterException caught) {
							// Accounter.showError(UIUtils.getVendorString(
							// "No Purchase Orders For Supplier",
							// "No Purchase Orders For Vendor")
							// + vendor.getName());
							return;

						}

						public void onSuccess(List<PurchaseOrdersList> result) {
							if (result == null)
								onFailure(new Exception());

							if (result.size() > 0) {
								showPurchasesDialog(result);
							} else {
								onFailure(new Exception());
							}

						}

					});

		}
	}

	protected void showPurchasesDialog(List<PurchaseOrdersList> result) {
		if (result == null)
			return;

		List<PurchaseOrdersList> filteredList = new ArrayList<PurchaseOrdersList>();
		filteredList.addAll(result);

		for (PurchaseOrdersList record : result) {
			for (ClientPurchaseOrder purchaseOrder : selectedPurchaseOrders) {
				if (purchaseOrder.getID() == record.getTransactionId())
					filteredList.remove(record);
			}
		}

		if (filteredList.size() > 0) {
			if (dialog != null) {
				dialog.setPurchaseOrderList(filteredList);
			} else
				dialog = new VendorPurchaseListDialog(this, filteredList);

			dialog.show();
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
		this.vendorCombo.setFocus();
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// NOTHING TO DO.
	}

	@Override
	public void deleteSuccess(Boolean result) {
		// NOTHING TO DO.
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (!AccounterValidator.validateTransactionDate(transactionDate)) {
			result.addError(transactionDate,
					AccounterErrorType.InvalidTransactionDate);
		} else if (AccounterValidator
				.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, AccounterErrorType.InvalidDate);
		}
		result.add(vendorForm.validate());
		if (!AccounterValidator.validate_dueOrDelivaryDates(
				deliveryDateItem.getEnteredDate(), this.transactionDate,
				Accounter.constants().deliveryDate())) {

			result.addError(deliveryDateItem, Accounter.constants().the()
					+ " "
					+ Accounter.constants().deliveryDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());

		}
		if (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
			result.addError(vendorTransactionGrid,
					AccounterErrorType.blankTransaction);
		}
		result.add(vendorTransactionGrid.validateGrid());
		return result;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.addComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				payTermsSelect.addComboItem((ClientPaymentTerms) core);
			break;
		case AccounterCommand.UPDATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.updateComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				payTermsSelect.updateComboItem((ClientPaymentTerms) core);
			break;
		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.removeComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				payTermsSelect.removeComboItem((ClientPaymentTerms) core);
			break;
		}
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
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
		vendorCombo.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		payTermsSelect.setDisabled(isEdit);
		purchaseLabel.setDisabled(isEdit);
		vendorTransactionGrid.setDisabled(isEdit);

		deliveryDateItem.setDisabled(isEdit);
		super.onEdit();
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected Double getTransactionTotal() {
		return this.transactionTotalItem.getAmount();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().itemReciepts();
	}

}
