package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

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
	AccounterConstants accounterConstants = Accounter.constants();
	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;

	public ItemReceiptView() {
		super(ClientTransaction.TYPE_ITEM_RECEIPT);
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

		if (this.isInViewMode()) {
			dateNoForm.setDisabled(true);
		}

		// formItems.add(transactionDateItem);
		// formItems.add(transactionNumber);

		vendorCombo = createVendorComboItem(messages.vendorName(Global.get()
				.Vendor()));
		vendorCombo.setWidth(100);
		purchaseLabel = new LinkItem();
		purchaseLabel.setLinkTitle(Accounter.constants().purchaseOrders());
		purchaseLabel.setShowTitle(false);
		purchaseLabel.setDisabled(isInViewMode());
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
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		phoneSelect.setDisabled(false);

		// formItems.add(phoneSelect);

		vendorForm = UIUtils.form(Global.get().vendor());
		vendorForm.setNumCols(3);
		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, purchaseLabel, contactCombo,
				emptylabel, phoneSelect, emptylabel, billToCombo, emptylabel);

		// formItems.add(contactCombo);
		// formItems.add(billToCombo);

		payTermsSelect = createPaymentTermsSelectItem();

		deliveryDateItem = createTransactionDeliveryDateItem();

		termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setFields(payTermsSelect, deliveryDateItem);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		netAmount = new AmountLabel("Net Amount");
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);
		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		HTML lab2 = new HTML("<strong>"
				+ Accounter.constants().itemsAndExpenses() + "</strong>");
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax() && isTrackPaidTax(), isTaxPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				ItemReceiptView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return ItemReceiptView.this.isShowPriceWithVat();
			}
		};

		vendorAccountTransactionTable.setDisabled(isInViewMode());

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
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");
		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				ItemReceiptView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return ItemReceiptView.this.isShowPriceWithVat();
			}
		};

		vendorItemTransactionTable.setDisabled(isInViewMode());

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
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");
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
		if (isTrackTax() && isTrackPaidTax()) {
			bottomLayout.add(memoForm);
			bottomLayout.add(vatCheckform);
			if (!isTaxPerDetailLine()) {
				taxCodeSelect = createTaxCodeSelectItem();
				DynamicForm form = new DynamicForm();
				form.setFields(taxCodeSelect);
				bottomLayout.add(form);
			}
			bottomLayout.setCellHorizontalAlignment(vatCheckform, ALIGN_RIGHT);
			bottomLayout.add(totalForm);
			bottomLayout.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);
		} else {
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
		// mainVLay.add(menuButton);
		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		mainVLay.add(bottomLayout);

		this.add(mainVLay);

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

		if (transaction == null)
			vendorAccountTransactionTable.resetRecords();
		selectedPurchaseOrders = new ArrayList<ClientPurchaseOrder>();

		vendorCombo.setComboItem(vendor);
		paymentTermsSelected(getCompany().getPaymentTerms(
				vendor.getPaymentTerms()));
		if (transaction == null)
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
		comboItem.setDisabled(isInViewMode());
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

	private void initPaymentTerms() {

		List<ClientPaymentTerms> paymentTermsList = Accounter.getCompany()
				.getPaymentsTerms();

		payTermsSelect.initCombo(paymentTermsList);

	}

	@Override
	protected void initMemoAndReference() {
		if (this.isInViewMode()) {

			ClientItemReceipt itemReceipt = (ClientItemReceipt) transaction;

			if (itemReceipt != null) {

				memoTextAreaItem.setValue(itemReceipt.getMemo());
				// refText.setValue(itemReceipt.getReference());

			}

		}

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientItemReceipt());
		} else {
			ClientCompany company = getCompany();
			this.setVendor(company.getVendor(transaction.getVendor()));
			this.contact = transaction.getContact();
			if (transaction.getPhone() != null)
				this.phoneNo = transaction.getPhone();
			phoneSelect.setValue(this.phoneNo);
			// this.ve = itemReceipt.getVendorAddress();
			this.billingAddress = transaction.getVendorAddress();
			this.paymentTerm = company.getPaymentTerms(transaction
					.getPaymentTerm());
			if (transaction.getDeliveryDate() != 0) {
				deliveryDateItem.setValue(new ClientFinanceDate(transaction
						.getDeliveryDate()));
			}
			this.deliveryDate = transaction.getDeliveryDate();
			this.transactionItems = transaction.getTransactionItems();

			vendorSelected(this.getVendor());
			paymentTermsSelected(this.paymentTerm);

			if (getPreferences().isTrackPaidTax()) {
				netAmount.setAmount(getAmountInTransactionCurrency(transaction.getNetAmount()));
				vatTotalNonEditableText.setAmount(getAmountInTransactionCurrency(transaction.getTotal()
						- transaction.getNetAmount()));
			}
			transactionTotalNonEditableText.setAmount(getAmountInTransactionCurrency(transaction.getTotal()));

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}
			if (transaction.getMemo() != null)
				memoTextAreaItem.setValue(transaction.getMemo());
			initAccounterClass();
		}
		super.initTransactionViewData();
		initPaymentTerms();
		initTransactionNumber();
	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		transactionTotalNonEditableText.setAmount(getAmountInTransactionCurrency(grandTotal));
		netAmount.setAmount(getAmountInTransactionCurrency(lineTotal));
		if (getPreferences().isTrackPaidTax()) {
			vatTotalNonEditableText.setAmount(getAmountInTransactionCurrency(grandTotal - lineTotal));
		}
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		super.saveAndUpdateView();

		saveOrUpdate(transaction);
	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Vendor
		transaction.setVendor(this.getVendor().getID());

		// Setting Contact
		if (contact != null)
			transaction.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress((billingAddress));

		// Setting Phone
		if (phoneNo != null)
			transaction.setPhone(phoneNo);

		if (paymentTerm != null)
			transaction.setPaymentTerm(paymentTerm.getID());
		if (deliveryDateItem != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getDate());

		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());

		if (vatinclusiveCheck != null)
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());

		transaction.setPurchaseOrder(selectedPurchaseOrder);

		if (getPreferences().isTrackPaidTax()) {
			transaction.setNetAmount(getAmountInBaseCurrency(netAmount.getAmount()));
		}
		// itemReceipt.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
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
					if (isTrackTax()) {
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
			vendorAccountTransactionTable
					.setAllRows(getAccountTransactionItems(itemsList));
			vendorItemTransactionTable
					.setAllRows(getItemTransactionItems(itemsList));
		}

	}

	private void getPurchaseOrders() {
		if (this.rpcUtilService == null)
			return;
		if (getVendor() == null) {
			Accounter.showError(messages.pleaseSelectVendor(Global.get()
					.vendor()));
		} else {
			this.rpcUtilService
					.getNotReceivedPurchaseOrdersList(
							getVendor().getID(),
							new AccounterAsyncCallback<ArrayList<PurchaseOrdersList>>() {

								public void onException(
										AccounterException caught) {
									// Accounter.showError(UIUtils.getVendorString(
									// "No Purchase Orders For Supplier",
									// "No Purchase Orders For Vendor")
									// + vendor.getName());
									return;

								}

								public void onResultSuccess(
										ArrayList<PurchaseOrdersList> result) {
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
	public void deleteFailed(AccounterException caught) {
		// NOTHING TO DO.
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// NOTHING TO DO.
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. isValidTransactionDate?
		// 2. isInPreventPostingBeforeDate?
		// 3. vendorForm validation?
		// 4. isValidDueOrDeliveryDate?
		// 5. isBlankTransaction?
		// 6. validateGrid?

		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// accounterConstants.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}

		result.add(vendorForm.validate());

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				deliveryDateItem.getEnteredDate(), this.transactionDate)) {

			result.addError(deliveryDateItem, Accounter.constants().the()
					+ " "
					+ Accounter.constants().deliveryDate()
					+ " "
					+ " "
					+ Accounter.constants()
							.cannotbeearlierthantransactiondate());

		}
		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable,
					accounterConstants.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}
		return result;
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
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
		vendorCombo.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		payTermsSelect.setDisabled(isInViewMode());
		purchaseLabel.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());

		deliveryDateItem.setDisabled(isInViewMode());
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
		return getAmountInBaseCurrency(this.transactionTotalItem.getAmount());
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().itemReciepts();
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else {
			taxCodeSelect.setValue("");
		}
	}

	@Override
	protected void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems) {
		vendorAccountTransactionTable
				.setRecords(getAccountTransactionItems(transactionItems));
		vendorItemTransactionTable
				.setRecords(getItemTransactionItems(transactionItems));
	}

	@Override
	protected void removeAllRecordsFromGrid() {
		vendorAccountTransactionTable.removeAllRecords();
		vendorItemTransactionTable.removeAllRecords();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);

	}

	@Override
	protected void refreshTransactionGrid() {
		// vendorTransactionTable.refreshAllRecords();
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
	}

}
