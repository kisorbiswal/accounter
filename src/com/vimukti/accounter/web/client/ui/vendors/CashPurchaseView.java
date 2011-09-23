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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author vimukti1
 * 
 */
public class CashPurchaseView extends
		AbstractVendorTransactionView<ClientCashPurchase> {

	protected DynamicForm vendorForm;
	protected DynamicForm termsForm;
	public List<String> selectedComboList;
	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;
	private TextAreaItem billToAreaItem;
	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();
	private boolean locationTrackingEnabled;
	protected VendorAccountTransactionTable vendorAccountTransactionTable;
	protected VendorItemTransactionTable vendorItemTransactionTable;
	protected AddNewButton accountTableButton, itemTableButton;

	public CashPurchaseView() {
		super(ClientTransaction.TYPE_CASH_PURCHASE);
	}

	protected CashPurchaseView(int type) {
		super(type);
	}

	@Override
	protected void createControls() {
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
		// setTitle(UIUtils.title(vendorConstants.cashPurchase()));

		titlelabel = new Label(Accounter.constants().cashPurchase());
		titlelabel.setStyleName(Accounter.constants().labelTitle());
		// titlelabel.setHeight("50px");
		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							deliveryDateItem.setEnteredDate(date);
							setTransactionDate(date);
						}
					}
				});
		transactionDateItem.setWidth(100);

		transactionNumber = createTransactionNumberItem();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		if (locationTrackingEnabled)
			dateNoForm.setFields(locationCombo);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.setHorizontalAlignment(ALIGN_RIGHT);
		labeldateNoLayout.setCellHorizontalAlignment(datepanel, ALIGN_RIGHT);
		labeldateNoLayout.add(datepanel);

		if (this.isInViewMode())
			// --the form need to be disabled here
			dateNoForm.setDisabled(true);

		// formItems.add(transactionDateItem);
		// formItems.add(transactionNumber);

		vendorCombo = createVendorComboItem(messages.vendorName(Global.get()
				.Vendor()));
		vendorCombo.setHelpInformation(true);
		// vendorCombo.setWidth(100);
		contactCombo = createContactComboItem();
		contactCombo.setHelpInformation(true);
		// contactCombo.setWidth(100);
		billToAreaItem = new TextAreaItem(Accounter.constants().billTo());
		billToAreaItem.setHelpInformation(true);
		billToAreaItem.setWidth(100);
		billToAreaItem.setDisabled(true);
		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				this.getAction().getCatagory()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		if (isInViewMode())
			phoneSelect.setDisabled(true);

		vendorForm = UIUtils.form(Global.get().Vendor());

		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorCombo, contactCombo, phoneSelect,
				billToAreaItem);
		vendorForm.getCellFormatter().setWidth(0, 0, "160px");
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// formItems.add(contactCombo);
		// formItems.add(billToCombo);

		payFromCombo = createPayFromCombo(Accounter.constants().payFrom());
		// payFromCombo.setWidth(100);
		payFromCombo.setPopupWidth("500px");
		checkNo = createCheckNumberItem(Accounter.constants().chequeNo());
		checkNo.setDisabled(true);
		checkNo.setWidth(100);
		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);

		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		paymentMethodCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentMethodSelected(paymentMethodCombo
								.getSelectedValue());
						if (paymentMethodCombo.getSelectedValue().equals(
								Accounter.constants().check())
								|| paymentMethodCombo.getSelectedValue()
										.equals(Accounter.constants().cheque())) {
							checkNo.setDisabled(false);
						} else {
							checkNo.setDisabled(true);
						}

						if (paymentMethod.equals(Accounter.constants().check())
								&& isInViewMode() && payFromAccount != null) {
							ClientCashPurchase cashPurchase = (ClientCashPurchase) transaction;
							checkNo.setValue(cashPurchase.getCheckNumber());
						}
					}
				});

		termsForm = new DynamicForm();
		termsForm.setWidth("100%");
		termsForm.setFields(paymentMethodCombo, payFromCombo, checkNo,
				deliveryDateItem);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "203px");

		// formItems.add(checkNo);
		// formItems.add(deliveryDateItem);

		Label lab2 = new Label(Accounter.constants().itemsAndExpenses());
		// menuButton = createAddNewButton();

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);
		transactionTotalNonEditableText = createTransactionTotalNonEditableItem();

		vatTotalNonEditableText = createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				CashPurchaseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashPurchaseView.this.isShowPriceWithVat();
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
				CashPurchaseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashPurchaseView.this.isShowPriceWithVat();
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

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);
		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("invoice-total");
		totalForm.setFields(netAmount, vatTotalNonEditableText,
				transactionTotalNonEditableText);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);

		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "41%");
		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		if (isTrackPaidTax()) {
			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setWidth("100%");
			vpanel.setHorizontalAlignment(ALIGN_RIGHT);
			vpanel.add(totalForm);

			bottomLayout.add(memoForm);
			if (!isTaxPerDetailLine()) {
				taxCodeSelect = createTaxCodeSelectItem();
				DynamicForm form = new DynamicForm();
				form.setFields(taxCodeSelect);
				bottomLayout.add(form);
			}
			bottomLayout.add(totalForm);
			bottomLayout.setCellWidth(totalForm, "30%");

			bottompanel.add(vpanel);
			bottompanel.add(bottomLayout);

			// VerticalPanel vPanel = new VerticalPanel();
			// vPanel.add(menuButton);
			// vPanel.add(memoForm);
			// vPanel.setWidth("100%");
			//
			// bottomLayout.add(vPanel);
			// bottomLayout.add(vatCheckform);
			// bottomLayout.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// bottomLayout.add(totalForm);
			// bottomLayout.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			memoForm.setStyleName("align-form");
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setWidth("100%");
			vPanel.add(memoForm);

			bottompanel.add(vPanel);
		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(titlelabel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		// mainVLay.add(lab2);

		mainVLay.add(accountsDisclosurePanel);
		mainVLay.add(itemsDisclosurePanel);
		// mainVLay.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
		mainVLay.add(bottompanel);

		// setOverflow(Overflow.SCROLL);
		this.add(mainVLay);
		// addChild(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		initViewType();

		settabIndexes();
	}

	@Override
	protected void accountSelected(ClientAccount account) {

		if (account == null) {
			payFromCombo.setValue("");
			return;
		}
		this.payFromAccount = account;
		payFromCombo.setComboItem(payFromAccount);
		if (account != null
				&& paymentMethod.equalsIgnoreCase(Accounter.constants()
						.cheque()) && isInViewMode()) {
			ClientCashPurchase cashPurchase = (ClientCashPurchase) transaction;
			checkNo.setValue(cashPurchase.getCheckNumber());
			// setCheckNumber();
		} else if (account == null) {
			checkNo.setValue("");
		}
	}

	protected void setCheckNumber() {

		rpcUtilService.getNextCheckNumber(payFromAccount.getID(),
				new AccounterAsyncCallback<Long>() {

					public void onException(AccounterException t) {
						// //UIUtils.logError(
						// "Failed to get the next check number!!", t);
						checkNo.setValue(Accounter.constants().toBePrinted());
						return;
					}

					public void onResultSuccess(Long result) {
						if (result == null)
							onFailure(null);

						checkNumber = String.valueOf(result);
						checkNo.setValue(result.toString());
					}

				});

	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientCashPurchase());
		} else {

			super.vendorSelected(getCompany()
					.getVendor(transaction.getVendor()));
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			this.billingAddress = transaction.getVendorAddress();
			if (billingAddress != null) {

				billToAreaItem.setValue(getValidAddress(billingAddress));

			} else
				billToAreaItem.setValue("");
			// paymentMethodSelected(cashPurchaseToBeEdited.getPaymentMethod()
			// !=
			// null ? cashPurchaseToBeEdited
			// .getPaymentMethod()
			// : "");
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			accountSelected(getCompany().getAccount(transaction.getPayFrom()));
			// transactionDateItem.setEnteredDate(cashPurchaseToBeEdited.get)
			initMemoAndReference();
			checkNo.setDisabled(true);
			if (getPreferences().isTrackPaidTax()) {
				netAmount.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
			}
			transactionTotalNonEditableText.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		super.initTransactionViewData();
		initTransactionNumber();
		initPayFromAccounts();

	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientCashPurchase ent = (ClientCashPurchase) this.transaction;

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
		super.vendorSelected(vendor);
		if (vendor == null) {
			return;
		}
		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billToAreaItem.setValue(getValidAddress(billingAddress));
		} else
			billToAreaItem.setValue("");
		long code = vendor.getTAXCode();
		if (code == 0) {
			code = Accounter.getCompany().getDefaultTaxCode();
		}
		vendorAccountTransactionTable.setTaxCode(code, false);
		vendorItemTransactionTable.setTaxCode(code, false);

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		// super.paymentMethodSelected(paymentMethod);
		// setDisableStateForCheckNo(paymentMethod);
		// paymentMethodCombo.setValue(paymentMethod);
	}

	private void setDisableStateForCheckNo(String paymentMethod) {

		if (paymentMethod.equalsIgnoreCase(Accounter.constants().cheque())) {
			checkNo.setDisabled(false);
		} else {
			checkNo.setValue("");
			checkNo.setDisabled(true);

		}
		if (isInViewMode()) {
			if (paymentMethod.equalsIgnoreCase(Accounter.constants().cheque())) {
				checkNo.setDisabled(false);
			} else {
				checkNo.setDisabled(true);
			}
		}
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		if (getPreferences().isTrackPaidTax()) {
			transaction.setNetAmount(netAmount.getAmount());
			// if (vatinclusiveCheck != null)
			// cashPurchase.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
			// .getValue());
		}
		super.saveAndUpdateView();

		createAlterObject();
	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CASH_PURCHASE);

		if (this.getVendor() != null) {
			// Setting Vendor
			transaction.setVendor(this.getVendor().getID());
		}

		// Setting Contact
		if (contact != null)
			transaction.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress((billingAddress));

		// Setting Phone
		// if (phoneNo != null)
		transaction.setPhone(phoneSelect.getValue().toString());

		// Setting Payment Methods
		transaction.setPaymentMethod(paymentMethodCombo.getValue().toString());

		// Setting Pay From Account
		transaction
				.setPayFrom(payFromCombo.getSelectedValue() != null ? payFromCombo
						.getSelectedValue().getID() : 0);

		// Setting Check number
		transaction.setCheckNumber(checkNo.getValue().toString());
		// transaction
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getDate());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// cashPurchase.setReference(getRefText());
	}

	public void createAlterObject() {

		saveOrUpdate((ClientCashPurchase) transaction);

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(true);
		setMemoTextAreaItem(((ClientCashPurchase) transaction).getMemo());
		// setRefText(((ClientCashPurchase) transactionObject).getReference());
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

		netAmount.setAmount(lineTotal);
		if (getCompany().getPreferences().isTrackPaidTax()) {
			vatTotalNonEditableText.setAmount(grandTotal - lineTotal);
		}
		transactionTotalNonEditableText.setAmount(grandTotal);
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = super.validate();
		// Validations
		// 1. isValidTransactionDate?
		// 2. isInPreventPostingBeforeDate?
		// 3. form validations
		// 4. isValidDueOrDeliveryDate?
		// 5. isBlankTransaction?
		// 6. validateGrid?

		if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateTransactionDate());
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}

		result.add(vendorForm.validate());
		result.add(termsForm.validate());

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

	public static CashPurchaseView getInstance() {
		return new CashPurchaseView();
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

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	protected void onLoad() {
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = ((AccounterException) caught).getErrorCode();
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
		vendorCombo.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		if (paymentMethod.equals(Accounter.constants().check())
				|| paymentMethod.equals(Accounter.constants().cheque())) {
			checkNo.setDisabled(isInViewMode());
		} else {
			checkNo.setDisabled(!isInViewMode());
		}
		deliveryDateItem.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		super.onEdit();
	}

	protected void initViewType() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return this.transactionTotalNonEditableText.getAmount();
	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		vendorForm.setWidth("75%");
		// refText.setWidth("200px");

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().cashPurchases();
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
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void refreshTransactionGrid() {
		// vendorTransactionTable.refreshAllRecords();
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billToAreaItem.setTabIndex(4);
		transactionDateItem.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		paymentMethodCombo.setTabIndex(7);
		payFromCombo.setTabIndex(8);
		checkNo.setTabIndex(9);
		deliveryDateItem.setTabIndex(10);
		memoTextAreaItem.setTabIndex(11);
		// menuButton.setTabIndex(12);
		saveAndCloseButton.setTabIndex(13);
		saveAndNewButton.setTabIndex(14);
		cancelButton.setTabIndex(15);

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
