package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.TransactionsTree;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * modified by Ravi Kiran.G, Murali.A
 * 
 */
public class VendorBillView extends
		AbstractVendorTransactionView<ClientEnterBill> {
	private PaymentTermsCombo paymentTermsCombo;
	private ClientPaymentTerms selectedPaymentTerm;
	private DateField dueDateItem;

	private AmountLabel netAmount;

	private DynamicForm vendorForm;
	private Double balanceDue = 0.0;

	private ArrayList<DynamicForm> listforms;
	protected VendorAccountTransactionTable vendorAccountTransactionTable;
	protected VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private final VerticalPanel totalForm = new VerticalPanel();
	private DisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;

	// private WarehouseAllocationTable inventoryTransactionTable;
	// private DisclosurePanel inventoryDisclosurePanel;

	TransactionsTree<PurchaseOrdersAndItemReceiptsList> transactionsTree;

	private VendorBillView() {
		super(ClientTransaction.TYPE_ENTER_BILL);
	}

	private void resetGlobalVariables() {

		this.setVendor(null);
		this.billingAddress = null;
		this.contact = null;
		this.phoneNo = null;
		this.addressListOfVendor = null;
		this.contacts = null;
		List<ClientContact> list = new ArrayList<ClientContact>();
		list.addAll(contacts);
		contactCombo.initCombo(list);
		List<ClientAddress> adrsList = new ArrayList<ClientAddress>();
		adrsList.addAll(addressListOfVendor);
		billToCombo.initCombo(adrsList);
		contactCombo.setDisabled(isInViewMode());
		billToCombo.setDisabled(isInViewMode());
		// phoneSelect.setValueMap();
		setMemoTextAreaItem("");
		// setRefText("");
	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientEnterBill());
		} else {
			if (currencyWidget != null) {
				if (transaction.getCurrency() > 1) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				if (this.currency != null) {
					currencyWidget.setSelectedCurrency(this.currency);
				}
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}
			if (!getAccountTransactionItems(transaction.getTransactionItems())
					.isEmpty()) {
				this.vendorAccountTransactionTable
						.setRecords(getAccountTransactionItems(transaction
								.getTransactionItems()));
			}
			this.vendorItemTransactionTable
					.setRecords(getItemTransactionItems(transaction
							.getTransactionItems()));

			paymentTermsCombo.setValue(transaction.getPaymentTerm());
			dueDateItem
					.setValue(new ClientFinanceDate(transaction.getDueDate()));
			deliveryDateItem.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));

			ClientVendor vendor = getCompany().getVendor(
					transaction.getVendor());
			vendorCombo.setValue(vendor);
			this.vendor = vendor;
			billToaddressSelected(transaction.getVendorAddress());
			selectedVendor(vendor);
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			phoneSelect.setDisabled(isInViewMode());
			transactionNumber.setValue(transaction.getNumber());
			// if (isTrackTax()) {
			// netAmount.setAmount(transaction.getNetAmount());
			// vatTotalNonEditableText.setAmount(transaction.getTotal()
			// - transaction.getNetAmount());
			// }

			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					netAmount.setAmount(transaction.getNetAmount());
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					selectTAXCode();
				}
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
				}
			}

			if (transaction.getTransactionItems() != null) {
				if (isTrackDiscounts()) {
					if (!isDiscountPerDetailLine()) {
						this.discountField.setAmount(getdiscount(transaction
								.getTransactionItems()));
					}
				}
			}
			if (isTrackClass()) {
				if (!isClassPerDetailLine()) {
					this.accounterClass = getClassForTransactionItem(transaction
							.getTransactionItems());
					if (accounterClass != null) {
						this.classListCombo.setComboItem(accounterClass);
						classSelected(accounterClass);
					}
				}
			}
			transactionTotalNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			balanceDueNonEditableText.setAmount(transaction.getBalanceDue());
			// balanceDueNonEditableText.setCurrency(getTransactionCurrency());

			this.dueDateItem
					.setValue(transaction.getDueDate() != 0 ? new ClientFinanceDate(
							transaction.getDueDate()) : getTransactionDate());
			initMemoAndReference();
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));

		super.initTransactionViewData();
		initTransactionNumber();
		initPaymentTerms();

		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initBalanceDue() {

		if (isInViewMode()) {

			setBalanceDue(transaction.getBalanceDue());

		}

	}

	public void setBalanceDue(Double balanceDue) {
		if (balanceDue == null)
			balanceDue = 0.0D;
		this.balanceDue = balanceDue;
		balanceDueNonEditableText.setAmount(balanceDue);
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	private void initPaymentTerms() {
		paymentTermsList = getCompany().getPaymentsTerms();

		paymentTermsCombo.initCombo(paymentTermsList);
		paymentTermsCombo.setDisabled(isInViewMode());

		if (isInViewMode() && transaction.getPaymentTerm() != 0) {
			ClientPaymentTerms paymentTerm = getCompany().getPaymentTerms(
					transaction.getPaymentTerm());
			paymentTermsCombo.setComboItem(paymentTerm);
			selectedPaymentTerm = paymentTerm;

		} else {
			for (ClientPaymentTerms paymentTerm : paymentTermsList) {
				if (paymentTerm.getName().equals("Due on Receipt")) {
					paymentTermsCombo.addItemThenfireEvent(paymentTerm);
					break;
				}
			}
			this.selectedPaymentTerm = paymentTermsCombo.getSelectedValue();
		}

	}

	public void selectedVendor(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		updatePurchaseOrderOrItemReceipt(vendor);
		if (!transaction.isTemplate()) {
			getPurchaseOrdersAndItemReceipt();
		}
		super.vendorSelected(vendor);
		if (isInViewMode() && this.selectedPaymentTerm != null)
			paymentTermSelected(selectedPaymentTerm);
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		transactionsTree.clear();
		updatePurchaseOrderOrItemReceipt(vendor);

		super.vendorSelected(vendor);
		if (transaction == null)
			vendorAccountTransactionTable.resetRecords();

		if (!(isInViewMode() && vendor.getID() == transaction.getVendor()))
			setPaymentTermsCombo(vendor);

		long currency = vendor.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
					transactionDateItem.getDate().getDate());
		} else {
			ClientCurrency clientCurrency = getCompany().getPrimaryCurrency();
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}
		if (vendor.getPhoneNo() != null) {
			phoneSelect.setValue(vendor.getPhoneNo());
		} else {
			phoneSelect.setValue("");
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(vendor.getCurrency()));
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
		getPurchaseOrdersAndItemReceipt();
	}

	private void updatePurchaseOrderOrItemReceipt(ClientVendor vendor) {
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientEnterBill ent = this.transaction;
			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorAccountTransactionTable
						.setRecords(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setRecords(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorAccountTransactionTable.resetRecords();
				this.vendorAccountTransactionTable.updateTotals();
				this.vendorItemTransactionTable.updateTotals();
			}
		}
	}

	private void setPaymentTermsCombo(ClientVendor vendor) {
		ClientPaymentTerms vendorPaymentTerm = getCompany().getPaymentTerms(
				vendor.getPaymentTermsId());
		// if (transactionObject != null && this.selectedPaymentTerm != null)
		// paymentTermSelected(selectedPaymentTerm);

		// else if (transactionObject == null) {
		if (vendorPaymentTerm != null) {
			paymentTermsCombo.setComboItem(vendorPaymentTerm);
			paymentTermSelected(vendorPaymentTerm);

		} else {
			paymentTermsList = getCompany().getPaymentsTerms();
			for (ClientPaymentTerms paymentTerm : paymentTermsList) {
				if (paymentTerm.getName().equals("Due on Receipt")) {
					paymentTermsCombo.addItemThenfireEvent(paymentTerm);
					break;
				}
			}
			this.selectedPaymentTerm = paymentTermsCombo.getSelectedValue();
			paymentTermSelected(this.selectedPaymentTerm);
		}
		// }

	}

	private void setDueDate(long date) {
		dueDateItem.setEnteredDate(new ClientFinanceDate(date));
	}

	private ClientFinanceDate getDueDate() {
		return dueDateItem.getEnteredDate();
	}

	@Override
	protected void createControls() {
		Label lab1;
		lab1 = new Label(messages.enterBill());

		lab1.setStyleName("label-title");
		transactionDateItem = createTransactionDateItem();
		transactionDateItem.setTitle(messages.billDate());
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							deliveryDateItem.setEnteredDate(date);
							dueDateItem.setEnteredDate(date);
							setTransactionDate(date);
						}
					}
				});
		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(messages.invNo());
		listforms = new ArrayList<DynamicForm>();

		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(true);

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		if (isTemplate) {
			dateNoForm.setFields(transactionNumber);
		} else {
			dateNoForm.setFields(transactionDateItem, transactionNumber);
		}

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		vatinclusiveCheck = getVATInclusiveCheckBox();

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));
		// vendorCombo.setWidth(100);
		// purchaseLabel = new LinkItem();
		// purchaseLabel.setLinkTitle(FinanceApplication.constants()
		// .purchaseAndItemReceipt());
		// purchaseLabel.setShowTitle(false);
		// purchaseLabel.setDisabled(isEdit);
		// purchaseLabel.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// getPurchaseOrdersAndItemReceipt();
		// }
		// });
		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		contactCombo = createContactComboItem();
		// contactCombo.setWidth(100);
		billToCombo = createBillToComboItem();
		billToCombo.setWidth(100);
		if (this.isInViewMode())
			billToCombo.setDisabled(true);

		vendorForm = UIUtils.form(Global.get().Vendor());
		// vendorForm.setWidth("100%");
		vendorForm.setNumCols(3);
		vendorForm.setFields(vendorCombo, emptylabel, contactCombo, emptylabel);

		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			vendorForm.setFields(classListCombo);
		}

		// formItems.add(vendorCombo);
		// formItems.add(contactCombo);
		// formItems.add(billToCombo);

		phoneSelect = new TextItem(messages.phone());
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setHelpInformation(true);
		// phoneSelect.setWidth(80);
		phoneSelect.setDisabled(false);
		// formItems.add(phoneSelect);

		dueDateItem = new DateField(messages.dueDate());
		dueDateItem.setToolTip(messages.selectDateUntilDue(this.getAction()
				.getViewName()));
		dueDateItem.setHelpInformation(true);
		dueDateItem.setEnteredDate(getTransactionDate());
		dueDateItem.setColSpan(1);
		dueDateItem.setTitle(messages.dueDate());
		dueDateItem.setDisabled(isInViewMode());

		paymentTermsCombo = new PaymentTermsCombo(messages.paymentTerms());
		paymentTermsCombo.setHelpInformation(true);
		// paymentTermsCombo.setWidth(80);
		paymentTermsCombo.setDisabled(isInViewMode());
		paymentTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {

						paymentTermSelected(selectItem);

					}

				});

		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);
		taxCodeSelect = createTaxCodeSelectItem();

		DynamicForm termsForm = UIUtils.form(messages.terms());
		termsForm.setStyleName("vender-form");
		// termsForm.setWidth("75%");
		// termsForm.setFields(phoneSelect, paymentTermsCombo);

		DynamicForm dateform = new DynamicForm();
		// dateform.setWidth("100%");
		dateform.setNumCols(2);
		if (locationTrackingEnabled)
			dateform.setFields(locationCombo);
		if (isTemplate) {
			dateform.setItems(phoneSelect, paymentTermsCombo);
		} else {
			dateform.setItems(phoneSelect, paymentTermsCombo, dueDateItem,
					deliveryDateItem);
		}
		// dateform.getCellFormatter().setWidth(0, 0, "200px");
		netAmount = new AmountLabel(messages.currencyNetAmount(getCompany()
				.getPrimaryCurrency().getFormalName()));
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem(getCompany()
				.getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		// balanceDueNonEditableText = new AmountField(messages
		// .balanceDue(), this, getBaseCurrency());
		balanceDueNonEditableText = new AmountLabel(messages.balanceDue());
		balanceDueNonEditableText.setHelpInformation(true);
		balanceDueNonEditableText.setDisabled(true);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		transactionsTree = new TransactionsTree<PurchaseOrdersAndItemReceiptsList>(
				this) {
			@Override
			public void updateTransactionTotal() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			public void setTransactionDate(ClientFinanceDate transactionDate) {
				VendorBillView.this.setTransactionDate(transactionDate);
			}

			@Override
			public boolean isinViewMode() {
				return !(VendorBillView.this.isInViewMode());
			}
		};

		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this, isCustomerAllowedToAdd(),
				isTrackClass(), isClassPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorBillView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return VendorBillView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isTrackJob() {
				return VendorBillView.this.isTrackJob();
			}
		};
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorAccountTransactionTable.getElement().getStyle()
				.setMarginTop(10, Unit.PX);

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});
		FlowPanel accountFlowPanel = new FlowPanel();
		accountsDisclosurePanel = new DisclosurePanel(
				messages.ItemizebyAccount());
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");

		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this, isCustomerAllowedToAdd(),
				isTrackClass(), isClassPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorBillView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return VendorBillView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getAmount() != null
						&& discountField.getAmount() != 0) {
					row.setDiscount(discountField.getAmount());
				}
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			protected int getTransactionType() {
				return ClientTransaction.TYPE_ENTER_BILL;
			}

			@Override
			protected boolean isTrackJob() {
				return VendorBillView.this.isTrackJob();
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
		itemsDisclosurePanel = new DisclosurePanel(
				messages.ItemizebyProductService());
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");
		// Inventory table..
		// inventoryTransactionTable = new WarehouseAllocationTable();
		// inventoryTransactionTable.setDesable(isInViewMode());
		//
		// FlowPanel inventoryFlowPanel = new FlowPanel();
		// inventoryDisclosurePanel = new
		// DisclosurePanel("Warehouse Allocation");
		// inventoryFlowPanel.add(inventoryTransactionTable);
		// inventoryDisclosurePanel.setContent(inventoryFlowPanel);
		// inventoryDisclosurePanel.setWidth("100%");
		// ---Inverntory table-----
		memoTextAreaItem = createMemoTextAreaItem();
		// memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);

		// addLinksButton = new Button(FinanceApplication.constants()
		// /addLinks());
		// //FIXME--need to disable basing on the mode of the view being opened
		// addLinksButton.setEnabled(isEdit);
		// addLinksButton.setEnabled(true);
		// linksText = new TextItem();
		// linksText.setWidth(100);
		// linksText.setShowTitle(false);
		// linksText.setDisabled(isEdit);
		// formItems.add(linksText);
		currencyWidget = createCurrencyFactorWidget();
		DynamicForm tdsForm = new DynamicForm();
		tdsForm.setWidth("100%");
		tdsForm.setFields();

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		// memoForm.setWidget(3, 0, addLinksButton);
		// memoForm.setWidget(3, 1, linksText.getMainWidget());

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);

		totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");
		// netAmount.setWidth((netAmount.getMainWidget().getOffsetWidth() +
		// "102")
		// + "px");
		HorizontalPanel taxPanel = new HorizontalPanel();
		taxPanel.setWidth("100%");
		discountField = getDiscountField();

		DynamicForm form = new DynamicForm();

		if (isTrackTax() && isTrackPaidTax()) {
			if (!isTaxPerDetailLine()) {
				form.setFields(taxCodeSelect);
			}
			form.setFields(vatinclusiveCheck);
			DynamicForm netAmountForm = new DynamicForm();
			netAmountForm.setNumCols(2);
			netAmountForm.setFields(netAmount);
			totalForm.add(netAmountForm);
			totalForm.add(vatTotalNonEditableText);
			totalForm.setCellHorizontalAlignment(netAmountForm, ALIGN_RIGHT);
		}
		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				form.setFields(discountField);
			}
		}
		taxPanel.add(form);
		taxPanel.setCellHorizontalAlignment(form,
				HasHorizontalAlignment.ALIGN_LEFT);
		DynamicForm transactionTotalForm = new DynamicForm();
		transactionTotalForm.setNumCols(2);

		transactionTotalForm.setFields(transactionTotalNonEditableText);
		if (isMultiCurrencyEnabled()) {
			transactionTotalForm.setFields(foreignCurrencyamountLabel);
		}

		totalForm.add(transactionTotalForm);
		taxPanel.add(totalForm);

		totalForm.setCellHorizontalAlignment(vatTotalNonEditableText,
				ALIGN_RIGHT);
		totalForm.setCellHorizontalAlignment(transactionTotalForm, ALIGN_RIGHT);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(tdsForm);

		if (this.isInViewMode()) {
			DynamicForm balanceDueForm = new DynamicForm();
			balanceDueForm.setFields(balanceDueNonEditableText);
			totalForm.add(balanceDueForm);
			totalForm.setCellHorizontalAlignment(balanceDueForm, ALIGN_RIGHT);
		}
		VerticalPanel leftVLay = new VerticalPanel();
		// leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setHorizontalAlignment(ALIGN_LEFT);
		// rightVLay.setWidth("100%");
		rightVLay.add(termsForm);
		rightVLay.add(dateform);
		rightVLay.setCellHorizontalAlignment(dateform,
				HasHorizontalAlignment.ALIGN_RIGHT);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			rightVLay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		topHLay.getElement().getStyle().setPaddingTop(20, Unit.PX);
		topHLay.getElement().getStyle().setPaddingBottom(20, Unit.PX);

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");
		//

		// if (isTrackTax()) {

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setWidth("100%");
		verticalPanel.setHorizontalAlignment(ALIGN_RIGHT);
		VerticalPanel vpanel = new VerticalPanel();
		vpanel.setHorizontalAlignment(ALIGN_RIGHT);
		// vpanel.setWidth("100%");

		// vpanel.add(hpanel);
		bottomLayout.add(memoForm);
		bottomLayout.add(taxPanel);
		bottomLayout.setCellWidth(totalForm, "30%");

		bottompanel.add(bottomLayout);

		// VerticalPanel vPanel = new VerticalPanel();
		// vPanel.add(menuButton);
		// vPanel.add(memoForm);
		// vPanel.setWidth("100%");
		//
		// bottomLayout.add(vPanel);
		// bottomLayout.add(vatCheckform);
		// // bottomLayout.setHorizontalAlignment(align)
		// bottomLayout.setCellHorizontalAlignment(vatCheckform,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// bottomLayout.add(totalForm);
		// bottomLayout.setCellHorizontalAlignment(totalForm,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// } else if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_INDIA) {
		// bottomLayout.add(horizontalPanel);
		// bottomLayout.add(totalForm);
		// bottomLayout.setCellWidth(totalForm, "30%");
		//
		// memoForm.setStyleName("align-form");
		// VerticalPanel vPanel = new VerticalPanel();
		// vPanel.add(hpanel);
		// vPanel.setWidth("100%");
		//
		// vPanel.setCellHorizontalAlignment(hpanel, ALIGN_RIGHT);
		// vPanel.add(horizontalPanel);
		// vPanel.add(memoForm);
		//
		// bottompanel.add(vPanel);
		// bottompanel.add(bottomLayout);

		// } else {
		// memoForm.setStyleName("align-form");
		// VerticalPanel vPanel = new VerticalPanel();
		// vPanel.setWidth("100%");
		//
		// vPanel.add(memoForm);
		//
		// bottompanel.add(vPanel);
		// }

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(labeldateNoLayout);
		// mainVLay.setCellHorizontalAlignment(topHLay, ALIGN_RIGHT);
		mainVLay.add(topHLay);
		mainVLay.add(transactionsTree);
		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		mainVLay.add(bottompanel);

		// if (UIUtils.isMSIEBrowser()) {
		// resetFormView();
		// vendorForm.setWidth("68%");
		// termsForm.setWidth("100%");
		// dateform.setWidth("100%");
		// }

		this.add(mainVLay);
		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);

		listforms.add(dateform);

		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(transactionTotalForm);
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}
		settabIndexes();
	}

	private boolean isCustomerAllowedToAdd() {
		if (transaction != null) {
			List<ClientTransactionItem> transactionItems = transaction
					.getTransactionItems();
			for (ClientTransactionItem clientTransactionItem : transactionItems) {
				if (clientTransactionItem.isBillable()
						|| clientTransactionItem.getCustomer() != 0) {
					return true;
				}
			}
		}
		if (getPreferences().isBillableExpsesEnbldForProductandServices()
				&& getPreferences()
						.isProductandSerivesTrackingByCustomerEnabled()) {
			return true;
		}
		return false;
	}

	private void paymentTermSelected(ClientPaymentTerms selectItem) {
		if (selectItem == null) {
			return;
		}
		selectedPaymentTerm = selectItem;

		// paymentTermsCombo.setComboItem(selectedPaymentTerm);
		// if (isInViewMode()) {
		// // setDueDate(((ClientEnterBill) transactionObject).getDueDate());
		// setDueDate(Utility.getCalculatedDueDate(getTransactionDate(),
		// selectedPaymentTerm).getDate());
		// } else {
		// setDueDate(Utility.getCalculatedDueDate(getTransactionDate(),
		// selectedPaymentTerm).getDate());
		// }
		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();
		setDueDate(Utility.getCalculatedDueDate(transDate, selectedPaymentTerm)
				.getDate());
	}

	@Override
	public ClientEnterBill saveView() {
		ClientEnterBill saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		super.saveAndUpdateView();
		saveOrUpdate(transaction);
	}

	@Override
	protected void updateTransaction() {
		if (transaction == null)
			return;
		super.updateTransaction();
		// Setting Vendor
		if (getVendor() != null)
			transaction.setVendor(getVendor());

		// Setting Contact
		if (contact != null)
			transaction.setContact(contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// Setting Phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());
		// else
		// transaction.setPhone(phoneNo);

		// Setting Payment Terms
		if (selectedPaymentTerm != null)
			transaction.setPaymentTerm(selectedPaymentTerm);

		// Setting Due date
		if (dueDateItem.getEnteredDate() != null)
			transaction.setDueDate((dueDateItem.getEnteredDate()).getDate());

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal()
				+ transactionsTree.getGrandTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// transaction.setReference(getRefText());

		ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
				transactionDateItem.getEnteredDate(), selectedPaymentTerm);
		transaction.setDiscountDate(discountDate.getDate());

		List<ClientTransaction> selectedRecords = transactionsTree
				.getSelectedRecords();
		List<ClientPurchaseOrder> orders = new ArrayList<ClientPurchaseOrder>();
		for (ClientTransaction clientTransaction : selectedRecords) {
			if (clientTransaction instanceof ClientPurchaseOrder) {
				orders.add((ClientPurchaseOrder) clientTransaction);
			}
		}
		transaction.setPurchaseOrders(orders);

		transaction.setNetAmount(netAmount.getAmount());
		setAmountIncludeTAX();

		if (isTrackDiscounts()) {
			if (discountField.getAmount() != 0.0 && transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getAmount());
				}
			}
		}

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		// if (getCompany().getPreferences().isInventoryEnabled()
		// && getCompany().getPreferences().iswareHouseEnabled())
		// transaction.setWareHouseAllocations(inventoryTransactionTable
		// .getAllRows());
		// enterBill.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());
	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : vendorAccountTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
			for (ClientTransactionItem item : vendorItemTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}

		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal()
				+ transactionsTree.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal()
				+ transactionsTree.getGrandTotal();

		transactionTotalNonEditableText
				.setAmount(getAmountInBaseCurrency(grandTotal));

		foreignCurrencyamountLabel.setAmount(grandTotal);

		netAmount.setAmount(lineTotal);
		if (isTrackTax()) {
			List<ClientTransaction> selectedRecords = transactionsTree
					.getSelectedRecords();
			if (!isInViewMode()) {
				List<ClientPurchaseOrder> orders = new ArrayList<ClientPurchaseOrder>();
				for (ClientTransaction clientTransaction : selectedRecords) {
					if (clientTransaction instanceof ClientPurchaseOrder) {
						orders.add((ClientPurchaseOrder) clientTransaction);
					}
				}
				transaction.setPurchaseOrders(orders);
			}
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
				transaction.setTransactionItems(vendorAccountTransactionTable
						.getAllRows());
				transaction.getTransactionItems().addAll(
						vendorItemTransactionTable.getAllRows());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);
		}

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(isInViewMode());
		setMemoTextAreaItem(transaction.getMemo());
		// setRefText(((ClientEnterBill) transactionObject).getReference());

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. is Valid transaction date?
		// 2. is in prevent posting before date?
		// 3. vendorForm valid?
		// 4. is valid due date?
		// 5. isBlank transaction?
		// 6. is vendor transaction grid valid?
		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}
		result.add(vendorForm.validate());

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				dueDateItem.getEnteredDate(), this.transactionDate)) {
			result.addError(dueDateItem,
					messages.the() + " " + messages.dueDate() + " " + " "
							+ messages.cannotbeearlierthantransactiondate());
		}

		boolean isSelected = transactionsTree.validateTree();
		if (!isSelected) {
			if (transaction.getTotal() <= 0
					&& vendorAccountTransactionTable.isEmpty()
					&& vendorItemTransactionTable.isEmpty()) {
				result.addError(this,
						messages.transactiontotalcannotbe0orlessthan0());
			}
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		} else {
			boolean hasTransactionItems = false;
			for (ClientTransactionItem clientTransactionItem : getAllTransactionItems()) {
				if (clientTransactionItem.getAccount() != 0
						|| clientTransactionItem.getItem() != 0) {
					hasTransactionItems = true;
					continue;
				}
			}
			if (hasTransactionItems) {
				result.add(vendorAccountTransactionTable.validateGrid());
				result.add(vendorItemTransactionTable.validateGrid());
			} else {
				transaction
						.setTransactionItems(new ArrayList<ClientTransactionItem>());
			}
		}
		if (!isSelected && isTrackTax() && isTrackPaidTax()
				&& !isTaxPerDetailLine()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						messages.pleaseSelect(messages.taxCode()));
			}
		} else if (isSelected && isTrackTax() && isTrackPaidTax()
				&& !isTaxPerDetailLine()
				&& !transaction.getTransactionItems().isEmpty()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						messages.pleaseSelect(messages.taxCode()));
			}
		}

		String creditLimitWarning = isExceedCreditLimit(vendor,
				transaction.getTotal());
		if (creditLimitWarning != null) {
			result.addWarning(vendorCombo, creditLimitWarning);
		}

		// if (getAllTransactionItems().isEmpty()) {
		// result.addError(vendorAccountTransactionTable,
		// messages.blankTransaction());
		// } else {
		// result.add(vendorAccountTransactionTable.validateGrid());
		// result.add(vendorItemTransactionTable.validateGrid());
		// }
		return result;
	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	// super.setViewConfiguration(viewConfiguration);
	//
	// if (isEdit && (!transactionObject.isEnterBill()))
	// throw new Exception("Unable to load the Required EnterBill....");
	//
	// if (viewConfiguration.isInitWithPayee()) {
	// ClientPayee payee = viewConfiguration.getPayeeObject();
	//
	// if (payee == null || (!payee.isVendor()))
	// throw new Exception("Required Vendor Could Not be Loaded...");
	// }
	//
	// }

	public static VendorBillView getInstance() {
		return new VendorBillView();

	}

	protected void getPurchaseOrdersAndItemReceipt() {
		if (this.rpcUtilService == null
				|| !getPreferences().isPurchaseOrderEnabled())
			return;
		if (getVendor() == null) {
			Accounter.showError(messages.pleaseSelectThePayee(Global.get()
					.Vendor()));
		} else {
			AccounterAsyncCallback<ArrayList<PurchaseOrdersAndItemReceiptsList>> callback = new AccounterAsyncCallback<ArrayList<PurchaseOrdersAndItemReceiptsList>>() {

				@Override
				public void onException(AccounterException caught) {
					// Accounter.showError(FinanceApplication.constants()
					// .noPurchaseOrderAndItemReceiptForVendor()
					// + vendor.getName());
					return;

				}

				@Override
				public void onResultSuccess(
						ArrayList<PurchaseOrdersAndItemReceiptsList> result) {
					if (result == null) {
						onFailure(new Exception());
					} else {
						List<ClientPurchaseOrder> salesAndEstimates = transaction
								.getPurchaseOrders();
						if (transaction.getID() != 0 && !result.isEmpty()) {
							ArrayList<PurchaseOrdersAndItemReceiptsList> estimatesList = new ArrayList<PurchaseOrdersAndItemReceiptsList>();
							ArrayList<ClientTransaction> notAvailableEstimates = new ArrayList<ClientTransaction>();

							for (ClientTransaction clientTransaction : salesAndEstimates) {
								boolean isThere = false;
								for (PurchaseOrdersAndItemReceiptsList estimatesalesorderlist : result) {
									if (estimatesalesorderlist
											.getTransactionId() == clientTransaction
											.getID()) {
										estimatesList
												.add(estimatesalesorderlist);
										isThere = true;
									}
								}
								if (!isThere) {
									notAvailableEstimates
											.add(clientTransaction);
								}
							}

							if (transaction.isDraft()) {
								for (ClientTransaction clientTransaction : notAvailableEstimates) {
									salesAndEstimates.remove(clientTransaction);
								}
							}

							for (PurchaseOrdersAndItemReceiptsList estimatesAndSalesOrdersList : estimatesList) {
								result.remove(estimatesAndSalesOrdersList);
							}
						}
						transactionsTree.setAllrows(result,
								transaction.getID() == 0 ? true
										: salesAndEstimates.isEmpty());
						if (transaction.getPurchaseOrders() != null) {
							transactionsTree
									.setRecords(new ArrayList<ClientTransaction>(
											transaction.getPurchaseOrders()));
						}
						transactionsTree.setEnabled(!isInViewMode());
						refreshTransactionGrid();
					}
				}
			};

			this.rpcUtilService.getPurchasesAndItemReceiptsList(getVendor()
					.getID(), callback);
		}

		// if (vendor == null)
		// Accounter.showError("Please Select the Vendor");
		// else
		// new VendorBillListDialog(this).show();

	}

	@Override
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

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {

		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {

		balanceDueNonEditableText.setVisible(false);
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				String errorString = null;
				if (errorCode == AccounterException.ERROR_CANT_EDIT) {
					errorString = messages.billPaidSoYouCantEdit();
				} else if (errorCode == AccounterException.USED_IN_INVOICE) {
					errorString = messages.usedinInvoiceSoYoucantEdit();
				} else {
					errorString = AccounterExceptions.getErrorString(errorCode);
				}
				Accounter.showError(errorString);
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
		phoneSelect.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		// purchaseLabel.setDisabled(isEdit);
		paymentTermsCombo.setDisabled(isInViewMode());
		dueDateItem.setDisabled(isInViewMode());
		deliveryDateItem.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		balanceDueNonEditableText.setDisabled(true);
		memoTextAreaItem.setDisabled(isInViewMode());
		discountField.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}
		transactionsTree.setEnabled(!isInViewMode());
		classListCombo.setDisabled(isInViewMode());
		super.onEdit();
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return null;
		// return this.total.getAmount();
	}

	private void resetFormView() {
		// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
		// phoneSelect.setWidth("210px");
		// paymentTermsCombo.setWidth("210px");
	}

	@Override
	protected String getViewTitle() {
		return messages.enterBills();
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
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
		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
		transactionsTree.updateTransactionTreeItemTotals();
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		transactionDateItem.setTabIndex(3);
		transactionNumber.setTabIndex(4);
		phoneSelect.setTabIndex(5);
		paymentTermsCombo.setTabIndex(6);
		dueDateItem.setTabIndex(7);
		deliveryDateItem.setTabIndex(8);
		memoTextAreaItem.setTabIndex(9);
		// menuButton.setTabIndex(10);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(11);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(12);
		cancelButton.setTabIndex(13);
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
		if (taxCodeSelect.getSelectedValue() != null) {
			vendorAccountTransactionTable.setTaxCode(taxCodeSelect
					.getSelectedValue().getID(), true);
		}
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
		if (taxCodeSelect != null && taxCodeSelect.getSelectedValue() != null) {
			vendorItemTransactionTable.setTaxCode(taxCodeSelect
					.getSelectedValue().getID(), true);
		}
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		vendorAccountTransactionTable.updateAmountsFromGUI();
		vendorItemTransactionTable.updateAmountsFromGUI();
	}

	public void modifyForeignCurrencyTotalWidget() {
		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(formalName));
		}
		netAmount.setTitle(messages.currencyNetAmount(formalName));
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getAmount() != null) {
			vendorItemTransactionTable.setDiscount(discountField.getAmount());
			vendorAccountTransactionTable
					.setDiscount(discountField.getAmount());
		} else {
			discountField.setAmount(0d);
		}
	}

	@Override
	protected void classSelected(ClientAccounterClass accounterClass) {
		this.accounterClass = accounterClass;
		if (accounterClass != null) {
			classListCombo.setComboItem(accounterClass);
			vendorAccountTransactionTable
					.setClass(accounterClass.getID(), true);
			vendorItemTransactionTable.setClass(accounterClass.getID(), true);
		} else {
			classListCombo.setValue("");
		}
	}
}